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
package gov.nasa.ensemble.core.rcp.perspective;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.ui.ForbiddenWorkbenchUtils;
import gov.nasa.ensemble.common.ui.detail.view.DetailView;

import java.util.Collections;
import java.util.Properties;

import org.eclipse.ui.IPageLayout;

public final class PlanningPerspectiveFactory extends BasicPerspectiveFactory {

	public static final String PERSPECTIVE_CATEGORY_ID = "gov.nasa.ensemble.core.rcp.perspective.category.planning";
	private static final String PERSPECTIVE_ID = "gov.nasa.ensemble.core.rcp.perspective.planning";

	private static final String DETAIL_VIEW = DetailView.ID;
	private static final String DICTIONARY_VIEW = "gov.nasa.ensemble.core.activityDictionary.view.ActivityDictionaryView";

	/**
	 * Creates the initial planning perspective layout.
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		layout.setEditorAreaVisible(true);
		addViewByEditor(layout, DETAIL_VIEW, IPageLayout.RIGHT, 0.75f);
		if (showActivityDictionaryView()) {
			addViewByEditor(layout, DICTIONARY_VIEW, IPageLayout.BOTTOM, 0.6f);
		} else {
			addViewPlaceholderByEditor(layout, DICTIONARY_VIEW, IPageLayout.BOTTOM, 0.6f);
		}
		setViewMovableNotClosable(layout, DICTIONARY_VIEW);
		// We register the detail view, because we constructed it as part of this method and
		// also in the plugin.xml.  It is registered here for layout purposes, and registered
		// in the plugin.xml to define the plugin id so that it can be retrieved by ClassIdRegistry. (see DetailView.ID)
		ForbiddenWorkbenchUtils.registerExistingParts(Collections.singletonList(DETAIL_VIEW));
		// Because the extension mechanism processes extensions in alphabetical order, we use
		// our method to register them first, in the order in which the dependencies require
		// them to be registered.
		ForbiddenWorkbenchUtils.instantiatePerspectiveExtension(layout);
	}

	/**
	 * @return the perspective id
	 */
	public static String getPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	/**
	 * Switch to the planning perspective if needed.
	 */
	public static void switchToPlanningPerspective() {
		if (!PerspectiveUtils.switchToCategory(PERSPECTIVE_CATEGORY_ID)) {
			PerspectiveUtils.switchToPerspective(PERSPECTIVE_ID);
		}
	}

	private boolean showActivityDictionaryView() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		if (CommonPlugin.isJunitRunning()) {
			String value = properties.getProperty("activitydictionary.showview");
			if (value != null) {
				return (value.equalsIgnoreCase("true")) ? true : false;
			}
		}
		String value = properties.getProperty("activitydictionary.showview");
		if (value != null) {
			return (value.equalsIgnoreCase("true")) ? true : false;
		}
		return false;
	}

}
