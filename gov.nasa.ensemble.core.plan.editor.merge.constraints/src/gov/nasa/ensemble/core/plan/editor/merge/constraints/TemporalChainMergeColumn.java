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
package gov.nasa.ensemble.core.plan.editor.merge.constraints;

import gov.nasa.ensemble.common.ui.treetable.IAdvancedDrawingColumn;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;

import java.util.Comparator;
import java.util.List;

import gov.nasa.ensemble.common.ui.color.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;

public class TemporalChainMergeColumn extends AbstractMergeColumn<ConstraintsMember> implements IAdvancedDrawingColumn<ConstraintsMember> {

	private static Image TOP_CHAIN_IMAGE = ConstraintsMergePlugin.createIcon("chain2_top.gif");
	private static Image MIDDLE_CHAIN_IMAGE = ConstraintsMergePlugin.createIcon("chain2_middle.gif");
	private static Image BOTTOM_CHAIN_IMAGE = ConstraintsMergePlugin.createIcon("chain2_bottom.gif");
//	private static Image HEADER_IMAGE = gov.nasa.ensemble.core.plan.editor.constraints.Activator.createIcon("ChainIcon_3.gif");
	
	public TemporalChainMergeColumn(IMergeColumnProvider provider) {
		super(provider, "Chains", 40);
//		setHeaderImage(HEADER_IMAGE);
	}
	
	@Override
	public ConstraintsMember getFacet(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement planElement = (EPlanElement) element;
			return planElement.getMember(ConstraintsMember.class, false);
		}
		return null; 
	}

	@Override
	public boolean needsUpdate(Object feature) {
		return (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN);
	}

	@Override
	public int getAlignment() {
	    return SWT.LEFT;
	}
	
	/**
	 * This method is implemented in this way to provoke
	 * a redraw.  Although the visual feedback is supplied 
	 * in PaintItem, this doesn't cause a redraw in eclipse.
	 * We need to provide different text for each image so
	 * that redraws will occur properly when the image changes. 
	 */
	@Override
	public String getText(ConstraintsMember facet) {
		if (facet != null) {
			TemporalChain chain = facet.getChain();
			if (chain != null) {
				List<EPlanElement> elements = chain.getElements();
				int index = elements.indexOf(facet.getPlanElement());
				if (index == 0) {
					return " ";
				} else if (index == elements.size() - 1) {
					return "  ";
				} else {
					return "   ";
				}
			}
		}
		return "";
	}
	
	@Override
	public boolean handleMeasureItem(ConstraintsMember facet, Event event) {
		if (facet != null) {
			TemporalChain chain = facet.getChain();
			if (chain != null) {
				List<EPlanElement> elements = chain.getElements();
				int index = elements.indexOf(facet.getPlanElement());
				if (index == 0) {
					event.width = TOP_CHAIN_IMAGE.getBounds().width;
					event.height = TOP_CHAIN_IMAGE.getBounds().height;
				} else if (index == elements.size() - 1) {
					event.width = BOTTOM_CHAIN_IMAGE.getBounds().width;
					event.height = BOTTOM_CHAIN_IMAGE.getBounds().height;
				} else {
					event.width = MIDDLE_CHAIN_IMAGE.getBounds().width;
					event.height = MIDDLE_CHAIN_IMAGE.getBounds().height;
				}
				if (facet.getPlanElement() instanceof EActivity) {
					event.width *= 2 + 3;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean handleEraseItem(ConstraintsMember facet, Event event) {
		// use default behavior
		return false;
	}
	
	@Override
	public boolean handlePaintItem(ConstraintsMember facet, Event event) {
		if (facet != null) {
			TemporalChain chain = facet.getChain();
			if (chain != null) {
				GC gc = event.gc;
				Color oldForeground = gc.getForeground();
				Color oldBackground = gc.getBackground();
				gc.setForeground(ColorConstants.red);
				gc.setBackground(ColorConstants.white);
				List<EPlanElement> elements = chain.getElements();
				int index = elements.indexOf(facet.getPlanElement());
				int left = event.x;
				int top = event.y;
				boolean indent = (facet.getPlanElement() instanceof EActivity);
				if (index == 0) {
					drawImage(gc, TOP_CHAIN_IMAGE, left, top, indent);
				} else if (index == elements.size() - 1) {
					drawImage(gc, BOTTOM_CHAIN_IMAGE, left, top, indent);
				} else {
					drawImage(gc, MIDDLE_CHAIN_IMAGE, left, top, indent);
				}
				gc.setForeground(oldForeground);
				gc.setBackground(oldBackground);
				return true;
			}
		}
		return false;
	}

	@Override
	public Comparator<ConstraintsMember> getComparator() {
		return null; // SPF-1588: not sortable
	}
	
	private void drawImage(GC gc, Image image, int left, int top, boolean indent) {
		if (indent) {
			left += image.getBounds().width;
		}
		gc.drawImage(image, left, top);
	}
	
}
