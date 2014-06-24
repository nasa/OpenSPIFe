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
package gov.nasa.ensemble.core.model.plan.diff.report.html;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.diff.report.html.trees.PlanDiffOutputAsTreeHTML;

import java.io.IOException;
import java.io.Writer;

import org.apache.html.dom.HTMLDOMImplementationImpl;
import org.w3c.dom.html.HTMLDOMImplementation;
import org.w3c.dom.html.HTMLDocument;

public class HTMLReportUtil {
	
	private static final HTMLDOMImplementation DOM = HTMLDOMImplementationImpl.getHTMLDOMImplementation();

	private HTMLReportUtil() {
		// Static utility class, can't be instantiated
	}
	
	/** Works around weird linkage problem, SPF-8671,
	 * in some builds, where one plugin can import this
	 * and another can't.
	 * @param title 
	 * @return
	 */
	public static HTMLDocument createDocument(String title) {
//		ClassLoader.getSystemClassLoader().loadClass("org.apache.html.dom.HTMLDOMImplementationImpl", true);
		LogUtil.warn("Static " + PlanDiffOutputAsTreeHTML.DOM + " is class " + PlanDiffOutputAsTreeHTML.DOM.getClass());
		LogUtil.warn(DOM + " is class " + DOM.getClass());

		// FIXME:  It's a complete mystery ?why the simple call below doesn't work.  See SPF-8671.
		return DOM.createHTMLDocument(title);
//		try {
//			Class<?> class1 = ClassLoader.getSystemClassLoader().loadClass("org.apache.html.dom.HTMLDOMImplementationImpl");
//			Class <? extends HTMLDOMImplementation> class2 = (Class<? extends HTMLDOMImplementation>) class1;
//			return class2.getMethod(", arg1)
//		} catch (Exception e) {
//			LogUtil.error(e);
//			return null;
//		}
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
	
	@SuppressWarnings("deprecation") // FIXME -- better alternative to these methods?
	public static void writeDocument(Writer writer, HTMLDocument document) throws IOException {
		format.setIndenting(true);
		serializer.setOutputCharStream(writer);
		serializer.serialize(document);
		writer.close();
	}

}
