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
package gov.nasa.ensemble.common.runtime;

import java.util.Formatter;

/**
 * Thrown when {@link MemoryHogException}.check() finds that more than the target fraction of remaining memory has been used. Callers should catching this and give up, possibly undoing the partial
 * work and possibly logging or notifying the user.
 * 
 * @author kanef
 */
public class MemoryHogException extends Exception {

	long nObjectsCreatedSoFar;
	String nameOfObject;
	long freeMemoryNow;
	long freeMemoryAtStart;

	public MemoryHogException(long nObjectsCreatedSoFar, String nameOfObject, long freeMemoryNow, long freeMemoryAtStart) {
		super();
		this.nObjectsCreatedSoFar = nObjectsCreatedSoFar;
		this.nameOfObject = nameOfObject;
		this.freeMemoryNow = freeMemoryNow;
		this.freeMemoryAtStart = freeMemoryAtStart;
	}

	@Override
	public String getMessage() {
		StringBuilder out = new StringBuilder();
		Formatter formatter = new Formatter(out);
		formatter.format("The %,d %s created consume too much memory -- %d %s.", nObjectsCreatedSoFar, nameOfObject, roundedMemory(freeMemoryAtStart - freeMemoryNow), memoryUnits(freeMemoryAtStart - freeMemoryNow));
		formatter.close();
		return out.toString();
	}

	/** E.g. 42 for 42 gigabytes. */
	private long roundedMemory(long memory) {
		if (memory < 0)
			return -roundedMemory(-memory);
		double scaledDown = memory;
		while (scaledDown > 1024) {
			scaledDown = scaledDown / 1024;
		}
		return Math.round(scaledDown);
	}

	/** E.g. "Gb" for 42 gigabytes. */
	private String memoryUnits(long memory) {
		if (memory < 0)
			return memoryUnits(-memory);
		if (memory < 1024)
			return "bytes";
		if (memory < 1024 * 1024)
			return "kB";
		if (memory < 1024 * 1024 * 1024)
			return "Mb";
		if (memory < 1024 * 1024 * 1024 * 1024)
			return "Gb";
		return "Tb";
	}

	public long getnObjectsCreatedSoFar() {
		return nObjectsCreatedSoFar;
	}

	public String getNameOfObject() {
		return nameOfObject;
	}

	public long getFreeMemoryNow() {
		return freeMemoryNow;
	}

	public long getFreeMemoryAtStart() {
		return freeMemoryAtStart;
	}

}
