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
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.operation.OperationCanceledException;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.transaction.impl.TransactionChangeRecorder;

public class UndoableObservableValue extends AbstractObservableValue implements IObserving {

	private EObject target;
	private IItemPropertyDescriptor pd;
	private Adapter listener;

	public UndoableObservableValue(EObject object, IItemPropertyDescriptor pd) {
		super(Realm.getDefault());
		this.target = object;
		this.pd = pd;
	}

	@Override
	public synchronized void dispose() {
		disposeListener();
		target = null;
		pd = null;
	}

	@Override
	public Object getObserved() {
		return target;
	}

	@Override
	public Object getValueType() {
		return pd.getFeature(target);
	}

	@Override
	protected void firstListenerAdded() {
		final Object eStructuralFeature = pd.getFeature(target);
		attachListener(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification notification) {
				if (eStructuralFeature == notification.getFeature() && !notification.isTouch()) {
					final ValueDiff diff = Diffs.createValueDiff(notification.getOldValue(), notification.getNewValue());
					getRealm().exec(new Runnable() {
						@Override
						public void run() {
							fireValueChange(diff);
						}
					});
				}
			}
		});
	}

	private void attachListener(AdapterImpl adapter) {
		listener = adapter;
		if (pd instanceof MultiItemPropertyDescriptor) {
			MultiItemPropertyDescriptor descriptor = (MultiItemPropertyDescriptor) pd;
			List<EObject> owners = descriptor.getCommandOwners();
			if (!owners.isEmpty()) {
				for (EObject owner : owners) {
					owner.eAdapters().add(listener);
				}
				return;
			}
		}
		EObject owner = EMFDetailUtils.getCommandOwner(pd, target);
		if (owner != null) {
			owner.eAdapters().add(listener);
			return;
		}
		target.eAdapters().add(listener);
	}

	private void disposeListener() {
		if (listener != null) {
			if (pd instanceof MultiItemPropertyDescriptor) {
				MultiItemPropertyDescriptor descriptor = (MultiItemPropertyDescriptor) pd;
				List<EObject> owners = descriptor.getCommandOwners();
				if (!owners.isEmpty()) {
					for (EObject owner : owners) {
						owner.eAdapters().remove(listener);
					}
					return;
				}
			}
			EObject owner = EMFDetailUtils.getCommandOwner(pd, target);
			if (owner != null) {
				owner.eAdapters().remove(listener);
				return;
			}
			target.eAdapters().remove(listener);
		}
	}

	@Override
	protected void doSetValue(final Object value) {
		Object oldValue = doGetValue();
		if (CommonUtils.equals(value, oldValue)) {
			return; // nothing to do
		}
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		String name = EMFUtils.getDisplayName(target, feature);
		IUndoableOperation operation = new ChangeObservableOperation("Edit " + name, value);
		EObject observed = (EObject) getObserved();
		operation = EMFUtils.addContributorOperations(operation, observed, feature, oldValue, value);
		IUndoContext context = gov.nasa.ensemble.emf.transaction.TransactionUtils.getUndoContext(observed);
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		IUndoableOperation previous = history.getUndoOperation(context);
		if (previous instanceof TextModifyUndoableObservableValue.TextModifyObservableOperation) {
			// Reset the TextModifyObservableOperation to clear the dirty flag on its TextModifyUndoableObservableValue
			((TextModifyUndoableObservableValue.TextModifyObservableOperation) previous).reset();
			// Remove the TextModifyObservableOperation operation from the operation history as it should be replaced by the new operation
			history.replaceOperation(previous, new IUndoableOperation[0]);
		}
		CommonUtils.execute(operation, context);
	}

	@Override
	protected Object doGetValue() {
		return EMFUtils.getPropertyValue(pd, target);
	}

	public final class ChangeObservableOperation extends AbstractTransactionUndoableOperation {
		private Object oldValue;
		private final Object newValue;
		private CompoundCommand command;

		private ChangeObservableOperation(String label, Object newValue) {
			super(label);
			this.newValue = newValue;
		}

		@Override
		protected void dispose(UndoableState state) {
			// nothing to dispose
		}

		@Override
		protected void execute() throws Throwable {
			execute(new NullProgressMonitor());
		}

		@Override
		protected void execute(IProgressMonitor monitor) throws Throwable {
			if (pd instanceof MultiItemPropertyDescriptor) {
				this.oldValue = ((MultiItemPropertyDescriptor) pd).getValues();
			} else {
				this.oldValue = doGetValue();
			}
			if (CommonUtils.equals(oldValue, newValue)) {
				// We get multiple operations per change sometimes
				// due to the way data binding works?
				// Just ignore this one so we don't get multiple
				//
				throw new OperationCanceledException();
			}
			doit(monitor);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void undo() throws Throwable {
			if (command != null) {
				Object observed = getObserved();
				EditingDomain domain = EMFUtils.getAnyDomain(observed);
				domain.getCommandStack().execute(new AbstractCommand() {
					@Override
					protected boolean prepare() {
						return true;
					}

					@Override
					public void execute() {
						command.undo();
					}

					@Override
					public void undo() {
						command.redo();
					}

					@Override
					public void redo() {
						command.undo();
					}
				});
			} else if (pd instanceof MultiItemPropertyDescriptor) {
				((MultiItemPropertyDescriptor) pd).setValues((Collection<Object>) oldValue);
			} else {
				pd.setPropertyValue(getObserved(), oldValue);
			}
		}

		@Override
		protected void redo() throws Throwable {
			if (command != null) {
				Object observed = getObserved();
				EditingDomain domain = EMFUtils.getAnyDomain(observed);
				domain.getCommandStack().execute(new AbstractCommand() {
					@Override
					protected boolean prepare() {
						return true;
					}

					@Override
					public void execute() {
						command.redo();
					}

					@Override
					public void undo() {
						command.undo();
					}

					@Override
					public void redo() {
						command.redo();
					}
				});
			} else {
				doit(new NullProgressMonitor());
			}
		}

		private void doit(IProgressMonitor monitor) {
			final Object observed = getObserved();
			if (isUnparented(observed)) { // MSLICE-765
				LogUtil.warn("Prevented attempt to set property on object without a Resource but with a TransactionChangeRecorder listening.");
			} else {
				EditingDomain domain = EMFUtils.getAnyDomain(observed);
				CommandStackListener listener = null;
				try {
					if (domain != null) {
						final Thread thread = Thread.currentThread();
						final CommandStack stack = domain.getCommandStack();
						command = new CompoundCommand(getLabel());
						listener = new CommandStackListener() {
							@Override
							public void commandStackChanged(EventObject event) {
								if (thread == Thread.currentThread()) {
									command.append(stack.getMostRecentCommand());
								}
							}
						};
						stack.addCommandStackListener(listener);
					}
					pd.setPropertyValue(observed, newValue);
				} finally {
					if (domain != null) {
						domain.getCommandStack().removeCommandStackListener(listener);
					}
				}
			}
		}

		private boolean isUnparented(final Object observed) {
			if (observed instanceof EObject) {
				final EObject eObserved = (EObject) observed;
				if (eObserved.eResource() == null) {
					EList<Adapter> eAdapters = eObserved.eAdapters();
					for (final Adapter adapter : eAdapters) {
						if (adapter instanceof TransactionChangeRecorder) {
							return true;
						}
					}
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return "oldValue='" + oldValue + "', newValue='" + newValue + "'";
		}

	}

}
