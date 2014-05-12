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

/**
 * SPF-6009:  A mechanism for code to make sure it doesn't use up an unreasonable amount of memory, relative to what's available. 
 * Use for code that repeatedly allocates memory in a loop, to make it fail before actually exhausting memory.
 * <pre>
 * 		MemoryHogMonitor monitor = new MemoryHogMonitor("integers", .15);
		try {
			for (...) {
				... your memory-eating code here ...
				monitor.check();
			}
		} catch (MemoryHogException e) {
			LogUtil.warn(e);
		}
 * </pre>
 * @since SPF-6009
 * @author kanef
 *
 */
public class MemoryHogMonitor {
	
	private Runtime runtime = Runtime.getRuntime();
	long freeMemoryAtStart;
	long freeMemoryMinimum;
	long nObjectsCreatedSoFar;
	String nameOfObject;
	
	/**
	 * @param nameOfObject -- description of object type (plural) to use in error message.
	 * @param maxFractionToUse -- a number strictly between 0 and 1, representing amount of remaining memory it would be reasonable to use
	 */
	public MemoryHogMonitor(String nameOfObject, double maxFractionToUse) {
		this.nameOfObject = nameOfObject;
		this.freeMemoryAtStart = runtime.freeMemory();
		this.freeMemoryMinimum = (long) (freeMemoryAtStart * (1.0-maxFractionToUse));		
	}

	/**
	 * Call from inside the outer loop of code that repeatedly allocates memory, to keep it from using too much.
	 * Forces a GC and rechecks before throwing exception
	 * @throws MemoryHogException if more than this amount has been used
	 */
	
	public void check () throws MemoryHogException {
		check(true);
	}
	
	/**
	 * Call from inside the outer loop of code that repeatedly allocates memory, to keep it from using too much.
	 * @param tryGC = true to force a GC and recheck before throwing exception
	 * @throws MemoryHogException if more than this amount has been used
	 */
	public void check (boolean tryGC) throws MemoryHogException {
		long freeMemoryNow = runtime.freeMemory();
		nObjectsCreatedSoFar++;
		if (freeMemoryNow < freeMemoryMinimum) {
			if (tryGC) {
				runtime.gc();
				check(false);
			}
			
			throw new MemoryHogException(nObjectsCreatedSoFar, nameOfObject, freeMemoryNow, freeMemoryAtStart);
		}
	}

}
