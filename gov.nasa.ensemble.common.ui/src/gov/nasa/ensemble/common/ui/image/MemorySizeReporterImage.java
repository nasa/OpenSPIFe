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
package gov.nasa.ensemble.common.ui.image;

import gov.nasa.ensemble.common.cache.SizeReporter;

import org.eclipse.swt.graphics.ImageData;

public class MemorySizeReporterImage implements SizeReporter {

	private ImageData image;
	private long size;
	
	public MemorySizeReporterImage(ImageData image) {
		this.image = image;
		
		/* compute size as size of raster times 4 bytes per pixel (ARGB) */
		size = image.width *  image.height * 4;
	}
	
	@Override
	public long getSize() {
		return size;
	}

	public ImageData getImage() {
		return image;
	}

}
