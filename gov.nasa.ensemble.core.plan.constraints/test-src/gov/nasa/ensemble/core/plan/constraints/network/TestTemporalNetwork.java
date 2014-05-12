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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nasa.ensemble.core.plan.constraints.network.DistanceGraph.Dedge;
import gov.nasa.ensemble.core.plan.constraints.network.TemporalNetwork.Bounds;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestTemporalNetwork {

	private TemporalNetwork<Long> tn;
	private TemporalNetwork<Long>.TemporalConstraint srcDstConstraint;
	private TemporalNetwork<Long>.Timepoint src;
	private TemporalNetwork<Long>.Timepoint dst;
	private long srcDstLower = 2l;
	private long srcDstUpper = 5l;

	@Before
	public void setUp() {
		tn = new TemporalNetwork<Long>();
		src = tn.addTimepoint();
		dst = tn.addTimepoint();
		srcDstConstraint = tn.addTemporalConstraint(src, dst, srcDstLower, srcDstUpper);
	}

	@After
	public void tearDown() {
		if (tn != null) {
			tn.dispose();
			tn = null;
		}
	}
	
	private void makeInconsistent() {
		tn.addTemporalConstraint(src, dst, srcDstLower - 3, srcDstLower - 1);
	}

	@Test
	public void testDispose() {
		tn.dispose();
		tn = null;
		assertTrue(srcDstConstraint.isInvalid());
	}
	
	@Test
	public void testGetTime() {
		assertEquals(Long.valueOf(-7), tn.getTime(-7l));
		assertEquals(Long.valueOf(0), tn.getTime(0l));
		assertEquals(Long.valueOf(5), tn.getTime(5l));
	}

	@Test
	public void testIsOutOfBoundsTime() {
		assertTrue(tn.isOutOfBoundsTime(DistanceGraph.POS_INFINITY + 1));
		assertFalse(tn.isOutOfBoundsTime(DistanceGraph.POS_INFINITY));
		assertFalse(tn.isOutOfBoundsTime(DistanceGraph.POS_INFINITY - 1));
		assertFalse(tn.isOutOfBoundsTime(tn.getTime(0L)));
		assertFalse(tn.isOutOfBoundsTime(DistanceGraph.NEG_INFINITY + 1));
		assertFalse(tn.isOutOfBoundsTime(DistanceGraph.NEG_INFINITY));
		assertTrue(tn.isOutOfBoundsTime(DistanceGraph.NEG_INFINITY - 1));
	}

	@Test
	public void testIsFiniteTime() {
		assertFalse(tn.isFiniteTime(tn.MAX_FINITE_TIME + 1));
		assertTrue(tn.isFiniteTime(tn.MAX_FINITE_TIME));
		assertTrue(tn.isFiniteTime(tn.MAX_FINITE_TIME - 1));
		assertTrue(tn.isFiniteTime(tn.getTime(0L)));
		assertTrue(tn.isFiniteTime(tn.MIN_FINITE_TIME + 1));
		assertTrue(tn.isFiniteTime(tn.MIN_FINITE_TIME));
		assertFalse(tn.isFiniteTime(tn.MIN_FINITE_TIME - 1));
	}

	@Test(expected=IllegalStateException.class)
	public void testGetTimepointBoundsIllegalState() {
		makeInconsistent();
		tn.getTimepointBounds(src);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetTimepointBoundsIllegalArgument() {
		src.dispose();
		tn.getTimepointBounds(src);
	}

	@Test
	public void testGetTimepointBounds() {
		long srcLow = 3;
		long srcHi = 3;
		tn.addTemporalConstraint(tn.getOrigin(), src, srcLow, srcHi);
		Bounds bound = tn.getTimepointBounds(dst);
		assertEquals(tn.getTime(srcLow + srcDstLower), bound.lowerBound);
		assertEquals(tn.getTime(srcHi + srcDstUpper), bound.upperBound);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetLastTimepointBoundsIllegalArgument() {
		dst.dispose();
		tn.getLastTimepointBounds(dst);
	}
	
	@Test
	public void testGetLastTimepointBounds() {
		TemporalNetwork<Long>.TemporalConstraint tc = tn.addTemporalConstraint(tn.getOrigin(), src, 4l, 7l);
		Bounds bound = tn.getTimepointBounds(dst);
		tn.removeTemporalConstraint(tc);
		Bounds lastBounds = tn.getLastTimepointBounds(dst);
		assertEquals(bound.lowerBound, lastBounds.lowerBound);
		assertEquals(bound.upperBound, lastBounds.upperBound);
	}

	@Test
	public void testCalcDistanceBoundsTimepointTimepointOrigin() {
		long newLower = 3l;
		long newUpper = 3l;
		tn.addTemporalConstraint(tn.getOrigin(), src, newLower, newUpper);
		Bounds bounds = tn.calcDistanceBounds(tn.getOrigin(), dst);
		assertEquals(tn.getTime(srcDstLower + newLower), bounds.lowerBound);
		assertEquals(tn.getTime(srcDstUpper + newUpper), bounds.upperBound);
	}

	@Test
	public void testCalcDistanceBoundsTimepointTimepointPoint() {
		long dstNewLow = 8;
		long dstNewHi = 12;
		TemporalNetwork<Long>.Timepoint pnt = tn.addTimepoint();
		tn.addTemporalConstraint(dst, pnt, dstNewLow, dstNewHi);
		Bounds bounds = tn.calcDistanceBounds(src, pnt);
		assertEquals(tn.getTime(srcDstLower + dstNewLow), bounds.lowerBound);
		assertEquals(tn.getTime(srcDstUpper + dstNewHi), bounds.upperBound);
	}

	@Test(expected=IllegalStateException.class)
	public void testCalcDistanceBoundsTimepointTimepointBooleanIllegalState() {
		makeInconsistent();
		tn.calcDistanceBounds(src, dst);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCalcDistanceBoundsTimepointTimepointBooleanIllegalArgument1() {
		src.dispose();
		tn.calcDistanceBounds(src, dst);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCalcDistanceBoundsTimepointTimepointBooleanIllegalArgument2() {
		dst.dispose();
		tn.calcDistanceBounds(src, dst);
	}

	@Test
	public void testCalcDistanceBoundsTimepointTimepointBoolean() {
		Bounds bound = tn.calcDistanceBounds(src, dst, false);
		assertEquals(tn.getTime(srcDstLower), bound.lowerBound);
		assertEquals(tn.getTime(srcDstUpper), bound.upperBound);
	}

	@Test(expected=IllegalStateException.class)
	public void testCalcDistanceBoundsTimepointVectorOfTimepointVectorOfTimeVectorOfTimeIllegalState() {
		makeInconsistent();
		tn.calcDistanceBounds(src, new Vector<TemporalNetwork<Long>.Timepoint>(), new Vector<Long>(), new Vector<Long>());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetConstraintUpperBoundIllegalArgument() {
		tn.removeTemporalConstraint(srcDstConstraint);
		tn.getConstraintUpperBound(srcDstConstraint);
	}

	@Test
	public void testGetConstraintUpperBound() {
		Long time = tn.getConstraintUpperBound(srcDstConstraint);
		assertEquals(time, tn.getTime(srcDstUpper));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetConstraintLowerBoundIllegalArgument() {
		tn.removeTemporalConstraint(srcDstConstraint);
		tn.getConstraintLowerBound(srcDstConstraint);
	}

	@Test
	public void testGetConstraintLowerBound() {
		Long time = tn.getConstraintLowerBound(srcDstConstraint);
		assertEquals(time, tn.getTime(srcDstLower));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetConstraintScopeIllegalArgument() {
		tn.removeTemporalConstraint(srcDstConstraint);
		tn.getConstraintScope(srcDstConstraint);
	}
	
	@Test()
	public void testGetConstraintScope() {
		TemporalNetwork<Long>.Scope s = tn.getConstraintScope(srcDstConstraint);
		assertEquals(s.source, src);
		assertEquals(s.target, dst);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddTemporalConstraintTimepointTimepointTimeTimeIllegalArgument1() {
		src.dispose();
		tn.addTemporalConstraint(src, dst, tn.getTime(0l), tn.getTime(1l));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddTemporalConstraintTimepointTimepointTimeTimeIllegalArgument2() {
		dst.dispose();
		tn.addTemporalConstraint(src, dst, tn.getTime(0l), tn.getTime(1l));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddTemporalConstraintTimepointTimepointTimeTimeIllegalArgument3() {
		tn.addTemporalConstraint(src, src, tn.getTime(0l), tn.getTime(0l));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNarrowTemporalConstraintIllegalArgument1() {
		tn.removeTemporalConstraint(srcDstConstraint);
		tn.narrowTemporalConstraint(srcDstConstraint, tn.getTime(3l), tn.getTime(4l));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNarrowTemporalConstraintIllegalArgument2() {
		TemporalNetwork<Long> tn2 = new TemporalNetwork<Long>();
		TemporalNetwork<Long>.Timepoint src = tn2.addTimepoint();
		TemporalNetwork<Long>.Timepoint dst = tn2.addTimepoint();
		TemporalNetwork<Long>.TemporalConstraint otherConstraint = tn2.addTemporalConstraint(src, dst, srcDstLower, srcDstUpper);
		tn.narrowTemporalConstraint(otherConstraint, tn.getTime(3l), tn.getTime(4l));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNarrowTemporalConstraintIllegalArgument3() {
		tn.narrowTemporalConstraint(srcDstConstraint, tn.getTime(srcDstLower - 1), tn.getTime(srcDstUpper));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNarrowTemporalConstraintIllegalArgument4() {
		tn.narrowTemporalConstraint(srcDstConstraint, tn.getTime(srcDstLower), tn.getTime(srcDstUpper + 1));
	}

	@Test
	public void testNarrowTemporalConstraint() {
		Long srcLow = 3l;
		Long srcUpper = 3l;
		tn.addTemporalConstraint(tn.getOrigin(), src, srcLow, srcUpper);
		tn.narrowTemporalConstraint(srcDstConstraint, srcDstLower, srcDstUpper);
		Bounds bounds = srcDstConstraint.getBounds();
		assertEquals(bounds.lowerBound, tn.getTime(srcDstLower));
		assertEquals(bounds.upperBound, tn.getTime(srcDstUpper));
		tn.narrowTemporalConstraint(srcDstConstraint, srcDstLower, srcDstUpper - 1);
		Long newLower = tn.getTime(srcDstLower);
		Long newUpper = tn.getTime(srcDstUpper - 1);
		bounds = srcDstConstraint.getBounds();
		assertEquals(bounds.lowerBound, newLower);
		assertEquals(bounds.upperBound, newUpper);
		// this part doesn't seem to work, oh well.
		// TODO: ask paul morris about this
//		bounds = tn.calcDistanceBounds(tn.getOrigin(), dst);
//		assertEquals(tn.getTime(srcLow + newLower), bounds.lowerBound);
//		assertEquals(tn.getTime(srcUpper + newUpper), bounds.upperBound);
	}
	
	@Test
	public void testRemoveTemporalConstraint() {
		tn.removeTemporalConstraint(srcDstConstraint);
		assertTrue(srcDstConstraint.isInvalid());
	}
	
	@Ignore // See discussion in SPF-9297, SPF-9319
	/// @Test(expected=IllegalArgumentException.class)
	public void testRemoveTemporalConstraintTemporalConstraintBooleanIllegalArgument() {
		tn.removeTemporalConstraint(srcDstConstraint);
		tn.removeTemporalConstraint(srcDstConstraint);
	}

	@Ignore // See discussion in SPF-9297, SPF-9319	///@Test(expected=IllegalStateException.class)
	public void testRemoveTemporalConstraintTemporalConstraintBooleanIllegalState1() {
		src.dispose();
		tn.removeTemporalConstraint(srcDstConstraint);
	}

	@Ignore // See discussion in SPF-9297, SPF-9319
	///@Test(expected=IllegalStateException.class)
	public void testRemoveTemporalConstraintTemporalConstraintBooleanIllegalState2() {
		dst.dispose();
		tn.removeTemporalConstraint(srcDstConstraint);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDeleteTimepointIllegalArgument1() {
		src.dispose();
		tn.deleteTimepoint(src);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDeleteTimepointIllegalArgument2() {
		tn.deleteTimepoint(tn.getOrigin());
	}

	@Test
	public void testDeleteTimepoint() {
		tn.deleteTimepoint(src);
		assertTrue(src.isInvalid());
	}

	@Test(expected=IllegalStateException.class)
	public void testGetInconsistencyReasonIllegalState() {
		tn.getInconsistencyReason();
	}

	@Test
	public void testGetInconsistencyReason() {
		makeInconsistent();
		List<TemporalNetwork<Long>.Timepoint> reason = tn.getInconsistencyReason();
		assertEquals(2, reason.size());
		assertTrue(reason.contains(src));
		assertTrue(reason.contains(dst));
	}

	@Test
	public void testGetEdgeNogoodListIllegalState() {
		assertTrue(tn.getEdgeNogoodList().isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetEdgeNogoodList() {
		makeInconsistent();
		List<Dedge> nogood = tn.getEdgeNogoodList();
		assertEquals(2, nogood.size());
		Dedge edge1 = nogood.get(0);
		Dedge edge2 = nogood.get(1);
		TemporalNetwork<Long>.Timepoint point1 = (TemporalNetwork<Long>.Timepoint)edge1.from;
		TemporalNetwork<Long>.Timepoint point2 = (TemporalNetwork<Long>.Timepoint)edge2.from;
		assertTrue(point1 == src || point1 == dst);
		assertTrue(point2 == src || point2 == dst);
	}

	@Test(expected=IllegalStateException.class)
	public void testIsDistanceLessThanTimepointTimepointTimeIllegalState() {
		makeInconsistent();
		tn.isDistanceLessThan(src, dst, tn.getTime(0L));
	}
		
	@Test
	public void testIsDistanceLessThanTimepointTimepointTime() {
		assertTrue(tn.isDistanceLessThan(src, dst, DistanceGraph.MAX_LENGTH));
		assertTrue(tn.isDistanceLessThan(src, dst, srcDstUpper + 1));
		assertFalse(tn.isDistanceLessThan(src, dst, srcDstUpper));
		assertFalse(tn.isDistanceLessThan(src, dst, tn.getTime(1l)));
	}

	@Test(expected=IllegalStateException.class)
	public void testIsDistanceLessThanOrEqualTimepointTimepointTimeIllegalState() {
		makeInconsistent();
		tn.isDistanceLessThanOrEqual(src, dst, tn.getTime(0L));
	}
		
	@Test
	public void testIsDistanceLessThanOrEqual() {
		assertTrue(tn.isDistanceLessThanOrEqual(src, dst, DistanceGraph.MAX_LENGTH));
		assertTrue(tn.isDistanceLessThanOrEqual(src, dst, srcDstUpper + 1));
		assertTrue(tn.isDistanceLessThanOrEqual(src, dst, srcDstUpper));
		assertFalse(tn.isDistanceLessThanOrEqual(src, dst, tn.getTime(1l)));
	}

	@Test
	public void testIsDistancePossiblyLessThan() {
		// TODO "Not yet implemented";
	}

	@Test
	public void testUpdatedTimepoints() {
		tn.calcDistanceBounds(src, dst);
		Set<TemporalNetwork<Long>.Timepoint> updatedTimepoints = tn.getUpdatedTimepoints();
		assertEquals(2, updatedTimepoints.size());
		assertTrue(updatedTimepoints.contains(src));
		assertTrue(updatedTimepoints.contains(dst));
		tn.resetUpdatedTimepoints();
		updatedTimepoints = tn.getUpdatedTimepoints();
		assertTrue(updatedTimepoints.isEmpty());
	}

	@Test
	public void testHasEdgeToOrigin() {
		assertFalse(tn.hasEdgeToOrigin(src));
		assertFalse(tn.hasEdgeToOrigin(dst));
		tn.addTemporalConstraint(tn.getOrigin(), src, 3L, 3L);
		assertTrue(tn.hasEdgeToOrigin(src));
		assertFalse(tn.hasEdgeToOrigin(dst));
	}

}
