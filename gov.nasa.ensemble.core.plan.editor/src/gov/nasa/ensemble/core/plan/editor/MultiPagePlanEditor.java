/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.authentication.AuthenticationUtil;
import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.common.ui.ResourceUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.detail.view.DetailPage;
import gov.nasa.ensemble.common.ui.detail.view.DetailView;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorModel.IDirtyListener;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.ModelingConfigurationRegistry;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.PlanPersister;
import gov.nasa.ensemble.core.plan.editor.lifecycle.SaveAsWizard;
import gov.nasa.ensemble.core.plan.editor.preferences.PlanEditorPreferences;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanPage;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanPageProvider;
import gov.nasa.ensemble.core.rcp.perspective.PlanningPerspectiveFactory;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.expressions.ICountable;
import org.eclipse.core.expressions.IIterable;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.undo.DeleteResourcesOperation;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * A plan editor has multiple pages
 */
public class MultiPagePlanEditor extends MultiPageEditorPart implements IReusableEditor {

	static {
		IAdapterManager adapterManager = Platform.getAdapterManager();
		adapterManager.registerAdapters(new IAdapterFactory() {
			@Override
			public Class[] getAdapterList() {
				return new Class[] { IIterable.class, ICountable.class };
			}
			@Override
			public Object getAdapter(Object adaptableObject, Class adapterType) {
				if (!(adaptableObject instanceof MultiPagePlanEditor)) {
					return null;
				}
				final MultiPagePlanEditor editor = (MultiPagePlanEditor) adaptableObject;
				if (IIterable.class.equals(adapterType)) {
					return new IIterable() {
						@Override
						public Iterator iterator() {
							return new MultiPagePlanEditorIterator(editor);
						}
					};
				}
				if (ICountable.class.equals(adapterType)) {
					return new ICountable() {
						@Override
						public int count() {
							return editor.getPageCount();
						}
					};
				}
				return null;
			}
		}, MultiPagePlanEditor.class);

	}
	
	private static final String FAILED_TO_CREATE_PLAN_EDITOR_MODEL = "failed to create plan editor model";

	/** The string identifier for this class. */
	public  static final String ID = "gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor";

	/** The property ID signifying that the page property has changed. */
	private static final int PROPERTY_PAGE = 10002;
	
	/**
	 *  Cache a collection of all registered TemplatePlanPageProvider objects for use in fetching
	 *  the plan pages.
	 */
	private static final List<TemplatePlanPageProvider> TEMPLATE_PLAN_PAGE_PROVIDERS = ClassRegistry.createInstances(TemplatePlanPageProvider.class);
	
	/** The ID string of the plan editor extension point. */
	private static final String EDITOR_EXTENSIONS = "gov.nasa.ensemble.core.plan.editor.PlanEditor";
	
	/** Cache of the singleton template plan page, fetched lazily. */
	private Object templatePlanPage = null;
	
	/** An object that contains a reference to this editor part and knows what the container is. */
	protected MultiPagePlanEditorDecorator decorator;
	
	protected PlanNameHelper nameHelper;

	/**
	 * A flag set when a save or save-as attempt failed. If set, the progress monitor is canceled.
	 */
	private boolean saveFailed = false;

	/** Specifies what to do on change of selection. */
	protected final EnsembleSelectionProvider multipagePlanSelectionProvider = new EnsembleSelectionProvider(this.toString());

	/** Listens on change of dirty state and fires a property-changed event. */
	private final IDirtyListener dirtyListener = new DirtyListenerImpl();

	/** Listens on change of a plan's name and updates the editor's label. */
	protected Adapter nameChangedListener = new PlanNameChangeListener();
	
	/** Listens on deletion of the file in which the plan was stored, closing the plan. */
	private IResourceChangeListener resourceDeletedListener;
	
	private final MultiPagePlanEditorConfigurator multiPagePlanEditorConfigurator;
	
	protected Properties properties;
	
	public MultiPagePlanEditor() {
		super();
		MultiPagePlanEditorConfigurator c = new MultiPagePlanEditorConfigurator();
		try {
			c = MissionExtender.construct(MultiPagePlanEditorConfigurator.class);
		} catch (ConstructionException e) {
			LogUtil.error(e);
		}
		multiPagePlanEditorConfigurator = c;
	}

	/**
	 * Returns the plan editor model's plan, if there is a plan editor model; otherwise null
	 */
	public EPlan getPlan() {
		/*
		 * the planEditorModel might be null if the editor is in the process of
		 * opening
		 */
		EPlan ePlan = null;
		PlanEditorModel planEditorModel = getPlanEditorModel();
		if(planEditorModel != null) {
			ePlan = planEditorModel.getEPlan();
		}
		return ePlan;
	}

	/** Return the plan editor model's undo context. */
	public IUndoContext getUndoContext() {
		PlanEditorModel planEditorModel = getPlanEditorModel();
		return (planEditorModel != null) ? planEditorModel.getUndoContext() : null;
	}

	/** Return the plan editor model, which is mapped to the editor input in a registry. */
	public PlanEditorModel getPlanEditorModel() {
		return PlanEditorModelRegistry.getPlanEditorModel(getEditorInput());
	}

