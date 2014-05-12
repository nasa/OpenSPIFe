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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.type.stringifier.IntegerStringifier;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.util.PlanHierarchyPositionMaintenerService;
import gov.nasa.ensemble.core.plan.editor.merge.AbstractPlanMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;

import java.util.ArrayList;
import java.util.List;

public class PlanHierarchyPositionColumnProvider extends AbstractPlanMergeColumnProvider {

	private static final IntegerStringifier STRINGIFIER = new IntegerStringifier();
	private static final String PROVIDER_NAME = "Reference";
	
	private List<AbstractMergeColumn> columns = new ArrayList();
	
	public PlanHierarchyPositionColumnProvider(EPlan plan) {
		super(plan);
		if (PlanHierarchyPositionMaintenerService.isHierarchyPositionMaintained()) {
			columns.add(new HierarchyPositionColumn(this));
		}
	}

	@Override
	public List<? extends AbstractMergeColumn> getColumns() {
		return columns;
	}

	@Override
	public String getName() {
		return PROVIDER_NAME;
	}
	
	private class HierarchyPositionColumn extends AbstractMergeColumn<Integer> {

		public HierarchyPositionColumn(IMergeColumnProvider provider) {
			super(provider, "Reference", 10);
		}

		@Override
		public boolean needsUpdate(Object feature) {
			return (feature == PlanPackage.Literals.EPLAN_PARENT__CHILDREN);
		}

		@Override
		public Integer getFacet(Object element) {
			if (element instanceof EPlanChild) {
				return ((EPlanChild) element).getHierarchyPosition();
			}
			return null;
		}
		
		@Override
		public String getText(Integer facet) {
			if (CommonUtils.equals(facet, -1)) {
				return "";
			}
			return STRINGIFIER.getDisplayString(facet);
		}
		
		@Override
		public boolean updateAll() {
			return true;
		}
		
	}

}
