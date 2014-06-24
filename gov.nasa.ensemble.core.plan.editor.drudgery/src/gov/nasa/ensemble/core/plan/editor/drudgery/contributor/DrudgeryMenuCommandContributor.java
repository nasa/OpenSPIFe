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
package gov.nasa.ensemble.core.plan.editor.drudgery.contributor;

import gov.nasa.ensemble.core.plan.editor.actions.AbstractCommandItemContributor;
import gov.nasa.ensemble.core.plan.editor.actions.PlanEditorCommandConstants;

public class DrudgeryMenuCommandContributor extends AbstractCommandItemContributor implements PlanEditorCommandConstants {

	@Override
	protected void contributeCommandItems() {
		contribute(MOVE_COMMAND_ID);
		contribute(ALIGN_START_COMMAND_ID);
		contribute(ALIGN_END_COMMAND_ID);
		contribute(DISTRIBUTE_EVENLY_COMMAND_ID);
		contribute(DISTRIBUTE_BY_COMMAND_ID);
		contribute(SWAP_COMMAND_ID);
		contribute(HOP_RIGHT_COMMAND_ID);
		contribute(HOP_LEFT_COMMAND_ID);
		contribute(COMPRESS_LEFT_COMMAND_ID);
		contribute(COMPRESS_RIGHT_COMMAND_ID);
		contribute(COPY_MANY_TIMES_COMMAND_ID);
		contribute(COMPRESS_CHAIN_COMMAND_ID);
	}

}
