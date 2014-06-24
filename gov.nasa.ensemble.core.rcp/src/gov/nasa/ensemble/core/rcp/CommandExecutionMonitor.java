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
package gov.nasa.ensemble.core.rcp;

import gov.nasa.ensemble.common.debug.EnsembleUsageLogger;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class CommandExecutionMonitor  implements IExecutionListener {
	
	private Map<String, Long> durationMap = new HashMap<String, Long>();
	
	@Override
	public void notHandled(String commandId, NotHandledException exception) {
		StringBuilder sb = new StringBuilder(commandId);
		double duration = addDuration(sb, commandId);
		addLine(sb, commandId, exception.getMessage());
		EnsembleUsageLogger.logUsage("CMD.notHandled: "+commandId, duration);
	}

	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
		StringBuilder sb = new StringBuilder(commandId);
		double duration = addDuration(sb, commandId);
		addLine(sb, commandId, exception.getMessage());
		EnsembleUsageLogger.logUsage("CMD.postExecuteFailure: "+sb.toString(), duration);
	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		StringBuilder sb = new StringBuilder(commandId);
		double duration = addDuration(sb, commandId);
		addLine(sb, "return value", returnValue);
		EnsembleUsageLogger.logUsage("CMD.postExecuteSuccess: "+sb.toString(), duration);
	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		StringBuilder sb = new StringBuilder(commandId);
		addLine(sb, "active part", HandlerUtil.getActivePartId(event));
		IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor != activePart)
			addLine(sb, "active editor", HandlerUtil.getActiveEditorId(event));
		if (editor != null)
			addLine(sb, "active editor input", editor.getEditorInput());
		addLine(sb, "current selection", HandlerUtil.getCurrentSelection(event));
		EnsembleUsageLogger.logUsage("CMD.preExecute: "+sb.toString());
		durationMap.put(commandId, System.currentTimeMillis());
	}

	private double getDurationSeconds(Long startTime) {
		return startTime == null ? -1 : (System.currentTimeMillis() - startTime) / 1000.0;
	}
	
	private String getDurationString(double duration) {
		if (duration < 0)
			return "unknown duration";
		return new DecimalFormat().format(duration) + " s";
	}
	
	private double addDuration(StringBuilder sb, String commandId) {
		double duration = getDurationSeconds(durationMap.remove(commandId));
		sb.append(" (" + getDurationString(duration ) + ")");
		return duration;
	}
	
	private void addLine(StringBuilder sb, String label, Object object) {
		if (object != null)
			sb.append("\n   ").append(label).append(": ").append(object);
	}
	
}
