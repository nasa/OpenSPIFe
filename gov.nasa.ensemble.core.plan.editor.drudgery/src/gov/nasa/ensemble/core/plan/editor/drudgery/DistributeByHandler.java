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
package gov.nasa.ensemble.core.plan.editor.drudgery;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.jscience.util.DurationStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.jscience.physics.amount.Amount;

public class DistributeByHandler extends DrudgerySavingHandler {

	public final static double MARS_MULTIPLIER = 1.0275;
	private final static IStringifier<Amount<Duration>> DURATION_STRINGIFIER = new DurationStringifier();
	
	public DistributeByHandler() {
		super("Distribute By", "Distributing By");
	}
	
	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements) {
		Map<EPlanElement, Date> map = new HashMap<EPlanElement, Date>();
		IEditorPart activeEditorPart = getActiveEditor();
		if (activeEditorPart == null) {
			return map;
		}
		DistributeByDialog distributeByDialog = new DistributeByDialog(activeEditorPart.getSite().getShell());
		distributeByDialog.setBlockOnOpen(true);
		int code = distributeByDialog.open();
		if (code == Window.OK) {
			Amount<Duration> duration = null;
			try {
				duration = DURATION_STRINGIFIER.getJavaObject(distributeByDialog.getTextValue(), null);
				// span is separation distance in milliseconds
				long span = duration.longValue(DateUtils.MILLISECONDS);
				double multiplier = 1.0;
				// adjustment for mars hours
				if (!distributeByDialog.isEarthDurationSelected()) {
					multiplier = MARS_MULTIPLIER;
				}
				span *= multiplier;
				// iterate through all items, and increment start time from initial
				// start time by the length of span - this determines the start of
				// the following item(s)
				long initialStartTime = 0;
				TemporalExtent extent = elements.get(0).getMember(TemporalMember.class).getExtent();
				if (distributeByDialog.isStartAndStartSelected()) {
					initialStartTime = extent.getStart().getTime();
				} else {
					initialStartTime = extent.getEnd().getTime();
				}
				long cumulativeDuration = 0; 
				for (int i = 1; i < elements.size(); i++) {
					map.put(elements.get(i), new Date(initialStartTime + (span * i) + cumulativeDuration));
					if (!distributeByDialog.isStartAndStartSelected()) {
						extent = elements.get(i).getMember(TemporalMember.class).getExtent();
						cumulativeDuration += extent.getDurationMillis();
					}
				}
			} catch(ParseException e) {
				LogUtil.error(e);
			}
		}
		return map;
	}

	@Override
	public String getCommandId() {
		return DISTRIBUTE_BY_COMMAND_ID;
	}

}
