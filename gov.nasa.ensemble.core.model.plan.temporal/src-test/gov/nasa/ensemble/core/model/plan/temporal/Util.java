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
package gov.nasa.ensemble.core.model.plan.temporal;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.consistency.IConsistencyPropertyTester;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import org.junit.Assert;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jscience.physics.amount.Amount;

public class Util extends Assert {

	public static final Amount<Duration> TEST_DURATION = AmountUtils.toAmount(350*1000, DateUtils.MILLISECONDS);

	public static void check(TemporalMember member, Date start, Amount<Duration> duration, Date end) {
		Date startTime = member.getStartTime();
		Date endTime = member.getEndTime();
		if ((startTime != null) && (endTime != null)) {
			assertFalse("the start time is after the end time", startTime.after(endTime));
		}
		assertEquals("start time wrong", start, startTime);
		assertEquals("start timepoint wrong", start, member.getTimepointDate(Timepoint.START));
		assertEquals("end time wrong", end, endTime);
		assertEquals("end timepoint wrong", end, member.getTimepointDate(Timepoint.END));
		assertEquals("duration wrong", duration, member.getDuration());
		TemporalExtent extent = member.getExtent();
		if ((start == null) && (end == null)) {
			assertEquals("extent wrong", null, extent);
		} else {
			assertEquals("extent start time wrong", start, extent.getStart());
			assertEquals("extent end time wrong", end, extent.getEnd());
			assertEquals("extent duration wrong", duration, extent.getDuration());
		}
	}

	public static List<EPlanElement> createDurativeElements() {
		List<EPlanElement> elements = Util.createTestElements();
		for (EPlanElement element : elements) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setExtent(new TemporalExtent(new Date(), Util.TEST_DURATION));
		}
		return elements;
	}

	public static List<EPlanElement> createTestElements() {
		List<EPlanElement> list = createPlansElements();
		list.add(PlanFactory.eINSTANCE.createEActivityGroup());
		EActivityGroup group = PlanFactory.eINSTANCE.createEActivityGroup();
		group.getMember(TemporalMember.class, true).setUseChildTimes(true);
		list.add(group);
		list.add(PlanFactory.eINSTANCE.createEActivityGroup());
		EActivityGroup activity = PlanFactory.eINSTANCE.createEActivityGroup();
		activity.getMember(TemporalMember.class, true).setUseChildTimes(true);
		list.add(activity);
		return list;
	}

	public static List<EPlanElement> createPlansElements() {
		List<EPlanElement> list = new ArrayList<EPlanElement>();
		list.add(PlanFactory.eINSTANCE.createEPlan());
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		plan.getMember(TemporalMember.class, true).setUseChildTimes(true);
		list.add(plan);
		return list;
	}

	public static ENotificationImpl setStart(TemporalMember member, Date oldStart, Date newStart) {
		return new ENotificationImpl((InternalEObject)member, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__START_TIME, oldStart, newStart);
	}

	public static ENotificationImpl setDuration(TemporalMember member, Amount<Duration> oldDuration, Amount<Duration> newDuration) {
		return new ENotificationImpl((InternalEObject)member, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__DURATION, oldDuration, newDuration);
	}

	public static ENotificationImpl setEnd(TemporalMember member, Date oldEnd, Date newEnd) {
		return new ENotificationImpl((InternalEObject)member, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__END_TIME, oldEnd, newEnd);
	}

	public static ENotificationImpl addChild(EPlanParent parent, EPlanChild child) {
		return new ENotificationImpl((InternalEObject)parent, Notification.ADD, PlanPackage.Literals.EPLAN_PARENT__CHILDREN, null, child);
	}

	public static ENotificationImpl addChildren(EPlanParent parent, List<? extends EPlanChild> children) {
		return new ENotificationImpl((InternalEObject)parent, Notification.ADD_MANY, PlanPackage.Literals.EPLAN_PARENT__CHILDREN, null, children);
	}

	public static ENotificationImpl removeChild(EPlanParent parent, EPlanChild child) {
		return new ENotificationImpl((InternalEObject)parent, Notification.REMOVE, PlanPackage.Literals.EPLAN_PARENT__CHILDREN, child, null);
	}
	
	public static ENotificationImpl removeChildren(EPlanParent parent, List<? extends EPlanChild> children) {
		int [] positions = getPositions(parent.getChildren(), children);
		return new ENotificationImpl((InternalEObject)parent, Notification.REMOVE_MANY, PlanPackage.Literals.EPLAN_PARENT__CHILDREN, children, positions);
	}
	
	/**
	 * Lifted from the removeAll method in NotifyingListImpl. (and simplified)
	 * 
	 * @param <T>
	 * @param data
	 * @param collection
	 * @return
	 */
	public static <T> int[] getPositions(EList<T> data, List<? extends T> collection) {
		int listSize = collection.size();
		int [] positions = new int [listSize];
		int count = 0;
		BasicEList<Object> list = new BasicEList<Object>(collection);
        Object[] objects = list.data();
		for (int i = 0; i < data.size(); ++i) {
			@SuppressWarnings("unchecked") T initialObject = data.get(i);
			T object = initialObject;
			LOOP:
			for (int repeat = 0; repeat < 2; ++repeat) {
				for (int j = listSize; --j >= 0; ) {
					if (object == objects[j]) {
						if (positions.length <= count) {
							int [] oldPositions = positions;
							positions = new int [2 * positions.length];
							System.arraycopy(oldPositions, 0, positions, 0, count);
						}
						positions[count++] = i;
						break LOOP;
					}
				}
				if (object == initialObject) {
					break;
				}
			}
		}
		return positions;
	}
	
	/**
	 * Test the consistency of the element and all of its children
	 * @param element
	 */
	public static final List<IConsistencyPropertyTester> CONSISTENCY_PROPERTY_TESTERS = ClassRegistry.createInstances(IConsistencyPropertyTester.class);
	public static void testConsistency(EPlanElement element) {
		final List<Throwable> failures = new ArrayList<Throwable>();
		new PlanVisitor(true) {
			@Override
			protected void visit(EPlanElement element) {
				try {
					for (IConsistencyPropertyTester tester : CONSISTENCY_PROPERTY_TESTERS) {
						tester.test(element);
					}
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					failures.add(t);
				}
			}
		}.visitAll(element);
		if (!failures.isEmpty()) {
			AssertionError assertionError = new ConsistencyCheckFailure("Failed consistency checking for " + failures.size() + " elements.", failures);
			assertionError.initCause(failures.get(0));
			throw assertionError;
		}
	}
	
	private static final class ConsistencyCheckFailure extends AssertionError {
		private final List<Throwable> failures;

		private ConsistencyCheckFailure(Object detailMessage, List<Throwable> failures) {
			super(detailMessage);
			this.failures = failures;
		}

		@Override
		public void printStackTrace(PrintStream s) {
			for (Throwable throwable : failures) {
				throwable.printStackTrace(s);
			}
		}

		@Override
		public void printStackTrace(PrintWriter w) {
			for (Throwable throwable : failures) {
				throwable.printStackTrace(w);
			}
		}

		@Override
		public String getMessage() {
			StringBuilder builder = new StringBuilder(super.getMessage());
			for (Throwable throwable : failures) {
				builder.append(throwable.getMessage());
			}
			return builder.toString();
		}
	}
	
}
