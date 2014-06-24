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
package gov.nasa.ensemble.core.detail.emf.util;

import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class LabelProviderWrapper extends LabelProvider {

	private IItemLabelProvider itemLabelProvider;

	public LabelProviderWrapper(IItemLabelProvider itemLabelProvider) {
		super();
		this.itemLabelProvider = itemLabelProvider;
	}

	@Override
	public String getText(Object object) {
		if (object instanceof EEnumLiteral) {
			return ((EEnumLiteral)object).getLiteral();
		}
		if (itemLabelProvider != null)
			return itemLabelProvider.getText(object);
		
		IItemLabelProvider labeler = EMFUtils.adapt(object, IItemLabelProvider.class);
		if (labeler != null) {
			return labeler.getText(object);
		}
		return "";
	}

	@Override
	public Image getImage(Object object) {
		IItemLabelProvider labeler = EMFUtils.adapt(object, IItemLabelProvider.class);
		if (labeler != null) {
			return ExtendedImageRegistry.getInstance().getImage(labeler.getImage(object));
		}
		return ExtendedImageRegistry.getInstance().getImage(itemLabelProvider.getImage(object));
	}	
}
