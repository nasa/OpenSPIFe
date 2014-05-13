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

import gov.nasa.arc.spife.core.plan.rules.view.Activator;
import gov.nasa.arc.spife.core.plan.rules.view.PlanRulesContentProvider;
import gov.nasa.arc.spife.core.plan.rules.view.PlanRulesTreeViewer;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;


public class PlanRulesDialog extends Dialog {

	private static final String DIALOG_SETTINGS_SECTION = "PlanRulesDialogSettings"; //$NON-NLS-1$

	private final Object element;
	private Set<ERule> waivedRules;
	private PlanRulesTreeViewer viewer;
	
	public PlanRulesDialog(Shell parent, Object element) {
		super(parent);
		this.element = element;
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | getDefaultOrientation());
		setBlockOnOpen(true);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Plan rules");
//		newShell.setImage(CREATE_CONSTRAINT_IMAGE);
		super.configureShell(newShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		ScrolledComposite scrollable = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scrollable.setExpandHorizontal(true);
		scrollable.setExpandVertical(true);
		scrollable.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer = new PlanRulesTreeViewer(scrollable);
		viewer.setContentProvider(new PlanRulesContentProvider());
		viewer.setInput(element);
		setViewerFromRules();
		Tree tree = viewer.getTree();
		scrollable.setContent(tree);
		tree.setSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return composite;
	}
	
	@Override
	protected void okPressed() {
		this.getShell().setFocus();
		setRulesFromViewer();
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		Logger logger = Logger.getLogger(PlanRulesDialog.class);
		logger.debug("cancel");
		super.cancelPressed();
	}

	@Override
	protected IDialogSettings getDialogBoundsSettings() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
        if (section == null) section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
        return section;
	}
	
	public Set<ERule> getWaivedRules() {
		return waivedRules;
	}
	
	public void setWaivedRules(Set<ERule> waivedRules) {
		this.waivedRules = waivedRules;
	}

	private void setViewerFromRules() {
		List<ERule> allRules = ActivityDictionary.getInstance().getDefinitions(ERule.class);
		Object[] checkedElements;
		if ((waivedRules == null) || waivedRules.isEmpty()) {
			checkedElements = allRules.toArray();
		} else {
			checkedElements = new Object[allRules.size() - waivedRules.size()];
			int i = 0;
			for (ERule rule : allRules) {
				if (!waivedRules.contains(rule)) {
					checkedElements[i++] = rule;
				}
			}
		}
		viewer.setCheckedElements(checkedElements);
		viewer.refresh(true);
	}

	private void setRulesFromViewer() {
		List<ERule> allRules = ActivityDictionary.getInstance().getDefinitions(ERule.class);
		Object[] unwaivedRules = viewer.getCheckedElements();
		Set<ERule> waivedRules = new LinkedHashSet<ERule>(allRules);
		waivedRules.removeAll(Arrays.asList(unwaivedRules));
		this.waivedRules = waivedRules;
	}
	
}
