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
package gov.nasa.ensemble.dictionary.nddl;

import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EClaimableEffect;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.dictionary.ESharableResourceEffect;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceEffect;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.Period;
import gov.nasa.ensemble.dictionary.nddl.CommandLineArguments.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

//import com.sun.tools.javac.util.Pair;

@SuppressWarnings("unchecked")
public class ParseInterpreter {
	private static final boolean DEBUG = false;
	private EActivityDictionary ad;
	private String version;
	private String CPUstate = null;
	@SuppressWarnings("unused")
	private String CPUonValue = null;
	private String CPUwindow = null;
	private Integer CPUpre;
	private Integer CPUpost;
	private Integer CPUgap;
	
	// counter for creating unique variable names
	private Integer varN;

	// new list of all object defs, which will become Classes that can be claimed by activities
	private List<ObjectDef> allObjectDefs = Collections.EMPTY_LIST;
	
	private List<String> objectDefNames = Collections.EMPTY_LIST;
	private List<EClaimableResourceDef> allClaims = Collections.EMPTY_LIST;
	private List<String> claimNames = Collections.EMPTY_LIST;
	private List<ESharableResourceDef> allShares = Collections.EMPTY_LIST;
	private List<String> shareNames = Collections.EMPTY_LIST;
	
	//private List<PowerLoadType> allPowerLoads = Collections.EMPTY_LIST;
	private List<EStateResourceDef> allStates = Collections.EMPTY_LIST;
	
	private List<String> stateNames = Collections.EMPTY_LIST;
	private List<EActivityDef> activityDefs = Collections.EMPTY_LIST;
	
	// enum state conditions that appear as a deleted precondition
	private List<String> deletedPreconds = Collections.EMPTY_LIST;
	
	// maps a state name to the allowable state (string) values
	private HashMap<String, List<String>> stateValuesMap;
	
	// maps a state name to the disallowed state (string) values
	private HashMap<String, Set<String>> stateNotValuesMap;
	
	// maps a state name to the type of the state: either Enum or Threshold
	private HashMap<String, String> stateTypesMap;
	
	// maps a subsystem to the list of activities it contains
	private HashMap<String, List<EActivityDef>> subsystemActivitiesMap;
	
	// maps an activity to its subsystem
	private HashMap<String, String> activitySubsystemMap;
	
	// Map of enum state name to pairs of activity name and state value effected
	// at start
	private HashMap<String, List<String[]>> enumActEffectMap;
	
	// Map of act name to pairs of threshold state and threshold value effected
	// at start
	private HashMap<String, List<String[]>> actThresholdEffectMap;
	
	// Map of enum state name to pairs of activity name and state value required
	// throughout
	private HashMap<String, List<String[]>> enumActRequireMap;
	
	// Map of enum state name to pairs of activity name and disallowed state value
	// required throughout
	private HashMap<String, List<String[]>> enumActNotRequireMap;
	
	// PHM 12/05/2011 Map of enum state name to activity names
	// that require it or effect it at start or end
	private HashMap<String, List<String>> enumActMap;
	
	// Map of act name to pairs of threshold state name and threshold value
	// required throughout
	private HashMap<String, List<String[]>> actThresholdRequireMap;
	
	// Map of Activity pairs to a list of Enum states that cause them to be
	// mutually exclusive (Conditions #1 or #2 or #4 or #5)
	private HashMap<String, List<String>> exclusiveActsStatesMap;
	
	
	// Map of a state to the list of values in AtEnd effects, used for
	// determining incon predicate and initial state file
	private HashMap<String, Set<String>> stateEndEffectsMap;

	List<ADTranslator> adTranslators;
	
	/**
	 * 
	 * @param ad
	 * @param CPUwindow -
	 *            The predicate that indicates for the cpu to be available
	 * @param CPUpre -
	 *            The duration of the cpu boot in seconds
	 * @param CPUpost -
	 *            The duration of the shutdown (in seconds)
	 * @param CPUgap -
	 *            The min gap between the end of one shutdown and the start of
	 *            the next boot (in seconds)
	 */
	ParseInterpreter(EActivityDictionary ad, String CPUstate, String CPUonValue, Integer CPUpre,
			Integer CPUpost, Integer CPUgap) {
		this.ad = ad;
		this.CPUstate = CPUstate;
		this.CPUonValue = CPUonValue;
		this.CPUpre = CPUpre;
		this.CPUpost = CPUpost;
		this.CPUgap = CPUgap;
		if (CPUstate != null && CPUonValue != null) CPUwindow = CPUstate + "_" + CPUonValue;
		
		adTranslators = new ArrayList<ADTranslator>();
		if (ModelGenerator.getPreferences().translateNumericResources())
			adTranslators.add(new NumericResourceTranslator(this));
	}

	/**
	 * Function run after castor has parsed the AD to create all the data
	 * structures needed to create the model files
	 * 
	 * @param quiet suppress normal output
	 */

	public void interpretAD(boolean quiet) {
		varN = 0;
		try {
			version = "\"" + ad.getVersion() + "\"";
		} catch (NullPointerException e) {
			System.out.println("The version number is not defined in AD");
			version = "0.0.0";
		}
		
		allObjectDefs = ad.getDefinitions(ObjectDef.class);
//		allPowerLoads = ad.getDefinitions(EPowerLoadDef.class);
		activityDefs = ad.getDefinitions(EActivityDef.class);		
		subsystemActivitiesMap = new HashMap<String, List<EActivityDef>>();
		activitySubsystemMap = new HashMap<String, String>();
		
		objectDefNames = new ArrayList<String>();
		for (ObjectDef objdef : allObjectDefs) {
		    objectDefNames.add(NDDLUtil.escape(objdef.getName()));
		}

		for (ADTranslator translator : adTranslators)
			translator.interpretAD(ad);
		
		allClaims = ad.getDefinitions(EClaimableResourceDef.class);
		claimNames = new ArrayList<String>();
		for (EClaimableResourceDef claim : allClaims) {
		    claimNames.add(NDDLUtil.escape(claim.getName()));
		}

		allShares = ad.getDefinitions(ESharableResourceDef.class);
		shareNames = new ArrayList<String>();
		for (ESharableResourceDef share : allShares) {
		    shareNames.add(NDDLUtil.escape(share.getName()));
		}
		
		allStates = ad.getDefinitions(EStateResourceDef.class);
		stateNames = new ArrayList<String>();
		deletedPreconds = new ArrayList<String>();
		stateValuesMap = new HashMap<String, List<String>>();
		stateNotValuesMap = new HashMap<String, Set<String>>();
		stateTypesMap = new HashMap<String, String>();
		enumActEffectMap = new HashMap<String, List<String[]>>();
		actThresholdEffectMap = new HashMap<String, List<String[]>>();
		enumActRequireMap = new HashMap<String, List<String[]>>();
		enumActNotRequireMap = new HashMap<String, List<String[]>>();
		actThresholdRequireMap = new HashMap<String, List<String[]>>();
		stateEndEffectsMap = new HashMap<String, Set<String>>();

		for (EStateResourceDef state : allStates) {
		    String stateName = NDDLUtil.escape(state.getName());
			Object typeDef = state.getEnumeration();
			if (typeDef == null)
				typeDef = state.getEType();
			stateNames.add(stateName);
			if (typeDef instanceof EEnum) {
				EEnum eEnumDef = (EEnum) typeDef;
				List<String> values = new ArrayList<String>();
				for (EEnumLiteral l : eEnumDef.getELiterals()) {
				    values.add(NDDLUtil.escape(l.getLiteral()));
				}
				stateValuesMap.put(stateName, values);
				stateTypesMap.put(stateName, "Enum");
			} else {
				System.err.print("*State " + stateName
						+ " is not of type Enum *\n\n");
			}
		}
		// For G1, treat PowerLoad resources as State Resources of type Enum
//		for (PowerLoadType pel : allPowerLoads) {
//			String stateName = NDDLUtil.escape(pel.getName());
//			stateNames.add(stateName);
//			if (pel.getDefinedStates().getState().size() > 0) {
//				List<String> values = pel.getDefinedStates().getState();
//				stateValuesMap.put(stateName, values);
//				stateTypesMap.put(stateName, "Enum");
//			} else {
//				Object typeDef = getTypeDefNamed(pel.getType());
//				if (typeDef instanceof EnumType) {
//					List<String> values = ((EnumType) typeDef).getValue();
//					stateValuesMap.put(stateName, values);
//					stateTypesMap.put(stateName, "Enum");
//				} else if (typeDef instanceof ThresholdEnumType) {
//					List<String> values = ((ThresholdEnumType) typeDef)
//							.getValue();
//					// Remove the last value, since this can never be used in a
//					// requirement
//					// since the state is always less than or equal to this
//					// value
//					if (values.size() < 2) {
//						System.err
//								.printf(
//										"\n * ThresholdEnum %s has less than 2 values *\n",
//										stateName);
//					} else {
//						values.remove(values.size() - 1);
//					}
//					stateValuesMap.put(stateName, values);
//					stateTypesMap.put(stateName, "Threshold");
//				} else {
//					System.err.print("*PowerLoad " + stateName
//							+ " is not of type Enum nor ThresholdEnum*\n\n");
//				}
//			}
//		}

		removeUnUsedResources(quiet);

		// Create mapping of subsystem name to ActivityDefs
		// Also create mapping from activity name to subsystem name
		List<EActivityDef> currentValues;
		List<String[]> info;
		String[] entry;
		String actName;
		String resource;
		String requiredValue;
		String requiredNotValue;
		List<String> allowedValues;
		Set<String> disallowedValues;
		for (EActivityDef activityDef : activityDefs) {
			actName = NDDLUtil.escape(activityDef.getName());
			// process requirements and update state-activity maps
			// note that for enums, key is resource, but for thresholds, key is
			// act
			for (EStateRequirement requirement : activityDef.getStateRequirements()) {
				if (requirement.getPeriod() == Period.REQUIRES_THROUGHOUT) {
				    resource = NDDLUtil.escape(requirement.getName());
					if (!stateTypesMap.containsKey(resource)) {
						System.err.print("*Required resource " + resource
								+ " was not defined as a state*\n\n");
					} else if (stateTypesMap.get(resource).equals("Enum")) {
						if (requirement.getRequiredState() != null) {
						    requiredValue = NDDLUtil.escape(requirement.getRequiredState());
							entry = new String[] { actName, requiredValue };
							if (enumActRequireMap.containsKey(resource)) {
								enumActRequireMap.get(resource).add(entry);
							} else {
								info = new ArrayList<String[]>();
								info.add(entry);
								enumActRequireMap.put(resource, info);
							}
						} else if (requirement.getDisallowedState() != null) {
						    requiredNotValue = NDDLUtil.escape(requirement.getDisallowedState());
							entry = new String[] { actName, requiredNotValue };
							if (enumActNotRequireMap.containsKey(resource)) {
								enumActNotRequireMap.get(resource).add(entry);
							} else {
								info = new ArrayList<String[]>();
								info.add(entry);
								enumActNotRequireMap.put(resource, info);
							}
							// Also add disallowed value to stateNotValuesMap
							if (stateNotValuesMap.containsKey(resource)) {
								stateNotValuesMap.get(resource).add(requiredNotValue);
							} else {
								disallowedValues = new HashSet<String>();
								disallowedValues.add(requiredNotValue);
								stateNotValuesMap.put(resource, disallowedValues);
							}
						} else if (requirement.getAllowedStates() != null && requirement.getAllowedStates().size() > 0) {
							//***** NEED TO ADD NOT-VALUES FOR REMAINING STATE VALUES *****
							allowedValues = requirement.getAllowedStates();
							for (String val : stateValuesMap.get(resource)) {
							    if (!allowedValues.contains(NDDLUtil.unescape(val))) {
									// add to disallowed values in enumNotRequireMap and to stateNotValuesMap
									entry = new String[] { actName, val };
									if (enumActNotRequireMap.containsKey(resource)) {
										enumActNotRequireMap.get(resource).add(entry);
									} else {
										info = new ArrayList<String[]>();
										info.add(entry);
										enumActNotRequireMap.put(resource, info);
									}
									if (stateNotValuesMap.containsKey(resource)) {
										stateNotValuesMap.get(resource).add(val);
									} else {
										disallowedValues = new HashSet<String>();
										disallowedValues.add(val);
										stateNotValuesMap.put(resource, disallowedValues);
									}
								}
							}
						} else {System.err.print("*Required resource " + resource
								+ " did not have a value specified*\n\n");
							}
					} else if (stateTypesMap.get(resource).equals("Threshold")) {
					    requiredValue = NDDLUtil.escape(requirement.getRequiredState());
						entry = new String[] { resource, requiredValue };
						if (actThresholdRequireMap.containsKey(actName)) {
							actThresholdRequireMap.get(actName).add(entry);
						} else {
							info = new ArrayList<String[]>();
							info.add(entry);
							actThresholdRequireMap.put(actName, info);
						}
					} else {
						System.err
						.print("*Required resource "
								+ resource
								+ " is not of type Enum nor ThresholdEnum*\n\n");
					}
				} else {
					// Note which enum states appear as a
					// delted_precondition
				    resource = NDDLUtil.escape(requirement.getDefinition().getName());
					if (stateTypesMap.get(resource).equals("Enum")
							&& !deletedPreconds.contains(resource)) {
						deletedPreconds.add(resource);
					}
				}
			}

			for (EStateResourceEffect<?> effect : activityDef.getStateEffects()) {
			    resource = NDDLUtil.escape(effect.getName());
			    String effectAtStartValue = NDDLUtil.escape(effect.getStartEffect());
			    String effectAtEndValue = NDDLUtil.escape(effect.getEndEffect());
				// process effects and update state-activity maps
				// note that for enums, key is resource, but for thresholds,
				// key is act
				if (effectAtStartValue != null) {
					if (!stateTypesMap.containsKey(resource)) {
						System.err.print("*Effected resource " + resource
								+ " was not defined as a state*\n\n");
					}
					else {
						String stateTypeName = stateTypesMap.get(resource);
						if (stateTypeName != null
								&& stateTypeName.equals("Enum")) {
							entry = 	new String[] { actName, effectAtStartValue };
							if (enumActEffectMap.containsKey(resource)) {
								info = enumActEffectMap.get(resource);
								info.add(entry);
								enumActEffectMap.put(resource, info);
							} else {
								info = new ArrayList<String[]>();
								info.add(entry);
								enumActEffectMap.put(resource, info);
							}
						} else if (stateTypeName != null
								&& stateTypeName.equals("Threshold")) {
							entry = new String[] { resource, effectAtStartValue };
							if (actThresholdEffectMap.containsKey(actName)) {
								info = enumActEffectMap.get(actName);
								info.add(entry);
								actThresholdEffectMap.put(actName, info);
							} else {
								info = new ArrayList<String[]>();
								info.add(entry);
								actThresholdEffectMap.put(actName, info);
							}
						}
					}
					// process end effects for Enum states only
					if (effectAtEndValue != null) {
						String stateTypeName = stateTypesMap.get(resource);
						if (stateTypeName != null
								&& stateTypeName.equals("Enum")) {
							if (stateEndEffectsMap.containsKey(resource)) {
								stateEndEffectsMap.get(resource).add(
										effectAtEndValue);
							} else {
								Set<String> valSet = new HashSet<String>();
								valSet.add(effectAtEndValue);
								stateEndEffectsMap.put(resource, valSet);
							}
						}
					}
				}
			}

			String subsystem = activityDef.getCategory();
			if (subsystem == null || subsystem.equals("")) {
				System.err.println("* Activity "
						+ NDDLUtil.escape(activityDef.getName())
						+ " has no subsytem *\n");
				// for now, just use a catch-all subsystem
				subsystem = "MISC_SUBSYSTEM";
			}
			activitySubsystemMap.put(NDDLUtil.escape(activityDef.getName()),
						 NDDLUtil.escape(subsystem));
			// update subsytemActivitiesMap
			if (subsystemActivitiesMap.containsKey(subsystem)) {
				currentValues = subsystemActivitiesMap.get(subsystem);
				currentValues.add(activityDef);
				subsystemActivitiesMap.put(subsystem, currentValues);
			} else {
				currentValues = new ArrayList<EActivityDef>();
				currentValues.add(activityDef);
				subsystemActivitiesMap.put(subsystem, currentValues);
			}
		}
		// PHM 12/06/2011 mod for resource solving
		if (ModelGenerator.getPreferences().useResourceSolvingForStateConstraints()) {
		    computeEnumActMap();
		    // Make an empty mutex map to placate later code
		    exclusiveActsStatesMap = new HashMap<String, List<String>>();
		} else {
			computeEnumStateExclusions();
		}
	}

