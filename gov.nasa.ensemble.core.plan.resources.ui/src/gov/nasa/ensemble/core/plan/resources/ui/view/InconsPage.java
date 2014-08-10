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
package gov.nasa.ensemble.core.plan.resources.ui.view;

import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.Page;

public class InconsPage extends Page {

	private final PlanEditorModel planEditorModel;
	private final PostCommitListener modelChangeListener = new PlanInconsModelChangeListener();
	private final EPlan plan;
	private DeleteInconsAction deleteInconsAction = new DeleteInconsAction();
	private EnsembleSelectionProvider selectionProvider;
	private Control control;
	private TableViewer inconsViewer;

	public InconsPage(IEditorPart editor, PlanEditorModel model) {
		this.planEditorModel = model;
		this.plan = model.getEPlan();
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new EnsembleComposite(parent, SWT.NONE);
		control = composite;
		IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				EPlan plan = (EPlan) inputElement;
				ResourceConditionsMember member = plan.getMember(ResourceConditionsMember.class);
				return member.getConditions().toArray();
			}

			@Override
			public void dispose() { /* empty */
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { /* empty */
			}
		};
		inconsViewer = InconsTableUtils.getTableViewer(composite, contentProvider);
		inconsViewer.setInput(plan);
		setup();
	}

	private void setup() {
		TransactionalEditingDomain domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(plan);
		domain.addResourceSetListener(modelChangeListener);
		selectionProvider = new EnsembleSelectionProvider(this.toString());
		selectionProvider.attachSelectionProvider(inconsViewer);
		selectionProvider.addSelectionChangedListener(deleteInconsAction);
		IActionBars actionBars = getSite().getActionBars();
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteInconsAction);
		getSite().setSelectionProvider(selectionProvider);
	}

	@Override
	public void dispose() {
		TransactionalEditingDomain domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(plan);
		domain.removeResourceSetListener(modelChangeListener);
		selectionProvider.removeSelectionChangedListener(deleteInconsAction);
		getSite().setSelectionProvider(null);
		super.dispose();
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public void setFocus() {
		Control control = getControl();
		if ((control != null) && !control.isDisposed()) {
			control.setFocus();
		}
	}

	public void removeConditions(IStructuredSelection selection) {
		List list = ((StructuredSelection) selection).toList();
		EditingDomain domain = planEditorModel.getEditingDomain();
		Command command = RemoveCommand.create(domain, list);
		EMFUtils.executeCommand(domain, command);
	}

	private class PlanInconsModelChangeListener extends PostCommitListener {
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			boolean update = false;
			for (Notification notification : event.getNotifications()) {
				Object feature = notification.getFeature();
				if (feature == MemberPackage.Literals.RESOURCE_CONDITIONS_MEMBER__CONDITIONS || feature == MemberPackage.Literals.CONDITIONS__DESCRIPTION || feature == MemberPackage.Literals.CONDITIONS__TIME || feature == MemberPackage.Literals.CONDITIONS__ACTIVE) {
					update = true;
					break;
				}
			}
			if (update) {
				TransactionUtils.runInDisplayThread(control, plan, new Runnable() {
					@Override
					public void run() {
						inconsViewer.refresh(true);
					}
				});
			}

		}
	}

	private final class DeleteInconsAction extends Action implements ISelectionChangedListener {
		private DeleteInconsAction() {
			super("Delete Incons");
		}

		@Override
		public void run() {
			ISelection selection = selectionProvider.getSelection();
			if (!selection.isEmpty() && (selection instanceof IStructuredSelection)) {
				removeConditions((IStructuredSelection) selection);
			}
		}

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			boolean enabled = shouldBeEnabled(selection);
			this.setEnabled(enabled);
		}

		private boolean shouldBeEnabled(ISelection selection) {
			if (!selection.isEmpty() && (selection instanceof IStructuredSelection)) {
				Object selected = ((IStructuredSelection) selection).getFirstElement();
				if (selected instanceof Conditions) {
					return true;
				}
			}
			return false;
		}
	}

}
