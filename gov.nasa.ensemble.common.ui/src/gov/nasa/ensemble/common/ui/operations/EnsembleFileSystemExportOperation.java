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
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IOverwriteQuery;



/**
 *	Operation for exporting the contents of a resource to the local file system.
 */
@SuppressWarnings("restriction")
public class EnsembleFileSystemExportOperation implements IRunnableWithProgress {
    private IPath path;

    protected IProgressMonitor monitor;

    private EnsembleFileSystemExporter exporter;

    private List resourcesToExport;

    private IOverwriteQuery overwriteCallback;

    private IResource resource;

    private List<IStatus> errorTable = new ArrayList<IStatus>(1);

    //The constants for the overwrite 3 state
    private static final int OVERWRITE_NOT_SET = 0;

    private static final int OVERWRITE_NONE = 1;

    private static final int OVERWRITE_ALL = 2;

    private int overwriteState = OVERWRITE_NOT_SET;

    private boolean createLeadupStructure = true;

    private boolean createContainerDirectories = true;

	protected boolean displayFinishedDialog = true;
    
    // per SPF-5901, we want to hide the confirmation message if at least one export has been cancelled
    private static boolean cancelledOnce = false;
    
    private static IResource currentResource = null;

    public static IResource getCurrentResource() {
    	return currentResource;
    }

    /**
     * Subclasses can override to return their own custom exporter.
     * @return an exporter to perform the exporting
     */
    protected EnsembleFileSystemExporter getEnsembleFileSystemExporter() {
    	if(exporter == null) {
    		exporter = new EnsembleFileSystemExporter();
    	}
    	return exporter;
    }
    
    /**
     *  Create an instance of this class.  Use this constructor if you wish to
     *  recursively export a single resource
     */
    public EnsembleFileSystemExportOperation(IResource res, String destinationPath,
            IOverwriteQuery overwriteImplementor) {
        super();
        resource = res;
        path = new Path(destinationPath);
        overwriteCallback = overwriteImplementor;
        cancelledOnce = false;
        exporter = getEnsembleFileSystemExporter();
    }

    /**
     *  Create an instance of this class.  Use this constructor if you wish to
     *  export specific resources with a common parent resource (affects container
     *  directory creation)
     */
    public EnsembleFileSystemExportOperation(IResource res, List resources,
            String destinationPath, IOverwriteQuery overwriteImplementor) {
        this(res, destinationPath, overwriteImplementor);
        resourcesToExport = resources;
        cancelledOnce = false;
    }

    /**
     *  Answer the total number of file resources that exist at or below self in the
     *  resources hierarchy.
     *
     *  @return int
     *  @param parentResource org.eclipse.core.resources.IResource
     */
    protected int countChildrenOf(IResource parentResource)
            throws CoreException {
        if (parentResource.getType() == IResource.FILE) {
        	if( isAcceptableFileType( (IFile) parentResource) ){
        		return 1;
        	}else {
        		return 0;
        	}
		}

        int count = 0;
        if (parentResource.isAccessible()) {
            IResource[] children = ((IContainer) parentResource).members();
            for (int i = 0; i < children.length; i++) {
				count += countChildrenOf(children[i]);
			}
        }

        return count;
    }

    /**
     *	Answer a boolean indicating the number of file resources that were
     *	specified for export
     *
     *	@return int
     */
    protected int countSelectedResources() throws CoreException {
        int result = 0;
        Iterator resources = resourcesToExport.iterator();

        while (resources.hasNext()) {
			result += countChildrenOf((IResource) resources.next());
		}

        return result;
    }

    /**
     *  Create the directories required for exporting the passed resource,
     *  based upon its container hierarchy
     *
     *  @param childResource org.eclipse.core.resources.IResource
     */
    protected void createLeadupDirectoriesFor(IResource childResource) {
        IPath resourcePath = childResource.getFullPath().removeLastSegments(1);

        for (int i = 0; i < resourcePath.segmentCount(); i++) {
            path = path.append(resourcePath.segment(i));
            exporter.createFolder(path);
        }
    }

    /**
     *	Recursively export the previously-specified resource
     */
    protected void exportAllResources() throws InterruptedException {
        if (resource.getType() == IResource.FILE) {
			exportFile((IFile) resource, path);
		} else {
            try {
                exportChildren(((IContainer) resource).members(), path);
            } catch (CoreException e) {
                // not safe to show a dialog
                // should never happen because the file system export wizard ensures that the
                // single resource chosen for export is both existent and accessible
                errorTable.add(e.getStatus());
            }
        }
    }

