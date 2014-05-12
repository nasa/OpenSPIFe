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

import gov.nasa.ensemble.common.ui.color.ColorConstants;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A safer Composite with better behavior:
 * 1. preferred size of 0, 0 if no children
 * 2. error if a layout is not set
 * @author Andrew
 */
public class EnsembleComposite extends Composite {

	public EnsembleComposite(Composite parent, int style) {
		super (parent, style);
	}
	
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		if (getLayout() == null) {
			nolayout();
			return new Point(200, 100);
		}		
		if (getChildren().length == 0) {
			return new Point(0, 0);
		}
		return super.computeSize(wHint, hHint, changed);
	}
	
	private void nolayout() {
		MessageDialog dialog = new MessageDialog(Display.getDefault().getActiveShell(), "EnsembleComposite", null,
				"layout must be set for EnsembleComposite: " + this,
				MessageDialog.ERROR, new String[] { "OK" }, 1);
		dialog.open();
		setBackground(ColorConstants.red);
	}

}
