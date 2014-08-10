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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.ui.timeline.policy.EditPolicyRegistry;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public abstract class TimelineViewerEditPart<T> extends AbstractGraphicalEditPart implements TimelineConstants {

	private TransactionalEditingDomain domain;

	/**
	 * Convenience method to cast the model
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T getModel() {
		return (T) super.getModel();
	}

	protected TransactionalEditingDomain getEditingDomain() {
		if (domain == null) {
			domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(getViewer().getModel());
		}
		return domain;
	}

	@Override
	public DragTracker getDragTracker(Request request) {
		return new TimelineViewerEditPartsTracker(this);
	}

	/**
	 * Removes all references from the <code>EditPartViewer</code> to this EditPart. This includes:
	 * <UL>
	 * <LI>deselecting this EditPart if selected
	 * <LI>setting the Viewer's focus to <code>null</code> if this EditPart has <i>focus</i>
	 * <LI>{@link #unregister()} this EditPart
	 * </UL>
	 * <P>
	 * In addition, <code>removeNotify()</code> is called recursively on all children EditParts. Subclasses should <em>extend</em> this method to perform any additional cleanup.
	 * 
	 * @see EditPart#removeNotify()
	 */
	@Override
	public void removeNotify() {
		TimelineViewer viewer = getViewer();
		if (getSelected() != SELECTED_NONE)
			viewer.removeNotify(this);
		if (hasFocus())
			viewer.setFocus(null);

		List children = getChildren();
		for (int i = 0; i < children.size(); i++)
			((EditPart) children.get(i)).removeNotify();
		unregister();
	}

	/**
	 * Method to restrict the model to the parameterized type
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setModel(Object model) {
		T casted = (T) model;
		super.setModel(casted);
	}

	@Override
	public RootEditPart getRoot() {
		if (getParent() == null) {
			return null;
		}
		return super.getRoot();
	}

	public final Timeline getTimeline() {
		TimelineViewer viewer = getViewer();
		return viewer == null ? null : viewer.getTimeline();
	}

	@Override
	public void addNotify() {
		register();
		createEditPolicies();
		EditPolicyRegistry.installEditPolicies(this);
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			EditPart editPart = (EditPart) children.get(i);
			try {
				editPart.addNotify();
			} catch (Exception e) {
				LogUtil.error("failed to addNotify an edit part", e);
			}
		}
		refresh();
	}

	/**
	 * Convenience method to retrieve the casted viewer.
	 */
	@Override
	public TimelineViewer getViewer() {
		RootEditPart root = getRoot();
		if (root == null) {
			return null;
		}
		return (TimelineViewer) root.getViewer();
	}

	@Override
	public Object getAdapter(Class key) {
		Object object = super.getAdapter(key);
		if (object == null) {
			TimelineViewer viewer = getViewer();
			if (viewer != null) {
				object = viewer.getAdapter(key);
			}
		}
		return object;
	}

	@Override
	protected void activateEditPolicies() {
		EditPolicyIterator i = getEditPolicyIterator();
		while (i.hasNext()) {
			EditPolicy policy = i.next();
			try {
				policy.activate();
			} catch (RuntimeException e) {
				Logger logger = Logger.getLogger(TimelineViewerEditPart.class);
				logger.error("policy.activate() failed: " + policy.getClass().getCanonicalName(), e);
			}
		}
	}

	@Override
	protected void deactivateEditPolicies() {
		EditPolicyIterator i = getEditPolicyIterator();
		while (i.hasNext()) {
			EditPolicy policy = i.next();
			try {
				policy.deactivate();
			} catch (RuntimeException e) {
				Logger logger = Logger.getLogger(TimelineViewerEditPart.class);
				logger.error("policy.deactivate() failed: " + policy.getClass().getCanonicalName(), e);
			}
		}
	}

	public boolean isScrollInhibited() {
		return false;
	}

	/*
	 * Utility methods
	 */
	protected final boolean getBoolean(String key) {
		return TimelineUtils.getBoolean(getViewer(), key);
	}

	/**
	 * Convenience method to run refresh in the display thread
	 */
	protected void refreshInDisplayThread() {
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
	}

	/**
	 * Convenience method to run refreshVisuals in the display thread
	 */
	protected void refreshVisualsInDisplayThread() {
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				refreshVisuals();
			}
		});
	}

	/**
	 * Convenience method to run refreshChildren in the display thread
	 */
	protected void refreshChildrenInDisplayThread() {
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				refreshChildren();
			}
		});
	}

}
