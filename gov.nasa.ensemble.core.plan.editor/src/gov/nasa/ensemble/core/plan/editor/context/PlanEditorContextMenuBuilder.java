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
package gov.nasa.ensemble.core.plan.editor.context;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.plan.editor.IPlanEditorConstants;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;

public class PlanEditorContextMenuBuilder {

	private static final List<String> ATTRIBUTES = EnsembleProperties.getStringListPropertyValue(IPlanEditorConstants.PLAN_EDITOR_CONTEXT_MENU_PROPERTY_KEY, Collections.<String> emptyList());
	
	public static void buildContextMenu(IMenuManager manager, IStructuredSelection selection) {
		if (ATTRIBUTES.isEmpty()) {
			return;
		}
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		for (Object object : selection.toList()) {
			if (object instanceof EPlanElement) {
				elements.add((EPlanElement) object);
			}
		}
		if (elements.isEmpty()) {
			return;
		}
		EPlanElement representative = elements.get(0);
		for (String attributeName : ATTRIBUTES) {
			EStructuralFeature feature = getFeature(elements, attributeName);
			if (feature != null) {
				String displayName = EMFUtils.getDisplayName(representative, feature);
				IItemPropertyDescriptor pd = getPropertyDescriptor(representative, feature);
				if (pd != null) {
					EClassifier eType = feature.getEType();
					if (eType == EcorePackage.Literals.EBOOLEAN || 
						eType == EcorePackage.Literals.EBOOLEAN_OBJECT) {
						manager.add(new BooleanAction(displayName, elements, feature));
					} else if (feature instanceof EReference || eType instanceof EEnum) {
						ImageDescriptor icon = PlanEditorContextMenuIconProvider.getInstance().get(feature);
						Collection<?> choiceOfValues = pd.getChoiceOfValues(representative);
						MenuManager attributeMenu = new MenuManager(displayName, icon, null);
						for (Object value : choiceOfValues) {
							attributeMenu.add(new ValueAction(displayName, elements, feature, value));
						}
						manager.add(attributeMenu);
					} else {
						LogUtil.warn(feature.getName() + " is not supported by plan editor context menu.");
					}
				}
			}
		}
		manager.add(new Separator());
	}
	
	private static IItemPropertyDescriptor getPropertyDescriptor(EObject object, EStructuralFeature feature) {
		IItemPropertySource source = EMFUtils.adapt(object, IItemPropertySource.class);
		if (source != null) {
			return source.getPropertyDescriptor(object, feature);
		}
		return null;
	}
	
	private static EStructuralFeature getFeature(List<EPlanElement> elements, String name) {
		EStructuralFeature commonFeature = null;
		for (EPlanElement element : elements) {
			try {
				EStructuralFeature feature = ADParameterUtils.getParameterFeature(element, name);
				if (commonFeature == null) {
					commonFeature = feature;
				} else if (commonFeature != feature) {
					return null;
				}
			} catch (UndefinedParameterException e) {
				return null;
			}
		}
		return commonFeature;
	}
	
	protected static Object getCurrentValue(List<EPlanElement> elements, EStructuralFeature feature) {
		Object value = null;
		try {
			for (EPlanElement element : elements) {
				Object object = ADParameterUtils.getParameterObject(element, feature.getName());
				if (value == null) {
					value = object;
				} else if (!CommonUtils.equals(value, object)) {
					return null;
				}
			}
			return value;
		} catch (UndefinedParameterException e) {
			return null;
		}
	}
	
}
