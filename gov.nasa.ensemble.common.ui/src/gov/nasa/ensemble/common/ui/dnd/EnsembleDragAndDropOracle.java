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
package gov.nasa.ensemble.common.ui.dnd;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;

import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;

/**
 * This class is a workaround for gaps in knowledge in the drag 
 * and drop API.  In particular, there is no way within the 
 * confines of the drop target listener interface for a drop 
 * target listener to determine that the source of the drag is 
 * the 'same' document, until the drop is actually executed.
 * However, this knowledge is required earlier in order to
 * properly set the 'detail'.
 * 
 * Additionally, this oracle supports optimizations for
 * in-application drag and drop.
 * 
 * @author Andrew
 */
public class EnsembleDragAndDropOracle {

	private static IEnsembleEditorModel dragSourceEditorInput;
	private static DragSource currentDragSource;
	private static DropTarget currentDropTarget;
	
	public static synchronized void acquireDragSourceEditorModel(IEnsembleEditorModel editorModel) {
		if (dragSourceEditorInput != null) {
			LogUtil.warn("drag source editor model not properly released");
		}
		dragSourceEditorInput = editorModel;
	}
	
	public static synchronized void releaseDragSourceEditorModel(IEnsembleEditorModel editorModel) {
		if (dragSourceEditorInput != editorModel) {
			LogUtil.warn("drag source editor model released from wrong model");
		}
		dragSourceEditorInput = null;
	}

	public static synchronized boolean isDragSourceEditorModel(IEnsembleEditorModel editorModel) {
		return (dragSourceEditorInput == editorModel);
	}
	
	public static synchronized void acquireDragging(DragSource source) {
		if (currentDragSource != null) {
			LogUtil.warn("current drag source not properly released");
		}
		currentDragSource = source;
	}
	
	public static synchronized void releaseDragging(DragSource source) {
		if (currentDragSource != source) {
			LogUtil.warn("current drag source released from wrong model");
		}
		currentDragSource = null;
		currentDropTarget = null;
	}
	
	public static synchronized boolean isSameProcessDragSourceAndDropTarget() {
		return (currentDragSource != null) && (currentDropTarget != null);
	}

	public static void acquireDropping(DropTarget target) {
		if (currentDropTarget != null) {
			LogUtil.warn("current drop target not properly released");
		}
		currentDropTarget = target;
	}
	
}
