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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.core.plan.editor.timeline.commands.SnapToTimelineHandler;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.ui.timeline.InfobarComposite;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartSite;

public class PlanTimeline extends Timeline<EPlan> implements PlanTimelineConstants {

	private PlanEditorModel planEditorModel;
	private MoveThread moveThread;

	/** Update mechanism for the info bar */
	private InfobarUpdater infobarUpdater;

	/** Snap tolerance for the node snapping to its neighbor */
	private int snapToTolerance;

	/** Special snap mode introduced for LASS in August 2011 */
	private boolean snapToOrbitEnabled = false;

	private List<PlanTimelineService> timelineServices = null;

	private TransactionalEditingDomain domain = null;

	public PlanTimeline() {
		super();
	}

	public PlanTimeline(ETimeline timelineModel) {
		super(timelineModel);
	}

	public EPlan getPlan() {
		if (planEditorModel != null) {
			return planEditorModel.getEPlan();
		}
		return null;
	}
	
	public MoveThread getMoveThread() {
		return moveThread;
	}
	
	/**
	 * Sets the site and input for this editor then creates and initializes the actions.
	 * Subclasses may extend this method, but should always call <code>super.init(site, input)
	 * </code>.
	 * @see org.eclipse.ui.IEditorPart#init(IEditorSite, IEditorInput)
	 */
	public void init(IWorkbenchPartSite site, PlanEditorModel planEditorModel) {
		super.init(site, planEditorModel.getEPlan());
		this.planEditorModel = planEditorModel;
		EPlan ePlan = planEditorModel.getEPlan();
		this.domain = TransactionUtils.getDomain(ePlan);
		this.moveThread = new MoveThread(ePlan, this);
		IPreferenceStore store = TimelinePlugin.getDefault().getPreferenceStore();
		if (store.getBoolean(TimelinePreferencePage.P_SNAP_TO_ACTIVE)) {
			setSnapTolerance(SnapToTimelineHandler.DEFAULT_TOLERANCE);
		}
		//FIXME:  Make stores consistent.  See also SnapToAssessment.isEnabled().
		setSnapToOrbitEnabled(TimelineConstants.TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_SNAP_TO_ORBIT_ACTIVE));
		timelineServices = ClassRegistry.createInstances(PlanTimelineService.class);
		for (PlanTimelineService s : timelineServices) {
			s.setTimeline(this);
		}
	}

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		InfobarComposite infobarComposite = getInfobarComposite();
		if (infobarComposite != null) {
			infobarUpdater = new InfobarUpdater(infobarComposite);
		}
		for (PlanTimelineService service : timelineServices) {
			try {
				service.activate();
			} catch (RuntimeException e) {
				LogUtil.error("service.activate() failed: " + service.getClass().getCanonicalName(), e);
			}
		}
		moveThread.start();
	}

	public TransactionalEditingDomain getEditingDomain() {
		return domain;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		for (PlanTimelineService service : timelineServices) {
			try {
				service.deactivate();
			} catch (Exception e) {
				LogUtil.error("service.deactivate() failed: " + service.getClass().getCanonicalName(), e);
			}
		}
		timelineServices = Collections.emptyList();
		moveThread.quit();
		super.dispose();
		//TODO: dispose timelineMarkerManager
	}

	/*
	 * Properties access
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if (IResource.class == adapter) {
			Plan plan = getPlanEditorModel().getPlan();
			IResource resource = (IResource)plan.getAdapter(IResource.class);
			return resource;
		}
		return super.getAdapter(adapter);
	}

	public PlanEditorModel getPlanEditorModel() {
		return planEditorModel;
	}

	public void setFocus() {
		getControl().setFocus();
	}

	public InfobarUpdater getInfobarUpdater() {
		return infobarUpdater;
	}

	public void setSnapTolerance(int pixels) {
		snapToTolerance = pixels;
	}

	public int getSnapToTolerance() {
		return snapToTolerance;
	}
	
	public void setSnapToOrbitEnabled(boolean value) {
		snapToOrbitEnabled = value;
	}

	public boolean getSnapToOrbitEnabled() {
		return snapToOrbitEnabled;
	}

	/*
	 * Utility functions
	 * 
	 * @deprecated get the timeline from your local context instead
	 */
	@Deprecated
	public static PlanTimeline getCurrent()
	{
		@SuppressWarnings("deprecation") // use of deprecated method by deprecated method should be okay
		IEditorPart editor = EditorPartUtils.getCurrent();
		if (!(editor instanceof MultiPagePlanEditor)) {
			return null;
		}

		TimelineEditorPart timelineEditor = null;
		MultiPagePlanEditor mppe = (MultiPagePlanEditor) editor;
		for (int i=0; i<mppe.getPageCount() && timelineEditor == null; i++) {
			IEditorPart part = mppe.getEditor(i);
			if (part instanceof TimelineEditorPart) {
				timelineEditor = (TimelineEditorPart) part;
				return timelineEditor.getTimeline();
			}
		}
		return null;
	}

}
