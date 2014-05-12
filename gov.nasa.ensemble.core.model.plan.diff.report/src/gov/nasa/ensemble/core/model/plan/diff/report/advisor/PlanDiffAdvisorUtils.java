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
package gov.nasa.ensemble.core.model.plan.diff.report.advisor;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameterOrReference;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalModifier;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.plan.temporal.TemporalModifierImpl;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class PlanDiffAdvisorUtils {
	
	public static IUndoableOperation getFeatureModificationOperation(ChangedByModifyingParameterOrReference modification) {
		EStructuralFeature feature = modification.getParameter();
		EPlanElement target = modification.getOldCopyOfOwner();
		EObject object = modification.getRelevantPartOf(target);
		Object newValue = modification.getNewValue();
		Object oldValue = modification.getOldValue();
		if (feature instanceof EReference) {
			if (((EReference)feature).isContainment()) {
				// need to copy the contained objects so the old objects don't have their container changed
				if (newValue instanceof EObject) {
					newValue = EMFUtils.copy((EObject)newValue);
				} else if (newValue instanceof Collection<?>) {
					newValue = EcoreUtil.copyAll((Collection<? extends EObject>)newValue);
				}
			}
			if (oldValue instanceof Collection<?>) {
				// need to copy the old list of referenced objects for use by Undo as it is modified by the update
				oldValue = new ArrayList((Collection)oldValue);
			}
		}
		if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME
				|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION
				|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME) {
			TemporalModifier modifier = TemporalModifier.getInstance();
			if (modifier instanceof TemporalModifierImpl) {
				return ((TemporalModifierImpl)modifier).set((TemporalMember)object, feature, newValue, CalculatedVariable.END);
			}
			return modifier.set((TemporalMember)object, feature, newValue);
		}
		return new FeatureTransactionChangeOperation("Update " + feature.getName(), object, feature, oldValue, newValue);
	}
	
}
