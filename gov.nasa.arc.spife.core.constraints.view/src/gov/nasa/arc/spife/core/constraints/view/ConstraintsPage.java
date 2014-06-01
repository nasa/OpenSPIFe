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
/*
 * Created on Apr 6, 2005
 *
 */
package gov.nasa.arc.spife.core.constraints.view;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.plan.IPlanEditApprover;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalConstraintPrinter;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalBoundOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalRelationOperation;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.TemporalNodeHyperlinkListener;
import gov.nasa.ensemble.core.plan.editor.constraints.ConstraintDialog;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.Page;

/**
 *	A pagebook page that listens to the model and updates itself with a description of
 *	temporalbound and temporal relation information.
 *	@author bachmann
 */
class ConstraintsPage extends Page {
	
	private static Image CHAIN_IMAGE = gov.nasa.ensemble.core.plan.editor.constraints.Activator.createIcon("ChainIcon_3.gif");
	private static Image DELETE_IMAGE = gov.nasa.ensemble.core.plan.editor.constraints.Activator.createIcon("delete_constraint.gif");
	
    private final boolean useDebuggingColors = false;
    private final Logger trace = Logger.getLogger(ConstraintsPage.class);
	
	private Set<EPlanElement> nodes = Collections.emptySet();

	private FormToolkit toolkit;
    private ScrolledForm form;
    private HeaderComposite headerComposite = null;
	private Composite constraintsComposite = null;
	
    private final PostCommitListener modelChangeListener = new ConstraintsCommitListener();  
    private final ISelectionProvider selectionProvider;
    private final ISelectionListener selectionListener = new SelectionChangedListener();
    private final PlanEditorModel model;
	private final IdentifiableRegistry<EPlanElement> identifiableRegistry = new IdentifiableRegistry<EPlanElement>();
	private final TemporalConstraintPrinter constraintPrinter = new TemporalConstraintPrinter(identifiableRegistry);

    public ConstraintsPage(IEditorPart editor, PlanEditorModel model) {
    	this.selectionProvider = editor.getSite().getSelectionProvider();
		this.model = model;
    }
    
    public EPlan getPlan() {
		return model.getEPlan();
	}
    
    @Override
	public void createControl(Composite parent) {
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        form.setExpandHorizontal(true);
        form.setExpandVertical(true);
        if (useDebuggingColors) parent.setBackground(ColorConstants.lightGreen);
        createBody(form.getBody()); 
		headerComposite.setFont(form.getFont());
		setup();
    }

	private void setup() {
	    TransactionalEditingDomain domain = TransactionUtils.getDomain(model.getEPlan());
		domain.addResourceSetListener(modelChangeListener);
		getSite().setSelectionProvider(selectionProvider);
		getSite().getPage().addPostSelectionListener(selectionListener);
    }
    
    @Override
	public void dispose() {
		getSite().setSelectionProvider(null);
		getSite().getPage().removePostSelectionListener(selectionListener);
		TransactionalEditingDomain domain = TransactionUtils.getDomain(model.getEPlan());
		domain.removeResourceSetListener(modelChangeListener);
        super.dispose();
    }
    
