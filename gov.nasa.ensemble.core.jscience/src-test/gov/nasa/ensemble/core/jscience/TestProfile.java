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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.measure.unit.Unit;

import org.junit.Assert;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.jscience.physics.amount.Amount;
import org.junit.Test;

public class TestProfile extends Assert {
	protected static final JScienceFactory JSCIENCE_FACTORY = JScienceFactory.eINSTANCE;

	@Test
	public void testStepProfile() {
		doProfileStepInterpolationTest(EcorePackage.Literals.ELONG_OBJECT, Long.MAX_VALUE, new Long(0), Long.MIN_VALUE);
		doProfileStepInterpolationTest(EcorePackage.Literals.EDOUBLE_OBJECT, new Double(-1), new Double(0), new Double(5), Double.MAX_VALUE, Double.POSITIVE_INFINITY, new Double(0));
		doProfileStepInterpolationTest(EcorePackage.Literals.ELONG_OBJECT, Long.MAX_VALUE, new Long(0), Long.MIN_VALUE);
		doProfileStepInterpolationTest(EcorePackage.Literals.ESTRING, "start", "mid", "end");
		Amount longAmountMax = AmountUtils.toAmount(Long.MAX_VALUE, Unit.ONE);
		Amount longAmountZero = AmountUtils.exactZero(Unit.ONE);
		Amount longAmountMin = AmountUtils.toAmount(Long.MIN_VALUE, Unit.ONE);
		doProfileStepInterpolationTest(JSciencePackage.Literals.EAMOUNT, longAmountMax, longAmountZero, longAmountMin);
		Amount doubleAmountMax = AmountUtils.toAmount(Double.MAX_VALUE, Unit.ONE);
		Amount doubleAmountZero = AmountUtils.inexactZero(Unit.ONE);
		Amount doubleAmountMin = AmountUtils.toAmount(Double.MIN_VALUE, Unit.ONE);
		doProfileStepInterpolationTest(JSciencePackage.Literals.EAMOUNT, doubleAmountMax, doubleAmountZero, doubleAmountMin);
	}
	
	@Test
	public void testBooleanProfiles() {
		Date[] ticks = new Date[14];
		for (int i = 0; i < ticks.length; ++i)
			ticks[i] = getTestTimeTick(i);
		
		Profile a = createBooleanProfile(0, "");
		assertEquals(0, a.getDataPoints().size());
		
		Profile b = createBooleanProfile(0, "_");
		assertEquals(1, b.getDataPoints().size());
		assertEquals(ticks[0], ((DataPoint) b.getDataPoints().get(0)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) b.getDataPoints().get(0)).getValue());
		
