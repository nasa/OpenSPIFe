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
package gov.nasa.arc.spife.ui.table.days;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DayControl extends Label {
	
	private Day input;

	public DayControl(Composite parent, int style) {
		super(parent, style);
		setAlignment(SWT.CENTER);
	}

	@Override
	protected void checkSubclass() {
		// it's checked
	}
	
	public Day getInput() {
		return input;
	}
	
	public void setInput(Day day) {
		this.input = day;
		setText(String.valueOf(day.getDate()));
	}
	
	@Override
	public String toString() {
		return super.toString() + " : " + getLocation().x;
	}
	
}
