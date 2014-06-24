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
package gov.nasa.ensemble.core.rcp;

import static gov.nasa.ensemble.common.ui.ForbiddenWorkbenchUtils.removeActionSets;
import static gov.nasa.ensemble.common.ui.ForbiddenWorkbenchUtils.removePopupMenus;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.EditorAreaDropAdapter;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;
import org.eclipse.ui.internal.registry.ViewRegistry;
import org.eclipse.ui.views.IViewDescriptor;

@SuppressWarnings("restriction")
public class EnsembleWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public EnsembleWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
		
		// set the initialize app window size
		Rectangle monitorSize = PlatformUI.getWorkbench().getDisplay().getPrimaryMonitor().getClientArea();
		configurer.setInitialSize(getInitialWindowSize(monitorSize.width, monitorSize.height));
		
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);
		configurer.setShowPerspectiveBar(true);
		
		// add the drag and drop support for the editor area
		// TODO investigate how much of the following commented code would be useful to us
//		configurer.addEditorAreaTransfer(EditorInputTransfer.getInstance());
//		configurer.addEditorAreaTransfer(ResourceTransfer.getInstance());
//		configurer.addEditorAreaTransfer(FileTransfer.getInstance());
//		configurer.addEditorAreaTransfer(MarkerTransfer.getInstance());
		configurer.configureEditorAreaDropListener(new EditorAreaDropAdapter(configurer.getWindow()));
		
		removeActionSets(getUndesirableActionSets());
		removePopupMenus(getUndesirablePopupMenus());
	}
	
	/**
	 * @return what the initial workbench window size should be. Subclasses may
	 *         override to customize this.
	 */
	protected Point getInitialWindowSize(int screenWidth, int screenHeight) {
		int width  = (int) (screenWidth  * 0.9);
		int height = (int) (screenHeight * 0.9);
		return new Point(width, height);
	}
	
	protected void cleanupViews() {
		ViewRegistry viewRegistry = (ViewRegistry) PlatformUI.getWorkbench().getViewRegistry();
		final Map<String, IViewDescriptor> descs = ReflectionUtils.get(viewRegistry, "descriptors");
		descs.keySet().removeAll(getUndesirableViews());
	}
	
	protected void cleanupPerspectives() {
		PerspectiveRegistry pRegistry = (PerspectiveRegistry) PlatformUI.getWorkbench().getPerspectiveRegistry();
		Collection<String> undesirables = getUndesirablePerspectives();
		List<IPerspectiveDescriptor> pToRemove = new ArrayList<IPerspectiveDescriptor>();
		for (IPerspectiveDescriptor pDesc : pRegistry.getPerspectives()) {
			if (undesirables.contains(pDesc.getId())) {
				pToRemove.add(pDesc);
			}
		}
		pRegistry.removeExtension(null, pToRemove.toArray(new Object[pToRemove.size()]));
		System.out.println();
	}
	
	private Collection<String> getUndesirablePerspectives() {
		return Arrays.asList(new String[] {
				"org.eclipse.mylyn.tasks.ui.perspectives.planning",
				"org.eclipse.debug.ui.DebugPerspective",
				"org.eclipse.ecf.ui.perspective.communications",
				"org.eclipse.team.ui.TeamSynchronizingPerspective",
				"org.eclipse.ui.resourcePerspective",
				"org.eclipse.wst.xml.ui.perspective"
		});
	}

	protected Collection<String> getUndesirableViews() {
		return Collections.emptySet();
	}

	protected String[] getUndesirableActionSets() {
		return new String[] {
				"org.eclipse.search.searchActionSet",
				"org.eclipse.team.ui.actionSet", 
				"org.eclipse.ui.edit.text.actionSet.navigation",
				"org.eclipse.ui.edit.text.actionSet.annotationNavigation",
				"org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo",
				"org.eclipse.ui.edit.text.actionSet.presentation",
				// commented this out because MSEQ needs it
//				"org.eclipse.ui.NavigateActionSet",
				"org.eclipse.ui.WorkingSetActionSet", 
				"org.eclipse.ui.WorkingSetModificationActionSet",
				"org.eclipse.mylyn.tasks.ui.navigation" };
	}

	protected String[] getUndesirablePopupMenus() {
		return new String[0];
	}
	
	protected String[] getUndesirablePreferencePageIDRegExes() {
		return new String[0];
	}
	
	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new EnsembleActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		// We want curvy tabs and the perspective bar on the top right
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
		                                           IWorkbenchPreferenceConstants.TOP_RIGHT);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR, false);
		
		
		moveAdvancedPreferencesPages();
		cleanupPreferencePages();
	}
	
	@Override
    public void postWindowCreate() {
		super.postWindowCreate();
    	cleanupMenusAndCoolBar();
	}
	
	protected Map<String, Set<String>> getItemsToRemove() {
		return new HashMap<String, Set<String>>();
	}
	
	protected void cleanupMenusAndCoolBar() {
    	IContributionItem[] mItems, mSubItems;
    	IMenuManager mm = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
    	mItems = mm.getItems();
    	for (int i = 0; i < mItems.length; i++) {
			if (mItems[i] instanceof MenuManager) {
				mSubItems = ((MenuManager) mItems[i]).getItems();
				for (int j = 0; j < mSubItems.length; j++) {
					if (getItemsToRemove().keySet().contains(mItems[i].getId())) {
						for (String fileItemToRemove : getItemsToRemove().get(mItems[i].getId()))
							((MenuManager)mItems[i]).remove(fileItemToRemove);
					}
				}
			}
		}
    	if (getItemsToRemove().keySet().contains("file")) {
    		ICoolBarManager cbm = getWindowConfigurer().getActionBarConfigurer().getCoolBarManager();
        	for (String itemToRemove : getItemsToRemove().get("file")) {
        		for (IContributionItem item : cbm.getItems()) {
        			if (item instanceof ToolBarContributionItem)
        				((ToolBarContributionItem)item).getToolBarManager().remove(itemToRemove);
        			else
        				cbm.remove(itemToRemove);
        		}
        	}
        	cbm.update(true);
    	}
	}
	
	protected String[] getAdvancedPreferenceRegExp() {
		return new String[0];
	}
	
	protected final void cleanupPreferencePages() {
		Collection<Pattern> patterns = new HashSet<Pattern>();
		for (String s : getUndesirablePreferencePageIDRegExes())
			patterns.add(Pattern.compile(s));
		if (patterns.size() == 0)
			return;
		PreferenceManager manager = PlatformUI.getWorkbench().getPreferenceManager();
		for (IPreferenceNode node : manager.getRootSubNodes()) {
			for (Pattern pattern : patterns) {
				if (pattern.matcher(node.getId()).matches()) {
					manager.remove(node);
					continue;
				}
			}
			cleanupPreferencePages(node, patterns);
		}
	}

	private void cleanupPreferencePages(IPreferenceNode parent, Collection<Pattern> patterns) {
		for (IPreferenceNode node : parent.getSubNodes()) {
			for (Pattern pattern : patterns) {
				if (pattern.matcher(node.getId()).matches()) {
					parent.remove(node);
					continue;
				}
			}
			cleanupPreferencePages(node, patterns);
		}
	}
	
	protected final void moveAdvancedPreferencesPages() {
		Collection<Pattern> patterns = new HashSet<Pattern>();
		for (String s : getAdvancedPreferenceRegExp())
			patterns.add(Pattern.compile(s));
		if (patterns.size() == 0)
			return;
		
		PreferenceManager manager = PlatformUI.getWorkbench().getPreferenceManager();
		IPreferenceNode[] rootSubNodes = manager.getRootSubNodes();
		
		PreferenceNode advanced = null;
		Pattern advancePattern = Pattern.compile("AdvancedPreferenceNode");
		for(IPreferenceNode node : rootSubNodes) {
			if (advancePattern.matcher(node.getId()).matches()) {
				advanced = (PreferenceNode) node;
				break;
			}
		}
		if(advanced == null)
			return;
		
		for (IPreferenceNode node : rootSubNodes) {
			for (Pattern pattern : patterns) {
				if (pattern.matcher(node.getId()).matches()) {
					manager.remove(node);
					advanced.add(node);
					continue;
				}
			}
		}
		
	}
}
