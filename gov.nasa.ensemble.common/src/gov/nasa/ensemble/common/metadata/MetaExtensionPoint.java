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
package gov.nasa.ensemble.common.metadata;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import fj.F;
import fj.P;
import fj.P1;
import fj.P2;
import fj.data.Option;
import gov.nasa.ensemble.common.extension.ExtensionUtils;
import gov.nasa.ensemble.common.logging.LogUtil;

public class MetaExtensionPoint {
	public static final P1<Map<String, List<PropertyChangeListener>>> listenerMap = 
		new P1<Map<String, List<PropertyChangeListener>>>() {
		@Override
		public Map<String, List<PropertyChangeListener>> _1() {
			return initialize()._1();
		}
	}.memo();
	
	public static final P1<Map<String, MetaDataDefaultFactory>> defaultFactoryMap = 
		new P1<Map<String, MetaDataDefaultFactory>>() {
		@Override
		public Map<String, MetaDataDefaultFactory> _1() {
			return initialize()._2();
		}
	}.memo();

	private static 
	P2<Map<String, List<PropertyChangeListener>>, Map<String, MetaDataDefaultFactory>> 
	initialize() {
		final Map<String, List<PropertyChangeListener>> listenerMap = 
			new HashMap<String, List<PropertyChangeListener>>();
		final Map<String, MetaDataDefaultFactory> defaultFactoryMap = 
			new HashMap<String, MetaDataDefaultFactory>();

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			final IExtensionPoint extensionPoint = registry.getExtensionPoint("gov.nasa.ensemble.common.metadata");
			for (final IConfigurationElement extension : extensionPoint.getConfigurationElements()) {
				final String extName = extension.getName();
				if ("listener".equals(extName)) {
					final IConfigurationElement listenerElement = extension;
					final String className = listenerElement.getAttribute("class");
					final Class<PropertyChangeListener> listenerClass = 
						ExtensionUtils.getClass(listenerElement, className);
					for (final IConfigurationElement keys : listenerElement.getChildren("key")) {
						final String key = keys.getAttribute("name");
						try {
							List<PropertyChangeListener> listeners = listenerMap.get(key);
							if (listeners == null) {
								listeners = new ArrayList<PropertyChangeListener>();
								listenerMap.put(key, listeners);
							}
							listeners.add(listenerClass.newInstance());
						} catch (Exception e) {
							LogUtil.error(e);
						}
					}
				} else if ("defaultValueFactory".equals(extName)) {
					final IConfigurationElement defaultElement = extension;
					final String className = defaultElement.getAttribute("class");
					final Class<MetaDataDefaultFactory> factoryClass = 
						ExtensionUtils.getClass(defaultElement, className);
					for (final IConfigurationElement keys : defaultElement.getChildren("key")) {
						final String key = keys.getAttribute("name");
						try {
							final MetaDataDefaultFactory preexisting = defaultFactoryMap.get(key);
							if (preexisting != null) {
								LogUtil.warn("More than one default value registered for key " + key);
								continue;
							}
							defaultFactoryMap.put(key, factoryClass.newInstance());
						} catch (Exception e) {
							LogUtil.error(e);
						}
					}
				}
			}
		}
		return P.p(listenerMap, defaultFactoryMap);
	}
	
	public static List<PropertyChangeListener> getListeners(final String key) {
		return Option.fromNull(listenerMap._1().get(key)).orSome(Collections.<PropertyChangeListener>emptyList());
	}
	
	public static <T> Option<T> getDefault(final Object object, final String key) {
		return Option.fromNull(defaultFactoryMap._1().get(key)).bind(
			new F<MetaDataDefaultFactory, Option<T>>() {
				@Override
				public Option<T> f(final MetaDataDefaultFactory input) {
					return input.getDefault(object, key);
				}
			});
	}
}
