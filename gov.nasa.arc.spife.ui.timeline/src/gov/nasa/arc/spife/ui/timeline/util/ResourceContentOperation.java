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
/**
 * 
 */
package gov.nasa.arc.spife.ui.timeline.util;

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class ResourceContentOperation extends AbstractEnsembleUndoableOperation {

	private final TYPE type;
	private final Resource resource;
	private final Collection<? extends EObject> objects;
	
	public enum TYPE {
		ADD, REMOVE
	}
	
	public ResourceContentOperation(String label, TYPE type, Resource resource, Collection<? extends EObject> objects) {
		super(label);
		this.type = type;
		this.resource = resource;
		this.objects = objects;
	}

	@Override
	protected void dispose(UndoableState state) {
		// no disposal
	}

	@Override
	protected void execute() throws Throwable {
		if (type == TYPE.ADD) {
			resource.getContents().addAll(objects);
		} else {
			resource.getContents().removeAll(objects);
		}
	}

	@Override
	public String toString() {
		return null;
	}
	
	@Override
	protected void undo() throws Throwable {
		if (type == TYPE.ADD) {
			resource.getContents().removeAll(objects);
		} else {
			resource.getContents().addAll(objects);
		}
	}
	
}
