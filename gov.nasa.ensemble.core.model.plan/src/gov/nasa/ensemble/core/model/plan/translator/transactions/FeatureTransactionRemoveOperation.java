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
package gov.nasa.ensemble.core.model.plan.translator.transactions;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreEList;

public class FeatureTransactionRemoveOperation<T> extends AbstractTransactionUndoableOperation {

	private final EList<T> list;
	private final T object;
	private int index;

	public FeatureTransactionRemoveOperation(String label, EList<T> list, T object) {
		super(label);
		this.list = list;
		this.object = object;
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}
	
	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(list, new Runnable() {
			public void run() {
				index = list.indexOf(object);
				if (index != -1) {
					list.remove(index);
				}
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		if (index != -1) {
			TransactionUtils.writing(list, new Runnable() {
				public void run() {
					list.add(index, object);
				}
			});
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(Object.class.getSimpleName());
		builder.append(":");
		if (list instanceof EcoreEList) {
			builder.append(((EcoreEList) list).getEStructuralFeature().getName());
			builder.append(" on " + ((EcoreEList) list).getEObject());
		}
		builder.append(" remove ");
		builder.append(String.valueOf(object));
		return builder.toString();
	}
	
	public static <T> void execute(EcoreEList<T> list, T object) {
		String label = "remove from " + list.getEStructuralFeature().getName();
		execute(label, list, object);
	}

	public static <T> void execute(String label, EcoreEList<T> list, T object) {
		if (!list.contains(object)) {
			return;
		}
		IUndoableOperation op = new FeatureTransactionRemoveOperation<T>(label, list, object);
		IUndoContext context = TransactionUtils.getUndoContext(list);
		if (context != null) {
			op.addContext(context);
			try {
		        IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		    	history.execute(op, null, null);
		    } catch (Exception e) {
		    	LogUtil.error("execute", e);
		    }
		}
	}
	
}
