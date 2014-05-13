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
package gov.nasa.ensemble.dictionary.provider;

import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDictionary;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EObjectItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.osgi.framework.Bundle;


public class DefinedObjectItemProviderAdapterFacory extends AdapterFactoryImpl {
	
	public DefinedObjectItemProviderAdapterFacory() {
		super();
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (IItemLabelProvider.class == type) {
			return new DefinedObjectItemProvider(this);
		}
		return super.adapt(target, type);
	}

	@Override
	public boolean isFactoryForType(Object object) {
		if (object instanceof EActivityDictionary) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() instanceof EActivityDictionary;
		}
		if (object instanceof EObject 
				&& DictionaryPackage.Literals.OBJECT_DEF.isSuperTypeOf(((EObject)object).eClass())) {
			return true;
		}
		if (IItemLabelProvider.class == object) {
			return true;
		}
		return super.isFactoryForType(object);
	}
	
	private static class DefinedObjectItemProvider extends EObjectItemProvider implements IItemLabelProvider {

		public DefinedObjectItemProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public String getText(Object object) {
			EObject eObject = (EObject) object;
			EClass eClass = eObject.eClass();
			EStructuralFeature nameFeature = eClass.getEStructuralFeature("name");
			if (nameFeature != null 
					&& nameFeature instanceof EAttribute 
					&& EcorePackage.Literals.ESTRING == ((EAttribute)nameFeature).getEAttributeType()) {
				return (String) eObject.eGet(nameFeature);
			}
			return eClass.getName();
		}

		@Override
		public Object getImage(Object object) {
			IProduct product = Platform.getProduct();
			if (product == null) {
				return null;
			}
			Bundle bundle = product.getDefiningBundle();
			String name = ((EObject)object).eClass().getName();
			
			Path relativePath = new Path("icons/"+name+".png");
			URL pluginRelativeURL = FileLocator.find(bundle, relativePath, null);
			if (pluginRelativeURL != null) {
				try {
					return FileLocator.resolve(pluginRelativeURL);
				} catch (IOException e) {
					// okay
				}
			}
			return null;
		}
		
	}
	
}
