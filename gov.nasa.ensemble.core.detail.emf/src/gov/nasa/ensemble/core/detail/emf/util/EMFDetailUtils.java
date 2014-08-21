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
package gov.nasa.ensemble.core.detail.emf.util;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;
import fj.data.Option;
import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.detail.DetailUtils;
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.core.detail.emf.ChildrenBindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.EMFDetailFormPart;
import gov.nasa.ensemble.core.detail.emf.IBindingFactory;
import gov.nasa.ensemble.core.detail.emf.IDetailProvider;
import gov.nasa.ensemble.core.detail.emf.binding.BooleanBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.CDateTimeBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.ComboBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.DurationBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.IPathBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.MultiSelectBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.RGBBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.SatisfactionUpdateValueStrategy;
import gov.nasa.ensemble.core.detail.emf.binding.Selector;
import gov.nasa.ensemble.core.detail.emf.binding.SelectorObservableValue;
import gov.nasa.ensemble.core.detail.emf.binding.TableBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.TextBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.TextModifyUndoableObservableValue;
import gov.nasa.ensemble.core.detail.emf.binding.URIBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.URLBindingFactory;
import gov.nasa.ensemble.core.detail.emf.binding.UndoableObservableValue;
import gov.nasa.ensemble.core.detail.emf.binding.UniqueMultiSelectBindingFactory;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.emf.model.common.ObjectFeature;
import gov.nasa.ensemble.emf.provider.CommandDelegate;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor.OverrideableCommandOwner;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.ILiveValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.emf.validation.util.FilteredCollection;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class EMFDetailUtils {

	/**
	 * The annotation source key in order to configure and extend detail for capabilities
	 */
	public static final String ANNOTATION_SOURCE_DETAIL = "detail";

	/**
	 * Short description that appears to the right of the EStructuralFeatureEditor. This can be simple things like units, or display formats. Results in the loss of editor width due to extra space
	 * being taken up by the description.
	 */
	public static final String ANNOTATION_DETAIL_SHORT_DESCRIPTION = "shortDescription";

	public static final String ANNOTATION_DETAIL_DISPLAY_NAME = "displayName";

	public static final String ANNOTATION_DETAIL_EDITABLE = "editable";

	public static final String UNCATEGORIZED = "Uncategoried";

	private static final List<String> HIDDEN_CATEGORY_HEADERS = EnsembleProperties.getStringListPropertyValue("detail.category.header.hide", Collections.<String> emptyList());

	/** Binding factory constant for text editing */
	public static final TextBindingFactory TEXT_BINDING_FACTORY = new TextBindingFactory();

	/** Binding factory constant for boolean editing */
	public static final BooleanBindingFactory BOOLEAN_BINDING_FACTORY = new BooleanBindingFactory();

	public static final IPathBindingFactory IPATH_BINDING_FACTORY = new IPathBindingFactory();

	public static final RGBBindingFactory RGB_BINDING_FACTORY = new RGBBindingFactory();

	/** Binding factory constant for choice editing */
	public static final ComboBindingFactory COMBO_BINDING_FACTORY = new ComboBindingFactory(parseBoolean(getProperty("ensemble.detail.useTypableCombo", "true")));

	/** Binding factory for URL types */
	public static final URIBindingFactory URI_BINDING_FACTORY = new URIBindingFactory();

	public static final URLBindingFactory URL_BINDING_FACTORY = new URLBindingFactory();

	public static final MultiSelectBindingFactory MULTI_SELECT_BINDING_FACTORY = new MultiSelectBindingFactory();

	public static final UniqueMultiSelectBindingFactory UNIQUE_MULTI_SELECT_BINDING_FACTORY = new UniqueMultiSelectBindingFactory();

	public static final TableBindingFactory TABLE_BINDING_FACTORY = new TableBindingFactory();

	public static final CDateTimeBindingFactory DATE_TIME_BINDING_FACTORY = new CDateTimeBindingFactory(false);

	public static final CDateTimeBindingFactory TIME_BINDING_FACTORY = new CDateTimeBindingFactory(true);

	public static final DurationBindingFactory DURATION_BINDING_FACTORY = new DurationBindingFactory();

	private static final Map<EObject, Map<EStructuralFeature, List<Control>>> controlMap = new HashMap<EObject, Map<EStructuralFeature, List<Control>>>();
	private static final Map<Control, ValidationControlDecoration> decorationMap = new HashMap<Control, ValidationControlDecoration>();

	public static LinkedHashMap<String, List<IItemPropertyDescriptor>> groupByCategory(Object target, List<IItemPropertyDescriptor> pds) {
		LinkedHashMap<String, List<IItemPropertyDescriptor>> map = new LinkedHashMap<String, List<IItemPropertyDescriptor>>();
		for (IItemPropertyDescriptor pd : pds) {
			String c = pd.getCategory(target);
			if (c == null) {
				c = UNCATEGORIZED;
			}

			List<IItemPropertyDescriptor> list = map.get(c);
			if (list == null) {
				list = new ArrayList<IItemPropertyDescriptor>();
				map.put(c, list);
			}
			list.add(pd);
		}
		return map;
	}

	public static String getDisplayName(EObject target, IItemPropertyDescriptor pd) {
		String displayName = EMFUtils.getAnnotation((EStructuralFeature) pd.getFeature(target), ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_DISPLAY_NAME);
		return (displayName == null) ? pd.getDisplayName(target) : displayName;
	}

	public static Label createLabel(Composite parent, FormToolkit toolkit, EObject target, IItemPropertyDescriptor pd) {
		Label label = createLabel(parent, toolkit, getDisplayName(target, pd));

		// set filtered labels italic
		Collection<String> availableFilterFlags = DetailUtils.getAvailableFilterFlags();
		String[] filterFlags = pd.getFilterFlags(target);
		if (filterFlags != null) {
			for (String filterFlag : filterFlags) {
				if (availableFilterFlags.contains(filterFlag)) {
					FontData fontData = label.getFont().getFontData()[0];
					Font font = new Font(parent.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.ITALIC));
					label.setFont(font);
				}
			}
		}

		return addDescriptionTooltip(pd, target, label);
	}

	public static Label createLabel(Composite parent, FormToolkit toolkit, String string) {
		Label label = toolkit.createLabel(parent, string);
		label.setBackground(parent.getBackground());
		if (parent.getLayout() instanceof GridLayout) {
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		} else if (parent.getLayout() instanceof ColumnLayout) {
			ColumnLayoutData data = new ColumnLayoutData();
			data.horizontalAlignment = SWT.LEFT;
			label.setLayoutData(data);
		} else {
			Logger logger = Logger.getLogger(EMFDetailFormPart.class);
			logger.warn("unexpected layout for label");
		}
		return label;
	}

	public static Binding bindEMFUndoable(DetailProviderParameter p, IObservableValue targetObservableValue) {
		return bindEMFUndoable(p, targetObservableValue, null, null);
	}

	public static Binding bindEMFUndoable(DetailProviderParameter p, IObservableValue targetObservableValue, UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		DataBindingContext dbc = p.getDataBindingContext();
		EObject model = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		UndoableObservableValue modelObservableValue = new UndoableObservableValue(model, pd);
		return dbc.bindValue(targetObservableValue, modelObservableValue, targetToModel, modelToTarget);
	}

	public static void bindTextModifyUndoable(final Text text, EObject model, String label) {
		final TextModifyUndoableObservableValue modelObservableValue = new TextModifyUndoableObservableValue(model, label, text);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				int stateMask = event.stateMask;
				int keyCode = event.keyCode;
				char character = event.character;
				// System.out.println("State: " + stateMask + "Key: " + keyCode + "Character: " + (int)character);
				if (character != 0 && (stateMask == 0 || stateMask == (0 | SWT.SHIFT))) {
					if (keyCode == SWT.ESC || ((text.getStyle() & SWT.MULTI) == 0 && keyCode == SWT.CR)) {
						return;
					}
					modelObservableValue.textModified();
				}
			}
		});
	}

	/**
	 * Defaulting method to call bindControlViability with equalityCheck == true
	 */
	public static void bindControlViability(DataBindingContext dbc, Control control, EObject target, EStructuralFeature feature, Object value) {
		bindControlViability(dbc, control, target, feature, value, true);
	}

	/**
	 * Bind the control's enabled state and editability (for Text controls) with respect to the target object's feature value. If equalityCheck is true, the control is editable and enabled if
	 * target.eGet(feature).equals(value), otherwise if the equalityCheeck is false, then the control is editable and enabled if !target.eGet(feature).equals(value)
	 * 
	 * @param dbc
	 *            data binding context
	 * @param control
	 *            to control editability and enabled state
	 * @param target
	 *            model to observe
	 * @param feature
	 *            to observe
	 * @param value
	 *            to check for equality
	 * @param equalityCheck
	 *            true if equality implies enabled/editable
	 */
	public static void bindControlViability(DataBindingContext dbc, Control control, EObject target, EStructuralFeature feature, Object value, boolean equalityCheck) {
		bindControlViability(dbc, target, feature, value, SWTObservables.observeEnabled(control), equalityCheck);
		if (control instanceof Text) {
			bindControlViability(dbc, target, feature, value, SWTObservables.observeEditable(control), equalityCheck);
		}
	}

	private static void bindControlViability(DataBindingContext dbc, EObject target, EStructuralFeature feature, Object value, ISWTObservableValue targetObservable, boolean equalityCheck) {
		IObservableValue modelObservable = EMFObservables.observeValue(target, feature);
		UpdateValueStrategy modelToTarget = (equalityCheck) ? new SatisfactionUpdateValueStrategy.Equality(value) : new SatisfactionUpdateValueStrategy.Inequality(value);
		Binding binding = dbc.bindValue(targetObservable, modelObservable, null, modelToTarget);
		dbc.addBinding(binding);
	}

	public static void bindSelector(DetailProviderParameter parameter, Selector<?> selector) {
		Button button = selector.getButton();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		button.setEnabled(pd.canSetProperty(parameter.getTarget()));
		bindValidatorDecoration(parameter, button);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		// SPF-7921 Allow the selector button to be narrower than its preferred size to reduce the need for horizontal scrolling
		gridData.widthHint = Activator.CONTROL_WIDTH_HINT;
		gridData.minimumWidth = -1;
		button.setLayoutData(gridData);
		SelectorObservableValue targetObservableValue = new SelectorObservableValue(selector);
		DataBindingContext dbc = parameter.getDataBindingContext();
		dbc.addBinding(bindEMFUndoable(parameter, targetObservableValue, null, null));
	}

	public static void bindValidatorDecoration(DetailProviderParameter parameter, final Control control) {
		final EObject target = parameter.getTarget();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		final EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		addControl(target, feature, control);
		final IObservableValue modelObservable = EMFObservables.observeValue(target, feature);
		final IValueChangeListener listener = new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				Object value = event.getObservableValue().getValue();
				validateFeatureValue(target, feature, value, control, true);
			}
		};
		modelObservable.addValueChangeListener(listener);
		control.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				modelObservable.removeValueChangeListener(listener);
				modelObservable.dispose();
				removeControl(target, feature, control);
				control.removeDisposeListener(this);
			}
		});
		validateFeatureValue(target, feature, modelObservable.getValue(), control, false);
	}

	private static void addControl(EObject object, EStructuralFeature feature, Control control) {
		Map<EStructuralFeature, List<Control>> controlsByFeature = controlMap.get(object);
		if (controlsByFeature == null) {
			controlsByFeature = new HashMap<EStructuralFeature, List<Control>>();
			controlMap.put(object, controlsByFeature);
		}
		List<Control> controls = controlsByFeature.get(feature);
		if (controls == null) {
			controls = new ArrayList<Control>();
			controlsByFeature.put(feature, controls);
		}
		controls.add(control);
	}

	private static void removeControl(EObject object, EStructuralFeature feature, Control control) {
		Map<EStructuralFeature, List<Control>> controlsByFeature = controlMap.get(object);
		if (controlsByFeature != null) {
			List<Control> controls = controlsByFeature.get(feature);
			controls.remove(control);
			if (controls.isEmpty()) {
				controlsByFeature.remove(feature);
				if (controlsByFeature.isEmpty()) {
					controlMap.remove(object);
				}
			}
		}
	}

	private static List<Control> getControls(EObject object, EStructuralFeature feature) {
		Map<EStructuralFeature, List<Control>> controlsByFeature = controlMap.get(object);
		if (controlsByFeature != null) {
			return controlsByFeature.get(feature);
		}
		return null;
	}

	private static void validateFeatureValue(EObject target, EStructuralFeature feature, Object value, Control control, boolean showTip) {
		ILiveValidator validator = (ILiveValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.LIVE);
		// FIXME -- Workaround for copy not being attached to resource. See SPF-7060.
		validator.setNotificationFilter(new NotNecessarilyAttachedToResourceNotificationFilter());
		Notification notification = new ENotificationImpl((InternalEObject) target, Notification.SET, feature, null, value);
		IStatus status = validator.validate(notification);
		List<ValidationControlDecoration> otherDecorations = null;
		int severity = status.getSeverity();
		if (severity == IStatus.OK) {
			ValidationControlDecoration controlDecoration = getDecoration(control, false);
			if (controlDecoration != null) {
				controlDecoration.clearDisplay();
				List<ConstraintStatus> oldFailures = controlDecoration.getValidationFailures();
				for (ConstraintStatus failure : oldFailures) {
					otherDecorations = removeFailureFromOtherDecorations(failure, target, feature, otherDecorations);
				}
				controlDecoration.clearValidationFailures();
			}
		} else {
			ValidationControlDecoration controlDecoration = getDecoration(control, true);
			List<ConstraintStatus> newFailures = getStatusLeaves(status, severity);
			List<ConstraintStatus> oldFailures = controlDecoration.getValidationFailures();
			controlDecoration.setValidationFailures(newFailures);
			controlDecoration.redisplay(feature, showTip);
			for (ConstraintStatus failure : oldFailures) {
				if (!hasEquivalentValidationFailure(newFailures, failure)) {
					otherDecorations = removeFailureFromOtherDecorations(failure, target, feature, otherDecorations);
				}
			}
			for (ConstraintStatus failure : newFailures) {
				if (!hasEquivalentValidationFailure(oldFailures, failure)) {
					otherDecorations = addFailureToOtherDecorations(failure, target, feature, otherDecorations);
				}
			}
		}
		if (otherDecorations != null) {
			for (ValidationControlDecoration decoration : otherDecorations) {
				decoration.redisplay(null, false);
			}
		}
	}

	public static void reValidateFeature(EObject target, EStructuralFeature feature) {
		List<Control> controls = getControls(target, feature);
		if (controls != null && !controls.isEmpty()) {
			Object value = target.eGet(feature);
			for (Control control : controls) {
				validateFeatureValue(target, feature, value, control, false);
			}
		}
	}

	public static boolean hasErrorDecoration(Control control) {
		ValidationControlDecoration decoration = getDecoration(control, false);
		return decoration != null && decoration.determineSeverity() == IStatus.ERROR;
	}

	private static ValidationControlDecoration getDecoration(Control control, boolean create) {
		ValidationControlDecoration decoration = decorationMap.get(control);
		if (create && decoration == null) {
			decoration = new ValidationControlDecoration(control, SWT.LEFT | SWT.TOP);
			decorationMap.put(control, decoration);
		}
		return decoration;
	}

	private static List<ConstraintStatus> getStatusLeaves(IStatus status, int severity) {
		List<ConstraintStatus> leaves = new ArrayList<ConstraintStatus>();
		if (status.isMultiStatus()) {
			for (IStatus child : status.getChildren()) {
				if (child.getSeverity() == severity) {
					leaves.addAll(getStatusLeaves(child, severity));
				}
			}
		} else {
			leaves.add((ConstraintStatus) status);
		}
		return leaves;
	}

	private static boolean hasEquivalentValidationFailure(List<ConstraintStatus> failures, ConstraintStatus failure) {
		for (ConstraintStatus status : failures) {
			if (equivalentValidationFailures(status, failure)) {
				return true;
			}
		}
		return false;
	}

	private static boolean equivalentValidationFailures(ConstraintStatus failure1, ConstraintStatus failure2) {
		return (failure1.getMessage().equals(failure2.getMessage()) && equivalentFailureLoci(failure1.getResultLocus(), failure2.getResultLocus()));
	}

	private static boolean equivalentFailureLoci(Set<EObject> locus1, Set<EObject> locus2) {
		if (locus1.size() != locus2.size()) {
			return false;
		}
		for (EObject object : locus2) {
			if (object instanceof ObjectFeature && !locus1.contains(object)) {
				return false;
			}
		}
		return true;
	}

	private static List<ValidationControlDecoration> addFailureToOtherDecorations(ConstraintStatus failure, EObject target, EStructuralFeature feature, List<ValidationControlDecoration> decorations) {
		Set<EObject> failureLocus = failure.getResultLocus();
		for (EObject locusComponent : failureLocus) {
			if (locusComponent instanceof ObjectFeature) {
				ObjectFeature objectFeature = (ObjectFeature) locusComponent;
				EObject otherTarget = objectFeature.getObject();
				EStructuralFeature otherFeature = objectFeature.getFeature();
				if (otherTarget != target || otherFeature != feature) {
					List<Control> controls = getControls(otherTarget, otherFeature);
					if (controls != null)
						for (Control control : controls) {
							ValidationControlDecoration decoration = getDecoration(control, true);
							decoration.addValidationFailure(failure);
							if (decorations == null) {
								decorations = new ArrayList<ValidationControlDecoration>();
								decorations.add(decoration);
							} else if (!decorations.contains(decoration)) {
								decorations.add(decoration);
							}
						}
				}
			}
		}
		return decorations;
	}

	private static List<ValidationControlDecoration> removeFailureFromOtherDecorations(ConstraintStatus failure, EObject target, EStructuralFeature feature, List<ValidationControlDecoration> decorations) {
		Set<EObject> failureLocus = failure.getResultLocus();
		for (EObject locusComponent : failureLocus) {
			if (locusComponent instanceof ObjectFeature) {
				ObjectFeature objectFeature = (ObjectFeature) locusComponent;
				EObject otherTarget = objectFeature.getObject();
				EStructuralFeature otherFeature = objectFeature.getFeature();
				if (otherTarget != target || otherFeature != feature) {
					List<Control> controls = getControls(otherTarget, otherFeature);
					if (controls != null)
						for (Control control : controls) {
							ValidationControlDecoration decoration = getDecoration(control, false);
							if (decoration == null) {
								continue;
							}
							decoration.removeValidationFailure(failure);
							if (decorations == null) {
								decorations = new ArrayList<ValidationControlDecoration>();
								decorations.add(decoration);
							} else if (!decorations.contains(decoration)) {
								decorations.add(decoration);
							}
						}
				}
			}
		}
		return decorations;
	}

	public static <T extends Control> T addDescriptionTooltip(final IItemPropertyDescriptor pd, final EObject target, final T control) {
		Option<String> description = Option.fromString(pd.getDescription(target));
		if (description.isSome())
			control.setToolTipText(description.some());

		return control;
	}

	/**
	 * A specialization of ControlDecoration that records the list of ConstraintStatus objects that result from a failed EMF validation of the EMF object bound to the associated control
	 * 
	 * @author rnado
	 * 
	 */
	static class ValidationControlDecoration extends ControlDecoration {

		private List<ConstraintStatus> validationFailures;

		public ValidationControlDecoration(Control control, int position) {
			super(control, position);
		}

		public List<ConstraintStatus> getValidationFailures() {
			if (validationFailures == null) {
				return Collections.emptyList();
			}
			return validationFailures;
		}

		public void addValidationFailure(ConstraintStatus failure) {
			if (validationFailures == null) {
				validationFailures = new ArrayList<ConstraintStatus>();
				validationFailures.add(failure);
			} else if (!hasValidationFailure(failure)) {
				validationFailures.add(failure);
			}
		}

		public void removeValidationFailure(ConstraintStatus failure) {
			if (validationFailures != null) {
				for (ListIterator<ConstraintStatus> iter = validationFailures.listIterator(); iter.hasNext();) {
					ConstraintStatus status = (ConstraintStatus) iter.next();
					if (failure.getMessage().equals(status.getMessage()) && equivalentFailureLoci(failure.getResultLocus(), status.getResultLocus())) {
						iter.remove();
						break;
					}
				}
			}
		}

		public void clearValidationFailures() {
			if (validationFailures != null) {
				validationFailures.clear();
			}
		}

		public boolean hasValidationFailure(ConstraintStatus failure) {
			if (validationFailures == null) {
				return false;
			}
			for (ConstraintStatus status : validationFailures) {
				if (equivalentValidationFailures(status, failure))
					return true;
			}
			return false;
		}

		public void setValidationFailures(List<ConstraintStatus> validationFailures) {
			this.validationFailures = validationFailures;
		}

		public boolean isCulprit(EStructuralFeature feature) {
			if (validationFailures == null || validationFailures.isEmpty()) {
				return false;
			}
			if (feature == null) {
				return true;
			}
			boolean isCulprit = false;
			for (ConstraintStatus status : validationFailures) {
				Set<EObject> locus = status.getResultLocus();
				// the feature is implicitly a culprit if the locus only contains the target EObject
				if (locus.size() == 1 || failureLocusContainsFeature(locus, feature)) {
					isCulprit = true;
					break;
				}
			}
			return isCulprit;
		}

		private boolean failureLocusContainsFeature(Set<EObject> failureLocus, EStructuralFeature feature) {
			for (EObject locusComponent : failureLocus) {
				if (locusComponent instanceof ObjectFeature && ((ObjectFeature) locusComponent).getFeature().equals(feature)) {
					return true;
				}
			}
			return false;
		}

		public void clearDisplay() {
			Control control = getControl();
			if (control instanceof Text) {
				TextBindingFactory.clearError((Text) control);
			}
			hide();
		}

		public void redisplay(EStructuralFeature feature, boolean showTip) {
			if (isCulprit(feature)) {
				String description = determineDescription();
				int severity = determineSeverity();
				Control control = getControl();
				if (severity == IStatus.ERROR && control instanceof Text) {
					TextBindingFactory.showError((Text) control, description, showTip);
				}
				show();
				setDescriptionText(description);
				setImage(determineImage(severity));
			} else {
				clearDisplay();
			}
		}

		@Override
		protected void update() {
			Rectangle bounds = getControl().getBounds();
			if (bounds.width > 0 && bounds.height > 0) {
				super.update();
			}
		}

		public int determineSeverity() {
			int highest = 0;
			if (validationFailures != null) {
				for (ConstraintStatus status : validationFailures) {
					int severity = status.getSeverity();
					if (severity > highest) {
						highest = severity;
					}
				}
			}
			return highest;
		}

		private String determineDescription() {
			StringBuilder builder = new StringBuilder();
			int i = 0;
			for (ConstraintStatus status : validationFailures) {
				if (i++ > 0) {
					builder.append('\n');
				}
				builder.append(status.getMessage());
			}
			return builder.toString();
		}

		private Image determineImage(int severity) {
			String id = "";
			switch (severity) {
			case IStatus.INFO:
				id = FieldDecorationRegistry.DEC_INFORMATION;
				break;
			case IStatus.WARNING:
				id = FieldDecorationRegistry.DEC_WARNING;
				break;
			case IStatus.ERROR:
				id = FieldDecorationRegistry.DEC_ERROR;
				break;
			default:
				Logger logger = Logger.getLogger(EMFDetailFormPart.class);
				logger.warn("unexpected status for validation failure: " + severity);
				id = FieldDecorationRegistry.DEC_ERROR;
			}
			return FieldDecorationRegistry.getDefault().getFieldDecoration(id).getImage();
		}

		@Override
		public void dispose() {
			decorationMap.remove(getControl());
			super.dispose();
		}

	}

	public static void bindControlViability(DetailProviderParameter p, Control[] controls) {
		EObject eObject = p.getTarget();
		IDetailProvider provider = EMFUtils.adapt(eObject, IDetailProvider.class);
		if (!(provider instanceof DetailProvider)) {
			return;
		}
		IItemPropertyDescriptor pDescriptor = p.getPropertyDescriptor();
		if (!pDescriptor.canSetProperty(eObject)) {
			return;
		}
		IObservableValue observable = ((DetailProvider) provider).getEditabilityObservable(p);
		if (observable != null) {
			DataBindingContext dbc = p.getDataBindingContext();
			bindControlViability(dbc, observable, controls);
		}
	}

	public static void bindControlViability(DataBindingContext dbc, IObservableValue observable, Control[] controls) {
		for (Control control : controls) {
			try {
				Binding binding = dbc.bindValue(SWTObservables.observeEditable(control), observable);
				dbc.addBinding(binding);
				binding.updateModelToTarget();
			} catch (IllegalArgumentException e) { /* some controls just cannot be controlled as such */
			} catch (Exception e) {
				LogUtil.error("cannot create binding", e);
			}
			try {
				Binding binding = dbc.bindValue(SWTObservables.observeEnabled(control), observable);
				dbc.addBinding(binding);
				binding.updateModelToTarget();
			} catch (IllegalArgumentException e) { /* some controls just cannot be controlled as such */
			} catch (Exception e) {
				LogUtil.error("cannot create binding", e);
			}
		}
	}

	public static IBindingFactory getBindingFactory(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		EClassifier eType = feature.getEType();
		IBindingFactory bindingFactory = getBindingFactoryForType(eType);
		if (bindingFactory != null) {
			return bindingFactory;
		}

		// If choices exist, they can be either represented as a multi select if the property
		// represents a multitude of values, or a combo if only one can be set at a time
		//
		if (feature instanceof EReference) {
			EReference reference = (EReference) feature;
			if (reference.isContainment()) {
				ChildrenBindingFactory.createBinding(parameter);
				// UGLY: we must have come here through EMFDetailFormPart.createReferenceBinding
				// so the return value is going to be ignored anyway. so we just return null.
				return null;
			}
			if (pd.isMany(target)) {
				if (feature.isUnique()) {
					bindingFactory = UNIQUE_MULTI_SELECT_BINDING_FACTORY;
				} else {
					bindingFactory = MULTI_SELECT_BINDING_FACTORY;
				}
			} else {
				bindingFactory = COMBO_BINDING_FACTORY;
			}
		} else {

			EDataType eDataType = (EDataType) eType;
			Class<?> instanceClass = eDataType.getInstanceClass();
			if (eDataType.isSerializable()) {
				boolean hasChoiceOfValues = EMFUtils.hasChoiceOfValues(pd, target);
				if (hasChoiceOfValues) {
					if (pd.isMany(target)) {
						if (feature.isUnique()) {
							bindingFactory = UNIQUE_MULTI_SELECT_BINDING_FACTORY;
						} else {
							bindingFactory = MULTI_SELECT_BINDING_FACTORY;
						}
					} else {
						bindingFactory = COMBO_BINDING_FACTORY;
					}
				} else if (pd.isMany(target)) {
					bindingFactory = TABLE_BINDING_FACTORY;
				} else if (instanceClass == Boolean.class || instanceClass == Boolean.TYPE) {
					bindingFactory = BOOLEAN_BINDING_FACTORY;
				} else if (RGB.class == eType.getInstanceClass() || ERGB.class == eType.getInstanceClass()) {
					bindingFactory = RGB_BINDING_FACTORY;
				} else if (URI.class == eType.getInstanceClass()) {
					bindingFactory = URI_BINDING_FACTORY;
				} else if (URL.class == eType.getInstanceClass()) {
					bindingFactory = URL_BINDING_FACTORY;
				} else if (IPath.class == eType.getInstanceClass()) {
					bindingFactory = IPATH_BINDING_FACTORY;
				} else {
					bindingFactory = TEXT_BINDING_FACTORY;
				}
			}
		}
		return bindingFactory;
	}

	private static IBindingFactory getBindingFactoryForType(EClassifier eType) {
		List<Object> types = Arrays.asList(new Object[] { eType.getEPackage(), IBindingFactory.class });
		AdapterFactory factory = EMFUtils.GLOBAL_ADAPTER_FACTORY.getFactoryForTypes(types);
		if (factory != null) {
			return (IBindingFactory) factory.adapt(eType, IBindingFactory.class);
		}
		return null;
	}

	/** @see SPF-7060, org.eclipse.emf.validation.internal.service.LiveValidator.AttachedToResourceNotificationFilter */
	private static class NotNecessarilyAttachedToResourceNotificationFilter implements FilteredCollection.Filter<Notification> {

		@Override
		public boolean accept(Notification element) {
			return (element.getNotifier() instanceof EObject);
			// && (((EObject) element.getNotifier()).eResource() != null);
		}
	}

	public static boolean isCategoryEmpty(Object target, List<IItemPropertyDescriptor> propertyDescriptors) {
		// +
		if (propertyDescriptors.size() == 1) {
			Object object = EMFUtils.getPropertyValue(propertyDescriptors.get(0), target);
			if (object != null && object instanceof EMap) {
				return ((EMap<?, ?>) object).size() < 1;
			}
		}
		return false;
	}

	public static boolean isCategoryHeaderHidden(String category) {
		return (category != null && HIDDEN_CATEGORY_HEADERS.contains(category));
	}

	public static IUndoableOperation addContributorOperations(IUndoableOperation operation, EObject target, IItemPropertyDescriptor pd, Object newValue) {
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		Object oldValue = null;
		if (pd instanceof MultiItemPropertyDescriptor) {
			oldValue = ((MultiItemPropertyDescriptor) pd).getValues();
		} else {
			oldValue = EMFUtils.getPropertyValue(pd, target);
		}
		return EMFUtils.addContributorOperations(operation, target, feature, oldValue, newValue);
	}

	public static EObject getCommandOwner(IItemPropertyDescriptor pd, Object model) {
		if (pd instanceof CommandDelegate) {
			EObject owner = ((CommandDelegate) pd).getCommandOwner(model);
			if (owner != null) {
				return owner;
			}
		}
		if (pd instanceof OverrideableCommandOwner) {
			OverrideableCommandOwner descriptor = (OverrideableCommandOwner) pd;
			EObject owner = (EObject) descriptor.getCommandOwner();
			if (owner != null) {
				return owner;
			}
		}
		return null;
	}

}
