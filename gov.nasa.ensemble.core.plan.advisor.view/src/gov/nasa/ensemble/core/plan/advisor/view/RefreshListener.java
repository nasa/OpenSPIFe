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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.AdvisorListener;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;

public class RefreshListener extends AdvisorListener {

	/**
	 * 
	 */
	private final TreeViewer viewer;
	private final ISelectionChangedListener listener;
	private final EPlan plan;

	/**
	 * @param viewer
	 * @param listener
	 */
	public RefreshListener(TreeViewer viewer, EPlan plan, ISelectionChangedListener listener) {
		this.viewer = viewer;
		this.listener = listener;
		this.plan = plan;
	}

	private volatile boolean refreshQueued = false;

	@Override
	public void advisorsUpdating() {
		queueRefresh();
	}

	@Override
	public void advisorsUpdated() {
		queueRefresh();
	}

	@Override
	public void violationsAdded(Set<ViolationTracker> violations) {
		queueRefresh();
	}

	@Override
	public void violationsRemoved(Set<ViolationTracker> violations) {
		queueRefresh();
	}

	public synchronized void queueRefresh() {
		if (!refreshQueued) {
			refreshQueued = true;
			TransactionUtils.runInDisplayThread(viewer.getControl(), plan, new Runnable() {
				@Override
				public void run() {
					refreshQueued = false;
					viewer.refresh();
					listener.selectionChanged(null);
				}
			});
		}
	}

}
