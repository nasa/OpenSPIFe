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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.type.editor.StringTypeEditor;
import gov.nasa.ensemble.core.jscience.util.DurationStringifier;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.constraints.operations.ChangeTemporalRelationOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.CreateTemporalRelationOperation;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jscience.physics.amount.Amount;

public class ConstraintDialog extends org.eclipse.jface.dialogs.Dialog {

	private static final Logger trace = Logger.getLogger(ConstraintDialog.class);

	private static final Amount<Duration> ZERO = Amount.valueOf(0, SI.SECOND);
	private static final Amount<Duration> DEFAULT_ATLEAST_DELTA_TIME = Amount.valueOf(1, NonSI.HOUR);
	private static final Amount<Duration> DEFAULT_EXACTLY_DELTA_TIME = Amount.valueOf(1, NonSI.HOUR);
	private static final Amount<Duration> DEFAULT_BETWEEN_MIN_DELTA_TIME = Amount.valueOf(1, NonSI.HOUR);
	private static final Amount<Duration> DEFAULT_BETWEEN_MAX_DELTA_TIME = Amount.valueOf(2, NonSI.HOUR);
	private static final Image CREATE_CONSTRAINT_IMAGE = Activator.createIcon("create_constraint_icon.gif");
	private static final int SWAP_ID = IDialogConstants.CLIENT_ID;
	
	private final IUndoContext undoContext;
	private final BinaryTemporalConstraint oldConstraint;
	
	private EPlanElement elementA;
	private EPlanElement elementB;

	private Map<String, EAttribute> displayNameToAttributeA = new HashMap<String, EAttribute>();
	private Map<String, EAttribute> displayNameToAttributeB = new HashMap<String, EAttribute>();
	
	/**
	 * Create a constraint dialog that will create a new constraint between the two plan elements.
	 * The dialog is set up so that it is possible to constrain A to be before, but not vice versa. 
	 * 
	 * @param parent
	 * @param undoContext
	 * @param elementA
	 * @param elementB
	 */
	public ConstraintDialog(Shell parent, IUndoContext undoContext, EPlanElement elementA, EPlanElement elementB) {
		super(parent);
		setShellStyle(SWT.DIALOG_TRIM | getDefaultOrientation());
		setBlockOnOpen(false);
		if (undoContext == null) {
			throw new NullPointerException("null undo context");
		}
		if (elementA == null) {
			throw new NullPointerException("null elementA");
		}
		if (elementB == null) {
			throw new NullPointerException("null elementB");
		}
		this.undoContext = undoContext;
		this.elementA = elementA;
		this.elementB = elementB;
		this.oldConstraint = null;
	}
	
	/**
	 * Create a constraint dialog that will edit an existing constraint. (remove it and add a new one)
	 * The constraint should be ordered as if it were constructed by this dialog, or the results
	 * may be odd.  For example, if the constraint is "the start of A must occur after the end of B",
	 * then this constraint will probably be displayed as a weird-looking 'between' option in dialog.  
	 * 
	 * @param parent
	 * @param undoContext
	 * @param oldConstraint
	 */
	public ConstraintDialog(Shell parent, IUndoContext undoContext, BinaryTemporalConstraint oldConstraint) {
		super(parent);
		setShellStyle(SWT.DIALOG_TRIM | getDefaultOrientation());
		setBlockOnOpen(false);
		if (undoContext == null) {
			throw new NullPointerException("null undo context");
		}
		if (oldConstraint.getPointA().getElement() == null) {
			throw new NullPointerException("null elementA on oldConstraint");
		}
		if (oldConstraint.getPointB().getElement() == null) {
			throw new NullPointerException("null elementB on oldConstraint");
		}
		this.undoContext = undoContext;
		this.elementA = oldConstraint.getPointA().getElement();
		this.elementB = oldConstraint.getPointB().getElement();
		this.oldConstraint = oldConstraint;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Create constraint");
		newShell.setImage(CREATE_CONSTRAINT_IMAGE);
		super.configureShell(newShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.fill = true;
		composite.setLayout(layout);
		createRowForPlanElementA(composite);
		createOptions(composite);
		createRowForPlanElementB(composite);
		createRationaleRow(composite);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, SWAP_ID, "&Swap order", false);
		super.createButtonsForButtonBar(parent);
	}
	
