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
package gov.nasa.arc.spife.ui.timeline.action;


import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class FreezeSectionAction extends Action {
	public static final ImageDescriptor FREEZE_SECTION_IMAGE = 
			ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/freezeSection.png"));
	
	private Timeline timeline;
	private Section section;
	
	public FreezeSectionAction(Timeline timeline, String name, Section section) {
		super(name);
		setImageDescriptor(FREEZE_SECTION_IMAGE);
		this.timeline = timeline;
		this.section = section;
	}

	@Override
	public void run() {
		ETimeline timelineModel = timeline.getTimelineModel();
		EList<Section> contents = timelineModel.getContents();
		int index = contents.indexOf(section);
		if (index != -1) {
			EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(timelineModel);
			Section section = contents.get(index);
			Section copy = EcoreUtil.copy(section);
			Command addCommand = AddCommand.create(domain, timelineModel, TimelinePackage.Literals.ETIMELINE__TOP_CONTENTS, Collections.singleton(copy));
			EMFUtils.executeCommand(domain, addCommand);
			timeline.layoutTimelineContentInDisplayThread();
		}
	}
}
