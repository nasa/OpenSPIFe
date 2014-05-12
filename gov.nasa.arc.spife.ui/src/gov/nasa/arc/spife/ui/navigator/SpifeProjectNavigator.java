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
package gov.nasa.arc.spife.ui.navigator;

import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.ui.ide.navigator.EnsembleCommonNavigator;
import gov.nasa.ensemble.common.ui.ide.navigator.EnsembleFilteredTree;
import gov.nasa.ensemble.common.ui.ide.navigator.EnsemblePatternFilter;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonViewer;

@SuppressWarnings("restriction")
public class SpifeProjectNavigator extends EnsembleCommonNavigator {
	public static final String ID = ClassIdRegistry.getUniqueID(SpifeProjectNavigator.class);
	
	@Override
	public void setFocus() {
		getCommonViewer().getControl().setFocus();
	}
	
	@Override
	protected CommonViewer createCommonViewer(Composite parent) {
		CommonViewer commonViewer = super.createCommonViewer(parent);
		ColumnViewerToolTipSupport.enableFor(commonViewer);
		return commonViewer;
	}
	
	@Override
	protected CommonViewer createCommonViewerObject(Composite aParent) {
			EnsemblePatternFilter filter = new EnsemblePatternFilter();
			EnsembleFilteredTree tree = new EnsembleFilteredTree(aParent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, filter, true, getViewSite());
			return (CommonViewer)tree.getViewer();
	}	
	
}
