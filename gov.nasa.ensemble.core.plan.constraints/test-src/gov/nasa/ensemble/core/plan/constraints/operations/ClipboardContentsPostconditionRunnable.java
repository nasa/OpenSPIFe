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
package gov.nasa.ensemble.core.plan.constraints.operations;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.ui.TemporalChainTransferExtension;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.tests.core.plan.editor.TestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class ClipboardContentsPostconditionRunnable implements Runnable {

	private final EPlanElement[] expectedElements;
	private final boolean copied;
	private final Set<TemporalChain> preservedChains = new HashSet<TemporalChain>();
	private final PlanClipboardPasteOperation pasteOperation;
	
	public ClipboardContentsPostconditionRunnable(EPlanElement[] selectedElements, boolean copied) {
		this.copied = copied;
		this.expectedElements = EPlanUtils.getConsolidatedPlanElements(Arrays.asList(selectedElements)).toArray(new EPlanElement[] { });
		Set<TemporalChain> mentionedChains = new HashSet<TemporalChain>();
		for (EPlanElement planElement : selectedElements) {
			TemporalChain chain = planElement.getMember(ConstraintsMember.class, true).getChain();
			if (chain != null) {
				if (mentionedChains.contains(chain)) {
					preservedChains.add(chain);
				} else {
					mentionedChains.add(chain);
				}
			}
		}
		IStructuredSelection selection = new StructuredSelection(selectedElements);
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		this.pasteOperation = new PlanClipboardPasteOperation(selection, modifier);
	}

	@Override
	public void run() {
		PlanTransferable clipboardTransferable = TestUtils.getClipboardContents();
		List<? extends EPlanElement> planElements = clipboardTransferable.getPlanElements(); 
		Assert.assertEquals("number of elements differed from expected number",
			                expectedElements.length, planElements.size());
		// check the clipboard contents
		if (copied) {
			int i = 0;
			for (EPlanElement expectedElement : EPlanUtils.copy(Arrays.asList(expectedElements))) {
				pasteOperation.assertMatches(expectedElement, planElements.get(i++), false);
			}
		} else {
			for (int i = 0 ; i < planElements.size() ; i++) {
				EPlanElement expectedElement = expectedElements[i];
				Assert.assertEquals(expectedElement, planElements.get(i));
			}
		}
		Set<TemporalChain> chains = TemporalChainTransferExtension.getTransferableChains(clipboardTransferable);
		Assert.assertEquals("all chains mentioned twice should be preserved", 
			                preservedChains.size(), chains.size());
	}

}
