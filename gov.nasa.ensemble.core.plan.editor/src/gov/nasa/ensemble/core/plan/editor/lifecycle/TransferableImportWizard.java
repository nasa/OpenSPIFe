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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityTransferProvider;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;
import gov.nasa.ensemble.emf.resource.ProgressMonitorInputStream;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TransferableImportWizard extends PlanImportWizard {

	private static final PlanContainerTransferProvider containerTransferProvider = new PlanContainerTransferProvider();
	private static final ActivityTransferProvider activityTransferProvider = new ActivityTransferProvider();

	@Override
	protected FileSelectionPage createFileSelectionPage() {
		FileSelectionPage page = super.createFileSelectionPage();
		page.setPreferredExtensions("spife");
		return page;
	}
	
	@Override
	protected EPlan loadPlan(File file, IProgressMonitor monitor) throws Exception {
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Length of file: " + file.getName() + " longer than Integer.MAX_VALUE: " + length);
		}
		byte[] inputObject = new byte[(int)length];
		InputStream input = ProgressMonitorInputStream.openFileProgressMonitorInputStream(file, monitor);
		Assert.assertTrue("all the bytes should be available", input.available() == length);
		Assert.assertTrue("all the bytes should be read", input.read(inputObject) == length);
		ITransferable inputTransferable = containerTransferProvider.unpackTransferObject(inputObject);
		if (inputTransferable == null) {
			inputTransferable = activityTransferProvider.unpackTransferObject(inputObject);
		}
		if (!(inputTransferable instanceof PlanTransferable)) {
			return null;
		}
		final PlanTransferable planTransferable = (PlanTransferable) inputTransferable;
		List<? extends EPlanElement> elements = planTransferable.getPlanElements();
		final EPlan plan;
		if (!elements.isEmpty() && (elements.get(0) instanceof EPlan)) {
			plan = (EPlan)elements.get(0);
			TransactionUtils.writing(plan, new Runnable() {
				@Override
				public void run() {
					List<EPlanChild> groups = new ArrayList<EPlanChild>(plan.getChildren());
					plan.getChildren().clear();
					planTransferable.setPlanElements(groups);
				}
			});
		} else {
			plan = PlanFactory.getInstance().createPlan(file.getName());
		}
		ISelection planSelection = new StructuredSelection(plan);
		IStructureLocation planLocation = PlanStructureModifier.INSTANCE.getInsertionLocation(inputTransferable, planSelection, InsertionSemantics.ON);
		PlanStructureModifier.INSTANCE.add(inputTransferable, planLocation);
		return plan;
	}
	
	@Override
	protected String getPreferredExtension() {
		return "spife";
	}
}
