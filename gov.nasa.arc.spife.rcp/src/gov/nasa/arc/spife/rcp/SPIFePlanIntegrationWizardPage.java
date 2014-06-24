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
package gov.nasa.arc.spife.rcp;

import gov.nasa.arc.spife.rcp.events.OrbitEventUtil;
import gov.nasa.arc.spife.ui.lifecycle.SpifeProjectUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.operation.JobOperationStatus;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.dialogs.WorkingSetGroup;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileManipulations;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.ILeveledImportStructureProvider;
import org.eclipse.ui.internal.wizards.datatransfer.TarEntry;
import org.eclipse.ui.internal.wizards.datatransfer.TarException;
import org.eclipse.ui.internal.wizards.datatransfer.TarFile;
import org.eclipse.ui.internal.wizards.datatransfer.TarLeveledStructureProvider;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * The WizardProjectsImportPage is the page that allows the user to import
 * projects from a particular location.
 */

@SuppressWarnings("restriction")
public class SPIFePlanIntegrationWizardPage extends EnsembleWizardPage implements
		IOverwriteQuery {

	/**
	 * The name of the folder containing metadata information for the workspace.
	 */
	public static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$

	private static final String INTEGRATING_PLANS_MESSAGE = "Select the plans you want to integrate from your workspace.";
	private static final String NO_EVENTS_IN_PLAN_MESSAGE = "Driver plan does not have plan orbital events.";
	private static final String NO_EVENTS_IN_AD_MESSAGE = "Activity Dictionary does not define any event types.";
	private IProject driverProject = null;
	private EPlan driverPlan = null;
	public static final String P_PLAN_REPOSITORY = "plan.repository.location";
	/**
	 * The import structure provider.
	 * 
	 * @since 3.4
	 */
	private ILeveledImportStructureProvider structureProvider;
	
	/**
	 * @since 3.5
	 *
	 */
	private final class ProjectLabelProvider extends LabelProvider implements IFontProvider{
		
		@Override
		public String getText(Object element) {
			return ((ProjectRecord) element).getProjectLabel();
		}

		@Override
		public Font getFont(Object element) {
			ProjectRecord projectRecord = (ProjectRecord) element;
			
			if(projectRecord.hasConflicts) {
				Font styledFont = FontUtils.getStyledFont(SWT.ITALIC);
				return styledFont;
			}
			return null;
		}
		
		
	}

	/**
	 * Class declared public only for test suite.
	 * 
	 */
	public class ProjectRecord {
		File projectSystemFile;

		Object projectArchiveFile;

		String projectName;

		Object parent;

		int level;
		
		boolean hasConflicts;

		IProjectDescription description;

		/**
		 * Create a record for a project based on the info in the file.
		 * 
		 * @param file
		 */
		ProjectRecord(File file) {
			projectSystemFile = file;
			setProjectName();
		}

		/**
		 * @param file
		 * 		The Object representing the .project file
		 * @param parent
		 * 		The parent folder of the .project file
		 * @param level
		 * 		The number of levels deep in the provider the file is
		 */
		ProjectRecord(Object file, Object parent, int level) {
			this.projectArchiveFile = file;
			this.parent = parent;
			this.level = level;
			setProjectName();
		}

		/**
		 * Set the name of the project based on the projectFile.
		 */
		private void setProjectName() {
			try {
				if (projectArchiveFile != null) {
					InputStream stream = structureProvider
							.getContents(projectArchiveFile);

					// If we can get a description pull the name from there
					if (stream == null) {
						if (projectArchiveFile instanceof ZipEntry) {
							IPath path = new Path(
									((ZipEntry) projectArchiveFile).getName());
							projectName = path.segment(path.segmentCount() - 2);
						} else if (projectArchiveFile instanceof TarEntry) {
							IPath path = new Path(
									((TarEntry) projectArchiveFile).getName());
							projectName = path.segment(path.segmentCount() - 2);
						}
					} else {
						description = IDEWorkbenchPlugin.getPluginWorkspace()
								.loadProjectDescription(stream);
						stream.close();
						projectName = description.getName();
					}

				}

				// If we don't have the project name try again
				if (projectName == null) {
					IPath path = new Path(projectSystemFile.getPath());
					// if the file is in the default location, use the directory
					// name as the project name
					if (isDefaultLocation(path)) {
						projectName = path.segment(path.segmentCount() - 2);
						description = IDEWorkbenchPlugin.getPluginWorkspace()
								.newProjectDescription(projectName);
					} else {
						description = IDEWorkbenchPlugin.getPluginWorkspace()
								.loadProjectDescription(path);
						projectName = description.getName();
					}

				}
			} catch (CoreException e) {
				// no good couldn't get the name
			} catch (IOException e) {
				// no good couldn't get the name
			}
		}

		/**
		 * Returns whether the given project description file path is in the
		 * default location for a project
		 * 
		 * @param path
		 * 		The path to examine
		 * @return Whether the given path is the default location for a project
		 */
		private boolean isDefaultLocation(IPath path) {
			// The project description file must at least be within the project,
			// which is within the workspace location
			if (path.segmentCount() < 2)
				return false;
			return path.removeLastSegments(2).toFile().equals(
					Platform.getLocation().toFile());
		}

		/**
		 * Get the name of the project
		 * 
		 * @return String
		 */
		public String getProjectName() {
			return projectName;
		}

		/**
		 * Gets the label to be used when rendering this project record in the
		 * UI.
		 * 
		 * @return String the label
		 * @since 3.4
		 */
		public String getProjectLabel() {
			if (description == null)
				return projectName;

			String path = projectSystemFile == null ? structureProvider
					.getLabel(parent) : projectSystemFile
					.getParent();

			return NLS.bind(
					DataTransferMessages.WizardProjectsImportPage_projectLabel,
					projectName, path);
		}
		
		/**
		 * @return Returns the hasConflicts.
		 */
		public boolean hasConflicts() {
			return hasConflicts;
		}

		public File getProjectSystemFile() {
			return projectSystemFile;
		}
	}

	// dialog store id constants
	private final static String STORE_COPY_PROJECT_ID = "WizardProjectsImportPage.STORE_COPY_PROJECT_ID"; //$NON-NLS-1$

	private final static String STORE_ARCHIVE_SELECTED = "WizardProjectsImportPage.STORE_ARCHIVE_SELECTED"; //$NON-NLS-1$

	private Text directoryPathField;

	private CheckboxTreeViewer projectsList;

	private Button copyCheckbox;

	private boolean copyFiles = true;

	private ProjectRecord[] selectedProjects = new ProjectRecord[0];

	// Keep track of the directory that we browsed to last time
	// the wizard was invoked.
	private static String previouslyBrowsedDirectory = ""; //$NON-NLS-1$

	// Keep track of the archive that we browsed to last time
	// the wizard was invoked.
	private static String previouslyBrowsedArchive = ""; //$NON-NLS-1$

	private Button projectFromDirectoryRadio;

	private Button projectFromArchiveRadio;

	private Text archivePathField;

	private Button browseDirectoriesButton;

	private Button browseArchivesButton;

	private IProject[] wsProjects;

	// constant from WizardArchiveFileResourceImportPage1
	private static final String[] FILE_IMPORT_MASK = {
			"*.jar;*.zip;*.tar;*.tar.gz;*.tgz", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$

	// The initial path to set
	private String initialPath;
	
	// The last selected path to minimize searches
	private String lastPath;
	// The last time that the file or folder at the selected path was modified
	// to mimize searches
	private long lastModified;

	private WorkingSetGroup workingSetGroup;

	/**
	 * Creates a new project creation wizard page.
	 * 
	 */
	public SPIFePlanIntegrationWizardPage(IProject driverProject) {
		this("lassWizardPlanIntegrationPage", null, driverProject); //$NON-NLS-1$
	}

	/**
	 * Create a new instance of the receiver.
	 * 
	 * @param pageName
	 */
	public SPIFePlanIntegrationWizardPage(String pageName, IProject driverProject) {
		this(pageName,null,driverProject);
	}
			
	/**
	 * More (many more) parameters.
	 * 
	 * @param pageName
	 * @param initialPath
	 * @param currentSelection
	 * @since 3.5
	 */
	public SPIFePlanIntegrationWizardPage(String pageName, String initialPath, IProject driverProject) {
 		super(pageName);
		this.initialPath = initialPath;
		this.driverProject = driverProject;
		try {
			this.driverPlan = SpifeProjectUtils.getOrLoadScheduleFromProject(driverProject);
		} catch (IOException e) {
			LogUtil.error(e);
		}
		setPageComplete(false);
		setTitle("Plan Integration");
		setDescription(INTEGRATING_PLANS_MESSAGE);
	}

	@Override
	public void dispose() {
		SpifeProjectUtils.disposeSchedule(driverPlan);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite workArea = new Composite(parent, SWT.NONE);
		setControl(workArea);

		workArea.setLayout(new GridLayout());
		workArea.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		createProjectsRoot(workArea);
		createProjectsList(workArea);
		
		String location = getDefaultPlanRepositoryLocation();
		updateProjectsList(location);
		//createOptionsArea(workArea);
		//createWorkingSetGroup(workArea);
		restoreWidgetValues();
		Dialog.applyDialogFont(workArea);
		
		if (!OrbitEventUtil.eventsAreDefined()) {
			setError(getClass(), NO_EVENTS_IN_AD_MESSAGE);
		} else if (OrbitEventUtil.getPlannedOrbitalEvents(driverPlan).isEmpty()) {
			setWarning(getClass(), NO_EVENTS_IN_PLAN_MESSAGE);
		}


	}

	private String getDefaultPlanRepositoryLocation() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		File file = new File(root.getLocationURI());
		String location = (file.isFile() ? file.getParent() : file.toString());
		return location;
	}
	
	/**
	 * @param workArea
	 */
	/*
	private void createWorkingSetGroup(Composite workArea) {
		String[] workingSetIds = new String[] {"org.eclipse.ui.resourceWorkingSetPage",  //$NON-NLS-1$
				"org.eclipse.jdt.ui.JavaWorkingSetPage"};  //$NON-NLS-1$
		workingSetGroup = new WorkingSetGroup(workArea, currentSelection, workingSetIds);
	}
	*/

	/**
	 * Create the area with the extra options.
	 * 
	 * @param workArea
	 */
	@SuppressWarnings("unused")
	private void createOptionsArea(Composite workArea) {
		Composite optionsGroup = new Composite(workArea, SWT.NONE);
		optionsGroup.setLayout(new GridLayout());
		optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		copyCheckbox = new Button(optionsGroup, SWT.CHECK);
		copyCheckbox
				.setText(DataTransferMessages.WizardProjectsImportPage_CopyProjectsIntoWorkspace);
		copyCheckbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		copyCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copyFiles = copyCheckbox.getSelection();
			}
		});
	}

	/**
	 * Create the checkbox list for the found projects.
	 * 
	 * @param workArea
	 */
	private void createProjectsList(Composite workArea) {

		Label title = new Label(workArea, SWT.NONE);
		title
				.setText(DataTransferMessages.WizardProjectsImportPage_ProjectsListTitle);

		Composite listComposite = new Composite(workArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = false;
		listComposite.setLayout(layout);

		listComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		projectsList = new CheckboxTreeViewer(listComposite, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = new PixelConverter(projectsList.getControl()).convertWidthInCharsToPixels(25);
		gridData.heightHint = new PixelConverter(projectsList.getControl()).convertHeightInCharsToPixels(10);
		projectsList.getControl().setLayoutData(gridData);
		
		/*
		 * compare the folders containing the .project files, not the .project files themselves
		 */
		final Comparator comparator = new Comparator() {
			@Override
			public int compare(Object arg0, Object arg1) {
				ProjectRecord pr0 = (ProjectRecord)arg0;
				ProjectRecord pr1 = (ProjectRecord)arg1;
				File projectSystemFile0 = pr0.getProjectSystemFile();
				File projectFolder0 = projectSystemFile0.getParentFile();
				
				File projectSystemFile1 = pr1.getProjectSystemFile();
				File projectFolder1 = projectSystemFile1.getParentFile();
				
				long lastModified0 = projectFolder0.lastModified();
				long lastModified1 = projectFolder1.lastModified();
				
				int result = 0;
				if(lastModified0 < lastModified1) {
					result = 1;
				} else if(lastModified0 > lastModified1){
					result = -1;
				}
				return result;
			}
		};
		
		projectsList.setContentProvider(new ITreeContentProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java
			 * .lang.Object)
			 */
			@Override
			public Object[] getChildren(Object parentElement) {
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements
			 * (java.lang.Object)
			 */
			@Override
			public Object[] getElements(Object inputElement) {
				ProjectRecord[] projectRecords = getProjectRecords();
				Arrays.sort(projectRecords, comparator);
				return projectRecords;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java
			 * .lang.Object)
			 */
			@Override
			public boolean hasChildren(Object element) {
				return false;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java
			 * .lang.Object)
			 */
			@Override
			public Object getParent(Object element) {
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
			 */
			@Override
			public void dispose() {
				// do nothing
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
			 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// do nothing
			}

		});

		projectsList.setLabelProvider(new ProjectLabelProvider());

		projectsList.addCheckStateListener(new ICheckStateListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged
			 * (org.eclipse.jface.viewers.CheckStateChangedEvent)
			 */
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				
				//ProjectRecord element = (ProjectRecord) event.getElement();		
				Object[] checkedElements = projectsList.getCheckedElements();
				boolean foundConflict = false;
				for(Object object :checkedElements) {
					ProjectRecord projectRecord = (ProjectRecord)object;
					if(projectRecord.hasConflicts) {
						setWarning(getClass(),"One or more of the selected projects already exists in the workspace and will be overwritten");
						foundConflict = true;
						break;
					}
				}
				
				if(!foundConflict) {
					setMessage(INTEGRATING_PLANS_MESSAGE);
				}
				
				/*
				if(element.hasConflicts) {
					projectsList.setChecked(element, false);					
				}*/
				
				setPageComplete(checkedElements.length > 0);
			}
		});

		projectsList.setInput(this);
		//projectsList.setComparator(new ViewerComparator());
		createSelectionButtons(listComposite);
	}

	/**
	 * Create the selection buttons in the listComposite.
	 * 
	 * @param listComposite
	 */
	private void createSelectionButtons(Composite listComposite) {
		Composite buttonsComposite = new Composite(listComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		buttonsComposite.setLayout(layout);

		buttonsComposite.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));

		Button selectAll = new Button(buttonsComposite, SWT.PUSH);
		selectAll.setText(DataTransferMessages.DataTransfer_selectAll);
		selectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < selectedProjects.length; i++) {
					if(selectedProjects[i].hasConflicts)
						projectsList.setChecked(selectedProjects[i], false);
					else
						projectsList.setChecked(selectedProjects[i], true);
				}
				setPageComplete(projectsList.getCheckedElements().length > 0);
			}
		});
		Dialog.applyDialogFont(selectAll);
		setButtonLayoutData(selectAll);

		Button deselectAll = new Button(buttonsComposite, SWT.PUSH);
		deselectAll.setText(DataTransferMessages.DataTransfer_deselectAll);
		deselectAll.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {

				projectsList.setCheckedElements(new Object[0]);
				setPageComplete(false);
			}
		});
		Dialog.applyDialogFont(deselectAll);
		setButtonLayoutData(deselectAll);

		Button refresh = new Button(buttonsComposite, SWT.PUSH);
		refresh.setText(DataTransferMessages.DataTransfer_refresh);
		refresh.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (projectFromDirectoryRadio != null && projectFromDirectoryRadio.getSelection()) {
					updateProjectsList(directoryPathField.getText().trim());
				} else if(archivePathField != null){
					updateProjectsList(archivePathField.getText().trim());
				}
			}
		});
		Dialog.applyDialogFont(refresh);
		setButtonLayoutData(refresh);
	}

	/**
	 * Create the area where you select the root directory for the projects.
	 * 
	 * @param workArea
	 * 		Composite
	 */
	private void createProjectsRoot(Composite workArea) {

		// project specification group
		/*
		Composite projectGroup = new Composite(workArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = false;
		layout.marginWidth = 0;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		*/

		// new project from directory radio button
		/*
		projectFromDirectoryRadio = new Button(projectGroup, SWT.RADIO);
		projectFromDirectoryRadio
				.setText(DataTransferMessages.WizardProjectsImportPage_RootSelectTitle);
				*/

		// project location entry field
		/*
		this.directoryPathField = new Text(projectGroup, SWT.BORDER);

		GridData directoryPathData = new GridData(SWT.FILL, SWT.NONE, true, false);
		directoryPathData.widthHint = new PixelConverter(directoryPathField).convertWidthInCharsToPixels(25);
		directoryPathField.setLayoutData(directoryPathData);
		*/
		
		// browse button
		/*
		browseDirectoriesButton = new Button(projectGroup, SWT.PUSH);
		browseDirectoriesButton
				.setText(DataTransferMessages.DataTransfer_browse);
		setButtonLayoutData(browseDirectoriesButton);
		*/

		// new project from archive radio button
		/*
		projectFromArchiveRadio = new Button(projectGroup, SWT.RADIO);
		projectFromArchiveRadio
				.setText(DataTransferMessages.WizardProjectsImportPage_ArchiveSelectTitle);
		*/
		
		// project location entry field

/*		archivePathField = new Text(projectGroup, SWT.BORDER);

		GridData archivePathData = new GridData(SWT.FILL, SWT.NONE, true, false);
		archivePathData.widthHint = new PixelConverter(archivePathField).convertWidthInCharsToPixels(25);
		archivePathField.setLayoutData(archivePathData); // browse button
		browseArchivesButton = new Button(projectGroup, SWT.PUSH);
		browseArchivesButton.setText(DataTransferMessages.DataTransfer_browse);
		setButtonLayoutData(browseArchivesButton);

		projectFromDirectoryRadio.setSelection(true);
		archivePathField.setEnabled(false);
		browseArchivesButton.setEnabled(false);

		browseDirectoriesButton.addSelectionListener(new SelectionAdapter() {
			
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetS
			 * elected(org.eclipse.swt.events.SelectionEvent)
			 
			public void widgetSelected(SelectionEvent e) {
				handleLocationDirectoryButtonPressed();
			}

		});

		browseArchivesButton.addSelectionListener(new SelectionAdapter() {
			
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 
			public void widgetSelected(SelectionEvent e) {
				handleLocationArchiveButtonPressed();
			}

		});

		directoryPathField.addTraverseListener(new TraverseListener() {

			
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse
			 * .swt.events.TraverseEvent)
			 
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
					updateProjectsList(directoryPathField.getText().trim());
				}
			}

		});

		directoryPathField.addFocusListener(new FocusAdapter() {

			
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt
			 * .events.FocusEvent)
			 
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				updateProjectsList(directoryPathField.getText().trim());
			}

		});

		archivePathField.addTraverseListener(new TraverseListener() {

			
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse
			 * .swt.events.TraverseEvent)
			 
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
					updateProjectsList(archivePathField.getText().trim());
				}
			}

		});

		archivePathField.addFocusListener(new FocusAdapter() {
			
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt
			 * .events.FocusEvent)
			 
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				updateProjectsList(archivePathField.getText().trim());
			}
		});

		projectFromDirectoryRadio.addSelectionListener(new SelectionAdapter() {
			
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 
			public void widgetSelected(SelectionEvent e) {
				directoryRadioSelected();
			}
		});

		projectFromArchiveRadio.addSelectionListener(new SelectionAdapter() {
			
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 
			public void widgetSelected(SelectionEvent e) {
				archiveRadioSelected();
			}
		});*/
	}

	private void archiveRadioSelected() {
		if (projectFromArchiveRadio != null && projectFromArchiveRadio.getSelection()) {
			directoryPathField.setEnabled(false);
			browseDirectoriesButton.setEnabled(false);
			archivePathField.setEnabled(true);
			browseArchivesButton.setEnabled(true);
			updateProjectsList(archivePathField.getText());
			archivePathField.setFocus();
			copyCheckbox.setSelection(true);
			copyCheckbox.setEnabled(false);
		}
	}

	private void directoryRadioSelected() {
		if (projectFromDirectoryRadio != null && projectFromDirectoryRadio.getSelection()) {
			directoryPathField.setEnabled(true);
			browseDirectoriesButton.setEnabled(true);
			archivePathField.setEnabled(false);
			browseArchivesButton.setEnabled(false);
			updateProjectsList(directoryPathField.getText());
			directoryPathField.setFocus();
			copyCheckbox.setEnabled(true);
			copyCheckbox.setSelection(copyFiles);
		}
	}

	/*
	 * (non-Javadoc) Method declared on IDialogPage. Set the focus on path
	 * fields when page becomes visible.
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible && this.projectFromDirectoryRadio != null
				&& this.projectFromDirectoryRadio.getSelection()) {
			this.directoryPathField.setFocus();
		}
		if (visible && this.projectFromArchiveRadio != null
				&& this.projectFromArchiveRadio.getSelection()) {
			this.archivePathField.setFocus();
		}
	}

	/**
	 * Update the list of projects based on path. Method declared public only
	 * for test suite.
	 * 
	 * @param path
	 */
	public void updateProjectsList(final String path) {
		// on an empty path empty selectedProjects
		if (path == null || path.length() == 0) {
			setMessage(INTEGRATING_PLANS_MESSAGE);
			selectedProjects = new ProjectRecord[0];
			projectsList.refresh(true);
			projectsList.setCheckedElements(selectedProjects);
			setPageComplete(projectsList.getCheckedElements().length > 0);
			lastPath = path;
			return;
		}

		final File directory = new File(path);
		long modified = directory.lastModified();
		if (path.equals(lastPath) && lastModified == modified) {
			// since the file/folder was not modified and the path did not
			// change, no refreshing is required
			return;
		}

		lastPath = path;
		lastModified = modified;

		// We can't access the radio button from the inner class so get the
		// status beforehand
		boolean directorySelected = true;
		if(projectFromDirectoryRadio != null) {
			directorySelected = projectFromDirectoryRadio.getSelection();
		}
		final boolean dirSelected = directorySelected;
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.jface.operation.IRunnableWithProgress#run(org
				 * .eclipse.core.runtime.IProgressMonitor)
				 */
				@Override
				public void run(IProgressMonitor monitor) {

					monitor
							.beginTask(
									DataTransferMessages.WizardProjectsImportPage_SearchingMessage,
									100);
					selectedProjects = new ProjectRecord[0];
					Collection files = new ArrayList();
					monitor.worked(10);
					if (!dirSelected
							&& ArchiveFileManipulations.isTarFile(path)) {
						TarFile sourceTarFile = getSpecifiedTarSourceFile(path);
						if (sourceTarFile == null) {
							return;
						}

						structureProvider = new TarLeveledStructureProvider(
								sourceTarFile);
						Object child = structureProvider.getRoot();

						if (!collectProjectFilesFromProvider(files, child, 0,
								monitor)) {
							return;
						}
						Iterator filesIterator = files.iterator();
						selectedProjects = new ProjectRecord[files.size()];
						int index = 0;
						monitor.worked(50);
						monitor
								.subTask(DataTransferMessages.WizardProjectsImportPage_ProcessingMessage);
						while (filesIterator.hasNext()) {
							selectedProjects[index++] = (ProjectRecord) filesIterator
									.next();
						}
					} else if (!dirSelected
							&& ArchiveFileManipulations.isZipFile(path)) {
						ZipFile sourceFile = getSpecifiedZipSourceFile(path);
						if (sourceFile == null) {
							return;
						}
						structureProvider = new ZipLeveledStructureProvider(
								sourceFile);
						Object child = structureProvider.getRoot();

						if (!collectProjectFilesFromProvider(files, child, 0,
								monitor)) {
							return;
						}
						Iterator filesIterator = files.iterator();
						selectedProjects = new ProjectRecord[files.size()];
						int index = 0;
						monitor.worked(50);
						monitor
								.subTask(DataTransferMessages.WizardProjectsImportPage_ProcessingMessage);
						while (filesIterator.hasNext()) {
							selectedProjects[index++] = (ProjectRecord) filesIterator
									.next();
						}
					}

					else if (dirSelected && directory.isDirectory()) {

						if (!collectProjectFilesFromDirectory(files, directory,
								null, monitor)) {
							return;
						}
						Iterator filesIterator = files.iterator();
						selectedProjects = new ProjectRecord[files.size()];
						int index = 0;
						monitor.worked(50);
						monitor
								.subTask(DataTransferMessages.WizardProjectsImportPage_ProcessingMessage);
						while (filesIterator.hasNext()) {
							File file = (File) filesIterator.next();
							selectedProjects[index] = new ProjectRecord(file);
							index++;
						}
					} else {
						monitor.worked(60);
					}
					monitor.done();
				}

			});
		} catch (InvocationTargetException e) {
			IDEWorkbenchPlugin.log(e.getMessage(), e);
		} catch (InterruptedException e) {
			// Nothing to do if the user interrupts.
		}

		projectsList.refresh(true);
		ProjectRecord[] projects = getProjectRecords();
		boolean displayWarning = false;
		for (int i = 0; i < projects.length; i++) {
			if(projects[i].hasConflicts) {
				displayWarning = true;
				//projectsList.setGrayed(projects[i], true);
			}else {
				//SPF-6076 Do not check any projects by default for import from plan repository
				//projectsList.setChecked(projects[i], true);
			}
		}
		
		if (displayWarning) {
			/*setMessage(
					DataTransferMessages.WizardProjectsImportPage_projectsInWorkspace,
					WARNING);
					*/
		} else {
			setMessage(INTEGRATING_PLANS_MESSAGE);
		}
		setPageComplete(projectsList.getCheckedElements().length > 0);
		if(selectedProjects.length == 0) {
			setWarning(getClass(),  "No projects are found to integrate.");
		}
	}

	/**
	 * Answer a handle to the zip file currently specified as being the source.
	 * Return null if this file does not exist or is not of valid format.
	 */
	private ZipFile getSpecifiedZipSourceFile(String fileName) {
		if (fileName.length() == 0) {
			return null;
		}

		try {
			return new ZipFile(fileName);
		} catch (ZipException e) {
			displayErrorDialog(DataTransferMessages.ZipImport_badFormat);
		} catch (IOException e) {
			displayErrorDialog(DataTransferMessages.ZipImport_couldNotRead);
		}

		archivePathField.setFocus();
		return null;
	}

	/**
	 * Answer a handle to the zip file currently specified as being the source.
	 * Return null if this file does not exist or is not of valid format.
	 */
	private TarFile getSpecifiedTarSourceFile(String fileName) {
		if (fileName.length() == 0) {
			return null;
		}

		try {
			return new TarFile(fileName);
		} catch (TarException e) {
			displayErrorDialog(DataTransferMessages.TarImport_badFormat);
		} catch (IOException e) {
			displayErrorDialog(DataTransferMessages.ZipImport_couldNotRead);
		}

		archivePathField.setFocus();
		return null;
	}

	/**
	 * Display an error dialog with the specified message.
	 * 
	 * @param message
	 * 		the error message
	 */
	protected void displayErrorDialog(String message) {
		MessageDialog.open(MessageDialog.ERROR, getContainer().getShell(),
				getErrorDialogTitle(), message, SWT.SHEET);
	}

	/**
	 * Get the title for an error dialog. Subclasses should override.
	 */
	protected String getErrorDialogTitle() {
		return IDEWorkbenchMessages.WizardExportPage_internalErrorTitle;
	}

	/**
	 * Collect the list of .project files that are under directory into files.
	 * 
	 * @param files
	 * @param directory
	 * @param directoriesVisited
	 * 		Set of canonical paths of directories, used as recursion guard
	 * @param monitor
	 * 		The monitor to report to
	 * @return boolean <code>true</code> if the operation was completed.
	 */
	private boolean collectProjectFilesFromDirectory(Collection files,
			File directory, Set directoriesVisited, IProgressMonitor monitor) {

		if (monitor.isCanceled()) {
			return false;
		}
		monitor.subTask(NLS.bind(
				DataTransferMessages.WizardProjectsImportPage_CheckingMessage,
				directory.getPath()));
		File[] contents = directory.listFiles();
		if (contents == null)
			return false;

		// Initialize recursion guard for recursive symbolic links
		if (directoriesVisited == null) {
			directoriesVisited = new HashSet();
			try {
				directoriesVisited.add(directory.getCanonicalPath());
			} catch (IOException exception) {
				StatusManager.getManager().handle(
						StatusUtil.newStatus(IStatus.ERROR, exception
								.getLocalizedMessage(), exception));
			}
		}

		// first look for project description files
		final String dotProject = IProjectDescription.DESCRIPTION_FILE_NAME;
		for (int i = 0; i < contents.length; i++) {
			File file = contents[i];
			if (file.isFile() && file.getName().equals(dotProject)) {
				files.add(file);
				// don't search sub-directories since we can't have nested
				// projects
				return true;
			}
		}
		// no project description found, so recurse into sub-directories
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].isDirectory()) {
				if (!contents[i].getName().equals(METADATA_FOLDER)) {
					try {
						String canonicalPath = contents[i].getCanonicalPath();
						if (!directoriesVisited.add(canonicalPath)) {
							// already been here --> do not recurse
							continue;
						}
					} catch (IOException exception) {
						StatusManager.getManager().handle(
								StatusUtil.newStatus(IStatus.ERROR, exception
										.getLocalizedMessage(), exception));

					}
					collectProjectFilesFromDirectory(files, contents[i],
							directoriesVisited, monitor);
				}
			}
		}
		return true;
	}

	/**
	 * Collect the list of .project files that are under directory into files.
	 * 
	 * @param files
	 * @param monitor
	 * 		The monitor to report to
	 * @return boolean <code>true</code> if the operation was completed.
	 */
	private boolean collectProjectFilesFromProvider(Collection files,
			Object entry, int level, IProgressMonitor monitor) {

		if (monitor.isCanceled()) {
			return false;
		}
		monitor.subTask(NLS.bind(
				DataTransferMessages.WizardProjectsImportPage_CheckingMessage,
				structureProvider.getLabel(entry)));
		List children = structureProvider.getChildren(entry);
		if (children == null) {
			children = new ArrayList(1);
		}
		Iterator childrenEnum = children.iterator();
		while (childrenEnum.hasNext()) {
			Object child = childrenEnum.next();
			if (structureProvider.isFolder(child)) {
				collectProjectFilesFromProvider(files, child, level + 1,
						monitor);
			}
			String elementLabel = structureProvider.getLabel(child);
			if (elementLabel.equals(IProjectDescription.DESCRIPTION_FILE_NAME)) {
				files.add(new ProjectRecord(child, entry, level));
			}
		}
		return true;
	}

	/**
	 * The browse button has been selected. Select the location.
	 */
	protected void handleLocationDirectoryButtonPressed() {

		DirectoryDialog dialog = new DirectoryDialog(directoryPathField
				.getShell(), SWT.SHEET);
		dialog
				.setMessage(DataTransferMessages.WizardProjectsImportPage_SelectDialogTitle);

		String dirName = directoryPathField.getText().trim();
		if (dirName.length() == 0) {
			dirName = previouslyBrowsedDirectory;
		}

		if (dirName.length() == 0) {
			dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace()
					.getRoot().getLocation().toOSString());
		} else {
			File path = new File(dirName);
			if (path.exists()) {
				dialog.setFilterPath(new Path(dirName).toOSString());
			}
		}

		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			previouslyBrowsedDirectory = selectedDirectory;
			directoryPathField.setText(previouslyBrowsedDirectory);
			updateProjectsList(selectedDirectory);
		}

	}

	/**
	 * The browse button has been selected. Select the location.
	 */
	protected void handleLocationArchiveButtonPressed() {

		FileDialog dialog = new FileDialog(archivePathField.getShell(), SWT.SHEET);
		dialog.setFilterExtensions(FILE_IMPORT_MASK);
		dialog
				.setText(DataTransferMessages.WizardProjectsImportPage_SelectArchiveDialogTitle);

		String fileName = archivePathField.getText().trim();
		if (fileName.length() == 0) {
			fileName = previouslyBrowsedArchive;
		}

		if (fileName.length() == 0) {
			dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace()
					.getRoot().getLocation().toOSString());
		} else {
			File path = new File(fileName).getParentFile();
			if (path != null && path.exists()) {
				dialog.setFilterPath(path.toString());
			}
		}

		String selectedArchive = dialog.open();
		if (selectedArchive != null) {
			previouslyBrowsedArchive = selectedArchive;
			archivePathField.setText(previouslyBrowsedArchive);
			updateProjectsList(selectedArchive);
		}

	}

	/**
	 * Integrate the selected projects
	 * 
	 * @return boolean <code>true</code> if all project integration were
	 * 	successful.
	 */
	public boolean integratePlans() {
		saveWidgetValues();
		//final IProject currentDriverProject = this.driverProject;
		final Object[] selected = projectsList.getCheckedElements();
		createdProjects = new ArrayList();
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					monitor.beginTask("", selected.length); //$NON-NLS-1$
					if (monitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					for (int i = 0; i < selected.length; i++) 
						integratePlan((ProjectRecord) selected[i], new SubProgressMonitor(monitor, 1));
					
					//EPlan driverPlan = getScheduleFromProject(currentDriverProject);
					//driverPlan.eResource().save(null);
				} finally {
					monitor.done();
				}
			}
		};
		// run the new project creation operation
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			// one of the steps resulted in a core exception
			Throwable t = e.getTargetException();
			String message = DataTransferMessages.WizardExternalProjectImportPage_errorMessage;
			IStatus status;
			if (t instanceof CoreException) {
				status = ((CoreException) t).getStatus();
			} else {
				status = new Status(IStatus.ERROR,
						IDEWorkbenchPlugin.IDE_WORKBENCH, 1, message, t);
			}
			ErrorDialog.openError(getShell(), message, null, status);
			return false;
		}
		ArchiveFileManipulations.closeStructureProvider(structureProvider,
				getShell());

		// Adds the projects to the working sets
