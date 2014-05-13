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
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.operation.OperationJob.IJobOperation;
import gov.nasa.ensemble.common.operation.OperationJob.IPostJobRunnable;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

/**
 * This is an interface that can be implemented for an operation to perform a task
 * in the display thread after it has been executed from WidgetUtils.execute.
 * 
 * For example, setting the selection, focusing widgets.
 * 
 * @author abachman
 *
 */
public interface IDisplayOperation extends IJobOperation {
	/**
	 * Executed after the main operation has completed successfully
	 * @param widget
	 * @param site
	 */
	public void displayExecute(Widget widget, IWorkbenchSite site);
	
	/**
	 * This class is used to attach the display execute call to
	 * the job created for an operation.
	 * 
	 * @author abachman
	 */
	public static class Adaptable extends IPostJobRunnable.Adaptable {
		
		private final IDisplayOperation displayOperation;
		private final Widget widget;
		private final IWorkbenchSite site;
		
		public Adaptable(IDisplayOperation displayOperation, Widget widget, IWorkbenchSite site) {
			this.displayOperation = displayOperation;
			this.widget = widget;
			this.site = site;
		}
		
		@Override
		public Object getAdapter(Class adapter) {
			if (adapter == Widget.class) {
				return widget;
			}
			if (adapter == IWorkbenchSite.class) {
				return site;
			}
			return super.getAdapter(adapter);
		}

		@Override
		public void jobSuccessful() {
			WidgetUtils.runInDisplayThread(widget, new Runnable() {
				@Override
				public void run() {
					displayOperation.displayExecute(widget, site);
				}
			}, true);
		}
		
	}
	
}
