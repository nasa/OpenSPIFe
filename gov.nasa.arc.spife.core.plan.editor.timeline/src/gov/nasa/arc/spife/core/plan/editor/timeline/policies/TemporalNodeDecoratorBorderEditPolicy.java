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

import gov.nasa.arc.spife.core.plan.editor.timeline.TimelinePlugin;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.util.ITimelineBorderDecoratorColorProvider;
import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;

public class TemporalNodeDecoratorBorderEditPolicy extends PlanTimelineViewerEditPolicy {
	
	public static final ITimelineBorderDecoratorColorProvider COLOR_PROVIDER = ClassRegistry.createInstance(ITimelineBorderDecoratorColorProvider.class);
	public static final String ROLE = TemporalNodeDecoratorBorderEditPolicy.class.getName();

	// listen to the model
	private Listener listener = new Listener();
	
	private final boolean ALWAYS_SHOW_TIME_CRITICAL = true;
	
	// should borders for violations be show on the timeline?
	private boolean showViolations = Activator.getDefault().getPreferenceStore().getBoolean(TimelineConstants.VIOLATIONS_VISIBLE);

	// show borders for plan constraints be shown on the timeline?
	private boolean showPlanConstraints = TimelinePlugin.getDefault().getPreferenceStore().getBoolean(PLAN_CONSTRAINTS_VISIBLE);

	// colors to be used for the different type of borders
	private static final Color violatedColor = ColorConstants.red;
	private static final Color constraintColor = ColorConstants.blue;
	private static final Color ostpvColor = ColorConstants.midBlue;

	// figure which actually represents the border
	private RectangleFigure borderFigure;

	private static final int BORDER_THICKNESS = 2;

	private boolean isViolated = false;
	private boolean isConstrained = false;
	private boolean isOstpvUpdateRequired = false;

	public TemporalNodeDecoratorBorderEditPolicy() {
		super();	
	}

	public void initBorderFigure() {
		EditPart host = getHost();
		if (borderFigure == null) {
			borderFigure = new RectangleFigure();
			borderFigure.setVisible(false);
			borderFigure.setLineWidth(BORDER_THICKNESS);
			if(COLOR_PROVIDER != null) {
				borderFigure.setForegroundColor(COLOR_PROVIDER.getForeground(host));
			}
			borderFigure.setFill(false);
		}
	}

