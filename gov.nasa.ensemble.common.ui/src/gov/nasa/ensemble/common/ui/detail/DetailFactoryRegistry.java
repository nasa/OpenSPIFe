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

import gov.nasa.ensemble.common.extension.ClassRegistry;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class DetailFactoryRegistry {

	static final List<IDetailFactory> factories = ClassRegistry.createInstances(IDetailFactory.class);

	/**
	 * Given an object (usually gotten from the workbench selection), the factory cycles until it finds the first factory that
	 * returns a non null value and returns it.
	 * 
	 * @param object
	 * @param toolkit
	 * @param form
	 * @param selectionProvider TODO
	 * @return IDetailSheet
	 */
	public static IDetailSheet buildDetailSheet(Object object, FormToolkit toolkit, ScrolledForm form, ISelectionProvider selectionProvider) {
		for (IDetailFactory f : factories) {
			try {
				IDetailSheet sheet = f.buildDetailSheet(object, toolkit, form, selectionProvider);
				if (sheet != null) {
					return sheet;
				}
			} catch (RuntimeException e) {
				Logger.getLogger(DetailFactoryRegistry.class).error("Error building detail sheet from factory " + f, e);
			}
		}
		return null;
	}
}
