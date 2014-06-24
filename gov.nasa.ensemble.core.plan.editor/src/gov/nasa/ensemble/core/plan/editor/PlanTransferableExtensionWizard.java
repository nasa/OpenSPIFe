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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.ui.TransferableExtensionWizardPage;
import gov.nasa.ensemble.common.ui.WizardPageProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class PlanTransferableExtensionWizard extends Wizard {
	
	private Collection<String> warnings = null;
	private List<WizardPageProvider> providers = null;
	
	private final PlanInsertionLocation location;
	
	public PlanTransferableExtensionWizard(IPlanElementTransferable transferable, PlanInsertionLocation location) {
		this.location = location;
		
		if (providers == null) {
			providers = new ArrayList<WizardPageProvider>();
			for (IPlanTransferableExtension extension : PlanTransferableExtensionRegistry.getInstance().getExtensions()) {
				WizardPageProvider provider = (WizardPageProvider) extension.getAdapter(WizardPageProvider.class);
				if (provider != null) {
					providers.add(provider);
				}
			}
			Collections.sort(providers);
		}

		warnings = new ArrayList<String>();
		for (WizardPageProvider provider : providers) {
			warnings.addAll(provider.getWarnings(transferable, location));
		}
		
	}
	
	@Override
	public void addPages() {
		super.addPages();
		if (warnings.size() > 0) {
			addPage(new WizardWarningPage());
		}
		
		for (WizardPageProvider provider : providers) {
			TransferableExtensionWizardPage createWizardPage = provider.createWizardPage();
			createWizardPage.setStructureLocation(location);
			addPage(createWizardPage);
		}
	}
	
	public Collection<String> getWarnings() {
		return Collections.unmodifiableCollection(warnings);
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
	
	private class WizardWarningPage extends WizardPage {

		public WizardWarningPage() {
			super("Transfer Warnings");
		}
		
		@Override
		public void createControl(Composite parent) {
			Composite pageArea = new Composite(parent, SWT.NONE);
			FillLayout layout = new FillLayout();
			layout.marginWidth = 10;
			layout.marginHeight = 10;
			layout.spacing = 5;
			pageArea.setLayout(layout);
			
			Table table = new Table(pageArea, SWT.BORDER);
			for (String warning : warnings) {
	            TableItem item = new TableItem(table, SWT.NONE);
	            item.setText(warning);
			}
			setControl(pageArea);
			setTitle("Transfer warnings occurred");
			setDescription("Use wizard to handle potential conflicts");
		}
		
	}

}
