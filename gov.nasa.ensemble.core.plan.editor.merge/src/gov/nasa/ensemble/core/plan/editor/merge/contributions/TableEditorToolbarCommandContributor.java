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
package gov.nasa.ensemble.core.plan.editor.merge.contributions;

import gov.nasa.ensemble.core.plan.editor.actions.AbstractCommandItemContributor;

import org.eclipse.swt.SWT;

public class TableEditorToolbarCommandContributor extends AbstractCommandItemContributor {

	@Override
	protected void contributeCommandItems() {
		contribute(FLATTEN_HIERARCHY_COMMAND_ID, SWT.CHECK);
		contribute(PICK_TEMPORAL_PLAN_MODIFIER_COMMAND_ID, SWT.CHECK);
		contribute(SHOW_ROW_HIGHLIGHT_COMMAND_ID, SWT.CHECK);
	}

}
