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
package gov.nasa.ensemble.emf;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * Provides a means of listening for adapter target changes, and implements
 * {@link #isAdapterForType(Object)} to return whether the passed object is 
 * the class of the adapter.
 */
public class CommonAdapter extends AdapterImpl {

	@Override
	public final void setTarget(Notifier newTarget) {
		Notifier oldTarget = getTarget();
		super.setTarget(newTarget);
		targetChanged(oldTarget, newTarget);
	}

	protected void targetChanged(Notifier oldTarget, Notifier newTarget) {
		// nothing by default
	}
	
	@Override
	public boolean isAdapterForType(Object type) {
		return getClass() == type;
	}
}
