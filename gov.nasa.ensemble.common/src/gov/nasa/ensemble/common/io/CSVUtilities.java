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
package gov.nasa.ensemble.common.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVUtilities {
	
	private static final String CRLF = new String(new char[] {(char) 0x0D, (char) 0x0A});
	private static final char DOUBLE_QUOTE = '"';
	private static final String UNESCAPED_DOUBLE_QUOTE = Character.toString(DOUBLE_QUOTE);
	private static final String ESCAPED_DOUBLE_QUOTE
			= UNESCAPED_DOUBLE_QUOTE + UNESCAPED_DOUBLE_QUOTE;
	
	/** Parse a comma-separated value line.  Does not trim whitespace. */
	public static List<String> parseCSVLine(final BufferedReader in) throws IOException {
		return parseCSVLine(in, false);
	}
	/** Parse a comma-separated value line.  Does not trim whitespace. */
	public static List<String> parseCSVLine(String in) throws IOException {
		return parseCSVLine(in, false);
	}
	
	/**
	 * Parse a comma-separated value line.
	 * @param in Reader
	 * @param trim true to trim whitespace if not double-quoted.
	 * @return list of cell contents
	 * @throws IOException
	 */
	public static List<String> parseCSVLine(final BufferedReader in, boolean trim) throws IOException {
		return parseCSVLine(new Lines() {
			@Override
			public String readLine() throws IOException {
				return in.readLine();
			}
		}, trim);
	}
	
	/**
	 * Parse a comma-separated value line.
	 * @param in String
	 * @param trim true to trim whitespace if not double-quoted.
	 * @return list of cell contents
	 * @throws IOException
	 */
	public static List<String> parseCSVLine(String in, boolean trim) throws IOException {
		if (in == null) {
			return Collections.emptyList();
		}
		final String[] lines = in.split("\n");
		return parseCSVLine(new Lines() {
			int i = 0;
			@Override
			public String readLine() {
				return lines[i++];
			}
		}, trim);
	}
	
	private static List<String> parseCSVLine(Lines in, boolean trim) throws IOException {
		String line = in.readLine();
		if (line == null) {
			return null; // If eof return
		}
		
		List<String> contents = new ArrayList<String>();
		
		boolean reachedEnd = false;
		// Add any extra columns
		do {
			String element = "";

			// now need to check where things are
			int quoteIndex = line.indexOf("\"");
			
			// found a quote, get second end quote
			if (quoteIndex == 0) {
				int quoteIndex2 = line.indexOf("\"", quoteIndex +1);
				
				while (quoteIndex2 < (line.length()-1) && line.charAt(quoteIndex2 + 1) == '"') {
					// Found a ""
					if (line.charAt(quoteIndex2 + 1) == '"') {
						quoteIndex2++;
					}
					
					// Keep looking
					quoteIndex2 = line.indexOf("\"", quoteIndex2+1);
					
					if ( quoteIndex2 == -1) {
						line += "\n " + in.readLine();
					}
				}
				
				element = line.substring(quoteIndex, quoteIndex2+1); // include "
				
				// update line
				if (line.length() >= quoteIndex2+2) {
					line = line.substring(quoteIndex2+2); //add one to skip the" and add 1 to skip the ,
				}
				else {
					reachedEnd  = true;
				}
			}
			else {
				int commaIndex = line.indexOf(',');
				reachedEnd = commaIndex == -1;
				if (reachedEnd) {
					element = line;
				} else {
					element = line.substring(0, commaIndex); // do not include ,
				}
				// update line
				if (!reachedEnd) {
					line = line.substring(commaIndex+1);
				}
				else {
					line = "";
				}
			}
			if (trim && !isDoubleQuoted(element)) {
				contents.add(dequote(element).trim());
			} else {
				contents.add(dequote(element));
			}
		} while (!reachedEnd);
		
		return contents;
		
	}
	
	public static String dequote(String value) {
		if (isDoubleQuoted(value)) {
			// remove end and beginning quote
			value = value.substring(1, value.length()-1);
			// Check for "" to indicate a single quote
			value = value.replaceAll("\"\"", "\"");
		}

		// handle the case where we inserted a space
		if (value.equals(" ")) {
			return "";
		}

		return value;
	}
	
	private static boolean isDoubleQuoted(String value) {
		return value != null && value.length() > 0
			&& value.charAt(0) == '"' && value.charAt(value.length()-1) == '"';	
	}
	
	/**
	 * Write one row of a CSV file.
	 * @param out -- stream
	 * @param fields -- printed representation of cell contents
	 * @see RFC-4180
	 */
	public static StringBuffer writeCSV(String[] fields) {
		StringBuffer sb = null;
		for (String field : fields) {
			sb = append(sb, field);
		}
		if (sb == null) {
			sb = new StringBuffer();
		}
		sb.append(CRLF);
		return sb;
	}
	
	public static StringBuffer append(StringBuffer csv, String field) {
		if (csv == null) {
			return new StringBuffer(quote(field));
		}
		return csv.append(',' + quote(field));
	}
	
	public static String quote(String s) {
		return DOUBLE_QUOTE 
		+ s.replaceAll(UNESCAPED_DOUBLE_QUOTE, ESCAPED_DOUBLE_QUOTE)
		+ DOUBLE_QUOTE;
	}
	
	private interface Lines {
		public String readLine() throws IOException;
	}
}
