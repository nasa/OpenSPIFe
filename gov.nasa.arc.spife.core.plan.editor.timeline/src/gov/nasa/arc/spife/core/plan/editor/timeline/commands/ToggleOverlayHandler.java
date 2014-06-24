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
package gov.nasa.arc.spife.core.plan.editor.timeline.commands;

import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class ToggleOverlayHandler extends AbstractPlanEditorHandler {

	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}
	
	@Override
	protected boolean isEnabledForSelection(ISelection selection) {
		MultiPagePlanEditor editor = getActiveEditor();
		if (editor != null) {
			for (int i = 0; i < editor.getPageCount(); i++) {
				IEditorPart subEditor = editor.getEditor(i);
				if (subEditor.getAdapter(Timeline.class) != null) {
					EList<EPlanElement> elements = getSelectedTemporalElements(selection);
					return !elements.isEmpty();
				}
			}
		}
		return false;
	}
	
	@Override
	protected void partActivated(IWorkbenchPart part) {
		setBaseEnabled(isEnabledForSelection(getSelection()));
	}
	
	@Override
	protected void pageChanged(org.eclipse.jface.dialogs.PageChangedEvent event) {
		setBaseEnabled(isEnabledForSelection(getSelection()));
	}
	
	@Override
	public boolean isCheckedForSelection(ISelection selection) {
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		if (elements.isEmpty()) {
			return false;
		}
		for (EPlanElement element : elements) {
			CommonMember member = element.getMember(CommonMember.class);
			if (!member.isMarked()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		boolean state = getCommandState(event.getCommand());
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor != null) {
			EPlan plan = (EPlan) editor.getAdapter(EPlan.class);
			if (plan != null) {
				TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
				final List<CommonMember> selected = new ArrayList<CommonMember>();
				Timeline<EPlan> timeline = TimelineUtils.getTimeline(event);
				if (timeline != null) {
					for (TimelineViewer v : timeline.getTimelineViewers()) {
						List<?> selectedEditParts = v.getSelectedEditParts();
						for (Object ep : selectedEditParts) {
							if (ep instanceof TemporalNodeEditPart) {
								EPlanElement pe = ((TemporalNodeEditPart) ep).getModel();
								selected.add(pe.getMember(CommonMember.class));
							}
						}
					}
					List<Command> commands = new ArrayList<Command>();
					for(CommonMember commonMember : selected) {
						Command create = SetCommand.create(domain, commonMember,
								PlanPackage.Literals.COMMON_MEMBER__MARKED, !state);
						commands.add(create);
					}
					EMFUtils.executeCommand(domain, new CompoundCommand(commands));
					try {
						HandlerUtil.toggleCommandState(event.getCommand());
					} catch (ExecutionException e) {
						LogUtil.error(e);
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getCommandId() {
		return OVERLAY_COMMAND_ID;
	}

//	@Override
//	protected void updateEnablement() {
//		if(updatingCheckedState) {
//			return;
//		}
//		
//		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		if(activeWorkbenchWindow != null) {
//			IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
//			if(activePage != null) {
//				IEditorPart activeEditor = activePage.getActiveEditor();
//				if(activeEditor != null) {
//					IWorkbenchPartSite site = activeEditor.getSite();
//					if(site != null){
//						ISelectionProvider selectionProvider = site.getSelectionProvider();
//						// does nothing if the listener has already been added
//						if(selectionProvider != null) {
//							selectionProvider.addSelectionChangedListener(this);
//							
//							ISelection selection = selectionProvider.getSelection();
//							IStructuredSelection structuredSelection = null;
//							if(selection instanceof IStructuredSelection) {
//								structuredSelection
//									= (IStructuredSelection)selection;
//							}
//
//							List<?> list = null;
//							
//							if(structuredSelection != null) {
//								list = structuredSelection.toList();
//							}							
//							
//							else {
//								list = Collections.EMPTY_LIST;
//							}
//							
//							updateToggleState(list);
//							
//							boolean shouldBeEnabled = false;
//							for(Object object : list) {
//								if(object  instanceof EPlanElement
//										&& !(object instanceof EPlan)) {
//									shouldBeEnabled = true; 
//									break;
//								}
//							}
//							
//							if(this.isEnabled() != shouldBeEnabled) {
//								this.setBaseEnabled(shouldBeEnabled);
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		// get here if can't get selection provider
//		updateToggleState(Collections.EMPTY_LIST);
//	}	

//	private void updateToggleState(List<?> list) {
//		if (timeline == null) {
//			return;
//		}
//		boolean foundOverlayableItem = false;
//		boolean marked = false;
//		for (Object o : list) {
//			if (o instanceof EPlanElement) {
//				if (!(o instanceof EPlan)) {
//					foundOverlayableItem = true;
//				}
//				EPlanElement pe = (EPlanElement) o;
//				CommonMember member = pe.getMember(CommonMember.class);
//				marked = member.isMarked();
//				if (marked) {
//					break;
//				}
//			}
//		}
//		boolean shouldBeChecked = marked && foundOverlayableItem;
//		if (shouldBeChecked != isChecked) {
//			try {
//				updatingCheckedState = true;
//				isChecked = !HandlerUtil.toggleCommandState(getCommand());
//				updatingCheckedState = false;
//			} catch (ExecutionException e) {
//				LogUtil.error(e);
//			}
//		}
//	}
//
//	public void selectionChanged(SelectionChangedEvent event) {
//		ISelection selection = event.getSelection();
//		if(selection instanceof IStructuredSelection) {
//			List<?> list = ((IStructuredSelection)selection).toList();
//
//			boolean shouldBeEnabled = false;
//			for(Object object : list) {
//				if(object  instanceof EPlanElement) {
//					if(!(object instanceof EPlan)) {
//						shouldBeEnabled = true; 
//						break;
//					}
//				}
//			}
//			
//			this.setBaseEnabled(shouldBeEnabled);		
//			updateToggleState(list);
//		}			
//	}

}
