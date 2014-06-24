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
package gov.nasa.arc.spife.ui.timeline.policy;

import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;


/** Avoid letting Figure.paintChildren() use the default strategy of creating the same Rectangle[1] over and over,
 * especially during rapid-fire drawing of marching ants, which contributed to that causing a lot of GC's.
 */
public class MemorySavingClippingStrategy implements IClippingStrategy {
	
	public final Rectangle[] array = new Rectangle[] { null };

	@Override
	public Rectangle[] getClip(IFigure figure) {
		array[0] = figure.getBounds();
		return array;
	}

}
