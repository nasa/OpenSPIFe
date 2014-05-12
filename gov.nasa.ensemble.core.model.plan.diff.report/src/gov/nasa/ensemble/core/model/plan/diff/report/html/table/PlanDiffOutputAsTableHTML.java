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
package gov.nasa.ensemble.core.model.plan.diff.report.html.table;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.info.UserExtendedNameProvider;
import gov.nasa.ensemble.core.jscience.util.DurationStringifier;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingNewElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByMovingChild;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedConstraintOrProfile;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.report.html.HTMLStaticContent;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.constraints.TemporalConstraintPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

import org.apache.html.dom.HTMLDOMImplementationImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;

public class PlanDiffOutputAsTableHTML {
	
	protected diff_match_patch textDiffEngine = new diff_match_patch();

	private static final String CHARSET_NAME = "UTF-8";

	private String activityGroupNameSingular = EnsembleProperties.getProperty("ensemble.plan.activityGroupName", "activity group");
	
	protected HTMLDOMImplementationImpl documentObjectModel = (HTMLDOMImplementationImpl) HTMLDOMImplementationImpl.getHTMLDOMImplementation();
	
	public static final HTMLDOMImplementationImpl DOM = (HTMLDOMImplementationImpl) HTMLDOMImplementationImpl.getHTMLDOMImplementation(); //SPF-8671 debugging
	
	protected DurationStringifier durationStringifier = new DurationStringifier();

	private static final String[] MAIN_FIELDS_FOR_ACTIVITY = EnsembleProperties.getStringArrayPropertyValue(
			"ensemble.plan.diff.additions.main.attributes", new String[]{"Ops_Notes", "Execution_Notes"});
	private static final String[] POPUP_FIELDS_FOR_ACTIVITY = EnsembleProperties.getStringArrayPropertyValue(
			"ensemble.plan.diff.popup.main.attributes", new String[]{"startTime", "endTime", "duration" ,"crewMembers"});

