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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RuleSetBindingFactory extends BindingFactory {

	@Override
	public Binding createBinding(DetailProviderParameter p) {
		FormToolkit toolkit = p.getDetailFormToolkit();
		Composite parent = p.getParent();
		EObject target = p.getTarget();
		if (target instanceof MultiEObject) {
			LogUtil.warnOnce("multiselection not supported for rule set binding factory");
			return null;
		}
		if (!(target instanceof RuleAdvisorMember)) {
			LogUtil.warn("couldn't generate rule set binding for: " + target);
			return null;
		}
		RuleAdvisorMember member = (RuleAdvisorMember)target;
		WaiverPropertiesEntry rulesEntry = WaiverUtils.getWaiverEntry(member, RuleAdvisorMember.RULE_WAIVERS_KEY, false);
		EPlanElement element = member.getPlanElement();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		boolean isEditable = pd.canSetProperty(member);
		EMFDetailUtils.createLabel(parent, toolkit, member, pd);
		RulesSelector selector = RulesSelectorFactoryRegistry.getRulesSelectorFactory(parent, element);
		Button button = selector.getButton();
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		button.setEnabled(isEditable);
		toolkit.adapt(button, true, true);
		IItemPropertySource source = EMFUtils.adapt(rulesEntry, IItemPropertySource.class);
		pd = source.getPropertyDescriptor(rulesEntry, "value");
		p.setTarget(rulesEntry);
		p.setPropertyDescriptor(pd);
		EMFDetailUtils.bindControlViability(p, new Control[] {button});
		return EMFDetailUtils.bindEMFUndoable(p, new RuleSetSelectorObservableValue(selector));
	}
	
}
