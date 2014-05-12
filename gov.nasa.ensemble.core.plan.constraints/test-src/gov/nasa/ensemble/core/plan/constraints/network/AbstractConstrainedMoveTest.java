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
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.common.data.test.SharedTestData;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Duration;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.jscience.physics.amount.Amount;

public abstract class AbstractConstrainedMoveTest extends TestCase {

	protected boolean DEBUG = false;

	// testing strategy
	static final long INITIAL_STEP_SIZE_IN_MILLISECONDS = 555;
	long nextStepSizeInMilliseconds = INITIAL_STEP_SIZE_IN_MILLISECONDS;
	float stepSizeIncreaseFactor = (float) 1.15;
	
	
	private EPlan plan;
	protected final Set<EPlanChild> allChildrenInPlan = new LinkedHashSet<EPlanChild>();
	private final Set<TemporalConstraint> allConstraints = new LinkedHashSet<TemporalConstraint>();
	private Set<TemporalChain> allChains;
	private final Map<EPlanChild, Amount<Duration>> initialOffsets = new LinkedHashMap<EPlanChild, Amount<Duration>>();
	protected TemporalExtentsCache initialExtents;
	protected SpifePlanModifier modifier;
	protected Date leftEdge;
	protected Date rightEdge;
	protected final Amount<Duration> EPSILON = TemporalConstraintImpl.TOLERANCE;
	private static IStringifier<Date> stringifier = StringifierRegistry.getStringifier(Date.class);