	public List<String> getClaimNames() {
		return claimNames;
	}
	
	public List<String> getObjectDefNames() {
		return objectDefNames;
	}
	
	public HashMap<String, List<String>> getexclusiveActsStatesMap() {
		return exclusiveActsStatesMap;
	}

	public List<String> getStateNames() {
		return stateNames;
	}
	
	/**
	 * Function to determine mutually exclusive pairs of activities based on
	 * Enum states It creates a mapping from mutually exclusive pairs of
	 * activies to a list of one or more state variables that cause them to be
	 * exclusive
	 * 
	 * Two activities, A1 & A2, are exclusive wrt to state var x if any of the
	 * following conditions hold, where y and z are not equal: 
	 * (1) A1 RequiresThroughout that x=y and A2 RequiresThroughout that x=z 
	 * (2) A1 RequiresThroughout that x=y and A2 has an EffectAtStart that x=z 
	 * (3) A1 has an EffectAtStart that x=y and A2 has an EffectAtStart that x=z
	 * At least initially we are eliminated the third condition, because it is
	 * covered (typically) by using a claim
	 * (4) A1 RequiresThroughout that x = !y and A2 RequiresThroughout that x = y
	 * (5) A1 RequiresThroughout that x = !y and A2 has an EffectAtStart that x=y
	 * 
	 */
	private void computeEnumStateExclusions() {
		exclusiveActsStatesMap = new HashMap<String, List<String>>();
		// exclusiveActsStatesMap2 = new HashMap<String, List<String>>();
		Integer from;
		Integer to;
		for (String state : enumActRequireMap.keySet()) {
			List<String[]> actValList = enumActRequireMap.get(state);
			to = actValList.size();
			from = 0;
			for (String[] actVal1 : actValList) {
				if (++from < to) {
					for (String[] actVal2 : actValList.subList(from, to)) {
						// Condition #1
						condition1(state, actVal1, actVal2);
					}
				}
				// Do not create mutual exclusions based on an effect w.r.t the CPU state variable
				if (enumActEffectMap.containsKey(state) && !state.equals(CPUstate)) {
					for (String[] actVal2 : enumActEffectMap.get(state)) {
						// Condition #2
						condition2(state, actVal1, actVal2);
					}
				}
			}
		}
		// Handle the Negated Requirements
		if (enumActNotRequireMap.keySet() != null) {
			for (String state : enumActNotRequireMap.keySet()) {
				List<String[]> actNotValList = enumActNotRequireMap.get(state);
				List<String[]> actValList = enumActRequireMap.get(state);
				if (actNotValList != null) {
					for (String[] actVal1 : actNotValList) {
						if (actValList != null) {
							for (String[] actVal2 : actValList) {
								// Condition #4
								condition4_5(state, actVal1, actVal2);
							}
						}
						// Do not create mutual exclusions based on an effect w.r.t the CPU state variable
						if (enumActEffectMap.containsKey(state) && !state.equals(CPUstate)) {
							for (String[] actVal2 : enumActEffectMap.get(state)) {
								// Condition #5
								condition4_5(state, actVal1, actVal2);
							}
						}
					}
				}
			}
		}
	}

	private void condition1(String state, String[] actVal1, String[] actVal2) {
		if (!actVal1[1].equals(actVal2[1])) {
			if (actVal1[0].equals(actVal2[0])) {
				System.err.print("\n* Activity " + actVal1[0]
				                                           + " is inconsistent w.r.t. state "
				                                           + state + " *\n\n");
			} else {
				String mutexActs = actVal1[0] + "__mx__"
				+ actVal2[0];
				if (exclusiveActsStatesMap
						.containsKey(mutexActs)) {
					if (!exclusiveActsStatesMap.get(mutexActs)
							.contains(state)) {
						exclusiveActsStatesMap.get(mutexActs)
						.add(state);
					}
				} else {
					List<String> stateList = new ArrayList<String>();
					stateList.add(state);
					exclusiveActsStatesMap.put(mutexActs,
							stateList);
				}
			}
		}
	}

	private void condition2(String state, String[] actVal1, String[] actVal2) {
		if (!actVal1[1].equals(actVal2[1])) {
			if (actVal1[0].equals(actVal2[0])) {
				System.err.print("\n* Activity " + actVal1[0]
				                                           + " is inconsistent w.r.t. state "
				                                           + state + " *\n\n");
			} else {
				// do not duplicate what is already mutually
				// exclusive via Condition #1
				// want to avoid duplicate mutual exclusions, so
				// look for both symmetric relations
				String mutexActs1 = actVal1[0] + "__mx__"
				+ actVal2[0];
				String mutexActs2 = actVal2[0] + "__mx__"
				+ actVal1[0];

				if (exclusiveActsStatesMap
						.containsKey(mutexActs1)) {
					if (!exclusiveActsStatesMap
							.get(mutexActs1).contains(state)) {
						exclusiveActsStatesMap.get(mutexActs1)
						.add(state);
					}
				} else if (exclusiveActsStatesMap
						.containsKey(mutexActs2)) {
					if (!exclusiveActsStatesMap
							.get(mutexActs2).contains(state)) {
						exclusiveActsStatesMap.get(mutexActs2)
						.add(state);
					}
				} else {
					List<String> stateList = new ArrayList<String>();
					stateList.add(state);
					exclusiveActsStatesMap.put(mutexActs1,
							stateList);
				}
			}
		}
	}

	private void condition4_5(String state, String[] actVal1, String[] actVal2) {
		if (actVal1[1].equals(actVal2[1])) {
			if (actVal1[0].equals(actVal2[0])) {
				System.err.print("\n* Activity " 
						+ actVal1[0]                                 
						          + " is inconsistent w.r.t. state "
						          + state + " *\n\n");
			} else {
				// do not duplicate what is already mutually exclusive 
				// want to avoid duplicate mutual exclusions, so
				// look for both symmetric relations
				String mutexActs1 = actVal1[0] + "__mx__" + actVal2[0];
				String mutexActs2 = actVal2[0] + "__mx__" + actVal1[0];
				if (exclusiveActsStatesMap
						.containsKey(mutexActs1)) {
					if (!exclusiveActsStatesMap
							.get(mutexActs1).contains(state)) {
						exclusiveActsStatesMap.get(mutexActs1)
						.add(state);
					}
				} else if (exclusiveActsStatesMap
						.containsKey(mutexActs2)) {
					if (!exclusiveActsStatesMap
							.get(mutexActs2).contains(state)) {
						exclusiveActsStatesMap.get(mutexActs2)
						.add(state);
					}
				} else {
					List<String> stateList = new ArrayList<String>();
					stateList.add(state);
					exclusiveActsStatesMap.put(mutexActs1,
							stateList);
				}
			}
		}
	}

	/**
	 * PHM 12/02/2011 Utility function
	 * to safely add elements to enumActMap
	 */
    private void addToEnumActMap (String resource, String actName) {
	// First make sure the resource key is set up
	if (!enumActMap.containsKey(resource)
			|| enumActMap.get(resource) == null) {
	    enumActMap.put(resource, new ArrayList<String>());
	}
	// Now add actName but avoid duplicates
	if ( !enumActMap.get(resource).contains(actName) ) {
	    enumActMap.get(resource).add(actName);
	}
    }

	/**
	 * PHM 12/02/2011 establish enumActMap
	 * Get elements from enumActRequireMap and enumActEffectMap
	 * and also collect and add end effects. (enumActEffectMap
	 * is based only on start effects.)
	 */
    private void computeEnumActMap() {
    enumActMap = new HashMap<String, List<String>>();
	for (String state : enumActRequireMap.keySet()) {
	    for (String[] entry : enumActRequireMap.get(state)) {
		addToEnumActMap(state, entry[0]);
	    }
	}
	for (String state : enumActEffectMap.keySet()) {
	    for (String[] entry : enumActEffectMap.get(state)) {
		addToEnumActMap(state, entry[0]);
	    }
	}
	String resource;
	for (EActivityDef activityDef : activityDefs) {
	    String actName = NDDLUtil.escape(activityDef.getName());
            for (EStateResourceEffect<?> effect
                     : activityDef.getStateEffects()) {
                resource = NDDLUtil.escape(effect.getName());
                String effectAtEndValue = NDDLUtil.escape(effect.getEndEffect());
                if (effectAtEndValue != null) {
                    String stateTypeName = stateTypesMap.get(resource);
                    if (stateTypeName != null && stateTypeName.equals("Enum")) {
			addToEnumActMap(resource, actName);
                    }
                }
            }
        }			
    }


