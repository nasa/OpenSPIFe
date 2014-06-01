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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.operation.OperationJob.IPostJobRunnable;
import gov.nasa.ensemble.core.jscience.TimeOfDayStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.editor.constraints.TemporalBoundEditOperation;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.text.ParseException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Properties;
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

public class TemporalBoundColumn extends AbstractMergeColumn<ConstraintsMember> {

	private static final TimeOfDayStringifier STRINGIFIER = new TimeOfDayStringifier();
	
	public static final Image IMAGE_START_ANYTIME_AFTER = ConstraintsMergePlugin.createIcon("anytime_after.png");
	public static final Image IMAGE_END_ANYTIME_BEFORE = ConstraintsMergePlugin.createIcon("anytime_before.png");
	public static final Image IMAGE_UNKNOWN = ConstraintsMergePlugin.createIcon("unknown.jpg");

	private Timepoint timepoint = null;
	
	public TemporalBoundColumn(IMergeColumnProvider provider, Timepoint timepoint) {
		super(provider, buildHeaderName(timepoint), 130);
		this.timepoint = timepoint;
		String name = null;
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		switch (timepoint) {
		case START:
			name = properties.getProperty("merge.column.Start_TOS.name", "Earliest");
			break;
		case END:
			name = properties.getProperty("merge.column.End_TOS.name", "Latest");
			break;
		}
		if (name != null) {
			setHeaderName(name);
		}
	}

	private static String buildHeaderName(Timepoint timepoint) {
		return timepoint.toString().substring(0,1).toUpperCase() +
		       timepoint.toString().substring(1).toLowerCase() +
		       " TOS";
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
		Set<PeriodicTemporalConstraint> constraints = getTimepointPeriodicTemporalConstraints(facet);
		if (constraints.isEmpty()) {
			return "";
		}
		if (constraints.size() > 1) {
			return "+";
		}
		PeriodicTemporalConstraint constraint = constraints.iterator().next();
		Amount<Duration> latest = constraint.getLatest();
		Amount<Duration> earliest = constraint.getEarliest();
		if ((latest == null) && (earliest == null)) {
			return "null";
		} else if (latest == null) {
			// any time after the earliest
			if (timepoint == Timepoint.START) {
				return STRINGIFIER.getDisplayString(earliest);
			}
		} else if (earliest == null) {
			// any time before the latest
			if (timepoint == Timepoint.END) {
				return STRINGIFIER.getDisplayString(latest);
			}
		} else {
			return "?";
		}
		return "";
	}
	
	@Override
	public String getToolTipText(ConstraintsMember facet) {
		if (facet == null) {
			return null;
		}
		Set<PeriodicTemporalConstraint> constraints = getTimepointPeriodicTemporalConstraints(facet);
		if (constraints.isEmpty()) {
			return "";
		}
		if (constraints.size() > 1) {
			return constraints.size() + " constraints";
		}
		PeriodicTemporalConstraint constraint = constraints.iterator().next();
		Amount<Duration> latest = constraint.getLatest();
		Amount<Duration> earliest = constraint.getEarliest();
		StringBuilder builder = new StringBuilder(timepoint.toString() + " anytime");
		if (earliest != null) {
			// any time after the earliest
			builder.append(" after " + STRINGIFIER.getDisplayString(earliest));
		}
		if (latest != null) {
			if (earliest != null) {
				builder.append(" and");
			}
			// any time before the latest
			builder.append(" before " + STRINGIFIER.getDisplayString(latest));
		}
		return builder.toString();
	}
	
	@Override
	public Image getImage(ConstraintsMember facet) {
		if (facet == null) {
			return null;
		}
		Set<PeriodicTemporalConstraint> constraints = getTimepointPeriodicTemporalConstraints(facet);
		if (constraints.isEmpty()) {
			return null;
		}
		boolean hasEarliest = false;
		boolean hasLatest = false;
		for (PeriodicTemporalConstraint constraint : constraints) {
			Amount<Duration> latest = constraint.getLatest();
			Amount<Duration> earliest = constraint.getEarliest();
			if (latest != null) {
				hasLatest = true;
			}
			if (earliest != null) {
				hasEarliest = true;
			}
			if (hasEarliest && hasLatest) {
				return IMAGE_UNKNOWN;
			}
		}
		if (hasEarliest && (timepoint == Timepoint.START)) {
			return IMAGE_START_ANYTIME_AFTER;
		}
		if (hasLatest && (timepoint == Timepoint.END)) {
			return IMAGE_END_ANYTIME_BEFORE;
		}
		return IMAGE_UNKNOWN;
	}

	//
	// in-place editing support
	//
	
	@Override
	public boolean canModify(ConstraintsMember facet) {
		if (facet != null) {
			EPlanElement element = facet.getPlanElement();
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
		TransactionUtils.writing(facet, new Runnable() {
			@Override
			public void run() {
				modify(facet, text, undoContext);
			}
		});
	}

	@Override
	public Comparator<ConstraintsMember> getComparator() {
		return null; // SPF-1588: not sortable
	}
	
	//
	// utility functions
	//
	
	/* public for testing */
	public void modify(final ConstraintsMember facet, String text, IUndoContext undoContext) {
		EPlanElement planElement = facet.getPlanElement();
		Set<PeriodicTemporalConstraint> oldConstraints = getTimepointPeriodicTemporalConstraints(facet);
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
				Amount<Duration> latest = oldConstraint.getLatest();
				if ((timepoint == Timepoint.START) && (latest == null) && (earliest != null) && (earliest.compareTo(offset) == 0)) {
					return; // same as an existing constraint
				}
				if ((timepoint == Timepoint.END) && (earliest == null) && (latest != null) && (latest.compareTo(offset) == 0)) {
					return; // same as an existing constraint
				}
			}
			newConstraint = createPeriodicTemporalConstraint(planElement, offset.times(MissionConstants.getInstance().getEarthSecondsPerLocalSeconds()));
		}
		TemporalBoundEditOperation operation = new TemporalBoundEditOperation(planElement, oldConstraints, newConstraint);
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
		switch(timepoint) {
		case START:
			point.setEndpoint(Timepoint.START);
			constraint.setEarliest(offset);
			break;
		case END:
			point.setEndpoint(Timepoint.END);
			constraint.setLatest(offset);
			break;
		}
		return constraint;
	}
	
	// Exposed for test purposes only 
	public Set<PeriodicTemporalConstraint> getTimepointPeriodicTemporalConstraints(ConstraintsMember constraintsMember) {
		Set<PeriodicTemporalConstraint> filteredSet = new LinkedHashSet<PeriodicTemporalConstraint>();
		for (PeriodicTemporalConstraint constraint : constraintsMember.getPeriodicTemporalConstraints()) {
			if (timepoint == constraint.getPoint().getEndpoint()) {
				if (!CommonUtils.equals(constraint.getEarliest(), constraint.getLatest())) {
					filteredSet.add(constraint);
				}
			}
		}
		return filteredSet;
	}

	private final class PostConditionCheck extends IPostJobRunnable.Adaptable {
		
		private final ConstraintsMember facet;
		private final PeriodicTemporalConstraint newConstraint;

		private PostConditionCheck(ConstraintsMember facet, PeriodicTemporalConstraint newConstraint) {
			this.facet = facet;
			this.newConstraint = newConstraint;
		}

		@Override
		public void jobSuccessful() {
			// check post condition
			Set<PeriodicTemporalConstraint> newConstraints = getTimepointPeriodicTemporalConstraints(facet);
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
