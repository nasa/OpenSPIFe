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

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.figure.LabeledPolylineConnection;
import gov.nasa.arc.spife.ui.timeline.model.Link;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Collection;
import java.util.Map;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.graphics.Image;

public class DefaultConnectionEditPart extends AbstractConnectionEditPart implements TimelineConstants {

	private Label label = null;
	protected static final int FLAG_FIGURE_ACTIVE = 3;
	
	public DefaultConnectionEditPart() {
		// default constructor
	}
	
	public DefaultConnectionEditPart(Image image) {
		this.label = new Label(image);
	}
	
	public DefaultConnectionEditPart(Label label) {
		this.label = label;
	}

	@Override
	protected void activateFigure() {
		IFigure layer = getLayer(LAYER_FEEDBACK_DATA);
		IFigure figure = getFigure();
		
		if(!layer.getChildren().contains(figure)) {
			layer.add(figure);
		}
		
		this.setFlag(FLAG_FIGURE_ACTIVE, true);
	}

	@Override
	public void activate() {
		super.activate();
		activateFigure();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		deactivateFigure();
	}

	@Override
	protected void deactivateFigure() {
		if(this.figureIsActive()) {
			getLayer(LAYER_FEEDBACK_DATA).remove(getFigure());
			getConnectionFigure().setSourceAnchor(null);
			getConnectionFigure().setTargetAnchor(null);
			this.setFlag(FLAG_FIGURE_ACTIVE, false);
		}
	}
	
	@Override
	protected IFigure createFigure() {
		return new LabeledPolylineConnection(label);
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
	}

	public boolean figureIsActive() {
		return this.getFlag(FLAG_FIGURE_ACTIVE);
	}
	
	@Override
	protected ConnectionAnchor getTargetConnectionAnchor() {
		EditPart calculatedTarget = getTarget();
		if(calculatedTarget == null) {
			Object model = this.getModel();
			if(model instanceof Link) {
				Link link = (Link)model;
				Object target = link.getTarget();
				Map<?, ?> editPartRegistry = this.getViewer().getEditPartRegistry();
				if(editPartRegistry == null) {
					LogUtil.warn("edit part registry is null, can't lookup edit part. try refreshing constraints.");
				}

				else {
					Object object = editPartRegistry.get(target);
					if(object instanceof GraphicalEditPart) {
						calculatedTarget = (EditPart) object;
					}

					else if(object instanceof Collection<?>) {
						Collection<?> collection = (Collection<?>)object;
						for(Object collectionObject : collection) {
							if(collectionObject instanceof GraphicalEditPart) {
								calculatedTarget = (EditPart) collectionObject;
							}
						}
					}

					// make the target the same as the source, at least we get an icon
					else if(object == null) {
						calculatedTarget = getSource();
					}
				}
			}
		}

		if (calculatedTarget != null) {
			if (calculatedTarget instanceof NodeEditPart) {
				NodeEditPart editPart = (NodeEditPart) calculatedTarget;
				return editPart.getTargetConnectionAnchor(this);
			}
			GraphicalEditPart target = (GraphicalEditPart)getTarget();
			if (target != null) {
				IFigure f = target.getFigure();
				return new ChopboxAnchor(f);
			}
		}
		return null;
	}

	@Override
	protected ConnectionAnchor getSourceConnectionAnchor() {
		EditPart calculatedSource = getSource();
		if(calculatedSource == null) {
			Object model = this.getModel();
			if(model instanceof Link) {
				Link link = (Link)model;
				Object source = link.getSource();
				Map<?, ?> editPartRegistry = this.getViewer().getEditPartRegistry();
				if(editPartRegistry == null) {
					LogUtil.warn("edit part registry is null, can't lookup edit part. try refreshing constraints.");
				}

				else {
					Object object = editPartRegistry.get(source);
					if(object instanceof GraphicalEditPart) {
						calculatedSource = (EditPart) object;
					}

					else if(object instanceof Collection<?>) {
						Collection<?> collection = (Collection<?>)object;
						for(Object collectionObject : collection) {
							if(collectionObject instanceof GraphicalEditPart) {
								calculatedSource = (EditPart) collectionObject;
							}
						}
					}

					// make the target the same as the source, at least we get an icon
					else if(object == null) {
						calculatedSource = getTarget();
					}
				}
			}
		}

		if (calculatedSource != null) {
			if (calculatedSource instanceof NodeEditPart) {
				NodeEditPart editPart = (NodeEditPart) calculatedSource;
				return editPart.getSourceConnectionAnchor(this);
			}
			GraphicalEditPart graphicalEditPart = (GraphicalEditPart)getSource();
			if (graphicalEditPart != null) {
				IFigure f = graphicalEditPart.getFigure();
				return new ChopboxAnchor(f);
			}
		}

		return null;
	}
	
}
