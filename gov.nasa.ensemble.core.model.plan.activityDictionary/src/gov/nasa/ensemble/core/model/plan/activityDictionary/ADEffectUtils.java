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

import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.dictionary.EResourceDef;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

public class ADEffectUtils {

	public static void setEffectAmount(EPlanElement element, EObject object, EResourceDef definition, ComputableAmount amount) {
		ADEffectMember member = element.getMember(ADEffectMember.class);
		setEffectAmount(member, object, definition, amount);
	}

	public static void setEffectAmount(ADEffectMember member, EObject object, EResourceDef definition, ComputableAmount amount) {
		EMap<ADEffectKey, ComputableAmount> map = member.getEffects();
		ADEffectKey key = createKey(object, definition);
		if ((amount == null) || (amount.getAmount() == null)) {
			map.remove(key);
		} else {
			map.put(key, amount);
		}
	}

	public static ComputableAmount getEffectAmount(EPlanElement element, EResourceDef definition) {
		return getEffectAmount(element, null, definition);
	}
	
	public static ComputableAmount getEffectAmount(EPlanElement element, EObject object, EResourceDef definition) {
		ADEffectMember member = element.getMember(ADEffectMember.class);
		EMap<ADEffectKey, ComputableAmount> map = member.getEffects();
		ADEffectKey key = createKey(object, definition);
		return map.get(key);
	}
	
	private static ADEffectKey createKey(EObject object, EResourceDef definition) {
		if (definition == null) {
			throw new NullPointerException("keys must have a definition");
		}
		ADEffectKey key = ActivityDictionaryFactory.eINSTANCE.createADEffectKey();
		key.setObject(object);
		key.setResourceDef(definition);
		return key;
	}

}