	/**
	 * Function that writes out the objects.nddl model file to the given output
	 * stream based on a given Activity Dictionary that has already been parsed
	 * 
	 * @param shortName
	 *            of oStrm
	 */
	public void writeObjects(OutputStream oStrm) {
		PrintStream out = new PrintStream(oStrm);
		
		// For now, global declarations in the AD are ignored
		// We first define globals used in all models
		// #include \"SaturatedResource.nddl\"\n\n"
		out.printf("string \t AD_VERSION = \t %s;\n"
				+ "float \t STATE_COND_TRUE = \t 1000.0;\n"
				+ "float \t STATE_COND_FALSE = \t 1000.0;\n"
				+ "int \t inconStart;\n"
				+ "int \t CPU_BOOT_DUR = \t %d;\n"
				+ "int \t CPU_SHUTDOWN_DUR = \t %d;\n"
				+ "int \t POST_CPU_WINDOW = \t %d;\n\n", 
				version, CPUpre, CPUpost, CPUpost + CPUgap);

		// Define globals for active enforcement and passive checking of flight
		// rules and set each one to true to enable it by default
		out.print("\nbool \t Enable_Passive_Checking;\nEnable_Passive_Checking.specify(true);\n"
						+ "bool \t Enable_Active_Enforcement;\nEnable_Active_Enforcement.specify(true);\n\n");
		for (String objdef : objectDefNames) {
			out.printf("bool \t Enforce_%s_claim;\nEnforce_%s_claim.specify(true);\n\n", objdef, objdef);
		}
		out.println();	
		for (String claim : claimNames) {
			out.printf("bool \t Enforce_%s;\nEnforce_%s.specify(true);\n\n",
					claim, claim);
		}
		out.println();
		// PHM 12/05/2011 Different guards for resource solving
		if (ModelGenerator.getPreferences().useResourceSolvingForStateConstraints()) {
			// PHM 04/05/2012 Extra guards for resource solving
			for (String share : shareNames) {
				out.printf("bool \t Enforce_sx_%s;\nEnforce_sx_%s.specify(true);\n\n",
						share, share);
			}
			out.println();
		    for (String state : enumActMap.keySet()) {
			for (String actName : enumActMap.get(state)) {
			    out.printf("bool \t Enforce_mx_%s__%s;\nEnforce_mx_%s__%s.specify(true);\n\n",
				       actName, state, actName, state);
			}
		    }
		} else { // The old timeline solving code
		    for (String mutex : exclusiveActsStatesMap.keySet()) {
			for (String state : exclusiveActsStatesMap.get(mutex)) {
			    out.printf("bool \t Enforce_%s__%s;\nEnforce_%s__%s.specify(true);\n\n",
				       mutex, state, mutex, state);
			}
		    }
		}
		out.println();
		// globals for threshold state enforcement
		for (String state : stateNames) {
			String stateTypeName = stateTypesMap.get(state);
			if (stateTypeName != null && stateTypeName.equals("Threshold")) {
				out.printf(
						"bool \t Enforce_%s;\nEnforce_%s.specify(true);\n\n",
						state, state);
			}
		}
		out.println();
		// Define class used for instance-based enable/disable of active enforcement
		// Each Activity predicate will have a parameter of this class
		out.print("\n" + "class Active_Enforcer extends Object {\n");
		for (String objdef : objectDefNames) {
			out.printf("\t bool \t Enforce_%s_claim;\n", objdef);
		}
		for (String claim : claimNames) {
			out.printf("\t bool \t Enforce_%s;\n", claim);
		}
		// PHM 12/07/2011 Different guards for resource solving
		if (ModelGenerator.getPreferences().useResourceSolvingForStateConstraints()) {
			// PHM 04/05/2012 Extra guards for resource solving
			for (String share : shareNames) {
				out.printf("\t bool \t Enforce_sx_%s;\n", share);
			}
		    for (String state : enumActMap.keySet()) {
			for (String actName : enumActMap.get(state)) {
			    out.printf("\t bool \t Enforce_mx_%s__%s;\n",
				       actName, state);
			}
		    }
		} else { // The old timeline solving code
		    for (String mutex : exclusiveActsStatesMap.keySet()) {
			for (String state : exclusiveActsStatesMap.get(mutex)) {
			    out.printf("\t bool \t Enforce_%s__%s;\n", mutex, state);
			}
		    }
		}
		for (String state : stateNames) {
			String stateTypeName = stateTypesMap.get(state);
			if (stateTypeName != null && stateTypeName.equals("Threshold")) {
				out.printf(
						"\t bool \t Enforce_%s;\n", state);
			}
		}
		out.println();
		out.print("    Active_Enforcer() {}\n}\n\n");
		// Define Claimable resources
		out.print("class Unit_Capacity_Resource extends Reservoir {"
				+ "\n  string profileType;"
				+ "\n  string detectorType;"
				+ "\n Unit_Capacity_Resource(float initCap) { \n"
				+ "\n\t super(initCap, 0.0, 10.0);"
				+ "\n\t profileType = \"AddGroundedProfile\";"
				+ "\n\t detectorType = \"PassiveFVDetector\";"
				+ "\n\t}\n}\n");
		for (String objdef : objectDefNames) {
			out.printf("class %s_claim extends Unit_Capacity_Resource {\n    %s_claim(float initCap) {\n\t super(initCap);\n\t}\n}\n", objdef, objdef);
		}
		for (String claim : claimNames) {
			out.printf("class %s extends Unit_Capacity_Resource {\n    %s(float initCap) {\n\t super(initCap);\n\t}\n}\n",
							claim, claim);
		}
		// Define Sharable resources
		// PHM 12/08/2011 Different Flaw detector for Resource Solving
		if (ModelGenerator.getPreferences().useResourceSolvingForStateConstraints()) {
		out.print("\n\n\nclass Multi_Capacity_Resource extends Reservoir {"
				+ "\n  string profileType;"
				+ "\n  string detectorType;"
				+ "\n    Multi_Capacity_Resource(float initCap, float maxCap) { \n"
				+ "\t super(initCap, 0.0, maxCap);"
				+ "\n\t profileType = \"AddGroundedProfile\";"
				+ "\n\t detectorType = \"GroundedFlawDetector\";"
				+ "\n\t}\n}\n");
		} else {
		out.print("\n\n\nclass Multi_Capacity_Resource extends Reservoir {"
				+ "\n  string profileType;"
				+ "\n  string detectorType;"
				+ "\n    Multi_Capacity_Resource(float initCap, float maxCap) { \n"
				+ "\t super(initCap, 0.0, maxCap);"
				+ "\n\t profileType = \"AddGroundedProfile\";"
				+ "\n\t detectorType = \"PassiveFVDetector\";"
				+ "\n\t}\n}\n");
		}
		for (ESharableResourceDef share : allShares) {
			out.printf("class %s extends Multi_Capacity_Resource {\n    %s(float initCap, float maxCap) {\n\t super(initCap, maxCap);\n\t}\n}\n",
				   NDDLUtil.escape(share.getName()), NDDLUtil.escape(share.getName()));
		}

		// Define Enum and Threshold Enum state conditions
		// Note that Enum has been renamed EnumType
		// First print part of resource defintions common to all models
		// PHM 12/08/2011 Different Flaw detector for Resource Solving
		if (ModelGenerator.getPreferences().useResourceSolvingForStateConstraints()) {
		out.print("\nclass State_Condition extends Reservoir {"
				+ "\n  string profileType;"
				+ "\n  string detectorType;"
				+ "\n    State_Condition(float initCap) {\n"
						+ "\t super(initCap, 0.0, +inff);"
						+ "\n\t profileType = \"SatGroundedProfile\";"
						+ "\n\t detectorType = \"GroundedFlawDetector\";"
						+ "\n\t}\n}\n\n" 
						+ "class Threshold_Condition extends Reservoir {"
						+ "\n  string profileType;"
						+ "\n  string detectorType;"
						+ "    Threshold_Condition(float initCap) {\n"
						+ "\t super(initCap, 0.0, +inff);"
						+ "\n\t profileType = \"AddGroundedProfile\";"
						+ "\n\t detectorType = \"GroundedFlawDetector\";"
						+ "\n\t}\n}\n\n");
		} else {
		out.print("\nclass State_Condition extends Reservoir {"
				+ "\n  string profileType;"
				+ "\n  string detectorType;"
				+ "\n    State_Condition(float initCap) {\n"
						+ "\t super(initCap, 0.0, +inff);"
						+ "\n\t profileType = \"SatGroundedProfile\";"
						+ "\n\t detectorType = \"PassiveFVDetector\";"
						+ "\n\t}\n}\n\n" 
						+ "class Threshold_Condition extends Reservoir {"
						+ "\n  string profileType;"
						+ "\n  string detectorType;"
						+ "    Threshold_Condition(float initCap) {\n"
						+ "\t super(initCap, 0.0, +inff);"
						+ "\n\t profileType = \"AddGroundedProfile\";"
						+ "\n\t detectorType = \"PassiveFVDetector\";"
						+ "\n\t}\n}\n\n");
		}
		// No longer using Deleted_Precondition distinction
		// "class Deleted_Precondition extends Resource {\n" +
		// " Deleted_Precondition(float initCap) {\n" +
		// "\t super(initCap, 0.0, +inff, +inff, +inff, -inff,
		// -inff);\n\t}\n}\n\n"

		if (CPUwindow != null) {
			out.print("class CPU_Windows extends Reservoir {\n"
					        + "    string profileType;\n"
					        + "    string detectorType;\n"
							+ "    CPU_Windows() {\n"
							+ "\t super(0.0, -inff, +inff);\n"
					        + "\t profileType = \"AddGroundedProfile\";\n"
							+ "\t detectorType = \"PassiveFVDetector\";\n"
							+ "\t}\n}\n\n");
		}

		for (String state : stateTypesMap.keySet()) {
			String name;
			if (stateTypesMap.get(state).equals("Enum")) {
				// distinguish the enum states that appear as a deleted
				// precondition
				// We cannot use this distinction because the mod-filter of
				// flaws is needed for all state conditions
				// due to the way the state effects are implemented, a
				// stateVar-value can be made false twice
				// if (deletedPreconds.contains(state)) {
				// for (String value : stateValuesMap.get(state)) {
				// name = state + "_" + value;
				// out.printf("class %s extends Deleted_Precondition {\n
				// %s(float initCap) {\n\t super(initCap);\n\t }\n}\n\n", name,
				// name);
				// }
				// } else {
				// for (String value : stateValuesMap.get(state)) {
				// name = state + "_" + value;
				// out.printf("class %s extends State_Condition {\n %s(float
				// initCap) {\n\t super(initCap);\n\t }\n}\n\n", name, name);
				// }
				// }

				for (String value : stateValuesMap.get(state)) {
					name = state + "_" + value;
					String correctName = NDDLUtil.escape(name);
					out.printf("class %s extends State_Condition {\n    %s(float initCap) {\n\t super(initCap);\n\t }\n}\n\n",
									correctName, correctName);
				}
				// handle the states with negated values
				if (stateNotValuesMap.get(state) != null) {
					for (String value : stateNotValuesMap.get(state)) {
						name = "not_" + state + "_" + value;
						String correctName = NDDLUtil.escape(name);
						out.printf("class %s extends State_Condition {\n    %s(float initCap) {\n\t super(initCap);\n\t }\n}\n\n",
								correctName, correctName);
					}
				}
			} else if (stateTypesMap.get(state).equals("Threshold")) {
				for (String value : stateValuesMap.get(state)) {
					name = state + "_" + value;
					name = state + "_" + value;
					out.printf("class %s extends Threshold_Condition {\n    %s(float initCap) {\n\t super(initCap);\n\t }\n}\n\n",
									name, name);
				}
			}
		}

		// define mutual exclusion timelines for claims
		for (String objdef : objectDefNames) {
			out.print("class Active_" + objdef + "_claim extends Timeline {\n"
					+ "  Active_" + objdef + "_claim() {}\n" + "    predicate "
					+ objdef + "_claim_MX {}\n}\n\n");
		}
		for (String claimNam : claimNames) {
			out.print("class Active_" + claimNam + " extends Timeline {\n"
					+ "  Active_" + claimNam + "() {}\n" + "    predicate "
					+ claimNam + "_MX {}\n}\n\n");
		}
		// define mutual exclusion timelines for threshold states
		for (EStateResourceDef state : allStates) {
		    String stateName = NDDLUtil.escape(state.getName());
			String stateTypeName = stateTypesMap.get(stateName);
			if (stateTypeName != null && stateTypeName.equals("Threshold")) {
				for (String val : stateValuesMap.get(stateName)) {
				    stateName = NDDLUtil.escape(state.getName()) + "_" + val;
					out.print("class Active_" + stateName
							+ " extends Timeline {\n" + "  Active_" + stateName
							+ "() {}\n" + "    predicate GT_" + stateName
							+ " {}\n" + "    predicate LE_" + stateName
							+ " {}\n}\n\n");
				}
			}
		}
		// PHM 12/07/2011 Will be empty for resource solving
		// define mutual exclusion timelines for enumeration state conflicts
		for (String mutex : exclusiveActsStatesMap.keySet()) {
			out.printf("class Active_%s extends Timeline {\n"
					+ "  Active_%s() {}\n" + "    predicate %s_MX {}\n}\n\n",
					mutex, mutex, mutex);
		}
		// for (String mutex : exclusiveActsStatesMap2.keySet()) {
		// out.printf("class Active_%s extends Timeline {\n" +
		// " Active_%s() {}\n" +
		// " predicate %s_MX {}\n}\n\n",
		// mutex, mutex, mutex);
		// }

		// Define Activity Types
		// All activity types must have a subsystem specified, as this is the
		// class they will belong to

		// Print the subsytem class definitions with a predicate for each
		// activity
		// Also include classes that are common to all models
		
		// Create a HIDDEN (sub-activity) class for each dynamic object reference
		for (String objdef : objectDefNames) {
			out.printf("\nclass %s extends Object {\n" +
					"    %s_claim passive;\n" +
					"    Active_%s_claim active; \n" +
					"  %s() {\n" +
					"    passive = new %s_claim(1.0);\n" +
					"    active = new Active_%s_claim();\n" +
					"  } \n" +
					"  predicate Assign_%s {\n"
					+ "    int \t priority;\n"
					+ "    int \t reftime;\n"
					+ "    bool \t enforced;\n"
					+ "    bool \t scheduled;\n" 
					+ "    bool \t solved;\n" 
					+ "    bool \t subSolved;\n"
					+ "    int \t offset;\n"
					+ "    float \t container_id;\n"
					+ "    Active_Enforcer \t myEnforce;\n"
					+ "    bool \t afterIncon;\n" + "  }\n}\n\n"
					, objdef, objdef, objdef, objdef, objdef, objdef, objdef);
		}
		
		out.print("\n" + "class Misc extends Object { \n" + "  Misc() {} \n\n"
				+ "  predicate GENERIC_ACTIVITY {\n" 
				+ "    int \t priority;\n"
				+ "    int \t reftime;\n" + "    bool \t enforced;\n"
				+ "    bool \t scheduled;\n" + "    bool \t solved;\n"
				+ "    bool \t subSolved;\n" 
				+ "    int \t offset;\n"
				+ "    float \t container_id;\n  }\n\n" + "}\n\n"
				+ "class ContainerObj extends Object { \n"
				+ "  ContainerObj() {} \n\n" + "  predicate CONTAINER {\n"
				+ "    int \t priority;\n" + "    int \t reftime;\n"
				+ "    bool \t enforced;\n" + "    bool \t scheduled;\n"
				+ "    bool \t solved;\n" + "    bool \t subSolved;\n"
				+ "    int \t offset;\n"
				+ "    float \t container_id;\n"
				+ "    float \t name; \n  }\n\n" + "}\n");
		// define Incon activity type
		// we assume all claimable & sharable resources and all states are in
		// the Incon
		out.print("\n" + "class InitialConds extends Object {\n"
				+ "  InitialConds() {}\n\n" + "  predicate incon {\n"
				+ "    int \t priority;\n" + "    int \t reftime;\n"
				+ "    bool \t enforced;\n" + "    bool \t scheduled;\n"
				+ "    bool \t solved;\n" + "    bool \t subSolved;\n"
				+ "    int \t offset;\n"
				+ "    float \t container_id;\n");
		for (String claim : claimNames) {
			out.printf("    float \t _%s;\n", NDDLUtil.escape(claim));
		}
		for (String share : shareNames) {
			out.printf("    float \t _%s;\n", NDDLUtil.escape(share));
		}
		// note that we use state-value pairs
		// for each state, one and only one value should be TRUE
		for (String state : stateNames) {
			List<String> stateValues = stateValuesMap.get(state);
			if (stateValues != null && stateValues.size() > 0) {
				for (String val : stateValues) {
					out.printf("    float \t _%s_%s;\n",
							NDDLUtil.escape(state), NDDLUtil.escape(val));
				}
			}
		}
		for (ADTranslator translator : adTranslators)
		    if (translator instanceof NumericResourceTranslator) {
			((NumericResourceTranslator) translator).writeExtraInconParameters(out);
		    }
		// PHM 08/08/2013 Give Incon highest (lowest number) priority
		out.print("\n" + "\teq(priority, -1000000);");
		out.print("\n" + "\teq(duration, 1);\n" + "  }\n}\n\n");
		// define a class for each subsystem
		for (String subsystem : subsystemActivitiesMap.keySet()) {
			List<EActivityDef> activities = subsystemActivitiesMap.get(subsystem);
			out.printf("\nclass %s extends Object {\n  %s() {} \n\n",
					subsystem, subsystem);
			for (EActivityDef activity : activities) {
				out.print("  predicate " + NDDLUtil.escape(activity.getName())
						+ " {\n" + "    int \t priority;\n"
						+ "    int \t reftime;\n" 
						+ "    bool \t enforced;\n"
						+ "    bool \t scheduled;\n" 
						+ "    bool \t solved;\n"
						+ "    bool \t subSolved;\n"
						+ "    int \t offset;\n"
						+ "    float \t container_id;\n"
						+ "    Active_Enforcer \t myEnforce;\n"
						+ "    bool \t afterIncon;\n");
				writeStateParameters(out, activity);
				for (ADTranslator translator : adTranslators)
					if (translator instanceof NumericResourceTranslator) {
				      ((NumericResourceTranslator) translator).writeExtraParameters(out, activity);
					}
				out.print(" }\n\n");
			}
			out.print("}\n\n");
		}

		// print out for debugging purposes
		if (DEBUG) {
			System.out.println("** Subsystems **");
			for (String subsys : subsystemActivitiesMap.keySet()) {
				System.out.printf("%s:\t{", subsys);
				List<EActivityDef> activities = subsystemActivitiesMap.get(subsys);
				for (EActivityDef act : activities) {
				    System.out.printf("%s ", NDDLUtil.escape(act.getName()));
				}
				System.out.print("}\n");
			}		
			System.out.print("\n\n** Defined States **\n\n");
			for (String stateName : stateValuesMap.keySet()) {
				System.out.printf("%s:\t{", stateName);
				for (String val : stateValuesMap.get(stateName)) {
					System.out.printf("%s ", val);
				}
				System.out.print("}\n");
			}		
			System.out.print("\n\n** Defined Negated States **\n\n");	
			if (stateNotValuesMap.keySet() != null) {
				for (String stateName : stateNotValuesMap.keySet()) {
					System.out.printf("%s:\t{", stateName);
					for (String val : stateNotValuesMap.get(stateName)) {
						System.out.printf("%s ", val);
					}
					System.out.print("}\n");
				}		
			}
			System.out.print("\n\n** Enumeration State Resource Effects by state **");
			for (String state : enumActEffectMap.keySet()) {
				System.out.printf("\n\n%s:  ", state);
				for (String[] entry : enumActEffectMap.get(state)) {
					System.out.printf("(%s, %s) ", entry[0], entry[1]);
				}
			}
			System.out.print("\n\n** Threshold State Resource Effects by act **");
			for (String act : actThresholdEffectMap.keySet()) {
				System.out.printf("\n\n%s:  ", act);
				for (String[] entry : actThresholdEffectMap.get(act)) {
					System.out.printf("(%s, %s) ", entry[0], entry[1]);
				}
			}
			System.out.print("\n\n** Enumeration State Resource Requirements by state **");
			for (String state : enumActRequireMap.keySet()) {
				System.out.printf("\n\n%s:  ", state);
				for (String[] entry : enumActRequireMap.get(state)) {
					System.out.printf("(%s, %s) ", entry[0], entry[1]);
				}
			}		System.out.print("\n\n** Enumeration Negated State Resource Requirements by state **");
			if (enumActNotRequireMap.keySet() != null) {
				for (String state : enumActNotRequireMap.keySet()) {
					System.out.printf("\n\n%s:  ", state);
					if (enumActNotRequireMap.get(state) != null) {
						for (String[] entry : enumActNotRequireMap.get(state)) {
							System.out.printf("(%s, %s) ", entry[0], entry[1]);
						}
					}
					System.out.print("\n\n** Threshold State Resource Requirements by act **");
					if (actThresholdRequireMap.keySet() != null) {
						for (String act : actThresholdRequireMap.keySet()) {
							System.out.printf("\n\n%s:  ", act);
							if (actThresholdRequireMap.get(act) != null) {
								for (String[] entry : actThresholdRequireMap.get(act)) {
									System.out.printf("(%s, %s) ", entry[0], entry[1]);
								}
							}
						}
					}
					System.out.print("\n\n");
				}
			}
		}
		
		for (ADTranslator translator : adTranslators)
			translator.writeObjects(out);
	}
	
