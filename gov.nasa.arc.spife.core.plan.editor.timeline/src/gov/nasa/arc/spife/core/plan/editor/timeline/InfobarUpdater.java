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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.core.plan.editor.timeline.models.BackgroundEventData;
import gov.nasa.arc.spife.ui.timeline.InfobarComposite;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * Mini diagrams legend:
 * '#' is the highlighted area
 * '^' is a possible cursor position for this case 
 * 
 * @author Andrew Bachmann
 */

public class InfobarUpdater {

	/**
	 *  Show smallest time window information.
	 *  This variable is to be set for user testing.
	 *  See Mike McCurdy or Andrew Bachmann for more explanation.
	 */
	private static final boolean DEFAULT_DISPLAY_SMALL_TIME_WINDOWS = false;
	
	private boolean displaySmallTimeWindows = DEFAULT_DISPLAY_SMALL_TIME_WINDOWS;
	
	private final InfobarComposite infobar;
	private final PostCommitListener temporalExtentListener;
	private final ILabelProviderListener labelProviderListener;
	
	private IInfobarContributor contributor = null;

	private BackgroundEventData data;
	private TransactionalEditingDomain domain;
    private ILabelProvider leftLabelProvider;
    private ILabelProvider rightLabelProvider;
    private ILabelProvider node1LabelProvider;
    private ILabelProvider node2LabelProvider;

