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
package gov.nasa.ensemble.core.plan.editor.util;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.rcp.perspective.PlanningPerspectiveFactory;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

public class PlanEditorUtil {

	protected static final Logger trace = Logger.getLogger(PlanEditorUtil.class);

	public static final String TEMPLATE_TARGET_MODE_SUFFIX = ".template.target.mode";
	public static final String TEMPLATE_SOURCE_URI_SUFFIX = ".template.source.uri";
	
	private static final QualifiedName QN_PLAN_EXPORT_EXTENT = new QualifiedName("plan.export", "extent");
	
	/**
	 * Open the specified plan in the plan editor using the current workbench window.
	 * 
	 * @param plan
	 */
	public static void openPlanEditor(EPlan plan) {
		openPlanEditor(plan, true);
	}

	public static void openPlanEditor(EPlan plan, boolean switchToPlanningPerspective) {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (plan == null) {
			MessageDialog.openError(workbenchWindow.getShell(), "Plan open error", "Plan is null.");
			return;
		}
		openPlanEditor(plan, workbenchWindow, switchToPlanningPerspective);
	}

	/**
	 * Open the specified plan in the plan editor using the specified workbench
	 * window.
	 * 
	 * @param plan
	 *            the plan to edit
	 * @param workbenchWindow
	 *            the workbench window to build the editor in
	 * @param switchToPlanningPerspective
	 *            if true, switch to the planning perspective before opening the
	 *            editor
	 * @return true if the editor was already open
	 */
	public static boolean openPlanEditor(final EPlan plan, IWorkbenchWindow workbenchWindow, 
			final boolean switchToPlanningPerspective)
	{
		if (activateExistingPlanEditor(plan, workbenchWindow))
			return true;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (switchToPlanningPerspective) 
					PlanningPerspectiveFactory.switchToPlanningPerspective();
				MultiPagePlanEditor.openEditor(plan);
			}
		});
		return false;
	}
	
	public static TemporalExtent getPlanExportExtentPropertyValue(EPlan plan) {
		Resource resource = plan.eResource();
		if (resource != null) {
			IFile file = EMFUtils.getFile(resource);
			if (file != null) {
				try {
					String extentString = file.getPersistentProperty(QN_PLAN_EXPORT_EXTENT);
					if (extentString != null) {
						return (TemporalExtent) JScienceFactory.eINSTANCE.createFromString(JSciencePackage.Literals.TEMPORAL_EXTENT, extentString);
					}
				} catch (CoreException e) {
					LogUtil.error(e);
				}
			}
		}
		PlanTemporalMember planTemporalMember = plan.getMember(PlanTemporalMember.class);
		Date start = planTemporalMember.getStartBoundary();
		Date end = planTemporalMember.getEndBoundary();
		if (start != null && end != null) {
			return new TemporalExtent(start, end);
		}
		return plan.getMember(TemporalMember.class).getExtent();
	}
	
	public static void setPlanExportExtentPropertyValue(EPlan plan, TemporalExtent extent) {
		Resource resource = plan.eResource();
		if (resource != null) {
			IFile file = EMFUtils.getFile(resource);
			if (file != null) {
				String extentString = JScienceFactory.eINSTANCE.convertToString(JSciencePackage.Literals.TEMPORAL_EXTENT, extent);
				try {
					file.setPersistentProperty(QN_PLAN_EXPORT_EXTENT, extentString);
				} catch (CoreException e) {
					LogUtil.error(e);
				}
			}
		}
	}

	/**
	 * Attempt to activate an existing plan editor.
	 * 
	 * @param plan
	 * @param workbenchWindow
	 * @return true if it was able to activate it
	 */
	private static boolean activateExistingPlanEditor(EPlan plan, IWorkbenchWindow workbenchWindow) {
		for (IWorkbenchPage page : workbenchWindow.getPages()) {
			for (IEditorReference editorRef : page.getEditorReferences()) {
				final IEditorPart editorPart = editorRef.getEditor(true);
				PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(editorPart.getEditorInput());
				if (model != null) {
					EPlan plan2 = model.getEPlan();
					if (plan2 == plan) {
						final IWorkbenchPage thePage = page;
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								thePage.activate(editorPart);
							}
						});
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//** PLAN EDITOR TEMPLATE **//
	
	public static enum PlanEditorTemplateTargetMode {
		WORKSPACE,
		PROJECT,
	}
	
	public static Resource getPlanEditorTemplateResource(String editorId, IEditorInput input) {
		PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
		ResourceSet resourceSet = model.getEditingDomain().getResourceSet();
		IFile editorFile = CommonUtils.getAdapter(input, IFile.class);
		return getPlanEditorTemplateResource(editorId, resourceSet, editorFile);
	}

	public static Resource getPlanEditorTemplateResource(String editorId, ResourceSet resourceSet, IFile editorFile) {
		//check if there's a template file in project
		PlanEditorTemplateTargetMode mode = getTemplateTargetMode(editorId);
		Bundle bundle = EditorPlugin.getDefault().getBundle();
		URI inputUri = null;
		URI uri = null;
		if (editorFile != null) {
			inputUri = EMFUtils.getURI(editorFile);
			uri = inputUri.trimFileExtension().appendFileExtension(editorId);
			if (!resourceSet.getURIConverter().exists(uri, null)) {
				// if there's not and preference is WORKSPACE get from there
				if (mode == PlanEditorTemplateTargetMode.WORKSPACE) {
					Location instanceLocation = Platform.getInstanceLocation();
					try {
						URL dataArea = FileUtilities.getDataArea(instanceLocation, bundle.getSymbolicName());
						URL fullTargetPath = new URL(dataArea + "/user." + editorId);
						File file = new File(CommonUtils.decodeUTF8(fullTargetPath.getFile()));
						uri = URI.createURI(file.toURI().toString());
					} catch (IOException e) {
						LogUtil.error(e);
					}
				}
			}
		}

		if (uri == null) {
			return null;
		}

    	//try to load current template file
		Resource resource = loadResource(resourceSet, uri);
		if (resource == null) {
			//if you cant, try to get a default template file set in ensemble.properties
			URI defaultSourceURI = getTemplateDefaultSourceURI(editorId);
			Resource defaultResource = loadResource(resourceSet, defaultSourceURI);
			if (defaultResource != null) {
				defaultResource.setURI(uri);
				return defaultResource;
			}
			//if all else fails... just create it with default values
			return resourceSet.createResource(uri);
		}
		return resource;
	}
	
	public static URI getTemplateDefaultSourceURI(String editorId) {
		String propertyName = editorId + TEMPLATE_SOURCE_URI_SUFFIX;
		String sourcePath = EnsembleProperties.getProperty(propertyName);
		if (sourcePath == null) {
			return null;
		}
		return URI.createURI(sourcePath);
	}
	
	public static PlanEditorTemplateTargetMode getTemplateTargetMode(String editorExtension) {
		String propertyName = editorExtension + TEMPLATE_TARGET_MODE_SUFFIX;
		String value = EnsembleProperties.getStringPropertyValue(propertyName, PlanEditorTemplateTargetMode.WORKSPACE.toString());
		try {
			PlanEditorTemplateTargetMode mode = PlanEditorTemplateTargetMode.valueOf(value);
			if (mode != null) {
				return mode;
			}
		} catch (IllegalArgumentException e) {
			//just return WORSPACE as default
			LogUtil.warnOnce(e.getMessage() + ". Using WORSPACE as default.");
		}
		return PlanEditorTemplateTargetMode.WORKSPACE;
	}

	private static Resource loadResource(ResourceSet resourceSet, URI uri) {
		Resource resource = null;
		if (uri == null) {
			return null;
		}
		if (resourceSet.getURIConverter().exists(uri, null)) {
			try {
				resource = resourceSet.getResource(uri, true);
			} catch (Exception e) {
				try {
					// try again
					resource = resourceSet.getResource(uri, true);
				} catch (Exception x) {
					// fall through and create a new one
					LogUtil.error("error reading "+uri, e);
				}
			}
		}
		return resource;
	}
	
	public static Collection<EPlan> getOpenPlans() {
		Collection<EPlan> plans = new ArrayList<EPlan>();
		for (IEditorPart editorPart : EditorPartUtils.getEditors()) {
			if (editorPart instanceof MultiPagePlanEditor) {
				EPlan plan = ((MultiPagePlanEditor)editorPart).getPlan();
				if (plan != null) {
					plans.add(plan);
				}
			}
		}
		return plans;
	}

	/**
	 * If the selection is a structured selection, it can have elements. Return the su set of the
	 * selection's elements that are plan elements.
	 * @param selection a selection
	 * @return all elements of the selection that are plan elements
	 */
	public static Set<EPlanElement> emfFromSelection(ISelection selection) {
		return WidgetUtils.typeFromSelection(selection, EPlanElement.class);
	}
	
}
