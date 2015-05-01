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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;
import gov.nasa.ensemble.common.thread.ThreadUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PerspectiveExtensionReader;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.e4.compatibility.ModeledPageLayout;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

@SuppressWarnings("restriction")
public class ForbiddenWorkbenchUtils {

	/**
	 * Hide action set contributions
	 * @param removeActionSets - action set ids to hide
	 */
	public static void removeActionSets(String[] removeActionSets) {
		List<String> removeSetsList = Arrays.asList(removeActionSets);
		ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
		IActionSetDescriptor[] actionSets = reg.getActionSets();
		for (IActionSetDescriptor actionSetDescriptor : actionSets) {
			if (removeSetsList.contains(actionSetDescriptor.getId())) {
				actionSetDescriptor.setInitiallyVisible(false);
//				IConfigurationElement configurationElement = actionSetDescriptor.getConfigurationElement();
//				IExtension ext = configurationElement.getDeclaringExtension();
//				reg.removeExtension(ext, new Object[] { actionSetDescriptor });
			}
		}
	}
	
	public static void removePopupMenus(String[] popupIds) {
		
		final java.util.List<IExtension> extensionsToRemove = new java.util.ArrayList<IExtension>();
		
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final IExtensionPoint point = registry.getExtensionPoint("org.eclipse.ui.popupMenus");
		final List<String> popups = Arrays.asList(popupIds);
		for (final IConfigurationElement elem : point.getConfigurationElements()) {
			final String id = elem.getAttribute("id");
			if (popups.contains(id))
				extensionsToRemove.add(elem.getDeclaringExtension());
		}
		
		for (IExtension ext : extensionsToRemove) {
			try {
				registry.removeExtension(ext, ReflectionUtils.get(registry, "masterToken"));
			} catch (Throwable t) {
				// ignore
			}
		}
	}
	
	/**
	 * This is a sneaky function that ignores access restrictions
	 * on org.eclipse.ui.internal.  It attempts to set an "empty"
	 * EditorStack as the current active editor workbook.  This
	 * is helpful because newly opened editors will be opened in
	 * the current active editor workbook.  If there are no empty
	 * workbooks, or the internal api has changed inconsistently
	 * with the way that this method operates, this method will
	 * have no effect.
	 * 
	 * @param page
	 */
//	public static void activateEmptyEditorWorkbook(IWorkbenchPage page) {
//		try {
//			if (page instanceof WorkbenchPage) {
//				WorkbenchPage workbenchPage = (WorkbenchPage) page;
//				EditorAreaHelper helper = workbenchPage.getEditorPresentation();
//				String workbookId = null;
//				for (Object workbook : helper.getWorkbooks()) {
//					if (workbook instanceof EditorStack) {
//						EditorStack editorStack = (EditorStack) workbook;
//						if (editorStack.getChildren().length == 0) {
//							workbookId = editorStack.getID();
//							break;
//						}
//					}
//				}
//				if (workbookId != null) {
//					helper.setActiveEditorWorkbookFromID(workbookId);
//				}
//			}
//		} catch (ThreadDeath td) {
//			throw td;
//		} catch (Throwable t) {
//			// c'est la vie
//		}
//	}
	
	/**
	 * Because we are exploiting the launching/console infrastructure provided by eclipse, we inherited some default behavior
	 * regarding progress. Namely, the job is called "Launching -name-" and a substantial amount of progress is established before
	 * we get control. (about 55%)
	 * 
	 * This method subverts the default behavior by climbing the progress monitor tree and resetting the work back to zero. It also
	 * changes the job name along the way.
	 * 
	 * The result of this method is a newly minted progress monitor that allocates 100% of its work directly to the job monitor.
	 * 
	 * @param jobName
	 * @param monitor
	 * @return IProgressMonitor
	 */
	public static IProgressMonitor resetJobProgressMonitor(String jobName, IProgressMonitor monitor) {
		try {
			if (monitor instanceof SubProgressMonitor) {
				IProgressMonitor wrapped1 = ((SubProgressMonitor) monitor).getWrappedProgressMonitor();
				ReflectionUtils.set(wrapped1, "sentToParent", 0.0);
				IProgressMonitor wrapped2 = ((SubProgressMonitor) wrapped1).getWrappedProgressMonitor();
				ReflectionUtils.set(wrapped2, "sentToParent", 0.0);
				IProgressMonitor jobMonitor = ((SubProgressMonitor) wrapped2).getWrappedProgressMonitor();
				Job job = ReflectionUtils.get(jobMonitor, "job");
				job.setName(jobName);
				ProgressManager progressManager = ProgressManager.getInstance();
				Object jobInfo = ReflectionUtils.invoke(progressManager, "internalGetJobInfo", job);
				Object taskInfo = ReflectionUtils.get(jobInfo, "taskInfo");
				ReflectionUtils.set(taskInfo, "preWork", 0.0);
				ReflectionUtils.invoke(progressManager, "refreshJobInfo", jobInfo);
				int total = ReflectionUtils.get(taskInfo, "totalWork");
				monitor = new SubProgressMonitor(jobMonitor, total);
			}
		} catch (Exception e) {
			LogUtil.warn("resetJobProgressMonitor failed", e);
		}
		return monitor;
	}
	
