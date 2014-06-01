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
package gov.nasa.arc.spife.core.plan.rules.view.detail;

import gov.nasa.ensemble.core.model.plan.advisor.provider.detail.RulesSelector;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class PlanRulesSelector extends RulesSelector {
	
	public static final String PROP_WAIVEDRULESCHANGE = "waivedRules"; //$NON-NLS-1$

	private final Object element;
	private final Button button;
	
	private Set<ERule> waivedRules = Collections.emptySet();

	public PlanRulesSelector(Composite parent, Object element) {
		super(parent, element);
		this.element = element;
		this.button = new Button(parent, SWT.PUSH);
		updateButtonText();
		getButton().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				open();
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				open();
			}
		});
	}
	
    @Override
	public void addListener(IPropertyChangeListener listener) {
        addListenerObject(listener);
    }

    @Override
	public void removeListener(IPropertyChangeListener listener) {
        removeListenerObject(listener);
    }

	@Override
	public Button getButton() {
		return button;
	}

    @Override
	public void setEnabled(boolean state) {
    	getButton().setEnabled(state);
    }
    
	@Override
	public Set<ERule> getWaivedRules() {
		return waivedRules;
	}
	
	@Override
	public void setWaivedRules(Set<ERule> waivedRules) {
		this.waivedRules = waivedRules;
		updateButtonText();
	}

	private void updateButtonText() {
		int size = (waivedRules != null ? waivedRules.size() : 0);
		String text;
		if (size == 0) {
			text = "No rules waived";
		} else if (size == 1) {
			text = "One rule waived";
		} else {
			text = size + " rules waived";
		}
		getButton().setText(text);
	}
	
	private void open() {
		PlanRulesDialog dialog = new PlanRulesDialog(getButton().getShell(), element);
		dialog.setWaivedRules(waivedRules);
		int result = dialog.open();
		if (result == Window.OK) {
			Set<ERule> newWaivedRules = dialog.getWaivedRules(); 
			Set<ERule> oldWaivedRules = waivedRules;
			waivedRules = newWaivedRules;
			final Object[] finalListeners = getListeners();
		    if (finalListeners.length > 0) {
		        PropertyChangeEvent pEvent = new PropertyChangeEvent(
		                this, PROP_WAIVEDRULESCHANGE, oldWaivedRules, newWaivedRules);
		        for (int i = 0; i < finalListeners.length; ++i) {
		            IPropertyChangeListener listener = (IPropertyChangeListener) finalListeners[i];
		            listener.propertyChange(pEvent);
		        }
		    }
		    updateButtonText();
		}
	}

}
