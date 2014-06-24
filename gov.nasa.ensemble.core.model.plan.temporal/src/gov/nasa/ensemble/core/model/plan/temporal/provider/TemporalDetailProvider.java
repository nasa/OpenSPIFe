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
package gov.nasa.ensemble.core.model.plan.temporal.provider;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.provider.AbstractMemberDuplexingObservableValue;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.util.TemporalMemberUtils;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class TemporalDetailProvider extends DetailProvider {

	private static final EAttribute CALCULATED_VARIABLE_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__CALCULATED_VARIABLE;
	private static final Image EDITABLE_IMAGE = getImage("editable.png");
	private static final Image CALCULATED_IMAGE = getImage("calculated.png");
	private static final boolean USE_CDATE_TIME = EnsembleProperties.getBooleanPropertyValue("detail.cdatetime", false);
	
	@Override
	public boolean canCreateBindings(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		IItemPropertyDescriptor descriptor = parameter.getPropertyDescriptor();
		Object feature = descriptor.getFeature(target);
		if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_AMOUNT
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_AMOUNT
			) {
			return true;
		}
		return false;
	}
	
	private static Image getImage(String string) {
		Object image = TemporalPlanningEditPlugin.INSTANCE.getImage(string);
		return ExtendedImageRegistry.INSTANCE.getImage(image);
	}

	@Override
	public void createBinding(DetailProviderParameter parameter) {
		final List<TemporalMember> members;
		final EObject target = parameter.getTarget();
		if (target instanceof MultiEObject) {
			MultiEObject multiEObject = (MultiEObject) target;
			members = new ArrayList<TemporalMember>();
			Collection<? extends EObject> multiObjects = multiEObject.getEObjects();
			for (EObject object : multiObjects) {
				EPlanElement element = (EPlanElement)object;
				TemporalMember temporalMember = element.getMember(TemporalMember.class);
				members.add(temporalMember);
			}
		} else {
			TemporalMember temporalMember = (TemporalMember)target;
			members = Collections.singletonList(temporalMember);
		}
		IItemPropertyDescriptor descriptor = parameter.getPropertyDescriptor();
		Object feature = descriptor.getFeature(target);
		if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME) {
			FormToolkit toolkit = parameter.getDetailFormToolkit();
			Composite parent = parameter.getParent();
			EMFDetailUtils.createLabel(parent, toolkit, target, descriptor);
			DataBindingContext dbc = parameter.getDataBindingContext();
			final boolean approved = isApproved(members);
			boolean showButton = shouldShowButton(members, feature) && approved;
			Composite composite = parent;
			if (showButton) {
				composite = createTextButtonComposite(toolkit, parent);
			}
			Binding binding = null;
			if (USE_CDATE_TIME) {
				if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION) {
					binding = EMFDetailUtils.DURATION_BINDING_FACTORY.createBinding(composite, false, parameter);
				} else {
					binding = EMFDetailUtils.DATE_TIME_BINDING_FACTORY.createBinding(composite, false, parameter);
				}
			} else {
				binding = EMFDetailUtils.TEXT_BINDING_FACTORY.createBinding(composite, false, parameter);
			}
			final Control control = (Control)((ISWTObservable)binding.getTarget()).getWidget();
			if (anyUsingChildren(members) || hasSubActivities(members)) {
				boolean enablement = (feature != TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION);
				control.setEnabled(enablement && !hasSubActivities(members));
			} else {
				bindEditabilityAndCalculatedVariable(dbc, target, feature, control);
			}
			if (!approved)
				control.setEnabled(false);
			// calculated variable button should set calculated variable
			if (showButton) {
				addToggleEditabilityButton(target, feature, members, toolkit, dbc, composite);
			}		
		} else {
			for (TemporalMember member : members) {
				if (!member.isUseParentTimes() || member.getPlanElement().eContainer() instanceof EPlan) {
					return;
				}
			}
			if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT
					|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT) {
				EMFDetailUtils.COMBO_BINDING_FACTORY.createBinding(parameter);
	
			} else if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_AMOUNT 
					|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_AMOUNT) {
				EMFDetailUtils.TEXT_BINDING_FACTORY.createBinding(parameter);
			}
		}
	}
	
	private static boolean isApproved(List<TemporalMember> members) {
		for (TemporalMember tm : members)
			if (!EPlanUtils.canEdit(tm))
				return false;
		
		return true;
	}
	
	private boolean hasSubActivities(List<TemporalMember> members) {
		for (TemporalMember member : members) {
			final EObject container = member.eContainer();
			if (container instanceof EActivity) {
				if (((EActivity) container).isIsSubActivity())
					return true;
			}
		}
		return false;
	}

	private boolean shouldShowButton(List<TemporalMember> members, Object feature) {
		if (anyUsingChildren(members)) {
			return false;
		}
		if (hasSubActivities(members)) {
			return false;
		}
		if (feature != TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION) {
			return true;
		}
		for (TemporalMember member : members) {
			if (TemporalMemberUtils.hasDurationFormula(member)) {
				return false;
			}
		}
		return true;
	}

	private boolean anyUsingChildren(List<TemporalMember> members) {
		for (TemporalMember member : members) {
			if (member.isUseChildTimes()) {
				EPlanElement element = member.getPlanElement();
				if ((element != null) && !element.getChildren().isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void addToggleEditabilityButton(final EObject target, Object feature, final List<TemporalMember> members, FormToolkit toolkit, DataBindingContext dbc, Composite composite) {
		boolean isEditable = EMFUtils.isEditable(target);
		Button button = toolkit.createButton(composite, null, SWT.PUSH);
		button.setImage(EDITABLE_IMAGE);
		button.setEnabled(isEditable);
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		final CalculatedVariable calculatedVariable = getCalculatedVariable(feature);
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (TemporalMember member : members) {
					setCalculatedVariable(member, calculatedVariable);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				for (TemporalMember member : members) {
					setCalculatedVariable(member, calculatedVariable);
				}
			}
		});
		IObservableValue modelObservableValue = observeCalculatedVariable(target);
		UpdateValueStrategy calculatedVariableToImage = new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				if (value instanceof Set) {
					if (((Set) value).contains(calculatedVariable)) {
						return CALCULATED_IMAGE;
					}
					return EDITABLE_IMAGE;
				}
				if (value == calculatedVariable) {
					return CALCULATED_IMAGE;
				}
				return EDITABLE_IMAGE;
			}
		};
		dbc.bindValue(SWTObservables.observeImage(button), modelObservableValue, null, calculatedVariableToImage);
	}

	private IObservableValue observeCalculatedVariable(final EObject target) {
		if (!(target instanceof MultiEObject)) {
			return EMFObservables.observeValue(target, CALCULATED_VARIABLE_FEATURE);
		}
		MultiEObject multiEObject = (MultiEObject) target;
		@SuppressWarnings("unchecked")
		List<EPlanElement> elements = (List<EPlanElement>)multiEObject.getEObjects();
		EStructuralFeature type = CALCULATED_VARIABLE_FEATURE;
		return new AbstractMemberDuplexingObservableValue(elements, TemporalMember.class, type, type) {
			@Override
			protected Object coalesceElements(Collection elements) {
				Set<CalculatedVariable> result = new LinkedHashSet<CalculatedVariable>();
				for (Object o : elements) {
					IObservableValue v = (IObservableValue) o;
					CalculatedVariable value = (CalculatedVariable) v.getValue();
					result.add(value);
				}
				return result;
			}
		};
	}

	private Composite createTextButtonComposite(FormToolkit toolkit, Composite parent) {
		Composite composite = toolkit.createComposite(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gridData);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		return composite;
	}
	
	private void bindEditabilityAndCalculatedVariable(DataBindingContext dbc, EObject target, Object feature, Control control) {
		ISWTObservableValue textEditability = SWTObservables.observeEnabled(control);
		IObservableValue calculatedVariableValue = observeCalculatedVariable(target);
		final CalculatedVariable calculatedVariable = getCalculatedVariable(feature);
		UpdateValueStrategy calculatedVariableToEditability = new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				if (value instanceof Set) {
					return !(((Set) value).contains(calculatedVariable));
				}
				if (value == calculatedVariable) {
					return false;
				}
				return true;
			}
		};
		Binding editabilityBinding = dbc.bindValue(textEditability, calculatedVariableValue, null, calculatedVariableToEditability);
		dbc.addBinding(editabilityBinding);
	}

	private void setCalculatedVariable(TemporalMember target, CalculatedVariable calculatedVariable) {
		Object oldValue = target.eGet(CALCULATED_VARIABLE_FEATURE);
		if (oldValue == calculatedVariable) {
			if (oldValue != CalculatedVariable.END) {
				calculatedVariable = CalculatedVariable.END;
			} else if (TemporalMemberUtils.hasDurationFormula(target)) {
				calculatedVariable = CalculatedVariable.START;
			} else {
				calculatedVariable = CalculatedVariable.DURATION;
			}
		}
		IUndoableOperation operation = new FeatureTransactionChangeOperation("Set calculated variable", target, CALCULATED_VARIABLE_FEATURE, oldValue, calculatedVariable);
		operation.addContext(EMFUtils.getUndoContext(target));
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		try {
			history.execute(operation, null, null);
		} catch (ExecutionException e) {
			LogUtil.error("failed to set calculated variable", e);
		}
	}
	
	private CalculatedVariable getCalculatedVariable(Object feature) {
		if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME) {
			return CalculatedVariable.START;
		}
		if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION) {
			return CalculatedVariable.DURATION;
		}
		if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME) {
			return CalculatedVariable.END;
		}
		throw new IllegalArgumentException();
	}
	
}
