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
package gov.nasa.ensemble.core.plan.resources.ui.wizard;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.lifecycle.FileSelectionPage;
import gov.nasa.ensemble.core.plan.editor.lifecycle.PlanExportWizardImpl;
import gov.nasa.ensemble.core.plan.resources.ConditionsProvider;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.util.ResourceConditionsUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class ConditionsExportWizard extends PlanExportWizardImpl {

	private ConditionsExportDateSelectionPage page;

	private List<ConditionsProvider> providers;
	
	public ConditionsExportWizard() {
		super();
		providers = ClassRegistry.createInstances(ConditionsProvider.class);
	}

	@Override
	protected String getPreferredExtension() {
		return "conditions";
	}
	
	@Override
	public void addPages(EPlan plan) {
		StringBuffer buffer = new StringBuffer();
		for (ConditionsProvider provider : providers) {
			IStatus status = provider.getStatus(plan);
			int code = status.getSeverity();
			switch (code) {
			case IStatus.ERROR:
				buffer.append(status.getMessage());
			}
		}
		super.addPages(plan);
		if (buffer.length() > 0) {
			page.setErrorMessage(buffer.toString());
		}
	}

	@Override
	protected FileSelectionPage createFileSelectionPage() {
		page = new ConditionsExportDateSelectionPage(getPlan(), SWT.SAVE);
		page.setFileType(getPreferredExtension());
		return page;
	}
	
	/**
	 * Save the plan conditions, a few things to look into:
	 * 
	 * TODO: Better merge strategy to detect possible conditions
	 * TODO: Handle individual provider exceptions better
	 */
	@Override
	protected void savePlan(final EPlan plan, final File file) throws Exception {
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					try {
						writeConditionsFromPlan(plan, file);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}
				}
			});
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			if (targetException instanceof Exception) {
				Exception exception = (Exception)targetException;
				Logger.getLogger(ConditionsExportWizard.class).error("failed to write conditions: ", exception);
				throw exception;
			}
		}
	}

	private void writeConditionsFromPlan(final EPlan plan, final File file) throws Exception, IOException {
        MemberFactory factory = MemberFactory.eINSTANCE;
        Conditions conditions = factory.createConditions();
        Date date = page.getDate();
        ResourceConditionsUtils.setConditionsDate(conditions, date);
        for (ConditionsProvider provider : providers) {
        	Conditions providerConditions = provider.getConditions(plan, date);
        	if (providerConditions == null) {
        		continue;
        	}
        	conditions.getClaims().addAll(providerConditions.getClaims());
        	conditions.getNumericResources().addAll(providerConditions.getNumericResources());
        	conditions.getPowerLoads().addAll(providerConditions.getPowerLoads());
        	conditions.getSharableResources().addAll(providerConditions.getSharableResources());
        	conditions.getStateResources().addAll(providerConditions.getStateResources());
        	conditions.setDescription("as modeled from " + plan.getName());
        	Date providerTime = providerConditions.getTime();
        	if ((providerTime != null) && !providerTime.equals(conditions.getTime())) {
        		String message = "a provider gave a different fincon time";
        		Logger.getLogger(ConditionsExportWizard.class).warn(message);
        	}
        }
        ResourceConditionsUtils.writeConditions(conditions, file);
    }

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		// FIXME provide large icon
		return null;
	}
}
