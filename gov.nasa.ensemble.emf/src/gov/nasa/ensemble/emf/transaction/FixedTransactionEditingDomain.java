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
package gov.nasa.ensemble.emf.transaction;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.emf.EnsembleResourceSet;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Map;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.PrivilegedRunnable;
import org.eclipse.emf.transaction.impl.TransactionChangeRecorder;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;

public class FixedTransactionEditingDomain extends TransactionalEditingDomainImpl {
	
	private static final int DEFAULT_UNDO_LIMIT = EnsembleProperties.getIntegerPropertyValue("emf.domain.undo.limit", 100);

	private IUndoContext undoContext = new ObjectUndoContext(this);
	private ThreadLocal<InternalTransaction> threadTransaction = new ThreadLocal<InternalTransaction>();
	private ThreadLocal<InternalTransaction> unprivilegedTransaction = new ThreadLocal<InternalTransaction>();
	
	public FixedTransactionEditingDomain(AdapterFactory adapterFactory) {
		super(adapterFactory, new FixedTransactionalCommandStackImpl(), new FixedTransactionResourceSet());
		((FixedTransactionResourceSet)getResourceSet()).setEditingDomain(this);
		OperationHistoryFactory.getOperationHistory().setLimit(undoContext, DEFAULT_UNDO_LIMIT);
	}

	@Override
	public void dispose() {
		OperationHistoryFactory.getOperationHistory().dispose(undoContext, true, true, true);
		EMFUtils.clearReachableObjectsOfType(getResourceSet());
	    super.dispose();
	    getResourceSet().getLoadOptions().clear(); // clear XMLResource.OPTION_USE_PARSER_POOL
	    getResourceSet().getResources().clear(); // help limit memory leaks
	}
	
	public IUndoContext getUndoContext() {
	    return undoContext;
    }

	@Override
	public boolean isReadOnly(Resource resource) {
		if (resourceToReadOnlyMap == null) {
			return false;
		}
		Boolean result = resourceToReadOnlyMap.get(resource);
		if (result == null && resource != null) {
			ResourceSet set = (resource.getResourceSet() != null ? resource.getResourceSet() : resourceSet);
			URIConverter uriConverter = set.getURIConverter();
			URI uri = resource.getURI();
			if (uriConverter != null && uri != null) {
				Map<String, ?> attributes = uriConverter.getAttributes(uri, null);
				result = Boolean.TRUE.equals(attributes.get(URIConverter.ATTRIBUTE_READ_ONLY));
				resourceToReadOnlyMap.put(resource, result);
			}
		}
		return Boolean.TRUE.equals(result);
	}
	
	@Override
	protected TransactionChangeRecorder createChangeRecorder(ResourceSet rset) {
		return new FixedTransactionChangeRecorder(this, rset);
	}

	// Documentation copied from the inherited specification
	@Override
	public void activate(InternalTransaction tx) throws InterruptedException {
		boolean writing = !tx.isReadOnly();
		InternalTransaction existingTransaction = TransactionUtils.checkTransaction(this, writing);
		try {
			threadTransaction.set(tx);
			super.activate(tx);
		} finally {
			threadTransaction.set(existingTransaction);
		}
	}
	
	public InternalTransaction getThreadTransaction() {
		return threadTransaction.get();
	}

	@Override
	public InternalTransaction startTransaction(boolean readOnly, Map<?, ?> options) throws InterruptedException {
		InternalTransaction result = new FixedTransactionImpl(this, readOnly, options);
		result.start();
		return result;
	}
	
	/**
	 * Notice that we're now executing on that other thread's transaction,
	 * but hang on to our own transaction (if any).
	 */
	@Override
	public void startPrivileged(PrivilegedRunnable<?> runnable) {
		super.startPrivileged(runnable);
		unprivilegedTransaction.set(threadTransaction.get());
		threadTransaction.set((InternalTransaction) runnable.getTransaction());
	}
	
	/**
	 * Forget about that other thread's transaction, and remember our own.
	 */
	@Override
	public void endPrivileged(PrivilegedRunnable<?> runnable) {
		super.endPrivileged(runnable);
		threadTransaction.set(unprivilegedTransaction.get());
		unprivilegedTransaction.set(null);
	}
	
	private static class FixedTransactionResourceSet extends EnsembleResourceSet implements IEditingDomainProvider {
		
		private EditingDomain editingDomain;
		
		@Override
		public EditingDomain getEditingDomain() {
			return editingDomain;
		}

		public void setEditingDomain(EditingDomain editingDomain) {
			this.editingDomain = editingDomain;
		}
		
	}

}
