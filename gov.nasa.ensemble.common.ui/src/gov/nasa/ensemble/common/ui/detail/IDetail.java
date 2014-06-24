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
package gov.nasa.ensemble.common.ui.detail;

import org.eclipse.swt.widgets.Composite;

/**
 * Provides a single interface to allow objects to provide details
 * about themselves in the form of name/value pairs. Display of
 * the information will be done through a standard text box for the 
 * name, as well as a custom value editor composite that this class 
 * shall provide
 * 
 * Implementations may standardize many of the value editor composites
 * so that all objects of boolean types return a checkbox, strings return
 * a text box, etc.
 *  
 * @author aaghevli
 *
 */
public interface IDetail {

	/**
	 * Return the display name for the detail
	 * @return display name
	 */
	public String getName();
	
	/**
	 * Allows detail to provide descriptive information
	 * @return tool tip text
	 */
	public String getToolTipText();
	
	/**
	 * Return the composite that is responsible for
	 * displaying the name and editing the value. 
	 * This may be a simple label and text pair, or
	 * a more complex combination of name display and
	 * value editing composite
	 * 
	 * This call assumes composite initializers such
	 * as parent composites and/or SWT types have been
	 * set elswhere
	 * 
	 * @return editing composite
	 */
	public Composite createValueEditorComposite(Composite parent);
	
	/**
	 * Set the visibility of the detail
	 */
	public void setVisible(boolean visibility);
	
	/**
	 * Get the visibility of the detail
	 */
	public boolean isVisible();
}
