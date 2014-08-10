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
package gov.nasa.arc.spife.ui.timeline.chart;

import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.util.TimelineEditorUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.UndoRedoUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.synchronizer.ProfileSynchronizer;
import gov.nasa.ensemble.core.jscience.ui.profile.tree.ProfileTreePage;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.resource.ProjectResourceSetSynchronizer;
import gov.nasa.ensemble.emf.transaction.PreCommitListener;
import gov.nasa.ensemble.emf.transaction.RunnableRecordingCommand;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.IProgressService;

public class ChartEditor extends EditorPart {

	public static final String ID = "gov.nasa.arc.spife.ui.timeline.chart.editor";

	private Timeline<?> timeline;
	private ETimeline timelineModel = null;
	private TransactionalEditingDomain editingDomain;
	private EditorPartInputActivationListener listener = null;
	private DirtyMonitor dirtyMonitor = new DirtyMonitor();
	private IUndoContext undoContext;
	private ProjectResourceSetSynchronizer synchronizer;
	private ProfileTreePage profileTreePage = null;
	private ProfileSynchronizer profileSynchronizer;
	private ResourceSetListener pageExtentListener = new Listener();

	private URI chartURI;

	public ETimeline getTimelineModel() {
		return timelineModel;
	}

	public Timeline getTimeline() {
		return timeline;
	}

	/**
	 * @throws PartInitException
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		listener = new EditorPartInputActivationListener(this);
		IFile file = CommonUtils.getAdapter(input, IFile.class);
		IProject project = file.getProject();
		synchronizer = new ProjectResourceSetSynchronizer(project, gov.nasa.ensemble.emf.transaction.TransactionUtils.createTransactionResourceSet());
		ResourceSet resourceSet = synchronizer.getResourceSet();
		resourceSet.setURIConverter(new ProjectURIConverter(project));
		resourceSet.getPackageRegistry().put(ChartPackage.eNS_URI, ChartPackage.eINSTANCE);
		profileSynchronizer = ProfileSynchronizer.createInstance(project, resourceSet);
		editingDomain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(resourceSet);
		undoContext = new ObjectUndoContext(getEditingDomain());
		site.setSelectionProvider(new EnsembleSelectionProvider(this.toString()));
		UndoRedoUtils.setupUndoRedo(site.getActionBars(), site, undoContext);
		createModel(input);
		editingDomain.addResourceSetListener(pageExtentListener);
	}

	@Override
	public void dispose() {
		UndoRedoUtils.disposeUndoRedo(getEditorSite().getActionBars());
		super.dispose();
		if (editingDomain != null) {
			editingDomain.removeResourceSetListener(pageExtentListener);
		}
		if (listener != null) {
			listener.dispose();
			listener = null;
		}
		if (dirtyMonitor != null) {
			dirtyMonitor.dispose();
			dirtyMonitor = null;
		}
		if (synchronizer != null) {
			synchronizer.dispose();
			synchronizer = null;
		}
		if (editingDomain != null) {
			editingDomain.dispose();
			ResourceSet resourceSet = editingDomain.getResourceSet();
			editingDomain = null;
			for (Resource r : resourceSet.getResources()) {
				r.unload();
			}
		}
		if (timeline != null) {
			timeline.dispose();
			timeline = null;
		}
		if (timelineModel != null) {
			timelineModel = null;
		}
		if (profileSynchronizer != null) {
			profileSynchronizer.dispose();
			profileSynchronizer = null;
		}
		IWorkbenchPartSite site = getSite();
		if (site != null) {
			ISelectionProvider provider = site.getSelectionProvider();
			if (provider != null) {
				provider.setSelection(StructuredSelection.EMPTY);
			}
		}
		if (undoContext != null) {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.dispose(undoContext, true, true, true);
			undoContext = null;
		}
	}

	@Override
	protected void setSite(IWorkbenchPartSite newSite) {
		super.setSite(newSite);
		IContextService contextService = (IContextService) newSite.getService(IContextService.class);
		contextService.activateContext("timeline");
	}

	protected void createModel(IEditorInput input) {
		URI uri = EditUIUtil.getURI(input);
		Resource resource = null;
		try {
			resource = getEditingDomain().getResourceSet().getResource(uri, true);
		} catch (Exception e) {
			resource = getEditingDomain().getResourceSet().getResource(uri, true);
		}
		timelineModel = getTimelineModel(resource);
		if (timelineModel.getContents().isEmpty()) {
			if (input instanceof URIEditorInput) {
				populateTimelineFromFragment((URIEditorInput) input, resource);
			} else {
				final List<Section> sections = createDefaultSections(resource);
				gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(timelineModel, new Runnable() {
					@Override
					public void run() {
						timelineModel.getContents().addAll(sections);
					}
				});
			}
		}
		refreshPage();
	}

	private void refreshPageInDisplayThread() {
		WidgetUtils.runInDisplayThread(getTimeline().getControl(), new Runnable() {
			@Override
			public void run() {
				refreshPage();
			}
		});
	}

	private void refreshPage() {
		final List<Profile> profiles = new ArrayList<Profile>();
		final List<Plot> plots = new ArrayList<Plot>();
		for (EObject object : timelineModel.getContents()) {
			if (object instanceof Profile) {
				profiles.add((Profile) object);
			} else if (object instanceof Chart) {
				Chart chart = (Chart) object;
				plots.addAll(chart.getPlots());
			}
		}
		ComputingPageExtent runnable = new ComputingPageExtent(profiles, plots);
		try {
			IWorkbenchPartSite site = getSite();
			IProgressService service = (IProgressService) site.getService(IProgressService.class);
			service.busyCursorWhile(runnable);
		} catch (InvocationTargetException e) {
			LogUtil.error(e);
		} catch (InterruptedException e) {
			LogUtil.error(e);
		}
		Date start = runnable.getStart();
		Date end = runnable.getEnd();
		if (start.getTime() != Long.MAX_VALUE) {
			final Page page = timelineModel.getPage();
			final TemporalExtent extent = new TemporalExtent(start, end);
			gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(timelineModel, new Runnable() {
				@Override
				public void run() {
					page.setStartTime(extent.getStart());
					page.setDuration(extent.getDuration());
				}
			});
		}
	}

	private void populateTimelineFromFragment(URIEditorInput input, Resource resource) {
		String fragment = input.getURI().fragment();
		EObject eObject = resource.getEObject(fragment);
		final Chart chart;
		if (eObject instanceof Chart) {
			chart = (Chart) eObject;
		} else {
			chart = ChartFactory.eINSTANCE.createChart();
			chart.setMinimumHeight(500);
			Plot plot;
			if (eObject instanceof Plot) {
				plot = (Plot) eObject;
			} else {
				plot = ChartFactory.eINSTANCE.createPlot();
				Profile profile;
				if (eObject instanceof Profile) {
					profile = (Profile) eObject;
				} else {
					return;
				}
				plot.setProfile(profile);
			}
			chart.getPlots().add(plot);
		}
		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(timelineModel, new Runnable() {
			@Override
			public void run() {
				timelineModel.getContents().add(chart);
			}
		});
	}

	protected ETimeline getTimelineModel(Resource resource) {
		TransactionalEditingDomain domain = getEditingDomain();
		if (domain != null) {
			URI chartURI = getChartURI(false);
			IFile file = EMFUtils.getFile(chartURI);
			if (file == null || !file.exists()) {
				ETimeline timelineModel = CommonUtils.getAdapter(resource, ETimeline.class);
				if (timelineModel != null) {
					Resource timelineResource = timelineModel.eResource();
					if (timelineResource == null) {
						timelineResource = domain.getResourceSet().createResource(chartURI);
						timelineResource.getContents().add(timelineModel);
					} else {
						domain.getResourceSet().getResources().add(timelineResource);
					}
					return timelineModel;
				}
			}
			return TimelineEditorUtils.getTimelineModel(domain, chartURI);
		}
		return null;
	}

	/**
	 * Tests the adaptability of the resource to a chart provider, and then applies a simple heuristic that reads the resource and places the plot in the chart if only one exists
	 * 
	 * @param resource
	 * @return not supplied
	 */
	protected List<Section> createDefaultSections(Resource resource) {
		Chart chart = ChartFactory.eINSTANCE.createChart();
		if (resource.getContents().size() == 1) {
			final EObject eObject = resource.getContents().get(0);
			if (eObject instanceof Profile) {
				Profile profile = (Profile) eObject;
				Plot plot = ChartFactory.eINSTANCE.createPlot();
				plot.setProfile(profile);
				chart.getPlots().add(plot);
			}
		}
		return new ArrayList<Section>(Collections.singletonList(chart));
	}

