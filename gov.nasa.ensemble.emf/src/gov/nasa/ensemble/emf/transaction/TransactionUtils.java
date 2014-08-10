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

import static fj.data.Option.some;

import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.internal.jobs.JobManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor.Registry;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.transaction.internal.EMFTransactionPlugin;
import org.eclipse.emf.transaction.internal.EMFTransactionStatusCodes;
import org.eclipse.emf.transaction.internal.Tracing;
import org.eclipse.emf.transaction.internal.l10n.Messages;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import fj.data.Option;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

@SuppressWarnings("restriction")
public class TransactionUtils {

	private static final Map<String, Boolean> WRITING_OPTIONS = new HashMap<String, Boolean>();
	static {
		WRITING_OPTIONS.put(Transaction.OPTION_NO_UNDO, Boolean.TRUE);
		WRITING_OPTIONS.put(Transaction.OPTION_NO_VALIDATION, Boolean.TRUE);
	}
	private static final JobManager JOB_MANAGER = (JobManager) Job.getJobManager();
	private static final ThreadMXBean THREAD_MX_BEAN = ManagementFactory.getThreadMXBean();

	private static final TransactionEditingDomainFactory EDITING_DOMAIN_FACTORY = createTransactionEditingDomainFactory();

	public static void reading(Object object, final Runnable runnable) {
		reading(object, new RunnableWithResult.Impl<Object>() {
			@Override
			public void run() {
				runnable.run();
			}

			@Override
			public String toString() {
				return runnable.toString();
			}
		});
	}

