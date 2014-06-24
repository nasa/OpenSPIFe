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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.text.StringEscapeFormat;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.ScrolledFormText;

public class ViolationDetailsTreeListener implements ISelectionChangedListener {
	
	private final TreeViewer viewer;
	private final ScrolledFormText violationDetails;
	private final IdentifiableRegistry<EPlanElement> identifiableRegistry;
	private String oldText = null;

	public ViolationDetailsTreeListener(TreeViewer viewer, ScrolledFormText violationDetails, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		this.viewer = viewer;
		this.violationDetails = violationDetails;
		this.identifiableRegistry = identifiableRegistry;
		viewer.addPostSelectionChangedListener(this);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		selected(viewer.getSelection());
	}
	
	private void selected(ISelection selection) {
		StringBuilder builder = new StringBuilder();
		builder.append("<form>");
		java.util.List<Object> objects = consolidateItems(selection);
		appendViolationContents(builder, objects);
		builder.append("</form>");
		String text = builder.toString();
		if (!text.equals(oldText)) {
			oldText = text;
			String formText = StringEscapeFormat.encodeSpecialALTCharacters(StringEscapeUtils.unescapeHtml(text));
			try {
				violationDetails.getFormText().setText(formText, true, true);
			} catch (Exception ex) {
				// SPF-11302 -- in case there are unmatched tags in an activity name or violation description
				violationDetails.setText(text);
			}
			violationDetails.reflow(true);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Object> consolidateItems(ISelection selection) {
		List<Object> objects = new ArrayList<Object>();
		if (selection instanceof IStructuredSelection) {
			List list = ((IStructuredSelection)selection).toList();
			for (Object object : list) {
				objects.add(object);
			}
		}
		return objects;
	}

	private void appendViolationContents(StringBuilder builder,
			List<Object> objects) {
		FormText formText = violationDetails.getFormText();
		for (Object object : objects) {
			builder.append("<p>");
			if (object instanceof ViolationTracker) {
				Violation violation = ((ViolationTracker)object).getViolation();
				builder.append(violation.getFormText(formText, identifiableRegistry));
			} else {
				builder.append(StringEscapeFormat.escape(object.toString()));
			}
			builder.append("</p>");
		}
	}
	
	
}
