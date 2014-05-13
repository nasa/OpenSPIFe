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
package gov.nasa.ensemble.common.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.IViewLayout;

class ViewCapturingLayout implements IPageLayout, IFolderLayout, IViewLayout, IPerspectiveDescriptor {
	final Set<String> viewIds = new HashSet<String>();
	
	public void addFastView(String id) {
		viewIds.add(id);
	}
	
	public void addFastView(String id, float ratio) {
		viewIds.add(id);
	}
	
	public void addStandaloneView(String viewId, boolean showTitle,
			int relationship, float ratio, String refId) {
		viewIds.add(refId);
	}
	
	public void addStandaloneViewPlaceholder(String viewId,
			int relationship, float ratio, String refId, boolean showTitle) {
		viewIds.add(refId);
	}
	
	public void addView(String viewId, int relationship, float ratio, String refId) {
		viewIds.add(refId);
	}

	public void addActionSet(String actionSetId) {
		// no impl		
	}

	public void addNewWizardShortcut(String id) {
		// no impl		
	}

	public void addPerspectiveShortcut(String id) {
		// no impl		
	}

	public void addPlaceholder(String viewId, int relationship,
			float ratio, String refId) {
		// no impl		
	}

	public void addShowInPart(String id) {
		// no impl		
	}

	public void addShowViewShortcut(String id) {
		// no impl		
	}

	public IFolderLayout createFolder(String folderId, int relationship,
			float ratio, String refId) {
		return this;
	}

	public IPlaceholderFolderLayout createPlaceholderFolder(
			String folderId, int relationship, float ratio, String refId) {
		return this;
	}

	public IPerspectiveDescriptor getDescriptor() {
		return this;
	}

	public String getEditorArea() {
		return null;
	}

	public int getEditorReuseThreshold() {
		return 0;
	}

	public IPlaceholderFolderLayout getFolderForView(String id) {
		return this;
	}

	public IViewLayout getViewLayout(String id) {
		return this;
	}

	public boolean isEditorAreaVisible() {
		return false;
	}

	public boolean isFixed() {
		return false;
	}

	public void setEditorAreaVisible(boolean showEditorArea) {
		// no impl		
	}

	public void setEditorReuseThreshold(int openEditors) {
		// no impl		
	}

	public void setFixed(boolean isFixed) {
		// no impl		
	}

	public void addView(String viewId) {
		viewIds.add(viewId);
	}

	public void addPlaceholder(String viewId) {
		// no impl		
	}

	public String getProperty(String id) {
		return null;
	}

	public void setProperty(String id, String value) {
		// no impl		
	}

	public boolean getShowTitle() {
		return false;
	}

	public boolean isCloseable() {
		return false;
	}

	public boolean isMoveable() {
		return false;
	}

	public boolean isStandalone() {
		return false;
	}

	public void setCloseable(boolean closeable) {
		// no impl		
	}

	public void setMoveable(boolean moveable) {
		// no impl		
	}

	public String getDescription() {
		return null;
	}

	public String getId() {
		return null;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getLabel() {
		return null;
	}
}
