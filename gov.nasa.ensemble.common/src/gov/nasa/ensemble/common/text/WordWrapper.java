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
package gov.nasa.ensemble.common.text;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordWrapper {
	
	private static HashMap<Integer, Pattern> patternMap = new HashMap<Integer, Pattern>();
	
	public static final String newLine = System.getProperty("line.separator", "\n");
	
	// TODO if the length is too short (i.e. less than the max word length in
	// the string), the set length to the max word length
	// TODO add a test case for the above TODO
	public static String[] wordWrapArr(String s, int length) {
		Pattern pattern = getPattern(length);
		
		List<String> list = new LinkedList<String>();
		Matcher m = pattern.matcher(s);
		while (m.find()) {
			String group = m.group();
			
			// the last match the pattern makes is an empty, so don't add it
			if (group.length() > 0)
				list.add(group);
		}
		return list.toArray(new String[list.size()]);
	}

	
	public static StringBuilder wordWrap(String s, int length) {
		
		StringBuilder tem = new StringBuilder();
		
		String[] arr = wordWrapArr(s, length);
		
		for ( String v: arr ) {
			tem.append(v).append(newLine);
		}
		
		return tem;
	}
	
	public static StringBuilder wordWrap(String s, int length, String prepend) {
		
		StringBuilder tem = new StringBuilder();
		
		String[] arr = wordWrapArr(s, length);
		
		for ( String v: arr ) {
			tem.append(prepend).append(v).append(newLine);
		}
		
		return tem;
	}

	
	private static Pattern getPattern(int length) {
		// check the hash map for a pattern with the specified length
		Pattern pattern = patternMap.get(length);
		
		if ( pattern == null ) {
			pattern = Pattern.compile(".{0,"+length+"}(?:\\S(?:-| |$)|$)");
		} 
		
		return pattern;
	}
	
}
