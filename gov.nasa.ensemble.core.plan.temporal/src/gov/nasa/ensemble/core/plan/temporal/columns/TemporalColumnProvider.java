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

import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TemporalColumnProvider implements IMergeColumnProvider {
	
	@Override
	public List<? extends AbstractMergeColumn<?>> getColumns() {
		List<AbstractMergeColumn<?>> columns = new ArrayList<AbstractMergeColumn<?>>();
		columns.add(new StartTimeParameterColumn(this));
		columns.add(new ScheduledColumn(this));
		columns.add(new DurationSumColumn(this));
		columns.add(new DurationExtentColumn(this));
		for (Timepoint timepoint : Timepoint.values()) {
			appendColumn(columns, timepoint, DateFormatRegistry.DFID_SFOC);
			appendColumn(columns, timepoint, DateFormatRegistry.DFID_ISO8601);
		}
		columns.add(new DateFormatTimepointParameterColumn(this, Timepoint.END));
		columns.add(new TemporalParameterColumn(this, "Start Offset", 56, TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_AMOUNT));
		columns.add(new TemporalParameterColumn(this, "Start Timepoint", 70, TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT));
		columns.add(new TemporalParameterColumn(this, "End Offset", 56, TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_AMOUNT));
		columns.add(new TemporalParameterColumn(this, "End Timepoint", 70, TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT));
		return columns;
	}
	
	private void appendColumn(List<AbstractMergeColumn<?>> columns, Timepoint timepoint, String dfid) {
		DateFormat dateFormat = DateFormatRegistry.INSTANCE.lookupDateFormat(dfid);
		if (dateFormat == null) {
			Logger.getLogger(TemporalColumnProvider.class).warn("no date format found for id '"+dfid+"'");
		} else {
			DateFormatTimepointParameterColumn column = new DateFormatTimepointParameterColumn(this, dateFormat, timepoint);
			if (column != null) {
				columns.add(column);
			}
		}
	}

	@Override
	public String getName() {
		return "Temporal";
	}
	
}
