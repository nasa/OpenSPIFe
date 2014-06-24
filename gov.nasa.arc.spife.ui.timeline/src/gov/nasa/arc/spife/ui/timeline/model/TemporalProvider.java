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
package gov.nasa.arc.spife.ui.timeline.model;

import java.util.Date;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.jscience.physics.amount.Amount;

public abstract class TemporalProvider extends AdapterImpl implements IChangeNotifier, IDisposable {

	private final IChangeNotifier notifier = new ChangeNotifier();
	
	public abstract Date getStartTime(Object object);
	
	public abstract Amount<Duration> getDuration(Object object);
	
	@Override
	public void addListener(INotifyChangedListener notifyChangedListener) {
		notifier.addListener(notifyChangedListener);
	}

	@Override
	public void fireNotifyChanged(Notification notification) {
		notifier.fireNotifyChanged(notification);
	}

	@Override
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		notifier.removeListener(notifyChangedListener);
	}

	@Override
	public void dispose() {
		// no default implementation
	}
	
}
