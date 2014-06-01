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
package gov.nasa.ensemble.common.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

public class EnsembleSelectionProvider implements ISelectionProvider {
	
	private static final Logger trace = Logger.getLogger(EnsembleSelectionProvider.class);

	private final List<ISelectionChangedListener> selectionListeners = new ArrayList<ISelectionChangedListener>();
	private final ISelectionChangedListener selectionChangedListener = new SelectionChangedListener();
	private final List<ISelectionProvider> notifySelectionProviders = new ArrayList<ISelectionProvider>();
	
    private ISelection theSelection = StructuredSelection.EMPTY;
    private boolean firingSelection = false;

	private final String label;
	
    public EnsembleSelectionProvider() {
		this(null);
    }

    public EnsembleSelectionProvider(String label) {
		this.label = label;
    }

    @Override
    public String toString() {
    	String string = super.toString();
    	if (label != null) {
    		return string + "[" + label + "]";
    	}
    	return string;
    }
    
    /**
     * Convenience method for follow and add
     * 
     * @see follow
     * @see add
     * @param provider
     */
    public void attachSelectionProvider(ISelectionProvider provider) {
    	follow(provider);
    	add(provider);
    }

    /**
     * Make this EnsembleSelectionProvider to listen to the
     * provided ISelectionProvider for changes. 
     * 
     * @see leave
     * @param provider
     */
	public void follow(ISelectionProvider provider) {
    	if (provider instanceof IPostSelectionProvider) {
			IPostSelectionProvider postProvider = (IPostSelectionProvider) provider;
			postProvider.addPostSelectionChangedListener(selectionChangedListener);
    	} else {
    		provider.addSelectionChangedListener(selectionChangedListener);
    	}
	}

	/**
	 * Make this EnsembleSelectionProvider notify the
	 * provided ISelectionProvider when we get a setSelection.
	 * 
	 * @see remove
	 * @param provider
	 */
	public void add(ISelectionProvider provider) {
   		notifySelectionProviders.add(provider);
	}
    
    /**
     * Convenience method for remove and follow
     * 
     * @see follow
     * @see add
     * @param provider
     * @param notifyPost
     */
    public void detachSelectionProvider(ISelectionProvider provider) {
    	leave(provider);
    	remove(provider);
    	theSelection = null;
    }

    /**
     * Set this EnsembleSelectionProvider to no longer
     * follow the provided ISelectionProvider.
     * 
     * @see follow
     * @param provider
     */
	public void leave(ISelectionProvider provider) {
		provider.removeSelectionChangedListener(selectionChangedListener);
    	if (provider instanceof IPostSelectionProvider) {
			IPostSelectionProvider postProvider = (IPostSelectionProvider) provider;
			postProvider.removePostSelectionChangedListener(selectionChangedListener);
    	}
	}

	/**
	 * Set this EnsembleSelectionProvider to no longer
	 * notify the provided ISelectionProvider.
	 * 
	 * @see remove
	 * @param provider
	 */
	public void remove(ISelectionProvider provider) {
		notifySelectionProviders.remove(provider);
	}

	/**
	 * Get the currently selected items in this input
	 * @return the selection
	 */
	@Override
	public ISelection getSelection() {
		return theSelection;
	}
	
	/**
	 * Set the currently selected items in this input
	 * @param selection
	 */
	@Override
	public void setSelection(final ISelection selection) {
		
		//if (!sameSelection(selection, theSelection))
		//we want to scroll to the selected edit part even if the selection is
		//the same...
		{
			SelectionUtils.logSelection(trace, "setSelection()", selection);
//			System.out.println(this + " set " + selection);
			theSelection = selection;
			fireSelectionChangedEvents(selection, selectionListeners, notifySelectionProviders);
		}
	}
	
	/**
	 * Add a listener to be notified for every change to the selection.
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (!selectionListeners.contains(listener)) {
			selectionListeners.add(listener);
		}
	}
	
	/**
	 * Remove a listener to be notified for every change to the selection.
	 */
	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}
	
	/**
	 * Returns true if two selections are equivalent.
	 * 
	 * @param selection1
	 * @param selection2
	 * @return true if two selections are equivalent.
	 */
	public static boolean sameSelection(ISelection selection1, ISelection selection2) {
		if(selection1 == null ) return selection2 == null;
		return selection2 != null && (selection1.equals(selection2) || selection2.equals(selection1));
	}
	
	/*
	 * Utility functions 
	 */

	private void fireSelectionChangedEvents(final ISelection selection, List<ISelectionChangedListener> listeners, List<ISelectionProvider> selectionProviders) {
		firingSelection = true;
		final SelectionChangedEvent e = new SelectionChangedEvent(this, selection);
		Object[] listenersArray = listeners.toArray();
        for (int i = 0; i < listenersArray.length; i++) {
            final ISelectionChangedListener l = (ISelectionChangedListener) listenersArray[i];
            SafeRunner.run(new SafeRunnable() {
                @Override
				public void run() {
                    l.selectionChanged(e);
                }
            });
		}
		Object[] providersArray = selectionProviders.toArray();
        for (int i = 0; i < providersArray.length; i++) {
            final ISelectionProvider provider = (ISelectionProvider) providersArray[i];
            SafeRunner.run(new SafeRunnable() {
                @Override
				public void run() {
                	ISelection providerSelection = provider.getSelection();
					if (!sameSelection(providerSelection, selection)) {
                		provider.setSelection(selection);
                	}
                }
            });
		}
		firingSelection = false;
	}
	
	private class SelectionChangedListener implements ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (!firingSelection) {
				setSelection(event.getSelection());
			}
		}
	}
	
}
