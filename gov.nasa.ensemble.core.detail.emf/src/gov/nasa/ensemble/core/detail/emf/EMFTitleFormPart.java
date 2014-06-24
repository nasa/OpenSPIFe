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
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class EMFTitleFormPart extends AbstractFormPart {

	protected Composite titleComposite;
	protected Label textLabel;
	protected Label imageLabel;
	private EObject input = null;
	private ILabelProvider labelProvider = null;
	private ILabelProviderListener listener = new Listener();
	
	@Override
	public void initialize(IManagedForm mform) {
		super.initialize(mform);
		FormToolkit toolkit = mform.getToolkit();
		ScrolledForm sform = mform.getForm();
		Composite body = sform.getBody();
		titleComposite = toolkit.createComposite(body);
		titleComposite.setLayout(new GridLayout(3, false));
//        titleComposite.setLayoutData(new ColumnLayoutData());
        populateTitleComposite(toolkit, sform, titleComposite);
	}
	
	public Composite getComposite() {
		return titleComposite;
	}
	
	protected void populateTitleComposite(FormToolkit toolkit, ScrolledForm sform, Composite titleComposite) {
		imageLabel = toolkit.createLabel(titleComposite, "");
		Form form = sform.getForm();
		textLabel = toolkit.createLabel(titleComposite, "");
		textLabel.setFont(form.getFont());
		textLabel.setForeground(form.getForeground());
	}

	@Override
	public boolean setFormInput(final Object input) {
		if (this.input != input) {
			if (this.input != null) {
				this.input.eAdapters().remove(listener);
				if (this.labelProvider != null) {
					this.labelProvider.removeListener(listener);
					this.labelProvider.dispose();
					this.labelProvider = null;
				}
			}
		}
		WidgetUtils.runInDisplayThread(titleComposite, new Runnable() {
			@Override
			public void run() {
				if (input == null) {
					textLabel.setText("");
					return;
				}
				if (imageLabel != null) {
					imageLabel.setImage(getImage(input));
				}
				if (textLabel != null) {
					String text = getText(input);
					textLabel.setText(text == null ? "" : text);
				}
			}
		});
		if (this.input != input) {
			this.input = (EObject) input;
		}
		return true;
	}

	@Override
	public void dispose() {
		if (textLabel != null) {
			textLabel.dispose();
			textLabel = null;
		}
		
		if (imageLabel != null) {
			imageLabel.dispose();
			imageLabel = null;
		}
	
		if (labelProvider != null) {
			labelProvider.removeListener(listener);
			labelProvider.dispose();
			labelProvider = null;
		}
		
		if (this.input != null) {
			this.input.eAdapters().remove(listener);
		}
		super.dispose();
	}
	
	/**
	 * Refresh's the icon and text labels in the title
	 */
	@Override
	public void refresh() {
		setFormInput(getManagedForm().getInput());
		if (textLabel != null && !textLabel.isDisposed())
			textLabel.getParent().layout(true, true);
		super.refresh();
	}

	protected Image getImage(Object input) {
		if (input != null) {
			ILabelProvider p = getLabelProvider(input);
			if (p != null) {
				return p.getImage(input);
			}
		}
		return null;
	}

	protected String getText(Object input) {
		if (input != null) {
			ILabelProvider p = getLabelProvider(input);
			if (p != null) {
				return p.getText(input);
			}
		}
		return "";
	}

	private ILabelProvider getLabelProvider(final Object input) {
		if (labelProvider == null) {
			labelProvider = CommonUtils.getAdapter(input, ILabelProvider.class);
			if (labelProvider == null) {
				AdapterFactory adapterFactory = EMFUtils.getAdapterFactory(input);
				if (adapterFactory != null) {
					labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
				}
			}
			if (labelProvider != null) {
				this.labelProvider.addListener(listener);
			}
		}
		return labelProvider;
	}

	private class Listener implements ILabelProviderListener {

		@Override
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			WidgetUtils.runInDisplayThread(titleComposite, new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
		}
	}
}
