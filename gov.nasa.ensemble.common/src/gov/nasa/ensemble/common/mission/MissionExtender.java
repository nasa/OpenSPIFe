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
package gov.nasa.ensemble.common.mission;

import fj.P1;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * MissionExtender provides access to mission-specific versions of particular classes within Ensemble. 
 * 
 * To override a core Ensemble class with mission-specific functionality, follow these steps:
 * 
 *   1.  Tag the mission-independent class with the MissionExtendable interface
 *   2.  Make all of the constructors in the class protected or private.
 *   3.  Write your mission-specific class within a mission-specific plugin, extending the mission-independent class.
 *   4.  Have your mission-specific plugin extend the gov.nasa.ensemble.common.MissionExtension extension point.
 * 
 */
public class MissionExtender {
	
	private static final HashMap<String, TreeSet<String>> extensionRegistryClassNames = new HashMap<String, TreeSet<String>>();
	private static final HashMap<String, Bundle> nameToBundle = new HashMap<String, Bundle>();
	
	private static final String MISSION_EXTENSIONS = "gov.nasa.ensemble.common.MissionExtension";

	private MissionExtender() {
		// Not Constructable
	}
	
	static {
		//TODO: Enforce the rule that the extendable object must implement MissionExtendable and have all hidden constructor
		try {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			if(registry != null) {
				IExtensionPoint extensionPoint = registry.getExtensionPoint(MISSION_EXTENSIONS);
				IExtension[] extensions = extensionPoint.getExtensions();
				for (IExtension extension : extensions) {
					Bundle bundle = Platform.getBundle(extension.getContributor().getName());
					IConfigurationElement[] elements = extension.getConfigurationElements();
					for (IConfigurationElement element : elements) {
						try {
							String missionIndependent = element.getAttribute("MissionIndependentClass");
							String missionSpecific    = element.getAttribute("MissionSpecificClass");
							TreeSet<String> set = extensionRegistryClassNames.get(missionIndependent);
							if (set == null) {
								// Use TreeSet/Comparator to break ties by using alphabetical order, for reproducibility
								set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER); 
								extensionRegistryClassNames.put(missionIndependent, set);
							}
							set.add(missionSpecific);
							nameToBundle.put(missionSpecific, bundle);
						} catch (RuntimeException e) {
							LogUtil.error(e);
						}
					}
				}
			}
		} catch (RuntimeException e) {
			LogUtil.error(e);
		}
	}
	
	public static <T extends MissionExtendable> T construct(Class<T> type, Object ... args) throws ConstructionException  {
		// TODO: Support null arguments
		// Check to see if any plugins have registered a mission-specific version of this class.
		Class[] argTypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		return construct(type, argTypes, args);
	}
	
	public static <T extends MissionExtendable> P1<T> constructLazily(final Class<T> type, final Object ... args) {
		return new P1<T>() {
			@Override
			public T _1() {
				try {
					return construct(type, args);
				} catch (ConstructionException e) {
					throw new Error(e);
				}
			}
		}.memo();
	}
	
	public static <T extends MissionExtendable> T constructSafely(Class<T> type, Object ... args) {
		return constructSafely(type, false, args);
	}
	
	public static <T extends MissionExtendable> T constructSafely(Class<T> type, boolean warnOnException, Object ... args) {
		try {
			return construct(type, args);
		} catch (ConstructionException ce) {
			if (warnOnException)
				Logger.getLogger(MissionConstants.class).warn("Couldn't construct mission-specific version of " 
						+ type.getSimpleName());
			return null;
		}
	}
	
	/**
	 * Constructs an object with the given array of arguments that dictates
	 * which classes the constructed object will be constructed with.
	 * 
	 * @param type
	 * @param argTypes
	 * @param args
	 * @return not supplied
	 * @throws ConstructionException
	 */
	public static <T extends MissionExtendable> T construct(Class<T> type, Class[] argTypes, Object... args)
			throws ConstructionException {
		Class<? extends T> missionSpecificType = getMissionSpecificClass(type);
		try {
			Constructor<? extends T> constructor = findMatchingConstructor(missionSpecificType, argTypes);
			// Override access control on the constructor
			constructor.setAccessible(true);
			return constructor.newInstance(args);
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			throw new ConstructionException(t);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Constructor<? extends T> findMatchingConstructor(Class<? extends T> klass, Class[] argTypes)
			throws NoSuchMethodException {
		Constructor[] constructors = klass.getDeclaredConstructors();
		Constructor<? extends T> constructor = null;
		for (int i = 0; i < constructors.length; i++) {
		    if (argumentsContentsMatch(argTypes, constructors[i].getParameterTypes())) {
		        constructor = constructors[i];
		        break;
		    }
		}
		if (constructor == null) {
			throw new NoSuchMethodException("couldn't find a matching constructor for MissionExtendable");
		}
		return constructor;
	}
	
    @SuppressWarnings("unchecked")
	private static boolean argumentsContentsMatch(Class[] instanceTypes, Class[] methodArgumentTypes) {
        if (instanceTypes == null) {
            return methodArgumentTypes == null || methodArgumentTypes.length == 0;
        }
        if (methodArgumentTypes == null) {
            return instanceTypes.length == 0;
        }
        if (instanceTypes.length != methodArgumentTypes.length) {
            return false;
        }
        for (int i = 0; i < instanceTypes.length; i++) {
            if (!methodArgumentTypes[i].isAssignableFrom(instanceTypes[i])) {
                return false;
            }
        }
        return true;
    }

	@SuppressWarnings("unchecked")
	private static <T extends MissionExtendable> Class<? extends T> getMissionSpecificClass(Class<T> type) {
		String extendableClassName = type.getName();
		TreeSet<String> extendedClassNames = extensionRegistryClassNames.get(extendableClassName);
		if (extendedClassNames != null) {
			Class lastExtendedClass = null;
			for (String extendedClassName : extendedClassNames) {
				Class<?> extendedClass;
				try {
					Bundle bundle = nameToBundle.get(extendedClassName);
					extendedClass = bundle.loadClass(extendedClassName);
				} catch (ClassNotFoundException e) {
					String message = "ClassNotFoundException while instantiating MissionExtender " 
						           + extendedClassName + " of " + extendableClassName;
					LogUtil.error(message, e);
					throw new Error(message, e);
				}
				if (lastExtendedClass != null) {
					if (!lastExtendedClass.isAssignableFrom(extendedClass) && !extendedClass.isAssignableFrom(lastExtendedClass)) {
						String message = "mission independent class " + extendableClassName + 
								" has multiple independent mission specific associations: " + 
								lastExtendedClass.getName() + ", " + extendedClassName;
						Error error = new Error(message);
						LogUtil.error(error);
						throw error;
					}
					if (extendedClass.isAssignableFrom(lastExtendedClass)) {
						// the new class is a superclass of the one we already saw
						continue;
					}
				}
				lastExtendedClass = extendedClass;
			}
			if (lastExtendedClass != null) {
				return lastExtendedClass;
			}
		}
		return type;
	}

	public static boolean hasMissionSpecificClass(Class type) {
		Class missionSpecificClass = getMissionSpecificClass(type);
		return !(CommonUtils.equals(type, missionSpecificClass));
	}
	
	@SuppressWarnings("serial")
	public static class ConstructionException extends Exception {
		public ConstructionException(Throwable cause) {
			super(cause);
		}
	}
	
}
