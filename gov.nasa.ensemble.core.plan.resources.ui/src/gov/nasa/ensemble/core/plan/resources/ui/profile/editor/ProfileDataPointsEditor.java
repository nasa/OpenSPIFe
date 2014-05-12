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
package gov.nasa.ensemble.core.plan.resources.ui.profile.editor;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.treetable.AbstractTreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnLayout;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.common.ui.treetable.TreeTableLabelProvider;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.core.detail.emf.DetailFormToolkit;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.profile.operations.AddProfileDataPointOperation;
import gov.nasa.ensemble.core.plan.resources.profile.operations.RemoveProfileDataPointsOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class ProfileDataPointsEditor {

	private static final String EDITABLE_MESSAGE = "Click on table to edit.";
	private static final String READ_ONLY_MESSAGE = "Data is read only.";
	private static final String INSERT_BUTTON_TEXT = "Insert";
	private static final String INSERT_TOOLTIP_MESSAGE = "Insert new data point.";
	private static final String REMOVE_BUTTON_TEXT = "Remove";
	private static final String REMOVE_TOOLTIP_MESSAGE = "Remove selected data points.";

	public ProfileDataPointsEditor(Composite parent, DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		if (!(target instanceof Profile)) {
			throw new IllegalArgumentException("ProfileDataPointEditor target must be a Profile.");
		}
		Profile profile = (Profile) target;
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		FormToolkit toolkit = parameter.getDetailFormToolkit();

		String displayName = EMFDetailUtils.getDisplayName(profile, pd);
		Section section = DetailFormToolkit.createSection(toolkit, parent, displayName, null, false);
		Composite sectionComposite = toolkit.createComposite(section);
		sectionComposite.setLayout(new GridLayout(2, false));
		section.setClient(sectionComposite);

		String editabilityText = (isProfileEditable(profile) ? EDITABLE_MESSAGE : READ_ONLY_MESSAGE);
		Label editabilityLabel = toolkit.createLabel(sectionComposite, editabilityText);
		editabilityLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));

		final TreeTableViewer viewer = createTreeTableViewer(sectionComposite, toolkit, profile);
		EditingDomain domain = EMFUtils.getAnyDomain(profile);
		IUndoContext undoContext = new ObjectUndoContext(domain);
		ProfileEditorModel editorModel = new ProfileEditorModel(isProfileEditable(profile), undoContext);
		viewer.setEditorModel(editorModel);
		viewer.setInput(profile);
		parent.layout(true);
	}

	@SuppressWarnings("unchecked")
	private TreeTableViewer createTreeTableViewer(final Composite parent, FormToolkit toolkit, final Profile profile) {
		boolean isProfileEditable = isProfileEditable(profile);
		//create tree viewer
		List<AbstractTreeTableColumn> columns = new ProfileDataPointColumnProvider(profile, isProfileEditable).getColumns();
		TreeTableColumnConfiguration config = new TreeTableColumnConfiguration(columns.get(0), columns, columns.get(0));
		TreeTableComposite ttc = new TreeTableComposite(parent, config, false);
		ttc.setLayout(new TreeTableColumnLayout(false));
		final TreeTableViewer viewer = new TreeTableViewer(ttc, config, null);

		//set providers
		viewer.setContentProvider(new ProfileEditorContentProvider());
		viewer.setLabelProvider(new TreeTableLabelProvider() {
			@Override public boolean needsUpdate(Object feature) { return false; }
			@Override public Font getFont(Object element) 		 { return null; }
			@Override public Color getBackground(Object element) { return null; }
		});
		
		//if there's any changes in the profile data points... refresh viewer!
		final TransactionalEditingDomain domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(profile);
		final ResourceSetListenerImpl viewerRefreshListener = new ResourceSetListenerImpl() {
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event) {
				for (Notification notification : event.getNotifications()) {
					if (notification.getNotifier() == profile 
						&& notification.getFeature() == JSciencePackage.Literals.PROFILE__DATA_POINTS) {
						WidgetUtils.runInDisplayThread(viewer.getTree(), new Runnable() {
							@Override
							public void run() {
								viewer.refresh();
								parent.getParent().getParent().layout();
							}
						});
					}
				}
			}
		};
		if (domain != null) {
			domain.addResourceSetListener(viewerRefreshListener);
		}

		if (isProfileEditable) {
			Composite buttonsComposite = toolkit.createComposite(parent);
			buttonsComposite.setLayout(new GridLayout(2, false));
			buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));

			//insert new DataPoint
			final Button insertButton = toolkit.createButton(buttonsComposite, INSERT_BUTTON_TEXT, SWT.PUSH);
			insertButton.setToolTipText(INSERT_TOOLTIP_MESSAGE);
			final SelectionAdapter insertButtonSelectionListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					EList<DataPoint> dataPoints = profile.getDataPoints();
					DataPoint newDataPoint;
					if (dataPoints.isEmpty()) {
						EPlan ePlan = EPlanUtils.getPlanFromResourceSet(profile);
						Date date = (ePlan != null ? ePlan.getMember(TemporalMember.class).getStartTime() : new Date());
						newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint(date, null);
					} else {
						Date date;
						ISelection selection = viewer.getSelection();
						if (selection == null || selection.isEmpty()) {
							date = dataPoints.get(dataPoints.size()-1).getDate();
						} else if (selection instanceof StructuredSelection) {
							Object[] array = ((StructuredSelection) selection).toArray();
							date = ((DataPoint) array[array.length-1]).getDate();
						} else {
							date = new Date(); //if everything fails... just set to now
						}
						newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint(DateUtils.addSeconds(date, 1), null);
					}
					if (!dataPoints.contains(newDataPoint)) {
						try {
							AddProfileDataPointOperation op = new AddProfileDataPointOperation(profile, newDataPoint);
							op.addContext(viewer.getModel().getUndoContext());
							IOperationHistory history = OperationHistoryFactory.getOperationHistory();
							history.execute(op, null, null);
							viewer.setSelection(new StructuredSelection(newDataPoint));
						} catch (Exception e) {
							LogUtil.error(e);
						}
					} else {
						showOperationError();
						viewer.setSelection(new StructuredSelection(newDataPoint));
					}
					
				}
			};
			insertButton.addSelectionListener(insertButtonSelectionListener);

			//remove Data Point
			final Button removeButton = toolkit.createButton(buttonsComposite, REMOVE_BUTTON_TEXT, SWT.PUSH);
			removeButton.setToolTipText(REMOVE_TOOLTIP_MESSAGE);
			removeButton.setEnabled(!viewer.getSelection().isEmpty());
			final SelectionAdapter removeButtonSelectionListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					ISelection selection = viewer.getSelection();
					if (!selection.isEmpty()) {
						if (selection instanceof StructuredSelection) {
							StructuredSelection ss = (StructuredSelection) selection;
							int selectionSize = ss.size();
							DataPoint[] dps = new DataPoint[selectionSize];
							Iterator<DataPoint> iterator = ss.iterator();
							int i = 0;
							while (iterator.hasNext()) {
								dps[i++] = iterator.next();
							}
							RemoveProfileDataPointsOperation op = new RemoveProfileDataPointsOperation(profile, dps);
							op.addContext(EMFUtils.getUndoContext(profile));
							IOperationHistory history = OperationHistoryFactory.getOperationHistory();
							try {
								history.execute(op, null, null);
								viewer.setSelection(StructuredSelection.EMPTY);
							} catch (Exception e) {
								LogUtil.error(e);
							}
						}
					}
				}
			};
			removeButton.addSelectionListener(removeButtonSelectionListener);
			final ISelectionChangedListener removeButtonEnablementListener = new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					removeButton.setEnabled(!viewer.getSelection().isEmpty());
				}
			};
			viewer.addSelectionChangedListener(removeButtonEnablementListener);

			
			//set dispose listener for editable profiles
			viewer.getControl().addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					if (domain != null) {
						domain.removeResourceSetListener(viewerRefreshListener);
					}
					insertButton.removeSelectionListener(insertButtonSelectionListener);
					removeButton.removeSelectionListener(removeButtonSelectionListener);
					viewer.removeSelectionChangedListener(removeButtonEnablementListener);
					dispose(viewer);
				}
			});
		} else {
			//set dispose listener for non-editable profiles
			viewer.getControl().addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					if (domain != null) {
						domain.removeResourceSetListener(viewerRefreshListener);
					}
					dispose(viewer);
				}
			});
		}
		return viewer;
	}
	
	private static boolean isProfileEditable(Profile profile) {
		EditingDomain domain = EMFUtils.getAnyDomain(profile);
		if (domain != null) {
			Resource resource = profile.eResource();
			boolean readOnly = domain.isReadOnly(resource);
			if (readOnly) {
				return false;
			}
		}
		return profile.isExternalCondition();
	}

	private void showOperationError() {
		//TODO handle operation errors better?
		String errorMessage = "Can't add duplicate Data Points to a Profile.";
		Status status = new Status(IStatus.ERROR, "gov.nasa.ensemble.core.plan.resources.profile.editor", 0, errorMessage, null);
		ErrorDialog.openError(Display.getCurrent().getActiveShell(),  "Profile Editor Error", "Can't insert Data Point.", status);
	}
	
	private void dispose(final TreeTableViewer viewer) {
		IEnsembleEditorModel model = viewer.getModel();
		if (model instanceof ProfileEditorModel) {
			((ProfileEditorModel) model).dispose();
		}
		viewer.setEditorModel(null);
		viewer.getControl().dispose();
	}
	
}
