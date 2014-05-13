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

import org.eclipse.swt.graphics.Color;

public interface ColorConstants {

	public static final Color brown      = new Color(null, 128, 64, 0);
	public static final Color magenta    = new Color(null, 128, 0,  64);
	public static final Color navy       = new Color(null, 0,   0,  128);
	public static final Color paleYellow = new Color(null, 255, 255, 215);
	public static final Color paleGreen  = new Color(null, 200, 255, 180);
	public static final Color paleRed    = new Color(null, 255, 100, 100);
	public static final Color paleBlue   = new Color(null, 200, 200, 255);
	public static final Color midGreen   = new Color(null, 50, 200, 50);
	public static final Color midBlue 	 = new Color(null, 50, 50, 200);
	public static final Color darkCyan 	 = new Color(null, 0, 100, 150);

	/*
	 * The following colors are from draw2d color constants,
	 * copied to avoid necessitating a dependency on draw2d/gef.
	 */
	
	/*
	 * Misc. colors
	 */
	/** One of the pre-defined colors */
	Color white = new Color(null, 255, 255, 255);
	/** One of the pre-defined colors */
	Color lightGray = new Color(null, 192, 192, 192);
	/** One of the pre-defined colors */
	Color gray = new Color(null, 128, 128, 128);
	/** One of the pre-defined colors */
	Color darkGray = new Color(null, 64, 64, 64);
	/** One of the pre-defined colors */
	Color black = new Color(null, 0, 0, 0);
	/** One of the pre-defined colors */
	Color red = new Color(null, 255, 0, 0);
	/** One of the pre-defined colors */
	Color orange = new Color(null, 255, 196, 0);
	/** One of the pre-defined colors */
	Color yellow = new Color(null, 255, 255, 0);
	/** One of the pre-defined colors */
	Color green = new Color(null, 0, 255, 0);
	/** One of the pre-defined colors */
	Color lightGreen = new Color(null, 96, 255, 96);
	/** One of the pre-defined colors */
	Color darkGreen = new Color(null, 0, 127, 0);
	/** One of the pre-defined colors */
	Color cyan = new Color(null, 0, 255, 255);
	/** One of the pre-defined colors */
	Color lightBlue = new Color(null, 127, 127, 255);
	/** One of the pre-defined colors */
	Color blue = new Color(null, 0, 0, 255);
	/** One of the pre-defined colors */
	Color darkBlue = new Color(null, 0, 0, 127);

	Color darkRed = new Color(null, 220, 0, 0);
}
