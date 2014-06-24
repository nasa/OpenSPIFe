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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EAttribute;
import org.jscience.physics.amount.Amount;

/**
 * This class takes a list of plan elements and sums up the ComputedAmount values of the EMember attribute
 */
public class SummingAmountObservableValue extends AbstractMemberDuplexingObservableValue {
	
	public SummingAmountObservableValue(final List<EPlanElement> elements, final Class<? extends EMember> memberClass, final EAttribute eAttribute) {
		super(elements, memberClass, eAttribute, JSciencePackage.Literals.EAMOUNT);
	}

	@Override
	protected Object coalesceElements(Collection elements) {
		Amount total = null;
		for (Object o : elements) {
			IObservableValue v = (IObservableValue) o;
			Object value = v.getValue();
			Amount<?> current = null;
			if (value instanceof ComputableAmount) {
				current = ((ComputableAmount)value).getAmount();
			} else if (value instanceof Amount) {
				current = (Amount<?>) value;
			} else {
				continue;
			}
			
			if (current != null) {
				if (total == null) {
					total = current;
				} else {
					total = total.plus(current);
				}
			}
		}
		return total;
	}
	
}
