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
package gov.nasa.ensemble.core.jscience.synchronizer;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.SafeAdapter;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Handle resource changes which may be addition or removal of Profiles from the EMF Resource contents
 */
final class ProfileSynchronizerEMFResourceContentsAdapter extends SafeAdapter {
	
	private ProfileSynchronizer synchronizer;
	
	public ProfileSynchronizerEMFResourceContentsAdapter(ProfileSynchronizer synchronizer) {
		this.synchronizer = synchronizer;
	}
	public void dispose() {
		this.synchronizer = null;
	}
	
	@Override
	protected void handleNotify(Notification notification) {
		for (Profile profile : EMFUtils.getRemovedObjects(notification, Profile.class)) {
			forgetProfile(profile);
		}
		for (Profile profile : EMFUtils.getAddedObjects(notification, Profile.class)) {
			rememberProfile(profile);
		}
	}
	
	public void startWatching(Resource resource) {
		resource.eAdapters().add(this);
		EList<EObject> contents = resource.getContents();
		for (EObject content : contents) {
			if (content instanceof Profile) {
				rememberProfile((Profile)content);
			}
		}
	}
	
	public void stopWatching(Resource resource) {
		resource.eAdapters().remove(this);
		EList<EObject> contents = resource.getContents();
		for (EObject content : contents) {
			if (content instanceof Profile) {
				forgetProfile((Profile)content);
			}
		}
	}
	
	private void rememberProfile(final Profile profile) {
		synchronizer.rememberProfile(profile);
		try {
			Resource resource = profile.eResource();
			if (resource != null) {
				final Boolean valid = synchronizer.isValid(resource);
				if (valid != null) {
					TransactionUtils.writeIfNecessary(profile, new Runnable() {
						@Override
						public void run() {
							profile.setValid(valid);
						}
					});
				}
			}
		} catch (Exception e) {
			LogUtil.error("failed to initialize validity", e);
		}
	}
	
	private void forgetProfile(Profile profile) {
		synchronizer.forgetProfile(profile);
	}
	
}
