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
package gov.nasa.ensemble.core.model.plan.activityDictionary.provider;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

public class ContainedElementObservable extends AbstractObservableValue {
	
	private final EObject target;
	private final EStructuralFeature feature;
	private final EObject value;
	private final EditingDomain domain;

	private Adapter listener;
	
	public ContainedElementObservable(EditingDomain domain, EObject target, EStructuralFeature feature, EObject value) {
		super();
		this.domain = domain;
		this.feature = feature;
		this.target = target;
		this.value = value;
	}
	
	@Override
	  protected void firstListenerAdded()
	  {
	    listener =
	      new AdapterImpl()
	      {
	        @Override
	        public void notifyChanged(Notification notification)
	        {
	          if (feature == notification.getFeature() && !notification.isTouch())
	          {
	        	Boolean oldValue = CommonUtils.equals(notification.getOldValue(), value) || (notification.getOldValue() instanceof List && ((List)notification.getOldValue()).contains(value));
	        	Boolean newValue = CommonUtils.equals(notification.getNewValue(), value) || (notification.getNewValue() instanceof List && ((List)notification.getNewValue()).contains(value));
	            final ValueDiff diff = Diffs.createValueDiff(oldValue, newValue);
	            getRealm().exec
	              (new Runnable()
	               {
	                 @Override
					public void run()
	                 {
	                   fireValueChange(diff);
	                 }
	               });
	          }
	        }
	      };
	    target.eAdapters().add(listener);
	  }

	  @Override
	  protected void lastListenerRemoved()
	  {
		target.eAdapters().remove(listener);
	    listener = null;
	  }

	@Override
	protected Object doGetValue() {
		List list = (List)target.eGet(feature);
		return list.contains(value);
	}

	@Override
	public Object getValueType() {
		return Boolean.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doSetValue(Object value) {
		Command command = createSetValueCommand(value);
		if (command != null) {
			EMFUtils.executeCommand(domain, command);
		}
	}
	
	private Command createSetValueCommand(Object value) {
		Command cmd = null;
		List list = (List)target.eGet(feature);
		boolean contains = list.contains(this.value);
		if (Boolean.TRUE == value && !contains) {
			cmd = AddCommand.create(domain, target, feature, Collections.singleton(this.value));
		} else if (Boolean.FALSE == value && contains) {
			cmd = RemoveCommand.create(domain, target, feature, Collections.singleton(this.value));
		}
		return cmd;
	}
	
}