	protected HTMLDocument document = null;
	protected HTMLBodyElement body;
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.model.plan.diff.report.html.table.IReportWriter#writeToStream(java.util.List, gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList, gov.nasa.ensemble.core.model.plan.EPlan, gov.nasa.ensemble.core.model.plan.EPlan, java.lang.String, java.io.PrintStream)
	 */
	public void writeToStream(PlanDiffList differenceInfo, EPlan plan1, EPlan plan2, String summaryOfUpdates, 
			OutputStream out) throws IOException {
		writeDocument(new OutputStreamWriter(out, CHARSET_NAME),
				createDocument(differenceInfo, plan1, plan2, summaryOfUpdates));
		out.close();	
		}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.model.plan.diff.report.html.table.IReportWriter#createDocument(gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList, gov.nasa.ensemble.core.model.plan.EPlan, gov.nasa.ensemble.core.model.plan.EPlan, java.lang.String)
	 */
	public HTMLDocument createDocument(PlanDiffList differences, EPlan plan1, EPlan plan2, String summaryOfUpdates) {
		String title = "Plan diff";
		document = documentObjectModel.createHTMLDocument(title);
		adjustTextDiffParameters();
//		LogUtil.debug(documentObjectModel + " is class " + documentObjectModel.getClass());
//		LogUtil.debug("Static " + documentObjectModel + " is class " + documentObjectModel.getClass());
		body = (HTMLBodyElement) document.getBody();
		Node head = document.getFirstChild().getFirstChild();

		try {
			head.appendChild(getCharsetElement()); 
			// This simplified, non-DOM-browser-friendly version uses no scripts.
//			head.appendChild(element("SCRIPT", HTMLStaticContent.getStaticFileContents("table/script.js")));
			head.appendChild(element("STYLE", HTMLStaticContent.getStaticFileContents("table/style.css")));
			body.appendChild(makeHeading(plan1));
			if (summaryOfUpdates != null && !summaryOfUpdates.isEmpty()) {
				makeSummaryOfUpdates(summaryOfUpdates);
				body.appendChild(element("H2", "Details:"));
			}
			List<PlanChange> diffsWorthMentioning = new LinkedList();
			
			diffsWorthMentioning.addAll(differences.getAdditions());
			diffsWorthMentioning.addAll(differences.getDeletions());
			diffsWorthMentioning.addAll(differences.getParameterChanges());
			Collections.sort(diffsWorthMentioning, new SortByStartTime());
			
			body.appendChild(mainTableOfDifferences(diffsWorthMentioning));
			
			describeAnyOtherChanges(differences, !diffsWorthMentioning.isEmpty());
			
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	protected void adjustTextDiffParameters() {
		textDiffEngine.Diff_Timeout = .25f;
	}

	private Element mainTableOfDifferences(Collection<? extends PlanChange> diffs) {
		if (diffs.isEmpty()) return emptyElement("DIV");
		Element table = emptyElement("TABLE");
		Element header = emptyElement("TR");
		header.appendChild(element("TH", "Update"));
		header.appendChild(element("TH", "Before"));
		header.appendChild(element("TH", "After"));
		table.appendChild(header);
		EPlanElement lastActivityMentioned = null;
		for (PlanChange diff : diffs) {
			EPlanElement activity = PlanDiffUtils.getOwner(diff);
			if (activity != lastActivityMentioned) {
				lastActivityMentioned = activity;
				table.appendChild(makeActivityNameRow(activity));
			}
			Element row = makeRowForDiff(diff);
			if (row != null) {
				table.appendChild(row);
			}
		}
		return table;
	}

	private Element makeActivityNameRow(EPlanElement activity) {
		Element row = emptyElement("TR", "activity-heading");
		Element cell = element("TD", activity.getName(), "activity-heading");
		cell.setAttribute("colspan", "3");
		cell.setAttribute("title", "Click for details");
		cell.setAttribute("onclick", makePopupCommand(makeActivityDetailsStringForPopup(activity)));
		row.appendChild(cell);
		return row;
	}

	private Element makeRowForDiff(PlanChange diff) {
		if (diff instanceof ChangedByAddingNewElement) {
			return makeRowForSpecificDiff((ChangedByAddingNewElement) diff);
		}
		if (diff instanceof ChangedByRemovingElement) {
			return makeRowForSpecificDiff((ChangedByRemovingElement) diff);
		}
		if (diff instanceof ChangedByMovingChild) {
			return makeRowForSpecificDiff((ChangedByMovingChild) diff);
		}
		if (diff instanceof ChangedByModifyingParameter) {
			return makeRowForSpecificDiff((ChangedByModifyingParameter) diff);
		}
		if (diff instanceof ChangedConstraintOrProfile) {
			return makeRowForSpecificDiff((ChangedConstraintOrProfile) diff);
		}
		throw new IllegalArgumentException(diff.getClass().getSimpleName());
	}

	private Element makeRowForSpecificDiff(ChangedByAddingNewElement diff) {
		Element row = emptyElement("TR", "addition");
		Element cell = emptyElement("TD");
		cell.setAttribute("colspan", "3");
		String what = "Activity";
		EPlanChild addedElement = diff.getAddedElement();
		if (addedElement instanceof EActivityGroup) {
			what = activityGroupNameSingular;
		}
		cell.appendChild(element("DIV", element("INS", what  + " Added")));
		cell.appendChild(element("P", formatTimeRange(addedElement.getMember(TemporalMember.class))));
		for (String parameterName : MAIN_FIELDS_FOR_ACTIVITY) {
			try {
				EStructuralFeature feature = ADParameterUtils.getParameterFeature(addedElement, parameterName);
				String name = PlanDiffUtils.convertFromCamelToPretty(PlanDiffUtils.getObjectName(feature));
				String valueString = PlanDiffUtils.getDisplayString(feature, addedElement.getData().eGet(feature));
				Element paragraph = element("P", element("SPAN", name + ": ", "parameter-name"), "parameter");
				Element valueElement = formatMultiLineString(valueString, false);
				valueElement.setAttribute("class", "value");
				paragraph.appendChild(valueElement);
				cell.appendChild(paragraph);
			} catch (UndefinedParameterException e) {
				// Just skip anything undefined.
			}
		}
		row.appendChild(cell);
		return row;
	}
	
	private String makePopupCommand(String content) {
		return "alert('" + content.replaceAll("\"|'", "").replaceAll("\n", "\\\\n")
				+ "')"; 
	}
	
	private String makeActivityDetailsStringForPopup(EPlanElement planElement) {
		StringBuilder s = new StringBuilder(planElement.getName());
		for (String parameterName : POPUP_FIELDS_FOR_ACTIVITY) {
			String name = PlanDiffUtils.convertFromCamelToPretty(parameterName);
			if ("Start Time".equals(name)) {
				s.append('\n');
				s.append(name);
				s.append(": ");
				s.append(formatDate(ReportDateFormats.POPOVER, planElement.getMember(TemporalMember.class).getStartTime()));
			}
			if ("End Time".equals(name)) {
				s.append('\n');
				s.append(name);
				s.append(": ");
				s.append(formatDate(ReportDateFormats.POPOVER, planElement.getMember(TemporalMember.class).getEndTime()));
			}
			if ("Duration".equals(name)) {
				s.append('\n');
				s.append(name);
				s.append(": ");
				s.append(durationStringifier.getDisplayString(planElement.getMember(TemporalMember.class).getDuration()));
			}
			else try {
				EStructuralFeature feature = ADParameterUtils.getParameterFeature(planElement, parameterName);
				String valueString = PlanDiffUtils.getDisplayString(feature, planElement.getData().eGet(feature));
				s.append('\n');
				s.append(name);
				s.append(": ");
				s.append(valueString);

			} catch (UndefinedParameterException e) {
				// Just skip anything undefined.
			}
		}
		return s.toString();
	}

	private String formatTimeRange(TemporalMember member) {
		StringBuilder s = new StringBuilder();
		s.append(formatDate(ReportDateFormats.ADDITIONS_START, member.getStartTime()));
		s.append(" to ");
		s.append(formatDate(ReportDateFormats.ADDITIONS_END, member.getEndTime()));
		s.append(" (");
		s.append(DurationStringifier.toEnglish(member.getDuration()));
		s.append(")");
		return s.toString();
	}

	private Element makeRowForSpecificDiff(ChangedByRemovingElement diff) {
		Element row = emptyElement("TR", "removal");
		Element cell = emptyElement("TD");
		String what = "Activity";
		if (diff.getRemovedElement() instanceof EActivityGroup) {
			what = activityGroupNameSingular;
		}
		cell.appendChild(element("DEL", what  + " Removed"));
		cell.setAttribute("colspan", "3");
		row.appendChild(cell);
		return row;
	}

	private Element makeRowForSpecificDiff(ChangedByMovingChild diff) {
		// Not currently called -- TODO: would be easy enough to show names of new and old parent (or "top-level").
		Element row = emptyElement("TR", "move");
		row.appendChild(element("TD", "Move not implemented in report"));
		return row;
	}

	private Element makeRowForSpecificDiff(ChangedByModifyingParameter diff) {
//		modifiedParametersAlreadyListed.add(diff.getParameter());
		Element row = emptyElement("TR", "modification");
		Element cell = emptyElement("TD");
		row.appendChild(element("TD", PlanDiffUtils.convertFromCamelToPretty(diff.getParameter().getName())));
		if (bothAreOfType(diff, Date.class, true)) {
			row.appendChild(element("TD", formatTwoLineDate(ReportDateFormats.MODIFICATIONS, (Date) diff.getOldValue())));
			row.appendChild(element("TD", formatTwoLineDate(ReportDateFormats.MODIFICATIONS, (Date) diff.getNewValue())));
		} else if (bothAreOfType(diff, String.class, false)) {
			LinkedList<Diff> textDiffs = textDiffEngine.diff_main((String)diff.getOldValue(), (String)diff.getNewValue());
			row.appendChild(element("TD", formatTextDiffs(textDiffs, false, true)));
			row.appendChild(element("TD", formatTextDiffs(textDiffs, true, false)));			
		} else {
			String oldValue = getDisplayString(diff.getParameter(), diff.getOldValue());
			String newValue = getDisplayString(diff.getParameter(), diff.getNewValue());
			LinkedList<Diff> textDiffs = textDiffEngine.diff_main(oldValue, newValue, true);
			row.appendChild(element("TD", formatTextDiffs(textDiffs, false, true)));
			row.appendChild(element("TD", formatTextDiffs(textDiffs, true, false)));			
		}
		cell.setAttribute("colspan", "3");
		row.appendChild(cell);
		return row;
	}
	
	private Element makeRowForSpecificDiff(ChangedConstraintOrProfile diff) {
		String cssClass = null;
		Element row = emptyElement("TR", cssClass);
		row.appendChild(element("TD", diff.getDiffType().name().toLowerCase()
				+ " "
				+ diff.getObject().getClass().getSimpleName()
				.replace("Impl", "").replaceAll("([A-Z])", " $1").toLowerCase()));
		String displayString;
		if (diff.getObject() instanceof TemporalConstraint) {
			TemporalConstraintPrinter constraintPrinter = new TemporalConstraintPrinter(new IdentifiableRegistry<EPlanElement>());
			constraintPrinter.setUseHTML(false); // can't integrate its serialized HTML with this DOM code
			if (diff.getObject() instanceof BinaryTemporalConstraint) {
				displayString = constraintPrinter.getText(((BinaryTemporalConstraint)diff.getObject()), diff.getOldCopyOfOwner());
			} else if (diff.getObject() instanceof PeriodicTemporalConstraint) {
				displayString = constraintPrinter.getText(((PeriodicTemporalConstraint)diff.getObject()), false);
			} else if (diff.getObject() instanceof TemporalChain) {
				displayString = constraintPrinter.getText(((TemporalChain)diff.getObject()), diff.getOldCopyOfOwner());
			} else {
				displayString = diff.getObject().toString();
			}
		} else {
			displayString = diff.getObject().toString();
		}
		row.appendChild(element("TD", displayString));
		return row;
	}

	private String getDisplayString(EStructuralFeature parameter, Object value) {
		if (value instanceof List) {
			return getListAsLines(parameter, (List) value);
		} else if (value instanceof EObject) {
			EObject object = (EObject) value;
			String name = PlanDiffUtils.getObjectName(object);
			if (CommonUtils.isNullOrEmpty(name)) {
				return structureToMultiLineString(object);
			} else {
				return name;
			}
		} else {
			return PlanDiffUtils.getDisplayString(parameter, value);
		}
	}

	private String getListAsLines(EStructuralFeature parameter, List list) {
		StringBuilder s = new StringBuilder();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			s.append(getDisplayString(parameter, iterator.next()));
			if (iterator.hasNext()) {
				s.append('\n');
			}
		}
		return s.toString();
	}

	private Element formatTextDiffs(LinkedList<Diff> textDiffs, boolean showInsertions, boolean showDeletions) {
		Element element = emptyElement("DIV", "text");
		for (Diff diff : textDiffs) {
			String text = diff.text;
			switch (diff.operation) {
			case INSERT:
				if (showInsertions) {
					element.appendChild(element("INS", formatMultiLineString(text, true)));
				}
				break;
			case DELETE:
				if (showDeletions) {
					element.appendChild(element("DEL", formatMultiLineString(text, true)));
				}
				break;
			case EQUAL:
				element.appendChild(formatMultiLineString(text, false));
				break;
			}
		}
		return element;
	}

	private Element formatTwoLineDate(DateFormat format, Date value) {
		return formatMultiLineString(formatDate(format, value), false);
	}
		
	private Element formatMultiLineString(String text, boolean visibleParagraph) {
		if (text.indexOf('\n') == -1) {
			return element("SPAN", text);
		}
		Element element = emptyElement("SPAN");
		String[] lines = text.split("\n", 0);
		if (lines.length==0) {
			element.appendChild(visibleNewline());
		}
		for (String line : lines) {
			element.appendChild(element("SPAN", line));
			if (visibleParagraph) {
				element.appendChild(visibleNewline());
			}
			element.appendChild(emptyElement("BR"));
		}
		return element;
	}
	
	private String structureToMultiLineString(EObject structure) {
		StringBuilder s = new StringBuilder();
		for (EStructuralFeature feature : structure.eClass().getEAllAttributes()) {
			if (!PlanDiffUtils.isIdFeature(feature)) {
				Object field = structure.eGet(feature);
				if (field instanceof String) {
					s.append(field);
					s.append('\n');
				}
			}
		}			
		return s.toString();
	}
	
	private Node visibleNewline() {
		return element("SPAN", document.createEntityReference("para"), "visible-newline");
	}

	private boolean bothAreOfType(ChangedByModifyingParameter diff, Class<?> relevantClass, boolean nullOK) {
		Object oldValue = diff.getOldValue();
		Object newValue = diff.getNewValue();
		return !(oldValue==null && newValue==null)
				&&
				((nullOK && oldValue==null) || relevantClass.isInstance(oldValue))
				&&
				((nullOK && newValue==null) || relevantClass.isInstance(newValue));
	}

	private void describeAnyOtherChanges(PlanDiffList differences, boolean anyDifferencesAlreadyMentioned) {
		int nMoves = differences.getMoves().size();
		if (nMoves > 0) {
			body.appendChild(element("H2", "Moves:"));
			StringBuilder s = new StringBuilder();
			s.append(nMoves); s.append(' ');
			s.append(nMoves==1? "activity was" : "activities were");
			s.append(" moved from one " + activityGroupNameSingular.toLowerCase()
					+ " to another.");
			body.appendChild(element("P", s.toString()));			
		} else if (!anyDifferencesAlreadyMentioned) {
			body.appendChild(element("H2", "There are no differences between the plans."));
		}
		if (!differences.getConstraintAndProfileChanges().isEmpty()) {
			body.appendChild(element("H2", "Differences in constraints and requirements were detected."));
			body.appendChild(mainTableOfDifferences(differences.getConstraintAndProfileChanges()));			
		}		
	}

	private Element makeHeading(EPlan plan) {
		Element box = emptyElement("DIV", "heading");
		Element line1 = emptyElement("DIV", "heading1");
		Element line2 = emptyElement("DIV", "heading2");
		box.appendChild(line1);
		box.appendChild(line2);
		PlanTemporalMember member = plan.getMember(PlanTemporalMember.class);
		String formatString = EnsembleProperties.getStringPropertyValue(
				"ensemble.plan.diff.heading.line1.sentence",
				"%s to %s in %s");
		Date start = member.getStartBoundary();
		Date end = member.getEndBoundary();
		if (start==null) start = member.getStartTime();
		if (end==null) end = member.getEndTime();
		line1.setTextContent(String.format(formatString,
				formatDate(ReportDateFormats.LINE1, start),
				formatDate(ReportDateFormats.LINE1, end),
				plan.getName()));
		line2.appendChild(element("SPAN", getReportPublicationLine()));
		Element linkToSelf = element("A", "   Save", "save");
		linkToSelf.setAttribute("href", "#");
		linkToSelf.setAttribute("title", "Right-click to save this report in file system");
		line2.appendChild(linkToSelf);
		return box;
	}
	
	protected String getReportPublicationLine() {
		String formatString = EnsembleProperties.getStringPropertyValue(
				"ensemble.plan.diff.heading.line2.sentence",
				"Published %s by %s");
		return String.format(formatString,
				formatDate(ReportDateFormats.LINE2, new Date()),
				UserExtendedNameProvider.getInstance().getUserNameForReports("unknown user"));
	}

	private String formatDate(DateFormat format, Date date) {
		if (date==null) return "???";
		else return format.format(date);
	}

	private void makeSummaryOfUpdates(String text) {
		body.appendChild(element("H2", "Summary of Updates:"));
		body.appendChild(element("P", text, "summary-of-updates"));
	}

	protected Node getCharsetElement() {
		Element result = emptyElement("META");
		result.setAttribute("http-equiv", "content-type");
		result.setAttribute("content", "text/html; charset="+CHARSET_NAME);
		return result;
	}

	protected Element element(String tag, String text) {
		return element(tag, text, null);
	}
	
	protected Element element(String tag, Node child) {
		return element(tag, child, null);
	}

	private Element element(String tag, String text, String className) {
		return element(tag, document.createTextNode(text), className);
	}
	
	
	private Element element(String tag, Node child, String className) {
		Element node = emptyElement(tag, className);
		if (child != null) node.appendChild(child);
		return node;
	}

	private Element emptyElement(String tag, String className) {
		Element node = document.createElement(tag);
		node.setAttribute("class", className);
		return node;
	}
	
	protected Element emptyElement(String tag) {
		return document.createElement(tag);
	}

	// Maybe class will always load from this file but no other.  See SPF-8671.
	public static HTMLDocument createDocument(String title) {
		LogUtil.warn(DOM + " is class " + DOM.getClass());
		return DOM.createHTMLDocument(title);
	}
	
	@SuppressWarnings("deprecation")
	protected org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat();
	@SuppressWarnings("deprecation")
	protected
	// A web search doesn't find any advice for a replacement:
	// http://marc.info/?l=xerces-j-dev&m=108071323405980&w=2 says:
	// "The deprecation of this class is meant to be a warning for new developers, 
	// so that they look for alternatives if they're able to use something else." 
	// and there's a mention of "getting it into xml-commons someday".
	org.apache.xml.serialize.HTMLSerializer serializer = new org.apache.xml.serialize.HTMLSerializer(format);
	
	@SuppressWarnings("deprecation")  // I keep trying to find something to replace HTMLSerializer and OutputFormat.
	public void writeDocument(Writer writer, HTMLDocument document) throws IOException {
		format.setIndenting(false); // newlines cause extraneous spaces in INS and DEL text diffs
		doSerializationCustomizations();
		serializer.setOutputCharStream(writer);
		serializer.serialize(document);
		writer.close();
	}

	protected void doSerializationCustomizations() {
		// No-op unless subclass overrides
	}

}
