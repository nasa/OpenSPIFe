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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.ResourceUtils;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.model.common.CommonPackage;
import gov.nasa.ensemble.emf.model.common.ObjectFeature;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.validation.marker.IMarkerConfigurator;
import org.eclipse.emf.validation.marker.MarkerUtil;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXParseException;

public class PlanResourceIncrementalProjectBuilder extends IncrementalProjectBuilder {

	public static final String ID = "gov.nasa.ensemble.core.plan.resources.PlanResourceIncrementalProjectBuilder";

	private static final Set<String> fileNames = new HashSet<String>();
	private static final Set<String> fileExtensions = new HashSet<String>();
	private static final Set<String> folderNames = new HashSet<String>();

	private static final String ANNOTATION_SOURCE_DETAIL = "detail";
	private static final String ANNOTATION_DETAIL_TYPE = "type";
	private static final List<String> PROJECT_RESOURCE_TYPES = Arrays.asList(new String[] { "IFile", "IFolder", "IContainer", "IPath" });

	private static final Logger trace = Logger.getLogger(PlanResourceIncrementalProjectBuilder.class);

	static {
		try {
			initializeFileAssociations();
		} catch (Exception e) {
			trace.error(e.getMessage(), e);
		}
	}

	private static void initializeFileAssociations() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry == null)
			return;
		IExtensionPoint extensionPoint = registry.getExtensionPoint("gov.nasa.ensemble.core.plan.resources.builderFiles");
		if (extensionPoint == null)
			return;
		for (IConfigurationElement configElement : extensionPoint.getConfigurationElements()) {
			String names = configElement.getAttribute("file-names");
			if (names != null) {
				for (String name : CommonUtils.COMMA_PATTERN.split(names)) {
					fileNames.add(name.trim());
				}
			}
			String extensions = configElement.getAttribute("file-extensions");
			if (extensions != null) {
				for (String extension : CommonUtils.COMMA_PATTERN.split(extensions)) {
					fileExtensions.add(extension.trim());
				}
			}
			String folders = configElement.getAttribute("folder-names");
			if (folders != null) {
				for (String folder : CommonUtils.COMMA_PATTERN.split(folders)) {
					folderNames.add(folder.trim());
				}
			}
		}
	}

	private final Set<IProject> fullBuildProjects = new HashSet<IProject>();
	private final Map<IResource, List<IFile>> resourceToValidatedFileMap = new HashMap<IResource, List<IFile>>();
	private final Map<IFile, List<IResource>> validatedFileToResourceMap = new HashMap<IFile, List<IResource>>();

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (!PlatformUI.isWorkbenchRunning()) {
			return new IProject[0];
		}
		IProject[] projects = null;
		switch (kind) {
		case IncrementalProjectBuilder.CLEAN_BUILD:
			clean(monitor);
			break;
		case IncrementalProjectBuilder.FULL_BUILD:
			projects = fullBuild(args, monitor);
			break;
		case IncrementalProjectBuilder.AUTO_BUILD:
		case IncrementalProjectBuilder.INCREMENTAL_BUILD:
			projects = incrementalBuild(args, monitor);
			break;
		}
		return projects;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		removeAllProjectBuilderMarkers(getProject(), monitor);
	}

	@Override
	public ISchedulingRule getRule(int kind, Map args) {
		return getProject();
	}

	private IBatchValidator getBatchValidator() {
		IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_TRACK_RESOURCES, true);
		validator.setIncludeLiveConstraints(true);
		validator.setReportSuccesses(true);
		return validator;
	}

	protected IProject[] fullBuild(Map args, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Updating resource markers", 100);
		try {
			removeAllProjectBuilderMarkers(getProject(), new SubProgressMonitor(monitor, 10));
			if (monitor.isCanceled()) {
				return null;
			}
			IBatchValidator validator = getBatchValidator();
			createMarkers(getProject(), validator, new SubProgressMonitor(monitor, 90));
			fullBuildProjects.add(getProject());
		} finally {
			monitor.done();
		}
		return null;
	}

	protected IProject[] incrementalBuild(Map args, IProgressMonitor monitor) throws CoreException {
		IProject project = getProject();
		if (!fullBuildProjects.contains(project)) {
			return fullBuild(args, monitor);
		}
		IResourceDelta resourceDelta = getDelta(getProject());
		if (resourceDelta == null) {
			return null;
		} else {
			try {
				monitor.beginTask("Processing resource update", 100);
				monitor.subTask("getting changed resources");
				Collection<IResource> changedResources = ResourceUtils.getChangedResources(resourceDelta);
				monitor.worked(10);
				if (monitor.isCanceled()) {
					return null;
				}
				monitor.subTask("removing dependencies from changed resources");
				for (IResource changed : changedResources) {
					if (changed instanceof IFile) {
						// remove all dependencies on resources for the changed file as it will be revalidated
						// and its dependencies on other resources recalculated
						removeFileDependencies((IFile) changed);
					}
				}
				monitor.worked(10);
				if (monitor.isCanceled()) {
					return null;
				}
				monitor.subTask("getting affected resources");
				Collection<IResource> affectedResources = getAffectedResources(changedResources);
				if (affectedResources.isEmpty()) {
					return null;
				}
				monitor.worked(10);
				if (monitor.isCanceled()) {
					return null;
				}
				monitor.subTask("updating affected resources");
				int step = 70 / affectedResources.size();
				IBatchValidator validator = getBatchValidator();
				for (IResource affectedResource : affectedResources) {
					removeAllProjectBuilderMarkers(affectedResource, new SubProgressMonitor(monitor, step / 2));
					// Don't try to validate deleted resources
					if (affectedResource.exists()) {
						createMarkers(affectedResource, validator, new SubProgressMonitor(monitor, step / 2));
					} else {
						monitor.worked(step / 2);
					}
				}
			} finally {
				monitor.done();
			}
		}
		return null;
	}

	private Collection<IResource> getAffectedResources(Collection<IResource> changedResources) {
		Set<IResource> affected = new HashSet<IResource>(changedResources);
		for (IResource changed : changedResources) {
			List<IFile> affectedFiles = getAffectedFiles(changed);
			if (affectedFiles != null) {
				affected.addAll(affectedFiles);
			}
		}
		return affected;
	}

	protected void removeAllProjectBuilderMarkers(IResource resource, IProgressMonitor monitor) throws CoreException {
		boolean includeSubtypes = true;
		int depth = IResource.DEPTH_INFINITE;
		if (resource.exists()) {
			final IMarker[] problemMarkers = resource.findMarkers(IResourceMarkers.PLAN_RESOURCE_PROBLEM_MARKER, includeSubtypes, depth);
			final IMarker[] validationMarkers = resource.findMarkers(IResourceMarkers.MODEL_VALIDATION_PROBLEM_MARKER, includeSubtypes, depth);
			if (problemMarkers.length > 0 || validationMarkers.length > 0) {
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
					@Override
					public void run(IProgressMonitor monitor) throws CoreException {
						monitor.beginTask("Removing old markers", problemMarkers.length + validationMarkers.length);
						try {
							for (IMarker problemMarker : problemMarkers) {
								if (problemMarker.exists()) {
									problemMarker.delete();
								}
								monitor.worked(1);
								if (monitor.isCanceled()) {
									return;
								}
							}
							for (IMarker validationMarker : validationMarkers) {
								if (validationMarker.exists()) {
									validationMarker.delete();
								}
								monitor.worked(1);
								if (monitor.isCanceled()) {
									return;
								}
							}
						} finally {
							monitor.done();
						}
					}
				}, resource, IWorkspace.AVOID_UPDATE, monitor);
			} else {
				monitor.beginTask("No old markers to remove", 1);
				monitor.done();
			}
		}
	}

	/**
	 * Will try to add markers to applicable resource(s)
	 * 
	 * @param resource
	 * @param monitor
	 * @throws CoreException
	 */
	protected void createMarkers(IResource resource, IBatchValidator validator, IProgressMonitor monitor) throws CoreException {
		TransactionalEditingDomain domain = TransactionUtils.createTransactionEditingDomain(false);
		ResourceSet resourceSet = domain.getResourceSet();
		IProject project = resource.getProject();
		if (project != null) {
			resourceSet.setURIConverter(new ProjectURIConverter(project));
		}
		monitor.beginTask("Creating markers for " + resource.getName(), 100);
		try {
			monitor.subTask("getting resource files");
			List<IFile> resourceFiles = getResourceFiles(resource, monitor);
			if (resourceFiles.isEmpty()) {
				return;
			}
			monitor.worked(10);
			if (monitor.isCanceled()) {
				return;
			}
			int step = 90 / resourceFiles.size();
			for (IFile file : resourceFiles) {
				monitor.subTask("getting " + file.getName());
				Resource emfResource = getEmfResource(new SubProgressMonitor(monitor, step / 2), file, resourceSet);
				if (monitor.isCanceled()) {
					return;
				}
				if (emfResource != null) {
					List<EObject> objectsToValidate = emfResource.getContents();
					monitor.subTask("validating " + file.getName());
					validate(new SubProgressMonitor(monitor, step / 2), file, validator, objectsToValidate);
					if (monitor.isCanceled()) {
						return;
					}
					monitor.subTask("clearing " + file.getName());
					resourceSet.getResources().remove(emfResource);
				}
			}
		} finally {
			try {
				domain.dispose();
			} catch (Exception e) {
				LogUtil.error(e);
			}
			monitor.done();
		}
	}

	/**
	 * Recursively get all IFiles contained in the IResource
	 * 
	 * @param resource
	 * @param monitor
	 *            for cancelling only
	 * @return
	 */
	private List<IFile> getResourceFiles(IResource resource, IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return Collections.emptyList();
		}
		if (resource instanceof IFile) {
			// filter out files that don't match the registered file names or extensions
			if (!folderNames.contains(resource.getParent().getName()) && !fileNames.contains(resource.getName()) && !fileExtensions.contains(resource.getFileExtension())) {
				return Collections.emptyList();
			}
			IFile file = (IFile) resource;
			return Collections.singletonList(file);
		}
		if (resource instanceof IContainer) {
			IContainer container = (IContainer) resource;
			IResource[] members = null;
			try {
				members = container.members();
			} catch (CoreException e) {
				LogUtil.errorOnce("members unavailable: " + container.getName() + ", message=" + e.getMessage());
			}
			if (members != null) {
				List<IFile> files = new ArrayList<IFile>();
				for (IResource member : members) {
					files.addAll(getResourceFiles(member, monitor));
				}
				return files;
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Strange but needs to be done: try to load the resource twice...
	 * 
	 * @param monitor
	 * @param file
	 * @param resourceSet
	 * @return
	 * @throws CoreException
	 */
	private Resource getEmfResource(IProgressMonitor monitor, final IFile file, ResourceSet resourceSet) throws CoreException {
		Resource emfResource = null;
		try {
			if (file.exists()) {
				URI uri = getPlatformResourceURI(file);
				emfResource = resourceSet.getResource(uri, true);
				emfResource.load(null);
			}
		} catch (final Exception e) {
			createMarkerForException(monitor, file, e);
			try {
				if (emfResource != null) {
					emfResource.load(null);
				} else {
					// do nothing
				}
			} catch (Exception x) {
				// can't load this resource
			}
		}
		return emfResource;
	}

	private void createMarkerForException(IProgressMonitor monitor, final IFile file, final Exception e) throws CoreException {

		if (e instanceof WrappedException) {
			WrappedException wrappedException = (WrappedException) e;
			Exception exception = wrappedException.exception();
			if (exception instanceof SAXParseException) {
				// do nothing
				return;
			}
		}

		IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = file.createMarker(IResourceMarkers.PLAN_RESOURCE_PROBLEM_MARKER);
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put(IMarker.MESSAGE, e.getMessage());
				attributes.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				marker.setAttributes(attributes);
			}
		};

		ResourcesPlugin.getWorkspace().run(workspaceRunnable, file, IWorkspace.AVOID_UPDATE, monitor);
	}

	private synchronized void validate(IProgressMonitor monitor, final IFile file, IBatchValidator batchValidator, List<EObject> objectsToValidate) throws CoreException {
		for (EObject eObject : objectsToValidate) {
			IStatus status = batchValidator.validate(eObject);
			final IStatus flatStatus = flattenStatus(status);
			addResourceDependencies(flatStatus, file);
			if (!status.isOK()) {
				IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {
					@Override
					public void run(IProgressMonitor monitor) throws CoreException {
						createMarkers(file, flatStatus, IResourceMarkers.MODEL_VALIDATION_PROBLEM_MARKER, new IMarkerConfigurator() {

							@Override
							public void appendMarkerConfiguration(IMarker marker, IConstraintStatus status) throws CoreException {
								EObject target = status.getTarget();
								EPlanElement element = null;
								if (target instanceof EPlanElement) {
									element = (EPlanElement) target;
								} else if (target.eContainer() instanceof EPlanElement) {
									element = (EPlanElement) target.eContainer();
								}
								if (element != null) {
									marker.setAttribute(IMarker.LOCATION, element.eResource().getURIFragment(element));
								}
								EStructuralFeature feature = getStatusFeature(target, status);
								if (feature != null) {
									marker.setAttribute(MarkerConstants.NAME, EMFUtils.getDisplayName(target, feature));
								}
							}
						});
					}

				};
				ResourcesPlugin.getWorkspace().run(workspaceRunnable, file, IWorkspace.AVOID_UPDATE, monitor);
			}
		}
	}

	/**
	 * A simplified version of org.eclipse.emf.validation.marker.MarkerUtil.createMarkers that takes advantage of knowledge of the IFile for which markers are created, creates markers for all
	 * severities, and avoids running a nested IWorkspaceRunnable using the workspace root as scheduling rule. The latter would cause an error when run under the validate method, whose
	 * IWorkspaceRunnable is run with the IFile as scheduling rule.
	 * 
	 * @param file
	 * @param status
	 * @param markerType
	 * @param configurator
	 * @throws CoreException
	 */
	private void createMarkers(IFile file, IStatus status, String markerType, IMarkerConfigurator configurator) throws CoreException {
		if (status.isMultiStatus()) {
			for (IStatus element : status.getChildren()) {
				// recursively unwrap all children of multi-statuses
				createMarkers(file, element, markerType, configurator);
			}
		} else if (status instanceof IConstraintStatus) {
			createMarker(file, (IConstraintStatus) status, markerType, configurator);
		}
	}

	private void createMarker(IFile file, IConstraintStatus status, String markerType, IMarkerConfigurator configurator) throws CoreException {
		if (!status.matches(IStatus.INFO | IStatus.ERROR | IStatus.WARNING | IStatus.CANCEL)) {
			return;
		}

		IMarker marker = file.createMarker(markerType);

		switch (status.getSeverity()) {
		case IStatus.INFO:
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_LOW);
			break;
		case IStatus.WARNING:
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
			break;
		case IStatus.ERROR:
		case IStatus.CANCEL:
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			break;
		}

		marker.setAttribute(IMarker.MESSAGE, status.getMessage());
		marker.setAttribute(EValidator.URI_ATTRIBUTE, EcoreUtil.getURI(status.getTarget()).toString());
		marker.setAttribute(MarkerUtil.RULE_ATTRIBUTE, status.getConstraint().getDescriptor().getId());

		if (configurator != null) {
			configurator.appendMarkerConfiguration(marker, status);
		}
	}

	private EStructuralFeature getStatusFeature(EObject target, IConstraintStatus status) {
		for (EObject locusObject : status.getResultLocus()) {
			if (locusObject instanceof ObjectFeature) {
				ObjectFeature objectFeature = (ObjectFeature) locusObject;
				if (objectFeature.getObject().equals(target)) {
					return objectFeature.getFeature();
				}
			}
		}
		return null;
	}

	private void addResourceDependencies(IStatus status, IFile file) {
		if (status.isMultiStatus()) {
			for (IStatus child : status.getChildren()) {
				addResourceDependencies(child, file);
			}
		} else if (status instanceof IConstraintStatus) {
			IConstraintStatus conStatus = (IConstraintStatus) status;
			for (EObject locusComponent : conStatus.getResultLocus()) {
				if (locusComponent instanceof ObjectFeature) {
					ObjectFeature objectFeature = (ObjectFeature) locusComponent;
					EObject object = objectFeature.getObject();
					EStructuralFeature feature = objectFeature.getFeature();
					if (feature instanceof EAttribute) {
						EAttribute attribute = (EAttribute) feature;
						if (attribute.getEAttributeType() == CommonPackage.Literals.IPATH) {
							IResource resource = getResourceFromAttributeValue(object, attribute);
							if (resource != null) {
								addResourceAffectsFile(resource, file);
							}
						}
					}
				}
			}
		}
	}

	private IResource getResourceFromAttributeValue(EObject target, EAttribute attribute) {
		String type = EMFUtils.getAnnotation(attribute, ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_TYPE);
		if (type == null || !PROJECT_RESOURCE_TYPES.contains(type)) {
			return null;
		}
		IPath path = (IPath) target.eGet(attribute);
		if (path == null) {
			return null;
		}
		URI baseURI = target.eResource().getURI();
		URI uri = URI.createURI(path.toString()).resolve(baseURI);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String platformString = uri.toPlatformString(true);
		if (platformString == null) {
			return null;
		} else if (type.equals("IFile")) {
			return root.getFile(new Path(platformString));
		} else {
			return root.getFolder(new Path(platformString));
		}
	}

	private IStatus flattenStatus(IStatus status) {
		if (status.isMultiStatus()) {
			boolean nestedMulti = false;
			for (IStatus child : status.getChildren()) {
				if (child.isMultiStatus()) {
					nestedMulti = true;
					break;
				}
			}
			if (nestedMulti) {
				MultiStatus newStatus = new MultiStatus(status.getPlugin(), status.getCode(), status.getMessage(), null);
				for (IStatus child : status.getChildren()) {
					newStatus.merge(flattenStatus(child));
				}
				return newStatus;
			}
		}
		return status;
	}

	private URI getPlatformResourceURI(IFile file) {
		return EMFUtils.getURI(file);
	}

	public void addResourceAffectsFile(IResource resource, IFile file) {
		List<IFile> affectedFiles = resourceToValidatedFileMap.get(resource);
		if (affectedFiles == null) {
			affectedFiles = new ArrayList<IFile>(3);
			resourceToValidatedFileMap.put(resource, affectedFiles);
		}
		if (!affectedFiles.contains(file)) {
			affectedFiles.add(file);
			addFileAffectedByResource(file, resource);
		}
	}

	public void removeResourceAffectsFile(IResource resource, IFile affected) {
		List<IFile> affectedFiles = resourceToValidatedFileMap.get(resource);
		if (affectedFiles != null) {
			affectedFiles.remove(affected);
			if (affectedFiles.isEmpty()) {
				resourceToValidatedFileMap.remove(resource);
			}
		}
	}

	public void addFileAffectedByResource(IFile file, IResource resource) {
		List<IResource> resources = validatedFileToResourceMap.get(file);
		if (resources == null) {
			resources = new ArrayList<IResource>(3);
			validatedFileToResourceMap.put(file, resources);
			resources.add(resource);
		} else if (!resources.contains(resource)) {
			resources.add(resource);
		}
	}

	public void removeResourceAffectedByFile(IFile file, IResource resource) {
		List<IResource> resources = validatedFileToResourceMap.get(file);
		if (resources != null) {
			resources.remove(resource);
			if (resources.isEmpty()) {
				validatedFileToResourceMap.remove(resource);
			}
		}
	}

	public void removeFileDependencies(IFile file) {
		List<IResource> resources = validatedFileToResourceMap.get(file);
		if (resources != null) {
			for (IResource resource : resources) {
				removeResourceAffectsFile(resource, file);
			}
			validatedFileToResourceMap.remove(file);
		}
	}

	public List<IFile> getAffectedFiles(IResource resource) {
		return resourceToValidatedFileMap.get(resource);
	}
}
