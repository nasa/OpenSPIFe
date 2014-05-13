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
package gov.nasa.arc.spife.rcp.events;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.core.activityDictionary.events.EventSubsetOfDictionary;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class OrbitEventUtil {
	
	private static final String ORBIT_ARG_NAME = EnsembleProperties.getStringPropertyValue("lass.integrate.ad.argument.for.events", "orbit");
	private static final String GENERIC_ATTRIBUTE_NAME
	= EnsembleProperties.getStringPropertyValue("lass.integrate.ad.attribute.for.generic", "Generic");
	private static String temp_ORBIT_ARG_NAME = null;
	private static String temp_GENERIC_ATTRIBUTE_NAME = null;
	public static String ORBIT_NAME = EnsembleProperties.getStringPropertyValue("lass.orbit.name", "Orbit");
	
	private static final boolean DEFAULT_ISGENERIC_WHEN_UNDEFINED = false;

	private static Map<String, EActivityDef> eventTypesByName;
	private static List<EActivityDef> cachedEventDefinitions = null;

	static {initializeFromAD();}

	/** Planned (non-generic) orbit events are singletons;
	 * e.g. if two plans have a Sunrise_Terminator for Orbit 42, they're the same, by definition,
	 * for purposes of plan integration and diff. */
	public static void setSpecialDiffId(EActivity event, EStructuralFeature orbitArg) {
		event.getMember(CommonMember.class).setDiffID(generateDiffIdForEvent(event, orbitArg));
	}
	
	/** Planned (non-generic) orbit events are singletons;
	 * e.g. if two plans have a Sunrise_Terminator for Orbit 42, they're the same, by definition,
	 * for purposes of plan integration and diff. */
	public static String generateDiffIdForEvent(EActivity event) {
		EClass eventDefinition = event.getData().eClass();
		EStructuralFeature orbitArg = eventDefinition.getEStructuralFeature(getNameOfOrbitArg());
		return generateDiffIdForEvent(event, orbitArg);
	}	
	
	/** Planned (non-generic) orbit events are singletons;
	 * e.g. if two plans have a Sunrise_Terminator for Orbit 42, they're the same, by definition,
	 * for purposes of plan integration and diff. 
	 * 
	 * It would be nice if we could do a ClassRegistry extension of DiffGenerator
	 * to apply this rule at activity creation time no matter how created.
	 * Unfortunately, DiffGenerator only gets the eclass as a parameter,
	 * not the activity type or the Generic setting.
	 **/
	public static String generateDiffIdForEvent(EActivity event, EStructuralFeature orbitArg) {
		return generateDiffIdForEvent(event.getType(), event.getData().eGet(orbitArg).toString());
	}
	
	/** Planned (non-generic) orbit events are singletons;
	 * e.g. if two plans have a Sunrise_Terminator for Orbit 42, they're the same, by definition,
	 * for purposes of plan integration and diff. 
	 * 
	 * It would be nice if we could do a ClassRegistry extension of DiffGenerator
	 * to apply this rule at activity creation time no matter how created.
	 * Unfortunately, DiffGenerator only gets the eclass as a parameter,
	 * not the activity type or the Generic setting.
	 **/
	public static String generateDiffIdForEvent(String type, String orbitNumber) {
		return "The " + type + " for orbit #" + orbitNumber;
	}


	/** Only needed for events imported before SPF-8089 implemented. */ 
	public static void setSpecialDiffIds(EPlan plan) {
		for (EActivity event : getPlannedOrbitalEvents(plan)) {
			event.getMember(CommonMember.class).setDiffID(generateDiffIdForEvent(event));
		}
	}
	
	public static boolean eventsAreDefined() {
		return !getEventDefinitionsInternal().isEmpty();
	}

	public static List<EActivityDef> getEventDefinitions() {
		List<EActivityDef> eventDefinitions = getEventDefinitionsInternal();
		if (eventDefinitions.isEmpty()) {
			throw new IllegalStateException("Activity Dictionary does not define any event types with an argument named "
					+ EventSubsetOfDictionary.getIdentifyingArgName());
		}
		return eventDefinitions;
	}
	
	private static List<EActivityDef> getEventDefinitionsInternal() {
		if (cachedEventDefinitions != null) {
			return cachedEventDefinitions;
		}
		cachedEventDefinitions = new EventSubsetOfDictionary().eventDefinitions();
		return cachedEventDefinitions;
	}

	public static Set<EActivity> getPlannedOrbitalEvents (EPlanElement under) {
		Set<EActivity> result = new HashSet<EActivity>();
		for (EActivity activityInstance : getAllOrbitalEvents(under)) {
			if (!isGeneric(activityInstance)) {
				result.add(activityInstance);
			}
		}
		return result;
	}
	
	public static Set<EActivity> getGenericOrbitalEvents (EPlanElement under) {
		Set<EActivity> result = new HashSet<EActivity>();
		for (EActivity activityInstance : getAllOrbitalEvents(under)) {
			if (isGeneric(activityInstance)) {
				result.add(activityInstance);
			}
		}
		return result;
	}

	private static Collection<EActivity> getAllOrbitalEvents (EPlanElement under) {
		List<EActivity> result = new LinkedList<EActivity>();
		for (EActivity activityInstance : getFlatListOfActivities(under)) {
			String definitionName = activityInstance.getType();
			if (definitionName != null && getEventTypesByName().containsKey(definitionName)) {
				result.add(activityInstance);
			}
		}
		return result;
	}
	
	public static boolean isOrbitEvent(EPlanElement element) {
		if (element instanceof EActivity)
			return isOrbitEvent((EActivity) element);
		else return false; // only activities
	}
	
	public static boolean isGeneric(EPlanElement element) {
		if (element instanceof EActivity)
			return isGeneric((EActivity) element);
		else return false; // only activities
	}
	
	public static boolean isOrbitEvent(EActivity activityInstance) {
		EObject data = activityInstance.getData();
		if (data==null) return false; // no AD definition
		return getEventDefinitionsInternal().contains(data.eClass());
	}
	
	public static boolean isGeneric(EActivity activityInstance) {
		EObject data = activityInstance.getData();
		if (data==null) return DEFAULT_ISGENERIC_WHEN_UNDEFINED;
		EActivityDef eventDefinition = (EActivityDef) data.eClass();
		EStructuralFeature genericAttrib = eventDefinition.getEStructuralFeature(getNameOfGenericAttribute());
		if (genericAttrib==null) return DEFAULT_ISGENERIC_WHEN_UNDEFINED;
		Object value = data.eGet(genericAttrib);
		if (value==null) return DEFAULT_ISGENERIC_WHEN_UNDEFINED;
		return (Boolean) value;
	}
	
	public static void setGeneric(EActivity activityInstance, boolean value) {
		EObject data = activityInstance.getData();
		EActivityDef eventDefinition = (EActivityDef) data.eClass();
		EStructuralFeature genericAttrib = eventDefinition.getEStructuralFeature(getNameOfGenericAttribute());
		if (genericAttrib != null) {
			data.eSet(genericAttrib, value);
		}
	}
	
	public static Collection<EActivity> getFlatListOfActivities(EPlanElement under) {
		return getFlatListOfElements(under, EActivity.class);
	}

	static Collection<EPlanChild> getFlatListOfElements(EPlanElement under) {
		return getFlatListOfElements(under, EPlanChild.class);
	}
	
	static <T> Collection<T> getFlatListOfElements(EPlanElement under, Class<T> desiredClass) {
		if (under==null) return Collections.EMPTY_LIST;
		Collection<T> result = new ArrayList<T>();
		if (desiredClass.isAssignableFrom(under.getClass())) {
			result.add((T) under);
		}
		for (EPlanChild child : under.getChildren()) {
			result.addAll(getFlatListOfElements(child, desiredClass));
		}
		return result;
	}
	

	public static void initializeFromAD() {
		cachedEventDefinitions = null;
		eventTypesByName = makeNameMap(getEventDefinitionsInternal());
	}

	private static <T extends INamedDefinition> Map<String, T> makeNameMap(List<T> objects) {
		Map<String, T> result = new HashMap<String, T>(objects.size());
		for (T object : objects) {
			result.put(object.getName(), object);
		}
		return result;
	}

	public static Map<String, EActivityDef> getEventTypesByName() {
		return eventTypesByName;
	}

	/** For use by unit tests that have their own dictionary,
	 * so that they don't need to be changed when the mission changes its mind
	 * about the names of these two parameters in the AD.
	 * Call overrideEventParameterNames in setUp.
	 * Call unoverrideEventParameterNames in tearDown.
	 */
	public static void overrideEventParameterNames(String tempNameOfGenericAttribute, String tempNameOfOrbitArg) {
		temp_GENERIC_ATTRIBUTE_NAME = tempNameOfGenericAttribute;
		temp_ORBIT_ARG_NAME = tempNameOfOrbitArg;
		EventSubsetOfDictionary.setIdentifyingArgName(getNameOfOrbitArg());
		initializeFromAD();
		}
	
	/** For use by unit tests that have their own dictionary,
	 * so that they don't need to be changed when the mission changes its mind
	 * about the names of these two parameters in the AD.
	 */
	public static void unoverrideEventParameterNames() {
		overrideEventParameterNames(null, null);
	}

	public static String getNameOfOrbitArg() {
		if (temp_ORBIT_ARG_NAME != null) return temp_ORBIT_ARG_NAME;
		return ORBIT_ARG_NAME;
	}

	public static String getNameOfGenericAttribute() {
		if (temp_GENERIC_ATTRIBUTE_NAME != null) return temp_GENERIC_ATTRIBUTE_NAME;
		return GENERIC_ATTRIBUTE_NAME;
	}


}
