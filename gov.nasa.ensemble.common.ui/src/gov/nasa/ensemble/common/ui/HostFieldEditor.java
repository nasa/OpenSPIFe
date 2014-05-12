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
package gov.nasa.ensemble.common.ui;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */
public class HostFieldEditor extends StringFieldEditor {
	
	public HostFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		setErrorMessage("Cannot resolve host");
	}
	
	/**
	 * Check whether the host can be resolved.
	 *
	 * @return <code>true</code> if the field value is valid,
	 *   and <code>false</code> if invalid
	 */
	@Override
	protected boolean doCheckState() {
		// Disabling this until Windows performance issues are resolved
		// and we've had the time to test it without an inet connection.
//		String host = getTextControl().getText();
//		if (host.trim().length() == 0) return false;
//		try {
//			InetAddress address = InetAddress.getByName(host);
//		} catch (UnknownHostException e) {
//			return false;
//		}
		
		return true;
	}
}
