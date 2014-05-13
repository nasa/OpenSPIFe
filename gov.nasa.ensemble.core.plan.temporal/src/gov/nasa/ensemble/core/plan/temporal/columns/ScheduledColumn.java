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

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.PlanParameterDescriptor;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterFacet;
import gov.nasa.ensemble.core.plan.parameters.SpifePlanUtils;
import gov.nasa.ensemble.core.plan.temporal.ScheduledOperation;
import gov.nasa.ensemble.core.plan.temporal.TemporalPlugin;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

public class ScheduledColumn extends AbstractMergeColumn<ParameterFacet<Boolean>> {
	
	private static final EAttribute SCHEDULED_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED;
	private static final Logger trace = Logger.getLogger(ScheduledColumn.class);
	private static final int DEFAULT_WIDTH = 35;

	public ScheduledColumn(IMergeColumnProvider provider) {
		super(provider, "Scheduled", DEFAULT_WIDTH);
	}
	
	@Override
	public boolean needsUpdate(Object feature) {
		return (feature == SCHEDULED_FEATURE);
	}

	@Override
	public ParameterFacet<Boolean> getFacet(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement planElement = (EPlanElement)element;
			TemporalMember temporalMember = planElement.getMember(TemporalMember.class);
			Boolean scheduled = temporalMember.getScheduled();
			return new ParameterFacet<Boolean>(temporalMember, SCHEDULED_FEATURE, scheduled);
		}
		return null;
	}

	@Override
	public Image getImage(ParameterFacet<Boolean> facet) {
		if (facet == null) {
			return null;
		}
		TriState scheduled = SpifePlanUtils.getScheduled(facet.getElement());
		switch (scheduled) {
		case FALSE:
			return TemporalPlugin.getDefault().getUnscheduledImage();
		case QUASI:
			return TemporalPlugin.getDefault().getQuasiScheduledImage();
		case TRUE:
			return TemporalPlugin.getDefault().getScheduledImage();
		}
		return null;
	}
	
	/*
	 * in place editing
	 */
	
	@Override
	public CellEditor getCellEditor(Composite parent, ParameterFacet<Boolean> facet) {
		return new CheckboxCellEditor(parent);
	}
	
	/**
	 * check to see if you actually have permission to modify the priority
	 * depending on your role
	 */
	@Override
	public boolean canModify(ParameterFacet<Boolean> facet) {
		if (facet == null) {
			return false;
		}
		return PlanEditApproverRegistry.getInstance().canModify(facet.getElement())
				&& PlanParameterDescriptor.getInstance().isEditable(facet.getElement(), facet.getFeature());
	}
	
	@Override
	public boolean editOnActivate(ParameterFacet<Boolean> facet, IUndoContext context, TreeItem item, int index) {
		EPlanElement element = facet.getElement();
		if ((element instanceof EActivity) || (element instanceof EActivityGroup)) {
			if (PlanEditApproverRegistry.getInstance().canModify(element)) {
				try {
					ScheduledOperation.toggleScheduledness(element);
				} catch (ThreadDeath td) {
					throw td;
			    } catch (Throwable t) {
			    	trace.error("ScheduledColumn.editOnActivate", t);
			    }
			}
		}
		return true;
	}

	@Override
	public Object getValue(ParameterFacet<Boolean> parameter) {
		return parameter.getObject();
	}
	
	@Override
	public void modify(ParameterFacet<Boolean> parameter, Object object, IUndoContext undoContext) {
		// ignore this part
	}
	
	@Override
	public Comparator<ParameterFacet<Boolean>> getComparator() {
		return BooleanParameterComparator.INSTANCE;
	}


}
