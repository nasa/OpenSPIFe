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
package gov.nasa.ensemble.core.plan.editor.merge.export;

import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class MultipleChoiceSelectionPage<T> extends EnsembleWizardPage {

	private List<T> choices;
	private Map<T, String> labels;
	private T selectedChoice;
	private List<IPropertyChangeListener> listeners = new LinkedList<IPropertyChangeListener>();
	
	public MultipleChoiceSelectionPage(String pageName, List<T> choices) {
		this(pageName, choices, Collections.EMPTY_MAP);
	}

	public MultipleChoiceSelectionPage(String pageName, List<T> choices, Map<T,String> names) {
		super(pageName);
		setTitle(pageName);
		this.choices = choices;
		this.labels = names;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new RowLayout(SWT.VERTICAL));
		for (final T choice : choices) {
			String label = labels.get(choice);
			if (label==null) label = choice.toString();
			Button button = new Button(container, SWT.RADIO);
			button.setText(label);
			button.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectedChoice = choice;
					firePropertyChange(selectedChoice, choice);
					pageUpdated();
					getWizard().getContainer().updateButtons();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// No-op
				}
			});
		}
		setControl(container);
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return selectedChoice != null && super.canFlipToNextPage();
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listeners.remove(listener);
	}
	
	protected void firePropertyChange (T oldValue, T newValue) {
		for (IPropertyChangeListener listener : listeners) {
			listener.propertyChange(new PropertyChangeEvent(this, "CHOICE", oldValue, newValue));
		}
	}

	public T getSelectedChoice() {
		return selectedChoice;
	}

}
