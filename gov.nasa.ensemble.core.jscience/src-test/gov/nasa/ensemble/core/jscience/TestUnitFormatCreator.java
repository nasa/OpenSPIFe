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

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

public abstract class TestUnitFormatCreator extends UnitFormat {

	private static final long serialVersionUID = -2528076738449987285L;

	public static UnitFormat newTestFormat() {
		DefaultFormat format = new DefaultFormat() {
			private static final long serialVersionUID = -1345662241285090481L;
		};
		initializeUnitFormat(format);
		return format;
	}

	private static void initializeUnitFormat(UnitFormat unitFormat) {
		unitFormat.alias(SI.SECOND, "sec");
		unitFormat.alias(SI.SECOND, "seconds");
		unitFormat.alias(SI.MILLI(SI.SECOND).times(5.1), "incs");
		
		unitFormat.alias(SI.NEWTON.times(SI.METER), "Nm");
		
		unitFormat.alias(SI.AMPERE, "amps");
		unitFormat.alias(SI.AMPERE, "amp");
		
		unitFormat.alias(SI.OHM, "ohms");
		
		unitFormat.alias(NonSI.BYTE, "bytes");
		
		unitFormat.alias(SI.RADIAN, "radian");
		unitFormat.alias(SI.RADIAN, "rad");
		unitFormat.alias(SI.RADIAN.pow(-1), "curvature");
		unitFormat.alias(NonSI.DEGREE_ANGLE, "deg");
		
		unitFormat.alias(SI.CELSIUS, "degC");
		
		unitFormat.alias(SI.WATT, "watts");
		
		unitFormat.alias(Unit.ONE, "samples");
		unitFormat.alias(Unit.ONE, "count");
		unitFormat.alias(Unit.ONE, "cells");
		unitFormat.alias(Unit.ONE, "records");
		unitFormat.alias(Unit.ONE, "images");
		unitFormat.alias(Unit.ONE, "datasets");
		unitFormat.alias(Unit.ONE, "instances");
		unitFormat.alias(Unit.ONE, "iterations");
		unitFormat.alias(Unit.ONE, "features");
		unitFormat.alias(Unit.ONE, "pyramid");
		unitFormat.alias(Unit.ONE, "slot");
		unitFormat.alias(Unit.ONE, "slots");
		unitFormat.alias(Unit.ONE, "levels");
		unitFormat.alias(Unit.ONE, "buffers");
		unitFormat.alias(Unit.ONE, "failures");
		unitFormat.alias(Unit.ONE, "parameters");
		unitFormat.alias(Unit.ONE, "points");
		unitFormat.alias(Unit.ONE, "entries");
		unitFormat.alias(Unit.ONE, "errors");
		unitFormat.alias(Unit.ONE, "states");
		unitFormat.alias(Unit.ONE, "magnitude");
		unitFormat.alias(Unit.ONE, "counts");
		unitFormat.alias(Unit.ONE, "stages");
		unitFormat.alias(Unit.ONE, "segments");
		unitFormat.alias(Unit.ONE, "flushes");
		unitFormat.alias(Unit.ONE, "counts");
	}

}
