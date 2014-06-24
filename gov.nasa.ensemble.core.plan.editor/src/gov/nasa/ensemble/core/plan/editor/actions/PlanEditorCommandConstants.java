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
package gov.nasa.ensemble.core.plan.editor.actions;



public interface PlanEditorCommandConstants {

	//Drudgery Saving Tasks
	public static final String MOVE_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.move";
	public static final String ALIGN_START_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.align.start";
	public static final String ALIGN_END_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.align.end";
	public static final String DISTRIBUTE_EVENLY_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.distribute.evenly";
	public static final String DISTRIBUTE_BY_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.distribute.x";
	public static final String SWAP_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.swap";
	public static final String HOP_RIGHT_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.hop.right";
	public static final String HOP_LEFT_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.hop.left";
	public static final String COMPRESS_LEFT_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.compressLeftCommand";
	public static final String COMPRESS_RIGHT_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.compressRightCommand";
	public static final String COPY_MANY_TIMES_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.drudgery.copy.multiple";
	public static final String COMPRESS_CHAIN_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.constraints.compress.chain";
	
	//Constraints
	public static final String PIN_COMMAND_ID =  "gov.nasa.ensemble.core.plan.editor.constraints.pin";
	public static final String EARLIEST_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.constraints.earliest";
	public static final String LATEST_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.constraints.latest";
	public static final String CHAIN_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.constraints.chain";
	public static final String BINARY_CONSTRAINT_COMMAND_ID =  "gov.nasa.ensemble.core.plan.editor.constraints.binary";
	public static final String CONSTRAINT_EXACTLY_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.constraints.exactly";
	
	//Open Plan Search
	public static final String OPEN_PLAN_SEARCH_COMMAND_ID =  "gov.nasa.ensemble.core.plan.editor.open.plan.search";
	
	//Follow Current Time
	public static final String FOLLOW_CURRENT_TIME_COMMAND_ID = "gov.nasa.arc.spife.core.plan.editor.timeline.follow.current.time";
	
	//Timeline
	public static final String SHOW_VIOLATIONS_COMMAND_ID =  "gov.nasa.arc.spife.core.plan.editor.timeline.show.violations";
	public static final String SHOW_CONSTRAINTS_COMMAND_ID = "gov.nasa.arc.spife.core.plan.editor.timeline.show.constraints";
	public static final String SNAP_COMMAND_ID = "gov.nasa.arc.spife.core.plan.editor.timeline.snap";
	public static final String OVERLAY_COMMAND_ID =  "gov.nasa.arc.spife.core.plan.editor.timeline.commands.overlay";
	
	//Reorder by start time
	public static final String REORDER_BY_START_TIME_COMMAND_ID = "gov.nasa.ensemble.plan.order.ascending";
	
	//Table Commands
	public static final String FLATTEN_HIERARCHY_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.merge.toggle_flatten";
	public static final String PICK_TEMPORAL_PLAN_MODIFIER_COMMAND_ID = "gov.nasa.ensemble.core.plan.temporal.modification";
	public static final String SHOW_ROW_HIGHLIGHT_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.merge.row.highlight";
	
}
