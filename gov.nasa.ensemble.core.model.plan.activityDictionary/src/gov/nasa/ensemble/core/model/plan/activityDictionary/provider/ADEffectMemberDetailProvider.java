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
package gov.nasa.ensemble.core.model.plan.activityDictionary.provider;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.AutoListMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.ComputableAmountStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryFactory;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage;
import gov.nasa.ensemble.core.model.plan.impl.EMemberImpl;
import gov.nasa.ensemble.core.model.plan.provider.AbstractMemberDuplexingObservableValue;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jscience.physics.amount.Amount;

/**
 * Binding Factory for the ADEffectMember effects
 * 
 * @author ideliz
 */
class ADEffectMemberDetailProvider extends DetailProvider {

	private static final String AD_RESOURCES_ALWAYS_VISIBLE_FLAG = "activitydictionary.resources.alwaysvisible";
	private static final boolean SHOW_ZERO_VALUE_RESOURCES = EnsembleProperties.getBooleanPropertyValue("activitydictionary.resources.showzerovalues", false);
	private static final List<String> AD_RESOURCES_ALWAYS_VISIBLE = EnsembleProperties.getStringListPropertyValue(AD_RESOURCES_ALWAYS_VISIBLE_FLAG);
	private static final EStructuralFeature EFFECTS_FEATURE = ActivityDictionaryPackage.Literals.AD_EFFECT_MEMBER__EFFECTS;
	private static final IStringifier<ComputableAmount> STRINGIFIER = new ComputableAmountStringifier();
	private Composite effectsComposite;

