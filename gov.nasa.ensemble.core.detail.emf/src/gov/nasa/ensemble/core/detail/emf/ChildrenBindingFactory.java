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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.detail.emf.binding.ChildrenHyperlinkListener;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

public class ChildrenBindingFactory {

	private static final int MAX_CHILD_COUNT = 50;

	public static void createBinding(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget(); 
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		Object value = EMFUtils.getPropertyValue(pd, target);
		Collection<?> children = (Collection<?>) value;
		if (children != null && !children.isEmpty()) {
			FormToolkit toolkit = parameter.getDetailFormToolkit(); 
			Composite parent = parameter.getParent(); 
			Section section = DetailFormToolkit.createSection(toolkit, parent, pd.getDisplayName(target), null);
			Composite composite = toolkit.createComposite(section);
			composite.setLayout(new GridLayout(2, false));
			section.setClient(composite);
			if (children.size() > MAX_CHILD_COUNT) {
				toolkit.createLabel(composite, children.size() + " children");
			} else {
				ISelectionProvider selectionProvider = parameter.getSelectionProvider();
				for (Object child : children) {
					ILabelProvider labeler = CommonUtils.getAdapter(child, ILabelProvider.class);
					if (labeler == null) {
						AdapterFactory adapterFactory = EMFUtils.getAdapterFactory(child);
						if (adapterFactory != null) {
							labeler = new AdapterFactoryLabelProvider(adapterFactory);
						}
					}
					if (labeler != null) {
						Image image = labeler.getImage(child);
						if (image != null) {
							Label imageLabel = toolkit.createLabel(composite, "");
							imageLabel.setImage(image);
						}
						String text = labeler.getText(child);
						if (text == null) {
							text = "";
						}
						if (selectionProvider != null) {
							Hyperlink link = toolkit.createHyperlink(composite, text, SWT.NONE);
							GridData gridData = new GridData();
							gridData.horizontalAlignment = SWT.FILL;
							gridData.grabExcessHorizontalSpace = true;
							// SPF-7921 Allow hyperlink to be narrower than its preferred size to reduce the need for horizontal scrolling
							// Ellipses will be introduced in the shortened link text
							gridData.minimumWidth = 1;
							gridData.widthHint = Activator.CONTROL_WIDTH_HINT;
							link.setLayoutData(gridData);
							link.addHyperlinkListener(new ChildrenHyperlinkListener(selectionProvider, child));
						} else {
							toolkit.createLabel(composite, text);
						}
						labeler.dispose();
					}
				}
			}
		}
	}

}
