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
package gov.nasa.ensemble.core.detail.emf.treetable;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnLayout;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;

public class EMFTreeTableUtils {

	private static final String ANNOTATION_DETAIL_COLUMN_WIDTH = "columnWidth";
	public static final String ANNOTATION_SOURCE_TABLE = "table";
	public static final String ANNOTATION_SOURCE_DESCRIPTOR = "descriptor";
	public static final int DEFAULT_MIN_NUM_ROWS = -1;

	
	/**
	 * Method to create a dynamic TreeTableViewer
	 * 
	 * @param parent
	 *            the parent of this control
	 * @param eReference
	 * @param editingDomain
	 * @param actionBars
	 * @param forceTreeParent
	 *            this parameter should be true if there is a desire to force the parent parameter to be the parent of the tree too.
	 * @return TreeTableViewer
	 */
	public static TreeTableViewer<EObject, EAttribute> createEMFTreeTableViewer(Composite parent, EReference eReference, EditingDomain editingDomain, boolean forceTreeParent) {
		return createEMFTreeTableViewer(parent, eReference, eReference.getEReferenceType(), editingDomain, forceTreeParent, true, true, true);
	}

	public static TreeTableViewer<EObject, EAttribute> createEMFTreeTableViewer(Composite parent, EStructuralFeature structuralFeature, EClass eClass, EditingDomain editingDomain, boolean forceTreeParent, boolean useDefaultSortColumn, boolean isScrollable, boolean editable) {
		EPackage ePackage = eClass.getEPackage();
		EObject eObject = ePackage.getEFactoryInstance().create(eClass);
		AdapterFactory domainAdapterFactory = EMFUtils.getAdapterFactory(editingDomain);
		IItemPropertySource source = (IItemPropertySource) domainAdapterFactory.adapt(eObject, IItemPropertySource.class);

		List<ITreeTableColumn> columns = new ArrayList<ITreeTableColumn>();
		for (final IItemPropertyDescriptor descriptor : source.getPropertyDescriptors(eObject)) {
			if (!isVisible(descriptor.getFeature(eObject))) {
				continue;
			}
			DetailProviderParameter parameter = new DetailProviderParameter();
			parameter.setPropertyDescriptor(descriptor);
			parameter.setTarget(eObject);
			ITreeTableColumn column = null;
			IEMFTreeTableProvider provider = EMFUtils.adapt(eObject, IEMFTreeTableProvider.class);
			if (provider != null) {
				column = provider.getTreeTableColumn(parameter);
			}
			if (column == null) {
				String displayName = EMFDetailUtils.getDisplayName(eObject, descriptor);
				int columnWidth = getColumnWidth(eObject, descriptor);
				column = new ValidatedReferenceTreeTableColumn(parameter, displayName, columnWidth, editable);
			}
			columns.add(column);
		}
		
		TreeTableColumnConfiguration configuration = new TreeTableColumnConfiguration(columns.get(0), columns, (useDefaultSortColumn) ? columns.get(0) : null);
		TreeTableComposite composite = new TreeTableComposite(parent, configuration, useDefaultSortColumn, isScrollable);
		// this odd "if" preserves existing semantics that conflated sorting with layouts
		if (useDefaultSortColumn) {
			composite.setLayout(new FillLayout());
		} else {
			composite.setLayout(new TreeTableColumnLayout(true));
		}
		TreeTableViewer<EObject, EAttribute> viewer = new ReflowingTreeTableViewer<EObject, EAttribute>(composite, configuration, null);
		
		if(forceTreeParent) {
			viewer.getTree().setParent(parent);
		}
		
		AdapterFactory adapterFactory = EMFUtils.getAdapterFactory(editingDomain);
		viewer.setEditorModel(new IEnsembleEditorModel.STUB(new ObjectUndoContext(editingDomain)));
		viewer.setContentProvider(new EMFTreeTableContentProvider(adapterFactory, structuralFeature, eClass));
		viewer.setLabelProvider(new EMFTreeTableLabelProvider(adapterFactory));
		return viewer;		
	}

	/**
	 * Check the visibility of the annotation WRT the annotations, there is a lot of capability repeated here acros
	 * the details, ParameterDescritpor, etc, which we should clean up an unify
	 * @param feature
	 * @return
	 */
	private static boolean isVisible(Object feature) {
		if (feature instanceof EModelElement) {
			EModelElement modelElement = (EModelElement) feature;
			String valueString = EMFUtils.getAnnotation(modelElement, ANNOTATION_SOURCE_TABLE, "visible"); // TODO: Move ParameterDescriptor to core
			if (valueString != null && valueString.equalsIgnoreCase("FALSE")) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean canAdd(EReference feature) {
		String valueString = EMFUtils.getAnnotation(feature, ANNOTATION_SOURCE_TABLE, "add");
		if (valueString != null && valueString.equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}
	
	public static boolean canRemove(EReference feature) {
		String valueString = EMFUtils.getAnnotation(feature, ANNOTATION_SOURCE_TABLE, "remove");
		if (valueString != null && valueString.equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

	protected static int getColumnWidth(EObject eObject, final IItemPropertyDescriptor descriptor) {
		int columnWidth = 100;
		Object feature = descriptor.getFeature(eObject);
		if (feature instanceof EModelElement) {
			EModelElement element = (EModelElement) feature;
			String valueString = EMFUtils.getAnnotation(element, ANNOTATION_SOURCE_TABLE, ANNOTATION_DETAIL_COLUMN_WIDTH);
			if (valueString != null) {
				try {
					columnWidth = Integer.parseInt(valueString);
				} catch (Exception e) {
					LogUtil.error("cannot parse '"+valueString+"' for "+element);
				}
			}
		}
		return columnWidth;
	}
	
	/**
	 * Class to support refreshing the underlying SharedScrolledComposite when the table changes.
	 */
	protected static class ReflowingTreeTableViewer<O, F> extends TreeTableViewer<O, F> {

		public ReflowingTreeTableViewer(
				TreeTableComposite treeComposite,
				TreeTableColumnConfiguration configuration,
				IWorkbenchPartSite site) {
			super(treeComposite, configuration, site);
		}
		
		@Override
		protected void internalRefresh(Widget widget, Object element,
				boolean doStruct, boolean updateLabels) {
			super.internalRefresh(widget, element, doStruct, updateLabels);
			IEditorPart current = EditorPartUtils.getCurrent(getSite());
			if(current != null) {
				Object adapter = current.getAdapter(SharedScrolledComposite.class);
				if(adapter instanceof SharedScrolledComposite) {
					SharedScrolledComposite composite = (SharedScrolledComposite) adapter;
					boolean flushCache = true;
					composite.reflow(flushCache);
				}
			}
		}		
	}
}
