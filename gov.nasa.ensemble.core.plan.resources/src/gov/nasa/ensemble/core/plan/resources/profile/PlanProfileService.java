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
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.synchronizer.ProfileSynchronizer;
import gov.nasa.ensemble.core.jscience.synchronizer.ProfileSynchronizerListener;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.PlanService;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.PlatformUI;

public class PlanProfileService extends PlanService {

	private ProfileSynchronizer synchronizer;
	private ProfileSynchronizerListener synchronizerListener = new Listener();
	
	public PlanProfileService(EPlan plan) {
		super(plan);
	}

	@Override
	public void activate() {
		super.activate();
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		IFile file = EMFUtils.getFile(getPlan().eResource());
		if (file != null) {
			IProject project = file.getProject();
			if (project != null) {
				ResourceSet resourceSet = getEditingDomain().getResourceSet();
				this.synchronizer = ProfileSynchronizer.createInstance(project, resourceSet);
				this.synchronizer.getIgnorableResources().add(file);
				this.synchronizer.addListener(synchronizerListener);
			}
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();
		if (this.synchronizer != null) {
			this.synchronizer.removeListener(synchronizerListener);
			this.synchronizer.dispose();
		}
	}

	private ResourceProfileMember getResourceProfileMember() {
		EPlan plan = getPlan();
		return WrapperUtils.getMember(plan, ResourceProfileMember.class);
	}
	
	private class Listener implements ProfileSynchronizerListener {

		@Override
		@SuppressWarnings("unchecked")
		public void profilesAdded(Collection<Profile> profiles) {
			final Collection<? extends Profile<?>> adding = new LinkedHashSet<Profile<?>>((Collection<? extends Profile<?>>) profiles);
			final EList<Profile<?>> memberProfiles = getResourceProfileMember().getResourceProfiles();
			adding.removeAll(memberProfiles);
			if (!adding.isEmpty()) {
				TransactionUtils.writing(getEditingDomain(), new Runnable() {
					@Override
					public void run() {
						memberProfiles.addAll(adding);
					}
				});
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public void profilesRemoved(final Collection<Profile> profiles) {
			TransactionUtils.writing(getEditingDomain(), new Runnable() {
				@Override
				public void run() {
					getResourceProfileMember().getResourceProfiles().removeAll(profiles);
				}
			});
		}
		
	}

}
