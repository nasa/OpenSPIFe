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
package gov.nasa.arc.spife.ui.timeline;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;

/**
 * This interface is used to give access to normally private and/or protected
 * 	methods of tools without having to change their inheritance structure.
 * @author Eugene Turkov
 *
 */
public interface AccessibleTool {
	public Point getLocation();
	public EditPartViewer getCurrentViewer();
}
