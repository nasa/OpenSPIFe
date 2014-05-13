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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.GroupingTimelineContentProvider;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.TimelineFeedbackManager;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.advisor.preferences.PlanAdvisorPreferences;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.dnd.DropTargetEvent;

public class RowDataDropEditPolicy extends TimelineDropEditPolicy {

  private static boolean debugP = false;
  
	private TimelineFeedbackManager timelineFeedbackManager;
	private boolean isDraggingActivity = false;
	private EPlanElement activityDragged = null;
  private Object lastModel = null;
  private boolean isMouseButtonPressed = false;
  private Request currentRequest = null;
  private boolean hasStateChanged = true;

	@Override
	protected EPlanElement getTargetNode() {
    //System.out.println("enter getTargetNode()");
		TreeTimelineContentProvider cp = getCastedViewer().getTreeTimelineContentProvider();
		if (cp instanceof GroupingTimelineContentProvider) {
	    //System.out.println("leave getTargetNode()");
			return getCastedViewer().getPlan();
		}	
		EditPart ep = getHost();
		Object model = ep.getModel();
		if ( model != lastModel ) {
		  LogUtil.debug( "getTargetNode(): model = " + model );
		  lastModel = model;
		}
		if (model instanceof EPlanElement) {
			EPlanElement ePlanElement = (EPlanElement) model;
			while (ePlanElement.eContainer() instanceof EPlanElement) {
				ePlanElement = (EPlanElement) ePlanElement.eContainer();
			}
      //System.out.println("leave getTargetNode()");
			return ePlanElement;
		}
		ep = ep.getParent();
		if (ep != null) {
			TimelineDropEditPolicy policy = (TimelineDropEditPolicy) ep.getEditPolicy(DROP_ROLE);
			if (policy != null) {
				EPlanElement pe = policy.getTargetNode();
				if (pe != null) {
		      //System.out.println("leave getTargetNode()");
					return pe;
				}
			}
			ep = ep.getParent();
		}
    //System.out.println("leave getTargetNode()");
		return null;
	}

  private EPlanElement getEPlanElement( Object model ) {
    if ( model instanceof EPlanElement ) return (EPlanElement)model;
    return null;
  }
  private EPlanElement getSelectedEPlanElement( Request request ) {
    //System.out.println( "enter getSelectedEPlanElement(R)" );
    EPlanElement selectedNotPrimary = null;
    for ( Object o : getCastedViewer().getSelectedEditParts() ) {
      if ( o instanceof TemporalNodeEditPart ) {
        TemporalNodeEditPart tnep = (TemporalNodeEditPart)o;
        EPlanElement a = getEPlanElement( tnep.getModel() );
        if ( a != null ) {
          if ( tnep.getSelected() == EditPart.SELECTED_PRIMARY ) {
            //System.out.println( "leave getSelectedEPlanElement(R)" );
            return a;
          } else if ( selectedNotPrimary == null
                      && tnep.getSelected() == EditPart.SELECTED ) {
            selectedNotPrimary = a;
          }
        }
      }
    }
    if ( selectedNotPrimary != null ) {
      //System.out.println( "leave getSelectedEPlanElement(R)" );
      return selectedNotPrimary;
    }
    //System.out.println( "leave getSelectedEPlanElement(R)" );
    return null;
  }

  boolean updateEditState( Request newRequest, boolean buttonPressed,
                       EPlanElement newDraggedActivity ) {
    //System.out.println("enter updateState()");
    hasStateChanged = false;
    if ( buttonPressed != isMouseButtonPressed ) {
      hasStateChanged = true;
      isMouseButtonPressed = buttonPressed;
    }
    if ( currentRequest != newRequest && 
         ( currentRequest == null || 
           !currentRequest.getType().equals( newRequest.getType() ) ) ) {
      hasStateChanged = true;
      currentRequest = newRequest;
    }
    if ( activityDragged != newDraggedActivity ) {
      hasStateChanged = true;
      //if ( newDraggedActivity != null ) {
        activityDragged = newDraggedActivity;
      //}
    }
    //System.out.println("leave updateState()");
    return hasStateChanged;
  }
  
