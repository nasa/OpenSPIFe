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
package gov.nasa.ensemble.core.model.plan.activityDictionary.util;

import fj.data.Option;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityGroupDef;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;

public class ADParameterUtils {
	/**
	 * Used to find case-insensitive values for EEnum type literals.
	 * 
	 * Map of EEnum type to Map of uppercase literal
	 */
	private static Map<EEnum, Map<String, EEnumLiteral>> literalValues = new HashMap<EEnum, Map<String,EEnumLiteral>>();

	public static EActivityDef getActivityDef(EActivity activity) {
		EObject object = activity.getData();
		if (object != null) {
			EClass eClass = object.eClass();
			if (eClass instanceof EActivityDef) {
				return (EActivityDef) eClass;
			}
			for (EClass eSupertype : eClass.getESuperTypes()) {
				if (eSupertype instanceof EActivityDef) {
					return (EActivityDef) eSupertype;
				}
			}
			return ActivityDictionary.getInstance().getActivityDef(object.eClass().getName());
		}
		return null;
	}
	
	public static EActivityGroupDef getActivityGroupDef(EActivityGroup activityGroup) {
		EObject object = activityGroup.getData();
		if (object != null) {
			EClass eClass = object.eClass();
			if (eClass instanceof EActivityGroupDef) {
				return (EActivityGroupDef) eClass;
			}
			for (EClass eSupertype : eClass.getESuperTypes()) {
				if (eSupertype instanceof EActivityGroupDef) {
					return (EActivityGroupDef) eSupertype;
				}
			}
			return ActivityDictionary.getInstance().getActivityGroupDef();
		}
		return null;
	}
	
	/**
	 * Returns true if the supplied notifier is a data for a plan element (parameter change),
	 * or a plan element itself (attribute change).
	 * 
	 * @param notifier
	 * @return
	 */
	public static boolean isActivityAttributeOrParameter(Object notifier) {
		return isAttributeNotifier(notifier) || isParameterNotifier(notifier);
	}
	
	public static boolean isActivityGroupAttributeOrParameter(Object notifier) {
		return notifier instanceof EActivityGroup 
				|| getPlanElementFromParameterNotifier(notifier, EActivityGroup.class, EActivityGroupDef.class) != null;
	}
	
	/**
	 * Returns the plan element if the supplied notifier is a data for a plan element (parameter change),
	 * or a plan element itself (attribute change); otherwise dalse.
	 * 
	 * @param notifier
	 * @return
	 */
	public static EActivity getActivityAttributeOrParameter(Object notifier) {
		return getPlanElementGroupAttributeOrParameter(notifier, EActivity.class, EActivityDef.class);
	}
	