	/**
	 * Function that writes out the model file specified by the given output
	 * stream and based on a given Activity Dictionary that has already been
	 * parsed
	 * 
	 * @param shortName
	 *            of oStrm
	 */

	public void writeCompats(OutputStream oStrm, String objFileName) {
		PrintStream out = new PrintStream(oStrm);
		out.printf("#include \"PlannerConfig.nddl\"\n"
				+ "#include \"Resources.nddl\"\n"
				+ "#include \"%s\"\n\n\n"
				+ "#include \"Boolean_Object.nddl\"\n",
				objFileName);
		this.writePassiveCompats(oStrm);
		if (ModelGenerator.getPreferences().useResourceSolvingForStateConstraints()) {
		    this.writeActiveTimelineCompats(oStrm);
		    this.writeActiveResourceCompats(oStrm);
		} else {
		    this.writeActiveCompats(oStrm);
		}
		
		for (ADTranslator translator : adTranslators)
			translator.writeCompats(out);
	}

	/**
	 * Function that writes out the passive compatibilities into the model file
	 * specified by the given output stream and based on a given Activity
	 * Dictionary that has already been parsed
	 * 
	 * @param shortName
	 *            of oStrm
	 */

	public void writePassiveCompats(OutputStream oStrm) {
		PrintStream out = new PrintStream(oStrm);
		String startVar;
		String endVar;
		String qVar;
		String stateName;
		String state;
		String atStartValue;
		String atEndValue;
		String claimName;
		@SuppressWarnings("unused")
		String objrefName;
		String shareName;
		List<String> allowedValues;

		// first handle the incon activity, then all others
		out.print("InitialConds::incon {\n" +
				"  if (scheduled == true) {\n" + 
				"    if (Enable_Passive_Checking == true) {\n" +
				"        eq(inconStart, start);\n\n");
		// PHM 05/10/2011 Declare the negated states as locals if they exist
		for (String stat : stateNames) {
		    Set<String> stateNotValues = stateNotValuesMap.get(stat);
		    if (stateNotValues != null && stateNotValues.size() > 0) {
			   for (String val : stateNotValues) {
			       out.printf("    float \t _not_%s_%s;\n",
				       NDDLUtil.escape(stat), NDDLUtil.escape(val));
			   }
		    }
		}
		// PHM 05/10/2011 Set the negated state values
		for (String stat : stateNames) {
		    Set<String> stateNotValues = stateNotValuesMap.get(stat);
		    if (stateNotValues != null && stateNotValues.size() > 0) {
			   for (String val : stateNotValues) {
			       out.printf("    sum(_not_%s_%s, _%s_%s, STATE_COND_TRUE);\n",
				       NDDLUtil.escape(stat), NDDLUtil.escape(val),
				       NDDLUtil.escape(stat), NDDLUtil.escape(val));
			   }
		    }
		}
		for (String claim : claimNames) {
			claimName = NDDLUtil.escape(claim);
			startVar = NDDLUtil.escape("i" + varN++);
			out.printf("\n" + "\t starts(%s.produce %s);\n"
					+ "\t eq(%s.quantity, _%s);\n", claimName, startVar,
					startVar, claimName);
		}
		for (String share : shareNames) {
			startVar = NDDLUtil.escape("i" + varN++);
			shareName = NDDLUtil.escape(share);
			out.printf("\n" + "\t starts(%s.produce %s);\n"
					+ "\t eq(%s.quantity, _%s);\n", shareName, startVar,
					startVar, shareName);
		}
		// note that we use state-value pairs
		// for each state, one and only one value should be TRUE
		for (String resource : stateNames) {
			List<String> stateValues = stateValuesMap.get(resource);
			if (stateValues != null && stateValues.size() > 0) {
				for (String val : stateValues) {
					String resourceName = NDDLUtil.escape(resource + "_" + val);
					startVar = NDDLUtil.escape("i" + varN++);
					out.printf("\n" + "\t starts(%s.produce %s);\n"
							+ "\t eq(%s.quantity, _%s);\n", resourceName,
							startVar, startVar, resourceName);
				}
			}
			// add in the negated values if they exist
			Set<String> stateNotValues = stateNotValuesMap.get(resource);
			if (stateNotValues != null && stateNotValues.size() > 0) {
				for (String val : stateNotValues) {
					String resourceName = NDDLUtil.escape("not_" + resource + "_" + val);
					startVar = NDDLUtil.escape("i" + varN++);
					out.printf("\n" + "\t starts(%s.produce %s);\n"
							+ "\t eq(%s.quantity, _%s);\n", resourceName,
							startVar, startVar, resourceName);
				}
			}
		}
		out.print("    }\n  }\n}\n\n");

		
		// handle passive compat for each dynamic object claim
		for (String objdef : objectDefNames) {
			startVar = "o" + varN++;
			endVar = "o" + varN++;
			qVar = "q" + varN++;
			out.printf("\n%s::Assign_%s {\n" +
					"  if (isSingleton(object)) {\n" +
					"    if (scheduled == true) {\n" +
					"      if (Enable_Passive_Checking == true) {\n" +
					"          condleq(afterIncon, inconStart, start);\n" +
					"          float %s;\n" +
			        "          eq(%s, afterIncon);\n" +
					"          starts(object.passive.consume %s);\n" +
					"          eq(%s.quantity, %s);\n\n" +
					"          ends(object.passive.produce %s);\n" +
					"          eq(%s.quantity, 1.0);\n\n         }\n     }\n  }\n}\n\n", 
					objdef, objdef, qVar, qVar, startVar, startVar, qVar, endVar, endVar);
		}

		// Due to the incon guard, have to handle the start transitions and end
		// transitions separately
		// ** Since we've eliminated the incon guard, this is no longer necessary,
		// but code 
		
		for (EActivityDef activityDef : activityDefs) {
//			if ((activityDef.getExpansion() == null || activityDef
//					.getExpansion().getSubActivities() == null)
//					&& (activityDef.getClaims() != null
//							|| activityDef.getSharedReservations() != null
//							|| (activityDef.getRequirements() != null && activityDef
//									.getRequirements().getStateRequirements() != null) || (activityDef
//							.getEffects() != null && activityDef.getEffects()
//							.getStateEffects() != null))) {
				
				if (!activityDef.getClaimableEffects().isEmpty()
					|| !activityDef.getSharedEffects().isEmpty()
					|| !activityDef.getStateRequirements().isEmpty()
					|| !activityDef.getStateEffects().isEmpty()) {
				out.printf("%s::%s {\n" 
						+ "  if (scheduled == true) {\n"
						+ "    if (Enable_Passive_Checking == true) {\n\n",
						activitySubsystemMap.get(
							NDDLUtil.escape(activityDef.getName())), 
							NDDLUtil.escape(activityDef.getName()));
				// first process only the start transitions for the activity

				// handle claims
				for (EClaimableEffect claim : activityDef.getClaimableEffects()) {
					claimName = NDDLUtil.escape(claim.getName());
					if (claimNames.contains(claimName)) {
						startVar = "c" + varN++;
						qVar = "q" + varN++;
						out.printf("\n"
								+ "          condleq(afterIncon, inconStart, start);\n"
								+ "          float %s;\n"
						        + "          eq(%s, afterIncon);\n"
								+ "          starts(%s.consume %s);\n"
								+ "          eq(%s.quantity, %s);\n",
								qVar, qVar, claimName, startVar, startVar, qVar);
					} else {
						System.err.print("\n* Undefined claim " + claimName
								+ " in activity "
								+ NDDLUtil.escape(activityDef.getName())
								+ " *\n\n");
					}
				}
				// handle shared reservations
				for (ESharableResourceEffect share : activityDef.getSharedEffects()) {
					shareName = NDDLUtil.escape(share.getName());
					if (shareNames.contains(shareName)) {
						startVar = "r" + varN++;
						qVar = "q" + varN++;
						int reservations = share.getReservations();
						if (reservations > 0) {
							out.printf("\n"
									+ "          condleq(afterIncon, inconStart, start);\n"
									+ "          float %s;\n"
							        + "          product(%s, %d, afterIncon);\n"
							        + "          starts(%s.consume %s);\n"
									+ "          eq(%s.quantity, %s);\n",
									qVar, qVar, reservations, shareName, startVar, startVar, qVar);
						}
					} else {
						System.err.print("\n* Undefined share " + shareName
								+ " in activity "
								+ NDDLUtil.escape(activityDef.getName())
								+ " *\n\n");
					}
				}
				// handle state requirements
				for (EStateRequirement stateReq : activityDef.getStateRequirements()) {
					// period = 0 means RequiresThroughout; period = 1 means
					// RequiresBeforeStart
					// we only handle RequiresThroughout
				    state = NDDLUtil.escape(stateReq.getName());
					if (stateNames.contains(state)) {
						if (stateReq.getPeriod() == Period.REQUIRES_THROUGHOUT) {
							// For requirements, Enum and Threshold states are no longer 
							// handled identically due to negation and disjunction
							if (stateTypesMap.get(state).equals("Enum")) {
								if (stateReq.getRequiredState() != null) {
									stateName = NDDLUtil.escape(state + "_" + stateReq.getRequiredState());
									startVar = "r" + varN++;
									qVar = "q" + varN++;
									out.printf("\n"
											+ "          condleq(afterIncon, inconStart, start);\n"
											+ "          float %s;\n"
									        + "          eq(%s, afterIncon);\n"	
									        +"           starts(%s.consume %s);\n"
											+ "          eq(%s.quantity, %s);\n",
											qVar, qVar, stateName, startVar, startVar, qVar);
								} else if (stateReq.getDisallowedState() != null) {
									stateName = NDDLUtil.escape("not_" + state + "_" + stateReq.getDisallowedState());
									startVar = "r" + varN++;
									qVar = "q" + varN++;
									out.printf("\n"
											+ "          condleq(afterIncon, inconStart, start);\n"
											+ "          float %s;\n"
									        + "          eq(%s, afterIncon);\n"	
									        +"           starts(%s.consume %s);\n"
											+ "          eq(%s.quantity, %s);\n",
											qVar, qVar, stateName, startVar, startVar, qVar);
								} else if (stateReq.getAllowedStates() != null && 
										   stateReq.getAllowedStates().size() > 0) {
									allowedValues = stateReq.getAllowedStates();
									for (String val : stateValuesMap.get(state)) {
										if (!allowedValues.contains(val)) {
											stateName = NDDLUtil.escape("not_" + state + "_" + val);
											startVar = "r" + varN++;
											qVar = "q" + varN++;
											out.printf("\n"
													+ "          condleq(afterIncon, inconStart, start);\n"
													+ "          float %s;\n"
											        + "          eq(%s, afterIncon);\n"	
											        +"           starts(%s.consume %s);\n"
													+ "          eq(%s.quantity, %s);\n",
													qVar, qVar, stateName, startVar, startVar, qVar);
										}
									}
								} else {System.err.print("*Required resource " + state
											+ " did not have a value specified*\n\n");
									}
								} else if (stateTypesMap.get(state).equals("Threshold")) {
								stateName = NDDLUtil.escape(state + "_" + stateReq.getRequiredState());
								startVar = "r" + varN++;
								qVar = "q" + varN++;
								out.printf("\n"
										+ "          condleq(afterIncon, inconStart, start);\n"
										+ "          float %s;\n"
								        + "          eq(%s, afterIncon);\n"	
								        +"           starts(%s.consume %s);\n"
										+ "          eq(%s.quantity, %s);\n",
										qVar, qVar, stateName, startVar, startVar, qVar);
							} else {
								System.err.print("*Required resource "
										+ state
										+ " is not of type Enum nor ThresholdEnum*\n\n");
							}
						} 
					} else {
					System.err.print("\n* Undefined state " + state
							+ " in activity "
							+ NDDLUtil.escape(activityDef.getName())
							+ " *\n\n");
					}
				}

				// handle state effects
				for (EStateResourceEffect<?> effect : activityDef.getStateEffects()) {
				    state = NDDLUtil.escape(effect.getName());
					atStartValue = effect.getStartEffect();
					// for effects, Enum and Threshold states are handled
					// differently
					String stateTypeName = stateTypesMap.get(state);
					if (stateTypeName != null
							&& stateTypeName.equals("Enum")) {
						if (atStartValue != null) {

						  writeEnumStateEffectSection(out, state, atStartValue, "starts");

						}
					} else if (stateTypeName != null
							&& stateTypeName.equals("Threshold")) {
						// we assume that there is an atStart value
						// and that atEnd we retract the effect

						// make all LOWER values False at start and True at
						// end
						for (String val : stateValuesMap.get(state)) {
							if (val.equals(atStartValue)) {
								break;
							}
							stateName = state + "_" + val;
							startVar = "s" + varN++;
							qVar =  "q" + varN++;
							out.printf("\n"
									+ "          condleq(afterIncon, inconStart, start);\n"
									+ "          float %s;\n"
							        + "          product(%s, STATE_COND_FALSE, afterIncon);\n"
									+ "          starts(%s.consume %s);\n"
									+ "          eq(%s.quantity, %s);\n",
									qVar, qVar, stateName, startVar, startVar, qVar);
							out.println();
						}
					}
				}
					
				// now process the end transitions for the activity

				// handle claims
				for (EClaimableEffect claim : activityDef.getClaimableEffects()) {
				    claimName = NDDLUtil.escape(claim.getName());
					if (claimNames.contains(claimName)) {
						endVar = "c" + varN++;
						out.printf("\n\t\tends(%s.produce %s);\n"
								+ "\t\teq(%s.quantity, 1.0);\n", NDDLUtil
								.escape(claimName),
								NDDLUtil.escape(endVar), NDDLUtil
										.escape(endVar));
					} else {
						System.err.print("\n* Undefined claim " + claimName
								+ " in activity "
								+ NDDLUtil.escape(activityDef.getName())
								+ " *\n\n");
					}
				}
				// handle shared reservations
				for (ESharableResourceEffect share : activityDef.getSharedEffects()) {
				    shareName = NDDLUtil.escape(share.getName());
					if (shareNames.contains(shareName)) {
						endVar = "r" + varN++;
						int reservations = share.getReservations();
						if (reservations > 0) {
							out.printf("\n\t\tends(%s.produce %s);\n"
									+ "\t\teq(%s.quantity, %d);\n",
									NDDLUtil.escape(shareName), NDDLUtil
											.escape(endVar), NDDLUtil
											.escape(endVar), reservations);
						}
					} else {
						System.err.print("\n* Undefined share " + shareName
								+ " in activity "
								+ NDDLUtil.escape(activityDef.getName())
								+ " *\n\n");
					}
				}
				
				// handle state requirements
				for (EStateRequirement stateReq : activityDef.getStateRequirements()) {
					// period = 0 means RequiresThroughout; period = 1 means
					// RequiresBeforeStart
					// we only handle RequiresThroughout
				    state = NDDLUtil.escape(stateReq.getName());
					if (stateNames.contains(state)) {
						if (stateReq.getPeriod() == Period.REQUIRES_THROUGHOUT) {
							// For requirements, Enum and Threshold states
							// are no longer handled identically due to negation and disjunction
							if (stateTypesMap.get(state).equals("Enum")) {
								if (stateReq.getRequiredState() != null) {
									stateName = state + "_" + stateReq.getRequiredState();
									endVar = NDDLUtil.escape("r" + varN++);
									out.printf("\n\t\tends(%s.produce %s);\n"
											+ "\t\teq(%s.quantity, 1.0);\n",
											NDDLUtil.escape(stateName), endVar,
											endVar);
									// if stateName is the CPUwindow predicate, then
									// add the Window capability
									if (stateName.equals(CPUwindow)) {
										startVar = NDDLUtil.escape("w" + varN++);
										out.printf("\n\t\tany(CPU_Windows.produce %s);\n"
																+ "\t\teq(%s.quantity, 1.0);\n"
																+ "\t\ttemporalDistance(%s.time, CPU_BOOT_DUR, start);\n",
														startVar, startVar,
														startVar);
										endVar = NDDLUtil.escape("w" + varN++);
										out.printf("\t\tany(CPU_Windows.consume %s);\n"
																+ "\t\teq(%s.quantity, 1.0);\n"
																+ "\t\ttemporalDistance(end, POST_CPU_WINDOW, %s.time);\n",
														endVar, endVar, endVar);
									}
								} else if (stateReq.getDisallowedState() != null) {
									stateName = "not_" + state + "_" + stateReq.getDisallowedState();
									endVar = NDDLUtil.escape("r" + varN++);
									out.printf("\n\t\tends(%s.produce %s);\n"
											+ "\t\teq(%s.quantity, 1.0);\n",
											NDDLUtil.escape(stateName), endVar,
											endVar);
								} else if (stateReq.getAllowedStates() != null && 
										   stateReq.getAllowedStates().size() > 0) {
									allowedValues = stateReq.getAllowedStates();
									for (String val : stateValuesMap.get(state)) {
										if (!allowedValues.contains(val)) {
											stateName = "not_" + state + "_" + val;
											endVar = NDDLUtil.escape("r" + varN++);
											out.printf("\n\t\tends(%s.produce %s);\n"
													+ "\t\teq(%s.quantity, 1.0);\n",
													NDDLUtil.escape(stateName), endVar,
													endVar);
										}
									}
								} else {System.err.print("*Required resource " + state
										+ " did not have a value specified*\n\n");
								}
							} else if (stateTypesMap.get(state).equals("Threshold")) {
								stateName = state + "_" + stateReq.getRequiredState();
								endVar = NDDLUtil.escape("r" + varN++);
								out.printf("\n\t\tends(%s.produce %s);\n"
										+ "\t\teq(%s.quantity, 1.0);\n",
										NDDLUtil.escape(stateName), endVar,
										endVar);
							}
						}
					} else {
						System.err.print("\n* Undefined state " + state
								+ " in activity "
								+ NDDLUtil.escape(activityDef.getName())
								+ " *\n\n");
					}
				}
				
				
				// handle state effects
				for (EStateResourceEffect<?> effect : activityDef.getStateEffects()) {
				    state = NDDLUtil.escape(effect.getName());
					atEndValue = effect.getEndEffect();
					// for effects, Enum and Threshold states are handled
					// differently
					String stateTypeName = stateTypesMap.get(state);
					if (stateTypeName != null
							&& stateTypeName.equals("Enum")) {
						if (atEndValue != null) {
						  writeEnumStateEffectSection(out, state, atEndValue, "ends");
						}
					} else if (stateTypeName != null
							&& stateTypeName.equals("Threshold")) {
						// we assume that there is an atStart value
						// and that atEnd we retract the effect

						// make all LOWER values False at start and True at
						// end
						for (String val : stateValuesMap.get(state)) {
							if (val.equals(atEndValue)) {
								break;
							}
							stateName = NDDLUtil.escape(state + "_" + val);
							endVar = NDDLUtil.escape("e" + varN++);
							out.printf("\t\tends(%s.produce %s);\n\t\teq(%s.quantity, STATE_COND_TRUE);\n",
											stateName, endVar, endVar);
							out.println();
						}
					}
				}
				out.print("\n    }\n  }\n}\n\n");
			}
		}
	}

