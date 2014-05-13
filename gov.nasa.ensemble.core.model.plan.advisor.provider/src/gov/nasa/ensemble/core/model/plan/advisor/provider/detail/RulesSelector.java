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
package gov.nasa.ensemble.core.model.plan.advisor.provider.detail;

import gov.nasa.ensemble.dictionary.ERule;

import java.util.Set;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public abstract class RulesSelector extends EventManager {

	public RulesSelector(Composite parent, Object element) {
		// default constructor
	}
	
    public abstract void addListener(IPropertyChangeListener listener);

    public abstract void removeListener(IPropertyChangeListener listener);

	public abstract Button getButton();

    public abstract void setEnabled(boolean state);
    
	public abstract Set<ERule> getWaivedRules();
	
	public abstract void setWaivedRules(Set<ERule> waivedRules);
}
