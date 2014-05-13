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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Color map that varies the hue, keeping saturation and brightness constant.
 * Use this to assign a usually-unique color code to each object.
 * 
 * Any number of colors will be allocated as needed,
 * starting with distinguishable colors and then filling in the gaps.
 * Specific colors that are used for special purposes can be reserved.
 * 
 * @param <T> the type of object being color-coded.
 */
public class ColorMap<T> {
	
	public static ColorMap<RGB> RGB_INSTANCE = new ColorMap<RGB>() {

		@Override
		protected Color createNewColor(RGB key) {
			return new Color(null, key);
		}
		
	};
	
	protected final float saturation;
	protected final float brightness;

	protected Map<T, Color> innerMap = new HashMap<T, Color>();
	protected SortedSet<Float> sortedHues = new TreeSet<Float>();
	
	/**
	 * Construct a color map.  Any number of colors will be allocated as needed,
	 * starting with distinguishable colors and then filling in the gaps.
	 */
	public ColorMap() {
		this(.9f, .9f);
	}
	
	public ColorMap(float desiredSaturation, float desiredBrightness) {
		super();
		saturation = desiredSaturation;
		brightness = desiredBrightness;
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
	public Color getColor(T key) {
		Color color = innerMap.get(key);
		if (color == null || color.isDisposed()) {
			color = createNewColor(key);
			assignColor(key, color);
		}

		return color;
	}
	
	/**
	 * Get the Color from the ColorMap, the one associated with this key.
	 * @param key the thing which is associated with the color.
	 * @return the Color corresponding to the given key
	 */	
	public Color getCachedColor(T key) {
		return innerMap.get(key);		
	}
	
	/**
	 * Assign a desired color to the given key.
	 * Usually this is randomly assigned:
	 * Use getColor to 
	 * 
	 * @param key the key to lookup the Color
	 * @param color the Color to associated with the given key
	 * @see getColor -- to get the color assigned, or to assiign an arbitrary color.
	*/
	public void assignColor(T key, Color color) {
		innerMap.put(key, color);
		sortedHues.add(ColorUtils.getHSB(color.getRGB())[0]);
	}
	
	/**
	 * Remove a Color from the ColorMap.  Calls dispose to deallocate the O/S Color resource.
	 * 
	 * @param key the key to lookup the Color to remove and dispose.
	 */
	public void removeColor(T key) {
		Color color = innerMap.get(key);
		if (color != null) {
			color.dispose();
		}
		innerMap.remove(key);
	}

	/**
	 * Dispose all the Colors in the ColorMap.
	 */
	public void dispose() {
		for (Color c : innerMap.values()) {
			c.dispose();
		}
		innerMap.clear();
	}

	protected Color createNewColor(T key) {
		return createNewColor (findUnusedHue(), saturation, brightness);
	}
	
	/**
	 * Find a hue that is as far removed from the existing ones as possible.
	 * @return
	 */
	private float findUnusedHue () {
		float best_so_far = 0;
		float best_distance = 0;
		float redder_neighbor = 0;
		SortedSet<Float> reseved = getReservedHues();
		for (float bluer_neighbor: reseved) {
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
	
	private SortedSet<Float> getReservedHues() {
		SortedSet<Float> reseved = new TreeSet<Float>();
		reseved.addAll(sortedHues);
		return reseved;
	}
	
	private Color createNewColor(float hue, float saturation, float brightness) {
		return new Color(null, ColorUtils.getRGB(hue, saturation, brightness));
	}

}
