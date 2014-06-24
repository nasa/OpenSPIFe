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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.editor.transfers;

import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanElementState;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanElementTransferable;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A transferable that represents an activity definition.
 */
public class ActivityDefTransferable extends PlanElementTransferable {
	
	/** The set of activity definitions, passed in by the constructor. */
	public final List<EActivityDef> definitions;
	
	/** List of the activity plan elements; initially empty. */
	private List<? extends EPlanElement> elements = Collections.emptyList();
	
	/**
	 * The only constructor takes a list of activity definitions and saves them
	 * @param definitions a list of activity definitions to be saved
	 */
	public ActivityDefTransferable(final List<EActivityDef> definitions) {
		this.definitions = definitions;
	}

	/** Dispose of each plan element that is itself a plan. */
	@Override
	public void dispose() {
		for (EPlanElement element : elements) {
			if (element instanceof EPlan) {
				EPlan plan = (EPlan) element;
				WrapperUtils.dispose(plan);
			}
		}
		elements = Collections.emptyList();
	}
	
	/**
	 * Set the list of plan elements to be a list of activity plan elements, one for each
	 * element of the definitions list.
	 * @param target where the transfer is to be inserted
	 * @param semantics the insertion position for activities relative to the target 
	 */
	public void instantiateElements(EPlanElement target, InsertionSemantics semantics) {
		PlanElementState state = PlanUtils.getAddLocationForActivities(target, semantics);
		EPlanParent parent = state.getParent();
		List<EActivity> activities = new ArrayList<EActivity>();
		for (EActivityDef definition : definitions) {
			activities.add(PlanFactory.getInstance().createActivity(definition, parent));
		}
		elements = Collections.unmodifiableList(activities);
	}

	/**
	 * Return a reference to the stored list of plan elements.
	 * @return a reference to the stored list of plan elements.
	 */
	@Override
	public List<? extends EPlanElement> getPlanElements() {
		return elements;
	}

	/**
	 * The print name consists of the class name followed by the definitions and the elements.
	 * @return the print name of this activity definition transferable
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append("[");
        builder.append("defs=" + definitions + ", ");
        builder.append("elements=" + getPlanElements());
        builder.append("]");
		return builder.toString();
    }
	
}
