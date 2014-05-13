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
package gov.nasa.ensemble.common.ui.gef;

import org.eclipse.gef.RequestConstants;

/**
 * The set of constants used to identify <code>Requests</code> by their {@link
 * Request#getType() type}. Applications can extend this set of constants with their own.
 */
public interface EnsembleRequestConstants extends RequestConstants {

	/**
	 * Indicates that something is being dropped on the edit part.
	 */
	String REQ_DROP = "drop";//$NON-NLS-1$

	/**
	 * Indicates that a context menu is being requested for the edit part.
	 */
	String REQ_CONTEXT_MENU = "context menu";//$NON-NLS-1$

}
