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
package gov.nasa.ensemble.emf.transaction;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.Activator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionChangeDescription;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalLifecycle;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.PrivilegedRunnable;
import org.eclipse.emf.transaction.impl.TransactionChangeRecorder;
import org.eclipse.emf.transaction.impl.TransactionImpl;
import org.eclipse.emf.transaction.util.CommandChangeDescription;
import org.eclipse.emf.transaction.util.CompositeChangeDescription;
import org.eclipse.emf.transaction.util.ConditionalRedoCommand;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.transaction.util.TriggerCommand;

public class FixedTransactionImpl implements InternalTransaction {

	private static long nextId = 0L;
	
	final long id;
	
	private final TransactionalEditingDomain domain;
	private Thread owner;
	private final boolean readOnly;
	private final Map<Object, Object> options;
	private final Map<Object, Object> mutableOptions;
	
	private InternalTransaction parent;
	private InternalTransaction root;
	
	private boolean active;
	private boolean closing; // prevents re-entrant commit/rollback
	private boolean rollingBack;
	protected List<Notification> notifications;
	protected final CompositeChangeDescription change;
	
	private boolean aborted;
	private IStatus status = Status.OK_STATUS;
	private Command triggers;
	private CommandChangeDescription triggerChange;
	
	/**
	 * Initializes me with my editing domain and read-only state.
	 * 
	 * @param domain the editing domain in which I operate
	 * @param readOnly <code>true</code> if I am read-only; <code>false</code>
	 *     if I am read/write
	 */
	public FixedTransactionImpl(TransactionalEditingDomain domain, boolean readOnly) {
		this(domain, readOnly, null);
	}
	
	/**
	 * Initializes me with my editing domain, read-only state, and additional
	 * options.
	 * 
	 * @param domain the editing domain in which I operate
	 * @param readOnly <code>true</code> if I am read-only; <code>false</code>
	 *     if I am read/write
	 * @param options my options, or <code>null</code> for defaults
	 */
	public FixedTransactionImpl(TransactionalEditingDomain domain, boolean readOnly,
			Map<?, ?> options) {
		this.domain = domain;
		this.readOnly = readOnly;
		this.owner = Thread.currentThread();
		
		this.mutableOptions = new java.util.HashMap<Object, Object>();
		this.options = Collections.unmodifiableMap(mutableOptions);
		
		if (options != null) {
			mutableOptions.putAll(options);
		}
		
		synchronized (FixedTransactionImpl.class) {
			this.id = nextId++;
		}
		
		change = new CompositeChangeDescription();
		
		if (collectsNotifications(this)) {
			notifications = new org.eclipse.emf.common.util.BasicEList.FastCompare<Notification>();
		} else {
			// no need to collect any notifications if we won't use them
			notifications = null;
		}
	}

	
	// Documentation copied from the inherited specification
	@Override
	public synchronized void start() throws InterruptedException {
		if (Thread.currentThread() != getOwner()) {
			IllegalStateException exc = new IllegalStateException("Not transaction owner"); //$NON-NLS-1$
			LogUtil.error("start", exc); //$NON-NLS-1$
			throw exc;
		}
		
		if (isActive()) {
			IllegalStateException exc = new IllegalStateException("Transaction is already active"); //$NON-NLS-1$
			LogUtil.error("start", exc); //$NON-NLS-1$
			throw exc;
		}
		
		if (getInternalDomain().getActiveTransaction() == null) {
			// attempting to start a new root transaction
			InternalLifecycle lifecycle = getLifecycle();
			if (lifecycle != null) {
				lifecycle.transactionStarting(this);
			}
		}
		
		try {
			getInternalDomain().activate(this);
		} catch (InterruptedException e) {
			if (getInternalDomain().getActiveTransaction() == null) {
				// interrupted attempting to start a new root transaction
				InternalLifecycle lifecycle = getLifecycle();
				if (lifecycle != null) {
					lifecycle.transactionInterrupted(this);
				}
			}
			
			throw e; // re-throw
		}
		
		active = true;

		if (this != getInternalDomain().getActiveTransaction()) {
			IllegalStateException exc = new IllegalStateException("Activated transaction while another is active"); //$NON-NLS-1$
			LogUtil.error("start", exc); //$NON-NLS-1$
			throw exc;
		}
		
		// do this after activation, because I only have a parent once I have
		//    been activated
		if (parent != null) {
			// the parent stops recording here; I record for myself to implement
			//    support rollback of my changes only
			parent.pause();
		}
		
//		if (getRoot() == this) {
//		    // root transaction sets up validate-edit support
//    		
//    		Object validateEdit = getOptions().get(OPTION_VALIDATE_EDIT);
//    		if (Boolean.TRUE.equals(validateEdit)) {
//    		    // default implementation
//    		    validateEdit = new ValidateEditSupport.Default();
//    		}
//    		
//    		if (validateEdit instanceof ValidateEditSupport) {
//    		    getInternalDomain().getChangeRecorder().setValidateEditSupport(
//    		        (ValidateEditSupport) validateEdit);
//    		}
//		}
		
		startRecording();
		
		if (getParent() == null) {
			// started a new root transaction
			InternalLifecycle lifecycle = getLifecycle();
			if (lifecycle != null) {
				lifecycle.transactionStarted(this);
			}
		}
	}
	
