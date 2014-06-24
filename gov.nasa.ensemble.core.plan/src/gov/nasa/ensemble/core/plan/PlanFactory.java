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
package gov.nasa.ensemble.core.plan;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADParameterMemberFactory;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceFactoryImpl;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.measure.quantity.Duration;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.SI;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.graphics.RGB;
import org.jscience.physics.amount.Amount;

/**
 * A singleton class whose instance can create {@link EPlan}s,
 * {@link EActivityGroup}s and {@link EActivity}s
 */
public class PlanFactory implements MissionExtendable {

	private static PlanFactory instance;

	protected static final Logger trace = Logger.getLogger(PlanFactory.class);
	protected static final BaseUnit<Duration> DURATION_UNITS = SI.SECOND;
	protected static final String DEFAULT_DURATION
		= EnsembleProperties.getProperty("ensemble.plan.defaultActivityDuration");

	protected PlanResourceFactoryImpl planResourceFactory = new PlanResourceFactoryImpl();


	protected PlanFactory() {
		// default constructor
	}

	/**
	 * @return the PlanFactory singleton instance
	 */
	public static PlanFactory getInstance() {
		if (instance == null) {
			try {
				instance = MissionExtender.construct(PlanFactory.class);
			} catch (ConstructionException e) {
				instance = new PlanFactory();
			}
		}
		return instance;
	}

	public final EPlan createPlanInstance(String planName) {
		EPlan plan = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEPlan();
		plan.setName(planName);
		plan.setData(ADParameterMemberFactory.FACTORY.createData(PlanPackage.Literals.EPLAN));
		plan.getMember(TemporalMember.class, true).setUseChildTimes(true);
		plan.getMember(CommonMember.class).setNotes("");
		URI uri = null;
		IFile iFle = PlanUtils.getFile(plan);
		if (iFle != null) {
			uri = EMFUtils.getURI(iFle);
		} else {
			try {
				File file = FileUtilities.createTempFile("plan", "plan");
				uri = URI.createFileURI(file.getAbsolutePath()).appendFileExtension("plan");
			} catch (IOException e) {
				LogUtil.error(e);
			}
		}
//		Resource.Factory resourceFactory = getResourceFactoryRegistry().getFactory(uri, contentType);
		Resource resource = planResourceFactory.createResource(uri);
		resource.getContents().add(plan);
		return plan;
	}

	public EActivityGroup createActivityGroupInstance() {
		return gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEActivityGroup();
	}

	public EActivity createActivityInstance() {
		return gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEActivity();
	}

	/**
	 * Creates a {@link Plan} with the given name.
	 */
	public final EPlan createPlan(final String planName) {
		EPlan plan = createPlanInstance(planName);
		initPlan(plan);
		return plan;
	}

	/**
	 * Called after creation to add required parameters and start member threads.
	 * @param plan - the plan
	 */
	public void initPlan(final EPlan plan) {
		final ResourceSet resourceSet;
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		if (domain != null) {
			resourceSet = domain.getResourceSet();
		} else {
			resourceSet = TransactionUtils.createTransactionResourceSet(false);
		}
		TransactionUtils.writing(resourceSet, new Runnable() {
			@Override
			public void run() {
				Resource resource = plan.eResource();
				if (resource.getResourceSet() == null) {
					resourceSet.getResources().add(resource);
				}
				String versionNumber = ActivityDictionary.getInstance().getVersion();
				if (versionNumber != null) {
					// Adding a reference to the current activity dictionary version number if it exists
					plan.getMember(ADPlanMember.class).setActivityDictionaryVersion(versionNumber);
				}
			}
		});
	}
	
	/**
	 * Creates an {@link EActivityGroup}.  Does not add it to a plan.
	 */
	public EActivityGroup createActivityGroup() {
		return createActivityGroup(null);
	}

	/**
	 * Creates an {@link EActivityGroup} within the given parent.
	 */
	public EActivityGroup createActivityGroup(final EPlanParent parent) {
		String nameOfPlan = "Unnamed";
		if (parent != null) {
			nameOfPlan = TransactionUtils.reading(parent, new RunnableWithResult.Impl<String>() {
				@Override
				public void run() {
					setResult(createName(parent, EPlanUtils.getActivityGroupDisplayName()));
				}
			});
		}
		final String name = nameOfPlan; 
		final EActivityGroup group = createActivityGroupInstance();
		TransactionUtils.writeIfNecessary(group, new Runnable() {
			@Override
			public void run() {
				group.setData(ADParameterMemberFactory.FACTORY.createData(PlanPackage.Literals.EACTIVITY_GROUP));
				group.setName(name);
				TemporalMember temporalMember = group.getMember(TemporalMember.class);
				temporalMember.setUseChildTimes(true);
				CommonMember commonMember = group.getMember(CommonMember.class);
				commonMember.setNotes("");
				RGB randomColor = ColorUtils.getRandomColor();
				commonMember.setColor(ColorUtils.getAsERGB(randomColor));
			}
		});
		return group;
	}

