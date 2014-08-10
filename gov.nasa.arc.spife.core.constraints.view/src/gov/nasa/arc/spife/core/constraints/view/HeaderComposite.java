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
package gov.nasa.arc.spife.core.constraints.view;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Arrays;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * A header for forms that display info for PlanElements
 */
/* package */class HeaderComposite extends Composite {

	private final String nothingSelected = "Nothing selected";

	private final Label imageLabel;
	private final Label textLabel;

	private final ILabelProviderListener listener = new NameChangeListener();

	private EPlanElement planElement = null;
	private ILabelProvider labelProvider = null;

	/* package */HeaderComposite(Composite parent, FormToolkit toolkit) {
		super(parent, SWT.NULL);
		toolkit.adapt(this);
		setLayout(new GridLayout(2, false));
		imageLabel = toolkit.createLabel(this, "");
		imageLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		textLabel = toolkit.createLabel(this, nothingSelected);
		textLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		layout();
	}

	@Override
	public void dispose() {
		setPlanElement(null);
		super.dispose();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		textLabel.setFont(font);
	}

	@Override
	public void setForeground(Color color) {
		super.setForeground(color);
		textLabel.setForeground(color);
	}

	public void setPlanElements(Set<EPlanElement> nodes) {
		Image image = null;
		String text = nothingSelected;
		if ((nodes != null) && (nodes.size() == 1)) {
			EPlanElement element = nodes.iterator().next();
			setPlanElement(element);
			if (labelProvider != null) {
				image = labelProvider.getImage(element);
				text = labelProvider.getText(element);
				if (text == null) {
					text = element.getName();
				}
			} else {
				text = element.getName();
			}
		} else {
			setPlanElement(null);
		}
		if ((nodes != null) && (nodes.size() > 1)) {
			text = nodes.size() + " items selected";
		}
		if (text == null) {
			text = "<null>";
		}
		imageLabel.setImage(image);
		textLabel.setText(labelize(text));
		layout();
	}

	private void setPlanElement(EPlanElement element) {
		if (labelProvider != null) {
			labelProvider.removeListener(listener);
			labelProvider.dispose();
			labelProvider = null;
		}
		planElement = element;
		if (planElement != null) {
			TransactionalEditingDomain domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(planElement);
			if (domain != null) {
				AdapterFactory factory = ((AdapterFactoryEditingDomain) domain).getAdapterFactory();
				if (factory != null) {
					labelProvider = (ILabelProvider) factory.adapt(element, ILabelProvider.class);
					if (labelProvider != null) {
						labelProvider.addListener(listener);
					} else {
						LogUtil.warnOnce("null label provider for element: " + element.getName());
					}
				}
			}
		}
	}

	private class NameChangeListener implements ILabelProviderListener {

		@Override
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			Object[] elements = event.getElements();
			if ((elements == null) || (Arrays.asList(elements).contains(planElement))) {
				TransactionUtils.runInDisplayThread(imageLabel, planElement, new Runnable() {
					@Override
					public void run() {
						textLabel.setText(labelize(PlanPrinter.getPrintName(planElement)));
						imageLabel.setImage(PlanUtils.getIcon(planElement));
						layout();
					}
				});
			}
		}
	}

	public String labelize(String label) {
		return label.replaceAll("&", "&&");
	}
}
