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
package gov.nasa.ensemble.core.plan.editor.merge.contributions;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;

/**
 * Mission extendable class to allow contributions from the EPlan 
 * to the Table and Days editor.
 * 
 * @see LASSTablePlanContributor-example in SPIFe's LASS product.
 * 
 * @author ideliz
 */
public class MergeTreePlanContributor implements MissionExtendable {
	
	private static MergeTreePlanContributor INSTANCE;
	
	public static MergeTreePlanContributor getInstance() {
		if (INSTANCE == null) {
			try {
				INSTANCE = MissionExtender.construct(MergeTreePlanContributor.class);
			} catch (ConstructionException e) {
				INSTANCE = new MergeTreePlanContributor();
			}
		}
		return INSTANCE;
	}
	
	/**
	 * Return contributions from the EPlan to display in the editors.
	 * Override in subclass.
	 * @param plan
	 * @return contributions
	 */
	public List<Object> getContributions(EPlan plan) {
		return null;
	}

	/**
	 * Because the Table/Days editor columns are specifically designed for EPlanElements
	 * and it's features you have to map the contribution objects to the column features
	 * to view the values. Override in subclass.
	 * 
	 * @param object
	 * @param feature
	 * @return value mapped for the columns feature feature
	 */
	public Object getValueForFeature(EObject object, EStructuralFeature feature) {
		return object.eGet(feature);
	}
	
	/**
	 * For filtering purposes. Find the EObject's extent and compare it to 'extent'.
	 * Return true if they match.  Override in subclass.
	 * @param object
	 * @param extent
	 * @return is the object extent in extent
	 */
	public boolean intersectsExtent(EObject object, TemporalExtent extent) {
		return true;
	}
	
}
