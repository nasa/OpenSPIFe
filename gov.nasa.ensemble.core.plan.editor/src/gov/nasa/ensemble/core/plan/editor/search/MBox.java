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
package gov.nasa.ensemble.core.plan.editor.search;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * This class creates a Combo or Text object for user
 * input. All the method names for MBox are the same for
 * those needed by Combo and Text. Objects of this class
 * may be thought of as a Combo or Text depending on the
 * boolean value passed to the constructor.
 * 
 * @author Alonzo Benavides
 * @bug no known bugs
 */
class MBox{
	private Combo searchCombo;
	private Text searchText;
	private boolean isCombo = false;
	
	public MBox(Composite composite, boolean isCombo){
		this.isCombo = isCombo;
		
		if(isCombo){
			searchText = null;
			searchCombo = new Combo(composite, SWT.SINGLE | SWT.BORDER);
			searchCombo.setFocus();
		}
		else{
			searchText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			searchText.setFocus();
			searchCombo = null;
		}
	}
	
	/**
	 * For clarification on these method calls see: 
	 * org.eclipse.swt.widgets.Combo
	 * org.eclipse.swt.widgets.Text
	 */
	
	public void setFont(Font font){
		if(isCombo){
			searchCombo.setFont(font);
		}
		else{
			searchText.setFont(font);
		}
	}
	
	public void setLayoutData(Object data){
		if(isCombo){
			searchCombo.setLayoutData(data);
		}
		else{
			searchText.setLayoutData(data);
		}
	}
	
	public String[] getItems(){
		if(isCombo){
			return searchCombo.getItems();
		}
		return null;
	}
	
	public void add(String item){
		if(isCombo){
			searchCombo.add(item);
		}
	}
	
	public void setText(String text){
		if(isCombo){
			searchCombo.setText(text);
		}
		else{
			searchText.setText(text);
		}
	}
	
	public String getText(){
		if(isCombo){
			return searchCombo.getText();
		}
		else{
			return searchText.getText();
		}
	}
	
	public void dispose(){
		if(isCombo){
			searchCombo.dispose();
		}
		else{
			searchText.dispose();
		}
	}
	
	public void select(int index){
		if(isCombo){
			searchCombo.select(index);
		}
	}
	
	public void setVisible(boolean visible){
		if(isCombo){
			searchCombo.setVisible(visible);
		}
		else{
			searchText.setVisible(visible);
		}
	}
	
	public void setEditable(boolean flag){
		if(isCombo){
			searchCombo.setEnabled(flag);
		}
		else{
			searchText.setEditable(false);
		}
	}
}
