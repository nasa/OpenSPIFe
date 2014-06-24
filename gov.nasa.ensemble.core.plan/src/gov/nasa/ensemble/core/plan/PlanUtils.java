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
package gov.nasa.ensemble.core.plan;

import static fj.data.List.*;
import static gov.nasa.ensemble.common.functional.Functions.*;
import fj.F;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.DefaultComparator;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.model.plan.util.PlanElementCopier;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.emf.transaction.FixedTransactionEditingDomain;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public class PlanUtils {
	
	public static final String PLAN_FILE_EXTENSION = "plan";

	public static final boolean ALLOW_GENERALIZED_HEIRARCHY_NESTING_EDITS = true;

	private final static String TMP_PROJECT_NAME = "tmp";
	private final static String TMP_FILE_NAME_SUFFIX = ".pln";
	
	public static final String TYPE_NAME_FOR_A_GROUP = EnsembleProperties.getStringPropertyValue("ensemble.plan.activityGroupName", "Group");
	public static final String TYPE_NAME_FOR_AN_ACTIVITY = "Activity";
	
	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	
	public static final Comparator<EPlanElement> INHERENT_ORDER = new Comparator<EPlanElement>() {
		@Override
		public int compare(EPlanElement o1, EPlanElement o2) {
			List<EPlanElement> path1 = getPath(o1);
			List<EPlanElement> path2 = getPath(o2);
			Iterator<EPlanElement> iterator1 = path1.iterator();
			Iterator<EPlanElement> iterator2 = path2.iterator();
			EPlanElement parent = null;
			while (iterator1.hasNext() && iterator2.hasNext()) {
				EPlanElement e1 = iterator1.next();
				EPlanElement e2 = iterator2.next();
				if (e1 != e2) {
					if (parent == null) {
						break;
					}
					int index1 = parent.getChildren().indexOf(e1);
					int index2 = parent.getChildren().indexOf(e2);
					return CommonUtils.compare(index1, index2);
				}
				parent = e1;
			}
			Logger trace = Logger.getLogger(PlanUtils.class);
			trace.debug("INHERENT_ORDER: fallback");
			return DefaultComparator.INSTANCE.compare(o1, o2);
		}

		private List<EPlanElement> getPath(EPlanElement o1) {
			List<EPlanElement> path = new ArrayList<EPlanElement>();
			EPlanElement pathElement = o1;
			while (pathElement instanceof EPlanChild) {
				EPlanChild child = (EPlanChild) pathElement;
				path.add(pathElement);
				pathElement = child.getParent();
			}
			return path;
        }
	};

	public static final Comparator<EPlanElement> START_TIME_ORDER = new Comparator<EPlanElement>() {
		@Override
		public int compare(EPlanElement o1, EPlanElement o2) {
			Date startTime1 = o1.getMember(TemporalMember.class).getStartTime();
			Date startTime2 = o2.getMember(TemporalMember.class).getStartTime();
			if (startTime1 == startTime2) {
				return 0;
			}
			if (startTime1 == null) {
				return -1;
			}
			if (startTime2 == null) {
				return 1;
			}
			return startTime1.compareTo(startTime2);
		}
	};

	public static final Comparator<EPlanElement> NAME_ORDER = new Comparator<EPlanElement>() {
		@Override
		public int compare(EPlanElement o1, EPlanElement o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	/**
	 * Get the corresponding icon for the given plan element,
	 * regardless of type.
	 * @param pe
	 * @return
	 */
	public static Image getIcon(EPlanElement pe) {
		if (pe != null) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(pe);
			if (domain != null) {
				AdapterFactory factory = ((AdapterFactoryEditingDomain)domain).getAdapterFactory();
				if (factory != null) {
					ILabelProvider lp = (ILabelProvider) factory.adapt(pe, ILabelProvider.class);
					if (lp != null) {
						Image image = lp.getImage(pe);
						lp.dispose();
						return image;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Adding some activities into the target with the given semantics.
	 * @param target
	 * @param semantics
	 * @return the location where the activities will be added
	 */
    public static PlanElementState getAddLocationForActivities(EPlanElement target, InsertionSemantics semantics) {
    	return getAddLocation(target, semantics);
	}

	/**
	 * Adding some activity groups into the target with the given semantics.
	 * @param target
	 * @param semantics
	 * @return the location where the activity groups will be added
	 */
    public static PlanElementState getAddLocationForActivityGroups(EPlanElement target, InsertionSemantics semantics) {
    	return getAddLocation(target, semantics);
	}

	private static PlanElementState getAddLocation(EPlanElement target, InsertionSemantics semantics) {
	    EPlanParent parent = null;
	    Integer index = null;
	    if (target instanceof EPlan) {
	    	EPlan plan = (EPlan) target;
	    	parent = plan;
	    	index = plan.getChildren().size();
	    }
	    if (target instanceof EActivityGroup) {
	    	EActivityGroup group = (EActivityGroup) target;
	    	switch (semantics) {
	    	case BEFORE:
	    		parent = group.getParent();
	    		index = group.getListPosition();
	    		break;
	    	case ON:
	    		parent = group;
	    		index = group.getChildren().size();
	    		break;
	    	case AFTER:
	    		parent = group.getParent();
	    		index = group.getListPosition() + 1;
	    		break;
	    	}
	    }
	    if (target instanceof EActivity) {
	    	EActivity activity = (EActivity) target;
	    	EActivity child = activity;
	    	EPlanElement element = child.getParent();
	    	while (element instanceof EActivity) {
	    		if (child.getListPosition() < element.getChildren().size() - 1) {
	    			break;
	    		}
	    		child = (EActivity)element;
	    		element = child.getParent();
	    	}
	    	if (element instanceof EPlanParent) {
	    		parent = (EPlanParent)element;
	    		if (InsertionSemantics.BEFORE == semantics) {
	    			index = child.getListPosition();
	    		} else {
	    			index = child.getListPosition() + 1;
	    		}
	    	}
	    }
	    if ((parent == null) || (index == null)) {
	    	return null;
	    }		
	    return new PlanElementState(parent, index);
    }

    public static void addElementsAtIndices(Map<EPlanParent, Set<EPlanChild>> parentToNewChildren,
			Map<EPlanChild, Integer> childToIndex) {
		for (Map.Entry<EPlanParent, Set<EPlanChild>> entry : parentToNewChildren.entrySet()) {
			EPlanParent currentParent = entry.getKey();
			Set<EPlanChild> elements = entry.getValue();
			int currentIndex = -2;
			int lastIndex = -2;
			List<EPlanChild> currentElements = new ArrayList<EPlanChild>(elements.size());
			Iterator<? extends EPlanChild> elementsItr = elements.iterator();
			while (elementsItr.hasNext()) {
				EPlanChild element = elementsItr.next();
				Integer index = childToIndex.get(element);
				if (index == null) {
					Logger trace = Logger.getLogger(PlanUtils.class);
					trace.warn("missing index for element, skipping: " + getElementNameForDisplay(element));
					continue;
				}
				if (index != lastIndex + 1) {
					// time to start a new collection
					if (!currentElements.isEmpty()) {
						// add what we had up to this point
						addElementsHere(currentParent, currentIndex, currentElements);
						currentElements.clear();
					}
					currentIndex = index;
				}
				currentElements.add(element);
				lastIndex = index;
			}
			if (!currentElements.isEmpty()) {
				addElementsHere(currentParent, currentIndex, currentElements);
			}
		}
	}

	public static void addElementsHere(EPlanParent parent, int index, List<? extends EPlanChild> children) {
		parent.getChildren().addAll(index, children);
	}

	// since we no longer guarantee that elements with the same parent are grouped,
	// this implementation may be less efficient than possible
	public static void removeElements(Collection<? extends EPlanElement> elements) {
		EPlanElement currentParent = null;
		List<EPlanElement> currentChildren = new ArrayList<EPlanElement>(elements.size());
		for (EPlanElement element : elements) {
			EPlanElement parent = (EPlanElement)element.eContainer();
			if (parent != currentParent) {
				// time to start a new collection
				if (!currentChildren.isEmpty()) {
					// remove what we had up to this point
					removeChildrenHere(currentParent, currentChildren);
					currentChildren.clear();
				}
				currentParent = parent;
			}
			currentChildren.add(element);
		}
		if (!currentChildren.isEmpty()) {
			removeChildrenHere(currentParent, currentChildren);
		}
	}
	
	private static void removeChildrenHere(EPlanElement parent, List<EPlanElement> children) {
		if (parent instanceof EActivityGroup) {
			EActivityGroup group = (EActivityGroup) parent;
			group.getChildren().removeAll(children);
		} else if (parent instanceof EPlan) {
			EPlan plan = (EPlan) parent;
			plan.getChildren().removeAll(children);
		} else if (parent == null) {
			throw new NullPointerException("missing parent in removeChildrenHere");
		} else {
			throw new IllegalArgumentException("unexpected parent type in removeChildrenHere");
		}
	}

	public static PlanElementState getCurrentState(EPlanElement element) {
		EObject container = element.eContainer();
		if (!(container instanceof EPlanParent)) {
			return null;
		}
		EPlanParent parent = (EPlanParent)container;
		int position = 0;
		if (element instanceof EPlanChild) {
			EPlanChild child = ((EPlanChild)element);
			position = child.getListPosition();
		}
		return new PlanElementState(parent, position);
	}
	
	/**
	 * Returns a list of node names in print form.
	 */
	public static String getNameListString(Iterable<? extends EPlanElement> elements) {
		StringBuilder builder = new StringBuilder();
		Iterator<? extends EPlanElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			EPlanElement element = iterator.next();
			if (element == null) {
				continue;
			}
			builder.append(getElementNameForDisplay(element));
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

	/**
	 * Returns a list of node names and start dates in print form.
	 * @since SPF-6951
	 */
	public static String getNameAndDateListString(Iterable<EPlanElement> elements) {
		StringBuilder builder = new StringBuilder();
		Iterator<EPlanElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			EPlanElement element = iterator.next();
			if (element == null) {
				continue;
			}
			builder.append(getElementNameForDisplay(element));
			Date startTime = element.getMember(TemporalMember.class).getStartTime();
			if (startTime != null) {
				builder.append(" — "); // aka " \u2013 "
				builder.append(DATE_STRINGIFIER.getDisplayString(startTime));
			}
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

    /**
	 * Returns the local file representing the plan. creates a temp file if 
	 * a local file is not found.
	 * 
	 * @return the local file representing the plan. create a new file if one 
	 * does not exist.
	 */
	public static IFile getFile(EPlan plan) {
		IFile existingFile = (IFile)plan.getAdapter(IFile.class);
		if (existingFile != null) {
			return existingFile;
		}
		// determine the prefix and suffix for the complete file name with extension)
		String planFileNamePrefix = String.valueOf(plan.hashCode());
		// here we have established the full name of the file
		final String fullPlanFileName = planFileNamePrefix + TMP_FILE_NAME_SUFFIX;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Path path = new Path(TMP_PROJECT_NAME + System.getProperty("file.separator") + fullPlanFileName);
		final IFile file = root.getFile(path);
		if (file.exists()) {
			try {
				new IWorkspaceRunnable() {
					@Override
					public void run(IProgressMonitor monitor) throws CoreException {
						file.delete(true, null);
					}
				}.run(null);
			} catch (CoreException e) {
				Logger trace = Logger.getLogger(PlanUtils.class);
				trace.error(e);
			}
			return file;
		}
		return null;
	}
	
	/**
	 * Calls {@link EMFUtils} getProject method
	 * @deprecated use EMFUtils.getProject(EObject) instead
	 */
	@Deprecated
    public static IProject getProject(EPlan plan) {
    	return EMFUtils.getProject(plan);
    }
    
	public static final F<IResource, Boolean> isPlanFile = new F<IResource, Boolean>() {
		@Override
		public Boolean f(final IResource rsrc) {
			return rsrc instanceof IFile && PLAN_FILE_EXTENSION.equals(rsrc.getFileExtension());
		}
	};

	public static fj.data.List<IFile> getPlanFiles(IProject project) throws CoreException {
		return list(project.members()).filter(isPlanFile).map(cast(IResource.class, IFile.class));
	}
	
	public static void executeExpansionOperation(EPlanElement element, boolean expanded) {
		CommonMember member = element.getMember(CommonMember.class);
		String action = (expanded ? "expanded" : "collapsed");
		IUndoableOperation operation = new FeatureTransactionChangeOperation(action + " " + getElementNameForDisplay(element), member, PlanPackage.Literals.COMMON_MEMBER__EXPANDED, expanded);
		try {
			InternalTransaction existingTransaction = null;
			TransactionalEditingDomain te = TransactionUtils.getDomain(element);
			if (te instanceof FixedTransactionEditingDomain) {
				FixedTransactionEditingDomain domain = (FixedTransactionEditingDomain) te;
				existingTransaction = domain.getThreadTransaction();
			};
			if (existingTransaction != null && !existingTransaction.isReadOnly()) {
				LogUtil.warn("There is an existing transaction executing so the expansion operation cannot be executed at this point");
				return;
			}				
			operation.execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			LogUtil.error(e);
		}
	}
	

	/** Clones a plan using the sure but slow method of serializing and deserializing it.
	 * @see PlanElementCopier */
	public static EPlan duplicatePlan(EPlan plan, ResourceSet resourceSet) {
				
		Map<String, Boolean> options = new HashMap<String, Boolean>();
		options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, false);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Resource oldPlanResource = new PlanResourceImpl(URI.createURI("http://junit/in.plan"));
			resourceSet.getResources().add(oldPlanResource);
			oldPlanResource.getContents().add(plan);
			oldPlanResource.save(out, options);
			String planString = new String(out.toByteArray());
			out.close();
			
			ByteArrayInputStream in = new ByteArrayInputStream(planString.getBytes());
			Resource newPlanResource = new PlanResourceImpl(URI.createURI("http://junit/out.plan"));
			resourceSet.getResources().add(newPlanResource);
			newPlanResource.load(in, options);
			in.close();
			return (EPlan) newPlanResource.getContents().get(0);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getElementNameForDisplay(EPlanElement element) {
		String name = element.getName();
		if (name != null && !name.isEmpty()) {
			return name;
		}
		else {
			String typeName = "plan element";
			if (element instanceof EActivity) typeName = TYPE_NAME_FOR_AN_ACTIVITY.toLowerCase();
			else if (element instanceof EActivityGroup) typeName = TYPE_NAME_FOR_A_GROUP.toLowerCase();
			// tried adding <i>...</i> but it causes the whole thing to not be printed.
			return "unnamed " + typeName;
		}
	}

}
