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
package gov.nasa.ensemble.emf.util;

import gov.nasa.ensemble.common.DefaultApplication;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

public class DottyRenderer extends DefaultApplication {
	
	public static void createGraphDotty(EObject root, OutputStream stream) {
		PrintStream out = new PrintStream(stream);
		Set<String> set = new HashSet<String>();
		out.println("digraph diagram {");
		createLinks(root, out, set);
		out.print("}");
	}

	private static void createLinks(EObject root, PrintStream out, Set<String> set) {
		ITreeItemContentProvider cp = EMFUtils.adapt(root, ITreeItemContentProvider.class);
		for (Object c : cp.getChildren(root)) {
			EObject child = (EObject) c;
			String link = createLink(root, child);
			if (!set.contains(link)) {
				set.add(link);
				out.println(link);
			}
			
			createLinks(child, out, set);
		}
	}
	
	private static String createLink(EObject root, EObject child) {
		String link = new StringBuffer("\t")
				.append(EMFUtils.getDisplayName(root).replaceAll("[^a-zA-Z0-9_]", "_"))
				.append(" -> ")
				.append(EMFUtils.getDisplayName(child).replaceAll("[^a-zA-Z0-9_]", "_"))
				.append(";").toString();
		return link;
	}
	
	@Override
	protected Object start(String[] args) throws Exception {
		URI uri = URI.createURI(args[0]);
		Resource resource = EMFUtils.createResourceSet().getResource(uri, true);
		createGraphDotty(resource.getContents().get(0), System.out);
		return null;
	}
	
}
