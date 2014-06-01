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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.detail.view.DetailPage;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorModel.IDirtyListener;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorPart;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.provider.TemporalItemProviderAdapterFactory;
import gov.nasa.ensemble.core.model.plan.temporal.provider.TemporalMemberItemProvider;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry.RegistrationException;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public class TemplatePlanElementEditor extends AbstractEnsembleEditorPart {
	

	/** This listener is stored so that it can be removed on disposal. */
	private IDirtyListener dirtyListener = new DirtyListener();
	
	/** This listener is stored so that it can be removed on disposal. */
	private Adapter nameChangeListener = new NameChangeListener();
	
	/** 
	 * The input to this editor that was most recently registered successfully.
	 * This is stored so that it can be unregistered on disposal.
	 */
	private IEditorInput  lastRegistered = null;
	
	/** It looks like this is not currently being set. */
	private TemplatePlanPage templatePlanPage = null;
	
	/** It's not clear why this is being stored; it could have been local to init(). */
	private EnsembleSelectionProvider ensembleSelectionProvider = new EnsembleSelectionProvider(this.toString());
	
	/**
	 * This is set in createPartControl() because it depends on the editor being initialized.
	 * It is used by isSaveOnCloseNeeded().
	 */
	private DetailPage detailPage = null;
	
	/**
	 * This editor's undo context is it's model's undo context
	 * @return the model's undo context
	 */
	@Override
	public IUndoContext getUndoContext() {
		return getPlanEditorModel().getUndoContext();
	}
	
	/**
	 * Method to get the current plan editor model for this editor. This editor
	 * is using a very simple plan editor model. The model just contains an almost
	 * empty plan (just has the model that is being edited in this editor).
	 * @return a model that just has the template plan element to be edited by this 
	 * editor.
	 */
	public PlanEditorModel getPlanEditorModel() {
		// use a temporary input for the temporary model
		URIEditorInput editorInput = getAlteredURIEditorInput();
		PlanEditorModel planEditorModel = PlanEditorModelRegistry.getPlanEditorModel(editorInput);
		if(planEditorModel == null) {
			EPlan tempPlan = getTemporaryEPlan();
			planEditorModel = new PlanEditorModel(tempPlan);
			planEditorModel.addDirtyListener(dirtyListener);
			try {
				PlanEditorModelRegistry.registerPlanEditorModel(editorInput, planEditorModel);
				lastRegistered = editorInput;
				PlanEditorModelRegistry.registerEditor(planEditorModel, this);
			} catch (RegistrationException e) {
				LogUtil.error(e);
			}
		}
		return planEditorModel;
	}
	
	/**
	 * Editor input wrapped around the altered URI. This is necessary so that
	 * there are no conflics between the actual template.plan URIs and our fictional
	 * URIs (template.copy.plan)
	 * @return a newly-created URIEditorInput for the URI
	 */
	private URIEditorInput getAlteredURIEditorInput() {
		URIEditorInput editorInput = getURIEditorInput();
		URI alteredURI = getAlteredURI(editorInput.getURI());
		URIEditorInput alteredURIEditorInput = new URIEditorInput(alteredURI);
		return alteredURIEditorInput;
	}
	
	/**
	 * Alter the template plan URI to read template.copy.plan so that there are no
	 * conflicts between the dummy URI and the original.
	 * @param uri the original
	 * @return an altered URI, with "template.plan" replaced by "template.copy.plan"
	 */
	private URI getAlteredURI(URI uri) {
		String uriString = uri.toString();
		URI result = URI.createURI(uriString.replaceAll("template.plan", "template.copy.plan"));
		return result;		
	}
	
	/**
	 * Return the underlying template plan element that is being edited by this editor. The
	 * element returned is disconnected from the original template plan but is retrieved
	 * from the TemplatePlanURIRegistry.
	 * @return the model that this editor is editing.
	 */
	public EPlanElement getTemplatePlanElement() {
		URIEditorInput uriEditorInput = getURIEditorInput();
		URI uri = uriEditorInput.getURI();
		EPlanElement ePlanElement = TemplatePlanURIRegistry.INSTANCE.getRegisteredValue(uri);
		return ePlanElement;
	}
	
	/**
	 * Create a temporary ePlan that will contain an appropriate model based on the given
	 * uri.
	 * @return a temporary plan so that the template plan does not have to be reloaded.
	 */
	private EPlan getTemporaryEPlan() {
		ResourceSet resourceSet = TransactionUtils.createTransactionResourceSet();
		URI updatedURI = getAlteredURIEditorInput().getURI();
		PlanResourceImpl planResource = new PlanResourceImpl(updatedURI);		
		EPlan tempPlan = PlanFactory.eINSTANCE.createEPlan();
		planResource.getContents().add(tempPlan);
		EPlanElement ePlanElement = getTemplatePlanElement();
		ePlanElement.eAdapters().add(nameChangeListener);	
		tempPlan.getChildren().add((EPlanChild)ePlanElement);
		resourceSet.getResources().add(planResource);
		hideStartAndEndTimes(tempPlan);
		return tempPlan;
	}

	/**
	 * Add an adapter factory to the AdapterFactoryEditingDomain if it is a ComposedAdapterFactory
	 * Auxiliary to getTemporaryEPlan() to help initialize its return value.
	 * Re-implemented to exclude startTime and endTime property descriptors.
	 * @param tempPlan the plan returned by getTemporaryEPlan()
	 */
	private void hideStartAndEndTimes(EPlan tempPlan) {
		TransactionalEditingDomain editingDomain = TransactionUtils.getDomain(tempPlan);
		AdapterFactoryEditingDomain adapterFactoryEditingDomain = (AdapterFactoryEditingDomain)editingDomain;
		AdapterFactory f = adapterFactoryEditingDomain.getAdapterFactory();
		if (f instanceof ComposedAdapterFactory) {
		       final ComposedAdapterFactory factory = (ComposedAdapterFactory) f;
		       factory.addAdapterFactory(new TemporalItemProviderAdapterFactory() {
				@Override
				protected Adapter createAdapter(final Notifier target, Object type) {
					Adapter adapter = null;
					if(target instanceof TemporalMember) {
						adapter = new TemporalMemberItemProvider(factory) {
							@Override
							public List<IItemPropertyDescriptor> getPropertyDescriptors(Object target) {
								if (itemPropertyDescriptors == null) {
									List<IItemPropertyDescriptor> propertyDescriptors = super.getPropertyDescriptors(target);
									List<IItemPropertyDescriptor> itemsToRemove = new ArrayList<IItemPropertyDescriptor>();
									for(IItemPropertyDescriptor propertyDescriptor : propertyDescriptors) {
										Object feature = propertyDescriptor.getFeature(target);
										if(feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME
												|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME) {
											itemsToRemove.add(propertyDescriptor);
										}
									}
									itemPropertyDescriptors.removeAll(itemsToRemove);
								}
								return itemPropertyDescriptors;
							}
						};
					} else {
						adapter = super.createAdapter(target, type);
					}

					return adapter;
				}  
		     });
		}
	}

	/**
	 * If there is a plan editor model, return its dirty bit; otherwise return false.
	 */
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
	 * Return the PlanStructureModifier singleton instance.
	 */
	@Override
	public IStructureModifier getStructureModifier() {
		return PlanStructureModifier.INSTANCE;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		URI uri = getURIEditorInput().getURI();
		TemplatePlanURIRegistry.INSTANCE.register(uri, getTemplatePlanElement());
		getPlanEditorModel().resetDirty();
	}	

	/**
	 * Handle the cases in which the class is PlanEditorModel, EPlanElement, EPlan and
	 * TemplatePlanPage
	 * @param adapter the class of the adapter that is sought
	 * @return 
	 */
	@Override
	public Object getAdapter(Class adapter) {
		Object result = null;
		if(adapter == PlanEditorModel.class){
			result = getPlanEditorModel();
		} else if(adapter == EPlanElement.class) {
			result = getTemplatePlanElement();
		} else if(adapter == TemplatePlanPage.class) {
			result = templatePlanPage;
		} else if(adapter == EPlan.class) {
			if(templatePlanPage != null) {
				//result = templatePlanPage.getTemplatePlan();
			}
		} else {
			result = super.getAdapter(adapter);
		}
		return result;
	}

	/**
	 * The "save-as" operation is not supported, so this is a no-op.
	 */
	@Override
	public void doSaveAs() {
		// save as not supported
	}

	/**
	 * The "save-as" operation is not supported, so this returns false
	 * @return false
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Create and store the detail page.
	 * @param parent the Composite parent of the detail-page control
	 */
	@Override
	public void createPartControl(Composite parent) {		
		detailPage = new DetailPage(this, getEditorSite());
		detailPage.createControl(parent);
		URIEditorInput uriEditorInput = getURIEditorInput();
		if(uriEditorInput != null) {
			EPlanElement templatePlanElement = getTemplatePlanElement();
			IStructuredSelection selection = new StructuredSelection(templatePlanElement);
			detailPage.updateSelection(selection);
		}
		
	}
	
	/**
	 * Decorate the init() method to set the part's name to the name of the template plan
	 * element and to set the selection provider
	 * @param site this editor's site
	 * @param input this editor's intput
	 * @throws PartInitException
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		//rememberTemplatePlanPage(site, input);
		setPartName(getTemplatePlanElement().getName());
		ISelectionProvider selectionProvider = site.getSelectionProvider();
		if(selectionProvider == null) {
			site.setSelectionProvider(ensembleSelectionProvider);
			ensembleSelectionProvider.setSelection(new StructuredSelection(getTemplatePlanElement()));
		}
	}	

	@SuppressWarnings("unused")
	private void rememberTemplatePlanPage(IEditorSite site, IEditorInput input) {
		if(input instanceof URIEditorInput) {
			URIEditorInput uriEditorInput = (URIEditorInput)input;
			URI uri = uriEditorInput.getURI().trimFragment().trimSegments(1);
			
			/*
			 * Iterate through all editors to find one that has the template plan page we want.
			 */
			IWorkbenchPage page = site.getPage();
			IEditorReference[] editorReferences = page.getEditorReferences();
			for(IEditorReference editorReference : editorReferences) {
				IEditorPart editor = editorReference.getEditor(false);
				if(editor != null) {
					Object adapter = editor.getAdapter(EPlan.class);
					if(adapter instanceof EPlan) {
						EPlan plan = (EPlan)adapter;
						Resource eResource = plan.eResource();
						URI planResourceURI = eResource.getURI().trimSegments(1);
						if(uri.equals(planResourceURI)) {
							adapter = editor.getAdapter(TemplatePlanPage.class);
							if(adapter instanceof TemplatePlanPage) {
								templatePlanPage = (TemplatePlanPage)adapter;
								break;
							}
						}
					}
				}	
			}
		}
	}
	
	/**
	 * A listener that is called when an edited model flips its dirty state.
	 */
	private class DirtyListener implements IDirtyListener {
		/**
		 * When the "dirty" bit it toggled, run in its own thread a method that fires the
		 * property-change event for the "dirty" property.
		 */
		@Override
		public void dirtyStateChanged() {
			Widget widget = getSite().getShell();
			WidgetUtils.runInDisplayThread(widget, new Runnable() {
				@Override
				public void run() {
					firePropertyChange(PROP_DIRTY);
				}
			});		
		}		
	}	
	
	/**
	 * Dispose of this editor; this has about half a dozen steps.
	 */
	@Override
	public void dispose() {
		super.dispose();
		EPlanElement templatePlanElement = getTemplatePlanElement();
		templatePlanElement.eAdapters().remove(nameChangeListener);		
		PlanEditorModel planEditorModel = getPlanEditorModel(); 
		PlanEditorModelRegistry.unregisterEditor(planEditorModel, this);
		PlanEditorModelRegistry.unregisterInput(getAlteredURIEditorInput());
		PlanEditorModelRegistry.unregisterInput(lastRegistered);
		planEditorModel.removeEditor(this);
		planEditorModel.removeDirtyListener(dirtyListener);
		TemplatePlanURIRegistry.INSTANCE.unregisterKey(getURIEditorInput().getURI());
	}	
	
	/**
	 * If this editor's input is a URIEditorInput, return it; otherwise return null.
	 */
	private URIEditorInput getURIEditorInput() {
		URIEditorInput uriEditorInput = null;
		IEditorInput editorInput = getEditorInput();
		if(editorInput instanceof URIEditorInput) {
			uriEditorInput = (URIEditorInput)editorInput;
		}
		return uriEditorInput;
	}
	
	/**
	 * Keep the editor title in sync with the name of the element as it is edited.
	 * This callback is executed when there is a name change. If a plan element's name has been
	 * changed, run in its own thread a method that sets the name of this part to be the new
	 * plan element name.
	 * @param msg the notification of the name change, specifying what feature was changed
	 */
	private class NameChangeListener extends AdapterImpl {
		@Override
		public void notifyChanged(final Notification msg) {
			Object feature = msg.getFeature();
			Widget widget = getSite().getShell();
			if(feature == PlanPackage.Literals.EPLAN_ELEMENT__NAME) {
				WidgetUtils.runInDisplayThread(widget, new Runnable() {
					@Override
					public void run() {
						Object newValue = msg.getNewValue();
						if(newValue == null) {
							newValue = "";
						}
						String partName = newValue.toString();
						setPartName(partName);
					}
				});	
			}
		}		
	}
	
	/**
	 * This is a no-op, but must be implemented.
	 */
	@Override
	public void setFocus() {
		// no impl
	}

	/**
	 * This decorates the isSaveOnCloseNeeded() method to add a side effect: if the detail
	 * page is not null and has a non-disposed control, make that the focus, thereby causing
	 * text fields to lose focus.
	 * @return whatever the superclass returns
	 */
	@Override
	public boolean isSaveOnCloseNeeded() {
		// cause any text fields to loose focus
		if(detailPage != null) {
			Control control = detailPage.getControl();
			if(control != null && !control.isDisposed()) {
				control.setFocus();
			}
		}
		return super.isSaveOnCloseNeeded();
	}
	
	@Override
	protected boolean useUndoableOperations() {
		return false;
	}
}