	/**
	 * Creates an {@link EActivity}. Does not add it to an {@link EActivityGroup}.
	 *
	 * @param def
	 *            an {@link EActivityDef} from which to construct the activity
	 * @return a newly created and initialized {@link EActivity}
	 */
	public EActivity createActivity(EActivityDef def) {
		return createActivity(def, null);
	}

	public EActivity createActivity(EActivityDef def, EPlanElement parent) {
		boolean isSubActivity = DictionaryUtil.isHidden(def);
		EActivity activity = createEActivity(def, parent, isSubActivity);
		if (activity == null) {
			return null;
		}
		if (parent instanceof EActivity) {
			EActivity parentActivity = (EActivity) parent;
			parentActivity.getChildren().add(activity);
		}

		return activity;
	}

	public EActivity createEActivity(EActivityDef def, EPlanElement parent) {
		boolean isSubActivity = DictionaryUtil.isHidden(def);
		return createEActivity(def, parent, isSubActivity);
	}

	private EActivity createEActivity(EActivityDef def, EPlanElement eParent, boolean isSubActivity) {
		if (def == null) {
			return null;
		}
		EActivity activity = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEActivity();
		activity.setName(def.getName());
		activity.setIsSubActivity(isSubActivity || (eParent instanceof EActivity));
		activity.setData(ADParameterMemberFactory.FACTORY.createData(def));
		TemporalMember temporalMember = activity.getMember(TemporalMember.class);
		temporalMember.setStartTime(null);
		String duration = def.getDuration();
		if (duration == null) {
			duration = DEFAULT_DURATION;
		}
		if (duration != null) {
			// SPF-6383
			try {
				long time = DurationFormat.parseFormattedDuration(duration);
				Amount<Duration> amount = Amount.valueOf(time, DURATION_UNITS);
				temporalMember.setDuration(amount);
			} catch (Exception e) {
				// ignore these
			}
		}
		
		setDefaultMemberValues(activity, def);
		
		return activity;
	}
	
	private void setDefaultMemberValues(EActivity activity, EActivityDef def) {
		if (def == null) {
			return;
		}
		EAnnotation eAnnotation = def.getEAnnotation(DictionaryUtil.ANNOTATION_SOURCE_OVERRIDE);
		if (eAnnotation != null) {
			for (String key : eAnnotation.getDetails().keySet()) {
				try {
					setMemberValueFromAnnotation(activity, key, eAnnotation);
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}
	}

	private void setMemberValueFromAnnotation(EPlanElement pe, String key, EAnnotation eAnnotation) {
		int index = key.indexOf(':');
		if (index == -1) {
			return;
		}
		String memberKey = key.substring(0, index);
		if (memberKey == null) {
			return;
		}
		String featureKey = key.substring(index+1);
		if (featureKey == null) {
			return;
		}
		String valueString = eAnnotation.getDetails().get(key);
		if (memberKey != null && featureKey != null && valueString != null) {
			EMember member = pe.getMember(memberKey);
			EStructuralFeature feature = member.eClass().getEStructuralFeature(featureKey);
			if (member != null && feature != null) {
				if (feature instanceof EAttribute) {
					EDataType eDataType = ((EAttribute)feature).getEAttributeType();
					Object value = EcoreUtil.createFromString(eDataType, valueString);
					member.eSet(feature, value);
				}
			}
		}
	}

	/**
	 * Creates a name in a given PlanElement. If the given name already
	 * exists as a child, a counter will be appended and incremented until
	 * the modified name does not exist in the PlanElement.
	 *
	 * @return the baseName if there was no collision, otherwise the baseName
	 *         followed by a counter to make it unique.
	 */
	public String createName(final EPlanElement planElement, final String baseName) {
		if (planElement == null) {
			return baseName;
		}
		List<? extends EPlanElement> children = EPlanUtils.getChildren(planElement);
		NEXT_NAME: for (int counter = 1 ; ; counter++) {
			String name = baseName + " " + counter;
			for (EPlanElement child : children) {
				if (CommonUtils.equalsIgnoreCase(child.getName(), name)) {
					continue NEXT_NAME;
				}
			}
			return name;
		}
	}

}
