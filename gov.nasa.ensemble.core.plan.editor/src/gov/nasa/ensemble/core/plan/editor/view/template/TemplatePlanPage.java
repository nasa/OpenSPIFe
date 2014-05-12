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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.view.page.TreeViewerPage;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.provider.EPlanElementLabelProvider;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityTransferProvider;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * A page in the TemplatePlanView for displaying a tree of template plans.
 */
public abstract class TemplatePlanPage extends TreeViewerPage {
	
	/**
	 * An array of all Transfers. There are two of them: for a plan container and for an
	 * activity.
	 */
	private static final Transfer[] TRANSFERS = new Transfer[] {
		PlanContainerTransferProvider.transfer,
		ActivityTransferProvider.transfer,
	};
	
	/** The stored template plans. */
	private List<EPlan> templatePlans = null;
	protected boolean multiplePlans = false;
	protected boolean readOnly = true;

	/** Initializing the TemplatePlanView can cause loadTemplatePlan to be called twice */
	private volatile boolean loadJobInProgress;
	
	/**
	 * Add the option to alphabetically sort or not the TreeViewer. 
	 * Default is true, set template.sort.alphabetically in ensemble.properties.
	 */
	protected final static String SORT_TREE_VIEWER_ALPHABETICALLY = "templates.sort.alphabetical.default";
	protected TreeViewerAlphabeticalSorterAction alphabeticalSorterAction;
	
	/**
	 * The searchable field defaults to true in the zero-argument constructor.
	 * Initializing the searchable property is redundant, since the field is initialized to true.
	 */
	public TemplatePlanPage() {
		super(true);
	}
	
	/**
	 * A constructor that allows the caller to set the searchable property.
	 * @param searchable whether the page can be searched
	 */
	public TemplatePlanPage(boolean searchable) {
		super(searchable);
	}
	
	/**
	 * A constructor that allows the caller to set the searchable and searchDelay properties.
	 * @param searchable whether the page can be searched
	 * @param searchDelay milliseconds to delay after search text modification before initiating search
	 */
	public TemplatePlanPage(boolean searchable, int searchDelay) {
		super(searchable, searchDelay);
	}

	/**
	 * To dispose of this page, remove references to the selection provider and the template
	 * plan.
	 */
	@Override
	public void dispose() {
		templatePlans = null;
		super.dispose();
	}

	/**
	 * To refresh the contents of this page, re-load the template plan into the TreeViewer.
	 * This is the way the template plan is initially loaded.
	 */
	protected void refreshContents() {
		loadTemplatePlans();
	}

	/**
	 * Require concrete subclasses to implement the load method. This is what is attempted in
	 * a Job.
	 * @param monitor a progress monitor
	 * @return the plan that was loaded
	 * @throws Exception 
	 */
	protected abstract EPlan doLoadTemplatePlan(IProgressMonitor monitor) throws Exception;
	
	protected List<EPlan> doLoadTemplatePlans(IProgressMonitor monitor) throws Exception {
		EPlan templatePlan = doLoadTemplatePlan(monitor);
		if (templatePlan == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(templatePlan);
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY, TRANSFERS, new TemplatePlanViewDragSourceListener(viewer));
	}

	@Override
	protected void configureTreeViewer(TreeViewer viewer) {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		viewer.setContentProvider(getViewerContentProvider());
		viewer.setLabelProvider(getViewerLabelProvider(adapterFactory));
		createTreeViewerActions(viewer);
	}
	
	protected ITreeContentProvider getViewerContentProvider() {
		return multiplePlans ? new MultiTemplatePlanViewContentProvider() : new TemplatePlanViewContentProvider();
	}
	
	protected AdapterFactoryLabelProvider getViewerLabelProvider(AdapterFactory adapterFactory) {
		return new EPlanElementLabelProvider(adapterFactory, false);
	}
	
	protected void createTreeViewerActions(final TreeViewer viewer) {
		alphabeticalSorterAction = new TreeViewerAlphabeticalSorterAction();
		
		//add actions to toolbar
		IToolBarManager mgr = getSite().getActionBars().getToolBarManager();
		mgr.add(new Separator());
		mgr.add(alphabeticalSorterAction);
	}
	
	@Override
	protected void searchInputChanged(String key, String searchString) {
		IContentProvider cp = treeViewer.getContentProvider();
		if (cp instanceof TemplatePlanViewContentProvider) {						
			((TemplatePlanViewContentProvider) cp).searchInputChanged(key, searchString);			
		}
	}

