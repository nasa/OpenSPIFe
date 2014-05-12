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
package gov.nasa.ensemble.core.plan.editor.merge.constraints;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.operation.OperationJob.IPostJobRunnable;
import gov.nasa.ensemble.core.jscience.TimeOfDayStringifier;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.text.ParseException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.jscience.physics.amount.Amount;

public class PinMergeColumn extends AbstractMergeColumn<ConstraintsMember> {

	private static final TimeOfDayStringifier STRINGIFIER = new TimeOfDayStringifier();
	
	public static final Image IMAGE_PINNED = ConstraintsMergePlugin.createIcon("at_exactly.png");
	
	public PinMergeColumn(IMergeColumnProvider provider) {
		super(provider, "Pin", 130);
	}
	
	@Override
	public int getAlignment() {
		return SWT.LEFT;
	}
	
	@Override
	public boolean needsUpdate(Object feature) {
		return (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS);
	}
	
	@Override
	public ConstraintsMember getFacet(Object object) {
		if (object instanceof EPlanElement) {
			EPlanElement element = ((EPlanElement)object);
			ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
			return constraintsMember;
		}
		return null;
	}

	//
	// facet presentation
	//

	@Override
	public String getText(ConstraintsMember facet) {
		if (facet == null) {
			return "";
		}
		Set<PeriodicTemporalConstraint> constraints = getPinConstraints(facet);
		if (constraints.isEmpty()) {
			return "";
		}
		PeriodicTemporalConstraint constraint = constraints.iterator().next();
		Amount<Duration> offset = constraint.getLatest();
		return STRINGIFIER.getDisplayString(offset);
	}

	@Override
	public String getToolTipText(ConstraintsMember facet) {
		if (facet == null) {
			return "";
		}
		Set<PeriodicTemporalConstraint> constraints = getPinConstraints(facet);
		if (constraints.isEmpty()) {
			return "";
		}
		PeriodicTemporalConstraint constraint = constraints.iterator().next();
		Amount<Duration> offset = constraint.getLatest();
		return "Pinned at " + STRINGIFIER.getDisplayString(offset); 
	}
	
	@Override
	public Image getImage(ConstraintsMember facet) {
		if (facet == null) {
			return null;
		}
		Set<PeriodicTemporalConstraint> constraints = getPinConstraints(facet);
		if (constraints.isEmpty()) {
			return null;
		}
		return IMAGE_PINNED;
	}

	//
	// in-place editing support
	//
	
	@Override
	public boolean canModify(ConstraintsMember facet) {
		if (facet != null) {
			EPlanElement element = facet.getPlanElement();
			if ((element instanceof EActivityGroup) && !ConstraintsPlugin.ALLOW_PULL_GROUP_CONSTRAINTS) {
				return false;
			}
			return PlanEditApproverRegistry.getInstance().canModify(element);
		}
		return false;
	}

	@Override
	public Object getValue(ConstraintsMember facet) {
		String text = getText(facet);
		return text == null ? "" : text; 
	}

	@Override
	public CellEditor getCellEditor(Composite parent, ConstraintsMember facet) {
		return new TimeOfDayCellEditor(parent);
	}

	@Override
	public void modify(final ConstraintsMember facet, Object value, final IUndoContext undoContext) {
		final String text = (String) value;
		modify(facet, text, undoContext);
	}

	@Override
	public Comparator<ConstraintsMember> getComparator() {
		return null; // SPF-1588: not sortable
	}

	//
	// utility functions
	//
	
	private void modify(final ConstraintsMember facet, String text, IUndoContext undoContext) {
		EPlanElement planElement = facet.getPlanElement();
		Set<PeriodicTemporalConstraint> oldConstraints = getPinConstraints(facet);
		final PeriodicTemporalConstraint newConstraint;
		if ((text == null) || (text.trim().length() <= 0)) {
			newConstraint = null;
		} else {
			Amount<Duration> offset;
			try {
				offset = STRINGIFIER.getJavaObject(text, null);
			} catch (ParseException e) {
				return; // warn user but don't log
			} catch (Exception e) {
				LogUtil.error("modify:stringifier", e);
				return;
			}
			for (PeriodicTemporalConstraint oldConstraint : oldConstraints) {
				Amount<Duration> earliest = oldConstraint.getEarliest();
				if (earliest.compareTo(offset) == 0) {
					return; // same as an existing pin
				}
			}			
			newConstraint = createPeriodicTemporalConstraint(planElement, offset.times(MissionConstants.getInstance().getEarthSecondsPerLocalSeconds()));
		}
		ChangePinOperation operation = new ChangePinOperation(planElement, oldConstraints, newConstraint);
		if (operation.canExecute()) {
			operation.addContext(undoContext);
			try {
		        IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		    	history.execute(operation, null, new PostConditionCheck(facet, newConstraint));
		    } catch (Exception e) {
		    	LogUtil.error("execute", e);
		    }
		} else {
			operation.dispose();
		}
	}
	
	private PeriodicTemporalConstraint createPeriodicTemporalConstraint(EPlanElement planElement, Amount<Duration> offset) {
		PeriodicTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		ConstraintPoint point = constraint.getPoint();
		point.setElement(planElement);
		point.setEndpoint(Timepoint.START);
		constraint.setEarliest(offset);
		constraint.setLatest(offset);
		return constraint;
	}
	
	private Set<PeriodicTemporalConstraint> getPinConstraints(ConstraintsMember facet) {
		Set<PeriodicTemporalConstraint> result = new LinkedHashSet<PeriodicTemporalConstraint>();
		for (PeriodicTemporalConstraint constraint : facet.getPeriodicTemporalConstraints()) {
			Amount<Duration> earliest = constraint.getEarliest();
			Amount<Duration> latest = constraint.getLatest();
			if ((earliest != null) && earliest.equals(latest)) {
				result.add(constraint);
			}
		}
		return result;
	}

	private final class PostConditionCheck extends IPostJobRunnable.Adaptable {
		
		private final ConstraintsMember facet;
		private final PeriodicTemporalConstraint newConstraint;

		private PostConditionCheck(ConstraintsMember facet,
				PeriodicTemporalConstraint newConstraint) {
			this.facet = facet;
			this.newConstraint = newConstraint;
		}

		@Override
		public void jobSuccessful() {
			// check post condition
		    Set<PeriodicTemporalConstraint> newConstraints = getPinConstraints(facet);
		    if (newConstraint != null) {
		    	if (newConstraints.size() != 1) {
		    		LogUtil.warn("expected one constraint after constraint operation");
		    	} else if (newConstraints.iterator().next() != newConstraint) {
		    		LogUtil.warn("didn't find the expected constraint after constraint operation");
		    	}
		    } else {
		    	 if (!newConstraints.isEmpty()) {
		    		 LogUtil.warn("expected no constraints to exist after constraint operation");
		    	 }
		    }
		}
	}

}
