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
package gov.nasa.ensemble.common.ui.viewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

/**
 * Same as ContainerCheckedTreeViewer but ignores any items that have been disposed.
 * @since SPF-11790
 */
public class EnsembleContainerCheckedTreeViewer extends ContainerCheckedTreeViewer {

	public EnsembleContainerCheckedTreeViewer(Composite parent) {
		super(parent);
	}

	public EnsembleContainerCheckedTreeViewer(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Item[] getChildren(Widget parent) {
		if (parent.isDisposed()) return new Item[0];
		Item[] result = super.getChildren(parent);
		for (Item item : result) {
			if (item.isDisposed()) {
				return getChildrenIgnoringDisposed(result).toArray(result);
			}
		}
		return result;
	}

	@Override
	protected Widget doFindInputItem(Object element) {
		Widget item = super.doFindInputItem(element);
		if (item==null) {
			return null;
		} else if (item.isDisposed()) {
			return null;
		} else {
			return item;
		}
	}

	@Override
	protected Widget doFindItem(Object element) {
		Widget item = super.doFindItem(element);
		if (item==null) {
			return null;
		} else if (item.isDisposed()) {
			return null;
		} else {
			return item;
		}
	}
	
	@Override
	protected boolean usingElementMap() {
		// Only way we can get protected final Widget[] findItems to call our overriden methods
		// to ensure it does not return a disposed item.
		return false;
	}

	private List<Item> getChildrenIgnoringDisposed(Item[] unfiltered) {
		ArrayList result = new ArrayList(unfiltered.length);
		for (Item child : unfiltered) {
			if (!child.isDisposed()) {
				result.add(child);
			}
		}
		return result;
	}

	@Override
	protected void doCheckStateChanged(Object element) {
		Widget widget = findItem(element);
		if (widget != null && !widget.isDisposed()) {
			super.doCheckStateChanged(element);
		}
	}

}
