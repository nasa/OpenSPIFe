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

import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.IDetailProvider;
import gov.nasa.ensemble.core.detail.emf.binding.DuplexingObservableList;
import gov.nasa.ensemble.core.detail.emf.binding.StringifierUpdateValueStrategy;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.provider.AbstractMemberDuplexingObservableValue;
import gov.nasa.ensemble.core.model.plan.provider.SummingAmountObservableValue;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.emf.PropertyDescriptorContributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.measure.unit.SI;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.DuplexingObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jscience.physics.amount.Amount;

public class TemporalPropertyDescriptorContributor implements PropertyDescriptorContributor {

	private static final String MULTI_SELECT_SCHEDULE = "Multi-select Schedule";
	private static final String GAP = "Gap";
	private static final String SPAN = "Span";
	private static final String START_OFFSET = "Start Offset";
	private static final String END_OFFSET = "End Offset";
	private static final String EARLIEST_START_TIME = "Earliest Start Time";
	private static final String LATEST_END_TIME = "Latest End Time";
	private static final String DURATION_SUM = "Sum of Durations";
	
	@Override
	@SuppressWarnings("unchecked")
	public List<IItemPropertyDescriptor> getPropertyDescriptors(EObject target) {
		if (target instanceof MultiEObject) {
			List<EPlanElement> elements = new ArrayList<EPlanElement>();
			Collection<? extends EObject> objects = ((MultiEObject)target).getEObjects();
			for (EObject object : objects) {
				if (object instanceof EPlanElement) {
					elements.add((EPlanElement)object);
				} else {
					return Collections.emptyList();
				}
			}
			return createPropertyDescriptors(elements);
		}
		return Collections.emptyList();
	}
	
	private List<IItemPropertyDescriptor> createPropertyDescriptors(List<EPlanElement> elements) {
		List<IItemPropertyDescriptor> pds = new ArrayList<IItemPropertyDescriptor>();
		boolean isTwo = elements.size() == 2;
		if (isTwo) pds.add(new TemporalStatisticDescriptor(GAP));
		pds.add(new TemporalStatisticDescriptor(SPAN));
		if (isTwo) pds.add(new TemporalStatisticDescriptor(START_OFFSET));
		if (isTwo) pds.add(new TemporalStatisticDescriptor(END_OFFSET));
		pds.add(new TemporalStatisticDescriptor(EARLIEST_START_TIME));
		pds.add(new TemporalStatisticDescriptor(LATEST_END_TIME));
		pds.add(new TemporalStatisticDescriptor(DURATION_SUM));
		return pds;
	}
	
	private static class TemporalStatisticDescriptor extends ItemPropertyDescriptor implements IAdaptable {

		private TemporalStatisticDescriptor(String name) {
			super(new ComposedAdapterFactory(), name, null, (EStructuralFeature)null, false, MULTI_SELECT_SCHEDULE);
		}

		@Override
		public Object getAdapter(Class adapter) {
			if (IDetailProvider.class == adapter) {
				return new TemporalDetailsProvider();
			}
			return null;
		}
		
	}
	
	private static class TemporalDetailsProvider extends DetailProvider {
		
