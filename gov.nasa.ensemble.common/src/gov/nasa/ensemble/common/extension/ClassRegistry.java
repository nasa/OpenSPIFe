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
package gov.nasa.ensemble.common.extension;

import static fj.data.Option.fromNull;
import fj.P;
import fj.P1;
import fj.data.Option;
import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ClassRegistry {

	private static Map<Class, List<Class>> classLookupMap = new HashMap<Class, List<Class>>();
	private static Map<Class, List<ClassRegistryFactory>> factoryLookupMap = new HashMap<Class, List<ClassRegistryFactory>>(); 
	
	public static <T> List<T> createInstances(Class<T> lookupClass, Class[] argTypes, Object[] argValues) {
		List<T> instances = new ArrayList<T>(); 
		for (Class<T> c : getClasses(lookupClass)) {
			try {
				Constructor<T> constructor = c.getConstructor(argTypes);
				instances.add(constructor.newInstance(argValues));
			} catch (Throwable t) {
				LogUtil.error("Error instantiating lookup class " + lookupClass, t);
			}
		}
		List<ClassRegistryFactory> factories = factoryLookupMap.get(lookupClass);
		if (factories != null) {
			for (ClassRegistryFactory<T> factory : factories) {
				try {
					instances.addAll(factory.createInstances());
				} catch (Throwable t) {
					LogUtil.error("Error creating instances from factory " + factory, t);
				}
			}
		}
		if (Comparable.class.isAssignableFrom(lookupClass)) {
			Collections.sort((List<? extends Comparable>)instances);
		}
		return Collections.unmodifiableList(instances);
	}
	
	public static <T> List<T> createInstances(Class<T> lookupClass) {
		return createInstances(lookupClass, new Class[0], new Object[0]);
	}
	
	public static <T> T createInstance(Class<T> lookupClass) {
		return createInstance(lookupClass, (T)null);
	}
	
	public static <T> T createInstance(Class<T> lookupClass, T defaultInstance) {
		return createInstance(lookupClass, P.p(defaultInstance));
	}	

	public static <T> T createInstance(Class<T> lookupClass, P1<T> defaultInstance) {
		List<T> instances = createInstances(lookupClass);
		if (instances.isEmpty())
			return defaultInstance._1();
		if (instances.size() > 1)
			LogUtil.warn("More than one instance registered for " + 
					lookupClass + ": " + instances + "; returning the first");
		return instances.get(0);
	}
	
	public static final <T> P1<Option<T>> lazyOptionalInstance(final Class<T> clazz) {
		return new P1<Option<T>>() {
			@Override
			public synchronized Option<T> _1() {
				return fromNull(createInstance(clazz));
			}
		}.memo();
	}
	
	public static final <T> P1<T> lazyRequiredInstance(final Class<T> clazz) {
		return new P1<T>() {
			@Override
			public synchronized T _1() {
				final T instance = createInstance(clazz);
				if (instance == null)
					throw new Error("Configuration error: " + clazz + " has no registered instances");
				return instance;
			}
		}.memo();
	}
	
	public static final <T> P1<T> lazyInstance(final Class<T> clazz, final P1<T> defaultInstance) {
		return new P1<T>() {
			@Override
			public T _1() {
				return Option.fromNull(ClassRegistry.createInstance(clazz)).orSome(defaultInstance);
			}
		}.memo();
	}
	
	public static final <T> P1<fj.data.List<T>> lazyInstances(final Class<T> clazz) {
		return new P1<fj.data.List<T>>() {
			@Override
			public fj.data.List<T> _1() {
				return Lists.fj(ClassRegistry.createInstances(clazz));
			}
		}.memo();
	}
	
	private synchronized static <T> List<Class<T>> getClasses(Class<T> lookupClass) {
		@SuppressWarnings("unchecked")
		List<Class> classes = classLookupMap.get(lookupClass);
		if (classes == null) {
			initialize(lookupClass);
			classes = classLookupMap.get(lookupClass);
			classLookupMap.put(lookupClass, classes);
		}
		return (List<Class<T>>) (classes == null ? Collections.<Class<T>>emptyList() : classes);
	}

	private static void initialize(final Class lookupClass) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry == null)
			return;
		IExtensionPoint extensionPoint = registry.getExtensionPoint("gov.nasa.ensemble.common.ClassRegistry");
		for (IConfigurationElement lookupElement : extensionPoint.getConfigurationElements()) {
			String className = lookupElement.getAttribute("class");
			if (!lookupClass.getName().equals(className))
				continue;
			Class clazz = ExtensionUtils.getClass(lookupElement, className);
			if (clazz != null) {
				List<Class> classes = classLookupMap.get(clazz);
				if (classes == null) {
					classes = new ArrayList<Class>();
					classLookupMap.put(clazz, classes);
				}
				
//				LogUtil.debug(clazz.getName());
				for (IConfigurationElement implementationElement : lookupElement.getChildren("implementation")) {
					Class implClass = ExtensionUtils.getClass(implementationElement, implementationElement.getAttribute("class"));
					if (implClass != null) {
						if (!clazz.isAssignableFrom(implClass)) {
							LogUtil.error("Class "+implClass.getName()+" is not an instance of "+clazz.getName()+" ignoring");
						}
						
//						LogUtil.debug("\t"+implClass.getName());
						classes.add(implClass);
					}
				}
				
				List<ClassRegistryFactory> factories = factoryLookupMap.get(clazz);
				if (factories == null) {
					factories = new ArrayList<ClassRegistryFactory>();
					factoryLookupMap.put(clazz, factories);
				}
				for (IConfigurationElement implementationElement : lookupElement.getChildren("factory")) {
					try {
						ClassRegistryFactory factory = (ClassRegistryFactory) implementationElement.createExecutableExtension("class");
						factories.add(factory);
//						LogUtil.debug("\t"+factory.getClass().getName());
					} catch (CoreException e) {
						LogUtil.error(e.getMessage(), e);
					}
				}
			}
		}
	}
}
