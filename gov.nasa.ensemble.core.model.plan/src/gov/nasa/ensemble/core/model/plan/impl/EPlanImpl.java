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
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.core.model.plan.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.collections.DefaultComparator;
import gov.nasa.ensemble.core.model.plan.EDay;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EPlan</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#getRuntimeId <em>Runtime Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#getReferences <em>References</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#isTemplate <em>Template</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#getENamespaceURI <em>ENamespace URI</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#isReadOnly <em>Read Only</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#getModelVersion <em>Model Version</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl#getDays <em>Days</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EPlanImpl extends EPlanElementImpl implements EPlan {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<EPlanChild> children;
	/**
	 * The default value of the '{@link #getRuntimeId() <em>Runtime Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuntimeId()
	 * @generated
	 * @ordered
	 */
	protected static final long RUNTIME_ID_EDEFAULT = 0L;
	/**
	 * The cached value of the '{@link #getReferences() <em>References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> references;
	/**
	 * The default value of the '{@link #isTemplate() <em>Template</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTemplate()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TEMPLATE_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isTemplate() <em>Template</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTemplate()
	 * @generated
	 * @ordered
	 */
	protected static final int TEMPLATE_EFLAG = 1 << 8;
	/**
	 * The default value of the '{@link #getENamespaceURI() <em>ENamespace URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getENamespaceURI()
	 * @generated NOT
	 * @ordered
	 */
	protected static final String ENAMESPACE_URI_EDEFAULT = PlanPackage.eNS_URI;
	/**
	 * The cached value of the '{@link #getENamespaceURI() <em>ENamespace URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getENamespaceURI()
	 * @generated
	 * @ordered
	 */
	protected String eNamespaceURI = ENAMESPACE_URI_EDEFAULT;
	/**
	 * The default value of the '{@link #isReadOnly() <em>Read Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReadOnly()
	 * @generated
	 * @ordered
	 */
	protected static final boolean READ_ONLY_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isReadOnly() <em>Read Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReadOnly()
	 * @generated
	 * @ordered
	 */
	protected static final int READ_ONLY_EFLAG = 1 << 9;

	/**
	 * The default value of the '{@link #getModelVersion() <em>Model Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String MODEL_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModelVersion() <em>Model Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelVersion()
	 * @generated
	 * @ordered
	 */
	protected String modelVersion = MODEL_VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDays() <em>Days</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDays()
	 * @generated
	 * @ordered
	 */
	protected EList<EDay> days;

	private static long runtime_id_counter = 0;
	private long runtimeId = runtime_id_counter++;
	private boolean warnedAboutLogicalResource = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EPlanImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanPackage.Literals.EPLAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<EPlanChild> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<EPlanChild>(EPlanChild.class, this, PlanPackage.EPLAN__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public long getRuntimeId() {
		return runtimeId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<EObject> getReferences() {
		if (references == null) {
			references = new EObjectContainmentEList.Resolving<EObject>(EObject.class, this, PlanPackage.EPLAN__REFERENCES);
		}
		return references;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTemplate() {
		return (eFlags & TEMPLATE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTemplate(boolean newTemplate) {
		boolean oldTemplate = (eFlags & TEMPLATE_EFLAG) != 0;
		if (newTemplate) eFlags |= TEMPLATE_EFLAG; else eFlags &= ~TEMPLATE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN__TEMPLATE, oldTemplate, newTemplate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getENamespaceURI() {
		return eNamespaceURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setENamespaceURI(String newENamespaceURI) {
		String oldENamespaceURI = eNamespaceURI;
		eNamespaceURI = newENamespaceURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN__ENAMESPACE_URI, oldENamespaceURI, eNamespaceURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isReadOnly() {
		return (eFlags & READ_ONLY_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReadOnly(boolean newReadOnly) {
		boolean oldReadOnly = (eFlags & READ_ONLY_EFLAG) != 0;
		if (newReadOnly) eFlags |= READ_ONLY_EFLAG; else eFlags &= ~READ_ONLY_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN__READ_ONLY, oldReadOnly, newReadOnly));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getModelVersion() {
		return modelVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelVersion(String newModelVersion) {
		String oldModelVersion = modelVersion;
		modelVersion = newModelVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN__MODEL_VERSION, oldModelVersion, modelVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EDay> getDays() {
		if (days == null) {
			days = new EObjectContainmentEList.Resolving<EDay>(EDay.class, this, PlanPackage.EPLAN__DAYS);
		}
		return days;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EDay getEDay(String dayID) {
		for (EDay day : getDays()) {
			if (CommonUtils.equals(dayID, day.getDate())) {
				return day;
			}
		}
		EDay day = PlanFactory.eINSTANCE.createEDay();
		day.setDate(dayID);
		return day;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setEDayNotes(EDay day, String note) {
		day.setNotes(note);
		getDays().add(day);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PlanPackage.EPLAN__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
			case PlanPackage.EPLAN__REFERENCES:
				return ((InternalEList<?>)getReferences()).basicRemove(otherEnd, msgs);
			case PlanPackage.EPLAN__DAYS:
				return ((InternalEList<?>)getDays()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanPackage.EPLAN__CHILDREN:
				return getChildren();
			case PlanPackage.EPLAN__RUNTIME_ID:
				return getRuntimeId();
			case PlanPackage.EPLAN__REFERENCES:
				return getReferences();
			case PlanPackage.EPLAN__TEMPLATE:
				return isTemplate();
			case PlanPackage.EPLAN__ENAMESPACE_URI:
				return getENamespaceURI();
			case PlanPackage.EPLAN__READ_ONLY:
				return isReadOnly();
			case PlanPackage.EPLAN__MODEL_VERSION:
				return getModelVersion();
			case PlanPackage.EPLAN__DAYS:
				return getDays();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PlanPackage.EPLAN__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends EPlanChild>)newValue);
				return;
			case PlanPackage.EPLAN__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection<? extends EObject>)newValue);
				return;
			case PlanPackage.EPLAN__TEMPLATE:
				setTemplate((Boolean)newValue);
				return;
			case PlanPackage.EPLAN__ENAMESPACE_URI:
				setENamespaceURI((String)newValue);
				return;
			case PlanPackage.EPLAN__READ_ONLY:
				setReadOnly((Boolean)newValue);
				return;
			case PlanPackage.EPLAN__MODEL_VERSION:
				setModelVersion((String)newValue);
				return;
			case PlanPackage.EPLAN__DAYS:
				getDays().clear();
				getDays().addAll((Collection<? extends EDay>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PlanPackage.EPLAN__CHILDREN:
				getChildren().clear();
				return;
			case PlanPackage.EPLAN__REFERENCES:
				getReferences().clear();
				return;
			case PlanPackage.EPLAN__TEMPLATE:
				setTemplate(TEMPLATE_EDEFAULT);
				return;
			case PlanPackage.EPLAN__ENAMESPACE_URI:
				setENamespaceURI(ENAMESPACE_URI_EDEFAULT);
				return;
			case PlanPackage.EPLAN__READ_ONLY:
				setReadOnly(READ_ONLY_EDEFAULT);
				return;
			case PlanPackage.EPLAN__MODEL_VERSION:
				setModelVersion(MODEL_VERSION_EDEFAULT);
				return;
			case PlanPackage.EPLAN__DAYS:
				getDays().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PlanPackage.EPLAN__CHILDREN:
				return children != null && !children.isEmpty();
			case PlanPackage.EPLAN__RUNTIME_ID:
				return getRuntimeId() != RUNTIME_ID_EDEFAULT;
			case PlanPackage.EPLAN__REFERENCES:
				return references != null && !references.isEmpty();
			case PlanPackage.EPLAN__TEMPLATE:
				return ((eFlags & TEMPLATE_EFLAG) != 0) != TEMPLATE_EDEFAULT;
			case PlanPackage.EPLAN__ENAMESPACE_URI:
				return ENAMESPACE_URI_EDEFAULT == null ? eNamespaceURI != null : !ENAMESPACE_URI_EDEFAULT.equals(eNamespaceURI);
			case PlanPackage.EPLAN__READ_ONLY:
				return ((eFlags & READ_ONLY_EFLAG) != 0) != READ_ONLY_EDEFAULT;
			case PlanPackage.EPLAN__MODEL_VERSION:
				return MODEL_VERSION_EDEFAULT == null ? modelVersion != null : !MODEL_VERSION_EDEFAULT.equals(modelVersion);
			case PlanPackage.EPLAN__DAYS:
				return days != null && !days.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (template: ");
		result.append((eFlags & TEMPLATE_EFLAG) != 0);
		result.append(", eNamespaceURI: ");
		result.append(eNamespaceURI);
		result.append(", readOnly: ");
		result.append((eFlags & READ_ONLY_EFLAG) != 0);
		result.append(", modelVersion: ");
		result.append(modelVersion);
		result.append(')');
		return result.toString();
	}

	private DefaultComparator<EObject> referenceComparator = new DefaultComparator<EObject>();

	public void addReferencedObjects(List<? extends EObject> objects) {
		if (objects.size() == 1) {
			EObject object = objects.get(0);
			addReferencedObject(object);
			return;
		}
		EList<EObject> references = getReferences();
		references.addAll(objects);
		ECollections.sort(references, referenceComparator);
	}

	public void addReferencedObject(EObject object) {
		List<EObject> references = getReferences();
		if (references.contains(object)) {
			return;
		}
		ListIterator<EObject> iterator = references.listIterator();
		while (iterator.hasNext()) {
			EObject next = iterator.next();
			int result = referenceComparator.compare(next, object);
			if (result == 0) {
				// already present, skip
				return;
			}
			if (result > 0) {
				// we should insert before this element.
				// back up the iterator so that the add will occur there.
				iterator.previous();
				break;
			}
		}
		iterator.add(object);
	}

	public void removeReferencedObjects(List<? extends EObject> objects) {
		getReferences().removeAll(objects);
	}

	public void removeReferencedObject(EObject object) {
		getReferences().remove(object);
	}

	@Override
	public Object getAdapter(Class type) {
		if (IResource.class.isAssignableFrom(type)) {
			Resource resource = eResource();
			if (resource == null) {
				return null;
			}
			URI uri = resource.getURI();
			ResourceSet resourceSet = resource.getResourceSet();
			if (resourceSet==null) return super.getAdapter(type);
			uri = resourceSet.getURIConverter().normalize(uri);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			if (uri.isPlatform()) {
				String platformResourceString = uri.toPlatformString(true);
				if (platformResourceString == null && resource.getURI().toFileString() != null) {
					IFile file = root.getFileForLocation(new Path(resource.getURI().toFileString()));
					if (file != null && file.exists()) {
						return file;
					}
				}
			    return platformResourceString != null ?
					      root.getFile(new Path(platformResourceString)) :
					      null;
			}
			if (uri.isFile()) {
				String fileString = uri.toFileString();
				java.io.File javaFile = new java.io.File(fileString);
				IFile file = getIFile(javaFile);
				return file;
			}
		}
		return super.getAdapter(type);
	}


	private IFile getIFile(java.io.File file) {
		String root = ResourcesPlugin.getWorkspace().getRoot().getLocationURI()
				.getPath();
		IPath path = new Path(file.toURI().getPath().replaceAll(root, ""));

		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		if(!iFile.exists() && file.exists() && !warnedAboutLogicalResource) {
			Logger.getLogger(EPlanImpl.class).warn("file exists, but outside of workspace!");
			warnedAboutLogicalResource = true;
		}
		return iFile;
	}
	
} //EPlanImpl
