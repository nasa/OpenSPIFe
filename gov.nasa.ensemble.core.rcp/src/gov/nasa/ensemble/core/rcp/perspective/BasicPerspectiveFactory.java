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

import gov.nasa.ensemble.common.logging.LogUtil;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * The parent class for all Ensemble perspectives. 
 *
 */
public class BasicPerspectiveFactory implements IPerspectiveFactory {
    
    public BasicPerspectiveFactory() {
		// nothing to do
    }
    
	public String getViewIdFromClassName(String viewClassName) {
		String viewId = null;
		try {
			
			// Try to find the bundle using the given class name.
			// This approach assumes that the bundle name (i.e. plug-in
			// name) is a substring of the fully qualified class name.
			Bundle bundle = null;
			String symbolicName = viewClassName;
			
			while(bundle == null && symbolicName.indexOf('.') != -1) {
				symbolicName = symbolicName.substring(0, symbolicName.lastIndexOf('.'));
				bundle = Platform.getBundle(symbolicName);
			}
			
			if(bundle != null) {
				Class<?> viewClass = bundle.loadClass(viewClassName);
				viewId = (String)viewClass.getDeclaredField("ID").get(viewClass);
			} else {
				LogUtil.error("Unable to find bundle for class " + viewClassName);
			}
			
		} catch (ClassNotFoundException cnfe) {
			LogUtil.error(viewClassName + " not found.");
		} catch (IllegalAccessException iae) {
			LogUtil.error(viewClassName + ".ID field can not be accessed.");
		} catch (NoSuchFieldException nsfe) {
			LogUtil.error(viewClassName + ".ID is missing.");
		}
		
		return viewId;
	}	
	
    /**
     *  Implements IPerspectiveFactory.  Automatically adds all registered perspectives to the shortcut bar.  This ensures
     *  that all perspectives will be easily accessible regardless of the perspective that is currently selected. 
     */
    @Override
	public void createInitialLayout(IPageLayout layout) {
    	for (IPerspectiveDescriptor per : PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives()) {
    		layout.addPerspectiveShortcut(per.getId());
    	}
    }


	/**
	 * Add the view with the given class name to the "Show View" menu. 
	 * @param layout
	 * @param name
	 */
	protected void addShowViewShortcut(IPageLayout layout, String name) {
		String constraintsViewId = getViewIdFromClassName(name);
		if (constraintsViewId != null) {
			layout.addShowViewShortcut(constraintsViewId);
		}
	}

	/**
	 * Add the view with the given class name to the layout in the given place.
	 * Also adds the view to the "Show View" menu.
	 * @param layout
	 * @param name
	 * @param relationship
	 * @param ratio
	 */
	protected String addViewByEditor(IPageLayout layout, String name, int relationship, float ratio) {
		String editorArea = layout.getEditorArea();
		String viewId = addViewByTarget(layout,name,editorArea,relationship,ratio);
		return viewId;
	}
	
	protected String addViewByTarget(IPageLayout layout, String name, String target, int relationship, float ratio) {
		String viewId = getViewIdFromClassName(name);
		if (viewId != null) {
			layout.addShowViewShortcut(viewId);
			layout.addView(viewId, relationship, ratio, target);
		}
		return viewId;
	}

	protected IFolderLayout addFolderByEditor(IPageLayout layout, String name, int relationship, float ratio) {
		String editorArea = layout.getEditorArea();
		return layout.createFolder(name, relationship, ratio, editorArea);
	}
	
	protected String addViewPlaceholderByEditor(IPageLayout layout, String name, int relationship, float ratio) {
		String editorArea = layout.getEditorArea();
		String viewId = addViewPlaceholderByTarget(layout,name,editorArea,relationship,ratio);
		return viewId;
	}
	
	protected String addViewPlaceholderByTarget(IPageLayout layout, String name, String target, int relationship, float ratio) {
		String viewId = getViewIdFromClassName(name);
		if (viewId != null) {
			layout.addShowViewShortcut(viewId);
			layout.addPlaceholder(viewId, relationship, ratio, target);
		}
		return viewId;
	}

    public void setViewMovableAndClosable(IPageLayout layout, String id, boolean state) {
		try {
			layout.getViewLayout(id).setCloseable(state);
			layout.getViewLayout(id).setMoveable(state);
		} catch (AssertionFailedException e) {
			LogUtil.error("Can not find view id: "+id,e);
		}
    }
    
	public void setViewMovableNotClosable(IPageLayout layout, String id) {
		try {
			layout.getViewLayout(id).setCloseable(false);
			layout.getViewLayout(id).setMoveable(true);
		} catch (AssertionFailedException e) {
			LogUtil.error("Can not find view id: "+id,e);
		}
	}
        
}
