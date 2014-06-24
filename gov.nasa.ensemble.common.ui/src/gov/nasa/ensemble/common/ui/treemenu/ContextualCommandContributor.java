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
package gov.nasa.ensemble.common.ui.treemenu;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IAdaptable;

/**
 * A simple API for a right-click context menu on a tree whose menu items
 * depend on the object in the tree, especially its class.
 * The isApplicable method will be called to let further criteria be applied.
 * (By default, all objects in the tree that are instances of the class T,
 * as well as those that can be adapted to instances of class T,
 * will show this menu item.)
 * Supports flat menus or one level of cascading.
 * @param <T> (Super)Class of tree item this is applicable for.
 * @see TreeContextMenuFactory
 * @author kanef
 *
 */
abstract public class ContextualCommandContributor<T> extends IntrospectiveContextualCommandContributor implements IContextualCommandContributor {

	/** Returns the name to appear in the menu (or on the button label or tooltip).
	 * If this returns null, the default isApplicable (if not overriden) will return false.
	 */
	abstract public String getMenuItemName(T selectedObject);
	
	/** This allows control over the order in which the menu items appear.
	 * The default is 1; use 0 or negative to place items before that, large numbers to place at end,
	 * or override on all items if you want complete control. */
	public int getOrder() {
		return 1;
	}
	
	/** Called when a tree item is right-clicked on and this menu item is selected. */
	abstract public void performAction(T selectedObject);

	public void performActionOnAdaptedObject(Object selectedObject) {
		if (selectedObject instanceof IAdaptable) {
			try {
				Object adaptation = ((IAdaptable) selectedObject).getAdapter(getApplicableClass());
				if (adaptation != null) {
					selectedObject = adaptation;
				}
			}
			catch (NoSuchMethodException exception) {
				LogUtil.error(exception);
			}
		}
		performAction((T) selectedObject);		
	}

	/** @return null if top-level, or name of submenu if one level down. */
	public String getSubmenuName() {
		return null;
	}
	
	public final boolean isApplicableToObject(Object selectedObject) {
		Object adaptedObject = ensureCorrectType(selectedObject);
		try {
			return isApplicableResult(adaptedObject) && isApplicable((T) adaptedObject)
					&& getMenuItemName((T) adaptedObject) != null; // in case isApplicable forgets to check
		} catch (ClassCastException e) {
			LogUtil.error("SPF-10353: " + e + "\n  selectedObject is a "
					+ selectedObject.getClass().getSimpleName() + "; adaptedObject is a "
					+ adaptedObject.getClass().getSimpleName() + ".");
			return false;
		}
	}
	
	/** 
	 * Override this method if the menu item does not apply to all objects of this type.
	 * @param treeItem
	 * @return true if this menu item should appear when this object is right-clicked on.
	 */
	public boolean isApplicable(T treeItem) {
		return getMenuItemName(treeItem) != null;
	}
	
	@Override
	public Collection<? extends ContextualCommandContributor<?>> getContributors(Object regardlessOfObjectSelected) {
		return Collections.singletonList(this);
	}

	@Override
	protected String getNameOfMethodAcceptingParameterOfTypeT() {
		return "performAction";
	}

	@Override
	protected int getNumberOfParameterOfTypeT() {
		return 0;
	}


}
