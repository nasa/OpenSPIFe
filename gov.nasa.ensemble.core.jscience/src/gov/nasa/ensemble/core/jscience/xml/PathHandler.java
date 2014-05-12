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
package gov.nasa.ensemble.core.jscience.xml;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class PathHandler extends DefaultHandler {
	private Stack<String> pathStack = new Stack<String>();
	private String pathString = "/";

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		pathStack.push(qName);
		updatePath();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		pathStack.pop();
		updatePath();
		super.endElement(uri, localName, qName);
	}

	private void updatePath() {
		if (pathStack.size() == 0) {
			pathString = "/";
		} else {
			StringBuffer sb = new StringBuffer(pathStack.size() * 10);
			for (String s : pathStack)
				sb.append("/").append(s);
			pathString = sb.toString();
		}
	}
	
	public String getPath() {
		return pathString;
	}
	
	public Stack<String> getPathStack() {
		return pathStack;
	}
}
