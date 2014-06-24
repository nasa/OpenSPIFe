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
package gov.nasa.ensemble.core.plan.resources.util;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.resources.NumericResourceDef;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.NamedCondition;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.PowerLoad;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.member.UndefinedResource;
import gov.nasa.ensemble.core.plan.resources.member.util.MemberResourceFactoryImpl;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.XMLResource;

public class ResourceConditionsUtils {
	
	private static final Copier COPIER = new Copier();
	
	public static void writeConditions(Conditions conditions, File file) throws IOException {
		Resource resource = createConditionsResource(URI.createFileURI(file.getAbsolutePath()));
		List<? extends EObject> contents = Collections.singletonList(conditions);
		Map<String, Object> options = new HashMap<String, Object>(); 
		options.put(XMLResource.OPTION_ROOT_OBJECTS, contents);
		options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
		resource.save(options);
	}
	
	public static Conditions readConditions(File file) throws IOException {
		return readConditions(URI.createFileURI(file.getAbsolutePath()));
	}

	public static Conditions readConditions(URI uri) throws IOException {
		Resource resource = createConditionsResource(uri);
		resource.load(Collections.emptyMap());
		EList<EObject> contents = resource.getContents();
		if (contents.isEmpty()) {
			throw new IOException("no contents found in " + uri);
		}
		EObject content = contents.get(0);
		if (!(content instanceof Conditions)) {
			throw new IOException("expected conditions in " + uri);
		}
		Conditions conditions = (Conditions) content;
		conditions.setEditable(true);
		return conditions;
	}
	
	private static Resource createConditionsResource(URI uri) {
		ResourceSet resourceSet = EMFUtils.createResourceSet();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new MemberResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(MemberPackage.eNS_URI, MemberPackage.eINSTANCE);
		Resource resource = resourceSet.createResource(uri);
		return resource;
	}

	public static Conditions getInitialConditions(EPlan ePlan) {
		return initializeConditions(ePlan.getMember(ResourceConditionsMember.class));
	}

	public static Conditions initializeConditions(ResourceConditionsMember member) {
		List<Conditions> conditionsList = member.getConditions();
		for (Conditions conditions : conditionsList) {
			if (conditions.isActive()) {
				return conditions;
			}
		}
		boolean delivering = member.eDeliver();
		member.eSetDeliver(false);
		Conditions initialConditions = createDefaultIncon();
		conditionsList.add(initialConditions);
		member.eSetDeliver(delivering);
		return initialConditions;
	}

	public static void setConditionsDate(Conditions incon, Date startTime) {
		incon.setTime(startTime);
	}

	/**
	 * Create a "default" incon.  Uses zero for numerics and the first item for discretes.
	 * @return
	 */
	private static Conditions createDefaultIncon() {
		ActivityDictionary AD = ActivityDictionary.getInstance();
		Conditions conditions = MemberFactory.eINSTANCE.createConditions();
		populateClaims(AD, conditions);
		populateSharableResources(AD, conditions);
		populateStateResources(AD, conditions);
		populateNumericResources(AD, conditions);
		conditions.setDescription("Default Incon");
		conditions.setEditable(true);
		conditions.setActive(true);
		return conditions;
	}

	private static void populateClaims(ActivityDictionary AD, Conditions conditions) {
		List<Claim> claims = conditions.getClaims();
		List<EClaimableResourceDef> claimableResourceDefs = AD.getDefinitions(EClaimableResourceDef.class);
		for (EClaimableResourceDef def : claimableResourceDefs) {
			String name = def.getName();
			Claim claim = MemberFactory.eINSTANCE.createClaim();
			claim.setName(name);
			claim.setUsed(false);
			claims.add(claim);
		}
	}
	
	public static NamedCondition findCondition(String key, Class<? extends NamedCondition> conditionClass, Conditions conditions) {
		if (UndefinedResource.class.isAssignableFrom(conditionClass)) {
			return findCondition(key, conditions.getUndefinedResources());
		} else if (Claim.class.isAssignableFrom(conditionClass)) {
			return findCondition(key, conditions.getClaims());
		} else if (NumericResource.class.isAssignableFrom(conditionClass)) {
			return findCondition(key, conditions.getNumericResources());
		} else if (PowerLoad.class.isAssignableFrom(conditionClass)) {
			return findCondition(key, conditions.getPowerLoads());
		} else if (SharableResource.class.isAssignableFrom(conditionClass)) {
			return findCondition(key, conditions.getSharableResources());
		} else if (StateResource.class.isAssignableFrom(conditionClass)) {
			return findCondition(key, conditions.getStateResources());
		} else {
			return null;
		}
	}
	
