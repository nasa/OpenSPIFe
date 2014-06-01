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

import gov.nasa.ensemble.common.collections.AutoListMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/* package */ class ProfileSynchronizerWorkspaceListener implements IResourceChangeListener {

	public static enum CHANGE_TYPE { REMOVE, VALID, CHANGE, INVALID, ADD }

	private final IProject project;
	private ProfileSynchronizer synchronizer;
	private TransactionalEditingDomain domain;
	private final Object disposeLock = new Object();

	public ProfileSynchronizerWorkspaceListener(IProject project, ProfileSynchronizer synchronizer, TransactionalEditingDomain transactionalEditingDomain) {
		this.project = project;
		this.synchronizer = synchronizer;
		this.domain = transactionalEditingDomain;
		int events = IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.POST_BUILD;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, events);
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		synchronized (disposeLock) {
			synchronizer = null;
			domain = null;
		}
	}

	/**
	 * Process the event into changes and kick off a job to handle those changes.
	 */
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		final Map<IResource, List<CHANGE_TYPE>> changes = new AutoListMap<IResource, CHANGE_TYPE>(IResource.class);
		resourceDeltaChanged(event.getDelta(), changes);
		int count = changes.size();
		if (count != 0) {
			final String message;
			if (count == 1) {
				IResource resourceName = changes.keySet().iterator().next();
				message = "Checking " + resourceName.getName() + " for profiles";
			} else {
				message = "Checking " + count + " resources for profiles";
			}
			WorkspaceJob job = new WorkspaceJob(message) {
				@Override
				@SuppressWarnings("unused")
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					synchronized (disposeLock) {
						if (synchronizer != null) {
							return handleResourceChanges(changes, message, monitor);
						}
					}
					return Status.OK_STATUS;
				}
			};
			// job.setSystem(true); // SPF-6488: Hide resource checking
			job.setRule(project);
			job.schedule();
		}
	}

	/**
	 * Recursively traverse the resource delta looking for changes.
	 * @param delta
	 * @param changes
	 */
	private void resourceDeltaChanged(IResourceDelta delta, Map<IResource, List<CHANGE_TYPE>> changes) {
		if (delta != null) {
			getUpdates(delta, changes);
			for (IResourceDelta child : delta.getAffectedChildren()) {
				resourceDeltaChanged(child, changes);
			}
		}
	}

	/**
	 * Look at this particular delta and accumulate the changes into the map. 
	 * 
	 * @param delta
	 * @param changes
	 */
	private void getUpdates(IResourceDelta delta, Map<IResource, List<CHANGE_TYPE>> changes) {
		boolean important = synchronizer.isImportant(delta, project);
		if (!important) {
			return;
		}
		IResource resource = delta.getResource();
		int kind = delta.getKind();
		switch (kind) {
		case IResourceDelta.CHANGED:
			Boolean valid = synchronizer.isValid(delta);
			if (valid == null) {
				changes.get(resource).add(CHANGE_TYPE.CHANGE);
			} else if (valid) {
				changes.get(resource).add(CHANGE_TYPE.VALID);
			} else {
				changes.get(resource).add(CHANGE_TYPE.INVALID);
			}
			break;
		case IResourceDelta.ADDED:
			changes.get(resource).add(CHANGE_TYPE.ADD);
			break;
		case IResourceDelta.REMOVED:
			changes.get(resource).add(CHANGE_TYPE.REMOVE);
			break;
		}
	}
	
	/**
	 * Handle the list of changes.
	 *  
	 * @param changes
	 * @param message
	 * @param monitor
	 * @return
	 */
	private final static int STEP = 10;
	private IStatus handleResourceChanges(Map<IResource, List<CHANGE_TYPE>> changes, String message, final IProgressMonitor monitor) {
		int count = 2 * changes.size();
		monitor.beginTask(message, STEP*count);
		try {
			// these are a list of profiles that went away externally 
			final List<Profile> externallyRemovedProfiles = new ArrayList<Profile>();
			
			// this is a list of resources that have profile data that needs to be loaded
			final List<IResource> externallyUpdatedResources = new ArrayList<IResource>();
			
			// this is a list of profile IDs that became invalid
			final List<String> externallyInvalidProfiles = new ArrayList<String>();
			
			// this is a list of profile IDs that became valid
			final List<String> externallyValidProfiles = new ArrayList<String>();
			
			for (Map.Entry<IResource, List<CHANGE_TYPE>> entry : changes.entrySet()) {
				IResource resource = entry.getKey();
				List<CHANGE_TYPE> list = entry.getValue();
				SubProgressMonitor changeMonitor = new SubProgressMonitor(monitor, STEP);
				Resource emfResource = ProfileUtil.getResource(resource, changeMonitor, false);
				long timeStamp = emfResource.getTimeStamp();
				long modificationStamp = resource.getLocalTimeStamp();
				if (timeStamp < modificationStamp) {
					continue;
				}
				List<Profile> relevantProfiles = getExternalProfiles(emfResource);
				if (relevantProfiles.isEmpty()) {
					monitor.worked(STEP); // won't show up in externallyUpdatedResources
					continue;
				}
				for (CHANGE_TYPE change : list) {
					switch (change) {
					case ADD:
					case CHANGE:
						externallyUpdatedResources.add(resource);
						break;
					case VALID:
						for (Profile profile : relevantProfiles) {
							String id = profile.getId();
							externallyInvalidProfiles.remove(id);
							externallyValidProfiles.add(id);
						}
						break;
					case INVALID:
						for (Profile profile : relevantProfiles) {
							String id = profile.getId();
							externallyValidProfiles.remove(id);
							externallyInvalidProfiles.add(id);
						}
						break;
					case REMOVE:
						for (Profile externalProfile : relevantProfiles) {
							String id = externalProfile.getId();
							Profile memoryProfile = synchronizer.findInMemoryProfile(id);
							if (memoryProfile != null) {
								externallyRemovedProfiles.add(memoryProfile);
							}
						}
						break;
					}
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
				}
			}
			final List<Profile> updatedProfiles = new ArrayList<Profile>();
			Map options = new HashMap();
			options.put("noCollaborationNotifications", Boolean.TRUE);
			TransactionUtils.checkedWriting(domain, new RunnableWithResult.Impl() {
				@Override
				public void run() {
					for (Profile profile : externallyRemovedProfiles) {
						profile.setDataPointsArray(new DataPoint[0]);
					}
					for (String id : externallyInvalidProfiles) {
						Profile profile = synchronizer.findInMemoryProfile(id);
						if (profile != null) {
							profile.setValid(false);
						}
					}
					for (String id : externallyValidProfiles) {
						Profile profile = synchronizer.findInMemoryProfile(id);
						if (profile != null) {
							profile.setValid(true);
						}
					}
					for (IResource iResource : externallyUpdatedResources) {
						Resource emfResource = ProfileUtil.getResource(iResource, new SubProgressMonitor(monitor, STEP), true);
						if (monitor.isCanceled()) {
							return;
						}
						Boolean valid = synchronizer.isValid(emfResource);
						List<Profile> externalProfiles = getExternalProfiles(emfResource);
						for (Profile externalProfile : externalProfiles) {
							if (!externalProfile.isExternalCondition()) {
								continue;
							}
							String id = externalProfile.getId();
							final Profile memoryProfile = synchronizer.findInMemoryProfile(id);
							if (memoryProfile != null) {
								memoryProfile.setDataPoints(externalProfile.getDataPoints());
								if (valid != null) {
									memoryProfile.setValid(valid);
								}
							}
						}
					}
				}
			}, options);
			synchronizer.dispatch.profilesRemoved(externallyRemovedProfiles);
			synchronizer.dispatch.profilesRemoved(updatedProfiles);
			synchronizer.dispatch.profilesAdded(updatedProfiles);
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
		} catch (Exception e) {
			LogUtil.error(e);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	/**
	 * Returns a list of the external profiles that are contained in the resource.
	 * Returns an empty list if passed a null resource.
	 * 
	 * @param emfResource 
	 * @return
	 */
	private List<Profile> getExternalProfiles(Resource emfResource) {
		if (emfResource == null || synchronizer == null) {
			return Collections.emptyList();
		}
		List<Profile> relevantProfiles = new ArrayList<Profile>();
		EList<EObject> contents = emfResource.getContents();
		for (EObject object : contents) {
			if (object instanceof Profile) {
				Profile externalProfile = (Profile) object;
				String id = externalProfile.getId();
				Profile memoryProfile = synchronizer.findInMemoryProfile(id);
				if (memoryProfile != null) {
					relevantProfiles.add(externalProfile);
				}
			}
		}
		contents.clear(); // disconnect all profiles from the resource
		return relevantProfiles;
	}

}
