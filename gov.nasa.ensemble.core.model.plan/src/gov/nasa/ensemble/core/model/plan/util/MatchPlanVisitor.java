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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

public abstract class MatchPlanVisitor extends PlanVisitor {
	
	private EPlanElement pe;
	
	public MatchPlanVisitor() {
		super();
	}

	public MatchPlanVisitor(boolean visitSubActivities) {
		super(visitSubActivities);
	}

	public EPlanElement getPlanElement() {
		return pe;
	}
	
	@Override
	public void visitAll(EPlanElement element) {
		try {
			visit(element);
		    if ((visitSubActivities || !(element instanceof EActivity)) && element != null) {
				for (EPlanChild child : element.getChildren()) {
					visitAll(child);
				}
		    }
		} catch (MatchPlanVisitor.MatchException e) {
			// do nothing, exit loop
		}
	}

	@Override
	public void visitAll(Iterable<? extends EPlanElement> elements) {
		try {
			for (EPlanElement element : elements) {
				visitAll(element);
			}
		} catch (MatchPlanVisitor.MatchException e) {
			// do nothing, exit loop
		}
	}

	@Override
	protected final void visit(EPlanElement element) {
		if (matches(element)) {
			pe = element;
			throw new MatchException();
		}
	}

	protected abstract boolean matches(EPlanElement element);
	
	private static final class MatchException extends RuntimeException {
		// marker exception
	}
	
}
