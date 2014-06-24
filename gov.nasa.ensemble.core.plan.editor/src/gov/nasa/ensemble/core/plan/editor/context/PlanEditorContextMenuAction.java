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
package gov.nasa.ensemble.core.plan.editor.context;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public abstract class PlanEditorContextMenuAction extends Action {
	
	protected static final PlanEditorContextMenuIconProvider ICON_PROVIDER = PlanEditorContextMenuIconProvider.getInstance();
	
	private String displayName;
 	private List<? extends EObject> objects;
	private EStructuralFeature feature;
	private IUndoContext undoContext;

	protected PlanEditorContextMenuAction(String displayName, List<? extends EObject> elements, EStructuralFeature feature) {
		if (elements.isEmpty()) {
			new IllegalStateException("Elements list is empty.");
		}
		this.displayName = displayName;
 		this.objects = elements;
 		this.feature  = feature;
 		this.undoContext = EMFUtils.getUndoContext(elements.get(0));
 	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ICON_PROVIDER.get(getFeature());
	}
	
	@Override
	public String getText() {
		return getDisplayName();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public List<? extends EObject> getObjects() {
		return objects;
	}
	
	public IUndoContext getUndoContext() {
		return undoContext;
	}
	
	public EStructuralFeature getFeature() {
		return feature;
	}
	
	@SuppressWarnings("unchecked")
	public Object getCommonValue() {
		Object value = null;
		for (EObject object : objects) {
			IItemPropertySource source = EMFUtils.adapt(object, IItemPropertySource.class);
			if (source != null) {
				IItemPropertyDescriptor pd = source.getPropertyDescriptor(object, feature);
				Object objectValue = EMFUtils.getPropertyValue(pd, object);
				if (value == null) {
					if (objectValue instanceof Collection) {
						value = new BasicEList((Collection<EObject>)objectValue);
					} else {
						value = objectValue;
					}
				} else if (objectValue instanceof Collection) {
					((Collection) value).retainAll((Collection) objectValue);
					if (((Collection) value).isEmpty()) {
						return null;
					}
				} else if (!CommonUtils.equals(value, objectValue)) {
					return null;
				}
			}
		}
		return value;
	}
	
	protected IUndoableOperation getOperation(EObject object, IItemPropertyDescriptor pd, Object oldValue, Object newValue) {
		return new ActionOperation(getDisplayName(), object, pd, oldValue, newValue);
	}
	
	private static class ActionOperation extends AbstractTransactionUndoableOperation {

		private IItemPropertyDescriptor pd;
		private EObject object;
		private Object newValue;
		private Object oldValue;
		
		public ActionOperation(String displayName, EObject object, IItemPropertyDescriptor pd, Object oldValue, Object newValue) {
			super(displayName);
			this.pd = pd;
			this.object = object;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		@Override
		protected void execute() throws Throwable {
			pd.setPropertyValue(object, newValue);
		}
		
		@Override
		protected void undo() throws Throwable {
			pd.setPropertyValue(object, oldValue);
		}
		
		@Override
		protected void dispose(UndoableState state) {
			//do nothing
		}

		@Override
		public String toString() {
			return null;
		}
		
	}
	
}
