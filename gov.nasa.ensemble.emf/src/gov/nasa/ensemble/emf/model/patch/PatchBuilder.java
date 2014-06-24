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
package gov.nasa.ensemble.emf.model.patch;

import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class PatchBuilder {
	
	private final Patch patch = PatchFactory.eINSTANCE.createPatch();
	
	public PatchBuilder() {
		// do nothing?
	}
	
	public Patch getPatch() {
		return patch;
	}
	
	public void modify(EObject target, EStructuralFeature feature, Object newValue) {
		Object oldValue = target.eGet(feature);
		modify(target, feature, oldValue, newValue);
	}
	
	public void modify(EObject target, EStructuralFeature feature, Object oldValue, Object newValue) {
		PatchFeatureChange change = patch.getFeatureChange(target, feature);
		if (!feature.isMany()) {
			if (feature instanceof EAttribute) {
				change.setValue(newValue);
				change.setOldValue(oldValue);
			} else if (feature instanceof EReference && newValue instanceof EObject) {
				EObject eNewValue = (EObject) newValue;
				if (((EReference) feature).isContainment()) {
					eNewValue = copy(eNewValue);
					((InternalEObject) eNewValue).eSetProxyURI(null);
					change.setReference(eNewValue);
				} else {
					change.setValue(eNewValue);
					change.setOldValue(oldValue);
				}
			}
		} else {
			List<?> oldList = (List<?>) target.eGet(feature);
			List<?> newList = (List<?>) (newValue == null ? new ArrayList() : newValue);
			for (Object object : oldList) {
				PatchListChange listChange = createRemoveListChange(feature, object);
				change.getListChanges().add(listChange);
			}
			int index = oldList.size();
			for (Object object : newList) {
				PatchListChange listChange = createAddListChange(feature, object, index++);
				change.getListChanges().add(listChange);
			}
		}
		generateDisplayMessage(target, change);
	}
	
	public void add(Resource resource, EObject content, int index) {
		PatchResourceChange change = patch.getResourceChange(resource);
		PatchListChange listChange = PatchFactory.eINSTANCE.createPatchListChange();
		listChange.setContainedObject(copy(content));
		listChange.setType(ChangeType.ADD);
		listChange.setIndex(index);
		change.getListChanges().add(listChange);
		patch.getResourceChanges().add(change);
	}
	
	public void remove(Resource resource, EObject content, int index) {
		PatchResourceChange change = patch.getResourceChange(resource);
		PatchListChange listChange = PatchFactory.eINSTANCE.createPatchListChange();
		listChange.setType(ChangeType.REMOVE);
		listChange.setIndex(index);
		change.getListChanges().add(listChange);
		URI uri = EcoreUtil.getURI(content);
		((InternalEObject) content).eSetProxyURI(uri);
		listChange.setNonContainedObject(content);
		patch.getResourceChanges().add(change);
	}
	
//	public void modify(Resource resource, EList<EObject> contents) {
//		PatchResourceChange change = patch.getResourceChange(resource);
//		System.out.println(contents);
//	}
	
	public void add(EObject target, EStructuralFeature feature, Object objectToAdd) {
		checkIfMany(feature);
		PatchFeatureChange change = patch.getFeatureChange(target, feature);
		PatchListChange listChange = createAddListChange(feature, objectToAdd);
		change.getListChanges().add(listChange);
		generateDisplayMessage(target, change);
	}
	
	public void add(EObject target, EStructuralFeature feature, Object objectToAdd, int index) {
		checkIfMany(feature);
		PatchFeatureChange change = patch.getFeatureChange(target, feature);
		PatchListChange listChange = createAddListChange(feature, objectToAdd, index);
		change.getListChanges().add(listChange);
		generateDisplayMessage(target, change);
	}
	
	public void addAll(EObject target, EStructuralFeature feature, List<?> objectsToAdd) {
		checkIfMany(feature);
		PatchFeatureChange change = patch.getFeatureChange(target, feature);
		List<Object> currentList = (List<Object>) target.eGet(feature);
		int index = currentList.size();
		for (Object object : objectsToAdd) {
			PatchListChange listChange = createAddListChange(feature, object, index++);
			change.getListChanges().add(listChange);
		}
		generateDisplayMessage(target, change);
	}
	
	public void addAll(EObject target, EStructuralFeature feature, List<?> objectsToAdd, int index) {
		checkIfMany(feature);
		PatchFeatureChange change = patch.getFeatureChange(target, feature);
		for (Object object : objectsToAdd) {
			int i = (index < 0) ? index : index++; 
			PatchListChange listChange = createAddListChange(feature, object, i);
			change.getListChanges().add(listChange);
		}
		generateDisplayMessage(target, change);
	}
	
	public void remove(EObject target, EStructuralFeature feature, Object objectToRemove) {
		checkIfMany(feature);
		List<Object> currentList = (List<Object>) target.eGet(feature);
		int index = currentList.indexOf(objectToRemove);
		remove(target, feature, objectToRemove, index);
	}
	
	public void remove(EObject target, EStructuralFeature feature, Object objectToRemove, int index) {
		checkIfMany(feature);
		PatchFeatureChange change = patch.getFeatureChange(target, feature);
		PatchListChange listChange = createRemoveListChange(feature, objectToRemove);
		listChange.setIndex(index);
		change.getListChanges().add(listChange);
		generateDisplayMessage(target, change);
	}
	
	public void removeAll(EObject target, EStructuralFeature feature, List<?> objectsToRemove) {
		checkIfMany(feature);
		PatchFeatureChange change = patch.getFeatureChange(target, feature);
		List<Object> currentList = (List<Object>) target.eGet(feature);
		ListIterator iterator = objectsToRemove.listIterator(objectsToRemove.size());
		while(iterator.hasPrevious()) {
			Object object = iterator.previous();
			int index = currentList.indexOf(object);
			PatchListChange listChange = createRemoveListChange(feature, object);
			listChange.setIndex(index);
			change.getListChanges().add(listChange);
		}
		generateDisplayMessage(target, change);
	}
	

	public void generateDisplayMessage(EObject target, PatchFeatureChange featureChange) {
		String targetDisplayName = EMFUtils.getDisplayName(target);
		StringBuffer message = new StringBuffer();
		EStructuralFeature feature = featureChange.getFeature();
		String displayName = EMFUtils.getAnnotation(feature, "detail", "displayName");
		if (displayName == null) {
			displayName = feature.getName();
		}
		message.append("modified ").append(featureChange.getFeature().getName()).append(" for ").append(targetDisplayName);
		featureChange.setDisplayMessage(message.toString());
	}

	private void checkIfMany(EStructuralFeature feature) {
		if (!feature.isMany()) {
			throw new UnsupportedOperationException(feature.getName() +" is not multi-value. Use 'modify' instead.");
		}
	}
	
	private PatchListChange createAddListChange(EStructuralFeature feature, Object toAdd) {
		return createAddListChange(feature, toAdd, -1);
	}

	private PatchListChange createAddListChange(EStructuralFeature feature, Object toAdd, int index) {
		PatchListChange listChange = PatchFactory.eINSTANCE.createPatchListChange();
		listChange.setIndex(index);
		listChange.setType(ChangeType.ADD);
		if (toAdd instanceof EObject) {
			if (feature instanceof EReference && ((EReference) feature).isContainment() && ((EObject) toAdd).eIsProxy()) {
				((InternalEObject) toAdd).eSetProxyURI(null);
			}
			toAdd = copy(feature, (EObject) toAdd);
		}
		listChange.setObject(feature, toAdd);
		return listChange;
	}

	private EObject copy(EStructuralFeature feature, EObject eObject) {
		if (eObject.eContainer() != null || eObject.eResource() != null) {
			if (feature instanceof EReference) {
				EReference reference = (EReference) feature;
				URI uri = (reference.isContainment() ? null : EcoreUtil.getURI(eObject));
				if (reference.isContainment()) {
					eObject = copy(eObject);
				}
				((BasicEObjectImpl)eObject).eSetProxyURI(uri);
			}
		}
		return eObject;
	}
	
	protected EObject copy(EObject eObject) {
		return EcoreUtil.copy(eObject);
	}
	
	protected PatchListChange createRemoveListChange(EStructuralFeature feature, Object objectToRemove) {
		PatchListChange listChange = PatchFactory.eINSTANCE.createPatchListChange();
		listChange.setType(ChangeType.REMOVE);
		listChange.setObject(feature, objectToRemove);
		return listChange;
	}
	
	

}
