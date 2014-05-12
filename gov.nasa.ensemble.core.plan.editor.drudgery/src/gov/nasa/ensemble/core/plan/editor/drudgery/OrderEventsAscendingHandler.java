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
package gov.nasa.ensemble.core.plan.editor.drudgery;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.common.ui.operations.MoveOperation;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.PlanMoveOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.preferences.PlanEditorPreferencePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.measure.unit.SI;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.jscience.physics.amount.Amount;

public class OrderEventsAscendingHandler extends AbstractHandler {

	public static final IPreferenceStore PREFERENCE_STORE = EditorPlugin.getDefault().getPreferenceStore();
	
	@Override
	public boolean isEnabled() {
		boolean parent =  super.isEnabled();
		// check if a plan is selected
		return parent && isPlanEditorCurrentEditor();
	}

	private boolean isPlanEditorCurrentEditor(){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window==null) return false;
		final IEditorPart editor = EditorPartUtils.getCurrent(window);
		if (editor==null) return false;
		final PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(editor.getEditorInput());
		if (model == null || !(editor instanceof MultiPagePlanEditor)){
			return  false;
		}
		return true;		
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		// check if a plan is selected
		final IEditorPart editor = EditorPartUtils.getCurrent(window );
		final PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(editor.getEditorInput());
		if (model == null || !(editor instanceof MultiPagePlanEditor)){
			MessageDialog.openInformation(window.getShell(), "Invalid Selection", "No plan is currently selected.");
			return null;
		}
		// get the plan from the part
		final EPlan plan = model.getEPlan();		
		editor.getTitleImage();
		runImpl((MultiPagePlanEditor) editor, plan);
		return null;
	}
	
	private EPlanElement getSelection(MultiPagePlanEditor editor){
		final ISelection selection = editor.getSite().getSelectionProvider().getSelection();
		if (selection == null || selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection structSelection = (IStructuredSelection)selection;
		// for now we'll just grab the first thing and get its parent
		Object o = structSelection.getFirstElement();
		if( o instanceof EPlanChild ){
			EPlanChild epc = (EPlanChild)o;
			return epc.getParent();
		}
		return null;
	}
	
	protected void runImpl(MultiPagePlanEditor editor, EPlan plan) {
		EPlanElement parent = getSelection(editor);
		if (parent==null){
			parent=plan;
		}
		List<EPlanChild> childList = new ArrayList<EPlanChild>();
		for (EPlanChild child : parent.getChildren()) {
			childList.add(child);
		}
		Collections.sort(childList, new ScheduledActivtyComparator());
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanTransferable transferable = new PlanTransferable();
		transferable.setPlanElements(childList);
		IStructureLocation location = modifier.getInsertionLocation(transferable, new StructuredSelection(parent), InsertionSemantics.ON);
		PlanEditorModel model = editor.getPlanEditorModel();
		MoveOperation operation = new PlanMoveOperation(transferable, modifier, location);
		IUndoContext undoContext = model.getUndoContext();
		operation.addContext(undoContext);
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.execute(operation, null, null);
		} catch (Exception e) {
			LogUtil.error(OrderEventsAscendingHandler.class.getName(), e);
		}
	}

	
	public static final class ScheduledActivtyComparator implements Comparator<EPlanChild> {
		@Override
		public int compare(EPlanChild planChild1, EPlanChild planChild2) {
			TemporalMember tm1 = planChild1.getMember(TemporalMember.class);
			TemporalMember tm2 = planChild2.getMember(TemporalMember.class);
			Date time1 = tm1.getExtent().getStart();
			Date time2 = tm2.getExtent().getStart();
			if (time1 == null) {
				return 1;
			}
			if (time2 == null) {
				return -1;
			}
			int result = time1.compareTo(time2);
// 			Needed to test if 2 times are the same in mars time, not sure what to do for other situations			
//			MarsLSTDateFormat f = new MarsLSTDateFormat();
//			String s1 = f.format(time1);
//			String s2 = f.format(time2);			
//			if( s1.equalsIgnoreCase(s2)){
//				result = 0;
//			}
			if (result == 0) {
				// hours to seconds
				long largeElementSeconds = PREFERENCE_STORE.getInt(PlanEditorPreferencePage.P_WATERFALL_LARGE_ELEMENT_DURATION_IN_HOURS) * 60 * 60;
				Amount largeElementSize = AmountUtils.toAmount( largeElementSeconds, SI.SECOND);
				boolean useEndTime1 = tm1.getDuration()!= null && tm1.getDuration().isGreaterThan(largeElementSize);
				boolean useEndTime2 = tm2.getDuration()!=null && tm2.getDuration().isGreaterThan(largeElementSize);
				if (useEndTime1 || useEndTime2) {
					time1 = tm1.getStartTime();
					time2 = tm2.getStartTime();
					result = time1.compareTo(time2);					
				}
				if (result == 0) {
					String name1 = planChild1.getName();
					String name2 = planChild2.getName();
					return name1.compareTo(name2);
				}
			}
			return result;	
		}
	}
}
