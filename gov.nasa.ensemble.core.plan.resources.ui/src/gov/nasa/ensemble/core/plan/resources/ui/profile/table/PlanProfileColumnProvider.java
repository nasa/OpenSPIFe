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
package gov.nasa.ensemble.core.plan.resources.ui.profile.table;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.editor.merge.AbstractPlanMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlanProfileColumnProvider extends AbstractPlanMergeColumnProvider {

	private static final String COLUMN_PROVIDER_NAME = "Profile";
	private static final ProfileColumnComparator COMPARATOR = new ProfileColumnComparator(); 
	
	private final List<ProfileColumn> columns = new ArrayList<ProfileColumn>();
	
	public PlanProfileColumnProvider(EPlan plan) {
		super(plan);
		final ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		if (member != null) {
			for (Profile<?> profile : member.getResourceProfiles()) {
				columns.add(new ProfileColumn(this, profile));
			}
		}
		Collections.sort(columns, COMPARATOR);
	}
	
	@Override
	public List<? extends AbstractMergeColumn> getColumns() {
		return columns;
	}

	@Override
	public String getName() {
		return COLUMN_PROVIDER_NAME;
	}
	
	private static class ProfileColumnComparator implements Comparator<ProfileColumn> {
		
		@Override
		public int compare(ProfileColumn o1, ProfileColumn o2) {
			return o1.getHeaderName().compareTo(o2.getHeaderName());
		}
		
	}

}
