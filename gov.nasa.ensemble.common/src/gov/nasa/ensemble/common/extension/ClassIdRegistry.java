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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.WeakHashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Caches the class-to-ID mapping from the various plugin.xml files for rapid lookup, assuming that the class
 * has only one unique ID.
 * @author Eugene Turkov
 */
public final class ClassIdRegistry {
	
	private static final WeakHashMap<Class,String> classToIdMap =
		new WeakHashMap<Class,String>();
	
	  /**
	   * Configuration elements in the typical plugin.xml file can specify class,
	   * id, and other similar attributes. This method can be used to determine the
	   * unique ID assigned to a class in the plugin.xml file. However, if the same
	   * class is assigned multiple unique IDs, then this method will fail and throw
	   * a RuntimeException.
	   * <br><br>
	   * Example use: String id = getUniqueID(getClass(), "defaultHandler");
	   * <br><br>
	   * Lazy loading features are preserved when using this method.
	   * 
	   * @param clazz the class for which a unique ID should be retrieved.
	   * @param attributeName the name of the attribute whose value is the class
	   * @return the unique ID for the class, or null if one is not specified
	   * @throws RuntimeException if more than one valid ID is found.
	   */	
	  public static String getUniqueID(Class<?> clazz, String attributeName) {
		  String id = classToIdMap.get(clazz);
		  if(id == null) {
			  try {			
				  IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
				  IExtensionPoint[] extensionPoints = extensionRegistry.getExtensionPoints();
				  int idCount = 0;
				  for (IExtensionPoint extensionPoint : extensionPoints) {
					  IConfigurationElement[] configurationElements = extensionRegistry.getConfigurationElementsFor(extensionPoint.getUniqueIdentifier());
					  for (IConfigurationElement configurationElement : configurationElements) {
						  String attributeValue = configurationElement.getAttribute(attributeName);
						  String namespaceIdentifier = configurationElement.getNamespaceIdentifier();
						  if (attributeValue != null
								  && !namespaceIdentifier.equals("org.eclipse.emf.ecore.editor") // ignore this namespace, it causes problems
								  && attributeValue.equals(clazz.getName())) {
							  String newId = configurationElement.getAttribute("id");
							  if (idCount == 0) {
								  id = newId;
								  idCount++;
							  } else if (!newId.equalsIgnoreCase(id)) {
								  throw new RuntimeException("Multiple IDs" +
										  " found for " + attributeName + " " + clazz.getName());
							  }
						  }
					  }
				  }
			  }
	
			  catch (Exception ex) {
				  LogUtil.error(ex);
			  }
			  if (id != null) {
				  classToIdMap.put(clazz, id);
			  } else {
				  LogUtil.error("no id for you: " + clazz);
			  }
		  }

		  return id;
	  }
	  
	  /**
	   * Configuration elements in the typical plugin.xml file can specify class,
	   * id, and other similar attributes. This method can be used to determine the
	   * unique ID assigned to a class in the plugin.xml file. However, if the same
	   * class is assigned multiple unique IDs, then this method will fail and throw
	   * a RuntimeException. If the class attribute name is other than "class," you
	   * can use the other version of this method (different method signature).
	   * <br><br>
	   * Example use: String id = getUniqueID(getClass());
	   * <br><br>
	   * Lazy loading features are preserved when using this method.
	   * 
	   * @param clazz the class for which a unique ID should be retrieved.
	   * @return the unique ID for the class, or null if one is not specified
	   * @throws RuntimeException if more than one valid ID is found.
	   */	  
	  public static String getUniqueID(Class<?> clazz) {
		  return getUniqueID(clazz, "class");
	  }
}
