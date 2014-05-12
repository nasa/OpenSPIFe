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
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class FeatureTransactionChangeOperation extends AbstractTransactionUndoableOperation {

	private final EObject object;
	private final EStructuralFeature feature;
	private final Object oldValue;
	private final Object newValue;

	public FeatureTransactionChangeOperation(String label, EObject object, EStructuralFeature feature, Object newValue) {
		super(label);
		this.object = object;
		this.feature = feature;
		this.oldValue = object.eGet(feature);
		this.newValue = newValue;
	}

	public FeatureTransactionChangeOperation(String label, EObject object, EStructuralFeature feature, Object oldValue, Object newValue) {
		super(label);
		this.object = object;
		this.feature = feature;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public EObject getObject() {
		return object;
	}

	public EStructuralFeature getFeature() {
		return feature;
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(object, new Runnable() {
			public void run() {
				object.eSet(feature, newValue);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(object, new Runnable() {
			public void run() {
				object.eSet(feature, oldValue);
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(Object.class.getSimpleName());
		builder.append(":");
		builder.append(feature.getName());
		builder.append(" on " + object);
		builder.append(" from ");
		builder.append(String.valueOf(oldValue));
		builder.append(" to ");
		builder.append(String.valueOf(newValue));
		return builder.toString();
	}
	
	public static void execute(EObject object, EStructuralFeature feature, Object newValue) {
		Object oldValue = object.eGet(feature);
		if (CommonUtils.equals(oldValue, newValue)) {
			return;
		}
		String featureName = EMFUtils.getDisplayName(object, feature);
		String label = "Edit " + featureName;
		IUndoableOperation op = new FeatureTransactionChangeOperation(label, object, feature, oldValue, newValue);
		op = EMFUtils.addContributorOperations(op, object, feature, oldValue, newValue);
		IUndoContext context = TransactionUtils.getUndoContext(object);
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
