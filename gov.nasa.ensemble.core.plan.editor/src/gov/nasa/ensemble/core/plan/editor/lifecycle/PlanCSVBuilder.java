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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.core.activityDictionary.AttributeDef;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;

import java.beans.FeatureDescriptor;
import java.util.List;

public class PlanCSVBuilder {
	
	protected static final String NEWLINE = System.getProperty("line.separator");

	private boolean quote = true; // Change default to use quoted items
	
	public String buildCSV(List<String[]> rows) {
		return buildCSV(rows, ',');
	}
	
	public String buildCSV(List<String[]> rows, char delimiter) {
		StringBuilder result = new StringBuilder();
		
		// add fields
		for (String[] rowData : rows) {
			StringBuilder row = new StringBuilder();
			// add header row
			int numcols = 0;
			for (String value : rowData) { // assumes keys are by default in orer  
				// add a , if needed
				if(row.length() > 0) {
					row.append(delimiter);
					numcols++;
				}
				
				//	If value was unset skip it.
				if (value == null || value.length() == 0) {
					// If the value is null.
					// put in a spacer.
					value = " ";
				}
				
				// Quote the value to add ""
				
				String text = value;
				if (quote) {
					text = quote(value);
				}
				row.append(text);
			}
			
			result.append(row);
			result.append(NEWLINE); // newline
		}
		
		return result.toString();
	}

	public void setQuote(boolean quote) {
		this.quote = quote;
	}

	protected final String quote(String value) {
		
		// Can't quote empty values.
		if (value == null || value.equals("")) {
			return "";
		}
		
		// replace any quotes with a DOUBLED  quote.
		value = value.replace("\"", "\"\"");
		
		// Add quotes for the comma value
		return "\"" + value + "\"";
	}
	
	public static class ColumnDescriptor extends FeatureDescriptor {
		
		/** The key to use to access the values. Typically done as a PlanElement parameter lookup. */
		private String key;
		
		/** Name to appear at the top of the column. Can be null, in which case the key is used */
		private String displayName;

		public ColumnDescriptor() {
			// default constructor
		}

		public ColumnDescriptor(String key) {
			this.key = key;
		}
		
		public ColumnDescriptor(AttributeDef def) {
			this.key = def.getName();
			this.displayName = ParameterDescriptor.getInstance().getDisplayName(def);
		}
		
		public ColumnDescriptor(String key, String type, String displayName) {
			super();
			this.key = key;
			this.displayName = displayName;
		}
		
		// 
		// Property Accessors
		// 
		
		@Override
		public String getDisplayName() {
			return displayName;
		}

		@Override
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}
	
}
