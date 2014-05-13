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
package gov.nasa.arc.spife.rcp.events;

import gov.nasa.ensemble.common.ui.GlobalAction;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class SPIFeMultiPagePlanEditorActionBarContributor extends
MultiPageEditorActionBarContributor
{
	// need to call these singletons to create instances of the actions
	// so that the actions are registered in the global action registry
	
	private static final GlobalAction moveToOrbitAction = MoveToOrbitAction.getInstance();
	
	private static final GlobalAction toggleSnapToActionAction = ToggleSnapToOrbitAction.getInstance(); 
	
	@Override
	public void init(IActionBars bars)
	{ 
		super.init(bars);		
		bars.setGlobalActionHandler(moveToOrbitAction.getId(), moveToOrbitAction);
		bars.setGlobalActionHandler(toggleSnapToActionAction.getId(), toggleSnapToActionAction);
	}

	@Override // this overrides an abstract method
	public void setActivePage(IEditorPart activeEditor)
	{
		moveToOrbitAction.setEnabled(activeEditor != null);
		toggleSnapToActionAction.setEnabled(activeEditor != null);
	}

	@Override
	public void setActiveEditor(IEditorPart part)
	{
		super.setActiveEditor(part);
		moveToOrbitAction.setEnabled(part != null);
		toggleSnapToActionAction.setEnabled(part != null);
	}

	@Override
	public void dispose()
	{
		super.dispose();
		moveToOrbitAction.setEnabled(false);
		toggleSnapToActionAction.setEnabled(false);
	}
	
	
}
