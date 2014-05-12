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
package gov.nasa.arc.spife.ui.timeline.chart;

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.Activator;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.ide.IDE;

public class ChartEditorOpenAction extends BaseSelectionListenerAction implements ISelectionListener {
	private Object selectedObject = null;
	private IWorkbenchPage workbenchPage;
	
	public ChartEditorOpenAction(IWorkbenchPage page) {
		super("Open Chart Editor");
		workbenchPage = page;
		page.addSelectionListener(this);
	}

	@Override
	public boolean isEnabled() {
		if (selectedObject instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable)selectedObject;
			Object adapter = adaptable.getAdapter(Profile.class);
			if (adapter != null && adapter instanceof Profile) {
				return true;
			}
		}
		else if (selectedObject instanceof Profile) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		open(workbenchPage, selectedObject);
	}
	
	public static void open(IWorkbenchPage page, Object object) {
		Object o = null;
		
		if(object instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable)object;
			o = adaptable.getAdapter(Profile.class);
		}
		
		else if(object instanceof Profile) {
			o = object;
		}
									
		if (o != null && o instanceof Profile) {
			Profile profile = (Profile) o;
			URI chartUri = getChartUri(profile);
			ResourceSet resourceSet = EMFUtils.createResourceSet();
			if (!resourceSet.getURIConverter().exists(chartUri, null)) {
				ETimeline timeline = TimelineFactory.eINSTANCE.createETimeline();
				Chart chart = ChartFactory.eINSTANCE.createChart();
				chart.setMinimumHeight(500);
				Plot plot = ChartFactory.eINSTANCE.createPlot();
				plot.setProfile(profile);
				chart.getPlots().add(plot);
				timeline.getContents().add(chart);
				
				Resource resource = new XMIResourceImpl(chartUri);
				resource.getContents().add(timeline);
				resourceSet.getResources().add(resource);
				
				try {
					resource.save(null);
				} catch (Exception e) {
					ErrorDialog.openError(page.getWorkbenchWindow().getShell(), 
							"Opening profile", "Opening "+ chartUri,
							new ExceptionStatus(Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
			
			URIEditorInput input = new URIEditorInput(chartUri);
			try {
				IDE.openEditor(page, input, ChartEditor.ID, true);
			} catch (Exception e) {
				ErrorDialog.openError(page.getWorkbenchWindow().getShell(),
						"Opening profile", "Opening "+ chartUri,
						new ExceptionStatus(Activator.PLUGIN_ID, e.getMessage(), e));
			}
		}
	}

	private static URI getChartUri(Profile profile) {
		Resource profileResource = profile.eResource();
		String profileFragment = profileResource.getURIFragment(profile);
		int start = profileFragment.indexOf('[');
		int end = profileFragment.indexOf(']');
		if (start != -1 && end != -1) {
			profileFragment = profileFragment.substring(start, end+1);
		} else {
			profileFragment = "[" + profileFragment + "]";
		}
		return URI.createURI(profileResource.getURI().trimFileExtension().toString()+profileFragment).appendFileExtension(TimelineConstants.TIMELINE_FILE_EXT);
	}

	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		if (selection.size() == 1) {
			selectedObject = selection.getFirstElement();
		}
		return super.updateSelection(selection);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection)
			super.selectionChanged((IStructuredSelection) selection);		
	}
}