	public static EActivityGroup getActivityGroupAttributeOrParameter(Object notifier) {
		return getPlanElementGroupAttributeOrParameter(notifier, EActivityGroup.class, EActivityGroupDef.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends EPlanElement, C> T getPlanElementGroupAttributeOrParameter(Object notifier, Class<T> planElementType, Class<C> defType) {
		if (planElementType.isAssignableFrom(notifier.getClass())) {
			return (T)notifier;
		} else {
			return getPlanElementFromParameterNotifier(notifier, planElementType, defType);
		}
	}
	
	private static boolean isAttributeNotifier(Object notifier) {
		return notifier instanceof EActivity;
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends EPlanElement, C> T getPlanElementFromParameterNotifier(Object notifier, Class<T> planElementType, Class<C> defType) {
		if (notifier instanceof EObject
				&& defType.isAssignableFrom(((EObject)notifier).eClass().getClass())) {
			EObject object = (EObject) notifier;
			EObject container = object.eContainer();
			if (planElementType.isAssignableFrom(container.getClass())) {
				T element = ((T) container);
				if (element.getData() == object) {
					return element;
				}
			}
		}
		return null;
	}

	public static boolean isParameterNotifier(Object notifier) {
		return getPlanElementFromParameterNotifier(notifier, EActivity.class, EActivityDef.class) != null;
	}
	
	/**
	 * Get the value of the named AD parameter for the activity
	 * 
	 * @param element
	 * @param parameterName
	 * @return
	 * @throws UndefinedParameterException
	 */
	@SuppressWarnings("unchecked")
    public static <T> T getParameterObject(EPlanElement element, String parameterName) throws UndefinedParameterException {
		EObject data = element.getData();
		if (data==null) throw new UndefinedParameterException("Null data for plan element " + element);
		EStructuralFeature feature = getParameterFeature(data, parameterName);
		return (T)data.eGet(feature);
	}
	
	
	/**
	 * Get the value of the AD parameter by type for the activity
	 * 
	 * @param element
	 * @param parameterType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Option<T> getParameterObjectByType(EPlanElement element, EClass parameterType) {
		EObject data = element.getData();
		EList<EStructuralFeature> structuralFeatures = data.eClass().getEStructuralFeatures();
		for (int i = 0; i<structuralFeatures.size(); i++) {
			final EStructuralFeature feature = structuralFeatures.get(i);
			if (parameterType == feature.getEType())
				return Option.fromNull((T) data.eGet(feature));
		}
		return Option.none();
	}
	
	/**
	 * Returns whether activity contains AD parameter of given type
	 * 
	 * @param element
	 * @param parameterType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isParameterForType(EPlanElement element, EClass parameterType) {
		EObject data = element.getData();
		EList<EStructuralFeature> structuralFeatures = data.eClass().getEStructuralFeatures();
		for (int i = 0; i<structuralFeatures.size(); i++) {
			final EStructuralFeature feature = structuralFeatures.get(i);
			if (parameterType == feature.getEType())
				return true;
		}
		return false;
	}

	public static boolean implementsInterface(Object object, Class interf){
	    return interf.isInstance(object);
	}
	
	/**
	 * Set the value of the named AD parameter for the activity
	 * @param element
	 * @param parameterName
	 * @param object
	 * @throws UndefinedParameterException
	 */
	public static void setParameterObject(EPlanElement element, String parameterName, Object newValue) throws UndefinedParameterException {
		EObject data = element.getData();
		EStructuralFeature feature = getParameterFeature(data, parameterName);
		data.eSet(feature, newValue);
	}
	
	/**
	 * Set the value of the named AD parameter for the activity without throwing any errors
	 * @param element
	 * @param parameterName
	 * @param object
	 */
	public static void setParameterObjectSafely(EPlanElement element, String parameterName, Object newValue) {
		if(parameterName == null || newValue == null) {
			return;
		}
		try {
			setParameterObject(element, parameterName, newValue);
		} catch (UndefinedParameterException e) {
			Logger logger = Logger.getLogger(ADParameterUtils.class);
			logger.error("parameter '" + parameterName + "'is not a valid parameter.");
		}
	}
	
	/**
	 * Get the value of the named AD parameter for the activity as a string.
	 * Works with several types, not just strings -- enums, for example.
	 * @param element
	 * @param parameterName
	 * @return null if the parameter is not found
	 */
	public static String getParameterString(EPlanElement element, String parameterName) {
		EObject data = element.getData();
	    if (data==null) return null;
		EStructuralFeature feature;
        try {
	        feature = getParameterFeature(data, parameterName);
        } catch (UndefinedParameterException e) {
	        return null;
        }
		Object object = data.eGet(feature);
		if (object instanceof EEnumLiteral) {
			EEnumLiteral literal = (EEnumLiteral) object;
			return literal.getName();
		}
		EClassifier type = feature.getEType();
		if (type instanceof EDataType) {
			EDataType dataType = (EDataType) type;
			EPackage typePackage = dataType.getEPackage();
			EFactory factory = typePackage.getEFactoryInstance();
			String string = factory.convertToString(dataType, object);
			return string;
		}
		LogUtil.warnOnce("feature type '" + type + "'is not EDataType: " + parameterName);
		return String.valueOf(object);
	}

	
	public static String getObjectString(EObject object, String parameterName) {
		EStructuralFeature feature;
        try {
	        feature = getParameterFeature(object, parameterName);
        } catch (UndefinedParameterException e) {
	        return null;
        }
		Object value = object.eGet(feature);
		if (value instanceof EEnumLiteral) {
			EEnumLiteral literal = (EEnumLiteral) value;
			return literal.getName();
		}
		EClassifier type = feature.getEType();
		if (type instanceof EDataType) {
			EDataType dataType = (EDataType) type;
			EPackage typePackage = dataType.getEPackage();
			EFactory factory = typePackage.getEFactoryInstance();
			String string = factory.convertToString(dataType, value);
			return string;
		}
		LogUtil.warnOnce("feature type '" + type + "'is not EDataType: " + parameterName);
		return String.valueOf(value);
	}
	
	/**
	 * Set the value of the named AD parameter for the activity
	 * @param element
	 * @param parameterName
	 * @param object
	 * @throws UndefinedParameterException
	 */
	public static void setParameterString(EPlanElement element, String parameterName, String newValue) throws UndefinedParameterException {
		setParameterStringInData(element.getData(), parameterName, newValue);
	}
	
	/** Use setParameterString instead, unless calling from an XML loader that has not yet created the plan element. */
	public static void setParameterStringInData(EObject data, String parameterName, String newValue) throws UndefinedParameterException {
		EStructuralFeature feature = getParameterFeature(data, parameterName);
		EClassifier type = feature.getEType();
		Object object;
		if (type instanceof EEnum) {
			EEnum enumType = (EEnum) type;
			object = enumType.getEEnumLiteral(newValue);
		} else if (type instanceof EDataType) {
			EDataType dataType = (EDataType) type;
			EPackage typePackage = dataType.getEPackage();
			EFactory factory = typePackage.getEFactoryInstance();
			object = factory.createFromString(dataType, newValue);
		} else {
			Logger logger = Logger.getLogger(ADParameterUtils.class);
			logger.warn("feature type '" + type + "'is not EDataType: " + parameterName);
			object = newValue;
		}
		data.eSet(feature, object);
	}
	

	/**
	 * Set the value of the named AD parameter for the activity without throwing any errors
	 * @param element
	 * @param parameterName
	 * @param object
	 */
	public static void setParameterStringSafely(EPlanElement element, String parameterName, String newValue) {
		if(parameterName == null || newValue == null) {
			return;
		}
		try {
			setParameterString(element, parameterName, newValue);
		} catch(UndefinedParameterException e) {
			Logger logger = Logger.getLogger(ADParameterUtils.class);
			logger.error("parameter '" + parameterName + "'is not a valid parameter.");
		}
	}
	
	/**
	 * If the parameter is an EEnum, it is set to the first available literal which equalsIgnoreCase matches newValue
	 * 
	 * @param element
	 * @param parameterName
	 * @param newValue
	 * @return The value the parameter was set to, in its proper case.
	 * @throws UndefinedParameterException
	 */
	public static String setParameterStringCaseInsensitive(EPlanElement element, String parameterName, String newValue) throws UndefinedParameterException {
		EObject data = element.getData();
		EStructuralFeature feature = getParameterFeature(data, parameterName);
		EClassifier type = feature.getEType();
		if (type instanceof EEnum) {
			EEnum enumType = (EEnum) type;
			EEnumLiteral literalValue = getCaseInsensitiveValue(enumType, newValue);
			data.eSet(feature, literalValue);
			return literalValue == null ? null : literalValue.getLiteral();
		} else {
			setParameterString(element, parameterName, newValue);
			return newValue;
		}
	}

	public static String setParameterStringCaseInsensitiveSafely(EPlanElement element, String parameterName, String newValue) {
		try {
			return setParameterStringCaseInsensitive(element, parameterName, newValue);
		} catch (UndefinedParameterException e) {
			LogUtil.error(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static void addParameterObjectToList(EPlanElement element, String parameterName, Object object) throws UndefinedParameterException {
		EObject data = element.getData();
		EStructuralFeature feature = getParameterFeature(data, parameterName);
		Object value = data.eGet(feature);
		if(!(value instanceof EList)) {
			Logger logger = Logger.getLogger(ADParameterUtils.class);
			logger.warn("feature value '" + value + "' is not EList: " + parameterName);
			return;
		}
		EList list = (EList) value;
		if(object != null && !list.contains(object)) {
			list.add(object);
		}
	}
	
	/**
	 * Add a value to a named AD multi-select EEnum parameter for the activity
	 * @param element
	 * @param parameterName
	 * @param newValue
	 * @throws UndefinedParameterException
	 */
	@SuppressWarnings("unchecked")
	public static void addParameterStringToList(EPlanElement element, String parameterName, String newValue) throws UndefinedParameterException {
		EObject data = element.getData();
		EStructuralFeature feature = getParameterFeature(data, parameterName);
		EClassifier type = feature.getEType();
		Object object;
		if (type instanceof EEnum) {
			EEnum enumType = (EEnum) type;
			object = enumType.getEEnumLiteral(newValue);
		} else if (type instanceof EDataType) {
			EDataType dataType = (EDataType) type;
			EPackage typePackage = dataType.getEPackage();
			EFactory factory = typePackage.getEFactoryInstance();
			object = factory.createFromString(dataType, newValue);
		} else {
			Logger logger = Logger.getLogger(ADParameterUtils.class);
			logger.warn("feature type '" + type + "'is not EDataType: " + parameterName);
			object = newValue;
		}
		addParameterObjectToList(element, parameterName, object);
	}

	/**
	 * Add a value to a named AD multi-select EEnum parameter for the activity without throwing any errors
	 * @param element
	 * @param parameterName
	 * @param newValue
	 */
	public static void addParameterStringToListSafely(EPlanElement element, String parameterName, String newValue) {
		if(parameterName == null || newValue == null) {
			return;
		}
		try {
			addParameterStringToList(element, parameterName, newValue);
		} catch(UndefinedParameterException e) {
			Logger logger = Logger.getLogger(ADParameterUtils.class);
			logger.error("parameter '" + parameterName + "'is not a valid parameter.");
		}
	}

	public static String addParameterStringToListCaseInsensitive(EPlanElement element, String parameterName, String newValue) throws UndefinedParameterException {
		EObject data = element.getData();
		EStructuralFeature feature = getParameterFeature(data, parameterName);
		EClassifier type = feature.getEType();
		if (type instanceof EEnum) {
			EEnum enumType = (EEnum) type;
			EEnumLiteral literal = getCaseInsensitiveValue(enumType, newValue);
			addParameterObjectToList(element, parameterName, literal);
			return literal == null ? null : literal.getLiteral();
		} else {
			addParameterStringToList(element, parameterName, newValue);
			return newValue;
		}
	}
	
	public static String addParameterStringToListCaseInsensitiveSafely(EPlanElement element, String parameterName, String newValue) {
		try {
			return addParameterStringToListCaseInsensitive(element, parameterName, newValue);
		} catch (UndefinedParameterException e) {
			LogUtil.error(e);
			return null;
		}
	}
	
	/**
	 * Add a reference to a named AD 
	 * @param element
	 * @param referenceURI
	 * @param referenceID
	 * @param attribute
	 */
	@SuppressWarnings("unchecked")
	public static void addReference(EPlanElement element, URI referenceURI, String referenceID, String attribute) {
		if (referenceURI != null) {
			EObject data = element.getData();
			EStructuralFeature feature = data.eClass().getEStructuralFeature(attribute);
			EClass referenceClass = (EClass) feature.getEType();
			List groupReferences = (List)data.eGet(data.eClass().getEStructuralFeature(attribute));

			URI uri = referenceURI.appendFragment(referenceID);
			EObject groupObject = ActivityDictionary.getInstance().getEFactoryInstance().create(referenceClass);
			((BasicEObjectImpl)groupObject).eSetProxyURI(uri);
			if (!groupReferences.contains(groupObject)) {
				groupReferences.add(groupObject);
			}
		}
	}
	
	public static List getReferences(EPlanElement element, String attribute) throws UndefinedParameterException {
		EObject data = element.getData();
		if (data == null) {
			return null;
		}
			
		EStructuralFeature feature = getParameterFeature(data, attribute);
		List groupReferences = (List)data.eGet(feature);
		return groupReferences;
	}
	
	/**
	 * Get the feature for the named parameter
	 * 
	 * @param element
	 * @param name
	 * @return
	 * @throws UndefinedParameterException
	 */
	public static EStructuralFeature getParameterFeature(EPlanElement element, String name) throws UndefinedParameterException {
		EObject data = element.getData();
		return getParameterFeature(data, name);
	}

	
	/**
	 * Get the feature for the named parameter
	 * 
	 * @param data
	 * @param name
	 * @return
	 * @throws UndefinedParameterException
	 */
	public static EStructuralFeature getParameterFeature(EObject data, String name) throws UndefinedParameterException {
		if (data==null) {
			throw new UndefinedParameterException("No activity definition");
		}
		EClass eClass = data.eClass();
		EStructuralFeature feature = eClass.getEStructuralFeature(name);
		if (feature == null) {
			Resource resource = data.eResource();
			String info = (resource != null ? " in resource: " + resource.getURI() : "");
			throw new UndefinedParameterException(name + info);
		}
		return feature;
	}

	private static Map<String, EEnumLiteral> cacheLiterals(EEnum enumType) {
		Map<String, EEnumLiteral> map = new HashMap<String, EEnumLiteral>();
		EList<EEnumLiteral> literals = enumType.getELiterals();
		for (EEnumLiteral literal : literals) {
			String literalString = literal.getLiteral();
			map.put(literalString.toUpperCase(), literal);
		}
		literalValues.put(enumType, map);
		return map;
	}
	
	public static EEnumLiteral getCaseInsensitiveValue(EEnum enumType, String value) {
		Map<String, EEnumLiteral> map = literalValues.get(enumType);
		if (map == null) {
			map = cacheLiterals(enumType);
		}
		return map.get(value.toUpperCase());
	}
	
	public static Unit getUnits(EStructuralFeature feature) {
		Unit<?> unit = null;
		if (feature instanceof EAttributeParameter) {
			unit = ((EAttributeParameter)feature).getUnits();
		} else {
			String unitString = EMFUtils.getAnnotation(feature, "parameter", "unit");
			if (unitString != null) {
				try {
					unit = EnsembleUnitFormat.INSTANCE.parse(unitString);
				} catch (ParseException e) {
					// ignore, we tried
				}
			}
		}
		return unit;
	}
	
	/**
	 * Exception thrown when a parameter is referenced that isn't defined
	 * 
	 * @author abachmann
	 */
	public static class UndefinedParameterException extends Exception {
		public UndefinedParameterException(String name) {
			super("Undefined parameter: " + name);
		}
	}

}
