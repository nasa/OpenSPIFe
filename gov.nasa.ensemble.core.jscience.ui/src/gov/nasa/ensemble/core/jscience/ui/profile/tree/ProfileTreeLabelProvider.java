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
package gov.nasa.ensemble.core.jscience.ui.profile.tree;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public final class ProfileTreeLabelProvider extends AdapterFactoryLabelProvider {
	
	private static final Image IMAGE_FOLDER = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
	
	public ProfileTreeLabelProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Image getImage(Object object) {
		if (object instanceof String) {
			return IMAGE_FOLDER;
		}
		return super.getImage(object);
	}

	@Override
	public String getText(Object object) {
		if (object instanceof String) {
			return (String) object;
		}
		return super.getText(object);
	}
	
}
