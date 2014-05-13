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

import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.help.ContextProvider;
import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.EActivityDef;

import org.eclipse.help.IContextProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


/**
 * This class provides the ViewPart implementation of the activity dictionary
 * view.
 * 
 * @see gov.nasa.ensemble.core.activityDictionary.view.ActivityDictionaryViewer
 * @see gov.nasa.ensemble.core.activityDictionary.view.ActivityDictionaryContentProvider
 */
public class ActivityDictionaryView extends DefinitionTreeView {
	
	/**
	 * This ID should be the same as the identifier as in the extension point
	 * org.eclipse.ui.views that defines this view
	 */
	public static final String ID = ClassIdRegistry.getUniqueID(ActivityDictionaryView.class);
	
	/**
	 * This label provider provides custom method implementations for retrieving
	 * an activity definition's icon and name.
	 */
	private static class ViewLabelProvider extends NamedDefinitionLabelProvider {
		
		/**
		 * @param element
		 * @return the image associated with the specified element 
		 */
		@Override
		public Image getImage(Object element) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			
			if (element instanceof String)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			
			else if (element instanceof EActivityDef) {
				EActivityDef activityDef = (EActivityDef) element;
				String activityDefName  = activityDef.getCategory();
				return MissionUIConstants.getInstance().getIcon(activityDefName);
			}
			
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	public ActivityDictionaryView() {
		ActivityDictionary AD = ActivityDictionary.getInstance();
		String versionLabel = AD.getNsURI();
		String versionNumber = AD.getVersion();
		String description = "";
		if (versionLabel != null) {
			description += versionLabel;
		}
		if (versionNumber != null) {
			if (description.length() != 0) {
				description += " ";
			}
			description += versionNumber;
		}
		if (description.length() > 0) {
			description = "Version: " + description;
			setContentDescription(description);
		}
	}
	
	@Override
	protected String getDisplayText(String filterModeText) {
		if (filterModeText == DEFINITION_NAME) {
			return "activity type";
		}
		if (filterModeText == DEFINITION_CATEGORY) {
			return "category";
		}
		return super.getDisplayText(filterModeText);
	}
	
	@Override
	protected String getToolTipText(Object element) {
		if (element instanceof EActivityDef) {
			return ((EActivityDef)element).getDescription();
		}
		return super.getToolTipText(element);
	}
	
	/**
	 * This class is merely a placeholder for possible future sorting
	 * functionality in this view. It currently provides only the default
	 * behavior available in <code>ViewerSorter</code>.
	 */
	private class NameSorter extends ViewerSorter { /* placeholder */ }
	
	
	@Override
	protected TreeViewer buildTreeViewer(Composite parent) {
		TreeViewer treeViewer = new ActivityDictionaryViewer(parent);
		treeViewer.setContentProvider(new ActivityDictionaryContentProvider());
		treeViewer.setLabelProvider(new ViewLabelProvider());
		treeViewer.setSorter(new NameSorter());
		treeViewer.setInput(getViewSite());
		treeViewer.expandToLevel(2);
        treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        treeViewer.getTree().setData("name", "ActivityDictionaryView.activityTree");
		return treeViewer;
	}
	
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContextProvider.class)) {
			return new ContextProvider(ID);
		}
		return super.getAdapter(key);
	}
}