    /**
     *	Export all of the resources contained in the passed collection
     *
     *	@param children java.util.Enumeration
     *	@param currentPath IPath
     */
    protected void exportChildren(IResource[] children, IPath currentPath)
            throws InterruptedException {
        for (int i = 0; i < children.length; i++) {
            IResource child = children[i];
            if (!child.isAccessible()) {
				continue;
			}

            if (child.getType() == IResource.FILE) {
				exportFile((IFile) child, currentPath);
			} else {
                IPath destination = currentPath.append(child.getName());
                exporter.createFolder(destination);
                try {
                    exportChildren(((IContainer) child).members(), destination);
                    exporter.copyFileProperties(child, destination);
                } catch (CoreException e) {
                    // not safe to show a dialog
                    // should never happen because:
                    // i. this method is called recursively iterating over the result of #members,
                    //		which only answers existing children
                    // ii. there is an #isAccessible check done before #members is invoked
                    errorTable.add(e.getStatus());
                } catch (IOException e) {
                	errorTable.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0, e.getMessage(), e));
				}
            }
        }
    }

    /**
     * subclasses can customize to return their own custom string path.
     * This is useful in a case where a file with an extension "a" has been
     * selected, but the desired exported file is the same file but with an
     * extension "b"
     * 
     * @param file the source file
     * @param location the location (folder) where the new file should be created.
     * @return the string path pointing to the (file) resource which should be created
     */
    protected String getProperPathString(IFile file, IPath location){
    	IPath fullPath = location.append(file.getName());
    	String properPathString = fullPath.toOSString();
    	return properPathString;
    }
    
    /**
     *  Export the passed file to the specified location
     *
     *  @param file org.eclipse.core.resources.IFile
     *  @param location org.eclipse.core.runtime.IPath
     */
    protected void exportFile(IFile file, IPath location)
            throws InterruptedException {
    	if( ! isAcceptableFileType(file) ){
    		return;
    	}
        monitor.subTask(file.getFullPath().toString());
        IPath fullPath = location.append(file.getName());
        String properPathString = getProperPathString(file, location);
        File targetFile = new File(properPathString);

        if (targetFile.exists()) {
            if (!targetFile.canWrite()) {
                errorTable.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID,
                        0, NLS.bind("Cannot overwrite file: {0}", targetFile.getAbsolutePath()),
                        null));
                monitor.worked(1);
                return;
            }

            if (overwriteState == OVERWRITE_NONE) {
            	monitor.worked(1);
				return;
			}

            if (overwriteState != OVERWRITE_ALL) {
                String overwriteAnswer = overwriteCallback
                        .queryOverwrite(properPathString);

                if (overwriteAnswer.equals(IOverwriteQuery.CANCEL)) {
					throw new InterruptedException();
				}

                if (overwriteAnswer.equals(IOverwriteQuery.NO)) {
                	ModalContext.checkCanceled(monitor);
                    monitor.worked(1);
                    return;
                }

                if (overwriteAnswer.equals(IOverwriteQuery.NO_ALL)) {
                    monitor.worked(1);
                    overwriteState = OVERWRITE_NONE;
                    return;
                }

                if (overwriteAnswer.equals(IOverwriteQuery.ALL)) {
					overwriteState = OVERWRITE_ALL;
				}
            }
        }

        try {
            exporter.write(file, fullPath, monitor);
        } catch (IOException e) {
            errorTable.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0,
                    NLS.bind("Error exporting {0}: {1}", fullPath, e.getMessage()), e));
        } catch (CoreException e) {
            errorTable.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0,
                    NLS.bind("Error exporting {0}: {1}", fullPath, e.getMessage()), e));
        }

        monitor.worked(1);
        ModalContext.checkCanceled(monitor);
    }

    /**
     *	Export the resources contained in the previously-defined
     *	resourcesToExport collection
     */
    protected void exportSpecifiedResources() throws InterruptedException {
        Iterator resources = resourcesToExport.iterator();
        IPath initPath = (IPath) path.clone();
        currentResource = null;
        while (resources.hasNext()) {
            currentResource = (IResource) resources.next();
            if (!currentResource.isAccessible()) {
				continue;
			}

            path = initPath;

            if (resource == null) {
                // No root resource specified and creation of containment directories
                // is required.  Create containers from depth 2 onwards (ie.- project's
                // child inclusive) for each resource being exported.
                if (createLeadupStructure) {
					createLeadupDirectoriesFor(currentResource);
				}

            } else {
                // Root resource specified.  Must create containment directories
                // from this point onwards for each resource being exported
                IPath containersToCreate = currentResource.getFullPath()
                        .removeFirstSegments(
                                resource.getFullPath().segmentCount())
                        .removeLastSegments(1);

                for (int i = 0; i < containersToCreate.segmentCount(); i++) {
                    path = path.append(containersToCreate.segment(i));
                    exporter.createFolder(path);
                }
            }

            if (currentResource.getType() == IResource.FILE) {
				exportFile((IFile) currentResource, path);
			} else {
				
				if (createContainerDirectories) {
                    path = path.append(currentResource.getName());
                    exporter.createFolder(path);
                }

                try {
                    exportChildren(((IContainer) currentResource).members(),
                            path);
                    
                    /*
                     * Set the last modified attribute on the new project folder to match 
                     * the last modified of the currentResource (Project)
                    */
                	if(currentResource.getType() == IResource.PROJECT) {
               			exporter.copyFileProperties(currentResource, path);
                	}
                } catch (CoreException e) {
                    // should never happen because #isAccessible is called before #members is invoked,
                    // which implicitly does an existence check
                    errorTable.add(e.getStatus());
                } catch (IOException e) {
                	errorTable.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0, e.getMessage(), e));
				}
            }
        }
    }
  
    /**
     * Normally this method wouldn't be required, however, in the case where
     * we are trying to call performFimish() without actually having a dialog open,
     * we need this method to get a handle to the appropriate shell.
     * @return a shell which can be used as a parent for a message dialog.
     */
	private Shell getShellForMessageDialog() {
		Shell shell = null;

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if(activeWorkbenchWindow == null) {
			IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
			if(workbenchWindows != null && workbenchWindows.length > 0) {
				shell = workbenchWindows[0].getShell();
			}
		}

		else {
			shell = activeWorkbenchWindow.getShell();
		}

		return shell;
	}    
    
	protected boolean queryYesNoQuestion(String message) {
            MessageDialog dialog = new MessageDialog(getShellForMessageDialog(),
            		"Question",
                    (Image) null, message, MessageDialog.NONE,
                    new String[] { IDialogConstants.YES_LABEL,
                            IDialogConstants.NO_LABEL }, 0) {
            	@Override
				protected int getShellStyle() {
            		return super.getShellStyle() | SWT.SHEET;
            	}
            };
            // ensure yes is the default

            return dialog.open() == 0;
        }    

    /**
     * Returns the status of the export operation.
     * If there were any errors, the result is a status object containing
     * individual status objects for each error.
     * If there were no errors, the result is a status object with error code <code>OK</code>.
     *
     * @return the status
     */
    public IStatus getStatus() {
        IStatus[] errors = new IStatus[errorTable.size()];
        errorTable.toArray(errors);
        return new MultiStatus(
                PlatformUI.PLUGIN_ID,
                IStatus.OK,
                errors,
                "Problems were encountered during export:",
                null);
    }

    /**
     *  Answer a boolean indicating whether the passed child is a descendent
     *  of one or more members of the passed resources collection
     *
     *  @return boolean
     *  @param resources java.util.List
     *  @param child org.eclipse.core.resources.IResource
     */
    protected boolean isDescendent(List resources, IResource child) {
        if (child.getType() == IResource.PROJECT) {
			return false;
		}

        IResource parent = child.getParent();
        if (resources.contains(parent)) {
			return true;
		}

        return isDescendent(resources, parent);
    }

    /**
     *	Export the resources that were previously specified for export
     *	(or if a single resource was specified then export it recursively)
     */
    @Override
	public void run(IProgressMonitor progressMonitor)
            throws InterruptedException {
        this.monitor = progressMonitor;

        if (resource != null) {
            if (createLeadupStructure) {
				createLeadupDirectoriesFor(resource);
			}

            if (createContainerDirectories
                    && resource.getType() != IResource.FILE) {
                // ensure it's a container
                path = path.append(resource.getName());
                exporter.createFolder(path);
            }
        }

        try {
            int totalWork = getTotalWork();
            if(monitor != null) {
            	monitor.beginTask("Exporting:", totalWork);
            }
            if (resourcesToExport == null) {
                exportAllResources();
            } else {
                exportSpecifiedResources();
            }
        } finally {
            if(monitor != null) {
            	monitor.done();
            }
            if( displayFinishedDialog )
            if(!cancelledOnce) {
	            Display display = WidgetUtils.getDisplay();
	            display.asyncExec(new Runnable() {
	            	@Override
					public void run() {
	            		MessageDialog.openInformation(getShellForMessageDialog(), "Export", "Export finished.\n\n"+ path.toOSString());	
	            	}
	            });
            }
            
        }
    }

	protected int getTotalWork() {
		int totalWork = IProgressMonitor.UNKNOWN;
		try {
		    if (resourcesToExport == null) {
				totalWork = countChildrenOf(resource);
			} else {
				totalWork = countSelectedResources();
			}
		} catch (CoreException e) {
		    // Should not happen
		    errorTable.add(e.getStatus());
		}
		return totalWork;
	}

    /**
     *	Set this boolean indicating whether a directory should be created for
     *	Folder resources that are explicitly passed for export
     *
     *	@param value boolean
     */
    public void setCreateContainerDirectories(boolean value) {
        createContainerDirectories = value;
    }

    /**
     *	Set this boolean indicating whether each exported resource's complete path should
     *	include containment hierarchies as dictated by its parents
     *
     *	@param value boolean
     */
    public void setCreateLeadupStructure(boolean value) {
        createLeadupStructure = value;
    }

    /**
     *	Set this boolean indicating whether exported resources should automatically
     *	overwrite existing files when a conflict occurs. If not
     *	query the user.
     *
     *	@param value boolean
     */
    public void setOverwriteFiles(boolean value) {
        if (value) {
			overwriteState = OVERWRITE_ALL;
		}
    }

    
	protected boolean isAcceptableFileType(IFile file) {
		return true;
	}
}
