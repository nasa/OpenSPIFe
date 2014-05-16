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
package gov.nasa.arc.spife.europa;

import gov.nasa.arc.spife.europa.clientside.EuropaCommand;
import gov.nasa.arc.spife.europa.model.IEuropaModelConverter;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.ui.preference.PlanConstraintsPreferences;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.nddl.NDDLUtil;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.jscience.physics.amount.Amount;

public class EuropaQueuer {
	
	private static final String EUROPA_SCHEDULED = "scheduled";
	private static final String EUROPA_PRIORITY = "priority";
	private static final Amount<Duration> ZERO = Amount.valueOf(0, SI.SECOND);
	private static final Logger trace = Logger.getLogger(EuropaQueuer.class);
	public static final String INCON_ID = "THE_INCON";
	private static final String TRUE_VALUE = "1000";
	private static final String FALSE_VALUE = "0";
	
	private final EuropaSessionClient client;
	private final EuropaClientSideModel clientSideModel;
	
	EuropaQueuer(EuropaSessionClient client, EuropaClientSideModel clientSideModel) {
		this.client = client;
		this.clientSideModel = clientSideModel;
	}

	/**
	 * Queue an update for the plan bound, removing the old plan bound if it exists.
	 * @param node
	 * @param expectedWork
	 * @return
	 */
	public int queueUpdatePlanBound(EPlan plan) {
		int expectedWork = 0;
		if ((clientSideModel.planBound != null) && queueRemoveConstraint(clientSideModel.planBound)) {
			clientSideModel.planBound = null;
			expectedWork++;
		}
		Date date = plan.getMember(gov.nasa.ensemble.core.model.plan.temporal.TemporalMember.class).getStartTime();
		Amount<Duration> offset = ConstraintUtils.getPeriodicConstraintOffset(date);
		PeriodicTemporalConstraint planBound = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		planBound.getPoint().setElement(plan);
		planBound.getPoint().setEndpoint(Timepoint.START);
		planBound.setEarliest(offset);
		planBound.setRationale("plan bounds");
		if (queueAddConstraint(planBound)) { // should always succeed
			clientSideModel.planBound = planBound;
			expectedWork++;
		} else {
			trace.warn("couldn't add plan bound?");
		}
		return expectedWork;
	}

	/**
	 * Queue an update for the temporal properties of the activity with the supplied unique id
	 * @param uniqueId
	 * @param newExtent
	 */
	public int queueTemporalProperties(EActivity activity, TemporalExtent newExtent) {
		String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(activity);
		if (uniqueId == null) {
			trace.debug("ignoring set properties on unknown node: " + activity.getName());
			return 0;
		}
		if (newExtent == null) {
			trace.debug("ignoring null extent set");
			return 0;
		}
		int expectedWork = 0;
		Date newTime = newExtent.getStart();
		trace.debug("moving " + uniqueId + " start to " + newTime);
		expectedWork += updateElementAndSubactivities(activity, newTime);
		Date oldTime = clientSideModel.lastStartTimeMap.get(activity);
		clientSideModel.lastStartTimeMap.put(activity, newTime);
		expectedWork += updateHMSbounds(activity, oldTime, newTime);
		long newDuration = newExtent.getDurationMillis();
		trace.debug("changing " + uniqueId + " duration to " + newDuration);
		Number europaDuration = EuropaConverter.convertDurationToEuropa(newExtent.getDuration());
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(uniqueId);
		parameters.add(europaDuration);
		client.queueExecute(EuropaCommand.UPDATE_ACTIVITY_DURATION, parameters);
		expectedWork++;
		clientSideModel.lastDurationMap.put(activity, newDuration);
		Map<EReference, Map<String, String>> resources = clientSideModel.activityResources.get(activity);
		if (resources != null) {
			for (Map<String, String> idmap : resources.values()) {
				for (String resourceId : idmap.values()) {
					Vector<Object> resourceParameters = new Vector<Object>();
					resourceParameters.add(resourceId);
					resourceParameters.add(europaDuration);
					client.queueExecute(EuropaCommand.UPDATE_ACTIVITY_DURATION, resourceParameters);
					expectedWork++;
				}
			}
		}
		clientSideModel.elementToConsistencyProperties.clear();
		return expectedWork;
	}

	private int updateElementAndSubactivities(EPlanElement element, Date time) {
		String parentUniqueId = clientSideModel.identifiableRegistry.getUniqueId(element);
		int expectedWork = 0;
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(parentUniqueId);
		parameters.add(clientSideModel.converter.convertDateToEuropa(time));
		client.queueExecute(EuropaCommand.SET_ACTIVITY_START_TIME, parameters);
		expectedWork++;
		expectedWork += updateSubactivities(element, time);
		return expectedWork;
	}

	private int updateSubactivities(EPlanElement parent, Date ownerTime) {
		int expectedWork = 0;
		List<? extends EPlanElement> children = EPlanUtils.getChildren(parent);
		for (EPlanElement child : children) {
			EActivity subActivity = (EActivity)child;
			String childUniqueId = clientSideModel.identifiableRegistry.getUniqueId(subActivity);
			TemporalExtent childExtent = getLeafExtent(subActivity);
			if ((childUniqueId != null) && (childExtent != null)) {
				Date childStart = childExtent.getStart();
				clientSideModel.lastStartTimeMap.put(subActivity, childStart);
				updateSubactivityOffset(childUniqueId, ownerTime, childStart);
				expectedWork++;
				expectedWork += updateSubactivities(subActivity, ownerTime);
			} else {
				trace.warn("ignoring unknown subactivity: "  + subActivity.getName());
			}
		}
		return expectedWork;
	}

	private void updateSubactivityOffset(String subactivityId, Date ownerStart, Date childStart) {
		Vector<Object> parameters;
		Amount<Duration> offset = DateUtils.subtract(childStart, ownerStart);
		if (offset.isLessThan(ZERO)) {
			offset = ZERO; // Europa doesn't like negative offsets. (who would?)
		}
		parameters = new Vector<Object>();
		parameters.add(subactivityId);
		parameters.add(EuropaConverter.convertTimeDistanceToEuropa(offset));
		client.queueExecute(EuropaCommand.SET_SUBACTIVITY_OFFSET, parameters);
	}

