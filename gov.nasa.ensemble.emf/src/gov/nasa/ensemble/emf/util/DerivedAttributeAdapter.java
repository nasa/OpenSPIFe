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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

public class DerivedAttributeAdapter extends AdapterImpl {
	
	protected final InternalEObject source;
	protected final EStructuralFeature derivedFeature;
	protected List<EStructuralFeature> localFeatures = new ArrayList<EStructuralFeature>();

	public DerivedAttributeAdapter(EObject source, EStructuralFeature derivedFeature) {
		this(source, derivedFeature, null);
	}
	
	public DerivedAttributeAdapter(EObject source, EStructuralFeature derivedFeature, EStructuralFeature dependentFeature) {
		super();
		this.source = (InternalEObject) source;
		this.derivedFeature = derivedFeature;
		if (dependentFeature != null) {
			localFeatures.add(dependentFeature);
		}
		source.eAdapters().add(this);
	}
	
	public void addLocalDependency(EStructuralFeature localFeature) {
		localFeatures.add(localFeature);
	}

	@Override
	public void notifyChanged(Notification notification) {
		if (notification.getEventType() != Notification.RESOLVE && localFeatures.contains(notification.getFeature())) {
			notifyDerivedAttributeChange();
		}
	}

	protected void notifyDerivedAttributeChange() {
		if (source.eNotificationRequired()) {
			Object newValue = source.eGet(derivedFeature, true, true);
			ENotificationImpl notification = new ENotificationImpl(source, Notification.SET, derivedFeature, null, newValue);
			source.eNotify(notification);
		}
	}
}
