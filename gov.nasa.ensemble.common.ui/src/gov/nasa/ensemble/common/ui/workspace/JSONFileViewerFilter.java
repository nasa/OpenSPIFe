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
package gov.nasa.ensemble.common.ui.workspace;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class JSONFileViewerFilter extends ViewerFilter {

	/**
	 * Return whether the given element is an IFile the name of which, ignoring case, is "plan.json".
	 * Refactor to look more like FileExtensionViewerFilter if multiple providers need to be registered.
	 * @param parentElement ignored
	 * @param element the element to filter; of interest only if it is an IFile
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return (!(element instanceof IFile)) || !((IFile) element).getName().equalsIgnoreCase("plan.json");
	}

}
