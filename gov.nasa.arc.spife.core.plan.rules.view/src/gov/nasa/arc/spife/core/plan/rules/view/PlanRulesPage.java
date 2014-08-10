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
/*
 * Created on Apr 6, 2005
 *
 */
package gov.nasa.arc.spife.core.plan.rules.view;

import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.RuleUtils;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.Page;

/**
 * A pagebook page that listens to the model and updates itself with a description of plan rule information.
 * 
 * @author bachmann
 */
class PlanRulesPage extends Page {

	private final ISelectionProvider selectionProvider;
	private final PlanEditorModel model;
	private final PostCommitListener modelChangeListener = new PlanRuleModelChangeListener();

	private Control control;
	private PlanRulesTreeViewer treeViewer;

	public PlanRulesPage(IEditorPart editor, PlanEditorModel model) {
		this.selectionProvider = editor.getSite().getSelectionProvider();
		this.model = model;
	}

	public EPlan getPlan() {
		return model.getEPlan();
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new EnsembleComposite(parent, SWT.NONE);
		composite.setBackground(ColorConstants.brown);
		control = composite;

		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);

		treeViewer = new PlanRulesTreeViewer(composite);
		treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		if (model.getEPlan().getMember(PlanAdvisorMember.class, false) == null) {
			return;
		}
		treeViewer.setContentProvider(new PlanRulesContentProvider());
		treeViewer.setInput(model.getEPlan());
		treeViewer.expandToLevel(2);
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object element = event.getElement();
				boolean active = event.getChecked();
				if (element instanceof ERule) {
					ERule rule = (ERule) element;
					setRuleActive(rule, active);
				}
				if (element instanceof RuleGroup) {
					RuleGroup group = (RuleGroup) element;
					setRuleGroupActive(group, active);
				}
			}
		});
		// Composite findFilterComposite = buildFindFilterComposite(composite);
		// findFilterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		setup();
	}

	private void setup() {
		domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(model.getEPlan());
		domain.addResourceSetListener(modelChangeListener);
		updateRules();
		getSite().setSelectionProvider(selectionProvider);
	}

	@Override
	public void dispose() {
		domain.removeResourceSetListener(modelChangeListener);
		getSite().setSelectionProvider(null);
		super.dispose();
	}

	/**
	 * @return Returns the control.
	 */
	@Override
	public Control getControl() {
		return control;
	}

	/**
	 * Gives focus to the control
	 */
	@Override
	public void setFocus() {
		Control control = getControl();
		if ((control != null) && !control.isDisposed()) {
			control.setFocus();
		}
	}

	/*
	 * Utility
	 */

	private void updateRules() {
		List<ERule> allRules = ActivityDictionary.getInstance().getDefinitions(ERule.class);
		Set<ERule> waivedRules = RuleUtils.getWaivedRules(model.getEPlan());
		Object[] checkedElements = new Object[allRules.size() - waivedRules.size()];
		int i = 0;
		for (ERule rule : allRules) {
			if (!waivedRules.contains(rule)) {
				checkedElements[i++] = rule;
			}
		}
		treeViewer.setCheckedElements(checkedElements);
		treeViewer.refresh(true);
	}

	private ThreadLocal<Boolean> modifyFromCheckbox = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};
	private TransactionalEditingDomain domain;

	private void setRuleActive(ERule rule, boolean active) {
		modifyFromCheckbox.set(Boolean.TRUE);
		RuleUtils.setWaived(model.getEPlan(), rule, !active);
		modifyFromCheckbox.set(Boolean.FALSE);
		treeViewer.update(rule, null);
	}

	private void setRuleGroupActive(RuleGroup group, boolean active) {
		Set<ERule> rules = new LinkedHashSet<ERule>();
		Queue<RuleGroup> groups = new LinkedList<RuleGroup>();
		groups.add(group);
		while (true) {
			RuleGroup currentGroup = groups.poll();
			if (currentGroup == null) {
				break;
			}
			groups.addAll(currentGroup.getGroups().values());
			rules.addAll(currentGroup.getRules());
		}
		modifyFromCheckbox.set(Boolean.TRUE);
		RuleUtils.setWaivedRules(model.getEPlan(), rules, !active);
		modifyFromCheckbox.set(Boolean.FALSE);
		treeViewer.update(rules.toArray(), null);
	}

	private class PlanRuleModelChangeListener extends PostCommitListener {
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			boolean update = false;
			for (Notification notification : event.getNotifications()) {
				Object feature = notification.getFeature();
				if ((feature == AdvisorPackage.Literals.RULE_ADVISOR_MEMBER__WAIVERS) || (feature == AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY__VALUE)) {
					update = true;
					break;
				}
			}
			if (update) {
				TransactionUtils.runInDisplayThread(control, model.getEPlan(), new Runnable() {
					@Override
					public void run() {
						updateRules();
					}
				});
			}

		}
	}

}