	@Override
	public boolean canCreateBindings(DetailProviderParameter parameter) {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void createBinding(final DetailProviderParameter parameter) {

		// actually populate the effects onto the effectsComposite
		BindingFactory factory = new BindingFactory() {
			@Override
			public Binding createBinding(final DetailProviderParameter parameter) {
				final FormToolkit toolkit = parameter.getDetailFormToolkit();
				effectsComposite = createEditorComposite(toolkit, parameter.getParent());
				observeADEffectMember(parameter);
				return null;
			}
		};
		factory.createBinding(parameter);
	}

	@SuppressWarnings("unchecked")
	private void observeADEffectMember(final DetailProviderParameter parameter) {
		final EObject target = parameter.getTarget();
		final IObservableValue observable;
		final IValueChangeListener valueChangeListener;
		final EMap effects;
		if (target instanceof MultiEObject) {
			final Collection<? extends EObject> eObjects = ((MultiEObject) target).getEObjects();
			List<EPlanElement> consolidatedPlanElements = EPlanUtils.getConsolidatedPlanElements((List<EPlanElement>) eObjects);
			observable = new ADEffectObservable(consolidatedPlanElements, EFFECTS_FEATURE);
			valueChangeListener = new IValueChangeListener() {
				@Override
				public void handleValueChange(ValueChangeEvent event) {
					final EMap value = (EMap) observable.getValue();
					// System.out.println("MULTI CHANGE! = " + value.size());
					TransactionUtils.runInDisplayThread(effectsComposite, target, new Runnable() {
						@Override
						public void run() {
							drawEffectsInDetails(value, parameter);
						}
					});
				}
			};
			effects = (EMap) observable.getValue();
		} else {
			observable = EMFObservables.observeValue(target, EFFECTS_FEATURE);
			valueChangeListener = new IValueChangeListener() {
				@Override
				public void handleValueChange(ValueChangeEvent event) {
					final EMap value = ((ADEffectMember) target).getEffects();
					// System.out.println("SINGLE CHANGE! = " + value.size());
					TransactionUtils.runInDisplayThread(effectsComposite, target, new Runnable() {
						@Override
						public void run() {
							drawEffectsInDetails(value, parameter);
						}
					});
				}
			};
			effects = ((ADEffectMember) target).getEffects();
		}
		observable.addValueChangeListener(valueChangeListener);
		effectsComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				observable.removeValueChangeListener(valueChangeListener);
			}
		});
		TransactionUtils.runInDisplayThread(effectsComposite, target, new Runnable() {
			@Override
			public void run() {
				drawEffectsInDetails(effects, parameter);
			}
		});
	}

	/**
	 * Populate the Effects composite with the effects
	 * 
	 * @param composite
	 * @param member
	 * @param toolkit
	 */
	@SuppressWarnings("unchecked")
	private void drawEffectsInDetails(final EMap effects, final DetailProviderParameter parameter) {
		if (effectsComposite.isDisposed()) {
			return;
		}
		for (Control c : effectsComposite.getChildren()) {
			c.dispose();
		}
		if (effects.isEmpty()) {
			return;
		}
		final FormToolkit toolkit = parameter.getDetailFormToolkit();
		final TreeSet<Entry<ADEffectKey, ComputableAmount>> objectlessDefs = new TreeSet<Entry<ADEffectKey, ComputableAmount>>(new ResourceDefComparator());
		final Map<EObject, List<Entry<ADEffectKey, ComputableAmount>>> objectToResource = new AutoListMap<EObject, Entry<ADEffectKey, ComputableAmount>>(EObject.class);
		final Set<EClass> objectTypes = new TreeSet<EClass>(new EClassNameComparator());
		TransactionalEditingDomain domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(effects);
		if (domain != null) { // it's okay if there is no domain
			if (gov.nasa.ensemble.emf.transaction.TransactionUtils.checkTransaction(domain, false) == null) {
				throw new IllegalStateException("must call drawEffectsInDetails from within a transaction");
			}
		}
		for (Object e : effects.entrySet()) {
			Entry<ADEffectKey, ComputableAmount> entry = (Entry<ADEffectKey, ComputableAmount>) e;
			ADEffectKey key = entry.getKey();
			EObject object = key.getObject();
			if (object == null) {
				objectlessDefs.add(entry);
			} else {
				objectToResource.get(object).add(entry);
				objectTypes.add(object.eClass());
			}
		}
		for (Entry<ADEffectKey, ComputableAmount> entry : objectlessDefs) {
			observeTextValue(effectsComposite, toolkit, entry);
		}
		for (EClass klass : objectTypes) {
			Collection<EObject> objects;
			final EObject target = parameter.getTarget();
			if (target instanceof MultiEObject) {
				final List eObjects = (List) ((MultiEObject) target).getEObjects();
				EPlanElement representative = (EPlanElement) eObjects.get(0);
				final ADEffectMember repMember = representative.getMember(ADEffectMember.class);
				objects = EMFUtils.getReachableObjectsOfType(repMember, klass);
			} else {
				objects = EMFUtils.getReachableObjectsOfType(target, klass);
			}
			for (EObject object : objects) {
				if (objectToResource.containsKey(object)) {
					List<Entry<ADEffectKey, ComputableAmount>> values = objectToResource.get(object);
					Collections.sort(values, new ResourceDefComparator());
					for (Entry<ADEffectKey, ComputableAmount> entry : values) {
						observeTextValue(effectsComposite, toolkit, entry);
					}
				}
			}
			effectsComposite.layout();
		}
	}

	private void observeValueAndRunOnChange(final Widget widget, EObject object, EStructuralFeature feature, final Runnable runnable) {
		final IObservableValue valueObservable = EMFObservables.observeValue(object, feature);
		final IValueChangeListener valueChangeListener = new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				WidgetUtils.runInDisplayThread(widget, runnable);
			}
		};
		valueObservable.addValueChangeListener(valueChangeListener);
		widget.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				valueObservable.removeValueChangeListener(valueChangeListener);
			}
		});
	}

	/**
	 * Each effect also has a listener to update the value if modified without having to redraw the whole effects composite
	 * 
	 * @param composite
	 * @param toolkit
	 * @param entry
	 */
	private void observeTextValue(Composite composite, FormToolkit toolkit, final Entry<ADEffectKey, ComputableAmount> entry) {
		ADEffectKey key = entry.getKey();
		final ComputableAmount effectValue = entry.getValue();
		EResourceDef resourceDef = key.getResourceDef();
		boolean nonNull = resourceDef != null;
		String resourceName = resourceDef == null ? "(Undefined resource)" : resourceDef.getName();
		if (!SHOW_ZERO_VALUE_RESOURCES // show zero value resources flag (default is not show)
				&& !(nonNull && resourceDef instanceof EClaimableResourceDef) // not Claims (always zero value)
				&& AmountUtils.approximatesZero(effectValue.getAmount()) // is zero value?
				&& (AD_RESOURCES_ALWAYS_VISIBLE == null || // not on the always visible resources list
				(AD_RESOURCES_ALWAYS_VISIBLE != null && !AD_RESOURCES_ALWAYS_VISIBLE.contains(resourceName)))) {
			return;
		}
		EMFDetailUtils.createLabel(composite, toolkit, key.toString());
		final Text text = new Text(composite, SWT.READ_ONLY);
		if (nonNull && resourceDef instanceof EClaimableResourceDef) {
			text.setText("");
			return; // dont attach listener because value won't change
		}
		text.setText(STRINGIFIER.getDisplayString(effectValue));
		EStructuralFeature valueFeature = ActivityDictionaryPackage.Literals.AD_EFFECT_ENTRY__VALUE;
		observeValueAndRunOnChange(text, (EObject) entry, valueFeature, new Runnable() {
			@Override
			public void run() {
				text.setText(STRINGIFIER.getDisplayString(entry.getValue()));
			}
		});
	}

	@SuppressWarnings("unused")
	private void showError(Composite composite, String errorMessage) {
		String qualMessage = errorMessage;
		if (target instanceof EMemberImpl) {
			EPlanElement planElement = ((EMemberImpl) target).getPlanElement();
			if (planElement != null) {
				qualMessage = planElement.getName() + " - " + qualMessage;
			}
		}
		LogUtil.warn(qualMessage);
		final Text text = new Text(composite, SWT.READ_ONLY);
		text.setText(errorMessage);
		text.setForeground(ColorConstants.darkRed);
		return;
	}

	/**
	 * Create base composite for effects
	 * 
	 * @param toolkit
	 * @param parent
	 * @return
	 */
	private Composite createEditorComposite(FormToolkit toolkit, Composite parent) {
		Composite composite = new EnsembleComposite(parent, SWT.NONE);
		toolkit.adapt(composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		return composite;
	}

	protected final static class ADEffectObservable extends AbstractMemberDuplexingObservableValue {
		protected ADEffectObservable(List<EPlanElement> elements, EStructuralFeature feature) {
			super(elements, ADEffectMember.class, EFFECTS_FEATURE, EcorePackage.Literals.EMAP);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object coalesceElements(Collection elements) {
			final EMap<ADEffectKey, ComputableAmount> effects = ActivityDictionaryFactory.eINSTANCE.createADEffectMember().getEffects();
			for (Object element : elements) {
				IObservableValue v = (IObservableValue) element;
				final EMap elementEffects = (EMap) v.getValue();
				Set entrySet = new HashSet(elementEffects.entrySet());
				for (Object e : entrySet) {
					Entry<ADEffectKey, ComputableAmount> entry = (Entry<ADEffectKey, ComputableAmount>) e;
					ADEffectKey key = entry.getKey();
					if (effects.containsKey(key)) {
						Amount<?> currentValue = EMFUtils.copy(entry.getValue()).getAmount();
						Amount<?> subTotal = EMFUtils.copy(effects.get(key)).getAmount();
						subTotal = subTotal.plus(currentValue);
						ComputableAmount totalCA = JScienceFactory.eINSTANCE.createComputableAmount(subTotal, ComputingState.COMPLETE);
						effects.put(key, totalCA);
					} else {
						effects.put(EMFUtils.copy(key), EMFUtils.copy(entry.getValue()));
					}
				}
			}
			return effects;
		}
	}

	// ************************** COMPARATORS ****************************//

	private class ResourceDefComparator implements Comparator<Entry<ADEffectKey, ComputableAmount>> {
		@Override
		public int compare(Entry<ADEffectKey, ComputableAmount> o1, Entry<ADEffectKey, ComputableAmount> o2) {
			EResourceDef resourceDef1 = o1.getKey().getResourceDef();
			EResourceDef resourceDef2 = o2.getKey().getResourceDef();
			if (resourceDef1 != null && resourceDef2 != null) {
				return resourceDef1.getName().compareTo(resourceDef2.getName());
			}
			return 0;
		}
	}

	private class EClassNameComparator implements Comparator<EClass> {
		@Override
		public int compare(EClass o1, EClass o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}
}
