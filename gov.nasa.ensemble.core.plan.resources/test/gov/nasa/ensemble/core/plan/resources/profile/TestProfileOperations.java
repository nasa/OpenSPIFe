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
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TestProfile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.plan.resources.profile.operations.AddProfileDataPointOperation;
import gov.nasa.ensemble.core.plan.resources.profile.operations.RemoveProfileDataPointsOperation;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

public class TestProfileOperations extends TestCase {

	private static final JScienceFactory JSCIENCE_FACTORY = JScienceFactory.eINSTANCE;

	@Test
	public void testAddOperationEmptyProfile() {
		Profile<Boolean> p = TestProfile.createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, new Boolean[0]);
		assertEquals(0, p.getDataPoints().size());
		DataPoint<Boolean> newDataPoint = JSCIENCE_FACTORY.createEDataPoint(new Date(), Boolean.TRUE);
		AddProfileDataPointOperation op = new AddProfileDataPointOperation(p, newDataPoint);
		op.execute(null, null);
		assertEquals(1, p.getDataPoints().size());
		op.undo(null, null);
		assertEquals(0, p.getDataPoints().size());
	}
	
	@Test 
	public void testInsertDataPoints() {
		Profile<Boolean> p = TestProfile.createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
		int count = p.getDataPoints().size();
		assertNotSame(0, count); //make sure profile is not empty to begin with
		
		//test dates that are not equal
		for (int i = 0; i < count; i++) { 
			DataPoint<Boolean> dp = p.getDataPoints().remove(i);
			assertEquals(count-1, p.getDataPoints().size());
			
			AddProfileDataPointOperation op = new AddProfileDataPointOperation(p, dp);
			op.execute(null, null);
			assertEquals(count, p.getDataPoints().size());
			op.undo(null, null);
			assertEquals(count-1, p.getDataPoints().size());
			op.redo(null, null);
			assertEquals(count, p.getDataPoints().size());

			assertEquals(i, p.getDataPoints().indexOf(dp));
		}
		
		//test dates that are equal
		for (int i = 0; i < count; i++) { 
			assertEquals(count, p.getDataPoints().size());
			DataPoint dp = p.getDataPoints().get(i);
			DataPoint<Boolean> newDataPoint = JSCIENCE_FACTORY.createEDataPoint(dp.getDate(), !((Boolean)dp.getValue()));
			
			AddProfileDataPointOperation op = new AddProfileDataPointOperation(p, newDataPoint);
			op.execute(null, null);
			assertEquals(count+1, p.getDataPoints().size());
			op.undo(null, null);
			assertEquals(count, p.getDataPoints().size());
			op.redo(null, null);
			assertEquals(count+1, p.getDataPoints().size());
			
			Object removed = p.getDataPoints().remove(i+1);
			assertEquals(count, p.getDataPoints().size());
			assertEquals(removed, newDataPoint);
		}
	}
	
	@Test
	public void testRemoveAllDataPointsOperation() {
		Profile<Boolean> p = TestProfile.createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
		int count = p.getDataPoints().size();
		assertNotSame(0, count); //make sure profile is not empty to begin with
		
		DataPoint[] all = p.getDataPoints().toArray(new DataPoint[count]);
		RemoveProfileDataPointsOperation removeAllOp = new RemoveProfileDataPointsOperation(p, all);
		removeAllOp.execute(null, null);
		assertEquals(0, p.getDataPoints().size());
		removeAllOp.undo(null, null);
		assertEquals(count, p.getDataPoints().size());
		removeAllOp.redo(null, null);
		assertEquals(0, p.getDataPoints().size());
	}
	
	@Test
	public void testRemoveOneDataPoint() {
		Profile<Boolean> p = TestProfile.createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
		int count = p.getDataPoints().size();
		assertNotSame(0, count); //make sure profile is not empty to begin with
		
		//test dates that are not equal
		for (int i = 0; i < count; i++) { 
			DataPoint<Boolean> dp = p.getDataPoints().get(i);
			RemoveProfileDataPointsOperation op = new RemoveProfileDataPointsOperation(p, new DataPoint[] { dp });
			op.execute(null, null);
			assertEquals(count-1, p.getDataPoints().size());
			assertEquals(-1, p.getDataPoints().indexOf(dp)); //its not
			op.undo(null, null);
			assertEquals(count, p.getDataPoints().size());
			assertEquals(i, p.getDataPoints().indexOf(dp)); //its there
			op.redo(null, null);
			assertEquals(count-1, p.getDataPoints().size()); 
			assertEquals(-1, p.getDataPoints().indexOf(dp)); //its not
			p.getDataPoints().add(i, dp); //reset!
		}
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testRemoveMultipleDataPoints() {
		Profile p = TestProfile.createStepProfile(EcorePackage.Literals.EBOOLEAN_OBJECT, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
		int count = p.getDataPoints().size();
		assertNotSame(0, count); //make sure profile is not empty to begin with
		
		for (int i = 0; i < count ; i++) {
			for (int j = i; j < count; j++) {
				List<DataPoint> dps = p.getDataPoints().subList(i, j);
				DataPoint[] toRemove = dps.toArray(new DataPoint[dps.size()]);
				RemoveProfileDataPointsOperation op = new RemoveProfileDataPointsOperation(p, toRemove);
				op.execute(null, null);
				assertEquals(count - (j-i), p.getDataPoints().size());
				
				op.undo(null, null);
				assertEquals(count, p.getDataPoints().size());
				
				op.redo(null, null);
				assertEquals(count - (j-i), p.getDataPoints().size());
				
				//reset
				for (DataPoint dataPoint : toRemove) {
					ProfileUtil.insertNewDataPoint(dataPoint, p.getDataPoints());
				}
			}
		}
	}
	
}
