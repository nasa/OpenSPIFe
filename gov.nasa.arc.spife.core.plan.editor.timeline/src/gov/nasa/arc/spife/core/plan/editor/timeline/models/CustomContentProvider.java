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
package gov.nasa.arc.spife.core.plan.editor.timeline.models;

import gov.nasa.arc.spife.core.plan.timeline.PlanSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow;
import gov.nasa.ensemble.core.model.plan.EPlan;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;

public class CustomContentProvider extends PlanSectionContentProvider {

	public CustomContentProvider(EPlan plan, PlanSection section) {
		super(plan, section);
	}

	@Override
	protected Collection getGroupingValues() {
		super.getGroupingValues();
		return getSection().getRows();
	}

	@Override
	protected boolean isFixedValueWrapper(Object wrapper) {
		return true;
	}

	@Override
	@SuppressWarnings("cast")
	protected Object getValueWrapper(Object value) {
		return value;
	}

	@Override
	public void handleResourceSetChanged(ResourceSetChangeEvent event) {
		super.handleResourceSetChanged(event);

		Set<Object> contentForRefresh = new LinkedHashSet<Object>();
		for (Notification n : event.getNotifications()) {
			Object notifier = n.getNotifier();
			if (notifier == getSection()
					|| (notifier instanceof PlanSectionRow
							&& ((PlanSectionRow) notifier).eContainer() == getSection())) {
				contentForRefresh.add(notifier);
			}
		}
		
		if (!contentForRefresh.isEmpty()) {
			update();
			refreshContents(contentForRefresh);
		}
	}
	
}
