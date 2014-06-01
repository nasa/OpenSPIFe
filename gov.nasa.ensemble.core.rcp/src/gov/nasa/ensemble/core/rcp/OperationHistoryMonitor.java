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

import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;

public class OperationHistoryMonitor implements IOperationHistoryListener {

	@Override
	public void historyNotification(OperationHistoryEvent event) {
		
		IUndoableOperation op = event.getOperation();
		String typeString = decodeEventType(event.getEventType());
//		System.out.println("type='"+typeString+"' operation='"+op+"' of type '"+op.getClass().getName()+"' e="+op.canExecute()+" u="+op.canUndo()+" r="+op.canRedo());
//		for(IUndoContext c : op.getContexts()) {
//			System.out.println("\t"+c.getLabel());
//		}
		
		switch(event.getEventType()) {
			case OperationHistoryEvent.OPERATION_ADDED:
			case OperationHistoryEvent.OPERATION_CHANGED:
			case OperationHistoryEvent.OPERATION_REMOVED:
			case OperationHistoryEvent.ABOUT_TO_EXECUTE:
			case OperationHistoryEvent.ABOUT_TO_REDO:
			case OperationHistoryEvent.ABOUT_TO_UNDO:
				break;
			default:
				EnsembleUsageLogger.logUsage(op.getLabel() + " - " + typeString + "\n" + op.toString());
		}
		
	}

	private static String decodeEventType(int eventType) {
		String typeString = null;
		switch(eventType) {
			case OperationHistoryEvent.ABOUT_TO_EXECUTE:typeString="ABOUT_TO_EXECUTE";break;
			case OperationHistoryEvent.ABOUT_TO_REDO:typeString="ABOUT_TO_REDO";break;
			case OperationHistoryEvent.ABOUT_TO_UNDO:typeString="ABOUT_TO_UNDO";break;
			case OperationHistoryEvent.DONE:typeString="DONE";break;
			case OperationHistoryEvent.OPERATION_ADDED:typeString="OPERATION_ADDED";break;
			case OperationHistoryEvent.OPERATION_CHANGED:typeString="OPERATION_CHANGED";break;
			case OperationHistoryEvent.OPERATION_NOT_OK:typeString="OPERATION_NOT_OK";break;
			case OperationHistoryEvent.OPERATION_REMOVED:typeString="OPERATION_REMOVED";break;
			case OperationHistoryEvent.REDONE:typeString="REDONE";break;
			case OperationHistoryEvent.UNDONE:typeString="UNDONE";break;
		}
		return typeString;
	}
	
}