	/** The editor is dirty if there is a plan editor model and it it dirty. */
	@Override
	public boolean isDirty() {
		boolean dirty = false;
		PlanEditorModel planEditorModel = getPlanEditorModel();
		if (planEditorModel != null) {
			dirty = planEditorModel.isDirty();
		}
		
		return dirty;
	}

	/**
	 * The decorator might override the superclass's idea of what the page container is.
	 * @param the parent Composite
	 * @return the container Composite
	 */
	@Override
	protected Composite createPageContainer(Composite parent) {
			PlanEditorModel planEditorModel = getPlanEditorModel();
			planEditorModel.activatePlanServices();
		
		Composite container = super.createPageContainer(parent);
		try {
			decorator = MissionExtender.construct(MultiPagePlanEditorDecorator.class);
			decorator.setEditorPart(this);
			return decorator.createPageContainer(container, getPlanEditorModel());
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			if (decorator != null) {
				decorator.dispose();
			}
			LogUtil.error("Error constructing mission extension of " + MultiPagePlanEditorDecorator.class.getSimpleName());
		}
		return container;
	}

	/**
	 * Creates the pages of the multi-page editor and activates services.
	 * @throws ThreadDeath
	 */
	@Override
	protected void createPages() {
		PlanEditorModel planEditorModel = getPlanEditorModel();
		if (planEditorModel != null) {
			ModelingConfigurationRegistry.putConfiguration(planEditorModel.getEPlan(), getEditorProperties());
			planEditorModel.interfaceCreated();
		}
		try {
			registerEditorExtensions(EDITOR_EXTENSIONS);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			LogUtil.warn("createPages", t);
		}
	}

	/**
	 * Return the decorator, which contains a reference to this editor part and knows what the
	 * container is.
	 * @return the decorator
	 */
	public MultiPagePlanEditorDecorator getDecorator() {
		return decorator;
	}

	/**
	 * Register all the editor extensions and add them to the multi-page editor. An editor
	 * extension is an editor page.
	 * @param EditorExtensions the ID string for the plan editor, with which to fetch extensions
	 * @throws CoreException
	 * @throws PartInitException
	 * @throws ThreadDeath
	 */
	private void registerEditorExtensions(String EditorExtensions) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(EditorExtensions);
		IConfigurationElement[] extensions = extensionPoint.getConfigurationElements();
		List<IConfigurationElement> configurationElements = Arrays.asList(extensions);
		Comparator<IConfigurationElement> comparator = multiPagePlanEditorConfigurator.getPagesConfigurationElementComparator(this);
		if (comparator == null) {
			comparator = new Comparator<IConfigurationElement>() {
				
				/**
				 * Sort the pages in order of their preferred tab position attribute.
				 * @param c1 the configuration element for an editor page
				 * @param c2 the configuration element for an editor page
				 * @return
				 */
				@Override
				public int compare(IConfigurationElement c1, IConfigurationElement c2) {
					return getPreferredTabPosition(c1) - getPreferredTabPosition(c2);
				}

				/**
				 * If there is a preferred-tab-position attribute for the configuration element
				 * that can be parsed as an int return that int; otherwise, return 100.
				 * @param c the configuration element for the editor page
				 */
				protected int getPreferredTabPosition(IConfigurationElement c) {
					String pos = c.getAttribute("preferred_tab_position");
					try {
						return Integer.parseInt(pos);
					} catch (NumberFormatException nfe) {
						return 100;
					}
				}
			};
		}
		
