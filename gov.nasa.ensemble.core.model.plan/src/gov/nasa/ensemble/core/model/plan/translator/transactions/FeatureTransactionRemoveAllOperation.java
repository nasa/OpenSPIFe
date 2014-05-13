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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.util.EcoreEList;

public class FeatureTransactionRemoveAllOperation<T> extends AbstractTransactionUndoableOperation {

	private final EcoreEList<T> list;
	private final List<T> objects;
	private final Map<Integer, List<T>> indexObjects = new LinkedHashMap<Integer, List<T>>();

	public FeatureTransactionRemoveAllOperation(String label, EcoreEList<T> list, List<T> objects) {
		super(label);
		this.list = list;
		this.objects = objects;
		int i = 0;
		List<T> matches = new ArrayList<T>();
		int matchIndex = -1;
		for (T object : list) {
			if (objects.contains(object)) {
				matches.add(object);
				if (matchIndex == -1) {
					matchIndex = i;
				}
			} else if (matchIndex != -1) {
				indexObjects.put(matchIndex, matches);
				matches = new ArrayList<T>();
				matchIndex = -1;
			}
			i++;
		}
		if (matchIndex != -1) {
			indexObjects.put(matchIndex, matches);
			matches = new ArrayList<T>();
			matchIndex = -1;
		}
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}
	
	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(list, new Runnable() {
			public void run() {
				list.removeAll(objects);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		if (!indexObjects.isEmpty()) {
			TransactionUtils.writing(list, new Runnable() {
				public void run() {
					for (Entry<Integer, List<T>> entry : indexObjects.entrySet()) {
						Integer index = entry.getKey();
						List<T> objects = entry.getValue();
						list.addAll(index, objects);
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
		builder.append(" remove many");
		return builder.toString();
	}
	
	public static <T> void execute(EcoreEList<T> list, List<T> objects) {
		String label = "remove many from " + list.getEStructuralFeature().getName();
		execute(label, list, objects);
	}

	public static <T> void execute(String label, EcoreEList<T> list, List<T> objects) {
		boolean containsOne = false;
		for (T item : list) {
			if (objects.contains(item)) {
				containsOne = true;
				break;
			}
		}
		if (!containsOne) {
			return;
		}
		IUndoableOperation op = new FeatureTransactionRemoveAllOperation<T>(label, list, objects);
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
