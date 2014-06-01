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
package gov.nasa.ensemble.common.ui;


import gov.nasa.ensemble.common.ui.color.ColorConstants;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;

public class CompositeListViewer extends ContentViewer {

	private CompositeList list;
	private ICompositeListLabel selectedLabel;
	@SuppressWarnings("unused")
	private Logger trace = Logger.getLogger(CompositeListViewer.class);
	public CompositeListViewer(Composite parent) {
		this(new CompositeList(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL));
	}
	
	public CompositeListViewer(CompositeList list) {
		this.list = list;
	}

	@Override
	public Control getControl() {
		return list;
	}


	/**
	 * Remove the old composites from the scrolled composite and clear the selection.
	 */
	private void clear() {
		Composite fillableComposite = list.getFillableComposite();
		if ((fillableComposite != null) && !fillableComposite.isDisposed()) {
			for (Control child : fillableComposite.getChildren()) {
				child.dispose();
			}
		}
		selectedLabel = null;
		ScrollBar scroll = list.getVerticalBar();
		scroll.setSelection(scroll.getMinimum());
		scroll = list.getHorizontalBar();
		scroll.setSelection(scroll.getMinimum());
		list.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	
	/**
	 * Clear the current composites via clear(), and display a label with the
	 * given message.
	 * 
	 * @param msg
	 */
	public void displayMessage(String msg) {
		clear();
		
		Composite fillableComposite = list.getFillableComposite();
		ScrolledComposite sc = (ScrolledComposite)fillableComposite.getParent();
		
		Label label = new Label(fillableComposite, SWT.NONE);
		label.setText(msg);
		
//		SWTUtils.relayout(fillableComposite);
		// Resize the scrollable region
		sc.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	
	
	/**
	 * Use the LabelProvider to add a Composite label for each element in the provided array.  
	 * This method should be called by the ContentProvider when it has new elements that should
	 * be displayed in the CompositeList.
	 * 
	 * Pass in null elements, to indicate that no results are available yet
	 * 
	 * @param elements
	 */
	public void addElements(Object[] elements) {
		clear();
		
		Composite fillableComposite = list.getFillableComposite();
		ScrolledComposite sc = (ScrolledComposite)fillableComposite.getParent();
		
		CompositeListLabelProvider labelProvider = (CompositeListLabelProvider) getLabelProvider();
		if (elements == null) {
			Label noPlansLabel = new Label(fillableComposite, SWT.NONE);
			noPlansLabel.setText("Processing ...");
		}
		else if (elements.length == 0) {
			Label noPlansLabel = new Label(fillableComposite, SWT.NONE);
			noPlansLabel.setText("No matches for the given criteria.");
		}
		else {
			for (Object element: elements) {
				final ICompositeListLabel label = labelProvider.addLabelForElement(fillableComposite, element);
				label.getComposite().addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent event) {
						labelMouseDoubleClick(label, event);
					}
					@Override
					public void mouseDown(MouseEvent event) {
						labelMouseDown(label, event);					
					}
					@Override
					public void mouseUp(MouseEvent event) {
						labelMouseUp(label, event);
					}
					
				});
			}
		}
		
		// Force a relayout of the cotnents
//		SWTUtils.relayout(fillableComposite);
		fillableComposite.layout(true);
	
		// Compute the size based on its contents
		Point computedSize = sc.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		ScrollBar scroll = list.getVerticalBar();
		// Add a basic increment based on the actual size.
		if (scroll != null) {
			scroll.setIncrement(computedSize.y / 20);
			scroll.setPageIncrement(computedSize.y / 10);
		}
		// Resize the minimum region to fit all the contents
		sc.setMinSize(computedSize);
	}
	
	@Override
	public ISelection getSelection() {
		if (selectedLabel == null) return null;
		return new StructuredSelection(selectedLabel);
	}

	@Override
	public void refresh() { 
		//do nothing
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) { 
		//feedback into the view: deselect any previous selected label
		if (getSelection() != null) {
			ICompositeListLabel oldLabel = (ICompositeListLabel) ((IStructuredSelection)getSelection()).getFirstElement();
			oldLabel.getComposite().setBackground(ColorConstants.white);
			for (Control child : oldLabel.getComposite().getChildren()) {
				child.setBackground(ColorConstants.white);
			}
		}
		
		//feedback into the view: select the newly selected label
		ICompositeListLabel newLabel = (ICompositeListLabel) ((IStructuredSelection)selection).getFirstElement();
		newLabel.getComposite().setBackground(ColorConstants.lightGray);
		for (Control child : newLabel.getComposite().getChildren()) {
			child.setBackground(ColorConstants.lightGray);
		}
		
		//cache the selected label
		selectedLabel = newLabel;
		SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
		fireSelectionChanged(event);
	}

	public CompositeList getCompositeList() {
		return list;
	}
	
	/**
	 * Mouse double click event code, placed in its own method so it can be overridden in subclasses  
	 * @param sourceLabel
	 * @param event
	 */
	protected void labelMouseDoubleClick(ICompositeListLabel sourceLabel, MouseEvent event) {
		// do nothing 
	}
	
	/**
	 * Mouse down event code, placed in its own method so it can be overridden in subclasses 
	 * @param sourceLabel
	 * @param event
	 */
	protected void labelMouseDown(ICompositeListLabel sourceLabel, MouseEvent event) {
		// do nothing
	}
	
	/**
	 * Mouse up event code, placed in its own method so it can be overridden in subclasses
	 * @param sourceLabel
	 * @param event
	 */
	protected void labelMouseUp(ICompositeListLabel sourceLabel, MouseEvent event) {
		// select this label
		setSelection(new StructuredSelection(sourceLabel));
		list.forceFocus(); // must use forceFocus
	}

}
