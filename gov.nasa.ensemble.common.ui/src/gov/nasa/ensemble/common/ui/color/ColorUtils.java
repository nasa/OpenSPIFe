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
package gov.nasa.ensemble.common.ui.color;

import gov.nasa.ensemble.common.ERGB;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorUtils {

	private static Random random = new Random();
	private static final ColorCache colorCache = new ColorCache();
	
	/**
	 * Converts the non-ui RGB to the ui RGB
	 * @param eRGB
	 * @return
	 */
	public static RGB getAsRGB(ERGB eRGB) {
		if (eRGB == null) {
			return null;
		}
		return new RGB(eRGB.red, eRGB.green, eRGB.blue);
	}
	
	/**
	 * 
	 * @param rgb
	 * @return
	 */
	public static ERGB getAsERGB(RGB rgb) {
		if (rgb == null) {
			return null;
		}
		return new ERGB(rgb.red, rgb.green, rgb.blue);
	}
	
	/**
	 * Convert to RGB values from given hue, saturation and brightness
	 * @param h the hue value
	 * @param s the saturation value
	 * @param b the brightness value
	 * @return an RGB containing red, green, blue equivalent of the given hue, saturation, brightness
	 */
	public static RGB getRGB(float h, float s, float b) {
		java.awt.Color color = new java.awt.Color(java.awt.Color.HSBtoRGB(h, s, b));
		return new RGB(color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * Convert to hue, saturation, brightness values from given RGB
	 * @param rgb red, green, blue values -- each 0..255.
	 * @return float array length 3, values of hue, saturation, and brightness (in that order) -- each 0..1.
	 */
	public static float[] getHSB(RGB rgb) {
		return java.awt.Color.RGBtoHSB(rgb.red, rgb.green, rgb.blue, null);
	}
	
	/**
	 * Converts a String to RGB, could be of several formats
	 * * @see {@link StringUtils}
	 */
	public static RGB parseRGB(String valueString) {
		try {
			if (valueString != null && valueString.startsWith("#")) {
				valueString = valueString.substring(1);
			}
			Integer hexcode = Integer.parseInt(valueString, 16);
			if (hexcode != null) {
				return new RGB((hexcode/256/256)%256, (hexcode/256)%256, hexcode%256);
			}
		} catch (NumberFormatException e) {
			try {
				return StringConverter.asRGB(valueString);
			} catch (Exception x) {
				// ignore it
			}
		}
		return null;
	}

	/**
	 * Converts rgb to hex
	 * @param rgb to convert
	 * @return hex string representation
	 */
	public static String formatRGB(RGB rgb) {
		int intRGB = rgb.red;
		intRGB = intRGB * 256 + rgb.green;
		intRGB = intRGB * 256 + rgb.blue;
		String hexString = Integer.toHexString(intRGB);
		
		// Ensure this is 6 digits
		return "000000".substring(hexString.length()) + hexString;
	}
	
	public static Color addReference(RGB rgb, String reference) {
		return colorCache.addReference(rgb, reference);
	}

	public static Color findColor(String reference) {
		return colorCache.findColor(reference);
	}

	public static void removeReference(String reference) {
		colorCache.removeReference(reference);
	}
	
	public static RGB getRandomColor() {
		return new RGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	public static String colorToString(Color color) {
		if (color == null)
			return "";
		return formatRGB(color.getRGB());
	}
	
	public static Color stringToColor(String colorString) {
		if (colorString == null || colorString.equals(""))
			return null;
		Color color = ColorUtils.findColor(colorString);
		if (color == null)
			color = ColorUtils.addReference(ColorUtils.parseRGB(colorString), colorString);
		return color;
	}
	
	public static int getBrightness(RGB rgb) {
		return ((rgb.red * 299) + (rgb.green * 587) + (rgb.blue * 114)) / 1000;
	}
	
	public static boolean isBrightColor(RGB rgb) {
		return getBrightness(rgb) > 119;	
	}
	
	public static RGB brighterColor(RGB rgb) {
		return brighterColor(rgb, 0.7);
	}
	
	public static RGB brighterColor(RGB rgb, double factor) {
		return new RGB(
				(int)Math.max(3, Math.max(rgb.red, Math.min(Math.round(rgb.red / factor), 255))),
				(int)Math.max(3, Math.max(rgb.green, Math.min(Math.round(rgb.green / factor), 255))),
				(int)Math.max(3, Math.max(rgb.blue, Math.min(Math.round(rgb.blue / factor), 255)))
		);
	}
	
	public static RGB darkerColor(RGB rgb) {
		return darkerColor(rgb, 0.9);
	}
	
	public static RGB darkerColor(RGB rgb, double factor) {
		return new RGB(
				(int)Math.max(Math.round(rgb.red * factor), 0),
				(int)Math.max(Math.round(rgb.green * factor), 0),
				(int)Math.max(Math.round(rgb.blue * factor), 0)
		);
	}
	
	public static RGB inverseColor(RGB rgb) {
		return new RGB(
			255 - rgb.red,	
			255 - rgb.green,	
			255 - rgb.blue	
		);
	}
}
