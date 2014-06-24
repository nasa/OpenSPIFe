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
package gov.nasa.ensemble.core.plan.formula;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * A {@link MissionExtendable} abstract class which provides the interface for
 * updating one or all of a {@link EPlanElement}'s parameters.
 */
public abstract class FormulaEngine implements MissionExtendable {
	
	private static final Logger trace = Logger.getLogger(FormulaEngine.class);

	private static FormulaEngine formulaEngine = null;
	private static boolean formulaEngineFailed = false;
	
	/**
	 * Returns a formula engine.  Since the formula engine is not multithread safe,
	 * you must call synchronize on it.
	 * @return a formula engine.
	 */
	public static FormulaEngine getInstance() {
		if (formulaEngine == null) {
			synchronized (FormulaEngine.class) {
				if (formulaEngine == null) {
					if (!formulaEngineFailed) {
						try {
							formulaEngine = MissionExtender.construct(FormulaEngine.class);
						} catch (Exception e) {
							formulaEngineFailed = true;
							trace.error("failed to create formula engine", e);
						}
					}
				}
			}
		}
        return formulaEngine;
	}
	
	public Object evaluate(EPlanElement planElement, String formula, Date date) throws Exception {
		return getValue(planElement, formula, date);
	}
	
	/**
	 * Evaluates a string representing a formula.
	 * @throws Exception
	 */
	public abstract Object getValue(EPlanElement planElement, String formula) throws Exception;
	
	/**
	 * Evaluates a string representing a formula, the date may be required when the
	 * activity expression depends upon the entire state of the plan (i.e. current data)
	 */
	public Object getValue(EPlanElement planElement, String formula, Date date) throws Exception {
		return getValue(planElement, formula);
	}
}
