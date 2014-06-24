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
package gov.nasa.arc.spife.ui.timeline.chart.util;

import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.common.ui.color.RGBMap;
import gov.nasa.ensemble.core.jscience.PowerValue;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.jface.resource.DataFormatException;
import org.eclipse.swt.graphics.RGB;
import org.jscience.physics.amount.Amount;

public class PlotRgbRegistry implements MissionExtendable {

	public static final String RGB = "rgb";
	public static final String DESCRIPTOR = "descriptor";
	public static final String P_SHOW_ZERO_COLOR = "timeline.heatmap.color.zero";
	private static final boolean SHOW_ZERO_COLOR = Boolean.parseBoolean(System.getProperty(P_SHOW_ZERO_COLOR, "false"));

	private Map<String, RGBMap<String>> rgbMapByPlotName = new HashMap<String, RGBMap<String>>();
	
	public static final PlotRgbRegistry INSTANCE = createInstance();

	private static PlotRgbRegistry createInstance() {
		try {
			return MissionExtender.construct(PlotRgbRegistry.class);
		} catch (ConstructionException e) {
			LogUtil.error(e);
		}
		return new PlotRgbRegistry();
	}
	
	public static final RGB NULL = new RGB(0,0,0);
	
	public RGB getRgb(Plot plot, Object value) {
		RGB rgb = null;
		if (value instanceof EEnumLiteral) {
			rgb = getRgb(plot, (EEnumLiteral) value);
		} else if (value instanceof String) {
			rgb = getRgb(plot, (String) value);
		} else if (value instanceof Boolean) {
			rgb = getRgb(plot, (Boolean)value);
		} else if (value instanceof Amount) {
			rgb = getRgb(plot, (Amount)value);
		} else if (value instanceof Number) {
			rgb = getRgb(plot, (Number)value);
		} else if (value instanceof PowerValue) {
			return getRgb(plot, ((PowerValue)value).getStateValue());
		}
		return rgb == NULL ? null : rgb;
	}

	public void clearPlotCache(Plot model) {
		rgbMapByPlotName.remove(model.getName());
	}
	
	private RGB getRgb(Plot plot, Amount amount) {
		RGB rgb;
		if (plot.isAutoAssignRGB()) {
			final RGBMap<String> rgbMap = getRgbMap(plot);
			final String valStr = amount.toString();
			rgb = rgbMap.getCachedRGB(valStr);
			if (rgb == null) {
				rgb = getColorForEntirePlot(plot);
			}
			if (rgb == null) {
				rgb = rgbMap.getRGB(valStr);
			}
		} else {
			rgb = plot.getRgb();
		}
		return rgb;
	}

	/** <profile name>.color=<hexadecimal value> in ensemble.properties */
	public RGB getColorForEntirePlot(Plot plot) {
		return getColorForValue(plot, "color");
	}
	
	private RGB getRgb(Plot plot, Number number) {
		RGB rgb;
		if (plot.isAutoAssignRGB()) {
			final RGBMap<String> rgbMap = getRgbMap(plot);
			final String valStr = number.toString();
			rgb = rgbMap.getCachedRGB(valStr);
			if (rgb == null) {
				rgb = getColorForEntirePlot(plot);
			}
			if (rgb == null) {
				rgb = rgbMap.getRGB(valStr);
			}
		} else {
			rgb = plot.getRgb();
		}
		return rgb;
	}
	
	private RGB getRgb(Plot plot, String key) {
		RGBMap<String> rgbMap = getRgbMap(plot);
		RGB rgb = rgbMap.getCachedRGB(key);
		if (rgb == null) {
			rgb = getColorForValue(plot, key);
		}
		if (rgb == null) {
			if (!plot.isAutoAssignRGB()) {
				rgb = plot.getRgb();
				rgbMap.assignRGB(key, rgb);
			} else {
				if (rgb == null) {
					rgb = rgbMap.getRGB(key);
				}
				rgbMap.assignRGB(key, rgb);
			}
		}
		return rgb;
	}
	
	private RGB getRgb(Plot plot, Boolean value) {
		RGBMap<String> rgbMap = getRgbMap(plot);
		String key = value.toString(); // "true" or "false", e.g. "Periapsis.true" property
		RGB rgb = rgbMap.getCachedRGB(key);
		if (rgb == null) {
			if (!plot.isAutoAssignRGB()) {
				rgb = getColorForValue(plot, key);
				if (rgb == null) {
					if (Boolean.TRUE == value) {
						rgb = plot.getRgb();
					} else {
						rgb = NULL;
					}
				}
				rgbMap.assignRGB(key, rgb);
			} else {
				rgb = rgbMap.getRGB(value.toString());
			}
		}
		return rgb;
	}

	private RGB getRgb(Plot plot, EEnumLiteral literal) {
		RGBMap<String> rgbMap = getRgbMap(plot);
		String key = literal.getName();
		RGB rgb = rgbMap.getCachedRGB(key);
		boolean showZeroColor = SHOW_ZERO_COLOR;
		if (rgb == null) {
			rgb = parseAnnotation(literal);
			if (rgb == null) {
				rgb = getColorForValue(plot, key);
				if (rgb != null) showZeroColor = true; // if an explicitly defined color is configured for the first value, use it
			}
			if (rgb == null) {
				int value = literal.getValue();
				EEnum enums = literal.getEEnum();
				EList<EEnumLiteral> literals = enums.getELiterals();
				// try this other way, maybe the value wasn't defined
				if (value == 0) {
					value = literals.indexOf(literal);
				}
				if (value == 0 && !showZeroColor) {
					rgb = NULL;
				} else if (!plot.isAutoAssignRGB()) {
					rgb = plot.getRgb();
				} else {
					float[] hsb = plot.getRgb().getHSB();
					float f = value / (float) (literals.size() - 1);
					float h = hsb[0];
					float s = hsb[1] * f;
					float b = hsb[2];
					rgb = new RGB(h, s, b);
				}
				rgbMap.assignRGB(key, rgb);
			} else {
				rgbMap.assignRGB(key, rgb);
			}
		}
		return rgb;
	}

	private RGBMap<String> getRgbMap(Plot plot) {
		RGBMap<String> rgbMap = rgbMapByPlotName.get(plot.getName());
		if (rgbMap == null) {
			rgbMap = new RGBMap<String>();
			rgbMapByPlotName.put(plot.getName(), rgbMap);
		}
		return rgbMap;
	}
	
	private RGB getColorForValue(Plot plot, String value) {
		// See if the profile's metadata defines <value>.color
		String profileDefined = plot.getProfile().getAttributes().get(value + ".color");
		if (profileDefined != null) {
			return parseRgb(profileDefined);
		}
		// Otherwise, look in ensemble.properties for <plotname>.<value>
		return parseProperty(plot.getName() + "." + value);
	}
	
	private RGB parseProperty(String key) {
		return parseRgb(EnsembleProperties.getProperty(key));
	}
	
	private RGB parseAnnotation(EEnumLiteral literal) {
		return parseRgb(EMFUtils.getAnnotation(literal, DESCRIPTOR, RGB));
	}
	
	private RGB parseRgb(String rgbString) {
		RGB rgb = null;
		if (rgbString != null) {
			if (rgbString.equalsIgnoreCase("null")) {
				rgb = NULL;
			} else {
				try {
					rgb = ColorUtils.parseRGB(rgbString);
				} catch (DataFormatException x) {
					LogUtil.error(x);
				}
			}
		}
		return rgb;
	}
	
}
