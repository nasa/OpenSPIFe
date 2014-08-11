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
package gov.nasa.ensemble.emf.model.patch.util;

import gov.nasa.ensemble.emf.model.patch.ObjectChanges;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.model.patch.PatchListChange;
import gov.nasa.ensemble.emf.model.patch.PatchPackage;
import gov.nasa.ensemble.emf.model.patch.PatchResourceChange;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage
 * @generated
 */
public class PatchAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static PatchPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = PatchPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This implementation returns <code>true</code> if the object is either the model's package or is an
	 * instance object of the model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PatchSwitch<Adapter> modelSwitch = new PatchSwitch<Adapter>() {
		@Override
		public Adapter casePatch(Patch object) {
			return createPatchAdapter();
		}

		@Override
		public Adapter caseObjectChanges(ObjectChanges object) {
			return createObjectChangesAdapter();
		}

		@Override
		public Adapter casePatchFeatureChange(PatchFeatureChange object) {
			return createPatchFeatureChangeAdapter();
		}

		@Override
		public Adapter casePatchListChange(PatchListChange object) {
			return createPatchListChangeAdapter();
		}

		@Override
		public Adapter casePatchResourceChange(PatchResourceChange object) {
			return createPatchResourceChangeAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.emf.model.patch.Patch <em>Patch</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.emf.model.patch.Patch
	 * @generated
	 */
	public Adapter createPatchAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.emf.model.patch.ObjectChanges <em>Object Changes</em>}'. <!-- begin-user-doc --> This default implementation returns null
	 * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.emf.model.patch.ObjectChanges
	 * @generated
	 */
	public Adapter createObjectChangesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange <em>Feature Change</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange
	 * @generated
	 */
	public Adapter createPatchFeatureChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange <em>List Change</em>}'. <!-- begin-user-doc --> This default implementation returns null
	 * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange
	 * @generated
	 */
	public Adapter createPatchListChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange <em>Resource Change</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchResourceChange
	 * @generated
	 */
	public Adapter createPatchResourceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This default implementation returns null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // PatchAdapterFactory
