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
package gov.nasa.arc.spife.core.plan.pear.view.internal;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;


public class AddProfileOperation extends AbstractProfileUndoableOperation {

	private Profile profile;
	private ProfileEnvelopeConstraint constraint;

	public AddProfileOperation(EPlan plan, Profile profile) {
		this(plan, profile, null);
	}

	public AddProfileOperation(EPlan plan, Profile profile, ProfileEnvelopeConstraint constraint) {
		super(plan, "Adding profiles");
		this.profile = profile;
		this.constraint = constraint;
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				// Set the constraint to the Plan.
				if(constraint != null) {
					plan.getMember(ProfileMember.class).getConstraints().add(constraint);
				}
				addProfile(profile);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				removeProfile(profile);
				if(constraint != null) {
					// Remove the constraint from the Plan.
					plan.getMember(ProfileMember.class).getConstraints().remove(constraint);
				}
			}
		});
	}
	
	@Override
	protected void redo() throws Throwable {
		execute();
	}

	@Override
	public String toString() {
		return getLabel();
	}

	@Override
	protected void dispose(UndoableState state) {
		this.plan = null;
		this.resourceSet = null;
		this.constraint = null;
	}

}
