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
package gov.nasa.arc.spife.rcp.columns;

import gov.nasa.ensemble.common.type.stringifier.LongStringifier;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.temporal.columns.DurationSumColumn;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.emf.ecore.EAttribute;
import org.jscience.physics.amount.Amount;


public class DurationSecondsColumn extends DurationSumColumn {

	private static final EAttribute DURATION_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;
	private static final LongStringifier STRINGIFIER = new LongStringifier();
	
	public DurationSecondsColumn(IMergeColumnProvider provider) {
	    super(provider);
	    setHeaderName("Duration");
    }

	@Override
    public boolean needsUpdate(Object feature) {
	    return (feature == DURATION_FEATURE);
    }

	@Override
	public String getText(Amount<Duration> facet) {
		if (facet == null) {
			return "_";
		}
	    return STRINGIFIER.getDisplayString(facet.longValue(SI.SECOND));
	}

}
