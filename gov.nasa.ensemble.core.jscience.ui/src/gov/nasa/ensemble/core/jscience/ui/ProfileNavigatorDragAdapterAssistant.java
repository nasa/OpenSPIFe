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
package gov.nasa.ensemble.core.jscience.ui;

import gov.nasa.ensemble.common.ui.dnd.EnsembleDragAndDropOracle;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.dnd.ProfileTransferProvider;
import gov.nasa.ensemble.core.jscience.ui.dnd.ProfileTransferable;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.navigator.CommonDragAdapterAssistant;

public class ProfileNavigatorDragAdapterAssistant extends CommonDragAdapterAssistant {

	protected static final Transfer[] TRANSFERS = new Transfer[] { ProfileTransferProvider.transfer };
		
	@Override
	public Transfer[] getSupportedTransferTypes() {
		return TRANSFERS;
	}

	@Override
	public boolean setDragData(DragSourceEvent event, IStructuredSelection selection) {
		List<Profile> profiles = getResourceProfiles(selection);
		if (profiles.isEmpty()) {
			return false;
		}
		
		ProfileTransferable transferable = new ProfileTransferable(profiles);
		event.data = TransferRegistry.getInstance().getDragData(transferable, event.dataType);
		return true;
	}
	
	private List<Profile> getResourceProfiles(IStructuredSelection selection) {
		List<Profile> profiles = new ArrayList<Profile>();
		for (Object object : selection.toList()) {
			if (object instanceof Profile) {
				profiles.add((Profile) object);
			} else if (object instanceof IFile) {
				IFile file = (IFile) object;
				URI uri = EMFUtils.getURI(file);
				ResourceSet resourceSet = EMFUtils.createResourceSet();
				resourceSet.getLoadOptions().put(ProfileUtil.IGNORE_DATA_POINTS, Boolean.TRUE);
				Resource resource = resourceSet.createResource(uri);
				try {
					resource.load(null);
				} catch (IOException e) {
					continue;
				}
				for (EObject o : resource.getContents()) {
					if (o instanceof Profile) {
						profiles.add((Profile) o);
					}
				}
			}
		}
		return profiles;
	}
	
	@Override
	public void dragStart(DragSourceEvent anEvent, IStructuredSelection aSelection) {
		DragSource source = (DragSource)anEvent.widget;
		EnsembleDragAndDropOracle.acquireDragging(source);
		super.dragStart(anEvent, aSelection);
	}

	@Override
	public void dragFinished(DragSourceEvent anEvent, IStructuredSelection aSelection) {
		DragSource source = (DragSource)anEvent.widget;
		EnsembleDragAndDropOracle.releaseDragging(source);
		super.dragFinished(anEvent, aSelection);
	}
	
}
