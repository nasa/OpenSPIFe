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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.Graph;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.ActivityTemporalEffectDependency;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyMaintenanceSystem;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.SummingDependency;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.TemporalActivityDependency;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.ENumericResourceEffectMode;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceEffect;
import gov.nasa.ensemble.dictionary.ETemporalEffect;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.javascript.rhino.FormulaInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.jscience.physics.amount.Amount;

public class ResourceUtils {
	
	public static final String ANNOTATION_DETAIL_COLOR = "color";
	public static final String ANNOTATION_SOURCE_EFFECT = "effect";
	public static final String STATE_RESOURCE_NO_CHANGE = "NO_CHANGE";

	public static Profile getProfile(EPlan plan, String profileKey) {
		ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		return member == null ? null : member.getProfile(profileKey);
	}
	
	public static Profile getProfile(ProfileReference reference) {
		String profileKey = reference.getProfileKey();
		if (profileKey == null || profileKey.trim().length() == 0) {
			return null;
		}
		if (reference.eContainer() instanceof ProfileMember) {
			ProfileMember profileMember = (ProfileMember) reference.eContainer();
			if (profileMember != null) {
				EPlan plan = EPlanUtils.getPlan(profileMember.getPlanElement());
				if (plan != null) {
					ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
					if (member != null) {
						return member.getProfile(profileKey);
					}
				}
			}
		}
		return null;
	}
	
	public static Set<String> getPlanPropertyReferences(String expression) {
		return FormulaInfo.getPropertyReferences(expression, "plan");
	}
	
	public static String getExpression(EStructuralFeature feature) {
		return EMFUtils.getAnnotation(feature, "resource", "formula");
	}
	
	public static String getColorExpression(EClass def) {
		return EMFUtils.getAnnotation(def, ANNOTATION_SOURCE_EFFECT, ANNOTATION_DETAIL_COLOR);
	}
	
	public static String getActivityResourceTimepointExpression(ETemporalEffect<? extends EResourceDef> effect, Timepoint timepoint) {
		return getActivityResourceTimepointExpression(effect, timepoint, null);
	}
	
	public static Object getStateResourceEffect(ActivityTemporalEffectDependency dependency, String literal) {
		EStateResourceEffect effect = (EStateResourceEffect) dependency.getEffect();
		Object effectValue = null;
		EStateResourceDef definition = effect.getDefinition();
		if (literal != null) {
			if (EcorePackage.Literals.ESTRING != definition.getEAttributeType()) {
				try {
					effectValue = ResourceUtils.evaluateStateValue(definition, literal);
				} catch (Exception e) {
					// we tried
				}
			}
			
			if (effectValue == null) {
				try {
					effectValue = dependency.getValueUnsafely(literal, dependency.getDate());
					if (CommonUtils.equals(STATE_RESOURCE_NO_CHANGE, effectValue)) {
						return effectValue;
					}
				} catch (Exception y) {
					// we tried, and tried, and have one last thing to try (if it is a string)
				}
			}
			if (effectValue == null && EcorePackage.Literals.ESTRING == definition.getEAttributeType()) {
				effectValue = literal;
			}
		}
		
		EClassifier eType = definition.getEType();
		if (eType instanceof EEnum && effectValue instanceof String) {
			String key = (String) effectValue;
			EEnum eEnum = (EEnum) eType;
			EEnumLiteral eEnumLiteral = eEnum.getEEnumLiteral(key);
			if (eEnumLiteral != null) {
				eEnumLiteral = eEnum.getEEnumLiteralByLiteral(key);
			}
			effectValue = eEnumLiteral != null ? eEnumLiteral : effectValue;
		}
		return effectValue;
	}
	
	public static String getActivityResourceTimepointExpression(ETemporalEffect<? extends EResourceDef> effect, Timepoint timepoint, EObject object) {
		String expression = null;
		switch (timepoint) {
			case START: 
				expression = effect.getStartEffect();
				break;
			case END:
				expression = effect.getEndEffect();
				break;
		}
		if (expression != null 
				&& effect instanceof ENumericResourceEffect
				&& ((ENumericResourceEffect)effect).getMode() == ENumericResourceEffectMode.SET) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("(").append(expression).append(")");
			if (object != null) {
				String id = EcoreUtil.getID(object);
				buffer.append(" - plan[\"").append(getObjectResourceID(id, effect.getDefinition())).append("\"]");
			} else {
				buffer.append(" - plan[\"").append(effect.getName()).append("\"]");
			}
			expression = buffer.toString();
		}
		return expression;
	}
	
	public static Object evaluateStateValue(EStateResourceDef resourceDef, String literal) {
		Object value = null;
		if (literal != null) {
			EDataType dataType = resourceDef.getEAttributeType();
			if (dataType == null) {
				EEnum enumeration = resourceDef.getEnumeration();
				value = enumeration.getEEnumLiteral(literal);
			} else if (dataType instanceof EEnum) {
				EEnum enumeration = (EEnum)dataType;
				value = enumeration.getEEnumLiteral(literal);
			} else {
				EFactory factory = dataType.getEPackage().getEFactoryInstance();
				value = factory.createFromString(dataType, literal);
			}
		}
		return value;
	}
	
	/**
	 * 
	 * @param objectEcoreId get this ID from calling EcoreUtil.getID(object)
	 * @param def
	 * @return
	 */
	public static String getObjectResourceID(String objectEcoreId, EResourceDef def) {
		return objectEcoreId + "_" + def.getName();
	}

	public static void printDependencyGraphDotty(Graph graph) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ResourceUtils.createDependencyGraphDotty(graph, baos);
			System.out.println(baos.toString());
			baos.close();
		} catch (IOException e) {
			LogUtil.error(e);
		}
	}
	
	public static void createDependencyGraphDotty(Graph graph, OutputStream stream) {
		PrintStream out = new PrintStream(stream);
		Set<String> set = new HashSet<String>();
		out.println("digraph diagram {");
		for (Dependency d : graph.getDependencies()) {
			for (Dependency n : d.getNext()) {
				if (n instanceof SummingDependency) {
					continue;
				}
				String link = new StringBuffer("\t")
								.append(d.getName().replaceAll("[^a-zA-Z0-9_]", "_"))
								.append(" -> ")
								.append(n.getName().replaceAll("[^a-zA-Z0-9_]", "_"))
								.append(";").toString();
				if (!set.contains(link)) {
					set.add(link);
					out.println(link);
				}
			}
		}
		out.print("}");
	}
	
	@SuppressWarnings("unchecked")
	public static Date getDate(TemporalActivityDependency dependency, TemporalMember temporalMember) {
		Date startTime = temporalMember.getStartTime();
		if (Timepoint.START == dependency.getTimepoint()) {
			return startTime;
		}
		if (startTime==null) {
			return null;
		}
		
		if (hasUpdatedDuration(dependency)) {
			return temporalMember.getEndTime();
		} 
		return startTime;
	}
	
	public static boolean hasUpdatedDuration(TemporalActivityDependency dependency) {
		DependencyMaintenanceSystem dms = dependency.getDependencyMaintenanceSystem();
		EActivity eActivity = dependency.getActivity();
		Dependency durationDependency = dms.getDurationDependency(eActivity);
		if (durationDependency == null) {
			return true;
		}
		
		Amount cachedDuration = (Amount) durationDependency.getValue();
		Amount<Duration> currentDuration = eActivity.getMember(TemporalMember.class).getDuration();
		return currentDuration != null 
				&& cachedDuration != null 
				&& currentDuration.approximates(cachedDuration);
	}
	
}