	public URI getChartURI(boolean requireChart) {
		if (chartURI == null) {
			URI uri = EditUIUtil.getURI(getEditorInput());
			if (uri.fileExtension().equals("chart")) {
				return uri;
			}
			if (requireChart) {
				return uri.trimFileExtension().appendFileExtension("chart");
			}
			return uri.trimFileExtension().appendFileExtension(TimelineConstants.TIMELINE_FILE_EXT);
		}
		return chartURI;
	}

	public void setChartURI(URI uri) {
		this.chartURI = uri;
	}

	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

	@Override
	public String getPartName() {
		IEditorInput input = getEditorInput();
		URI uri = EditUIUtil.getURI(input);
		if (uri != null) {
			return URI.decode(uri.lastSegment());
		}
		return super.getPartName();
	}

	@Override
	public void createPartControl(Composite parent) {
		timeline = new Timeline(timelineModel) {

			@Override
			public Object getAdapter(Class adapter) {
				if (IResource.class == adapter) {
					return CommonUtils.getAdapter(getEditorInput(), IFile.class);
				} else if (ChartEditor.class == adapter) {
					return true;
				}
				return super.getAdapter(adapter);
			}

		};
		timeline.init(getSite(), null);
		timeline.createPartControl(parent);
	}

	@Override
	public void setFocus() {
		timeline.getControl().setFocus();
	}

