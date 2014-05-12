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
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.merge.TableEditorUtils;
import gov.nasa.ensemble.core.plan.editor.merge.configuration.ColumnConfigurationResource;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;

public class ColumnsPicklistWizardPage extends AbstractPicklistWizardPage {

	private List<AbstractMergeColumn> columns;
	private Map<String, AbstractMergeColumn> pickMap;
	private String[] specialChoices = new String[0];
	
	public ColumnsPicklistWizardPage(String pageName, String title,
			ImageDescriptor titleImage, ColumnConfigurationResource configuration) {
		super(pageName, title, titleImage);
		this.columns = getColumns(configuration);
	}
	
	public ColumnsPicklistWizardPage(String pageName, ColumnConfigurationResource configuration) {
		this(pageName, pageName, null, configuration);
	}
	
	static public ColumnConfigurationResource getTableColumnConfigurationResource(EPlan plan) {
		EList<Resource> resources = plan.eResource().getResourceSet().getResources();
		for (Resource resource : resources) {
			if (resource instanceof ColumnConfigurationResource && CommonUtils.equals(resource.getURI().fileExtension(), "table")) {
				return (ColumnConfigurationResource) resource;
			}
		}
		return null;
	}
	
	private List<AbstractMergeColumn> getColumns(ColumnConfigurationResource configuration) {
		List<AbstractMergeColumn> allColumns = new ArrayList();
		allColumns.add(MergeEditorPreferences.nameMergeColumn);
		allColumns.addAll(TableEditorUtils.getAllColumns(configuration));
		return allColumns;
	}
	
	/** @returns Type of one of the selected Pick names, or null if unknown. */
	public Class<? extends AbstractMergeColumn> getPickType(String PickName) {
		AbstractMergeColumn pickObject = getPickObject(PickName);
		if (pickObject==null) return null;
		return pickObject.getClass();
	}
	
	/** @returns Object one of the selected Pick names, or null if unknown. */
	public AbstractMergeColumn getPickObject(String pickName) {
		return pickMap.get(pickName);
	}

	public void filterChoicesByType(Class<? extends AbstractMergeColumn>... include) {
		List<AbstractMergeColumn> includedColumns = new LinkedList<AbstractMergeColumn>();
		for (AbstractMergeColumn column : columns) {
			for (Class includedClass : include) {
				if (includedClass.isInstance(column)) {
					includedColumns.add(column);
				}
			}
		}
		columns = includedColumns;
	}

	/** Controls which things may not be on the attribute list and may need to be fetched with a special line of code. */
	public void setSpecialChoices(String... choices) {
		specialChoices = choices;
	}

	@Override
	public Collection<String> computeAvailableChoices() {
		pickMap = new LinkedHashMap(); 
		if (columns != null) {
			for (AbstractMergeColumn column : columns) {
				pickMap.put(column.getId(), column);
			}

			LinkedList<String> columnNames = new LinkedList();
			Collections.addAll(columnNames, specialChoices);
			columnNames.addAll(pickMap.keySet());
			return columnNames;
		}
		return Collections.EMPTY_LIST;
	}

	public List<AbstractMergeColumn> getSelectedColumns(){
		String[] names = this.getSelectedPickNames();
		ArrayList<AbstractMergeColumn> result = new ArrayList<AbstractMergeColumn>();
		for( String name:names){
			for( AbstractMergeColumn column:this.pickMap.values()){
				if(column.getId().equalsIgnoreCase(name)){
					result.add(column);
				}
			}
		}
		return result;
	}
}