	protected void loadPlan(String relativePath) throws IOException {
		// loaded from a URI
		URI uri = URI.createURI(SharedTestData.findTestData(ConstraintsPlugin.PLUGIN_ID, relativePath).toExternalForm());
		Resource resource = new PlanResourceImpl(uri);
		resource.load(Collections.EMPTY_MAP);
		plan = EMFUtils.getLoadedContent(resource);
		
//		// Alternative to file reading, for reference:  in-memory creation:
//		PlanFactory planFactory = PlanFactory.getInstance();
//		plan = planFactory.createPlan(TestConstrainedMove.class.getCanonicalName());
//		activity = planFactory.createActivityInstance();
		
		// TransactionUtils.writing not needed?
		initialExtents = new TemporalExtentsCache(plan);
		PlanVisitor visitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EPlanChild) {
					EPlanChild child = (EPlanChild) element;
					allChildrenInPlan.add(child);
					allConstraints.addAll(ConstraintUtils.getBinaryConstraints(element, false));
					allConstraints.addAll(ConstraintUtils.getPeriodicConstraints(element, false));
					Date originalChild = initialExtents.getStart(child);
					Date originalParent = initialExtents.getStart(child.getParent());
					Amount<Duration> offset = DateUtils.subtract(originalChild, originalParent);
					initialOffsets.put(child, offset);
				}
			}
		};
		visitor.visitAll(plan);
		allChains = TemporalChainUtils.getChains(plan);
		// Creation of the plan modifier must wait until the plan is completely set up
		// because the temporal network will not follow subsequent changes, because
		// we are not in transaction editing domain.
		modifier = new SpifePlanModifier();
		modifier.initialize(plan);
		leftEdge = getStart(plan);
		rightEdge = getEnd(plan);
	}

	/**
	 * Use this method to ensure sanity.
	 */
	@SuppressWarnings("unused")
	private void verifyReset() {
		IPlanConstraintInfo info = modifier.getPlanConstraintInfo();
		for (EPlanElement element : allChildrenInPlan) {
			TemporalExtent initial = initialExtents.get(element);
			assertEquals(element.getName() + " failed to reset start", initial.getStart(), getStart(element));
			assertEquals(element.getName() + " failed to reset end", initial.getEnd(), getEnd(element));
			StringBuilder builder = new StringBuilder(element.getName());
			ConsistencyBounds bounds = info.getBounds(element);
			builder.append(' ');
			builder.append(printDate(bounds.getEarliestStart()));
			builder.append('-');
			builder.append(printDate(bounds.getLatestStart()));
			builder.append(' ');
			builder.append(printDate(bounds.getEarliestEnd()));
			builder.append('-');
			builder.append(printDate(bounds.getLatestEnd()));
			builder.append(' ');
			builder.append(printDate(initial.getStart()));
			builder.append('-');
			builder.append(printDate(initial.getEnd()));
			builder.append(' ');
			ConsistencyProperties properties = info.getConstraintProperties(element);
			Set<ConsistencyConstraint> constraints = properties.getConstraints();
			for (ConsistencyConstraint constraint : constraints) {
				EPlanElement ae = constraint.affectedElement;
				Timepoint at = constraint.affectedTimepoint;
				Amount<Duration> max = constraint.maximumDistance;
				Amount<Duration> min = constraint.minimumDistance;
				builder.append(at);
				builder.append(" of ");
				builder.append(ae.getName());
				builder.append(' ');
				builder.append(min);
				builder.append('-');
				builder.append(max);
				builder.append(", ");
			}
			System.out.println(builder.toString());
		}
		assertNoConstraintsViolated();
	}

	protected void assertSuccess(final List<? extends EPlanElement> userElements, final Amount<Duration> commandedDelta) {
		List<EPlanElement> selectedElements = new ArrayList<EPlanElement>(EPlanUtils.computeContainedElements(userElements));
		try {
			final List<Date> selectedStarts = new ArrayList<Date>();
			for (EPlanElement element : selectedElements) {
				selectedStarts.add(getStart(element));
			}
			assertNoConstraintsViolated();
			for (EPlanChild element : allChildrenInPlan) {
				if (selectedElements.contains(element)) {
					assertSelectedActivityChangedAsExpected(commandedDelta, element);
				} else {
					assertUnselectedActivityChangedAsExpected(selectedElements, selectedStarts, element);
				}
			}
		} catch (AssertionFailedError error) {
			String message = error.getMessage();
			message += "\n while moving " + PlanUtils.getNameListString(selectedElements) 
					+ " by " + commandedDelta;
			AssertionFailedError newError = new AssertionFailedError(message);
			newError.setStackTrace(error.getStackTrace());
			throw newError;
		}
	}

	/**
	 * Success Criterion #1: No constraints violated.
	 */
	protected void assertNoConstraintsViolated() {
		for (TemporalConstraint constraint : allConstraints) {
			if (constraint.isViolated()) {
				printPlan();
				fail("A constraint is violated: " + constraintDescription(constraint));
			}
		}
	}

	/**
	 * Success Criterion #2: Each selected activity moves to either where you tell it to, or as far as it can
	 * 
	 * @param commandedDelta
	 * @param element
	 */
	protected void assertSelectedActivityChangedAsExpected(
			final Amount<Duration> commandedDelta, EPlanChild element) {
		TemporalExtent initialExtent = initialExtents.get(element);
		Date originalStartTime = initialExtent.getStart();
		Date desiredStartTime = DateUtils.add(originalStartTime, commandedDelta);
		if (canBeMovedCloserTo(element, desiredStartTime, null, null)) {
			// okay if it refuses to go beyond edge of plan
			Date actualStartTime = getStart(element);
			if (!leftEdge.before(actualStartTime)) {
				Date actualEndTime = getEnd(element);
				if (!rightEdge.after(actualEndTime)) {
					printFailureInformation(element);
					fail("Selected element " + element.getName() 
							+ " could have moved closer to " + printDate(desiredStartTime)
							+ " but stopped at " + printDate(actualStartTime));
				}
			}
		}
	}

	/**
	 * Success Criterion #3: Each unselected activity only moves as much as it needs to
	 *
	 * @param selectedElements
	 * @param selectedStarts
	 * @param element
	 */
	protected void assertUnselectedActivityChangedAsExpected(
			final List<? extends EPlanElement> selectedElements,
			final List<Date> selectedStarts, EPlanChild element) {
		Date desiredStartTime = getDesiredStartTime(element);
		if (canBeMovedCloserTo(element, desiredStartTime, selectedElements, selectedStarts)) {
			printFailureInformation(element);
			Date actualStartTime = getStart(element);
			String message = "While moving " + PlanUtils.getNameListString(selectedElements)
				+ " to " + printDates(selectedStarts)
				+ ", unselected element " + element.getName()
				+ " could have stayed closer to " + printDate(desiredStartTime)
				+ " but moved to " + printDate(actualStartTime);
			fail(message);
		}
	}

	/**
	 * When freezing hard:
	 * The desired start time of a child of the plan is its original start.
	 * The desired start time of any other child is its parent's
	 * current start time plus the original offset of that child.
	 * 
	 * Otherwise the desired start time of a child is its original start.
	 * 
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unused") // no freezing means desired position is initial time
	private Date getDesiredStartTime(EPlanChild element) {
		EPlanElement parent = element.getParent();
		if (true) { // (parent instanceof EPlan) {
			TemporalExtent initialExtent = initialExtents.get(element);
			Date desiredStartTime = initialExtent.getStart();
			return desiredStartTime;
		}
		// An activity should try to stay as close as possible 
		// to its relative position from its parent
		Amount<Duration> offset = initialOffsets.get(element);
		Date newParent = parent.getMember(TemporalMember.class).getStartTime();
		Date desiredStartTime = DateUtils.add(newParent, offset);
		return desiredStartTime;
	}

	private String constraintDescription(TemporalConstraint constraint) {
		String description = constraint.toString();
		if (constraint instanceof BinaryTemporalConstraint) {
			BinaryTemporalConstraint bc = (BinaryTemporalConstraint) constraint;
			description =
				bc.getPointA().getElement().getName() + " " +
				bc.getPointA().getEndpoint() + " " +
				ConstraintUtils.getDescription(bc, true)
				+ " " + bc.getPointB().getElement().getName()
				+ " " + bc.getPointB().getEndpoint();
		}
		return description;
	}

	/**
	 * (false if cannot be moved even 1ms closer to desired position without violating a constraint.)
	 * @param element
	 * @param desiredStartTime
	 * @param selectedElements
	 * @param selectedStarts
	 * @return
	 */
	protected boolean canBeMovedCloserTo(EPlanElement element, Date desiredStartTime, List<? extends EPlanElement> selectedElements,
			List<Date> selectedStarts) {
		TemporalMember time = element.getMember(TemporalMember.class);
		Date actualStartTime = time.getStartTime();
		if (actualStartTime.equals(desiredStartTime)) {
			return false;
		}
		try {
			Date trialTime = infinitesimallyCloser(actualStartTime, desiredStartTime);
			time.setStartTime(trialTime);
			if (selectedElements != null) {
				// check to see if one of the selected elements was moved as a consequence of moving this element
				for (int i = 0 ; i < selectedElements.size() ; i++) {
					EPlanElement selectedElement = selectedElements.get(0);
					Date selectedStart = selectedStarts.get(0);
					if (!getStart(selectedElement).equals(selectedStart)) {
						return false;
					}
				}
			}
			boolean result = !planHasViolation(element);
			return result;
		} finally {
			time.setStartTime(actualStartTime);
		}
	}

	/**
	 * returns true if there is a violation
	 * comprehensive search
	 * 
	 * @param element
	 * @return
	 */
	private boolean planHasViolation(EPlanElement element) {
		if (!parentExtentMatchesChildren(element)) {
			return true;
		}
		for (TemporalConstraint constraint : allConstraints) {
			if (constraint.isViolated()) {
				return true;
			}
		}
		for (TemporalChain chain : allChains) {
			Date lastEnd = new Date(-Long.MIN_VALUE);
			for (EPlanElement chainElement : chain.getElements()) {
				TemporalExtent extent = chainElement.getMember(TemporalMember.class).getExtent();
				if (lastEnd.before(extent.getStart())) {
					return true;
				}
				lastEnd = extent.getEnd();
			}
		}
		return false;
	}

	/**
	 * This checks to see if the bounds of the parent match
	 * the earliest and latest children.  It will return
	 * true if both of these are true:
	 * 1. the earliest start time of a child is the start time of the parent
	 * 2. the latest end time of a child is the end time of the parent
	 * 
	 * @param parent
	 * @return
	 */
	private boolean parentExtentMatchesChildren(EPlanElement parent) {
		Date start = null;
		Date end = null;
		for (EPlanChild child : parent.getChildren()) {
			TemporalMember childMember = child.getMember(TemporalMember.class, true);
			Date childStart = childMember.getStartTime();
			if ((childStart != null) && ((start == null) || childStart.before(start))) {
				start = childStart;
			}
			Date childEnd = childMember.getEndTime();
			if ((childEnd != null) && ((end == null) || childEnd.after(end))) {
				end = childEnd;
			}
		}
		TemporalMember parentMember = parent.getMember(TemporalMember.class);
		if ((start != null) && !parentMember.getStartTime().equals(start)) {
			return false;
		}
		if ((end != null) && !parentMember.getEndTime().equals(end)) {
			return false;
		}
		return true;
	}

	private static String printDate(Date date) {
		if (date == null) {
			return "<null>";
		}
		return stringifier.getDisplayString(date) + " (" + date.getTime() + ")";
	}

	private Date infinitesimallyCloser(Date actualStartTime, Date desiredStartTime) {
		if (desiredStartTime.before(actualStartTime)) {
			return DateUtils.subtract(actualStartTime, EPSILON);
		} else if (desiredStartTime.after(actualStartTime)) {
			return DateUtils.add(actualStartTime, EPSILON);
		} else {
			return actualStartTime;
		}
	}

	protected void printPlan() {
		for (EPlanElement element : allChildrenInPlan) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0 ; i < element.getDepth() ; i++) {
				builder.append(' ');
			}
			builder.append(printDate(getStart(element)));
			builder.append(" - ");
			builder.append(printDate(getEnd(element)));
			builder.append(" ");
			builder.append(element.getName());
			System.out.println(builder.toString());
		}
	}

	private void printFailureInformation(EPlanElement element) {
		printPlan();
		EPlanElement climb = element;
		Set<TemporalConstraint> elementConstraints = new LinkedHashSet<TemporalConstraint>();
		while (climb instanceof EPlanChild) {
			elementConstraints.addAll(ConstraintUtils.getBinaryConstraints(climb, false));
			elementConstraints.addAll(ConstraintUtils.getPeriodicConstraints(climb, false));
			climb = ((EPlanChild)climb).getParent();
		}
		for (TemporalConstraint constraint : elementConstraints) {
			System.out.println(constraintDescription(constraint));
		}
	}

	protected void initStepSizeToTest() {
		nextStepSizeInMilliseconds = INITIAL_STEP_SIZE_IN_MILLISECONDS;
	}

	protected long getNextStepSizeToTest() {
		return nextStepSizeInMilliseconds *= stepSizeIncreaseFactor;
	}

	private static String printDates(List<Date> selectedStarts) {
		StringBuilder build = new StringBuilder();
		Iterator<Date> iter = selectedStarts.iterator();
		while (iter.hasNext()) {
			Date date = iter.next();
			build.append(printDate(date));
			if (iter.hasNext()) {
				build.append(' ');
			}
		}
		return build.toString();
	}

	private static Date getStart(EPlanElement element) {
		return element.getMember(TemporalMember.class).getStartTime();
	}

	private static Date getEnd(EPlanElement element) {
		return element.getMember(TemporalMember.class).getEndTime();
	}

	public AbstractConstrainedMoveTest() {
		super();
	}

	public AbstractConstrainedMoveTest(String name) {
		super(name);
	}

}
