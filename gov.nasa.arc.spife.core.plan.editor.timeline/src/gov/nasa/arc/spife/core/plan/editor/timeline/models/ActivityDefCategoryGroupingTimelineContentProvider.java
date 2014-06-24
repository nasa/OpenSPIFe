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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.provider.EPlanElementLabelProvider;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public class ActivityDefCategoryGroupingTimelineContentProvider extends GroupingTimelineContentProvider {

	private static final String P_TIMELINE_SUBSYETM_ORDER = "timeline.subsystem.order";
	private static final String P_TIMELINE_SUBSYETM_IGNORE = "timeline.subsystem.ignore";
	private List<String> categories = null;
	private static final List<String> TIMELINE_ORDER = EnsembleProperties.getStringListPropertyValue(P_TIMELINE_SUBSYETM_ORDER, new ArrayList<String>());
	private static final Set<String> TIMELINE_IGNORE = new HashSet<String>(EnsembleProperties.getStringListPropertyValue(P_TIMELINE_SUBSYETM_IGNORE, new ArrayList<String>()));
	
	public ActivityDefCategoryGroupingTimelineContentProvider(EPlan plan) {
		super(plan);
	}
	
	@Override
	public String getName() {
		return "Subsystem";
	}

	@Override
	protected boolean isFixedValueWrapper(Object wrapper) {
		return true;
	}

	@Override
	protected Comparator<Object> getGroupingValuesComparator() {
		if (TIMELINE_ORDER == null || TIMELINE_ORDER.isEmpty()) {
			return null;
		}
		Comparator<Object> comparator = new Comparator<Object>() {
			@Override
			public int compare(Object o0, Object o1) {
				String s0 = (String) o0;
				String s1 = (String) o1;
				int i0 = TIMELINE_ORDER.indexOf(s0);
				if (i0 == -1) {
					i0 = TIMELINE_ORDER.size();
				}
				int i1 = TIMELINE_ORDER.indexOf(s1);
				if (i1 == -1) {
					i1 = TIMELINE_ORDER.size();
				}
				if (i0 == i1) {
					return s0.compareTo(s1);
				}
				return i0 - i1;
			}
		};
		return comparator;
	}

	@Override
	protected Collection getGroupingValues() {
		if (categories == null) {
			Set<String> set = new HashSet<String>();
			categories = new ArrayList<String>();
			for (EActivityDef activityDef : ActivityDictionary.getInstance().getActivityDefs()) {
				String category = activityDef.getCategory();
				if (!TIMELINE_IGNORE.contains(category) && !set.contains(category)) {
					set.add(category);
					categories.add(category);
				}
			}
		}
		Comparator<Object> comparator = getGroupingValuesComparator();
		if (comparator != null) {
			Collections.sort(categories, comparator);
		}
		return categories;
	}

	@Override
	protected List<? extends Object> getActivityValues(EActivity activity) {
		EActivityDef activityDef = ADParameterUtils.getActivityDef(activity);
		if (activityDef != null) {
			String category = activityDef.getCategory();
			if (!TIMELINE_IGNORE.contains(category)) {
				return Collections.singletonList(category);
			}
		}
		return Collections.emptyList();
	}
	
	@Override
	protected boolean isRelevant(EStructuralFeature f) {
		return false;
	}

	@Override
	protected Object getValueWrapper(Object value) {
		DynamicActivityGroup dag = new DynamicActivityGroup(plan, value) {
			@Override
			public Object getAdapter(Class adapter) {
				if (ILabelProvider.class == adapter) {
					return new EPlanElementLabelProvider(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE)) {

						@Override
						public Image getImage(Object element) {
							try {
								String string = (String) ((DynamicActivityGroup)element).getValue();
								return MissionUIConstants.getInstance().getIcon(string);
							} catch (Exception e) {
								// silence
							}
							return super.getImage(element);
						}
						
					};
				}
				return super.getAdapter(adapter);
			}
		};
		dag.setName(getValueString(value));
		return dag;
	}
	
}
