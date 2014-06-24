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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.temporal.modification;

import java.util.Collections;
import java.util.Map;

import org.eclipse.core.expressions.Expression;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

class PlanModifierContributionFactory extends AbstractContributionFactory {
	
	private static final String TEMPORAL_MODIFICATION_COMMAND_ID = "gov.nasa.ensemble.core.plan.temporal.modification";
	
	public PlanModifierContributionFactory(String location, String namespace) {
        super(location, namespace);
    }
	
	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
		for (PlanModifierFactory factory : PlanModifierRegistry.getInstance().getModifierFactories()) {
			String name = factory.getName();
			ImageDescriptor imageDescriptor = factory.getImageDescriptor();
			String id = null;
			String commandId = TEMPORAL_MODIFICATION_COMMAND_ID;
			Map<?, ?> parameters = Collections.singletonMap("name", name);
			ImageDescriptor icon = imageDescriptor;
			String label = name;
			String tooltip = name;
			FactoryServiceLocator locator = new FactoryServiceLocator(serviceLocator, factory);
			CommandContributionItemParameter parameter = new CommandContributionItemParameter(locator, id, commandId, parameters, icon, null, null, label, null, tooltip, SWT.RADIO, null, false);
			IContributionItem item = new CommandContributionItem(parameter);
			Expression visibleWhen = null;
			additions.addContributionItem(item, visibleWhen);
		}
	}
	
	public static final class FactoryServiceLocator implements IServiceLocator {

		private final IServiceLocator parent;
		private final PlanModifierFactory factory;

		public FactoryServiceLocator(IServiceLocator parent, PlanModifierFactory factory) {
	        this.parent = parent;
	        this.factory = factory;
        }

		@Override
		public Object getService(Class api) {
			if (api == PlanModifierFactory.class) {
				return factory;
			}
	        return parent.getService(api);
        }

		@Override
		public boolean hasService(Class api) {
	        return (api == PlanModifierFactory.class) || parent.hasService(api);
        }
		
	}
	
}