	private static NamedCondition findCondition(String key, List<? extends NamedCondition> candidates) {
		for (NamedCondition candidate : candidates) {
			if (key.equals(candidate.getName())) {
				return candidate;
			}
		}
		return null;
	}

	public static void addCondition(NamedCondition newCondition, Conditions destination) {
		if (newCondition instanceof UndefinedResource) {
			destination.getUndefinedResources().add((UndefinedResource) COPIER.copy(newCondition));
		} else if (newCondition instanceof Claim) {
			destination.getClaims().add((Claim) COPIER.copy(newCondition));
		} else if (newCondition instanceof NumericResource) {
			destination.getNumericResources().add((NumericResource) COPIER.copy(newCondition));
		} else if (newCondition instanceof PowerLoad) {
			destination.getPowerLoads().add((PowerLoad) COPIER.copy(newCondition));
		} else if (newCondition instanceof SharableResource) {
			destination.getSharableResources().add((SharableResource) COPIER.copy(newCondition));
		} else if (newCondition instanceof StateResource) {
			destination.getStateResources().add((StateResource) COPIER.copy(newCondition));
		}
	}
	
	public static void copyValue(NamedCondition source, NamedCondition destination) {
		if (source instanceof UndefinedResource) {
			((UndefinedResource)destination).setValueLiteral(((UndefinedResource) source).getValueLiteral());
		} else if (source instanceof Claim) {
			((Claim)destination).setUsed(((Claim) source).isUsed());
		} else if (source instanceof NumericResource) {
			((NumericResource)destination).setFloat(((NumericResource) source).getFloat());
		} else if (source instanceof PowerLoad) {
			((PowerLoad)destination).setState(((PowerLoad) source).getState());
		} else if (source instanceof SharableResource) {
			((SharableResource)destination).setUsed(((SharableResource) source).getUsed());
		} else if (source instanceof StateResource) {
			((StateResource)destination).setState(((StateResource) source).getState());
		}
	}
	
	private enum ConditionType { CLAIM, POWER, STATE, SHARABLE, NUMERIC, UNDEFINED }

	/** Iterate over all conditions regardless of type. */
	public static Iterable<? extends NamedCondition> iterateOverConditions(final Conditions conditions) {
		
		return new Iterable<NamedCondition>() {
			
			private List<ConditionType> getTypesThatArePresent() {
				ArrayList<ConditionType> result = new ArrayList<ConditionType>(ConditionType.values().length);
				if(!conditions.getClaims().isEmpty()) {
					result.add(ConditionType.CLAIM);
				}
				if(!conditions.getNumericResources().isEmpty()) {
					result.add(ConditionType.NUMERIC);
				}
				if(!conditions.getPowerLoads().isEmpty()) {
					result.add(ConditionType.POWER);
				}
				if(!conditions.getSharableResources().isEmpty()) {
					result.add(ConditionType.SHARABLE);
				}
				if(!conditions.getStateResources().isEmpty()) {
					result.add(ConditionType.STATE);
				}
				if(!conditions.getUndefinedResources().isEmpty()) {
					result.add(ConditionType.UNDEFINED);
				}
				return result;
			}

			@Override
			public Iterator<NamedCondition> iterator() {
				return new Iterator<NamedCondition>() {

					Iterator<ConditionType> typeIterator = getTypesThatArePresent().iterator();
					@SuppressWarnings("unchecked")
					Iterator<? extends NamedCondition> conditionIterator = Collections.EMPTY_LIST.iterator();

					@Override
					public boolean hasNext() {
						return conditionIterator.hasNext() || typeIterator.hasNext();
					}

					@Override
					public NamedCondition next() {
						while(!conditionIterator.hasNext()) {
							if (!typeIterator.hasNext()) {
								throw new NoSuchElementException();
							} else {
								switch (typeIterator.next()) {
								case CLAIM:
									conditionIterator = conditions.getClaims().iterator();
									break;
								case NUMERIC:
									conditionIterator = conditions.getNumericResources().iterator();
									break;
								case POWER:
									conditionIterator = conditions.getPowerLoads().iterator();
									break;
								case SHARABLE:
									conditionIterator = conditions.getSharableResources().iterator();
									break;
								case STATE:
									conditionIterator = conditions.getStateResources().iterator();
									break;
								case UNDEFINED:
									conditionIterator = conditions.getUndefinedResources().iterator();
									break;
								}
							}
						}
						return conditionIterator.next();
					}

					@Override
					public void remove() {
						throw new IllegalStateException("Remove method not supported.");
					}
				};
			}
		};
	}
	
