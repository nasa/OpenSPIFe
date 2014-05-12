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
/**
 * 
 */
package gov.nasa.ensemble.core.detail.emf.binding;

import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.common.ui.color.ColorUtils;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.RGB;

public final class RGBSelectorObservableValue extends AbstractObservableValue {
	
	private final ColorSelector colorSelector;
	private final boolean isERGB;
	private final WidgetListener widgetListener = new WidgetListener();

	public RGBSelectorObservableValue(ColorSelector colorSelector, boolean isERGB) {
		this.colorSelector = colorSelector;
		this.isERGB = isERGB;
		colorSelector.addListener(widgetListener);
		colorSelector.getButton().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
	}

	@Override
	public synchronized void dispose() {
		colorSelector.removeListener(widgetListener);
		super.dispose();
	}
	
	@Override
	protected void doSetValue(Object value) {
		if (value instanceof RGB) {
			RGB rgb = (RGB) value;
			colorSelector.setColorValue(rgb);
		} else if (value instanceof ERGB) {
			RGB rgb = ColorUtils.getAsRGB((ERGB) value);
			colorSelector.setColorValue(rgb);
		}
	}

	@Override
	protected Object doGetValue() {
		RGB colorValue = colorSelector.getColorValue();
		if (isERGB) {
			return ColorUtils.getAsERGB(colorValue);
		} // else...
		return colorValue;
	}

	@Override
	public Object getValueType() {
		return RGB.class;
	}
	
	private final class WidgetListener implements IPropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Object oldValue = event.getOldValue();
			Object newValue = event.getNewValue();
			fireValueChange(Diffs.createValueDiff(oldValue, newValue));
		}
	}

}
