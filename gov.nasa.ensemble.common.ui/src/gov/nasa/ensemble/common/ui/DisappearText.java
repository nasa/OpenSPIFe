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

/***
 * A simple override to have a text box that will remove the text the first time the user clicks it
 * This is useful for things like search boxes, where the default text will say "search"
 */

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DisappearText extends Text {

	private boolean beenClicked = false;
	
	public DisappearText(Composite parent, String text, int style) {
		super(parent, style);
		setText(text);
		setRedraw(true);
		setTextLimit(1000);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(!isBeenClicked()){
					setText("");
					setBeenClicked(true);
				}
			}
		});
	}
	
	@Override
	protected void checkSubclass() {
		// do nothing
	}
	
	/**
	 * Only makes text disappear if the box has never been clicked. ie: It will make the default text disappear if it is still displayed.
	 */
	public void makeTextDisappear(){
		if(!beenClicked){
			setText("");
		}
	}

	public void setBeenClicked(boolean beenClicked) {
		this.beenClicked = beenClicked;
	}

	public boolean isBeenClicked() {
		return beenClicked;
	}
	
	public void reset(String text) { 
		setText(text);
		beenClicked = false;
	}
	
	public void reset() {
		reset("");
	}
	
}
