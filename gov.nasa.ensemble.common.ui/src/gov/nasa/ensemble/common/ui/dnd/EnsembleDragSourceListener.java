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

import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;

import org.apache.log4j.Logger;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEffect;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * This is a convenience class to be extended by a class that wants to 
 * allow the user to drag some of its contents.  The class also adheres
 * strictly to the contract in DragSourceListener, allowing the extending
 * implementation to more reliably implement the interface and avoid
 * platform specific bugs.
 * 
 * @author Andrew
 */
public abstract class EnsembleDragSourceListener extends DragSourceEffect {

	private final Logger trace = Logger.getLogger(getClass());
	private final IEnsembleEditorModel editorModel;
	private Image feedback;
	
	protected EnsembleDragSourceListener(Control control, IEnsembleEditorModel editorInput) {
		super(control);
		this.editorModel = editorInput;
	}
	
	/**
	 * This function will be called first when a drag occurs.
	 * Use this opportunity to determine if the selection is
	 * a reasonable thing to drag.  Also, you should cache the
	 * objects that will be dragged because the selection may
	 * be lost on some platforms. (only Mac?)
	 * @param source
	 * @param time
	 * @return true if drag should be allowed, false otherwise
	 */
	protected abstract boolean isDragPossible(DragSource source, long time);

	/**
	 * This function will be called second when a drag occurs.
	 * Depending on which platform is being used, this function
	 * may be called earlier or later.  Implementors must ensure
	 * that the data being returned is of a compatible type to
	 * the dataType passed in.
	 * @param source
	 * @param time
	 * @param dataType the datatype that the dragged thing(s) should be represented with
	 * @return the data to be dragged (must match the dataType)
	 */
	protected abstract Object getDragData(DragSource source, long time, TransferData dataType);

	/**
	 * This function will be called after the drop successfully completed
	 * or has been terminated (such as hitting the ESC key).  Perform
	 * cleanup such as removing data from the source side on a successful
	 * move operation.
	 * @param source
	 * @param time
	 * @param doit true if the event completed succesfully, false otheriwse
	 * @param detail DND.DROP_NONE, DND.DROP_MOVE, etc.
	 */
	protected abstract void finishDrag(DragSource source, long time, boolean doit, int detail);

	/**
	 * Subclasses should override this if they wish to provide a feedback image.
	 * The returned image will not be copied, and will not be disposed of by
	 * this class.
	 * 
	 * @return an Image to use for feedback
	 */
	protected Image getFeedbackImage() {
		return null;
	}
	
	/*
	 * Event unpacking functions.  These functions extract
	 * the fields which each method is allowed to read and
	 * modify only the fields which each method is allowed
	 * to set.  (See DragSourceListener documentation)
	 * 
	 *  Do NOT modify these functions to provide read
	 *  access to other fields, or to modify other fields.
	 *  Doing so will violate the DragSourceListener
	 *  contract and probably cause platform specific
	 *  failures.
	 */
	
	@Override
	public final void dragStart(DragSourceEvent event) {
		try {
			EnsembleDragAndDropOracle.acquireDragSourceEditorModel(editorModel);
			DragSource source = (DragSource)event.widget;
			EnsembleDragAndDropOracle.acquireDragging(source);
			trace.debug("dragStart/isDragPossible: " + source.getControl() + " " + event.time);
			event.doit = isDragPossible(source, event.time & 0xFFFFFFFFL);
			event.image = createFeedback();
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dragStart/isDragPossible", t);
			// Disallow drop when isDragPossible is broken.
			// The doit field defaults to true, so this is necessary. 
			event.doit = false;
		}
	}
	
	@Override
	public final void dragSetData(DragSourceEvent event) {
		try {
			DragSource source = (DragSource)event.widget;
			trace.debug("dragSetData/getDragData: " + source.getControl() + " " + event.time + " " + event.dataType);
			event.data = getDragData(source, event.time & 0xFFFFFFFFL, event.dataType);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dragSetData/getDragData", t);
			event.data = null; // probably null already, but still...
		}
	}

	@Override
	public final void dragFinished(DragSourceEvent event) {
		DragSource source = null;
		try {
			source = (DragSource)event.widget;
			trace.debug("dragFinished/finishDrag: " + source.getControl() + " " + event.time + " " + event.doit + " " + getDetailString(event.detail));
			finishDrag(source, event.time & 0xFFFFFFFFL, event.doit, event.detail);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dragFinished/finishDrag", t);
		} finally {
			try {
				eraseFeedback();
			} finally {
				try {
					EnsembleDragAndDropOracle.releaseDragSourceEditorModel(editorModel);
				} finally {
					EnsembleDragAndDropOracle.releaseDragging(source);
				}
			}
		}
	}

	/**
	 * Utility function to get a string to print out a "detail"
	 * @param detail
	 * @return the string name of the detail.
	 */
	protected final String getDetailString(int detail) {
		switch (detail) {
			case DND.DROP_DEFAULT:     return "DND.DROP_DEFAULT"; 
			case DND.DROP_COPY:        return "DND.DROP_COPY";
			case DND.DROP_LINK:        return "DND.DROP_LINK";
			case DND.DROP_MOVE:        return "DND.DROP_MOVE";
			case DND.DROP_NONE:        return "DND.DROP_NONE";
			case DND.DROP_TARGET_MOVE: return "DND.DROP_TARGET_MOVE";
			default:                   return "DND.???";
		}
	}
	
	private Image createFeedback() {
		Image image = getFeedbackImage();
		if (image == null)
			return null;
		Rectangle bounds = new Rectangle(0, 0, image.getBounds().width + 10, image.getBounds().height);
		ImageData data = new ImageData(bounds.width, bounds.height, image.getImageData().depth, image.getImageData().palette);
		data.transparentPixel = 0;
		feedback = new Image(image.getDevice(), data);
		GC gc = new GC(feedback);
		gc.drawImage(image, 10, 0);
		gc.dispose();
		return feedback;
	}
	
	private void eraseFeedback() {
		if (feedback != null)
			feedback.dispose();
	}
}
