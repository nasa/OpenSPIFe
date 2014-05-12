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
package gov.nasa.ensemble.core.plan.parameters;

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.HashMap;
import java.util.Map;

public class VisibleOperation extends AbstractEnsembleUndoableOperation {

	private EPlanElement planElement = null;
	private boolean visible = false;
	
	private Map<EActivity, Boolean> nodeToStateCache = new HashMap<EActivity, Boolean>();
	
	public VisibleOperation(EPlanElement planElement, boolean visible) {
		super("visible");
		this.planElement = planElement;
		this.visible = visible;
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do here in any case
	}

	@Override
	protected void execute() {
		cache();
		SpifePlanUtils.setVisible(planElement, visible);
	}
	
	private void cache() {
		nodeToStateCache.clear();
		cache(planElement);
	}

	private void cache(EPlanElement pe) {
		if (pe instanceof EActivity) {
			// Activities cannot be quasi so...
			nodeToStateCache.put((EActivity)pe, SpifePlanUtils.getVisible(pe) == TriState.TRUE);
		}
		for (EPlanElement child : EPlanUtils.getChildren(pe)) {
			cache(child);
		}
	}
	
	@Override
	protected void undo() {
		for (EActivity node : nodeToStateCache.keySet()) {
			Boolean b = nodeToStateCache.get(node);
			SpifePlanUtils.setVisible(node, b);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(VisibleOperation.class.getSimpleName());
		builder.append(":");
		builder.append(planElement.getName());
		builder.append(" visible to ");
		builder.append(String.valueOf(visible));
		return builder.toString();
	}

}
