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
package gov.nasa.arc.spife.core.plan.editor.timeline.models;

import gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineFactory;
import gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

public class PlanReferencedObjectSectionContentProvider extends GroupingTimelineContentProvider {

	private static final ComparatorImpl COMPARATOR = new ComparatorImpl();

	private static final EObject VALUE_LOST = new EObjectImpl() { /* trivial instance */ };
	private static final ReferencedObjectRow UNTENDED_ROW = PlanTimelineFactory.eINSTANCE.createReferencedObjectRow();
	static {
		UNTENDED_ROW.setName("Untended");
		UNTENDED_ROW.setReference(VALUE_LOST);
	}
	
	private final PlanReferencedObjectSection section;

	public PlanReferencedObjectSectionContentProvider(EPlan plan, PlanReferencedObjectSection section) {
		super(plan);
		this.section = section;
	}
	
	@Override
	protected Comparator getGroupingValuesComparator() {
		return COMPARATOR;
	}
	
	@Override
	protected Collection getGroupingValues() {
		Collection values = super.getGroupingValues();
		if (!section.isShowUnreferecedRow()) { // will have untended row if it exists
			values.remove(VALUE_LOST);
		}
		return values;
	}

	@Override
	protected List<? extends Object> getActivityValues(EActivity activity) {
		boolean canHaveReference = false;
		List<Object> references = new ArrayList<Object>();
		EObject data = activity.getData();
		if (data != null) {
			for (EReference r : data.eClass().getEAllReferences()) {
				if (isRelevant(r)) {
					canHaveReference = true;
					references.addAll(EMFUtils.eGetAsList(data, r));
				}
			}
		}
		if (canHaveReference && references.isEmpty()) {
			references.add(VALUE_LOST);
		}
		return references;
	}
	
	@Override
	protected Object getValueWrapper(Object value) {
		if (VALUE_LOST == value) {
			return UNTENDED_ROW;
		}
		ReferencedObjectRow row = PlanTimelineFactory.eINSTANCE.createReferencedObjectRow();
		row.setReference((EObject) value);
		return row;
	}

	/**
	 * Slightly more cumbersome than necessary since the
	 * types do not seem to resolve properly against the
	 * EPackages and .equals or .isSuperType fail.
	 */
	@Override
	protected boolean isRelevant(EStructuralFeature f) {
		if (f instanceof EReference) {
			ObjectDef sType = section.getType();
			if (sType == null) {
				LogUtil.error("section type was null");
				return false;
			}
			EClass rType = ((EReference)f).getEReferenceType();
			if (rType == null) {
				LogUtil.error("reference type was null");
				return false;
			}
			return CommonUtils.equals(safelyGetURI(sType), safelyGetURI(rType))
				&& CommonUtils.equals(sType.getName(), rType.getName());
		}
		return false;
	}

	@Override
	protected boolean isFixedValueWrapper(Object wrapper) {
		if (wrapper instanceof ReferencedObjectRow
				&& ((ReferencedObjectRow)wrapper).getReference() == VALUE_LOST) {
			return true;
		}
		return super.isFixedValueWrapper(wrapper);
	}

	private String safelyGetURI(EClass type) {
		EPackage ePackage = type.getEPackage();
		if (ePackage == null) {
			LogUtil.errorOnce("ePackage missing for type: " + type.getName());
			return null;
		}
		String nsURI = ePackage.getNsURI();
		if (nsURI == null) {
			LogUtil.errorOnce("nsURI missing for ePackage: " + ePackage.getName());
		}
		return nsURI;
	}

	private static final class ComparatorImpl implements Comparator<Object> {
		@Override
		public int compare(Object o1, Object o2) {
			if (o1 == VALUE_LOST && o1 == o2) {
				return 0;
			} else if (o1 == VALUE_LOST) {
				return 1;
			} else if (o2 == VALUE_LOST) {
				return -1;
			}
			
			EObject e1 = (EObject) o1;
			EObject e2 = (EObject) o2;
			Resource r1 = e1.eResource();
			Resource r2 = e2.eResource();
			if (r1 == r2 && r1 != null) {
				int i1 = r1.getContents().indexOf(e1);
				int i2 = r2.getContents().indexOf(e2);
				if (i1 != -1 && i2 != -1) {
					return i1 - i2;
				}
			}
			return getText(e1).compareTo(getText(e2));
		}

		private String getText(EObject e) {
			IItemLabelProvider lp = EMFUtils.adapt(e, IItemLabelProvider.class);
			if (lp != null) {
				return lp.getText(e);
			}
			String id = EcoreUtil.getID(e);
			return id == null ? e.toString() : id;
		}
	}
	
}
