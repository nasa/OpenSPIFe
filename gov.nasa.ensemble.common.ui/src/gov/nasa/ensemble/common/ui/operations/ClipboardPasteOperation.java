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
/**
 * 
 */
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.transfers.ClipboardContents;
import gov.nasa.ensemble.common.ui.transfers.ClipboardServer;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;

public class ClipboardPasteOperation extends AbstractEnsembleUndoableOperation {
	
	private final Logger trace = Logger.getLogger(getClass());
	
	private final ISelection targetSelection;
	private final IStructureModifier modifier;
	protected List<TransferData> acceptableTypes;
	protected ITransferable transferable;
	protected IStructureLocation location;
	
	public ClipboardPasteOperation(ISelection targetSelection, IStructureModifier modifier) {
		super("paste");
		this.targetSelection = targetSelection;
		this.modifier = modifier;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		if (state == null) {
			return;
		}
		switch (state) {
		case DONE:
			// transferable is in the document
			break;
		case UNEXECUTED:
		case UNDONE:
			if (transferable != null) {
				transferable.dispose();
			}
			break;
		case FAILED:
			// don't know how to recover here 
		}
	}
	
	protected boolean somethingAvailable() {
		final Display display = WidgetUtils.getDisplay();
		final TransferData[][] typesArray = new TransferData[1][];
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				Clipboard clipboard = ClipboardServer.instance.getClipboard(display);
				typesArray[0] = clipboard.getAvailableTypes();
			}
		});
		TransferData[] types = typesArray[0];
		if ((types == null) || (types.length == 0)) {
			return false;
		}
		List<TransferData> acceptableTypes = new ArrayList<TransferData>();
		for (TransferData type : types) {
			if (modifier.canInsert(type, targetSelection, InsertionSemantics.ON)) {
				acceptableTypes.add(type);
			}
		}
		if (acceptableTypes.isEmpty()) {
			return false;
		}
		this.acceptableTypes = Collections.unmodifiableList(acceptableTypes);
		return true;
	}

	@Override
	public boolean isExecutable() {
		return somethingAvailable();
	}
	
	@Override
	protected void execute() {
		getTransferableFromClipboard();
		getInsertionLocation();
		putNewCopyOnClipboard();
		doit();
	}

	protected void getTransferableFromClipboard() {
		ClipboardContents contents = TransferRegistry.getInstance().getFromClipboard(acceptableTypes);
		if (contents == null) {
			throw new NullPointerException("clipboard contents unexpectedly null");
		}
		transferable = contents.transferable;
	}
	
	protected void getInsertionLocation() {
		location = modifier.getInsertionLocation(transferable, targetSelection, InsertionSemantics.ON);
	}

	protected void putNewCopyOnClipboard() {
		try {
			ITransferable copiedStuff = modifier.copy(transferable);
			TransferRegistry.getInstance().putOnClipboard(copiedStuff);
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			trace.warn("failed to clone clipboard on paste, clearing instead");
			TransferRegistry.clearClipboardContents();
		}
	}

	@Override
	protected void undo() {
		modifier.remove(transferable, location);
// Andrew says: I don't think we want this behavior
//		try {
//			// put back the original clipboard contents
//			Clipboard clipboard = ClipboardServer.instance.getClipboard(null);
//			TransferRegistry.getInstance().putOnClipboard(transferable, clipboard);
//		} catch (Throwable t) {
//			if (t instanceof ThreadDeath) {
//				throw (ThreadDeath)t;
//			}
//			trace.warn("failed to restore clipboard on undo paste");
//		}
	}

	@Override
	protected void redo() {
		doit();
	}

	protected void doit() {
		modifier.add(transferable, location);
	}

	/*
	 * Property accessors
	 */
	
	public IStructureModifier getModifier() {
		return modifier;
	}

	public ISelection getTargetSelection() {
		return targetSelection;
	}

	public ITransferable getTransferable() {
		return transferable;
	}

	public IStructureLocation getLocation() {
		return location;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(ClipboardPasteOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(transferable));
		builder.append(" onto ");
		builder.append(String.valueOf(targetSelection));
		builder.append(" landing at ");
		builder.append(String.valueOf(location));
		return builder.toString();
	}
	
}
