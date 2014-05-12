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
package gov.nasa.arc.spife.ui.navigator;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class SpifeNavigatorLabelProvider extends ColumnLabelProvider {

	private static final ILabelProvider WORKBENCH_LABEL_PROVIDER = new WorkbenchLabelProvider();
	private static final int MAXIMUM_TOOLTIP_MESSAGES = 10;
	
	@Override
	public Image getImage(Object element) {
		Image image = null;
		if (element instanceof IResource) {
			IResource resource = (IResource) element;
			SpifeGenericProjectNature nature = getSpifeNature(resource);
			if (nature != null) {
				image = nature.getImage(resource);
			}
		}
		if (image != null) {
			return image;
		}
		return WORKBENCH_LABEL_PROVIDER.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		return WORKBENCH_LABEL_PROVIDER.getText(element);
	}
	
	private SpifeGenericProjectNature getSpifeNature(IResource resource) {
		SpifeLabelProviderNature labelProviderNature = getSpifeLabelProviderNature(resource);
		if (labelProviderNature != null) {
			return labelProviderNature;
		}
		try {
			IProject project = resource.getProject();
			if (project != null) {
				IProjectNature nature = project.getNature(SpifeProjectNature.ID);
				if (nature instanceof SpifeProjectNature) {
					return (SpifeProjectNature) nature;
				}
			}
		} catch (CoreException e) {
			//silent fail
		}
		return null;
	}

	private SpifeLabelProviderNature getSpifeLabelProviderNature(IResource resource) {
		IProject project = resource.getProject();
		if (project != null) {
			try {
				IProjectDescription description = project.getDescription();
				String[] natureIds = description.getNatureIds();
				List<SpifeLabelProviderNature> labelProviderNatures = new ArrayList();
				for (String natureID : natureIds) {
					IProjectNature nature = project.getNature(natureID);
					if (nature instanceof SpifeLabelProviderNature) {
						labelProviderNatures.add((SpifeLabelProviderNature) nature);
					}
				}
				if (labelProviderNatures.size() > 1) {
					LogUtil.error("Found " + labelProviderNatures.size() + " SpifeLabelProviderNature's. Can't have more than one! Returning generic.");
				} else if (labelProviderNatures.size() == 1){
					return labelProviderNatures.get(0);
				}
			} catch (CoreException e) {
				//silent fail
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipShift(java.lang.Object)
	 */
	@Override
	public Point getToolTipShift(Object object) {
		return new Point(5,5);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipDisplayDelayTime(java.lang.Object)
	 */
	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 1000;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipTimeDisplayed(java.lang.Object)
	 */
	@Override
	public int getToolTipTimeDisplayed(Object object) {
		return 5000;
	}
	
	/**
	 * Get the text displayed in the tool tip for object.
	 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return the {@link String} or <code>null</code> if there is not text to
	 *         display
	 */
	@Override
	public String getToolTipText(Object element) {
		StringBuilder builder = new StringBuilder();
		if (element instanceof IResource) {
			IResource resource = (IResource)element;
			try {
				final IMarker[] problemMarkers = resource.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
				if (problemMarkers.length > 0) {
					int numMarkersToDisplay = Math.min(problemMarkers.length, MAXIMUM_TOOLTIP_MESSAGES);
					for (int i = 0; i < numMarkersToDisplay; i++) {
						IMarker marker = problemMarkers[i];
						if (i > 0) {
							builder.append('\n');
						}
						builder.append(marker.getAttribute(IMarker.MESSAGE));
					}
					if (problemMarkers.length > MAXIMUM_TOOLTIP_MESSAGES) {
						builder.append("\n... and " + (problemMarkers.length - MAXIMUM_TOOLTIP_MESSAGES) + " more violations");
					}
				}
			} catch (CoreException e) {
				// skip
			}
		}
		if (builder.length() == 0) {
			return null;
		}
		return builder.toString();
	}
}
