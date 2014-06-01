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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewerFilter;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;

public class MergeTotalViewer extends Viewer {

	private final MergeTotalComposite totalComposite;
	private TreeTableViewer treeTableViewer;
	private ISelection selection;
	private List<EPlanElement> selectedElements = Collections.emptyList();
	private EPlan plan;
	private TransactionalEditingDomain domain;
	
	public MergeTotalViewer(MergeTotalComposite totalComposite) {
		this.totalComposite = totalComposite;
	}

	@Override
	public Control getControl() {
		return totalComposite;
	}

	@Override
	public TreeTableViewer getInput() {
		return treeTableViewer;
	}

	@Override
	public void setInput(Object input) {
		treeTableViewer = (TreeTableViewer)input;
		if (plan == null) {
			plan = (EPlan)treeTableViewer.getInput();
			domain = TransactionUtils.getDomain(plan);
			setupDomainListener(domain);
			setupSelectionListener(treeTableViewer);
		} else {
			System.out.println("plan already set?");
		}
		refreshSubtotalColumns();
		refreshTotalColumns();
	}

	private void setupSelectionListener(final IPostSelectionProvider postSelectionProvider) {
		final ISelectionChangedListener listener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setSelection(event.getSelection());
			}
		};
		postSelectionProvider.addPostSelectionChangedListener(listener);
		totalComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				postSelectionProvider.removePostSelectionChangedListener(listener);
			}
		});
	}

	private void setupDomainListener(final TransactionalEditingDomain domain) {
		final ResourceSetListener listener = new UpdateModelChangeListener();
		domain.addResourceSetListener(listener);
		totalComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				domain.removeResourceSetListener(listener);
			}
		});
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		this.selection = selection;
		List<EPlanElement> newElements = new ArrayList<EPlanElement>();
		if (selection instanceof IStructuredSelection) {
			Set<EPlanElement> selectedElements = PlanEditorUtil.emfFromSelection(selection);
			Set<EPlanElement> parentElements = EPlanUtils.removeContainedElements(selectedElements);
			newElements = new ArrayList<EPlanElement>(parentElements);
		}
		synchronized (this) {
			if (selectedElements.equals(newElements)) {
				return; // nothing to do
			}
			selectedElements = newElements;
			refreshSubtotalColumns();
		}
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void refresh() {
		refreshSubtotalColumns();
		refreshTotalColumns();
	}

	/*
	 * Utility methods below
	 */
	
	/**
	 * @param selection
	 */
	private synchronized void refreshSubtotalColumns() {
		List<EPlanElement> elements = selectedElements;
		List<EActivity> selectedActivities = gatherIncludedActivities(elements);
		totalComposite.setSelectedActivitiesCount(selectedActivities.size());
		totalComposite.updateSubtotalRow(selectedActivities);
	}

	private synchronized void refreshTotalColumns() {
		List<EActivity> filteredActivities = gatherIncludedActivities(Collections.singletonList(plan));
		totalComposite.updateTotalRow(filteredActivities);
	}

	private List<EActivity> gatherIncludedActivities(List<? extends EPlanElement> elements) {
		final List<EActivity> selectedActivities = new ArrayList<EActivity>();
		final ViewerFilter[] filters = treeTableViewer.getFilters();
		new PlanVisitor(false) {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EActivity) {
					EActivity activity = (EActivity) element;
					if (isIncluded(filters, activity)) {
						selectedActivities.add(activity);
					}
				}
			}
		}.visitAll(elements);
		return selectedActivities;
	}
	
	private boolean isIncluded(final ViewerFilter[] filters, EActivity activity) {
		if (activity.isIsSubActivity()) {
			return false;
		}
		for (ViewerFilter filter : filters) {
			if (!filter.select(treeTableViewer, null, activity)) {
				return false;
			}
		}
		return true;
	}

	private final class UpdateModelChangeListener extends PostCommitListener {
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			boolean selectedChanged = false;
			boolean filteredChanged = false;
			ViewerFilter[] filters = treeTableViewer.getFilters();
			for (Notification notification : event.getNotifications()) {
				Object notifier = notification.getNotifier();
				if (!selectedChanged) {
					while ((notifier instanceof EObject) && !(notifier instanceof EPlanElement)) {
						EObject object = (EObject) notifier;
						notifier = object.eContainer();
					}
					if (notifier instanceof EActivity) {
						EActivity activity = (EActivity) notifier;
						if (isIncluded(filters, activity)) {
							selectedChanged = isSelected(activity);
						}
					}
					final List<? extends AbstractMergeColumn> columns = totalComposite.getConfiguration().getColumns();
					if (!filteredChanged) {
						for (ITreeTableColumn column : columns) {
							Object feature = notification.getFeature();
							filteredChanged = column.needsUpdate(feature);
							if (filteredChanged) {
								break;
							}
						}
					}
				}
				if (!filteredChanged) {
					filteredChanged = checkFilters(notification, filters);
				}
				if (filteredChanged) {
					break;
				}
			}
			if (selectedChanged || filteredChanged) {
				refreshSubtotalColumns();
			}
			if (filteredChanged) {
				refreshTotalColumns();
			}
		}

		/**
		 * Returns true if any of the following conditions hold:
		 * 1. an activity matching the current filter was removed from the plan
		 * 2. an activity matching the current filter was added to the plan
		 * 3. a feature was modified, and the feature affects a current filter
		 * 
		 * @param notification
		 * @param filters
		 * @return
		 */
		private boolean checkFilters(Notification notification, ViewerFilter[] filters) {
			Object feature = notification.getFeature();
			for (ViewerFilter filter : filters) {
				if (filter instanceof TreeTableViewerFilter) {
					TreeTableViewerFilter viewerFilter = (TreeTableViewerFilter) filter;
					if (viewerFilter.isFilterProperty(feature)) {
						return true;
					}
				}
			}
			Collection<EActivity> added = EPlanUtils.getActivitiesAdded(notification);
			for (EActivity activity : added) {
				if (isIncluded(filters, activity)) {
					return true;
				}
			}
			Collection<EActivity> removed = EPlanUtils.getActivitiesRemoved(notification);
			for (EActivity activity : removed) {
				if (isIncluded(filters, activity)) {
					return true;
				}
			}
			return false;
		}

		protected boolean isSelected(EActivity activity) {
			synchronized (MergeTotalViewer.this) {
				boolean selectedChanged;
				EPlanElement element = activity;
				while (!selectedElements.contains(element) && (element instanceof EPlanChild)) {
					element = ((EPlanChild)element).getParent();
				}
				selectedChanged = selectedElements.contains(element);
				return selectedChanged;
			}
		}
	}

}