	/**
	 * Function that writes out the active compatibilities into the model file
	 * specified by the given output stream and based on a given Activity
	 * Dictionary that has already been parsed
	 * 
	 * @param shortName
	 *            of oStrm
	 */

	public void writeActiveResourceCompats(OutputStream oStrm) {
		PrintStream out = new PrintStream(oStrm);
		String actName;
		String startVar;
		String endVar;
		String qVar;
		String stateName;
		String state;
		String atStartValue;
		String atEndValue;
		@SuppressWarnings("unused")
		String objrefName;
		String shareName;
		List<String> allowedValues;

		// first handle the incon activity, then all others
		out.print("InitialConds::incon {\n" +
				"  if (scheduled == true) {\n" + 
				"      if (Enable_Active_Enforcement == true) {\n" +
				"        if (subSolved == true) {\n" +
				"          if (enforced == true) {\n");
		// PHM 04/17/2013 Prevent fixViolations from moving activity to before the INCON
		out.print("            \neq(inconStart, reftime);\n");
		// PHM 05/10/2011 Declare the negated states as locals if they exist
		for (String stat : stateNames) {
		    Set<String> stateNotValues = stateNotValuesMap.get(stat);
		    if (stateNotValues != null && stateNotValues.size() > 0) {
			   for (String val : stateNotValues) {
			       out.printf("    float \t _not_%s_%s;\n",
				       NDDLUtil.escape(stat), NDDLUtil.escape(val));
			   }
		    }
		}
		// PHM 05/10/2011 Set the negated state values
		for (String stat : stateNames) {
		    Set<String> stateNotValues = stateNotValuesMap.get(stat);
		    if (stateNotValues != null && stateNotValues.size() > 0) {
			   for (String val : stateNotValues) {
			       out.printf("    sum(_not_%s_%s, _%s_%s, STATE_COND_TRUE);\n",
				       NDDLUtil.escape(stat), NDDLUtil.escape(val),
				       NDDLUtil.escape(stat), NDDLUtil.escape(val));
			   }
		    }
		}
		for (String share : shareNames) {
			startVar = NDDLUtil.escape("i" + varN++);
			shareName = NDDLUtil.escape(share);
			out.printf("\n" + "\t starts(%s.produce %s);\n"
					+ "\t eq(%s.quantity, _%s);\n", shareName, startVar,
					startVar, shareName);
		}
		// note that we use state-value pairs
		// for each state, one and only one value should be TRUE
		for (String resource : stateNames) {
			List<String> stateValues = stateValuesMap.get(resource);
			if (stateValues != null && stateValues.size() > 0) {
				for (String val : stateValues) {
					String resourceName = NDDLUtil.escape(resource + "_" + val);
					startVar = NDDLUtil.escape("i" + varN++);
					out.printf("\n" + "\t starts(%s.produce %s);\n"
							+ "\t eq(%s.quantity, _%s);\n", resourceName,
							startVar, startVar, resourceName);
				}
			}
			// add in the negated values if they exist
			Set<String> stateNotValues = stateNotValuesMap.get(resource);
			if (stateNotValues != null && stateNotValues.size() > 0) {
				for (String val : stateNotValues) {
					String resourceName = NDDLUtil.escape("not_" + resource + "_" + val);
					startVar = NDDLUtil.escape("i" + varN++);
					out.printf("\n" + "\t starts(%s.produce %s);\n"
							+ "\t eq(%s.quantity, _%s);\n", resourceName,
							startVar, startVar, resourceName);
				}
			}
		}
		out.print("    }\n  }\n}\n}\n}\n\n");

		// Due to the afterIncon check, have to handle the start transitions and end
		// transitions separately
		
		for (EActivityDef activityDef : activityDefs) {
			actName = NDDLUtil.escape(activityDef.getName());
				
				if (!activityDef.getSharedEffects().isEmpty()
					|| !activityDef.getStateRequirements().isEmpty()
					|| !activityDef.getStateEffects().isEmpty()) {
				out.printf("%s::%s {\n" 
						+ "  if (scheduled == true) {\n"
						+ "    if (Enable_Active_Enforcement == true) {\n"
						+ "      if (subSolved == true) {\n"
						+ "        if (enforced == true) {\n\n",
						activitySubsystemMap.get(
							NDDLUtil.escape(activityDef.getName())), 
							NDDLUtil.escape(activityDef.getName()));

				// handle shared reservations
				for (ESharableResourceEffect share : activityDef.getSharedEffects()) {
					shareName = NDDLUtil.escape(share.getName());
					if (shareNames.contains(shareName)) {
						startVar = "r" + varN++;
						qVar = "q" + varN++;
						endVar = "r" + varN++;
						int reservations = share.getReservations();
						if (reservations > 0) {

						    out.printf("    if (Enforce_sx_%s == true) {\n"
							       + "        if (myEnforce.Enforce_sx_%s == true) {\n",
							       shareName, shareName);

						    out.printf("\n"
							       + "          condleq(afterIncon, inconStart, start);\n"
							       + "          float %s;\n"
							       + "          product(%s, %d, afterIncon);\n"
							       + "          starts(%s.consume %s);\n"
							       + "          eq(%s.quantity, %s);\n",
							       qVar, qVar, reservations, shareName, startVar, startVar, qVar);
						    out.printf("\n\t\tends(%s.produce %s);\n"
							       + "\t\teq(%s.quantity, %d);\n",
							       NDDLUtil.escape(shareName), NDDLUtil
							       .escape(endVar), NDDLUtil
							       .escape(endVar), reservations);

						    out.printf("\n       }\n    }\n\n");

						}
					} else {
						System.err.print("\n* Undefined share " + shareName
								+ " in activity "
								+ NDDLUtil.escape(activityDef.getName())
								+ " *\n\n");
					}
				}
				
				// handle state requirements
				for (EStateRequirement stateReq : activityDef.getStateRequirements()) {
					// period = 0 means RequiresThroughout; period = 1 means
					// RequiresBeforeStart
					// we only handle RequiresThroughout
				    state = NDDLUtil.escape(stateReq.getName());
					if (stateNames.contains(state)) {
						if (stateReq.getPeriod() == Period.REQUIRES_THROUGHOUT) {
							// For requirements, Enum and Threshold states are no longer 
							// handled identically due to negation and disjunction
							if (stateTypesMap.get(state).equals("Enum")) {

							        // PHM 12/02/2011 Replace mutex guards per John Bresina suggestion
							        // Keep _mx_ substring used to identify state flight rules.
							        out.printf("    if (Enforce_mx_%s__%s == true) {\n"
									   + "        if (myEnforce.Enforce_mx_%s__%s == true) {\n",
									   actName, state, actName, state);

								if (stateReq.getRequiredState() != null) {
									stateName = NDDLUtil.escape(state + "_" + stateReq.getRequiredState());
									startVar = "r" + varN++;
									qVar = "q" + varN++;
									out.printf("\n"
											+ "          condleq(afterIncon, inconStart, start);\n"
											+ "          float %s;\n"
									        + "          eq(%s, afterIncon);\n"	
									        +"           starts(%s.consume %s);\n"
											+ "          eq(%s.quantity, %s);\n",
											qVar, qVar, stateName, startVar, startVar, qVar);

									endVar = NDDLUtil.escape("r" + varN++);
									out.printf("\n\t\tends(%s.produce %s);\n"
											+ "\t\teq(%s.quantity, 1.0);\n",
											NDDLUtil.escape(stateName), endVar,
											endVar);
								} else if (stateReq.getDisallowedState() != null) {
									stateName = NDDLUtil.escape("not_" + state + "_" + stateReq.getDisallowedState());
									startVar = "r" + varN++;
									qVar = "q" + varN++;
									out.printf("\n"
											+ "          condleq(afterIncon, inconStart, start);\n"
											+ "          float %s;\n"
									        + "          eq(%s, afterIncon);\n"	
									        +"           starts(%s.consume %s);\n"
											+ "          eq(%s.quantity, %s);\n",
											qVar, qVar, stateName, startVar, startVar, qVar);

									endVar = NDDLUtil.escape("r" + varN++);
									out.printf("\n\t\tends(%s.produce %s);\n"
											+ "\t\teq(%s.quantity, 1.0);\n",
											NDDLUtil.escape(stateName), endVar,
											endVar);
								} else if (stateReq.getAllowedStates() != null && 
										   stateReq.getAllowedStates().size() > 0) {
									allowedValues = stateReq.getAllowedStates();
									for (String val : stateValuesMap.get(state)) {
										if (!allowedValues.contains(val)) {
											stateName = NDDLUtil.escape("not_" + state + "_" + val);
											startVar = "r" + varN++;
											qVar = "q" + varN++;
											out.printf("\n"
													+ "          condleq(afterIncon, inconStart, start);\n"
													+ "          float %s;\n"
											        + "          eq(%s, afterIncon);\n"	
											        +"           starts(%s.consume %s);\n"
													+ "          eq(%s.quantity, %s);\n",
													qVar, qVar, stateName, startVar, startVar, qVar);

											endVar = NDDLUtil.escape("r" + varN++);
											out.printf("\n\t\tends(%s.produce %s);\n"
													+ "\t\teq(%s.quantity, 1.0);\n",
													NDDLUtil.escape(stateName), endVar,
													endVar);
										}
									}
								} else {System.err.print("*Required resource " + state
											+ " did not have a value specified*\n\n");
									}

								out.printf("\n       }\n    }\n\n");

							} else if (stateTypesMap.get(state).equals("Threshold")) {

							        // PHM 12/02/2011 Keep existing threshold guards
							        out.printf("    if (Enforce_%s == true) {\n"
									   + "        if (myEnforce.Enforce_%s == true) {\n",
									   state, state);

								stateName = NDDLUtil.escape(state + "_" + stateReq.getRequiredState());
								startVar = "r" + varN++;
								qVar = "q" + varN++;
								out.printf("\n"
										+ "          condleq(afterIncon, inconStart, start);\n"
										+ "          float %s;\n"
								        + "          eq(%s, afterIncon);\n"	
								        +"           starts(%s.consume %s);\n"
										+ "          eq(%s.quantity, %s);\n",
										qVar, qVar, stateName, startVar, startVar, qVar);

								endVar = NDDLUtil.escape("r" + varN++);
								out.printf("\n\t\tends(%s.produce %s);\n"
										+ "\t\teq(%s.quantity, 1.0);\n",
										NDDLUtil.escape(stateName), endVar,
										endVar);

								out.printf("\n       }\n    }\n\n");

							} else {
								System.err.print("*Required resource "
										+ state
										+ " is not of type Enum nor ThresholdEnum*\n\n");
							}
						} 
					} else {
					System.err.print("\n* Undefined state " + state
							+ " in activity "
							+ NDDLUtil.escape(activityDef.getName())
							+ " *\n\n");
					}
				}
				
				
				// handle start state effects
				for (EStateResourceEffect<?> effect : activityDef.getStateEffects()) {
				    state = NDDLUtil.escape(effect.getName());
					atStartValue = effect.getStartEffect();
					// for effects, Enum and Threshold states are handled
					// differently
					String stateTypeName = stateTypesMap.get(state);
					if (stateTypeName != null
							&& stateTypeName.equals("Enum")) {

					  if (atStartValue != null) {
					    // PHM 12/02/2011 Replace mutex guards per John Bresina suggestion
					    // Keep _mx_ substring used to identify state flight rules.
					    out.printf("    if (Enforce_mx_%s__%s == true) {\n"
						       + "        if (myEnforce.Enforce_mx_%s__%s == true) {\n",
						       actName, state, actName, state);
					    writeEnumStateEffectSection(out, state, atStartValue, "starts");
					    out.printf("\n       }\n    }\n\n");
					  }

					} else if (stateTypeName != null
							&& stateTypeName.equals("Threshold")) {

					        // PHM 12/02/2011 Keep existing threshold guards
					        out.printf("    if (Enforce_%s == true) {\n"
							   + "        if (myEnforce.Enforce_%s == true) {\n",
							   state, state);

						// we assume that there is an atStart value
						// and that atEnd we retract the effect

						// make all LOWER values False at start and True at
						// end
						for (String val : stateValuesMap.get(state)) {
							if (val.equals(atStartValue)) {
								break;
							}
							stateName = state + "_" + val;
							startVar = "s" + varN++;
							qVar =  "q" + varN++;
							out.printf("\n"
									+ "          condleq(afterIncon, inconStart, start);\n"
									+ "          float %s;\n"
							        + "          product(%s, STATE_COND_FALSE, afterIncon);\n"
									+ "          starts(%s.consume %s);\n"
									+ "          eq(%s.quantity, %s);\n",
									qVar, qVar, stateName, startVar, startVar, qVar);
							out.println();
						}

						out.printf("\n       }\n    }\n\n");

					}
				}
					
				// handle end state effects
				for (EStateResourceEffect<?> effect : activityDef.getStateEffects()) {
				    state = NDDLUtil.escape(effect.getName());
					atEndValue = effect.getEndEffect();
					// for effects, Enum and Threshold states are handled
					// differently
					String stateTypeName = stateTypesMap.get(state);
					if (stateTypeName != null
							&& stateTypeName.equals("Enum")) {

					  if (atEndValue != null) {

					    // PHM 12/02/2011 Replace mutex guards per John Bresina suggestion
					    // Keep _mx_ substring used to identify state flight rules.
					    out.printf("    if (Enforce_mx_%s__%s == true) {\n"
						       + "        if (myEnforce.Enforce_mx_%s__%s == true) {\n",
						       actName, state, actName, state);
					    writeEnumStateEffectSection(out, state, atEndValue, "ends");
					    out.printf("\n       }\n    }\n\n");

					  }

					} else if (stateTypeName != null
							&& stateTypeName.equals("Threshold")) {

					        // PHM 12/02/2011 Keep existing threshold guards
					        out.printf("    if (Enforce_%s == true) {\n"
							   + "        if (myEnforce.Enforce_%s == true) {\n",
							   state, state);

						// we assume that there is an atStart value
						// and that atEnd we retract the effect

						// make all LOWER values False at start and True at
						// end
						for (String val : stateValuesMap.get(state)) {
							if (val.equals(atEndValue)) {
								break;
							}
							stateName = NDDLUtil.escape(state + "_" + val);
							endVar = NDDLUtil.escape("e" + varN++);
							out.printf("\t\tends(%s.produce %s);\n\t\teq(%s.quantity, STATE_COND_TRUE);\n",
											stateName, endVar, endVar);
							out.println();
						}

						out.printf("\n       }\n    }\n\n");

					}
				}
				out.print("\n    }\n  }\n}\n}\n}\n\n");
			}
		}
	}

