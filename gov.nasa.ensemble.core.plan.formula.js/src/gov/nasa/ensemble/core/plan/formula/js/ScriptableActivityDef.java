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
package gov.nasa.ensemble.core.plan.formula.js;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptableActivityDef extends ScriptableObject {

	private final EActivityDef eActivityDef;
	
	private static final Set<String> warningCache = new HashSet<String>();
	private static final Logger trace = Logger.getLogger(ScriptableActivityDef.class);
	
	public ScriptableActivityDef(EActivityDef activityDef) {
		super();
		eActivityDef = activityDef;
	}

	@Override
	public String getClassName() {
		return eActivityDef.getName();
	}

	@Override
	public Object get(String name, Scriptable start) {
		EStructuralFeature f = eActivityDef.getEStructuralFeature(name);
		if (f == null) {
			return null;
		}

		Object value = f.getDefaultValue();
		if (value != null) {
			return value;
		}
		
		EClassifier classifier = f.getEType();
		if (EcorePackage.Literals.ELONG == classifier
				|| EcorePackage.Literals.ELONG_OBJECT == classifier
				|| EcorePackage.Literals.EFLOAT == classifier
				|| EcorePackage.Literals.EFLOAT_OBJECT == classifier
				|| EcorePackage.Literals.EINT == classifier
				|| EcorePackage.Literals.EINTEGER_OBJECT == classifier
				|| EcorePackage.Literals.EDOUBLE == classifier
				|| EcorePackage.Literals.EDOUBLE_OBJECT == classifier
				|| JSciencePackage.Literals.EDURATION == classifier) {
			value = 0;
		} else if (EcorePackage.Literals.EBOOLEAN == classifier
				|| EcorePackage.Literals.EBOOLEAN_OBJECT == classifier) {
			value = false;
		} // else...
		String msg = "Parameter " + name + " of ActivityDef " + eActivityDef.getName() + " is null, defaulting to: " + value;
		if (!warningCache.contains(msg)) {
			trace.warn(msg);
			warningCache.add(msg);
		}
		return value;
	}
	
}
