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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ColumnProviderRegistry;
import gov.nasa.ensemble.core.plan.editor.merge.configuration.ColumnConfigurationResource;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorInput;

public class TableEditorUtils {

	public static TreeTableColumnConfiguration getTableConfiguration(String editorId, IEditorInput input) {
		return getTableConfiguration(editorId, input, null, null);
	}
	
	public static TreeTableColumnConfiguration getTableConfiguration(String editorId, IEditorInput input, List<AbstractMergeColumn> defaultColumns, List<? extends AbstractMergeColumn> extraColumns) {
		PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
		EPlan ePlan = model.getEPlan();
		List<AbstractMergeColumn> moreColumns = ColumnProviderRegistry.getInstance().getExtraColumns(ePlan);
		ResourceSet resourceSet = model.getEditingDomain().getResourceSet();
		if (extraColumns != null) {
			moreColumns.addAll(extraColumns);
		}
		resourceSet.getLoadOptions().put(ColumnConfigurationResource.EXTRA_COLUMNS, moreColumns);
		Resource resource = PlanEditorUtil.getPlanEditorTemplateResource(editorId, input);
		URI uri = resource.getURI();
		if (resource instanceof ColumnConfigurationResource) {
			return ((ColumnConfigurationResource) resource).getConfiguration();
		} else {
			resourceSet.getResources().remove(resource);
		}
		return ColumnConfigurationResource.createDefaultConfiguration(uri);
	}
	
	
	public static List<AbstractMergeColumn> getAllColumns(ColumnConfigurationResource ccr) {
		List<AbstractMergeColumn> columns = new ArrayList();
		if (ccr == null) {
			return columns;
		}
		columns.addAll(ColumnProviderRegistry.getInstance().getMergeColumns());
		ResourceSet resourceSet = ccr.getResourceSet();
		List<AbstractMergeColumn> extraColumns = getExtraColumns(resourceSet);
		if (extraColumns != null) {
			for (AbstractMergeColumn extra : extraColumns) {
				AbstractMergeColumn toAdd = extra;
				for (ITreeTableColumn configColumn : ccr.getConfiguration().getColumns()) {
					if(configColumn instanceof AbstractMergeColumn && 
						CommonUtils.equals(((AbstractMergeColumn) configColumn).getId(), extra.getId())) {
						toAdd = (AbstractMergeColumn) configColumn;
						break;
					}
				}
				columns.add(toAdd);
			}
		}
		return columns;
	}
	
	private static List<AbstractMergeColumn> getExtraColumns(ResourceSet resourceSet) {
		Object extraColumns = resourceSet.getLoadOptions().get(ColumnConfigurationResource.EXTRA_COLUMNS);
		if (extraColumns instanceof List<?>) {
			return (List<AbstractMergeColumn>) extraColumns;
		}
		return new ArrayList();
	}
	
	
}
