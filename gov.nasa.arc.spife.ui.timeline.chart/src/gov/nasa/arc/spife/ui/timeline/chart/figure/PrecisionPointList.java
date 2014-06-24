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
package gov.nasa.arc.spife.ui.timeline.chart.figure;

/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Class implementing a list of <code>PrecisionPoint</code> similarly to
 * <code>PointList</code> class.
 * 
 * @author aboyko
 *
 */
public class PrecisionPointList extends PointList {

	private double[] points = new double[0];
	private PrecisionRectangle bounds;
	private int size = 0;

	static final long serialVersionUID = 1;

	/** 
	 * Constructs an empty PrecisionPointList.
	 */
	public PrecisionPointList() { 
		//
	}

	/**
	 * Constructs a PointList with the given points.
	 * @param points double array where two consecutive double form the coordinates of a point
	 */
	public PrecisionPointList(double points[]) {
		this.points = points;
		this.size = points.length / 2;
	}

	/** 
	 * Constructs a PrecisionPointList with initial capacity <i>size</i>, but no points.
	 * 
	 * @param size  Number of points to hold.
	 */
	public PrecisionPointList(int size) {
		points = new double[size * 2];
	}
	
	public PrecisionPointList(PointList pointList) {
		this();
		addAll(pointList);
	}