		Profile c = createBooleanProfile(0, "=");
		assertEquals(2, c.getDataPoints().size());
		assertEquals(ticks[0], ((DataPoint) c.getDataPoints().get(0)).getDate());
		assertEquals(Boolean.TRUE, ((DataPoint) c.getDataPoints().get(0)).getValue());
		assertEquals(ticks[1], ((DataPoint) c.getDataPoints().get(1)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) c.getDataPoints().get(1)).getValue());
		
		Profile d = createBooleanProfile(0, "_=");
		assertEquals(3, d.getDataPoints().size());
		assertEquals(ticks[0], ((DataPoint) d.getDataPoints().get(0)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) d.getDataPoints().get(0)).getValue());
		assertEquals(ticks[1], ((DataPoint) d.getDataPoints().get(1)).getDate());
		assertEquals(Boolean.TRUE, ((DataPoint) d.getDataPoints().get(1)).getValue());
		assertEquals(ticks[2], ((DataPoint) d.getDataPoints().get(2)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) d.getDataPoints().get(2)).getValue());
		
		Profile e = createBooleanProfile(0, " =");
		assertEquals(2, e.getDataPoints().size());
		assertEquals(ticks[1], ((DataPoint) e.getDataPoints().get(0)).getDate());
		assertEquals(Boolean.TRUE, ((DataPoint) e.getDataPoints().get(0)).getValue());
		assertEquals(ticks[2], ((DataPoint) e.getDataPoints().get(1)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) e.getDataPoints().get(1)).getValue());
		
		Profile f = createBooleanProfile(0, " _");
		assertEquals(1, f.getDataPoints().size());
		assertEquals(ticks[1], ((DataPoint) f.getDataPoints().get(0)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) f.getDataPoints().get(0)).getValue());
		
		Profile g = createBooleanProfile(0, "  ==__==__==");
		assertEquals(6, g.getDataPoints().size());
		assertEquals(ticks[2], ((DataPoint) g.getDataPoints().get(0)).getDate());
		assertEquals(Boolean.TRUE, ((DataPoint) g.getDataPoints().get(0)).getValue());
		assertEquals(ticks[4], ((DataPoint) g.getDataPoints().get(1)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) g.getDataPoints().get(1)).getValue());
		assertEquals(ticks[6], ((DataPoint) g.getDataPoints().get(2)).getDate());
		assertEquals(Boolean.TRUE, ((DataPoint) g.getDataPoints().get(2)).getValue());
		assertEquals(ticks[8], ((DataPoint) g.getDataPoints().get(3)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) g.getDataPoints().get(3)).getValue());
		assertEquals(ticks[10], ((DataPoint) g.getDataPoints().get(4)).getDate());
		assertEquals(Boolean.TRUE, ((DataPoint) g.getDataPoints().get(4)).getValue());
		assertEquals(ticks[12], ((DataPoint) g.getDataPoints().get(5)).getDate());
		assertEquals(Boolean.FALSE, ((DataPoint) g.getDataPoints().get(5)).getValue());
	}

	@Test
	public void testBooleanMerge() {
		// for a merge we need
		// a) existing boolean profile data
		// b) new boolean profile data
		// c) A timeslice from which to exercise the new data on the old
		
		// 3 slots before, 5 slots inside, 3 slots after
		// ---|-----|---
		testCoverageMerge(3, 8, "==_==_==_==", "   =_=_=   ", "==_=_=_=_==");
		testCoverageMerge(3, 8, "=====__====", " ====__=== ", " ====__=== ");
		testCoverageMerge(3, 8, "===_____===", "   ==__=   ", "   ==__=   ");
		testCoverageMerge(3, 8, "   ==__=   ", "===_____===", "===_____===");
		testCoverageMerge(3, 8, "==_______==", "  ===_===  ", "  ===_===  ");
		testCoverageMerge(3, 8, "==_______==", " ====_==== ", " ====_==== ");
		// test clipping:
		testCoverageMerge(3, 8, "====___====", "     =     ", "===__=__===");
	}
	
	private void testCoverageMerge(int integrationStartTick, int integrationEndTick, String existing, String replacing, String expectedResult) {
		Profile existingProfile = createBooleanProfile(0, existing);
		Profile replacingProfile = createBooleanProfile(0, replacing);
		Profile expectedResultProfile = createBooleanProfile(0, expectedResult);
		Date integrationStart = getTestTimeTick(integrationStartTick);
		Date integrationEnd = getTestTimeTick(integrationEndTick);
		List<DataPoint> coverage = ProfileUtil.getCoverageMerge(integrationStart, integrationEnd, replacingProfile.getDataPoints(), existingProfile.getDataPoints());
		Profile resultProfile = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT);
		resultProfile.setDataPoints(coverage);
		if (!resultProfile.getDataPoints().equals(expectedResultProfile.getDataPoints())) {
			System.out.println("Expected: " + renderBooleanProfile(expectedResultProfile, 15));
			System.out.println("Actual:   " + renderBooleanProfile(resultProfile, 15));
			fail("Merged coverage is not what was expected");
		}
		
	}
	
	@Test
	public void testStripRedundantMidpoints() {
		List<DataPoint<Integer>> a = createDataPointList(0, 1, 2, 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(a, true).size() == 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(a, false).size() == 3);
		List<DataPoint<Integer>> b = createDataPointList(0, 1, 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(b, true).size() == 2);
		assertTrue(ProfileUtil.stripRedundantMidpoints(b, false).size() == 2);
		List<DataPoint<Integer>> c = createDataPointList(0, 1, 1, 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(c, true).size() == 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(c, false).size() == 2);
		List<DataPoint<Integer>> d = createDataPointList(0, 1, 1, 2, 2, 3, 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(d, true).size() == 6);
		assertTrue(ProfileUtil.stripRedundantMidpoints(d, false).size() == 4);
		List<DataPoint<Integer>> e = createDataPointList(0, 1, 1, 1, 2, 2, 2, 3, 3, 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(e, true).size() == 6);
		assertTrue(ProfileUtil.stripRedundantMidpoints(e, false).size() == 4);
		List<DataPoint<Integer>> f = createDataPointList(0, 1, 2, 1, 2, 1, 2, 3, 3, 3);
		assertTrue(ProfileUtil.stripRedundantMidpoints(f, true).size() == 8);
		assertTrue(ProfileUtil.stripRedundantMidpoints(f, false).size() == 8);
	}
	
	public void doProfileStepInterpolationTest(EDataType dataType, Object... values) {
		Profile p = createStepProfile(dataType, values);

		assertTrue(values.length == 0 || !p.getDataPoints().isEmpty());
		if (values.length == 0)
			return;

		DataPoint firstDataPoint = (DataPoint) p.getDataPoints().get(0);
		Date beforeFirst = new Date(firstDataPoint.getDate().getTime() - 30);
		DataPoint beforeFirstDataPoint = p.getDataPoint(beforeFirst);

		assertNotNull(beforeFirstDataPoint);
		assertEquals(beforeFirst, beforeFirstDataPoint.getDate());
		assertNull(beforeFirstDataPoint.getValue());

		for (Object o : p.getDataPoints()) {
			assertTrue(o instanceof DataPoint);
			DataPoint dp = (DataPoint) o;
			Date atDate = dp.getDate();
			Date afterDate = new Date(atDate.getTime() + 60 * 1000);
			DataPoint atDataPoint = p.getDataPoint(atDate);
			DataPoint afterDataPoint = p.getDataPoint(afterDate);
			assertSame(dp, atDataPoint);
			assertNotNull(afterDataPoint);
			assertEquals(afterDate, afterDataPoint.getDate());
			assertEquals(atDataPoint.getValue(), afterDataPoint.getValue());
		}
	}
	
	@Test
	public void testProfileBinaryOperations() {
		Profile a = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
		Profile b = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
		Profile expectedUnion = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
		Profile expectedIntersection = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
		testProfileIntersection(a, b, expectedIntersection);
		testProfileUnion(a, b, expectedUnion);
	}
		
	public void testProfileIntersection(Profile a, Profile b, Profile expectedIntersection) {
		Profile binaryIntersection = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT);
		binaryIntersection.setDataPoints(ProfileUtil.getBinaryIntersection(a.getDataPoints(), b.getDataPoints()));
		
		for (Object o : expectedIntersection.getDataPoints()) {
			DataPoint dp = (DataPoint) o;
			Date d = dp.getDate();
			// System.out.printf("Date %s: %s || %s == %s%n", d, a.getValue(d), b.getValue(d), binaryIntersection.getValue(d));
			assertEquals(dp.getValue(), binaryIntersection.getValue(d));
		}
	}
	
	public void testProfileUnion(Profile a, Profile b, Profile expectedUnion) {
		
		Profile binaryUnion = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT);
		binaryUnion.setDataPoints(ProfileUtil.getBinaryUnion(a.getDataPoints(), b.getDataPoints()));
		
		for (Object o : expectedUnion.getDataPoints()) {
			DataPoint dp = (DataPoint) o;
			// Date d = dp.getDate();
			// System.out.printf("Date %s: %s && %s == %s%n", d, a.getValue(d), b.getValue(d), binaryUnion.getValue(d));
			assertEquals(dp.getValue(), binaryUnion.getValue(dp.getDate()));
		}
	}

	@Test
	public void testProfileOverlay() {
		Profile a = createBooleanProfile(0, "==____==_ ");
		Profile b = createBooleanProfile(0, "  ======_ ");
		Profile c = createBooleanProfile(0, "======_   ");
		Profile expectedAOverB = createBooleanProfile(0, "==____==_ ");
		Profile expectedBOverA = createBooleanProfile(0, "========_ ");
		Profile expectedCOverA = createBooleanProfile(0, "========_ ");
		testProfileOverlay(a, b, expectedAOverB);
		testProfileOverlay(b, a, expectedBOverA);
		testProfileOverlay(c, a, expectedCOverA);
	}
	
	@Test
	public void testInsertToEmptyProfile() {
		Profile p = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, new Boolean[0]);
		assertEquals(0, p.getDataPoints().size());
		DataPoint<Boolean> newDataPoint = JSCIENCE_FACTORY.createEDataPoint(new Date(), Boolean.TRUE);
		ProfileUtil.insertNewDataPoint(newDataPoint, p.getDataPoints());
		assertEquals(1, p.getDataPoints().size());
	}
	
	@Test 
	public void testInsertDataPoints() {
		Profile p = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
		//test dates that are not equal
		int count = p.getDataPoints().size();
		for (int i = 0; i < count; i++) { 
			DataPoint dp = (DataPoint) p.getDataPoints().remove(i);
			assertEquals(count-1, p.getDataPoints().size());
			ProfileUtil.insertNewDataPoint(dp, p.getDataPoints());
			assertEquals(count, p.getDataPoints().size());
			assertEquals(i, p.getDataPoints().indexOf(dp));
		}
		//test dates that are equal
		for (int i = 0; i < count; i++) { 
			assertEquals(count, p.getDataPoints().size());
			DataPoint dp = (DataPoint) p.getDataPoints().get(i);
			DataPoint<Boolean> newDataPoint = JSCIENCE_FACTORY.createEDataPoint(dp.getDate(), !((Boolean)dp.getValue()));
			ProfileUtil.insertNewDataPoint(newDataPoint, p.getDataPoints());
			assertEquals(count+1, p.getDataPoints().size());
			Object removed = p.getDataPoints().remove(i+1);
			assertEquals(count, p.getDataPoints().size());
			assertEquals(removed, newDataPoint);
		}
	}
	
	public void testProfileOverlay(Profile top, Profile bottom, Profile expectedOverlay) {
		Profile<Boolean> overlay = createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT);
		List<DataPoint<Boolean>> overlay2 = ProfileUtil.getOverlay(top.getDataPoints(), bottom.getDataPoints());
		List<DataPoint<Boolean>> stripped = ProfileUtil.stripInstantaneousTransitions(overlay2);
		Collection stripped2 = ProfileUtil.stripRedundantMidpoints(stripped, false);
		overlay.setDataPoints(stripped2);
				
		if (!overlay.getDataPoints().equals(expectedOverlay.getDataPoints())) {
			System.out.println("Expected: " + renderBooleanProfile(expectedOverlay, 10));
			System.out.println("Actual:   " + renderBooleanProfile(overlay, 10));
			fail("Overlay is not what was expected");
		}
