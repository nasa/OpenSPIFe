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
package gov.nasa.ensemble.dictionary.util;


import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.EChoice;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.Effect;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.edit.domain.EditingDomain;

public class DictionaryUtil {
	
	private static final Logger trace = Logger.getLogger(DictionaryUtil.class);
	
	private static final String ANNOTATION_SOURE_DESCRIPTOR = "descriptor";
	private static final String ANNOTATION_DETAIL_DESCRIPTOR_HIDDEN = "hidden";
	private static final String ANNOTATION_DETAIL_DESCRIPTOR_DISPLAY_NAME = "displayName";

	public static final String ANNOTATION_SOURCE_OVERRIDE = "override";
	
	public static boolean containsOnlyValues(List<EChoice> choices) {
		if (choices == null || choices.size() == 0) {
			return false;
		}
		
		for(EChoice choice : choices) {
			if (choice.getValue() == null) {
				return false;
			}
		}
		return true;
	}
	
	public static void setHidden(EModelElement element, boolean hidden) {
		EMFUtils.addAnnotation(element, ANNOTATION_SOURE_DESCRIPTOR, new String[] { ANNOTATION_DETAIL_DESCRIPTOR_HIDDEN, Boolean.toString(hidden) });
	}
	
	public static void setHidden(EModelElement element, String hidden) {
		EMFUtils.addAnnotation(element, ANNOTATION_SOURE_DESCRIPTOR, new String[] { ANNOTATION_DETAIL_DESCRIPTOR_HIDDEN, hidden });
	}
	
	public static boolean isHidden(EModelElement element) {
		String value = EMFUtils.getAnnotation(element, ANNOTATION_SOURE_DESCRIPTOR, ANNOTATION_DETAIL_DESCRIPTOR_HIDDEN);
		return value == null ? false : Boolean.parseBoolean(value);
	}
	
	public static void setDisplayName(EModelElement element, String displayName) {
		EMFUtils.addAnnotation(element, ANNOTATION_SOURE_DESCRIPTOR, new String[] { ANNOTATION_DETAIL_DESCRIPTOR_DISPLAY_NAME, displayName });
	}
	
	public static String getDisplayName(EModelElement element) {
		return EMFUtils.getAnnotation(element, ANNOTATION_SOURE_DESCRIPTOR, ANNOTATION_DETAIL_DESCRIPTOR_DISPLAY_NAME);
	}
	
	public static EActivityDictionary load(URI uri) {
		// Create the editing domain with a special command stack.
		//
		EditingDomain editingDomain = EMFUtils.createEditingDomain();
		ResourceSet resourceSet = editingDomain.getResourceSet();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl() {
			@Override
			public Resource createResource(URI uri) {
			    return new DictionaryResourceImpl(uri);
			}
		});
		
		Map<Object, Object> loadOptions = resourceSet.getLoadOptions();
		loadOptions.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
		loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
		loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
		loadOptions.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
		loadOptions.put(XMLResource.OPTION_URI_HANDLER, new URIHandlerImpl() {

			@Override
			public URI deresolve(URI uri) {
				if (!uri.isRelative()) {
					return uri;
				}
				return super.deresolve(uri);
			}

			@Override
			public URI resolve(URI uri) {
				if (!uri.isRelative()) {
					return uri;
				}
				return super.resolve(uri);
			}
			
		});
		Resource resource = resourceSet.createResource(uri);
		try {
			// Load the resource through the editing domain.
			//
			resource.load(null);
		}
		catch (Exception e) {
			trace.warn(e.getMessage());
			try {
				resource.load(null);
			} catch (Exception x) {
				trace.error(e.getMessage());
				return null;
			}
		}
		return EMFUtils.getLoadedContent(resource);
	}
	
	public static String getEffectString(Effect effect) {
		StringBuffer buffer = new StringBuffer();
		EResourceDef d = effect.getDefinition();
		if (d != null) {
			EClass eClass = d.getEContainingClass();
			if (eClass != null){
				buffer.append(eClass.getName()+"_");
			}
			buffer.append(d.getName());
		}
		return buffer.toString();
	}

	public static <T> T getDefaultValue(EClass eClass, EStructuralFeature feature) {
		if (feature instanceof EAttribute) {
			EAttribute attribute = (EAttribute)feature;
			String valueLiteral = EMFUtils.getAnnotation(eClass, ANNOTATION_SOURCE_OVERRIDE, feature.getName());
			if (valueLiteral != null) {
				EDataType eDataType = attribute.getEAttributeType();
				return (T)EcoreUtil.createFromString(eDataType, valueLiteral);
			}
		}
		return (T)feature.getDefaultValue();
	}
}
