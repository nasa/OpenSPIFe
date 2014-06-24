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
package gov.nasa.ensemble.common.ui.editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.ui.IEditorPart;

public abstract class AbstractEnsembleEditorModel implements IEnsembleEditorModel {

	public interface IDirtyListener {
		public void dirtyStateChanged();
	}
	
	private final Set<IEditorPart> editors = new HashSet<IEditorPart>();
	private final List<IDirtyListener> dirtyListeners = new ArrayList<IDirtyListener>();
	private final DirtyStateOperationHistoryListener historyListener;
	private IUndoableOperation cleanStateOperation = null;
	private boolean dirty = false;
	private boolean dirtyByDefault = false;

	public AbstractEnsembleEditorModel() {
		this(false);
	}
	
	public AbstractEnsembleEditorModel(boolean readOnly) {
		super();
		if (readOnly) {
			historyListener = null;
		} else {
			historyListener = new DirtyStateOperationHistoryListener();
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.addOperationHistoryListener(historyListener);
		}
	}

	/**
	 * Implement in your subclass to cleanup stored objects related to 
	 * this input.
	 */
	protected void dispose() {
		if (historyListener != null) {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.removeOperationHistoryListener(historyListener);
		}
	}

	/**
	 * Return the latest dirty state
	 */
	public boolean isDirty() {
		return dirty || dirtyByDefault;
	}
	
	/**
	 * Indicates if the model is forced dirty
	 * @param dirtyByDefault
	 */
	public void setForcedDirtyBit(boolean dirtyByDefault) {
		if (this.dirtyByDefault  != dirtyByDefault) {
			this.dirtyByDefault = dirtyByDefault;
			fireDirtyStateChanged();
		}
	}

	/**
	 * Modify the current dirty state
	 */
	public void resetDirty() {
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		cleanStateOperation = history.getUndoOperation(getUndoContext());
		if (dirty || dirtyByDefault) {
			dirtyByDefault = false;
			dirty = false;
			fireDirtyStateChanged();
		}
	}

	/**
	 * Add editor to the list of editors open on this input.
	 * Editors should add themselves to the list when they receive
	 * a setInput with this input as the parameter.
	 * 
	 * @param editor
	 */
	public void addEditor(IEditorPart editor) {
		editors.add(editor);
	}

	/**
	 * Remove editor from the list of editors open on this input.
	 * If this was the last editor, dispose the input.
	 * 
	 * Editors should remove themselves from the list when
	 * they receive a setInput and this input was the old input.
	 * 
	 * Editors should also perform a setInput(null) in their dispose()
	 * to ensure that this call is made. 
	 * 
	 * @param editor
	 */
	public void removeEditor(IEditorPart editor) {
		if (editors.remove(editor) && editors.isEmpty()) {
			try {
				dispose();
				System.gc(); // eagerly garbage collect
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				Logger.getLogger(AbstractEnsembleEditorModel.class).error("removeEditor", t);
			}
		}		
	}

	public synchronized void addDirtyListener(IDirtyListener listener) {
		dirtyListeners.add(listener);
	}
	
	public synchronized void removeDirtyListener(IDirtyListener listener) {
		dirtyListeners.remove(listener);
	}
	
	public synchronized void fireDirtyStateChanged() {
		for (IDirtyListener listener : dirtyListeners) {
			try {
				listener.dirtyStateChanged();
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				Logger.getLogger(AbstractEnsembleEditorModel.class).error("in listener", t);
			}
		}
	}
	
	public class DirtyStateOperationHistoryListener implements IOperationHistoryListener {
		@Override
		public void historyNotification(OperationHistoryEvent event) {
			IUndoableOperation eventOperation = event.getOperation();
			IUndoContext inputUndoContext = getUndoContext();
			IUndoContext[] operationContexts = eventOperation.getContexts();
			boolean matches = false;
			for (IUndoContext operationContext : operationContexts) {
				matches = operationContext.matches(inputUndoContext);
				if (matches) {
					break;
				}
			}
			if (!matches) {
				return;
			}
			switch (event.getEventType()) {
			case OperationHistoryEvent.REDONE: {
				if (eventOperation == cleanStateOperation) {
					dirty = false;
					fireDirtyStateChanged();
				} else if (!dirty) {
					dirty = true;
					fireDirtyStateChanged();
				}
				break;
			}
			case OperationHistoryEvent.DONE: {
				if (!dirty) {
					dirty = true;
					fireDirtyStateChanged();
				}
				break;
			}
			case OperationHistoryEvent.UNDONE: {
				IUndoableOperation undoOperation = event.getHistory().getUndoOperation(inputUndoContext);
				if (undoOperation == cleanStateOperation) {
					dirty = false;
					fireDirtyStateChanged();
				} else if (!dirty) {
					dirty = true;
					fireDirtyStateChanged();
				}
				break;
			}
			default:
				// nothing to do yet
				break;
			}
		}
	}
	
}
