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

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This interface allows classes to register themselves as DetailFactorys
 * without having to extend DetailFactory
 */
public interface IDetailFactory {

	/**
	 * Given a set of objects, this method will return a single detail sheet
	 * that details the element in the argument. If the element cannot be
	 * identified, the factory should return null.
	 * 
	 * @param object
	 *            to get details from
	 * @param selectionProvider TODO
	 * @return the detail sheet for the collection
	 */
	public IDetailSheet buildDetailSheet(Object object, FormToolkit formToolkit, ScrolledForm scrolledForm, ISelectionProvider selectionProvider);

}
