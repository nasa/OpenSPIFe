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

import gov.nasa.ensemble.common.ui.detail.IDetail;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class DetailFormPart extends AbstractFormPart {

	private static final Logger trace = Logger.getLogger(DetailFormPart.class);
	private static final TableWrapLayout layout = new TableWrapLayout();
	static {
		layout.numColumns = 2;
	}
	protected Section section;
	private String sectionText = null;
	
	public DetailFormPart() {
		// default constructor
	}
	
	public DetailFormPart(String sectionText) {
		this.sectionText = sectionText;
	}
	
	protected TableWrapLayout getLayout() {
		return layout;
	}
    
	@Override
	public void initialize(IManagedForm form) {
		super.initialize(form);
		section = addSection(form.getForm().getBody(), getSectionText());
	}

	protected final String getSectionText() {
		return sectionText;
	}
	
	protected final Section getSection() {
		return section;
	}

	protected Composite addDetails(Composite parent, List<? extends IDetail> details) {
		FormToolkit toolkit = getManagedForm().getToolkit();
		Composite composite = toolkit.createComposite(parent);
		composite.setLayout(getLayout());
    	for (IDetail d : details) {
    		addDetail(composite, d);
    	}
    	return composite;
	}
	
    protected Composite addDetail(Composite parent, IDetail detail) {
		try {
			return detail.createValueEditorComposite(parent);
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			trace.error("detail creation", t);
		}
		return null;
    }
	
	/**
	 * Adds a standardized section to the form part. This is intended
	 * to give a unified look and feel to the forms parts
	 */
	protected Section addSection(Composite parent, String text) {
		IManagedForm mForm = getManagedForm();
		FormToolkit toolkit = mForm.getToolkit();
		int style = (text == null ? ExpandableComposite.NO_TITLE : ExpandableComposite.TITLE_BAR);
		Section section = toolkit.createSection(parent, style);
		// this ensures that the section will fill up as much space as is available
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		Control buttonComposite = buildButtonComposite(section);
		if (buttonComposite != null) section.setTextClient(buttonComposite);
		if (text != null) {
			section.setText(text);
		}
		return section;
	}

	protected Control buildButtonComposite(Section section) { return null; }
}
