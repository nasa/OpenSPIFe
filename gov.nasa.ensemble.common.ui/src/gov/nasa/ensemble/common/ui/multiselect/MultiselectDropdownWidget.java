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
package gov.nasa.ensemble.common.ui.multiselect;

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;


public class MultiselectDropdownWidget implements ISelectionProvider {

	private boolean readOnly;
	private StyledText valueStyledText;
	private Button button;
	protected Map<Object, TriState> map;
	private Composite composite;	
	private List<String> checkedValues;
	private List<String> greyedValues;
	private List<String> contentList;
	private List<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();
	
	public static String CheckboxMultiSelectAttributeEditor_Select_X;
	public InPlaceCheckBoxTreeDialog selectionDialog;
	public static final org.eclipse.swt.graphics.Font TEXT_FONT = JFaceResources.getDefaultFont();
	
	public MultiselectDropdownWidget(Composite parent, Map<Object, TriState> map) {
		this.map = map;
		this.checkedValues = new ArrayList();
		this.greyedValues = new ArrayList();
		this.contentList = new ArrayList();
		
		setupWidget();
		createControl(parent, new FormToolkit(WidgetUtils.getDisplay()));
		updateStyledText(map);
	}
	
	public void addDisposeListener(DisposeListener disposeListener) {
		if(composite != null) {
			composite.addDisposeListener(disposeListener);
		}
	}
	
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}
	
	public void setupWidget() {
		for (final Object value : map.keySet()) {
			String text = (String)value;
			contentList.add(text);
			
			TriState triState = map.get(value);
			if(triState == TriState.TRUE) {
				checkedValues.add(text);
			} else if (triState == TriState.QUASI) {
				greyedValues.add(text);
			}			
		}
	}
	
	/** Opens it programmatically, as though clicked on. */
	public void open() {
		selectionDialog = new InPlaceCheckBoxTreeDialog(
				composite.getShell(), button, getCheckedValues(), getGreyedValues(), getContentList(), 
				NLS.bind(MultiselectDropdownWidget.CheckboxMultiSelectAttributeEditor_Select_X, ""));

		final List<String> tempCheckedValues = new ArrayList();
		final List<String> tempGreyedValues = new ArrayList();
		tempCheckedValues.addAll(checkedValues);
		tempGreyedValues.addAll(greyedValues);
	
		selectionDialog.addEventListener(new IInPlaceDialogListener() {
			@Override
			public void buttonPressed(InPlaceDialogEvent event) {
				if (event.getReturnCode() == AbstractInPlaceDialog.ID_OK || event.getReturnCode() == Window.OK) {
					List<String> newValues = selectionDialog.getSelectedValues();
					List<String> newPartialValues = selectionDialog.getPartialValues();
					
					if (!tempCheckedValues.equals(newValues) || 
						!tempGreyedValues.equals(newPartialValues)) 
					{
						checkedValues = newValues;
						greyedValues = newPartialValues;
						map = updateMapTriState(map, checkedValues, TriState.TRUE);
						map = updateMapTriState(map, greyedValues, TriState.QUASI);
						updateStyledText(map);
					}
												
				} else if (event.getReturnCode() == AbstractInPlaceDialog.ID_DESELECT_ALL) 
				{
					checkedValues = new ArrayList<String>();
					greyedValues = new ArrayList<String>();
					map = updateMapTriState(map, checkedValues, TriState.TRUE);
					map = updateMapTriState(map, greyedValues, TriState.QUASI);							
					updateStyledText(map);
				}
				fireSelectionChanged();
				
			}
		});

		selectionDialog.open();
	}
		
	public void createControl(final Composite parent, FormToolkit toolkit) {
		composite = toolkit.createComposite(parent);
		composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = 0; 	layout.marginRight = 0;

		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(layout);

		valueStyledText = new StyledText(composite, SWT.BORDER | SWT.FILL | SWT.WRAP);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(valueStyledText);
		valueStyledText.setFont(TEXT_FONT);
		valueStyledText.setEditable(false);

		button = toolkit.createButton(composite, "", SWT.ARROW | SWT.DOWN); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(button);

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
					open();
			} }
		);
		toolkit.adapt(valueStyledText, false, false);
	}
	
	private void fireSelectionChanged() {
		SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
		for (ISelectionChangedListener listener : listeners) {
			listener.selectionChanged(event);
		}
	}
	
	@Override
	public ISelection getSelection() {
		return new StructuredSelection(checkedValues);
	}

	@Override
	public void setSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			setCheckedValues(((IStructuredSelection)selection).toList());
		}
	}
	
	public List<String> getCheckedValues() {
		return checkedValues;
	}
	
	 public void setCheckedValues(List newValues) {
		checkedValues = newValues;
	}
	 
	 public List<String> getGreyedValues() {
		 return greyedValues;
	 }
	 
	 public void setGreyedValues(List newValues) {
		 greyedValues = newValues;
	 }
	 
	 public static String toLabel(String text) {
		return (text != null) ? text.replaceAll("&", "&&") : null; // mask & from SWT //$NON-NLS-1$ //$NON-NLS-2$
	 }


	public boolean isReadOnly() {
		return readOnly;
	 }
	
	 public List<String> getContentList() {
		 return contentList;
	 }
	 
	 public void updateWidgetWithCurrentMap() {
		 checkedValues = new ArrayList<String>();
		 greyedValues = new ArrayList<String>();

		 for (Iterator<Object> i = map.keySet().iterator(); i.hasNext(); ) {
			 Object value = i.next();
			 TriState triState = map.get(value);
			 if (TriState.FALSE == triState || triState == null) {
				 continue;
			 }
			 String text = (String)value;
			 if (TriState.QUASI == triState) {
				 greyedValues.add(text);
			 } else {
				 checkedValues.add(text);
			 }
		 }
	 }
	 
	 public void updateStyledText(Map<Object, TriState> map) {
		final StringBuffer buffer = new StringBuffer();
		final List<int[]> grayRanges = new ArrayList();
		
		for (Iterator<Object> i = map.keySet().iterator(); i.hasNext(); ) {
			Object value = i.next();
			TriState triState = map.get(value);
			if (TriState.FALSE == triState || triState == null) {
				continue;
			}
			
			String text = (String)value;
			
			if (TriState.QUASI == triState) {
				grayRanges.add(new int[] {buffer.length(), text.length()});
				buffer.append(text);
			} else {
				buffer.append(text);
			}
			
			if (i.hasNext()) {
				buffer.append(", ");
			}
		}
		
		if(buffer.lastIndexOf(", ") == buffer.length()-2) {
			buffer.delete(buffer.length()-2, buffer.length());
		}
		
		try {
			WidgetUtils.runInDisplayThread(valueStyledText, new Runnable() {
				@Override
				public void run() {
					try {
						valueStyledText.setText(buffer.toString());
						
						//set color style
						StyleRange style;
						for(int[] i : grayRanges) {
							style = new StyleRange();
							style.foreground = ColorConstants.gray;
							style.start = i[0];
							style.length = i[1];
							valueStyledText.setStyleRange(style);
						}
					} catch (IllegalArgumentException e) {
						//LogUtil.error("bad input string: " + string, e);
					}
				}
			});
		} catch (Exception e) {
			LogUtil.error("Error setting form text to " + buffer.toString());
		}
		
	}
	 
	public Composite getControl() {
		return composite;
	}
	
	public void setNewMap(Map<Object, TriState> map) {
		this.map = map;
	}
	
	public Map<Object, TriState> getReferencedMap() {
		return map;
	}
	
	public Map<Object, TriState> updateMapTriState(Map<Object, TriState> inputMap, List<String> list, TriState triState)
	{
		if (triState == TriState.TRUE){
			//reset all except QUASI records
			for(Object key : inputMap.keySet()){
				if (inputMap.get(key)== TriState.TRUE)
					inputMap.put(key, TriState.FALSE);
			}
			
			for (String value : list){
				if (inputMap.containsKey(value))
					inputMap.put(value, TriState.TRUE);
			}
		}
		else if (triState == TriState.QUASI){
			//reset all except TRUE and FALSE TriState records
			for(Object key : inputMap.keySet()){
				if (inputMap.get(key)== TriState.QUASI)
					inputMap.put(key, TriState.FALSE);
			}
			
			for (String value : list){
				if (inputMap.containsKey(value))
					inputMap.put(value, TriState.QUASI);
			}
		}
		return inputMap;
	}
}


