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
package gov.nasa.ensemble.core.activityDictionary.view;

import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.ActivityDefSerializableTransfer;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEffect;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This class acts as the tree viewer (controller in the MVC paradigm) for the
 * <code>ActivityDictionaryView</code>. Its structure is based heavily on the
 * <code>MergeTreeViewer</code>.
 * 
 * @see gov.nasa.ensemble.core.activityDictionary.view.ActivityDictionaryView
 * @see gov.nasa.ensemble.core.plan.editor.merge.MergeTreeViewer
 */
public class ActivityDictionaryViewer extends TreeViewer {

	/** logger */
	private static final Logger trace = Logger.getLogger(ActivityDictionaryViewer.class);
	
	/** A transfer object to enable drop-n-drop via serialization */
	private static final Transfer[] TRANSFERS = new Transfer[] { ActivityDefSerializableTransfer.getInstance() };
	
	/**
	 * Construct a tree viewer with the specified composite as the parent. The
	 * underlying tree control is created with the style bits enabling
	 * multi-selection.
	 * 
	 * @param parent
	 *            the parent control
	 */
	public ActivityDictionaryViewer(Composite parent) {
		// enable multi-selection
		super(parent, SWT.MULTI);
		
		// enable drag-n-drop support
		addDragSupport(DND.DROP_COPY, TRANSFERS, new ActivityDictionaryDragSourceAdapter(getControl()));
	}

	
	/**
	 * This class provides custom implementations for the methods of interest in
	 * the <code>DragSourceListener</code> interface, thus enabling
	 * drag-n-drop from the activity dictionary view onto allowable targets.
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener
	 */
	private class ActivityDictionaryDragSourceAdapter extends DragSourceEffect {
		
		public ActivityDictionaryDragSourceAdapter(Control control) {
			super(control);
		}
		
		/**
		 * Cache of the selected objects. Set during dragStart and read (and
		 * cleared) during dragSetData. The cache is required for drag and drop
		 * to work on all platforms -- on the Mac, the selection is cleared by
		 * the time dragSetData is called and therefore the cache is required.
		 */
		private Serializable[] selectedObjects;
		
		
		/**
		 * The user has begun the actions required to drag the widget. This
		 * event gives the application the chance to decide if a drag should be
		 * started.
		 * 
		 * In the case of the activity dictionary view, only if an activity
		 * definition is selected will the drag be started.
		 * 
		 * <p>
		 * The following fields in the DragSourceEvent apply:
		 * <ul>
		 * <li>(in)widget
		 * <li>(in)time
		 * <li>(in,out)doit
		 * </ul>
		 * </p>
		 * 
		 * @param event
		 *            the information associated with the drag start event
		 * 
		 * @see DragSourceEvent
		 */
		@Override
		public void dragStart(DragSourceEvent event) {
			// retrieve (and cache) all the selected activity definitions
			selectedObjects = getSelectedActivityDefs();
			
			if ( (selectedObjects == null) || (selectedObjects.length == 0) ) {
				event.doit = false;
				return;
			}

			// if any activity definitions are selected, then authorize the drag
			event.doit = true;
			event.image = createFeedback();
			DragSource source = (DragSource) event.getSource();
			source.setTransfer(TRANSFERS);
		}

		private Image feedback;
		private Image createFeedback() {
			if (Platform.OS_LINUX.equals(Platform.getOS())) {
				// Linux already has a pretty feedback image
				feedback = null;
			} else {
				Image image = getImage();
				Rectangle bounds = new Rectangle(0, 0, image.getBounds().width + 10, image.getBounds().height);
				ImageData data = new ImageData(bounds.width, bounds.height, image.getImageData().depth, image.getImageData().palette);
				data.transparentPixel = 0;
				feedback = new Image(image.getDevice(), data);
				GC gc = new GC(feedback);
				gc.drawImage(image, 10, 0);
				gc.dispose();
			}
			return feedback;
		}
		
		protected Image getImage() {
			return IconLoader.getIcon(ActivityDictionaryViewPlugin.getDefault().getBundle(), 
					"icons/activity_dictionary_view.png");
		}
		
		@Override
		public void dragFinished(DragSourceEvent event) {
			super.dragFinished(event);
			if (feedback != null) {
				feedback.dispose();
			}
		}
		
		/**
		 * The data is required from the drag source.
		 *
		 * <p>The following fields in the DragSourceEvent apply:
		 * <ul>
		 * <li>(in)widget
		 * <li>(in)time
		 * <li>(in)dataType - the type of data requested.
		 * <li>(out)data    - the application inserts the actual data here (must match the dataType)
		 * </ul></p>
		 *
		 * @param event the information associated with the drag set data event
		 * 
		 * @see DragSourceEvent
		 */
		@Override
		public void dragSetData(DragSourceEvent event) {
			try {
				// set the payload
				if ((selectedObjects != null) && (selectedObjects.length != 0))
					event.data = selectedObjects;
				
			} catch (Exception e) {
				trace.warn("Exception in dragSetData: " + e.getMessage(), e);
			}
			
			// clear the selected objects cache
			selectedObjects = null;
		}
		
		
		/**
		 * Filters any non activity definition from the current selection.
		 * 
		 * @return an array of all the selected activity definitions
		 */
		private Serializable[] getSelectedActivityDefs() {
			// get everything that is currently selected
			Object[] selections = ((StructuredSelection) getSelection()).toArray();

			List<String> activityDefIDs = new ArrayList<String>();

			// sift through the selected objects for the activity definitions
			for (Object selection : selections) {
				if (selection instanceof EActivityDef)
					activityDefIDs.add(((EActivityDef) selection).getName());
				else
					trace.warn("Cannot drag object: " + selection);
			}
			
			return activityDefIDs.toArray(new String[activityDefIDs.size()]);
		}
	}
	
}
