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
import gov.nasa.ensemble.common.collections.EncounterOrderComparator;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.IPlanElementMemberFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EPlan Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl#getMembers <em>Members</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl#getDepth <em>Depth</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl#getData <em>Data</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl#getPersistentID <em>Persistent ID</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class EPlanElementImpl extends MinimalEObjectImpl.Container implements EPlanElement {
	
	public static final MembersInvariantAdapter MEMBER_INVARIANT_ADAPTER = new MembersInvariantAdapter();
	
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The cached value of the '{@link #getMembers() <em>Members</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembers()
	 * @generated
	 * @ordered
	 */
	protected EList<EMember> members;
	/**
	 * The default value of the '{@link #getDepth() <em>Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepth()
	 * @generated
	 * @ordered
	 */
	protected static final int DEPTH_EDEFAULT = -1;
	/**
	 * The cached value of the '{@link #getDepth() <em>Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepth()
	 * @generated
	 * @ordered
	 */
	protected int depth = DEPTH_EDEFAULT;
	/**
	 * The cached value of the '{@link #getData() <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getData()
	 * @generated
	 * @ordered
	 */
	protected EObject data;
	/**
	 * The default value of the '{@link #getPersistentID() <em>Persistent ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPersistentID()
	 * @generated
	 * @ordered
	 */
	protected static final String PERSISTENT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPersistentID() <em>Persistent ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPersistentID()
	 * @generated NOT
	 * @ordered
	 */
	protected String persistentID = EcoreUtil.generateUUID();
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected EPlanElementImpl() {
		super();
		initializeMembers();
		eAdapters().add(MEMBER_INVARIANT_ADAPTER);
	}

	protected List<IPlanElementMemberFactory> getMemberFactories() {
		return IPlanElementMemberFactory.FACTORIES;
	}
	
	private void initializeMembers() {
		EList<EMember> members = getMembers();
		EClass eClass = eClass();
		List<IPlanElementMemberFactory> memberFactories = getMemberFactories();
		nextFactory: for (IPlanElementMemberFactory factory : memberFactories) {
			// avoid recreating members already contained in the plan element
			for (EMember member : members) {
				if (factory.getKey().equals(member.getKey())) {
					continue nextFactory;
				}
			}
			EMember member = factory.createMember(eClass);
			if (member != null) {
				String key = member.getKey();
				if (key == factory.getKey()) {
//					member.setPlanElement(element);
					members.add(member);
				} else {
					String message = "key mismatch on member: " + member + " from factory: " + factory;
					Logger.getLogger(EPlanElementImpl.class).error(message);
				}
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanPackage.Literals.EPLAN_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN_ELEMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<EMember> getMembers() {
		if (members == null) {
			members = new EObjectContainmentWithInverseEList.Resolving<EMember>(EMember.class, this, PlanPackage.EPLAN_ELEMENT__MEMBERS, PlanPackage.EMEMBER__PLAN_ELEMENT);
		}
		return members;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getDepth() {
		int count = -1;
		EObject ascendent = this;
		while (ascendent instanceof EPlanElement) {
			count++;
			ascendent = ascendent.eContainer();
		}
		return count;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EObject getData() {
		if (data != null && ((EObject)data).eIsProxy()) {
			InternalEObject oldData = (InternalEObject)data;
			data = (EObject)eResolveProxy(oldData);
			if (data != oldData) {
				InternalEObject newData = (InternalEObject)data;
				EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(newData);
				if (domain != null && domain instanceof AdapterFactoryEditingDomain) {
					AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) domain;
					Resource eResource = data.eResource();
					Map<Resource, Boolean> map = editingDomain.getResourceToReadOnlyMap();
					if (!map.containsKey(eResource))
						map.put(eResource, Boolean.TRUE);
				}
				
				NotificationChain msgs = oldData.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PlanPackage.EPLAN_ELEMENT__DATA, null, null);
				if (newData.eInternalContainer() == null) {
					msgs = newData.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PlanPackage.EPLAN_ELEMENT__DATA, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PlanPackage.EPLAN_ELEMENT__DATA, oldData, data));
			} else {
				LogUtil.error("failed to resolve the data proxy for activity: " + getName());
			}
		}
		return data;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetData() {
		return data;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetData(EObject newData, NotificationChain msgs) {
		EObject oldData = data;
		data = newData;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN_ELEMENT__DATA, oldData, newData);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setData(EObject newData) {
		if (newData != data) {
			NotificationChain msgs = null;
			if (data != null)
				msgs = ((InternalEObject)data).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PlanPackage.EPLAN_ELEMENT__DATA, null, msgs);
			if (newData != null)
				msgs = ((InternalEObject)newData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PlanPackage.EPLAN_ELEMENT__DATA, null, msgs);
			msgs = basicSetData(newData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN_ELEMENT__DATA, newData, newData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPersistentID() {
		return persistentID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPersistentID(String newPersistentID) {
		String oldPersistentID = persistentID;
		persistentID = newPersistentID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN_ELEMENT__PERSISTENT_ID, oldPersistentID, persistentID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean hasMember(Class<EMember> baseClass) {
		return (getMember(baseClass, false) != null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * This could be sped up by making a hashtable at member creation time.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EMember getMember(String key) {
		for (EMember member : getMembers()) {
 			if (key.equals(member.getKey())) {
 				return member;
 			}
 		}
		throw new IllegalStateException("Missing requested member: " + key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public <T extends EMember> T getMember(Class<T> klass) {
		return getMember(klass, true, true);
	}

	private Map<Class<? extends EMember>, EMember> memberByClass = new HashMap<Class<? extends EMember>, EMember>();
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public <T extends EMember> T getMember(Class<T> klass, boolean mustExist) {
		return getMember(klass, mustExist, true);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public <T extends EMember> T getMember(Class<T> klass, boolean mustExist, boolean mustBeUnique) {
		T eMember = klass.cast(memberByClass.get(klass));
		if (memberByClass.containsKey(klass) && (eMember == null)) {
			if (mustExist) {
				throw new IllegalStateException("Missing requested member: " + klass.getCanonicalName());
			} else {
				return null;
			}
		}
		if (eMember == null
				|| this.eIsProxy()
				|| eMember.eContainer() != this) {
			List<T> instances = new ArrayList<T>();
			for (EMember member : getMembers()) {
				if (klass.isInstance(member)) {
					T casted = klass.cast(member);
					instances.add(casted);
				}
			}
			if (mustBeUnique && instances.size() > 1) {
				throw new IllegalArgumentException("Class " + klass.getSimpleName() + 
						" matches more than one EMember: " + instances);
			} else if (instances.isEmpty()) {
				if (mustExist) {
					throw new IllegalStateException("Missing requested member: " + klass.getCanonicalName());
				} else {
					eMember = null;
				}
			} else {
				eMember = instances.get(0);
			}
			memberByClass.put(klass, eMember);
		}
		return eMember;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract List<? extends EPlanChild> getChildren();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PlanPackage.EPLAN_ELEMENT__MEMBERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getMembers()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PlanPackage.EPLAN_ELEMENT__MEMBERS:
				return ((InternalEList<?>)getMembers()).basicRemove(otherEnd, msgs);
			case PlanPackage.EPLAN_ELEMENT__DATA:
				return basicSetData(null, msgs);
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
			case PlanPackage.EPLAN_ELEMENT__NAME:
				return getName();
			case PlanPackage.EPLAN_ELEMENT__MEMBERS:
				return getMembers();
			case PlanPackage.EPLAN_ELEMENT__DEPTH:
				return getDepth();
			case PlanPackage.EPLAN_ELEMENT__DATA:
				if (resolve) return getData();
				return basicGetData();
			case PlanPackage.EPLAN_ELEMENT__PERSISTENT_ID:
				return getPersistentID();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PlanPackage.EPLAN_ELEMENT__NAME:
				setName((String)newValue);
				return;
			case PlanPackage.EPLAN_ELEMENT__DATA:
				setData((EObject)newValue);
				return;
			case PlanPackage.EPLAN_ELEMENT__PERSISTENT_ID:
				setPersistentID((String)newValue);
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
			case PlanPackage.EPLAN_ELEMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PlanPackage.EPLAN_ELEMENT__DATA:
				setData((EObject)null);
				return;
			case PlanPackage.EPLAN_ELEMENT__PERSISTENT_ID:
				setPersistentID(PERSISTENT_ID_EDEFAULT);
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
			case PlanPackage.EPLAN_ELEMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PlanPackage.EPLAN_ELEMENT__MEMBERS:
				return members != null && !members.isEmpty();
			case PlanPackage.EPLAN_ELEMENT__DEPTH:
				return depth != DEPTH_EDEFAULT;
			case PlanPackage.EPLAN_ELEMENT__DATA:
				return data != null;
			case PlanPackage.EPLAN_ELEMENT__PERSISTENT_ID:
				return PERSISTENT_ID_EDEFAULT == null ? persistentID != null : !PERSISTENT_ID_EDEFAULT.equals(persistentID);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();
		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(getName());
		result.append(')');
		return result.toString();
	}
	
	/**
	 * Compare the supplied plan elements.
	 * If both are plans, compare their names.
	 * If both are elements in the same container, compare on their list position
	 * If plan elements have different parents the result 
	 * will be given using the encounter order comparator
	 * @param other the plan element to which this plan element is compared
	 */
	public int compareTo(EPlanElement other) {
		if ((this instanceof EPlan) && (other instanceof EPlan)) {
			String thisName = getName();
			String thatName = other.getName();
			return String.CASE_INSENSITIVE_ORDER.compare(thisName, thatName);
		}
		if (this.eContainer() != other.eContainer()) {
			return EncounterOrderComparator.INSTANCE.compare(this, other);
		}
		if ((this instanceof EPlanChild) && (other instanceof EPlanChild)) {
			EPlanChild child1 = (EPlanChild) this;
			EPlanChild child2 = (EPlanChild) other;
			return child1.getListPosition() - child2.getListPosition();
		}
		Logger logger = Logger.getLogger(EPlanElementImpl.class);
		logger.warn("compareTo: unexpected type of EPlanElement");
		return EncounterOrderComparator.INSTANCE.compare(this, other);
	}
	
	@Override
	public void eNotify(Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		if (!CommonUtils.equals(oldValue, newValue)) {
			super.eNotify(notification);
		}
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		ResourceSet resourceSet = EMFUtils.createResourceSet();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMLResourceFactoryImpl());
		String string = (String)in.readObject();
		ByteArrayInputStream stream = new ByteArrayInputStream(string.getBytes());
		Resource resource = resourceSet.createResource(URI.createURI("EPlanElementImpl_readExternal.xmi"));
		resource.load(stream, null);
		EList<EObject> contents = resource.getContents();
		@SuppressWarnings("unused")
		EObject newThis = contents.get(0);
	}

	/**
	 * Write this plan element as XML to the given destination.
	 * @param out the distination for the XML output
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		String string = EMFUtils.convertToXML(this);
		out.writeObject(string);
	}

	/**
	 * Return the adapter of the given class for this plan element.
	 * @param adapter the desired adapter's class
	 * @return this plan element's adapter of the given class
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return EMFUtils.adapt(this, adapter);
	}

	/** This adapter is used only for logging add and remove events for plan element members. */
	public static final class MembersInvariantAdapter implements Adapter {
		
		/**
		 * Log add and remove events for plan element members
		 * @param notification a description of the change
		 */
		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == PlanPackage.Literals.EPLAN_ELEMENT__MEMBERS) {
				switch (notification.getEventType()) {
				case Notification.ADD:
				case Notification.ADD_MANY:
					LogUtil.error("members added");
					break;
				case Notification.REMOVE:
				case Notification.REMOVE_MANY:
					LogUtil.error("members removed");
				}
			}
		}

		public Notifier getTarget() 					{ return null;  }
		public void setTarget(Notifier newTarget) 		{ /* no impl */ }
		public boolean isAdapterForType(Object type) 	{ return false; }
	}
	
} //EPlanElementImpl