	/**
	 * {iconA} {nameA} must [comboTimepointA:start/end] ...
	 */
	private Label iconA;
	private Label nameA;
	private Combo comboTimepointA;
	private void createRowForPlanElementA(Composite parent) {
		Composite row = createRow(parent);
		iconA = createLabel(row, null); 
		nameA = createLabel(row, "");
		updateLabelsFromElement(iconA, nameA, elementA);
		comboTimepointA = new Combo(row, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboTimepointA.add("Start Time");
		comboTimepointA.add("End Time");
		List<String> anchorPoints = getAnchorPoints(elementA, displayNameToAttributeA);
		for (String anchorPoint : anchorPoints) {
			comboTimepointA.add(anchorPoint);
		}
		if (oldConstraint == null) {
			comboTimepointA.select(1);
		} else {
			Object anchorElementA = oldConstraint.getPointA().getAnchorElement();
			if (Timepoint.START == anchorElementA) {
				comboTimepointA.select(0);
			} else if (Timepoint.END == anchorElementA) {
				comboTimepointA.select(1);
			} else {
				EObject data = elementA.getData();
				if (data != null) {
					EStructuralFeature eStructuralFeature = data.eClass().getEStructuralFeature((String) anchorElementA);
					String displayName = ParameterDescriptor.getInstance().getDisplayName(eStructuralFeature);
					int indexA = anchorPoints.indexOf(displayName);
					if (indexA != -1) {
						comboTimepointA.select(indexA+2);
					}
				}
			}
		}
		createLabel(row, "must occur...");
	}

	/**
	 * (any time) 
	 * (immediately before)
	 * (exactly)
	 * (between)
	 */
	private Button anyTimeBeforeRadioButton;
	private Button immediatelyBeforeRadioButton;
	private Button exactlyRadioButton;
	private Button atleastRadioButton;
	private Button betweenRadioButton;
	private void createOptions(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 24;
		composite.setLayout(gridLayout);
		anyTimeBeforeRadioButton = new Button(composite, SWT.RADIO);
		createAnyTimeBeforeRow(composite);
		immediatelyBeforeRadioButton = new Button(composite, SWT.RADIO);
		createImmediatelyBeforeRow(composite);
		atleastRadioButton = new Button(composite, SWT.RADIO);
		createAtleastBeforeRow(composite);
		exactlyRadioButton = new Button(composite, SWT.RADIO);
		createExactlyBeforeRow(composite);
		betweenRadioButton = new Button(composite, SWT.RADIO);
		createBetweenRow(composite);
		if (oldConstraint == null) {
			anyTimeBeforeRadioButton.setSelection(true);
		} else {
			Amount<Duration> minimum = oldConstraint.getMinimumBminusA();
			Amount<Duration> maximum = oldConstraint.getMaximumBminusA();
			if ((maximum == null) && (minimum != null) && (minimum.compareTo(ZERO) == 0)) {
				anyTimeBeforeRadioButton.setSelection(true);
			} else if ((minimum != null) && (minimum.compareTo(ZERO) == 0)
					&& (maximum != null) && (maximum.compareTo(ZERO) == 0)) {
				immediatelyBeforeRadioButton.setSelection(true);
			} else if (maximum == null) {
				atleastRadioButton.setSelection(true);
			} else if (minimum == null) {
				// weird
				anyTimeBeforeRadioButton.setSelection(true);
			} else if (minimum.compareTo(maximum) == 0) {
				exactlyRadioButton.setSelection(true);
			} else {
				betweenRadioButton.setSelection(true);
			}
		}
	}

	/**
	 * anytime before
	 * @param parent
	 */
	private void createAnyTimeBeforeRow(Composite parent) {
		Composite row = createRow(parent);
		createLabel(row, "anytime before"); 
	}
	
	/**
	 * immediately before
	 * @param parent
	 */
	private void createImmediatelyBeforeRow(Composite parent) {
		Composite row = createRow(parent);
		createLabel(row, "immediately before"); 
	}
	
	/**
	 * exactly [exactlyTime] [exactlyCombo1:before/after]
	 */
	private StringTypeEditor atleastTime;
//	private Combo exactlyCombo1;
	private void createAtleastBeforeRow(Composite parent) {
		Composite row = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 3;
		row.setLayout(layout);
		Label atleastLabel = createLabel(row, "at least");
		atleastLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		Amount<Duration> delta = DEFAULT_ATLEAST_DELTA_TIME;
		if (oldConstraint != null) {
			delta = oldConstraint.getMinimumBminusA();
		}
		atleastTime = createDurationText(row, delta);
		atleastTime.getEditorControl().addModifyListener(new SelectButtonModifyListener(atleastRadioButton));
		createLabel(row, "before");
//		atleastCombo1 = createDurationCombo(row, delta);
//		atleastCombo1.addModifyListener(new SelectButtonModifyListener(atleastRadioButton));
	}

	/**
	 * exactly [exactlyTime] [exactlyCombo1:before/after]
	 */
	private StringTypeEditor exactlyTime;
	private Combo exactlyCombo1;
	private void createExactlyBeforeRow(Composite parent) {
		Composite row = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 3;
		row.setLayout(layout);
		Label exactlyLabel = createLabel(row, "exactly");
		exactlyLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		Amount<Duration> delta = DEFAULT_EXACTLY_DELTA_TIME;
		if (oldConstraint != null) {
			Amount<Duration> minimum = oldConstraint.getMinimumBminusA();
			Amount<Duration> maximum = oldConstraint.getMaximumBminusA();
			if ((minimum != null) && (maximum != null) && (minimum.compareTo(maximum) == 0)) {
				delta = minimum;
			}
		}
		exactlyTime = createDurationText(row, delta);
		exactlyTime.getEditorControl().addModifyListener(new SelectButtonModifyListener(exactlyRadioButton));
		exactlyCombo1 = createDurationCombo(row, delta);
		exactlyCombo1.addModifyListener(new SelectButtonModifyListener(exactlyRadioButton));
	}

	/**
	 * between [betweenTime1] [betweenCombo1:before/after] and [betweenTime2] [betweenCombo2:before/after]
	 */
	private StringTypeEditor betweenTime1;
	private Combo betweenCombo1;
	private StringTypeEditor betweenTime2;
	private Combo betweenCombo2;
	private void createBetweenRow(Composite parent) {
		Composite betweenComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 3;
		betweenComposite.setLayout(layout);
		Label betweenLabel = createLabel(betweenComposite, "between"); 
		betweenLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		Amount<Duration> delta1 = DEFAULT_BETWEEN_MIN_DELTA_TIME;
		if (oldConstraint != null) {
			Amount<Duration> minimum = oldConstraint.getMinimumBminusA();
			if (minimum != null) {
				delta1 = minimum;
			}
		}
		Amount<Duration> delta2 = DEFAULT_BETWEEN_MAX_DELTA_TIME;
		if (oldConstraint != null) {
			Amount<Duration> maximum = oldConstraint.getMaximumBminusA();
			if (maximum != null) {
				delta2 = maximum;
			}
		}
		
		// before and after
		// list the "before" value, *then* the "after" value
		if (delta1 != null && delta2 != null && delta1.isLessThan(ZERO) && !delta2.isLessThan(ZERO)) {
			Amount<Duration> oldDelta1 = delta1;
			delta1 = delta2;
			delta2 = oldDelta1;
		}
		
		betweenTime1 = createDurationText(betweenComposite, delta1);
		betweenTime1.getEditorControl().addModifyListener(new SelectButtonModifyListener(betweenRadioButton));
		betweenCombo1 = createDurationCombo(betweenComposite, delta1);
		betweenCombo1.addModifyListener(new SelectButtonModifyListener(betweenRadioButton));
		Label andLabel = createLabel(betweenComposite, "and");
		andLabel.setAlignment(SWT.RIGHT);
		andLabel.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		betweenTime2 = createDurationText(betweenComposite, delta2);
		betweenTime2.getEditorControl().addModifyListener(new SelectButtonModifyListener(betweenRadioButton));
		betweenCombo2 = createDurationCombo(betweenComposite, delta2);
		betweenCombo2.addModifyListener(new SelectButtonModifyListener(betweenRadioButton));
	}

	private Label iconB;
	private Label nameB;
	private Combo comboTimepointB;
	private void createRowForPlanElementB(Composite parent) {
		Composite row = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		row.setLayout(layout);
		iconB = createLabel(row, null); 
		nameB = createLabel(row, "");
		nameB.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		updateLabelsFromElement(iconB, nameB, elementB);
		comboTimepointB = new Combo(row, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboTimepointB.add("Start Time");
		comboTimepointB.add("End Time");
		EPlanElement pe = elementB;
		List<String> anchorPoints = getAnchorPoints(pe, displayNameToAttributeB);
		for (String anchorPoint : anchorPoints) {
			comboTimepointB.add(anchorPoint);
		}
		if (oldConstraint == null) {
			comboTimepointB.select(0);
		} else {
			Object anchorElementB = oldConstraint.getPointB().getAnchorElement();
			if (Timepoint.START == anchorElementB) {
				comboTimepointB.select(0);
			} else if (Timepoint.END == anchorElementB) {
				comboTimepointB.select(1);
			} else {
				EObject data = elementB.getData();
				if (data != null) {
					EStructuralFeature eStructuralFeature = data.eClass().getEStructuralFeature((String) anchorElementB);
					String displayName = ParameterDescriptor.getInstance().getDisplayName(eStructuralFeature);
					int indexB = anchorPoints.indexOf(displayName);
					if (indexB != -1) {
						comboTimepointB.select(indexB+2);
					}
				}
			}
		}
	}

	private Text rationaleText;
	private void createRationaleRow(Composite parent) {
		Composite row = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		row.setLayout(layout);
		createLabel(row, "Rationale:");
		rationaleText = new Text(row, SWT.SINGLE | SWT.BORDER);
		rationaleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		if ((oldConstraint != null) && (oldConstraint.getRationale() != null)) {
			rationaleText.setText(oldConstraint.getRationale());
		}
	}
	
	/*
	 * utils 
	 */
	
	private static Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		if (text != null) {
			label.setText(text);
		}
		return label;
	}

	private static void updateLabelsFromElement(Label icon, Label name, EPlanElement element) {
		icon.setImage(PlanUtils.getIcon(element));
		name.setText(element.getName());
	}

	private static DurationStringifier DURATION_STRINGIFIER = new DurationStringifier();
	private static StringTypeEditor createDurationText(Composite parent, Amount<Duration> delta) {
		return new StringTypeEditor(DURATION_STRINGIFIER, parent, delta.abs(), true);
	}
	
	private static Combo createDurationCombo(Composite parent, Amount<Duration> delta) {
		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.add("before");
		combo.add("after");
		if (delta.isLessThan(ZERO)) {
			combo.select(1);
		} else {
			combo.select(0);
		}
		return combo;
	}

	@SuppressWarnings("unchecked")
	private static Amount<Duration> getDuration(StringTypeEditor text, Combo combo) {
		Amount<Duration> duration = (Amount<Duration>)text.getObject();
		if (combo.getSelectionIndex() == 1) {
			// after, invert the duration
			duration = duration.opposite();
		}

		return duration;
	}

	private static Composite createRow(Composite parent) {
		Composite row = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.fill = true;
		row.setLayout(layout);
		return row;
	}

	/*
	 * Button row creation follows
	 */
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == SWAP_ID) {
			swap();
		} else {
			super.buttonPressed(buttonId);
		}
		
	}
	
	private void swap() {
		trace.debug("swap");
		EPlanElement swap = elementA;
		elementA = elementB;
		elementB = swap;
		updateLabelsFromElement(iconA, nameA, elementA);
		updateLabelsFromElement(iconB, nameB, elementB);
		swapComboElements();
		getShell().layout(true, true);
		getShell().pack();
	}
	
	private void swapComboElements() {
		int indexA = comboTimepointA.getSelectionIndex();
		int indexB = comboTimepointB.getSelectionIndex();
		String itemsA[] = comboTimepointA.getItems();
		String itemsB[] = comboTimepointB.getItems();
		comboTimepointA.setItems(itemsB);
		comboTimepointB.setItems(itemsA);
		comboTimepointA.select(indexB);
		comboTimepointB.select(indexA);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void okPressed() {
		// Force the individual editors to lose focus, so that the "getObject" calls returns the best results.
		this.getShell().setFocus();

		Timepoint timepointA = null;
		String anchorA = null;
		int selectionIndexA = comboTimepointA.getSelectionIndex();
		switch (selectionIndexA) {
		case 0:
			timepointA = Timepoint.START;
			break;
		case 1:
			timepointA = Timepoint.END;
			break;
		default:
			anchorA = getAnchorItem(comboTimepointA, displayNameToAttributeA);
			break;
		}
		
		Timepoint timepointB = null;
		String anchorB = null;
		int selectionIndexB = comboTimepointB.getSelectionIndex();
		switch (selectionIndexB) {
		case 0:
			timepointB = Timepoint.START;
			break;
		case 1:
			timepointB = Timepoint.END;
			break;
		default:
			anchorB = getAnchorItem(comboTimepointB, displayNameToAttributeB);
			break;
		}
		
		String debug = "create constraint between: "+ timepointA + " of " + elementA.getName() 
				     + " and " + timepointB + " of " + elementB.getName();
		trace.debug(debug);
		Amount<Duration> minimum;
		Amount<Duration> maximum;
		if (anyTimeBeforeRadioButton.getSelection()) {
			minimum = ZERO;
			maximum = null;
		} else if (immediatelyBeforeRadioButton.getSelection()) {
			minimum = ZERO;
			maximum = ZERO;
		} else if (atleastRadioButton.getSelection()) {
			Amount<Duration> duration = (Amount<Duration>)atleastTime.getObject();
			minimum = duration;
			maximum = null;
		} else if (exactlyRadioButton.getSelection()) {
			Amount<Duration> duration = getDuration(exactlyTime, exactlyCombo1);
			minimum = duration;
			maximum = duration;
		} else if (betweenRadioButton.getSelection()) {
			Amount<Duration> duration1 = getDuration(betweenTime1, betweenCombo1);
			Amount<Duration> duration2 = getDuration(betweenTime2, betweenCombo2);
			if (duration1.isLessThan(duration2)) {
				// the input is in the "natural" order
				minimum = duration1;
				maximum = duration2;
			} else {
				// the input was in the "reverse" order
				minimum = duration2;
				maximum = duration1;
			}
		} else {
			trace.error("no radio button selected in ConstraintDialog");
			return;
		}
		String rationale = rationaleText.getText();
		BinaryTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createBinaryTemporalConstraint();
		constraint.getPointA().setElement(elementA);
		constraint.getPointA().setEndpoint(timepointA);
		constraint.getPointA().setAnchor(anchorA);
		constraint.getPointB().setElement(elementB);
		constraint.getPointB().setEndpoint(timepointB);
		constraint.getPointB().setAnchor(anchorB);
		constraint.setMaximumBminusA(maximum);
		constraint.setMinimumBminusA(minimum);
		constraint.setRationale(rationale);
		createConstraint(constraint);
		super.okPressed();
	}

	private String getAnchorItem(Combo combo, Map<String, EAttribute> displayNameToAttribute) {
		int selectionIndex = combo.getSelectionIndex();
		String item = combo.getItem(selectionIndex);
		EAttribute eAttribute = displayNameToAttribute.get(item);
		if (eAttribute != null) {
			return eAttribute.getName();
		}
		return null;
	}

	private void createConstraint(BinaryTemporalConstraint constraint) {
		IUndoableOperation op;
		if (oldConstraint == null) {
			op = new CreateTemporalRelationOperation(constraint);
		} else {
			op = new ChangeTemporalRelationOperation(oldConstraint, constraint);
		}
		CommonUtils.execute(op, undoContext);
	}

	@Override
	protected void cancelPressed() {
		trace.debug("cancel");
		super.cancelPressed();
	}
	
	private List<String> getAnchorPoints(EPlanElement pe, Map<String, EAttribute> displayNameToAttribute) {
		List<String> anchorPoints = new ArrayList<String>();
		if (pe instanceof EActivity) {
			EActivityDef def = ADParameterUtils.getActivityDef((EActivity) pe);
			if (def != null) {
				for (EAttribute attribute : def.getEAllAttributes()) {
					if (ConstraintUtils.isAnchorable(attribute)) {
						if (EcorePackage.Literals.EDATE == attribute.getEType()) {
							String displayName = ParameterDescriptor.getInstance().getDisplayName(attribute);
							displayNameToAttribute.put(displayName, attribute);
							anchorPoints.add(displayName);
						}
					}
				}
			}
		}
		return anchorPoints;
	}

	/**
	 * This modify listener will select the supplied button 
	 * whenever a modification takes place. 
	 * @author Andrew
	 */
	private class SelectButtonModifyListener implements ModifyListener {
		private final Button button;
		public SelectButtonModifyListener(Button button) {
			this.button = button;
		}
		@Override
		public void modifyText(ModifyEvent e) {
			if (button.getSelection() != true) {
				anyTimeBeforeRadioButton.setSelection(false);
				immediatelyBeforeRadioButton.setSelection(false);
				atleastRadioButton.setSelection(false);
				exactlyRadioButton.setSelection(false);
				betweenRadioButton.setSelection(false);
				button.setSelection(true);
			}
		}
	}
	
}
