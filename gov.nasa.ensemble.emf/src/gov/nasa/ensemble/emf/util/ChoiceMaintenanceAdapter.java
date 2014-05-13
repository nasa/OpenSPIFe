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
package gov.nasa.ensemble.emf.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class maintains the fact that only one of many features can be set at
 * any one time. This essentially fixes the issue of autogenerated EMF models
 * from XSD files in which the xsd:choice is used and only one of several
 * features is allowed at any one time.
 *  
 * @author aaghevli
 */
public class ChoiceMaintenanceAdapter extends AdapterImpl {
	
	private final Set<EStructuralFeature> set;
	private boolean isUnsettingOtherChoices = false;

	public ChoiceMaintenanceAdapter(Collection<? extends EStructuralFeature> features) {
		super();
		this.set = Collections.unmodifiableSet(new HashSet<EStructuralFeature>(features));
	}

	
	@Override
	public void notifyChanged(Notification msg) {
		if (isUnsettingOtherChoices) {
			return;
		}
		Object f = msg.getFeature();
		EObject object = (EObject) msg.getNotifier();
		if (set.contains(f)) {
			for (EStructuralFeature feature : set) {
				if (f == feature) {
					continue;
				}
				isUnsettingOtherChoices = true;
				object.eUnset(feature);
				isUnsettingOtherChoices = false;
			}
		}
	}
	
}