		Collections.sort(configurationElements, comparator);
		// The dirty flag is set to true (the additional tabs change the plan editor)
		// by Eclipse during this loop.
		for (IConfigurationElement configurationElement : configurationElements) {
			try {
				Object cb = configurationElement.createExecutableExtension("class");
				String name = configurationElement.getAttribute("tab_title");
				IEditorPart pec = (IEditorPart) cb;
				int index = addPage(pec, getEditorInput());
				ISelectionProvider editorSelectionProvider = pec.getSite().getSelectionProvider();
				if (editorSelectionProvider != null)
					multipagePlanSelectionProvider.attachSelectionProvider(editorSelectionProvider);
				setPageText(index, name);
			} catch (InitDeclinedException i) {
				// skip this part
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				LogUtil.error("failed to create editor page", t);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSaveOnCloseNeeded() {
		// workaround for SPF-6678 "Dirty bit is only set when focus is removed from field in Template View, Details View "
		IWorkbenchPartSite site = getSite();
		if (site != null) {
			IWorkbenchPage page = site.getPage();
			IViewReference[] viewReferences = page.getViewReferences();
			for (IViewReference iViewReference : viewReferences) {
				IViewPart viewPart = iViewReference.getView(false);
				if(viewPart != null) {
					Object adapter = viewPart.getAdapter(IPage.class);
					if(adapter instanceof DetailPage) {
						DetailPage detailPage = (DetailPage) adapter;
						if(detailPage.hasSheet()) {
							this.setFocus();
							break;
						}
					}
				}	
			}			
		}
		// end workaround
		return super.isSaveOnCloseNeeded();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	@Override
	public void dispose() {
		ModelingConfigurationRegistry.dispose(getPlan());
		final IFile file = CommonUtils.getAdapter(getEditorInput(), IFile.class);
		if (decorator != null) {
			decorator.dispose();
		}
		
		if (resourceDeletedListener != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceDeletedListener);
		}
		
		/*
		 call super.dipose first, since some objects there need the input to dipose properly
		 for example, setting a persistent property on an IResource
		 */
		super.dispose();
		if (shouldDeleteOnClose()) {
			cleanupWorkspace(file);
		}
		setInput(null);
	}

	/**
	 * Look up and the delete-on-close property and return it
	 */
	private boolean shouldDeleteOnClose() {
		return PlanPersister.getInstance().shouldCleanupLocalCopy(getPlan());
	}
	
	/**
	 * If the file exists and is in the workspace and the file is in a project in the workspace,
	 * delete the project.
	 * @param file an Eclipse file resource
	 */
	private void cleanupWorkspace(IFile file) {
		if (file != null && file.exists()) {
			final IProject project = file.getProject();
			if (project != null && project.exists()) {
				IRunnableWithProgress op = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException {
						DeleteResourcesOperation op = new DeleteResourcesOperation(new IResource[] {project}, "deleting project", true);
						try {
							// see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=219901
							// directly execute the operation so that the undo state is
							// not preserved.  Making this undoable resulted in too many 
							// accidental file deletions.
							op.execute(monitor, null);
						} catch (ExecutionException e) {
							throw new InvocationTargetException(e);
						}
						if(project.exists()) {
							LogUtil.warn("The project " + project.getName() + " was not deleted.");
						}
					}
				};
				
				// run the new project creation operation
				try {
					CommonUtils.getAdapter(getSite(), IWorkbenchSiteProgressService.class)
									.run(true, true, op);
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}
	}
	
	/**
	 * Saves the multi-page editor's document. If the plan has the needs-save-as property, do
	 * a save-as operation. Else save if we are the custodian.
	 * @param monitor the progress monitor
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		//
		// Pass through to saveAs (in the case of import for instance)
		EPlan plan = getPlan();
		Boolean needsSaveAs = (Boolean) WrapperUtils.getRegistered(plan).getTransientProperty(Plan.ATTRIBUTE_NEEDS_SAVE_AS);
		if (Boolean.TRUE == needsSaveAs) {
			doSaveAs();
			monitor.setCanceled(saveFailed);
			return;
		}
		//
		// If we do not have custodial rights, fail
		boolean continueSave = checkCustodian(plan);
		if (!continueSave) {
			monitor.setCanceled(true);
			return;
		}
		//
		// run the save in a job
		IRunnableWithProgress job = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) {
				savePlan(getPlan(), monitor);
			}
		};
		
		try {
			CommonUtils.getAdapter(getSite(), IWorkbenchSiteProgressService.class).run(true, false, job);
		} catch (InvocationTargetException e) {
			LogUtil.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LogUtil.error(e.getMessage(), e);
		}
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the text for page 0's tab, and updates this multi-page
	 * editor's input to correspond to the nested editor's.
	 * First it runs the save-as wizard to solicit the new file name. If that succeeds, it tries
	 * to save the plan, setting a success flag depending on the outcome. 
	 */
	@Override
	public void doSaveAs() {
		saveFailed = false;
		//
		// Open the dialog
		SaveAsWizard saveAsWizard = SaveAsWizard.createSaveAsWizard(this);
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		WizardDialog saveAsDialog = new WizardDialog(shell, saveAsWizard);
		saveAsDialog.open();
		if (Window.OK != saveAsDialog.getReturnCode()) {
			saveFailed = true;
			return;
		}
	}
    
	/**
	 * Save the plan model and the editor pages. Reset the dirty bit and set the editor part's
	 * name t obe that of the plan.
	 * @param plan the plan model to save
	 * @param monitor ignored
	 * @throws ThreadDeath
	 */
	public void savePlan(EPlan plan, IProgressMonitor monitor) {
		try {
			doSavePlan(plan, monitor);
			//
			// notification
			getPlanEditorModel().resetDirty();
			updatePartName(plan);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			// If not successful, inform user of error
			saveFailed = true;
			announceSaveError(t);
			LogUtil.error("failed in saveCurrentPlan", t);
		}
	}

	protected void doSavePlan(final EPlan plan, final IProgressMonitor monitor) {
		//
		// Save the model
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				PlanPersister.getInstance().savePlan(plan, monitor);
			}
		});
		WidgetUtils.runInDisplayThread(getContainer(), new Runnable() {
			@Override
			public void run() {
				//
				// Save the editor pages
				int pageCount = getPageCount();
				for (int i=0; i<pageCount; i++) {
					IEditorPart editor = getEditor(i);
					editor.doSave(monitor);
				}
			}
		});
	}

