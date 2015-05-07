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
package gov.nasa.ensemble.tests.core.plan.editor;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.ui.transfers.ClipboardContents;
import gov.nasa.ensemble.common.ui.transfers.ClipboardServer;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.IPlanTransferableExtension;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanTransferableExtensionRegistry;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityTransferProvider;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * @author Andrew
 *
 */
public class TestUtils {

	private static final Transfer[] planTransfers = new Transfer[] { 
		PlanContainerTransferProvider.transfer,
		ActivityTransferProvider.transfer,
	};
	
	/**
	 * Get the clipboard contents.  Plan clipboard contents should consist
	 * of an array which is a string and then one or more plan elements.
	 * @param modifier
	 * @param expectedElements
	 */
	public static PlanTransferable getClipboardContents() {
		Clipboard clipboard = ClipboardServer.instance.getClipboard(null);
		TransferData[] types = clipboard.getAvailableTypes();
		Assert.assertNotNull(types);
		Assert.assertTrue(types.length != 0);
		List<TransferData> acceptableTypes = new ArrayList<TransferData>();
		for (TransferData type : types) {
			for (Transfer transfer : planTransfers) {
				if (transfer.isSupportedType(type)) {
					acceptableTypes.add(type);
					break;
				}
			}
		}
		Assert.assertFalse(acceptableTypes.isEmpty());
		ClipboardContents contents = TransferRegistry.getInstance().getFromClipboard(acceptableTypes);
		Assert.assertNotNull(contents);
		Assert.assertTrue(contents.transferable instanceof PlanTransferable);
		return (PlanTransferable)contents.transferable;
	}

	/*
	 * Testing support relating to the temporal transfer extension
	 */
	
	private static boolean IS_USING_TEMPORAL_EXTENSION = isUsingTemporalExtension();
	
	private static boolean isUsingTemporalExtension() {
		for (IPlanTransferableExtension extension : PlanTransferableExtensionRegistry.getInstance().getExtensions()) {
			String name = extension.getClass().getSimpleName();
			if (name.equals("TemporalTransferableExtension")) {
				return true;
			}
		}
		return false;
	}
	
	public static Date getEarliestStart(EPlanElement[] selectedElements) {
		Date earliest = null;
		if (IS_USING_TEMPORAL_EXTENSION) {
			for (EPlanElement element : selectedElements) {
				TemporalMember member = element.getMember(TemporalMember.class);
				Date startTime = member.getStartTime();
				if (earliest == null) {
					earliest = startTime;
				} else if (startTime != null) {
					earliest = DateUtils.earliest(earliest, startTime);
				}
			}
		}
		return earliest;
	}

	public static Date shiftExpectations(final Date targetDate, final Date earliestDate, final EPlanElement expectedElement) {
		final TemporalMember expectedMember = expectedElement.getMember(TemporalMember.class);
		final Date oldDate = expectedMember.getStartTime();
		if (IS_USING_TEMPORAL_EXTENSION) {
			TransactionUtils.writing(expectedMember, new Runnable() {
				@Override
				public void run() {
					if (oldDate == null) {
						expectedMember.setStartTime(targetDate);
					} else {
						Date expectedDate = DateUtils.add(targetDate, DateUtils.subtract(oldDate, earliestDate));
						expectedMember.setStartTime(expectedDate);
					}
				}
			});
		}
		return oldDate;
	}

	public static void restoreExpectation(final EPlanElement expectedElement, final Date oldDate) {
		if (IS_USING_TEMPORAL_EXTENSION) {
			final TemporalMember expectedMember = expectedElement.getMember(TemporalMember.class);
			TransactionUtils.writing(expectedMember, new Runnable() {
				@Override
				public void run() {
					expectedMember.setStartTime(oldDate);
				}
			});
		}
	}

}
