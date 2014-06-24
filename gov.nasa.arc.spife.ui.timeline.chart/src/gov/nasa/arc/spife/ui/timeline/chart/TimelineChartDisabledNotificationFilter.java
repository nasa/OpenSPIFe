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
package gov.nasa.arc.spife.ui.timeline.chart;

import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.emf.patch.DisabledPlanSharingNotificationFilter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

public class TimelineChartDisabledNotificationFilter extends
		DisabledPlanSharingNotificationFilter {

	@Override
	public boolean matches(Notification notification) {
		Object notifier = notification.getNotifier();
		return (notifier instanceof EObject && CommonUtils.equals(((EObject) notifier).eClass().getEPackage(), ChartPackage.eINSTANCE));
	}

}