	/**
	 * Because europa can't represent HMS bounds directly, we need to remove them
	 * and re-add them as the node travels between days. 
	 * 
	 * @param node
	 * @param oldTime
	 * @param newTime
	 * @return
	 */
	private int updateHMSbounds(EPlanElement node, Date oldTime, Date newTime) {
		int expectedWork = 0;
		int oldDay = (oldTime != null ? MissionCalendarUtils.getDayOfMission(oldTime) : -1);
		int newDay = (oldTime != null ? MissionCalendarUtils.getDayOfMission(newTime) : -1);
		if (oldDay != newDay) {
			Set<PeriodicTemporalConstraint> timepointConstraints = clientSideModel.getTimepointConstraints(node);
			if (timepointConstraints != null) {
				List<PeriodicTemporalConstraint> constraints = new ArrayList<PeriodicTemporalConstraint>(timepointConstraints);
				for (PeriodicTemporalConstraint constraint : constraints) {
					if (queueRemoveConstraint(constraint)) {
						expectedWork++;
						if (queueAddConstraint(constraint)) {
							expectedWork++;
						} else {
							trace.error("failed to add PeriodicTemporalConstraint after removing it during temporal properties");
						}
					} else {
						trace.warn("failed to remove PeriodicTemporalConstraint during temporal properties");
					}
				}
			}
		}
		return expectedWork;
	}

