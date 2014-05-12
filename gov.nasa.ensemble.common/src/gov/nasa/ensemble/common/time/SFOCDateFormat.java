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
package gov.nasa.ensemble.common.time;


import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * <p>SFOC (aka SCET) time is basically ISO8601 as
 * YYYY-DOYTHH:MM:SS.SSS (DOY = day of year, as opposed to MM-DD) but
 * the timezone is implied as GMT and DOES NOT include the "Z" at the
 * end that ISO8601 uses to indicate GMT.  This class extends the
 * ISO8601DateFormat class to handle SFOC time as follows:</p>
 *
 * <ol>
 *
 * <li>It forces the timezone to always be GMT.
 *
 * <li>It tacks on a "Z" to Strings being parsed so that the
 * ISO8601DateFormat parse() method will interpret the time in GMT and
 * not the local timezone.
 *
 * <li>It calls ISO8601DateFormat.formatWithoutTimeZone from its
 * format method since the timezone is implied in SFOC time
 *
 * <li>It configures the underlying ISO8601DateFormat class to output
 * Strings from its format() method in the YYYY-DOY (day of year)
 * style and not the YYYY-MM-DD style.  Note that both of these are
 * valid according to ISO 8601.
 *
 * </ol>
 *
 *  NOTE: NOT MT SAFE!  Build multiple SFOCDateFormat objects for
 * separate threads or synchronize!
 *
 * <p>Copyright 2003, California Institute of Technology.<br>
 * ALL RIGHTS RESERVED. U.S. Government Sponsorship acknowledged.</p>
 *
 * @author Jeff Norris
 **/
@SuppressWarnings("serial")
public class SFOCDateFormat extends ISO8601DateFormat {

	/* CONSTRUCTORS */
	
  public SFOCDateFormat() {
    super();
    // Force YYYY-DOY formatting instead of YYYY-MM-DD
    doyFormatMode = true;
  }

	/* PUBLIC INTERFACE */
	
	/* Class Constants */

	/* Mutators */
  /**
   * <p>Overrides superclass implementation to tack a "Z" onto the end
   * of the provided text so that the ISO8601DateFormat.parse method
   * correctly interprets the time relative to the GMT timezone.</p>
   */
  @Override
public Date parse(String text, ParsePosition pos) {
    text += "Z";
    return super.parse(text,pos);
  }

  /**
   * <p>Overrides superclass implementation to trim off the trailing
   * "Z" that indicates the GMT timezone since this is implied in the
   * SFOC format.</p>
   */
  @Override
public StringBuffer format(Date date, StringBuffer sbuf,
			     FieldPosition fieldPosition) {
   
    return super.formatWithoutTimeZone(date, sbuf, fieldPosition);
  }

        /* Accessors */
	
	/* Common Interface */
	
	//public String toString();
	//public boolean equals(Object obj);
  //public int hashCode();
	//protected Object clone() throws CloneNotSupportedException;
	//protected void finalize() throws Throwable;

	//public static void main(String arg[]);
	
	/* PRIVATE METHODS */
	
	/* CLASS AND OBJECT ATTRIBUTES */
	
}


