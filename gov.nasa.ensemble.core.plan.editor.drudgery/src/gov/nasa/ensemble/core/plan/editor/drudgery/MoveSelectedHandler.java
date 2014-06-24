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
package gov.nasa.ensemble.core.plan.editor.drudgery;

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.actions.PlanEditorCommandConstants;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.jscience.physics.amount.Amount;

public class MoveSelectedHandler extends DrudgerySavingHandler {

	public MoveSelectedHandler() {
		super("Move Selected", "Move Selected");
	}
	
	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}

	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> sortedElements) {
		IEditorPart editor = getActiveEditor();
		Shell shell = editor.getSite().getShell();
		ECollections.sort(sortedElements, TemporalChainUtils.CHAIN_ORDER);
		EPlanElement planElement = sortedElements.get(0);
		Date referenceDate = planElement.getMember(TemporalMember.class).getStartTime();
		MoveSelectedDialog dialog = new MoveSelectedDialog(shell, referenceDate);
		int code = dialog.open();
		if (code != Window.OK) {
			return Collections.emptyMap();
		}
		Map<EPlanElement, Date> map = new HashMap<EPlanElement, Date>();
		Amount<Duration> offset = dialog.getOffset();
		for (EPlanElement pe : sortedElements) {
			TemporalMember member = pe.getMember(TemporalMember.class);
			Date startTime = member.getStartTime();
			Date newTime = DateUtils.add(startTime, offset);
			map.put(pe, newTime);
		}
		return map;
	}

	@Override
	public String getCommandId() {
		return PlanEditorCommandConstants.MOVE_COMMAND_ID;
	}

}
