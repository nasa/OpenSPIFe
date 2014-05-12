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
package gov.nasa.ensemble.core.plan.editor.view.template.operations;

import gov.nasa.ensemble.core.model.plan.EPlan;

public abstract class TemplatePlanOperation {
	
	/** The template plan being operated on. Passed into the constructor; may be null. */
	private final EPlan templatePlan;
	
	/** Whether execution has dirtied the template plan. Initially false; may be set by execute(). */
	protected boolean needsSave = false;
	
	/** Passed into the constructor. May be null. */
	private final String label;
	
	/**
	 * The only constructor stores the label and the template plan.
	 * @param label an immutable string label that can be fetched; may be null
	 * @param templatePlan the template plan to be operated on; may be null.
	 */
	public TemplatePlanOperation(String label, EPlan templatePlan) {
		this.label = label;
		this.templatePlan = templatePlan;
	}
	
	/**
	 * Execute the operation. This might modify the needsSaved property, marking the template plan as dirty.
	 * @throws Throwable
	 */
	public abstract void execute() throws Throwable;
	
	/**
	 * Return the template plan passed into the constructor.
	 */
	public EPlan getTemplatePlan() {
		return templatePlan;
	}
	
	/**
	 * Return whether execution of the operation has "dirtied" the template plan so
	 * that it needs to be saved.
	 */
	public boolean isSaveNeeded() {
		return needsSave;
	}

	/** Return the label passed into the constructor. */
	public String getLabel() {
		return label;
	}
}
