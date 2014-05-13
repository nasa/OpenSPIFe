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
package gov.nasa.ensemble.emf.patch;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.Activator;
import gov.nasa.ensemble.emf.model.patch.ChangeType;
import gov.nasa.ensemble.emf.model.patch.ObjectChanges;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.model.patch.PatchListChange;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RollbackException;

public class PatchRollbackException extends RollbackException {

	private Patch patch;

	public PatchRollbackException(Patch p) {
		super(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "rollback patch"));
		this.patch = p;
	}

	public void doRollback() {
		if (patch != null) {
			for (ObjectChanges objectChange : patch.getObjectChanges()) {
				EObject target = objectChange.getObject();
				PatchFeatureChange featureChange = objectChange.getChange();
				EStructuralFeature feature = featureChange.getFeature();
				if (!feature.isMany()) {
					String oldValueString = featureChange.getOldValueString();
					if (oldValueString == null) {
						oldValueString = "";
					}
					Object revertTo = null;
					if (feature instanceof EAttribute) {
						EDataType type = ((EAttribute)feature).getEAttributeType();
						revertTo = EcoreUtil.createFromString(type, oldValueString);
					} else {
						URI uri = URI.createURI(oldValueString);
						EObject eObject = EcoreFactory.eINSTANCE.createEObject();
						((InternalEObject) eObject).eSetProxyURI(uri);
						revertTo = EcoreUtil.resolve(eObject, patch.eResource().getResourceSet());
						EClass type = ((EReference)feature).getEReferenceType();
						if (revertTo instanceof EObject && type != ((EObject) revertTo).eClass()) {
							LogUtil.error("Failed to rollback: "+ feature.getName() + " " + uri);
							continue;
						}
					}
					target.eSet(feature, revertTo);
				} else {
					int listChangesNumber = featureChange.getListChanges().size();
					ListIterator<PatchListChange> iterator = featureChange.getListChanges().listIterator(listChangesNumber);
					List<Object> currentList = (List<Object>) target.eGet(feature);
					while(iterator.hasPrevious()) {
						PatchListChange listChange = iterator.previous();

						Object object = listChange.getObject(target, feature);
						if ((ChangeType.ADD == listChange.getType() && !listChange.isReversed()) ||
								(ChangeType.REMOVE == listChange.getType() && listChange.isReversed())) {
							if (object != null) {
								int i = currentList.indexOf(object);
								if (i >= 0) {
									currentList.remove(i);
								} else if (object instanceof EObject) {
									URI objectUri = EcoreUtil.getURI((EObject) object);
									int index = listChange.getIndex();
									if (index < currentList.size()) {
										EObject objectAtIndex = (EObject) currentList.get(index);
										URI uriAtIndex = EcoreUtil.getURI(objectAtIndex);
										if (CommonUtils.equals(objectUri.fragment(), uriAtIndex.fragment())) {
											currentList.remove(index);
										}
									}
								}
							}
						} else if ((ChangeType.REMOVE == listChange.getType() && !listChange.isReversed()) ||
								(ChangeType.ADD == listChange.getType() && listChange.isReversed())) {
							if (object != null) {
								int index = listChange.getIndex();
								if (index < 0) {
									//try find it?
								} else if (index < currentList.size()) {
									Object objectAtIndex = currentList.get(index);
									if (objectAtIndex instanceof EObject) {
										URI objectUri = EcoreUtil.getURI((EObject) object);
										URI uriAtIndex = EcoreUtil.getURI((EObject) objectAtIndex);
										if (CommonUtils.equals(objectUri.fragment(), uriAtIndex.fragment())) {
											continue;
										}
									} else if (CommonUtils.equals(object, objectAtIndex)) {
										continue;
									}
									currentList.add(index, object);
								} else {
									currentList.add(object);
								}
							}
						}
					}
				}
			}

			//TODO revert resource changes
		}
	}

}
