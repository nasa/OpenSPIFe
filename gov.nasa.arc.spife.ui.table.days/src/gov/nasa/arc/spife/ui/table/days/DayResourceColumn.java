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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.util.ComputableAmountStringifier;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectUtils;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionAddOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionRemoveOperation;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.swt.widgets.TreeItem;
import org.jscience.physics.amount.Amount;

public class DayResourceColumn extends AbstractMergeColumn<DayResourceFacet> {
	
	private final EObject object;
	private String objectName;
	protected final EResourceDef resourceDef;
	private static final IStringifier<ComputableAmount> STRINGIFIER = new ComputableAmountStringifier();
	
	
	public DayResourceColumn(IMergeColumnProvider provider, EObject object, EResourceDef def) {
		super(provider, null, 30);
		this.resourceDef = def;
		this.object = object;
		IItemLabelProvider labeler = EMFUtils.adapt(object, IItemLabelProvider.class);
		if (labeler != null) {
			objectName = labeler.getText(object);
		}
		if(objectName == null) {
			// START: TEMP
			objectName = ProfileUtil.getObjectId(object);
			// END: TEMP
		}
		setHeaderName(objectName + " " + def.getName());
	}
	
	
	
	@Override
	public DayResourceFacet getFacet(Object element) {
		if (element instanceof List) {
			Collection<EActivity> activities = EPlanUtils.computeContainedActivities((List) element);
			EActivity representativeActivity = null;
			Amount amount = null;
			for (EActivity activity : activities) {
				if (representativeActivity == null) {
					representativeActivity = activity;
				}
				ComputableAmount value = ADEffectUtils.getEffectAmount(activity, object, resourceDef);
				if (value == null) {
					continue;
				}
				Amount<?> valueAmount = value.getAmount();
				if (valueAmount == null || ((Double)valueAmount.getEstimatedValue()).isNaN()) {
					continue;
				}
				if (amount == null) {
					amount = valueAmount;
				} else {
					amount = amount.plus(valueAmount);
				}
			}
			if (amount == null) {
				return null;
			}
			ComputableAmount facetAmount = JScienceFactory.eINSTANCE.createComputableAmount(amount, ComputingState.COMPLETE);
			return new DayResourceFacet(activities, facetAmount);
		}

		if (!(element instanceof EPlanElement)) {
			return null;
		}
		EPlanElement ePlanElement = (EPlanElement) element;
		ComputableAmount value = ADEffectUtils.getEffectAmount(ePlanElement, object, resourceDef);
		return new DayResourceFacet(ePlanElement, value);
	}
	
	@Override
	public String getText(DayResourceFacet facet) {
		if (facet == null) {
			return "-";
		}
		return STRINGIFIER.getDisplayString(facet.getValue());
	}
	
	private EStructuralFeature getFacetFeature(EPlanElement activity) {
		EObject data = activity.getData();
		if (data == null) {
			return null;
		}
		EClass klass = data.eClass();
		for (EStructuralFeature feature : klass.getEStructuralFeatures()) {
			EClassifier type = feature.getEType();
			if (type == object.eClass()) {
				return feature;
			}
		}
		return null;
	}

	@Override
	public boolean canModify(DayResourceFacet facet) {
		return facet.getElement() instanceof EActivity;
	}
	
	@Override
	public boolean editOnActivate(DayResourceFacet facet, IUndoContext undoContext, TreeItem item, int index) {
		Object element = facet.getElement();
		if (element instanceof EActivity) {
			EActivity activity = (EActivity) element;
			EStructuralFeature feature = getFacetFeature(activity);
			if (feature == null) {
				return false;
			}
			
			String featureName = EMFUtils.getDisplayName(activity, feature);
			IUndoableOperation operation = null;
			if (feature.isMany()) {
				EcoreEList value = (EcoreEList) (activity.getData().eGet(feature));
				if (!value.contains(object)) {
					operation = new FeatureTransactionAddOperation("add "+featureName, value, object);
				} else {
					operation = new FeatureTransactionRemoveOperation("remove "+featureName, value, object);
				}
			} else {
				operation = new FeatureTransactionChangeOperation("change "+featureName, activity, feature, object);
			}
			CommonUtils.execute(operation, undoContext);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean editOnDoubleClick() {
		return true;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		return true;
	}
	
	@Override
	public String toString() {
		return "{ " + getHeaderName() + " }";
	}
	
}