	@Override
	public void activate() {
		super.activate();
		initBorderFigure();
		
		// add the model change listener
		getTemporalMember().eAdapters().add(listener);
		EPlanElement pe = ((TemporalNodeEditPart)getHost()).getModel();
		EObject data = pe.getData();
		if (data != null) {
			data.eAdapters().add(listener);
		}
		ConstraintsMember member = getConstraintsMember();
		if (member != null) {
			member.eAdapters().add(listener);
		}
		getHostFigure().addAncestorListener(listener);
		// add the other listeners
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);		
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(listener);
		synchronizeStates();
		updateBorderToReflectCurrentState();
	}

	/**
	 * update the value of the isViolated instance variable
	 */
	private void synchronizeViolatedState() {
		isViolated = false;
		if (showViolations) {
			if (EPlanUtils.getSeverity(getEPlanElement(), false) == IMarker.SEVERITY_ERROR) {
				isViolated = true;
			}
		}
	}

	/**
	 * for now this method only checks if the timeCritical attribute is set
	 */
	private void synchronizeConstrainedState() {
		isConstrained = false;
		if(shouldAlwaysShowTimeCritical() || showPlanConstraints) {
			EPlanElement ePlanElement = getEPlanElement();
			EObject data = ePlanElement.getData();
			if(data != null) {
				EClass eClass = data.eClass();
				Object object = eClass.getEStructuralFeature("timeCritical");
				if(object instanceof EStructuralFeature) {
					EStructuralFeature feature = (EStructuralFeature)object;
					String objectString = String.valueOf(data.eGet(feature));
					isConstrained = objectString != null && Boolean.valueOf(objectString);
				}
			}	
		}
	}

	/**
	 * The checkbox for OSTPV Update Required is check marked.
	 */
	private void synchronizeOstpvUpdateRequiredState() {
		isOstpvUpdateRequired = false;
		EPlanElement ePlanElement = getEPlanElement();
		EObject data = ePlanElement.getData();
		if(data != null) {
			EClass eClass = data.eClass();
			Object ostpvStatusObject = eClass.getEStructuralFeature("ostpvStatus");
			if(ostpvStatusObject instanceof EStructuralFeature) {
				EStructuralFeature feature = (EStructuralFeature)ostpvStatusObject;
				String objectString = String.valueOf(data.eGet(feature));
				isOstpvUpdateRequired = objectString != null && Boolean.valueOf(objectString);
			}
		}
	}
	
	private boolean shouldAlwaysShowTimeCritical() {
		return ALWAYS_SHOW_TIME_CRITICAL;
	}

	private void synchronizeStates() {
		synchronizeViolatedState();
		synchronizeConstrainedState();
		synchronizeOstpvUpdateRequiredState();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		EPlanElement pe = ((TemporalNodeEditPart)getHost()).getModel();
		EObject data = pe.getData();
		if (data != null) {
			data.eAdapters().remove(listener);
		}
		getTemporalMember().eAdapters().remove(listener);
		ConstraintsMember member = getConstraintsMember();
		if (member != null) {
			member.eAdapters().remove(listener);
		}
		getHostFigure().removeAncestorListener(listener);
		if (borderFigure != null && borderFigure.getParent() != null) {
			IFigure figure = borderFigure.getParent();
			figure.remove(borderFigure);
		}
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(listener);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
	}

	/**
	 * Returns the EPlanElement that this color edit policy manages
	 * @return
	 */
	private EPlanElement getEPlanElement() {
		return ((TemporalNodeEditPart)getHost()).getModel();
	}	

	/**
	 * Get the backing resource for the host of this node.
	 * @return the IResource that contains the host of this node.
	 */
	private IResource getResource() {
		IResource resource = null;
		EPlanElement ePlanElement = getEPlanElement();
		EPlan plan = EPlanUtils.getPlan(ePlanElement);
		if(plan != null) {
			Object adapter = plan.getAdapter(IResource.class);
			if(adapter instanceof IResource) {
				resource = (IResource) adapter;
			}
		} else {
			LogUtil.warnOnce("can't get IResource for planElement.");
		}
		return resource;
	}

	private ConstraintsMember getConstraintsMember() {
		return getEPlanElement().getMember(ConstraintsMember.class, false);
	}	

	private TemporalMember getTemporalMember() {
		return getEPlanElement().getMember(TemporalMember.class);
	}

	/**
	 * This method is going to be called a lot, so it needs to be efficient.
	 */
	@Override
	public void showTargetFeedback(Request request) {
		super.showTargetFeedback(request);
		// the border may need to be repainted on most requests
		updateBorderToReflectCurrentState();
	}

	/**
	 * Inspect the instance variables isConstrained and isViolated. Being violated takes precedence over
	 * being constrained, so a violated color border should appear if isViolated is true. If isViolated is false, and
	 * isConstrained is true, the border color should be changed to constrained color. If neither is true, the border should
	 * be hidden.
	 */
	private void updateBorderToReflectCurrentState() {
		GEFUtils.runLaterInDisplayThread(getHost(), new Runnable() {
			@Override
			public void run() {
				if(!isViolated && !isConstrained && !isOstpvUpdateRequired) {
					borderFigure.setVisible(false);
					return;
				}
				
				// setting the color has no performance effect if the new color is the same as the old color.
				if(isViolated) {
					borderFigure.setForegroundColor(violatedColor);
				} else if(isConstrained) {
					borderFigure.setForegroundColor(constraintColor);
				}
				else if(isOstpvUpdateRequired) {
					borderFigure.setForegroundColor(ostpvColor);
				}
				
				// first update the border bounds, as this may be necessary
				updateBorderBounds();
				
				// make sure the border is a child of the figure
				if (borderFigure.getParent() == null) {
					getLayer(LAYER_FEEDBACK_DATA).add(borderFigure);
				}
				
				if (!borderFigure.isVisible()) {
					borderFigure.setVisible(true);
				}
				borderFigure.repaint();
			}
		});
	}

	private void updateBorderBounds() {
		IFigure hostFigure = getHostFigure();
		Rectangle bounds = hostFigure.getBounds();
		bounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height-1);
		borderFigure.setBounds(bounds);
	}

	private class Listener extends AdapterImpl implements IPropertyChangeListener, IResourceChangeListener, AncestorListener {

		/**
		 * Update the border only for notifications that are related to constraints or time
		 */
		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			if (feature instanceof EStructuralFeature) { // umm....
				synchronizeStates();
				updateBorderToReflectCurrentState();
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			boolean borderUpdateNeeded = false;
			if (VIOLATIONS_VISIBLE.equals(property)) {
				boolean newShowViolations = (Boolean) event.getNewValue();
				if (showViolations != newShowViolations) {
					showViolations = newShowViolations;
					borderUpdateNeeded = true;
				}
			} else if (shouldAlwaysShowTimeCritical() && PLAN_CONSTRAINTS_VISIBLE.equals(property)) {
				boolean newConstraintsVisible = (Boolean) event.getNewValue();
				if (showPlanConstraints != newConstraintsVisible) {
					showPlanConstraints = newConstraintsVisible;
					borderUpdateNeeded = true;
				}
			}
			if (borderUpdateNeeded) {
				synchronizeStates();
				updateBorderToReflectCurrentState();
			}
		}

		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				try {
					IResource resource = event.getResource();
					IResource backingResource = getResource();
					if(backingResource == null) {
						LogUtil.warnOnce("can't respond to resource change event, the backing resource is null.");
					} else {
						if(resource == null) {
							resource = backingResource; // assume the default resource
						}
						if(backingResource.equals(resource)) {
							synchronizeStates();
							updateBorderToReflectCurrentState();
						}
					}
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}

		@Override public void ancestorAdded(IFigure ancestor) { /* no implementation */ }
		@Override public void ancestorRemoved(IFigure ancestor) { /* no implementation */ }

		@Override
		public void ancestorMoved(IFigure ancestor) {
			updateBorderBounds();
		}

	}
	
}	
