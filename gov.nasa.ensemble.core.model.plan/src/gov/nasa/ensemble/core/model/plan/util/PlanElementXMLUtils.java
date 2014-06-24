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
/**
 * 
 */
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.emf.transaction.RunnableWithResult;

public final class PlanElementXMLUtils {

	private static EPlan DUMMY_PLAN = null;
	private static Map<String, Object> OPTIONS = null;
	
	public static void setUpMap() {
		DUMMY_PLAN = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEPlan();
		DUMMY_PLAN.setName("DUMMY_PLAN");
		final ResourceSet set = TransactionUtils.createTransactionResourceSet(false);
		TransactionUtils.writing(DUMMY_PLAN, new Runnable() {
			public void run() {
				Resource resource = set.createResource(URI.createFileURI("dummy.plan"));
				resource.getContents().add(DUMMY_PLAN);
			}
		});
		// PlanFactory.getInstance().createPlanInstance("DUMMY_PLAN");
		OPTIONS = EMFUtils.getDefaultXmlOptions();
		OPTIONS.put(XMLResource.OPTION_XML_MAP, new PlanXMLMapImpl());
	}

	public static void tearDownMap() {
		DUMMY_PLAN = null;
		OPTIONS = null;
	}
	
	public static String getString(EPlanElement element) {
		if (element instanceof EPlanChild) {
			final EPlanChild child = (EPlanChild) element;
			if (child.getParent() == null) {
				return TransactionUtils.writing(DUMMY_PLAN, new RunnableWithResult.Impl<String>() {
					public void run() {
						EList<EPlanChild> children = DUMMY_PLAN.getChildren();
						children.add(child);
						setResult(EMFUtils.convertToXML(child, OPTIONS));
						children.remove(child);
					}
				});
			}
		}
		return EMFUtils.convertToXML(element, OPTIONS);
	}
	
	/**
	 * TODO: We should just make this an annotation so that we can just turn off
	 * this check via the model
	 */
	public static final class PlanXMLMapImpl extends XMLMapImpl {
		boolean initializedPlanElement = false;
		boolean initializedCommonMember = false;
		boolean initializedScoreMember = false;

		@Override
		public EStructuralFeature getFeature(EClass class1,
				String namespaceURI, String name) {
			if (PlanPackage.Literals.EPLAN_ELEMENT.isSuperTypeOf(class1)
					&& PlanPackage.Literals.EPLAN__RUNTIME_ID.equals(name)) {
				return null;
			}
			if (class1 == PlanPackage.Literals.COMMON_MEMBER
					&& PlanPackage.Literals.COMMON_MEMBER__DIFF_ID.equals(name)) {
				return null;
			}
			return super.getFeature(class1, namespaceURI, name);
		}

		@Override
		public List<EStructuralFeature> getFeatures(EClass class1) {
			List<EStructuralFeature> features = super.getFeatures(class1);
			if (PlanPackage.Literals.EPLAN_ELEMENT.isSuperTypeOf(class1)) {
				if (!initializedPlanElement) {
					features.remove(PlanPackage.Literals.EPLAN_ELEMENT__PERSISTENT_ID);
					initializedPlanElement = true;
				}
			}
			if (class1 == PlanPackage.Literals.COMMON_MEMBER) {
				if (!initializedCommonMember) {
		            features.remove(PlanPackage.Literals.COMMON_MEMBER__DIFF_ID);
		            initializedCommonMember = true;
				}
			}
			if ("ScoreMember".equals(class1.getName())) {
				if (!initializedScoreMember) {
					Iterator<EStructuralFeature> iterator = features.iterator();
					while (iterator.hasNext()) {
						EStructuralFeature feature = iterator.next();
						if ("templateId".equals(feature.getName())) {
							iterator.remove();
							break;
						}
					}
					initializedScoreMember = true;
				}
			}
			return features;
		}
	}

}
