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
package gov.nasa.ensemble.core.plan.editor.merge.decorator;


import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class MergeRowHighlightDecorator {

	private static final String ANNOTATION_KEY_RGB = "rgb";
	
	private boolean rowColorCodingVisible = true;
	private String colorCodingParameterName = null;
	private Map<Object, Color> backgroundColorMap = new HashMap<Object, Color>();
	private Map<Object, Color> foregroundColorMap = new HashMap<Object, Color>();
	
	public MergeRowHighlightDecorator(String annotationKey) {
		ActivityDictionary activityDictionary = ActivityDictionary.getInstance();
		String parameterName = EMFUtils.getDescriptorAnnotation(activityDictionary, annotationKey);
		updateColors(parameterName);
	}
	
	public void updateColors(String parameterName) {
		disploseColorMap(backgroundColorMap);
		disploseColorMap(foregroundColorMap);

		//repopulate map with colors
		colorCodingParameterName = parameterName;
		if (colorCodingParameterName == null) {
			rowColorCodingVisible = false;
		} else {
			List<?> choiceOfObjects = getChoiceOfObjects(colorCodingParameterName);
			for (Object object : choiceOfObjects) {
				if (object instanceof EModelElement) {
					String background = EMFUtils.getDescriptorAnnotation((EModelElement) object, ANNOTATION_KEY_RGB);
					if (background != null) {
						RGB rgb = ColorUtils.parseRGB(background);
						if (rgb != null) {
							backgroundColorMap.put(object, new Color(null, rgb));
						}
					}
					
					String foreground = EMFUtils.getDescriptorAnnotation((EModelElement) object, ParameterDescriptor.ANNOTATION_DETAIL_FOREGROUND_COLOR);
					if (foreground != null) {
						RGB rgb = ColorUtils.parseRGB(foreground);
						if (rgb != null) {
							foregroundColorMap.put(object, new Color(null, rgb));
						}
					}
				}
			}
		}
	}

	private static void disploseColorMap(Map<Object, Color> map) {
		//clear map and dispose colors
		for (Color color : map.values()) {
			color.dispose();
		}
		map.clear();
	}
	
	private List<?> getChoiceOfObjects(String parameterName) {
		if (parameterName != null) {
			EParameterDef def = ActivityDictionary.getInstance().getAttributeDef(parameterName);
			if (def != null) {
				EGenericType eGenericType = def.getEGenericType();
				EClassifier classifier = eGenericType.getEClassifier();
				if (classifier instanceof EEnumImpl) {
					EEnumImpl enumImpl = (EEnumImpl) classifier;
					return enumImpl.getELiterals();
				} 
				LogUtil.warn("Row Highlighting only supports Enum types. Not supported for " + parameterName + " (" +classifier+").");
			} else {
				LogUtil.error("Invalid parameter name for Row Highligther: "+ parameterName);
			}
		} 
		return Collections.EMPTY_LIST;
	}
	
	/**
	 * Is there a parameter in the AD that encodes the color defined?
	 * currently supported: 	MergeEditor -> table_row_color_highlight_parameter
	 * 							DaysEditor -> days_row_color_highlight_parameter
	 */
	public boolean isRowColorHighlightingEnabled() {
		return colorCodingParameterName != null;
	}
	
	/**
	 * Assuming there's a parameter defined that encodes the highlight, is it visible? 
	 * (allows toggle visibility)
	 */
	public boolean isRowColorHighlightingVisible() {
		return isRowColorHighlightingEnabled() && rowColorCodingVisible;
	}

	/**
	 * Toggle visibility of row color coding 
	 * @return current value of showRowColorCoding
	 */
	public boolean toggleRowHighlightVisibility() {
		rowColorCodingVisible = !rowColorCodingVisible;
		return rowColorCodingVisible;
	}
	
	public Color getBackgroundColor(Object element) {
		if (colorCodingParameterName == null) {
			return null;
		}
		if (element instanceof EPlanElement) {
			try {
				Object value = ADParameterUtils.getParameterObject((EPlanElement) element, colorCodingParameterName);
				return backgroundColorMap.get(value);
			} catch (UndefinedParameterException e) {
				//silent exception
			}
		}
		return null;
	}
	
	public Color getForegroundColor(Object element) {
		if (colorCodingParameterName == null) {
			return null;
		}
		if (element instanceof EPlanElement) {
			try {
				Object value = ADParameterUtils.getParameterObject((EPlanElement) element, colorCodingParameterName);
				return foregroundColorMap.get(value);
			} catch (UndefinedParameterException e) {
				//silent exception
			}
		}
		return null;
	}
	
	public void dispose() {
		updateColors(null);
	}

	
}
