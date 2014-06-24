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

public class StringEscapeFormat {
	/**
	 * Escape characters.
	 */
	public final static String STR_AMP = "&amp;"; //$NON-NLS-1$

	public final static String STR_BACKSLASH = "&#92;"; //$NON-NLS-1$	

	public final static String STR_APOS = "&apos;"; //$NON-NLS-1$

	public final static String STR_CR = "&#13;"; //$NON-NLS-1$

	public final static String STR_GT = "&gt;"; //$NON-NLS-1$

	public final static String STR_LT = "&lt;"; //$NON-NLS-1$

	public final static String STR_LF = "&#10;"; //$NON-NLS-1$	

	public final static String STR_QUOT = "&quot;"; //$NON-NLS-1$

	public final static String STR_TAB = "&#9;"; //$NON-NLS-1$

	@SuppressWarnings("unused")
	private static final String CRLF = "\r\n"; //$NON-NLS-1$

	/**
	 * Private constructor to prevent this class from being instantiated. All
	 * methods in this class should be static.
	 */
	private StringEscapeFormat() {
		// do nothing
	}
	
	/**
	 * Escapes a string attribute to make it parser friendly.
	 * 
	 * @param str
	 *            The attribute string.
	 * @return The escaped string.
	 */
	public static String escapeAttr(String str) {
		if (str == null || str.length() == 0)
			return ""; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			switch (ch) {
			case '<':
				sb.append(STR_LT);
				break;
			case '&':
				sb.append(STR_AMP);
				break;
			case '"':
				sb.append(STR_QUOT);
				break;
			default:
				sb.append(ch);
				break;
			}
		}
		return sb.toString();
	}
	
	public static String encodeSpecialALTCharacters(String s)
	{
	    StringBuffer out = new StringBuffer();
	    for(int i=0; i<s.length(); i++)
	    {
	        char c = s.charAt(i);
	        if(c > 127 || c=='&')
	        {
	           out.append("&#"+(int)c+";");
	        }
	        else
	        {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}


	/**
	 * Escapes the given string to make it parser friendly.
	 * 
	 * @param str
	 *            The source string.
	 * @return The escaped string.
	 */
	public static String escape(String str) {
		if (str == null || str.length() == 0)
			return ""; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			switch (ch) {
			case '<':
				sb.append(STR_LT);
				break;
			case '>':
				sb.append(STR_GT);
				break;
			case '&':
				sb.append(STR_AMP);
				break;
			case '"':
				sb.append(STR_QUOT);
				break;
			case '\'':
				sb.append(STR_APOS);
				break;
			case '\r':
				sb.append(STR_CR);
				break;
			case '\n':
				sb.append(STR_LF);
				break;
			case '\\':
				sb.append(STR_BACKSLASH);
				break;
			default:
				sb.append(ch);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * Escapes the given string to make it parser friendly.
	 * 
	 * @param str
	 *            The source string.
	 * @param ignoreCRLF
	 *            If true, do not escape the CR and LF characters.
	 * @return The escaped string.
	 */
	public static String escape(String str, boolean ignoreCRLF) {
		if (str == null || str.length() == 0)
			return ""; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			switch (ch) {
			case '<':
				sb.append(STR_LT);
				break;
			case '>':
				sb.append(STR_GT);
				break;
			case '&':
				// This is to avoid the double escaping (see Bugzilla 179921)
				if (!str.startsWith(STR_LT+"p/", i) && !str.startsWith(STR_APOS, i) //$NON-NLS-1$ 
						&& !str.startsWith(STR_AMP, i))
					sb.append(STR_AMP);
				else
					sb.append(ch);
				break;
			case '"':
				sb.append(STR_QUOT);
				break;
			case '\'':
				sb.append(STR_APOS);
				break;
			case '\r':
				if (ignoreCRLF)
					sb.append(ch);
				else
					sb.append(STR_CR);
				break;
			case '\n':
				if (ignoreCRLF)
					sb.append(ch);
				else
					sb.append(STR_LF);
				break;
			default:
				sb.append(ch);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * Unescapes the given string.
	 * 
	 * @param str
	 *            The source string.
	 * @return The escaped string.
	 */
	public static String unescape(String str) {
		if (str == null || str.length() == 0)
			return ""; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			switch (ch) {
			case '&':
				if (str.startsWith(STR_LT, i)) {
					sb.append('<');
					i += 3;
				} else if (str.startsWith(STR_GT, i)) {
					sb.append('>');
					i += 3;
				} else if (str.startsWith(STR_AMP, i)) {
					sb.append('&');
					i += 4;
				} else if (str.startsWith(STR_QUOT, i)) {
					sb.append('"');
					i += 5;
				} else if (str.startsWith(STR_APOS, i)) {
					sb.append("\'"); //$NON-NLS-1$
					i += 5;
				} else if (str.startsWith(STR_CR, i)) {
					sb.append('\r');
					i += 4;
				} else if (str.startsWith(STR_LF, i)) {
					sb.append('\n');
					i += 4;
				} else {
					sb.append(ch);
				}
				break;
			default:
				sb.append(ch);
				break;
			}
		}
		return sb.toString();
	}
}
