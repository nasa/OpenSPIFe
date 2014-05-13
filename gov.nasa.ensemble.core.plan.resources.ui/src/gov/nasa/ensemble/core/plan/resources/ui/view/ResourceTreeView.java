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
package gov.nasa.ensemble.core.plan.resources.ui.view;

import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.help.ContextProvider;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.core.activityDictionary.view.DefinitionTreeView;
import gov.nasa.ensemble.core.activityDictionary.view.NamedDefinitionLabelProvider;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.NumericResourceDefTransferProvider;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.StateResourceDefTransferProvider;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import org.eclipse.help.IContextProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ResourceTreeView extends DefinitionTreeView {
	
	private static final int DND_OPERATIONS = DND.DROP_COPY;
	protected static final Transfer[] TRANSFERS = new Transfer[] {
		NumericResourceDefTransferProvider.transfer,
		StateResourceDefTransferProvider.transfer
	};
	
	private static final Image IMAGE_GRAPH = WidgetPlugin.getDefault().getImageRegistry().get(WidgetPlugin.KEY_IMAGE_GRAPH);
	private static final Image IMAGE_GRAPH_MODELED = WidgetPlugin.getDefault().getImageRegistry().get(WidgetPlugin.KEY_IMAGE_GRAPH_MODELED);
	private static final Image IMAGE_GRAPH_DISCRETE_MODELED = WidgetPlugin.getDefault().getImageRegistry().get(WidgetPlugin.KEY_IMAGE_GRAPH_DISCRETE_MODELED);
	private static final Image IMAGE_FOLDER = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
	
	@SuppressWarnings("unused")
	private static final Image IMAGE_GRAPH_DISCRETE = WidgetPlugin.getDefault().getImageRegistry().get(WidgetPlugin.KEY_IMAGE_GRAPH_DISCRETE);
	
	/**
	 * This ID should be the same as the identifier as in the extension point
	 * org.eclipse.ui.views that defines this view
	 */
	public static final String ID = ClassIdRegistry.getUniqueID(ResourceTreeView.class);
	
	@Override
	protected TreeViewer buildTreeViewer(Composite parent) {
		TreeViewer treeViewer = new TreeViewer(parent, SWT.MULTI);
		treeViewer.addDragSupport(DND_OPERATIONS, TRANSFERS, new ResourceTreeDragSourceListener(treeViewer, null));
		treeViewer.setContentProvider(new ResourceTreeContentProvider());
		treeViewer.setLabelProvider(new DefLabelProvider());
		treeViewer.setInput(getViewSite());
		treeViewer.expandToLevel(2);
        treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        treeViewer.getTree().setData("name", "ResourceTreeView.resourceTree");
		return treeViewer;
	}
	
	@Override
	protected String getToolTipText(Object element) {
		String tooltipText = null;
		if (element instanceof ENumericResourceDef) {
			tooltipText = ((ENumericResourceDef)element).getDescription();
		} else if (element instanceof EStateResourceDef) {
			((EStateResourceDef)element).getDescription();
		}
		return tooltipText;
	}
	
	/**
	 * This label provider provides custom method implementations for retrieving
	 * an activity definition's icon and name.
	 */
	static class DefLabelProvider extends NamedDefinitionLabelProvider {

		private static final EnsembleUnitFormat UNIT_FORMAT = EnsembleUnitFormat.INSTANCE;

		@Override
		public String getText(Object element) {
			if (element instanceof ENumericResourceDef) { 
				ENumericResourceDef def = (ENumericResourceDef) element;
				String unitString = UNIT_FORMAT.formatUnit(def.getUnits());
				if (unitString != null && unitString.length() > 0) {
					return super.getText(def) + " (" + unitString +")";
				}
			} else
			if (element instanceof EStateResourceDef) {
				EStateResourceDef def = (EStateResourceDef) element;
				return def.getName();
			}
			return super.getText(element);
		}
		
		/**
		 * @param element
		 * @return the image associated with the specified element 
		 */
		@Override
		public Image getImage(Object element) {
			if (element instanceof String) {
				return IMAGE_FOLDER;
			} else
			if (element instanceof ENumericResourceDef) {
				if (element instanceof ENumericResourceDef) {
					return IMAGE_GRAPH;
				} // else...
				return IMAGE_GRAPH_MODELED;
			} else
			if (element instanceof EStateResourceDef) {
				return IMAGE_GRAPH_DISCRETE_MODELED;
			} // else...
			return null;
		}
	}
	
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContextProvider.class)) {
			return new ContextProvider(ID);
		}
		return super.getAdapter(key);
	}

}
