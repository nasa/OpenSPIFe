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

import gov.nasa.ensemble.core.model.plan.diff.report.Activator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public class HTMLStaticContent {

	private static final Charset UTF8 = Charset.forName("UTF-8");

	public static String getStaticFileContents (String filename) throws MalformedURLException, IOException {
		String result = "";
		URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path("static-html-data/" + filename), null);
		if (url==null) throw new IOException("Can't find " + filename + " in html-static/.");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), UTF8));
		String line = in.readLine();
		while (line != null) { 
			result = result + line + "\n";
			line = in.readLine();
		}
		return  "\n" + result +  "\n";
	}

}