	@Override
	public boolean isDirty() {
		return dirtyMonitor.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		URI chartURI = getChartURI(true);
		IFile file = EMFUtils.getFile(chartURI);
		if (file != null) {
			if (!file.exists()) {
				// do save as
				doSaveAs();
			} else {
				try {
					TransactionalEditingDomain editingDomain = getEditingDomain();
					ResourceSet resourceSet = editingDomain.getResourceSet();
					Resource resource = resourceSet.getResource(chartURI, false);
					resource.save(null);
					dirtyMonitor.resetDirty();
				} catch (IOException e) {
					LogUtil.error("saving", e);
				}
			}
		}
	}

	@Override
	public void doSaveAs() {
		URI chartURI = getChartURI(true);
		SaveAsDialog dlg = new SaveAsDialog(getSite().getShell());
		dlg.setOriginalFile(EMFUtils.getFile(chartURI));
		if (dlg.open() == Window.OK) {
			IPath path = dlg.getResult();
			URI newChartURI = EMFUtils.getURI(path);
			try {
				Resource resource = timelineModel.eResource();
				resource.setURI(newChartURI);
				resource.save(null);
				dirtyMonitor.resetDirty();
				setChartURI(newChartURI);
				setInput(new FileEditorInput(EMFUtils.getFile(newChartURI)));
				setPartName(newChartURI.lastSegment());
			} catch (IOException e) {
				LogUtil.error(e);
			}
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (Timeline.class == adapter) {
			return getTimeline();
		} else if (IUndoContext.class.equals(adapter)) {
			return getUndoContext();
		} else if (ProfileTreePage.class == adapter) {
			return getProfileTreePage();
		}
		return super.getAdapter(adapter);
	}

	public IUndoContext getUndoContext() {
		return dirtyMonitor.getUndoContext();
	}

	private Object getProfileTreePage() {
		if (profileTreePage == null) {
			profileTreePage = new ProfileTreePage() {
				@Override
				protected void configureTreeViewer(TreeViewer treeViewer) {
					AdapterFactory adapterFactory = EMFUtils.getAdapterFactory(getEditingDomain());
					treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
					treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
					treeViewer.setInput(getEditingDomain().getResourceSet().getResources().get(0));
				}
			};
		}
		return profileTreePage;
	}

	private static final class ComputingPageExtent implements IRunnableWithProgress {
		private final List<Profile> profiles;
		private final List<Plot> plots;
		private Date start = new Date(Long.MAX_VALUE);
		private Date end = new Date(Long.MIN_VALUE);

		private ComputingPageExtent(List<Profile> profiles, List<Plot> plots) {
			this.profiles = profiles;
			this.plots = plots;
		}

		public Date getStart() {
			return start;
		}

		public Date getEnd() {
			return end;
		}

		@Override
		public void run(IProgressMonitor monitor) {
			monitor.beginTask("Computing page extent", profiles.size() + plots.size());
			try {
				for (Profile profile : profiles) {
					monitor.subTask("profile: " + profile.getName());
					updateBounds(profile);
					monitor.worked(1);
				}
				for (Plot plot : plots) {
					monitor.subTask("plot: " + plot.getName());
					Profile<?> profile = plot.getProfile();
					if (profile != null) {
						updateBounds(profile);
					}
					monitor.worked(1);
				}
			} finally {
				monitor.subTask("");
				monitor.done();
			}
		}

		private void updateBounds(Profile<?> profile) {
			TemporalExtent extent = ProfileUtil.getTemporalExtent(profile);
			if (extent != null) {
				if (extent.getStart().before(start)) {
					start = extent.getStart();
				}
				if (extent.getEnd().after(end)) {
					end = extent.getEnd();
				}
			}
		}
	}

	private class DirtyMonitor extends AbstractEnsembleEditorModel {

		private final IDirtyListener listener = new IDirtyListener() {
			@Override
			public void dirtyStateChanged() {
				WidgetUtils.runInDisplayThread(ChartEditor.this.getSite().getShell(), new Runnable() {
					@Override
					public void run() {
						firePropertyChange(ISaveablePart.PROP_DIRTY);
					}
				});
			}
		};

		public DirtyMonitor() {
			addDirtyListener(listener);
		}

		@Override
		public void dispose() {
			super.dispose();
			removeDirtyListener(listener);
		}

		@Override
		public IUndoContext getUndoContext() {
			return undoContext;
		}

	}

	private class Listener extends PreCommitListener {

		@Override
		public Command transactionAboutToCommit(ResourceSetChangeEvent event) {
			List<Notification> notifications = event.getNotifications();
			for (Notification notification : notifications) {
				List<Plot> addedPlots = EMFUtils.getAddedObjects(notification, Plot.class);
				List<Plot> removedPlots = EMFUtils.getAddedObjects(notification, Plot.class);
				if (!addedPlots.isEmpty() || !removedPlots.isEmpty()) {
					return new RunnableRecordingCommand("refresh timeline page", new Runnable() {
						@Override
						public void run() {
							refreshPageInDisplayThread();
						}

					});
				}
			}
			return null;
		}

	}

}