	public void writeActiveTimelineCompats(OutputStream oStrm) {
		PrintStream out = new PrintStream(oStrm);
		String actName;
		@SuppressWarnings("unused")
		String objrefName;
		String claimName;
		String mxVar;
		String[] mutexActs;
		
		// *** hack for test ***
		// exclusiveActsStatesMap2.get("MASTCAM_MOSAIC_GENERIC__mx__Arm_Unstow").add("Arm_Stationary");

		// debug print out of mutex mapping
		// PHM 12/07/2011 Will be empty for resource solving
		System.out.print("\n* MUTEX Mapping: *\n");
		for (String mutex : exclusiveActsStatesMap.keySet()) {
			mutexActs = mutex.split("__mx__");
			System.out.printf("(%s, %s)\t", mutexActs[0], mutexActs[1]);
			// out.print(mutex + ":\t");
			for (String state : exclusiveActsStatesMap.get(mutex)) {
				System.out.printf("%s ", state);
			}
			System.out.println();
		}
		// System.out.print("\n* MUTEX Mapping: Condition #2 *\n");
		// for (String mutex : exclusiveActsStatesMap2.keySet()) {
		// mutexActs = mutex.split("__mx__");
		// System.out.printf("(%s, %s)\t", mutexActs[0], mutexActs[1]);
		// //out.print(mutex + ":\t");
		// for (String state : exclusiveActsStatesMap2.get(mutex)) {
		// System.out.printf("%s ", state);
		// }
		// System.out.println();
		// }
		System.out.print("\n* Mapping of states to AtEnd Effects *\n");
		for (String state : stateEndEffectsMap.keySet()) {
			System.out.printf("%s:\t", state);
			for (String val : stateEndEffectsMap.get(state)) {
				System.out.printf("%s ", val);
			}
			System.out.println();
		}
		System.out.println();
		// end of debug printout

		// handle active compats on dynamic object claims

		for (String objdef : objectDefNames) {
			mxVar = "x" + varN++;
			out.printf("\n%s::Assign_%s {\n" +
					"  if (isSingleton(object)) {\n" +
					"    if (scheduled == true) {\n" +
					"      if (Enable_Active_Enforcement == true) {\n" +
					"        if (subSolved == true) {\n" +
					"          if (enforced == true) {\n" +
					"            if (Enforce_%s_claim == true) {\n" +
					"              if (myEnforce.Enforce_%s_claim == true) {\n\n" +
					"                  equals(object.active.%s_claim_MX %s);\n" +
					"                  neq(%s.state, MERGED);\n" +
					"              }\n" +
					"            }\n" +
					"          }\n" +
					"        }\n" +
					"      }\n" +
					"    }\n" +
					"  }\n" +
					"}\n\n", objdef, objdef, objdef, objdef, objdef, mxVar, mxVar);
		}
		
		for (EActivityDef activityDef : activityDefs) {
			actName = NDDLUtil.escape(activityDef.getName());
//			if ((activityDef.getExpansion() == null || activityDef
//					.getExpansion().getSubActivities() == null)
//					&& (activityDef.getClaims() != null
//							|| exclusiveActsStatesMap.keySet() != null ||
//							// exclusiveActsStatesMap2.keySet() != null ||
//							actThresholdRequireMap.get(actName) != null || actThresholdEffectMap
//							.get(actName) != null)) {
			
			if (!activityDef.getClaimableEffects().isEmpty()
			                || actThresholdRequireMap.get(actName) != null 
					|| actThresholdEffectMap.get(actName) != null) {
				
				out.printf("%s::%s {\n"
						+ "  if (scheduled == true) {\n"
						+ "    if (Enable_Active_Enforcement == true) {\n"
						+ "      if (subSolved == true) {\n"
						+ "        if (enforced == true) {\n\n",
						activitySubsystemMap.get(actName), actName);

				// handle claims
				for (EClaimableEffect claim : activityDef.getClaimableEffects()) {
				    claimName = NDDLUtil.escape(claim.getName());
					if (claimNames.contains(claimName)) {
						mxVar = "x" + varN++;
						out.printf("\t\t  if (Enforce_%s == true) {\n"
								+ "\t\t      if (myEnforce.Enforce_%s == true) {\n"
								+ "\t\t\t equals(Active_%s.%s_MX %s);\n"
								+ "\t\t\t neq(%s.state, MERGED);\n\t\t     }\n\t\t  }\n\n",
								claimName, claimName, claimName, claimName, mxVar, mxVar);
					} else {
						System.err.print("\n* Undefined claim " + claimName
								+ " in activity " + actName + " *\n\n");
					}

				}

				out.print("        }\n      }\n    }\n  }\n}\n\n");
			}
		}
	}

