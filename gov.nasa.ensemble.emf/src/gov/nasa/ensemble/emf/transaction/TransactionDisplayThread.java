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

import gov.nasa.ensemble.common.collections.Pair;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class TransactionDisplayThread extends Thread {

	private final Object queueLock = new int[0];
	private final Queue<Pair<Control, Runnable>> writingQueue = new LinkedList<Pair<Control, Runnable>>();
	private final Queue<Pair<Control, Runnable>> readingQueue = new LinkedList<Pair<Control, Runnable>>();
	private volatile boolean quit = false;
	private TransactionalEditingDomain domain;
	
	public TransactionDisplayThread(TransactionalEditingDomain domain) {
		super(TransactionDisplayThread.class.getSimpleName());
		this.domain = domain;
	}
	
	private void dispose() {
		domain = null;
		writingQueue.clear();
		readingQueue.clear();
	}

	@Override
	public void run() {
		try {
			while (!quit) {
				try {
					step();
				} catch (OutOfMemoryError e) {
					LogUtil.error(e);
				}
			}
		} catch (IllegalArgumentException e) {
			if (!"Can only deactivate the active transaction".equals(e.getMessage())) {
				throw e;
			}
		} finally {
			dispose();
		}
	}

	private void step() {
		boolean reading;
		Pair<Control, Runnable> pair;
		synchronized (queueLock) {
			while (readingQueue.isEmpty() && writingQueue.isEmpty()) {
				try {
					queueLock.wait();
				} catch (InterruptedException e) {
					// fall out and check status
				}
				if (quit) {
					return;
				}
			}
			pair = readingQueue.poll();
			reading = (pair != null);
			if (!reading) {
				pair = writingQueue.poll();
			}
		}
		if (pair == null) {
			return;
		}
		Control control = pair.getLeft();
		if ((control == null) || control.isDisposed()) {
			return;
		}
		Runnable runnable = pair.getRight();
		if (reading) {
			gov.nasa.ensemble.emf.transaction.TransactionUtils.reading(domain, displayRunnable(control, runnable));
		} else {
			gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(domain, displayRunnable(control, runnable));
		}
	}

	private Runnable displayRunnable(final Control control, final Runnable runnable) {
		return new Runnable() {
			@Override
			public void run() {
				if (control.isDisposed()) {
					return;
				}
				Display display = control.getDisplay();
				if (display.isDisposed()) {
					return;
				}
				final RunnableWithResult<?> privilegedRunnable = domain.createPrivilegedRunnable(runnable);
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						if (!control.isDisposed()) {
							privilegedRunnable.run();
						}
					}
				});
			}
			@Override
			public String toString() {
				return runnable.toString();
			}
		};
	}

	public void quit() {
		quit = true;
		synchronized (queueLock) {
			dispose();
			queueLock.notify();
		}
		interrupt();
	}
	
	public void queueWriting(Control control, Runnable runnable) {
		if (quit) {
			return;
		}
		lazyStart();
		synchronized (queueLock) {
			writingQueue.add(new Pair(control, runnable));
			queueLock.notify();
		}
	}

	public void queueReading(Control control, Runnable runnable) {
		if (quit) {
			return;
		}
		lazyStart();
		synchronized (queueLock) {
			readingQueue.add(new Pair(control, runnable));
			queueLock.notify();
		}
	}
	
	private void lazyStart() {
		if (!isAlive()) {
			try {
				start();
			} catch (IllegalThreadStateException e) {
				// rare chance that this occurs, and we don't care
			}
		}
	}
	
	
}
