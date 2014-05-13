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
package gov.nasa.ensemble.dictionary.nddl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public final class NDDLUtil {
	/**
	 * Escape characters.
	 */
	public final static String EXCLAMATIONMARK = "_EXCLAMATIONMARK_";
	public final static String DQUOT = "_DQUOT_";
	public final static String SQUOT = "_SQUOT_";
	public final static String NUMBERSIGN = "_NUMBERSIGN_";
	public final static String DOLLARSIGN = "_DOLLARSIGN_";
	public final static String PERCENTAGE = "_PERCENTAGESIGN_";
	public final static String AMPERSAND = "_AMPERSAND_";
	public final static String APOSTROPHE = "_APOSTROPHE_";
	public final static String LPARENTHESI = "_LPARENTHESI_";
	public final static String RPARENTHESI = "_RPARENTHESI_";
	public final static String ASTERISK = "_ASTERISK_";
	public final static String PLUS = "_PLUS_";
	public final static String COMMA = "_COMMA_;";
	public final static String DOT = "_DOT_";
	public final static String SEMICOLON = "_SEMICOLON_";
	public final static String COLON = "_COLON_";
	public final static String QUESTIONMARK = "_QUESTIONMARK_";
	public final static String BACKSLASH = "_BSLASH_";
	public final static String FORWARDSLASH = "_FSLASH_";
	public final static String GREATERTHAN = "_GREATERTHAN_;";
	public final static String LESSTHAN = "_LESSTHAN_";
	public final static String EQUALSIGN = "_EQUALSIGN_";
	public final static String ATARROA = "_ATARROA_";
	public final static String LBRACKET = "_LBRACKET_";
	public final static String RBRACKET = "_RBRACKET_";
	public final static String CARET = "_CARET_";
	public final static String LCURLYBRACE = "_LCURLYBRACE_";
	public final static String RCURLYBRACE = "_RCURLYBRACE_";
	public final static String VERTICALBAR = "_VERTICALBAR_";
	public final static String TILDE = "_TILDE_";
	public final static String HYPHEN = "_HYPHEN_";
	public final static String SPACE = "_SPACE_";

	public static String escape(String str) {
		if (str == null || str.length() == 0)
			return "";
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				str);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '!') {
				result.append(EXCLAMATIONMARK);
			} else if (character == '\"') {
				result.append(DQUOT);
			} else if (character == '\'') {
				result.append(SQUOT);
			} else if (character == '#') {
				result.append(NUMBERSIGN);
			} else if (character == '$') {
				result.append(DOLLARSIGN);
			} else if (character == '%') {
				result.append(PERCENTAGE);
			} else if (character == '&') {
				result.append(AMPERSAND);
			} else if (character == '`') {
				result.append(APOSTROPHE);
			} else if (character == '(') {
				result.append(LPARENTHESI);
			} else if (character == ')') {
				result.append(RPARENTHESI);
			} else if (character == '*') {
				result.append(ASTERISK);
			} else if (character == '+') {
				result.append(PLUS);
			} else if (character == ',') {
				result.append(COMMA);
			} else if (character == '.') {
				result.append(DOT);
			} else if (character == ';') {
				result.append(SEMICOLON);
			} else if (character == ':') {
				result.append(COLON);
			} else if (character == '?') {
				result.append(QUESTIONMARK);
			} else if (character == '\\') {
				result.append(BACKSLASH);
			} else if (character == '/') {
				result.append(FORWARDSLASH);
			} else if (character == '>') {
				result.append(GREATERTHAN);
			} else if (character == '<') {
				result.append(LESSTHAN);
			} else if (character == '=') {
				result.append(EQUALSIGN);
			} else if (character == '@') {
				result.append(ATARROA);
			} else if (character == '[') {
				result.append(LBRACKET);
			} else if (character == ']') {
				result.append(RBRACKET);
			} else if (character == '^') {
				result.append(CARET);
			} else if (character == '{') {
				result.append(LCURLYBRACE);
			} else if (character == '}') {
				result.append(RCURLYBRACE);
			} else if (character == '|') {
				result.append(VERTICALBAR);
			} else if (character == '~') {
				result.append(TILDE);
			} else if (character == '-') {
				result.append(HYPHEN);
			} else if (character == ' ') {
				result.append(SPACE);
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString().intern();  // intern so '==' works
	}

	public static String unescape(String str) {
		if (str == null || str.length() == 0)
			return "";

		final StringBuilder result = new StringBuilder();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char character = str.charAt(i);
			switch (character) {
			case '_':
				if (str.startsWith(EXCLAMATIONMARK, i)) {
					result.append('!');
					i += (EXCLAMATIONMARK.length() - 2);
				} else if (str.startsWith(DQUOT, i)) {
					result.append('\"');
					i += (DQUOT.length() - 1);
				} else if (str.startsWith(SQUOT, i)) {
					result.append('\'');
					i += (SQUOT.length() - 1);
				} else if (str.startsWith(NUMBERSIGN, i)) {
					result.append('#');
					i += (NUMBERSIGN.length() - 1);
				} else if (str.startsWith(DOLLARSIGN, i)) {
					result.append('$');
					i += (DOLLARSIGN.length() - 1);
				} else if (str.startsWith(PERCENTAGE, i)) {
					result.append('%');
					i += (PERCENTAGE.length() - 1);
				} else if (str.startsWith(AMPERSAND, i)) {
					result.append('&');
					i += (AMPERSAND.length() - 1);
				} else if (str.startsWith(APOSTROPHE, i)) {
					result.append('`');
					i += (APOSTROPHE.length() - 1);
				} else if (str.startsWith(LPARENTHESI, i)) {
					result.append('(');
					i += (LPARENTHESI.length() - 1);
				} else if (str.startsWith(RPARENTHESI, i)) {
					result.append(')');
					i += (RPARENTHESI.length() - 1);
				} else if (str.startsWith(ASTERISK, i)) {
					result.append('*');
					i += (ASTERISK.length() - 1);
				} else if (str.startsWith(PLUS, i)) {
					result.append('+');
					i += (PLUS.length() - 1);
				} else if (str.startsWith(COMMA, i)) {
					result.append(',');
					i += (COMMA.length() - 1);
				} else if (str.startsWith(DOT, i)) {
					result.append('.');
					i += (DOT.length() - 1);
				} else if (str.startsWith(SEMICOLON, i)) {
					result.append(';');
					i += (SEMICOLON.length() - 1);
				} else if (str.startsWith(COLON, i)) {
					result.append(':');
					i += (COLON.length() - 1);
				} else if (str.startsWith(QUESTIONMARK, i)) {
					result.append('?');
					i += (QUESTIONMARK.length() - 1);
				} else if (str.startsWith(BACKSLASH, i)) {
					result.append('\\');
					i += (BACKSLASH.length() - 1);
				} else if (str.startsWith(FORWARDSLASH, i)) {
					result.append('/');
					i += (FORWARDSLASH.length() - 1);
				} else if (str.startsWith(GREATERTHAN, i)) {
					result.append('>');
					i += (GREATERTHAN.length() - 1);
				} else if (str.startsWith(LESSTHAN, i)) {
					result.append('<');
					i += (LESSTHAN.length() - 1);
				} else if (str.startsWith(EQUALSIGN, i)) {
					result.append('=');
					i += (EQUALSIGN.length() - 1);
				} else if (str.startsWith(ATARROA, i)) {
					result.append('@');
					i += (ATARROA.length() - 1);
				} else if (str.startsWith(LBRACKET, i)) {
					result.append('[');
					i += (LBRACKET.length() - 1);
				} else if (str.startsWith(RBRACKET, i)) {
					result.append(']');
					i += (RBRACKET.length() - 1);
				} else if (str.startsWith(CARET, i)) {
					result.append('^');
					i += (CARET.length() - 1);
				} else if (str.startsWith(LCURLYBRACE, i)) {
					result.append('{');
					i += (LCURLYBRACE.length() - 1);
				} else if (str.startsWith(RCURLYBRACE, i)) {
					result.append('}');
					i += (RCURLYBRACE.length() - 1);
				} else if (str.startsWith(VERTICALBAR, i)) {
					result.append('|');
					i += (VERTICALBAR.length() - 1);
				} else if (str.startsWith(TILDE, i)) {
					result.append('~');
					i += (TILDE.length() - 1);
				} else if (str.startsWith(HYPHEN, i)) {
					result.append('-');
					i += (HYPHEN.length() - 1);
				} else if (str.startsWith(SPACE, i)) {
					result.append(' ');
					i += (SPACE.length() - 1);
				} else {
					result.append(character);
				}
				break;
			default:
				result.append(character);
				break;
			}
		}
		return result.toString().intern();  // intern so '==' works
	}

	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);
			IOUtils.copy(in, out);
			in.close();
			out.close();
		}
	}


	public static void main(String[] args) throws Exception {
		System.out.println(NDDLUtil.unescape("test_DOT_this"));
	}

	//copy a file from within this bundle to outside the bundle
	//is there a good way to handle copyDirectory from a jar? 
	public static String copyFile(String fileName, String outputFileName) throws IOException {

		File outputFile = new File(outputFileName);
		if(!outputFile.exists())
			outputFile.createNewFile();
		System.out.println("Copying " + fileName + " to " + outputFileName);
		OutputStream stream = new FileOutputStream(outputFile);
		InputStream input = FileLocator.openStream(Activator.getDefault().getBundle(), new Path(fileName), false);
		
		IOUtils.copy(input, stream);
		input.close();
		stream.close();
		return outputFile.getAbsolutePath();
	}
	
}
