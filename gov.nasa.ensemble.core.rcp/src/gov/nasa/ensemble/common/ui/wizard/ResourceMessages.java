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
package gov.nasa.ensemble.common.ui.wizard;

import org.eclipse.osgi.util.NLS;

// FIXME: replace with non-internal API
public class ResourceMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ui.internal.wizards.newresource.messages";
	
	// ==============================================================================
	// New Resource Wizards
	// ==============================================================================
	public static String FileResource_shellTitle;
	public static String FileResource_pageTitle;
	public static String FileResource_description;
	public static String FileResource_errorMessage;

	public static String NewFolder_title;
	public static String NewFolder_text;

	public static String NewProject_windowTitle;
	public static String NewProject_title;
	public static String NewProject_description;
	public static String NewProject_referenceTitle;
	public static String NewProject_referenceDescription;
	public static String NewProject_errorOpeningWindow;
	public static String NewProject_errorMessage;
	public static String NewProject_internalError;
	public static String NewProject_caseVariantExistsError;
	public static String NewProject_perspSwitchTitle;
	/**
	 * Combines a perspective name and text for introducing a perspective switch
	 */
	public static String NewProject_perspSwitchMessage;
	/**
	 * Combines a perspective name and description with text for introducing 
	 * a perspective switch
	 */
	public static String NewProject_perspSwitchMessageWithDesc;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, ResourceMessages.class);
	}
}
