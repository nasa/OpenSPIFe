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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.text.StringEscapeFormat;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanDeleteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IMarker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormText;

public abstract class Violation extends Advice {

	public static final ImageDescriptor WAIVE_ICON = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/violation_waived_from_activity.gif");
	
	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);

	public Violation(PlanAdvisor advisor) {
		super(advisor);
	}

	/**
	 * Returns the value for the property given by the key
	 */
	public String getPrintString(ViolationKey key) {
		switch (key) {
		case ADVISOR: return getAdvisor().getName();
		case STATUS: return String.valueOf(isCurrentlyViolated());
		case FIXABLE: return String.valueOf(isFixable());
		case TYPE: return getType();
		case NAME: return getName();
		case DESCRIPTION: return getDescription();
		case ELEMENTS: return PlanUtils.getNameAndDateListString(getDeletableElements());
		case CONTAINERS: return PlanUtils.getNameListString(getContainers(getElements()));
		case TIME: return DATE_STRINGIFIER.getDisplayString(getTime());
		case SEVERITY: return getSeverity().getName();
		case PPCR_STATUS: return "";
		}
		return null;
	}
	
	public int compareBy(Violation violation2, ViolationKey key) {
		int result;
		if (key == ViolationKey.TIME) {
			Date time1 = getTime();
			if (time1 == null) {
				time1 = MissionConstants.getInstance().getMissionStartTime();
			}
			Date time2 = violation2.getTime();
			if (time2 == null) {
				time2 = MissionConstants.getInstance().getMissionStartTime();
			}
			result = time1.compareTo(time2);
		} else if (key == ViolationKey.SEVERITY) {
			int level1 = getSeverity().getLevel();
			int level2 = violation2.getSeverity().getLevel();
			result = Float.compare(level1, level2);
		} else {
			String o1 = getPrintString(key);
			String o2 = violation2.getPrintString(key);
			result = String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
		}
		if (result != 0) {
			return result;
		}
		return Double.compare(this.hashCode(), violation2.hashCode());
	}

	public final boolean isOutOfDate() {
		return getAdvisor().isOutOfDate();
	}
	
	/**
	 * Override to return true when a violation is obsolete.
	 * A violation is obsolete if one of the participants, constraints,
	 * etc. has been removed from the plan.
	 */
	public boolean isObsolete() {
		for (EPlanElement element : getElements()) {
			if (EPlanUtils.getPlan(element) == null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Override to return false when a violation is no longer current.
	 * A violation is current if it hasn't been fixed yet, or was fixed
	 * and then the fix was removed.
	 * @return
	 */
	public boolean isCurrentlyViolated() {
		return true;
	}

	/**
	 * Override to return true when a violation is waived because
	 * the participants are waiving the violation in this instance.
	 * @return
	 */
	public boolean isWaivedByInstance() {
		return false;
	}

	/**
	 * Override to return true when a violation is waived because the
	 * rule for this kind of violation has been waived for all cases.
	 * @return
	 */
	public boolean isWaivedByRule() {
		return false;
	}

	/**
	 * Return whether this violation can be fixed by the advisor that
	 * reported it.
	 */
	public boolean isFixable() {
		return false;
	}
	
	/**
	 * Return whether this violation has been selected either directly or because it has
	 * a participating plan element that has been selected
	 * 
	 * @param selectedObjects
	 * @return boolean whether the violation is selected
	 */
	public boolean isSelectedToFix(Set<Object> selectedObjects) {
		List<? extends EPlanElement> elements = getElements();
		for (EPlanElement violationElement : elements) {
			if (selectedObjects.contains(violationElement)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Override to return a string describing the type of violation, 
	 * suitable for printing in the user interface.
	 * Example: "Flight Rule"
	 * Should not be null.
	 * Can be used for grouping violations by type.
	 * @return the type of the violation as a printable string
	 */
	public String getType() {
		return "Generic";
	}
	
	/**
	 * Override to return a short name for this violation,
	 * suitable for printing in the user interface.
	 * Example: "No Exercise", "Earliest", "Ordering", "Science Constraint"
	 * Should not be null.
	 * @return the short name as a printable string
	 */
	public String getName() {
		return "Unknown";
	}
	
	/**
	 * Override to return a complete description of the violation,
	 * suitable for printing in the user interface.
	 * Example: The end of Dinner D1 is before 7pm.
	 * Should not be null.
	 * <p> See SPF-4409 for design (for Score).
	 * @return the complete description as a printable string
	 */
	public String getDescription() {
		return "No description is available for this violation.";
	}
	
	/**
	 * Override to return the message that should be used for the resource
	 * marker associated with the violation
	 * @return the marker message string
	 */
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append(getType());
		builder.append(": ");
		builder.append(getName());
		builder.append(" ");
		builder.append(getDescription());
		List<? extends EPlanElement> elements = getElements();
		if (!elements.isEmpty()) {
			builder.append(" (involves ");
			List<String> strings = new ArrayList<String>(elements.size());
			for (EPlanElement element : elements) {
				strings.add(element.getName());
			}
			builder.append(PlanPrinter.getListText(strings));
			builder.append(")");
		}
		appendTime(builder);
		return builder.toString();
	}
	
	/**
	 * Override to return a list of the participating plan elements.
	 * Should not be null.
	 * @return a list of the participating plan elements
	 */
	public List<? extends EPlanElement> getElements() {
		return Collections.emptyList();
	}
	
	/**
	 * Override to return a time for the violation, if any exists.
	 * May be null if no useful time of day exists.
	 * @return a time for the violation
	 */
	public Date getTime() {
		Date earlierDate = null;
		for (EPlanElement element : getElements()) {
			if (element != null) {
				Date currentStart = element.getMember(TemporalMember.class).getStartTime();
				if (earlierDate == null) {
					earlierDate = currentStart;
				} else if (currentStart != null) {
					earlierDate = DateUtils.earliest(currentStart, earlierDate);
				}
			}
		}
		return earlierDate;
	}

	/**
	 * Override to return a different severity.
	 * @return
	 */
	public ViolationSeverity getSeverity() {
		return ViolationSeverity.ERROR;
	}

	/**
	 * Override to return form text for the violation.  The form text
	 * object is available for calling setColor/setFont.  Do not call
	 * setText on this because it will be ignored.
	 * Example:  Activity Requirement: ALL_S_BAND_AVAIL is required during COMMS-ETF HRD-DL involving COMMS-ETF HRD-DL
	 * @param text
	 * @param identifiableRegistry
	 * @return
	 */
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		PlanPrinter printer = new PlanPrinter(identifiableRegistry);
		StringBuilder builder = new StringBuilder();
		builder.append(StringEscapeFormat.escape(getType()));
		builder.append(": ");
		builder.append(StringEscapeFormat.escape(getName()));
		builder.append(" ");
		builder.append(StringEscapeFormat.escape(getDescription()));
		List<? extends EPlanElement> elements = getElements();
		if (!elements.isEmpty()) {
			builder.append(" (involves ");
			List<String> strings = new ArrayList<String>(elements.size());
			for (EPlanElement element : elements) {
				strings.add(printer.getText(element));
			}
			builder.append(PlanPrinter.getListText(strings));
			builder.append(")");
		}
		appendTime(builder);
		return builder.toString();
	}

	/**
	 * Override to return a set of suggestions for the violation.  The
	 * suggestions should be returned in a meaningful order.  (Don't
	 * use HashSet, use LinkedHashSet or TreeSet, or some other Set
	 * that maintains an order.)  The order will be used for presentation
	 * to the user.
	 * @return
	 */
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		for (EPlanElement element : getDeletableElements()) {
			ImageDescriptor icon = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
			String description = "Delete: " + PlanPrinter.getPrintName(element);
			ITransferable transferable = modifier.getTransferable(new StructuredSelection(element));
			IUndoableOperation operation = new PlanDeleteOperation(transferable, modifier);
			Suggestion suggestion = new Suggestion(icon, description, operation);
			suggestions.add(suggestion);
		}
		return suggestions;
	}

	/**
	 * Override to return a more specific marker type for the violation.
	 * @return
	 */
	public String getMarkerType() {
		return IMarker.PROBLEM;
	}

	/*
	 * Utility methods
	 */
	
	/**
	 * Return the elements that the user can delete to remove each of the elements from the plan
	 * If a subactivity is involved, this will return its containing activity.  For other plan 
	 * elements, it is the plan element itself.  Order is preserved and duplicates are removed.
	 * The first occurrence is kept.  
	 * @return
	 */
	protected Set<EPlanElement> getDeletableElements() {
		LinkedHashSet<EPlanElement> deletableElements = new LinkedHashSet<EPlanElement>();
		for (EPlanElement element : getElements()) {
			while (true) {
				EObject parent = element.eContainer();
				if (parent instanceof EActivity) {
					element = (EPlanElement)parent;
				} else {
					deletableElements.add(element);
					break;
				}
			}
		}
		return deletableElements;
	}
	
	/**
	 * Get containers for the elements
	 * @param elements
	 * @return
	 */
	private static List<EActivityGroup> getContainers(List<? extends EPlanElement> elements) {
		Set<EActivityGroup> parents = new LinkedHashSet<EActivityGroup>();
		for (EPlanElement element : elements) {
			if (element != null) {
				EObject parent = element.eContainer();
				// it's not useful to see the plan as the context.
				// Context is only useful if an activity is inside an ActivityGroup. -- SPF-6950
				if (parent instanceof EActivityGroup) {
					parents.add((EActivityGroup)parent);
				}
			}
		}
		return new ArrayList<EActivityGroup>(parents);
	}

	protected Suggestion createToggleWaiveSuggestion(String objectName, IWaivable waivable) {
		String waiverRationale = waivable.getWaiverRationale();
		String description;
		IUndoableOperation operation;
		ImageDescriptor icon;
		if (waiverRationale != null) {
			description = "Unwaive the " + objectName;
			operation = new RemoveWaiverOperation(description, waivable);
			icon = null;
		} else {
			description = "Waive the " + objectName;
			operation = new CreateWaiverOperation(description, waivable);
			icon = WAIVE_ICON;
		}
		return new Suggestion(icon, description, operation);
	}

	protected void appendTime(StringBuilder builder) {
		if (getTime() != null) {
			builder.append(" at ");
			builder.append(getPrintString(ViolationKey.TIME));
			builder.append(".");
		}
	}

	protected void appendWaiverRationale(StringBuilder result, String waiverRationale) {
		if (waiverRationale != null) {
			boolean requirementPrinted = (result.length() > 0);
			if (requirementPrinted) {
				result.append("<br/>");
			}
			int separatorIndex = waiverRationale.indexOf("::");
			if (separatorIndex > 0) {
				result.append("Waived by ").append(waiverRationale.substring(0, separatorIndex)).append(": ");
				waiverRationale = waiverRationale.substring(separatorIndex + 2);
			} else {
				result.append("Waived: ");
			}
			result.append(StringEscapeFormat.escape(waiverRationale)); 
		}
	}

	public String getPrintStringForParent(ViolationKey key) {
		return "";
	}
	
}
