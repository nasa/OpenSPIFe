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

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

public class DetailProvider extends AdapterImpl implements IDetailProvider {
	
	/**
	 * @see IDetailProvider#canCreateBindings(DetailProviderParameter)
	 */
	@Override
	public boolean canCreateBindings(DetailProviderParameter parameter) {
		return false;
	}
	
	/**
	 * @see IDetailProvider#createBinding(DetailProviderParameter)
	 */
	@Override
	public void createBinding(DetailProviderParameter parameter) {
		// no default implementation
	}
	
	/**
	 * Allows for the provider to return a custom editability observable in order to
	 * make the particular UI elements editable or uneditable.
	 * @see EditabilityObservables
	 * @param parameter a collection of metadata to adapt
	 * @return an {@link IObservableValue} that will return true/false based on editability
	 */
	public IObservableValue getEditabilityObservable(DetailProviderParameter parameter) {
		return null;
	}
	
	/**
	 * Return a sorter that sorts the property descriptors for the target element  
	 * @param parameter to sort for using the target element
	 * @return the sorter is possible, null otherwise
	 */
	@Override
	public PropertyDescriptorSorter getPropertyDescriptorSorter(DetailProviderParameter parameter) {
		return null;
	}
	
}
