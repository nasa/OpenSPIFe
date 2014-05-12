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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author bkanefsk
 *
 * @param <I> -- type of Input object in tree
 * @param <O> -- type of Output items on menu
 */
public abstract class RuntimeMenuContributor<I,O> extends IntrospectiveContextualCommandContributor implements IContextualCommandContributor {
	
	public abstract List<O> getItems(I selectedObject);
	
	public abstract String getMenuItemName(O object);
	
	protected String getSubmenuName() {
		return null;
	}

	protected boolean isApplicable(I selectedObject) {
		return true;
	}

	protected abstract void performAction(I selectedObject, O selectedMenuItem);


	@Override
	protected String getNameOfMethodAcceptingParameterOfTypeT() {
		// Finds I, the type of the Input object in tree
		return "performAction";
	}

	@Override
	protected int getNumberOfParameterOfTypeT() {
		// Finds I, the type of the Input object in tree
		return 0;
	}

	@Override
	public Collection<ContextualCommandContributor<I>> getContributors(Object selectedObject) {
		final RuntimeMenuContributor<I,O> provider = this;
		List<ContextualCommandContributor<I>> result = new LinkedList();
		selectedObject = ensureCorrectType(selectedObject);
		if (!isApplicableResult(selectedObject)) return Collections.EMPTY_LIST;
		List<O> items = getItems((I)selectedObject);
		if (items==null) return Collections.EMPTY_LIST;
		for (O menuItem : items) {
			final O currentMenuItem = menuItem;
			result.add(new ContextualCommandContributor<I>() {

				@Override
				public String getMenuItemName(I selectedObject) {
					return provider.getMenuItemName(currentMenuItem) ;
				}

				@Override
				public boolean isApplicable(I selectedObject) {
					return provider.isApplicable(selectedObject);
				}

				@Override
				public Class<?> getApplicableClass() throws NoSuchMethodException {
					return provider.getApplicableClass();
				}

				@Override
				public String getSubmenuName() {
					return provider.getSubmenuName();
				}

				@Override
				public void performAction(I selectedObject) {
					provider.performAction(selectedObject, currentMenuItem);
				}

			});}
		return result;
	}
}
