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
package gov.nasa.ensemble.core.detail.emf.binding;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;

public class DynamicItemProvider extends ReflectiveItemProvider {

	public DynamicItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	/**
	   */
	  @Override
	  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) 
	  {
	    // if (itemPropertyDescriptors == null)
	    {
	      itemPropertyDescriptors = new ArrayList<IItemPropertyDescriptor>();

	      for (EStructuralFeature eFeature : ((EObject)object).eClass().getEAllStructuralFeatures())
	      {
	        if (!(eFeature instanceof EReference) || !((EReference)eFeature).isContainment())
	        {
	          itemPropertyDescriptors.add
	            (new ItemPropertyDescriptor
	              (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
	               getFeatureText(eFeature),
	               getResourceLocator().getString
	                 ("_UI_Property_description", new Object [] { getFeatureText(eFeature), eFeature.getEType().getName() }),
	               eFeature,
	               false,
	               ItemPropertyDescriptor.GENERIC_VALUE_IMAGE));
	        }
	      }
	    }
	    return itemPropertyDescriptors;
	  }
}
