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
package gov.nasa.ensemble.core.jscience.ui;

import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class ProfileNavigatorLabelProvider extends LabelProvider {

	private static final ILabelProvider WORKBENCH_LABEL_PROVIDER = new WorkbenchLabelProvider();
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof EObject) {
			IItemLabelProvider labeler = EMFUtils.adapt(element, IItemLabelProvider.class);
			if (labeler != null) {
				return ExtendedImageRegistry.getInstance().getImage(labeler.getImage(element));
			}
		}
		return WORKBENCH_LABEL_PROVIDER.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof EObject) {
			IItemLabelProvider labeler = EMFUtils.adapt(element, IItemLabelProvider.class);
			if (labeler != null) {
				return labeler.getText(element);
			}
		}
		return WORKBENCH_LABEL_PROVIDER.getText(element);
	}

}