	// Documentation copied from the inherited specification
	@Override
	public final TransactionalEditingDomain getEditingDomain() {
		return domain;
	}

	/**
	 * Obtains the life-cycle adapter, if any, of my editing domain, with which
	 * I will notify it of my lifecycle events.
	 * 
	 * @return my domain's lifecycle adapter, or <code>null</code> if my editing
	 *         domain does not provide one
	 * 
	 * @since 1.3
	 */
	protected InternalLifecycle getLifecycle() {
		return TransactionUtil.getAdapter(getEditingDomain(),
			InternalLifecycle.class);
	}

	// Documentation copied from the inherited specification
	@Override
	public final Transaction getParent() {
		return parent;
	}
	
	// Documentation copied from the inherited specification
	@Override
	public final void setParent(InternalTransaction parent) {
		this.parent = parent;
		
		this.root = (parent == null)? this : parent.getRoot();
		
        inheritOptions(parent);
	}
	
	// Documentation copied from the inherited specification
	@Override
	public final InternalTransaction getRoot() {
		return root;
	}

	// Documentation copied from the inherited specification
	@Override
	public final Thread getOwner() {
		return owner;
	}

	// Documentation copied from the inherited specification
	@Override
	public final boolean isReadOnly() {
		return readOnly;
	}

	// Documentation copied from the inherited specification
	@Override
	public final Map<Object, Object> getOptions() {
		return options;
	}

	// Documentation copied from the inherited specification
	@Override
	public synchronized boolean isActive() {
		return active;
	}
	
	// Documentation copied from the inherited specification
	@Override
	public IStatus getStatus() {
		return status;
	}
	
	// Documentation copied from the inherited specification
	@Override
	public void setStatus(IStatus status) {
		if (status == null) {
			status = Status.OK_STATUS;
		}
		
		this.status = status;
	}
	
	// Documentation copied from the inherited specification
	@Override
	public synchronized void abort(IStatus status) {
		assert status != null;
		
		this.aborted = true;
		this.status = status;
		
		if (parent != null) {
			// propagate
			parent.abort(status);
		}
	}
	
	/**
	 * Queries whether I have been aborted.
	 * 
	 * @return <code>true</code> if I have been aborted; <code>false</code>, otherwise
	 * 
	 * @see InternalTransaction#abort(IStatus)
	 */
	protected boolean isAborted() {
		return aborted;
	}

