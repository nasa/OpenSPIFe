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
package gov.nasa.arc.spife.rcp.profiles;

import gov.nasa.arc.spife.rcp.Activator;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.AutoCollectionMap;
import gov.nasa.ensemble.common.collections.AutoListMap;
import gov.nasa.ensemble.common.collections.AutoSetMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoader;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoadingException;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;
import gov.nasa.ensemble.emf.resource.ProgressMonitorInputStream;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.measure.quantity.Duration;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.jscience.physics.amount.Amount;

public class ProfileImportOperation extends AbstractTransactionUndoableOperation {

	private static final String PROFILE_IMPORT_OPERATION_ELEMENT_NAME = "ProfileImportOperation";

	private static final String MARKER_FIELD = EnsembleProperties.getProperty("lass.integrate.ad.special.field.name", null);

	private static final Map<String, Set<EActivityDef>> PROFILE_EVENT_DEFS = new AutoSetMap<String, EActivityDef>(String.class);
	private static final Pattern PUNCT = Pattern.compile("\\p{Punct}");
	private IContainer profilesFolder;

	private List<Profile> importedProfiles;
	private List<Profile> importedOverwrittenProfiles;
	private List<Profile> importedNewProfiles;
	private boolean isRedoFlag = false;

	private List<File> sourceFiles;
	private boolean maintainExistingData = true;
	private List<IStatus> errorTable = new ArrayList<IStatus>(1);

	private Fixes fixes;
	private Date start;
	private Date end;
	
	private EPlan plan;
	private EditingDomain domain;
	private ResourceSet resourceSet;

	private IStatus status;

	static {
		if (MARKER_FIELD != null) {
			List<EActivityDef> defs = ActivityDictionary.getInstance().getActivityDefs();
			for (EActivityDef def : defs) {
				EStructuralFeature feature = def.getEStructuralFeature(MARKER_FIELD);
				if (feature != null) {
					EClassifier eType = feature.getEType();
					if ((eType == EcorePackage.Literals.EBOOLEAN) || (eType == EcorePackage.Literals.EBOOLEAN_OBJECT)) {
						Boolean value = DictionaryUtil.getDefaultValue(def, feature);
						if ((value != null) && value.booleanValue()) {
							List<EStateRequirement> stateRequirements = def.getStateRequirements();
							for (EStateRequirement requirement : stateRequirements) {
								String profileName = requirement.getName();
								PROFILE_EVENT_DEFS.get(profileName).add(def);
							}
							if (stateRequirements.isEmpty()) {
								LogUtil.warn("Activity Def " + def.getName() + " marked with " + MARKER_FIELD + " but has no state requirements.");
							}
							if (stateRequirements.size() > 1) {
								LogUtil.warn("Activity Def " + def.getName() + " with more than one state requirement is unlikely to be auto-created/updated to satisfy all requirements.");
							}
						}
					}
				}
			}
		}
	}

	private static String fixPunctuation(String s) {
		if (s == null || s.equals(""))
			return ProfileUtil.UNCATEGORIZED;
		return PUNCT.matcher(s).replaceAll("_");
	}

	/**
	 * Get the state requirement from the activity definition with the provided
	 * name
	 * 
	 * @param def
	 * @param name
	 * @return
	 */
	private static EStateRequirement getStateRequirement(EActivityDef def, String name) {
		List<EStateRequirement> stateRequirements = def.getStateRequirements();
		for (EStateRequirement stateRequirement : stateRequirements) {
			String stateRequirementName = stateRequirement.getName();
			if (CommonUtils.equals(stateRequirementName, name)) {
				return stateRequirement;
			}
		}
		return null;
	}

	private static boolean satisfiesRequirement(EStateRequirement stateRequirement, Object value) {
		String requiredState = stateRequirement.getRequiredState();
		if (requiredState != null) {
			return CommonUtils.equals(value, requiredState);
		}
		List<String> allowedStates = stateRequirement.getAllowedStates();
		if (allowedStates != null) {
			return (allowedStates.contains(value));
		}
		String disallowedState = stateRequirement.getDisallowedState();
		if (disallowedState != null) {
			return !CommonUtils.equals(value, disallowedState);
		}
		// trivial or null requirements are interpreted as satisfied
		return true;
	}