	/**
	 * Queues the add of the constraint, without clearing the consistency property table.
	 * A waived constraint will be ignored.
	 * @param timepointConstraint
	 * @return true if the constraint was queued for addition, 
	 *         false if the constraint is already known or the node is unknown (not queued)
	 */
	public boolean queueAddConstraint(PeriodicTemporalConstraint timepointConstraint) {
		if (timepointConstraint.getWaiverRationale() != null) {
			trace.debug("   (ignored) - this was a waived constraint");
			return false;
		}
		EPlanElement node = timepointConstraint.getPoint().getElement();
		String nodeUniqueId = clientSideModel.identifiableRegistry.getUniqueId(node);
		if (nodeUniqueId == null) {
			trace.warn("ignoring add timepoint constraint on unknown node: " + node.getName());
			return false;
		}
		if (clientSideModel.constraintRegistry.getUniqueId(timepointConstraint) != null) {
			trace.debug("   (ignored) - already heard about that one");
			return false;
		}
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug(" addConstraint(TemporalBound): " + timepointConstraint);
		}
		Date min = ConstraintUtils.getPeriodicConstraintEarliestDate(timepointConstraint);
		Date max = ConstraintUtils.getPeriodicConstraintLatestDate(timepointConstraint);
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(nodeUniqueId);
		parameters.add(EuropaConverter.convertTimepointToEuropa(timepointConstraint.getPoint().getEndpoint()));
		parameters.add(clientSideModel.converter.convertEarliestDateToEuropa(min));
		parameters.add(clientSideModel.converter.convertLatestDateToEuropa(max));
		parameters.add(clientSideModel.constraintRegistry.generateUniqueId(timepointConstraint));
		client.queueExecute(EuropaCommand.ADD_TEMPORAL_BOUND_CONSTRAINT, parameters);
		clientSideModel.addedTimepointConstraint(node, timepointConstraint);
		return true;
	}

	/**
	 * Queues the remove of the constraint, without clearing the consistency property table.
	 * @param timepointConstraint
	 * @return true if the constraint was queued for addition, 
	 *         false if the constraint wasn't known or the node is unknown (not queued)
	 */
	public boolean queueRemoveConstraint(PeriodicTemporalConstraint timepointConstraint) {
		String timepointConstraintUniqueId = clientSideModel.constraintRegistry.getUniqueId(timepointConstraint);
		if (timepointConstraintUniqueId == null) {
			trace.debug("   (ignored) - don't know that constraint");
			return false;
		}
		EPlanElement node = timepointConstraint.getPoint().getElement();
		if (clientSideModel.identifiableRegistry.getUniqueId(node) == null) {
			trace.warn("ignoring remove timepoint constraint on unknown node: " + node.getName());
			return false;
		}
		Vector<Object> parameters = new Vector<Object>(Collections.singletonList(timepointConstraintUniqueId));
		client.queueExecute(EuropaCommand.REMOVE_TEMPORAL_CONSTRAINT, parameters);
		clientSideModel.removedTimepointConstraint(node, timepointConstraint);
		clientSideModel.constraintRegistry.releaseIdentifiable(timepointConstraint); // release it regardless of whether or not europa agrees
		return true;
	}

	/**
	 * Queue the node to be scheduled/unscheduled.  Usually will
	 * update parent unscheduled state.
	 * 
	 * @param node
	 * @param scheduled
	 * @return the number of calls queued to update all the model state
	 */
	public int queueSetScheduled(EPlanElement node, boolean scheduled) {
		Boolean oldScheduledness = clientSideModel.lastSchedulednessMap.get(node);
		if ((oldScheduledness != null) && (oldScheduledness.booleanValue() == scheduled)) {
			return 0;
		}
		if (node.eContainer() instanceof EActivity) {
			// subactivities will get queued through their parent activity
			return 0;
		}
		int work = 0;
		if (queueNodeParameterValue(node, EUROPA_SCHEDULED, scheduled)) {
			clientSideModel.lastSchedulednessMap.put(node, Boolean.valueOf(scheduled));
			work++;
//			work += setSubActivitiesScheduledness(node, scheduled);
			ConstraintsMember member = node.getMember(ConstraintsMember.class, false);
			if (member != null) {
				TemporalChain chain = member.getChain();
				if (chain != null) {
					Set<BinaryTemporalConstraint> chainRelations = clientSideModel.chainRelations.get(chain);
					// update the chain by removing all current relations and putting back those we should have
					if (chainRelations != null) {
						work += queueRemoveChain(chain);
					}
					// PHM 05/09/2013 Moved the add part outside the condition because chainRelations
					// may legitimately be null for a 2-element chain with 1 element unscheduled.
					work += queueAddChain(chain);
				}
			}
		}
		EObject container = node.eContainer();
		if (container instanceof EPlanElement) {
			EPlanElement parent = (EPlanElement)container;
			boolean parentScheduled = (scheduled || Europa.getScheduled(parent));
			return queueSetScheduled(parent, parentScheduled) + work;
		}
		return work;
	}

	@SuppressWarnings("unused")
	private int setSubActivitiesScheduledness(EActivity node, boolean scheduled) {
		int work = 0;
		List<? extends EPlanElement> children = EPlanUtils.getChildren(node);
		for (EPlanElement child : children) {
			EActivity subActivity = (EActivity)child;
			Boolean oldScheduledness = clientSideModel.lastSchedulednessMap.get(subActivity);
			if (((oldScheduledness == null) || (oldScheduledness.booleanValue() != scheduled))
				&& (queueNodeParameterValue(subActivity, EUROPA_SCHEDULED, scheduled))) {
				clientSideModel.lastSchedulednessMap.put(subActivity, scheduled);
				work++;
			}
			work += setSubActivitiesScheduledness(subActivity, scheduled);
		}
		return work;
	}
	
	/**
	 * Queue the node to have its priority set.
	 * @param node
	 * @param priority
	 * @return true if the call was queued to update this node 
	 */
	public boolean queueSetPriority(EPlanElement node, Object priority) {
		return queueNodeParameterObject(node, EUROPA_PRIORITY, priority);
	}

	public int queueCreateResourceObjects(EPlan plan) {
		EPlanUtils.contributeProductResources(plan);
	    int expectedWork = 0;
		for (EClassifier classifier : ActivityDictionary.getInstance().getEClassifiers()) {
			if (classifier instanceof ObjectDef) {
				ObjectDef objectDef = (ObjectDef) classifier;
				EAttribute attribute = objectDef.getEIDAttribute();
				String objectClassName = objectDef.getName();
				Collection<EObject> objects = EMFUtils.getReachableObjectsOfType(plan, objectDef);
				Vector<String> objectIds = new Vector<String>();
				trace.debug("createResourceObjects for " + objectClassName);
				for (EObject object : objects) {
					String id = (String)object.eGet(attribute);
					trace.debug(" - " + id);
					objectIds.add(id);
				}
				Vector<Object> parameters = new Vector<Object>();
				parameters.add(objectClassName);
				parameters.add(objectIds);
				client.queueExecute(EuropaCommand.CREATE_OBJECTS, parameters);
				expectedWork++;
			}
		}
		return expectedWork;
    }

	public int queueUpdateParameters(EActivity activity, EObject data) {
	    int expectedWork = 0;
	    if (data != null) {
		    EClass dataClass = data.eClass();
		    HashSet<String> actParams = new HashSet<String>();
		    String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(activity);
		    if (uniqueId != null
		    		&& EuropaPreferences.isTranslateNumericResources()
		    		) {
		    	client.getActivityParameters(uniqueId, actParams);
		    }
			for (EAttribute attribute : dataClass.getEAllAttributes()) {
				String name = attribute.getName();
	//	    	EDataType attributeType = attribute.getEAttributeType();
		    	boolean shouldSendAttribute = false; // perform some check here (from the AD?)
		    	if (data.eGet(attribute) instanceof EEnumLiteral)
		    		shouldSendAttribute = true;
		    	else if (actParams != null && actParams.contains(name))
		    		shouldSendAttribute = true;
		    	if (shouldSendAttribute) {
		    		Object value = data.eGet(attribute);
		    		// Skip if value not yet there
		    		if (value == null)
		    			continue;
		    		trace.info("Sending parameter to Europa:  " + name + " -> " + value);
		    		boolean work = queueNodeParameterObject(activity, name, value);
		    		if (work) {
		    			expectedWork++;
		    		} else {
		    			Logger logger = Logger.getLogger(EuropaQueuer.class);
		    			logger.error("Could not queue parameter change");
		    		}
		    	}
		    }
			actParams = null;
		    for (EReference reference : dataClass.getEAllReferences()) {
		    	expectedWork += updateReference(activity, data, reference);
		    }
	    }
	    return expectedWork;
    }

	private int updateReference(EActivity activity, EObject data, EReference reference) {
		int expectedWork = 0;
	    EClassifier type = reference.getEType();
	    if (type instanceof ObjectDef) {
	        Set<String> newIds = getReferenceIds(data, reference, type);
	        Map<String, String> oldResources = getExistingResources(activity, reference);
	        Set<String> addedIds = new LinkedHashSet<String>(newIds);
	        addedIds.removeAll(oldResources.keySet());
	        Set<String> removedResources = new LinkedHashSet<String>();
	        for (Map.Entry<String, String> oldResource : oldResources.entrySet()) {
	        	String id = oldResource.getKey();
	        	String uniqueId = oldResource.getValue();
	        	if (!newIds.contains(id)) {
	        		removedResources.add(uniqueId);
	        	}
	        }
	        expectedWork += updateActivityObjects(activity, reference, removedResources, addedIds);
		}
		return expectedWork;
    }

	@SuppressWarnings("unchecked")
	private Set<String> getReferenceIds(EObject data, EReference reference, EClassifier type) {
		Object value = data.eGet(reference);
		Collection<EObject> values;
		if (reference.isMany()) {
			values = (Collection<EObject>)value;
	    } else if (value != null) {
	    	values = Collections.singleton((EObject)value);
	    } else {
	    	values = Collections.emptySet();
	    }
	    EClass objectType = (ObjectDef)type;
	    EAttribute attribute = objectType.getEIDAttribute();
	    Set<String> ids = new LinkedHashSet<String>();
	    for (EObject object : values) {
	    	Object id = object.eGet(attribute);
	    	ids.add(String.valueOf(id));
	    }
	    return ids;
    }
	
	private Map<String, String> getExistingResources(EActivity activity, EReference reference) {
	    Map<EReference, Map<String, String>> map = clientSideModel.activityResources.get(activity);
	    if (map == null) {
	    	map = new LinkedHashMap<EReference, Map<String, String>>();
	    	clientSideModel.activityResources.put(activity, map);
	    }
	    Map<String, String> oldResources = map.get(reference);
	    if (oldResources == null) {
	    	oldResources = Collections.emptyMap();
	    }
	    return oldResources;
    }

	private int updateActivityObjects(EActivity activity, EReference reference, Set<String> removedResources, Set<String> addedIds) {
		int expectedWork = 0;
		String activityUniqueId = clientSideModel.identifiableRegistry.getUniqueId(activity);
		if (activityUniqueId != null) {
			for (String uniqueId : removedResources) {
				if (queueRemoveActivity(uniqueId)) {
					expectedWork++;
				}				
			}
			Map<String, String> resources = new LinkedHashMap<String, String>(getExistingResources(activity, reference));
			resources.values().removeAll(removedResources);
			for (String id : addedIds) {
				String uniqueId = IdentifiableRegistry.generateUniqueId();
				resources.put(id, uniqueId);
				long milliseconds = clientSideModel.lastDurationMap.get(activity);
				Number duration = EuropaConverter.convertDurationToEuropa(AmountUtils.toAmount(milliseconds, DateUtils.MILLISECONDS));
				Number offsetTime = EuropaConverter.convertTimeDistanceToEuropa(DateUtils.ZERO_DURATION);
				String type = "Assign_" + reference.getEType().getName();
				boolean isScheduled = clientSideModel.isScheduled(activity);
				boolean unknownsOk = !EuropaPreferences.isStrictTypeChecking();
				Vector<Object> registerParameters = new Vector<Object>();
				registerParameters.add(uniqueId);
				registerParameters.add(activityUniqueId);
				registerParameters.add(duration);
				registerParameters.add(offsetTime);
				registerParameters.add(NDDLUtil.escape(type));
				registerParameters.add(isScheduled);
				registerParameters.add(unknownsOk);
				client.queueExecute(EuropaCommand.REGISTER_SUBACTIVITY, registerParameters);
				expectedWork++;
				Vector<Object> objectParameters = new Vector<Object>();
				objectParameters.add(uniqueId);
				objectParameters.add(id);
				client.queueExecute(EuropaCommand.SET_ACTIVITY_OBJECT, objectParameters);
				expectedWork++;
			}
			clientSideModel.activityResources.get(activity).put(reference, resources);
		}
		return expectedWork;
    }

	/**
	 * Sets the value of a parameter of the node in europa.  Europa should
	 * already know about this node. 
	 * 
	 * @param node
	 * @param name the name of the parameter to set
	 * @param value the value of the parameter
	 */
	private boolean queueNodeParameterObject(EPlanElement node, String name, Object value) {
		// TODO: Cache values to prevent setting values that haven't changed.
		if (value instanceof Boolean) {
			return queueNodeParameterValue(node, name, ((Boolean)value).booleanValue());
		} else if (value instanceof Float) {
			return queueNodeParameterValue(node, name, ((Float)value).floatValue());
		} else if (value instanceof Double) {
			return queueNodeParameterValue(node, name, ((Double)value).floatValue());
		} else if (value instanceof Integer) {
			return queueNodeParameterValue(node, name, ((Integer)value).intValue());
		} else if (value instanceof Long) {
			return queueNodeParameterValue(node, name, ((Long)value).longValue());
		} else if (value instanceof String) {
			return queueNodeParameterValue(node, name, (String)value);
		} else if (value instanceof EEnumLiteral) {
			return queueNodeParameterValue(node, name, ((EEnumLiteral)value).getName());
		} else if (value instanceof Amount<?>) {
			Amount<?> val = (Amount<?>) value;
			if (val.isExact()) {
				long intVal = val.getExactValue();
				return queueNodeParameterValue(node, name, intVal);
			} else {
				Double floatVal = val.getEstimatedValue();
				return queueNodeParameterValue(node, name, floatVal.floatValue());
			}
		} else {
		    return false;
		}
	}
	
	private boolean queueNodeParameterValue(EPlanElement node, String name, boolean value) {
		return queueNodeParameterValue(node, name, value, "bool");
	}
	
	private boolean queueNodeParameterValue(EPlanElement node, String name, float value) {
		return queueNodeParameterValue(node, name, value, "float");
	}
	
	private boolean queueNodeParameterValue(EPlanElement node, String name, long value) {
		return queueNodeParameterValue(node, name, value, "int");
	}
	
	private boolean queueNodeParameterValue(EPlanElement node, String name, String value) {
		return queueNodeParameterValue(node, name, value, "string");
	}

	private synchronized boolean queueNodeParameterValue(EPlanElement node, String name, Object value, String valueType) {
		String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(node);
		if (uniqueId != null) {
			Vector<Object> parameters = new Vector<Object>();
			parameters.add(uniqueId);
			parameters.add(name);
			parameters.add(value.toString()); 
			parameters.add(valueType);
			client.queueExecute(EuropaCommand.SET_ACTIVITY_PARAM_VALUE, parameters);
			return true;
		}
		return false;
	}

	/**
	 * Queues the add of the constraint, without clearing the consistency property table.
	 * A waived constraint will be ignored.
	 * @param timepointConstraint
	 * @return true if the constraint was queued for addition, 
	 *         false if the constraint is already known or either node is unknown (not queued)
	 */
	public boolean queueAddConstraint(BinaryTemporalConstraint distanceConstraint) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug(" addConstraint(TemporalRelation): " + distanceConstraint);
		}
		if (distanceConstraint.getWaiverRationale() != null) {
			trace.debug("   (ignored) - this was a waived constraint");
			return false;
		}
		String elementAuniqueId = clientSideModel.identifiableRegistry.getUniqueId(distanceConstraint.getPointA().getElement());
		if (elementAuniqueId == null) {
			trace.debug("   (ignored) - don't know about " + elementAuniqueId);
			return false;
		}
		String elementBuniqueId = clientSideModel.identifiableRegistry.getUniqueId(distanceConstraint.getPointB().getElement());
		if (elementBuniqueId == null) {
			trace.debug("   (ignored) - don't know about " + elementBuniqueId);
			return false;
		}
		if (clientSideModel.constraintRegistry.getUniqueId(distanceConstraint) != null) {
			trace.debug("   (ignored) - already heard about that one");
			return false;
		}
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(elementAuniqueId);
		parameters.add(EuropaConverter.convertTimepointToEuropa(distanceConstraint.getPointA().getEndpoint()));
		parameters.add(elementBuniqueId);
		parameters.add(EuropaConverter.convertTimepointToEuropa(distanceConstraint.getPointB().getEndpoint()));
		parameters.add(EuropaConverter.convertMinTimeDistanceToEuropa(distanceConstraint.getMinimumBminusA()));
		parameters.add(EuropaConverter.convertMaxTimeDistanceToEuropa(distanceConstraint.getMaximumBminusA()));
		parameters.add(clientSideModel.constraintRegistry.generateUniqueId(distanceConstraint));
		client.queueExecute(EuropaCommand.ADD_TEMPORAL_RELATION_CONSTRAINT, parameters);
		return true;
	}
	
	/**
	 * Queues the remove of the constraint, without clearing the consistency property table.
	 * @param distanceConstraint
	 * @return true if the constraint was queued for addition, 
	 *         false if the constraint wasn't known or the nodes are unknown (not queued)
	 */
	public boolean queueRemoveConstraint(BinaryTemporalConstraint distanceConstraint) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug(" removeConstraint(TemporalRelation): " + distanceConstraint);
		}
		String distanceConstraintUniqueId = clientSideModel.constraintRegistry.getUniqueId(distanceConstraint);
		if (distanceConstraintUniqueId == null) {
			trace.debug("   (ignored) - don't know that constraint");
			return false;
		}
		String elementAuniqueId = clientSideModel.identifiableRegistry.getUniqueId(distanceConstraint.getPointA().getElement());
		if (elementAuniqueId == null) {
			trace.debug("   (ignored) - don't know about " + elementAuniqueId);
			return false;
		}
		String elementBuniqueId = clientSideModel.identifiableRegistry.getUniqueId(distanceConstraint.getPointB().getElement());
		if (elementBuniqueId == null) {
			trace.debug("   (ignored) - don't know about " + elementBuniqueId);
			return false;
		}
		Vector<Object> parameters = new Vector<Object>(Collections.singletonList(distanceConstraintUniqueId));
		client.queueExecute(EuropaCommand.REMOVE_TEMPORAL_CONSTRAINT, parameters);
		clientSideModel.constraintRegistry.releaseIdentifiable(distanceConstraint); // release it regardless of whether or not europa agrees
		return true;
	}

	/**
	 * Queue up the addition of temporal distance constraints for the supplied chain
	 * @param chain
	 * @return
	 */
	public int queueAddChain(TemporalChain chain) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug(" addChain(TemporalChain): " + chain);
		}
	    if (clientSideModel.chainRelations.get(chain) != null) {
			trace.debug("   (ignored) - already heard about that one");
			return 0;
		}
		List<EPlanElement> elements = chain.getElements();
		if ((elements == null) || (elements.isEmpty())) {
			trace.warn("skipping empty chain: " + chain);
			return 0;
		}
		Iterator<EPlanElement> iterator = elements.iterator();
	    EPlanElement lastPlanElement = iterator.next();
	    while (iterator.hasNext()) {
	    	if (clientSideModel.isScheduled(lastPlanElement)) {
	    		break; // found a scheduled element, continue
	    	}
	    	lastPlanElement = iterator.next();
	    }
	    Set<BinaryTemporalConstraint> relations = new LinkedHashSet<BinaryTemporalConstraint>();
	    while (iterator.hasNext()) {
	    	EPlanElement chainElement = iterator.next();
			if (clientSideModel.identifiableRegistry.getUniqueId(chainElement) == null) {
	    		trace.warn("chain has an unknown element: " + chainElement.getName());
	    		return 0;
	    	}
			if (!clientSideModel.isScheduled(chainElement)) {
				continue; // skip unscheduled elements
			}
			String rationale = "chain: " + chain;
			Amount<Duration> minValue = ZERO;
			Amount<Duration> maxValue = null;
			if (PlanConstraintsPreferences.getUseMeetsChains()) {
				maxValue = ZERO;
			}
			BinaryTemporalConstraint relation = ConstraintUtils.createConstraint(lastPlanElement, Timepoint.END, chainElement, Timepoint.START, minValue, maxValue, rationale);
			relations.add(relation);
    		lastPlanElement = chainElement;
	    }
	    if (relations.isEmpty()) {
	    	// no relations
	    	return 0;
	    }
	    int expectedWork = 0;
	    for (BinaryTemporalConstraint relation : relations) {
			if (queueAddConstraint(relation)) { // really should always be true
				expectedWork++;
				clientSideModel.relationsChain.put(relation, chain);
			} else {
				LogUtil.error("failed to add a chain relation");
			}
	    }
	    clientSideModel.chainRelations.put(chain, relations);
	    return expectedWork;
	}

	/**
	 * Queue up the removal of temporal distance constraints for the supplied chain
	 * @param chain
	 * @return
	 */
	public int queueRemoveChain(TemporalChain chain) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug(" removeChain(TemporalChain): " + chain);
		}
	    Set<BinaryTemporalConstraint> relations = clientSideModel.chainRelations.get(chain);
	    if (relations == null) {
	    	trace.warn("chain " + chain + " has no relations in europa");
	    	return 0;
	    }
    	int expectedResults = 0;
	    for (BinaryTemporalConstraint relation : relations) {
			if (queueRemoveConstraint(relation)) { // really should always be true
				expectedResults++;
			}
			clientSideModel.relationsChain.remove(relation);
	    }
	    clientSideModel.chainRelations.put(chain, null);
	    return expectedResults;
	}

	/**
	 * Queue add of a leaf node. (no children)
	 * @param activity the element to add
	 */
	public void queueAddLeafInternal(EActivity activity, String uniqueId) {
		TemporalExtent extent = getLeafExtent(activity);
		Number start = clientSideModel.converter.convertDateToEuropa(extent.getStart());
		Number duration = EuropaConverter.convertDurationToEuropa(extent.getDuration());
		Boolean scheduledObject = activity.getMember(TemporalMember.class).getScheduled();
		String type = activity.getType();
		boolean isScheduled = (scheduledObject == null) || scheduledObject.booleanValue();
		boolean unknownsOk = !EuropaPreferences.isStrictTypeChecking();
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(uniqueId);
		parameters.add(duration);
		parameters.add(start);
		parameters.add(NDDLUtil.escape(type));
		parameters.add(isScheduled);
		parameters.add(unknownsOk);
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("  addNode: " + parameters);
		}
		client.queueExecute(EuropaCommand.REGISTER_ACTIVITY, parameters);
		clientSideModel.lastStartTimeMap.put(activity, extent.getStart());
		clientSideModel.lastDurationMap.put(activity, extent.getDurationMillis());
		clientSideModel.lastSchedulednessMap.put(activity, isScheduled);
	}

	public int queueAddSubActivity(EActivity owner, EActivity subActivity) {
		int work = 0;
		String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(owner);
		if (uniqueId != null) {
			String subActivityName = clientSideModel.identifiableRegistry.generateUniqueId(subActivity);
			String parentActivity = uniqueId;
			TemporalExtent extent = getLeafExtent(subActivity);
			Number duration = EuropaConverter.convertDurationToEuropa(extent.getDuration());
			Date parentDate = clientSideModel.lastStartTimeMap.get(owner);
			Amount<Duration> offset = DateUtils.subtract(extent.getStart(), parentDate);
			Number offsetTime = EuropaConverter.convertTimeDistanceToEuropa(offset);
			String type = subActivity.getType();
			boolean isScheduled = clientSideModel.isScheduled(owner);
			boolean unknownsOk = !EuropaPreferences.isStrictTypeChecking();
			Vector<Object> parameters = new Vector<Object>();
			parameters.add(subActivityName);
			parameters.add(parentActivity);
			parameters.add(duration);
			parameters.add(offsetTime);
			parameters.add(NDDLUtil.escape(type));
			parameters.add(isScheduled);
			parameters.add(unknownsOk);
			if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
				trace.debug("  addSubNode: " + parameters);
			}
			client.queueExecute(EuropaCommand.REGISTER_SUBACTIVITY, parameters);
			work++;
			clientSideModel.lastStartTimeMap.put(subActivity, extent.getStart());
			clientSideModel.lastDurationMap.put(subActivity, extent.getDurationMillis());
			clientSideModel.lastSchedulednessMap.put(subActivity, isScheduled);
			work += queueUpdateParameters(subActivity, subActivity.getData());
		}
		return work;
	}
	
	public void queueAddContainer(String uniqueId) {
		trace.debug("  addContainer: " + uniqueId);
		client.queueExecute(EuropaCommand.REGISTER_CONTAINER, Collections.singletonList(uniqueId));
	}


	public boolean queueRemoveActivity(String elementUniqueId) {
		client.queueExecute(EuropaCommand.UNREGISTER_ACTIVITY, Collections.singletonList(elementUniqueId));
		return true;
	}
	
	public int queueRemoveSubActivity(EPlanElement activity, EActivity subActivity) {
		int work = 0;
		String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(subActivity);
		if (uniqueId != null) {
			if (queueRemoveActivity(uniqueId)) {
				work++;
			}
		}
		return work;
	}
	

	/**
	 * Get the current extent for a leaf, or at least some reasonable stand-in.
	 * @param element
	 * @return
	 */
	private TemporalExtent getLeafExtent(EPlanElement element) {
		TemporalMember temporalMember = element.getMember(TemporalMember.class);
		TemporalExtent extent = temporalMember.getExtent();
		if (extent == null) {
			trace.debug("missing temporal extent on a leaf");
			Date date = temporalMember.getStartTime();
            if (date == null) {
            	EObject parent = element.eContainer();
            	if (parent instanceof EPlanElement) {
            		extent = getLeafExtent((EPlanElement)parent);
            	}
            	if (extent != null) {
            		date = extent.getStart();
            	} else {
            		date = MissionConstants.getInstance().getMissionStartTime();
            	}
            }
            Amount<Duration> duration = temporalMember.getDuration();
            if (duration == null) {
                duration = Amount.valueOf(1L, SI.SECOND);
            }
			extent = new TemporalExtent(date, duration);
		}
		return extent;
	}
	
	/**
     * @param parentID id for the container
     * @param childId id for the containee
     * @return
	 */
	public void queueAddContainsRelation(String parentID, String childID) {
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(parentID);
		parameters.add(childID);
		client.queueExecute(EuropaCommand.ADD_CONTAINS_RELATION, parameters);
	}

    /**
     * Queue the removal of the containment relationship for the child.
     * @param priorParent the parent of all the children removed in this removeElements
     * @param child the child, which may be a child of one of the original children
     * @return true if the relation was queued for removal,
     *         false if the child or parent or their unique id couldn't be determined 
     */
	public boolean queueRemoveContainsRelation(EPlanElement priorParent, EPlanElement child) {
		String elementUniqueId = clientSideModel.identifiableRegistry.getUniqueId(child);
		if (elementUniqueId != null) {
			EPlanElement parent = (EPlanElement)child.eContainer();
			if (parent == null) { // must have been newly removed, use the prior parent
				parent = priorParent;
			}
			if (parent != null) {
				String parentUniqueId = clientSideModel.identifiableRegistry.getUniqueId(parent);
				if (parentUniqueId != null) { // shouldn't ever be null here, but ignore it if it is
					Vector<Object> parameters = new Vector<Object>();
					parameters.add(parentUniqueId);
					parameters.add(elementUniqueId);
					client.queueExecute(EuropaCommand.REMOVE_CONTAINS_RELATION, parameters);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Queue setting whether or not flight rules will be waived for the given element
	 * @param element
	 * @param isWaivingFlightRules
	 * @return
	 */
	public int queueWaivingAllRulesForElement(EPlanElement element, boolean isWaivingFlightRules) {
		String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(element);
		if (uniqueId != null) {
			List<String> args = Arrays.asList(uniqueId);
			if (isWaivingFlightRules) {
				client.queueExecute(EuropaCommand.DISABLE_ACTIVITY_FLIGHT_RULE_ACTIVE_ENFORCEMENT, args);
			} else {
				client.queueExecute(EuropaCommand.ENABLE_ACTIVITY_FLIGHT_RULE_ACTIVE_ENFORCEMENT, args);
			}
			return 1;
		}
		return 0;
	}

	/**
	 * Queue setting which rules are waived for the entire plan
	 * @param newWaivedRules
	 * @return
	 */
	public int queueWaivingRulesForPlan(Set<ERule> newWaivedRules) {
		Set<ERule> oldWaivedRules = clientSideModel.waivedRulesForPlan;
		Set<ERule> formerlyWaivedRules = new HashSet<ERule>(oldWaivedRules); formerlyWaivedRules.removeAll(newWaivedRules);
		Set<ERule> newlyWaivedRules = new HashSet<ERule>(newWaivedRules); newlyWaivedRules.removeAll(oldWaivedRules);
		trace.debug("rules that are not waived any longer: " + formerlyWaivedRules);
		trace.debug("rules that have just become waived: " + newlyWaivedRules);
		int work = 0;
		for (ERule rule : formerlyWaivedRules) {
			work += queueRuleCommand(rule, EuropaCommand.ENABLE_SINGLE_RULE_ACTIVE_ENFORCEMENT);
		}
		for (ERule rule : newlyWaivedRules) {
			work += queueRuleCommand(rule, EuropaCommand.DISABLE_SINGLE_RULE_ACTIVE_ENFORCEMENT);
		}
		clientSideModel.waivedRulesForPlan = newWaivedRules;
		return work;
	}

	private int queueRuleCommand(ERule rule, EuropaCommand command) {
		int work = 0;
		for (String europaRuleName : IEuropaModelConverter.instance.convertRuleToEuropaNames(rule)) {
			Vector<Object> parameters = new Vector<Object>();
			parameters.add(europaRuleName);
			client.queueExecute(command, parameters);
			work++;
		}
		return work;
	}
	
	/**
	 * Queue settings which rules are waived for this individual activity instance
	 * @param element
	 * @param newWaivedRules
	 * @return
	 */
	public int queueWaivingRulesForActivityInstance(EPlanElement element, Set<ERule> newWaivedRules) {
		int work = 0;
		String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(element);
		if (uniqueId != null) {
			Set<ERule> oldWaivedRules = clientSideModel.waivedRulesForElement.get(element);
			if (newWaivedRules == null) {
				newWaivedRules = Collections.emptySet();
			}
			if (oldWaivedRules == null) {
				oldWaivedRules = Collections.emptySet();
			}
			Set<ERule> formerlyWaivedRules = new LinkedHashSet<ERule>(oldWaivedRules); formerlyWaivedRules.removeAll(newWaivedRules);
			Set<ERule> newlyWaivedRules = new LinkedHashSet<ERule>(newWaivedRules); newlyWaivedRules.removeAll(oldWaivedRules);
			trace.debug("rules that are not waived any longer: " + formerlyWaivedRules + " for element: " + element.getName());
			trace.debug("rules that have just become waived: " + newlyWaivedRules + " for element: " + element.getName());
			for (ERule rule : formerlyWaivedRules) {
				work += queueRuleActivityCommand(rule, uniqueId, false);
			}
			for (ERule rule : newlyWaivedRules) {
				work += queueRuleActivityCommand(rule, uniqueId, true);
			}
			clientSideModel.waivedRulesForElement.put(element, newWaivedRules);
		}
		return work;
	}

	private int queueRuleActivityCommand(ERule rule, String uniqueId, boolean b) {
		int work = 0;
		for (String europaRuleName : IEuropaModelConverter.instance.convertRuleToEuropaNames(rule)) {
			Vector<Object> parameters = new Vector<Object>();
			parameters.add(uniqueId);
			parameters.add(europaRuleName);
			parameters.add(b);
			client.queueExecute(EuropaCommand.SET_SINGLE_ACTIVITY_SINGLE_RULE_ENFORCEMENT, parameters);
			work++;
		}
		return work;
	}

	/**
	 * Queue creation of the incon
	 * @return
	 */
	public int queueCreateIncon() {
		String id = INCON_ID;
		int duration = 0;
		int startTime = 0;
		String type = "incon";
		boolean scheduled = true;
		boolean unknownsOk = false;
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(id);
		parameters.add(duration);
		parameters.add(startTime);
		parameters.add(type);
		parameters.add(scheduled);
		parameters.add(unknownsOk);
		client.queueExecute(EuropaCommand.REGISTER_ACTIVITY, parameters);
		return 1;
	}
	
	/**
	 * Queue all incon values
	 */
	public int queueUpdateIncon(Conditions conditions) {
		int work = 0;
		if (conditions != null) {
			work += queueClaims(conditions.getClaims());
			work += queueSharables(conditions.getSharableResources());
			work += queueStates(conditions.getStateResources());
			if (EuropaPreferences.isTranslateNumericResources())
			    work += queueNumerics(conditions.getNumericResources());
		}
		return work;
	}

	private int queueClaims(List<Claim> claims) {
		Map<String, Claim> claimConditions = new HashMap<String, Claim>();
		for (Claim claim : claims) {
			Claim previous = claimConditions.put(claim.getName(), claim);
			if (previous != null) {
				String message = "multiple values for Claim: " + claim.getName();
				Logger.getLogger(EuropaQueuer.class).warn(message);
			}
		}
		int work = 0;
		List<EClaimableResourceDef> claimDefs = ActivityDictionary.getInstance().getDefinitions(EClaimableResourceDef.class);
		for (EClaimableResourceDef claimDef : claimDefs) {
			String name = claimDef.getName();
			boolean used = false;
			Claim claim = claimConditions.get(name);
			if (claim == null) {
				String message = "Missing claim in initial conditions: " + name + ", using default: " + used;
				Logger.getLogger(EuropaQueuer.class).warn(message);
			} else {
				used = claim.isUsed();
			}
			String parameterName = "_" + name;
			String parameterValue = (used ? "0" : "1");
			if (queueInconParameter(parameterName, parameterValue, "float")) {
				work++;
			}
		}
		return work;
	}

	private int queueSharables(List<SharableResource> sharables) {
		Map<String, SharableResource> sharableConditions = new HashMap<String, SharableResource>();
		for (SharableResource sharable : sharables) {
			SharableResource previous = sharableConditions.put(sharable.getName(), sharable);
			if (previous != null) {
				String message = "multiple values for Claim: " + sharable.getName();
				Logger.getLogger(EuropaQueuer.class).warn(message);
			}
		}
		int work = 0;
		List<ESharableResourceDef> shareDefs = ActivityDictionary.getInstance().getDefinitions(ESharableResourceDef.class);
		for (ESharableResourceDef shareDef : shareDefs) {
			String name = shareDef.getName();
			Float capacity = shareDef.getCapacity() == null ? null : shareDef.getCapacity().floatValue();
			if (capacity == null) {
				String message = "Missing capacity for SharableResource: " + name + ", using 1.";
				Logger.getLogger(EuropaQueuer.class).warn(message);
				capacity = 1.0f;
			}
			int shared = 0; // default value
			SharableResource sharable = sharableConditions.get(name);
			if (sharable == null) {
				String message = "Missing SharableResource in initial conditions: " + name + ", using default: " + shared;
				Logger.getLogger(EuropaQueuer.class).warn(message);
			} else {
				int used = sharable.getUsed();
				if (used < 0) {
					String message = "Negative SharableResource in initial conditions: " + name + " = " + used ;
					Logger.getLogger(EuropaQueuer.class).warn(message);
					// NOTE: not clamping here to allow for working around model bugs by using strange inputs
				} else if (used > capacity) {
					String message = "SharableResource in initial conditions exceeds capactiy: " + name + " = " + used + " > " + capacity;
					Logger.getLogger(EuropaQueuer.class).warn(message);
					// NOTE: not clamping here to allow for working around model bugs by using strange inputs
				}
				shared = used;
			}
			String parameterName = "_" + name;
			String parameterValue = String.valueOf(capacity - shared);
			if (queueInconParameter(parameterName, parameterValue, "float")) {
				work++;
			}
		}
		return work;
	}

	private int queueNumerics(List<NumericResource> numerics) {
		Map<String, NumericResource> numericConditions = new HashMap<String, NumericResource>();
		for (NumericResource numeric : numerics) {
			NumericResource previous = numericConditions.put(numeric.getName(), numeric);
			if (previous != null) {
				String message = "multiple values for NumericResource: " + numeric.getName();
				Logger.getLogger(EuropaQueuer.class).warn(message);
			}
		}
		int work = 0;
		List<ENumericResourceDef> numericDefs = ActivityDictionary.getInstance().getDefinitions(ENumericResourceDef.class);
		for (ENumericResourceDef numericDef : numericDefs) {
		    // Filter out claimables, etc., leaving pure numeric resources
		    if (!(numericDef instanceof EExtendedNumericResourceDef)) {
			String name = numericDef.getName();
			Float level = 0.0f; // default value
			NumericResource numeric = numericConditions.get(name);
			if (numeric == null) {
				String message = "Missing numeric in initial conditions: " + name + ", using default: " + level;
				Logger.getLogger(EuropaQueuer.class).warn(message);
			} else {
				level = numeric.getFloat();
			}
			String parameterName = "_" + name;
			String parameterValue = String.valueOf(level);
			if (queueInconParameter(parameterName, parameterValue, "float")) {
				work++;
			}
		    }
		}
		return work;
	}

	private int queueStates(List<StateResource> states) {
		Map<String, StateResource> stateConditions = new HashMap<String, StateResource>();
		for (StateResource state : states) {
			StateResource previous = stateConditions.put(state.getName(), state);
			if (previous != null) {
				Logger.getLogger(EuropaQueuer.class).warn("multiple values for Claim: " + state.getName());
			}
		}
		int work = 0;
		List<EStateResourceDef> stateDefs = ActivityDictionary.getInstance().getDefinitions(EStateResourceDef.class);
		for (EStateResourceDef stateDef : stateDefs) {
			String name = stateDef.getName();
			List<String> values = stateDef.getAllowedStates();
			if ((values == null) || values.isEmpty()) {
				Logger.getLogger(getClass()).warn("no AllowedValues spec for StateResourceDef: " + stateDef.getName());
				// PHM 05/03/2013 skipping causes Europa error, use enum values instead
				values = new ArrayList();
				if (stateDef.getEnumeration() != null) {
					for (EEnumLiteral l : stateDef.getEnumeration().getELiterals()) {
						values.add(l.getLiteral());
					}
				} else {
					Object eTypeDef = stateDef.getEType();
					if (eTypeDef instanceof EEnum) {
						for (EEnumLiteral l : ((EEnum) eTypeDef).getELiterals()) {
							values.add(l.getLiteral());
						}
					}
				}
			}
			if ((values == null) || values.isEmpty()) {
				Logger.getLogger(getClass()).error("no Enum Values spec for StateResourceDef: " + stateDef.getName());
			}
			String state = values.get(0); // default value
			StateResource stateResource = stateConditions.get(name);
			if (stateResource == null) {
				// Logger.getLogger(getClass()).warn("Missing StateResource in initial conditions: " + name + ", using default: " + state);
			} else if (!values.contains(stateResource.getState())) {
				Logger.getLogger(getClass()).warn("The initial conditions state " + stateResource.getState() + " wasn't defined for StateResourceDef: " + name + ", using default: " + state);
			} else {
				state = stateResource.getState();
			}
			work += queueInconParameter(name, values, state);
		}
		return work;
	}

	private int queueInconParameter(String name, List<String> possibleValues, String currentValue) {
		int work = 0;
		for (String value : possibleValues) {
			String parameterName = "_" + name + "_" + value;
			String parameterValue = (CommonUtils.equals(value, currentValue) ? TRUE_VALUE : FALSE_VALUE);
			if (queueInconParameter(parameterName, parameterValue, "float")) {
				work++;
			}
		}
		return work;
	}

	private boolean queueInconParameter(String name, String value, String valueType) {
		
		if (client.isInconParameter(NDDLUtil.escape(name))) {
			Vector<Object> parameters = new Vector<Object>();
			parameters.add(INCON_ID);
			String sanitized_name = NDDLUtil.escape(name);
			parameters.add(sanitized_name );
			parameters.add(value);
			parameters.add(valueType);
			client.queueExecute(EuropaCommand.SET_ACTIVITY_PARAM_VALUE, parameters);
			return true;
		}
		else {
			LogUtil.debug("Skipping update to Incon parameter that can't be found on the europa server :"+name);
			return false;
		}
	}

}
