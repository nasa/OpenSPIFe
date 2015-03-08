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
package gov.nasa.ensemble.dictionary.provider;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class DictionaryLabelProvider extends AdapterFactoryLabelProvider implements Adapter {

	public DictionaryLabelProvider(AdapterFactory factory) {
		super(factory);
	}

	@Override
	public Image getImage(Object object) {
		// Get the adapters from the factory.
		//
		IItemLabelProvider itemLabelProvider = (IItemLabelProvider) adapterFactory.adapt(object, IItemLabelProvider.class);
		IItemColorProvider itemColorProvider = (IItemColorProvider) adapterFactory.adapt(object, IItemColorProvider.class);

		Image image = itemLabelProvider != null ? getImageFromObject(itemLabelProvider.getImage(object)) : getDefaultImage(object);

		if (itemColorProvider != null) {
			RGB rgb = (RGB) itemColorProvider.getBackground(object);
			if (rgb != null) {
				return getImageOverlay(image, rgb);
			}
		}
		return image;
	}
	
	private Image getImageOverlay(Image image, RGB rgb) {
		Rectangle rect = image.getBounds();
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		
		// Create an image data with a 2-color (i.e. depth 1) palette
		Color black = display.getSystemColor(SWT.COLOR_BLACK);
		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		PaletteData palette = new PaletteData(new RGB[] { black.getRGB(), 
				white.getRGB(), // pixel 1 = white
		});
		ImageData imageData = new ImageData(rect.width, rect.height, 1, palette);
		imageData.transparentPixel = 1; // set the transparent color to white
		
		// Create an image from the image data, fill it with white, and draw a circle on it
		Image imageOverlay = new Image(display, imageData);
		GC gc = new GC(imageOverlay);
		gc.setBackground(white);
		gc.fillRectangle(rect.x, rect.y, rect.width, rect.height); // fill the whole image with white
		gc.setBackground(new Color(null, rgb));
		gc.fillOval(rect.x, rect.y, rect.width, rect.height);
		gc.drawImage(image, rect.x, rect.y);
		gc.dispose();

		// Get the image data for the drawn image, and use it to create the final overlay image
		imageData = imageOverlay.getImageData();
		imageOverlay.dispose();
		
		return new Image(display, imageData, imageData.getTransparencyMask());
	}

	public Notifier getTarget() {
		return null;
	}

	public boolean isAdapterForType(Object type) {
		return false;
	}

	public void setTarget(Notifier newTarget) {
		// no implementation
	}
	
}