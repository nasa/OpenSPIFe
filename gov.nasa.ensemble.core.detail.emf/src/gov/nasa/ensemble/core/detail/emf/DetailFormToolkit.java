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
package gov.nasa.ensemble.core.detail.emf;

import gov.nasa.ensemble.emf.util.EMFUtils;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class DetailFormToolkit {

	/**
	 * Adds a standardized section to the form part. This is intended to give a unified look and feel to the forms parts.
	 * 
	 * @param parent
	 *            component to contribute to
	 * @param text
	 *            title of the section
	 * @param icon
	 *            image to set in the section
	 * @return Section object
	 */
	public static Section createSection(FormToolkit toolkit, Composite parent, String text, Image icon, int style) {
		style = style | (text == null ? ExpandableComposite.NO_TITLE : ExpandableComposite.TITLE_BAR);
		Section section = toolkit.createSection(parent, style);
		if (icon != null) {
			Label label = toolkit.createLabel(section, "");
			label.setImage(icon);
			section.setTextClient(label);
		}
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		layoutData.horizontalSpan = 2;
		layoutData.verticalIndent = 6;
		section.setLayoutData(layoutData);
		if (text != null) {
			section.setText(text);
		}
		return section;
	}
	
	public static Section createSection(FormToolkit toolkit, Composite parent, String text, Image icon) {
		return createSection(toolkit, parent, text, icon, 0);
	}
	
	public static Section createSection(FormToolkit toolkit, Composite parent, String text, Image icon, boolean hasTwistie) {
		return createSection(toolkit, parent, text, icon, (hasTwistie) ? ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED : 0);
	}
	
	public static Composite createSubSection(FormToolkit toolkit, Composite parent, EObject eObject, boolean hasLabel) {
		Composite subSection = null;
		IItemLabelProvider labelProvider = EMFUtils.adapt(eObject, IItemLabelProvider.class);
		if (labelProvider != null) {
			String text = labelProvider.getText(eObject);
			Image image = null;
			Object imageURL = labelProvider.getImage(eObject);
			if (imageURL != null) {
				try {
					image = ExtendedImageRegistry.getInstance().getImage(imageURL);
				} catch (Exception e) {
					Logger.getLogger(DetailFormToolkit.class).error("failed to get image", e);
				}
			}
			Section section = createSection(toolkit, parent, text, image);
			subSection = toolkit.createComposite(section);
			GridLayout layout = new GridLayout((hasLabel) ? 2 : 1, false);
			layout.verticalSpacing = 2;
			layout.horizontalSpacing = 10;
			subSection.setLayout(layout);
			section.setClient(subSection);
		}
		return subSection;
	}

}
