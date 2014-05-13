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
package gov.nasa.ensemble.core.plan.constraints.ui.advisor;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;

import java.util.List;

public class NogoodTemporalChain implements INogoodPart {
	
	private final TemporalChain chain;
	private final List<EPlanElement> participants;

	public NogoodTemporalChain(TemporalChain chain, List<EPlanElement> participants) {
		this.chain = chain;
		this.participants = participants;
	}

	public TemporalChain getChain() {
		return chain;
	}
	
	public List<EPlanElement> getParticipants() {
		return participants;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chain == null) ? 0 : chain.hashCode());
		result = prime * result + ((participants == null) ? 0 : participants.size());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NogoodTemporalChain other = (NogoodTemporalChain) obj;
		if (chain != other.chain) {
			return false;
		}
		if (participants == null) {
			if (other.participants != null) {
				return false;
			}
		} else if (!participants.equals(other.participants)) {
			return false;
		}
		return true;
	}

}