	// Documentation copied from the inherited specification
	@Override
	public void commit() throws RollbackException {
		if (Thread.currentThread() != getOwner()) {
			IllegalStateException exc = new IllegalStateException("Not transaction owner"); //$NON-NLS-1$
			LogUtil.error("commit", exc); //$NON-NLS-1$
			throw exc;
		}
		
		if (closing) {
			IllegalStateException exc = new IllegalStateException("Transaction is already closing"); //$NON-NLS-1$
			LogUtil.error("commit", exc); //$NON-NLS-1$
			throw exc;
		}
		
		if (!isActive()) {
			IllegalStateException exc = new IllegalStateException("Transaction is already closed"); //$NON-NLS-1$
			LogUtil.error("commit", exc); //$NON-NLS-1$
			throw exc;
		}
		
		try {
			if (getParent() == null) {
				// closing a root transaction
				InternalLifecycle lifecycle = getLifecycle();
				if (lifecycle != null) {
					lifecycle.transactionClosing(this);
				}
			}
			
			// first, check whether I have been aborted.  If so, then I must roll back
			if (isAborted()) {
				doRollback();
				RollbackException exc = new RollbackException(getStatus());
				LogUtil.error("commit", exc); //$NON-NLS-1$
				throw exc;
			}
			
			closing = true;
			
			if (isTriggerEnabled(this)) {
				try {
					getInternalDomain().precommit(this);
				} catch (RollbackException e) {
					doRollback();
					LogUtil.error("commit", e); //$NON-NLS-1$
					throw e;
				}
			}
			
//			if (getRoot() == this) {
//                // only the root validates.  Do validation before validate-edit
//			    // because its results are generally more interesting
//			    IStatus validationStatus = null;
//			    
//			    if (isValidationEnabled(this)) {
//    				validationStatus = validate();
//			    }
//
//			    // now do validate-edit if validation status is not roll-back
//			    if ((validationStatus == null)
//			            || (validationStatus.getSeverity() < IStatus.ERROR)) {
//			        
//	                ValidateEditSupport validateEdit = getInternalDomain()
//	                    .getChangeRecorder().getValidateEditSupport();
//	                
//	                if (validateEdit != null) {
//	                    Object context = getOptions().get(OPTION_VALIDATE_EDIT_CONTEXT);
//	                    IStatus editStatus = validateEdit.validateEdit(this, context);
//	                    
//	                    validationStatus = combine(validationStatus, editStatus);
//	                }
//			    }
//			    
//			    if (validationStatus != null) {
//    				setStatus(validationStatus);
//    				
//    				if (validationStatus.getSeverity() >= IStatus.ERROR) {
//    					doRollback();
//    					RollbackException exc = new RollbackException(validationStatus);
//    					LogUtil.error("commit", exc); //$NON-NLS-1$
//    					throw exc;
//    				}
//			    }
//	            
//	            ValidateEditSupport validateEdit = getInternalDomain()
//	                .getChangeRecorder().getValidateEditSupport();
//	            
//	            if (validateEdit != null) {
//	                validateEdit.finalizeForCommit();
//	            }
//			}
		} finally {
			// in case of exception, rollback() already stopped recording
			stopRecording();
			
//			if (getRoot() == this) {
//			    // clear the validate-edit tracking
//			    getInternalDomain().getChangeRecorder().setValidateEditSupport(null);
//			}
			
			close();
		}
	}
	
//	/**
//	 * Produces a status object combining live-validation status with
//	 * validate-edit status.
//	 * 
//	 * @param validationStatus a live-validation status, or <code>null</code>
//	 *    if validation is disabled
//	 * @param editStatus a validate-edit status, or <code>null</code> if it is
//	 *    not enabled
//	 *    
//	 * @return an appropriate status, which may even by OK just because both
//	 *    inputs are <code>null</code>
//	 */
//	private IStatus combine(IStatus validationStatus, IStatus editStatus) {
//	    IStatus result;
//	    
//	    if ((validationStatus == null) || validationStatus.isOK()) {
//	        if ((editStatus == null) || editStatus.isOK()) {
//	            result = Status.OK_STATUS;
//	        } else {
//	            result = editStatus;
//	        }
//	    } else if ((editStatus == null) || editStatus.isOK()) {
//	        result = validationStatus;
//	    } else if (editStatus.getSeverity() > validationStatus.getSeverity()) {
//            // validate-edit status has priority
//            result = new MultiStatus(Activator.PLUGIN_ID,
//                editStatus.getCode(),
//                new IStatus[] {editStatus, validationStatus},
//                editStatus.getMessage(), null);
//        } else {
//            // live-validation status has priority
//            result = new MultiStatus(Activator.PLUGIN_ID,
//                validationStatus.getCode(),
//                new IStatus[] {validationStatus, editStatus},
//                validationStatus.getMessage(), null);
//        }
//	    
//	    return result;
//	}

