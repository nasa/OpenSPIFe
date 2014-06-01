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
package gov.nasa.arc.spife.ui.timeline.figure;

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.FontUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

public class RowDataFigureLayout extends AbstractLayout implements LayoutManager {

	/** The layout constraints */
	private final SortedSet<NodeEdgeWrapper> edges = new TreeSet<NodeEdgeWrapper>();
	private final Map<IFigure, Object> constraints = new HashMap<IFigure, Object>();
	private final Map<IFigure, List<NodeEdgeWrapper>> figureToEdges = new HashMap<IFigure, List<NodeEdgeWrapper>>();
	private final Map<IFigure, Rectangle> figureToBounds = new HashMap<IFigure, Rectangle>();
	private final Map<IFigure, Object> figureToOldConstraint = new HashMap<IFigure, Object>();
	private float overlapFactor = 1f;
	private boolean lockVerticalPosition = false;
	private int rowElementHeight = TimelineUtils.getRowElementHeight();

	private final Page page;
	
	public RowDataFigureLayout(Page page) {
		this.page = page;
	}

	public void setOverlapFactor(float overlapFactor) {
		this.overlapFactor = overlapFactor;
		invalidate();
	}

	public boolean isLockVerticalPosition() {
		return lockVerticalPosition;
	}

	public void setLockVerticalPosition(boolean lockVerticalPosition) {
		this.lockVerticalPosition = lockVerticalPosition;
		invalidate();
	}

	@Override
	public Object getConstraint(IFigure child) {
		return constraints.get(child);
	}
	
	@Override
	public void setConstraint(IFigure child, Object constraint) {
		RowDataFigureLayoutData data = (RowDataFigureLayoutData) constraint;
		super.setConstraint(child, data);
		constraints.put(child, data);
	}

