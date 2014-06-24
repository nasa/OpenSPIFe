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
package gov.nasa.ensemble.common.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ISetSelectionTarget;

public abstract class ResourceUtils {
	   /**
     * Attempts to select and reveal the specified resource in all
     * parts within the supplied workbench window's active page.
     * <p>
     * Checks all parts in the active page to see if they implement <code>ISetSelectionTarget</code>,
     * either directly or as an adapter. If so, tells the part to select and reveal the
     * specified resource.
     * </p>
     *
     * @param resource the resource to be selected and revealed
     * @param window the workbench window to select and reveal the resource
     * 
     * @see ISetSelectionTarget
     */
    @SuppressWarnings("unchecked")
	public static void selectAndReveal(IResource resource,
            IWorkbenchWindow window) {
        // validate the input
        if (window == null || resource == null) {
			return;
		}
        IWorkbenchPage page = window.getActivePage();
        if (page == null) {
			return;
		}

        // get all the view and editor parts
        List parts = new ArrayList();
        IWorkbenchPartReference refs[] = page.getViewReferences();
        for (int i = 0; i < refs.length; i++) {
            IWorkbenchPart part = refs[i].getPart(false);
            if (part != null) {
				parts.add(part);
			}
        }
        refs = page.getEditorReferences();
        for (int i = 0; i < refs.length; i++) {
            if (refs[i].getPart(false) != null) {
				parts.add(refs[i].getPart(false));
			}
        }

        final ISelection selection = new StructuredSelection(resource);
        Iterator itr = parts.iterator();
        while (itr.hasNext()) {
            IWorkbenchPart part = (IWorkbenchPart) itr.next();

            // get the part's ISetSelectionTarget implementation
            ISetSelectionTarget target = null;
            if (part instanceof ISetSelectionTarget) {
				target = (ISetSelectionTarget) part;
			} else {
				target = (ISetSelectionTarget) part
                        .getAdapter(ISetSelectionTarget.class);
			}

            if (target != null) {
                // select and reveal resource
                final ISetSelectionTarget finalTarget = target;
                window.getShell().getDisplay().asyncExec(new Runnable() {
                    @Override
					public void run() {
                        finalTarget.selectReveal(selection);
                    }
                });
            }
        }
    }
    
    public static void openError(Shell parent, String title, String message,
            PartInitException exception) {
        // Check for a nested CoreException
        CoreException nestedException = null;
        IStatus status = exception.getStatus();
        if (status != null && status.getException() instanceof CoreException) {
			nestedException = (CoreException) status.getException();
		}

        if (nestedException != null) {
            // Open an error dialog and include the extra
            // status information from the nested CoreException
            ErrorDialog.openError(parent, title, message, nestedException
                    .getStatus());
        } else {
            // Open a regular error dialog since there is no
            // extra information to display. Don't use SWT.SHEET because
        	// we don't know if the title contains important information.
            MessageDialog.openError(parent, title, message);
        }
    }
    
	public static ArrayList<IResource> getChangedResources(IResourceDelta resourceDelta) {
		ArrayList<IResource> resources = new ArrayList<IResource>();
		if (resourceDelta != null) {
			IResourceDelta[] affectedChildren = resourceDelta.getAffectedChildren();
			if (affectedChildren.length == 0) {
				resources.add(resourceDelta.getResource());
			} else {
				for (IResourceDelta affectedChild : affectedChildren) {
					resources.addAll(getChangedResources(affectedChild));
				}
			}
		}
		return resources;
	}
	
