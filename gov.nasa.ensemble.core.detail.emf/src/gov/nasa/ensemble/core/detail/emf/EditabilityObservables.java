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
package gov.nasa.ensemble.core.detail.emf;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.detail.emf.binding.DuplexingObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.DuplexingObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EditabilityObservables {
	
	public static IObservableValue createMultiFeatureObservable(EObject object, List<EStructuralFeature> features, final EditableRule runnable) {
		List<IObservable> observables = new ArrayList<IObservable>();
		for (EStructuralFeature feature : features) {
			observables.add(EMFObservables.observeValue(object, feature));
		}
		return createMultiFeatureObservable(observables, runnable);
	}
	
	public static IObservableValue createMultiFeatureObservable(List<IObservable> observables, final EditableRule runnable) {
		DuplexingObservableList list = new DuplexingObservableList(observables, Boolean.class);
		DuplexingObservableValue observable = new DuplexingObservableValue(list) {
			@Override
			protected Object coalesceElements(Collection elements) {
				return runnable.isEditable();
			}
		};
		return observable;
	}
	
	public static IObservableValue createEqualityObservable(EObject object, EStructuralFeature feature, final Object expectedValue) {
		return new EditabilityObservableValue(object, feature) {
			@Override
			protected Boolean isEditable() {
				Object value = eObject.eGet(eStructuralFeature);
				return CommonUtils.equals(value, expectedValue);
			}
		};
	}
	
	public static IObservableValue createInequalityObservable(EObject object, EStructuralFeature feature, final Object expectedValue) {
		return new EditabilityObservableValue(object, feature) {
			@Override
			protected Boolean isEditable() {
				Object value = eObject.eGet(eStructuralFeature);
				return !CommonUtils.equals(value, expectedValue);
			}
		};
	}
	
	public static abstract class EditableRule {

		public abstract Boolean isEditable();
		
	}
	
}
