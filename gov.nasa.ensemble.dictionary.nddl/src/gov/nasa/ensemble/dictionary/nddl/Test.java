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
package gov.nasa.ensemble.dictionary.nddl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class Test implements IApplication {

	public static void main(String[] args) throws Exception {
		Test test = new Test();
		test.run(args);
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
		return run(context);
	}

	public Object run(Object notused) throws Exception {
		URI uri = URI.createPlatformPluginURI("/gov.nasa.ensemble.dictionary.nddl/ad/Reference.dictionary", true);
		ModelGenerator.generateModel(uri);
		return null;
	}

	@Override
	public void stop() {
		// no implementation
	}

}
