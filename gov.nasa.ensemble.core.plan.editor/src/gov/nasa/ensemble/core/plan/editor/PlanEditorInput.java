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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry.RegistrationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * This is a wrapper around the plan object that is provided to the editors that
 * will exist inside the MultiPagePlanEditor.
 * 
 * This class is the Controller in the MVC sense that is shared between the PlanTableWidget
 * and the SPIF-e timeline.  This Controller handles SelectionChangeEvents that are of interest to
 * both the Table and the Timeline.
 * 
 * There is also a controller that is table-specific (TableViewer), through which 
 * model changes are propagated.
 * 
 * This should be deprecated in order to comply with Eclipse contracts. No model information
 * should exist within the editor input and so nobody should be using this. The PlanEditorModel should be used
 * instead to associate the IEditorInput with PlanEditorModel (see PlanEditorModelRegistry)
 */
public class PlanEditorInput implements IFileEditorInput {
	
	// the file representing this plan locally
	private IFile file;

	private final PlanEditorModel editorModel;

	private final EPlan plan;
	
	public PlanEditorInput(EPlan plan, IFile iFile) {
		this.plan = plan;
		if (plan == null) {
			throw new NullPointerException("null plan");
		}
		file = iFile;
		editorModel = new PlanEditorModel(plan);
		try {
			PlanEditorModelRegistry.registerPlanEditorModel(this, editorModel);
		} catch (RegistrationException e) {
			org.apache.log4j.Logger.getLogger(getClass()).error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	@Override
	public boolean exists() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}
	
	/**
	 * Returns the name of the embedded plan.
	 */
	@Override
	public String getName() {
		return plan.getName();
	}
	
	/* 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}
	
	@Override
	public String getToolTipText() {
		return "";
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		Object object = null;
		if (adapter.isInstance(file)) {
			object = file;
		} else {
			object = editorModel.getAdapter(adapter);
		}
		return object;
	}
	
	@Override
	public boolean equals(Object editor) {
		if (this == editor) {
			return true;
		}
		if (editor instanceof IFileEditorInput) {
			IFile otherFile = ((IFileEditorInput)editor).getFile();
			Object modelFile = editorModel.getPlan().getAdapter(IFile.class);
			return otherFile.equals(modelFile);
		}
 		if (!(editor instanceof PlanEditorInput)) {
			return false;
 		}
		return editorModel.equals(editor);
	}

	@Override
	public int hashCode() {
		return editorModel.hashCode();
	}

	@Override
	public String toString() {
		return super.toString() + "[" + editorModel.toString() + "]";
	}

	@Override
	public IFile getFile() {
		// check if the file is null, try to pull up the resource instance
		if (file == null) {
			file = PlanUtils.getFile(editorModel.getEPlan());
		}
		return file;
	}
	
	@Override
	public IStorage getStorage() {
		return getFile();
	}
	
}
