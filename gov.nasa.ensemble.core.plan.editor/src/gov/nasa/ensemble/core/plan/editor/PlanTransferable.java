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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class PlanTransferable extends PlanElementTransferable {

	private static final long serialVersionUID = 1883844651780994439L;
	private static final String PLAN_UNIQUE_ID_KEY = "planUniqueId";
	private static final String PLAN_ELEMENTS_KEY = "planElements";
	
	@Override
	public void dispose() {
		for (EPlanElement element : getPlanElements()) {
			if (element instanceof EPlan) {
				EPlan plan = (EPlan) element;
				WrapperUtils.dispose(plan);
			}
		}
		setPlanElements(Collections.<EPlanElement>emptyList());
	}
	
	public void setPlanElements(List<? extends EPlanElement> elements) {
		map.put(PLAN_ELEMENTS_KEY, elements);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<? extends EPlanElement> getPlanElements() {
		return (List<? extends EPlanElement>)map.get(PLAN_ELEMENTS_KEY);
	}
	
	public void setPlanUniqueId(Long uniqueId) {
		map.put(PLAN_UNIQUE_ID_KEY, uniqueId);
	}

	public Long getPlanUniqueId() {
		return (Long)map.get(PLAN_UNIQUE_ID_KEY);
	}

	/**
     * Creates a map from the original transferable elements
     * to the new transferable elements.
     * @param original
     * @param copy
     * @return
     */
	public static Map<EPlanElement, EPlanElement> createCopyPlanElementMap(PlanTransferable original, PlanTransferable copy) {
		Set<? extends EPlanElement> originalElements = EPlanUtils.computeContainedElements(original.getPlanElements());
		Set<? extends EPlanElement> copyElements = EPlanUtils.computeContainedElements(copy.getPlanElements());
		Map<EPlanElement, EPlanElement> oldElementToNewPlanElement = new HashMap<EPlanElement, EPlanElement>();
		if (originalElements.size() != copyElements.size()) {
			Logger.getLogger(PlanTransferable.class).error("copy size didn't match original size");
		}
		Iterator<? extends EPlanElement> originalIterator = originalElements.iterator();
		Iterator<? extends EPlanElement> copyIterator = copyElements.iterator();
		while (originalIterator.hasNext() && copyIterator.hasNext()) {
			EPlanElement originalElement = originalIterator.next();
			EPlanElement copyElement = copyIterator.next();
			oldElementToNewPlanElement.put(originalElement, copyElement);
		}
		return oldElementToNewPlanElement;
	}
    
	
}
