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
import gov.nasa.ensemble.core.jscience.util.EDurationStringifier;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.emf.ecore.EAttribute;
import org.jscience.physics.amount.Amount;


public class DurationSumColumn extends AbstractMergeColumn<Amount<Duration>> {

	private static final EAttribute DURATION_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;
	private static final IStringifier<Amount<Duration>> DURATION_STRINGIFIER = new EDurationStringifier();
	
	public DurationSumColumn(IMergeColumnProvider provider) {
	    super(provider, "Duration Sum", 56);
    }

	@Override
    public boolean needsUpdate(Object feature) {
	    return (feature == DURATION_FEATURE);
    }
	
	@Override
    public Amount<Duration> getFacet(Object element) {
		if (element instanceof List) {
			Set<EActivity> activities = new LinkedHashSet<EActivity>();
			Set<EPlanElement> parents = new LinkedHashSet<EPlanElement>();
			for (Object object : (List) element) {
				if (object instanceof EActivity) {
					activities.add((EActivity)object);
				} else if (object instanceof EPlanElement) {
					parents.add((EPlanElement)object);
				}
			}
			activities.addAll(EPlanUtils.computeContainedActivities(parents));
			Amount<Duration> durationTotal = null;
			for (EActivity activity : activities) {
				Amount<Duration> duration = activity.getMember(TemporalMember.class).getDuration();
				if (durationTotal == null) {
					durationTotal = duration;
				} else if (duration != null) {
					durationTotal = durationTotal.plus(duration);
				}
			}
			return durationTotal;
		}
		if (element instanceof EPlanChild) {
			EPlanChild planElement = (EPlanChild)element;
			if (planElement instanceof EActivity) {
				return planElement.getMember(TemporalMember.class).getDuration();
			}
			return getFacet(EPlanUtils.getChildren(planElement));
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
