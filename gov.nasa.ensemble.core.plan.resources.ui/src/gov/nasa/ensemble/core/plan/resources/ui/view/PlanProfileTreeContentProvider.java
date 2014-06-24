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
package gov.nasa.ensemble.core.plan.resources.ui.view;

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class PlanProfileTreeContentProvider implements ITreeContentProvider {

	private TreeViewer viewer = null;
	private AdapterListener listener = new AdapterListener();
	private ResourceSetListener resourceSetListener = new MyResourceSetListener();
	private Map<EClass, List<ObjectDef>> objectDefsByType = new LinkedHashMap<EClass, List<ObjectDef>>(); 
	private Map<String, List<Profile>> profilesByCategory = new LinkedHashMap<String, List<Profile>>();
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof EPlan) {
			return profilesByCategory.keySet().toArray(new String[0]);
		} else if (parentElement instanceof String) {
			return profilesByCategory.get(parentElement).toArray(new Profile[0]);
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Profile) {
			return getCategory((Profile) element);
		} else if (element instanceof String) {
			return viewer.getInput();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof EPlan) {
			EPlan plan = (EPlan) element;
			ResourceProfileMember member = getResourceProfileMember(plan);
			EList<Profile<?>> profiles = member.getResourceProfiles();
			return !profiles.isEmpty();
		}
		return element instanceof String;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EPlan) {
			return getChildren(inputElement);
		}
		return new Object[0];
	}

	@Override
	public void dispose() {
		this.viewer = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		if (oldInput instanceof EPlan) {
			getResourceProfileMember((EPlan) oldInput).eAdapters().remove(listener);
			TransactionUtils.getDomain(oldInput).removeResourceSetListener(resourceSetListener);
		}
		profilesByCategory.clear();
		objectDefsByType.clear();
		if (newInput instanceof EPlan) {
			EPlan plan = (EPlan) newInput;
			ResourceProfileMember member = getResourceProfileMember(plan);
			member.eAdapters().add(listener);
			TransactionUtils.getDomain(newInput).addResourceSetListener(resourceSetListener);
			for (Profile profile : member.getResourceProfiles()) {
				addProfileToCategory(profile);
			}
		}
	}

	private ResourceProfileMember getResourceProfileMember(EPlan plan) {
		return WrapperUtils.getMember(plan, ResourceProfileMember.class);
	}

	private String getCategory(Profile profile) {
		if (profile == null) {
			return "Deleted";
		}
		String category = profile.getCategory();
		if (category == null) {
			return "Uncategorized";
		}
		return category;
	}

	private void addProfileToCategory(Profile profile) {
		String category = getCategory(profile);
		List<Profile> profiles = profilesByCategory.get(category);
		if (profiles == null) {
			profiles = new ArrayList<Profile>();
			profilesByCategory.put(category, profiles);
		}
		// Fix for SPF-8221, consider using Sets to get this behavior automatically instead.
		if (!profiles.contains(profile))
			profiles.add(profile);
	}

	/**
	 * Removes the profile from the Profiles View. 
	 * Since the profile instances may be different(in memory) than the one 
	 * stored in the tree, we want to look up the profile ID and if there is 
	 * a match then we will remove it.
	 * 
	 * @param profile
	 */
	private void removeProfileFromCategory(Profile profile) {
		String category = getCategory(profile);
		List<Profile> list = profilesByCategory.get(category);
		if (list != null) {
			for(Profile p : list) {
				if(p.getId().equals(profile.getId())) {
					list.remove(p);
					break;
				}
			}
			if (list.isEmpty()) {
				profilesByCategory.remove(category);
			}
		}
	}

	private class AdapterListener extends AdapterImpl {

		@Override
		public void notifyChanged(Notification notification) {
			List<Profile> adds = new ArrayList<Profile>();
			List<Profile> removes = new ArrayList<Profile>();
			boolean profilesChanged = ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == notification.getFeature();
			
			if (profilesChanged) {
				switch (notification.getEventType()) {
				case Notification.ADD:
					adds.add((Profile) notification.getNewValue());
					break;
				case Notification.ADD_MANY:
					adds.addAll((Collection<? extends Profile>) notification.getNewValue());
					break;
				case Notification.REMOVE:
					removes.add((Profile) notification.getOldValue());
					break;
				case Notification.REMOVE_MANY:
					removes.addAll((Collection<? extends Profile>) notification.getOldValue());
					break;
				}
			}
			
			boolean changed = !adds.isEmpty() || !removes.isEmpty();
			if (changed) {
				for (Profile profile : adds) 	addProfileToCategory(profile);
				for (Profile profile : removes) removeProfileFromCategory(profile);
				WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
					@Override
					public void run() {
						viewer.refresh();
					}
				});
			}
		}
		
	}
	
	private class MyResourceSetListener implements ResourceSetListener {

		@Override public NotificationFilter getFilter() {return null;}
		@Override public Command transactionAboutToCommit(ResourceSetChangeEvent event) {return null;}

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			for (Notification notification : event.getNotifications()) {
				listener.notifyChanged(notification);
			}
		}

		@Override public boolean isAggregatePrecommitListener() {return false;}
		@Override public boolean isPrecommitOnly() {return false;}
		@Override public boolean isPostcommitOnly() {return false;}

	}

	
}
