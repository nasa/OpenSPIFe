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
package gov.nasa.ensemble.core.plan.temporal.columns;


import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.type.editor.CocoaCompatibleTextCellEditor;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class DateFormatTimepointParameterColumn extends AbstractMergeColumn<TemporalMember> {
	
	private static final int DEFAULT_WIDTH = 115;
	private IStringifier<Date> dateStringifier = null;
	private DateFormat dateFormat = null;
	private final Timepoint timepoint;

	public DateFormatTimepointParameterColumn(IMergeColumnProvider provider, Timepoint timepoint) {
		super(provider, createHeaderName(Date.class, timepoint), DEFAULT_WIDTH);
		this.dateStringifier = StringifierRegistry.getStringifier(Date.class);
		this.timepoint = timepoint;
	}
	
	public DateFormatTimepointParameterColumn(IMergeColumnProvider provider, DateFormat format, Timepoint timepoint) {
		super(provider, createHeaderName(format.getClass(), timepoint), DEFAULT_WIDTH);
		this.dateFormat = format;
		this.timepoint = timepoint;
	}
	
	private static String createHeaderName(Class clazz, Timepoint timepoint) {
		return timepoint.toString() + " {" + clazz.getSimpleName() + "}";
	}
	
	@Override
	public TemporalMember getFacet(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement planElement = (EPlanElement) element;
			return planElement.getMember(TemporalMember.class);
		}
		return null;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		return (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION
			|| feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME);
	}
	
	@Override
	public String getText(TemporalMember facet) {
		if (facet == null) {
			return "_";
		}
		TemporalExtent extent = facet.getExtent();
		if (extent == null) {
			return "_";
		}
		Date date = extent.getTimepointDate(timepoint);
		return format(date);
	}
	
	@Override
	public CellEditor getCellEditor(Composite parent, TemporalMember facet) {
		return new CocoaCompatibleTextCellEditor(parent);
	}

	@Override
	public boolean canModify(TemporalMember facet) {
		return (facet != null);
	}
	
	@Override
	public Date getValue(TemporalMember facet) {
		TemporalExtent extent = facet.getExtent();
		if (extent == null) {
			return null;
		}
		return extent.getTimepointDate(timepoint);
	}
	
	@Override
	public void modify(TemporalMember facet, Object string, IUndoContext undoContext) {
		// ignore 
	}
	
	private String format(Date date) {
		if (dateFormat != null) {
			return dateFormat.format(date);
		} else {
			return dateStringifier.getDisplayString(date);
		}
	}

	@Override
	public Comparator<TemporalMember> getComparator() {
		return new Comparator<TemporalMember>() {
			
			@Override
			public int compare(TemporalMember facet1, TemporalMember facet2) {
				if (facet1 == facet2) return 0;
				if (facet1==null) return +1;
				if (facet2==null) return -1;
				Date date1 = getValue(facet1); 
				Date date2 = getValue(facet2);
				if (date1 == date2) return 0;
				if (date1==null) return +1;
				if (date2==null) return -1;
				if (date1.before(date2)) return +1;
				if (date1.after(date2))  return -1;
				return 0;
			}
		};
	}
	
}
