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
package gov.nasa.ensemble.core.model.plan.diff.trees;


import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import org.eclipse.emf.ecore.EObject;

public class PlanDiffObjectNode extends PlanDiffBadgedNode {

	private EObject object;
	
	public PlanDiffObjectNode(EObject object) {
		super();
		this.object = object;
		this.name = getNameOfObject(object);
		this.diffType = DiffType.UNCHANGED;
	}
	
	protected String getNameOfObject(EObject anything) {
		return PlanDiffUtils.getObjectName(anything);
	}

	public EObject getObject() {
		return object;
	}

	public boolean parentObjectMentionedAboveInTree() {
		EObject parentObject = object.eContainer();
		if (parentObject == null) return true;
		return mentionedAbove(parentObject);
	}
	
	@Override
	public boolean mentionedHere (Object object) {
		return this.object.equals(object);
	}

	public String getParentName() {
		EObject parentObject = object.eContainer();
		if (parentObject == null) return "";
		return getNameOfObject(parentObject);
	}
	
	@Override
	public int count (Class objectClass) {
		int count = super.count(objectClass);
		if (objectClass.isInstance(object)) count++;
		return count;
	}

	@Override
	public String getCSSclass() {
		Class klass = getObject().getClass();
		if (klass.getInterfaces() != null && klass.getInterfaces().length==1) {
			klass = klass.getInterfaces()[0]; // basically, removes the "Impl"
		}
		return klass.getSimpleName();
	}
	
	@Override
	public String getCSSclassForIcon() {
		return "blackborder " + getCSSclass() + "-icon";
	}

	@Override
	public boolean isUnscheduled() {
		if (object instanceof EPlanElement) {
			TemporalMember temporalMember = ((EPlanElement)object).getMember(TemporalMember.class);
			return !temporalMember.getScheduled();
		} else return super.isUnscheduled(); // for attributes etc.
	}
	
	@Override
	public boolean isUnchanged() {
		return object instanceof EPlanElement && diffType==DiffType.UNCHANGED;
	}
	
	@Override
	public boolean isDeleted() {
		return object instanceof EPlanElement
		&& (diffType==DiffType.REMOVE || diffType==DiffType.MOVE_OUT);
	}

}
