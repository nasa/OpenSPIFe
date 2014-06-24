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
package gov.nasa.ensemble.core.model.plan.diff.report.html.table;

import gov.nasa.ensemble.common.EnsembleProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

class ReportDateFormats {

	static final DateFormat LINE1 = prop(
			"ensemble.plan.diff.heading.line1.date.format",
			"ensemble.plan.diff.heading.line1.date.timezone",  
			"DDD/yyyy");
	static final DateFormat LINE2 = prop(
			"ensemble.plan.diff.heading.line2.date.format",
			"ensemble.plan.diff.heading.line2.date.timezone",  
			"MMM d, yyyy HH:mm (zzz)");
	static final DateFormat POPOVER = prop(
			"ensemble.plan.diff.date.format.planned.popover",
			"ensemble.plan.diff.date.timezone.planned",
			"DDD/yyyy HH:mm");                                
	static final DateFormat MODIFICATIONS = prop(
			"ensemble.plan.diff.date.format.planned.modifications",
			"ensemble.plan.diff.date.timezone.planned",
			"MMMM d, yyyy\nHH:mm");               
	static final DateFormat ADDITIONS_START = prop(
			"ensemble.plan.diff.date.format.planned.additions.start",
			"ensemble.plan.diff.date.timezone.planned",
			"HH:mm");                         
	static final DateFormat ADDITIONS_END = prop(
			"ensemble.plan.diff.date.format.planned.additions.end",
			"ensemble.plan.diff.date.timezone.planned",
			"HH:mm 'on' MMMM d, yyyy");           

	private static DateFormat prop(String formatPropertyName, String tzPropertyName, String defaultFormat) {
		SimpleDateFormat result = new SimpleDateFormat(EnsembleProperties.getStringPropertyValue(formatPropertyName, defaultFormat));
		result.setTimeZone(TimeZone.getTimeZone(EnsembleProperties.getStringPropertyValue(tzPropertyName, "UTC")));
		return result;
	}

}
