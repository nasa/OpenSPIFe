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
package gov.nasa.ensemble.common.ui.editor.lifecycle;

import gov.nasa.ensemble.common.CommonUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

public abstract class EnsembleWizardPage extends WizardPage {
	protected Map<Class, String> activeErrors = new LinkedHashMap<Class, String>();
	protected Map<Class, String> activeWarnings = new LinkedHashMap<Class, String>();
	private boolean requireSpecificMessage = false;
	private String requiredMessage = null;

	public EnsembleWizardPage(String pageName) {
		super(pageName);
	}

	public EnsembleWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	protected void pageUpdated() {
		// Override in children to check error status
	}
	
	/**
	 * Set an informative message to be displayed (not an error), doing so
	 * updates the displayed message and reevaluates the enablement of the
	 * wizard buttons.
	 * 
	 * @param newMessage
	 */
	@Override
	public void setMessage(String newMessage) {
		super.setMessage(newMessage, NONE);
		if (isCurrentPage())
			getContainer().updateButtons();		
	}
	
	/**
	 * Set a  message to be displayed, doing so updates the displayed message
	 * and reevaluates the enablement of the wizard buttons.
	 * 
	 * @param newMessage
	 * @param newType
	 * @deprecated {@link #setMessage(String)} should be used for non-error messages, and {@link #setError(Class, String)} for errors.
	 */
	@Deprecated
	@Override
	public void setMessage(String newMessage, int newType) {
		super.setMessage(newMessage, newType);
		if (isCurrentPage())
			getContainer().updateButtons();		
	}

	/**
	 * Adds a per class error message to the list of active errors, doing so
	 * updates the displayed error message and reevaluates the enablement of
	 * the wizard buttons.
	 * 
	 * @param source
	 * @param message
	 */
	public void setError(Class source, String message) {
		activeErrors.put(source, message);
		rebuildErrorMessage();
	}

	/**
	 * Removes a per class error message from the list of active errors, doing
	 * so updates or removes the displayed error message and reevaluates the
	 * enablement of the wizard buttons.
	 * 
	 * @param source
	 * @param message
	 */
	public void clearError(Class source) {
		activeErrors.remove(source);
		rebuildErrorMessage();
	}

	/**
	 * Removes a per class error message from the list of active errors and
	 * set an informative message to be displayed (not an error), doing so
	 * updates or removes the displayed error message and reevaluates the
	 * enablement of the wizard buttons.
	 * 
	 * This method is preferred to a call to {@link #clearError(Class)} and
	 * subsequently a call to {@link #setMessage(String)}, as this will cause
	 * the enablement of wizard buttons to be evaluated twice.
	 * 
	 * @param source
	 * @param message
	 */
	public void clearError(Class source, String newMessage) {
		activeErrors.remove(source);
		super.setMessage(newMessage, NONE);
		rebuildErrorMessage();
	}
	
	protected void rebuildErrorMessage() {
		if (activeErrors.isEmpty()) {
			super.setErrorMessage(null);
		} else {
			String message = activeErrors.values().iterator().next();
			if (activeErrors.size() == 1)
				super.setErrorMessage(message);
			else
				super.setErrorMessage(message + " (and " + (activeErrors.size() - 1) + " more)");
		}

		if (isCurrentPage())
			getContainer().updateButtons();
	}

	public void setWarning(Class source, String message) {
		if (source == null)
			return;
		activeWarnings.put(source, message);
		rebuildWarningMessage();
	}

	public void clearWarning(Class source) {
		activeWarnings.remove(source);
		rebuildWarningMessage();
	}

	protected void rebuildWarningMessage() {
		if (activeWarnings.isEmpty()) {
			super.setMessage(requiredMessage);
		} else {
			String message = activeWarnings.entrySet().iterator().next().getValue();
			if (activeWarnings.size() == 1)
				super.setMessage(message, WARNING);
			else
				super.setMessage(message + " (and " + (activeWarnings.size() - 1) + " more)", WARNING);
		}

		if (isCurrentPage())
			getContainer().updateButtons();
	}

	@Override
	public boolean isPageComplete() {
		return activeErrors.isEmpty() && (!requireSpecificMessage || CommonUtils.equals(getMessage(), requiredMessage)) && super.isPageComplete();
	}

	public void setRequiredMessage(String requiredMessage) {
		this.requireSpecificMessage = true;
		this.requiredMessage = requiredMessage;
	}

	@Override
	public void setErrorMessage(String message) {
		if (message != null)
			setError(null, message);
		else
			clearError(null);
	}

	public class DefaultModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			pageUpdated();
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		activeErrors.clear();
		activeWarnings.clear();
	}
}
