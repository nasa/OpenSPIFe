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
import gov.nasa.ensemble.common.ui.MultiLineStyledText;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This is a simple TEST SWT application which demo's the SpecialTextWidget functionality.
 * 
 * Run as an SWT application.
 * @author alexeiser
 *
 */
public class MultiLineStyledTextWidgetExample {
	
	public static void main(String[] args) {

		Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		Composite comp = new Composite(shell, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		comp.setLayout(gl);
		
		Label l = new Label(comp, SWT.NONE);
		l.setText("A Text Label");
		
		MultiLineStyledText stext = new MultiLineStyledText(comp, SWT.NONE);
		stext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		stext.setText("This is a multi line text widget example"  +
				  " the number of lines used is dependan't on the" +
				  " amount of text entered.");
		
		
		l = new Label(comp, SWT.NONE);
		l.setText("Another label below");
		
		shell.setSize(256, 512);
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}
