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
package gov.nasa.ensemble.core.plan.resources.ui.profile.editor;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.treetable.AbstractTreeTableColumn;
import gov.nasa.ensemble.common.ui.type.editor.StringifierCellEditor;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.plan.resources.profile.operations.SwapProfileDataPointOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.jscience.physics.amount.Amount;

/* package */ class ProfileDataPointColumnProvider {

	private static final IStringifier<Date> DATE_STRINGIFIER = new DateStringifier(ProfileUtil.getDateFormat());
	private static final String DATE_HEADER = "Date";
	private static final String VALUE_HEADER = "Value";

	private List<AbstractTreeTableColumn> columns;

	public ProfileDataPointColumnProvider(Profile profile) {
		this(profile, true);
	}
	
	public ProfileDataPointColumnProvider(Profile profile, boolean editable) {
		columns = new ArrayList<AbstractTreeTableColumn>();
		columns.add(new DateColumn(profile, editable));
		columns.add(new ValueColumn(profile, editable));
	}

	public List<AbstractTreeTableColumn> getColumns() {
		return columns;
	}
	
	private static class DateColumn extends ProfileColumn {

		private final boolean editable;
		
		public DateColumn(Profile profile, boolean editable) {
			super(DATE_HEADER, profile);
			this.editable = editable;
		}

		@Override
		public String getText(DataPoint facet) {
			return DATE_STRINGIFIER.getDisplayString(facet.getDate());
		}

		@Override
		public CellEditor getCellEditor(Composite parent, DataPoint facet) {
			return new StringifierCellEditor<Date>(parent, DATE_STRINGIFIER);
		}

		@Override
		public boolean canModify(DataPoint facet) {
			return editable && super.canModify(facet);
		}

		@Override
		public void modify(DataPoint oldDataPoint, Object value, IUndoContext undoContext) {
			if (value instanceof Date) {
				DataPoint newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint((Date) value, oldDataPoint.getValue());
				SwapProfileDataPointOperation op = new SwapProfileDataPointOperation(profile, oldDataPoint, newDataPoint);
				if (undoContext != null) {
					op.addContext(undoContext);
				}
				IOperationHistory history = OperationHistoryFactory.getOperationHistory();
				try {
					history.execute(op, null, null);
				} catch (ExecutionException e) {
					LogUtil.error(e);
				}
			}
		}

		@Override
		public Object getValue(DataPoint facet) {
			return facet.getDate();
		}

		@Override
		public Comparator<DataPoint> getComparator() {
			return DataPoint.DEFAULT_COMPARATOR;
		}
		
	}

	@SuppressWarnings("unchecked")
	private static class ValueColumn extends ProfileColumn {

		private final boolean editable;
		private IStringifier stringifier;
		
		@SuppressWarnings("deprecation") // not an eAttribute
		public ValueColumn(Profile profile, boolean editable) {
			super(VALUE_HEADER, profile);
			EDataType dataType = profile.getDataType();
			if (dataType != null && dataType.getInstanceClass() == Date.class){
				stringifier = DATE_STRINGIFIER;
			} else {
				stringifier = EMFUtils.getStringifier(dataType);
			}
			this.editable = editable;
		}

		@Override
		public String getText(DataPoint facet) {
			EDataType dataType = profile.getDataType();
			Object value = facet.getValue();
			if (value == null) {
				return "";
			}
			if  (dataType.getInstanceClass() == Amount.class || value instanceof Amount) {
				return EnsembleAmountFormat.INSTANCE.formatAmount((Amount) value);
			}
			if (dataType.getInstanceClass() == Date.class) {
				return DATE_STRINGIFIER.getDisplayString((Date) value);
			}
			if (stringifier != null) {
				try {
					return stringifier.getDisplayString(value);
				} catch (Exception e) {
					LogUtil.errorOnce(e.getMessage());
				}
			}
			return value.toString();
		}

		@Override
		public CellEditor getCellEditor(Composite parent, DataPoint facet) {
			if (stringifier != null) {
				return new StringifierCellEditor(parent, stringifier);
			} else {
				return null;
			}
		}

		@Override
		public boolean canModify(DataPoint facet) {
			return editable && super.canModify(facet);
		}

		@Override
		public void modify(DataPoint oldDataPoint, Object value, IUndoContext undoContext) {
			DataPoint newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint(oldDataPoint.getDate(), value);
			SwapProfileDataPointOperation op = new SwapProfileDataPointOperation(profile, oldDataPoint, newDataPoint);
			if (undoContext != null) {
				op.addContext(undoContext);
			}
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			try {
				history.execute(op, null, null);
			} catch (ExecutionException e) {
				LogUtil.error(e);
			}
		}
		
		@Override
		public Object getValue(DataPoint facet) {
			return facet.getValue();
		}
		
	}
	
	private abstract static class ProfileColumn extends AbstractTreeTableColumn<DataPoint> {

		protected Profile profile;
		
		public ProfileColumn(String header, Profile profile) {
			super(header, 140);
			this.profile = profile;
		}
		
		@Override
		public DataPoint getFacet(Object element) {
			return (DataPoint) ((element instanceof DataPoint) ? element : null);
		}
		
		@Override
		public boolean canModify(DataPoint facet) {
			return profile.isExternalCondition();
		}

		@Override
		public boolean needsUpdate(Object feature) { //TODO check what feature is here?
			return false;
		}
	}
}
