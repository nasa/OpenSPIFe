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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.dictionary.ObjectDef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

public class PlanElementCopier extends Copier {
	
	private boolean recursive = true;
	private Predicate<EObject> filter;
	
	public PlanElementCopier() {
		super();
	}

	public PlanElementCopier(boolean recursive) {
		super();
		this.recursive = recursive;
	}
	
	public PlanElementCopier(boolean recursive, Predicate<EObject> filter) {
		super();
		this.recursive = recursive;
		this.filter = filter;
	}
	
	@SuppressWarnings("unchecked") // copier code guarantees same type
	public <T extends EPlanElement> T copyPlanElement(T planElement) {
		return (T) copy(planElement);
	}
	
	/* By default, the PlanElementCopier should generate new id's for all internal containment references. 
	 * In some special cases, like creating a patch, copy should NOT regenerate id's so a boolean can be passed using 
	 * PlanElementCopier.copy(eObject, false) */
	@Override
	public EObject copy(EObject eObject) {
		return copy(eObject, true);
	}

	public EObject copy(EObject object, boolean newIds) {
		if (object == null) {
			return null;
		}
		EObject copyEObject = createCopy(object);
		put(object, copyEObject);
		EClass objectEClass = object.eClass();
		for (int i = 0, size = objectEClass.getFeatureCount(); i < size; ++i) {
			EStructuralFeature eStructuralFeature = objectEClass.getEStructuralFeature(i);
			if (eStructuralFeature.isChangeable() && !eStructuralFeature.isDerived()) {
				if (eStructuralFeature instanceof EAttribute) {
					copyAttribute((EAttribute)eStructuralFeature, object, copyEObject);
				} else {
					EReference eReference = (EReference)eStructuralFeature;
					if (eReference.isContainment()) {
						copyContainment(eReference, object, copyEObject, newIds);
					}
				}
			}
		}
		copyProxyURI(object, copyEObject);
		if ((object instanceof EPlanElement) && (copyEObject instanceof EPlanElement)) {
			EPlanElement objectElement = (EPlanElement) object;
			EPlanElement resultElement = (EPlanElement) copyEObject;
			for (EMember objectMember : objectElement.getMembers()) {
				String key = objectMember.getKey();
				EMember resultMember = resultElement.getMember(key);
				EClass memberEClass = objectMember.eClass();
				EStructuralFeature calculatedVariableFeature = objectMember.getCalculatedVariableFeature();
				if (calculatedVariableFeature != null) {
					// copy calculated variable feature first to prevent problems with determining which feature
					// is calculated in the copy when feature values are set
					copyStructuralFeature(calculatedVariableFeature, objectMember, resultMember);
				}
				for (int i = 0, size = memberEClass.getFeatureCount(); i < size; ++i) {
					EStructuralFeature eStructuralFeature = memberEClass.getEStructuralFeature(i);
					if (eStructuralFeature.isChangeable()
							&& !eStructuralFeature.isDerived()
							&& !objectMember.isCalculated(eStructuralFeature)
							&& eStructuralFeature != calculatedVariableFeature
							) {
						copyStructuralFeature(eStructuralFeature, objectMember, resultMember);
					}
				}
			}
		}
		return copyEObject;
	}
	
	protected void copyStructuralFeature(EStructuralFeature eStructuralFeature, EMember objectMember, EMember resultMember) {
		if (eStructuralFeature instanceof EAttribute) {
			copyAttribute((EAttribute) eStructuralFeature, objectMember, resultMember);
		} else {
			EReference eReference = (EReference) eStructuralFeature;
			if (eReference.isContainment()) {
				copyContainment(eReference, objectMember, resultMember);
			}
		}
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}
	
	protected void copyContainment(EReference eReference, EObject eObject, EObject copyEObject, boolean needNewIds) {
		if (recursive || !isChildrenReference(eReference)) {
			EClassifier classifier = eReference.getEType();
			EAttribute idFeature = ((EClass) classifier).getEIDAttribute();
			if (eObject.eIsSet(eReference)) {
				if (eReference.isMany()) {
					@SuppressWarnings("unchecked") List<EObject> source = (List<EObject>)eObject.eGet(eReference);
					@SuppressWarnings("unchecked") List<EObject> target = (List<EObject>)copyEObject.eGet(getTarget(eReference));
					target.clear();
					if (!source.isEmpty()) {
						Collection<EObject> copies = copyAllFiltered(source, needNewIds);
						if(needNewIds && classifier instanceof ObjectDef) {
							for (EObject object : copies) {
								object.eSet(idFeature, EcoreUtil.generateUUID());
							}
						}
						target.addAll(copies);
					}
				} else {
					EObject source = (EObject)eObject.eGet(eReference);
					EObject target = copy(source, needNewIds);
					if(needNewIds && target != null && classifier instanceof ObjectDef) {
						target.eSet(idFeature, EcoreUtil.generateUUID());
					}
					copyEObject.eSet(getTarget(eReference), source == null ? null : target);
				}
			}
		}
	}
	
	@Override
	protected void copyContainment(EReference eReference, EObject eObject, EObject copyEObject) {
		copyContainment(eReference, eObject, copyEObject, true);
	}
	
	protected <T> Collection<T> copyAll(Collection<? extends T> eObjects, boolean needNewIds) {
		Collection<T> result = new ArrayList<T>(eObjects.size());
		for (Object object : eObjects) {
			@SuppressWarnings("unchecked") T t = (T)copy((EObject)object, needNewIds);
			result.add(t);
		}
		return result;
	}

	private Collection<EObject> copyAllFiltered(List<EObject> source, boolean needNewIds) {
		if (filter == null) {
			return copyAll(source, needNewIds);
		} 
		Collection<EObject> result = new ArrayList<EObject>(source.size());
		for (EObject object : source) {
			if (filter.apply(object)) {
				result.add(copy((EObject)object, needNewIds));
			}
		}
		return result;
	}

	private boolean isChildrenReference(EReference eReference) {
		return (PlanPackage.Literals.EACTIVITY__CHILDREN == eReference)
			|| (PlanPackage.Literals.EPLAN_PARENT__CHILDREN == eReference);
	}

}