	public static boolean isEmpty(Conditions conditions) {
		for (NamedCondition condition : iterateOverConditions(conditions)) {
			@SuppressWarnings("unused")
			NamedCondition ignore = condition;
			return false;
		}
		return true;
	}

	
	private static void populateSharableResources(ActivityDictionary AD, Conditions conditions) {
		List<SharableResource> sharables = conditions.getSharableResources();
		List<ESharableResourceDef> sharableResourceDefs = AD.getDefinitions(ESharableResourceDef.class);
		for (ESharableResourceDef def : sharableResourceDefs) {
			String name = def.getName();
			int value = 0;
			SharableResource sharable = MemberFactory.eINSTANCE.createSharableResource();
			sharable.setName(name);
			sharable.setUsed(value);
			sharables.add(sharable);
		}
	}
	
	private static void populateStateResources(ActivityDictionary AD, Conditions conditions) {
		List<StateResource> stateResources = conditions.getStateResources();
		List<EStateResourceDef> stateResourceDefs = AD.getDefinitions(EStateResourceDef.class);
		for (EStateResourceDef stateDef : stateResourceDefs) {
			String name = stateDef.getName();
			List<String> values = stateDef.getAllowedStates();
			if ((values == null) || values.isEmpty()) {
//				String message = "no values for StateResourceDef: " + stateDef.getName();
//				LogUtil.warnOnce(message);
			} else {
				StateResource stateResource = MemberFactory.eINSTANCE.createStateResource();
				stateResource.setName(name);
				stateResource.setState(values.get(0));
				stateResources.add(stateResource);
			}
		}
	}

	private static void populateNumericResources(ActivityDictionary AD, Conditions conditions) {
		// PHM Modified this from LASS because text eAD uses more general
		// ENumericResourceDefImpl in place of ImmediateResourceDef.
		List<NumericResource> numericResources = conditions.getNumericResources();
		List<ENumericResourceDef> numericResourceDefs = AD.getDefinitions(ENumericResourceDef.class);
		for (ENumericResourceDef numericDef : numericResourceDefs) {
			// Filter out claimables, etc., leaving pure numeric resources
			if (!(numericDef instanceof EExtendedNumericResourceDef)) {
				String name = numericDef.getName();
				NumericResource numericResource = MemberFactory.eINSTANCE.createNumericResource();
				numericResource.setName(name);
				double defaultValue = getNumericResourceDefaultValue(numericDef);
				numericResource.setFloat((float) defaultValue);
				numericResources.add(numericResource);
			}
		}
	}

	private static double getNumericResourceDefaultValue(ENumericResourceDef numericDef) {
		// PHM Preserve compatibility with ImmediateResourceDef, just in case
		if (numericDef instanceof NumericResourceDef) {
			double defaultValue = ((NumericResourceDef) numericDef).getDefault();
			if (defaultValue != 0.0)
				return defaultValue;
		}
		// PHM ENumericResource uses defaultValueLiteral not defaultValue.
		String defaultValueLiteral = numericDef.getDefaultValueLiteral();
		if (defaultValueLiteral == null)
			defaultValueLiteral = "0.0";
		else {
			// Remove internal quotes (like "\"1.0\"") if present.
			defaultValueLiteral = defaultValueLiteral.replace('"', ' ').trim();
		}
		Unit<?> unit = numericDef.getUnits();
		return AmountUtils.valueOf(defaultValueLiteral, unit).getEstimatedValue();
	}

}