	private void createBody(Composite parent) {
        if (useDebuggingColors) parent.setBackground(ColorConstants.cyan);
		parent.setLayout(new TableWrapLayout());
        headerComposite = buildHeader(parent);
        headerComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        addCompositeSeparator(parent);
        constraintsComposite = buildConstraintsComposite(parent);
        constraintsComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
	}
	private void addCompositeSeparator(Composite parent) {
		Composite separator = toolkit.createCompositeSeparator(parent);
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP);
		data.maxHeight = 2;
		separator.setLayoutData(data);
	}
    
	/**
     * @return Returns the form.
     */
    @Override
	public Control getControl() {
        return form;
    }
    
    /**
     * @return Returns the form.
     */
    public ScrolledForm getForm() {
        return form;
    }

    /**
     * Gives focus to the form (if it is not disposed)
     */
    @Override
	public void setFocus() {
        if ((form != null) && (!form.isDisposed())) {
            form.setFocus();
        }
    }

	/**
	 * Returns a header composite using the parent's foreground
	 * @param parent the parent composite
	 * @return a header composite using the parent's foreground
	 */
	private HeaderComposite buildHeader(Composite parent) {
		HeaderComposite composite = new HeaderComposite(parent, toolkit);
		composite.setForeground(parent.getForeground());
		return composite;
	}
	
	/**
	 * Returns a composite displaying constraint information for the selected node
	 * @param parent the parent composite
	 * @return a composite displaying constraint information for the selected node
	 */
	private Composite buildConstraintsComposite(Composite parent) {
		Composite composite = toolkit.createComposite(parent);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
		return composite;
	}
	
	/** 
	 * Checks if nodes are already set and if not calls updateComposites()
	 * @param nodes - selected PlanElements
	 */
	private void setNodes(Set<EPlanElement> nodes) {
		try {
			if ((this.nodes.size() == nodes.size()) &&
				(this.nodes.containsAll(nodes))) {
				// no change necessary
				return;
			}
			this.nodes = nodes;
			updateLater();
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("setNodes failed", t);
		}
	}
	
	/**
	 * Deletes the existing controls in the Page and creates new ones using
	 * the given nodes.
	 * @param nodes - PlanElements to be used to update page
	 */
	private void updateComposites(Set<EPlanElement> nodes) {
		trace.debug("updateComposites: ");
		headerComposite.setPlanElements(nodes);
		// get rid of the old constraints
		for (Control control : constraintsComposite.getChildren()) {
			control.dispose();
		}
		boolean selected = ((nodes != null) && (nodes.size() == 1));
		if (nodes != null) {
			boolean timeOfSolConstraints = false;  
			for (EPlanElement node : nodes) {
				for (PeriodicTemporalConstraint bound : ConstraintUtils.getPeriodicConstraints(node, Timepoint.START, false)) {
					timeOfSolConstraints = true;
					addConstraint(bound, selected);
				}
				for (PeriodicTemporalConstraint bound : ConstraintUtils.getPeriodicConstraints(node, Timepoint.END, false)) {
					timeOfSolConstraints = true;
					addConstraint(bound, selected);
				}
			}
			if (!timeOfSolConstraints) {
				addNoTimeOfSolConstraint();
			}			
			addContentSeparator();
		}
		if ((nodes != null) && !nodes.isEmpty()) {
			Set<TemporalChain> chains = TemporalChainUtils.getChains(nodes, false);
			EPlanElement origin = (nodes.size() == 1 ? nodes.iterator().next() : null);
			for (TemporalChain chain : chains) {
				addChain(chain, origin);
			}
			if (!chains.isEmpty()) {
				addContentSeparator();
			}
			List<BinaryTemporalConstraint> constraints = new ArrayList<BinaryTemporalConstraint>();
			for (EPlanElement node : nodes) {
				List<BinaryTemporalConstraint> starts = ConstraintUtils.getBinaryConstraints(node, Timepoint.START, false);
				for (BinaryTemporalConstraint relation : starts) {
					if (!constraints.contains(relation)) {
						constraints.add(relation);
					}
				}
				List<BinaryTemporalConstraint> ends = ConstraintUtils.getBinaryConstraints(node, Timepoint.END, false);
				for (BinaryTemporalConstraint relation : ends) {
					if (!constraints.contains(relation)) {
						constraints.add(relation);
					}
				}
			}
			if (constraints.isEmpty()) {
				addNoConstraints();
			} else {
				for (BinaryTemporalConstraint constraint : constraints) {
					addConstraint(constraint, origin);
				}
			}
		}
        form.layout(true, true);
		form.reflow(true);
	}
	
	private void addNoTimeOfSolConstraint() {
		toolkit.createLabel(constraintsComposite, "");
		toolkit.createLabel(constraintsComposite, "No time-of-" + MissionCalendarUtils.getMissionDayName() + " constraint.");
		toolkit.createLabel(constraintsComposite, "");
	}

	private void addNoConstraints() {
		toolkit.createLabel(constraintsComposite, "");
		toolkit.createLabel(constraintsComposite, "No other temporal constraints.");
		toolkit.createLabel(constraintsComposite, "");
	}

	private void addContentSeparator() {
		toolkit.createLabel(constraintsComposite, "");
		Label separator = toolkit.createSeparator(constraintsComposite, SWT.HORIZONTAL);
		separator.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 1));
		toolkit.createLabel(constraintsComposite, "");
	}
	
	private void addChain(TemporalChain chain, EPlanElement origin) {
		Label label = toolkit.createLabel(constraintsComposite, "");
		label.setImage(CHAIN_IMAGE);
		label.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.MIDDLE));
		FormText text = buildChainText(chain, origin, constraintsComposite);
		TableWrapData data = new TableWrapData(TableWrapData.FILL);
		text.setLayoutData(data);
		toolkit.createLabel(constraintsComposite, "");
	}
	
	private void addConstraint(PeriodicTemporalConstraint bound, boolean selected) {
		Button button = buildDeleteButton(bound, constraintsComposite);
		button.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.MIDDLE));
		FormText text = buildConstraintText(bound, constraintsComposite, selected);
		TableWrapData data = new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE);
		text.setLayoutData(data);
		toolkit.createLabel(constraintsComposite, "");
	}
	
	private void addConstraint(BinaryTemporalConstraint constraint, EPlanElement origin) {
		Button button = buildDeleteButton(constraint, constraintsComposite);
		button.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.MIDDLE));
		FormText text = buildConstraintText(constraint, origin, constraintsComposite);
		text.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
		Button button2 = buildEditButton(constraint, constraintsComposite);
		button2.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.MIDDLE));
	}
	
	private Button buildDeleteButton(final Object constraint, Composite parent) {
		boolean editable = isConstraintEditable(constraint); 
		Button deleteButton = new Button(parent, SWT.PUSH);
		deleteButton.setEnabled(editable);
		deleteButton.setImage(DELETE_IMAGE);
		deleteButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				deleteConstraint(constraint);
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteConstraint(constraint);
			}
		});
		return deleteButton;
	}
	
	private void deleteConstraint(Object constraint) {
		IUndoableOperation operation = null;
		if (constraint instanceof BinaryTemporalConstraint) {
			operation = new DeleteTemporalRelationOperation((BinaryTemporalConstraint)constraint);
		} else if (constraint instanceof PeriodicTemporalConstraint) {
			operation = new DeleteTemporalBoundOperation((PeriodicTemporalConstraint)constraint);
		}
		if (operation == null) {
			trace.warn("unexpected type of constraint: " + constraint.getClass());
			return;
		}
		IUndoContext undoContext = model.getUndoContext();
		WidgetUtils.execute(operation, undoContext, getControl(), getSite());
	}

	private Button buildEditButton(final BinaryTemporalConstraint constraint, Composite parent) {
		Button editButton = toolkit.createButton(parent, null, SWT.PUSH);
		editButton.setText("Edit");
		editButton.setEnabled(isConstraintEditable(constraint));
		editButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				editConstraint(constraint);
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				editConstraint(constraint);
			}
		});
		return editButton;
	}

	private void editConstraint(BinaryTemporalConstraint constraint) {
		IUndoContext undoContext = model.getUndoContext();
		ConstraintDialog dialog = new ConstraintDialog(getSite().getShell(), undoContext, constraint);
		dialog.open();
	}
	
	private FormText buildChainText(TemporalChain chain, EPlanElement origin, Composite parent) {
		FormText text = constraintPrinter.createConstraintText(toolkit, parent);
		// display the english
		String string = "<form><p>";
		string += constraintPrinter.getText(chain, origin);
		string += "</p></form>";
		text.setText(string, true, false);
		trace.debug("  : " + string);
		text.addHyperlinkListener(new TemporalNodeHyperlinkListener(selectionProvider, model.getEPlan(), identifiableRegistry));
		return text;
	}
	
	private FormText buildConstraintText(PeriodicTemporalConstraint bound, Composite parent, boolean selected) {
		FormText text = constraintPrinter.createConstraintText(toolkit, parent);
		// display the english
		String string = "<form><p>";
		string += constraintPrinter.getText(bound, selected);
		string += "</p></form>";
		text.setText(string, true, false);
		trace.debug("  : " + string);
		// activate the links for hover/clicking
		text.addHyperlinkListener(new TemporalNodeHyperlinkListener(selectionProvider, model.getEPlan(), identifiableRegistry));
		return text;
	}
	
	private FormText buildConstraintText(BinaryTemporalConstraint constraint, EPlanElement origin, Composite parent) {
		FormText text = constraintPrinter.createConstraintText(toolkit, parent);
		// display the english
		String string = "<form><p>";
		string += constraintPrinter.getText(constraint, origin);
		String rationale = constraint.getRationale();
		if ((rationale != null) && (rationale.trim().length() != 0)) {
			string += "</p><p>" + constraintPrinter.getRationaleText(constraint);
		}
		string += "</p></form>";
		text.setText(string, true, false);
		trace.debug("  : " + string);
		// activate the links for hover/clicking
		text.addHyperlinkListener(new TemporalNodeHyperlinkListener(selectionProvider, model.getEPlan(), identifiableRegistry));
		return text;
	}

    private Runnable pendingRunnable = null;
    
    private void updateLater() {
    	if (pendingRunnable == null) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					if (pendingRunnable == this) {
						synchronized (ConstraintsPage.this) {
							if (pendingRunnable == this) {
								pendingRunnable = null;
							}
						}
						if (pendingRunnable == null) {
							updateComposites(nodes);
						}
					}
				}
			};
			synchronized (ConstraintsPage.this) {
				pendingRunnable = runnable;
				WidgetUtils.runLaterInDisplayThread(getControl(), runnable);
			}
    	}
	}

	private boolean isConstraintEditable(Object constraint) {
		IPlanEditApprover r = PlanEditApproverRegistry.getInstance();
		if (constraint instanceof BinaryTemporalConstraint) {
			BinaryTemporalConstraint c = (BinaryTemporalConstraint) constraint;
			if (r.canModify(c.getPointA().getElement())
				&& r.canModify(c.getPointB().getElement())) {
				return true;
			}
		} else if (constraint instanceof PeriodicTemporalConstraint) {
			PeriodicTemporalConstraint c = (PeriodicTemporalConstraint) constraint;
			if (r.canModify(c.getPoint().getElement())) {
				return true;
			}
		}
		return false;
	}

	private class SelectionChangedListener implements ISelectionListener {
        @Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        	if ((selection != null) && (selection instanceof IStructuredSelection)) {
	        	try {
	        		setNodes(new HashSet<EPlanElement>(PlanEditorUtil.emfFromSelection(selection)));
	        	} catch (Throwable t) {
	    			if (t instanceof ThreadDeath) {
	    				throw (ThreadDeath)t;
	    			}
	        		trace.error("ConstraintsPage.updateSelection", t);
	        	}
        	}
        }
    }

	public class ConstraintsCommitListener extends PostCommitListener {
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			boolean needsUpdate = false;
			notifications: for (Notification notification : event.getNotifications()) {
				Object feature = notification.getFeature();
				if (feature == PlanPackage.Literals.EPLAN_ELEMENT__NAME) {
					EPlanElement notifier = (EPlanElement)notification.getNotifier();
					if (nodes.contains(notifier)) {
						needsUpdate = true;
						break notifications;
					}
				} else if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS) {
					List<BinaryTemporalConstraint> constraints = new ArrayList<BinaryTemporalConstraint>();
					constraints.addAll(EMFUtils.getRemovedObjects(notification, BinaryTemporalConstraint.class));
					constraints.addAll(EMFUtils.getAddedObjects(notification, BinaryTemporalConstraint.class));
					for (BinaryTemporalConstraint constraint : constraints) {
						if (nodes.contains(constraint.getPointA().getElement())
							|| nodes.contains(constraint.getPointB().getElement())) {
							needsUpdate = true;
							break notifications;
						}
					}
				} else if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS) {
					List<PeriodicTemporalConstraint> constraints = new ArrayList<PeriodicTemporalConstraint>();
					constraints.addAll(EMFUtils.getRemovedObjects(notification, PeriodicTemporalConstraint.class));
					constraints.addAll(EMFUtils.getAddedObjects(notification, PeriodicTemporalConstraint.class));
					for (PeriodicTemporalConstraint constraint : constraints) {
						if (nodes.contains(constraint.getPoint().getElement())) {
							needsUpdate = true;
							break notifications;
						}
					}
				} else if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN) {
					TemporalChain oldChain = (TemporalChain)notification.getOldValue();
					TemporalChain newChain = (TemporalChain)notification.getNewValue();
					List<EPlanElement> elements = new ArrayList<EPlanElement>();
					if (oldChain != null) {
						elements.addAll(oldChain.getElements());
					}
					if (newChain != null) {
						elements.addAll(newChain.getElements());
					}
					for (EPlanElement element : elements) {
						if (nodes.contains(element)) {
							needsUpdate = true;
							break notifications;
						}
					}
				}
	//    		if (PlanEditApproverRegistry.getInstance().needsUpdate(event)) {
	//    			needsUpdate = true;
	//    		}
			}
			if (needsUpdate) {
				updateLater();
			}
		}
	}

}
