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

import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.temporal.columns.DateFormatTimepointParameterColumn;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/** 
 * "The time format for "Start Time" and "End Time" needs to be: 24 Aug 2013 23:20:00 GMT 
 * 
 * @author kanef
 *
 */
public class SPIFeColumnProvider implements IMergeColumnProvider {


	@Override
	public List<? extends AbstractMergeColumn<?>> getColumns() {
		List<AbstractMergeColumn<?>> columns = new ArrayList<AbstractMergeColumn<?>>();
		columns.add(new DurationSecondsColumn(this));
		//                                           "24 Aug 2013 23:20:00 GMT"
		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss 'GMT'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		for (Timepoint timepoint : Timepoint.values()) {
			appendDateColumn(columns, timepoint, dateFormat);
		}
		return columns;
	}
	
	private void appendDateColumn(List<AbstractMergeColumn<?>> columns, Timepoint timepoint, DateFormat dateFormat) {
		DateFormatTimepointParameterColumn column = new SPIFeDateFormatTimepointParameterColumn(this, dateFormat, timepoint);
		if (column != null) {
			columns.add(column);
		}
	}

	@Override
	public String getName() {
		return "SPIFe";
	}

	public class SPIFeDateFormatTimepointParameterColumn extends DateFormatTimepointParameterColumn {

		public SPIFeDateFormatTimepointParameterColumn(
				IMergeColumnProvider provider, DateFormat format,
				Timepoint timepoint) {
			super(provider, format, timepoint);
			setHeaderName(capitalize(timepoint.getName()) + " Time");
		}

		private String capitalize(String string) {
			return string.toUpperCase().substring(0,1)
			+ string.toLowerCase().substring(1);
		}
	
	}
}
