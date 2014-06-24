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
package gov.nasa.ensemble.common.type.stringifier;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.time.ICanListDateFormats;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;
import gov.nasa.ensemble.common.type.DateFormatWithDefaultDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

public class DateStringifier extends AbstractTrimmingStringifier<Date> {

	protected final DateFormat dateFormat;
	
	private Date earliestAcceptableDate = EnsembleProperties.getDatePropertyValue("date.limit.earliest");
	private Date latestAcceptableDate = EnsembleProperties.getDatePropertyValue("date.limit.latest");
	
	/**
	 * Warning: to get an appropriate date stringifier you should call
	 * StringifierRegistry.getStringifier(...)
	 */	
	public DateStringifier(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	@Override
	public String getDisplayString(Date javaObject) {
		String result = null;
		if (dateFormat==null) return javaObject.toString() + " (note: invalid date format in configuration)";
		if (javaObject != null) { 
			result = dateFormat.format(javaObject);
		}
		return (result == null ? "" : result);
	}

	@Override
	public Date getJavaObjectFromTrimmed(String string, Date defaultObject) throws ParseException {
		//SPF-4781:  I believe the defaultObject = new Date() line is obsolete due to SPF-4670 and SPF-4782 fixes.
		//The "validation" referred to would be from either Goto command that now passes a default,
		//or the Earliest and Latest commands that no longer accept a date at all.
		//If any future callers pass null, it means that a time with no date is invalid.
//		if (defaultObject==null) defaultObject = new Date(); // Otherwise validation will always reject time-only shorthands
		if (dateFormat==null) throw new ParseException("An unknown date format name is configured.", 0);
		try {
			Date result;
			if (dateFormat instanceof DateFormatWithDefaultDate) {
				DateFormatWithDefaultDate format = (DateFormatWithDefaultDate) dateFormat;
//				System.err.print("Parsing '" + string + "' with default of " + defaultObject);
				result = format.parse(string, new ParsePosition(0), defaultObject);
//				System.err.println(" returns " + result);
			} else {
				result = dateFormat.parse(string);
			}
			if (result==null) return result;
			if (earliestAcceptableDate != null && result.before(earliestAcceptableDate)) {
				throw new ParseException("Start time must not be before " + getDisplayStringForErrorMessage(earliestAcceptableDate), 0);
			}
			if (latestAcceptableDate != null && result.after(latestAcceptableDate)) {
				throw new ParseException("End time must not be after " + getDisplayStringForErrorMessage(latestAcceptableDate), 0);
			}
			return result;
		}
		catch (ParseException e) {
			throw e;
		}
		catch (NumberFormatException e) {
			//throw e;
		}
		return throwHelpfulException(defaultObject);
	}
	
	protected Date throwHelpfulException(Date defaultObject) throws ParseException {
		Date exampleDate = (defaultObject != null ? defaultObject : new Date());
		if (dateFormat instanceof ICanListDateFormats) {
			throw new ParseException(((ICanListDateFormats)dateFormat).getHelpString(defaultObject != null), 0);
		} else {
			throw new ParseException("Please type a date in this format:\n  \""
					+ getDisplayString(exampleDate) + "\"\nAs a shorthand, you may be able to type just the time, like 0800, 8:00, or 1345.", 0);
		}
	}

	private String getDisplayStringForErrorMessage(Date date) {
		// Don't use regular string, in case it's missing year, and also because it messed up a heuristic:  see SPF-10620.
		return ISO8601DateFormat.formatISO8601(date);
	}

	public Date getEarliestAcceptableDate() {
		return earliestAcceptableDate;
	}

	public void setEarliestAcceptableDate(Date earliestAcceptableDate) {
		this.earliestAcceptableDate = earliestAcceptableDate;
	}

	public Date getLatestAcceptableDate() {
		return latestAcceptableDate;
	}

	public void setLatestAcceptableDate(Date latestAcceptableDate) {
		this.latestAcceptableDate = latestAcceptableDate;
	}


}
