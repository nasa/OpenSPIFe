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

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.jscience.physics.amount.Amount;

public class TimelineFeedbackManager implements DragDetectListener,
                                                MouseListener {

  private static boolean debugP = false;
  
	private TimelineViewer timelineViewer;
	private LayerManager layerManager;
	private IFigure feedbackLayerData;
	private Map<Rectangle, InvalidRangeRectangleFigure> map;
	private PlanAdvisorMember planAdvisorMember = null;
	AbstractEditPolicy editPolicy = null;
	
  // This feedbackShowing flag just makes sure that figures are created already
  // and has not been disposed of yet. The mouse movements trigger the create
  // method quite often so this is used so the process will not get overloaded.
	private boolean feedbackShowing = false;
  private Map< Date, List< Violation >> whatIfViolations = null;
  
  private boolean mouseUp = true;
  private boolean isDragging = false;
	
	public TimelineFeedbackManager(TimelineViewer timelineViewer, EPlan plan,
	                               AbstractEditPolicy policy) {
		this.timelineViewer = timelineViewer;
		this.editPolicy = policy;
		layerManager = (LayerManager)timelineViewer.getEditPartRegistry().get(LayerManager.ID);
		feedbackLayerData = layerManager.getLayer(TimelineConstants.LAYER_FEEDBACK_DATA);
		map = new HashMap<Rectangle, InvalidRangeRectangleFigure>();
    planAdvisorMember = PlanAdvisorMember.get(plan);
    timelineViewer.getControl().addDragDetectListener( this );
    timelineViewer.getControl().addMouseListener( this );
	}
	
	public synchronized void createFeedbackRanges(EPlanElement ePlanElement) {
		if(!feedbackShowing) {
      if ( debugP ) {
        System.err.println( "create ranges" );
      }
      //removeFeedbackRanges();  // may use as a precaution
			List<Rectangle> rectangles = getFeedbackSelectionRectangles(ePlanElement);
			InvalidRangeRectangleFigure figure = null;
			for(Rectangle rect : rectangles) {
				// check if the rect boundary is already associated with a figure.
				figure = null;//map.get(rect); // REVIEW -- why wouldn't this always be null?
				if(figure == null) {
					// Create the figure for the rect boundary
					figure = new InvalidRangeRectangleFigure();
					figure.translateToRelative(rect);
					figure.setBounds(rect);
					map.put(rect, figure);
				}
	      // Set the figure creation blocking flag until we release the figures.
        feedbackShowing = true; // This is true only if we have rectangles to
                                // be in this loop; allows for trying again in
                                // case the advisors weren't ready.
			}
			if ( feedbackShowing ) {
			  addFeedback(map.values());
			}
		}
	}
	
  public synchronized void removeFeedbackRanges() {
    if ( feedbackShowing ) {
      if ( debugP ) {
        System.err.println( "remove ranges" );
      }
      removeFeedback( map.values() );
      map.clear();
      // Figures are released, allow creation to happen again.
      feedbackShowing = false;
    }
  }

	private void addToRectangleList(Date start, Date end, List<Rectangle> list ) {
    TemporalExtent temporalExtent = null;
    temporalExtent = new TemporalExtent(start, end);
    Rectangle rectangle = toRectangle(temporalExtent);
    if(!list.contains(rectangle)) {
      if ( debugP ) {
        System.err.println("adding rectangle: " + rectangle );
      }
      list.add(rectangle);
    }
	}
	
  private List< Rectangle >
      getFeedbackSelectionRectangles( EPlanElement ePlanElement ) {

    // the return list
    List< Rectangle > list = new ArrayList< Rectangle >();

    // Get the start and end range for calculating rectangles. 
    Timeline timeline = timelineViewer.getTimeline();
    Page page = timeline.getPage(); // REVIEW -- Should this be intersected with
                                    // the screen?
    if ( debugP ) {
      System.err.println( "page extent = " + page.getExtent() );
    }

    Date screenStartDate = timeline.getCurrentScreenEarliestDate();
    Date screenEndDate = timeline.getCurrentScreenLatestDate();
    if ( debugP ) {
      System.err.println( "screen = (" + screenStartDate + ", " + screenEndDate
                          + ")" );
    }

    // Get what-if violations, from which "badland" regions will generated to
    // cover adjacent intervals that have conflicts.
    whatIfViolations  =
        planAdvisorMember.getWhatIfViolations( ePlanElement, screenStartDate,
                                               screenEndDate );
    if ( debugP ) {
      System.out.println( "whatIfViolations = " + whatIfViolations );
    }

    // Walk through what if violation map, creating the rectangles.  
    boolean started = false, ending = false, first = true;
    boolean hasViolations = false, hadViolations = false;
    Date startRectangle = screenStartDate;
    Date date = null;
    Amount< Duration > activityDur = getDuration(ePlanElement);
    for ( Entry< Date, List< Violation > > entry : whatIfViolations.entrySet() ) {
      hadViolations = hasViolations;
      date = entry.getKey();
      hasViolations =
          ( entry.getValue() != null && !entry.getValue().isEmpty() );
      // Add duration to convert from valid start times to valid [st,et] intervals.
      if ( !hadViolations && hasViolations && !first ) {
        date = DateUtils.add(date, activityDur);
      }
      started = started || !date.before( screenStartDate );
      ending = date.after( screenEndDate );
      if ( debugP ) {
        System.out.println( "date=" + date + ", hasViolations=" + hasViolations
                            + ", started=" + started + ", ending=" + ending );
      }
      // Get a start or endpoint of the rectangle.
      if ( started ) {
        if ( !hadViolations && hasViolations ) {
          // Got start of rectangle.
          startRectangle = date;
          if ( debugP ) {
            System.out.println( "startRectangle = " + date );
          }
        } else if ( hadViolations && !hasViolations ) {
          // Got end of rectangle -- add it to the list!
          if ( ending ) date = screenEndDate;
          if ( debugP ) {
            System.out.println( "adding rectangle (" + startRectangle + ", "
                                + date + ")" );
          }
          if ( !startRectangle.after( date ) ) {
            addToRectangleList( startRectangle, date, list );
          }
        }
      }
      // Don't create rectangles outside of bounds.
      if ( ending ) break;
      first = false;
    }

    // handle the end boundary
    if ( hadViolations ) {
      if ( ending ) date = screenEndDate;
      if ( debugP ) {
        System.out.println( "adding rectangle (" + startRectangle + ", " + date
                            + ")" );
      }
      if ( !startRectangle.after( date ) ) {
        addToRectangleList( startRectangle, date, list );
      }
    } else if ( !hadViolations && hasViolations && !ending ) {
      if ( debugP ) {
        System.out.println( "adding rectangle (" + startRectangle + ", " + date
                            + ")" );
      }
      if ( !startRectangle.after( screenEndDate ) ) {
        addToRectangleList( startRectangle, screenEndDate, list );
      }
    }
    if ( debugP ) {
      System.out.println( "returning rectangle list = " + list );
    }
    return list;
  }

  /**
   * @param ePlanElement
   * @return the duration of the plan element
   */
  private static Amount<Duration> getDuration( EPlanElement ePlanElement ) {
    TemporalMember temporalMember = ePlanElement.getMember(TemporalMember.class); 
    Amount<Duration> duration = temporalMember.getDuration();
    if (duration == null) {
      duration = Amount.valueOf(0L, SI.SECOND);
    }
    return duration;
  }
  
	/**
	 * Create a rectangle from a Date range.
	 * @param extent the Date range
	 * @return the rectangle
	 */
	protected Rectangle toRectangle(TemporalExtent extent) {
		Rectangle bounds = new Rectangle(0,0,0,0);
		if (extent != null) {
			Date startTime = extent.getStart();

			long durationMillis = extent.getDurationMillis();
			Page page = timelineViewer.getPage();
			
	    Date screenStartDate = timelineViewer.getTimeline().getCurrentScreenEarliestDate();
      bounds.x = (int) page.convertToPixels(startTime.getTime() - screenStartDate.getTime());

			bounds.y = 0;
			long convertToPixels = page.convertToPixels(durationMillis);
			bounds.width = (int) Math.max(2, convertToPixels);
			bounds.height = feedbackLayerData.getBounds().height;//timelineViewer.getTimelineEditPart().getFigure().getBounds().height;
		}
		return bounds.getCopy();
	}
	
	/**
	 * Add the rectangles to the figure to be displayed.
	 * @param collection
	 */
	protected void addFeedback(Collection<InvalidRangeRectangleFigure> collection) {
		if (layerManager != null) {
			for(Rectangle rect : map.keySet()) {
				IFigure invalidRangeRectangleFigure = map.get(rect);
		    if ( debugP ) {
		      System.err.println("feedbackLayerData.add(" + invalidRangeRectangleFigure + ")");
		    }
				feedbackLayerData.add(invalidRangeRectangleFigure);
				feedbackLayerData.setConstraint(invalidRangeRectangleFigure, rect);
			}
	    timelineViewer.flush();
		}
	}
  /**
   * Remove the rectangles from the display.
   * @param collection
   */
	protected void removeFeedback(Collection<InvalidRangeRectangleFigure> collection) {
		if (layerManager != null) {
			for(IFigure figure : collection) {
				if(feedbackLayerData.getChildren().contains(figure)) {
					feedbackLayerData.remove(figure);
				}
			}
	    timelineViewer.flush();
		}
	}

  @Override
  public void dragDetected( DragDetectEvent e ) {
    isDragging = true;
    mouseUp = false;
  }

  @Override
  public void mouseDoubleClick( MouseEvent e ) {
    mouseUp = true;
    removeFeedbackRanges();
  }

  @Override
  public void mouseDown( MouseEvent e ) {
    mouseUp = false;
    if ( !timelineViewer.getSelectedEditParts().isEmpty() ) {
      Request request = new Request(this);
      editPolicy.showTargetFeedback( request );
    }
  }

  @Override
  public void mouseUp( MouseEvent e ) {
    mouseUp = true;    
    removeFeedbackRanges();
  }	

  public boolean isDragDetected() {
    return isDragging;
  }
  public boolean isMouseUp() {
    return mouseUp;
  }
  public boolean isMouseDown() {
    return !mouseUp;
  }

  /**
   * Class for painting the rectangle "figure."
   *
   */
  private class InvalidRangeRectangleFigure extends Figure {
    /**
     * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
      Rectangle bounds = getBounds().getCopy();
      
      graphics.setBackgroundColor(ColorConstants.gray);
      graphics.setAlpha(70);
      graphics.fillRectangle(bounds);
      graphics.setAlpha(255);
    }
  }

}
