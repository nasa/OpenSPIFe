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
package gov.nasa.ensemble.common.ui.preferences.time;

import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.ui.PickListFieldEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.swt.widgets.Composite;

public class DateFormatPicklistFieldEditor extends PickListFieldEditor {

	public DateFormatPicklistFieldEditor(
			String name, 
			String labelText, 
			Composite parent, 
			boolean ordered) {
		super(name, labelText, parent, addAbbreviations(DateFormatRegistry.INSTANCE.getDateFormatIDs()), ordered);
	}

	private static Collection<String> addAbbreviations(Collection<String> dateFormatIDs) {
		List<String> idsWithAbbrevs = new ArrayList<String>();
		for (String id : dateFormatIDs) {
			// SPF-8460 -- MET and LST should not have time zone abbreviation
			if (id.equals("MET") || id.equals("LST")) {
				idsWithAbbrevs.add(id);
				continue;
			}
			TimeZone zone = TimeZone.getTimeZone(id);
			String code = zone.getDisplayName(false, TimeZone.SHORT);
			String dlt = zone.getDisplayName(true, TimeZone.SHORT);
			if (!dlt.equals(code)) {
				code += "/" + dlt;
			}
			idsWithAbbrevs.add(id + " - " + code);
		}
		return idsWithAbbrevs;
	}

	@Override
	protected String createList(String[] items) {
		List<String> dateFormatIDs = new ArrayList<String>();
		for (String item : items) {
			int index = item.indexOf('-');
			if (index > 0) {
				item = item.substring(0, index).trim();
			}
			dateFormatIDs.add(item);
		}
		return super.createList(dateFormatIDs.toArray(new String[items.length]));
	}

	@Override
	protected String[] parseString(String stringList) {
		String[] dateFormatIDs = super.parseString(stringList);
		Collection<String> idsWithAbbrevs = addAbbreviations(Arrays.asList(dateFormatIDs));
		return idsWithAbbrevs.toArray(new String[idsWithAbbrevs.size()]);
	}
	
	

}
