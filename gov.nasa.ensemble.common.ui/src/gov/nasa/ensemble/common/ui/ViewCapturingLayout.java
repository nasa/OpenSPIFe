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
	
	@Override
	public void addFastView(String id) {
		viewIds.add(id);
	}
	
	@Override
	public void addFastView(String id, float ratio) {
		viewIds.add(id);
	}
	
	@Override
	public void addStandaloneView(String viewId, boolean showTitle,
			int relationship, float ratio, String refId) {
		viewIds.add(refId);
	}
	
	@Override
	public void addStandaloneViewPlaceholder(String viewId,
			int relationship, float ratio, String refId, boolean showTitle) {
		viewIds.add(refId);
	}
	
	@Override
	public void addView(String viewId, int relationship, float ratio, String refId) {
		viewIds.add(refId);
	}

	@Override
	public void addActionSet(String actionSetId) {
		// no impl		
	}

	@Override
	public void addNewWizardShortcut(String id) {
		// no impl		
	}

	@Override
	public void addPerspectiveShortcut(String id) {
		// no impl		
	}

	@Override
	public void addPlaceholder(String viewId, int relationship,
			float ratio, String refId) {
		// no impl		
	}

	@Override
	public void addShowInPart(String id) {
		// no impl		
	}

	@Override
	public void addShowViewShortcut(String id) {
		// no impl		
	}

	@Override
	public IFolderLayout createFolder(String folderId, int relationship,
			float ratio, String refId) {
		return this;
	}

	@Override
	public IPlaceholderFolderLayout createPlaceholderFolder(
			String folderId, int relationship, float ratio, String refId) {
		return this;
	}

	@Override
	public IPerspectiveDescriptor getDescriptor() {
		return this;
	}

	@Override
	public String getEditorArea() {
		return null;
	}

	@Override
	public int getEditorReuseThreshold() {
		return 0;
	}

	@Override
	public IPlaceholderFolderLayout getFolderForView(String id) {
		return this;
	}

	@Override
	public IViewLayout getViewLayout(String id) {
		return this;
	}

	@Override
	public boolean isEditorAreaVisible() {
		return false;
	}

	@Override
	public boolean isFixed() {
		return false;
	}

	@Override
	public void setEditorAreaVisible(boolean showEditorArea) {
		// no impl		
	}

	@Override
	public void setEditorReuseThreshold(int openEditors) {
		// no impl		
	}

	@Override
	public void setFixed(boolean isFixed) {
		// no impl		
	}

	@Override
	public void addView(String viewId) {
		viewIds.add(viewId);
	}

	@Override
	public void addPlaceholder(String viewId) {
		// no impl		
	}

	@Override
	public String getProperty(String id) {
		return null;
	}

	@Override
	public void setProperty(String id, String value) {
		// no impl		
	}

	@Override
	public boolean getShowTitle() {
		return false;
	}

	@Override
	public boolean isCloseable() {
		return false;
	}

	@Override
	public boolean isMoveable() {
		return false;
	}

	@Override
	public boolean isStandalone() {
		return false;
	}

	@Override
	public void setCloseable(boolean closeable) {
		// no impl		
	}

	@Override
	public void setMoveable(boolean moveable) {
		// no impl		
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getLabel() {
		return null;
	}
}