	@Override
	public void remove(IFigure child) {
		removeWrappers(child);
		super.remove(child);
	}

	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		updateFigureToBoundsCache(container);
		Dimension size = new Dimension(page.getWidth(), getRowElementHeight());
		for (Object o : container.getChildren()) {
			IFigure child = (IFigure) o;
			Rectangle childBounds = figureToBounds.get(child);
			if (childBounds != null) {
				size.height = Math.max(size.height, childBounds.y + childBounds.height);
			}
		}
		return size;
	}
	
	@Override
	public void layout(IFigure container) {
		updateFigureToBoundsCache(container);
		Rectangle bounds = container.getBounds();
		for (Object o : container.getChildren()) {
			IFigure child = (IFigure) o;
			Rectangle childBounds = figureToBounds.get(child);
			if (childBounds != null) {
				childBounds = childBounds.getCopy().translate(bounds.getLocation());
				child.setBounds(childBounds);
			}
		}
	}

	private void updateFigureToBoundsCache(IFigure container) {
		Map<IFigure, Rectangle> cache = new HashMap<IFigure, Rectangle>();
		if (isLockVerticalPosition()) {
			cache.putAll(figureToBounds);
		}
		figureToBounds.clear();
		
		for (Object o : container.getChildren()) {
			IFigure child = (IFigure) o;
			Rectangle childBounds = calculateChildBounds(child);
			if (childBounds != null) {
				figureToBounds.put(child, childBounds);
			}
		}
		if (isLockVerticalPosition()) {
			for (Object o : container.getChildren()) {
				IFigure child = (IFigure) o;
				Rectangle childBounds = figureToBounds.get(child);
				if (childBounds != null) {
					Rectangle old = cache.get(child);
					if (old != null) {
						childBounds.y = old.y;
					}
				}
			}
		} else {
			updateOffsets();
		}
	}

	private static int maxOverlap = 1024;
	private void updateOffsets() {
		int overlapFactor = 0;
		boolean[] occupiedOffsets = new boolean[maxOverlap]; // default value is false
		synchronized (edges) {
			for (NodeEdgeWrapper edge : edges) {
				IFigure figure = edge.getFigure();
				switch (edge.getSide()) {
					case LEFT: {
						int j = 0;
						while (j < occupiedOffsets.length && occupiedOffsets[j]) {
							j++;
							if (j > maxOverlap) {
								// hit the max, double and continue
								maxOverlap *= 2;
								boolean[] newOffsets = new boolean[maxOverlap];
								System.arraycopy(occupiedOffsets, 0, newOffsets, 0, occupiedOffsets.length);
								occupiedOffsets = newOffsets;
							}
						}
						
						if(j < occupiedOffsets.length) {
							occupiedOffsets[j] = true;
						}
						
						if (j > overlapFactor) {
							overlapFactor = j;
						}
						Rectangle bounds = figureToBounds.get(figure);
						if (bounds != null) {
							bounds.y = getTop(j);
						}
						break;
					}
					case RIGHT: {
						Rectangle bounds = figureToBounds.get(figure);
						if (bounds != null) {
							int offset = getOffset(bounds.y);
							occupiedOffsets[offset] = false;
						}
						break;
					}
				}
			}
		}
	}

	private Rectangle calculateChildBounds(IFigure figure) {
		Rectangle oldBounds = figure.getBounds();
		RowDataFigureLayoutData constraint = (RowDataFigureLayoutData) getConstraint(figure);
		if (constraint == null) {
			return null;
		}
		Object oldConstraint = figureToOldConstraint.get(figure);
		if (constraint.equals(oldConstraint)) {
			return oldBounds;
		}
		figureToOldConstraint.put(figure, oldConstraint);

		// On extent, determine the graphical positions
		// and if instantaneous, get the minimum size and
		// center
		//
		if (constraint.start != null && constraint.end != null) {
			Date left = constraint.start;
			Date right = constraint.end;
			int x = getX(left);
			int width = Math.max(getX(right) - x, 1);
			int height = getRowElementHeight();
			
			Dimension min = figure.getMinimumSize();
			if (constraint.instantaneous) {
				// SPF-9741 For instantaneous figures we do not want to change its height,
				// we will just use what is set in the timeline's section 'Row Height' detail.
				width = Math.max(width, min.width);
				x -= width / 2;
			}
			
			removeWrappers(figure);
			createWrappers(figure, x, x + width);
			return new Rectangle(x, 0, width, height);
		}
		// On date, assume the figure will determine it's own
		// size and center
		//
		else if (constraint.start != null) {
			Font font = figure.getFont();
			if(font == null) {
				LogUtil.error("null font for figure");
				figure.setFont(FontUtils.getStyledFont(SWT.DEFAULT));
			}			
			Date center = constraint.start;			
			
			Dimension size = figure.getPreferredSize();
			int width = size.width;
			int left = getX(center) - width / 2;
			return new Rectangle(left, (getRowElementHeight() - size.height) / 2, width, size.height);			
		} else {
			return null;
		}
	}
	
	private void removeWrappers(IFigure child) {
		List<NodeEdgeWrapper> list = figureToEdges.get(child);
		if (list != null) {
			figureToEdges.remove(child);
			edges.removeAll(list);
		}
	}

	private synchronized void createWrappers(IFigure child, long left, long right) {
		NodeEdgeWrapper leftEdge = new NodeEdgeWrapper(child, left, FigureSide.LEFT);
		NodeEdgeWrapper rightEdge = new NodeEdgeWrapper(child, right, FigureSide.RIGHT);
		synchronized (edges) {
			edges.add(leftEdge);
			edges.add(rightEdge);
			figureToEdges.put(child, Arrays.asList(leftEdge, rightEdge));
		}
	}

	private Page getPage() {
		return page;
	}
	
	private int getX(Date time) {
		Page page = getPage();
		return page.getScreenPosition(time);
	}
	
	private int getTop(int offset) {
		return offset * Math.round(getRowElementHeight() * overlapFactor);
	}
	
	public int getRowElementHeight() {
		return rowElementHeight;
	}
	
	public void setRowElementHeight(int rowElementHeight) {
		this.rowElementHeight = rowElementHeight;
		invalidate();
	}
	
	private int getOffset(int top) {
		return top / Math.max(1, Math.round(getRowElementHeight() * overlapFactor)); // Prevent divide by zero
	}

	private static enum FigureSide {
		LEFT,
		RIGHT,
		;
	}
	
	private static final class NodeEdgeWrapper implements Comparable<NodeEdgeWrapper> {
		
		private final IFigure figure;
		private final long x;
		private final FigureSide side;
		
		public NodeEdgeWrapper(IFigure figure, long x, FigureSide side) {
			this.figure = figure;
			this.x = x;
			this.side = side;
		}
		
		public IFigure getFigure() {
			return figure;
		}
		
		public FigureSide getSide() {
			return side;
		}
		
		@Override
		public final int compareTo(NodeEdgeWrapper o) {
			int result = CommonUtils.compare(this.x, o.x);
			if (result != 0) {
				return result;
			}
			if (this.side != o.side) {
				switch (this.side) {
				case RIGHT: return -1;
				case LEFT:  return 1;
				default: // shouldn't get here
				}
			}
			result = CommonUtils.compare(this.figure.hashCode(), o.figure.hashCode());
			if (result != 0) {
				return result;
			}
			return CommonUtils.compare(this.hashCode(), o.hashCode());
		}
		
	}
	
}
