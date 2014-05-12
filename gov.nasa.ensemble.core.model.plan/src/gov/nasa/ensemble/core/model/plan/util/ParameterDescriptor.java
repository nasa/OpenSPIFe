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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ParameterDescriptor {

	public static final String ANNOTATION_SOURCE = "descriptor";
	public static final String ANNOTATION_DETAIL_CATEGORY = "category";
	public static final String ANNOTATION_DETAIL_EDITABLE = "editable";
	public static final String ANNOTATION_DETAIL_VISIBLE = "visible";
	@Deprecated // USE ANNOTATION_DETAIL_VISIBLE, not ANNOTATION_DETAIL_HIDDEN
	public static final String ANNOTATION_DETAIL_HIDDEN = "hidden";
	public static final String ANNOTATION_DETAIL_DISPLAY_NAME = "display_name";
	public static final String ANNOTATION_DETAIL_MULTILINE = "multiline";
	public static final String ANNOTATION_DETAIL_FILTER_FLAGS = "filterFlags";
	public static final String ANNOTATION_DETAIL_COLOR = "rgb";
	public static final String ANNOTATION_DETAIL_FOREGROUND_COLOR = "foreground";
	
	public static final boolean DESCRIPTOR_EDITABLE_DEFAULT = true;
	public static final boolean DESCRIPTOR_VISIBLE_DEFAULT = true;
	public static final boolean DESCRIPTOR_MULTILINE_DEFAULT = false;
	
	private static ParameterDescriptor instance;
	public static ParameterDescriptor getInstance() {
		if (instance == null) {
			instance = new ParameterDescriptor();
		}
		return instance;
	}
	
	public String getDisplayName(EPlanElement planElement, String featureName) {
		EObject data = planElement.getData();
		if (data != null) {
			EStructuralFeature feature = data.eClass().getEStructuralFeature(featureName);
			if (feature != null) {
				return ParameterDescriptor.getInstance().getDisplayName(feature); 
			}
		}
		return featureName;
	}
	
	public String getDisplayName(EStructuralFeature feature) {
		String displayName = getAnnotation(feature, ANNOTATION_DETAIL_DISPLAY_NAME);
		if (displayName == null) {
			displayName = feature.getName();
		}
		return displayName;
	}
	
	public boolean isEditable(EStructuralFeature feature) {
		return getBooleanAnnotation(feature, ANNOTATION_DETAIL_EDITABLE, DESCRIPTOR_EDITABLE_DEFAULT);
	}
	
	public boolean isVisible(EStructuralFeature feature) {
		return getBooleanAnnotation(feature, ANNOTATION_DETAIL_VISIBLE, DESCRIPTOR_VISIBLE_DEFAULT);
	}

	public boolean isMultiline(EStructuralFeature feature) {
		return getBooleanAnnotation(feature, ANNOTATION_DETAIL_MULTILINE, DESCRIPTOR_MULTILINE_DEFAULT);
	}
	
	public String getCategory(EStructuralFeature feature) {
		return getAnnotation(feature, ANNOTATION_DETAIL_CATEGORY);
	}
	
	public String[] getFilterFlags(EStructuralFeature feature) {
		String annotation = getAnnotation(feature, ANNOTATION_DETAIL_FILTER_FLAGS);
		if (annotation == null) {
			return new String[0];
		}
		try {
			return CommonUtils.parseAsArray(annotation);
		} catch (Exception e) {
			LogUtil.errorOnce("cannot decode filterFlag string on "+feature.getName()+": "+annotation);
		}
		return new String[0];
	}
	
	/**
	 * Inspects the annotations on the ParameterDef and returns the value
	 * @param feature to inspect
	 * @param key to retrieve
	 * @return the value of the annotation given the ParameterDescriptor source
	 */
	public String getAnnotation(EStructuralFeature feature, String key) {
		if (feature == null) {
			return null;
		} // else...
		return EMFUtils.getAnnotation(feature, ANNOTATION_SOURCE, key);
	}
	
	/**
	 * Assumes a boolean annotation
	 * @param parameterDef to inspect
	 * @param key to retrieve
	 * @param defaultValue to return if none exists
	 * @return the parsed boolean value of the annotation
	 */
	private Boolean getBooleanAnnotation(EStructuralFeature feature, String key, boolean defaultValue) {
		String annotation = getAnnotation(feature, key);
		if (annotation == null || annotation.length() == 0) {
			return defaultValue;
		}
		return Boolean.parseBoolean(annotation);
	}
	
}
