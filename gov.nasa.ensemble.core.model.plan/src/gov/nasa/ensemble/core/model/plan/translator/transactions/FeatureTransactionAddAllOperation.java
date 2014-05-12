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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.util.EcoreEList;

public class FeatureTransactionAddAllOperation<T> extends AbstractTransactionUndoableOperation {

	private final EcoreEList<T> list;
	private final List<T> objects;

	public FeatureTransactionAddAllOperation(String label, EcoreEList<T> list, List<T> objects) {
		super(label);
		this.list = list;
		if (list.getEStructuralFeature().isUnique()) {
			this.objects = new ArrayList<T>(objects);
			this.objects.removeAll(list);
		} else {
			this.objects = objects;
		}
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}
	
	@Override
	protected void execute() throws Throwable {
		if (!objects.isEmpty()) {
			TransactionUtils.writing(list, new Runnable() {
				public void run() {
					list.addAllUnique(objects);
				}
			});
		}
	}

	@Override
	protected void undo() throws Throwable {
		if (!objects.isEmpty()) {
			TransactionUtils.writing(list, new Runnable() {
				public void run() {
					if (list.getEStructuralFeature().isUnique()) {
						list.removeAll(objects);
					} else {
						int size = list.size();
						List<T> addedList = list.subList(size - objects.size(), size);
						if (!CommonUtils.equals(addedList, objects)) {
							LogUtil.error("added list mismatched objects");
						}
						addedList.clear();
					}
				}
			});
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(Object.class.getSimpleName());
		builder.append(":");
		builder.append(list.getEStructuralFeature().getName());
		builder.append(" on " + list.getEObject());
		builder.append(" add many");
		return builder.toString();
	}
	
	public static <T> void execute(EcoreEList<T> list, List<T> objects) {
		String label = "add many to " + list.getEStructuralFeature().getName();
		execute(label, list, objects);
	}

	public static <T> void execute(String label, EcoreEList<T> list, List<T> objects) {
		if (list.getEStructuralFeature().isUnique() && list.containsAll(objects)) {
			return;
		}
		IUndoableOperation op = new FeatureTransactionAddAllOperation<T>(label, list, objects);
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
