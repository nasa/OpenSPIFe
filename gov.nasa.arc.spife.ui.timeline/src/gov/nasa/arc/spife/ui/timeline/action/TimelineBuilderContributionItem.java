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
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineBuilder;
import gov.nasa.arc.spife.ui.timeline.TimelineBuilderRegistry;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;

public class TimelineBuilderContributionItem extends ContributionItem {

	public TimelineBuilderContributionItem() {
		// default constructor
	}

	public TimelineBuilderContributionItem(String id) {
		super(id);
	}

	@Override
	public void fill(Menu menu, int index) {
		ImageDescriptor imageTimelineDescriptor = TimelineBuilder.IMAGE_TIMELINE_DESCRIPTOR;
		MenuManager addSectionItem = new MenuManager("Add timeline Section", imageTimelineDescriptor, getId());
		List<TimelineBuilder> builders = TimelineBuilderRegistry.getInstance().getAllTimelineBuilders();
		for (final TimelineBuilder builder : builders) {
			String text = builder.getName();
			ImageDescriptor imageDescriptor = builder.getActionImageDescriptor();
			if (imageDescriptor == null) {
				imageDescriptor = imageTimelineDescriptor;
			}
			Action action = new Action(text, imageDescriptor) {
				@Override
				public void runWithEvent(Event event) {
					IEditorPart activeEditor = EditorPartUtils.getCurrent();
					Timeline timeline = TimelineUtils.getTimeline(activeEditor);
					if (timeline == null) {
						return;
					}
					EObject section = builder.createTimelineSection();
					final ETimeline timelineModel = timeline.getTimelineModel();
					TransactionalEditingDomain domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(timelineModel);
					Command command = AddCommand.create(domain, timelineModel, TimelinePackage.Literals.ETIMELINE__CONTENTS, Collections.singleton(section));
					EMFUtils.executeCommand(domain, command);
				}
			};
			addSectionItem.add(action);
		}
		addSectionItem.fill(menu, index);
	}

}