		@Override
		public boolean canCreateBindings(DetailProviderParameter parameter) {
			return parameter.getTarget() instanceof MultiEObject;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void createBinding(DetailProviderParameter parameter) {
			//
			// unwrap the parameter
			DataBindingContext dbc = parameter.getDataBindingContext();
			IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
			Composite parent = parameter.getParent();
			MultiEObject target = (MultiEObject) parameter.getTarget();
			FormToolkit toolkit = parameter.getDetailFormToolkit();
			List list = new ArrayList(target.getEObjects());
			//
			// Create the label and text controls
			String displayName = pd.getDisplayName(target);
			Label label = toolkit.createLabel(parent, displayName, SWT.NONE);
			Text text = toolkit.createText(parent, "", SWT.READ_ONLY);
			text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			//
			// Bind them all
			if (GAP.equals(displayName)) {
				Amount zero = Amount.valueOf(0, SI.SECOND);
				GapObservable observable = new GapObservable(list);
				Amount amount = (Amount)observable.getValue();
				if (amount != null 
						&& !amount.approximates(zero) 
						&& amount.isLessThan(zero)) {
					label.setText("Overlap");
				}
				createBinding(dbc, text, observable);
			} else if (START_OFFSET.equals(displayName)) {
				createBinding(dbc, text, new DeltaTimeObservable(list, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME));
			} else if (END_OFFSET.equals(displayName)) {
				createBinding(dbc, text, new DeltaTimeObservable(list, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME));
			} else if (EARLIEST_START_TIME.equals(displayName)) {
				createBinding(dbc, text, new EarliestDateObservable(list, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME));
			} else if (LATEST_END_TIME.equals(displayName)) {
				createBinding(dbc, text, new LatestDateObservable(list, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME));
			} else if (SPAN.equals(displayName)) {	
				createBinding(dbc, text, new SpanObservable(list));
			} else if (DURATION_SUM.equals(displayName)) {
				createBinding(dbc, text, new SummingAmountObservableValue(list, TemporalMember.class, TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION));
			}
			super.createBinding(parameter);
		}

		private void createBinding(DataBindingContext dbc, Text text, DuplexingObservableValue observable) {
			dbc.addBinding(dbc.bindValue(
					SWTObservables.observeText(text, SWT.FocusOut), 
					observable, 
					new StringifierUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), 
					new StringifierUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE) {

						@Override
						public Object convert(Object value) {
							String string = (String) super.convert(value);
							if (string.startsWith("-")) {
								string = string.substring(1);
							}
							return string;
						}
						
					}));
		}
	}
	
	protected final static class DeltaTimeObservable extends AbstractMemberDuplexingObservableValue {
		protected DeltaTimeObservable(List<EPlanElement> elements, EAttribute eAttribute) {
			super(elements, TemporalMember.class, eAttribute, JSciencePackage.Literals.EDURATION);
		}

		@Override
		protected Object coalesceElements(Collection elements) {
			Date earliest = null;
			Date latest = null;
			for (Object o : elements) {
				IObservableValue v = (IObservableValue) o;
				Date value = (Date) v.getValue();
				if (value == null) {
					continue;
				}
				if (earliest == null || value.before(earliest)) {
					earliest = value;
				}
				if (latest == null || value.after(latest)) {
					latest = value;
				}
			}
			if (latest == null || earliest == null) {
				return Amount.valueOf(0L, SI.MILLI(SI.SECOND));
			}
			return Amount.valueOf(latest.getTime() - earliest.getTime(), SI.MILLI(SI.SECOND));
		}
	}

	protected final static class EarliestDateObservable extends AbstractMemberDuplexingObservableValue {
		protected EarliestDateObservable(List<EPlanElement> elements, EAttribute eAttribute) {
			super(elements, TemporalMember.class, eAttribute, EcorePackage.Literals.EDATE);
		}

		@Override
		protected Object coalesceElements(Collection elements) {
			Date latest = null;
			for (Object o : elements) {
				IObservableValue v = (IObservableValue) o;
				Date value = (Date) v.getValue();
				if (value == null) {
					continue;
				}
				if (latest == null || value.before(latest)) {
					latest = value;
				}
			}
			return latest;
		}
	}

	protected final static class LatestDateObservable extends AbstractMemberDuplexingObservableValue {
		protected LatestDateObservable(List<EPlanElement> elements, EAttribute eAttribute) {
			super(elements, TemporalMember.class, eAttribute, EcorePackage.Literals.EDATE);
		}

		@Override
		protected Object coalesceElements(Collection elements) {
			Date latest = null;
			for (Object o : elements) {
				IObservableValue v = (IObservableValue) o;
				Date value = (Date) v.getValue();
				if (value == null) {
					continue;
				}
				if (latest == null || value.after(latest)) {
					latest = value;
				}
			}
			return latest;
		}
	}

	protected final static class SpanObservable extends DuplexingObservableValue {
		
		protected SpanObservable(List<EPlanElement> elements) {
			super(
				new DuplexingObservableList(
					Arrays.asList(
						new IObservable[] {
							new EarliestDateObservable(elements, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME),
							new LatestDateObservable(elements, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME)
						}), JSciencePackage.Literals.EDURATION
					));
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Object coalesceElements(Collection elements) {
			IObservableValue[] objects = (IObservableValue[]) elements.toArray(new IObservableValue[0]);
			Date earliest = (Date) objects[0].getValue();
			Date latest = (Date) objects[1].getValue();
			if ((earliest == null) || (latest == null)) {
				return null;
			}
			long span = latest.getTime() - earliest.getTime();
			return Amount.valueOf(span, SI.MILLI(SI.SECOND));
		}
	}
	
	protected final static class GapObservable extends DuplexingObservableValue {
		
		protected GapObservable(List<EPlanElement> elements) {
			super(
				new DuplexingObservableList(
					Arrays.asList(
						new IObservable[] {
							new EarliestDateObservable(elements, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME),
							new LatestDateObservable(elements, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)
						}), JSciencePackage.Literals.EDURATION
					));
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Object coalesceElements(Collection elements) {
			IObservableValue[] objects = (IObservableValue[]) elements.toArray(new IObservableValue[0]);
			Date earliest = (Date) objects[0].getValue();
			Date latest = (Date) objects[1].getValue();
			if ((earliest == null) || (latest == null)) {
				return null;
			}
			long span = latest.getTime() - earliest.getTime();
			return Amount.valueOf(span, SI.MILLI(SI.SECOND));
		}
	}

}
