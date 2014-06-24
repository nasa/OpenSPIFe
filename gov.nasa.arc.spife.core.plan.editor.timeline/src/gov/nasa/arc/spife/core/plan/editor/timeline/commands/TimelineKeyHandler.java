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
package gov.nasa.arc.spife.core.plan.editor.timeline.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;

/**
 * Fixing an error in which the keys were not being forwarded 
 * to the parent key handler. Bug110483 reported to eclipse 
 * regarding KeyHandler. New key handler class created 
 * TimelineKeyHandler to solve this issue.  
 *
 * The key change has been marked below near CHANGE.
 *
 * @author aghevli
 */
public class TimelineKeyHandler extends KeyHandler {

	private Map<KeyStroke, IAction> actions;
	private KeyHandler parent;

	/**
	 * Processes a <i>key pressed</i> event. This method is called by the Tool whenever a key
	 * is pressed, and the Tool is in the proper state.
	 * @param event the KeyEvent
	 * @return <code>true</code> if KeyEvent was handled in some way
	 */
	@Override
	public boolean keyPressed(KeyEvent event) {
		if (performStroke(KeyStroke.getPressed(event.character, event.keyCode, event.stateMask), event)) {
			event.doit = false;
			return true;
		}
		return parent != null && parent.keyPressed(event);
	}

	/**
	 * Processes a <i>key released</i> event. This method is called by the Tool whenever a key
	 * is released, and the Tool is in the proper state.
	 * @param event the KeyEvent
	 * @return <code>true</code> if KeyEvent was handled in some way
	 */
	@Override
	public boolean keyReleased(KeyEvent event) {
		if (performStroke(KeyStroke.getReleased(event.character, event.keyCode, event.stateMask), event))
			return true;
		return parent != null && parent.keyReleased(event);
	}

	private boolean performStroke(KeyStroke key, KeyEvent keyEvent) {
		if (actions == null)
			return false;
		IAction action = actions.get(key);
		if (action == null)
			return false;
		
		boolean enabled = action.isEnabled();
		if (enabled) {
			Event event = new Event();
			event.character = keyEvent.character;
			event.data = keyEvent.data;
			event.display = keyEvent.display;
			event.doit = keyEvent.doit;
			event.keyCode = keyEvent.keyCode;
			event.keyLocation = keyEvent.keyLocation;
			event.stateMask = keyEvent.stateMask;
			event.time = keyEvent.time;
			event.widget = keyEvent.widget;
			action.runWithEvent(event);
		}
		return enabled; // CHANGED from "true" in the original eclipse code
	}

	/**
	 * Maps a specified <code>KeyStroke</code> to an <code>IAction</code>. When a KeyEvent
	 * occurs matching the given KeyStroke, the Action will be <code>run()</code> iff it is
	 * enabled.
	 * @param keystroke the KeyStroke
	 * @param action the Action to run
	 */
	@Override
	public void put(KeyStroke keystroke, IAction action) {
		if (actions == null)
			actions = new HashMap<KeyStroke, IAction>();
		actions.put(keystroke, action);
	}

	/**
	 * Removed a mapped <code>IAction</code> for the specified <code>KeyStroke</code>.
	 * @param keystroke the KeyStroke to be unmapped
	 */
	@Override
	public void remove(KeyStroke keystroke) {
		if (actions != null)
			actions.remove(keystroke);
	}

	/**
	 * Sets a <i>parent</i> <code>KeyHandler</code> to which this KeyHandler will forward
	 * un-consumed KeyEvents. This KeyHandler will first attempt to handle KeyEvents. If it
	 * does not recognize a given KeyEvent, that event is passed to its <i>parent</i>
	 * @param parent the <i>parent</i> KeyHandler
	 * @return <code>this</code> for convenience
	 */
	@Override
	public KeyHandler setParent(KeyHandler parent) {
		this.parent = parent;
		return this;
	}
	
}
