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
package gov.nasa.ensemble.core.detail.emf.binding;

/**
 * @author Ivy Deliz
 */

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.multiselect.InPlaceCheckBoxTreeDialog;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class UniqueMultiselectWidget {

	private Composite composite;

	protected Map<Object, TriState> map;
	public StyledText valueStyledText;
	private Button button;
	
	public DetailProviderParameter parameter;
	private EObject target;
	private EStructuralFeature feature;
	private IItemPropertyDescriptor propertyDescriptor;

	private List<String> checkedValues;
	private List<String> greyedValues;
	private List<String> contentList;
	private Collection choiceOfValues;

	public UniqueMultiselectWidget(Composite parent,DetailProviderParameter parameter, Map<Object, TriState> map) {
		init(parent, parameter, map);
	}
	
	public UniqueMultiselectWidget(Composite parent, DetailProviderParameter parameter, String label, Map<Object, TriState> map) {
		init(parent, parameter, map);
	}
	
	private void init (Composite parent, DetailProviderParameter parameter, Map<Object, TriState> map) {
		this.composite = parent;
		this.parameter = parameter;
		this.target = parameter.getTarget();
		this.propertyDescriptor = parameter.getPropertyDescriptor();
		this.feature = (EStructuralFeature) propertyDescriptor.getFeature(target);
		
		this.checkedValues = new ArrayList();
		this.greyedValues = new ArrayList();
				
		setupWidget();
		createControl(parent, new FormToolkit(WidgetUtils.getDisplay()));
		updateWidget(map);
	}

	/** Updates the widget with the map passed as an argument.
	 * 
	 *   @param map
	 *  	Map<Object, Tristate> that sets the tree dialog selection
	 */
	public void updateWidget(Map map) {
		setMap(map);
		updateStyledText();
		updateTreeDialogWithMap();
	}

	/** Method for creating the styled text box, button and listeners
	 * @param parent
	 * 		composite to create widget in
	 * @param toolkit
	 * 		FormToolkit 
	 */
	public void createControl(final Composite parent, FormToolkit toolkit) {

		parent.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = 0; 	layout.marginRight = 0;	layout.horizontalSpacing = 0;

		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL );
		parent.setLayoutData(gridData);
		parent.setLayout(layout);

		valueStyledText = new StyledText(parent, SWT.BORDER | SWT.FILL | SWT.WRAP) {
			//set a small width but fill horizontally so it doesn't stretch the parent composite (SPF-7038)
			@Override
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point size = super.computeSize(wHint, hHint, changed);
				size.x = 15;
				return size;
			}		
		};
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(valueStyledText);
		valueStyledText.setFont(JFaceResources.getDefaultFont());
		valueStyledText.setEditable(false);
	
		
		button = toolkit.createButton(parent, "", SWT.ARROW | SWT.DOWN);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(button);

		if(!this.propertyDescriptor.canSetProperty(target)) {
			button.setEnabled(false);
		}

		//adding multiselect widget button listener
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (propertyDescriptor.canSetProperty(target)) {
					InPlaceCheckBoxTreeDialog multiselectDialog = new InPlaceCheckBoxTreeDialog(parent.getShell(), button, getCheckedValues(), getGreyedValues(), getContentList()) {

						@Override
						public void dialogButtonPressOK() {
							checkedValues = this.getSelectedValues();
							greyedValues = this.getPartialValues();
							Map<Object, TriState> newMap = createMapFromDialogSelection();
							executeOperation(newMap);
						}

						@Override
						public void dialogButtonPressDeselect() {
							checkedValues = new ArrayList<String>();
							greyedValues = new ArrayList<String>();
							Map<Object, TriState> newMap = createMapFromDialogSelection();
							executeOperation(newMap);
						}
						
						private void executeOperation(Map<Object, TriState> newMap) {
							IUndoableOperation operation = new UniqueMultiSelectSetOperation(parameter, map, newMap);
							operation = addContributorOperations(operation, newMap);
							IOperationHistory history = OperationHistoryFactory.getOperationHistory();
							try {
								history.execute(operation, null, null);
							} catch (org.eclipse.core.commands.ExecutionException e) {
								LogUtil.error(e);
							}
						}

						@Override
						public boolean close() {
							updateTreeDialogWithMap();
							return super.close();
						}						
					};
					multiselectDialog.open();
				}
			} 
		});	

		toolkit.adapt(valueStyledText, false, false);

		//adding tooltips
		EMFDetailUtils.addDescriptionTooltip(propertyDescriptor, target, this.getControl());
		EMFDetailUtils.addDescriptionTooltip(propertyDescriptor, target, valueStyledText);
		EMFDetailUtils.addDescriptionTooltip(propertyDescriptor, target, button);
		
	}
	
	private IUndoableOperation addContributorOperations(IUndoableOperation operation, Map<Object, TriState> newMap) {
		if (EMFUtils.CONTRIBUTORS.isEmpty()) {
			return operation;
		}
		List<EObject> targets = new ArrayList<EObject>();
		List<IItemPropertyDescriptor> pds = new ArrayList<IItemPropertyDescriptor>();
		EObject target = parameter.getTarget();
		if (target instanceof MultiEObject) {
			targets.addAll(((MultiEObject)target).getEObjects());
			pds.addAll(((MultiItemPropertyDescriptor)parameter.getPropertyDescriptor()).getPropertyDescriptors());
		} else {
			targets.add(target);
			pds.add(parameter.getPropertyDescriptor());
		}
		
		if (targets.isEmpty()) {
			return operation;
		}
		for (int i=0; i<pds.size(); i++) {
			IItemPropertyDescriptor pd = pds.get(i);
			EObject model = target = targets.get(i);
			EStructuralFeature feature = (EStructuralFeature) pd.getFeature(model);
			EObject commandOwner = EMFDetailUtils.getCommandOwner(pd, model);
			if (commandOwner != null) {
				model = commandOwner;
			}
			Collection oldValues = (Collection)model.eGet(feature);
			Collection newValues = new ArrayList();
			for (Object value : choiceOfValues) {
				if (newMap.get(value) == TriState.TRUE) {
					newValues.add(value);
				}
			}
			operation = EMFUtils.addContributorOperations(operation, target, feature, oldValues, newValues);
		}
		return operation;
	}

	/**  Setting up initial values for the widget & tooltips */
	public void setupWidget() {
		if(choiceOfValues == null) {
			choiceOfValues = propertyDescriptor.getChoiceOfValues(target);
		}

		if(contentList == null) {
			contentList = new ArrayList<String>();
			for (Object object : choiceOfValues) {
				String text = getChoiceText(feature, object);
				contentList.add(text);
			}
		}
	}

	/**
	 * Creates new Map<Object, TriState> from selected items on the Widget
	 * */
	public Map<Object, TriState> createMapFromDialogSelection() {
		HashMap<Object, TriState> newMap = new HashMap();
		
		for (Object value : choiceOfValues) {
			String text = getChoiceText(feature, value);
			if(getCheckedValues().contains(text)) {
				newMap.put(value, TriState.TRUE);
			} else if (getGreyedValues().contains(text)) {
				newMap.put(value, TriState.QUASI);
			} else {
				newMap.put(value, TriState.FALSE);
			}
		}
		return newMap;
	}

	/** Updates the Styled Text box for the Multiselect Widget, including partial values in grey 
	 */
	public void updateStyledText() {
		final String COMA = ", ";

		final StringBuffer buffer = new StringBuffer();
		final List<int[]> grayRanges = new ArrayList();
		
		if(choiceOfValues != null) {
			propertyDescriptor.getChoiceOfValues(target);
		}
		for (Iterator<Object> i = choiceOfValues.iterator(); i.hasNext(); ) {
			Object value = i.next();
			TriState triState = map.get(value);
			if (triState == null || TriState.FALSE == triState) {
				continue;
			}

			String text = getChoiceText(feature, value);
			if (TriState.QUASI == triState) {
				grayRanges.add(new int[] {buffer.length(), text.length()});
				buffer.append(text);
			} else {
				buffer.append(text);
			}

			if (i.hasNext()) {
				buffer.append(COMA);
			}
		}
		
		final String sBuffer;
		if(buffer.toString().endsWith(COMA)) {
			sBuffer = buffer.substring(0, buffer.toString().length()-COMA.length());
		} else {
			sBuffer = buffer.toString();
		}

		try {
			WidgetUtils.runInDisplayThread(valueStyledText, new Runnable() {
				@Override
				public void run() { 
					try {
						if(!valueStyledText.isDisposed()) {
							valueStyledText.setText(sBuffer);
							StyleRange style;
							for(int[] i : grayRanges) {
								style = new StyleRange();
								style.foreground = ColorConstants.gray;
								style.start = i[0];
								style.length = i[1];
								valueStyledText.setStyleRange(style);
							}
						}
					} catch (IllegalArgumentException e) {
						LogUtil.error("bad input string: " + sBuffer, e);
					} 
				} 
			} ); 

		} catch (Exception e) {
			LogUtil.error("Error setting form text to " + sBuffer);
		}
	}
	
	/** Update tree dialog selection with current widget map */
	public void updateTreeDialogWithMap() {
		checkedValues = new ArrayList<String>();
		greyedValues = new ArrayList<String>();

		for (Iterator<Object> i = map.keySet().iterator(); i.hasNext(); ) {
			Object value = i.next();
			TriState triState = map.get(value);
			if (TriState.FALSE == triState || triState == null) {
				continue;
			}
			String text = getChoiceText(feature, value);
			if (TriState.QUASI == triState) {
				greyedValues.add(text);
			} else {
				checkedValues.add(text);
			}
		}
	}
	
	/** Get displayable text from object in map!*/ 
	private String getChoiceText(Object feature, final Object object) {
		String text = "";
		if (feature instanceof EReference) {
			if (object == null) {
				return text;
			}
			text = EMFUtils.getDisplayName((EObject)object);
		} else {
			IStringifier stringifier = EMFUtils.getStringifier((EAttribute)feature);
			text = stringifier.getDisplayString(object);
		}
		return text;
	}
	
	/** Print current map to console - debugging tools */
	public void printCurrentMap(Map<Object, TriState> aMap) {

		for (Iterator<Object> i = aMap.keySet().iterator(); i.hasNext(); ) {
			Object value = i.next();
			TriState triState = aMap.get(value);
			if (TriState.FALSE == triState || triState == null) {
				continue;
			}
			String text = getChoiceText(feature, value);
			System.out.print(text + " = " + triState + "; ");
		}
		System.out.println();
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

	public List<String> getContentList() {
		return contentList;
	}

	public Control getControl() {
		return this.composite;
	}

	public void setMap(Map<Object, TriState> map) {
		this.map = map;
	}

	public Map<Object, TriState> getMap() {
		return map;
	}
}