//		else {
//			System.out.println("Top:      " + renderBooleanProfile(top, 10));
//			System.out.println("Bottom:   " + renderBooleanProfile(bottom, 10));
//			System.out.println("Result:   " + renderBooleanProfile(overlay, 10));		
//		}
	}

	public String renderBooleanProfile(Profile<Boolean> p, int finalTimeSlot) {
		if (p == null || p.getDataPoints() == null || p.getDataPoints().isEmpty()) {
			char[] toRet = new char[finalTimeSlot];
			Arrays.fill(toRet, ' ');
			return new String(toRet);
		}
		StringBuilder toRet = new StringBuilder();
		List<DataPoint<Boolean>> dps = ProfileUtil.stripRedundantMidpoints(p.getDataPoints(), false);
		Date pStart = (dps.get(0)).getDate();
		Date pEnd = (dps.get(dps.size() - 1)).getDate();
		for (int slot = 0; slot <= finalTimeSlot; ++slot) {
			Date tickDate = getTestTimeTick(slot);
			if (tickDate.before(pStart) || !tickDate.before(pEnd)) {
				toRet.append(" ");
				continue;
			}
			if (p.getDataPoint(tickDate).getValue() == Boolean.TRUE)
				toRet.append("=");
			else 
				toRet.append("_");
		}
		return toRet.toString();
	}

	
	public Profile createBooleanProfile(int initialTick, String profile) {
		Profile p = JSCIENCE_FACTORY.createProfile();
		List<DataPoint> dp = new ArrayList<DataPoint>();
		if (profile.length() != 0) {
			Boolean current = null;
			for (int i = 0; i < profile.length(); ++i) {
				int tick = initialTick + i;
				if (profile.charAt(i) == '=' && current != Boolean.TRUE) {
					dp.add(JScienceFactory.eINSTANCE.createEDataPoint(getTestTimeTick(tick), Boolean.TRUE));
					current = Boolean.TRUE;
				}
				else if (profile.charAt(i) == '_' && current != Boolean.FALSE) {
					dp.add(JScienceFactory.eINSTANCE.createEDataPoint(getTestTimeTick(tick), Boolean.FALSE));
					current = Boolean.FALSE;
				}
			}
			if (!dp.isEmpty() && dp.get(dp.size() - 1).getValue() != Boolean.FALSE)
				dp.add(JScienceFactory.eINSTANCE.createEDataPoint(getTestTimeTick(initialTick + profile.lastIndexOf("=") + 1), Boolean.FALSE));
		}
		p.setDataPoints(dp);
		p.setInterpolation(INTERPOLATION.STEP);
		p.setDataType(EcorePackage.Literals.EBOOLEAN_OBJECT);
		return p;
	}
	
	public static <T> Profile<T> createStepProfile(EDataType dataType, T ... values) {
		return createStepProfile(dataType, 0, values);
	}
	
	public static <T> Profile<T> createStepProfile(EDataType dataType, int initialTick, T... values) {
		Profile p = JSCIENCE_FACTORY.createProfile();
		p.setDataPoints(createDataPointList(initialTick, values));
		p.setInterpolation(INTERPOLATION.STEP);
		p.setDataType(dataType);
		return p;
	}
	
	public static <T> List<DataPoint<T>> createDataPointList(int initialTick, T... values) {
		List<DataPoint<T>> toRet = new ArrayList<DataPoint<T>>(values.length);
		for (int i = 0; i < values.length; ++i) {
			Date dpTime = getTestTimeTick(initialTick + i * 2);
			DataPoint<T> dp = JSCIENCE_FACTORY.createEDataPoint(dpTime, values[i]);
			toRet.add(dp);
		}
		return toRet;
	}

	public static Date getTestTimeTick(int timeSlot) {
		return new Date(946713600000l + (60*1000*timeSlot));
	}
}
