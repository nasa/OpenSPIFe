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
package gov.nasa.ensemble.core.jscience;

import java.util.Collection;
import java.util.Date;

import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EResource Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#isExternalCondition <em>External Condition</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getMinLiteral <em>Min Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getMaxLiteral <em>Max Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getInterpolation <em>Interpolation</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#isValid <em>Valid</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getDataType <em>Data Type</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.Profile#getDataPoints <em>Data Points</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile()
 * @model
 * @generated
 */
public interface Profile<T> extends EObject {
	/*
	public static final String UNITS_ATTRIBUTE = "units";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String ID_ATTRIBUTE = "id";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String DATATYPE_ATTRIBUTE = "datatype";
	*/
	
	/**<!-- begin-user-doc -->
	 * An attribute that may be set on a Profile that will be parsed and passed to the Plot as a color.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public static final String RGB_ATTRIBUTE = "color";

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' attribute.
	 * @see #setCategory(String)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Category()
	 * @model
	 * @generated
	 */
	String getCategory();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getCategory <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category</em>' attribute.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(String value);

	/**
	 * Returns the value of the '<em><b>External Condition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>External Condition</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>External Condition</em>' attribute.
	 * @see #setExternalCondition(boolean)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_ExternalCondition()
	 * @model
	 * @generated
	 */
	boolean isExternalCondition();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#isExternalCondition <em>External Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>External Condition</em>' attribute.
	 * @see #isExternalCondition()
	 * @generated
	 */
	void setExternalCondition(boolean value);

	/**
	 * Returns the value of the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Valid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Valid</em>' attribute.
	 * @see #setValid(boolean)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Valid()
	 * @model
	 * @generated
	 */
	boolean isValid();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#isValid <em>Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Valid</em>' attribute.
	 * @see #isValid()
	 * @generated
	 */
	void setValid(boolean value);

	/**
	 * Returns the value of the '<em><b>Data Points</b></em>' attribute list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.jscience.DataPoint}&lt;T>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Points</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Points</em>' attribute list.
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_DataPoints()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDataPoint<T>"
	 * @generated
	 */
	EList<DataPoint<T>> getDataPoints();

	/**
	 * Returns the value of the '<em><b>Min Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Literal</em>' attribute.
	 * @see #setMinLiteral(String)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_MinLiteral()
	 * @model annotation="detail shortDescription='.units'"
	 * @generated
	 */
	String getMinLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getMinLiteral <em>Min Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Literal</em>' attribute.
	 * @see #getMinLiteral()
	 * @generated
	 */
	void setMinLiteral(String value);

	/**
	 * Returns the value of the '<em><b>Max Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Literal</em>' attribute.
	 * @see #setMaxLiteral(String)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_MaxLiteral()
	 * @model annotation="detail shortDescription='.units'"
	 * @generated
	 */
	String getMaxLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getMaxLiteral <em>Max Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Literal</em>' attribute.
	 * @see #getMaxLiteral()
	 * @generated
	 */
	void setMaxLiteral(String value);

	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes</em>' map.
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Attributes()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 *        annotation="detail table='true'"
	 * @generated
	 */
	EMap<String, String> getAttributes();

	/**
	 * Returns the value of the '<em><b>Data Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Type</em>' reference.
	 * @see #setDataType(EDataType)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_DataType()
	 * @model
	 * @generated
	 */
	EDataType getDataType();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getDataType <em>Data Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Type</em>' reference.
	 * @see #getDataType()
	 * @generated
	 */
	void setDataType(EDataType value);

	/**
	 * Returns the value of the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extent</em>' attribute.
	 * @see #setExtent(AmountExtent)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Extent()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EAmountExtent"
	 * @generated
	 */
	AmountExtent<?> getExtent();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getExtent <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extent</em>' attribute.
	 * @see #getExtent()
	 * @generated
	 */
	void setExtent(AmountExtent<?> value);

	/**
	 * Returns the value of the '<em><b>Interpolation</b></em>' attribute.
	 * The default value is <code>"STEP"</code>.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.core.jscience.INTERPOLATION}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interpolation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interpolation</em>' attribute.
	 * @see gov.nasa.ensemble.core.jscience.INTERPOLATION
	 * @see #setInterpolation(INTERPOLATION)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Interpolation()
	 * @model default="STEP"
	 * @generated
	 */
	INTERPOLATION getInterpolation();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getInterpolation <em>Interpolation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interpolation</em>' attribute.
	 * @see gov.nasa.ensemble.core.jscience.INTERPOLATION
	 * @see #getInterpolation()
	 * @generated
	 */
	void setInterpolation(INTERPOLATION value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Units</em>' attribute.
	 * @see #setUnits(Unit)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_Units()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EUnit"
	 * @generated
	 */
	Unit<?> getUnits();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getUnits <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Units</em>' attribute.
	 * @see #getUnits()
	 * @generated
	 */
	void setUnits(Unit<?> value);

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #setDefaultValue(Object)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getProfile_DefaultValue()
	 * @model
	 * @generated
	 */
	Object getDefaultValue();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.Profile#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(Object value);

	T getValue(Date time);
	
	DataPoint<T> getDataPoint(Date time);
	
	void setDataPointsArray(DataPoint<T>[] dataPoints);
	
    void setDataPoints(Collection<DataPoint<T>> dataPoints);
	
	void updateExtremes();

} // EResourceProfile
