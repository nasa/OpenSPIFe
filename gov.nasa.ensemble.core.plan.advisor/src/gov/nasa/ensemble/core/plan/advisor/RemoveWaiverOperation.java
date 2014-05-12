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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;

import java.util.List;

public class RemoveWaiverOperation extends AbstractTransactionUndoableOperation {

	private final IWaivable waivable;
	private final String prefix;
	private final List<String> waivers;
	protected final String waiverRationale;

	public RemoveWaiverOperation(String label, IWaivable waivable) {
		super(label);
		this.waivable = waivable;
		this.prefix = null;
		this.waivers = null;
		this.waiverRationale = waivable.getWaiverRationale();
	}

	public RemoveWaiverOperation(String label, String prefix, List<String> waivers) {
		super(label);
		this.waivable = null;
		this.prefix = prefix;
		this.waivers = waivers;
		this.waiverRationale = WaiverUtils.getRationale(prefix, waivers);
		
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to dispose
	}

	@Override
	protected void execute() throws Throwable {
		setRationale(null);
	}

	@Override
	protected void undo() throws Throwable {
		setRationale(waiverRationale);
	}

	protected void setRationale(String rationale) {
		if (waivable != null) {
			WaiverUtils.setRationale(waivable, rationale);
		}
		if (prefix != null) {
			WaiverUtils.setRationale(waivers, prefix, rationale);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(RemoveWaiverOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(waivable));
		builder.append(" with ");
		builder.append(String.valueOf(waiverRationale));
		return builder.toString();
	}

}