//		addToWorkingSets();
		
		return true;
	}
	
	List createdProjects;
	
	@SuppressWarnings("unused")
	private void addToWorkingSets() {		
		if(workingSetGroup != null){
			IWorkingSet[] selectedWorkingSets = workingSetGroup.getSelectedWorkingSets();
			if(selectedWorkingSets == null || selectedWorkingSets.length == 0)
				return; // no Working set is selected
			IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
			for (Iterator i = createdProjects.iterator(); i.hasNext();) {
				IProject project = (IProject) i.next();
				workingSetManager.addToWorkingSets(project, selectedWorkingSets);
			}
		}
	}

	/**
	 * Performs clean-up if the user cancels the wizard without doing anything
	 */
	public void performCancel() {
		ArchiveFileManipulations.closeStructureProvider(structureProvider,
				getShell());
	}

	/**
	 * Create the project described in record. If it is successful return true.
	 * 
	 * @param projectToBeIntegratedRecord
	 * @return boolean <code>true</code> if successful
	 * @throws InterruptedException
	 */
	private boolean integratePlan(final ProjectRecord projectToBeIntegratedRecord,
			IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		String projectName = projectToBeIntegratedRecord.getProjectName();
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject projectToBeIntegrated = workspace.getRoot().getProject(projectName);
		
		EPlan schedule = null;
		try {
			monitor.beginTask("Integrating Plans",1);
			
//			SPIFePlanIntegrationOperation op = new SPIFePlanIntegrationOperation(driverPlan, planToBeIntegrated);
//			op.mergePlans();
			schedule = SpifeProjectUtils.getOrLoadScheduleFromProject(projectToBeIntegrated);
			
			IUndoableOperation operation = new SPIFePlanIntegrationOperation(driverPlan, schedule);
			operation.addContext(TransactionUtils.getUndoContext(driverPlan));
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			IStatus status = history.execute(operation, monitor, null);
			if (status instanceof JobOperationStatus) {
				JobOperationStatus jobStatus = (JobOperationStatus) status;
				jobStatus.getJob().join();
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		} finally {
			SpifeProjectUtils.disposeSchedule(schedule);
			monitor.done();
		}
		return true;
	}

	/**
	 * The <code>WizardDataTransfer</code> implementation of this
	 * <code>IOverwriteQuery</code> method asks the user whether the existing
	 * resource at the given path should be overwritten.
	 * 
	 * @param pathString
	 * @return the user's reply: one of <code>"YES"</code>, <code>"NO"</code>,
	 * 	<code>"ALL"</code>, or <code>"CANCEL"</code>
	 */
	@Override
	public String queryOverwrite(String pathString) {

		Path path = new Path(pathString);

		String messageString;
		// Break the message up if there is a file name and a directory
		// and there are at least 2 segments.
		if (path.getFileExtension() == null || path.segmentCount() < 2) {
			messageString = NLS.bind(
					IDEWorkbenchMessages.WizardDataTransfer_existsQuestion,
					pathString);
		} else {
			messageString = NLS
					.bind(
							IDEWorkbenchMessages.WizardDataTransfer_overwriteNameAndPathQuestion,
							path.lastSegment(), path.removeLastSegments(1)
									.toOSString());
		}

		final MessageDialog dialog = new MessageDialog(getContainer()
				.getShell(), IDEWorkbenchMessages.Question, null,
				messageString, MessageDialog.QUESTION, new String[] {
						IDialogConstants.YES_LABEL,
						IDialogConstants.YES_TO_ALL_LABEL,
						IDialogConstants.NO_LABEL,
						IDialogConstants.NO_TO_ALL_LABEL,
						IDialogConstants.CANCEL_LABEL }, 0) {
			@Override
			protected int getShellStyle() {
				return super.getShellStyle() | SWT.SHEET;
			}
		};
		String[] response = new String[] { YES, ALL, NO, NO_ALL, CANCEL };
		// run in syncExec because callback is from an operation,
		// which is probably not running in the UI thread.
		getControl().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				dialog.open();
			}
		});
		return dialog.getReturnCode() < 0 ? CANCEL : response[dialog
				.getReturnCode()];
	}

	/**
	 * Method used for test suite.
	 * 
	 * @return Button the Import from Directory RadioButton
	 */
	public Button getProjectFromDirectoryRadio() {
		return projectFromDirectoryRadio;
	}

	/**
	 * Method used for test suite.
	 * 
	 * @return CheckboxTreeViewer the viewer containing all the projects found
	 */
	public CheckboxTreeViewer getProjectsList() {
		return projectsList;
	}

	/**
	 * Retrieve all the projects in the current workspace.
	 * 
	 * @return IProject[] array of IProject in the current workspace
	 */
	private IProject[] getProjectsInWorkspace() {
		if (wsProjects == null) {
			wsProjects = IDEWorkbenchPlugin.getPluginWorkspace().getRoot()
					.getProjects();
		}
		return wsProjects;
	}

	/**
	 * Get the array of  project records that can be imported from the
	 * source workspace or archive, selected by the user. If a project with the
	 * same name exists in both the source workspace and the current workspace,
	 * then the hasConflicts flag would be set on that project record.
	 * 
	 * Method declared public for test suite.
	 * 
	 * @return ProjectRecord[] array of projects that can be imported into the
	 * 	workspace
	 */
	public ProjectRecord[] getProjectRecords() {
		List projectRecords = new ArrayList();
		for (int i = 0; i < selectedProjects.length; i++) {
//			if (isProjectInWorkspace(selectedProjects[i].getProjectName())) {
//				selectedProjects[i].hasConflicts = true;
//			}
			
			if (selectedProjects[i].getProjectName().equalsIgnoreCase(driverProject.getName())) continue;
			projectRecords.add(selectedProjects[i]);
		}
		return (ProjectRecord[]) projectRecords
				.toArray(new ProjectRecord[projectRecords.size()]);
	}

	/**
	 * Determine if the project with the given name is in the current workspace.
	 * 
	 * @param projectName
	 * 		String the project name to check
	 * @return boolean true if the project with the given name is in this
	 * 	workspace
	 */
	@SuppressWarnings("unused")
	private boolean isProjectInWorkspace(String projectName) {
		if (projectName == null) {
			return false;
		}
		IProject[] workspaceProjects = getProjectsInWorkspace();
		for (int i = 0; i < workspaceProjects.length; i++) {
			if (projectName.equals(workspaceProjects[i].getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Use the dialog store to restore widget values to the values that they
	 * held last time this wizard was used to completion, or alternatively,
	 * if an initial path is specified, use it to select values.
	 * 
	 * Method declared public only for use of tests.
	 */
	public void restoreWidgetValues() {
				
		// First, check to see if we have resore settings, and
		// take care of the checkbox
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			// checkbox
			copyFiles = settings.getBoolean(STORE_COPY_PROJECT_ID);
			if(copyCheckbox != null) {
				copyCheckbox.setSelection(copyFiles);
			}
		}
				
		// Second, check to see if we don't have an initial path, 
		// and if we do have restore settings.  If so, set the
		// radio selection properly to restore settings
		
		if (initialPath==null && settings!=null)
		{
			// radio selection
			boolean archiveSelected = settings
					.getBoolean(STORE_ARCHIVE_SELECTED);
			if(projectFromDirectoryRadio != null) {
				projectFromDirectoryRadio.setSelection(!archiveSelected);
			}
			
			if(projectFromArchiveRadio != null) {
				projectFromArchiveRadio.setSelection(archiveSelected);
			}
			if (archiveSelected) {
				archiveRadioSelected();
			} else {
				directoryRadioSelected();
			}
		}	
		// Third, if we do have an initial path, set the proper
		// path and radio buttons to the initial value. Move
		// cursor to the end of the path so user can see the
		// most relevant part (directory / archive name)
		else if (initialPath != null) {
			boolean dir = new File(initialPath).isDirectory();

			projectFromDirectoryRadio.setSelection(dir);
			projectFromArchiveRadio.setSelection(!dir);

			if (dir) {
				directoryPathField.setText(initialPath);
				directoryPathField.setSelection(initialPath.length());
				directoryRadioSelected();
			} else {
				archivePathField.setText(initialPath);
				archivePathField.setSelection(initialPath.length());
				archiveRadioSelected();
			}
		}
	}

	/**
	 * Since Finish was pressed, write widget values to the dialog store so that
	 * they will persist into the next invocation of this wizard page.
	 * 
	 * Method declared public only for use of tests.
	 */
	public void saveWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			boolean selection = false;
			if(copyCheckbox != null) {
				selection = copyCheckbox.getSelection();
			}
			settings.put(STORE_COPY_PROJECT_ID, selection);

			selection = false;
			if(projectFromArchiveRadio != null) {
				selection = projectFromArchiveRadio.getSelection();
			}
			settings.put(STORE_ARCHIVE_SELECTED, selection);
		}
	}

	/**
	 * Method used for test suite.
	 * 
	 * @return Button copy checkbox
	 */
	public Button getCopyCheckbox() {
		return copyCheckbox;
	}
}
