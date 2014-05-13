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
package gov.nasa.ensemble.core.detail.emf.view;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.detail.view.AbstractDetailSheet;
import gov.nasa.ensemble.core.detail.emf.EMFDetailFormPart;
import gov.nasa.ensemble.core.detail.emf.EMFTitleFormPart;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class EMFDetailSheet extends AbstractDetailSheet {
	
	private final Layout formerFormBodyLayout;
	private final ISelectionProvider selectionProvider;
	private final Composite body;
	private boolean disposed = false;
	
	public EMFDetailSheet(FormToolkit toolkit, ScrolledForm form, ISelectionProvider selectionProvider) {
		super(toolkit, form);
		this.selectionProvider = selectionProvider;
		body = form.getBody();
		formerFormBodyLayout = body.getLayout();
	}

	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public boolean setInput(Object input) {
		ColumnLayout layout = new ColumnLayout();
		layout.topMargin = 0;
		layout.bottomMargin = 0;
		layout.verticalSpacing = 0;
		layout.maxNumColumns = 4;
		body.setLayout(layout);
		if (!(input instanceof EObject)) {
			return false;
		}
		FormToolkit toolkit = getToolkit();
		EObject object = (EObject) input;
		List<IFormPart> formParts = EMFDetailFormPart.getFormParts(toolkit, object, true, selectionProvider);
		for (IFormPart formPart : formParts) {
			addPart(formPart);
		}
		boolean result = super.setInput(input);
		ScrolledForm form = getForm();
		form.reflow(true);
		form.setOrigin(0, 0);
		checkParts(layout, formParts, input);
		return result;
	}

	private void checkParts(ColumnLayout layout, List<IFormPart> formParts, Object input) {
		for (IFormPart part : formParts) {
			Composite composite = null;
			if (part instanceof EMFDetailFormPart) {
				EMFDetailFormPart emfDetailFormPart = (EMFDetailFormPart) part;
				composite = emfDetailFormPart.getComposite();
			}
			if (part instanceof EMFTitleFormPart) {
				EMFTitleFormPart emfTitleFormPart = (EMFTitleFormPart) part;
				composite = emfTitleFormPart.getComposite();
			}
			if (composite != null) {
				checkControl(part, layout, composite, input);
			}
		}
	}

	private void checkControl(IFormPart emfDetailFormPart, Layout layout, Control control, Object input) {
		Object layoutData = control.getLayoutData();
		if (layoutData != null) {
			String mismatch = null;
			if ((layout instanceof ColumnLayout) && !(layoutData instanceof ColumnLayoutData)) {
				mismatch = "ColumnLayout is incompatible with " + layoutData;
			}
			if ((layout instanceof GridLayout) && !(layoutData instanceof GridData)) {
				mismatch = "GridLayout is incompatible with " + layoutData;
			}
			if ((layout instanceof TableWrapLayout) && !(layoutData instanceof TableWrapData)) {
				mismatch = "TableWrapLayout is incompatible with " + layoutData;
			}
			if (mismatch != null) {
				StringBuilder message = new StringBuilder(mismatch);
				message.append('\n');
				message.append("clearing invalid layout data on control " + control + " in form part: " + emfDetailFormPart);
				message.append('\n');
				message.append("the input was: " + input);
				LogUtil.error(message);
				control.setLayoutData(null);
			}
		}
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			Control[] children = composite.getChildren();
			for (Control child : children) {
				checkControl(emfDetailFormPart, composite.getLayout(), child, input);
			}
		}
	}

	public static Object getAdapter(EObject eObject, Class<?> cls) {
		for (Adapter adapter : eObject.eAdapters()) {
			if (cls.isInstance(adapter)) {
				return adapter;
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		disposed  = true;
		if (!body.isDisposed())
			body.setLayout(formerFormBodyLayout);
		super.dispose();
	}
	
}
