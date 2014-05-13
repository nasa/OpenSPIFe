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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

import java.text.ParseException;

import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

@SuppressWarnings("unchecked")
public class EnsembleUnitFormat implements MissionExtendable {

	public static final EnsembleUnitFormat INSTANCE = getInstance();
	private static EnsembleUnitFormat getInstance() {
		try { 
			return MissionExtender.construct(EnsembleUnitFormat.class);
		} catch (ConstructionException e) {
			LogUtil.warn("failed to construct mission extension", e);
			return new EnsembleUnitFormat();
		}
	}

	public static UnitFormat getUnitFormat() {
		return EnsembleFormat.INSTANCE;
	}

	public String format(Unit<?> unit) {
		return EnsembleFormat.INSTANCE.format(unit);
	}
	
	/**
	 * Forwards the call to the standard instance UnitFormat object. This
	 * instance has been initialized above, so we wish to use that instance
	 * and no other, thus it is final.
	 * 
	 * @param u unit to format
	 * @return formatted unit
	 */
	public String formatUnit(Unit<?> u) {
		return EnsembleFormat.INSTANCE.format(u);
	}
	
	/**
	 * Parse the unit from a string to the Unit object
	 * 
	 * @param units string to parse
	 * @return the Unit object
	 */
	public Unit<?> parse(String units) throws ParseException {
		return doParse(units);
	}

	/**
	 * Calls parse but never throws an exception and returns Unit.ONE on failure.
	 * @param units
	 * @return
	 */
	public Unit<?> parseSafely(String units) {
		try {
			return doParse(units);
		} catch (ParseException e) {
			LogUtil.errorOnce("Unknown unit: " + units);
			return Unit.ONE;
		}
	}
	
	/**
	 * Extracted so it can be called from parseSafely without
	 * causing the infinite loop when this was part of parse.
	 * (In the ScoreUnitFormat subclass which always parseSafely)
	 * 
	 * @param units
	 * @return
	 * @throws ParseException
	 */
	private Unit<?> doParse(String units) throws ParseException {
		if (units == null || units.trim().length() == 0) {
			return Unit.ONE;
		}
		units = units.replaceAll("-", "");
		return (Unit<?>) EnsembleFormat.INSTANCE.parseObject(units);
	}
	
}