	/**
	 * When the TreeViewer is refreshed, the template plans are re-loaded. Do this in a Job.
	 * @param viewer the TreeViewer being refreshed
	 */
	protected synchronized void loadTemplatePlans() {
		if (loadJobInProgress) {
			return;
		}
		final Job load = new TemplatePlanLoadingJob("Loading Template Plan", treeViewer);
		load.addJobChangeListener(new JobChangeAdapter() {
			/**
			 * When the Job is done, if it was successful clear the Job's name.
			 * @param event contains the Job's results, indicating whether successful
			 */
			@Override
			public void done(IJobChangeEvent event) {
				loadJobInProgress = false;
				if (event.getResult().isOK())
					load.setName("");
			}
		});
		load.schedule();
		loadJobInProgress = true;
	}

	/**
	 * If the tree viewer's model is a plan, return it.  Otherwise, if the tree viewer has no
	 * model, return the stored plan.
	 * @return the plan, preferably from the tree viewer but in a pinch from this object
	 */
	@SuppressWarnings("unchecked")
	public List<EPlan> getTemplatePlans() {
		Object input = getTreeViewer() == null ? null : getTreeViewer().getInput();
		if (input instanceof EPlan) {
			return Collections.singletonList((EPlan)input);
		} 
		if (input instanceof List<?>) {
			return (List<EPlan>)input;
		} 
		if (templatePlans != null) {
			// default to whatever was saved
			return templatePlans;
		} 
		return Collections.emptyList();	
	}
	
	public EPlan getTemplatePlan() {
		List<EPlan> templatePlans = getTemplatePlans();
		if (!templatePlans.isEmpty()) {
			return templatePlans.get(0);
		}
		return null;
	}
	
	private final class TemplatePlanLoadingJob extends Job {
		
		private final TreeViewer viewer;

		private TemplatePlanLoadingJob(String name, TreeViewer viewer) {
			super(name);
			this.viewer = viewer;
		}

		/**
		 * The method to run as the Job. Call the subclass's implementation of the load
		 * operation. Change the label. On success, configure the template plan.
		 * @param monitor a progress monitor
		 * @return a success code 
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			setTreeLabelTextInDisplayThread("Initializing...");
			try {
				final List<EPlan> templatePlans = doLoadTemplatePlans(monitor);
				TemplatePlanPage.this.templatePlans = templatePlans;
				if (templatePlans.isEmpty()) {
					setTreeLabelTextInDisplayThread("No templates available.");
					return Status.CANCEL_STATUS;
				}
				for (EPlan plan : templatePlans) {
					configurePlan(plan);
				}
				WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
					/** Configure the TreeViewer and set the label to the URI if there is a single template plan. */
					@Override
					public void run() {
						if (multiplePlans) {
							viewer.setInput(templatePlans);
							setTreeLabelText("Available templates");
						} else {
							EPlan templatePlan = templatePlans.get(0);
							viewer.setInput(templatePlan);
							Resource eResource = templatePlan.eResource();
							if (eResource != null) {
								setTreeLabelText(eResource.getURI().toString());
							}
						}
						
					}
				});
			} catch (final Exception e) {
				setTreeLabelTextInDisplayThread("error - "+e.getMessage());
			} finally {
				monitor.done();
			}
			return Status.OK_STATUS;
		}

		private void configurePlan(final EPlan templatePlan) {
			TransactionUtils.writing(templatePlan, new Runnable() {
				
				/**
				 * If the plan is a template plan, set a flag in the plan specifying that.
				 * If there is no resource, create a temporary file and add the plan to it.
				 * Specify that the resource is read-only.
				 */
				@Override
				public void run() {
					if (!templatePlan.isTemplate()) {
						templatePlan.setTemplate(true);
					}
					Resource planResource = templatePlan.eResource();
					if (planResource == null) {
						try {
							File tempFile = File.createTempFile("plan", "template");
							tempFile.deleteOnExit();
							Resource resource = new PlanResourceImpl(URI.createFileURI(tempFile.getPath()));
							resource.getContents().add(templatePlan);
						} catch (IOException e) {
							LogUtil.error(e);
						}
					}
					if (readOnly) {
						EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(templatePlan);
						if (domain instanceof AdapterFactoryEditingDomain) {
							((AdapterFactoryEditingDomain) domain).getResourceToReadOnlyMap().put(templatePlan.eResource(), Boolean.TRUE);
						}
					}
				}
			});
		}
	}
	
	private class TreeViewerAlphabeticalSorterAction extends Action {

		public TreeViewerAlphabeticalSorterAction() {
			super("Sort Alphabetically", AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "/icons/sort.gif"));
			setChecked(EnsembleProperties.getBooleanPropertyValue(SORT_TREE_VIEWER_ALPHABETICALLY, true));
			sort();
		}
		
		@Override
		public void run() {
			sort();
		}
		
		protected void sort() {
			if (treeViewer != null) {
				TreePath[] expandedTreePaths = treeViewer.getExpandedTreePaths();
				if (isChecked()) {
					setTreeViewerAlphabeticalSorter();
				} else {
					treeViewer.setSorter(null);
				}
				treeViewer.setExpandedTreePaths(expandedTreePaths);
			}
		}
	}

}
