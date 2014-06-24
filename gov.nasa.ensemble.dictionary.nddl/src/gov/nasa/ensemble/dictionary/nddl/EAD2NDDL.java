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

import gov.nasa.ensemble.common.DefaultApplication;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;

import org.eclipse.emf.common.util.URI;

public class EAD2NDDL extends DefaultApplication {

	@Override
	protected Object start(String[] args) throws Exception {
		String uriString = args[0];
		URI uri = URI.createURI(uriString);
		try {
			// Demand load resource for this file.
			//
			EActivityDictionary ad = DictionaryUtil.load(uri);
			return ModelGenerator.generateModel(ad);
		} catch (RuntimeException rte) {
			System.out.println("Problem loading " + uri);
			rte.printStackTrace();
		} catch (Exception e) {
			System.out.println("Problem generating NDDL files ");
			e.printStackTrace();
			throw e;
		}
		return false;
	}
	
}