	public void writeActiveCompats(OutputStream oStrm) {
		PrintStream out = new PrintStream(oStrm);
		String actName;
		@SuppressWarnings("unused")
		String objrefName;
		String claimName;
		String mxVar;
		String[] mutexActs;
		Integer dgN;
		String dgName;
		
		dgN = 1;

		// *** hack for test ***
		// exclusiveActsStatesMap2.get("MASTCAM_MOSAIC_GENERIC__mx__Arm_Unstow").add("Arm_Stationary");

		// debug print out of mutex mapping
		System.out.print("\n* MUTEX Mapping: *\n");
		for (String mutex : exclusiveActsStatesMap.keySet()) {
			mutexActs = mutex.split("__mx__");
			System.out.printf("(%s, %s)\t", mutexActs[0], mutexActs[1]);
			// out.print(mutex + ":\t");
			for (String state : exclusiveActsStatesMap.get(mutex)) {
				System.out.printf("%s ", state);
			}
			System.out.println();
		}
		// System.out.print("\n* MUTEX Mapping: Condition #2 *\n");
		// for (String mutex : exclusiveActsStatesMap2.keySet()) {
		// mutexActs = mutex.split("__mx__");
		// System.out.printf("(%s, %s)\t", mutexActs[0], mutexActs[1]);
		// //out.print(mutex + ":\t");
		// for (String state : exclusiveActsStatesMap2.get(mutex)) {
		// System.out.printf("%s ", state);
		// }
		// System.out.println();
		// }
		System.out.print("\n* Mapping of states to AtEnd Effects *\n");
		for (String state : stateEndEffectsMap.keySet()) {
			System.out.printf("%s:\t", state);
			for (String val : stateEndEffectsMap.get(state)) {
				System.out.printf("%s ", val);
			}
			System.out.println();
		}
		System.out.println();
		// end of debug printout

		// handle active compats on dynamic object claims

		for (String objdef : objectDefNames) {
			mxVar = "x" + varN++;
			out.printf("\n%s::Assign_%s {\n" +
					"  if (isSingleton(object)) {\n" +
					"    if (scheduled == true) {\n" +
					"      if (Enable_Active_Enforcement == true) {\n" +
					"        if (subSolved == true) {\n" +
					"          if (enforced == true) {\n" +
					"            if (Enforce_%s_claim == true) {\n" +
					"              if (myEnforce.Enforce_%s_claim == true) {\n\n" +
					"                  equals(object.active.%s_claim_MX %s);\n" +
					"                  neq(%s.state, MERGED);\n" +
					"              }\n" +
					"            }\n" +
					"          }\n" +
					"        }\n" +
					"      }\n" +
					"    }\n" +
					"  }\n" +
					"}\n\n", objdef, objdef, objdef, objdef, objdef, mxVar, mxVar);
		}
		
		for (EActivityDef activityDef : activityDefs) {
			actName = NDDLUtil.escape(activityDef.getName());
//			if ((activityDef.getExpansion() == null || activityDef
//					.getExpansion().getSubActivities() == null)
//					&& (activityDef.getClaims() != null
//							|| exclusiveActsStatesMap.keySet() != null ||
//							// exclusiveActsStatesMap2.keySet() != null ||
//							actThresholdRequireMap.get(actName) != null || actThresholdEffectMap
//							.get(actName) != null)) {
			
			if (!activityDef.getClaimableEffects().isEmpty()
					|| exclusiveActsStatesMap.keySet() != null 
					|| actThresholdRequireMap.get(actName) != null 
					|| actThresholdEffectMap.get(actName) != null) {
				
				out.printf("%s::%s {\n"
						+ "  if (scheduled == true) {\n"
						+ "    if (Enable_Active_Enforcement == true) {\n"
						+ "      if (subSolved == true) {\n"
						+ "        if (enforced == true) {\n\n",
						activitySubsystemMap.get(actName), actName);

				// handle claims
				for (EClaimableEffect claim : activityDef.getClaimableEffects()) {
				    claimName = NDDLUtil.escape(claim.getName());
					if (claimNames.contains(claimName)) {
						mxVar = "x" + varN++;
						out.printf("\t\t  if (Enforce_%s == true) {\n"
								+ "\t\t      if (myEnforce.Enforce_%s == true) {\n"
								+ "\t\t\t equals(Active_%s.%s_MX %s);\n"
								+ "\t\t\t neq(%s.state, MERGED);\n\t\t     }\n\t\t  }\n\n",
								claimName, claimName, claimName, claimName, mxVar, mxVar);
					} else {
						System.err.print("\n* Undefined claim " + claimName
								+ " in activity " + actName + " *\n\n");
					}

				}

				// handle enum state mutexes
				for (String mutex : exclusiveActsStatesMap.keySet()) {
					mutexActs = mutex.split("__mx__");
					if (actName.equals(mutexActs[0])
							|| actName.equals(mutexActs[1])) {
						mxVar = "x" + varN++;
						if (exclusiveActsStatesMap.get(mutex).size() > 1) {
							dgName = "disjunctGuard" + dgN;
							dgN++;
							out.printf("\t\t  bool %s;\n"
									+  "\t\t  EqualMaximum(%s", dgName, dgName);
							for (String state : exclusiveActsStatesMap
									.get(mutex)) {
								out.printf(", Enforce_%s__%s", mutex, state);
							}
							out.printf(");\n\t\t  if (%s == true) {\n", dgName);
							dgName = "disjunctGuard" + dgN;
							dgN++;
							out.printf("\t\t      bool %s;\n"
									+  "\t\t      EqualMaximum(%s", dgName, dgName);
							for (String state : exclusiveActsStatesMap
									.get(mutex)) {
								out.printf(", myEnforce.Enforce_%s__%s", mutex, state);
							}
							out.printf(");\n\t\t      if (%s == true) {\n", dgName);
						} else {
							String state0 = exclusiveActsStatesMap.get(mutex).get(0);
							out.printf("\t\t  if (Enforce_%s__%s == true) {\n"
									+  "\t\t      if (myEnforce.Enforce_%s__%s == true) {\n",
									mutex, state0, mutex, state0);
						}
						out.printf("\t\t\t equals(Active_%s.%s_MX %s);\n"
								+  "\t\t\t neq(%s.state, MERGED);\n\t\t     }\n\t\t  }\n\n",
										mutex, mutex, mxVar, mxVar);
					}
				}
				// for (String mutex : exclusiveActsStatesMap2.keySet()) {
				// mutexActs = mutex.split("__mx__");
				// if (actName.equals(mutexActs[0]) ||
				// actName.equals(mutexActs[1])) {
				// mxVar = "x" + varN++;
				// if (exclusiveActsStatesMap2.get(mutex).size() > 1) {
				// out.print("\t\t or(disjunctGuard");
				// for (String state : exclusiveActsStatesMap2.get(mutex)) {
				// out.printf(", Enforce_%s__%s", mutex, state);
				// }
				// out.print(");\n\t\t if (disjunctGuard == true) {\n");
				// } else {out.printf("\t\t if (Enforce_%s__%s == true) {\n",
				// mutex, exclusiveActsStatesMap2.get(mutex).get(0));}
				// out.printf("\t\t equals(Active_%s.%s_MX %s);\n" +
				// "\t\t neq(%s.state, MERGED);\n\t\t }\n\n",
				// mutex, mutex, mxVar, mxVar);
				// }
				// }
				// handle threshold state requirements
				if (actThresholdRequireMap.get(actName) != null) {
					for (String[] stateVal : actThresholdRequireMap
							.get(actName)) {
						String stateValName = stateVal[0] + "_" + stateVal[1];
						out.printf("\t\t  if (Enforce_%s == true) {\n"
								+  "\t\t      if (myEnforce.Enforce_%s == true) {\n"
								+  "\t\t\t contained_by(Active_%s.LE_%s);\n\t\t     }\n\t\t  }\n\n",
								stateVal[0], stateVal[0], stateValName, stateValName);
					}
				}
				// handle threshold state effects
				if (actThresholdEffectMap.get(actName) != null) {
					for (String[] stateVal : actThresholdEffectMap.get(actName)) {
						out.printf("\t\t  if (Enforce_%s == true) {\n"
								+  "\t\t      if (myEnforce.Enforce_%s == true) {\n",
								stateVal[0], stateVal[0]);
						for (String val : stateValuesMap.get(stateVal[0])) {
							if (val.equals(stateVal[1])) {
								break;
							}
							String stateValName = stateVal[0] + "_" + val;
							out.printf("\t\t\t contained_by(Active_%s.GT_%s);\n",
											stateValName, stateValName);
						}
						out.print("\t\t     }\n\t\t  }\n\n");
					}
				}
				out.print("        }\n      }\n    }\n  }\n}\n\n");
			}
		}
	}

	/**
	 * Function that writes out the initial state file specified by the given
	 * output stream and based on a given Activity Dictionary that has already
	 * been parsed
	 * 
	 * @param shortName
	 *            of oStrm
	 */