	public InfobarUpdater(InfobarComposite infobar) {
		this.infobar = infobar;
		this.temporalExtentListener = new TemporalExtentListener();
		this.labelProviderListener = new InfobarLabelProviderListener();
		this.infobar.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
			    detachListeners();
			}
		});
	}
	
	public void updateBackgroundData(BackgroundEventData data) {
		if (contributor == null) {
			contributor = data.getContributor();
		} else if (contributor != data.getContributor()) {
			contributor.controlLost();
			contributor = data.getContributor();
		}
		if (domain != null) {
			detachListeners();
		}
		EPlanElement leftNode = data.getLeftNode();
		EPlanElement rightNode = data.getRightNode();
		EPlanElement representative = (leftNode != null ? leftNode : rightNode);
		if (representative != null) {
			this.data = data;
			attachListeners(data, leftNode, rightNode, representative);
		}
		displayContents(data.getNodes(), leftNode, data.getLeftTimepoint(), 
						rightNode, data.getRightTimepoint(), data.getSelectionStartTime());
	}

	private void detachListeners() {
		if (domain != null) {
		    domain.removeResourceSetListener(temporalExtentListener);
		    domain = null;
		}
	    if (leftLabelProvider != null) {
	    	leftLabelProvider.removeListener(labelProviderListener);
	    	leftLabelProvider.dispose();
	    	leftLabelProvider = null;
	    }
	    if (rightLabelProvider != null) {
	    	rightLabelProvider.removeListener(labelProviderListener);
	    	rightLabelProvider.dispose();
	    	rightLabelProvider = null;
	    }
	    if (node1LabelProvider != null) {
	    	node1LabelProvider.removeListener(labelProviderListener);
	    	node1LabelProvider.dispose();
	    	node1LabelProvider = null;
	    }
	    if (node2LabelProvider != null) {
	    	node2LabelProvider.removeListener(labelProviderListener);
	    	node2LabelProvider.dispose();
	    	node2LabelProvider = null;
	    }
    }

	private void attachListeners(BackgroundEventData data, EPlanElement leftNode, EPlanElement rightNode, EPlanElement representative) {
	    domain = TransactionUtils.getDomain(representative);
	    if (domain != null) {
		    domain.addResourceSetListener(temporalExtentListener);
		    AdapterFactory factory = ((AdapterFactoryEditingDomain)domain).getAdapterFactory();
		    if (factory != null) {
		    	if (leftNode != null) {
		    		leftLabelProvider = (ILabelProvider)factory.adapt(leftNode, ILabelProvider.class);
		    		if (leftLabelProvider != null) {
		    			leftLabelProvider.addListener(labelProviderListener);
		    		}
		    	}
		    	if (rightNode != null) {
		    		rightLabelProvider = (ILabelProvider)factory.adapt(rightNode, ILabelProvider.class);
		    		if (rightLabelProvider != null) {
		    			rightLabelProvider.addListener(labelProviderListener);
		    		}
		    	}
		    	Iterator<EPlanElement> iterator = data.getNodes().iterator();
		    	if (iterator.hasNext()) {
		    		node1LabelProvider = (ILabelProvider)factory.adapt(iterator.next(), ILabelProvider.class);
		    		if (node1LabelProvider != null) {
		    			node1LabelProvider.addListener(labelProviderListener);
		    		}
		    		if (iterator.hasNext()) {
		    			node2LabelProvider = (ILabelProvider)factory.adapt(iterator.next(), ILabelProvider.class);
			    		if (node2LabelProvider != null) {
			    			node2LabelProvider.addListener(labelProviderListener);
			    		}
		    		}
		    	}
		    }
	    }
    }

	private void displayContents(Set<EPlanElement> nodes, EPlanElement leftNode, Timepoint leftTimepoint,
						EPlanElement rightNode, Timepoint rightTimepoint, Date selectionStart) {
		if ((leftNode == null) && (rightNode == null)) {
			// nothing to display
			return;
		}
		infobar.clear();
		if (leftNode == null) {
			displayStartOfPlan(rightNode, rightLabelProvider);
		} else if (rightNode == null) {
			displayEndOfPlan(leftNode, leftLabelProvider);
		} else if (leftNode == rightNode) {
			displayOne(leftNode, leftLabelProvider, selectionStart);
		} else if (nodes.isEmpty()) {
			displayDistance(leftNode, leftTimepoint, rightNode, rightTimepoint);
		} else if (nodes.size() == 1) {
			if (displaySmallTimeWindows) {
				displayDistance(leftNode, leftTimepoint, rightNode, rightTimepoint);
			} else {
				EPlanElement node = nodes.iterator().next();
				displayOne(node, node1LabelProvider, selectionStart);
			}
		} else if ((nodes.size() == 2) && displaySmallTimeWindows) {
			displayDistance(leftNode, leftTimepoint, rightNode, rightTimepoint);
		} else {
			Date left = getEndpointTime(leftNode, leftTimepoint);
			Date right = getEndpointTime(rightNode, rightTimepoint);
			displayMultipleOverlapping(nodes, left, right);
		}
		infobar.layout();
		infobar.redraw();
	}
	
	/**
	 * Start of Plan:
	 * <pre>
	 * ##^##
	 * #####+--------+
	 * ##^##|earliest|
	 * #####+--------+
	 * ##^##
	 * </pre>
	 * Gap at the start of plan:
	 * {Name/Description} = "No activities before"
	 * {Name/Details}     = Name  of earliest activity
	 * {Start}            = Start of earliest activity
	 * {End}              = End   of earliest activity
	 * {Type}             = "Plan Start"
	 * @param provider label provider
	 */
	
	private void displayStartOfPlan(EPlanElement element, ILabelProvider provider) {
		infobar.setNameDescription("No activities before ");
		infobar.setIcon2(provider.getImage(element));
		infobar.setNameDetails(provider.getText(element));
		infobar.setType("Plan Start");
		TemporalExtent extent = getTemporalExtent(element);
		infobar.setDates(extent.getStart(), extent.getEnd());
	}

	/**
	 * End of Plan:
	 * <pre>
	 *         #####
	 * +------+#####
	 * |latest|##^##
	 * +------+#####
	 *         #####
	 * </pre>
	 * Gap at the start/end of plan:
	 * {Name/Description} = "No activities after"
	 * {Name/Details}     = Name  of latest activity
	 * {Start}            = Start of latest activity
	 * {End}              = End   of latest activity
	 * {Type}             = "Plan End"
	 * @param provider label provider
	 */
	
	private void displayEndOfPlan(EPlanElement element, ILabelProvider provider) {
		infobar.setNameDescription("No activities after ");
		infobar.setIcon2(provider.getImage(element));
		infobar.setNameDetails(" " + provider.getText(element));
		infobar.setType("Plan End");
		TemporalExtent extent = getTemporalExtent(element);
		infobar.setDates(extent.getStart(), extent.getEnd());
	}

	/**
	 * One:
	 * <pre>
	 * #######
	 * ###^###
	 * #######
	 * +-----+
	 * |no^de|
	 * +-----+
	 * #######
	 * ###^###
	 * #######
	 * </pre>
	 * Single activity occurring, or within an activity
	 * 2. show priority of that activity
	 * 4. show type of that activity (PANCAM_MOSAIC)
	 * 5. show resources for that activity
	 * one activity
	 * {Name/Description} = Name of node
	 * {Name/Details}     = "start of " + name of right
	 * {Start}            = Start of node
	 * {End}              = End   of node
	 * @param provider label provider
	 */
	protected void displayOne(EPlanElement element, ILabelProvider provider, Date selectionStart) {
		infobar.setIcon1(provider.getImage(element));
		infobar.setNameDescription(provider.getText(element));
		infobar.setType(getType(element));
		// TODO: details
//		infobar.setNameDetails("{priority} {resources}");
		TemporalExtent extent = getTemporalExtent(element);
		infobar.setDates(extent.getStart(), extent.getEnd(), selectionStart);
	}
	
	/**
	 * Arbitrary distance display
	 * 1. show the icons for the nodes
	 * 2. show the name of the left and right endpoints (includes node names)
	 * 3. show time of the left endpoint, time of the right endpoint, and distance
	 * @param leftNode
	 * @param leftEndpoint
	 * @param rightNode
	 * @param rightEndpoint
	 */
	private void displayDistance(EPlanElement leftNode, Timepoint leftEndpoint, 
									EPlanElement rightNode, Timepoint rightEndpoint) {
		Date left = getEndpointTime(leftNode, leftEndpoint);
		Date right = getEndpointTime(rightNode, rightEndpoint);
		infobar.setIcon1(leftLabelProvider.getImage(leftNode));
		infobar.setNameDescription(getEndpointString(leftNode, leftEndpoint));
		infobar.setIcon2(rightLabelProvider.getImage(rightNode));
		infobar.setNameDetails("to " + getEndpointString(rightNode, rightEndpoint));
		infobar.setDates(left, right);
		if ((leftEndpoint == Timepoint.END) && (rightEndpoint == Timepoint.START)) {
			infobar.setType("Gap");
		} else if ((leftEndpoint == Timepoint.START) && (rightEndpoint == Timepoint.END)) {
			infobar.setType("Overlap");
		} else {
			infobar.setType("Distance");
		}
	}

	private String getEndpointString(EPlanElement node, Timepoint endpoint) {
		return (endpoint == Timepoint.START ? "start of " : "end of ") + node.getName(); 
	}
	
	/**
	 * Two+ overlapping activities
	 * 1. show count of overlapping activities
	 * 2. show start/end/duration of overlap
	 */
	private void displayMultipleOverlapping(Set<EPlanElement> nodes, Date left, Date right) {
		if (nodes.size() == 2) {
			Iterator<EPlanElement> iterator = nodes.iterator();
			EPlanElement node1 = iterator.next();
			EPlanElement node2 = iterator.next();
			infobar.setIcon1(node1LabelProvider.getImage(node1));
			infobar.setNameDescription(node1LabelProvider.getText(node1));
			infobar.setIcon1(node2LabelProvider.getImage(node2));
			infobar.setNameDetails("and " + node2LabelProvider.getText(node2) + " are overlapping");
		} else {
			infobar.setNameDescription(nodes.size() + " overlapping");
		}
		infobar.setDates(left, right);
	}
	
	private Date getEndpointTime(EPlanElement pe, Timepoint endpoint) {
		TemporalExtent extent = getTemporalExtent(pe);
		if (extent == null) {
			return null;
		}
		
		switch (endpoint) {
		case START:
			return extent.getStart();
		case END:
			return extent.getEnd();
		default:
			Logger.getLogger(InfobarUpdater.class).warn("unexpected endpoint: " + endpoint);
			return null;
		}
	}
	
	private String getType(EPlanElement pe) {
		if (pe instanceof EActivity) {
			return ((EActivity)pe).getType();
		} else if (pe instanceof EActivityGroup) {
			return "ActivityGroup";
		} else if (pe instanceof EPlan) {
			return pe.getName();
		}
		throw new IllegalArgumentException("PlanElement cannot be identified"); 
	}
	
	private TemporalExtent getTemporalExtent(EPlanElement pe) {
		return pe.getMember(TemporalMember.class).getExtent();
	}
	
	private final class TemporalExtentListener extends PostCommitListener {

	    @Override
	    public void resourceSetChanged(ResourceSetChangeEvent event) {
	        for (Notification notification : event.getNotifications()) {
	        	Object feature = notification.getFeature();
	        	if ((feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)
	        		|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME)
	        		|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION)) {
	        		TemporalMember notifier = (TemporalMember)notification.getNotifier();
	        		EPlanElement element = notifier.getPlanElement();
	        		if ((element == data.getLeftNode()) || (element == data.getRightNode())
	        			|| data.getNodes().contains(element)) {
	        			WidgetUtils.runInDisplayThread(infobar, new Runnable() {
	        				@Override
							public void run() {
	        					displayContents(data.getNodes(), data.getLeftNode(), data.getLeftTimepoint(), 
	        							data.getRightNode(), data.getRightTimepoint(), data.getSelectionStartTime());
	        				}
	        			});
	        		}
	        	}
	        }
	    }
    }
	
    private final class InfobarLabelProviderListener implements ILabelProviderListener {

	    @Override
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			WidgetUtils.runInDisplayThread(infobar, new Runnable() {
				@Override
				public void run() {
					displayContents(data.getNodes(), data.getLeftNode(), data.getLeftTimepoint(), 
							data.getRightNode(), data.getRightTimepoint(), data.getSelectionStartTime());
				}
			});
	    }
	    
    }

}
