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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanElementState;
import gov.nasa.ensemble.core.plan.PlanUtils;

import java.util.List;

public class PlanInsertionLocation extends PlanStructureLocation {

	private EPlanElement target;
	private PlanElementState insertionState;

	/**
	 * Only use this constructor if you can guarantee that 
	 * the inserted items are not being moved 
	 * from the parent they are being inserted into.
	 * 
	 * @param target
	 * @param insertionState
	 */
	public PlanInsertionLocation(final EPlanElement target, final PlanElementState insertionState) {
		this.target = target;
		this.insertionState = insertionState;
	}
	
	/**
	 * Construct a PlanInsertionLocation.
	 * If the parameters aren't compatible, the 
	 * insertionState will be null after construction.
	 * 
	 * @param target
	 * @param semantics
	 * @param insertingElements
	 */
	public PlanInsertionLocation(final EPlanElement target, final InsertionSemantics semantics, final List<? extends EPlanElement> insertingElements) {
		this.target = target;
		PlanElementState state;
		if (insertingElements.get(0) instanceof EActivity) {
			state = PlanUtils.getAddLocationForActivities(target, semantics);
		} else {
			state = PlanUtils.getAddLocationForActivityGroups(target, semantics);
		}
		if (state != null) {
			EPlanParent parent = state.getParent();
			if (parent != null) {
				int index = state.getIndex();
				int moveCount = 0;
				int i = 0;
				for (EPlanElement child : EPlanUtils.getChildren(parent)) {
					if (i++ >= index) {
						break;
					}
					if (insertingElements.contains(child)) {
						moveCount++;
					}
				}
				if (moveCount != 0) {
					state = new PlanElementState(parent, index - moveCount);
				}
			}
		}
		this.insertionState = state;
	}
	
	public EPlanElement getTarget() {
		return target;
	}
	
	public void setTarget(EPlanElement target) {
		this.target = target;
	}

	public PlanElementState getInsertionState() {
		return insertionState;
	}
	
	public void setInsertionState(PlanElementState insertionState) {
		this.insertionState = insertionState;
	}
	
    @Override
	public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append("[");
        builder.append("element=" + getTarget() + ",");
        builder.append("insertionState=" + getInsertionState());
        builder.append("]");
		return builder.toString();
    }
	
}
