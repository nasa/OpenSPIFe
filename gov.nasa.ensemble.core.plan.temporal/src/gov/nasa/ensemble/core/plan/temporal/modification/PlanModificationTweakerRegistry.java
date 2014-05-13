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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/** A plan modification tweaker can be mixed and matched with any plan modifier that calls it.
 * The use case is snapping to an orbital or daily event after other time changes have been
 * made according to their own rules and before the changes are applied.
 * @author kanef
 * @since SPF-7695
 */
public class PlanModificationTweakerRegistry {
	
	private static PlanModificationTweakerRegistry registry;
	private List<TweakerFactory> tweakerFactories = new ArrayList<TweakerFactory>();
	
	public static PlanModificationTweakerRegistry getInstance() {
		if (registry == null) {
			registry = new PlanModificationTweakerRegistry();
		}
		return registry;
	}

	private PlanModificationTweakerRegistry() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("gov.nasa.ensemble.core.plan.temporal.PlanModificationTweaker");
		for (IExtension extension : point.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				String elementName = configurationElement.getName();
				if ("tweaker".equals(elementName)) {
					tweakerFactories.add(new TweakerFactory(configurationElement));
				}
			}
		}
	}
	
	/** Modifies the contents of changedTimes to reflect further changes.
	 * Intended to be called continually during a drag, so continually snapping to a new time is not appropriate. */
	public void applyTweaksDuringMove(EPlan plan, Map<EPlanElement, TemporalExtent> changedTimes) {
		for (TweakerFactory factory : tweakerFactories) {
			factory.tweakDuringMove(plan, changedTimes);
		}
	}
	
	/** Modifies the contents of changedTimes to reflect further changes.
	 * Intended to be called after a move is completed, for snap actions. */
	public void applyTweaksAfterMove(EPlan plan, Map<EPlanElement, TemporalExtent> changedTimes) {
		for (TweakerFactory factory : tweakerFactories) {
			factory.tweakAfterMove(plan, changedTimes);
		}
	}
	
	public class TweakerFactory {

		private IPlanModificationTweaker extension;

		protected TweakerFactory(IConfigurationElement configurationElement) {
			try {
				extension = (IPlanModificationTweaker) configurationElement.createExecutableExtension("class");
			} catch (CoreException e) {
				LogUtil.error(e);
			}
		}

		/** Modifies the contents of changedTimes to reflect further changes.
		 * Intended to be called continually during a drag, so continually snapping to a new time is not appropriate. */
		public void tweakDuringMove(EPlan plan, Map<EPlanElement, TemporalExtent> changedTimes) {
			if (!changedTimes.isEmpty()) {
				extension.initialize(plan, changedTimes);
				changedTimes.putAll(extension.tweakDuringMove());
			}
		}
		
		/** Modifies the contents of changedTimes to reflect further changes.
		 * Intended to be called after a move is completed, for snap actions. */
		public void tweakAfterMove(EPlan plan, Map<EPlanElement, TemporalExtent> changedTimes) {
			if (!changedTimes.isEmpty()) {
				extension.initialize(plan, changedTimes);
				changedTimes.putAll(extension.tweakAfterMove());
			}
		}

	}

	
}
