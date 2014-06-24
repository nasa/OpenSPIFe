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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.Timeline.SELECTION_MODE;
import gov.nasa.arc.spife.ui.timeline.action.FreezeSectionAction;
import gov.nasa.arc.spife.ui.timeline.action.RemoveFreezeSectionAction;
import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.arc.spife.ui.timeline.model.TickManager;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;
import gov.nasa.arc.spife.ui.timeline.part.AbstractTimelineEditPart;
import gov.nasa.arc.spife.ui.timeline.part.AbstractTimelineEditPart.ScrollingGraphicalEditPart;
import gov.nasa.arc.spife.ui.timeline.part.TimelineRootEditPart;
import gov.nasa.arc.spife.ui.timeline.util.TimelineLayoutData;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.gef.EnsembleContextMenuProvider;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.common.ui.gef.SplitModel;
import gov.nasa.ensemble.common.ui.gef.TransferDropTargetListener;
import gov.nasa.ensemble.common.ui.layout.BorderLayout;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.dnd.DelegatingDropAdapter;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TimelineViewer extends GraphicalViewerImpl implements IAdaptable {

	public static final String CONTENT_PROVIDER = "timeline.content.provider";
	public static final String LABEL_PROVIDER = "timeline.provider.label";

	public static final String CONTENT_OBJECT = "timeline.content.object";
	
	private Timeline timeline;

	private Label nameLabel = null;
	private ImageDescriptor imageDescriptor = TimelineBuilder.IMAGE_TIMELINE_DESCRIPTOR;
	private Image image = null;
	private boolean closeable = true;
	private Object model;
	
	private SectionNameAdapter sectionNameAdapter = new SectionNameAdapter();

	private boolean showToolbar = true;
	private Composite toolbarComposite = null;
	protected TimelineToolBarContributor toolbarContribution;

	private ToolbarMouseListener toolbarMouseListener = new ToolbarMouseListener();
	
	private DelegatingDropAdapter dropAdapter = new TimelineDelegatingDropAdapter(); 
	
	public TimelineViewer(Timeline timeline) {
		super();
		this.timeline = timeline;
		setProperty(TimelineConstants.P_DISPLAY_INSTANTANEOUS, Boolean.TRUE);
		setProperty(ExpansionModel.ID, new ExpansionModel());
	}

	/**
	 * Replace the range model for this viewer
	 * 
	 * @param rangeModel
	 */
	public void setHorizontalRangeModel(RangeModel rangeModel) {
		AbstractTimelineEditPart timelineEditPart = getTimelineEditPart();
		ScrollPane dataScrollPane = timelineEditPart.getDataScrollPane();
		Viewport viewport = dataScrollPane.getViewport();
		viewport.setHorizontalRangeModel(rangeModel);
	}
	
	/**
	 * Set to false to avoid creating the toolbar.
	 * Must be called before createControl().
	 * 
	 * @param showToolbar
	 */
	public void setShowToolbar(boolean showToolbar) {
		this.showToolbar = showToolbar;
	}
	
	public void dispose() {
		getContents().deactivate();
		if (getControl() != null) {
			getControl().dispose();
		}
		if (toolbarComposite != null) {
			toolbarComposite.dispose();
		}
		if (imageDescriptor != null && image != null) {
			imageDescriptor.destroyResource(image);
			image = null;
		}
		ExpansionModel expansionModel = (ExpansionModel) getProperty(ExpansionModel.ID);
		if (expansionModel != null) {
			expansionModel.dispose();
		}
		TreeTimelineContentProvider cp = getTreeTimelineContentProvider();
		if (cp != null) {
			cp.dispose();
		}
		setTimelineSectionModel(null);
		sectionNameAdapter = null;
		getEditPartRegistry().clear();
		getVisualPartMap().clear();
		toolbarContribution = null;
		toolbarMouseListener = null;
		timeline = null;
		model = null;
		imageDescriptor = null;
	}

	public Object getModel() {
		return model;
	}

	@Override
	public void setContents(Object contents) {
		this.model = contents;
		super.setContents(contents);
	}
	
	@Override
	protected DelegatingDropAdapter getDelegatingDropAdapter() {
		return dropAdapter;
	}

	public void setTimelineSectionModel(Section content) {
		Section oldContent = getTimelineSectionModel();
		if (oldContent != null) {
			oldContent.eAdapters().remove(sectionNameAdapter);
		}
		setProperty(CONTENT_OBJECT, content);
		if (content != null) {
			content.eAdapters().add(sectionNameAdapter);
		}
	}
	
	public Section getTimelineSectionModel() {
		return (Section) getProperty(CONTENT_OBJECT);
	}
	
	public boolean isTimelineSectionFrozen() {
		Section timelineSection = getTimelineSectionModel();
		return getTimeline().isFrozen(timelineSection);
	}

	public boolean isAnimated() {
		return getTimeline().isAnimated();
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		if (getControl() != null) {
			throw new IllegalStateException("Cannot call setCloseable() after createControl(). Call has no effect");
		}
		this.closeable = closeable;
	}

	public void setImageDescriptor(ImageDescriptor imageDescriptor) {
		if (getControl() != null) {
			throw new IllegalStateException("Cannot call setImage() after createControl(). Call has no effect");
		}
		this.imageDescriptor = imageDescriptor;
	}

	public String getName() {
		Section section = getTimelineSectionModel();
		if (section != null) {
			return section.getName();
		}
		return null;
	}

	public void setLabelProvider(ILabelProvider labelProvider) {
		setProperty(LABEL_PROVIDER, labelProvider);
	}

	public ILabelProvider getLabelProvider() {
		return (ILabelProvider) getProperty(LABEL_PROVIDER);
	}

	public void setTreeTimelineContentProvider(TreeTimelineContentProvider newContentProvider) {
		TreeTimelineContentProvider oldContentProvider = getTreeTimelineContentProvider();
		if (oldContentProvider != null) {
			oldContentProvider.dispose();
		}
		if (newContentProvider != null) {
			newContentProvider.activate();
			if (timeline != null) {
				Control control = timeline.getControl();
				if (control != null) {
					Display display = control.getDisplay();
					Realm realm = SWTObservables.getRealm(display);
					newContentProvider.setRealm(realm);
				}
			}
		}
		setProperty(CONTENT_PROVIDER, newContentProvider);
	}

	public TreeTimelineContentProvider getTreeTimelineContentProvider() {
		return (TreeTimelineContentProvider) getProperty(CONTENT_PROVIDER);
	}

	public void setTimelineToolBarContributionItem(TimelineToolBarContributor toolbarContribution) {
		if (getControl() != null) {
			throw new IllegalStateException("Cannot call setToolbarContribution() after createControl(). Call has no effect");
		}
		this.toolbarContribution = toolbarContribution;
		this.toolbarContribution.setTimelineViewer(this);
	}

	public Timeline getTimeline() {
		return timeline;
	}

	public AbstractTimelineEditPart getTimelineEditPart() {
		return (AbstractTimelineEditPart) getContents();
	}
	
	/*
	 * Convenience methods
	 */

	public ZoomManager getZoomManager() {
		return getTimeline().getZoomManager();
	}

	public Page getPage() {
		return getTimeline().getPage();
	}

	public IWorkbenchPartSite getSite() {
		return getTimeline().getSite();
	}

	public SplitModel getSplitModel() {
		return getTimeline().getSplitModel();
	}

	public TickManager getTickManager() {
		return getTimeline().getTickManager();
	}

	@Override
	public Object getAdapter(Class adapter) {
		Object o = getProperty(adapter.getName());
		if (o == null) {
			o = getTimeline().getAdapter(adapter);
		}
		return o;
	}

	private void buildTimelineViewerIdentification(Composite toolbarComposite) {
		Composite idComposite = new Composite(toolbarComposite, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 4;
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 10;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		idComposite.setLayout(layout);
		idComposite.setLayoutData(SWT.LEFT);

		if (imageDescriptor != null && image == null) {
			if(isTimelineSectionFrozen()) {
				image = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/frozen.png").createImage();
			}
			else {
				image = (Image) imageDescriptor.createResource(toolbarComposite.getDisplay());
			}
			Label imageLabel = new Label(idComposite, SWT.NONE);
			imageLabel.setImage(image);
			imageLabel.addMouseListener(toolbarMouseListener);
		}

		String name = getName();
		if (name == null) {
			name = "";
		}
		nameLabel = new Label(idComposite, SWT.NONE);
		nameLabel.setText(name);
		nameLabel.addMouseListener(toolbarMouseListener);
	}
	
	@Override
	public Control createControl(final Composite parent) {
		TransferDropTargetListener.addDropSupport(this);
		
		if (showToolbar) {
			toolbarComposite = buildToolBarComposite(parent);
			TimelineLayoutData data = new TimelineLayoutData();
			data.preferredSize = new Point(SWT.DEFAULT, 20);
			toolbarComposite.setLayoutData(data);
			toolbarComposite.addMouseListener(toolbarMouseListener);
		}
		TimelineRootEditPart rootEditPart = getRootEditPart();
		final IFigure figure = rootEditPart.getFigure();
		figure.addLayoutListener(new LayoutListener.Stub() {
			@Override
			public void postLayout(IFigure container) {
				final Control control = getControl();
				WidgetUtils.runLaterInDisplayThread(control, new Runnable() {
					@Override
					public void run() {
						Dimension preferredSize = figure.getPreferredSize();
						Point controlSize = control.getSize();
						if (preferredSize.height != controlSize.y) {
							parent.layout(true);
						}
					}
				});
			}
		});

		Canvas canvas = new TimelineCanvas(parent);
		Control control = this.getControl();
		if (control == null) {
			setControl(canvas);
		} else {
			LogUtil.warn("Trying to set the control but the control is already set.");
		}
		
		MenuManager menuProvider = new EnsembleContextMenuProvider(this);
		menuProvider.setRemoveAllWhenShown(true);
		IMenuListener listener = new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager m) {
				boolean isSectionFrozen = isTimelineSectionFrozen();
				if(isSectionFrozen) {
					// Remove Top Freeze Section
					m.add(new RemoveFreezeSectionAction(getTimeline(), "R&emove Frozen Section", getTimelineSectionModel()));
				}
				else {
					// Top Freeze Section
					m.add(new FreezeSectionAction(getTimeline(), "&Freeze Section", getTimelineSectionModel()));					
				}
			}
		};
		menuProvider.addMenuListener(listener);
		setContextMenu(menuProvider);
		IWorkbenchPartSite site = getSite();
		if (site instanceof IEditorSite) {
			// allow extensions to the context menu but not those based on the editor input
			((IEditorSite)site).registerContextMenu(menuProvider, getSite().getSelectionProvider(), false);
		}
		TreeTimelineContentProvider cp = getTreeTimelineContentProvider();
		if (cp != null) {
			Display display = canvas.getDisplay();
			Realm realm = SWTObservables.getRealm(display);
			cp.setRealm(realm);
		}
		return canvas;
	}

	protected Composite buildToolBarComposite(Composite parent) {
		Composite toolbarComposite = new Composite(parent, SWT.BORDER);
		toolbarComposite.setData("debug", TimelineViewer.class.getSimpleName() + ".toolbarComposite");
		toolbarComposite.setLayout(new BorderLayout());

		buildTimelineViewerIdentification(toolbarComposite);

		Composite buttons = createToolbarButtons(toolbarComposite);
		buttons.setLayoutData(SWT.RIGHT);

		return toolbarComposite;
	}

	private Composite createToolbarButtons(Composite toolbarComposite) {
		Composite buttons = new Composite(toolbarComposite, SWT.NONE);
		buttons.setLayout(new RowLayout());

		if (toolbarContribution != null) {
			toolbarContribution.fill(buttons);
		}
		
		buildCustomButtons(buttons);

		Label upLabel = new Label(buttons,SWT.SHADOW_IN);
		upLabel.setImage(WidgetPlugin.getImageFromRegistry(WidgetPlugin.KEY_IMAGE_NAV_UP));
		upLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {/* no action */}
			@Override
			public void mouseDown(MouseEvent e) {/* no action */}
			@Override
			public void mouseUp(MouseEvent e) {
				moveTimelineUp();
			}
		});
		Label downLabel = new Label(buttons,SWT.SHADOW_IN);
		downLabel.setImage(WidgetPlugin.getImageFromRegistry(WidgetPlugin.KEY_IMAGE_NAV_DOWN));
		downLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {/* no action */}
			@Override
			public void mouseDown(MouseEvent e) {/* no action */}
			@Override
			public void mouseUp(MouseEvent e) {
				moveTimelineDown();
			}
		});
		
		if (isCloseable()) {
			Label closeLabel = new Label(buttons,SWT.SHADOW_IN);
			closeLabel.setImage(WidgetPlugin.getImageFromRegistry(WidgetPlugin.KEY_IMAGE_CLOSE_NICE));
			closeLabel.addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {/* no action */}
				@Override
				public void mouseDown(MouseEvent e) {/* no action */}
				@Override
				public void mouseUp(MouseEvent e) {
					closeTimelineViewer();
				}
			});
		}
		return buttons;
	}

	protected void buildCustomButtons(Composite buttons) {
		// for extending classes to implement
	}

	protected void closeTimelineViewer() {
		Object contentObject = getTimelineSectionModel();
		if (contentObject != null) {
			ETimeline timelineModel = getTimeline().getTimelineModel();
			EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(timelineModel);
			EReference featureReference = getContentsReference();
			Command command = RemoveCommand.create(domain, timelineModel, featureReference, Collections.singleton(contentObject));
			EMFUtils.executeCommand(domain, command);
		}
	}
	protected void moveTimelineUp() {
		Object contentObject = getTimelineSectionModel();
		if (contentObject != null) {
			ETimeline timelineModel = getTimeline().getTimelineModel();
			EList<Section> timelineContents = getTimelineContents();
			int index = timelineContents.indexOf(contentObject);
			if (index > 0) {
				EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(timelineModel);
				EReference featureReference = getContentsReference();
				Command command = MoveCommand.create(domain, timelineModel, featureReference, contentObject, index-1);
				EMFUtils.executeCommand(domain, command);
			}
		}
	}
	
	protected void moveTimelineDown() {
		Object contentObject = getTimelineSectionModel();
		if (contentObject != null) {
			ETimeline timelineModel = getTimeline().getTimelineModel();
			EList<Section> timelineContents = getTimelineContents();
			int index = timelineContents.indexOf(contentObject);
			if (index < timelineContents.size()-1) {
				EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(timelineModel);
				EReference featureReference = getContentsReference();
				Command command = MoveCommand.create(domain, timelineModel, featureReference, contentObject, index+1);
				EMFUtils.executeCommand(domain, command);
			}
		}
	}
	
	protected EList<Section> getTimelineContents() {
		ETimeline timelineModel = getTimeline().getTimelineModel();
		return isTimelineSectionFrozen() ? timelineModel.getTopContents() : timelineModel.getContents();
	}
	
	/**
	 * Determines if the section is for the frozen Top Contents or the main Contents.
	 * 
	 * @param contentObject
	 * @return
	 */
	protected EReference getContentsReference() {
		return isTimelineSectionFrozen() ? TimelinePackage.Literals.ETIMELINE__TOP_CONTENTS : TimelinePackage.Literals.ETIMELINE__CONTENTS;
	}
	
	@Override
	protected void createDefaultRoot() {
		setRootEditPart(new TimelineRootEditPart());
	}
	
	@Override
	public TimelineRootEditPart getRootEditPart() {
		return (TimelineRootEditPart) super.getRootEditPart();
	}
	
	public void centerCursorTimeOnHorzLocation(int x) {
		ScrollPane dataScrollPane = getTimelineEditPart().getDataScrollPane();
		int local = x - dataScrollPane.getBounds().x + dataScrollPane.getViewport().getHorizontalRangeModel().getValue();
		getTimeline().scrollTo(getTimeline().getCursorTime(), local);
	}

	public void registerLayer(Object key, IFigure layer) {
		((TimelineRootEditPart) super.getRootEditPart()).registerLayer(key, layer);
	}

	protected void expand(Set<Object> objects) {
		ExpansionModel expansionModel = (ExpansionModel) getProperty(ExpansionModel.ID);
		TreeTimelineContentProvider contentProvider = getTreeTimelineContentProvider();
		if (contentProvider == null || expansionModel == null) {
			return;
		}
		Set<Object> parents = new LinkedHashSet<Object>();
		for (Object object : objects) {
			Object parent = contentProvider.getParent(object);
			while (parent != null) {
				object = parent;
				parents.add(object);
				parent = contentProvider.getParent(object);
			}
		}
		expansionModel.setExpanded(parents, true);
	}

	public void removeNotify(EditPart editPart) {
		editPart.setSelected(EditPart.SELECTED_NONE);
	}

	/**
	 * Given and edit part model, we want to expand the selection to
	 * include all the edit parts that reference that model, as well
	 * as query the edit part for additional selections
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void appendSelection(EditPart ep) {
		super.appendSelection(ep);
		Set<Object> models = new LinkedHashSet<Object>();
		models.add(ep.getModel());
		SelectionManager selectionManager = getSelectionManager();
		for (Object o : ((IStructuredSelection)selectionManager.getSelection()).toArray()) {
			models.add(((EditPart)o).getModel());
		}
		Set<EditPart> editParts = new LinkedHashSet<EditPart>(getSelectableEditParts(models));
		ISelection epSelection = CommonUtils.getAdapter(ep, ISelection.class);
		if (epSelection != null) {
			editParts.remove(ep);
			editParts.addAll(((IStructuredSelection)epSelection).toList());
		} else {
			editParts.remove(ep); // move the primary selection to the end of the list
			editParts.add(ep);
		}
		selectionManager.setSelection(new StructuredSelection(editParts.toArray(new EditPart[0])));
	}

	public void selectModels(Set<Object> models) {
		final Set<EditPart> layedOutEditParts = getSelectableEditParts(models);
		GEFUtils.runInDisplayThread(getRootEditPart(), new Runnable() {
			@Override
			public void run() {
				setSelection(new StructuredSelection(layedOutEditParts.toArray()));
			}
		});
	}

	protected Set<EditPart> getSelectableEditParts(Set<Object> models) {
		//
		// Expand the edit parts, probably want to look at 'reveal'
		expand(models);
		//
		// We only want to select the additional edit parts that can be selected and dragged
		Set<EditPart> editParts = getEditParts(models);
		LinkedHashSet<EditPart> result = new LinkedHashSet<EditPart>();
		for(EditPart editPart: editParts) {
			if(editPart.isSelectable()) {
				if (editPart.understandsRequest(new Request(RequestConstants.REQ_MOVE))
						|| editPart.getModel() instanceof TimelineMarker)
					result.add(editPart);
			}
		}
		return result;
	}

	/**
	 * Given some models and the edit part registry, this method will return all of the edit parts for the given models with the
	 * given edit part registry
	 * 
	 * @param models
	 *            some model
	 * @return all of the edit parts given a set of models.
	 */
	public Set<EditPart> getEditParts(Collection<? extends Object> models) {
		LinkedHashSet<EditPart> result = new LinkedHashSet<EditPart>();
		for (Object model : models) {
			result.addAll(getEditParts(model));
		}
		return result;
	}

	/**
	 * Given a model and the edit part registry, this method will return all of the edit parts for the given model with the given
	 * edit part registry
	 * 
	 * @param model
	 *            some model
	 * @return all of the edit parts given then model.
	 */
	protected Set<EditPart> getEditParts(Object model) {
		LinkedHashSet<EditPart> result = new LinkedHashSet<EditPart>();
		Object object = getEditPartRegistry().get(model);
		if (object instanceof List<?>) {
			List<?> editParts = (List<?>) object;
			for (Object o : editParts) {
				if (o instanceof EditPart) {
					result.add((EditPart) o);
				}
			}
		} else if (object instanceof EditPart) {
			result.add((EditPart) object);
		}
		return result;
	}

	public void scrollToEditPart(final EditPart editPart) {
		EditPart parentEditPart = editPart;
		while (parentEditPart != null && !(parentEditPart instanceof ScrollingGraphicalEditPart)) {
			parentEditPart = parentEditPart.getParent();
		}
		
		if(!TimelineUtils.isScrollable(editPart)) {
			return;
		}
		
		if (parentEditPart != null) {
			ScrollingGraphicalEditPart scrollingEP = (ScrollingGraphicalEditPart) parentEditPart;
			final IFigure figure = ((GraphicalEditPart)editPart).getFigure();
			final ScrollPane scrollPane = (ScrollPane) scrollingEP.getFigure();
			SELECTION_MODE selectionMode = getTimeline().getSelectionMode();
			if (selectionMode == SELECTION_MODE.SCROLL_TO_CENTER) {
				doScrollCenterToEditPart(scrollPane, figure, 0.5f, 0.5f);
				timeline.doVerticalScroll(TimelineViewer.this, (GraphicalEditPart) editPart);
			} else {
				Rectangle bounds = figure.getBounds();
				Rectangle clientArea = scrollPane.getClientArea();
				boolean outOfBounds = bounds.height != 0 && bounds.width != 0
//						&& (bounds.x < clientArea.x || (bounds.x + bounds.width > clientArea.x + clientArea.width));
						&& (bounds.x >= clientArea.x + clientArea.width || (bounds.x + bounds.width <= clientArea.x));
				if (outOfBounds) {
					if (selectionMode == SELECTION_MODE.SCROLL_TO_VISIBLE) {
						doScrollToEditPart(scrollPane, figure);
					} else if (selectionMode == SELECTION_MODE.SCROLL_TO_LEFT_JUSTIFY) {
						doScrollCenterToEditPart(scrollPane, figure, 0f, 0.5f);
					}
				}
			}
		}
	}

	public static void doScrollToEditPart(final ScrollPane scrollPane, final IFigure figure) {
		Rectangle bounds = figure.getBounds().getCopy();
		// because GEF is in absolute coords, we need to adjust for that
		if (figure.getParent() != null) {
			Rectangle parentBounds = figure.getParent().getBounds();
			Rectangle viewportBounds = scrollPane.getViewport().getBounds();
			Rectangle clientArea = scrollPane.getClientArea();
			int viewportMidpoint_x = (viewportBounds.width - viewportBounds.x)/2;
			int viewportMidpoint_y = (viewportBounds.height - viewportBounds.y)/2;

			int x = bounds.x - parentBounds.x;
			// if shortest path is to scroll right, do so
			if (bounds.x > viewportMidpoint_x) {
				x -= clientArea.width - bounds.width;
			}

			int y = bounds.y - parentBounds.y;
			if (bounds.y > viewportMidpoint_y) {
				y -= clientArea.height - bounds.height;
			}
			bounds.x = x;
			bounds.y = y;
		}
		scrollPane.scrollTo(bounds.getLocation());
	}

	public static void doScrollCenterToEditPart(final ScrollPane scrollPane, final IFigure figure, float percentFromLeft, float percentFromTop) {
		Rectangle bounds = figure.getBounds().getCopy();
		// because GEF is in absolute coords, we need to adjust for that
		if (figure.getParent() != null) {
			Rectangle parentBounds = figure.getParent().getBounds();
			Rectangle clientArea = scrollPane.getClientArea();
			
			int x = (int) (bounds.x - parentBounds.x - clientArea.width * percentFromLeft + bounds.width*percentFromLeft);
			int y = (int) (bounds.y - parentBounds.y - clientArea.height * percentFromTop + bounds.height*percentFromTop);
			
			bounds.x = x;
			bounds.y = y;
		}
		scrollPane.scrollTo(bounds.getLocation());
	}
	
	private final class ToolbarMouseListener implements MouseListener {
		@Override
		public void mouseDown(MouseEvent e) {
			Object property = getTimelineSectionModel();
			if (property == null) {
				property = getModel();
			}
			getSite().getSelectionProvider().setSelection(new StructuredSelection(property));
		}

		@Override public void mouseUp(MouseEvent e) 		 { /* nothing */ }
		@Override public void mouseDoubleClick(MouseEvent e) { /* nothing */ }
	}

	private final class SectionNameAdapter extends AdapterImpl {
		@Override
		public void notifyChanged(Notification msg) {
			if (TimelinePackage.Literals.SECTION__NAME == msg.getFeature()) {
				final String newName = msg.getNewStringValue();
				WidgetUtils.runInDisplayThread(nameLabel, new Runnable() {
					@Override
					public void run() {
						nameLabel.setText(newName);
						if (toolbarComposite != null) {
							toolbarComposite.layout();
						}
					}
				});
			}
		}
	}

	private final class TimelineCanvas extends Canvas {

		private TimelineCanvas(Composite parent) {
			super(parent, SWT.NO_BACKGROUND);
			if (CommonUtils.isWSCocoa()) {
				// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=282229
				// and more related issues talking about this flag and
				// buffering behavior.
				int no_buffer = ~SWT.DOUBLE_BUFFERED & super.getStyle();
				ReflectionUtils.set(this, "style", no_buffer);
			}
		}
		
		// This is similar to FigureCanvas.computeSize
		@Override
		public Point computeSize(int wHint, int hHint, boolean changed) {
			// this forwards the size calculation to the figure of the contents
			GraphicalEditPart contents = (GraphicalEditPart) TimelineViewer.this.getContents();
			IFigure figure = contents.getFigure();
			// SPF-9611 -- Need to flush cached preferred sizes so that vertical scrolling
			// will work properly when the contents or row height of a section are changed
			if (changed) {
				figure.invalidateTree();
			}
			Dimension d = figure.getPreferredSize(wHint, hHint);
			return new Point(d.width, d.height);
		}

	}
	
	/**
	 * This corrects a problem with org.eclipse.jface.util.DelegatingDropAdapter (extended by
	 * org.eclipse.gef.dnd.DelegatingDropAdapter) that prevents the dropAccept method from being called
	 * on the current TransferDropTargetListener.  In the overall drop event sequence, a dragLeave event
	 * precedes the dropAccept event which causes the current listener to be set to null, preventing the dropAccept
	 * from being invoked on it.
	 * 
	 * This patch calls dragEnter to reestablish the current listener, calls the original dropAccept method, then
	 * calls dragLeave to clear the current listener again (not sure this last step is needed).
	 * 
	 * @author rnado
	 *
	 */
	static class TimelineDelegatingDropAdapter extends DelegatingDropAdapter {

		@Override
		public void dropAccept(DropTargetEvent event) {
			dragEnter(event);
			super.dropAccept(event);
			dragLeave(event);
		}
		
	}

}