	private static List<String> alreadyRegistered = new ArrayList<String>();
	public static void registerExistingParts(Collection<? extends String> parts) {
		alreadyRegistered.addAll(parts);
	}
	public static boolean alreadyExistingPart(String partId) {
		return alreadyRegistered.contains(partId);
	}

	/**
	 * Instantiate perspective extensions in dependency order.
	 * 
	 * @param l
	 */
	public static void instantiatePerspectiveExtension(IPageLayout l) {
		ModeledPageLayout layout = (ModeledPageLayout)l;
		String perspectiveId = layout.getDescriptor().getId();
		PerspectiveExtensionReader extender = new DependencyPerspectiveExtensionReader();
		extender.extendLayout(PlatformUI.getWorkbench().getExtensionTracker(), perspectiveId, layout);
	}

	private static final class DependencyPerspectiveExtensionReader extends PerspectiveExtensionReader {
		@Override
		public void readRegistry(IExtensionRegistry registry, String pluginId, String extensionPoint) {
			IExtensionPoint point = registry.getExtensionPoint(pluginId, extensionPoint);
		    if (point == null) {
				return;
			}
		    IExtension[] extensions = point.getExtensions();
		    extensions = orderExtensions(extensions);
		    Comparator<IExtension> comparator = new PerspectiveExtensionComparator();
		    List<IExtension> extensionsRemaining = new LinkedList<IExtension>(Arrays.asList(extensions));
		    List<IExtension> sortedExtensions = new ArrayList<IExtension>(extensionsRemaining.size());
		    while (!extensionsRemaining.isEmpty()) {
		    	Iterator<IExtension> iterator = extensionsRemaining.iterator();
		    	IExtension lowestExtension = iterator.next();
		    	while (iterator.hasNext()) {
		    		IExtension extension = iterator.next(); 
		    		int compare = comparator.compare(lowestExtension, extension);
		    		if (compare == -1) {
		    			lowestExtension = extension;
		    		}
		    	}
		    	sortedExtensions.add(lowestExtension);
		    	extensionsRemaining.remove(lowestExtension);
		    }
		    for (IExtension extension : sortedExtensions) {
				readExtension(extension);
				List<String> needs = new ArrayList<String>();
				List<String> provides = new ArrayList<String>();
				gatherViewDependencies(extension, needs, provides);
				registerExistingParts(provides);
		    }
		}
	}

	private static final class PerspectiveExtensionComparator implements Comparator<IExtension> {
		
		@Override
		public int compare(IExtension o1, IExtension o2) {
			List<String> needs1 = new ArrayList<String>();
			List<String> provides1 = new ArrayList<String>();
			gatherViewDependencies(o1, needs1, provides1);
			List<String> needs2 = new ArrayList<String>();
			List<String> provides2 = new ArrayList<String>();
			gatherViewDependencies(o2, needs2, provides2);
			provides1.retainAll(needs2);
			provides2.retainAll(needs1);
			boolean extension2dependsOnExtension1 = !provides1.isEmpty();
			boolean extension1dependsOnExtension2 = !provides2.isEmpty();
			if (extension2dependsOnExtension1 && extension1dependsOnExtension2) {
				String message = "cyclical dependency between extensions: "; 
				message += o1.getUniqueIdentifier() + " & " + o2.getUniqueIdentifier();
				LogUtil.warn(message);
			} else if (extension1dependsOnExtension2) {
				return -1;
			} else if (extension2dependsOnExtension1) {
				return 1;
			}
			return 0;
		}

	}

	private static void gatherViewDependencies(IExtension extension, List<String> needs, List<String> provides) {
		IConfigurationElement[] elements = extension.getConfigurationElements();
		for (IConfigurationElement element : elements) {
			IConfigurationElement[] children = element.getChildren();
			for (IConfigurationElement child : children) {
				String type = child.getName();
				if (IWorkbenchRegistryConstants.TAG_VIEW.equals(type)) {
					String id = child.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
					String relative = child.getAttribute(IWorkbenchRegistryConstants.ATT_RELATIVE);
					provides.add(id);
					if (relative != null) {
						needs.add(relative);
					}
				}
			}
		}
	}

	/**
	 * Call this to process any pending events such as
	 * asyncExec or syncExec.  To be safe, the caller
	 * should be holding no locks.
	 * 
	 * @param display
	 */
	public static void processPendingEvents(final Display display) {
		ReflectionUtils.invoke(display, "runAsyncMessages", Boolean.TRUE);
		ThreadUtils.sleep(50);
	}

}
