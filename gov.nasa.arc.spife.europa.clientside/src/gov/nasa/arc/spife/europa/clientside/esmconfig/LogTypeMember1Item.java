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
package gov.nasa.arc.spife.europa.clientside.esmconfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Log Type Member1 Item</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getLogTypeMember1Item()
 * @model extendedMetaData="name='log-type_._member_._1_._item'"
 * @generated
 */
public enum LogTypeMember1Item implements Enumerator {
	/**
	 * The '<em><b>ERROR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ERROR_VALUE
	 * @generated
	 * @ordered
	 */
	ERROR(0, "ERROR", "ERROR"),

	/**
	 * The '<em><b>TRACE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TRACE_VALUE
	 * @generated
	 * @ordered
	 */
	TRACE(1, "TRACE", "TRACE"),

	/**
	 * The '<em><b>TRACEALL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TRACEALL_VALUE
	 * @generated
	 * @ordered
	 */
	TRACEALL(2, "TRACEALL", "TRACE_ALL"),

	/**
	 * The '<em><b>RETURN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RETURN_VALUE
	 * @generated
	 * @ordered
	 */
	RETURN(3, "RETURN", "RETURN"),

	/**
	 * The '<em><b>TIMER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TIMER_VALUE
	 * @generated
	 * @ordered
	 */
	TIMER(4, "TIMER", "TIMER");

	/**
	 * The '<em><b>ERROR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ERROR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ERROR
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ERROR_VALUE = 0;

	/**
	 * The '<em><b>TRACE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TRACE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TRACE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int TRACE_VALUE = 1;

	/**
	 * The '<em><b>TRACEALL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TRACEALL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TRACEALL
	 * @model literal="TRACE_ALL"
	 * @generated
	 * @ordered
	 */
	public static final int TRACEALL_VALUE = 2;

	/**
	 * The '<em><b>RETURN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>RETURN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #RETURN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int RETURN_VALUE = 3;

	/**
	 * The '<em><b>TIMER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TIMER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TIMER
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int TIMER_VALUE = 4;

	/**
	 * An array of all the '<em><b>Log Type Member1 Item</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final LogTypeMember1Item[] VALUES_ARRAY =
		new LogTypeMember1Item[] {
			ERROR,
			TRACE,
			TRACEALL,
			RETURN,
			TIMER,
		};

	/**
	 * A public read-only list of all the '<em><b>Log Type Member1 Item</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<LogTypeMember1Item> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Log Type Member1 Item</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LogTypeMember1Item get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LogTypeMember1Item result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Log Type Member1 Item</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LogTypeMember1Item getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LogTypeMember1Item result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Log Type Member1 Item</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LogTypeMember1Item get(int value) {
		switch (value) {
			case ERROR_VALUE: return ERROR;
			case TRACE_VALUE: return TRACE;
			case TRACEALL_VALUE: return TRACEALL;
			case RETURN_VALUE: return RETURN;
			case TIMER_VALUE: return TIMER;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private LogTypeMember1Item(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //LogTypeMember1Item
