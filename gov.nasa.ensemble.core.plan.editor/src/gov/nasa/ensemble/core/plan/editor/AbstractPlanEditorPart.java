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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorPart;
import gov.nasa.ensemble.common.ui.operations.AbstractUndoableOperationAction;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.editor.context.PlanEditorContextMenuBuilder;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;

public abstract class AbstractPlanEditorPart extends AbstractEnsembleEditorPart {

	protected final AbstractUndoableOperationAction addGroupHandler = new PlanEditorUndoableOperationAction(PlanActionFactory.ADD_GROUP.getId()); 
	private EPlan plan;
	private IUndoContext undoContext;
	
	public AbstractPlanEditorPart() {
		String string = "Add " + EPlanUtils.getActivityGroupDisplayName();
		addGroupHandler.setText(string);
		addGroupHandler.setToolTipText(string);
	}
	
	/** Override in subclass. It serves as the extension for configuration files and 
	 * for setting configuration preferences in ensemble.properties.
	 * @return
	 */
	public String getId() {
		return "editor";
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!PlanEditorModelRegistry.isAcceptable(input)) {
			throw new PartInitException("unacceptable input for plan model");
		}
		super.init(site, input);
	}


	@Override
	public void dispose() {
	    super.dispose();
	    addGroupHandler.dispose();
	    plan = null;
	    undoContext = null;
	}
	
	@Override
	protected void attachModel(IEditorInput input, IWorkbenchPartSite site) {
		PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
		plan = model.getEPlan();
		undoContext = model.getUndoContext();
	    super.attachModel(input, site);
		addGroupHandler.init(site, undoContext);
		addGroupHandler.updateEnablement();
	}
	
    public EPlan getPlan() {
	    return plan;
    }
    
    @Override
    public IUndoContext getUndoContext() {
        return undoContext;
    }
	
	@Override
	public void updateActionBars(IActionBars bars) {
		super.updateActionBars(bars);
		if (bars != null) {
			addGroupHandler.updateEnablement();
			bars.setGlobalActionHandler(IPlanEditorConstants.ACTION_SET_NEW_ACTIVITY_GROUP_RETARGET, addGroupHandler);
			bars.updateActionBars();
		}
	}
	
	@Override
	public IStructureModifier getStructureModifier() {
		return PlanStructureModifier.INSTANCE;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (Plan.class.equals(adapter)) {
			return WrapperUtils.getRegistered(getPlan());
		}
		return super.getAdapter(adapter);
	}

	public Resource getPlanEditorTemplateResource() {
		return PlanEditorUtil.getPlanEditorTemplateResource(getId(), getEditorInput());
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		StructuredSelection selection = (StructuredSelection) getCurrentSelection();
		PlanEditorContextMenuBuilder.buildContextMenu(menu, selection);
		super.fillContextMenu(menu);
	}

	@Override
	protected IUndoableOperation getCutOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		return new PlanClipboardCutOperation(transferable, modifier);
	}
	
	@Override
	protected IUndoableOperation getCopyOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		return new PlanClipboardCopyOperation(transferable, modifier);
	}

	@Override
	protected IUndoableOperation getDeleteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		return new PlanDeleteOperation(transferable, modifier);
	}

	@Override
	protected IUndoableOperation getPasteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ISelection pasteSelection = ((plan == null || !PlanEditorUtil.emfFromSelection(selection).isEmpty()) ? selection : new StructuredSelection(plan));
		return new PlanClipboardPasteOperation(pasteSelection, modifier);
	}

	@Override
	protected IUndoableOperation getSelectAllOperation(IStructureModifier modifier, ISelection selection) {
		return new PlanSelectAllOperation(plan, getSite());
	}
	
	protected IUndoableOperation getAddGroupOperation(IStructureModifier modifier, ISelection selection) {
		EPlanElement target = getTargetElement(modifier, selection);
		if (target == null) {
			return null;
		}
		return new AddGroupOperation(modifier, target);
	}

	protected EPlanElement getTargetElement(IStructureModifier modifier, ISelection selection) {
		EPlan currentPlan = plan;
		if (currentPlan != null) {
			ISelection targetSelection = (!PlanEditorUtil.emfFromSelection(selection).isEmpty() ? selection : new StructuredSelection(currentPlan));
			return ((PlanStructureModifier)modifier).getTargetElement(targetSelection);
		}
		return null;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// handled by MultiPagePlanEditor
	}

	@Override
	public void doSaveAs() {
		// handled by MultiPagePlanEditor
	}

	@Override
	public boolean isDirty() {
		// handled by MultiPagePlanEditor
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// handled by MultiPagePlanEditor
		return false;
	}

	public void refresh() {
		// do nothing by default. Override if necessary.
	}
	
	@Override
	public void setFocus() {
		// Does nothing by default - subclasses may override
	}

	private final class PlanEditorUndoableOperationAction extends AbstractUndoableOperationAction {
		private final String id;
		
		/**
		 * Create an editor undoable operation action using the given id
		 * The id should be only of the ids from the ActionFactory.  See
		 * getOperation() below for recognized ids.
		 * @param id
		 */
		public PlanEditorUndoableOperationAction(String id) {
			this.id = id;
		}

		@Override
		public IUndoableOperation getOperation(Event event) {
			IStructureModifier modifier = getStructureModifier();
			IUndoableOperation operation = null;
			if (modifier != null) {
				ISelection selection = getCurrentSelection();
				if (PlanActionFactory.ADD_GROUP.getId().equals(id)) {
					operation = getAddGroupOperation(modifier, selection);
				}
			}
			return operation;
		}
		
	}

}
