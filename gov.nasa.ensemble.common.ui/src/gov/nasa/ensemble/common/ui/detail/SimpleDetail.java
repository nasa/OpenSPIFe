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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * This implementation assumes that the control is 
 * a two columned composite consisting of the name on
 * the left and an editor on the right. 
 * 
 * Extending classes should implement the createValueEditorControl
 * function in order to return the control that would be placed upon
 * the right hand of the name field.
 * 
 * @author Arash
 *
 */
public abstract class SimpleDetail extends Detail {

	protected FormToolkit toolkit = null;
	private Label label;
	private Control control;

	public SimpleDetail(FormToolkit toolkit) {
		this.toolkit = toolkit;
	}
	
	protected Label createLabel(Composite parent) {
		Label label;
		String name = getName();
		if (name == null)
			name = "";
		boolean truncated = name.length() > 20;
		if (truncated) {
			name = name.substring(0,17)+"...";
		}
		label = toolkit.createLabel(parent, name);
		String toolTipText = "";
		if (truncated) {
			toolTipText = getName();
		}
		if ((getToolTipText() != null) && (getToolTipText().trim().length() != 0)) {
			if (truncated) {
				toolTipText += " \n";
			}
			toolTipText += getToolTipText();
		}
		label.setToolTipText(toolTipText);
		label.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
		return label;
	}
	
	public abstract Control createControl(Composite parent);

	/**
	 * Creates a composite that calls the abstract function 
	 * createValueEditorControl and lays them out as such.
	 * 
	 * _______________________
	 * |  Name  |   Control  |
	 * -----------------------
	 * 
	 */
	@Override
	public Composite createValueEditorComposite(Composite parent) {
	    try {
	    	label = createLabel(parent);
			control = createControl(parent);
			if (control == null && label != null) label.dispose();
	    } catch (RuntimeException e) {
	    	// prevent 'odd' ordered arguments when the value editor control fails to create
	    	if (label != null) {
	    		label.dispose();
	    	}
	    	if (control != null) {
	    		control.dispose();
	    	}
	    	throw e;
	    }
	    
	    label.setVisible(isVisible());
	    control.setVisible(isVisible());
	    
	    return parent;
	}
	
	@Override
	public void setVisible(boolean visibility) {
		super.setVisible(visibility);
	    label.setVisible(visibility);
	    control.setVisible(visibility);
	}

	public Label getLabel() {
		return label;
	}
	
	public Control getControl() {
		return control;
	}
}
