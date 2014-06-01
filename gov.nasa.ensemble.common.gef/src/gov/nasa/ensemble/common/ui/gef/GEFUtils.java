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
package gov.nasa.ensemble.common.ui.gef;

import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;

public class GEFUtils {

	public static EditPartViewer getViewerSafely(EditPart editPart) {
		try {
			return editPart.getViewer();
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * If this is called from within the UI thread, it will be run immediately otherwise
	 * it will be asyncExeced.
	 * @param runnable
	 */
	public static void runInDisplayThread(final EditPart editPart, final Runnable runnable) {
		if (editPart == null || !editPart.isActive())
			return;
		EditPartViewer viewer = getViewerSafely(editPart);
		if (viewer != null) {
			WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
				@Override
				public void run() {
					if (getViewerSafely(editPart) != null && editPart.isActive())
						runnable.run();
				}
			});
		}
	}
	
	public static void runLaterInDisplayThread(final EditPart editPart, final Runnable runnable) {
		if (editPart == null)
			return;
		EditPartViewer viewer = getViewerSafely(editPart);
		if (viewer != null) {
			WidgetUtils.runLaterInDisplayThread(viewer.getControl(), new Runnable() {
				@Override
				public void run() {
					if (getViewerSafely(editPart) != null) {
						runnable.run();
					}
				}
			});
		}
	}
	
	public static void refreshLaterInDisplayThread(final EditPart ep) {
		if (ep == null) {
			return;
		}
		runLaterInDisplayThread(ep, new Runnable() {
			@Override
			public void run() {
				ep.refresh();
			}
		});
	}
}
