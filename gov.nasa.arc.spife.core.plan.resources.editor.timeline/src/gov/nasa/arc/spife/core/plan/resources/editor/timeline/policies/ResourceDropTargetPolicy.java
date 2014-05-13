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
package gov.nasa.arc.spife.core.plan.resources.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartDropEditPolicy;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.NumericResourceDefTransferProvider;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.NumericResourceDefTransferable;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.StateResourceDefTransferProvider;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.StateResourceDefTransferable;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.dnd.ProfileTransferable;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

public class ResourceDropTargetPolicy extends ChartDropEditPolicy {

	private static final Transfer NRD_TRANSFER = NumericResourceDefTransferProvider.transfer;
	private static final Transfer SRD_TRANSFER = StateResourceDefTransferProvider.transfer;
	
	@Override
	protected boolean isSupportedTransferData(TransferData d) {
		return NRD_TRANSFER.isSupportedType(d) || SRD_TRANSFER.isSupportedType(d) || super.isSupportedTransferData(d);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Command getDropCommand(final Request request) {
		PlanTimelineViewer viewer = (PlanTimelineViewer) getHost().getViewer();
		final EPlan plan = viewer.getPlan();
		final Chart graph = (Chart) getHost().getModel();
		Map<String, Object> data = request.getExtendedData();
		DropTargetEvent event = (DropTargetEvent)data.get("dropTargetEvent"); 
		final ITransferable transferable = TransferRegistry.getInstance().getDroppedObjects(event.data, event.currentDataType);
		
		if (transferable instanceof NumericResourceDefTransferable) {
			return new Command() {
				@Override
				public void execute() {
					NumericResourceDefTransferable nrdt = (NumericResourceDefTransferable) transferable;
					for (ENumericResourceDef def : nrdt.getNamedDefinitions()) {
						addResoucePlot(plan, graph, def);
					}
				}
				@Override public boolean canExecute() 	{ return true; 	}
				@Override public boolean canUndo() 		{ return false; }
			};
		} else
		if (transferable instanceof StateResourceDefTransferable) {
			return new Command() {
				@Override
				public void execute() {
					StateResourceDefTransferable srdt = (StateResourceDefTransferable) transferable;
					for (EStateResourceDef def : srdt.getNamedDefinitions()) {
						addResoucePlot(plan, graph, def);
					}
				}
				@Override public boolean canExecute() 	{ return true; 	}
				@Override public boolean canUndo() 		{ return false; }
			};
		} else
		if (transferable instanceof ProfileTransferable) {
			return super.getDropCommand(request);
		}
		return null;
	}
	
	private void addResoucePlot(EPlan ePlan, final Chart graph, EResourceDef def) {
		final Profile profile = getResourceProfile(ePlan, def);
		if (profile != null) {
			addResourceProfile(graph, profile);
		}
	}

	private Profile getResourceProfile(EPlan plan, EResourceDef def) {
		Profile profile = null;
		for (Profile p : WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles()) {
			if (CommonUtils.equals(def.getName(), p.getId())) {
				profile = p;
				break;
			}
		}
		return profile;
	}

}
