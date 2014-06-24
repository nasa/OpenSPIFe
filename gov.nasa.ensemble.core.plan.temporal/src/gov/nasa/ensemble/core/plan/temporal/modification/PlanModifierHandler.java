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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.editor.CommandRefreshingActiveEditorPartListener;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.temporal.TemporalPlugin;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.services.IServiceLocator;

/**
 * @see gov.nasa.arc.spife.core.plan.editor.timeline.commands.ToggleConstrainedMoveAction
 *
 */
public class PlanModifierHandler extends AbstractHandler implements IElementUpdater {

	private static final List<PlanModifierFactory> PLAN_MODIFIER_FACTORIES = PlanModifierRegistry.getInstance().getModifierFactories();
	private static final String TEMPORAL_MODIFICATION_COMMAND_ID = "gov.nasa.ensemble.core.plan.temporal.modification";
	private final CommandRefreshingActiveEditorPartListener ACTIVE_EDITOR_LISTENER = new CommandRefreshingActiveEditorPartListener(TEMPORAL_MODIFICATION_COMMAND_ID) {
		
		@Override
		public void partHidden(IWorkbenchPartReference partRef) {
			IWorkbenchPart part = partRef.getPart(false);
			if(part instanceof IEditorPart) {
				IEditorPart editorPart = (IEditorPart)part;
				Object adapter = editorPart.getAdapter(EPlan.class);
				if(adapter != null) {
					updateEnablement(editorPart);
				}
			}
		}

		@Override
		public void partVisible(IWorkbenchPartReference partRef) {
			IWorkbenchPart part = partRef.getPart(false);
			if(part instanceof IEditorPart) {
				IEditorPart editorPart = (IEditorPart)part;
				Object adapter = editorPart.getAdapter(EPlan.class);
				if(adapter != null) {
					updateEnablement(editorPart);
				}
			}
		}		
	};

	static {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IMenuService menuService = (IMenuService)workbench.getService(IMenuService.class);
//		String menuURI = "menu:gov.nasa.ensemble.core.plan.temporal.modification.menu";
//		menuService.addContributionFactory(new PlanModifierContributionFactory(menuURI, TemporalPlugin.ID);
		String toolbarURI = "menu:gov.nasa.ensemble.core.plan.temporal.modification.pulldown";
		menuService.addContributionFactory(new PlanModifierContributionFactory(toolbarURI, TemporalPlugin.ID));
		ICommandService commandService = (ICommandService)workbench.getService(ICommandService.class);
		commandService.refreshElements(TEMPORAL_MODIFICATION_COMMAND_ID, null);
	}
	
	protected void updateEnablement(IEditorPart editorPart) {
		boolean enabled = false;
		if (editorPart != null) {
			Object adapter = editorPart.getAdapter(EPlan.class);
			EPlan ePlan = (adapter instanceof EPlan ? (EPlan)adapter : null);
			boolean editorIsVisible = editorPart.getSite().getPage().isEditorAreaVisible();
			enabled = ePlan != null && editorIsVisible;
		}
		if (PlanModifierHandler.this.isEnabled() != enabled) {
			PlanModifierHandler.this.setBaseEnabled(enabled);
		}
	}

	@Override
	public void setEnabled(Object evaluationContext) {
		boolean enabled = isEnabled(evaluationContext);
    	setBaseEnabled(enabled);
	}

	private boolean isEnabled(Object evaluationContext) {
		Object activeEditor = HandlerUtil.getVariable(evaluationContext, ISources.ACTIVE_EDITOR_NAME);
    	if (activeEditor instanceof MultiPagePlanEditor) {
    		MultiPagePlanEditor multiPagePlanEditor = (MultiPagePlanEditor)activeEditor;
    		boolean editorAreaVisible = multiPagePlanEditor.getCurrentEditor().getEditorSite().getPage().isEditorAreaVisible();
    		if(editorAreaVisible) {
    			return true;
    		}
    	}
 
	    return false;
    }
	
	/**
	 * @throws ExecutionException  
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		IEditorInput editorInput = editor.getEditorInput();
		PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(editorInput);
		EPlan plan = model.getEPlan();
		PlanModifierMember planModifierMember = PlanModifierMember.get(plan);
		String factoryName = event.getParameter("name");
		PlanModifierFactory factory = (factoryName != null ? getNamedFactory(factoryName) : getNextFactory(planModifierMember));
		if (factory != null) {
			IPlanModifier modifier = factory.instantiateModifier();
			modifier.initialize(plan);
			planModifierMember.setModifier(modifier);
			ICommandService commandService = (ICommandService)editor.getSite().getService(ICommandService.class);
			commandService.refreshElements(event.getCommand().getId(), null);
		}
		return null;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		IServiceLocator serviceLocator = element.getServiceLocator();
		IPartService partService = (IPartService)serviceLocator.getService(IPartService.class);
		partService.addPartListener(ACTIVE_EDITOR_LISTENER); // adding the same listener multiple times is ok and ignored
		PlanModifierFactory elementFactory = (PlanModifierFactory)serviceLocator.getService(PlanModifierFactory.class);
		IPlanModifier planModifier = getCurrentPlanModifier(parameters);
		PlanModifierFactory planFactory = PlanModifierRegistry.getInstance().getFactory(planModifier);
		if (elementFactory != null) {
			if (elementFactory == planFactory) {
				element.setChecked(true);
			} else {
				element.setChecked(false);
			}
		} else if (PLAN_MODIFIER_FACTORIES.size() == 2) {
			boolean checked = !(planModifier instanceof DirectPlanModifier);
			element.setChecked(checked);
			if (planFactory != null) {
				element.setTooltip(planFactory.getName());
				element.setIcon(planFactory.getImageDescriptor());
			}
		}
	}

	private static PlanModifierFactory getNamedFactory(String factoryName) {
		PlanModifierFactory newFactory = null;
	    for (PlanModifierFactory factory : PLAN_MODIFIER_FACTORIES) {
	    	String name = factory.getName();
	    	if (CommonUtils.equals(name, factoryName)) {
	    		newFactory = factory;
	    		break;
	    	}
	    }
	    return newFactory;
    }

	private static PlanModifierFactory getNextFactory(PlanModifierMember planModifierMember) {
	    IPlanModifier oldModifier = planModifierMember.getModifier();
		PlanModifierFactory oldFactory = PlanModifierRegistry.getInstance().getFactory(oldModifier);
	    int oldIndex = PLAN_MODIFIER_FACTORIES.indexOf(oldFactory);
	    int newIndex = (oldIndex + 1) % PLAN_MODIFIER_FACTORIES.size();
	    return PLAN_MODIFIER_FACTORIES.get(newIndex);
    }
	
	private static IPlanModifier getCurrentPlanModifier(Map parameters) {
	    IPlanModifier planModifier = null;
		Object workbenchWindow = parameters.get("org.eclipse.ui.IWorkbenchWindow");
		if (workbenchWindow instanceof IWorkbenchWindow) {
			IWorkbenchWindow window = (IWorkbenchWindow) workbenchWindow;
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IEditorPart editor = page.getActiveEditor();
				if (editor != null) {
					IEditorInput editorInput = editor.getEditorInput();
					PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(editorInput);
					if (model != null) {
						EPlan plan = model.getEPlan();
						planModifier = PlanModifierMember.get(plan).getModifier();
					}
				}
			}
		}
	    return planModifier;
    }
	
}
