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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.PickListFieldEditor;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.swt.widgets.Composite;


public class AttributesPickListFieldEditor extends PickListFieldEditor {

	private static EPlanElement ePlanElement = PlanFactory.eINSTANCE.createEActivity();
	private static IItemPropertySource source = EMFUtils.adapt(ePlanElement, IItemPropertySource.class);

	public AttributesPickListFieldEditor(
			String name,
			String labelText,
			Composite parent,
			boolean ordered) {
		super(name, labelText, parent, getAttributes(), ordered);
	}

	private static List<String> getAttributes() {
		Set<String> attributes = new HashSet<String>();
		Map<EStructuralFeature, EStructuralFeature> containerFeatureMap = new HashMap<EStructuralFeature, EStructuralFeature>();
		Set<EStructuralFeature> eStructuralFeatures = getAllEStructuralFeatures(containerFeatureMap);
		for(EStructuralFeature eFeature : eStructuralFeatures) {
			IItemPropertyDescriptor itemPropertyDescriptor = source.getPropertyDescriptor(ePlanElement, eFeature);
			String displayName = null;
			if(itemPropertyDescriptor == null) {
				displayName = ParameterDescriptor.getInstance().getDisplayName(eFeature);
				EStructuralFeature containerFeature = containerFeatureMap.get(eFeature);
				if (containerFeature != null) {
					String containerFeatureDisplayName = ParameterDescriptor.getInstance().getDisplayName(containerFeature);
					displayName = containerFeatureDisplayName + ": " + displayName;
				}
				
			} else {
				displayName = itemPropertyDescriptor.getDisplayName(eFeature);
			}

			attributes.add(displayName);
		}
		
		List<String> attributesList = new ArrayList<String>(attributes);
		Collections.sort(attributesList);
		return attributesList;
	}
	
	private static Set<EStructuralFeature> getAllEStructuralFeatures(Map<EStructuralFeature, EStructuralFeature> containerFeatureMap) {
		HashSet<EStructuralFeature> eStructuralFeatures = new HashSet<EStructuralFeature>();
		
		List<EActivityDef> activityDefs = ActivityDictionary.getInstance().getActivityDefs();
		for(EActivityDef def : activityDefs) {
			for(EStructuralFeature feature : def.getEStructuralFeatures()) {
				if (ParameterDescriptor.getInstance().isVisible(feature)) {
					eStructuralFeatures.add(feature);
				}
			}
		}
		
		List<EParameterDef> attributeDefs = ActivityDictionary.getInstance().getAttributeDefs();
		for (EParameterDef def : attributeDefs) {
			if (def instanceof EAttribute) {
				if (ParameterDescriptor.getInstance().isVisible(def)) {
					eStructuralFeatures.add(def);
				}
			} else {
				// "AttributeDef" is really a reference
				if (StringifierRegistry.hasRegisteredStringifier(def.getName())) {
					if (ParameterDescriptor.getInstance().isVisible(def)) {
						eStructuralFeatures.add(def);
					}
				}
				EClassifier type = def.getEType();
				if (!def.isMany() && type instanceof ObjectDef) {
					for (EStructuralFeature feature : ((ObjectDef) type).getEStructuralFeatures()) {
						if (ParameterDescriptor.getInstance().isVisible(feature)) {
							eStructuralFeatures.add(feature);
							containerFeatureMap.put(feature, def);
						}
					}
				}
			}
		}
		
		// Does this ever add anything to end?  Always seems to return null for the sample ePlanElement.
		// If we add even the ones that are null, a long list is appended, but selecting them seems
		// to have no effect in Preferences > Tooltip, so it's probably right to exclude them.
		EList<EMember> eMembers = ePlanElement.getMembers();
		for(EMember eMember : eMembers) {
			for (EStructuralFeature feature : eMember.eClass().getEAllStructuralFeatures()) {
				if (source.getPropertyDescriptor(ePlanElement, feature) != null) {
					eStructuralFeatures.add(feature);
				}
			}
		}				
		return eStructuralFeatures;
	}
	
}
