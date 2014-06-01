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
package gov.nasa.ensemble.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;

public class DefinitionContextImpl implements DefinitionContext {

	private final EObject											target;
	
	private Map<Class<?>, List<? extends Object>> 					definitionsByClass 			= new HashMap<Class<?>, List<? extends Object>>();
	private Map<Class<?>, Map<String, ? extends INamedDefinition>> 	definitionsByNameByClass	= new HashMap<Class<?>, Map<String, ? extends INamedDefinition>>();
	
	public DefinitionContextImpl(EObject target) {
		this.target = target;
		this.target.eAdapters().add(new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				int type = msg.getEventType();
				if (Notification.ADD == type
						|| Notification.ADD_MANY == type
						|| Notification.REMOVE == type
						|| Notification.REMOVE_MANY == type) {
					clearCache();
				}
				super.notifyChanged(msg);
			}
			
		});
	}
	
	@Override
	public final void clearCache() {
		definitionsByClass.clear();
		definitionsByNameByClass.clear();
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.activityDictionary.IDefinitionContext#getDefinitions(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> getDefinitions(Class<T> klass) {
		if (definitionsByClass.containsKey(klass)) {
			return (List) definitionsByClass.get(klass);
		}
		synchronized (target) {
			List definitions = new ArrayList();
			for (Object def : target.eContents()) {
				if (klass.isAssignableFrom(def.getClass())) {
					definitions.add(def);
				}
			}
			definitionsByClass.put(klass, Collections.unmodifiableList(definitions));
			return definitions;
		}
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.activityDictionary.IDefinitionContext#getDefinition(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T extends INamedDefinition> T getDefinition(Class<T> klass, String name) {
		if (definitionsByNameByClass.containsKey(klass)) {
			@SuppressWarnings("unchecked")
			Map<String, T> cache = (Map) definitionsByNameByClass.get(klass);
			return cache.get(name);
		}
		
		Map<String, T> definitionsByName = new HashMap<String, T>();
		for(T definition : getDefinitions(klass)) {
			definitionsByName.put(definition.getName(), definition);
		}
		definitionsByNameByClass.put(klass, definitionsByName);
		return definitionsByName.get(name);
	}
	
}
