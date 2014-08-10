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
package gov.nasa.ensemble.core.detail.emf.binding;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Text;

public class TextModifyUndoableObservableValue extends AbstractObservableValue {

	private EObject target;
	private String label;
	private Text text;
	private String oldTextValue;
	private boolean dirty = false;

	public TextModifyUndoableObservableValue(EObject object, String label, Text text) {
		super(Realm.getDefault());
		this.target = object;
		this.label = label;
		this.text = text;
	}

	public void textModified() {
		if (!dirty) {
			oldTextValue = text.getText();
			doSetValue("Dirty");
		}
	}

	@Override
	public Object getValueType() {
		return String.class;
	}

	@Override
	protected void doSetValue(final Object value) {
		IUndoableOperation operation = new TextModifyObservableOperation("modify text for " + label);
		IUndoContext context = gov.nasa.ensemble.emf.transaction.TransactionUtils.getUndoContext(target);
		CommonUtils.execute(operation, context);
	}

	@Override
	protected Object doGetValue() {
		return oldTextValue;
	}

	public class TextModifyObservableOperation extends AbstractEnsembleUndoableOperation {

		protected TextModifyObservableOperation(String label) {
			super(label);
		}

		@Override
		protected void execute() throws Throwable {
			dirty = true;
		}

		public void reset() {
			dirty = false;
		}

		@Override
		protected void undo() throws Throwable {
			if (dirty) { // SPF-8284, MSLICE-977
				WidgetUtils.runInDisplayThread(text, new Runnable() {

					@Override
					public void run() {
						text.setText(oldTextValue);
						dirty = false;
					}

				}, true);
			}
		}

		@Override
		protected boolean isRedoable() {
			return false;
		}

		@Override
		protected void dispose(UndoableState state) {
			// nothing to dispose
		}

		@Override
		public String toString() {
			return "Text field modified oldValue='" + oldTextValue;
		}

	}

}
