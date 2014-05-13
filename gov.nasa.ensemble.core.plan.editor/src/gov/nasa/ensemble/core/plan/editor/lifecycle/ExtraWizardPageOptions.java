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

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.core.model.plan.EPlan;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a hook for extra functionality to be added to a wizard
 * page.
 */
public class ExtraWizardPageOptions implements MissionExtendable {
	
	private Composite parent;
	protected EnsembleWizardPage page;
	
	/**
	 * Factory method for constructing an instance of this class.
	 * 
	 * @return an instance of this class (constructed via the MissionExtender)
	 */
	public static ExtraWizardPageOptions buildExtraWizardPageOptions() {
		ExtraWizardPageOptions extraOptions = null;
		try {
			extraOptions = MissionExtender.construct(ExtraWizardPageOptions.class);
		} catch (ConstructionException e) {
			Logger.getLogger(ExtraWizardPageOptions.class).warn(
					"problem constructing " +
					ExtraWizardPageOptions.class.getSimpleName() +
					" : " + e.toString(),
					e);
		}
		return extraOptions;
	}

	/**
	 * Add extra options to a wizard page.
	 * 
	 * The base class implementation sets the parent pointer. Subclasses should
	 * override this method and call super() first.
	 * 
	 * @param page
	 *            the parent Composite of the wizard page
	 * @param type
	 */
	public void createControl(Composite parent, String type) {
		this.parent = parent;
	}
	
	/**
	 * Execute the extra options that were added via the addExtraOptionsToPage
	 * method.
	 * 
	 * The base class implementation is empty. Subclasses should override this
	 * method.
	 * 
	 * @param plan
	 * @param path
	 */
	public void executeOptions(EPlan plan, String path) {
		// default implementation does nothing
	}
	
	/**
	 * @return the parent composite
	 */
	public Composite getParent() {
		return this.parent;
	}

  /**
   * Update the extra options based on changes to the parent Page.
   * 
   * The base class implementation is empty. Subclasses should override this
   * method.
   * 
   * @param plan
   * @param path
   */
   public void pageUpdated(WizardPage directorySelectionPage) {
	   // default implementation does nothing.
   }
   
   public boolean isComplete() {
	   return true;
   }

	public void setPage(EnsembleWizardPage page) {
		this.page = page;

	}
}
