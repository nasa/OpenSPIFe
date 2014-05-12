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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.ensemble.common.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class PlatformObjectTimelineHeaderEditPart<T extends IAdaptable> extends TreeTimelineHeaderEditPart<T> {

	private ITreeContentProvider treeContentProvider = null;

	@Override
	protected List getModelChildren() {
		List<Object> children = new ArrayList<Object>();
		ITreeContentProvider contentProvider = getContentProvider();
		if (contentProvider != null) {
			children.addAll(Arrays.asList(contentProvider.getChildren(getModel())));
		}
		return children;
	}

	private ITreeContentProvider getContentProvider() {
		if (treeContentProvider == null) {
			treeContentProvider = CommonUtils.getAdapter(getModel(), ITreeContentProvider.class);
		}
		return treeContentProvider;
	}

}
