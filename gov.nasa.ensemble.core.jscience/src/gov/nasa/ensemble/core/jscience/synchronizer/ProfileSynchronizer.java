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
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * ProfileSynchronizer is responsible for updating in-memory profiles when
 * their external (on disk) representations are updated.
 * 
 * It tracks the in-memory profiles by adding adapters to the resource set
 * and to each resource's contents.  These are implemented by the inner
 * classes.
 * 
 * It updates the in-memory profiles when it observes external changes
 * via a resource change listener.
 * 
 * @author abachman
 *
 */

public class ProfileSynchronizer implements MissionExtendable {

	public static ProfileSynchronizer createInstance(IProject project, ResourceSet resourceSet) {
		ProfileSynchronizer profileSynchronizer = null;
		try {
			profileSynchronizer = MissionExtender.construct(ProfileSynchronizer.class, project, resourceSet);
		} catch (ConstructionException e) {
			LogUtil.error("constructing mission extendable ProfileSynchronizer", e);
			profileSynchronizer = new ProfileSynchronizer(project, resourceSet);
		}
		return profileSynchronizer;
	}
	
	// the project for which profiles are to be synchronized
	final protected IProject project;
	
	// this watches the in-memory objects for changes that may affect what profiles the profile synchronizer should track
	private final ProfileSynchronizerEMFResourceSetContentsAdapter emfResourceSetContentsAdapter;
	
	// this watches the project for changes that the profile synchronizer cares about
	private final ProfileSynchronizerWorkspaceListener workspaceListener;

	// this is a mechanism for observing when profiles get updated by the synchronizer
	final ProfileSynchronizerDispatch dispatch = new ProfileSynchronizerDispatch();

	// this maps the profile ids to the in-memory profile
	final Map<String, Profile> profiles = new ConcurrentHashMap<String, Profile>();
	
	/**
	 * Constructs a new profile synchronizer and adds a listener to
	 * the resourceSet and resources contained therein
	 * @param resourceSet to hook into
	 */
	protected ProfileSynchronizer(IProject project, ResourceSet resourceSet) {
		super();
		this.project = project;
		this.emfResourceSetContentsAdapter = new ProfileSynchronizerEMFResourceSetContentsAdapter(resourceSet, this);
		this.workspaceListener = new ProfileSynchronizerWorkspaceListener(project, this, TransactionUtils.getDomain(resourceSet));
	}
	
	/**
	 * Disposes and releases listeners to resourceSet and resource contained therein
	 */
	public void dispose() {
		this.emfResourceSetContentsAdapter.dispose();
		this.workspaceListener.dispose();
		profiles.clear(); // should be empty by this point anyway, but make sure
	}

	public Set<IResource> getIgnorableResources() {
		return ignorableResources;
	}

	private final Set<IResource> ignorableResources = new HashSet<IResource>();

	/**
	 * Track this profile.
	 * 
	 * @param profile
	 */
	/* package */ void rememberProfile(Profile profile) {
		String id = profile.getId();
		profiles.put(id, profile);
	}

	/**
	 * Stop tracking this profile.
	 * 
	 * @param profile
	 */
	/* package */ void forgetProfile(Profile profile) {
		String id = profile.getId();
		profiles.remove(id);
	}

	/**
	 * Find the in memory profile with this profile id, if it exists.
	 * 
	 * @param profileId
	 */
	/* package */ Profile findInMemoryProfile(String profileId) {
		return profiles.get(profileId);
	}
	
	/*
	 * Listener methods
	 */
	
	public void addListener(ProfileSynchronizerListener listener) {
		dispatch.addListener(listener);
	}

	public void removeListener(ProfileSynchronizerListener listener) {
		dispatch.removeListener(listener);
	}
	
	/**
	 * Determines if the profiles contained in the resource are current invalid
	 * 
	 * @param file
	 *            resource change delta
	 * @return true if the profiles should marked as valid, null if neutral
	 */
	protected Boolean isValid(Resource file) {
		return null;
	}

	/**
	 * Determines if the profiles contained in the resource should be marked as invalid as a result of the event
	 * 
	 * @param delta resource change delta
	 * @return true if the profiles should now be marked as	valid as a result of the event, false if not, null if neutral */
	protected Boolean isValid(IResourceDelta delta) {
		return null;
	}

	/**
	 * Can be overriden to determine whether this delta is worth consideration or not
	 * 
	 * @param delta
	 * @param synchronizerProject
	 * @return
	 */
	protected boolean isImportant(IResourceDelta delta, IProject synchronizerProject) {
		IResource resource = delta.getResource();
		boolean isProfileContentType = isProfileContentType(resource);
		if (!isProfileContentType) {
			return false;
		}
		int flags = delta.getFlags();
		return resource != null
				&& !getIgnorableResources().contains(resource)
				&& IResourceDelta.DESCRIPTION != flags
				&& IResourceDelta.MARKERS != flags
				&& resource instanceof IFile
				&& resource.getProject().equals(synchronizerProject);
	}

	protected boolean isProfileContentType(IResource resource) {
		return ProfileUtil.isProfileContentType(resource);
	}

}
