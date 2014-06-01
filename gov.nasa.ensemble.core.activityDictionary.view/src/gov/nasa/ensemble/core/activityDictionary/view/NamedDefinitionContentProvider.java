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
package gov.nasa.ensemble.core.activityDictionary.view;

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public abstract class NamedDefinitionContentProvider implements ITreeContentProvider {

	protected static final ActivityDictionary AD = ActivityDictionary.getInstance();
	
	private static final String CATEGORY_UNDEFINED = "Undefined";
	
	/**
	 * A trivial class to represent the "invisible" root node of the activity
	 * dictionary tree. This node is the parent of all other nodes in the tree,
	 * but has not visual representation in the view.
	 */
	protected class InvisibleRoot { /* empty */ }

	
	/** The "invisible" root node of the activity dictionary tree */
	private InvisibleRoot invisibleRoot;
	
	/** Map of the resources by their 'parent' category */
	private Map<String, List<Object>> defsByCategory = new HashMap<String, List<Object>>();

	/** List of all categories in the tree */
	private List<String> categories = new ArrayList<String>();

	
	public NamedDefinitionContentProvider() {
		this.invisibleRoot = new InvisibleRoot();
	}

	protected abstract String getCategory(INamedDefinition def);
	
	protected abstract List<? extends INamedDefinition> getNamedDefinitions();
	
	private void addDefinition(String category, INamedDefinition def) {
		if (category == null) {
			category = CATEGORY_UNDEFINED;
		}
		
		List<Object> list = defsByCategory.get(category);
		if (list != null) {
			list.add(def);
		} else {
			categories.add(category);
			list = new ArrayList<Object>();
			list.add(def);
			defsByCategory.put(category, list);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ContentProvider required methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the child elements of the given parent element.
	 * 
	 * @param parentElement
	 *            the parent element
	 * @return an array of child elements
	 */
	@Override
	public synchronized Object[] getChildren(Object parent) {
		if (parent == invisibleRoot || parent == AD) {
			if (categories == null) {
				return new Object[0];
			}
			return categories.toArray(new String[0]);
		} 
		
		else if (parent instanceof String) {
			List<? extends Object> defs = defsByCategory.get(parent);
			return defs.toArray(new Object[0]);
		}
		
		// All other objects (including INamedDefinition objects
		// are assumed to have no children.
		return new Object[0];
	}

	/**
	 * Returns the parent for the given element, or <code>null</code>
	 * indicating that the parent can't be computed.
	 * 
	 * @param element
	 *            the element
	 * @return the parent element, or <code>null</code> if it has none or if
	 *         the parent cannot be computed
	 */
	@Override
	public Object getParent(Object element) {
		// the root has not parent
		if (element == invisibleRoot || element == AD) {
			return null;
			
		// the category is the child of the root
		} else if (element instanceof String) {
			return invisibleRoot;
			
		// if a named def, get the category
		} else if (element instanceof INamedDefinition) {
			String category = getCategory((INamedDefinition)element);
			if (category == null) {
				category = CATEGORY_UNDEFINED;
			}
			return category;
		}
		
		// in all other cases, a parent cannot be computed
		return null;
	}

	/**
	 * Returns whether the given element has children.
	 * <p>
	 * Intended as an optimization for when the viewer does not need the actual
	 * children. Clients may be able to implement this more efficiently than
	 * <code>getChildren</code>.
	 * </p>
	 * 
	 * @param element
	 *            the element
	 * @return <code>true</code> if the given element has children, and
	 *         <code>false</code> if it has no children
	 */
	@Override
	public synchronized boolean hasChildren(Object element) {
		if (element == invisibleRoot || element == AD) {
			return true;
		} else if (element instanceof String) {
			return defsByCategory.get(element).size() > 0;
		}
		return false;
	}

	/**
	 * Returns the elements to display in the viewer when its input is set to
	 * the given element.  The result is not modified by the viewer.
	 * 
	 * @param inputElement
	 *            the input element (ignored)
	 * @return the array of elements to display in the viewer
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(invisibleRoot);
	}

    /**
	 * Disposes of this content provider. This is called by the viewer when it
	 * is disposed.
	 * 
	 * <p>
	 * The viewer should not be updated during this call, as it is in the
	 * process of being disposed.
	 * </p>
	 */
	@Override
	public void dispose() { /* do nothing */}

    /**
	 * Notifies this content provider that the given viewer's input has been
	 * switched to a different element.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param oldInput
	 *            the old input element, or <code>null</code> if the viewer
	 *            did not previously have an input
	 * @param newInput
	 *            the new input element, or <code>null</code> if the viewer
	 *            does not have an input
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		setTreeViewer((TreeViewer) viewer);
	}
	
	/**
	 * Set the tree viewer (i.e. controller in the MVC paradigm) for this
	 * content provider.
	 * 
	 * The effect of setting the tree viewer is to start a job to load the
	 * contents of the activity dictionary. This method will return as soon as
	 * that job has been scheduled to run.
	 * 
	 * @param treeViewer
	 */	
	private void setTreeViewer(final TreeViewer treeViewer) {
		this.invisibleRoot = new InvisibleRoot();
		
		// create a job to load the contents of the activity dictionary thus
		// guaranteeing that this load will not take place in the UI threadO
		Job load = new Job("Loading Activity Dictionary") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				createTree(treeViewer);
				return Status.OK_STATUS;
			}
		};
		
		// schedule the job
		load.schedule();
	}
	
	/**
	 * Build the internal data structures necessary for displaying a tree
	 * organized by definition categories.
	 * @param treeViewer 
	 */
	private void createTree(final TreeViewer treeViewer) {
		List<? extends INamedDefinition> namedDefinitions = getNamedDefinitions();
		synchronized (this) {
			defsByCategory.clear();
			categories.clear();
			for (INamedDefinition def : namedDefinitions) {
				addDefinition(getCategory(def), def);
			}
			Collections.sort(categories);
		}
		WidgetUtils.runInDisplayThread(treeViewer.getTree(),
			new Runnable() {
				@Override
				public void run() {
					treeViewer.refresh();
				}
			}
		);
	}
	
}
