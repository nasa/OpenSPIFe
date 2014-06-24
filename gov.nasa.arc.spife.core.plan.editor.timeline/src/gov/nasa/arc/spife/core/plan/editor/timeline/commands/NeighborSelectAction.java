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
//package gov.nasa.arc.spife.core.plan.editor.timeline.commands;
//
//import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineEditPart;
//import gov.nasa.arc.spife.core.plan.editor.timeline.parts.RowData;
//import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
//import gov.nasa.ensemble.core.plan.tree.PlanTreeModel;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.eclipse.core.commands.operations.IUndoableOperation;
//import org.eclipse.gef.EditPart;
//import org.eclipse.jface.viewers.StructuredSelection;
//
//public class NeighborSelectAction extends DrudgeryRemovingAction {
//
//	public enum Direction {
//		LEFT, RIGHT, UP, DOWN
//	}
//	
//	private Direction direction = null;
//	
//	public NeighborSelectAction(String label, PlanTimelineEditPart ep, Direction direction) {
//		super(label, ep);
//		this.direction = direction;
//	}
//
//	@Override
//	public void run() {
//		TemporalNodeEditPart ep = (TemporalNodeEditPart) getTimelineViewer().getSelectedEditParts().get(0);
//		TemporalNodeEditPart neighbor = getNeighbor(ep);
//		if( neighbor != null ) {
//			getTimelineViewer().getSelectionManager().setSelection(new StructuredSelection(neighbor));
//		}
//	}
//
//	@Override
//	protected boolean calculateEnabled() {
//		if (getTimelineViewer().getSelectedEditParts().size() != 1) {
//			return false;
//		}
//		
//		Object o = getTimelineViewer().getSelectedEditParts().get(0);
//		if (!(o instanceof TemporalNodeEditPart)) {
//			return false;
//		}
//		
//		TemporalNodeEditPart ep = (TemporalNodeEditPart) o;
//		PlanTimelineModel m = getPlanTimelineEditPart().getModel();
//		return m.getDepth() == PlanTreeModel.getInstance().getDepth(ep.getModel());
//	}
//
//	@Override
//	protected IUndoableOperation createOperation() {
//		return null;
//	}	
//	
//	@SuppressWarnings("unchecked")
//	private RowData getNeighborRow(TemporalNodeEditPart ep, Direction direction) {
//		RowData rowData = getRowData(ep);
//		if (rowData == null) {
//			return null;
//		}
//		
//		PlanTimelineModel model = getTimelineModel(rowData);
//		if (model == null) {
//			return null;
//		}
//		
//		int index = model.getRows().indexOf(rowData.getModel());
//		if (index == -1) {
//			return null;
//		}
//		
//		switch(direction) {
//		case UP:
//			index--; break;
//		case DOWN:
//			index++; break;
//		case LEFT:
//		case RIGHT:
//			// same
//			break;
//		}
//
//		if (index < 0 || index >= model.getRows().size()) {
//			return null;
//		} // else...
//		Map map = getTimelineViewer().getEditPartRegistry();
//		return (RowData) map.get(model.getRows().get(index));
//	}
//	
//
//	private PlanTimelineModel getTimelineModel(RowData rowData) {
//		EditPart ep = rowData.getParent();
//		while(ep != null && !(ep.getModel() instanceof PlanTimelineModel)) {
//			ep = ep.getParent();
//		}
//		
//		return (PlanTimelineModel) (ep == null ? null : ep.getModel());
//	}
//
//	/*
//	 * Get neighbor (with respect to start time)
//	 */
//	private TemporalNodeEditPart getNeighbor(TemporalNodeEditPart ep) {
//		RowData rowData = getRowData(ep);
//		if (rowData == null) {
//			return null;
//		}
//		
//		RowData neighborRowData = getNeighborRow(ep, direction);
//		if( neighborRowData == null ) {
//			return null;
//		}
//
//		List<TemporalNodeEditPart> parts = new ArrayList<TemporalNodeEditPart>();
//		for(Object child : neighborRowData.getChildren()) {
//			if (child instanceof TemporalNodeEditPart) {
//				parts.add((TemporalNodeEditPart) child);
//			}
//		}
//		
//		if (rowData != neighborRowData) {
//			parts.add(ep);
//		}
//		
//		Collections.sort(parts, new Comparator<TemporalNodeEditPart>() {
//        	public int compare(TemporalNodeEditPart a, TemporalNodeEditPart b) {
//        		Date cat_a = a.getTemporalExtent().getStart();
//        		Date cat_b = b.getTemporalExtent().getStart();
//        		return cat_a.compareTo(cat_b);
//        	}
//        });
//
//		int index = parts.indexOf(ep);
//		TemporalNodeEditPart prevPart = index>0					?parts.get(index-1):null;
//		TemporalNodeEditPart nextPart = index+1<parts.size()	?parts.get(index+1):null;
//		
//		TemporalNodeEditPart closestPart = null;
//		long curTime = ep.getTemporalExtent().getStart().getTime();
//		long prevTime = prevPart==null?Integer.MAX_VALUE:prevPart.getTemporalExtent().getStart().getTime();
//		long nextTime = nextPart==null?Integer.MAX_VALUE:nextPart.getTemporalExtent().getStart().getTime();
//		if (Math.abs(prevTime - curTime) < Math.abs(nextTime - curTime)) {
//			closestPart = prevPart;
//		} else {
//			closestPart = nextPart;
//		}
//		
//		if( prevPart == null && nextPart == null ) {
//			return null;
//		}
//		
//		switch(direction) {
//		case LEFT:
//			return prevPart;
//		case RIGHT:
//			return nextPart;
//		case UP:
//		case DOWN:
//			return closestPart;
//		}
//		return null;
//	}
//	
//	private RowData getRowData(TemporalNodeEditPart ep) {
//		EditPart parent = ep.getParent();
//		while(parent != null && !(parent instanceof RowData)) {
//			parent = parent.getParent();
//		}
//		return (RowData) parent;
//	}
//}