	public static <T> T reading(Object object, final RunnableWithResult<T> runnable) {
		TransactionalEditingDomain domain = getDomain(object);
		if (domain == null) { // not connected yet
			runnable.run();
			return runnable.getResult();
		}
		try {
			checkForDeadlock(domain);
			return runExclusive(domain, runnable);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writing(Object object, final Runnable runnable) {
		writing(object, new WritingWithVoidResult(runnable));
	}

	public static void checkedWriting(Object object, final Runnable runnable) throws InterruptedException, RollbackException {
		checkedWriting(object, new WritingWithVoidResult(runnable));
	}

	public static void writeIfNecessary(Object object, final Runnable runnable) {
		TransactionalEditingDomain domain = TransactionUtils.getDomain(object);
		if (domain != null) {
			writing(domain, runnable);
		} else {
			runnable.run();
		}
	}

	public static <T extends Throwable> void writing(final Object object, final Class<T> throwableClass, final RunnableWithThrowable runnable) throws T {
		final Option<T> exception = TransactionUtils.writing(object, new RunnableWithResult.Impl<Option<T>>() {
			@Override
			public void run() {
				try {
					runnable.run();
					setResult(Option.<T> none());
				} catch (Throwable throwable) {
					if (throwableClass.isAssignableFrom(throwable.getClass())) {
						setResult(some((T) throwable));
						return;
					}
					if (throwable instanceof RuntimeException)
						throw (RuntimeException) throwable;
					if (throwable instanceof Error)
						throw (Error) throwable;
					throw new RuntimeException(throwable);
				}
			}
		});
		if (exception.isSome())
			throw exception.some();
	}

	public static <T> T writing(Object object, final RunnableWithResult<T> runnable) {
		try {
			return checkedWriting(object, runnable);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (RollbackException e) {
			throw handleRollback(e);
		}
	}

	public static <T> T checkedWriting(Object object, final RunnableWithResult<T> runnable) throws InterruptedException, RollbackException {
		return checkedWriting(object, runnable, null);
	}

	public static <T> T checkedWriting(Object object, final RunnableWithResult<T> runnable, Map<Object, Object> options) throws InterruptedException, RollbackException {

		final EditingDomain domain = EMFUtils.getAnyDomain(object);
		if (domain instanceof TransactionalEditingDomain) {
			TransactionalEditingDomain transactionDomain = (TransactionalEditingDomain) domain;
			checkForDeadlock(transactionDomain);
			InternalTransaction transaction = ((TransactionalEditingDomainImpl) transactionDomain).getActiveTransaction();
			if ((transaction != null) && (transaction.getOwner() == Thread.currentThread()) && !transaction.isReadOnly()) {
				// nested writing
				runnable.run();
				return runnable.getResult();
			}
			TransactionalCommandStack stack = (TransactionalCommandStack) domain.getCommandStack();
			if (stack != null) {
				Map writingOptions = getWritingOptions(domain);
				Map newOptions = new HashMap();
				if (options != null) {
					newOptions.putAll(options);
				}
				if (writingOptions != null) {
					newOptions.putAll(writingOptions);
				}
				writingOptions = newOptions;
				stack.execute(new RunnableRecordingCommand("writing", runnable), writingOptions);
			} else {
				runnable.run();
			}
		} else {
			runnable.run();
		}
		return runnable.getResult();
	}

	private static Map<?, ?> getWritingOptions(EditingDomain domain) {
		if (domain instanceof TransactionalEditingDomainImpl)
			return ((TransactionalEditingDomainImpl) domain).getDefaultTransactionOptions();
		return WRITING_OPTIONS;
	}

	public static TransactionalEditingDomain getDomain(Object object) {
		EditingDomain domain = EMFUtils.getAnyDomain(object);
		if (domain instanceof TransactionalEditingDomain) {
			return (TransactionalEditingDomain) domain;
		}
		return null;
	}

	public static IUndoContext getUndoContext(Object object) {
		EditingDomain d = EMFUtils.getAnyDomain(object);
		if (d instanceof FixedTransactionEditingDomain) {
			FixedTransactionEditingDomain domain = (FixedTransactionEditingDomain) d;
			return domain.getUndoContext();
		}
		if (object instanceof EObject) {
			return EMFUtils.getUndoContext((EObject) object, d);
		}
		return null;
	}

	public static TransactionalEditingDomain createTransactionEditingDomain() {
		return createTransactionEditingDomain(true);
	}

	public static TransactionalEditingDomain createTransactionEditingDomain(Map<?, ?> txOptions) {
		return createTransactionEditingDomain(true, txOptions);
	}

	public static TransactionalEditingDomain createTransactionEditingDomain(boolean addListener) {
		return createTransactionEditingDomain(addListener, WRITING_OPTIONS);
	}

	public static TransactionalEditingDomain createTransactionEditingDomain(boolean addListener, Map<?, ?> txOptions) {
		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactoryImpl(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		FixedTransactionEditingDomain domain = EDITING_DOMAIN_FACTORY.createEditingDomain(composedAdapterFactory);
		domain.setDefaultTransactionOptions(txOptions);
		if (addListener) {
			ExtensionPointResourceSetListener.addListener(domain);
		}
		ResourceSet resourceSet = domain.getResourceSet();
		resourceSet.getAdapterFactories().add(composedAdapterFactory);
		return domain;
	}

	public static ResourceSet createTransactionResourceSet() {
		return createTransactionResourceSet(true);
	}

	public static ResourceSet createTransactionResourceSet(boolean addListener, Map<?, ?> txOptions) {
		return createTransactionEditingDomain(addListener, txOptions).getResourceSet();
	}

	public static ResourceSet createTransactionResourceSet(boolean addListener) {
		TransactionalEditingDomain domain = createTransactionEditingDomain(addListener);
		return domain.getResourceSet();
	}

	/**
	 * This constructor will create a consistency only resource set.
	 * 
	 * @param ePackage
	 * @param defaultResourceFactory
	 * @return ResourceSet
	 */
	public static ResourceSet createTransactionResourceSet(EPackage ePackage, Resource.Factory defaultResourceFactory) {
		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		TransactionalEditingDomain domain = EDITING_DOMAIN_FACTORY.createEditingDomain(composedAdapterFactory);
		ExtensionPointResourceSetListener.addListener(domain, true);
		ResourceSet resourceSet = domain.getResourceSet();
		resourceSet.getAdapterFactories().add(composedAdapterFactory);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, defaultResourceFactory);
		resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		return resourceSet;
	}

	public static <T> T runExclusive(TransactionalEditingDomain d, Runnable read) throws InterruptedException {
		InternalTransactionalEditingDomain domain = (InternalTransactionalEditingDomain) d;
		Transaction active = domain.getActiveTransaction();
		Transaction tx = null;
		long start = -1;
		if ((active == null) || !(active.isActive() && active.isReadOnly() && (active.getOwner() == Thread.currentThread()))) {
			start = System.currentTimeMillis();
			// only need to start a new transaction if we don't already have
			// exclusive read-only access
			tx = domain.startTransaction(true, getWritingOptions(domain));
		}
		final RunnableWithResult<?> rwr = (read instanceof RunnableWithResult) ? (RunnableWithResult<?>) read : null;
		try {
			read.run();
		} finally {
			if ((tx != null) && (tx.isActive())) {
				// commit the transaction now
				try {
					tx.commit();
					if (rwr != null) {
						rwr.setStatus(Status.OK_STATUS);
					}
				} catch (RollbackException e) {
					Tracing.catching(TransactionalEditingDomainImpl.class, "runExclusive", e); //$NON-NLS-1$
					EMFTransactionPlugin.INSTANCE.log(new MultiStatus(EMFTransactionPlugin.getPluginId(), EMFTransactionStatusCodes.READ_ROLLED_BACK, new IStatus[] { e.getStatus() }, Messages.readTxRollback, null));
					if (rwr != null) {
						rwr.setStatus(e.getStatus());
					}
				}
			}
		}
		if (start != -1) {
			long duration = System.currentTimeMillis() - start;
			if (duration > 2 * 1000) {
				LogUtil.warn("runnable " + read + " held transaction for " + (duration / 1000.0) + " seconds");
			}
		}
		if (rwr != null) {
			@SuppressWarnings("unchecked")
			T result = (T) rwr.getResult();
			return result;
		}
		return null;
	}

	/*
	 * Utility methods
	 */

	private static RuntimeException handleRollback(RollbackException e) {
		Logger logger = Logger.getLogger(TransactionUtils.class);
		IStatus status = e.getStatus();
		switch (status.getCode()) {
		case IStatus.ERROR:
			logger.error(status.getMessage(), status.getException());
			break;
		case IStatus.WARNING:
			logger.warn(status.getMessage(), status.getException());
			break;
		case IStatus.INFO:
			logger.info(status.getMessage(), status.getException());
			break;
		}

		final Throwable exception = Option.fromNull(status.getException()).orSome(e);
		if (exception instanceof RuntimeException)
			return (RuntimeException) exception;

		return new RuntimeException(exception);
	}

	private static void checkForDeadlock(EditingDomain domain) {
		try {
			// I am the display thread
			if (domain instanceof InternalTransactionalEditingDomain) {
				InternalTransactionalEditingDomain internalDomain = (InternalTransactionalEditingDomain) domain;
				InternalTransaction activeTransaction = internalDomain.getActiveTransaction();
				if (activeTransaction != null) {
					// There is already an active transaction
					Thread owner = activeTransaction.getOwner();
					Thread currentThread = Thread.currentThread();
					if (owner != currentThread) {
						// I'm going to block/run display runnable
						if (owner.getState() == State.BLOCKED) {
							checkForBasicDeadlock();
							// The transaction owner is blocked too
							long ownerId = owner.getId();
							ThreadInfo threadInfo = null;
							long nextId = ownerId;
							while (nextId != -1) {
								threadInfo = THREAD_MX_BEAN.getThreadInfo(nextId, 2);
								if (threadInfo == null) {
									break; // the thread being blocked on has terminated
								}
								nextId = threadInfo.getLockOwnerId();
								if (nextId == ownerId) {
									String message = "the transaction is held by a deadlock 2";
									Logger.getLogger(TransactionUtils.class).error(message);
								}
							}
							checkForThreadJobJoinRun(threadInfo);
						}
					}
				}
			}
		} catch (RuntimeException e) {
			Logger.getLogger(TransactionUtils.class).error("exception in TransactionUtils", e);
		}
	}

	private static void checkForBasicDeadlock() {
		long[] threads = THREAD_MX_BEAN.findMonitorDeadlockedThreads();
		if (threads != null) {
			String message = "the transaction is held by a deadlock 1";
			Logger.getLogger(TransactionUtils.class).error(message);
		}
	}

	private static void checkForThreadJobJoinRun(ThreadInfo threadInfo) {
		if ((threadInfo != null) && !JOB_MANAGER.isIdle()) {
			StackTraceElement[] stack = threadInfo.getStackTrace();
			if (stack.length == 2) {
				String className = stack[1].getClassName();
				String methodName = stack[1].getMethodName();
				if (className.equals("org.eclipse.core.internal.jobs.ThreadJob") && methodName.equals("joinRun")) {
					String message = "the transaction is held by a deadlock 3";
					Logger.getLogger(TransactionUtils.class).error(message);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void transferDomainListeners(TransactionalEditingDomain from, TransactionalEditingDomain to) {
		List<ResourceSetListener> precommitListeners = (List<ResourceSetListener>) ReflectionUtils.get(from, "precommitListeners");
		List<ResourceSetListener> aggregatePrecommitListeners = (List<ResourceSetListener>) ReflectionUtils.get(from, "aggregatePrecommitListeners");
		List<ResourceSetListener> postcommitListeners = (List<ResourceSetListener>) ReflectionUtils.get(from, "postcommitListeners");
		synchronized (precommitListeners) {
			for (ResourceSetListener listener : precommitListeners) {
				to.addResourceSetListener(listener);
			}
		}
		synchronized (aggregatePrecommitListeners) {
			for (ResourceSetListener listener : aggregatePrecommitListeners) {
				to.addResourceSetListener(listener);
			}
		}
		synchronized (postcommitListeners) {
			for (ResourceSetListener listener : postcommitListeners) {
				to.addResourceSetListener(listener);
			}
		}
	}

	public static void transferDomainListeners(ResourceSet from, ResourceSet to) {
		transferDomainListeners(getDomain(from), getDomain(to));
	}

	/**
	 * If this domain is a FixedTransactionEditingDomain, return the current transaction. If writing is true, then throw an illegal state exception if there is already a current transaction.
	 * 
	 * @param d
	 * @param writing
	 * @return
	 */
	public static InternalTransaction checkTransaction(TransactionalEditingDomain d, boolean writing) {
		InternalTransaction existingTransaction = null;
		if (d instanceof FixedTransactionEditingDomain) {
			FixedTransactionEditingDomain domain = (FixedTransactionEditingDomain) d;
			existingTransaction = domain.getThreadTransaction();
		}
		if ((existingTransaction != null) && writing) {
			throw new IllegalStateException("can't start a transaction while handling UI events for a suspended transaction");
		}
		return existingTransaction;
	}

	private static final class ComposedAdapterFactoryImpl extends ComposedAdapterFactory {

		private ComposedAdapterFactoryImpl(Registry adapterFactoryDescriptorRegistry) {
			super(adapterFactoryDescriptorRegistry);
			changeNotifier = new ChangeNotifier() {

				@Override
				public void fireNotifyChanged(Notification notification) {
					int size = size();
					INotifyChangedListener[] listeners = new INotifyChangedListener[size];
					toArray(listeners);
					int expectedModCount = modCount;
					for (int i = 0; i < size; ++i) {
						INotifyChangedListener notifyChangedListener = listeners[i];
						if (notifyChangedListener == null) {
							continue;
						}
						if (expectedModCount == modCount || this.contains(notifyChangedListener)) {
							notifyChangedListener.notifyChanged(notification);
						}
					}
				}

			};
		}

		@Override
		public void dispose() {
			super.dispose();
			// SPF-12117 -- These two fields were holding onto plan structure after a plan editor is closed
			adapterFactories.clear();
			changeNotifier = null;
		}

		@Override
		public void addListener(INotifyChangedListener notifyChangedListener) {
			if (changeNotifier != null) {
				super.addListener(notifyChangedListener);
			}
		}

		@Override
		public void removeListener(INotifyChangedListener notifyChangedListener) {
			if (changeNotifier != null) {
				super.removeListener(notifyChangedListener);
			}
		}

		@Override
		public void fireNotifyChanged(Notification notification) {
			if (changeNotifier != null) {
				super.fireNotifyChanged(notification);
			}
		}

	}

	private static final class WritingWithVoidResult extends RunnableWithResult.Impl<Object> {
		private final Runnable runnable;

		private WritingWithVoidResult(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void run() {
			runnable.run();
		}
	}

	private static TransactionEditingDomainFactory createTransactionEditingDomainFactory() {
		try {
			return MissionExtender.construct(TransactionEditingDomainFactory.class);
		} catch (ConstructionException e) {
			LogUtil.error(e);
		}
		return new TransactionEditingDomainFactory();
	}

	/**
	 * Run the runnable in the display thread while holding the read lock for the object. The runnable will be queued to run asynchronously when the lock becomes available, or the runnable will run
	 * synchronously if this thread is the display thread and the lock is already held.
	 * 
	 * @param control
	 * @param object
	 * @param runnable
	 */
	public static void runInDisplayThread(Control control, Object object, Runnable runnable) {
		if (control.isDisposed())
			return;
		Display display = control.getDisplay();
		if ((display != null) && (object != null) && !display.isDisposed()) {
			TransactionalEditingDomain domain = getDomain(object);
			if (domain instanceof FixedUITransactionEditingDomain) {
				FixedUITransactionEditingDomain fixed = (FixedUITransactionEditingDomain) domain;
				InternalTransaction active = fixed.getActiveTransaction();
				if (((display.getThread() != Thread.currentThread()) || (active == null) || !(active.isActive() && active.isReadOnly() && (active.getOwner() == Thread.currentThread())))) {
					fixed.queueReading(control, runnable);
				} else {
					runnable.run();
				}
			} else if (domain != null) {
				gov.nasa.ensemble.emf.transaction.TransactionUtils.reading(object, runnable);
			} else {
				LogUtil.warn("failed to get the transaction domain for runnable: " + runnable);
			}
		}
	}
}