	/**
	 * Appends all of the given points to this PrecisionPointList.
	 * @param source the source PrecisionPointlist
	 */
	public void addAll(PrecisionPointList source) {
		ensureCapacity(size + source.size);
		System.arraycopy(source.points, 0, points, size * 2, source.size * 2);
		size += source.size;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#addAll(org.eclipse.draw2d.geometry.PointList)
	 */
	@Override
	public void addAll(PointList source) {
		if (source instanceof PrecisionPointList) {
			addAll((PrecisionPointList)source);
		}
		ensureCapacity(size + source.size());
		for (int i = 0; i < source.size(); i++) {
			addPoint(source.getPoint(i));
		}
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#addPoint(org.eclipse.draw2d.geometry.Point)
	 */
	@Override
	public void addPoint(Point p) {
		if (p instanceof PrecisionPoint) {
			PrecisionPoint precisionPt = (PrecisionPoint)p;
			addPrecisionPoint(precisionPt.preciseX(), precisionPt.preciseY());
		} else {
			addPrecisionPoint(p.preciseX(), p.preciseY());
		}
	}
	
	/** 
	 * Adds the input point values to this PointList.
	 * @param x  X value of a point to add
	 * @param y  Y value of a point to add
	 */
	public void addPrecisionPoint(double x, double y) {
		bounds = null;
		int index = size * 2;
		ensureCapacity(size + 1);
		points[index] = x;
		points[index + 1] = y;
		size++;
	}

	private void ensureCapacity(int newSize) {
		newSize *= 2;
		if (points.length < newSize) {
			double old[] = points;
			points = new double[Math.max(newSize, size * 4)];
			System.arraycopy(old, 0, points, 0, size * 2);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#getBounds()
	 */
	@Override
	public Rectangle getBounds() {
		if (bounds != null)
			return bounds;
		bounds = new PrecisionRectangle();
		if (size > 0) {
			bounds.setLocation(getPoint(0));
			for (int i = 0; i < size; i++) {
				PrecisionPoint p = (PrecisionPoint)getPoint(i);
				bounds.union(p);
			}
		}
		return bounds;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#getCopy()
	 */
	@Override
	public PrecisionPointList getCopy() {
		PrecisionPointList result = new PrecisionPointList(size);
		System.arraycopy(points, 0, result.points, 0, size * 2);
		result.size = size;
		result.bounds = null;
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#getLastPoint()
	 */
	@Override
	public Point getLastPoint() {
		return getPoint(size - 1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#getMidpoint()
	 */
	@Override
	public Point getMidpoint() {
		if (size() % 2 == 0)
			return getPoint(size() / 2 - 1).
				getTranslated(getPoint(size() / 2)).
				scale(0.5f);
		return getPoint(size() / 2);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#getPoint(int)
	 */
	@Override
	public Point getPoint(int index) {
		if (index < 0 || index >= size)
		    throw new IndexOutOfBoundsException(
		    	"Index: " + index + //$NON-NLS-1$
		    	", Size: " + size); //$NON-NLS-1$
		index *= 2;
		return new PrecisionPoint(points[index], points[index + 1]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#getPoint(org.eclipse.draw2d.geometry.Point, int)
	 */
	@Override
	public Point getPoint(Point p, int index) {
		if (index < 0 || index >= size)
		    throw new IndexOutOfBoundsException(
		    	"Index: " + index + //$NON-NLS-1$
		    	", Size: " + size); //$NON-NLS-1$
		index *= 2;
		if (p instanceof PrecisionPoint) {
			PrecisionPoint preciseP = (PrecisionPoint) p;
			preciseP.setPreciseX(points[index]);
			preciseP.setPreciseY(points[index + 1]);
			// preciseP.updateInts(); done automatically from setPreciseX/Y
		} else {
			p.x = (int)Math.floor(points[index] + 0.000000001);
			p.y = (int)Math.floor(points[index + 1] + 0.000000001);
		}
		return p;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#insertPoint(org.eclipse.draw2d.geometry.Point, int)
	 */
	@Override
	public void insertPoint(Point p, int index) {
		if (bounds != null && !bounds.contains(p))
			bounds = null;
		if (index > size || index < 0)
		    throw new IndexOutOfBoundsException(
		    	"Index: " + index + //$NON-NLS-1$
		    	", Size: " + size); //$NON-NLS-1$
		index *= 2;

		int length = points.length;
		double old[] = points;
		points = new double[length + 2];
		System.arraycopy(old, 0, points, 0, index);
		System.arraycopy(old, index, points, index + 2, length - index);
		
		if (p instanceof PrecisionPoint) {
			PrecisionPoint precisionPt = (PrecisionPoint)p;
			points[index] = precisionPt.preciseX();
			points[index + 1] = precisionPt.preciseY();
		} else {
			points[index] = p.x;
			points[index + 1] = p.y;
		}
		size++;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#performScale(double)
	 */
	@Override
	public void performScale(double factor) {
		for (int i = 0; i < points.length; i++)
			points[i] = points[i] * factor;
		bounds = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#performTranslate(int, int)
	 */
	@Override
	public void performTranslate(int dx, int dy) {
		for (int i = 0; i < size * 2; i += 2) {
			points[i] += dx;
			points[i + 1] += dy;
		}
		if (bounds != null)
			bounds.translate(dx, dy);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#removeAllPoints()
	 */
	@Override
	public void removeAllPoints() {
		bounds = null;
		size = 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#removePoint(int)
	 */
	@Override
	public Point removePoint(int index) {
		bounds = null;
		if (index < 0 || index >= size)
		    throw new IndexOutOfBoundsException(
		    	"Index: " + index + //$NON-NLS-1$
		    	", Size: " + size); //$NON-NLS-1$
			
		index *= 2;
		PrecisionPoint pt = new PrecisionPoint(points[index], points[index + 1]);
		if (index != size * 2 - 2)
			System.arraycopy(points, index + 2, points, index, size * 2 - index - 2);
		size--;
		return pt;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#reverse()
	 */
	@Override
	public void reverse() {
		double temp;
		for (int i = 0, j = size * 2 - 2; i < size; i += 2 , j -= 2) {
			temp = points[i];
			points[i] = points[j];
			points[j] = temp;
			temp = points[i + 1];
			points[i + 1] = points[j + 1];
			points[j + 1] = temp;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#setPoint(org.eclipse.draw2d.geometry.Point, int)
	 */
	@Override
	public void setPoint(Point pt, int index) {
		if (index < 0 || index >= size)
		    throw new IndexOutOfBoundsException(
		    	"Index: " + index + //$NON-NLS-1$
		    	", Size: " + size); //$NON-NLS-1$
		if (bounds != null && !bounds.contains(pt))
			bounds = null;
		if (pt instanceof PrecisionPoint) {
			PrecisionPoint precisionPt = (PrecisionPoint)pt;
			points[index * 2] = precisionPt.preciseX();
			points[index * 2 + 1] = precisionPt.preciseY();
		} else {
			points[index * 2] = pt.x;
			points[index * 2 + 1] = pt.y;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#setSize(int)
	 */
	@Override
	public void setSize(int newSize) {
		if (points.length > newSize * 2) {
			size = newSize;
			return;
		}
		double[] newArray = new double[newSize * 2];
		System.arraycopy(points, 0, newArray, 0, points.length);
		points = newArray;
		size = newSize;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#size()
	 */
	@Override
	public int size() {
		return size;
	}

	/** 
	 * Returns the contents of this PrecisionPointList as a double array.  The returned array is by
	 * reference.  Any changes made to the array will also be changing the original PrecisionPointList.
	 * 
	 * @return the integer array of points by reference
	 */
	public double[] toDoubleArray() {
		if (points.length != size * 2) {
			double[] old = points;
			points = new double[size * 2];
			System.arraycopy(old, 0, points, 0, size * 2);
		}
		return points;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#toIntArray()
	 */
	@Override
	public int[] toIntArray() {
		int [] intArray = new int[size * 2];
		for (int i = 0; i < size(); i++) {
			Point p = getPoint(i);
			int idx = 2*i;
			intArray[idx] = p.x;
			intArray[idx + 1] = p.y;
		}
		return intArray;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#translate(int, int)
	 */
	@Override
	public void translate(int x, int y) {
		if (x == 0 && y == 0)
			return;
		if (bounds != null)
			bounds.translate(x, y);
		for (int i = 0; i < size * 2; i += 2) {
			points[i] += x;
			points[i + 1] += y;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.geometry.PointList#transpose()
	 */
	@Override
	public void transpose() {
		double temp;
		if (bounds != null)
			bounds.transpose();
		for (int i = 0; i < size * 2; i += 2) {
			temp = points[i];
			points[i] = points[i + 1];
			points[i + 1] = temp;
		}
	}
	
}
