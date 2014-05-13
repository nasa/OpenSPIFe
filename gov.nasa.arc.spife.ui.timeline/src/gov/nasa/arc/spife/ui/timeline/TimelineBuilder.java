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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.timeline.model.TimelineBuilderContent;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.resource.ImageDescriptor;


public abstract class TimelineBuilder<T> extends AdapterImpl {

	public static final ImageDescriptor IMAGE_TIMELINE_DESCRIPTOR = ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/timeline_part.gif"));
	public static final ImageDescriptor IMAGE_TIMELINE_DESCRIPTOR_NEW = ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/new_timeline_part.gif"));
	
	public abstract String getName();

	public ImageDescriptor getActionImageDescriptor() {
		return IMAGE_TIMELINE_DESCRIPTOR_NEW;
	}
	
	public Section createTimelineSection() {
		TimelineBuilderContent section = TimelineFactory.eINSTANCE.createTimelineBuilderContent();
		section.setIdentifier(getName());
		return section;
	}
	
	public abstract TimelineViewer build(Timeline timeline, T model);

}
