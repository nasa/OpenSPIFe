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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.hibernate.HibernateMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.IMember;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.emf.transaction.PreCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelection;

public class WrapperUtils {

	private static final EAttribute EPLAN_ELEMENT_NAME = PlanPackage.Literals.EPLAN_ELEMENT__NAME;

	public static final String ATTRIBUTE_NAME = "ensemble_name";

	public static void dispose(final EPlan ePlan) {
		if (ePlan == null) {
			return;
		}
		EPlanUtils.clearConsolidatedPlanElementsCache();
		HibernateMember member = ePlan.getMember(HibernateMember.class, true);
		final Plan wrapper = (Plan)member.getWrapper();
		TransactionalEditingDomain domain = TransactionUtils.getDomain(ePlan);
		if (wrapper != null) {
			if (domain != null) {
				domain.addResourceSetListener(new PreCommitListener() {
					@Override
					public Command transactionAboutToCommit(ResourceSetChangeEvent event)
							throws RollbackException {
						if (event.getNotifications().isEmpty()) {
							return null;
						}
						LogUtil.error("event during disposal of plan: " + ePlan.getName());
						throw new RollbackException(Status.CANCEL_STATUS);
					}
				});
			}
			if (wrapper != null) {
				wrapper.dispose();
			}
			member.setWrapper(null);
		}
		Resource resource = ePlan.eResource();
		if (domain != null) {
			domain.dispose();
			domain.getResourceSet().getResources().remove(resource);
		}
		if (resource != null) {
			resource.getContents().remove(ePlan);
		}
	}
	
	public static Plan getRegistered(EPlan ePlan) {
		if (ePlan == null) {
			return null;
		}
		HibernateMember member = ePlan.getMember(HibernateMember.class, true);
		Plan wrapper = (Plan)member.getWrapper();
		if (wrapper == null) {
			wrapper = new Plan(ePlan);
			member.setWrapper(wrapper);
		}
		return wrapper;
	}
	
	public static <T extends IMember> T getMember(EPlan plan, Class<T> memberClass) {
		Plan registered = getRegistered(plan);
		T member = null;
		if (registered != null) {
			member = registered.getMember(memberClass);
		}
		return member;
	}

	public static String mapStructuralFeatureToParameterName(EStructuralFeature structuralFeature) {
		return mapStructuralFeatureToParameterName(structuralFeature, true);
	}

	public static String mapStructuralFeatureToParameterName(EStructuralFeature structuralFeature, boolean warn) {
		if (structuralFeature.isTransient()) {
			return null;
		}
		if ((PlanPackage.Literals.EPLAN_PARENT__CHILDREN == structuralFeature) 
			|| (PlanPackage.Literals.EACTIVITY__IS_SUB_ACTIVITY == structuralFeature)
			|| (PlanPackage.Literals.EACTIVITY__CHILDREN == structuralFeature)
			|| (PlanPackage.Literals.EPLAN_ELEMENT__MEMBERS == structuralFeature)
//			|| (PlanPackage.Literals.EActivity_ListPosition() == attribute) 
//			|| (PlanPackage.Literals.EActivityGroup_ListPosition() == attribute) 
//			|| (HibernatePackage.Literals.EPlanWrapper() == attribute)
//			|| (HibernatePackage.Literals.EActivityGroupWrapper() == attribute)
//			|| (HibernatePackage.Literals.EActivityWrapper() == attribute)
			) {
			return null;
		}
		if (EPLAN_ELEMENT_NAME == structuralFeature) {
			return ATTRIBUTE_NAME;
		}
		EAnnotation hibernateAnnotation = structuralFeature.getEAnnotation("hibernate");
		if (hibernateAnnotation != null) {
			return hibernateAnnotation.getDetails().get("parameterName");
		}
		if (structuralFeature instanceof EReference) {
			return null;
		}
		if (warn) {
			warnAboutStructuralFeature(structuralFeature);
		}
		return structuralFeature.getName();
	}

	private static void warnAboutStructuralFeature(EStructuralFeature structuralFeature) {
		String name = structuralFeature.getName().intern();
		LogUtil.warnOnce("unknown structural feature: " + name);
	}

	public static void writeExternal(final ObjectOutput out, final EPlanElement eObject) throws IOException {
		IOException result = TransactionUtils.reading(eObject, new RunnableWithResult.Impl<IOException>() {
			@Override
			public void run() {
				try {
					out.writeObject(eObject);
				} catch (IOException e) {
					Logger.getLogger(WrapperUtils.class).error("writeExternal", e);
					setResult(e);
				}
			}
		});
		if (result != null) {
			throw result;
		}
	}