  protected void updateFeedbackRanges(Request request) {
    //System.out.println( "enter updateFeedbackRanges()" );
    boolean mouseButtonPressed = getTimelineFeedbackManager().isMouseDown();
    EPlanElement activity = getSelectedEPlanElement( request );
    if ( debugP ) {
      if ( activity == null && activityDragged != null ) {
        System.out.println( "no activity" );
      }
      if ( activity != null && activityDragged == null ) {
        System.out.println( "got activity " + activity.getName() );
      }
    }

    boolean changed = updateEditState( request, mouseButtonPressed, activity );
    if ( changed && debugP ) {
      System.err.println( "updateFeedbackRanges: "
                          + ( isDraggingActivity ? "dragging " : "NOT dragging " )
                          + ( activityDragged == null ? "null" : activity.getName() )
                          + " "
                          + ( mouseButtonPressed ?
                              "button PRESSED " : "button NOT pressed " )
                          + request.getClass().getSimpleName() + " type="
                          + request.getType() );
    }

    boolean wasDraggingActivity = isDraggingActivity;
    isDraggingActivity = mouseButtonPressed && ( activityDragged != null );
    if ( !wasDraggingActivity && isDraggingActivity ) {
      if ( debugP ) {
        System.out.println( "dragging ON" );
      }
    }
    if ( wasDraggingActivity && !isDraggingActivity ) {
      if ( debugP ) {
        System.out.println( "dragging OFF" );
      }
      activityDragged = null;
    }
    // Always try to create feedback so that updates can come in while in the
    // middle of a drag.
    if ( isDraggingActivity ) {
      // if the activity changed, we need to flush
      if ( changed ) {
        getTimelineFeedbackManager().removeFeedbackRanges();
      }
      getTimelineFeedbackManager().createFeedbackRanges( activityDragged );
    } else {
      getTimelineFeedbackManager().removeFeedbackRanges();
    }
    //System.out.println( "leave updateFeedbackRanges()" );
  }

  @Override
	public void showTargetFeedback(Request request) {
    //System.out.println( "enter showTargetFeedback()" );
		super.showTargetFeedback(request);

		boolean isAspenReordering = PlanAdvisorPreferences.isAspenFeedback();
		if (isAspenReordering) {
		  updateFeedbackRanges(request);
		}

    //System.out.println( "leave showTargetFeedback()" );
	}

	@Override
	public void eraseTargetFeedback(Request request) {
    //System.out.println( "enter eraseTargetFeedback()" );
		super.eraseTargetFeedback(request);
		
    boolean isAspenReordering = PlanAdvisorPreferences.isAspenFeedback();
    if (isAspenReordering) {
      updateFeedbackRanges(request);
    }
    //System.out.println( "leave eraseTargetFeedback()" );
	}

	@Override
	protected DropTargetEvent getDropTargetEvent(Request request) {
    //System.out.println( "enter getDropTargetEvent()" );
		DropTargetEvent event = (DropTargetEvent)request.getExtendedData().get(DROP_TARGET_EVENT);
		event.getSource();
    //System.out.println( "leave getDropTargetEvent()" );
		return event;
	}
	
	private TimelineFeedbackManager getTimelineFeedbackManager() {
    //System.out.println( "enter getTimelineFeedbackManager()" );
		if(timelineFeedbackManager == null) {
			PlanTimelineViewer castedViewer = getCastedViewer();
			PlanTimeline timeline = castedViewer.getTimeline();
			EPlan plan = timeline.getPlan();
			// TODO PlanAdvisor for Violations
			PlanAdvisorMember planAdvisorMember = PlanAdvisorMember.get(plan);
			List<ViolationTracker> violationTrackers = planAdvisorMember.getViolationTrackers();
			for (ViolationTracker tracker : violationTrackers) {
				tracker.getViolation();
			}
			
      timelineFeedbackManager =
          new TimelineFeedbackManager( castedViewer, castedViewer.getPlan(),
                                       this );
		}
    //System.out.println( "leave getTimelineFeedbackManager()" );
		return timelineFeedbackManager;
	}
}
