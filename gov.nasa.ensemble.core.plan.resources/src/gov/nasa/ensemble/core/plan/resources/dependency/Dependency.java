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
package gov.nasa.ensemble.core.plan.resources.dependency;

import gov.nasa.ensemble.core.jscience.JScienceFactory;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getNext <em>Next</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getPrevious <em>Previous</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getValue <em>Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#isValid <em>Valid</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage#getDependency()
 * @model abstract="true"
 * @generated
 */
public interface Dependency extends EObject {
	
	public static final JScienceFactory JSCIENCE_FACTORY = JScienceFactory.eINSTANCE;
	
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage#getDependency_Name()
	 * @model changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	String getName();

	/**
	 * Returns the value of the '<em><b>Next</b></em>' reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getPrevious <em>Previous</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next</em>' reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage#getDependency_Next()
	 * @see gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getPrevious
	 * @model opposite="previous"
	 * @generated
	 */
	List<Dependency> getNext();

	/**
	 * Returns the value of the '<em><b>Previous</b></em>' reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getNext <em>Next</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Previous</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Previous</em>' reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage#getDependency_Previous()
	 * @see gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getNext
	 * @model opposite="next"
	 * @generated
	 */
	List<Dependency> getPrevious();

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage#getDependency_Value()
	 * @model transient="true" derived="true"
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

	/**
	 * Returns the value of the '<em><b>Valid</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Valid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Valid</em>' attribute.
	 * @see #setValid(boolean)
	 * @see gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage#getDependency_Valid()
	 * @model default="false"
	 * @generated
	 */
	boolean isValid();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.dependency.Dependency#isValid <em>Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Valid</em>' attribute.
	 * @see #isValid()
	 * @generated
	 */
	void setValid(boolean value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean update();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void applyValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void invalidate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void dispose();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean shouldUpdateIfRemoved();

} // Dependency