	/**
	 * Display an error dialog for the exception that occurred.
	 * 
	 * @param t the Throwable to announce
	 */
	private void announceSaveError(final Throwable t) {
		WidgetUtils.runInDisplayThread(getContainer(), new Runnable() {
			@Override
			public void run() {
				IStatus error = new ExceptionStatus(EditorPlugin.ID, "Failed to save the plan.", t);
				ErrorDialog.openError(getEditorSite().getWorkbenchWindow().getShell(), "Error saving plan", null, error);
			}
		});
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method checks that the input is an instance of
	 * <code>PlanEditorInput</code>.
	 * Decorates by checking validity, setting a selection provider and adding a resource-deleted
	 * listener.
	 * @param site the editor's site
	 * @param editorInput the descriptor of the editor's input
	 * @throws PartInitException
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!PlanEditorModelRegistry.isAcceptable(editorInput)) {
			throw new PartInitException("IEditorInput class not recognized: " + editorInput.getClass());
		}
		IFile file = CommonUtils.getAdapter(editorInput, IFile.class);
		if (file != null && !file.exists()) {
			LogUtil.error("file does not exist: "+file);
			throw new PartInitException(Status.CANCEL_STATUS);
		}
		try {
			super.init(site, editorInput);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			if (FAILED_TO_CREATE_PLAN_EDITOR_MODEL.equals(t.getMessage())) {
				t = t.getCause();
			}
			String message = "Failed to initialize the editor from " + editorInput.getName();
			message += "\nbecause of the error: " + t.getMessage();
			throw new PartInitException(message, t);
		}
		site.setSelectionProvider(multipagePlanSelectionProvider);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(getResourceDeletedListener());
		registerActions(getEditorSite());
	}
	
	private void registerActions(IEditorSite site) {
		final IActionBars bars = site.getActionBars();
		final IToolBarManager manager = bars.getToolBarManager();
		for (PlanEditorContributionItem item : ClassRegistry.createInstances(PlanEditorContributionItem.class, new Class[] {IPartService.class}, new Object[] {getSite().getPage()})) {
			manager.add(item);
		}
		bars.updateActionBars();
	}
	
	/**
	 * Listen to check if this editor should be closed. Temporary fix due to
	 * an eclipse bug 302791
	 * Lazily creates and returns a singleton instance of the ResourceDeletedListener
	 * @return the singleton ResourceDeletedListener
	 */
	private IResourceChangeListener getResourceDeletedListener() {
		if (resourceDeletedListener == null) {
			resourceDeletedListener = new ResourceDeletedListener();
		}
		return resourceDeletedListener;
	}

	/**
	 * Close all editors that are editing the given plan
	 * @param ePlan the plan to close
	 */
	public void closePlan(final EPlan ePlan) {
		IEditorPart editor = MultiPagePlanEditor.this.getActiveEditor();
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] editorReferences = activePage.getEditorReferences();
		for (IEditorReference editorReference : editorReferences) {
			IEditorPart editorPart = editorReference.getEditor(false);
			if (editorPart == null) {
				continue;
			}
			Object potentialEPlan = editorPart.getAdapter(EPlan.class);
			if (potentialEPlan != null) {
				EPlan foreignEPlan = (EPlan) potentialEPlan;
				if (foreignEPlan.equals(ePlan)) {
					IEditorReference[] editorReferenceArray = new IEditorReference[] { editorReference };
					IStructuredSelection structuredSelection = new StructuredSelection(Collections.EMPTY_LIST);
					editor.getSite().getSelectionProvider().setSelection(structuredSelection);
					String uniqueID = ClassIdRegistry.getUniqueID(DetailView.class);
					IViewPart viewPart = activePage.findView(uniqueID);
					editor.getEditorSite().getPage().closeEditors(editorReferenceArray, false);
					if (viewPart != null) {
						// FIXME: shouldn't have to do this...
						viewPart.getSite().getSelectionProvider().setSelection(structuredSelection);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	/**
	 * The save-as operation is allowed unless this is overridden.
	 * @return true
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * If there is an active page, increment its index by one, wrapping back to 1 if advancing
	 * from the last page.
	 */
	public void nextPage() {
		int page = getActivePage();
		if (page == -1) {
			return;
		}

		int pageCount = getPageCount();
		int index = (page + 1) % pageCount;
		setActivePage(index);
	}

	/**
	 * If there is an active page, decrement its index by one, wrapping back to the last page
	 * if backtracking from the first page.
	 */
	public void prevPage() {
		int page = getActivePage();
		if (page == -1) {
			return;
		}

		int pageCount = getPageCount();
		int index = Math.abs(page - 1) % pageCount;
		setActivePage(index);
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		firePropertyChange(PROPERTY_PAGE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (EditingDomain.class.equals(adapter)) {
			PlanEditorModel model = getPlanEditorModel();
			return model == null ? null : model.getEditingDomain();
		} else if (IUndoContext.class.equals(adapter)) {
			return getUndoContext();
		} else if (PlanEditorModel.class.equals(adapter)) {
			return getPlanEditorModel();
		} else if (Plan.class.equals(adapter)) {
			return WrapperUtils.getRegistered(getPlan());
		} else if (EPlan.class.equals(adapter)) {
			return getPlan();
		} else if (ActivityDictionary.class == adapter) {
			return ActivityDictionary.getInstance();
		} else if (ISelectionProvider.class == adapter) {
			return getEditorSite().getSelectionProvider();
		} else if ("org.eclipse.gef.editparts.ZoomManager".equals(adapter.getCanonicalName())) {
			return getCurrentEditor().getAdapter(adapter);
		} else if (EObject.class == adapter) {
			return getPlan();
		} else if (IContentOutlinePage.class == adapter) {
			return new PlanEditorOutlinePage();
		} else if (TemplatePlanPage.class == adapter) {
			return getTemplatePlanPage();
//		} else if (IPropertySheetPage.class == adapter) {
//			return getPropertySheetPage();
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Lazily fetch and return the template plan page from the first template plan page provider
	 * that returns one. Auxiliary to getAdapter().
	 * @return the template plan page if found; otherwise null
	 */
	private Object getTemplatePlanPage() {
		if (templatePlanPage == null) {
			for (TemplatePlanPageProvider p : TEMPLATE_PLAN_PAGE_PROVIDERS) {
				templatePlanPage = p.getTemplatePlanPage(this);
				if (templatePlanPage != null) {
					break;
				}
			}
		}
		return templatePlanPage;
	}

	/**
	 * Required by IReusableEditor - change our editor to a new input.
	 * Unconditionally remove the existing input if there is one, undoing listeners and providers.
	 * If the given input is not null, set the editor to work on it. Finally, notify that the
	 * dirty and input properties have changed.
	 * @param input the new input which is to replace the existing input
	 */
	@Override
	public void setInput(IEditorInput input) {
		unsetOldInput();
		if (input == null) {
			setPartName("<disposed>");
		} else {
			setNewInput(input);
		}
		setPageInputs(input);

		firePropertyChange(ISaveablePart.PROP_DIRTY);
		// following the instructions from the javadoc on IReusableEditor
		firePropertyChange(IWorkbenchPartConstants.PROP_INPUT);
	}

	/**
	 * For each editor page, set the new input.
	 * Auxiliary to setInput()
	 * @param input the new editor input; may be null
	 */
	protected void setPageInputs(IEditorInput input) {
		for (int i = 0; i < getPageCount(); i++) {
			IEditorPart editorPart = getEditor(i);
			if (editorPart instanceof IReusableEditor) {
				((IReusableEditor) editorPart).setInput(input);
			} else {
				// should be an illegal state since we want all of our editors to be reusable
				throw new IllegalStateException("All editors in the multi page plan editor must implement IReusableEditor");
			}
		}
	}

	/**
	 * After unsetting the old input, create the model for the input and hook it in.
	 * Auxiliary to setInput().
	 * @param input the new editor input
	 */
	protected void setNewInput(IEditorInput input) {
		super.setInput(input);
		PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
		if (model == null) {
			try {
				model = PlanEditorModelRegistry.createPlanEditorModel(input);
			} catch (Exception e) {
				throw new RuntimeException(FAILED_TO_CREATE_PLAN_EDITOR_MODEL, e);
			}
		}
		if (PlanEditorPreferences.isCrossEditorSelection()) {
			multipagePlanSelectionProvider.attachSelectionProvider(model.getSelectionProvider());
		}
		EPlan plan = model.getEPlan();
		model.addDirtyListener(dirtyListener);
		PlanEditorModelRegistry.registerEditor(model, this);
		setupPartNaming(plan);
	}

	protected void setupPartNaming(EPlan plan) {
		nameHelper = new PlanNameHelper();
		try {
			nameHelper = MissionExtender.construct(PlanNameHelper.class);
		} catch (ConstructionException e) {
			LogUtil.error("Could not construct PlanNameHelper");
		}
		plan.eAdapters().add(nameChangedListener);
		updatePartName(plan);
	}

	private void updatePartName(EPlan plan) {
		try {
			setPartName(nameHelper.getName(plan));
		} catch (Throwable e) {
			// If we can't rename it, it's not the end of the world.
			// MSLICE-1331 would otherwise prevent even having a plan open, let alone looking at times.
		}
	}

	/**
	 * Before setting the new input, undo everything associated with the current input, if any.
	 * Auxiliary to setInput().
	 */
	protected void unsetOldInput() {
		IEditorInput oldInput = getEditorInput();
		if (oldInput != null) {
			PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(oldInput);
			if (model != null) {
				model.removeDirtyListener(dirtyListener);
				multipagePlanSelectionProvider.detachSelectionProvider(model.getSelectionProvider());
				PlanEditorModelRegistry.unregisterEditor(model, this);
				removePartNaming(model);
			}
		}
	}

	protected void removePartNaming(PlanEditorModel model) {
		EObject eObject = model.getEPlan();
		if (eObject != null) {
			eObject.eAdapters().remove(nameChangedListener);
		}
	}

	/**
	 * Set the part name based on the supplied string. If the string is not null, use only the
	 * portion that follows that last file-separator character
	 * Expose this method to others.
	 * @param name
	 */
	@Override
	public void setPartName(final String name) {
		getSite().getShell().getDisplay().syncExec(new Runnable() {
            @Override
			public void run() {
            	MultiPagePlanEditor.super.setPartName(getShortName(name));
            }
        });
	}

	/**
	 * Expose this method to others.
	 * @param titleImage
	 */
	@Override
	public void setTitleImage(Image titleImage) {
		super.setTitleImage(titleImage);
	}

	/**
	 * Broaden the scope of this method so outside classes can access the individual editors that are contained within the
	 * multipage editor.
	 * This changes access from protected to public.
	 */
	@Override
	public int getPageCount() {
		return super.getPageCount();
	}

	/**
	 * Specify whether the model is dirty at load time.
	 * @param dirtyByDefault whether the model is dirty at load time
	 */
	public void setDirtyByDefault(boolean dirtyByDefault) {
		getPlanEditorModel().setForcedDirtyBit(dirtyByDefault);
	}

	/**
	 * This saves a little work when finding the current edit part.
	 * @return the editor for the active page
	 */
	public IEditorPart getCurrentEditor() {
		int i = getActivePage();
		if (i < 0) {
			return null;
		}
		return getEditor(i);
	}

	/**
	 * Broaden the scope of this method so outside classes can access the individual
	 * editors that are contained within this editor.
	 * This changes access from protected to public.
	 */
	@Override
	public IEditorPart getEditor(int pageIndex) {
		return super.getEditor(pageIndex);
	}

	/**
	 * Returns all the MultiPagePlanEditors that are currently in use in the workspace.
	 * 
	 * @return
	 */
	public static List<MultiPagePlanEditor> getMultiPagePlanEditors() {
		List<MultiPagePlanEditor> planEditors = new ArrayList<MultiPagePlanEditor>();
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			for (IWorkbenchPage page : window.getPages()) {
				for (IEditorReference ref : page.getEditorReferences()) {
					IEditorPart part = ref.getEditor(false);
					if (part == null)
						continue;
					if (part instanceof MultiPagePlanEditor) {
						planEditors.add((MultiPagePlanEditor) part);
					}
				}
			}
		}
		return planEditors;
	}

	/**
	 * Open the editor with the given plan as input
	 * @param plan the plan to edit
	 */
	public static synchronized void openEditor(EPlan plan) {
		openEditor(new PlanEditorInput(plan, null));
	}
	
	/**
	 * Open the editor with the given plan as input
	 * @param plan the plan to edit
	 */
	public static synchronized void openEditor(EPlan plan, boolean switchToPlanningPerspective) {
		openEditor(new PlanEditorInput(plan, null), switchToPlanningPerspective);
	}

	/**
	 * Open and editor of the approriate kind on the plan file.
	 * @param planFile a file resource that contains a plan specification
	 * @param workbench used to fetch the active window
	 * @return
	 */
	public static boolean openEditorOnPlanFile(IFile planFile) {
        // Open editor on new file.
		IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow dw = workbench.getActiveWorkbenchWindow();
        if (dw != null) {
	        try {
	            IWorkbenchPage page = dw.getActivePage();
	            if (page != null) {
	                IEditorPart editor = IDE.openEditor(page, planFile, true, true);
	    			if (editor != null) {
	    				page.bringToTop(editor);
	    				if (editor instanceof MultiPagePlanEditor) {
	    					MultiPagePlanEditor planEditor = (MultiPagePlanEditor) editor;
	    					EPlan plan = planEditor.getPlan();
	    					StructuredSelection initialSelection = new StructuredSelection(plan);
	    					editor.getEditorSite().getSelectionProvider().setSelection(initialSelection);
	    					boolean tranformed = checkPlanCompatibility(plan, dw);
	    					((MultiPagePlanEditor)editor).setDirtyByDefault(tranformed);
	    					isPlanCompatibleWithAD(plan, dw);
	    				}
	    			}
	                return true;
	            }
	        } catch (ThreadDeath td) {
	        	throw td;
    		} catch (Throwable t) {
    			LogUtil.error("Could not open editor", t);
    		}
        }
        return false;
	}
    
	public static synchronized void openEditor(IEditorInput editorInput) {
		openEditor(editorInput, true);
	}
	
	/**
	 * Open a multi-page plan editor on the given editor input.
	 * @param editorInput an editor input for a plan
	 */
	public static synchronized void openEditor(IEditorInput editorInput, boolean switchToPlanningPerspective) {
		try {
			if (switchToPlanningPerspective) {
				PlanningPerspectiveFactory.switchToPlanningPerspective();
			}
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
//			ForbiddenWorkbenchUtils.activateEmptyEditorWorkbook(page);
			
			Object file = editorInput.getAdapter(IFile.class);
			IEditorPart editor = null;
			if (file instanceof IFile) {
				editor = IDE.openEditor(page, (IFile) file, true, true);
			} 
			if (editor == null) {
				editor = page.openEditor(editorInput, MultiPagePlanEditor.class.getName(), true);
			}
			if (editor != null) {
				page.bringToTop(editor);
				if (editor instanceof MultiPagePlanEditor) {
					MultiPagePlanEditor planEditor = (MultiPagePlanEditor) editor;
					EPlan plan = planEditor.getPlan();
					StructuredSelection initialSelection = new StructuredSelection(plan);
					editor.getEditorSite().getSelectionProvider().setSelection(initialSelection);
					boolean tranformed = checkPlanCompatibility(plan, window);
					((MultiPagePlanEditor)editor).setDirtyByDefault(tranformed);
					isPlanCompatibleWithAD(plan, window);
				}
			}
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			LogUtil.error("Could not open editor", t);
		}
	}

	/**
	 * Check to see if this plan was upgraded to the current AD
	 * 
	 * @param plan the plan being checked
	 * @param workbenchWindow the parent for a warning message dialog
	 * @return true if compatibility feedback is present, false otherwise
	 */
	private static boolean checkPlanCompatibility(EPlan plan, IWorkbenchWindow workbenchWindow) {
		String upgradeNotes = (String) WrapperUtils.getRegistered(plan).getTransientProperty(EditorPlugin.ATTRIBUTE_UPGRADE_NOTES);
		if ((upgradeNotes != null) && upgradeNotes.trim().length() > 0) {
			Shell parent = workbenchWindow.getShell();
			String title = "Plan Compatibility";
			String message = "The plan has been upgraded to the latest AD.";
			String version_ad = ActivityDictionary.getInstance().getVersion();
			if (version_ad != null) {
				message += "\nversion: " + version_ad;
			}
			URL url = null;
			if (upgradeNotes.startsWith("http:") || upgradeNotes.endsWith("html")) {
				try {
					url = new URL(upgradeNotes);
				} catch (MalformedURLException e) {
					// fall out with url = null
				}
			}
			if (url != null) {
				MessageDialog dialog = new MessageDialog(parent, title, null, message + "\n\nDo you wish to open the report?\n"
						+ upgradeNotes, MessageDialog.INFORMATION, new String[] { IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL }, 0);
				int result = dialog.open();
				if (result == 0) {
					IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
					try {
						IWebBrowser browser = browserSupport.getExternalBrowser();
						browser.openURL(url);
					} catch (PartInitException e) {
						MessageDialog.openError(parent, "Error", "Failed to open external browser on URL:\n" + url);
					}
				}
			} else {
				MessageDialog dialog = new MessageDialog(parent, title, null, message + "\n\n" + upgradeNotes,
						MessageDialog.INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0);
				dialog.open();
			}
			return true;
		}
		return false;
	}

	/**
	 * Check if the Plan being loaded is compatible with the current Activity Dictionary.
	 * 
	 * @param plan the plan being loaded
	 * @param workbenchWindow the parent of a warning message dialog
	 */
	private static void isPlanCompatibleWithAD(EPlan plan, final IWorkbenchWindow workbenchWindow) {
		final String version_ad = ActivityDictionary.getInstance().getVersion();
		final String version_plan = plan.getMember(ADPlanMember.class).getActivityDictionaryVersion();
		if (version_ad != null && version_ad.trim().length() > 0) {
			if (version_plan == null || version_plan.trim().length() == 0) {
				LogUtil.error("Plan " + plan.getName() + " does not contain an AD version number");
			} else {
				if (!version_ad.equals(version_plan)) {
					WidgetUtils.runInDisplayThread(workbenchWindow.getShell(), new Runnable() {
						@Override
						public void run() {
							MessageDialog.openWarning(workbenchWindow.getShell(), "Incompatible Activity Dictionary",
									"You are attempting to load a plan created with AD version number " + version_plan
											+ " and your current AD version is " + version_ad);
						}
					});
				}
			}
		}
	}

	/**
	 * If the name is null, return null. Else return the portion of the name that follows the
	 * last file-separator character; that is, remove the directory path, leaving only the file
	 * name.
	 * @param name the name of a file; may be null
	 * @return null if the name is null; otherwise the file name with the directory path removed
	 */
	protected String getShortName(String name) {
		if (name == null)
			return name;
		int index = name.lastIndexOf(File.separator);
		if (index > -1)
			return name.substring(index + 1);
		index = name.lastIndexOf("\\");
		if (index > -1)
			return name.substring(index + 1);

		return name;
	}
		
	protected Properties getEditorProperties() {
		if (properties == null) {
			properties = new Properties();
		}
		return properties;
	}

	/**
	 * Is the user editing the plan the "custodian" of the plan?
	 * @param plan a plan
	 * @return whether this editor user is the custodian of the given plan
	 */
	private boolean checkCustodian(EPlan plan) {
		if (PlanEditorPreferences.isCheckCustodian()) {
            String worldPermissions = WrapperUtils.getAttributeValue(plan, PermissionConstants.WORLD_PERMISSIONS_KEY);
            if (worldPermissions == null || !PermissionConstants.PERMISSION_EDIT_BY_ROLE.equals(worldPermissions)) {
            	String custodian = WrapperUtils.getAttributeValue(plan, EditorPlugin.ATTRIBUTE_CUSTODIAN);
            	String ensembleUser = AuthenticationUtil.getEnsembleUser();
            	if ((custodian != null) && (ensembleUser != null) && !custodian.equals(ensembleUser)) {
            		IWorkbenchWindow window = getEditorSite().getWorkbenchWindow();
            		String message = "The custodian of this plan is " + custodian + ", " + "but your username is " + ensembleUser
            				+ ".  " + "Are you sure you want to save?";
            		MessageDialog dialog = new MessageDialog(
            				window.getShell(), 
            				"Custodian/User mismatch", 
            				null, message, 
            				MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 1);
            		return 0 == dialog.open();
            	}
            }
		}
		return true;
	}
	
	/**
	 * The type of a listener that responds to resource deletion, closing the plan editor if its file
	 * is deleted.
	 */
	private final class ResourceDeletedListener implements IResourceChangeListener {
		
		/**
		 * If the file resource underlying the multi-page plan editor's plan has been deleted, close
		 * the plan editor.
		 * @param event the change event; only post-change events are of interest
		 */
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			if (IResourceChangeEvent.POST_CHANGE == event.getType()) {
				List<IResource> changedResources = ResourceUtils.getChangedResources(event.getDelta());
				for (IResource resource : changedResources) {
					final EPlan ePlan = (EPlan) MultiPagePlanEditor.this.getAdapter(EPlan.class);
					if (ePlan != null) {
						Object object = ePlan.getAdapter(IResource.class);
						if (object != null) {
							IResource underlyingResource = CommonUtils.getAdapter(MultiPagePlanEditor.this.getEditorInput(), IFile.class);
							if (underlyingResource.equals(resource)) {
								if (!resource.exists()) {
									Display display = WidgetUtils.getDisplay();
									display.asyncExec(new Runnable() {
										@Override
										public void run() {
											closePlan(ePlan);
										}
									});
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * The type of an outline view associated with a plan editor. It contains a tree viewer that
	 * operates on the plan editor's model's domain. Link with the editor using a selection provider.
	 * @author lbrownst
	 *
	 */
	private class PlanEditorOutlinePage extends ContentOutlinePage {

		/**
		 * Add the widgets to the parent to build the plan editor outline page.
		 * @param parent the Composite that will contain the new widgets
		 */
		@Override
		public void createControl(Composite parent) {
			super.createControl(parent);
			TreeViewer viewer = getTreeViewer();
			PlanEditorModel model = getPlanEditorModel();
			EditingDomain domain = model.getEditingDomain();
			AdapterFactory adapterFactory = EMFUtils.getAdapterFactory(domain);
			viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
			viewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory) {

				@Override
				public Object[] getElements(Object object) {
					EditingDomain domain = (EditingDomain) object;
					ResourceSet resourceSet = domain.getResourceSet();
					EList<Resource> resources = resourceSet.getResources();
					return resources.toArray(new Resource[0]);
				}
				
			});
			viewer.setInput(domain);
			EnsembleSelectionProvider selectionProvider = new EnsembleSelectionProvider(this.toString());
			selectionProvider.attachSelectionProvider(viewer);
			getSite().setSelectionProvider(selectionProvider);
		}
		
	}
	
	/**
	 * The type of a listener that responds to changes in the dirty status of the editor; that
	 * is, either it was saved or it was modified for the first time after having been created,
	 * opened or saved.
	 */
	private class DirtyListenerImpl implements IDirtyListener {
		
		/**
		 * When the dirty state changes, fire a property-change event for the "dirty" property.
		 */
		@Override
		public void dirtyStateChanged() {
			WidgetUtils.runInDisplayThread(MultiPagePlanEditor.this.getContainer(), new Runnable() {
				@Override
				public void run() {
					firePropertyChange(ISaveablePart.PROP_DIRTY);
				}
			});
		}
		
	}
	
	/**
	 * The type of a listener that responds to changes of a plan element's name.
	 */
	private class PlanNameChangeListener extends AdapterImpl {
		
		/**
		 * If the name of a plan element has changed, update the name of the part in which the
		 * change occurred.
		 * @param msg the notification that specifies what feature was changed; we're interested
		 * in the name of a plan element
		 */
		@Override
		public void notifyChanged(Notification msg) {
			if (nameHelper.getNameAttributes().contains(msg.getFeature())) {
				WidgetUtils.runInDisplayThread(MultiPagePlanEditor.this.getContainer(), new Runnable() {
					@Override
					public void run() {
						updatePartName(getPlan());
					}
				});
			}
		}
		
	}
	
	/**
	 * An iterator to iterate through the pages for the multi page plan editor.
	 * 
	 * @author abachman
	 */
	private static final class MultiPagePlanEditorIterator implements Iterator {
		
		private final MultiPagePlanEditor editor;
		private int i = 0;

		private MultiPagePlanEditorIterator(MultiPagePlanEditor editor) {
			this.editor = editor;
		}

		@Override
		public boolean hasNext() {
			return (i < editor.getPageCount());
		}

		@Override
		public Object next() {
			return editor.getEditor(i++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

}
