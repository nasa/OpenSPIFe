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
package gov.nasa.ensemble.core.model.plan.diff.top;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleOption;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingNewElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameterOrReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByMovingChild;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedConstraintOrProfile;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange;
import gov.nasa.ensemble.core.model.plan.diff.impl.ReferenceAndEObject;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class PlanDiffUtils {
	
	/**
	 * Tries various ways of getting the name of an EMF object.
	 * @param object -- object whose name to get
	 * @return name or null
	 */
	static public String getObjectNameOrNull(EObject object) {
		if (object==null) return null;
		else return EPlanUtils.getDisplayName("name", object, null);
	}

	/**
	 * Tries various ways of getting the name of an EMF object.
	 * @param object -- object whose name to get
	 * @return a string in all cases, unless object itself is null.
	 */
	static public String getObjectName(EObject object) {
		if (object==null) return null;
		String displayName = getObjectNameOrNull(object);
		if (displayName == null) displayName = findBuriedName(object);
		if (displayName != null) return displayName;
		if (displayName == null && object.eIsProxy() && object instanceof DynamicEObjectImpl) {
			return "[Unresolved reference to " + ((DynamicEObjectImpl)object).eProxyURI().toString() + "]";
		}
		else
			return displayName; /// "Unnamed " + object.eClass().getName();
	}
	
	public static String getObjectId(EObject object) {
		if (object.eIsProxy() && object instanceof DynamicEObjectImpl) {
			return ((DynamicEObjectImpl)object).eProxyURI().fragment();
		}
		String id1 = ADParameterUtils.getObjectString(object, "id");
		if (id1 != null) return id1;
		String id2 = ADParameterUtils.getObjectString(object, "id");
		if (id2 != null) return id2;
		return null;
	}
	
	public static String structureToStrings(DynamicEObjectImpl structure) {
		StringBuilder s = new StringBuilder();
		for (EStructuralFeature field : structure.eClass().getEStructuralFeatures()) {
			Object value = structure.eGet(field, true, true);
			if (value instanceof String) {
				s.append(value.toString());
				s.append('\n');
			}
		}
		return s.toString();
	}
	
	public static String getNameOfReference(ReferenceAndEObject reference) {
		String objectName = getObjectNameOrNull(reference.getObject());
		if (objectName == null) objectName = findBuriedName(reference.getObject());
		if (objectName != null) return objectName;
		return getObjectName(reference.getObject());
	}
	
	private static String findBuriedName(Object object) {
		if (object instanceof List) {
			List list = (List) object;
			for (Object element : list) {
				String found = findBuriedName(element);
				if (found != null) return found;
			}
		}
		else if (object instanceof EStringToStringMapEntryImpl) {
			EStringToStringMapEntryImpl map = (EStringToStringMapEntryImpl) object;
			if (map.getKey().equalsIgnoreCase("name")) {
				return map.getValue();
			}
		} else if (object instanceof EObject) {
			return findBuriedName(((EObject) object).eContents());
		}
		return null;
	}
	
	public static boolean deepEquals(Object oldValue, Object newValue, Object... previouslyVisited) {
		if (oldValue==newValue) return true;
		if (oldValue==null || newValue==null) return false;
		if (oldValue.equals(newValue)) return true;
		if (arrayStackContains(previouslyVisited, oldValue)
				|| arrayStackContains(previouslyVisited, newValue)) {
				return false;
		}
		if (oldValue instanceof List && newValue instanceof List) {
			List<Object> oldList = (EList) oldValue;
			List<Object> newList = (EList) newValue;
			if (oldList.size() != newList.size()) return false;
			for (int i=0; i < oldList.size(); i++) {
				if (!deepEquals(
						oldList.get(i), newList.get(i),
						oldValue, newValue, previouslyVisited)) {
					return false;
				}
			}
			return true;
		}
		if (oldValue instanceof EObject && newValue instanceof EObject) {
			EObject oldObject = (EObject) oldValue;
			EObject newObject = (EObject) newValue;
			EClass oldClass = oldObject.eClass();
			EClass newClass = newObject.eClass();
			if (oldClass != newClass) return false;
			if ((oldObject.eIsProxy() || newObject.eIsProxy())
				&& CommonUtils.equals(
					getObjectId(oldObject), 
					getObjectId(newObject))) {
				return true;
			}
			for (EStructuralFeature feature : oldClass.getEAllStructuralFeatures())  {
				if (!isIdFeature(feature)) {
					if (!deepEquals(
							oldObject.eGet(feature), newObject.eGet(feature),
							previouslyVisited)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param arrayStack -- results from passing Object... on parameter list to track where we've been
	 * @param value -- object to look for
	 * @return true if object is present, however far down
	 */
	public static boolean arrayStackContains(Object[] arrayStack, Object value) {
		for (Object element : arrayStack) {
			if (element==value) return true;
		}
		if (value.getClass().isArray()) {
			for (int i=0; i < Array.getLength(value); i++) {
				if (arrayStackContains(arrayStack, Array.get(value, i))) {
					return true;
				}
			}
		}		
		return false;
	}

	
	public static boolean isIdFeature(EStructuralFeature feature) {
		String name = feature.getName();
		return name.equalsIgnoreCase("id") || name.endsWith("ID") || name.endsWith("Id");
	}
	
	public static EPlanElement getOwner(PlanChange diff) {
		if (diff instanceof ChangedByAddingNewElement) {
			return ((ChangedByAddingNewElement) diff).getAddedElement();
		}
		if (diff instanceof ChangedByRemovingElement) {
			return ((ChangedByRemovingElement) diff).getRemovedElement();
		}
		if (diff instanceof ChangedByMovingChild) {
			return ((ChangedByMovingChild) diff).getOldCopyOfElement();
		}
		if (diff instanceof ChangedByModifyingParameterOrReference) {
			return ((ChangedByModifyingParameterOrReference) diff).getOldCopyOfOwner();
		}
		if (diff instanceof ChangedConstraintOrProfile) {
			return ((ChangedConstraintOrProfile) diff).getOldCopyOfOwner();
		}
		return null;
	}

	/**
	 * If object's frobnitz attribute has a value of 90 seconds, for example, this returns "00:01:30".
	 * @param attribute, e.g. Marked (belonging to some activity)
	 * @param value, e.g. true
	 * @return e.g. "true"
	 */
	@SuppressWarnings("unchecked")
	public
	static String getDisplayString(EAttribute attribute, Object value) {
		String string;
		try {
			IStringifier stringifier = EMFUtils.getStringifier(attribute);
			string = stringifier.getDisplayString(value);
		}
		catch (Exception e) {
			string = "[Error displaying value: " + e.getMessage() + "]";
		}
		if (string==null) return "<none>";
		if ("".equals(string)) return "<blank>";
		return string;
	}
	
	public static String getDisplayString(EStructuralFeature feature, Object value) {
		if (value==null) return "<none>";
		else if (feature instanceof EAttribute) {
			return getDisplayString((EAttribute) feature, value);
		} else if (value instanceof List) {
			return getListElementNames(feature, (List) value);
		} else if (value instanceof EObject) {
			return getObjectName((EObject) value);
		} else {
			return "<structure>";
		}
	}
	
	private static String getListElementNames(EStructuralFeature feature, List list) {
		if (list.isEmpty()) return "<empty list>";
		StringBuilder s = new StringBuilder();
		Iterator iterator = list.iterator();
		s.append('{');
		while (iterator.hasNext()) {
			s.append(getDisplayString(feature, iterator.next()));
			if (iterator.hasNext()) {
				s.append(", ");
			}
		}
		s.append('}');
		return s.toString();
	}

	/**
	 * Loads a plan from a .plan file.
	 * @param file a .plan file
	 * @param resourceSet shared ResourceSet, or null if it can be standalone
	 * @return null on failure, or a plan object
	 */
	static public EPlan loadPlanFromFile (File file, ResourceSet resourceSet) {
		return loadPlanFromFile(URI.createURI(file.toURI().toString()), resourceSet);
	}
	
	/**
	 * Loads a plan from a .plan file.
	 * @param file a .plan file
	 * @param resourceSet shared ResourceSet, or null if it can be standalone
	 * @return null on failure, or a plan object
	 */
	static public EPlan loadPlanFromFile (URL file, ResourceSet resourceSet) {
		return loadPlanFromFile(URI.createURI(file.toString()), resourceSet);
	}

	/**
	 * Loads a plan from a .plan file.
	 * @param uri a .plan file
	 * @param resourceSet shared ResourceSet, or null if it can be standalone
	 * @return null on failure, or a plan object
	 */
	static public EPlan loadPlanFromFile (URI uri, ResourceSet resourceSet) {
		try {
			if (resourceSet==null) resourceSet = new ResourceSetImpl();
			resourceSet.getLoadOptions().put(EnsembleOption.OPTION_TO_DISABLE_PLAN_ADVISOR, Boolean.TRUE);
			return EPlanUtils.loadPlanIntoResourceSetWithErrorChecking(resourceSet, uri, null);
		} catch (Exception e) {
			LogUtil.error(e);
			return null;
		}

	}

	/** I would use the parameter displayName but although it's in the AD it doesn't seem to get stored in the model. */
	public static String convertFromCamelToPretty(String uglyCamel) {
			if (uglyCamel.isEmpty()) return ""; // shouldn't happen
			StringBuilder pretty = new StringBuilder(uglyCamel.length() + 5);
			pretty.append(Character.toUpperCase(uglyCamel.charAt(0)));
			char lastChar = ' ';
			for (int i = 1; i < uglyCamel.length(); i++) {
				char nextChar = uglyCamel.charAt(i);
				if (nextChar=='_') nextChar = ' ';
				if (Character.isUpperCase(nextChar) && lastChar != ' ') {
					pretty.append(' ');
				}
				pretty.append(nextChar);
				lastChar = nextChar;
			}
			return pretty.toString();
		}

	public static String collapseWhitespace(String string) {
		if (string.isEmpty()) {
			return string;
		}
		char space = ' ';
		char newline = '\n';
		if (string.indexOf(newline) == -1 && string.indexOf('\r') != -1) {
			newline = '\r';
		}
		StringBuilder s = new StringBuilder(string.length());
		char previousChar = newline; 
		for (char currentChar : string.toCharArray()) {
			// Collapse multiple newlines.
			if (currentChar==newline && previousChar==newline) continue;
			// Collapse multiple spaces.
			if (currentChar==space && previousChar==space) continue;
			// Remove whitespace at beginning of line (indentation).
			if (currentChar==space && previousChar==newline) continue;
			// At end of line, if previous line ended with a space
			// (or spaces, but they collapse into one), go back and remove it.
			if (currentChar==newline  && previousChar==space) {
				s.deleteCharAt(s.length()-1);
			}
			s.append(currentChar);
			previousChar = currentChar;
		}
		// At end of string, if previous line ended with a space or newline
		// (or multiple, but they collapse into one), go back and remove it.
		if (previousChar==space || previousChar==newline) {
			s.deleteCharAt(s.length()-1);
		}
		return s.toString();
	}



}
