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
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.ui.IStructureModifier;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ISelection;

public class ClipboardPasteSpecialOperation extends ClipboardPasteOperation {
	
	private static final PasteSpecialSettings SETTINGS = PasteSpecialSettings.getInstance();
	
	public ClipboardPasteSpecialOperation(ISelection targetSelection, IStructureModifier modifier) {
		super(targetSelection, modifier);
		setLabel("paste special");
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {
		SETTINGS.put(PasteSpecialSettings.ACTIVE, true);
		IStatus status = null;
		try {
			status = super.execute(monitor, info);
		} finally {
			SETTINGS.put(PasteSpecialSettings.ACTIVE, false);
		}
		return status;
	}
	
}
