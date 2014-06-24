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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.ComputableAmountStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.jscience.physics.amount.Amount;

public class ResourceColumn extends AbstractMergeColumn<ComputableAmount> {

	protected static final EnsembleUnitFormat UNIT_FORMAT = EnsembleUnitFormat.INSTANCE;
	private static final IStringifier<ComputableAmount> STRINGIFIER = new ComputableAmountStringifier();
	protected final EResourceDef resourceDef;
	
	private final Comparator<ComputableAmount> resourceComparator = new ResourceComparator();
	
	public ResourceColumn(IMergeColumnProvider provider, EResourceDef def) {
		super(provider, computeHeaderName(def), 30);
		this.resourceDef = def;
    }

	private static String computeHeaderName(EResourceDef def) {
		String name = def.getName();
		if (def instanceof ESummaryResourceDef) {
			return name + " (Summary)";
		}
		return name;
	}

	@Override
	public boolean needsUpdate(Object feature) {
	    EAttribute adEffectEntryValue = ActivityDictionaryPackage.Literals.AD_EFFECT_ENTRY__VALUE;
	    EReference adEffectMemberEffects = ActivityDictionaryPackage.Literals.AD_EFFECT_MEMBER__EFFECTS;
		return (feature == adEffectEntryValue) || (feature == adEffectMemberEffects);
	}
	
	@Override
	public ComputableAmount getFacet(Object element) {
		if (element instanceof List) {
			List<EPlanElement> list = new ArrayList<EPlanElement>();
			for (Object object : (List) element) {
				if (object instanceof EPlanElement) {
					list.add((EPlanElement)object);
				}
			}
			Amount amount = null;
			for (EPlanElement pe : EPlanUtils.getConsolidatedPlanElements(list)) {
				ComputableAmount value = ADEffectUtils.getEffectAmount(pe, resourceDef);
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
			return JScienceFactory.eINSTANCE.createComputableAmount(amount, ComputingState.COMPLETE);
		}
		if (element instanceof EPlanElement) {
			EPlanElement pe = (EPlanElement) element;
			return ADEffectUtils.getEffectAmount(pe, resourceDef);
		}
		return null;
	}

	@Override
	public String getText(ComputableAmount computableAmount) {
		if (computableAmount == null) {
			return "_";
		}
		
		//*** SPF-7319 ***//
		Amount<?> amount = computableAmount.getAmount();
		if (amount != null) {
			Unit<?> unit = amount.getUnit();
			if (unit != null) {
				final Unit<?> su = unit.getStandardUnit();
				if (su != null && SI.BIT.equals(su)) {
					Number number = AmountUtils.getNumericValue(amount);
					if (AmountUtils.approximatesZero(amount)) {
						return number + " " + UNIT_FORMAT.format(JSciencePackage.MEGA_BIT);
					} else if (number.floatValue() > 0.0 && ((JSciencePackage.MEGA_BIT.equals(unit) && number.floatValue() < 1.0 )
							|| JSciencePackage.KILO_BIT.equals(unit) || SI.BIT.equals(unit))) {
						return "< 1 "+ UNIT_FORMAT.format(JSciencePackage.MEGA_BIT);
					} 
				}
			}
		}
	    
		return STRINGIFIER.getDisplayString(computableAmount);
	}
	
	@Override
	public Comparator<ComputableAmount> getComparator() {
		return resourceComparator;
	}
	

	public class ResourceComparator implements Comparator<ComputableAmount> {

		@Override
		public int compare(ComputableAmount value1, ComputableAmount value2) {
			Integer null1 = compareNull(value1, value2);
			if (null1 != null) return null1;
			Amount amount1 = value1.getAmount();
			Amount amount2 = value2.getAmount();
			Integer null2 = compareNull(amount1, amount1);
			if (null2 != null) return null1;
			return amount1.compareTo(amount2);
		}
		
		/**
		 * Take care of cases where one or both values are null.
		 * Return an integer result if this applies, null if it's the normal case.
		 * Policy is that null is less than any number, and two nulls are equl.
		 */
		private Integer compareNull(Object a, Object b) {
			if (a==null && b==null) return 0;
			if (a==null) return -1;
			if (b==null) return +1;
			return null;
		}

	}

	
}
