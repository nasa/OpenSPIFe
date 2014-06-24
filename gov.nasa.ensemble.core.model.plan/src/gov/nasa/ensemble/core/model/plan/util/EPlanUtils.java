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

 import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleOption;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class EPlanUtils {

	private static final XMLParserPoolImpl PARSER_POOL = new XMLParserPoolImpl();
	private static final List<PlanResourceSetContributor> PLAN_RESOURCE_CONTRIBUTORS = ClassRegistry.createInstances(PlanResourceSetContributor.class);
	private static final List<PlanElementSeverityProvider> SEVERITY_PROVIDERS = ClassRegistry.createInstances(PlanElementSeverityProvider.class);
	
	/**
	 * a list of EPlanElements contributing to a marker
	 */
	public static final String CULPRITS = "CULPRITS";
	public static final boolean MODEL_EVEN_TEMPLATES = EnsembleProperties.getBooleanPropertyValue("model.even.templates", false);

	/**
	 * This method will return the EPlan containing the
	 * supplied EObject, if it is contained by an EPlan.
	 * It will return null otherwise.
	 *
	 * @param object
	 * @return
	 */
	public static EPlan getPlan(EObject object) {
		EObject container = object;
		while (container != null) {
			if (container instanceof EPlan) {
				EPlan ePlan = (EPlan) container;
				return ePlan;
			}
			container = container.eContainer();
		}
		return null;
	}
	
	public static IFile getPlanFile(IProject project) {
		final List<IFile> planFiles = new ArrayList<IFile>();
		try {
			project.accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) {
					if (resource instanceof IFile 
							&& CommonUtils.equals("plan", resource.getFileExtension())
							&& !CommonUtils.equals("template.plan", resource.getName())) {
						planFiles.add((IFile) resource);
					}
					return true;
				}
			});
		} catch (CoreException e) {
			LogUtil.error(e);
		}
		if (planFiles.isEmpty()) {
			LogUtil.warn("Didn't fine any .plan files in project " + project.getName());
			return null;
		} else if (planFiles.size() > 1) {
			LogUtil.warn("Found more than one .plan file in project " + project.getName());
		} 
		return planFiles.get(0);
	}

	/**
	 * This method will return the EPlanElements contained by
	 * this EPlanElement.  This method will return null if
	 * supplied with null.
	 *
	 * @param planElement
	 */
	public static List<? extends EPlanChild> getChildren(EPlanElement planElement) {
		if (planElement == null) {
			return null;
		}
		return planElement.getChildren();
	}

	/**
	 * This method will return all of the top-level EActivitys
	 * contained by this EPlanElement.  Subactivities are not
	 * included.  This method will return null if supplied with
	 * null.
	 * @param elements
	 * @return
	 */
	public static List<EActivity> getActivities(EPlanElement planElement) {
		if (planElement == null) {
			return null;
		}
		if (planElement instanceof EActivity) {
			return Collections.emptyList();
		}
		List<EActivity> activities = new ArrayList<EActivity>();
		for (EPlanChild child : planElement.getChildren()) {
			if (child instanceof EActivity) {
				activities.add((EActivity)child);
			} else {
				activities.addAll(getActivities(child));
			}
		}
		return activities;
	}

	
	public static List<EPlanChild> getNonParentActivities(List<EPlanChild> planElements) {
		
		List<EPlanChild> results = new ArrayList<EPlanChild>();
		for (EPlanChild child : planElements) {
			results.addAll(getNonParentActivities(child));
		}
		return results;
	}
		
	/**
	 * This method will return all of the EPlanChilds
	 * contained by this EPlanElement that are not EPlanParents.
	 * This method will return null 
	 * if supplied with null.
	 * @param elements
	 * @return
	 */
	public static List<EPlanChild> getNonParentActivities(EPlanElement planElement) {
		if (planElement == null) {
			return null;
		}
		if (!(planElement instanceof EPlanParent)) {
			return Arrays.asList((EPlanChild)planElement);
		}
		List<EPlanChild> activities = new ArrayList<EPlanChild>();
		for (EPlanChild child : planElement.getChildren()) {
			if (child instanceof EPlanParent) {
				activities.addAll(getNonParentActivities(child));
			} else {
				activities.add((EActivity)child);
			}
		}
		return activities;
	}

	/**
	 * This method will return all of the EActivityGroups
	 * contained by this EPlanElement.  This method will return null 
	 * if supplied with null.
	 * @param elements
	 * @return
	 */
	public static List<EActivityGroup> getActivityGroups(EPlanElement planElement) {
		if (planElement == null) {
			return null;
		}
		if (planElement instanceof EActivity) {
			return Collections.emptyList();
		}
		List<EActivityGroup> groups = new ArrayList<EActivityGroup>();
		for (EPlanChild child : planElement.getChildren()) {
			if (child instanceof EActivityGroup) {
				groups.add((EActivityGroup)child);
			}
			if (child instanceof EPlanParent) {
				groups.addAll(getActivityGroups(child));
			}
		}
		return groups;
	}
	
	/**
	 * This method will return all of the EActivityGroups
	 * that are descendants of this EPlanElement.  This method will return null 
	 * if supplied with null.
	 * @param elements
	 * @return
	 */
	public static List<EPlanChild> getNestedPlanChildren(EPlanElement planElement) {
		if (planElement == null) {
			return null;
		}
		if (planElement instanceof EActivity) {
			return Collections.emptyList();
		}
		List<EPlanChild> children = new ArrayList<EPlanChild>();
		for (EPlanChild child : planElement.getChildren()) {
			children.add(child);
			if (child instanceof EPlanParent) {
				children.addAll(getNestedPlanChildren(child));
			}
		}
		return children;
	}
	
	/**
	 * This method will return all of the EActivity objects
	 * that are descendants of this EPlanElement.  This method will return null 
	 * if supplied with null.
	 * @param elements
	 * @return
	 */
	public static List<EActivity> getNestedActivities(EPlanElement planElement) {
		if (planElement == null) {
			return null;
		}
		if (planElement instanceof EActivity) {
			return Collections.singletonList((EActivity) planElement);
		}
		List<EActivity> children = new ArrayList<EActivity>();
		for (EPlanChild child : planElement.getChildren()) {
			children.addAll(getNestedActivities(child));
		}
		return children;
	}
	
	/**
	 * Return the subset of the objects argument such that if the subset contains a plan
	 * element, it does not also contain that plan element's parent.
	 * @param objects a collection of plan elements
	 * @return the subset of the objects argument such that if the subset contains a plan element,
	 * it does not also contain that plan element's parent
	 */
	public static Set<EPlanElement> removeContainedElements(Collection<? extends EPlanElement> objects) {
		Set<EPlanElement> result = new LinkedHashSet<EPlanElement>(objects);
		Iterator<EPlanElement> iterator = result.iterator();
		while (iterator.hasNext()) {
			EPlanElement element = iterator.next();
			EObject parent = element.eContainer();
			while (parent instanceof EPlanElement) {
				if (result.contains(parent)) {
					iterator.remove();
					break;
				}
				parent = parent.eContainer();
			}
		}
		return result;
	}

	/**
	 * Returns the set of elements contained by the supplied plan elements.
	 * The set will include the elements themselves, all their children, including any subactivities.
	 * 
	 * @param parents
	 * @return
	 */
	public static Set<EPlanElement> computeContainedElements(Collection<? extends EPlanElement> parents) {
		final Set<EPlanElement> result = new LinkedHashSet<EPlanElement>(parents);
		PlanVisitor activityVisitor = new PlanVisitor(true) {
			@Override
			protected void visit(EPlanElement element) {
				result.addAll(element.getChildren());
			}
		};
		activityVisitor.visitAll(parents);
		return result;
	}

	/**
	 * Returns the set of activities contained by the supplied plan elements.
	 * The set will include any elements that are themselves activities.
	 * It will not include any subactivities except those provided in the supplied plan elements.
	 * 
	 * @param parents
	 * @return
	 */
	public static final Collection<EActivity> computeContainedActivities(Collection<? extends EPlanElement> parents) {
		final Set<EActivity> result = new LinkedHashSet<EActivity>();
		PlanVisitor activityVisitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EActivity) {
					result.add((EActivity)element);
				}
			}
		};
		activityVisitor.visitAll(parents);
		return result;
	}
	
	/**
	 * This method return the EPlan corresponding to a
	 * ResourceSetChangeEvent, if any.
	 * It will return null otherwise.
	 *
	 * @param event
	 * @return
	 */
	public static EPlan getPlanNotifications(ResourceSetChangeEvent event) {
		for (Notification notification : event.getNotifications()) {
			EPlan plan = getPlan(notification);
			if (plan != null) {
				return plan;
			}
		}
		return null;
	}

	/**
	 * This method returns the EPlan corresponding to a
	 * notification, if any.
	 * It will return null otherwise.
	 * @param notification
	 */
	private static EPlan getPlan(Notification notification) {
		Object notifier = notification.getNotifier();
		return getPlanFromResourceSet(notifier);
	}

	public static EPlan getPlanFromResourceSet(Object notifier) {
		while (notifier instanceof EObject && !(notifier instanceof Resource)) {
			EObject object = (EObject) notifier;
			if (object instanceof EPlan  && !skipModelingTemplate(object)) {
				return (EPlan)notifier;
			}
			EObject container = object.eContainer();
			if (container != null) {
				notifier = container;
			} else {
				notifier = object.eResource();
			}
		}
		EPlan plan = null;
		if (notifier instanceof Resource) {
			Resource resource = (Resource) notifier;
			ResourceSet set = resource.getResourceSet();
			plan = getPlanFromResourceSet(set);
		}

		return plan;
	}

	public static boolean skipModelingTemplate(EObject object) {
		 return !MODEL_EVEN_TEMPLATES && isTemplatePlan(object);
	}

	public static EPlan getPlanFromResourceSet(ResourceSet resourceSet) {
		EPlan plan = null;
		// when first sharing a plan, there is a transfer from one resource
		// set to another, so there may be a moment in time when the
		// resource set is null
		if (resourceSet != null) {
			EList<Resource> siblings = resourceSet.getResources();
			for (Resource sibling : siblings) {
				if (sibling instanceof PlanResourceImpl) {
					EList<EObject> contents = sibling.getContents();
					for (EObject object : contents) {
						if (object instanceof EPlan && !isTemplatePlan(object)) {
							if (plan  == null) {
								plan = (EPlan)object;
							} else {
								LogUtil.warnOnce("multiple EPlans detected in the ResourceSet (using first found)");
							}
						}
					}
				}
			}
		}
		return plan;
	}

	/**
	 * Returns true if the element is a template plan
	 * @param object to determine if in template plan
	 * @return true if template plan, false otherwise
	 */
	public static boolean isTemplatePlan(Object object) {
		if (object instanceof EPlan) {
			EPlan plan = (EPlan) object;
			if (plan.isTemplate()) {
				return true;
			}
			return isTemplatePlanResource(plan.eResource());
		}
		return false;
	}

	public static boolean isTemplatePlanResource(Resource resource) {
		if (resource != null) {
			URI uri = resource.getURI();
			if (uri != null) {
				String lastSegment = uri.lastSegment();
				if ("template.plan".equals(lastSegment)) {
					return true;
				}
				String fileExtension = uri.fileExtension();
				if ("template".equals(fileExtension)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isTemplatePlanHierarchyElement(Object object) {
		if (object instanceof EObject) {
			EPlan plan = getPlan((EObject) object);
			return isTemplatePlan(plan);
		}
		return false;
	}
	
	/**
	 * This method will return a list of the EPlanElements added
	 * during this notification, if any.
	 *
	 * @param notification
	 * @return
	 */
	public static List<EPlanElement> getElementsAdded(Notification notification) {
		if (CommonUtils.equals(notification.getFeature(), PlanPackage.Literals.EPLAN_PARENT__CHILDREN)) {
			return EMFUtils.getAddedObjects(notification, EPlanElement.class);
		}
		return Collections.emptyList();
	}

	public static Collection<EActivity> getActivitiesAdded(Notification notification) {
		List<EPlanElement> elements = getElementsAdded(notification);
		if (elements.isEmpty()) {
			return Collections.emptySet();
		}
		return getAllChildActivities(elements);
	}

	/**
	 * This method will return a list of the EPlanElements removed
	 * during this notification, if any.
	 *
	 * @param notification
	 * @return
	 */
	public static List<EPlanElement> getElementsRemoved(Notification notification) {
		if (CommonUtils.equals(notification.getFeature(), PlanPackage.Literals.EPLAN_PARENT__CHILDREN)) {
			return EMFUtils.getRemovedObjects(notification, EPlanElement.class);
		}
		return Collections.emptyList();
	}

	public static Collection<EActivity> getActivitiesRemoved(Notification notification) {
		List<EPlanElement> elements = getElementsRemoved(notification);
		if (elements.isEmpty()) {
			return Collections.emptySet();
		}
		return getAllChildActivities(elements);
	}

	public static Collection<EActivity> getAllChildActivities(List<EPlanElement> elements) {
		final Set<EActivity> visitor = new LinkedHashSet<EActivity>(elements.size());
		PlanVisitor removalVisitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivity) {
			    	EActivity activity = (EActivity)element;
					visitor.add(activity);
			    }
			}
		};
		removalVisitor.visitAll(elements);
		return visitor;
	}

	/**
	 * Return the members for the entire eplan hierarchy that
	 * is of the type passed in
	 */
	public static <T extends EMember> Collection<T> getMembers(EPlanElement element, final Class<T> klass) {
		final List<T> members = new ArrayList<T>();
		new PlanVisitor(true) {
			@Override
			protected void visit(EPlanElement element) {
				try {
					members.add(element.getMember(klass));
				} catch (RuntimeException e) {
					/* ignore */
				}
			}
		}.visitAll(element);
		return members;
	}

	public static <T extends EPlanElement> List<T> copy(final List<T> elements) {
		return copy(elements, true);
	}
	
	public static <T extends EPlanElement> List<T> copy(final List<T> elements,
			final boolean recursive) {
		return TransactionUtils.reading(elements, new RunnableWithResult.Impl<List<T>>() {
			@SuppressWarnings("unchecked")
			public void run() {
				PlanElementCopier copier = new PlanElementCopier(recursive);
				List<T> list = new ArrayList<T>();
				for (T element : elements) {
					T copy = (T)copier.copy(element);
					list.add(copy);
				}
				copier.copyReferences();
				setResult(list);
			}
		});
	}
	
	public static <T extends EObject> T copy(T eObject) {
		return copy(eObject, true);
	}
	
	public static <T extends EObject> T copy(T eObject, boolean newIds) {
		PlanElementCopier copier = new PlanElementCopier(true);
	    EObject result = copier.copy(eObject, newIds);
	    copier.copyReferences();
	    
	    @SuppressWarnings("unchecked")T t = (T)result;
	    return t;
	}

	private static final String PLAN_DOMAIN = "ensemble.plan";
	private static final String PLAN_ACTIVITY_GROUP_NAME = PLAN_DOMAIN + ".activityGroupName";
	private static final String PLAN_ACTIVITY_GROUP_NAME_PLURAL = PLAN_ACTIVITY_GROUP_NAME + "s";

	public static String getActivityGroupDisplayName() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String name = properties.getProperty(PLAN_ACTIVITY_GROUP_NAME);
		if ((name == null) || (name.length() == 0)) {
			name = "Activity Group";
		}
		return name;
	}

	public static String getActivityGroupDisplayNamePlural() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String name = properties.getProperty(PLAN_ACTIVITY_GROUP_NAME_PLURAL);
		if ((name == null) || (name.length() == 0)) {
			name = "Activity Groups";
		}
		return name;
	}

	/** A cache of the latest-supplied object argument to getConsolidatedPlanElements(). */
	private static Collection<? extends EPlanElement> lastObjects;
	
	/** A cache of the latest-returned value from getConsolidatedPlanElements(). */
	private static List<EPlanElement> lastConsolidatedPlanElements;
	
	/**
	 * If objects is empty, return an empty list. Otherwise, return a list of those element of
	 * the objects argument for which the parent is not also in the list. Cache both the argument
	 * and the return value for subsequent calls.
	 * @param objects a list of plan elements
	 * @return a list of those elements of the argument for which the parent is not also in the list
	 */
	public static List<EPlanElement> getConsolidatedPlanElements(Collection<? extends EPlanElement> objects) {
		if (objects.isEmpty()) {
			// short circuit
			return Collections.emptyList();
		}
		synchronized (EPlanUtils.class) {
			if (CommonUtils.equals(objects, lastObjects)) {
				return new ArrayList<EPlanElement>(lastConsolidatedPlanElements);
			}
		}
		List<EPlanElement> elements = Collections.unmodifiableList(new ArrayList<EPlanElement>(removeContainedElements(objects)));
		synchronized (EPlanUtils.class) {
			lastObjects = objects;
			lastConsolidatedPlanElements = elements;
		}
		return elements;
	}
	
	public static void clearConsolidatedPlanElementsCache() {
		synchronized (EPlanUtils.class) {
			lastObjects = null;
			lastConsolidatedPlanElements = null;
		}
	}
	
	public static void setENamespaceURI(final EPlan plan, final String uri) {
		TransactionUtils.writing(plan, new Runnable() {
			public void run() {
				plan.setENamespaceURI(uri);
			}
		});
	}

	/**
	 * This method performs the expansion of the input activity and returns only the leaf nodes of the expansion.
	 *
	 * @param activity
	 * @return
	 */
	public static final List<EActivity> getLowestLevelExpansion(EActivity activity) {
		List<EActivity> expansion = new ArrayList<EActivity>();
		List<? extends EPlanElement> children = getChildren(activity);
		for (EPlanElement subActivity : children) {
			expansion.addAll(getLowestLevelExpansion((EActivity)subActivity));
		}
		// if we have no expansion, then add ourselves
		if (expansion.size() == 0) {
			expansion.add(activity);
		}
		return expansion;
	}

	public static Resource loadPlanResource(URI uri, Map<?,?> options) {
		Resource resource = new PlanResourceImpl(uri);
		try {
			resource.load(options);
		} catch (Exception e) {
			try {
				resource.load(options);
			} catch (Exception x) {
				Logger.getLogger(EPlanUtils.class).error("loading "+uri, x);
				return null;
			}
		}
		return resource;
	}
	

	/**
	 * Loads a plan.
	 * @param inputStream source file
	 * @return an EPlan (never null)
	 * @throws IOException on any failure
	 */
	public static EPlan loadOnePlan(InputStream inputStream) throws IOException {
		Resource resource = new PlanResourceImpl(null);
		resource.load(inputStream, null);
		return loadOnePlan(resource);
	}
	
	

	/**
	 * Loads a plan.
	 * @param inputStream source file
	 * @param uriForResolvingReferences e.g. for resolving Resources/CrewMember.resource
	 * @return an EPlan (never null)
	 * @throws IOException on any failure
	 */
	public static EPlan loadOnePlan(InputStream inputStream, URI uriForResolvingReferences) throws IOException {
		ResourceSet resourceSet = EMFUtils.createResourceSet();
		Resource resource = new PlanResourceImpl(uriForResolvingReferences);
		resourceSet.getResources().add(resource);
		EcoreUtil.resolveAll(resourceSet);
		resource.load(inputStream, null);
		return (EPlan) resource.getContents().get(0);
	}
	
	/**
	 * Loads a plan.
	 * @param uri source file
	 * @return an EPlan (never null)
	 * @throws IOException on any failure
	 */
	public static EPlan loadOnePlan(URI uri) throws IOException {
		return loadOnePlan(EMFUtils.createResourceSet().createResource(uri));
	}
	
	/**
	 * Loads a plan.
	 * @param url source file
	 * @return an EPlan (never null)
	 * @throws IOException on any failure
	 */
	public static EPlan loadOnePlan(URL url) throws IOException {
		return loadOnePlan(URI.createURI(url.toString()));
	}
	
	/**
	 * Loads a plan.
	 * @param file source file
	 * @return an EPlan (never null)
	 * @throws IOException on any failure
	 */
	public static EPlan loadOnePlan(File file) throws IOException {
		return loadOnePlan(URI.createFileURI(file.getPath()));
	}
	
	/**
	 * Loads a plan (without starting up the Plan Advisor), e.g. for report generation.
	 * @param resource
	 * @return an EPlan (never null)
	 * @throws IOException on any failure
	 */
	public static EPlan loadOnePlan(IResource resource) throws IOException {
		IPath path = resource.getFullPath();
		URI uri = URI.createPlatformResourceURI(path.toString(), true);
		Resource planResource = EPlanUtils.loadPlanResource(uri, null);
		if (planResource == null) {
			throw new IOException("Unable to load plan resource.");
		} else {
			return loadOnePlan(planResource);
		}
	}
	
	/**
	 * Loads a plan (without starting up the Plan Advisor), e.g. for report generation.
	 * @param planResource
	 * @return an EPlan (never null)
	 * @throws IOException on any failure
	 */
	public static EPlan loadOnePlan(Resource planResource) throws IOException {
		TransactionalEditingDomain domain = TransactionUtils
					.createTransactionEditingDomain();
		ResourceSet resourceSet = domain.getResourceSet();
		resourceSet.getResources().add(planResource);
		resourceSet.getLoadOptions().put(EnsembleOption.OPTION_TO_DISABLE_PLAN_ADVISOR, true);
		EMFUtils.useProjectURIConverter(planResource);
		if (!planResource.isLoaded()) {
			planResource.load(Collections.EMPTY_MAP);
		}
		EList<EObject> contents = planResource.getContents();
		EPlan planReadFromFile = null;
		if (contents != null && !contents.isEmpty()) {
			planReadFromFile = (EPlan) contents.get(0);
		}
		if (planReadFromFile == null) {
			StringBuilder s = new StringBuilder("Unable to read plan from " + planResource.getURI());
			if (planResource.getErrors().isEmpty()) {
				s.append(" for unknown reasons.");
			} else {
				s.append(":");
				for (Diagnostic error : planResource.getErrors()) {
					s.append("\n * " + error.getMessage());
				}
			}
			throw new IOException(s.toString());
		}
		return planReadFromFile;
	}


	@SuppressWarnings("unchecked")
	public static void savePlan(EPlan plan, File file) throws Exception {
		Map options = new HashMap();
		options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, false);
		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);
		FileOutputStream out = new FileOutputStream(file);
		plan.eResource().save(out, options);
		out.close();
	}
	
	/**
	 * Use this method to determine the severity level of an object. Since markers
	 * apply to the underlying plan resource, and not individual EPlanElement instances,
	 * it is necessary to iterate through each marker, to check for which element it has
	 * been created.
	 * @param object the potential EPlanElement
	 * @return the associated severity, or Integer.MIN_VALUE if no severity is found.
	 */
	public static int getSeverity(Object object) {
		return getSeverity(object, true);
	}
	
	public static int getSeverity(Object object, boolean includeChildren) {
		if (object instanceof EPlanElement) {
			EPlanElement element = (EPlanElement) object;
			EPlan plan = EPlanUtils.getPlan(element);
			if (plan != null) {
				boolean isPlan = element instanceof EPlan;
				if (!isPlan && !SEVERITY_PROVIDERS.isEmpty()) {
					for (PlanElementSeverityProvider provider : SEVERITY_PROVIDERS) {
						Integer severity = provider.getSeverity(element, plan, includeChildren);
						if (severity != null) {
							return severity;
						}
					}
				}
				IResource resource = (IResource) plan.getAdapter(IResource.class);
				if ((resource != null) && resource.exists()) {
					try {
						boolean includeSubtypes = true;
						int depth = 0;
						if (element instanceof EPlan) {
							return resource.findMaxProblemSeverity(IMarker.PROBLEM, includeSubtypes, depth);
						}
						IMarker[] markers = resource.findMarkers(IMarker.PROBLEM, includeSubtypes, depth);
						int severity = getHighestSeverity(markers, element, includeChildren);
						return severity;
					} catch (CoreException e) {
						LogUtil.error("getSeverity", e);
					}
				}
			}
		}
		return Integer.MIN_VALUE;
	}

	/**
	 * Use this to get the highest severity of a given eObject, given the available
	 * markers.
	 * @param markers.
	 * @param eObject the object whose severity is being determined
	 * @return the highest severity of the eObject
	 * @throws CoreException
	 */
	private static int getHighestSeverity(IMarker[] markers, EPlanElement element, boolean includeChildren) {
		int severity = Integer.MIN_VALUE;
		EPlan plan = EPlanUtils.getPlan(element);
		Resource resource = plan == null ? null : plan.eResource();
		for (IMarker marker : markers) {
			List<EPlanElement> culprits = getCulprits(marker, resource);
			if (culprits.contains(element)) {
				int markerSeverity = marker.getAttribute(IMarker.SEVERITY, Integer.MIN_VALUE);
				if (markerSeverity >= IMarker.SEVERITY_ERROR) {
					return markerSeverity;
				}
				if (severity < markerSeverity) {
					severity = markerSeverity;
				}
			}
		}
		if (includeChildren && element instanceof EPlanChild) {
			List<? extends EPlanChild> children = ((EPlanChild)element).getChildren();
			for (EPlanChild child : children) {
				int highestSeverity = getHighestSeverity(markers, child, true);
				if (highestSeverity == IMarker.SEVERITY_ERROR) {
					return highestSeverity;
				}
				if (severity < highestSeverity) {
					severity = highestSeverity;
				}
			}
		}
		return severity;
	}

	/**
	 * Returns the culprits for a marker, or the empty list if none found.
	 * 
	 * @param marker
	 * @return a list of the culprits for the marker (not null)
	 */
	@SuppressWarnings("unchecked")
	public static List<EPlanElement> getCulprits(IMarker marker, Resource resource) {
		List<EPlanElement> culprits = null;
		try {
			culprits = (List<EPlanElement>)marker.getAttribute(CULPRITS);
			if (culprits != null) {
				return culprits;
			}
		} catch (CoreException e) {
			// fall through
		}
		String location = marker.getAttribute(IMarker.LOCATION, null);
		if (location != null && resource != null) {
			EObject eObject = resource.getEObject(location);
			if (eObject instanceof EPlanElement) {
				EPlanElement element = (EPlanElement) eObject;
				return Collections.singletonList(element);
			}
		}
		return Collections.<EPlanElement>emptyList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<EPlanElement> getCulprits(Map<String, Object> markerAttributes, Resource resource) {
		List<EPlanElement> culprits = (List<EPlanElement>) markerAttributes.get(CULPRITS);
		if (culprits != null) {
			return culprits;
		}
		String location = (String) markerAttributes.get(IMarker.LOCATION);
		if (location != null && resource != null) {
			EObject eObject = resource.getEObject(location);
			if (eObject instanceof EPlanElement) {
				EPlanElement element = (EPlanElement) eObject;
				return Collections.singletonList(element);
			}
		}
		return Collections.<EPlanElement>emptyList();
	}
	
	/**
	 * Returns the culprits for a marker delta, or the empty list if none found.
	 * 
	 * @param marker
	 * @return a list of the culprits for the marker delta (not null)
	 */
	@SuppressWarnings("unchecked")
	public static List<EPlanElement> getCulprits(IMarkerDelta delta) {
		List<EPlanElement> list = (List<EPlanElement>)delta.getAttribute(CULPRITS);
		if (list != null) {
			return list;
		}
		return Collections.<EPlanElement>emptyList();
	}
	
	/**
	 * Use to get the nicely formatted display version of a feature/attribute name. 
	 * @param feature the feature whose name is to be returned, nicely formatted of course
	 * @param eObject the element to which the feature belongs
	 * @param data typically eObject.getData()
	 * @return a nice display name version of the feature name, or null if none is available.
	 */
	public static String getDisplayName(EStructuralFeature feature , EObject eObject, EObject data) {
		String displayName = null;
		IItemPropertySource itemPropertySource = EMFUtils.adapt(eObject, IItemPropertySource.class);
		if(itemPropertySource != null) {
			IItemPropertyDescriptor itemPropertyDescriptor = itemPropertySource.getPropertyDescriptor(eObject, feature);
			if(itemPropertyDescriptor != null) {
				displayName = itemPropertyDescriptor.getDisplayName(eObject);
			}						
		}
		
		Object name = eObject.eGet(feature);
		if(name != null) {
			displayName = name.toString();
		}
		
		return displayName;
	}
	
	/**
	 * Use to get the nicely formatted display version of a feature/attribute name. 
	 * @param featureName the feature whose name is to be returned, nicely formatted of course
	 * @param eObject the EObject to which the feature belongs
	 * @param data typically eObject.getData()
	 * @return a nice display name version of the feature name, or null if none is available
	 */	
	public static String getDisplayName(String featureName, EObject eObject, EObject data) {	
		String displayName = null;
		
		if(data != null) {
			EClass eClass = data.eClass();
			if(eClass != null) {
				EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(featureName);
				if(eStructuralFeature != null) {
					displayName = getDisplayName(eStructuralFeature, eObject, data);
				}
			}
		} else {
			EStructuralFeature eStructuralFeature = eObject.eClass().getEStructuralFeature(featureName);
			if(eStructuralFeature != null) {
				displayName = getDisplayName(eStructuralFeature, eObject, data);
			}
		}
		return displayName;
	}	
	
	/** @deprecated Use the method in ADParameterUtils instead.  It works for groups too. */
	@Deprecated
	public static Object getADDefinedArgumentValue(EActivity activity, String argumentName) {
		return getADDefinedArgumentList(activity).eGet(getADDefinedArgumentFeature(activity, argumentName));
	}

	/** @deprecated Use the method in ADParameterUtils instead.  It works for groups too. */
	@Deprecated
	public static void setADDefinedArgumentValue(EActivity activity, String argumentName, Object newValue) {
		getADDefinedArgumentList(activity).eSet(getADDefinedArgumentFeature(activity, argumentName), newValue);
	}

	private static EObject getADDefinedArgumentList(EActivity activity) {
		EObject activityData = activity.getData();
		if (activityData==null) {
			throw new NullPointerException("Activity type not found in AD.");
		}
		return activityData;
	}
	
	/** @deprecated Use the method in ADParameterUtils instead.  It works for groups too. */
	@Deprecated
	public static EStructuralFeature getADDefinedArgumentFeature(EActivity activity, String argumentName) {
		EClass activityDef = getADDefinedArgumentList(activity).eClass();
		if (activityDef==null) {
			throw new IllegalStateException("Activity type not found in AD.");
		}
		EStructuralFeature eStructuralFeature = activityDef.getEStructuralFeature(argumentName);
		if (eStructuralFeature==null) {
			String typeName = activity.getClass().getSimpleName();
			if (activity instanceof EActivity) {
				typeName = ((EActivity)activity).getType();
			}
			throw new IllegalStateException("Undefined argument " + argumentName + " in " + typeName);
		}
		return eStructuralFeature;
	}
	
	/** @deprecated Use the method in ADParameterUtils instead.  It works for groups too. */
	@Deprecated
	public static boolean hasADDefinedArgument(EActivity activity, String argumentName) {
		EClass activityDef = getADDefinedArgumentList(activity).eClass();
		if (activityDef==null) {
			return false;
		}
		EStructuralFeature eStructuralFeature = activityDef.getEStructuralFeature(argumentName);
		if (eStructuralFeature==null) {
			return false;
		}
		return true;
	}
	
	public static final void contributeProductResources(EPlan plan) {
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(plan);
		// TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		ResourceSet resourceSet = domain.getResourceSet();
		if (resourceSet != null) {
			for (PlanResourceSetContributor c : PLAN_RESOURCE_CONTRIBUTORS) {
				try {
					c.contributePlanResources(plan, resourceSet);
				} catch (Exception e) {
					LogUtil.error("loading from resource set contributor "+c, e);
				}
			}
		}
	}
	
	public static class PlanResourceSetContributor implements Comparable<PlanResourceSetContributor> {
		
		private static long count = 0;
		private long index = count++;
		
		public void contributePlanResources(EPlan plan, ResourceSet resourceSet) {
			// no default implementation
		}

		public final int compareTo(PlanResourceSetContributor o) {
			if (this.dependsOn(o)) {
				return 1;
			}
			if (o.dependsOn(this)) {
				return -1;
			}
			return CommonUtils.compare(index, o.index);
		}
		
		public boolean dependsOn(PlanResourceSetContributor o) {
			return false;
		}
		
	}
	
	public static abstract class PlanElementSeverityProvider {
		
		public abstract Integer getSeverity(EPlanElement element, EPlan plan, boolean includeChildren);
	}
	

	/**
	 * This method loads a plan while checking for errors.  It loads the plan into 
	 * an EnsembleEditingDomain model resource set, to avoid many notifications 
	 * that occur when loading the plan into a transaction resource set.  After the
	 * plan has been loaded, it will be reparented to the provided resource set.
	 * The plan is then returned.
	 * 
	 * @param resourceSet
	 * @param uri
	 * @param monitor TODO
	 * @return plan, or null if resource doesn't exist
	 * @throws Exception
	 */
	public static EPlan loadPlanIntoResourceSetWithErrorChecking(ResourceSet resourceSet, URI uri, IProgressMonitor monitor) throws Exception {
		long start = System.currentTimeMillis();
		ResourceSet tempResourceSet = EMFUtils.createResourceSet();
		Resource resource = tempResourceSet.createResource(uri);
		EMFUtils.useProjectURIConverter(resource);
		Exception firstException = null;
//		Map<Object, IProgressMonitor> monitorMap = ProgressMonitorInputStream.option(monitor);
		Map<Object, Object> monitorMap = resourceSet.getLoadOptions();
		monitorMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
		monitorMap.put(XMLResource.OPTION_USE_PARSER_POOL, PARSER_POOL);
		monitorMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
		monitorMap.put(XMLResource.OPTION_SKIP_ESCAPE, Boolean.TRUE);
		monitorMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
		monitorMap.put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
		monitorMap.put(IProgressMonitor.class.getCanonicalName(), monitor);
		try {
			resource.load(monitorMap);
		} catch (NullPointerException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			firstException = e;
			try {
				resource.load(null);
			} catch (Exception x) {
				throw x;
			}
		}
		long loaded = System.currentTimeMillis();
		LogUtil.info("loading plan at URI: " + uri + " took " + (loaded - start) / 1000.0 + " seconds");
		resourceSet.getResources().add(resource);
		long added = System.currentTimeMillis();
		LogUtil.info("adding the plan to the resource set took " + (added - loaded) / 1000.0 + " seconds");
		// Move any additional resources that may have been loaded by resolving proxies from the temporary resourceSet to the
		// input resourceSet
		List<Resource> newResources = new ArrayList<Resource>(tempResourceSet.getResources());
		for (Resource newResource : newResources) {
			resourceSet.getResources().add(newResource);
		}
		EList<EObject> contents = resource.getContents();
		if (contents.isEmpty()) {
			if (firstException != null) {
				throw firstException;
			}
			throw new IOException("failed to load a plan from the URI: " + uri);
		} else if (firstException != null) {
			LogUtil.error("loading plan resource "+firstException.getMessage());
		}
		EPlan plan = (EPlan) contents.get(0);
		return plan;
	}

	public static String getPlanElementTypeName(Object object) {
		if (object instanceof EPlan) {
			return "plan";
		} else if (object instanceof EActivityGroup) {
			return EPlanUtils.getActivityGroupDisplayName();
		} else if (object instanceof EActivity) {
			return "activity";
		}
		return "plan element";
	}
	
	public static EActivityGroup getTopLevelGroup(EPlanChild child) {
		EPlanElement parentOrPlan = child.getParent();
		if (!(parentOrPlan instanceof EPlanChild)) {
			return null;
		}
		EPlanChild parent = (EPlanChild) parentOrPlan;
		EPlanElement grandparent = parent.getParent();
		if (!(grandparent instanceof EActivityGroup)) {
			return (EActivityGroup) parentOrPlan;
		}
		// If grandparent is not the plan, then parent is not the top-level group
		// and we need to look further up.
		return getTopLevelGroup(parent);
	}
	
	public static boolean canEdit(final EMember member) {
		return canEdit(member.getPlanElement());
	}
	
	public static boolean canEdit(final EPlanElement planElement) {
		for (PlanElementApprover approver : ClassRegistry.createInstances(PlanElementApprover.class))
			if (!approver.canEdit(planElement))
				return false;
		return true;
	}
}