	// Documentation copied from the inherited specification
	@Override
	public void rollback() {
		if (Thread.currentThread() != getOwner()) {
			IllegalStateException exc = new IllegalStateException("Not transaction owner"); //$NON-NLS-1$
			LogUtil.error("rollback", exc); //$NON-NLS-1$
			throw exc;
		}
		
		if (closing) {
			IllegalStateException exc = new IllegalStateException("Transaction is already closing"); //$NON-NLS-1$
			LogUtil.error("rollback", exc); //$NON-NLS-1$
			throw exc;
		}
		
		if (!isActive()) {
			IllegalStateException exc = new IllegalStateException("Transaction is already closed"); //$NON-NLS-1$
			LogUtil.error("rollback", exc); //$NON-NLS-1$
			throw exc;
		}
		
		if (getParent() == null) {
			// closing a root transaction
			InternalLifecycle lifecycle = getLifecycle();
			if (lifecycle != null) {
				lifecycle.transactionClosing(this);
			}
		}
		
		closing = true;
		doRollback();
	}
	
	private void doRollback() {
		rollingBack = true;
		
		try {
			if (!isReadOnly()) {
				// ensure that validation of a nesting transaction does not
				//   include any of my changes that I have rolled back and that
				//   post-commit doesn't find any of my changes, either.  Do
				//   this now (before deactivation) so that the validator can
				//   see that I am rolling back
				getInternalDomain().getValidator().remove(this);
				notifications = null;
				
				stopRecording();
				
				if (isUndoEnabled(this)) {
					change.apply();
					
					// forget the description.  The changes are reverted
					change.clear();
				}
			}
			
//			if (getRoot() == this) {
//                ValidateEditSupport validateEdit = getInternalDomain()
//                    .getChangeRecorder().getValidateEditSupport();
//                
//                if (validateEdit != null) {
//        			validateEdit.finalizeForRollback();
//                }
//			}
			
			// ensure an appropriate transaction status for roll-back
			IStatus status = getStatus();
			if ((status == null) || status.isOK()) {
				// generic error status for roll-back
				setStatus(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "rollback requested"));
			} else if (status.getSeverity() < IStatus.ERROR) {
				// make a multi-status for rollback error but preserving the
				// original status
				IStatus rbStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "rollback requested");
				setStatus(new MultiStatus(Activator.PLUGIN_ID,
					rbStatus.getCode(), new IStatus[]{rbStatus, status},
					rbStatus.getMessage(), null));
			}
		} finally {
			rollingBack = false;
			close();
		}
	}

	// Documentation copied from the inherited specification
	@Override
	public void yield() {
		getEditingDomain().yield();
	}

	// Documentation copied from the inherited specification
	@Override
	public TransactionChangeDescription getChangeDescription() {
		return (isActive() && !closing) ? null : change;
	}

	/**
	 * Obtains my owning editing domain as the internal interface.
	 * 
	 * @return the internal view of my editing domain
	 */
	protected InternalTransactionalEditingDomain getInternalDomain() {
		return (InternalTransactionalEditingDomain) getEditingDomain();
	}

	/**
	 * Starts recording changes upong activation or resumption from a child
	 * transaction, unless undo recording is disabled by my options.
	 */
	private void startRecording() {
		TransactionChangeRecorder recorder = getInternalDomain().getChangeRecorder();
		
		if (isUndoEnabled(this)) {
			if (!recorder.isRecording()) {
				recorder.beginRecording();
			} else if (recorder.isPaused()) {
				recorder.resume();
			}
		}
	}
	
	/**
	 * Stops recording changes and adds them to my composite change description,
	 * unless undo recording is disabled by my options.
	 */
	private void stopRecording() {
		TransactionChangeRecorder recorder = getInternalDomain().getChangeRecorder();
		
		if (isUndoEnabled(this) && recorder.isRecording()) {
			Transaction active = getInternalDomain().getActiveTransaction();
			if ((active != null) && !isUndoEnabled(active)) {
				// the child is not recording, so we just suspend our change
				//    recording and resume it later.  This is a lightweight
				//    alternative to cutting a change description and starting
				//    a new one, later
				recorder.pause();
			} else {
				change.add(recorder.endRecording());
			}
		}
	}
	
	// Documentation copied from the inherited specification
	@Override
	public void pause() {
		// if we are rolling back, then we don't need to worry about recording
		//    changes because we are permanently undoing changes.
		//    See additional comments in the resume(TransactionChangeDescription)
		//    method
		if (!isRollingBack()) {
			stopRecording();
		}
	}
	
	// Documentation copied from the inherited specification
	@Override
	public void resume(TransactionChangeDescription nestedChanges) {
		// if we are rolling back, then we don't need to worry about recording
		//    changes because we are permanently undoing changes.  It can happen
		//    that a nested transaction is created in order to roll back changes
		//    that are nested within non-EMF changes, which would cause a
		//    concurrent modification of our composite change if we were to add
		//    the nested transaction's changes to us
		if (!isRollingBack()) {
			if (isUndoEnabled(this) && (nestedChanges != null)) {
				change.add(nestedChanges);
			}
			
			startRecording();
		}
	}
	
	// Documentation copied from the inherited specification
	@Override
	public boolean isRollingBack() {
		return rollingBack || ((parent != null) && parent.isRollingBack());
	}

	/**
	 * Closes me.  This is the last step in committing or rolling back,
	 * deactivating me in my editing domain.  Also, if I have a parent
	 * transaction, I {@link InternalTransaction#resume(TransactionChangeDescription) resume}
	 * it.
	 * <p>
	 * If a subclass overrides this method, it <em>must</em> ensure that this
	 * implementation is also invoked.
	 * </p>
	 */
	protected synchronized void close() {
		if (isActive()) {
			active = false;
			closing = false;
			getInternalDomain().deactivate(this);
			
			if (parent != null) {
				// my parent resumes recording its changes now that mine are either
				//  committed to it or rolled back. The parent accumulates
				//  my changes except for certain special cases where we must
				//  prevent the passing of our changes.
				
				if (hasOption(this, TransactionImpl.BLOCK_CHANGE_PROPAGATION)
						&& hasOption(parent, TransactionImpl.ALLOW_CHANGE_PROPAGATION_BLOCKING)) {
					parent.resume(null);
				} else {
					parent.resume(change);
				}
			} else {
				// I am a root transaction.  Forget my notifications, if any
				notifications = null;
				
				// closing a root transaction
				InternalLifecycle lifecycle = getLifecycle();
				if (lifecycle != null) {
					lifecycle.transactionClosed(this);
				}
			}
		}
	}
	
	// Documentation copied from the inherited specification
	@Override
	public void add(Notification notification) {
		if (!rollingBack && (notifications != null) && !notification.isTouch()) {
			notifications.add(notification);
		}
	}
	
	// Documentation copied from the inherited specification
	@Override
	public List<Notification> getNotifications() {
		return (notifications == null)? Collections.<Notification>emptyList()
			: notifications;
	}
	
	/**
	 * Validates me.  Should only be called during commit.
	 * 
	 * @return the result of validation.  If this is an error or worse,
	 *     then I must roll back
	 */
	protected IStatus validate() {
		return getInternalDomain().getValidator().validate(this);
	}
	
	// Documentation copied from the inherited specification
	@Override
	public Command getTriggers() {
		return triggers;
	}

	// Documentation copied from the inherited specification
	@Override
	public void addTriggers(TriggerCommand triggers) {
		// extract out only the triggered commands (exclude the triggering command)
		List<Command> triggerCommands = triggers.getTriggers();
		Command triggerCommand;
		
		if (triggerCommands.isEmpty()) {
			return;
		}
		
		// bug 165026:  Must copy the list, because it may be read-only like
		//    GMF's NOOP_TRIGGER command
		triggerCommand = new ConditionalRedoCommand.Compound(
				new java.util.ArrayList<Command>(triggerCommands));
		
		if (this.triggers == null) {
			this.triggers = triggerCommand;
			
			// record the triggers in my change description
			triggerChange = new CommandChangeDescription(triggerCommand);
			change.add(triggerChange);
		} else {
			this.triggers = triggerChange.chain(triggerCommand);
		}
	}
	
	// Documentation copied from the inherited specification
	@Override
	public void startPrivileged(PrivilegedRunnable<?> runnable) {
		if (runnable.getTransaction() != this) {
			throw new IllegalArgumentException(
					"runnable has no privileges on this transaction"); //$NON-NLS-1$
		}
		
		InternalTransactionalEditingDomain internalDomain =
			(InternalTransactionalEditingDomain) getEditingDomain();
		
		if (!isActive() || (internalDomain.getActiveTransaction() != this)) {
			throw new IllegalStateException(
					"transaction is not the domain's current transaction"); //$NON-NLS-1$
		}
		
		internalDomain.startPrivileged(runnable);
		
		owner = Thread.currentThread();
	}

	// Documentation copied from the inherited specification
	@Override
	public void endPrivileged(PrivilegedRunnable<?> runnable) {
		if (runnable.getTransaction() != this) {
			throw new IllegalArgumentException(
					"runnable has no privileges on this transaction"); //$NON-NLS-1$
		}
		
		InternalTransactionalEditingDomain internalDomain =
			(InternalTransactionalEditingDomain) getEditingDomain();
		
		if (!isActive() || (internalDomain.getActiveTransaction() != this)) {
			throw new IllegalStateException(
					"transaction is not the domain's current transaction"); //$NON-NLS-1$
		}
		
		owner = runnable.getOwner();
		
		internalDomain.endPrivileged(runnable);
	}
	
	private void inheritOptions(Transaction parent) {
	    // if we have no parent transaction, then we are a root and we "inherit"
	    // the editing domain's default transaction options.  In the root case,
		// we don't consider whether an option is hereditary because, of course,
		// we aren't actually inheriting it from any other transaction
		final boolean isRoot = parent == null;
		@SuppressWarnings("null")
        Map<?, ?> parentOptions = isRoot ?
        	getDefaultOptions(getEditingDomain()) : parent.getOptions();
        
        if (parentOptions != null) {
			Transaction.OptionMetadata.Registry reg = TransactionUtil
				.getTransactionOptionRegistry(getEditingDomain());
			
            for (Object option : parentOptions.keySet()) {
				reg.getOptionMetadata(option).inherit(parentOptions,
					mutableOptions, isRoot);
            }            
        }
	}
	
	@Override
	public String toString() {
		return "Transaction[active=" + isActive() //$NON-NLS-1$
			+ ", read-only=" + isReadOnly() //$NON-NLS-1$
			+ ", owner=" + getOwner().getName() + ']'; //$NON-NLS-1$
	}
	
	/**
	 * Queries whether the specified transaction should record undo information,
	 * according to its {@link Transaction#getOptions() options} and
	 * {@link Transaction#isReadOnly() read-only state}.
	 * 
	 * @param tx a transaction
	 * @return <code>true</code> if the transaction should record undo
	 *     information; <code>false</code>, otherwise
	 */
	protected static boolean isUndoEnabled(Transaction tx) {
		return !(tx.isReadOnly()
				|| hasOption(tx, OPTION_NO_UNDO)
				|| hasOption(tx, OPTION_UNPROTECTED));
	}
	
