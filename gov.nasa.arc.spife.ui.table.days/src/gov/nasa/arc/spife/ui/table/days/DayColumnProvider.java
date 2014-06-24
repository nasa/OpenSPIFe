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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ColumnProviderRegistry;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ObjectDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DayColumnProvider implements IMergeColumnProvider {

	private static final ColumnProviderRegistry REGISTRY = ColumnProviderRegistry.getInstance();
	private static final Collection<? extends AbstractMergeColumn> DEFAULT_COLUMNS = Arrays.asList(
		REGISTRY.getMergeColumnById("Temporal - Start Time"),
		REGISTRY.getMergeColumnById("Temporal - Duration Extent")
	);
	
	private final List<AbstractMergeColumn> mentionedColumns = new ArrayList<AbstractMergeColumn>();
	private final List<AbstractMergeColumn> allColumns = new ArrayList<AbstractMergeColumn>();
	
	public DayColumnProvider(EPlan plan) {
		MentionedResourceObjectsVisitor visitor = new MentionedResourceObjectsVisitor();
		visitor.visitAll(plan);
		ActivityDictionary AD = ActivityDictionary.getInstance();
		for (ObjectDef objectDef : AD.getDefinitions(ObjectDef.class)) {
			for (EStructuralFeature feature : objectDef.getEStructuralFeatures()) {
				if (feature instanceof ENumericResourceDef) {
					ENumericResourceDef resourceDef = (ENumericResourceDef) feature;
					Set<EObject> set = visitor.getResourceObjects().get(resourceDef);
					for (EObject object : EMFUtils.getReachableObjectsOfType(plan, objectDef)) {
						DayResourceColumn column = new DayResourceColumn(this, object, resourceDef) {
							@Override
							public int getDefaultWidth() {
								return 45;
							}
						};
						allColumns.add(column); 
						if (set.contains(object)) {
							mentionedColumns.add(column);
						}
					}
				}
			}
		}
		if (allColumns.isEmpty()) {
			allColumns.addAll(DEFAULT_COLUMNS);
			mentionedColumns.addAll(DEFAULT_COLUMNS);
		}
	}
	
	public List<? extends AbstractMergeColumn> getMentionedColumns() {
		return mentionedColumns;
	}
	
	@Override
	public List<? extends AbstractMergeColumn> getColumns() {
		return allColumns;
	}

	@Override
	public String getName() {
		return "Resources";
	}
		
}
