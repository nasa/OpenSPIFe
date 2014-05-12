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
package gov.nasa.ensemble.common;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;

import org.apache.commons.lang.StringUtils;

public class ERGBUtils {

	private static Random random = new Random();

	public static ERGB getRandomColor() {
		return new ERGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
	
	/**
	 * Converts a String to RGB, could be of several formats
	 * * @see {@link StringUtils}
	 */
	public static ERGB parseERGB(String valueString) {
		try {
			if (valueString != null && valueString.startsWith("#")) {
				valueString = valueString.substring(1);
			}
			Integer hexcode = Integer.parseInt(valueString, 16);
			if (hexcode != null) {
				return new ERGB((hexcode/256/256)%256, (hexcode/256)%256, hexcode%256);
			}
		} catch (NumberFormatException e) {
			try {
				return asRGB(valueString);
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
	public static String formatERGB(ERGB rgb) {
		int intRGB = rgb.red;
		intRGB = intRGB * 256 + rgb.green;
		intRGB = intRGB * 256 + rgb.blue;
		String hexString = Integer.toHexString(intRGB);
		
		// Ensure this is 6 digits
		return "000000".substring(hexString.length()) + hexString;
	}

    /**
     * COPIED FROM org.eclipse.jface.resource.StringConverter
     * 
     * Converts the given value into an SWT RGB color value.
     * This method fails if the value does not represent an RGB
     * color value.
     * <p>
     * A valid RGB color value representation is a string of the form
     * <code><it>red</it>,<it>green</it></code>,<it>blue</it></code> where
     * <code><it>red</it></code>, <it>green</it></code>, and 
     * <code><it>blue</it></code> are valid ints.
     * </p>
     *
     * @param value the value to be converted
     * @return the value as an RGB color value
     * @exception DataFormatException if the given value does not represent
     *	an RGB color value
     */
    private static ERGB asRGB(String value) throws DataFormatException {
        if (value == null) {
			throw new DataFormatException("Null doesn't represent a valid RGB"); //$NON-NLS-1$
		}
        StringTokenizer stok = new StringTokenizer(value, ","); //$NON-NLS-1$

        try {
            String red = stok.nextToken().trim();
            String green = stok.nextToken().trim();
            String blue = stok.nextToken().trim();
            int rval = 0, gval = 0, bval = 0;
            try {
                rval = Integer.parseInt(red);
                gval = Integer.parseInt(green);
                bval = Integer.parseInt(blue);
            } catch (NumberFormatException e) {
                throw new DataFormatException(e.getMessage());
            }
            return new ERGB(rval, gval, bval);
        } catch (NoSuchElementException e) {
            throw new DataFormatException(e.getMessage());
        }
    }
	
}
