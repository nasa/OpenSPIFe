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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Collection;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class EReferenceCellEditor extends ComboBoxViewerCellEditor {

	private final EReference reference;
	
	public EReferenceCellEditor(Composite parent, EPlanElement element, EReference reference) {
		super(parent);
		this.reference = reference;
		setContenProvider(new ContentProvider());
		setLabelProvider(new LabelProvider());
		setInput(element);
	}
	
	@Override
	protected Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		if (control instanceof CCombo) {
			CCombo combo = (CCombo) control;
			combo.setEditable(false);
		}
		return control;
	}

	/**
	 * Content provider that uses EMFUtils.getReachableObjectsOfType
	 * @author abachman
	 *
	 */
	private class ContentProvider implements IStructuredContentProvider {
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// ignore
		}

		@Override
		public void dispose() {
			// ignore
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof EObject) {
				EObject object = (EObject) inputElement;
				EClassifier type = reference.getEType();
				Collection<EObject> objects = EMFUtils.getReachableObjectsOfType(object, type);
				return objects.toArray(new EObject[objects.size()]);
			}
			return null;
		}
	}

	/**
	 * Label provider that uses EMFUtils.getText
	 * 
	 * @author abachman
	 *
	 */
	private final class LabelProvider implements ILabelProvider {
		@Override
		public void removeListener(ILabelProviderListener listener) {
			// don't need to update comobo live
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// don't need to update comobo live
			return false;
		}

		@Override
		public void dispose() {
			// all done
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// don't need to update comobo live
		}

		@Override
		public Image getImage(Object element) {
			// no image for now
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof EObject) {
				EObject object = (EObject) element;
				return EMFUtils.getText(object, "<?>");
			}
			return "";
		}
	}

	@Override
	public void doSetFocus() {
		final CCombo combo = (CCombo)getControl();
		if (!combo.isDisposed()) {
			String text = combo.getText();
			if (text.length() == 0 && CommonUtils.isWSCocoa()) {
				combo.getDisplay().timerExec(1000, new Runnable() {
					@Override
					public void run() {
						focusIt(combo);
					}
				});
			} else {
				focusIt(combo);
			}
		}	
	}
	
	private void focusIt(final CCombo combo) {
		if (!combo.isDisposed()) {
			combo.setFocus();
			combo.setListVisible(true);
		}
	}
	
}
