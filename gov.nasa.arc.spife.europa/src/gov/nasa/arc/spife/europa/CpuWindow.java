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
package gov.nasa.arc.spife.europa;

import java.util.Date;

public class CpuWindow implements ICpuWindow {

	private final Date start;
	private final Date end;
	
	public CpuWindow(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public Date getStartTime() {
		return start;
	}

	@Override
	public Date getEndTime() {
		return end;
	}

	@Override
	public String toString() {
		return "Cpu[" + start.getTime() + ", " + end.getTime() + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ICpuWindow)) {
			return false;
		}
		ICpuWindow other = (ICpuWindow)o;
		return (getStartTime().equals(other.getStartTime()))
		    && (getEndTime().equals(other.getEndTime()));
	}
	
}
