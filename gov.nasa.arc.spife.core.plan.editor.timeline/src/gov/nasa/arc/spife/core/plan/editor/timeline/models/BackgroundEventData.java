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
/**
 * 
 */
package gov.nasa.arc.spife.core.plan.editor.timeline.models;

import gov.nasa.arc.spife.core.plan.editor.timeline.IInfobarContributor;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class BackgroundEventData {
	
	private IInfobarContributor contributor;
	private Set<EPlanElement> nodes;
	private long leftTime;
	private EPlanElement leftNode;
	private Timepoint leftTimepoint;
	private long rightTime;
	private EPlanElement rightNode;
	private Timepoint rightTimepoint;
	private Date selectionStartTime;
	
	public BackgroundEventData(
		IInfobarContributor contributor, 
		Set<? extends EPlanElement> nodes,
		long leftTime, EPlanElement leftNode, Timepoint leftTimepoint,
		long rightTime, EPlanElement rightNode, Timepoint rightTimepoint, 
		Date selectionStartTime
	) {
		this.contributor = contributor;
		this.nodes = new LinkedHashSet<EPlanElement>(nodes);
		this.leftTime = leftTime;
		this.leftNode = leftNode;
		this.leftTimepoint = leftTimepoint;
		this.rightTime = rightTime;
		this.rightNode = rightNode;
		this.rightTimepoint = rightTimepoint;
		this.selectionStartTime = selectionStartTime;
	}
	
	public IInfobarContributor getContributor() {
		return contributor;
	}
	
	public Set<EPlanElement> getNodes() {
		return nodes;
	}
	
	public long getLeftTime() {
		return leftTime;
	}
	
	public EPlanElement getLeftNode() {
		return leftNode;
	}
	
	public Timepoint getLeftTimepoint() {
		return leftTimepoint;
	}
	
	public long getRightTime() {
		return rightTime;
	}
	
	public EPlanElement getRightNode() {
		return rightNode;
	}
	
	public Timepoint getRightTimepoint() {
		return rightTimepoint;
	}
	
	public Date getSelectionStartTime() {
		return selectionStartTime;
	}
	
}
