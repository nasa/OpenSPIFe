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
package gov.nasa.ensemble.core.detail.emf.multi;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import fj.data.Option;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

public class MultiEObject extends EObjectImpl implements IAdaptable, IEditingDomainProvider {

	private final List<? extends EObject> eObjects;
	private final MultiItemPropertySource source;
	private final Option<String> label;
	private final Option<Image> icon;

	public MultiEObject(List<? extends EObject> eObjects, MultiItemPropertySource source) {
		this(eObjects, source, Option.<String> none(), Option.<Image> none());
	}

	public MultiEObject(List<? extends EObject> eObjects, MultiItemPropertySource source, Option<String> label, Option<Image> icon) {
		this.eObjects = eObjects;
		this.source = source;
		this.label = label;
		this.icon = icon;
	}

	public MultiItemPropertySource getSource() {
		return source;
	}

	public Collection<? extends EObject> getEObjects() {
		return eObjects;
	}

	@Override
	public Object eGet(EStructuralFeature feature) {
		Object value = null;
		for (EObject eObject : eObjects) {
			if (value == null) {
				value = eObject.eGet(feature);
			} else {
				if (!CommonUtils.equals(value, eObject.eGet(feature))) {
					return null;
				}
			}
		}
		return value;
	}

	@Override
	public void eSet(final EStructuralFeature feature, final Object newValue) {
		TransactionUtils.writing(eObjects.toArray()[0], new Runnable() {
			@Override
			public void run() {
				for (EObject eObject : eObjects) {
					eObject.eSet(feature, newValue);
				}
			}
		});
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (ILabelProvider.class == adapter) {
			return new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (label.isSome())
						return label.some();
					return getEObjects().size() + " Items Selected";
				}

				@Override
				public Image getImage(Object element) {
					if (icon.isSome())
						return icon.some();
					return super.getImage(element);
				}
			};
		}
		return null;
	}

	@Override
	public EditingDomain getEditingDomain() {
		EditingDomain domain = null;
		for (EObject object : getEObjects()) {
			EditingDomain currentDomain = EMFUtils.getAnyDomain(object);
			if (domain == null) {
				domain = currentDomain;
			} else if (currentDomain != null && domain != currentDomain) {
				return null;
			}
		}
		return domain;
	}

	@Override
	public Resource eResource() {
		Resource resource = null;
		for (EObject object : getEObjects()) {
			Resource currentResource = object.eResource();
			if (resource == null) {
				resource = currentResource;
			} else if (currentResource != null && resource != currentResource) {
				return null;
			}
		}
		return resource;
	}

}