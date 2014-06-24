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
/*
 * Created on Mar 25, 2005
 */
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.core.plan.editor.timeline.builder.PlanTimelineBuilder;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.util.GEFDeleteOperation;
import gov.nasa.arc.spife.core.plan.editor.timeline.util.PlanTimelineEditorUtils;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants.ScrollAlignment;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.help.ContextProvider;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.common.ui.editor.MarkerUtils;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanEditorPart;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.constraints.IChainingReordersByTimeEditor;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.properties.UndoablePropertySheetEntry;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class TimelineEditorPart extends AbstractPlanEditorPart implements IEditorPart, IChainingReordersByTimeEditor, IGotoMarker {

	private ActionRegistry actionRegistry;
	private PlanTimeline timeline;

	@Override
	public String getId() {
		return TimelineConstants.TIMELINE_FILE_EXT;
	}

	/**
	 * Sets the site and input for this editor then creates and initializes the actions.
	 * Subclasses may extend this method, but should always call <code>super.init(site, input)
	 * </code>.
	 * @see org.eclipse.ui.IEditorPart#init(IEditorSite, IEditorInput)
	 */
	@Override
	public void init(final IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		final PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
		final ETimeline timelineModel = getTimelineModel();
		timeline = new PlanTimeline(timelineModel);
		timeline.init(site, model);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		
		ETimeline model = timeline.getTimelineModel();
		if (model == null) {
			return;
		}
		
		Resource resource = model.eResource();
		if (resource == null) {
			return;
		}
		
		try {
			resource.save(null);
		} catch (IOException e) {
			try {
				resource.save(null);
			} catch (IOException x) {
				LogUtil.error("saving timeline resource "+resource, e);
				e.printStackTrace();
			}
		}
	}

	private ETimeline getTimelineModel() {
		Resource resource = getPlanEditorTemplateResource();
		EObject content = EMFUtils.getLoadedContent(resource);
		if (content instanceof ETimeline) {
			return (ETimeline) content;
		}
		return PlanTimelineEditorUtils.getTimelineModel(this);
	}

	@Override
	protected void setSite(IWorkbenchPartSite newSite) {
		newSite.setSelectionProvider(new EnsembleSelectionProvider(this.toString()));
	    super.setSite(newSite);
		IContextService contextService = (IContextService)newSite.getService(IContextService.class);
		contextService.activateContext("timeline");
	}
	
	public PlanTimeline getTimeline() {
		return timeline;
	}

	@Override
	public void createPartControl(Composite parent) {
		timeline.createPartControl(parent);
	}
	
	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		timeline.dispose();
		timeline = null;
		super.dispose();
	}

	/**
	 * Lazily creates and returns the action registry.
	 * @return the action registry
	 */
	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	/** 
	 * Returns the adapter for the specified key.
	 * 
	 * <P><EM>IMPORTANT</EM> certain requests, such as the property sheet, may be made before
	 * or after {@link #createPartControl(Composite)} is called. The order is unspecified by
	 * the Workbench.
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class type) {
		if (Timeline.class == type) {
			return getTimeline();
		}
		if (ETimeline.class == type) {
			Resource resource = getPlanEditorTemplateResource();
			EObject content = EMFUtils.getLoadedContent(resource);
			if (content instanceof ETimeline) {
				return content;
			} 
		}
		if (EditingDomain.class == type) {
			IEditorInput input = getEditorInput();
			PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
			if (model != null) {
				return model.getEditingDomain();
			}
		}
		if (IPropertySheetPage.class.equals(type)) {
			PropertySheetPage page = new PropertySheetPage();
			page.setRootEntry(new UndoablePropertySheetEntry(getCommandStack()));
			return page;
		}
		if (CommandStack.class.equals(type)) {
			return getCommandStack();
		}
		if (ActionRegistry.class.equals(type)) {
			return getActionRegistry();
		}
		if (ZoomManager.class.equals(type)) {
			return timeline.getZoomManager();
		}
		if (Page.class.equals(type)) {
			return timeline.getPage();
		}
		if (IContextProvider.class.equals(type)) {
			return new ContextProvider("gov.nasa.arc.spife.core.plan.editor.timeline.TimelineEditorPart");
		}
		return super.getAdapter(type);
	}

	/**
	 * Returns the command stack.
	 * @return the command stack
	 */
	protected CommandStack getCommandStack() {
		return timeline.getEditDomain().getCommandStack();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		timeline.setFocus();
	}

	@Override
	protected IUndoableOperation getCutOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		return new TimelinePlanClipboardCutOperation(transferable, modifier);
	}
	
	@Override
	protected IUndoableOperation getDeleteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		if (transferable != null) {
			return new TimelinePlanDeleteOperation(transferable, modifier);
		}
		Request req = new Request(RequestConstants.REQ_DELETE);
		PlanTimeline timeline = getTimeline();
		if (timeline != null && selection instanceof IStructuredSelection) {
			List list = ((IStructuredSelection)selection).toList();
			List<EditPart> editParts = new ArrayList<EditPart>();
			for (TimelineViewer v : timeline.getTimelineViewers()) {
				for (EditPart ep : v.getEditParts(list)) {
					if (ep.understandsRequest(req)) {
						editParts.add(ep);
					}
				}
			}
			CompoundCommand command = new CompoundCommand();
			command.setDebugLabel("delete elements");
			for (EditPart editPart : editParts) {
				Command cmd = editPart.getCommand(req);
				if (cmd != null) {
					command.add(cmd);
				}
			}
			if (command.canExecute()) {
				GEFDeleteOperation operation = new GEFDeleteOperation(command);
				operation.addContext(EMFUtils.getUndoContext(getTimelineModel()));
				return operation;
			}
		}
		return null;
	}
	
	@Override
	protected IUndoableOperation getPasteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ISelection pasteSelection = (!PlanEditorUtil.emfFromSelection(selection).isEmpty() || getPlan() == null) ? selection : new StructuredSelection(getPlan());
		return new PlanTimelineClipboardPasteOperation(pasteSelection, modifier, event, getTimeline());
	}

	/**
	 * Sets the cursor and selection state for an editor to reveal the position
	 * of the given marker.
	 * 
	 * @param marker the marker that contains the position information.
	 */
	@Override
	public void gotoMarker(IMarker marker) {
		Date markerStartTime = MarkerUtils.getDate(marker, MarkerConstants.START_TIME); 
		if (markerStartTime != null) {
			// See SPF-6649
			timeline.scrollToTime(markerStartTime, ScrollAlignment.CENTER);
		}
		List<EPlanElement> culprits = EPlanUtils.getCulprits(marker, getPlan().eResource());
		if (!culprits.isEmpty()) {
			ISelectionProvider selectionProvider = getSelectionProvider();
			selectionProvider.setSelection(new StructuredSelection(culprits));
		}
	}
	
	public static enum DIRECTION { Up, Down, Right, Left }

	public void moveSelection(DIRECTION direction) {
		EPlanElement firstElement = getFirstSelectedEPlanElement();
		if (firstElement != null) {
			Map<EPlanElement, Spot> spots = getSpots();
			Spot firstSpot = spots.remove(firstElement);
			if (firstSpot != null) {
				Spot next = findNextSpot(firstSpot, spots.values(), direction);
				if (next != null) {
					PlanTimelineViewer viewer = next.getViewer();
					if (viewer.getName().equals(PlanTimelineBuilder.ID)) {
						viewer.setSelection(new StructuredSelection(next.getEditPart()));
					}
				}				
			}
			
		}
	}
	
	private static class Spot {
		
		private TemporalNodeEditPart editPart;
		private Rectangle bounds;
		private ScrolledComposite scrolledComposite = null;
		
		public Spot(TemporalNodeEditPart editPart) {
			this.editPart = editPart;
			this.bounds = editPart.getFigure().getBounds().getCopy();
			Control control = getViewer().getControl();
			while (!(control instanceof ScrolledComposite)) {
				control = control.getParent();
				bounds.translate(control.getLocation().x, control.getLocation().y);
			}
			scrolledComposite = (ScrolledComposite)control;
		}
		public TemporalNodeEditPart getEditPart() {
			return editPart;
		}
		public Rectangle getBounds() {
			return bounds;
		}
		public PlanTimelineViewer getViewer() {
			return (PlanTimelineViewer) editPart.getViewer();
		}
		public  Rectangle getViewBounds() {
			return new Rectangle(scrolledComposite.getBounds());
		}
		public boolean isInsideView(DIRECTION direction) {
			Rectangle b = getBounds().getCopy();
			Rectangle v = getViewBounds().getCopy();
			switch (direction) {
			case Up:
			case Down:
				b.y = v.y;
				b.height = v.height;
				break;
			case Left:
			case Right:
				b.x = v.x;
				b.width = v.width;
			}
			return b.intersects(v);
		}
	}
	
	private EPlanElement getFirstSelectedEPlanElement() {
		EPlanElement firstElement = null;
		ISelection selection = getCurrentSelection();
		if (selection != null && selection instanceof StructuredSelection) {
			StructuredSelection selection2 = (StructuredSelection)selection;
			for (Object obj: selection2.toList()) {
				if (obj instanceof EPlanElement) {
					firstElement = (EPlanElement)obj;
					break;
				}
			}
		}
		return firstElement;
	}
	
	private Map<EPlanElement, Spot> getSpots() {
		Map<EPlanElement, Spot> spots = new TreeMap<EPlanElement, Spot>();
		for (TimelineViewer v: timeline.getTimelineViewers()) {
			for (Object obj1: v.getEditPartRegistry().values()) {
				if (obj1 instanceof ArrayList<?>) {
					for (Object obj2: (ArrayList<?>)obj1) {
						if (obj2 instanceof TemporalNodeEditPart) {
							TemporalNodeEditPart editPart = (TemporalNodeEditPart)obj2;
							spots.put(editPart.getModel(), new Spot(editPart));
						}
					}
				}
			}
		}
		return spots;
	}
	
	private static Spot findNextSpot(Spot current, Collection<Spot> spots, DIRECTION direction) {
		Spot next = null;
		double distanceToNext = 0;
		for (Spot spot: spots) {
			if (spot.isInsideView(direction)) {
				double d = getDistanceBetween(direction, current, spot);
				if (d > 0 && (distanceToNext == 0 || d < distanceToNext)) {
					next = spot;
					distanceToNext = d;
				}
			}
		}
		return next;
	}
	
	private static double getDistanceBetween(DIRECTION direction, Spot spot1, Spot spot2) {
		switch (direction) {
		case Up: {
			int dy = spot1.getBounds().y - spot2.getBounds().y;
			int dx = spot1.getBounds().x - spot2.getBounds().x;
			if (dy == 0) {
				return (dx / 1000000d);
			} else {
				return dy + (dy/Math.abs(dy)) * (dx / 1000000d);
			}
		}
		case Down: {
			int dy = spot2.getBounds().y - spot1.getBounds().y;
			int dx = spot1.getBounds().x - spot2.getBounds().x;
			if (dy == 0) {
				return (dx / 1000000d);
			} else {
				return dy + (dy/Math.abs(dy)) * (dx / 1000000d);
			}
		}
		case Left: {
			int dy = spot1.getBounds().x - spot2.getBounds().x;
			int dx = spot1.getBounds().y - spot2.getBounds().y;
			if (dy == 0) {
				return (dx / 1000000d);
			} else {
				return dy + (dy/Math.abs(dy)) * (dx / 1000000d);
			}
		}
		case Right: {
			int dy = spot2.getBounds().x - spot1.getBounds().x;
			int dx = spot1.getBounds().y - spot2.getBounds().y;
			if (dy == 0) {
				return (dx / 1000000d);
			} else {
				return dy + (dy/Math.abs(dy)) * (dx / 1000000d);
			}
		}
		}
		return 0;
	}
	
}
