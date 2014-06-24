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
package gov.nasa.ensemble.core.plan.temporal.columns;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterFacet;

import org.eclipse.emf.ecore.EStructuralFeature;

public class TemporalParameterColumn extends ParameterColumn {

	private final EStructuralFeature structuralFeature;

	public TemporalParameterColumn(IMergeColumnProvider provider, String displayName, int defaultWidth, EStructuralFeature feature) {
		super(provider, displayName, defaultWidth);
		this.structuralFeature = feature;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		if (feature == structuralFeature) {
			return true;
		}
		return super.needsUpdate(feature);
	}

	@Override
	public ParameterFacet getFacet(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement ePlanElement = (EPlanElement) element;
			TemporalMember member = ePlanElement.getMember(TemporalMember.class);
			Object value = member.eGet(structuralFeature);
			return new ParameterFacet(member, structuralFeature, value);
		}
		return null;
	}

}