//	/**
//	 * Queries whether the specified transaction should validate changes,
//	 * according to its {@link Transaction#getOptions() options} and
//	 * {@link Transaction#isReadOnly() read-only state}.
//	 * 
//	 * @param tx a transaction
//	 * @return <code>true</code> if the transaction should validate
//	 *     changes; <code>false</code>, otherwise
//	 */
//	protected static boolean isValidationEnabled(Transaction tx) {
//		return !(tx.isReadOnly()
//				|| hasOption(tx, OPTION_NO_VALIDATION)
//				|| hasOption(tx, OPTION_UNPROTECTED));
//	}

	/**
	 * Queries whether the specified transaction should invoke pre-commit,
	 * listeners, according to its {@link Transaction#getOptions() options} and
	 * {@link Transaction#isReadOnly() read-only state}.
	 * 
	 * @param tx a transaction
	 * @return <code>true</code> if the transaction should perform the pre-commit
	 *     procedures; <code>false</code>, otherwise
	 */
	protected static boolean isTriggerEnabled(Transaction tx) {
		return !(tx.isReadOnly()
				|| hasOption(tx, OPTION_NO_TRIGGERS)
				|| hasOption(tx, OPTION_UNPROTECTED));
	}
	
	/**
	 * Queries whether the specified transaction should send post-commit events,
	 * according to its {@link Transaction#getOptions() options}.
	 * 
	 * @param tx a transaction
	 * @return <code>true</code> if the transaction should send post-commit
	 *     events; <code>false</code>, otherwise
	 */
	protected static boolean isNotificationEnabled(Transaction tx) {
		return !hasOption(tx, OPTION_NO_NOTIFICATIONS);
	}
	
	/**
	 * Queries whether the specified transaction is an unprotected write,
	 * according to its {@link Transaction#getOptions() options} and
	 * {@link Transaction#isReadOnly() read-only state}.
	 * 
	 * @param tx a transaction
	 * @return <code>true</code> if the transaction is an unprotected write
	 *     transaction; <code>false</code>, otherwise
	 */
	protected static boolean isUnprotected(Transaction tx) {
		return !tx.isReadOnly()
				&& hasOption(tx, OPTION_UNPROTECTED);
	}
	
	/**
	 * Queries whether the specified transaction collects notifications for broadcast to listeners or for validation. This is
	 * determined by the transaction's options.
	 * 
	 * @param tx
	 *            a transaction
	 * 
	 * @return <code>true</code> any of notification, triggers, and validation are enabled; <code>false</code>, otherwise
	 * 
	 * @see #isNotificationEnabled(Transaction)
	 * @see #isTriggerEnabled(Transaction)
	 */
	protected static boolean collectsNotifications(Transaction tx) {
		return isNotificationEnabled(tx) || isTriggerEnabled(tx);
		// || isValidationEnabled(tx);
	}
	
	/**
	 * Queries whether the specified transaction has a boolean option.
	 * 
	 * @param tx a transaction
	 * @param option the boolean-valued option to query
	 * @return <code>true</code> if the transaction has the option;
	 *    <code>false</code> if it does not
	 */
	protected static boolean hasOption(Transaction tx, String option) {
		return Boolean.TRUE.equals(tx.getOptions().get(option));
	}
	
	/**
	 * Obtains the default transaction options, if any, of the specified editing
	 * domain.
	 * 
	 * @param domain an editing domain
	 * @return its default transaction options, or an empty map if none are
	 *     defined
	 * 
	 * @since 1.2
	 */
	protected static Map<?, ?> getDefaultOptions(
			TransactionalEditingDomain domain) {
		
        TransactionalEditingDomain.DefaultOptions defaults = TransactionUtil
            .getAdapter(domain, TransactionalEditingDomain.DefaultOptions.class);
        
        return (defaults == null)? Collections.EMPTY_MAP
        	: defaults.getDefaultTransactionOptions();
    }
	
}