	/**
	 * Create a set of markers on a resource from a list of marker attribute maps
	 * 
	 * @param resource the resource to create the markers on
	 * @param markerId the id of the marker to create
	 * @param attributeMaps a list of maps giving the keys and values of the attributes for each marker
	 * @param monitor a fresh progress monitor, or null 
	 * @throws CoreException
	 */
	public static void createMarkersFromAttributeMaps(IResource resource, String markerId, List<Map<String, Object>> attributeMaps, IProgressMonitor monitor) throws CoreException {
		try {
			monitor = Policy.monitorFor(monitor);
			monitor.beginTask("Creating markers for " + resource.getName(), attributeMaps.size());
			for (Map<String, Object> attributes : attributeMaps) {
				Policy.checkCanceled(monitor);
				IMarker marker = resource.createMarker(markerId);
				marker.setAttributes(attributes);
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}
	}

	/**
	 * When there are a lot of markers on some of the resources,
	 * the default remove behavior can be ridiculously slow.
	 * Any optimization is possible when all the markers that are
	 * on a resource are going to be removed.  This implements
	 * that optimization.
	 *
	 * This is a replacement for:
	 *   resource.deleteMarkers(type, true, depth)
	 *   
	 * @param resource
	 * @param markerType
	 * @param depth
	 * @param monitor a fresh progress monitor, or null
	 * @throws CoreException
	 */
	@SuppressWarnings("restriction")
	public static void fastRemoveMarkers(final IResource resource, final String markerType, int depth, IProgressMonitor monitor) throws CoreException {
		monitor = Policy.monitorFor(monitor);
		if (resource.exists()) {
			final List<IResource> resourcesWhoseMarkersAllMatch = new ArrayList<IResource>();
			final List<IResource> resourcesThatHaveSomeMatchingMarkers = new ArrayList<IResource>();
			resource.accept(new IResourceProxyVisitor() {
				@Override
				public boolean visit(IResourceProxy proxy) throws CoreException {
					if (proxy.getType() == IResource.FILE) {
						IResource resource = proxy.requestResource();
						IMarker[] markers = resource.findMarkers(null, true, IResource.DEPTH_ZERO);
						if (markers.length > 0) {
							boolean allMarkersMatch = true;
							boolean atLeastOneMarkerMatches = false;
							for (IMarker marker : markers) {
								if (!marker.isSubtypeOf(markerType)) {
									allMarkersMatch = false;
								} else {
									atLeastOneMarkerMatches = true;
								}
								if (atLeastOneMarkerMatches && !allMarkersMatch) {
									break;
								}
							}
							if (allMarkersMatch) {
								resourcesWhoseMarkersAllMatch.add(resource);
							} else if (atLeastOneMarkerMatches) {
								resourcesThatHaveSomeMatchingMarkers.add(resource);
							}
						}
						return false;
					}
					return true;
				}
			}, depth);
			Policy.checkCanceled(monitor);
			final int work = resourcesWhoseMarkersAllMatch.size() + resourcesThatHaveSomeMatchingMarkers.size();
			if (work > 0) {
				final org.eclipse.core.internal.resources.Workspace workspace = (org.eclipse.core.internal.resources.Workspace) ResourcesPlugin.getWorkspace();
				workspace.run(new IWorkspaceRunnable() {
					@Override
					@SuppressWarnings("unused")
					public void run(IProgressMonitor monitor) throws CoreException {
						monitor.beginTask("Removing markers", work);
						org.eclipse.core.internal.resources.MarkerManager markerManager = workspace.getMarkerManager();
						for (IResource resource : resourcesWhoseMarkersAllMatch) {
							Policy.checkCanceled(monitor);
							markerManager.removeMarkers(resource, null, true, IResource.DEPTH_ZERO);
							monitor.worked(1);
						}
						for (IResource resource : resourcesThatHaveSomeMatchingMarkers) {
							Policy.checkCanceled(monitor);
							markerManager.removeMarkers(resource, markerType, true, IResource.DEPTH_ZERO);
							monitor.worked(1);
						}
						monitor.done();
					}
				}, resource, IWorkspace.AVOID_UPDATE, monitor);
			} else if (monitor != null) {
				monitor.beginTask("", 1);
				monitor.done();
			}
		}
	}
	
	/**
	 * If resource or any of its ancestors in the workspace are linked resources and a resource exits in the workspace 
	 * that is the target of the linkage, return that resource; otherwise return the original resource
	 * 
	 * @param resource a potentially linked resource
	 * @return the result of resolving the linkage or the original resource
	 */
	public static IResource resolveLinkedResource(IResource resource) {
		return resolveLinkedResource(resource, resource);
	}
	
	private static IResource resolveLinkedResource(IResource ancestorResource, IResource startResource) {
		if (ancestorResource.isLinked()) {
			IPath ancestorLocation = ancestorResource.getLocation();
			IProject project = ancestorResource.getProject();
			IPath projectLocation = ancestorResource.getProject().getLocation();
			if (projectLocation.isPrefixOf(ancestorLocation)) {
				int projectSegmentCount = projectLocation.segmentCount();
				IPath ancestorRelative = startResource.getFullPath().makeRelativeTo(ancestorResource.getFullPath());
				IPath projectRelative = ancestorLocation.removeFirstSegments(projectSegmentCount).append(ancestorRelative);
				IResource linkedResource = project.findMember(projectRelative);
				return linkedResource != null?linkedResource:startResource;
			}
		}
		IContainer parent = ancestorResource.getParent();
		if (parent != null) {
			return resolveLinkedResource(parent, startResource);
		}
		return startResource;
	}

}
