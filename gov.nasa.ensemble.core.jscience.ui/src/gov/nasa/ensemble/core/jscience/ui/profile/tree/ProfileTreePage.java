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
package gov.nasa.ensemble.core.jscience.ui.profile.tree;

import gov.nasa.ensemble.common.ui.view.page.TreeViewerPage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.dnd.ProfileTransferProvider;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;

public class ProfileTreePage extends TreeViewerPage {

	protected static final Transfer[] TRANSFERS = new Transfer[] {
		ProfileTransferProvider.transfer
	};
	
	@Override
	protected void searchInputChanged(String comboText, String searchString) {
		// do nothing, the filter should take care of it
	}

	@Override
	protected void configureTreeViewer(TreeViewer treeViewer) {
		treeViewer.addDragSupport(DND.DROP_COPY, TRANSFERS, new ProfileTreeDragSourceListener(treeViewer, null));
		treeViewer.setLabelProvider(new ProfileTreeLabelProvider(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE)));
		treeViewer.addFilter(new FindFilter());
	}

	/**
	 * This class filters the elements to display in the view based upon the
	 * currently specified search criteria.
	 */
	protected final class FindFilter extends ViewerFilter {
		
		/**
		 * Returns whether the given element makes it through this filter.
		 * 
		 * @param viewer
		 * @param parentElement
		 * @param element
		 * @return true if element meets the search criteria 
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			String filterText = getNameFilterText();
			
			// sanity check
			if(filterText == null) {
				return true; 
			}
			
			// retrieve the current case insensitive search criteria
			filterText = filterText.trim().toUpperCase();
			
			// return true if no search criteria specified
			if(filterText.length() == 0) return true;
			
			TreeViewer treeViewer         = (TreeViewer) viewer;
			ITreeContentProvider provider = (ITreeContentProvider) treeViewer.getContentProvider();
			
			// if the current element is an activity then immediately apply
			// case insensitive search criteria
			if (matchesProfile(treeViewer, element, filterText)) {
				return true;
			}
			
			// if the current element is not an activity then recurse
			// through all children until either a match is found or all
			// have been examined
			Queue<Object> openList = new LinkedList<Object>();
			openList.offer(element);
			while (!openList.isEmpty()) {
				Object thisElement = openList.poll();
				for(Object childElement : provider.getChildren(thisElement)) {
					if (matchesProfile(treeViewer, childElement, filterText)) {
						return true;
					}
					openList.offer(childElement);
				}
			}
			// if all else fails, return false
			return false;
		}

		@SuppressWarnings("unchecked")
		private boolean matchesProfile(TreeViewer treeViewer, Object childElement, String filterText) {
			if(childElement instanceof Profile) {
				Profile profile = (Profile) childElement;
				String text = ((ILabelProvider)treeViewer.getLabelProvider()).getText(profile);
				return text.toUpperCase().indexOf(filterText) != -1;
			}
			return false;
		}
	
	}
	
}
