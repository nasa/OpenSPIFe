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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.text.StringEscapeFormat;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanUtils;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 *	A base class for creating printer classes that populate forms
 */
public class PlanPrinter {
		
	private final IdentifiableRegistry<EPlanElement> identifiableRegistry;
	protected boolean useHTML = true;

	public PlanPrinter(IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		this.identifiableRegistry = identifiableRegistry;
	}
	
	/**
	 * Create an instance of form text prepared to display a plan element. 
	 * @param toolkit
	 * @param parent
	 * @return the form text instance.
	 */
	public static FormText createPlanText(FormToolkit toolkit, Composite parent) {
		FormText text = toolkit.createFormText(parent, true);
		text.setColor("start", ColorConstants.darkGreen);
		text.setColor("end", ColorConstants.red);
		return text;
	}

	/**
	 * Returns the node's name in print form.
	 * @param element
	 * @return the node's name in print form.
	 */
	public static String getPrintName(EPlanElement element) {
		if (element == null) {
			return "?null element?";
		}
		String name = element.getName(); 
		if (name == null) {
			return PlanUtils.getElementNameForDisplay(element);
		}
		name = name.replace('_',' ');
		
		return name;
	}
	
	/**
	 * Returns the node's name with replacements put in for
	 * characters with special semantics in hypertext. so far: '&'
	 *  
	 * @param element
	 * @return the name
	 */
	public static String getHyperlinkPrintName(EPlanElement element) {
		String name = getPrintName(element);
		if (name==null || name.isEmpty()) return PlanUtils.getElementNameForDisplay(element);
		name = StringEscapeFormat.escape(name); // form text doesn't accept '&', etc. gracefully
		return name;
	}

	public String getTextSelected(EPlanElement element) {
		return html("<b>") + getHyperlinkPrintName(element) + html("</b>");
	}

	/**
	 * Returns the plan element's name as a hyperlink string
	 * @param element
	 * @return the plan element's name as a hyperlink string
	 */
	public String getText(EPlanElement element) {
		String name = getHyperlinkPrintName(element);
		String uniqueId = identifiableRegistry.getOrRegister(element);
		String href = getHrefFromUniqueId(uniqueId);
		return html("<a href=" + '"' + href + '"' + ">") + name + html("</a>");
	}
	
	/**
	 * Returns the activity's name as a hyperlink string, preceded by its group's name as a hyperlink string,
	 * e.g. Cal Target for Morning Mosaic [Mars],
	 *      Breakfast for Subject 123 [BedRest].
	 * @param element
	 * @return a string containing one or two hyperlinked names.
	 */
	public String getTextWithGroup(EActivity element) {
		EPlanElement group = element.getParent();
		String elementText = getText(element);
		if (group == null) return elementText;
		return elementText + " for " + getText(group);
	}

	public void setUseHTML(boolean newValue) {
		useHTML = newValue;
	}

	protected String html(String string) {
		if (!useHTML) return "";
		return string;
	}

	/**
	 * Get an href from the unique id
	 * @param uniqueId
	 * @return
	 */
	public static String getHrefFromUniqueId(String uniqueId) {
		return "#" + uniqueId;
	}

	/**
	 * Extract the unique id from the href
	 * @param href
	 * @return
	 */
	public static String getUniqueIdFromHref(String href) {
		if (href != null) {
			int index = href.indexOf('#');
			if (index != -1) {
				href = href.substring(index + 1);
			}
		}
		return href;
	}

	/**
	 * Construct a list of strings in the english convention.  Examples:
	 * { } -> ""
	 * { "Wacko" } -> "Wacko"
	 * { "Wacko", "Yacko" } -> "Wacko and Yacko"
	 * { "Wacko", "Yacko", "Dot" } -> "Wacko, Yacko, and Dot"
	 * @param strings
	 * @return
	 */
	public static String getListText(List<String> strings) {
		String result = "";
		if (!strings.isEmpty()) {
			int size = strings.size();
			boolean useCommas = (size > 2);
			Iterator<String> stringItr = strings.iterator();
			while (stringItr.hasNext()) {
				String string = stringItr.next();
				if (!stringItr.hasNext() && (size != 1)) {
					// last one
					result += "and ";
				}
				result += string;
				if (stringItr.hasNext()) {
					if (useCommas) {
						result += ",";
					}
					result += " ";
				}
			}
		}
		return result;
	}
	
}
