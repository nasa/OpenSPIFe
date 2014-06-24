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
package gov.nasa.ensemble.common.ui.ide.navigator;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.navigator.CommonViewer;

@SuppressWarnings("restriction")
public class EnsembleNavigatorViewer extends CommonViewer {

	public EnsembleNavigatorViewer(String aViewerId, Composite aParent, int aStyle) {
		super(aViewerId, aParent, aStyle);
	}
	
	/**
	 * Override to use EnsembleNavigatorDecoratingLabelProvider, which is needed to give tooltip support
	 * in the face of Eclipse Common Navigator bug #311897 -- Unable to define tooltip on a CommonViewer
	 */
	@Override
	protected void init() {
		super.init();
		setLabelProvider(new EnsembleNavigatorDecoratingLabelProvider((NavigatorContentService)getNavigatorContentService()));
	}

}
