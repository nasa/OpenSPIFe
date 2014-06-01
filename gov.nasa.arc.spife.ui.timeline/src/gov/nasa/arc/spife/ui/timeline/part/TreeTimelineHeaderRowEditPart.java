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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineHeaderRowSelectionEditPolicy;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.emf.SafeAdapter;

import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ArrowButton;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class TreeTimelineHeaderRowEditPart<T> extends TimelineViewerEditPart<T> {

	private static final int LEVEL_PADDING 		   				= 25;

	private Listener listener 									= new Listener();
	
	protected RectangleFigure levelFigure 						= null;
	protected ArrowButton expansionButton						= null;
	protected Label titleLabel 									= null;
	
	private int minFigureHeight 								= 0;
	
	private ExpansionModel expansionModel 						= null;
	
	private ServiceListener serviceListener						= null;

	public ILabelProvider getLabelProvider() {
		return getViewer().getLabelProvider();
	}
	
	public TreeTimelineContentProvider getTimelineTreeContentProvider() {
		TimelineViewer viewer = getViewer();
		if (viewer==null) return null;
		return viewer.getTreeTimelineContentProvider();
	}
	
	@Override
	public void activate() {
		super.activate();
		if (getLabelProvider() != null) {
			getLabelProvider().addListener(listener);
		}
		expansionModel = (ExpansionModel) getViewer().getProperty(ExpansionModel.ID);
		if (expansionModel != null) {
			expansionModel.addPropertyChangeListener(listener);
		}
		getFigure().addLayoutListener(new LayoutListener.Stub() {
			@Override public void postLayout(IFigure container) { updateVisibility(); }
		});
		getViewer().addPropertyChangeListener(listener);
		getTimelineTreeContentProvider().addListener(listener);
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
		getViewer().getTimelineSectionModel().eAdapters().add(listener);
		serviceListener = new ServiceListener(getViewer().getTimeline(), (EObject)getModel());
		getTimeline().addPropertyChangeListener(listener);
		getTimeline().getWorkspaceResourceService().addResourceChangeListener(serviceListener);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		if (getLabelProvider() != null) {
			getLabelProvider().removeListener(listener);
			getLabelProvider().dispose();
		}
		getViewer().removePropertyChangeListener(listener);
		getTimelineTreeContentProvider().removeListener(listener);
		if (expansionModel != null) {
			expansionModel.removePropertyChangeListener(listener);
			expansionModel = null;
		}
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
		getViewer().getTimelineSectionModel().eAdapters().remove(listener);
		getTimeline().removePropertyChangeListener(listener);
		getTimeline().getWorkspaceResourceService().removeResourceChangeListener(serviceListener);
	}
	
	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new TimelineHeaderRowSelectionEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		ILabelProvider labelProvider = getLabelProvider();
		Figure figure = new Figure() {

			@Override
			public void setBackgroundColor(Color bg) {
				super.setBackgroundColor(bg);
				TreeTimelineHeaderRowEditPart.this.setBackgroundColor(this, bg);
			}						
		};
		ToolbarLayout layout = new ToolbarLayout(true) {

			@Override
			/* This is here so that the header height matches the row data height */
			protected Dimension calculateMinimumSize(IFigure container, int hint, int hint2) {
				Dimension d = super.calculateMinimumSize(container, hint, hint2);
				d.height = Math.max(TimelineUtils.getRowElementHeight(TreeTimelineHeaderRowEditPart.this), minFigureHeight);
				return d;
			}

			@Override
			protected Dimension calculatePreferredSize(IFigure container, int hint, int hint2) {
				Dimension d = new Dimension(hint, hint2);
				if (container.getFont() != null) {
					d = super.calculatePreferredSize(container, hint, hint2);
				}
				d.height = Math.max(TimelineUtils.getRowElementHeight(TreeTimelineHeaderRowEditPart.this), minFigureHeight);
				return d;
			}
			
		};
		layout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
		figure.setLayoutManager(layout);
		String text = getModel().toString();
		if (labelProvider != null) {
			text = labelProvider.getText(getModel());
		}
		figure.setToolTip(new Label(text));
		
		addButtons(figure);
		addTitle(figure);
		
		figure.setOpaque(true);
		return figure;
	}

	protected void addButtons(Figure figure) {
		// for extending classes to implement
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		updateExpansionVisual(isExpanded());
		updateTitleVisual();
	}

	protected void addTitle(IFigure figure) {
		Object model = getModel();
		
		final int rowHeight = TimelineUtils.getRowElementHeight(TreeTimelineHeaderRowEditPart.this);
		Figure title = new Figure();
		ToolbarLayout layout = new ToolbarLayout(true);
		layout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
		title.setLayoutManager(layout);
		layout.setSpacing(6);
		
		int depth = getDepth();
		TreeTimelineContentProvider cp = getTimelineTreeContentProvider();
		ILabelProvider lp = getLabelProvider();
		String labelText = lp != null ? lp.getText(model) : model + "";
		Image labelImage = lp != null ? lp.getImage(model) : null;
		
		{	
			levelFigure = new RectangleFigure(); 
			levelFigure.setOutline(false);
			levelFigure.setSize(depth*LEVEL_PADDING, rowHeight);
			levelFigure.setOpaque(true);
			title.add(levelFigure);
		}
		
		{
			expansionButton = new ArrowButton(PositionConstants.EAST) {
				
				@Override
				protected void createTriangle() {
					Triangle tri = new Triangle();
					tri.setOutline(true);
					tri.setBackgroundColor(ColorConstants.listForeground);
					tri.setForegroundColor(ColorConstants.listForeground);
					setContents(tri);
				}
				
			};
			expansionButton.setBorder(null);
			expansionButton.setOpaque(true);
			expansionButton.setPreferredSize(rowHeight,rowHeight);
			expansionButton.setForegroundColor(ColorConstants.black);
			expansionButton.setVisible(cp != null && cp.hasChildren(model));
			expansionButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					toggleExpanded();
				}
			});
			title.add(expansionButton);
		}
		
		{
			titleLabel = new Label();
			titleLabel.setTextAlignment(PositionConstants.CENTER);
			titleLabel.setText(labelText);
			if (labelImage != null) {
				titleLabel.setIcon(labelImage);
			}
			titleLabel.setOpaque(true);
			title.add(titleLabel);
			refreshTitleLabelFont();
		}

		figure.add(title);
	}

	protected int getDepth() {
		Object model = getModel();
		TreeTimelineContentProvider cp = getTimelineTreeContentProvider();
		int depth = 0;
		Object object = model;
		while (cp != null && cp.getParent(object) != null) {
			object = cp.getParent(object);
			depth++;
		}
		return Math.max(depth-1, 0);
	}
	
	public void setBackgroundColor(IFigure figure, Color color) {
		levelFigure.setBackgroundColor(color);
		expansionButton.setBackgroundColor(color);
		titleLabel.setBackgroundColor(color);
	}

	private void refreshTitleLabelFont() {
		int height = TIMELINE_PREFERENCES.getInt(TimelinePreferencePage.P_CONTENT_FONT_SIZE);
		height -= height * 0.2;
		titleLabel.setPreferredSize(SWT.DEFAULT, height);
		titleLabel.setFont(TimelineUtils.deriveRowElementHeightFont(titleLabel.getFont()));
	}
	
	protected void updateExpansionVisual(boolean expansion) {
		TreeTimelineContentProvider contentProvider = getTimelineTreeContentProvider();
		if (contentProvider == null) {
			Logger.getLogger(TreeTimelineHeaderRowEditPart.class).error("No content provider is null for: " + this);
		} else {
			boolean visible = contentProvider.hasChildren(getModel());
			expansionButton.setVisible(visible);
		}
		
		final int rowHeight = TimelineUtils.getRowElementHeight(TreeTimelineHeaderRowEditPart.this);
		if (expansion) {
			expansionButton.setDirection(PositionConstants.SOUTH);
			expansionButton.setPreferredSize(rowHeight / 2, rowHeight / 2);
		} else {
			expansionButton.setDirection(PositionConstants.EAST);
			expansionButton.setPreferredSize(rowHeight / 3, rowHeight / 2);
		}
		expansionButton.setVisible(expansionButton.isVisible() && isGrouped());
	}

	protected final boolean isGrouped() {
		TimelineViewer viewer = getViewer();
		if (viewer == null) {
			return false;
		}
		Object grouped = viewer.getProperty(TIMELINE_GROUP_ELEMENTS);
		return grouped == null || (Boolean) grouped;
	}
	
	protected void updateTitleVisual() {
		Object node = getModel();
		ILabelProvider lp = getLabelProvider();
		titleLabel.setText(lp != null ? lp.getText(node) : node + "");
		titleLabel.setIcon(lp != null ? lp.getImage(node) : null);
		
		int depth = getDepth();
		final int rowHeight = TimelineUtils.getRowElementHeight(TreeTimelineHeaderRowEditPart.this);
		levelFigure.setSize(depth*LEVEL_PADDING, rowHeight);
		levelFigure.revalidate();
	}

	@Override
	public void performRequest(final Request req) {
		if (REQ_ROW_DATA_LAYOUT == req.getType()) {
			GEFUtils.runInDisplayThread(this, new Runnable() {
				@Override
				public void run() {
					Integer newHeight = (Integer) req.getExtendedData().get("height");
					if (minFigureHeight != newHeight) {
						minFigureHeight = newHeight;
						getFigure().revalidate();
						refreshVisuals();
					}
				}
			});
		} else if (req.getType() == RequestConstants.REQ_OPEN) {
			final ExpansionModel model = (ExpansionModel) getViewer().getProperty(ExpansionModel.ID);
			if (model != null) {
				GEFUtils.runLaterInDisplayThread(this, new Runnable() {
					@Override
					public void run() {
						T model2 = getModel();
						Set<Object> objects = Collections.<Object>singleton(model2);
						model.setExpanded(objects, !model.isExpanded(model2));
					}
				});
			}
		}
		super.performRequest(req);
	}
	
	protected final boolean isExpanded() {
		return expansionModel != null && expansionModel.isExpanded(getModel());
	}

	protected void setExpanded(boolean expanded) {
		if (this.expansionModel != null) {
			T model2 = getModel();
			Set<Object> objects = Collections.<Object>singleton(model2);
			this.expansionModel.setExpanded(objects, expanded);
			updateExpansionVisual(expanded);
		}
	}
	
	protected final void toggleExpanded() {
		setExpanded(!isExpanded());
	}

	private void updateVisibility() {
		// Check if it is from the visible frozen content
		boolean visible = getViewer().isTimelineSectionFrozen();
		if(!visible) {
			// Check if it is visible within the scrollable content
			visible = TimelineUtils.isVerticallyVisible(this);
		}
		getFigure().setVisible(visible);
	}
	
	private class Listener extends SafeAdapter implements TreeTimelineContentProvider.Listener, ILabelProviderListener, IPropertyChangeListener, PropertyChangeListener {

		@Override
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			if (event.getElement() == getModel()) {
				GEFUtils.runInDisplayThread(TreeTimelineHeaderRowEditPart.this, new Runnable() {
					@Override
					public void run() {
						updateTitleVisual();
					}
				});
			}
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT.equals(event.getProperty())
					|| TimelinePreferencePage.P_SCALE_FONT_SIZE.equals(event.getProperty())
					|| TimelinePreferencePage.P_CONTENT_FONT_SIZE.equals(event.getProperty())) {
				refreshTitleLabelFont();
				getFigure().getParent().invalidateTree();
				refreshVisuals();
			}
		}
		
		@Override
		public void propertyChange(java.beans.PropertyChangeEvent event) {
			if (ExpansionModel.EXPANDED.equalsIgnoreCase(event.getPropertyName())) {
				refreshInDisplayThread();
			} else if (TIMELINE_GROUP_ELEMENTS.equals(event.getPropertyName())) {
				setExpanded(false);
				refreshInDisplayThread();
			} else if (TIMELINE_EVENT_VERTICAL_REFRESH.equals(event.getPropertyName())) {
				updateVisibility();
			} else if (TimelineViewer.CONTENT_PROVIDER.equals(event.getPropertyName())) {
				TreeTimelineContentProvider oldValue = (TreeTimelineContentProvider) event.getOldValue();
				if (oldValue != null) {
					oldValue.removeListener(this);
				}

				TreeTimelineContentProvider newValue = (TreeTimelineContentProvider) event.getNewValue();
				if (newValue != null) {
					newValue.addListener(this);
				}
				refreshInDisplayThread();
			}
		}

		@Override
		public void contentRefresh(Set<? extends Object> elements) {
			if (elements.contains(getModel())) {
				refresh();
			}
		}
		
		@Override
		public void labelUpdate(Set<? extends Object> elements) {
			if (elements.contains(getModel())) {
				updateTitleVisual();
			}
		}

		@Override
		protected void handleNotify(Notification notification) {
			Object feature = notification.getFeature();
			if (TimelinePackage.Literals.GANTT_SECTION__ROW_HEIGHT == feature
					|| TimelinePackage.Literals.TIMELINE_CONTENT__ROW_HEIGHT == feature) {
				refreshInDisplayThread();
			}
		}
		
	}
	
	/**
	 * Listen for workspace changes and update the header title image
	 */
	private class ServiceListener implements IResourceChangeListener {
		private Timeline timeline;
		private EObject model;
		
		public ServiceListener(Timeline timeline, EObject model) {
			this.timeline = timeline;
			this.model = model;
		}
		
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			IResource resource = CommonUtils.getAdapter(timeline, IResource.class);
			if (resource == null) {
				return;
			}
			Resource eResource = model.eResource();
			if (eResource == null) {
				return;
			}
			String uriFragment = eResource.getURIFragment(model);
			URI modelFragmentURI = URI.createURI(uriFragment.toString());
			boolean refresh = false;
			final boolean includeSubTypes = true;
			IMarkerDelta[] markerDeltas = event.findMarkerDeltas(IMarker.MARKER, includeSubTypes);
			for (IMarkerDelta delta : markerDeltas) {
				IResource markerResource = delta.getResource();
				if (CommonUtils.equals(resource, markerResource)) {
					try {
						Object attribute = null;
						if(delta.getKind() == IResourceDelta.REMOVED) {
							// get the old value of location, since the new value doesn't exist (marker deleted)
							attribute = delta.getAttribute(IMarker.LOCATION);
						} else {
							IMarker marker = delta.getMarker();
							attribute = marker.getAttribute(IMarker.LOCATION);
						}
						if (attribute != null) {
							String foreignFragment = attribute.toString();
							if (foreignFragment.startsWith(modelFragmentURI.toString())) {
								refresh = true;
								break;
							}
						}
					} catch (CoreException e) {
						String message = e.getMessage();
						if (!message.contains("Marker id") || !message.contains(" not found.")) {
							LogUtil.warn(e);
						}
					}
				}
			}		
			if (refresh) {
				WidgetUtils.runInDisplayThread(timeline.getControl(), new Runnable() {
					@Override
					public void run() {
						updateTitleVisual();
					}
				});
			}
		}

	}

}
