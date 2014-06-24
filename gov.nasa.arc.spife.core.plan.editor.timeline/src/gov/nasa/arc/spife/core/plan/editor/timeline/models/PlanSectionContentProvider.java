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
package gov.nasa.arc.spife.core.plan.editor.timeline.models;

import gov.nasa.arc.spife.core.plan.timeline.PlanSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineFactory;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EStructuralFeature;

public class PlanSectionContentProvider extends GroupingTimelineContentProvider {

	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(PlanSectionContentProvider.class);

	// Sorting
	private final Map<String, PlanSectionRow> rowByName = new LinkedHashMap<String, PlanSectionRow>();
	
	private final PlanSection section;
	
	private PlanSectionRow lostRow = null;
	
	public PlanSectionContentProvider(EPlan plan, PlanSection section) {
		super(plan);
		this.section = section;
		for (PlanSectionRow row : section.getRows()) {
			rowByName.put(row.getName(), row);
		}
	}

	/**
	 * Want to return an ordered list that corresponds to the order
	 * defined by the section
	 */
	@Override
	protected Comparator getGroupingValuesComparator() {
		return new Comparator<PlanSectionRow>() {
			
			@Override
			public int compare(PlanSectionRow o1, PlanSectionRow o2) {
				if (o1 == lostRow) {
					return 1;
				} else if (o2 == lostRow) {
					return -1;
				}
				int i1 = section.getRows().indexOf(o1);
				int i2 = section.getRows().indexOf(o2);
				return i1 - i2;
			}
			
		};
	}

	@Override
	public String getName() {
		return section.getName();
	}
	
	public PlanSection getSection() {
		return section;
	}

	@Override
	protected String getValueString(Object value) {
		if (value instanceof PlanSectionRow) {
			return ((PlanSectionRow)value).getName();
		}
		return super.getValueString(value);
	}
	
	@Override
	protected List<PlanSectionRow> getActivityValues(EActivity activity) {
		List<PlanSectionRow> values = new ArrayList<PlanSectionRow>();
		List<PlanSectionRow> rows = new ArrayList<PlanSectionRow>(section.getRows());
		for (PlanSectionRow c : rows) {
			try {
				if (c.satisfies(activity)) {
					values.add(c);
				}
			} catch (Exception e) {
				LogUtil.error("error in satisfies", e);
			}
		}
		if (values.isEmpty() && section.isShowUnreferecedRow()) {
			values.add(getLostCriteria());
		}
		return values;
	}

	private PlanSectionRow getLostCriteria() {
		if (lostRow == null) {
			lostRow = PlanTimelineFactory.eINSTANCE.createPlanSectionRow();
			lostRow.setName("Untended");
		}
		return lostRow;
	}

	@Override
	protected boolean isRelevant(EStructuralFeature feature) {
		List<PlanSectionRow> rows = new ArrayList<PlanSectionRow>(section.getRows());
		for (PlanSectionRow row : rows) {
			if (row.isRelevant(feature)) {
				return true;
			}
		}
		return false;
	}
	
}
