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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.AbstractObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class monitors a specific attribute across instances and fires a change notification if
 * any one of the elements changed
 */

public final class MemberAttributeOfListObservable extends AbstractObservableList {
	
	private final Class<? extends EMember> memberClass;
	private final List<EPlanElement> elements;
	private final EStructuralFeature feature;
	private List<IObservableValue> list = null;

	private final Listener listener = new Listener();

	public MemberAttributeOfListObservable(Class<? extends EMember> memberClass, List<EPlanElement> elements, EStructuralFeature feature) {
		this.memberClass = memberClass;
		this.elements = elements;
		this.feature = feature;
	}

	@Override
	protected void firstListenerAdded() {
		for (IObservableValue v : getObservables()) {
			v.addChangeListener(listener);
		}
		super.firstListenerAdded();
	}

	@Override
	protected void lastListenerRemoved() {
		for (IObservableValue v : getObservables()) {
			v.removeChangeListener(listener);
		}
		super.lastListenerRemoved();
	}

	@Override
	protected int doGetSize() {
		return getObservables().size();
	}

	@Override
	public Object get(int index) {
		return getObservables().get(index);
	}

	@Override
	public Object getElementType() {
		return EMember.class;
	}

	protected List<IObservableValue> getObservables() {
		if (list == null) {
			list = new ArrayList<IObservableValue>();
			for (EPlanElement pe : elements) {
				list.add(EMFObservables.observeValue(pe.getMember(memberClass), feature));
			}
		}
		return list;
	}

	private final class Listener implements IChangeListener {
		@Override
		public void handleChange(ChangeEvent event) {
			getRealm().asyncExec(new Runnable() {

				@Override
				public void run() {
					fireChange();
				}
				
			});
		}
	}
	
}
