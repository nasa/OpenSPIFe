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
package gov.nasa.ensemble.core.detail.emf.multi;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.IDetailProvider;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreAdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;

public class MultiEObjectDetailAdapterFactory extends EcoreAdapterFactory {

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (IItemPropertySource.class == type) {
			if(target instanceof MultiEObject) {
				return ((MultiEObject)target).getSource();
			}
			
		} else if (IDetailProvider.class == type) {
			return new MultiEObjectDetailProvider();
		}
		return null;
	}

	@Override
	public boolean isFactoryForType(Object object) {
		return
			object instanceof MultiEObject
				|| IItemPropertySource.class == object
				|| IDetailProvider.class == object
				|| super.isFactoryForType(object);
	}
	
	private static class MultiEObjectDetailProvider extends DetailProvider {
		
		private static final ComposedAdapterFactory DEFAULT_FACTORY = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		
		
		@Override
		public boolean canCreateBindings(DetailProviderParameter parameter) {
			IDetailProvider provider = getDetailProvider(parameter);
			if (provider == null) {
				return false;
			} else if (provider instanceof MultiEObjectDetailProvider) {
				return false;
			}
			return provider != null && provider.canCreateBindings(parameter);
		}

		private IDetailProvider getDetailProvider(DetailProviderParameter parameter) {
			EObject target = parameter.getTarget();
			if (target instanceof MultiEObject) {
				ComposedAdapterFactory adapterFactory = getAdapterFactory(target);
				MultiItemPropertyDescriptor pd = (MultiItemPropertyDescriptor) parameter.getPropertyDescriptor();
				EPackage ePackage = pd.getEPackage();
				List<Object> types = Arrays.asList(new Object[] {ePackage, IDetailProvider.class});
				AdapterFactory factory = adapterFactory.getFactoryForTypes(types);
				if (factory != null) {
					IDetailProvider provider = (IDetailProvider) factory.adapt(target, IDetailProvider.class);
					if (provider != null) {
						return provider;
					}
				}
				IItemPropertyDescriptor primaryDescriptor = pd.getPrimaryDescriptor();
				IDetailProvider provider = CommonUtils.getAdapter(primaryDescriptor, IDetailProvider.class);
				if (provider != null) {
					return provider;
				}
				for (EObject object : pd.getCommandOwners()) {
					provider = EMFUtils.adapt(object, IDetailProvider.class);
					if (provider != null) {
						return provider;
					}
				}
				for (EObject object : ((MultiEObject)target).getEObjects()) {
					provider = EMFUtils.adapt(object, IDetailProvider.class);
					if (provider != null) {
						return provider;
					}
				}
			}
			return null;
		}

		private ComposedAdapterFactory getAdapterFactory(EObject target) {
			ComposedAdapterFactory adapterFactory = null;
			MultiEObject mEObject = (MultiEObject) target;
			for (EObject source : mEObject.getEObjects()) {
				EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(source);
				if (domain instanceof AdapterFactoryEditingDomain) {
					AdapterFactory factory = ((AdapterFactoryEditingDomain)domain).getAdapterFactory();
					if (factory instanceof ComposedAdapterFactory) {
						adapterFactory = (ComposedAdapterFactory) factory;
					}
					break;
				}
			}
			return adapterFactory == null ? DEFAULT_FACTORY : adapterFactory;
		}

		@Override
		public void createBinding(DetailProviderParameter parameter) {
			IDetailProvider provider = getDetailProvider(parameter);
			if (provider != null) {
				provider.createBinding(parameter);
			}
		}
		
	}

}
