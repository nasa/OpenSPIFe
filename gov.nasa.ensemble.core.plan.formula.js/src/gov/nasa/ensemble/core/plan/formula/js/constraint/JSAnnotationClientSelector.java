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
package gov.nasa.ensemble.core.plan.formula.js.constraint;

import gov.nasa.ensemble.common.extension.DynamicExtensionUtils;
import gov.nasa.ensemble.core.plan.formula.js.Activator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.validation.model.IClientSelector;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class JSAnnotationClientSelector implements IClientSelector {

	private static Set<String> registeredPackages = Collections.synchronizedSet(new HashSet<String>());
	
	@SuppressWarnings("restriction")
	public static void registerPackage(String packageUri, Bundle contributorBundle) {
		if (packageUri == null) {
			return;
		}
		boolean newPackage = registeredPackages.add(packageUri);
		if (!newPackage) {
			return;
		}
		// To avoid duplicate constraint providers in the constraint cache, initialize the ModelValidationService
		// singleton before dynamically loading constraint provider extensions
		ModelValidationService.getInstance();
		DynamicExtensionUtils.addDynamicConstraintContribution(packageUri, contributorBundle, "datafiles/JSAnnotationExtension.xml", Activator.getDefault().getBundle());
	}
	
	public static void registerPackage(String packageUri) {
		if (packageUri == null) {
			return;
		}
		registeredPackages.add(packageUri);
	}
	
	public static void unregisterPackage(String packageUri)  {
		registeredPackages.remove(packageUri);
	}

	public synchronized boolean selects(Object object) {
		if (!(object instanceof EObject)) {
            return false;
        }
        EObject eObject = (EObject)object;
        EPackage ePackage = eObject.eClass().getEPackage();
        if (ePackage == null) {
        	return false;
        }
		return registeredPackages.contains(ePackage.getNsURI());
	}

}
