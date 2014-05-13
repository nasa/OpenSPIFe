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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

public class SetExpandedOperation extends AbstractTransactionUndoableOperation {
	
	private final boolean expanded;
	private final List<CommonMember> membersToModify = new ArrayList<CommonMember>();

	public SetExpandedOperation(EPlan plan, final boolean expanded) {
		this(plan.getChildren(), expanded, (expanded ? "expand all" : "collapse all"));
	}

	public SetExpandedOperation(List<EPlanChild> children, final boolean expanded, String label) {
		super(label);
		this.expanded = expanded;
		for (EPlanChild element : children) {
			CommonMember member = element.getMember(CommonMember.class);
			if (expanded != member.isExpanded()) {
				membersToModify.add(member);
			}
		}
	}
	
	@Override
	protected boolean isExecutable() {
		return !membersToModify.isEmpty();
	}
	
	@Override
	protected void dispose(UndoableState state) {
		membersToModify.clear();
	}

	@Override
	protected void execute(final IProgressMonitor monitor) throws Throwable {
		monitor.beginTask(getLabel(), membersToModify.size());
		try {
			TransactionUtils.writing(membersToModify, new Runnable() {
				@Override
				public void run() {
					for (CommonMember member : membersToModify) {
						member.setExpanded(expanded);
						monitor.worked(1);
					}
				}
			});
		} finally {
			monitor.done();
		}
	}
	
	@Override
	protected void execute() throws Throwable {
		execute(new NullProgressMonitor());
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(membersToModify, new Runnable() {
			@Override
			public void run() {
				for (CommonMember member : membersToModify) {
					member.setExpanded(!expanded);
				}
			}
		});
	}

	@Override
	public String toString() {
		return "Set all expanded to be " + expanded + " affecting " + membersToModify.size() + " elements.";
	}
	
}
