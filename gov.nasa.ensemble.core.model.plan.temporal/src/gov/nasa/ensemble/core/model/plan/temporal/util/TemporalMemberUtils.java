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
package gov.nasa.ensemble.core.model.plan.temporal.util;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl;
import gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPropagation;
import gov.nasa.ensemble.dictionary.EActivityDef;

import javax.measure.quantity.Duration;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.jscience.physics.amount.Amount;

public class TemporalMemberUtils {

	public static boolean hasDurationFormula(TemporalMember temporalMember) {
		EPlanElement ePlanElement = temporalMember.getPlanElement();
		if (ePlanElement instanceof EActivity) {
			EActivity eActivity = (EActivity)ePlanElement;
			EObject eObject = eActivity.getData();
			if (eObject instanceof DynamicEObjectImpl) {
				DynamicEObjectImpl dynamicEObjectImpl = (DynamicEObjectImpl)eObject;
				EClass eClass = dynamicEObjectImpl.eClass();
				if(eClass instanceof EActivityDef) {
					EActivityDef eActivityDef = (EActivityDef) eClass;
					String duration = eActivityDef.getDuration();
					if ((duration != null) && duration.trim().length() > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/** 
	 * Moves start time and/or end time so that element contains but does not extend past its children.
	 * @param parent -- element whose bounds to change
	 */
	public static void fitToChildren(EPlanParent parent, Amount<Duration> minDurationIfFirstChild) {
		TemporalMember member = parent.getMember(TemporalMember.class);
		// If first child added, assume the initial bounds were arbitrary and change them rather than expanding.
		if (parent.getChildren().size()==1) {
			member.setExtent(parent.getChildren().get(0).getMember(TemporalMember.class).getExtent());
			if (member.getDuration().isLessThan(minDurationIfFirstChild)) {
				member.setCalculatedVariable(CalculatedVariable.END);
				member.setDuration(minDurationIfFirstChild);
			}
		} else if (member instanceof TemporalMemberImpl) {
			TemporalPropagation.updateThisParentFromChildren((TemporalMemberImpl) member, null);
		} else {
			LogUtil.error("Temporal member of " + parent + " is of an unexpected class, " + member.getClass().getCanonicalName());
		}
		if (parent instanceof EPlan) {
			PlanTemporalMember ptm = parent.getMember(PlanTemporalMember.class); // currently same as TemporalMember if it exists
			if (ptm != null) {
				ptm.setStartBoundary(member.getStartTime());
				ptm.setEndBoundary(member.getEndTime());
			}
		}
	}

}
