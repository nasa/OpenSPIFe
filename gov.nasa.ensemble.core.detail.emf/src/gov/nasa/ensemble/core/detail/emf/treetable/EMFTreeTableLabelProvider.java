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
package gov.nasa.ensemble.core.detail.emf.treetable;

import gov.nasa.ensemble.common.ui.treetable.TreeTableLabelProvider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class EMFTreeTableLabelProvider extends TreeTableLabelProvider implements IBaseLabelProvider {

	private final ILabelProvider labelProvider;

	public EMFTreeTableLabelProvider(AdapterFactory adapterFactory) {
		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}

	@Override
	public void dispose() {
		super.dispose();
		this.labelProvider.dispose();
	}

	/**
	 * Override in order to not return an icon since
	 * they often are EMF defaults
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		return labelProvider.getText(element);
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}

	@Override
	public Font getFont(Object element) {
		return null;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		return false;
	}

}