	public void writeInitialState(OutputStream oStrm, String modelFileName) {
		PrintStream out = new PrintStream(oStrm);
		// output common header lines
		out.printf("#include \"%s\"\n\n"
								+ "PlannerConfig plannerConfiguration = new PlannerConfig(-10, +100000000, 10000, 10000);\n\n"
								+ "ContainerObj Containers = new ContainerObj();\n"
								+ "\tContainerObj.close();\n"
								+ "InitialConds Incons = new InitialConds();\n"
								+ "\tInitialConds.close();\n"
								+ "Misc MiscClass = new Misc();\n"
								+ "\tMisc.close();\n\n",
						modelFileName);
		if (CPUwindow != null) {
			out.printf("CPU_Windows Windows = new CPU_Windows();\n",
					modelFileName);
			out.print("\tCPU_Windows.close();\n");
		}
		// instantiate subsystems objects
		for (String subsys : subsystemActivitiesMap.keySet()) {
			out.printf("%s Sys_%s = new %s();\n", subsys, subsys, subsys);
			out.printf("\t%s.close();\n", subsys);
		}
		out.println();
		// initialize state resources (Enum and Threshold)
		// this needs to be coordinated with how Incon Activity is defined
		// for now, make everything is set to 0.0
		// hence, they all must be set in the incon or there will be violations
		for (String state : stateNames) {
			List<String> stateValues = stateValuesMap.get(state);
			if (stateValues != null) {
				for (String val : stateValues) {
					String stateVal = NDDLUtil.escape(state + "_" + val);
					out.printf("%s SC_%s = new %s(0.0);\n", stateVal, stateVal,
							stateVal);
					out.printf("\t%s.close();\n", stateVal);
				}
			}
			// handle the negated state values
			Set<String> stateNotValues = stateNotValuesMap.get(state);
			if (stateNotValues != null) {
				for (String val : stateNotValues) {
					String stateVal = NDDLUtil.escape("not_" + state + "_" + val);
					out.printf("%s SC_%s = new %s(0.0);\n", stateVal, stateVal,
							stateVal);
					out.printf("\t%s.close();\n", stateVal);
				}
			}
		}
		out.println();
		// initialize claimable resources
		// MODIFICATION: everything is now initialized to zero
		// so that the icon must specify the resource's value, not a delta
		for (String claim : claimNames) {
			String claimName = NDDLUtil.escape(claim);
			out.printf("%s UCR_%s = new %s(0.0);\n", claimName, claimName,
					claimName);
			out.printf("\t%s.close();\n", claimName);
		}
		out.println();
		// MODIFICATION: everything is now initialized to zero
		// so that the icon must specify the resource's value, not a delta
		for (ESharableResourceDef share : allShares) {
			String shareName = NDDLUtil.escape(share.getName());
			out.printf("%s MCR_%s = new %s(0.0, %f);\n", shareName, shareName,
					shareName, share.getCapacity().floatValue());
			out.printf("\t%s.close();\n", shareName);
		}
		out.println();
		// instantiate claim enforcement timelines
		for (String claim : claimNames) {
			String claimName = NDDLUtil.escape(claim);
			out.printf("Active_%s TL_%s = new Active_%s();\n", claimName,
					claimName, claimName);
			out.printf("\tActive_%s.close();\n", claimName);
		}
		out.println();
		// instantiate Enum state mutex enforcement timelines
		// PHM 12/07/2011 Empty for Resource Solving
		for (String mutex : exclusiveActsStatesMap.keySet()) {
			String mutexString = NDDLUtil.escape(mutex);
			out.printf("Active_%s TL_%s = new Active_%s();\n", mutexString,
					mutexString, mutexString);
			out.printf("\tActive_%s.close();\n", mutexString);
		}
		// for (String mutex : exclusiveActsStatesMap2.keySet()) {
		// out.printf("Active_%s TL_%s = new Active_%s();\n", mutex, mutex,
		// mutex);
		// }
		// instantiate Threshold enforcement timelines
		for (EStateResourceDef state : allStates) {
		    String stateTypeName = NDDLUtil.escape(state.getName());
			if (stateTypesMap.containsKey(stateTypeName)
					&& stateTypesMap.get(stateTypeName).equals("Threshold")) {
				for (String val : stateValuesMap.get(stateTypeName)) {
					String stateName = NDDLUtil.escape(state.getName() + "_"
							+ val);
					out.printf("Active_%s TL_%s = new Active_%s();\n",
							stateName, stateName, stateName);
					out.printf("\tActive_%s.close();\n", stateName);
				}
			}
		}
		
		for (ADTranslator translator : adTranslators)
			translator.writeInitialState(out);
		
		// The entire DB is no longer closed; instead, each class is closed except Active_Enforcer		
		// out.print("close();\n");		
	}

	public static void main(String[] args) throws Exception {
		try {
			CommandLineArguments cmd = new CommandLineArguments(args);

			// Create a resource set to hold the resources.
			//
			ResourceSet resourceSet = new ResourceSetImpl();

			// Register the package to ensure it is available during loading.
			//
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(DictionaryPackage.eNS_URI, DictionaryPackage.eINSTANCE);
	
			// Construct the URI for the instance file.
			// The argument is treated as a file path only if it denotes an
			// existing file.
			// Otherwise, it's directly treated as a URL.
			//

			File file = new File(cmd.getValue(Option.ACTIVITY_DICTIONARY_FILE));
			URI uri = file.isFile() ? URI.createFileURI(file.getAbsolutePath())
					: URI.createURI(cmd
							.getValue(Option.ACTIVITY_DICTIONARY_FILE));

			try {
				// Demand load resource for this file.
				//
				Resource resource = resourceSet.getResource(uri, true);
				EActivityDictionary eActivityDictionary = (EActivityDictionary) resource.getContents().get(0);

				ParseInterpreter parseInterpreter = new ParseInterpreter(eActivityDictionary,
						cmd.getValue(Option.CPU_STATE), 
						cmd.getValue(Option.CPU_VALUE), 
						cmd.getIntValue(Option.BOOT),
						cmd.getIntValue(Option.SHUTDOWN), 
						cmd.getIntValue(Option.GAP));
				parseInterpreter.interpretAD(false);

				String path = cmd.getValue(Option.OUTPUT_DIRECTORY_PATH);
				path = path + uri.trimFileExtension().lastSegment();

				FileOutputStream objStream = new FileOutputStream(path + "-objects.nddl");
				FileOutputStream modelStream = new FileOutputStream(path + "-model.nddl");
				FileOutputStream initStateStream = new FileOutputStream(path + "-initial-state.nddl");
				objStream.flush();
				modelStream.flush();
				initStateStream.flush();

				parseInterpreter.writeObjects(objStream);
				objStream.close();
				parseInterpreter.writeCompats(modelStream, path + "-objects.nddl");
				modelStream.close();
				parseInterpreter.writeInitialState(initStateStream, path + "-model.nddl");

			} catch (RuntimeException exception) {
				System.out.println("Problem loading " + uri);
				exception.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println(getManPageText());
		}
	}

	private static String getManPageText() {
		StringBuffer buf = new StringBuffer();
		buf.append("usage: " + getCommandName() + " ");
		for (Option option : Option.values()) {
			buf.append(option.toString());
			buf.append("\n        ");
		}
		return buf.toString();
	}

	private static String getCommandName() {
		return "ad2nddl";
	}

	private void removeUnUsedResources(boolean quiet) {
		if (stateValuesMap != null) {
			Set<String> unUsedResources = new LinkedHashSet<String>();
			for (Map.Entry<String, List<String>> element : stateValuesMap
					.entrySet()) {
				String resourceName = element.getKey();
				boolean found = false;
				for (EActivityDef activity : ad.getDefinitions(EActivityDef.class)) {
					for (EStateRequirement stateRequirement : activity.getStateRequirements()) {
					    if (resourceName.equalsIgnoreCase(NDDLUtil.escape(stateRequirement.getName()))) {
							found = true;
							break;
						}
					}	

					for (EStateResourceEffect<?> stateEffect : activity.getStateEffects()) {
					    if (resourceName.equalsIgnoreCase(NDDLUtil.escape(stateEffect.getName()))) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					unUsedResources.add(resourceName);
				}
			}
			for (String resourceName : unUsedResources) {
				if (!quiet) {
					System.out.printf("removing state %s\n", resourceName);
				}
				stateValuesMap.remove(resourceName);
				stateTypesMap.remove(resourceName);
			}
		}

		if (claimNames != null) {
			Set<String> unUsedClaims = new LinkedHashSet<String>();
			for (String claimName : claimNames) {
				boolean found = false;
				for (EActivityDef activity : ad.getDefinitions(EActivityDef.class)) {
					for (EClaimableEffect claim : activity.getClaimableEffects()) {
					    if (claimName.equalsIgnoreCase(NDDLUtil.escape(claim.getName()))) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					unUsedClaims.add(claimName);
				}
			}
			for (String claimName : unUsedClaims) {
				claimNames.remove(claimName);
			}
		}
	}
	
	public String getActivitySubsystem(EActivityDef activity)
	{
		return activitySubsystemMap.get(
				NDDLUtil.escape(activity.getName()));
	}


    protected void writeStateParameters(PrintStream out, EActivityDef activityDef)
    {
    	EList<EStructuralFeature> params = activityDef.getEStructuralFeatures();
    	for (EStructuralFeature param : params) {
    		if ((param instanceof EAttributeParameter)) {
    			EAttributeParameter parm = (EAttributeParameter) param;
    			if (!isEuropaParameter(parm))
    				continue;
    			String pname = NDDLUtil.escape(parm.getName());
    			out.printf("    ");
    			out.printf("string");
    			out.printf(" \t %s;", pname);
    			String units = null;
    			if (parm.getUnitsDisplayName() != null)
    				units = parm.getUnitsDisplayName().toString();
    			if (StringUtils.isBlank(units) && parm.getUnits() != null)
    				units = parm.getUnits().toString();
    			if (StringUtils.isNotBlank(units))
    				out.printf(" // %s", units);
    			out.printf("\n");
    		}
    	}
    }

    private Boolean isEuropaParameter(EAttributeParameter parm)
    {
		if (parm.getDefaultValue() instanceof EEnumLiteral)
			return true;
		if (parm.getEType() instanceof EEnum)
			return true;
		EAnnotation europa = parm.getEAnnotation("europa");
		if (europa != null && europa.getDetails().containsKey("translate"))
			return true;
		return false;    	
    }
    
    private void writeEnumStateEffectSection(PrintStream out,
					     String state,
					     String effectValue,
					     String startsOrEnds)
    {
      // PHM 04/30/2013 Simplify the translation by first getting the
      // INCON'ed STATE_COND_TRUE and STATE_COND_FALSE values (needed
      // only for start effects, can use normal values for end).
		
      String sc_true  = "STATE_COND_TRUE";   // ok for end effects
      String sc_false = "STATE_COND_FALSE";  // ok for end effects

      // PHM 04/30/2013 Set up afterIncon for sc defs.
      // Also prevents activity from being moved before Incon.
      out.printf("\n          condleq(afterIncon, inconStart, start);\n\n");
      
      if (startsOrEnds == "starts") {
		sc_true  = "SC_TRUE_" + varN++;
		sc_false = "SC_FALSE_" + varN++;
	
		out.printf("\n"
			   + "          float %s;\n"
			   + "          product(%s, STATE_COND_TRUE, afterIncon);\n"
			   + "          float %s;\n"
			   + "          product(%s, STATE_COND_FALSE, afterIncon);\n\n",
			   sc_true, sc_true, sc_false, sc_false);
      }

      // PHM 04/29/2013 Modify to handle effect passed in as a
      // variable (parameter or local or global).

      String tokenVar;

      if (!stateValuesMap.get(state).contains(effectValue)) {
		// Not a known state, assume a var of some kind and
		// conditionalize the effect in the nddl code.
		for (String val : stateValuesMap.get(state)) {
		  tokenVar = "s" + varN++;
		  String stateValName = state + "_" + val;
		  out.printf("      if (%s == \"%s\") {\n", effectValue, val);
		  out.printf("        %s(%s.produce %s);\n",
			     startsOrEnds, stateValName, tokenVar);
		  out.printf("        eq(%s.quantity, %s);\n", tokenVar, sc_true);
		  Set<String> notValues = stateNotValuesMap.get(state);
		  if (notValues != null && notValues.contains(val)) {
		    tokenVar = "s" + varN++;
		    out.printf("        %s(not_%s.consume %s);\n",
			       startsOrEnds, stateValName, tokenVar);
		    out.printf("        eq(%s.quantity, %s);\n", tokenVar, sc_false);
		  }
		  out.printf("      } else {\n");
		  out.printf("        %s(%s.consume %s);\n",
			     startsOrEnds, stateValName, tokenVar);
		  out.printf("        eq(%s.quantity, %s);\n", tokenVar, sc_false);
		  notValues = stateNotValuesMap.get(state);
		  if (notValues != null && notValues.contains(val)) {
		    tokenVar = "s" + varN++;
		    out.printf("        %s(not_%s.produce %s);\n",
			       startsOrEnds, stateValName, tokenVar);
		    out.printf("        eq(%s.quantity, %s);\n", tokenVar, sc_true);
		  }
		  out.printf("      }\n");
		}
		return;
      }

      // Otherwise use unconditional nddl code.
      String stateValueName = state + "_" + effectValue;

      // make the effect true
      tokenVar = "e" + varN++;
      out.printf("\n"
		 + "          %s(%s.produce %s);\n"
		 + "          eq(%s.quantity, %s);\n",
		 startsOrEnds, stateValueName, tokenVar, tokenVar, sc_true);
      // make ALL other allowable values False
      for (String val : stateValuesMap.get(state)) {
	if (!val.equals(effectValue)) {
	  String stateValName = state + "_" + val;
	  tokenVar = "s" + varN++;
	  out.printf("\n"
		     + "          %s(%s.consume %s);\n"
		     + "          eq(%s.quantity, %s);\n",
		     startsOrEnds, stateValName, tokenVar,
		     tokenVar, sc_false);
	}
      }
      // handle the negated values for the state
      if (stateNotValuesMap.get(state) != null) {
	for (String notVal : stateNotValuesMap.get(state)) {
	    if (notVal.equals(effectValue)) {
	      // if negated value equal to effect value, make it false
	      String stateName = "not_" + state + "_" + notVal;
	      tokenVar = "s" + varN++;
	      out.printf("\n"
			 + "          %s(%s.consume %s);\n"
			 + "          eq(%s.quantity, %s);\n",
			 startsOrEnds, stateName, tokenVar,
			 tokenVar, sc_false);
	    } else {
	      // if negated value not equal to effect value, make it true
	      String stateName = "not_" + state + "_" + notVal;
	      tokenVar = "e" + varN++;
	      out.printf("\n"
			   + "          %s(%s.produce %s);\n"
			   + "          eq(%s.quantity, %s);\n",
			   startsOrEnds, stateName, tokenVar, tokenVar, sc_true);
	    }
	}
	out.println();
      }
    }

}
