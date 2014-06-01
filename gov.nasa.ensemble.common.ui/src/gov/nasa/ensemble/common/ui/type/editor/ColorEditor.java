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
package gov.nasa.ensemble.common.ui.type.editor;

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorUtils;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ColorEditor extends AbstractTypeEditor<String> {

	private final Composite parent;
	private final ColorSelector colorChooser;
	
	public ColorEditor(Composite parent) {
		super(String.class);
		this.parent = parent;
		this.colorChooser = createColorSelector();
	}
	
	@Override
	public Control getEditorControl() {
		return colorChooser.getButton();
	}
	
	@Override
	public void setObject(final Object object) {
		super.setObject(object);
		WidgetUtils.runInDisplayThread(parent, new Runnable() {
			@Override
			public void run() {
				if (object != null) {
					try {
						// Color editor is expecting a HEX CODE INTEGER in string form
						Integer hexcode = Integer.parseInt(object.toString(), 16);
						if (hexcode != null) {
							RGB rgb = new RGB((hexcode/256/256)%256, (hexcode/256)%256, hexcode%256);
							colorChooser.setColorValue(rgb);
						}
					} catch (Exception e) {
						try {
							RGB rgb = StringConverter.asRGB(object.toString());
							if (rgb != null) {
								colorChooser.setColorValue(rgb);
							}
						} catch (Exception x) {
							// ignore it
						}
					}
				}
			}
		});
	}
	
	private ColorSelector createColorSelector() {
		final ColorSelector chooser = new ColorSelector(parent);
		chooser.addListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String oldValue = getObject();
				
				// get the RGB values, and update
				RGB rgb = colorChooser.getColorValue();
				String newValue = ColorUtils.formatRGB(rgb);
				setObject(newValue);
				firePropertyChange(oldValue, newValue);
			}
		});
		return chooser;
	}
	
}
