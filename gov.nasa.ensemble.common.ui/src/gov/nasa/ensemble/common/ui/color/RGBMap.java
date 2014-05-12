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


import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.swt.graphics.RGB;

/**
 * Color map that varies the hue, keeping saturation and brightness constant.
 * Use this to assign a usually-unique color code to each object.
 * 
 * <img src="https://jplis-ahs-003.jpl.nasa.gov:5843/confluence/download/attachments/3769867/RGBMap.png">
 * 
 * Any number of colors will be allocated as needed,
 * starting with distinguishable colors and then filling in the gaps.
 * Specific colors that are used for special purposes can be reserved.
 * 
 * @param <T> the type of object being color-coded.
 */
public class RGBMap<T> {

	protected final float saturation = .9f;
	protected final float brightness = .9f;

	protected Map<T, RGB> innerMap = new HashMap<T, RGB>();
	protected SortedSet<Float> sortedHues = new TreeSet<Float>();
	
	/**
	 * Construct a color map.  Any number of colors will be allocated as needed,
	 * starting with distinguishable colors and then filling in the gaps.
	 */
	
	public RGBMap() {
		super();
		sortedHues.add(1.0f); // to deal with circularity of color wheel (1.0 = 0.0 = red), stay away from 1.0
	}
	
	/**
	 * Get the Color from the ColorMap, the one associated with this key.
	 * This allocates an arbitrary new color to each key the first time,
	 * and thereafter returns the color previously associated with the key.
	 * @param key the thing being color-coded
	 * @return the Color corresponding to the given key
	 * @see assignColor -- when the caller wants to pick a particular color.
	 */
	public RGB getRGB(T key) {
		RGB rgb = innerMap.get(key);
		if (rgb == null) {
			rgb = findUnusedRGB();
			assignRGB(key, rgb);
		}
		return rgb;
	}
	
	/**
	 * Gets the currently cached color value, null if none exist
	 * @param key to check
	 * @return rgb
	 */
	public RGB getCachedRGB(T key) {
		return innerMap.get(key);
	}
	
	/**
	 * Assign a desired color to the given key. Usually this is randomly assigned: Use getColor to
	 * 
	 * @param key
	 *            the key to lookup the Color
	 * @param rgb
	 *            the Color to associated with the given key
	 * @see getColor -- to get the color assigned, or to assiign an arbitrary color.
	 */
	public void assignRGB(T key, RGB rgb) {
		innerMap.put(key, rgb);
		sortedHues.add(ColorUtils.getHSB(rgb)[0]);
	}

	/**
	 * Dispose all the Colors in the ColorMap.
	 */
	public void dispose() {
		innerMap.clear();
	}

	public void reserveRGB(RGB rgb) {
		sortedHues.add(ColorUtils.getHSB(rgb)[0]);
	}
	
	private RGB findUnusedRGB() {
		return ColorUtils.getRGB(findUnusedHue(), saturation, brightness);
	}
	
	/**
	 * Find a hue that is as far removed from the existing ones as possible.
	 * @return
	 */
	private float findUnusedHue () {
		float best_so_far = 0;
		float best_distance = 0;
		float redder_neighbor = 0;
		for (float bluer_neighbor: sortedHues) {
			float available_space = bluer_neighbor - redder_neighbor;
			float distance = available_space/2;
			float halfway_point = redder_neighbor + distance;
			if (distance > best_distance) {
				best_so_far = halfway_point;
				best_distance = distance;
			}
			redder_neighbor = bluer_neighbor;
		}
		return best_so_far;
	}
	
}
