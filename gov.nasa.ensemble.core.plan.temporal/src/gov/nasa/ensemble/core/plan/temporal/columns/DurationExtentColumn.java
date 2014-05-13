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

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.jscience.util.EDurationStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import org.eclipse.emf.ecore.EAttribute;
import org.jscience.physics.amount.Amount;


public class DurationExtentColumn extends AbstractMergeColumn<Amount<Duration>> {

	private static final EAttribute DURATION_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;
	private static final EAttribute START_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME;
	private static final EAttribute END_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME;
	private static final IStringifier<Amount<Duration>> DURATION_STRINGIFIER = new EDurationStringifier();
	
	public DurationExtentColumn(IMergeColumnProvider provider) {
	    super(provider, "Duration Extent", 56);
    }

	@Override
    public boolean needsUpdate(Object feature) {
	    return (feature == DURATION_FEATURE) || (feature == START_FEATURE) || (feature == END_FEATURE);
    }
	
	@Override
    public Amount<Duration> getFacet(Object element) {
		if (element instanceof List) {
			List<EPlanElement> list = new ArrayList<EPlanElement>();
			for (Object object : (List) element) {
				if (object instanceof EPlanElement) {
					list.add((EPlanElement)object);
				}
			}
			Date earliest = null;
			Date latest = null;
			for (EPlanElement planElement : EPlanUtils.getConsolidatedPlanElements(list)) {
				TemporalMember member = planElement.getMember(TemporalMember.class);
				Date startTime = member.getStartTime();
				Date endTime = member.getEndTime();
				if ((earliest == null) || ((startTime != null) && startTime.before(earliest))) {
					earliest = startTime;
				}
				if ((latest == null) || ((endTime != null) && endTime.after(latest))) {
					latest = endTime;
				}
			}
			if ((earliest != null) && (latest != null)) {
				return DateUtils.subtract(latest, earliest);
			}
		}
		if (element instanceof EPlanElement) {
			EPlanElement planElement = (EPlanElement)element;
			return planElement.getMember(TemporalMember.class).getDuration();
		}
	    return null;
    }

	@Override
	public String getText(Amount<Duration> facet) {
		if (facet == null) {
			return "_";
		}
	    return DURATION_STRINGIFIER.getDisplayString(facet);
	}
	
	@Override
	public Comparator<Amount<Duration>> getComparator() {
	    return DurationComparator.INSTANCE;
	}

}
