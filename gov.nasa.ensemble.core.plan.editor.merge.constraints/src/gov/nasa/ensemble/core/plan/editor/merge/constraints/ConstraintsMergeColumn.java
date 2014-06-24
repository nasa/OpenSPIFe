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
package gov.nasa.ensemble.core.plan.editor.merge.constraints;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

public class ConstraintsMergeColumn extends AbstractMergeColumn<Integer> {

	private static Image CONSTRAINT_PRESENT_IMAGE = ConstraintsMergePlugin.createIcon("constraint_present_icon.gif");

	public ConstraintsMergeColumn(IMergeColumnProvider provider) {
		super(provider, "Constraints", 35);
	}
	
	@Override
	public Integer getFacet(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement planElement = (EPlanElement) element;
			ConstraintsMember constraintsMember = planElement.getMember(ConstraintsMember.class, false);
			if (constraintsMember != null) {
				List<BinaryTemporalConstraint> constraints = constraintsMember.getBinaryTemporalConstraints();
				return constraints.size();
			}
		}
		if (element instanceof List) {
			List list = (List) element;
			Set<BinaryTemporalConstraint> constraints = new LinkedHashSet<BinaryTemporalConstraint>();
			for (Object object : list) {
				if (object instanceof EPlanElement) {
					EPlanElement planElement = (EPlanElement) object;
					ConstraintsMember constraintsMember = planElement.getMember(ConstraintsMember.class, false);
					if (constraintsMember != null) {
						constraints.addAll(constraintsMember.getBinaryTemporalConstraints());
					}
				}
			}
			return constraints.size();
		}
		return 0;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		return (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS);
	}
	
	@Override
	public String getText(Integer facet) {
		if ((facet != null) && (facet.intValue() != 0)) {
			return facet.toString();
		}
		return "";
	}
	
	@Override
	public Image getImage(Integer facet) {
		if ((facet != null) && (facet.intValue() != 0)) {
			return CONSTRAINT_PRESENT_IMAGE;
		}
		return null;
	}

}
