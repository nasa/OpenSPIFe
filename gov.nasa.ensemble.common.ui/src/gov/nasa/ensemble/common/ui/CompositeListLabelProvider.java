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

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Composite;

public abstract class CompositeListLabelProvider implements IBaseLabelProvider {

    private ListenerList listeners = new ListenerList(ListenerList.IDENTITY);

	/**
	 * Creates a Composite representing the provided element within the provided parent.
	 * 
	 * @param parent container for the Composite we should build
	 * @param element the thing we are building a Composite to visualize
	 */
	public abstract ICompositeListLabel addLabelForElement(Composite parent, Object element);

	/// Just implementation of IBaseLabelProvider IF below here - not really used.
	
    @Override
	public void dispose() {
    	// override and implement if necessary
    }
	
	/**
     * Method declared on IBaseLabelProvider.
     */
    @Override
	public void addListener(ILabelProviderListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires a label provider changed event to all registered listeners
     * Only listeners registered at the time this method is called are notified.
     *
     * @param event a label provider changed event
     *
     * @see ILabelProviderListener#labelProviderChanged
     */
    protected void fireLabelProviderChanged(final LabelProviderChangedEvent event) {
        Object[] listeners = this.listeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final ILabelProviderListener l = (ILabelProviderListener) listeners[i];
            SafeRunnable.run(new SafeRunnable() {
                @Override
				public void run() {
                    l.labelProviderChanged(event);
                }
            });
        }
    }

    /**
     * The <code>LabelProvider</code> implementation of this 
     * <code>IBaseLabelProvider</code> method returns <code>true</code>. Subclasses may 
     * override.
     */
    @Override
	public boolean isLabelProperty(Object element, String property) {
        return true;
    }

    /**
     * Method declared on IBaseLabelProvider.
     */
    @Override
	public void removeListener(ILabelProviderListener listener) {
        listeners.remove(listener);
    }

}

