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

import gov.nasa.ensemble.common.collections.AutoCollectionMap;
import gov.nasa.ensemble.common.collections.AutoSetMap;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ObjectDef;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

public class MentionedResourceObjectsVisitor extends PlanVisitor {
	
	private final Map<ObjectDef, Set<ENumericResourceDef>> objectResources = new AutoSetMap<ObjectDef, ENumericResourceDef>(ObjectDef.class);
	private final Map<ENumericResourceDef, Set<EObject>> resourceObjects = new AutoCollectionMap<ENumericResourceDef, Set<EObject>>(ENumericResourceDef.class) {
		@Override
		protected Set<EObject> createCollection() {
			return new TreeSet<EObject>(new Comparator<EObject>() {
				@Override
				public int compare(EObject o1, EObject o2) {
					String id1 = ProfileUtil.getObjectId(o1);
					String id2 = ProfileUtil.getObjectId(o2);
					return String.CASE_INSENSITIVE_ORDER.compare(id1, id2);
				}
			});
		}
	};

	public MentionedResourceObjectsVisitor() {
		ActivityDictionary AD = ActivityDictionary.getInstance();
		for (ObjectDef objectDef : AD.getDefinitions(ObjectDef.class)) {
			for (EStructuralFeature feature : objectDef.getEStructuralFeatures()) {
				if (feature instanceof ENumericResourceDef) {
					ENumericResourceDef resourceDef = (ENumericResourceDef) feature;
					if (!resourceDef.getName().equals("time")) {
						continue;
					}
					objectResources.get(objectDef).add(resourceDef);
				}
			}
		}
	}
	
	public Map<ENumericResourceDef, Set<EObject>> getResourceObjects() {
		return resourceObjects;
	}

	@Override
	protected void visit(EPlanElement element) {
		if (element instanceof EActivity) {
			EActivity activity = (EActivity) element;
			EObject data = activity.getData();
			if (data != null) {
				EClass klass = data.eClass();
				EList<EReference> references = klass.getEReferences();
				for (EReference reference : references) {
					EClassifier type = reference.getEType();
					if (type instanceof ObjectDef) {
						ObjectDef objectDef = (ObjectDef) type;
						addResourceObjects(data, reference, objectDef);
					}
				}
			}
		}
	}

	protected void addResourceObjects(EObject data, EReference reference, ObjectDef objectDef) {
		Set<ENumericResourceDef> resources = objectResources.get(objectDef);
		Object value = data.eGet(reference);
		if (value instanceof EObject) {
			value = Collections.singletonList(value);
		}
		if (value instanceof Collection) {
			Collection<EObject> values = (Collection<EObject>) value;
			for (ENumericResourceDef resourceDef : resources) {
				resourceObjects.get(resourceDef).addAll(values);
			}
		}
	}
}
