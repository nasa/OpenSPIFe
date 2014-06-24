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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.ui.timeline.ToolImpl.IToolListener.ToolListenerEvent;
import gov.nasa.arc.spife.ui.timeline.ToolImpl.IToolListener.ToolListenerEvent.TYPE;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Tool;
import org.eclipse.gef.tools.AbstractTool;

public abstract class ToolImpl extends AbstractTool implements AccessibleTool {

	private final List<IToolListener> listeners = new ArrayList<IToolListener>();

	/**
	 * Increase visibility
	 */
	@Override
	public boolean isActive() {
		return super.isActive();
	}

	@Override
	public void activate() {
		super.activate();
		fireToolListenerEvent(TYPE.ACTIVATED);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		fireToolListenerEvent(TYPE.DEACTIVATED);
	}

	public void addToolListener(IToolListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void fireToolListenerEvent(TYPE eventType) {
		for (IToolListener listener : listeners) {
			listener.handleEvent(new ToolListenerEvent(this, eventType));
		}
	}
	
	public void removeToolListener(IToolListener listener) {
		listeners.remove(listener);
	}
	
	public static interface IToolListener {
		
		public void handleEvent(ToolListenerEvent event);
		
		public static class ToolListenerEvent {
			
			public enum TYPE {
				ACTIVATED,
				DEACTIVATED
			}
			
			private final Tool tool;
			private final TYPE type;
			
			public ToolListenerEvent(Tool tool, TYPE type) {
				super();
				this.tool = tool;
				this.type = type;
			}

			public Tool getTool() {
				return tool;
			}

			public TYPE getType() {
				return type;
			}
		}		
	}
	
	@Override
	public Point getLocation() {
		return super.getLocation();
	}

	@Override
	public EditPartViewer getCurrentViewer() {
		return super.getCurrentViewer();
	}	
}
