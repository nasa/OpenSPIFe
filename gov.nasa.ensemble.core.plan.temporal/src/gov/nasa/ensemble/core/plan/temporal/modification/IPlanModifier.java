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
package gov.nasa.ensemble.core.plan.temporal.modification;


import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Date;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;


/**
 * This interface is implemented by the plan modifier extension point.
 * 
 * Plan modifiers take suggestions for modifications to the plan and 
 * attempt to modify the plan in a "minimal" way.  (The interpretation
 * of "minimal" is up to the particular modifier.)  A modifier might
 * maintain plan consistency, but this is not required by the interface.
 * 
 * @author bachmann
 *
 */
public interface IPlanModifier {
	
	/**
	 * This function will be called before any others in order to initialize
	 * the plan modifier for use with the specified model.  It will only be
	 * called once.  
	 * @param plan
	 */
	public void initialize(EPlan plan);

//	/**
//	 * Shift the start of the element by the given delta.
//	 * Holds end and varies duration.
//	 * 
//	 * @param element
//	 * @param delta
//	 * @param initialState
//	 * @return
//	 */
//	public Map<EPlanElement, TemporalExtent> shiftStart(EPlanElement element, Amount<Duration> delta, TemporalExtentsCache initialState);
	
	/**
	 * Set the start of the element to be at the specified time.
	 * Holds end and varies duration.
	 * 
	 * @param element
	 * @param start
	 * @param initialState
	 * @return
	 */
	public Map<EPlanElement, TemporalExtent> setStart(EPlanElement element, Date start, TemporalExtentsCache initialState);
	
//	/**
//	 * Shift the end of the element by the given delta.
//	 * Holds start and varies duration.
//	 * 
//	 * @param element
//	 * @param delta
//	 * @param initialState
//	 * @return
//	 */
//	public Map<EPlanElement, TemporalExtent> shiftEnd(EPlanElement element, Amount<Duration> delta, TemporalExtentsCache initialState);
	
	/**
	 * Set the end of the element to be at the specified time.
	 * Holds end and varies duration.
	 * 
	 * @param element
	 * @param end
	 * @param initialState
	 * @return
	 */
	public Map<EPlanElement, TemporalExtent> setEnd(EPlanElement element, Date end, TemporalExtentsCache initialState);
	
	
	/**
	 * Set the duration of the element to the specified amount.
	 * If fromStart, holds start and varies end; otherwise holds end and varies start.
	 * 
	 * @param element
	 * @param duration
	 * @param initialState
	 * @param fromStart
	 * @return
	 */
	public Map<EPlanElement, TemporalExtent> setDuration(EPlanElement element, Amount<Duration> duration, TemporalExtentsCache initialState, boolean fromStart);
	
	/**
	 * Shift the element by the given delta.
	 * Holds duration and varies both start and end.
	 * 
	 * @param element
	 * @param delta
	 * @param initialState
	 * @return
	 */
	public Map<EPlanElement, TemporalExtent> shiftElement(EPlanElement element, Amount<Duration> delta, TemporalExtentsCache initialState);
	
	/**
	 * Moving the element so that it starts at the specified time.
	 * Holds duration and varies both start and end.
	 * 
	 * @param element
	 * @param delta
	 * @param initialState
	 */
	public Map<EPlanElement, TemporalExtent> moveToStart(EPlanElement element, Date start, TemporalExtentsCache initialState);
	
	/**
	 * Moving the element so that it ends at the specified time.
	 * Holds duration and varies both start and end.
	 * 
	 * @param element
	 * @param delta
	 * @param initialState
	 */
	public Map<EPlanElement, TemporalExtent> moveToEnd(EPlanElement element, Date end, TemporalExtentsCache initialState);
	
}
