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
package gov.nasa.ensemble.common.ui.treetable;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public abstract class TreeTableLabelProvider extends LabelProvider {

	public TreeTableLabelProvider() {
		super();
	}

	public abstract boolean needsUpdate(Object feature);

	public abstract Font getFont(Object element);

	public abstract Color getBackground(Object element);

	/**
	 * 
	 * 
	 * @param element
	 */
	public void expand(Object element) {
		// do nothing
	}
	
	/**
	 * Override to return true or false if you maintain this state in the model.
	 * 
	 * @param element
	 * @return
	 */
	public Boolean isExpanded(Object element) {
		return null;
	}
	
	@Override
	public String getText(Object element) {
		return "";
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	public Color getForeground(Object element) {
		return null;
	}

	public String getTooltipText(Object element) {
		return null;
	}

}
