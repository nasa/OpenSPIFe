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
package gov.nasa.ensemble.common.ui.ide.navigator;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.rcp.RcpPlugin;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

public class MarkerLabelDecorator implements ILightweightLabelDecorator {

	private static ImageDescriptor ERROR_OVERLAY =  RcpPlugin.imageDescriptorFromPlugin(RcpPlugin.PLUGIN_ID, "icons/overlay_error.png");
	private static ImageDescriptor WARNING_OVERLAY =  RcpPlugin.imageDescriptorFromPlugin(RcpPlugin.PLUGIN_ID, "icons/overlay_warning.png");
	
	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IResource) {
			IResource resource = (IResource)element;
			if(resource.isAccessible()) {
				try {
					int severity = resource.findMaxProblemSeverity(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
					switch (severity) {
					case IMarker.SEVERITY_ERROR:
						decoration.addOverlay(ERROR_OVERLAY);
						break;
					case IMarker.SEVERITY_WARNING:
						decoration.addOverlay(WARNING_OVERLAY);
						break;
					}
				} catch (CoreException e) {
					LogUtil.error("deteriming severity", e);
				}
			}
		}
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// no impl
	}

	@Override
	public void dispose() {
		// no impl
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// no impl
	}
	
}