	public ProfileImportOperation(EPlan plan) {
		super("Profile Import");
		setPlan(plan);
	}

	/**
	 * Returns the status of the export operation. If there were any errors, the
	 * result is a status object containing individual status objects for each
	 * error. If there were no errors, the result is a status object with error
	 * code <code>OK</code>.
	 * 
	 * @return the status
	 */
	public IStatus getStatus() {
		return status;
	}

	public void importProfiles(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Importing Profiles", 100);
			//if (profiles != null) {
			if(importedProfiles != null) {
				loadProfiles(new SubProgressMonitor(monitor, 90));
			}
//			profilesFolder.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 10));
		} catch (InterruptedException e) {
			LogUtil.error(e);
//		} catch (CoreException e) {
//			LogUtil.error(e);
		} finally {
			monitor.done();
		}
	}

	private void setProfilesFolder(IContainer profilesFolder) {
		this.profilesFolder = profilesFolder;
	}

	private void setPlan(EPlan plan) {
		this.plan = plan;
		if (plan != null) {
			this.domain = AdapterFactoryEditingDomain.getEditingDomainFor(plan);
			this.resourceSet = domain.getResourceSet();
			if (plan.eResource() != null) {
				IFile file = EMFUtils.getFile(plan.eResource());
				IContainer parent = file.getParent();
				if (parent != null) {
					setProfilesFolder(parent.getFolder(new Path("Conditions")));
				}
			}
		}
	}

	public void setSourceFile(List<File> sourceFileList) {
		this.sourceFiles = sourceFileList;
	}
	
	public void setMaintainExistingData(boolean maintainExistingData) {
		this.maintainExistingData = maintainExistingData;
	}

	@Override
	public String toString() {
		if (sourceFiles != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("ProfileImportOperation: ");
			for(File file : sourceFiles) {
				sb.append(file.getPath()).append(" ");
			}
			return sb.toString().trim();
		}
		else
			return PROFILE_IMPORT_OPERATION_ELEMENT_NAME;
	}

	@Override
	protected void dispose(UndoableState state) {
		// Do nothing
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {
		IStatus operationStatus = super.execute(monitor, info);
		IStatus[] errors = errorTable.toArray(new IStatus[errorTable.size()]);
		if (errors.length > 0) {
			MultiStatus status = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, errors, "Problems were encountered during import:", null);
			status.add(operationStatus);
			this.status = status;
			return status;
		}
		this.status = operationStatus;
		return operationStatus;
	}
	
	@Override
	protected void execute() throws ProfileLoadingException, IOException {
		execute(new NullProgressMonitor());
	}

	@Override
	protected void redo() throws Throwable {
		isRedoFlag = true;
		importProfiles(new NullProgressMonitor());
		// finished redo
		isRedoFlag = false;
	}

	@Override
	protected void execute(IProgressMonitor monitor) throws ProfileLoadingException, IOException {
		importedProfiles = new ArrayList<Profile>();
		importedOverwrittenProfiles = new ArrayList<Profile>();
		importedNewProfiles = new ArrayList<Profile>();
		monitor.beginTask("Importing profiles", 100);
		ProfileLoader loader = null;

		for(File file : sourceFiles) {
			InputStream stream = ProgressMonitorInputStream.openFileProgressMonitorInputStream(file, monitor);
			loader = new ProfileLoader(stream);
			Collection<? extends Profile> moreProfiles = loader.readProfiles();
			importedProfiles.addAll(moreProfiles);
			loader.ensureDataLoaded();
		}
		importProfiles(monitor);
	}
	
	@Override
	protected void undo() throws Throwable {
		// to get to before state:
		// if profile was overwritten it's in outgoingProfiles AND profiles
		// if so it should OVERWRITE the NEW profile in memory
		// if it's in profiles but NOT outgoingProfiles it should be REMOVED
		// all other profiles shouldn't be touched.
		
		Map<String, Map<String, Profile>> profilesByType = ProfileUtil.getProfilesByCategory(importedProfiles);
		final Map<String, Profile> profilesById = ProfileUtil.getProfilesById(importedProfiles);
		final Map<String, Profile> overwrittenProfileById = ProfileUtil.getProfilesById(importedOverwrittenProfiles);
		for (String type : profilesByType.keySet()) {
			String filename = fixPunctuation(type);
			URI uri = EMFUtils.getURI(profilesFolder).appendSegment(URI.encodeSegment(filename + ".profile", false));
			final Resource resource = getResource(uri);
			resource.unload();
			TransactionUtils.writing(resource, new Runnable() {
				@Override
				public void run() {
					for (ListIterator<EObject> it = resource.getContents().listIterator(); it.hasNext();) {
						EObject resourceNode = it.next();
						if (!(resourceNode instanceof Profile))
							continue;
						Profile profile = (Profile) resourceNode;
						String profileId = profile.getId();
						// in undo: profiles which went out come back
						if (overwrittenProfileById.containsKey(profileId)) {
							// The profile that already existed before the import, it will be reverted back to it's previous data.
							Profile originalProfile = overwrittenProfileById.get(profileId);
							transferProfileAttributes(profile, originalProfile, maintainExistingData);
							profile.setDataPoints(originalProfile.getDataPoints());
						} else if (profilesById.containsKey(profileId)) {
							// If the imported brand new profile is found, delete it from the resource.
							profile.setDataPoints(Collections.EMPTY_LIST);
							it.remove();
						}
					}
					// Notify Profiles View: Remove all brand new profiles that were imported.
					plan.eNotify(new ENotificationImpl((InternalEObject)plan, Notification.REMOVE_MANY, ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES, importedNewProfiles, null));
				}
			});
		}
		fixes.undo();
	}
	
	private Resource getResource(URI uri) {
		Resource resource = resourceSet.getResource(uri, false);
		if (resource == null) {
			resource = resourceSet.createResource(uri);
		}
		return resource;
	}

	/**
	 * Create the activities within the plan. Makes a new activity group for
	 * each type of activity that is created.
	 * 
	 * @param plan
	 * @param activityExtents
	 */

	//
	private List<EActivity> createActivities(EActivityDef def, List<TemporalExtent> extents) {
		List<EActivity> activities = new ArrayList<EActivity>();
		for (TemporalExtent extent : extents) {
			EActivity activity = PlanFactory.getInstance().createActivity(def);
			activity.getMember(TemporalMember.class).setExtent(extent);
			activities.add(activity);
		}
		return activities;
	}

	/**
	 * Return a map of the temporal extents for activities that are required by
	 * the activity definitions for this profile.
	 * 
	 * @param profile
	 * @param stateRequirement
	 * @return
	 */
	private List<TemporalExtent> getActivityExtents(Profile profile, EStateRequirement stateRequirement) {
		List<TemporalExtent> activityExtents = new ArrayList<TemporalExtent>();
		EList<DataPoint> dataPoints = profile.getDataPoints();
		for (DataPoint dataPoint : dataPoints) {
			Object value = dataPoint.getValue();
			if (satisfiesRequirement(stateRequirement, value)) {
				Date date = dataPoint.getDate();
				activityExtents.add(new TemporalExtent(date, date));
			}
		}
		return activityExtents;
	}

	/**
	 * For each kind of definition, create a list of the activities in the plan
	 * with that definition, in temporal order.
	 * 
	 * @param plan2
	 * @param oldProfile
	 * @return
	 */
	private Map<EActivityDef, TreeSet<EActivity>> getExistingActivities(final Set<EActivityDef> defs) {
		final Map<EActivityDef, TreeSet<EActivity>> result = new ExistingActivityMap(EActivityDef.class);
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EActivity) {
					EObject data = element.getData();
					if (data != null) {
						EClass def = data.eClass();
						if (defs.contains(def)) {
							EActivity activity = (EActivity) element;
							result.get(def).add(activity);
						}
					}
				}
			}
		}.visitAll(plan);
		return result;
	}

	/**
	 * Returns the group that these activities belong to. (actually just the
	 * first group)
	 * 
	 * @param activities
	 * @return
	 */
	private EActivityGroup getGroup(TreeSet<EActivity> activities) {
		for (EActivity activity : activities) {
			if (activity.getParent() instanceof EActivityGroup) {
				return (EActivityGroup) activity.getParent();
			}
		}
		return null;
	}

	/**
	 * Loads profiles in to memory rather than saving them to disk, the other
	 * method should go away once we've sorted out where saving is happening.
	 * 
	 * @param monitor
	 * @param type
	 * @param profilesForType
	 * @throws InterruptedException
	 */
	private void loadProfiles(IProgressMonitor monitor) throws InterruptedException {

		fixes = new Fixes(plan);

		Map<String, Map<String, Profile>> profilesByType = ProfileUtil.getProfilesByCategory(importedProfiles);
		for (Map.Entry<String, Map<String, Profile>> entry : profilesByType.entrySet()) {
			String type = entry.getKey();
			Map<String, Profile> newProfiles = entry.getValue();
			updateResourceSetForType(type, newProfiles);
		}
		fixes.execute();
	}

	/**
	 * Move old activities to new extents where appropriate. Also modifies the
	 * newExtents parameter to supply only extents that aren't covered by moved
	 * activities.
	 * 
	 * @param newExtents
	 * @param activities
	 * @return
	 */
	private void match(List<TemporalExtent> newExtents, TreeSet<EActivity> activities) {
		Iterator<TemporalExtent> newIterator = newExtents.iterator();
		Iterator<EActivity> activityIterator = activities.iterator();
		LinkedHashSet<TemporalExtent> usedExtents = new LinkedHashSet<TemporalExtent>();
		// the following three are loop variables
		TemporalExtent priorExtent = null; // points to null, or to the prior
											// extent
		TemporalExtent nextExtent = newIterator.next();
		EActivity activity = activityIterator.next();
		while (true) {
			final Date nextStart = nextExtent.getStart();
			final Date activityStart = activity.getMember(TemporalMember.class).getStartTime();
			if (nextStart.before(activityStart)) {
				// there are no more activities prior to the new start time,
				// so update the prior start and get the next extent
				priorExtent = nextExtent;
				if (!newIterator.hasNext()) {
					break;
				}
				nextExtent = newIterator.next();
				continue;
			}
			if (nextStart.equals(activityStart)) {
				// matches the provided value exactly, mark that extent as used,
				// update the prior start, and get both the next extent and
				// activity
				usedExtents.add(nextExtent);
				priorExtent = nextExtent;
				if (!newIterator.hasNext() || !activityIterator.hasNext()) {
					break;
				}
				nextExtent = newIterator.next();
				activity = activityIterator.next();
				continue;
			}
			if (priorExtent == null) {
				// don't move activities before the profile
				if (!activityIterator.hasNext()) {
					break;
				}
				activity = activityIterator.next();
				continue;
			}
			// look at the activity and determine which way it goes
			Amount<Duration> toPrior = DateUtils.subtract(activityStart, priorExtent.getStart());
			Amount<Duration> toNext = DateUtils.subtract(nextStart, activityStart);
			TemporalExtent newExtent = (toPrior.isLessThan(toNext) ? priorExtent : nextExtent);
			fixes.move(activity, newExtent);
			usedExtents.add(newExtent);
			if (!activityIterator.hasNext()) {
				break;
			}
			activity = activityIterator.next();
		}
		newExtents.removeAll(usedExtents);
	}

	/**
	 * Overwrite anything on the existing profile with newer data, if
	 * !maintainExistingData then delete things on the old profile
	 * 
	 * @param oldProfile
	 * @param newProfile
	 */
	private static void transferProfileAttributes(Profile oldProfile, Profile newProfile, boolean maintainExistingData) {
		if (!maintainExistingData) {
			oldProfile.getAttributes().clear();
		}
		oldProfile.getAttributes().map().putAll(newProfile.getAttributes().map());
		if (!maintainExistingData) {
			oldProfile.setId(newProfile.getId());
			oldProfile.setName(newProfile.getName());
			oldProfile.setCategory(newProfile.getCategory());
			oldProfile.setDataType(newProfile.getDataType());
			oldProfile.setUnits(newProfile.getUnits());
			if (newProfile.getUnits() == null) {
				oldProfile.setUnits(null);
			}
		}
	}

	private void updateAttachedEvents(EPlan plan, EActivityDef def, Profile profile, TreeSet<EActivity> existingActivities) {
		String id = profile.getId();
		EStateRequirement stateRequirement = getStateRequirement(def, id);
		if (stateRequirement == null) {
			LogUtil.warn("state requirement was unexpectedly null for " + def + " and " + id);
			return;
		}
		List<TemporalExtent> newExtents = getActivityExtents(profile, stateRequirement);
		if (!newExtents.isEmpty() && !existingActivities.isEmpty()) {
			match(newExtents, existingActivities);
		}
		if (!newExtents.isEmpty()) { // NOTE: need another check because match
										// may have removed extents that existed
										// earlier
			List<EActivity> newActivities = createActivities(def, newExtents);
			EActivityGroup group = getGroup(existingActivities);
			if (group != null) {
				fixes.add(group, newActivities);
			} else {
				fixes.add(def, newActivities);
			}
		}
	}

	/**
	 * shift events in the plan
	 * 
	 * @param plan
	 * @param oldProfile
	 * @param newProfile
	 */
	private void updateAttachedEvents(Profile profile) {
		String id = profile.getId();
		if (PROFILE_EVENT_DEFS.containsKey(id)) { // use contains key to avoid
													// creating a set where not
													// necessary
			Set<EActivityDef> defs = PROFILE_EVENT_DEFS.get(id);
			Map<EActivityDef, TreeSet<EActivity>> existingActivities = getExistingActivities(defs);
			for (final EActivityDef def : defs) {
				TreeSet<EActivity> activities = existingActivities.get(def);
				updateAttachedEvents(plan, def, profile, activities);
			}
		}
	}

	private void updateResourceSetForType(final String type, final Map<String, Profile> newProfiles) {
		String filename = fixPunctuation(type);
		URI uri = EMFUtils.getURI(profilesFolder).appendSegment(URI.encodeSegment(filename + ".profile", false));
		final Resource resource = getResource(uri);
		TransactionUtils.writing(resource, new Runnable() {
			@Override
			public void run() {
				List<Profile> profilesToUpdate = new ArrayList<Profile>(newProfiles.values());
				EList<EObject> contents = resource.getContents();
				for (EObject resourceNode : contents) {
					if (!(resourceNode instanceof Profile))
						continue;
					Profile existingProfile = (Profile) resourceNode;
					String profileId = existingProfile.getId();
					Profile newProfile = newProfiles.remove(profileId);
					// When the profile is null, it means the profile with the profile ID never existed before.
					// If it is NOT null, the profile exists and we want to overwrite it with the new imported profile.
					if (newProfile != null) {
						if(!isRedoFlag) {
							importedOverwrittenProfiles.add(EcoreUtil.copy(existingProfile));
						}
						transferProfileAttributes(existingProfile, newProfile, maintainExistingData);
						if (maintainExistingData) {
							ProfileUtil.coverageMerge(start, end, newProfile, existingProfile);
						} else {
							existingProfile.setDataPoints(newProfile.getDataPoints());
						}
					}
				}
				// The rest of the profiles are brand new profile IDs that did not exists before.
				if(!isRedoFlag) {
					importedNewProfiles.addAll(newProfiles.values());
				}
				plan.eNotify(new ENotificationImpl((InternalEObject)plan, Notification.ADD_MANY, ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES, null, importedNewProfiles));
				for (Profile profile : importedNewProfiles) {
					ProfileUtil.trim(start, end, profile);
					contents.add(profile);
				}
				for (Profile profile : profilesToUpdate) {
					updateAttachedEvents(profile);
				}
			}
		});
	}

	private static final class ExistingActivityMap extends AutoCollectionMap<EActivityDef, TreeSet<EActivity>> implements Comparator<EActivity> {

		private ExistingActivityMap(Class<EActivityDef> keyClass) {
			super(keyClass);
		}

		@Override
		public int compare(EActivity o1, EActivity o2) {
			TemporalMember member1 = o1.getMember(TemporalMember.class);
			TemporalMember member2 = o2.getMember(TemporalMember.class);
			Date start1 = member1.getStartTime();
			Date start2 = member2.getStartTime();
			return CommonUtils.compare(start1, start2);
		}

		@Override
		protected TreeSet<EActivity> createCollection() {
			return new TreeSet<EActivity>(this);
		}

	}

	private static class Fixes {
		private final EPlan plan;
		private final TemporalExtentsCache temporalExtentsCache;
		private final Map<EActivity, TemporalExtent> changedTimes = new LinkedHashMap<EActivity, TemporalExtent>();
		private final Map<EActivityGroup, List<EActivity>> activitiesToAddToExistingGroup = new AutoListMap<EActivityGroup, EActivity>(EActivityGroup.class);
		private final Map<EActivityDef, List<EActivity>> activitiesToAddToNewGroup = new AutoListMap<EActivityDef, EActivity>(EActivityDef.class);
		private final List<EActivityGroup> newGroups = new ArrayList<EActivityGroup>();

		public Fixes(EPlan plan) {
			this.plan = plan;
			this.temporalExtentsCache = new TemporalExtentsCache(plan);
		}

		public void add(EActivityDef def, List<EActivity> children) {
			activitiesToAddToNewGroup.get(def).addAll(children);
		}

		public void add(EActivityGroup parent, List<EActivity> children) {
			activitiesToAddToExistingGroup.get(parent).addAll(children);
		}

		public void execute() {
			for (Map.Entry<EActivityDef, List<EActivity>> entry : activitiesToAddToNewGroup.entrySet()) {
				EActivityDef def = entry.getKey();
				List<EActivity> activities = entry.getValue();
				EActivityGroup group = PlanFactory.getInstance().createActivityGroup(plan);
				group.setName(def.getName());
				group.getChildren().addAll(activities);
				newGroups.add(group);
			}
			TransactionUtils.writing(plan, new Runnable() {
				@Override
				public void run() {
					TemporalUtils.setExtents(changedTimes);
					for (Map.Entry<EActivityGroup, List<EActivity>> entry : activitiesToAddToExistingGroup.entrySet()) {
						EActivityGroup parent = entry.getKey();
						List<EActivity> children = entry.getValue();
						parent.getChildren().addAll(children);
					}
					plan.getChildren().addAll(newGroups);
				}
			});
		}

		public void move(EActivity activity, TemporalExtent extent) {
			changedTimes.put(activity, extent);
		}

		public void undo() {
			TransactionUtils.writing(plan, new Runnable() {
				@Override
				public void run() {
					TemporalUtils.resetExtents(changedTimes.keySet(), temporalExtentsCache);
					for (Map.Entry<EActivityGroup, List<EActivity>> entry : activitiesToAddToExistingGroup.entrySet()) {
						EActivityGroup parent = entry.getKey();
						List<EActivity> children = entry.getValue();
						parent.getChildren().removeAll(children);
					}
					plan.getChildren().removeAll(newGroups);
				}
			});
		}
	}
}
