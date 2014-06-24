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
package gov.nasa.ensemble.core.model.plan.diff.report.html.trees;

import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.report.html.HTMLStaticContent;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffDifftypeNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffModificationNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNameNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffObjectNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffOutputMethod;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffParameterNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffRawNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTree;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.apache.html.dom.HTMLDOMImplementationImpl;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTableCellElement;

@SuppressWarnings("deprecation")  // I keep trying to find something to replace HTMLSerializer and OutputFormat.
public class PlanDiffOutputAsTreeHTML extends PlanDiffOutputMethod {

	private IStringifier<Date> dateStringifier = StringifierRegistry.getStringifier(Date.class);

	private static final String CHARSET_NAME = "UTF-8";

	private String activityGroupName = EnsembleProperties.getProperty("ensemble.plan.activityGroupName.plural", "activity groups").toLowerCase();

	private static final String VIEW_SELECTION_MENU_ID = "View-Selection";
	
	private static final String doubleQuote = "\"";
	protected static final String singleQuote = "'";
	
	private boolean initiallyHideUnscheduled = false;
	private boolean initiallyHideUnchanged = true;
	private boolean initiallyHideDeleted = false;

	protected HTMLDOMImplementationImpl documentObjectModel = (HTMLDOMImplementationImpl) HTMLDOMImplementationImpl.getHTMLDOMImplementation();
	
	public static final HTMLDOMImplementationImpl DOM = (HTMLDOMImplementationImpl) HTMLDOMImplementationImpl.getHTMLDOMImplementation(); //SPF-8671 debugging

	protected HTMLDocument document = null;
	protected HTMLBodyElement body;
	
	@Override
	public void writeToStream(List<AbstractDiffTree> diffTrees, PlanDiffList differenceInfo, EPlan plan1, EPlan plan2, OutputStream out) throws IOException {
		writeDocument(new OutputStreamWriter(out, CHARSET_NAME),
				createDocument(diffTrees, differenceInfo, plan1, plan2));
		out.close();	
		}
	
