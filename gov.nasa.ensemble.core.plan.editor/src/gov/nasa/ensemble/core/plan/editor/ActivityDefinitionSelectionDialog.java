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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.Comparator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

public class ActivityDefinitionSelectionDialog extends FilteredItemsSelectionDialog {

	public ActivityDefinitionSelectionDialog(Shell shell) {
		super(shell);
		
		setTitle("Select activity definition");
		ActivityDefLabelProvider labelProvider = new ActivityDefLabelProvider();
		setListLabelProvider(labelProvider);
		setDetailsLabelProvider(labelProvider);
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ActivityDefinitionItemsFilter();
	}

	@Override
	protected void fillContentProvider(
		AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor) 
	{

		int totalWork = ActivityDictionary.getInstance().getActivityDefs().size();
		progressMonitor.beginTask("Searching...", totalWork);
		for (EActivityDef def : ActivityDictionary.getInstance().getActivityDefs()) {
			if (progressMonitor.isCanceled()) {
				break;
			}
			contentProvider.add(def, itemsFilter);
			progressMonitor.worked(1);
		}
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		String sectionName = getClass().getName();
		IDialogSettings settings = EditorPlugin.getDefault().getDialogSettings().getSection(sectionName);
		if (settings == null) {
			settings = EditorPlugin.getDefault().getDialogSettings().addNewSection(sectionName);
		}
		return settings;
	}

	@Override
	public String getElementName(Object item) {
		if (item instanceof EActivityDef) {
			return ((EActivityDef)item).getName();
		}
		return null;
	}

	@Override
	protected Comparator getItemsComparator() {
		return new Comparator<EActivityDef>() {

			@Override
			public int compare(EActivityDef a0, EActivityDef a1) {
				return a0.getName().compareTo(a1.getName());
			}
			
		};
	}

	@Override
	protected IStatus validateItem(Object item) {
		return new Status(IStatus.OK, EditorPlugin.ID, 0, "", null); //$NON-NLS-1$
	}
	
	private class ActivityDefinitionItemsFilter extends ItemsFilter {

		@Override
		public boolean isConsistentItem(Object item) {
			return item instanceof EActivityDef;
		}

		@Override
		public boolean matchItem(Object item) {
			if (!isConsistentItem(item)) {
				return false;
			}
			return matches(((EActivityDef)item).getName());
		}
		
	}

	private static class ActivityDefLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element instanceof EActivityDef) {
				return MissionUIConstants.getInstance().getIcon(((EActivityDef)element).getName());
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof EActivityDef) {
				return ((EActivityDef)element).getName() + " - " + ((EActivityDef)element).getCategory();
			}
			return null;
		}
		
	}

}
