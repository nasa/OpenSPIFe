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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Due to the limited nature of color resources on some native
 * platforms, the {@link Color} object cannot be instantiated
 * ad-infinitum.
 * 
 * All colors are referenced via their {@link RGB} value and
 * a string reference.
 */
public class ColorCache {
	
	private static final Logger trace = Logger.getLogger(ColorCache.class);
	
	private Map<RGB, Color> rgbToColorCache = new HashMap<RGB, Color>();
	private Map<String, RGB> referenceToRGBCache = new HashMap<String, RGB>();
	private Map<RGB, Set<String>> rgbReferencesCache = new HashMap<RGB, Set<String>>();
	
	/**
	 * Return the cached color given the reference.
	 * @param reference of the object associated with the color
	 * @return a color if cache, null if none has been found
	 */
	public Color findColor(String reference) {
		Color color = null;
		RGB rgb = referenceToRGBCache.get(reference);
		if (rgb != null) {
			color = rgbToColorCache.get(rgb);
		}
		return color;
	}
	
	/**
	 * Allows for Colors to be reused without multiple allocations.
	 * Any color should be referenced via the RGB descriptor, as well
	 * as a unique string utilized by the referencing object. The
	 * returned color should never be null.
	 * 
	 * @param rgb color descriptor to be retrieved
	 * @param reference to use as the RGB reference
	 * @return Color found or allocated via the method. Should never be null.
	 */
	public Color addReference(RGB rgb, String reference) {
		removeReference(reference);
		Color color  = rgbToColorCache.get(rgb);
		if (color == null) {
			trace.debug("Allocating color: "+rgb);
			color = new Color(null, rgb);
			rgbToColorCache.put(rgb, color);
		}
		referenceToRGBCache.put(reference, rgb);
		
		Set<String> references = rgbReferencesCache.get(rgb);
		if (references == null) {
			references = new HashSet<String>();
			rgbReferencesCache.put(rgb, references);
		}
		
		if (!references.contains(reference)) {
			references.add(reference);
		}
		
		return color;
	}
	
	/**
	 * Remove the reference from the RGB cache, and if no
	 * more references for that RGB exist, dispose of the 
	 * color.  
	 * @param reference
	 */
	public void removeReference(String reference) {
		RGB rgb = referenceToRGBCache.get(reference);
		if (rgb != null) {
			removeReference(rgb, reference);
		}
	}

	private void removeReference(RGB rgb, String reference) {
		Set<String> references = rgbReferencesCache.get(rgb);
		if (references != null) {
			references.remove(reference);
		}
		
		if (references == null || references.size() == 0) {
			Color color = rgbToColorCache.remove(rgb);
			if (color != null) {
				trace.debug("Disposing color: "+rgb);
				color.dispose();
			}
		}
	}

}