	public HTMLDocument createDocument(List<AbstractDiffTree> diffTrees, PlanDiffList differenceInfo, EPlan plan1, EPlan plan2) {
		String title = "Plan diff";
		document = documentObjectModel.createHTMLDocument(title);
		LogUtil.warn(documentObjectModel + " is class " + documentObjectModel.getClass());
		LogUtil.warn("Static " + documentObjectModel + " is class " + documentObjectModel.getClass());
		body = (HTMLBodyElement) document.getBody();
		Node head = document.getFirstChild().getFirstChild();
		body.setAttribute("onload", "select_tree(" + singleQuote + diffTrees.get(0).getClass().getSimpleName() + singleQuote + ");");
		body.setAttribute("onmouseover", "highlight_row(event.target, '#EEEEFF')");
		body.setAttribute("onmouseout", "highlight_row(event.target, null)");

		try {
			head.appendChild(getCharsetElement()); 
			head.appendChild(element("SCRIPT", HTMLStaticContent.getStaticFileContents("trees/script.js")));
			head.appendChild(element("STYLE", HTMLStaticContent.getStaticFileContents("trees/style.css")));
			body.appendChild(makeCredits());
			body.appendChild(makeIdentification(plan1, plan2));
			body.appendChild(emptyElement("HR"));
			body.appendChild(makeSummary(diffTrees, differenceInfo));
			Element box = element("DIV", makeMenus(diffTrees), "containingbox blackborder");
			Element planDiv = emptyElement("DIV");
			box.appendChild(planDiv);
			body.appendChild(box);
			planDiv.setAttribute("id", "plan");
			for (AbstractDiffTree tree : diffTrees) {
				planDiv.appendChild(treeToHTML(tree));
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	protected Node getCharsetElement() {
		Element result = emptyElement("META");
		result.setAttribute("http-equiv", "content-type");
		result.setAttribute("content", "text/html; charset="+CHARSET_NAME);
		return result;
	}

	private Element makeMenus(List<AbstractDiffTree> diffTrees) {
		Element menus = emptyElement("DIV","float-right-top-align");
		Element menu = emptyElement("DIV","expansion-panel");
		menus.appendChild(element("SPAN", "View:"));
		menus.appendChild(createGroupByMenu(diffTrees));
		menus.appendChild(emptyElement("BR"));
		menus.appendChild(emptyElement("BR"));
		menu.appendChild(menuItem("Open Top Level", "expand_top_level()"));
		menu.appendChild(menuItem("Open All", "expand_all()"));
		menu.appendChild(menuItem("Close All", "collapse_all()"));
		menu.appendChild(menuLabel("Hide:"));
		menu.appendChild(showHideCheckbox("Unscheduled", "unscheduled", initiallyHideUnscheduled));
		if (diffTrees.get(0).includeUnchanged()) {
			menu.appendChild(showHideCheckbox("Unchanged", "unchanged", initiallyHideUnchanged));
		}
		menu.appendChild(showHideCheckbox("Deleted", "deleted", initiallyHideDeleted));
		{
			Element saveAsSection = menuItem("Save As...", "document.getElementsByName('saveas')[0].className='saveas expanded';");
			Element saveAsButton = emptyElement("DIV", "saveas hidden");
			saveAsButton.setAttribute("name", "saveas");
			Element saveAs = emptyElement("A", "saveas");
			saveAsSection.appendChild(saveAsButton);
			saveAsButton.appendChild(saveAs);
			saveAs.setAttribute("href", "#");
			saveAs.setTextContent("To Save As, right-click HERE and select the Save or Download Link command.");
			menu.appendChild(saveAsSection);
		}


		menus.appendChild(menu);
		return menus;
	}

	private Element makeSummary(List<AbstractDiffTree> diffTrees, PlanDiffList differenceInfo) {
		Element summary;
		{PlanDiffTree tree = (PlanDiffTree)diffTrees.get(0); // any tree  except the raw one can provide a summary
		summary = emptyElement("P");
		summary.appendChild(
				element ("DIV",
						tree.count(EActivityGroup.class, DiffType.ADD) + " " + activityGroupName + " added, " +
						tree.count(EActivityGroup.class, DiffType.REMOVE) + " removed, " +
						tree.count(EActivityGroup.class, DiffType.MODIFY)  + " changed.  "));
		summary.appendChild(
				element ("DIV",
						tree.count(EActivity.class, DiffType.ADD) + " " + "activities" + " added, " +
						tree.count(EActivity.class, DiffType.REMOVE) + " removed, " +
						(tree.count(EActivity.class, DiffType.MOVE_IN)
								+ tree.count(EActivity.class, DiffType.MOVE_OUT)) + " moved, " +
						tree.count(EActivity.class, DiffType.MODIFY)  + " changed.  "));
		if (tree.includeUnchanged()) {
			summary.appendChild(element("DIV", differenceInfo.getUnchangedActivityGroups().size()
					+ " " + activityGroupName 
					+ " and " + differenceInfo.getUnchangedActivities().size() + " activities unchanged."));
		}
		}
		return summary;
	}
	
	protected Element makeCredits() {
		Element result = emptyElement("H3", "credits");
		try {
			result.appendChild(element("SPAN", "Generated by " + System.getProperty("user.name")));
			result.appendChild(emptyElement("BR"));
		} catch (Throwable e) { /* ignore exceptions */ }
		try {
			result.appendChild(element("SPAN", "at " + StringifierRegistry.getStringifier(Date.class).getDisplayString(new Date())));
			result.appendChild(emptyElement("BR"));
		} catch (Throwable e) { /* ignore exceptions */ }
		try {
			result.appendChild(element("SPAN", "with " + Platform.getProduct().getName()));
		} catch (Throwable e) { /* ignore exceptions */ }
		return result;
		}

	private Element makeIdentification(EPlan plan1, EPlan plan2) {
		Element planDiffPrefix = element("H3", "PLAN DIFF:", "header-floatleft");
		planDiffPrefix.appendChild(document.createEntityReference("nbsp"));
		body.appendChild(planDiffPrefix);
		Element id2 = element("H3", doubleQuote + plan1.getName()  + doubleQuote, "header");
		id2.appendChild(emptyElement("BR"));
		id2.appendChild(document.createTextNode("VS. " + doubleQuote + plan2.getName() + doubleQuote));
		return element("H3",
				element("TABLE", element("TR", element("TD", id2))));
	}

	private Node createGroupByMenu(List<AbstractDiffTree> diffTrees) {
		Element groupByMenu = document.createElement("select");
		for (AbstractDiffTree diffTree : diffTrees) {
			Element item = element("option", diffTree.getDescriptionForUser());
			item.setAttribute("value", diffTree.getClass().getSimpleName());
			groupByMenu.appendChild(item);
		}
		groupByMenu.setAttribute("ID", VIEW_SELECTION_MENU_ID);
		groupByMenu.setAttribute("onchange", "select_tree(this[this.selectedIndex].value)");
		return groupByMenu;
	}

	protected Element treeToHTML(AbstractDiffTree diffTree) {
		Element result = document.createElement("DIV");
		result.setAttribute("name", "TopLevelTree");
		result.setAttribute("id", diffTree.getClass().getSimpleName());
		Element list = document.createElement("UL");
		for (PlanDiffNode child : diffTree.getRoot().getChildren()) {
			list.appendChild(subtreeToHTML(child));
		}
		result.appendChild(list);
		return result;
	}
	
	private Element subtreeToHTML(PlanDiffNode child) {
		Element result = document.createElement("LI");
		boolean bottomUpShouldBottomOut = child.getParent() instanceof PlanDiffParameterNode;
		result.setAttribute("name", "subtree");
		StringBuilder classes = new StringBuilder("subtree ");
		if (child.isUnchanged()) classes.append(initiallyHideUnchanged? "hidden unchanged " : "unchanged ");
		if (child.isUnscheduled()) classes.append(initiallyHideUnscheduled? "hidden unscheduled " : "unscheduled ");
		if (child.isDeleted()) classes.append(initiallyHideDeleted? "hidden deleted " : "deleted ");
		if (child.isModified()) classes.append("modified ");
		classes.append(child.getCSSclass());
		if (child.hasChildren() && !bottomUpShouldBottomOut) {
			classes.append(" collapsed");
			result.appendChild(makeExpandOrCollapseArrow("show", ">"));
			result.appendChild(makeExpandOrCollapseArrow("hide", "v"));
		}
		else {
			result.appendChild(document.createEntityReference("nbsp"));
			result.appendChild(document.createEntityReference("nbsp"));
		}
		result.setAttribute("class", classes.toString());
		Element list = document.createElement("UL");
		result.appendChild(nodeToHTML(child));
		for (PlanDiffNode grandchild : child.getChildren()) {
			list.appendChild(subtreeToHTML(grandchild));
		}
		result.appendChild(list);
		return result;
	}
	
	private Element nodeToHTML(PlanDiffNode node) {
		if (node instanceof PlanDiffParameterNode) return nodeToHTML((PlanDiffParameterNode)node);
		if (node instanceof PlanDiffBadgedNode) return nodeToHTML((PlanDiffBadgedNode)node);
		if (node instanceof PlanDiffNameNode) return nodeToHTML((PlanDiffNameNode)node);
		if (node instanceof PlanDiffModificationNode) return nodeToHTML((PlanDiffModificationNode)node);
		if (node instanceof PlanDiffDifftypeNode) return nodeToHTML((PlanDiffDifftypeNode)node);
		if (node instanceof PlanDiffRawNode) return nodeToHTML((PlanDiffRawNode)node);
		else return element("Span", "???" + node.getClass().getSimpleName() + "???");
	}
	
	private Element nodeToHTML(PlanDiffBadgedNode node) {
		Element table = document.createElement("TABLE");
		table.setAttribute("style", "display:inline; vertical-align:middle; width:100%");
		Element tableRow = document.createElement("TR");
		HTMLTableCellElement description = (HTMLTableCellElement) document.createElement("TD");
		table.appendChild(tableRow);
		tableRow.appendChild(description);
		Element icon = emptyElement("SPAN", node.getCSSclassForIcon());
		Element badge = emptyElement("SPAN", "badge difftype-" + node.getCSSclassForBadge());
		Element name = element("SPAN", node.getName(), node.getCSSclass() + " name");
		if (node.getDiffType()==DiffType.ADD) table.setAttribute("class", "added");
		else if (node.getDiffType()==DiffType.REMOVE) table.setAttribute("class", "deleted");
		else if (node.getDiffType()==DiffType.MODIFY) table.setAttribute("class", "modified");
		else if (node.getDiffType()==DiffType.MOVE_IN) table.setAttribute("class", "moved elsewhere");
		else if (node.getDiffType()==DiffType.MOVE_OUT) table.setAttribute("class", "moved here");
	
		if (node instanceof PlanDiffObjectNode) iconGetsActivityColor(icon, ((PlanDiffObjectNode) node).getObject());
		description.appendChild(icon);
		description.appendChild(badge);
		description.appendChild(name);
		
		if (node instanceof PlanDiffObjectNode) {
			HTMLTableCellElement startTime = (HTMLTableCellElement) document.createElement("TD");
			tableRow.appendChild(startTime);
			appendObjectInfo(((PlanDiffObjectNode) node), description, startTime);
		}
		return table;
	}
	
	private void iconGetsActivityColor(Element icon, EObject object) {
		ERGB eRGB = ((EPlanElement)object).getMember(CommonMember.class).getColor();
		icon.setAttribute("style", "background-color:rgb("
				+ eRGB.red + ","
				+ eRGB.green + ","
				+ eRGB.blue + ")"
				);
	}

	private void appendObjectInfo (PlanDiffObjectNode node, HTMLElement description, HTMLElement startTime) {		
		EObject object = node.getObject();
		if (object.eContainer() instanceof EActivityGroup
				&& !node.parentObjectMentionedAboveInTree()) {
			description.appendChild(element("SPAN", " "));
			description.appendChild(document.createEntityReference("laquo"));
			description.appendChild(element("SPAN", " "));
			description.appendChild(element("SPAN", node.getParentName(), "parent-name"));
		}
		if (object instanceof EPlanElement) {
			TemporalMember temporalMember = ((EPlanElement)object).getMember(TemporalMember.class);
			if (!temporalMember.getScheduled()) description.setClassName("unscheduled");
			String startTimeString = dateStringifier.getDisplayString(temporalMember.getStartTime());
			startTime.appendChild(element("SPAN", startTimeString, "start-time"));
		}
	}
	
	private Element nodeToHTML(PlanDiffModificationNode node) {
		Element result = document.createElement("SPAN");
		EStructuralFeature parameter = node.getDiff().getParameter();
		if (!node.mentionedAbove(parameter))
			result.appendChild(document.createTextNode(parameter.getName() + ": "));
		result.appendChild(document.createTextNode(getDisplayString(parameter, node.getDiff().getOldValue())));
		result.appendChild(document.createTextNode(" "));
		result.appendChild(document.createEntityReference("nbsp"));
		result.appendChild(document.createEntityReference("rarr"));
		result.appendChild(document.createEntityReference("nbsp"));
		result.appendChild(document.createTextNode(" "));
		result.appendChild(document.createTextNode(getDisplayString(parameter, node.getDiff().getNewValue())));
		return result;
	}
	
	private String getDisplayString(EStructuralFeature parameter, Object value) {
		if (value instanceof Date) {
			// Treat this specially so test gets predictable results with different ensemble.properties.
			return dateStringifier.getDisplayString((Date) value);
		}
		return PlanDiffUtils.getDisplayString(parameter, value);
	}

	private Element nodeToHTML(PlanDiffParameterNode node) {
		return element("SPAN", node.getDescription());
	}
	
	private Element nodeToHTML(PlanDiffNameNode node) {
		return element("SPAN", node.getName());
	}

	private Element nodeToHTML(PlanDiffDifftypeNode node) {
		return element("SPAN", node.getDescription());
	}
	
	private Element nodeToHTML(PlanDiffRawNode node) {
		return element("SPAN", node.getDescription());
	}
	
	private Element makeExpandOrCollapseArrow(String showOrHide, String glyph) {
		Element result = element("SPAN", glyph, showOrHide+"-arrow");
		result.setAttribute("onclick", showOrHide+"_children(event)");
		result.setAttribute("title", "Click to " + showOrHide + " everything beneath this in the tree");
		result.setAttribute("name", "all_" + showOrHide + "_arrows");
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
	
	private Element menuItem(String label, String onclick) {
		Element node = document.createElement("LI");
		node.appendChild(document.createTextNode(label));
		node.setAttribute("class", "expansion-command");
		node.setAttribute("onclick", onclick);
		return node;
	}
	
	private Element menuLabel(String label) {
		Element node = document.createElement("LI");
		node.appendChild(document.createTextNode(label));
		return node;
	}
	
	private Element showHideCheckbox(String label, String className, boolean initiallyHidden) {
		Element LI = document.createElement("LI");
		HTMLInputElement checkbox = (HTMLInputElement) document.createElement("input");
		LI.appendChild(checkbox);
		LI.appendChild(document.createTextNode(label));
		checkbox.setAttribute("type", "checkbox");
		String arglist = "('" + className + "')";
		String show = "show_nodes_of_class" + arglist;
		String hide = "hide_nodes_of_class" + arglist;
		checkbox.setAttribute("onchange", "if (this.checked) {" + hide + "} else {" + show + "}");
		checkbox.setChecked(initiallyHidden);
		return LI;
	}
	
	
	/** Provided so that a JUnit test can be more independent of the checked-in ensemble.properties configuration. */
	public void setDateStringifier(IStringifier<Date> dateStringifier) {
		this.dateStringifier = dateStringifier;
	}
	
	public String getActivityGroupName() {
		return activityGroupName;
	}

	public void setActivityGroupName(String activityGroupName) {
		this.activityGroupName = activityGroupName;
	}
	
	// Maybe class will always load from this file but no other.  See SPF-8671.
	public static HTMLDocument createDocument(String title) {
		LogUtil.warn(DOM + " is class " + DOM.getClass());
		return DOM.createHTMLDocument(title);
	}
	
	@SuppressWarnings("deprecation")
	private static org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat();
	@SuppressWarnings("deprecation")
	private static
	// A web search doesn't find any advice for a replacement:
	// http://marc.info/?l=xerces-j-dev&m=108071323405980&w=2 says:
	// "The deprecation of this class is meant to be a warning for new developers, 
	// so that they look for alternatives if they're able to use something else." 
	// and there's a mention of "getting it into xml-commons someday".
	org.apache.xml.serialize.HTMLSerializer serializer = new org.apache.xml.serialize.HTMLSerializer(format);
	
	public static void writeDocument(Writer writer, HTMLDocument document) throws IOException {
		format.setIndenting(true);
		serializer.setOutputCharStream(writer);
		serializer.serialize(document);
		writer.close();
	}

}
