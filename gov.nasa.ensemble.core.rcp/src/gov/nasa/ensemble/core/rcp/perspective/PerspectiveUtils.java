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

import gov.nasa.ensemble.common.collections.AutoListMap;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

public class PerspectiveUtils {
	
	private static final String PERSPECTIVE_CATEGORY_EXTENSION = "gov.nasa.ensemble.core.rcp.perspectiveCategory";
	private static final PerspectiveUtils INSTANCE = new PerspectiveUtils();
	
	private final Map<String, List<String>> categoryIdToPerspectiveIds = new AutoListMap<String, String>(String.class);
	
	private PerspectiveUtils() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IExtensionPoint extensionPoint = registry.getExtensionPoint(PERSPECTIVE_CATEGORY_EXTENSION);
			if (extensionPoint != null) {
				IExtension[] extensions = extensionPoint.getExtensions();
				for (IExtension extension : extensions) {
					IConfigurationElement[] elements = extension.getConfigurationElements();
					for (IConfigurationElement element : elements) {
						String categoryId = element.getAttribute("categoryId");
						String perspectiveId = element.getAttribute("perspectiveId");
						categoryIdToPerspectiveIds.get(categoryId).add(perspectiveId);
					}
				}
			}
		}
	}
	
	/**
	 * Switch to the perspective specified
	 * 
	 * @param categoryId
	 * @return true if there was no error in showing the perspective 
	 */
	public static boolean switchToPerspective(String perspectiveId) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			try {
                workbench.showPerspective(perspectiveId, window);
                return true;
            } catch (WorkbenchException e) {
    			Logger logger = Logger.getLogger(PerspectiveUtils.class);
    			logger.error("Error switching to perspectiveId " + perspectiveId, e);
            }
		}
		return false;
	}
	
	/**
	 * Switch to a perspective of the category specified,
	 * if the current perspective is not in the category.
	 * 
	 * @param categoryId
	 * @return true if the current perspective is in the category,
	 *         or true if one of the perspectives in the category
	 *         could be shown without an error 
	 */
	public static boolean switchToCategory(String categoryId) {
		return INSTANCE.doSwitchToCategory(categoryId);
	}
	private boolean doSwitchToCategory(String categoryId) {
		if (!categoryIdToPerspectiveIds.containsKey(categoryId)) {
			Logger logger = Logger.getLogger(PerspectiveUtils.class);
			logger.warn("No perspectives defined for categoryId: " + categoryId);
			return false;
		}
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			String currentPerspectiveId = null;
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IPerspectiveDescriptor perspectiveDesc = page.getPerspective();
					if (perspectiveDesc != null) {
						currentPerspectiveId = perspectiveDesc.getId();
					}
				}
			}
			List<String> perspectiveIds = categoryIdToPerspectiveIds.get(categoryId);
			if ((currentPerspectiveId != null) && perspectiveIds.contains(currentPerspectiveId)) {
				return true;
			}
			for (String perspectiveId : perspectiveIds) {
				try {
                    workbench.showPerspective(perspectiveId, window);
                    return true;
                } catch (WorkbenchException e) {
        			Logger logger = Logger.getLogger(PerspectiveUtils.class);
        			logger.error("Error switching to perspectiveId: " + perspectiveId + " for categoryId: " + categoryId, e);
                }
			}
		}
		return false;
	}
	
}