	public static EObject readExternal(ObjectInput in) throws IOException {
		try {
			return (EObject)in.readObject();
		} catch (IOException e) {
			Logger.getLogger(WrapperUtils.class).error("readExternal", e);
			throw e;
		} catch (final ClassNotFoundException e) {
			Logger.getLogger(WrapperUtils.class).error("readExternal", e);
			throw new IOException(e.getMessage()) {
				private static final long serialVersionUID = 1L;

				@Override
				public Throwable getCause() {
					return e;
				}
			};
		}
	}
	
	public static void setParameterName(EStructuralFeature feature, String name) {
		EAnnotation hibernateAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		hibernateAnnotation.setSource("hibernate");
		hibernateAnnotation.getDetails().put("parameterName", name);
		feature.getEAnnotations().add(hibernateAnnotation);
	}

	public static void initHibernateMember(Plan planElement, EPlan ePlan) {
		HibernateMember member = ePlan.getMember(HibernateMember.class, true);
		member.setWrapper(planElement);
	}

	/**
	 * If the selection is a structured selection, it can have elements. Return the su set of the
	 * selection's elements that are plan elements.
	 * @param selection a selection
	 * @return all elements of the selection that are plan elements
	 * @deprecated - use PlanEditorUtils instead
	 */
	@Deprecated
	public static Set<EPlanElement> emfFromSelection(ISelection selection) {
		return WidgetUtils.typeFromSelection(selection, EPlanElement.class);
	}

	/**
	 * Compare in the "old way"
	 * @param plan
	 * @param plan2
	 */
	public static final void assertPlanDeepEquals(EPlan plan, EPlan plan2) {
		Assert.assertEquals("plan names didn't match", plan.getName(), plan2.getName());
    	List<? extends EActivity> planActivities = EPlanUtils.getActivities(plan);
    	List<? extends EActivity> plan2Activities = EPlanUtils.getActivities(plan2);
		int planActivityCount = planActivities.size();
		int plan2ActivityCount = plan2Activities.size();
		Assert.assertEquals("the plans have different numbers of activities", planActivityCount, plan2ActivityCount);
    	for (int i = 0 ; i < planActivityCount ; i++) {
    		EActivity a1 = planActivities.get(i);
			EActivity a2 = plan2Activities.get(i);
			assertActivityDeepEquals(a1, a2);
    	}
		if (!EcoreUtil.equals(plan, plan2)) {
			System.out.println("not ecore equals");
//			return false;
		}
	}

	/**
	 * Compare in the "old way"
	 * @param plan
	 * @param plan2
	 */
	public static void assertActivityDeepEquals(EActivity a1, EActivity a2) {
		if (CommonUtils.equals(a1, a2)) {
			return;
		}
		assertParametersEqual(a1, a2);
		List<EMember> members1 = a1.getMembers();
		Assert.assertEquals("activities should have the same number of members", members1.size(), a2.getMembers().size());
		for (EMember member1 : members1) {
			EMember member2 = a2.getMember(member1.getKey());
			assertParametersEqual(member1, member2);
		}
    }

	private static void assertParametersEqual(EObject o1, EObject o2) {
		Assert.assertEquals("object classes should match", o1.eClass(), o2.eClass());
		for (EStructuralFeature feature : o1.eClass().getEAllStructuralFeatures()) {
			String parameterName = WrapperUtils.mapStructuralFeatureToParameterName(feature, false);
			if ((parameterName != null) && !parameterName.startsWith("ensemble_")) { // skip internal parameters that should be null
				Object object1 = o1.eGet(feature);
				Object object2 = o2.eGet(feature);
				if (!CommonUtils.equals(object1, object2)) {
					// what about valueString?
					String explanation = "feature named '" + feature.getName() + "' has a different value:";
					Assert.fail(explanation + " " + object1 + " & " + object2);
				}
			}
		}
    }

	public static String getAttributeValue(EPlan plan, String attribute) {
		for (EMember member : plan.getMembers()) {
			EClass klass = member.eClass();
			for (EStructuralFeature feature : klass.getEAllStructuralFeatures()) {
				String parameterName = WrapperUtils.mapStructuralFeatureToParameterName(feature, false);
				if (attribute.equals(parameterName)) {
					Object object = member.eGet(feature);
					return String.valueOf(object);
				}
			}
		}
		return ADParameterUtils.getParameterString(plan, attribute);
	}

	public static boolean sameFeature(EAttribute parameterDef, EStructuralFeature feature) {
		String parameterName = mapStructuralFeatureToParameterName(feature, false);
		String defName = mapStructuralFeatureToParameterName(parameterDef, false);
		return (defName.equalsIgnoreCase(parameterName));
	}
	
}
