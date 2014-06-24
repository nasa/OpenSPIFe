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
package gov.nasa.ensemble.core.plan.advisor.provider;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.advisor.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ActivityAdvisorLabelDecorator implements ILabelDecorator {

	private static ImageDescriptor errorImageDescriptor
		= AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID
				, "icons/badges/violation_badge.png");

	private static ImageDescriptor fixedImageDescriptor
		= AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID
			, "icons/badges/fixed_badge.png");

	private static ImageDescriptor waivedImageDescriptor
		= AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID
			, "icons/badges/waived_badge.png");

	private static ImageDescriptor warningImageDescriptor
		= AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID
			, "icons/badges/warning_badge.png");

	private static final String propertyKey = "advisor.badge.quadrant";
	private static int quadrant = IDecoration.BOTTOM_LEFT;
	private static final String badgeLocation
		= EnsembleProperties.getProperty(propertyKey);

	public ActivityAdvisorLabelDecorator() {
		if(badgeLocation == null) {
			return;
		}
		
		if(badgeLocation.equalsIgnoreCase("TOP_LEFT")) {
			quadrant = IDecoration.TOP_LEFT;
		}

		else if(badgeLocation.equalsIgnoreCase("TOP_RIGHT")) {
			quadrant = IDecoration.TOP_RIGHT;
		}

		else if(badgeLocation.equalsIgnoreCase("BOTTOM_LEFT")) {
			quadrant = IDecoration.BOTTOM_LEFT;
		}

		else if(badgeLocation.equalsIgnoreCase("BOTTOM_RIGHT")) {
			quadrant = IDecoration.BOTTOM_RIGHT;
		}

		else if(badgeLocation.equalsIgnoreCase("REPLACE")) {
			quadrant = IDecoration.BOTTOM_LEFT;
		}

		else if(badgeLocation.equalsIgnoreCase("UNDERLAY")) {
			quadrant = IDecoration.UNDERLAY;
		}

		else {
			LogUtil.warn("unsupported value entered for " + propertyKey + ", using default value instead");
		}
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		if (element instanceof EPlanElement) {
			int severity = EPlanUtils.getSeverity(element);

			if (severity == IMarker.SEVERITY_ERROR) {
				return new DecorationOverlayIcon(image, errorImageDescriptor, quadrant).createImage();
			}

			else if (severity == MarkerConstants.SEVERITY_FIXED) {
				return new DecorationOverlayIcon(image, fixedImageDescriptor, quadrant).createImage();
			}

			else if (severity == IMarker.SEVERITY_WARNING) {
				return new DecorationOverlayIcon(image, warningImageDescriptor, quadrant).createImage();
			}
			
			else if (severity == MarkerConstants.SEVERITY_WAIVED) {
				return new DecorationOverlayIcon(image, waivedImageDescriptor, quadrant).createImage();
			}

		}

		return image;
	}

	@Override
	public String decorateText(String text, Object element) {
		return text;
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
