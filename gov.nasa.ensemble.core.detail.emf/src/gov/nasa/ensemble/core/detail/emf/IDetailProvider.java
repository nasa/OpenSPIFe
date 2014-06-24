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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.common.notify.Adapter;

/**
 * Allows for delegation of the detail entry (typically a property descriptor) via
 * the EMF adaptation framework. This is called using the following API
 * 
 * <blockquote>
 * <pre>
 * IDetailProvider provider = EMFUtils.adapt(target, IDetailProvider.class);
 * if (provider != null && provider.canCreateBindings(parameter)) {
 * 	provider.createBinding(parameter);
 * }
 * </pre>
 * </blockquote>
 * 
 * The {@link DetailProviderParameter} is designed to allow for the API to grow without
 * impacting the method signatures.
 * 
 * This class is not intended to be implemented directly. Use {@link DetailProvider} 
 *
 */
public interface IDetailProvider extends Adapter {

	/**
	 * Allows the implementor to indicate if the particular {@link DetailProviderParameter}
	 * can be overriden by this particular provider
	 * @param parameter a collection of metadata to adapt
	 * @return true if this adapter will override, false otherwise
	 */
	public boolean canCreateBindings(DetailProviderParameter parameter);
	
	/**
	 * Created the binding. It is implied in the name that the code will eventually
	 * create a {@link Binding} element and add it to the {@link DataBindingContext}
	 * in order to take care of disposal properly.
	 * @param parameter a collection of metadata to adapt
	 */
	public void createBinding(DetailProviderParameter parameter);

	/**
	 * Return a sorter that sorts the property descriptors for the target element  
	 * @param parameter to sort for using the target element
	 * @return the sorter is possible, null otherwise
	 */
	public PropertyDescriptorSorter getPropertyDescriptorSorter(DetailProviderParameter parameter);
	
}
