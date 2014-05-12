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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.internal.navigator.NavigatorDecoratingLabelProvider;
import org.eclipse.ui.internal.navigator.extensions.NavigatorContentDescriptor;
import org.eclipse.ui.internal.navigator.extensions.NavigatorContentExtension;
import org.eclipse.ui.internal.navigator.extensions.SafeDelegateCommonLabelProvider;
import org.eclipse.ui.navigator.INavigatorContentDescriptor;

@SuppressWarnings("restriction")
public class EnsembleNavigatorDecoratingLabelProvider extends NavigatorDecoratingLabelProvider {

	private NavigatorContentService contentService;
	private Map<INavigatorContentDescriptor, ILabelProvider> labelProviderMap = new HashMap<INavigatorContentDescriptor, ILabelProvider>();
	
	public EnsembleNavigatorDecoratingLabelProvider(NavigatorContentService contentService) {
		super(contentService.createCommonLabelProvider());
		this.contentService = contentService;
	}
	
	/**
	 * Get the text displayed in the tool tip for object.
	 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return the {@link String} or <code>null</code> if there is not text to
	 *         display
	 */
	@Override
	public String getToolTipText(Object element) {
		Collection contentExtensions = contentService.findPossibleLabelExtensions(element);
		for (Iterator itr = contentExtensions.iterator(); itr.hasNext(); ) {
			NavigatorContentExtension extension = (NavigatorContentExtension) itr.next();
			ILabelProvider provider = extension.getLabelProvider();
			if (provider instanceof SafeDelegateCommonLabelProvider) {
				// can't get inside to get delegate so create another instance
				NavigatorContentDescriptor descriptor = (NavigatorContentDescriptor)extension.getDescriptor();
				provider = labelProviderMap.get(descriptor);
				if (provider == null) {
					try {
						provider = descriptor.createLabelProvider();
						labelProviderMap.put(descriptor, provider);
					} catch (CoreException e) {
						continue;
					}
				}
			}
			if (provider instanceof CellLabelProvider) {
				String text = ((CellLabelProvider)provider).getToolTipText(element);
				if (text != null) {
					return text;
				}
			}
		}
		return null;
	}

}
