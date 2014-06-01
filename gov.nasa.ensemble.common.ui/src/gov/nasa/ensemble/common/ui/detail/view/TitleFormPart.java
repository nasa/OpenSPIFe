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
package gov.nasa.ensemble.common.ui.detail.view;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class TitleFormPart extends AbstractFormPart {

	protected Composite titleComposite;
	protected Label textLabel;
	protected Label imageLabel;
	protected Label lockedLabel;
	
	private ILabelProvider labelProvider = null;
	
	private static final Image LOCKED_ICON = IconLoader.getIcon(CommonPlugin.getDefault().getBundle(), "icons/multimission/lock.png");
	
	public TitleFormPart() {
		// default constructor
	}
	
	public TitleFormPart(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}
	
	public TitleFormPart(final String staticTitle) {
		this(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return staticTitle;
			}
		});
	}
	
	@Override
	public void initialize(IManagedForm mform) {
		super.initialize(mform);
		
		FormToolkit toolkit = mform.getToolkit();
		ScrolledForm sform = mform.getForm();
		
		titleComposite = toolkit.createComposite(sform.getBody());
		titleComposite.setLayout(new GridLayout(getNumTitleComponents(), false));
        titleComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        
        populateTitleComposite(toolkit, sform, titleComposite);

		Composite separator = toolkit.createCompositeSeparator(sform.getBody());
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP);
		data.maxHeight = 2;
		separator.setLayoutData(data);
	}
	
	protected void populateTitleComposite(FormToolkit toolkit, ScrolledForm sform, Composite titleComposite) {
		imageLabel = toolkit.createLabel(titleComposite, "");
		
		Form form = sform.getForm();
		textLabel = toolkit.createLabel(titleComposite, "");
		textLabel.setFont(form.getFont());
		textLabel.setForeground(form.getForeground());
		
		lockedLabel = toolkit.createLabel(titleComposite, "");
	}
	
	protected int getNumTitleComponents() {
		return 3;
	}
	
	@Override
	public boolean setFormInput(final Object input) {
		WidgetUtils.runInDisplayThread(titleComposite, new Runnable() {
			@Override
			public void run() {
				if (input == null) {
					textLabel.setText("");
					textLabel.setToolTipText("");
					lockedLabel.setImage(null);
					return;
				}
				if (imageLabel != null) {
					imageLabel.setImage(getImage(input));
				}
				if (textLabel != null) {
					String text = getText(input);
					textLabel.setText(text == null ? "" : text);
					textLabel.setToolTipText(getTooltipText(input));
				}
				if (lockedLabel != null) {
					lockedLabel.setImage(getLocked(input)? LOCKED_ICON : null);
				}
			}
		});
		return true;
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

	/**
	 * Users may override this function to customize contents of
	 * the title image. This method is called by setFormInput and
	 * handles basic types (in order):
	 * 
	 * 	* If an ILabelProvider is defined, delegates
	 * 	* Else, null is returned
	 */
	protected Image getImage(Object input) {
		if (labelProvider != null) {
			return labelProvider.getImage(input);
		}
		return null;
	}

	/**
	 * Users may override this function to customize the contents
	 * of the title text. This method is called by setFormInput and
	 * and handles the following basic types (in order):
	 * 
	 * 	* In an ILabelProvider is defined, delegates
	 * 	* A null object is returned as an empty string
	 * 	* A string is returned as such
	 * 	* Finally, the toString method is called on the object
	 */
	protected String getText(Object input) {
		if (labelProvider != null)
			return labelProvider.getText(input);
		
		if (input == null) 
		{
			return "";
		}
		else if (input instanceof String) 
		{ 
			return (String) input;
		}
		return input.toString();
	}

	protected String getTooltipText(Object input) {
		return "";
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
		
		if (lockedLabel != null) {
			lockedLabel.dispose();
			lockedLabel = null;
		}
		
		super.dispose();
	}

	protected boolean getLocked(Object input) {
		return false;
	}
	
}
