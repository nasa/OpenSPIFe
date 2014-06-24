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
package gov.nasa.ensemble.core.model.plan.activityDictionary;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityGroupDef;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

public class ADParameterMemberFactory {

	private static final ActivityDictionary AD = ActivityDictionary.getInstance();
	
	public static final ADParameterMemberFactory FACTORY = new ADParameterMemberFactory();
	
	public EObject createData(EActivityDef def) {
		if (def == null) {
			return createData(PlanPackage.Literals.EACTIVITY);
		}
		EPackage p = def.getEPackage();
		if (p != null) {
			EFactory f = p.getEFactoryInstance();
			if (f != null) {
				return f.create(def);
			}
		}
		return null;
	}

	public EObject createData(EClass eClass) {
		EObject object = null;
		if (eClass == PlanPackage.Literals.EACTIVITY) {
			// no data specification
		} else if (eClass == PlanPackage.Literals.EACTIVITY_GROUP) {
			List<EActivityGroupDef> definitions = AD.getDefinitions(EActivityGroupDef.class);
			if (definitions.size() == 1) {
				EActivityGroupDef def = definitions.get(0);
				object = def.getEPackage().getEFactoryInstance().create(def);
			} // else... no data specification
		} else if (eClass == PlanPackage.Literals.EPLAN) {
			// no data specification
		}
		return object;
	}

}
