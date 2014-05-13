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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ParameterDef;
import gov.nasa.ensemble.core.model.plan.translator.ActivityDefUtils;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.parameters.EMFParameterSerializer;
import gov.nasa.ensemble.core.plan.parameters.IParameterSerializer;
import gov.nasa.ensemble.dictionary.EParameterDef;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ParameterSerializerRegistry {

	private static final Logger trace = Logger.getLogger(ParameterSerializerRegistry.class);
	
	private static final String EXTENSION_POINT_ID = "gov.nasa.ensemble.core.plan.IParameterSerializer";

	private static ParameterSerializerRegistry registry;
	
	private final Map<String, IParameterSerializer> typeSerializerMap = new HashMap<String, IParameterSerializer>();
	
	/**
	 * Private constructor.
	 */
	private ParameterSerializerRegistry() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
		for (IExtension extension : point.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				String type = configurationElement.getAttribute("type");
				if (type == null) {
					trace.error("type not specified for parameter serializer, skipped");
					continue;
				}
				try {
					IParameterSerializer serializer = (IParameterSerializer)configurationElement.createExecutableExtension("class");
					typeSerializerMap.put(type.toLowerCase(), serializer);
					trace.debug("Registering type '"+type+"' serialized by '"+serializer.getClass()+"'");
				} catch (ClassCastException e) {
					String classAttribute = configurationElement.getAttribute("class");
					trace.error("class '" + classAttribute + "' must implement IParameterSerializer\n" + e);
				} catch (CoreException e) {
					String classAttribute = configurationElement.getAttribute("class");
					trace.error("could not create executable extension for class '" + classAttribute + "'\n" + e);
				}
			}
		}
	}
	
	private static ParameterSerializerRegistry getInstance() {
		if (registry == null) {
			synchronized (ParameterSerializerRegistry.class) {
				if (registry == null) {
					registry = new ParameterSerializerRegistry();
				}
			}
		}
		return registry;
	}
	
	/**
	 * @param parameter
	 * @return a serializer for the type of parameter
	 */
	/* package */ static IParameterSerializer getSerializer(EStructuralFeature structuralFeature) {
		EClassifier eType = structuralFeature.getEType();
		if (eType instanceof EDataType) {
			return getSerializer((EDataType)eType);
		}
		EParameterDef parameterDef = null;
		if (structuralFeature instanceof ParameterDef) {
			parameterDef = (ParameterDef) structuralFeature;
		} else {
			String parameterName = WrapperUtils.mapStructuralFeatureToParameterName(structuralFeature);
			parameterDef = ActivityDictionary.getInstance().getAttributeDef(parameterName);
		}
		String hType;
		if (parameterDef instanceof ParameterDef) {
			hType = ((ParameterDef)parameterDef).getType();
		} else {
			hType = ActivityDefUtils.getInstance().getType(eType);
		}
		return getSerializer(hType);
	}
	
	public static IParameterSerializer getSerializer(String hType) {
		if (hType == null)
			return null;
		String lowerCaseType = hType.toLowerCase();
		IParameterSerializer serializer = getInstance().typeSerializerMap.get(lowerCaseType);
		if (serializer == null) {
			EDataType dataType = ActivityDefUtils.getInstance().getDataType(hType);
			serializer = getSerializer(dataType);
			getInstance().typeSerializerMap.put(lowerCaseType, serializer);
		}
		return serializer;
	}
	
	public static IParameterSerializer getSerializer(EDataType eDataType) {
		return eDataType == null ? null : new EMFParameterSerializer(eDataType);
	}
	
}
