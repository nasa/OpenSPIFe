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
package gov.nasa.ensemble.common.ui.slider;

import gov.nasa.ensemble.common.ui.color.ColorConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * A sol range slider selection widget that has two draggable sliding "thumbs"
 * (PickerWidgets) that move along a slider track (TrackWidget).
 */
public class SolPickerWidget extends Canvas implements RangeChangeListener {
	
	private TrackWidget tw;
	
	private PickerWidget pwmin, pwmax;
	
	private SolRangeModel solRangeModel;
	
	boolean draggingOnTrack;
	
	private int leftmostPickerLocation;
	
	private int rightmostPickerLocation;
	
	private int pickerWidth = 15, pickerHeight = 18;
	
	/**
	 * Construct a SolPickerWidget.
	 * @param parent the parent Composite containing this widget.
	 * @param style SWT style hints.
	 */
	public SolPickerWidget(Composite parent, int style) {
		super(parent, style);
		setBackground(ColorConstants.white);
		
		pwmin = new PickerWidget(this, SWT.NONE);
		pwmin.setSize(pickerWidth, pickerHeight);
		pwmin.setSolPickerWidget(this);
		
		pwmax = new PickerWidget(this, SWT.NONE);
		pwmax.setSize(pickerWidth, pickerHeight);
		pwmax.setSolPickerWidget(this);
		
		tw = new TrackWidget(this, SWT.NONE);
		tw.setSize(100, pickerHeight); // not 500
		tw.setLocation(pickerWidth / 2, 0);
		pwmin.setTrackWidget(tw);
		pwmax.setTrackWidget(tw);
		
		rightmostPickerLocation = tw.getBounds().width;
		leftmostPickerLocation = 0;
		pack();
		
		tw.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// do nothing
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				Rectangle bounds = new Rectangle(0, 0, tw.getSize().x, tw
						.getSize().y);
				if (bounds.contains(e.x, e.y)) {
					draggingOnTrack = true;
				}
				
				int midLocation = (pwmin.getLocation().x + pwmax.getLocation().x) / 2;
				
				if (e.x <= rightmostPickerLocation
						&& e.x >= leftmostPickerLocation) {
					if (e.x <= midLocation) {
						solRangeModel.setPickedMinValue(Math.round(e.x
								* (solRangeModel.getMaxValue() - solRangeModel
										.getMinValue())
										/ (float) tw.getSize().x)
										+ solRangeModel.getMinValue());
						pwmin.setMoving(true);
						pwmax.setMoving(false);
						
					}
					if (e.x > midLocation) {
						solRangeModel.setPickedMaxValue(Math.round(e.x
								* (solRangeModel.getMaxValue() - solRangeModel
										.getMinValue())
										/ (float) tw.getSize().x)
										+ solRangeModel.getMinValue());
						pwmax.setMoving(true);
						pwmin.setMoving(false);
					}
					
				}
				
			}
			
			@Override
			public void mouseUp(MouseEvent e) {
				draggingOnTrack = false;
			}
			
		});
		
		tw.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				if (draggingOnTrack) {
					if (e.x >= leftmostPickerLocation
							&& e.x <= rightmostPickerLocation)
						dragChanged(e.x, e.y);
					if (e.x < leftmostPickerLocation) {
						dragChanged(leftmostPickerLocation, e.y);
					}
					if (e.x > rightmostPickerLocation) {
						dragChanged(rightmostPickerLocation, e.y);
					}
				}
			}
			
		});
		
		pwmin.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// do nothing
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				pwmin.setMoving(true);
				pwmax.setMoving(false);
			}
			
			@Override
			public void mouseUp(MouseEvent e) {
				pwmin.setMoving(false);
			}
			
		});
		
		pwmax.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// do nothing
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				pwmax.setMoving(true);
				pwmin.setMoving(false);
			}
			
			@Override
			public void mouseUp(MouseEvent e) {
				pwmax.setMoving(false);
			}
			
		});
		
	}
	
	/**
	 * Store a reference to the sol range model that this widget controls.
	 *
	 * @param solRangeModel the sol range model to control.
	 */
	public void setSolRangeModel(SolRangeModel solRangeModel) {
		this.solRangeModel = solRangeModel;
		solRangeModel.addChangeListener(this);
		rangeChanged(solRangeModel.getMinValue(), solRangeModel.getMaxValue(),
				solRangeModel.getPickedMinValue(), solRangeModel
				.getPickedMaxValue());
	}
	
	/**
	 * Convenience method to return the maximum sol range value of the model.
	 * @return the max sol value
	 */
	public int getMaxSolValue() {
		return solRangeModel.getMaxValue();
	}
	
	/**
	 * Convenience method to return the minimum sol range value of the model.
	 * @return the min sol value
	 */
	public int getMinSolValue() {
		return solRangeModel.getMinValue();
	}
	
	/**
	 * Convenience method to set the maximum sol range value of the model.
	 * @param maxSol max sol value to set
	 */
	public void setMaxSolValue(int maxSol) {
		solRangeModel.setMaxValue(maxSol);
	}
	
	/**
	 * Convenience method to set the minimum sol range value of the model.
	 * @param minSol the min sol value to set
	 */
	public void setMinSolValue(int minSol) {
		solRangeModel.setMaxValue(minSol);
	}
	
	/**
	 * Determine the preferred size of this widget.
	 * @return the preferred size as a Point.
	 */
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		// start with this first...
		return super.computeSize(wHint, hHint, changed);
	}
	
	/**
	 * Called when one of the PickerWidgets moves.
	 * @param xCoordinate
	 * @param yCoordinate
	 */
	public void dragChanged(int xCoordinate, int yCoordinate) {
		int solForMousePosition = Math.round(xCoordinate
				* (solRangeModel.getMaxValue() - solRangeModel.getMinValue())
				/ (float) tw.getSize().x)
				+ solRangeModel.getMinValue();
		int maxDifference = solRangeModel.getPickedMaxValue()
		- solForMousePosition; // this is how i know which way im
		// moving
		int minDifference = solRangeModel.getPickedMinValue()
		- solForMousePosition;
		
		if (pwmin.isMoving()) {
			if (minDifference < 0
					&& solForMousePosition >= solRangeModel.getPickedMaxValue()) {
				solRangeModel.setPickedMaxValue(solForMousePosition);
			}
			solRangeModel.setPickedMinValue(solForMousePosition);
		}
		
		if (pwmax.isMoving()) {
			if (maxDifference > 0
					&& solForMousePosition <= solRangeModel.getPickedMinValue()) {
				solRangeModel.setPickedMinValue(solForMousePosition);
			}
			solRangeModel.setPickedMaxValue(solForMousePosition);
		}
	}
	
	/**
	 * Re-positions the PickerWidgets on the TrackWidget when the sol range model is updated.
	 * @param minAllowedSol min of the allowed sol range
	 * @param maxAllowedSol max of the allowed sol range
	 * @param pickedMinSol min selected sol
	 * @param pickedMaxSol max selected sol
	 */
	@Override
	public void rangeChanged(int minAllowedSol, int maxAllowedSol,
			int pickedMinSol, int pickedMaxSol) {
		pwmin.setLocation((int) Math
				.floor((pickedMinSol - minAllowedSol)
						* ((tw.getSize().x) / (float) (maxAllowedSol
								- minAllowedSol + 1))), 0);
		pwmax.setLocation((int) Math
				.floor((pickedMaxSol - minAllowedSol)
						* ((tw.getSize().x) / (float) (maxAllowedSol
								- minAllowedSol + 1))), 0);
	}
		
}
