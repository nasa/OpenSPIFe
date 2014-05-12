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
package gov.nasa.arc.spife.ui.timeline.chart.model.provider.detail;

import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.model.util.ChartAdapterFactory;
import gov.nasa.ensemble.common.ui.detail.IDetailFactory;
import gov.nasa.ensemble.core.detail.emf.IDetailProvider;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

public class ChartDetailAdapterFactory extends ChartAdapterFactory {

	@Override
	public boolean isFactoryForType(Object object) {
		return IDetailProvider.class == object 
				|| super.isFactoryForType(object);
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (target instanceof Plot && IDetailFactory.class == type) {
			return new PlotDetailFactory();
		} else if (target instanceof Chart && IDetailProvider.class == type) {
			return new ChartDetailProvider();
		}
		return null;
	}
	
}
