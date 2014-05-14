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
package gov.nasa.arc.spife.ui.timeline.chart.model.provider.detail;

import gov.nasa.ensemble.common.ui.detail.IDetailSheet;
import gov.nasa.ensemble.core.detail.emf.EMFTitleFormPart;

import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class PlotDetailSheet<T> extends ManagedForm implements IDetailSheet {

	public PlotDetailSheet(FormToolkit toolkit, ScrolledForm form) {
		super(toolkit, form);
		addPart(new EMFTitleFormPart());
		addPart(new PlotDetailFormPart());
		addPart(new PlotFitFormPart(form.getBody(), toolkit, ExpandableComposite.TITLE_BAR));
	}
	
}
