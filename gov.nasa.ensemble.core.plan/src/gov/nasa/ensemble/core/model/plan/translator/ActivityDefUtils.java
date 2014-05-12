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
package gov.nasa.ensemble.core.model.plan.translator;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

public class ActivityDefUtils {

	private static final ActivityDefUtils instance = new ActivityDefUtils();
	
	private final EPackage wrapperPackage = EcoreFactory.eINSTANCE.createEPackage();
	private final Map<String, EDataType> typeStringToEDataType = new HashMap<String, EDataType>();

	public static ActivityDefUtils getInstance() {
		return instance;
	}
	
	public ActivityDefUtils() {
		wrapperPackage.setName("TranslatorWrapper");
		wrapperPackage.setNsPrefix("wrapper");
		String uri = "http:///wrapper";
		wrapperPackage.setNsURI(uri);
		EPackage.Registry.INSTANCE.put(uri, wrapperPackage);
	}
	
	public EDataType getDataType(String type) {
		if (type.equalsIgnoreCase("string")) {
			return EcorePackage.Literals.ESTRING;
		}
		if (type.equalsIgnoreCase("boolean")) {
			return EcorePackage.Literals.EBOOLEAN_OBJECT;
		}
		if (type.equalsIgnoreCase("integer")) {
			return EcorePackage.Literals.EINTEGER_OBJECT;
		}
		if (type.equalsIgnoreCase("float")) {
			return EcorePackage.Literals.EFLOAT_OBJECT;
		}
		if (type.equalsIgnoreCase("long")) {
			return EcorePackage.Literals.ELONG_OBJECT;
		}
		if (type.equalsIgnoreCase("double")) {
			return EcorePackage.Literals.EDOUBLE_OBJECT;
		}
		if (type.equalsIgnoreCase("date")) {
			return EcorePackage.Literals.EDATE;
		}
		if (type.equalsIgnoreCase("color")) {
			return PlanPackage.Literals.ECOLOR;
		}
		if (type.equalsIgnoreCase("CALCULATED_VARIABLE")) {
			return TemporalPackage.Literals.CALCULATED_VARIABLE;
		}
		type = type.toUpperCase();
		EDataType eDataType = typeStringToEDataType.get(type);
		if (eDataType == null) {
			eDataType = EcoreFactory.eINSTANCE.createEDataType();
			eDataType.setName(type);
			eDataType.setInstanceClass(Object.class);
			Logger.getLogger(ActivityDefUtils.class).info("createDataType: " + type);
			wrapperPackage.getEClassifiers().add(eDataType);
			typeStringToEDataType.put(type, eDataType);
		}
		return eDataType;
	}
	
	public String getType(EClassifier eDataType) {
		if (eDataType == EcorePackage.Literals.ESTRING) {
			return "STRING";
		}
		if (eDataType == EcorePackage.Literals.EBOOLEAN_OBJECT) {
			return "BOOLEAN";
		}
		if (eDataType == EcorePackage.Literals.EBOOLEAN) {
			return "BOOLEAN";
		}
		if (eDataType == EcorePackage.Literals.EINTEGER_OBJECT) {
			return "INTEGER";
		}
		if (eDataType == EcorePackage.Literals.EINT) {
			return "INTEGER";
		}
		if (eDataType == EcorePackage.Literals.EFLOAT_OBJECT) {
			return "FLOAT";
		}
		if (eDataType == EcorePackage.Literals.EFLOAT) {
			return "FLOAT";
		}
		if (eDataType == EcorePackage.Literals.ELONG_OBJECT) {
			return "LONG";
		}
		if (eDataType == EcorePackage.Literals.ELONG) {
			return "LONG";
		}
		if (eDataType == EcorePackage.Literals.EDOUBLE_OBJECT) {
			return "DOUBLE";
		}
		if (eDataType == EcorePackage.Literals.EDOUBLE) {
			return "DOUBLE";
		}
		if (eDataType == EcorePackage.Literals.EDATE) {
			return "DATE";
		}
		if (eDataType == PlanPackage.Literals.ECOLOR) {
			return "COLOR";
		}
		if (eDataType == TemporalPackage.Literals.CALCULATED_VARIABLE) {
			return "CALCULATED_VARIABLE";
		}
		EAnnotation hibernateAnnotation = eDataType.getEAnnotation("hibernate");
		if (hibernateAnnotation != null) {
			return hibernateAnnotation.getDetails().get("parameterType");
		}
		String name = eDataType.getName();
		if (!typeStringToEDataType.containsKey(name)) {
			LogUtil.warnOnce("unknown type: " + eDataType);
		}
		return name;
	}

}
