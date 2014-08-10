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

import fj.data.Option;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.detail.DetailFactory;
import gov.nasa.ensemble.common.ui.detail.IDetailFactory;
import gov.nasa.ensemble.common.ui.detail.IDetailSheet;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemIntersectionPropertySource;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertySource;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemUnionPropertySource;
import gov.nasa.ensemble.core.detail.emf.multi.MultiObject;
import gov.nasa.ensemble.core.detail.emf.multi.MultiObject.Type;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class EMFDetailFactory extends DetailFactory {

	@Override
	public IDetailSheet buildDetailSheet(Object object, FormToolkit formToolkit, final ScrolledForm form, ISelectionProvider selectionProvider) {
		final EObject eObject = getEObject(object);
		if (eObject != null) {
			IDetailFactory factory = EMFUtils.adapt(object, IDetailFactory.class);
			if (factory != null) {
				return factory.buildDetailSheet(object, formToolkit, form, selectionProvider);
			}
			final IDetailSheet sheet = new EMFDetailSheet(formToolkit, form, selectionProvider);
			EObject target = eObject;
			if (target instanceof MultiEObject) {
				MultiEObject multiEObject = (MultiEObject) target;
				Iterator<? extends EObject> iterator = multiEObject.getEObjects().iterator();
				if (iterator.hasNext()) {
					target = iterator.next();
				}
			}
			TransactionalEditingDomain domain = TransactionUtils.getDomain(target);
			if (domain == null) {
				setSheetInput(form, eObject, sheet);
			} else {
				TransactionUtils.runInDisplayThread(form, domain, new Runnable() {
					@Override
					public void run() {
						setSheetInput(form, eObject, sheet);
					}
				});
			}
			return sheet;
		}
		return null;
	}

	private void setSheetInput(final ScrolledForm form, final EObject eObject, final IDetailSheet sheet) {
		try {
			if (!((EMFDetailSheet) sheet).isDisposed()) {
				sheet.setInput(eObject);
			}
		} catch (Exception e) {
			LogUtil.error("setting " + eObject, e);
		}
	}

	private EObject getEObject(Object object) {
		if (object instanceof Collection) {
			List<EObject> eObjects = new ArrayList<EObject>();
			Collection<?> collection = (Collection<?>) object;
			for (Object o : collection) {
				EObject eObject = getEObject(o);
				if (eObject != null) {
					eObjects.add(eObject);
				}
			}
			if (eObjects.size() != collection.size()) {
				return null;
			}
			if (eObjects.size() == 1) {
				object = eObjects.get(0);
			} else {
				final MultiItemPropertySource source;
				final Option<String> label;
				final Option<Image> icon;
				if (collection instanceof MultiObject) {
					final MultiObject multiObject = (MultiObject) collection;
					source = (multiObject.getType() == Type.UNION) ? new MultiItemUnionPropertySource(eObjects) : new MultiItemIntersectionPropertySource(eObjects);
					label = multiObject.getLabel();
					icon = multiObject.getIcon();
				} else {
					source = new MultiItemIntersectionPropertySource(eObjects);
					label = Option.none();
					icon = Option.none();
				}
				return new MultiEObject(eObjects, source, label, icon);
			}
		}
		if (object instanceof EObject) {
			return (EObject) object;
		} else if (object instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) object;
			Object result = adaptable.getAdapter(EObject.class);
			if (result instanceof EObject) {
				return (EObject) result;
			} else {
				if (adaptable instanceof Resource) {
					// TODO: maybe something more elaborate here?
					Resource resource = (Resource) adaptable;
					EList<EObject> contents = resource.getContents();
					return contents.iterator().next();
				}
				result = EMFUtils.adapt(object, EObject.class);
				if (result instanceof EObject) {
					return (EObject) result;
				}
			}
		} else if (object instanceof IWrapperItemProvider) {
			return (EObject) AdapterFactoryEditingDomain.unwrap(object);
		}
		return null;
	}

}
