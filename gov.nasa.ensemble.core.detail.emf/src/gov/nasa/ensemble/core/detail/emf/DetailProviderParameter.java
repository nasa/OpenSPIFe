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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DetailProviderParameter {

	private DataBindingContext dataBindingContext;
	private FormToolkit detailFormToolkit;
	private Composite parent;
	private EObject target;
	private IItemPropertyDescriptor propertyDescriptor;
	private EMFDetailFormPart formPart;
	private ISelectionChangedListener selectionChangedListener;
	private ISelectionProvider selectionProvider;

	public EMFDetailFormPart getFormPart() {
		return formPart;
	}

	public void setFormPart(EMFDetailFormPart formPart) {
		this.formPart = formPart;
	}

	public DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	public void setDataBindingContext(DataBindingContext dataBindingContext) {
		this.dataBindingContext = dataBindingContext;
	}

	public FormToolkit getDetailFormToolkit() {
		return detailFormToolkit;
	}
	
	public void setDetailFormToolkit(FormToolkit toolkit) {
		this.detailFormToolkit = toolkit;
	}

	public Composite getParent() {
		return parent;
	}
	
	public void setParent(Composite parent) {
		this.parent = parent;
	}

	public IItemPropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public void setPropertyDescriptor(IItemPropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}
	
	public EObject getTarget() {
		return target;
	}
	
	public void setTarget(EObject target) {
		this.target = target;
	}
	
	public void setSelectionChangedListener(ISelectionChangedListener selectionChangedListener) {
		this.selectionChangedListener = selectionChangedListener;
	}
	
	public ISelectionChangedListener getSelectionChangedListener() {
		return selectionChangedListener;
	}
	
	public void setSelectionProvider(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}
	
	public ISelectionProvider getSelectionProvider() {
		return selectionProvider;
	}
	
	public static DetailProviderParameter createDetailProviderParameter(Composite composite, EObject target, FormToolkit toolkit, IItemPropertyDescriptor pd, DataBindingContext context) {
		DetailProviderParameter p = new DetailProviderParameter();
		p.setParent(composite);
		p.setTarget(target);
		p.setDetailFormToolkit(toolkit);
		p.setPropertyDescriptor(pd);
		p.setDataBindingContext(context);
		return p;
	}
	
}
