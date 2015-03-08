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
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;

public class DictionaryLabelProviderAdapterFactory  extends AdapterFactoryImpl {

	@Override
	public boolean isFactoryForType(Object type) {
		if (DictionaryPackage.eINSTANCE == type) {
			return true;
		}
		if (type instanceof EObject) {
			return ((EObject)type).eClass().getEPackage() == DictionaryPackage.eINSTANCE;
		}
		return false;
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (target instanceof EActivityDef && ILabelProvider.class == type) {
			AdapterFactory factory = EMFUtils.getAdapterFactory(target);
			return new DictionaryLabelProvider(factory);
		}
		return super.adapt(target, type);
	}

}