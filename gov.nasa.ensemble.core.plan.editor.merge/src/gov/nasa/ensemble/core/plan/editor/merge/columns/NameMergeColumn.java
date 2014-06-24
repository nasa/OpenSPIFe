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

import gov.nasa.ensemble.common.ui.type.editor.CocoaCompatibleTextCellEditor;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.contributions.MergeTreePlanContributor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Comparator;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class NameMergeColumn extends AbstractMergeColumn<EObject> {

	private static final EAttribute NAME_ATTRIBUTE = PlanPackage.Literals.EPLAN_ELEMENT__NAME;

	public NameMergeColumn(IMergeColumnProvider provider) {
		super(provider, "Name", 300);
	}

	@Override
	public int getAlignment() {
		return SWT.LEFT;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		return (feature == NAME_ATTRIBUTE) || (feature == PlanPackage.Literals.COMMON_MEMBER__COLOR);
	}
	
	@Override
	public EObject getFacet(Object element) {
		if (element instanceof EObject) {
			return (EObject) element;
		}
		return null;
	}
	
	// facet presentation
	
	@Override
	public Image getImage(EObject element) {
		if (element instanceof EPlanElement) {
			return PlanUtils.getIcon((EPlanElement) element);
		}
		Object image = AdapterUtils.getItemProviderImage(element);
		if (image instanceof Image) {
			return (Image) image;
		}
		return null;
	}

	@Override
	public String getText(EObject element) {
		if (element  instanceof EPlanElement) {
			return ((EPlanElement) element).getName();
		}
		Object value = MergeTreePlanContributor.getInstance().getValueForFeature(element, NAME_ATTRIBUTE);
		if (value instanceof String) {
			return (String) value;
		}
		String text = AdapterUtils.getItemProviderText(element);
		if (text != null) {
			return text;
		}
		return "";
	}

    //
    // support for in-place editing
    //
    
	@Override
	public CellEditor getCellEditor(Composite parent, EObject element) {
		if (element instanceof EPlanElement) {
			return new NameTextCellEditor(parent);
		} 
		return null;
	}

	/**
	 * called when modification of an element in the MergeEditor is attempted
	 * 
	 * Allow modification if it is an Activity/Activity Group and the selected
	 * role has permissions to modify it.
	 */
	@Override
	public boolean canModify(EObject object) {
		if (object instanceof EPlanElement) {
			EPlanElement element = (EPlanElement) object;
			if (!PlanEditApproverRegistry.getInstance().canModify(element)) {
				return false;
			}
			IItemPropertyDescriptor descriptor = EMFUtils.getFeatureDescriptor(element, NAME_ATTRIBUTE);
			if (descriptor == null) {
				return false;
			}
			return descriptor.canSetProperty(element);			
		}
		return false;
	}

	@Override
	public Object getValue(EObject element) {
		if (element instanceof EPlanElement) {
			return ((EPlanElement) element).getName();
		}
		return "";
	}

	@Override
	public void modify(final EObject element, final Object value, final IUndoContext undoContext) {
		if ((element instanceof EActivityGroup) || (element instanceof EActivity)) {
			FeatureTransactionChangeOperation.execute(element, NAME_ATTRIBUTE, value);
		}
	}

	/*
	 * Support sorting by this column
	 */
	
	private static final Comparator<EObject> comparator =
		new Comparator<EObject>() {
			@Override
			public int compare(EObject o1, EObject o2) {
				String name1 = "";
				String name2 = "";
				if (o1 instanceof EPlanElement) {
					name1 = ((EPlanElement) o1).getName();
				}
				if (o2 instanceof EPlanElement) {
					name2 = ((EPlanElement) o2).getName();
				}
				return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
			}
		};
	
	@Override
	public Comparator<EObject> getComparator() {
		return comparator;
	}
	
	private static final class NameTextCellEditor extends CocoaCompatibleTextCellEditor {

		private NameTextCellEditor(Composite parent) {
			super(parent);
		}

		@Override
		protected void doSetValue(Object value) {
			if (value == null) {
				value = "";
			}
			super.doSetValue(value);
		}
		
	}

}
