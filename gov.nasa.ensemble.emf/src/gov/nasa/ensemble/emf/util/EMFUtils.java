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
package gov.nasa.ensemble.emf.util;

import static fj.P.*;
import static fj.data.List.*;
import static fj.data.Option.*;
import fj.F;
import fj.P2;
import fj.data.Option;
import fj.data.Stream;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.NoTestRequired;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.functional.Predicates;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.operation.CompositeOperation;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.DefaultStringifier;
import gov.nasa.ensemble.emf.Activator;
import gov.nasa.ensemble.emf.EnsembleEditingDomain;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.model.common.CommonFactory;
import gov.nasa.ensemble.emf.model.common.ObjectFeature;
import gov.nasa.ensemble.emf.resource.IgnorableResource;
import gov.nasa.ensemble.emf.transaction.RunnableWithThrowable;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.NotifyingList;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class EMFUtils {
	
	public static final ComposedAdapterFactory GLOBAL_ADAPTER_FACTORY = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	public static final Predicate<Resource> IGNORABLE_RESOURCE_PREDICATE = new Predicate<Resource>() {

		@Override
		public boolean apply(Resource resource) {
			return resource instanceof IgnorableResource;
		}
		
	};
	public static final String ANNOTATION_SOURCE_DESCRIPTOR = "descriptor";
	private static final String ANNOTATION_SOURCE_DETAIL = "detail";
	private static final String ANNOTATION_DETAIL_DISPLAY_NAME = "displayName";
	private static final String ANNOTATION_DESCRIPTOR_DISPLAY_NAME = "display_name";
	private static final String ANNOTATION_DESCRIPTOR_CATEGORY = "category";
	private static final String ANNOTATION_DESCRIPTOR_STRINGIFIER = "stringifier";
	private static final String ANNOTATION_DESCRIPTOR_STRINGIFIER_PARAMETER_VALUE = "stringifier.parameter";
	private static final Map<String, EDataType> COMMON_TYPES_BY_STRING = new HashMap<String, EDataType>();
	private static final Map<EDataType, String> COMMON_TYPES_BY_TYPE = new HashMap<EDataType, String>();
	static {
		COMMON_TYPES_BY_STRING.put("boolean", EcorePackage.Literals.EBOOLEAN_OBJECT);
		COMMON_TYPES_BY_STRING.put("date", EcorePackage.Literals.EDATE);
		COMMON_TYPES_BY_STRING.put("double", EcorePackage.Literals.EDOUBLE_OBJECT);
		COMMON_TYPES_BY_STRING.put("float", EcorePackage.Literals.EFLOAT_OBJECT);
		COMMON_TYPES_BY_STRING.put("integer", EcorePackage.Literals.EINTEGER_OBJECT);
		COMMON_TYPES_BY_STRING.put("long", EcorePackage.Literals.ELONG_OBJECT);
		COMMON_TYPES_BY_STRING.put("short", EcorePackage.Literals.ESHORT_OBJECT);
		COMMON_TYPES_BY_STRING.put("string", EcorePackage.Literals.ESTRING);
		for (Map.Entry<String, EDataType> entry : COMMON_TYPES_BY_STRING.entrySet())
			COMMON_TYPES_BY_TYPE.put(entry.getValue(), entry.getKey());
		// add aliases.
		COMMON_TYPES_BY_STRING.put("int", EcorePackage.Literals.EINTEGER_OBJECT);
		COMMON_TYPES_BY_TYPE.put(EcorePackage.Literals.EBOOLEAN, "boolean");
	}

	private static Map<ResourceSet, Map<EClassifier, Collection<EObject>>> TYPES_BY_RESOURCE_SET_CACHE = new WeakHashMap<ResourceSet, Map<EClassifier, Collection<EObject>>>();
	
	@SuppressWarnings("unchecked")
	public static <T> T adapt(Object input, Class<T> type) {
		/*
		 * Notice that the TransactionlEditingDomain is now replacing
		 * the former EditingDomain associated with the ResourceSet so 
		 * we should call TransactionUtils.getDomain(input) instead.
		 */
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(input);
		if (domain instanceof AdapterFactoryEditingDomain) {
			AdapterFactoryEditingDomain aDomain = (AdapterFactoryEditingDomain) domain;
			AdapterFactory adapterFactory = aDomain.getAdapterFactory();
			if (adapterFactory != null) {
				T adapter = (T) adapterFactory.adapt(input, type);
				if (adapter != null)
					return adapter;
			}
		}
		if (input instanceof EObject) {
			Resource resource = ((EObject) input).eResource();
			if (resource != null) {
				ResourceSet resourceSet = resource.getResourceSet();
				if (resourceSet != null) {
					EList<AdapterFactory> adapterFactories = resourceSet.getAdapterFactories();
					for (AdapterFactory factory : adapterFactories) {
						if (factory.isFactoryForType(type)) {
							T adapter = (T)factory.adapt(input, type);
							if (adapter != null)
								return adapter;
						}
					}
				}
			}
			/*
			 * If the loaded resource does not have an EditingDomain, 
			 * then we need to use the ItemProviderAdapter.
			 */
			Object adapter = null;
			for (Adapter inputAdapter : ((EObject)input).eAdapters()) {
				if (type.isInstance(inputAdapter)) {
					adapter = inputAdapter;
					break;
				}
			}
			if (adapter instanceof ItemProviderAdapter) {
				ItemProviderAdapter itemProviderAdapter = (ItemProviderAdapter) adapter;
				AdapterFactory adapterFactory = itemProviderAdapter.getAdapterFactory();
				if (adapterFactory != null) {
					return (T) adapterFactory.adapt(input, type);
				}
			}
		}
		AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		return (T) adapterFactory.adapt(input, type);
	}
	
	public static String getText(EObject object, String defaultText) {
		IItemLabelProvider lp = adapt(object, IItemLabelProvider.class);
		if (lp != null) {
			return lp.getText(object);
		}
		return defaultText;
	}
	
	public static EditingDomain createEditingDomain() {
		return new EnsembleEditingDomain();
	}
	
	public static ResourceSet createResourceSet() {
		return createEditingDomain().getResourceSet();
	}
	
	public static AdapterFactory getAdapterFactory(Object object) {
		EditingDomain domain = null;
		if (object instanceof EditingDomain) {
			domain = (EditingDomain) object;
		} else {
			domain = AdapterFactoryEditingDomain.getEditingDomainFor(object);
		}
		if (domain instanceof AdapterFactoryEditingDomain) {
			AdapterFactoryEditingDomain aDomain = (AdapterFactoryEditingDomain) domain;
			AdapterFactory adapterFactory = aDomain.getAdapterFactory();
			if (adapterFactory != null) {
				return adapterFactory;
			}
		}
		return new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	}
	
	public static String convertToXML(EObject eObject) {
		Map<String, Object> options = getDefaultXmlOptions();
		return convertToXML(eObject, options);
	}

	public static Map<String, Object> getDefaultXmlOptions() {
		Map<String, Object> options = new HashMap<String, Object>(); 
		options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_LINE_WIDTH, 80);
		return options;
	}

	public static String convertToXML(EObject eObject, Map<String, Object> options) {
		List<? extends EObject> contents = Collections.singletonList(eObject);
		options.put(XMLResource.OPTION_ROOT_OBJECTS, contents);
		boolean temporaryResource = false;
		Resource resource = eObject.eResource();
		EObject eRoot = null;
		if (resource == null || !(resource instanceof XMLResource)) {
			ResourceSet resourceSet = createResourceSet();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMLResourceFactoryImpl());
			resource = resourceSet.createResource(URI.createURI(""));
			temporaryResource = true;
			eRoot = EcoreUtil.getRootContainer(eObject);
			resource.getContents().add(eRoot);
		}
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				resource.save(stream, options);
				String string;
				if (options.get(XMLResource.OPTION_ENCODING) != null) {
					string = stream.toString(options.get(XMLResource.OPTION_ENCODING).toString());
				} else {
					string = stream.toString();
				}
				return string;
			} catch (IOException e) {
				throw new IllegalStateException("Error converting " + eObject + " to string", e);
			}
		} finally {
			if (temporaryResource) {
				resource.getContents().remove(eRoot);
			}
		}
	}
	
	public static String toString(final Resource resource) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			resource.save(out, null);
			return out.toString();
		} catch (IOException e) {
			LogUtil.error(e);
			return "<error converting resource to string>";
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				LogUtil.error(e);
			}
		}
	}
	
	public static EObject createFromXML(String xml) {
		ResourceSet resourceSet = createResourceSet();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMLResourceFactoryImpl());
		Resource resource = resourceSet.createResource(URI.createURI("createFromString.xml"));
		InputStream stream = new ByteArrayInputStream(xml.getBytes());
		try {
			resource.load(stream, null);
		} catch (IOException e) {
			throw new IllegalStateException("Error creating EObject from string " + xml, e);
		}
		return resource.getContents().remove(0);
	}
	
	public static void addAnnotation(EModelElement eNamedElement, String source, String... details) {
		EAnnotation eAnnotation = eNamedElement.getEAnnotation(source);
		if (eAnnotation == null) {
			eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			eAnnotation.setSource(source);
			eNamedElement.getEAnnotations().add(eAnnotation);
		}
		EMap<String, String> theDetails = eAnnotation.getDetails();
		for (int i = 1; i < details.length; i += 2) {
			theDetails.put(details[i - 1], details[i]);
		}
	}
	
	public static String getDescriptorAnnotation(EModelElement element, String key) {
		return EMFUtils.getAnnotation(element, ANNOTATION_SOURCE_DESCRIPTOR, key);
	}
	
	public static String getEEnumLiteralCategory(EEnumLiteral literal) {
		return getDescriptorAnnotation(literal, ANNOTATION_DESCRIPTOR_CATEGORY);
	}
	
	/**
	 * 
	 * @param parameter in an AD activity definition (or global attributes)
	 * @return null if none (or if no such class), else a IStringifier instance
	 */
	public static IStringifier getParameterStringifier(EAttribute parameter) {
		String className = getDescriptorAnnotation(parameter, ANNOTATION_DESCRIPTOR_STRINGIFIER);
		String parameterValue = getDescriptorAnnotation(parameter, ANNOTATION_DESCRIPTOR_STRINGIFIER_PARAMETER_VALUE);
		if (className==null) return null;
		try {
			Class<?> clazz = Class.forName(className);
			if (IStringifier.class.isAssignableFrom(clazz)) {
				if (parameterValue != null) {
					Constructor<?> constructor = clazz.getConstructor(String.class);
					return (IStringifier) constructor.newInstance(parameterValue);
				} else {
					return (IStringifier) clazz.newInstance();
				}
			} else {
				LogUtil.error(parameter.getName() + " AD definition refers to non-stringifier class " + className);
				return null;
			}
		} catch (ClassNotFoundException e) {
			LogUtil.error(parameter.getName() + " AD definition refers to undefined stringifier class " + className, e);
			return null;
		} catch (InstantiationException e) {
			LogUtil.error("Error instantiating " + className + " referenced from " + parameter.getName() + "'s AD definition with no arguments.", e);
			return null;
		} catch (IllegalAccessException e) {
			LogUtil.error("Error accessing " + className + " referenced from " + parameter.getName() + "'s AD definition.", e);
			return null;
		} catch (SecurityException e) {
			LogUtil.error(e);
			return null;
		} catch (NoSuchMethodException e) {
			LogUtil.error(e);
			return null;
		} catch (IllegalArgumentException e) {
			LogUtil.error(e);
			return null;
		} catch (InvocationTargetException e) {
			LogUtil.error(e);
			return null;
		}
	}
	
	public static String getAnnotation(EModelElement eNamedElement, String source, String key) {
		if (eNamedElement != null) {
			EAnnotation annotation = eNamedElement.getEAnnotation(source);
			if (annotation != null) {
				return annotation.getDetails().get(key);
			}
		}
		return null;
	}
	
	public static void hideAttribute(EModelElement element) {
		addAnnotation(element, "detail", new String[] { "hidden", "true" });
	}
	
	public static Boolean testBooleanAnnotation(EModelElement feature, String source, String key) {
		String booleanString = getAnnotation(feature, source, key);
		return Boolean.parseBoolean(booleanString);
	}
	
	public static void setBooleanAnnotation(EModelElement feature, String source, String key, boolean value) {
		addAnnotation(feature, source, key, Boolean.toString(value));
	}


	/**
	 * Given an object and one of its EStructuralFeatures, return the property descriptor for the feature. Assumes that the
	 * PropertyDescriptor's propertyID is the EStructuralFeature's name.
	 * 
	 * @param object
	 * @param feature
	 * @return the property descriptor for the feature
	 */
	public static IItemPropertyDescriptor getFeatureDescriptor(Object object, EStructuralFeature feature) {
		IItemPropertySource source = adapt(object, IItemPropertySource.class);
		if (source == null) {
			String objectName = null;
			if (object instanceof EObject) {
				objectName = getDisplayName((EObject)object);
			} else if (object == null) {
				objectName = "null";
			} else {
				objectName = object.toString();
			}
			LogUtil.warnOnce("Could not find a property source for " + objectName);
			return null;
		}
		return source.getPropertyDescriptor(object, feature.getName());
	}

	/**
	 * Returns the IItemLabelProvider for the object's feature
	 * 
	 * @param object
	 * @param feature
	 * @return the IItemLabelProvider for the object's feature
	 */
	public static IItemLabelProvider getItemLabelProvider(Object object, EStructuralFeature feature) {
		IItemPropertyDescriptor descriptor = getFeatureDescriptor(object, feature);
		return descriptor == null ? null : descriptor.getLabelProvider(object);
	}
	
	/**
	 * Returns a list of the objects with the given klass added in this notification, if any. To be used only with features that are
	 * lists.
	 * 
	 * @param <T>
	 * @param notification
	 * @param klass
	 * @return a list of the objects with the given klass added in this notification, if any
	 */
	public static <T> List<T> getAddedObjects(Notification notification, Class<T> klass) {
		int eventType = notification.getEventType();
		Object newValue = notification.getNewValue();
		if ((eventType == Notification.ADD) || (eventType == Notification.SET)) {
			if (klass.isInstance(newValue)) {
				T object = klass.cast(newValue);
				return Collections.singletonList(object);
			}
		} else if (eventType == Notification.ADD_MANY) {
			if (newValue instanceof List) {
				List<?> list = (List<?>) newValue;
				return getTypedObjects(list, klass);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns a list of the objects with the given EClass added in this notification, if any. To be used only with features that
	 * are lists.
	 * 
	 * @param <T>
	 * @param notification
	 * @param eClass
	 * @return a list of the objects with the given EClass added in this notification, if any
	 */
	public static List<? extends EObject> getAddedObjects(Notification notification, EClass eClass) {
		int eventType = notification.getEventType();
		Object newValue = notification.getNewValue();
		if ((eventType == Notification.ADD) || (eventType == Notification.SET)) {
			if (eClass.isInstance(newValue)) {
				EObject eNewValue = (EObject) newValue;
				return Collections.singletonList(eNewValue);
			}
		} else if (eventType == Notification.ADD_MANY) {
			if (newValue instanceof List) {
				List<?> list = (List<?>) newValue;
				return getTypedObjects(list, eClass);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns a list of the objects with the given klass removed in this notification, if any. To be used only with features that
	 * are lists.
	 * 
	 * @param <T>
	 * @param notification
	 * @param klass
	 * @return a list of the objects with the given klass removed in this notification, if any
	 */
	public static <T> List<T> getRemovedObjects(Notification notification, Class<T> klass) {
		int eventType = notification.getEventType();
		Object oldValue = notification.getOldValue();
		if ((eventType == Notification.REMOVE) || (eventType == Notification.SET)) {
			if (klass.isInstance(oldValue)) {
				T object = klass.cast(oldValue);
				return Collections.singletonList(object);
			}
		} else if (eventType == Notification.REMOVE_MANY) {
			if (oldValue instanceof List) {
				List<?> list = (List<?>) oldValue;
				return getTypedObjects(list, klass);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns a list of the objects with the given EClass removed in this notification, if any. To be used only with features that
	 * are lists.
	 * 
	 * @param <T>
	 * @param notification
	 * @param eClass
	 * @return a list of the objects with the given EClass removed in this notification, if any
	 */
	public static List<? extends EObject> getRemovedObjects(Notification notification, EClass eClass) {
		int eventType = notification.getEventType();
		Object value = notification.getOldValue();
		if ((eventType == Notification.REMOVE) || (eventType == Notification.SET)) {
			if (eClass.isInstance(value)) {
				EObject eValue = (EObject) value;
				return Collections.singletonList(eValue);
			}
		} else if (eventType == Notification.REMOVE_MANY) {
			if (value instanceof List) {
				List<?> list = (List<?>) value;
				return getTypedObjects(list, eClass);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns a typed list of objects of the given klass that are contained in the list.
	 * 
	 * @param <T>
	 * @param list
	 * @param klass
	 * @return a typed list of objects of the given klass that are contained in the list
	 */
	private static <T> List<T> getTypedObjects(List<?> list, Class<T> klass) {
		List<T> result = new ArrayList<T>();
		for (Object object : list) {
			if (klass.isInstance(object)) {
				result.add(klass.cast(object));
			}
		}
		return result;
	}

	/**
	 * Returns a typed list of objects of the given EClass that are contained in the list.
	 * 
	 * @param <T>
	 * @param list
	 * @param klass
	 * @return a typed list of objects of the given EClass that are contained in the list
	 */
	private static List<? extends EObject> getTypedObjects(List<?> list, EClass eClass) {
		List<EObject> result = new ArrayList<EObject>();
		for (Object object : list) {
			if (object instanceof EObject) {
				if (eClass.isInstance(object)) {
					result.add((EObject) object);
				}
			}
		}
		return result;
	}

	/**
	 * Invoke this after creating a dynamic package and all its classes to
	 * ensure that:
	 * 
	 * a) all feature IDs are set before CDO creates parallel CDOFeatures for
	 * them
	 * 
	 * b) the package is registered globally so that it is available for sharing
	 * 
	 * This fixes the -1 ArrayIndexOutOfBoundsException problem when sharing
	 * your model
	 */
	public static void finalizeDynamicModel(EPackage pkg) {
		try {
			ReflectionUtils.invoke(pkg, "fixEClassifiers");
		} catch (Throwable t) {
			if (!CommonPlugin.isJunitRunning()) {
				LogUtil.error("error in finalization", t);
			}
		}
		EPackage.Registry.INSTANCE.put(pkg.getNsURI(), pkg);
	}

	public static Object cast(Object value, EStructuralFeature eFeature) {
		EClassifier eType = eFeature.getEType();
		if (value instanceof Number) {
			if (EcorePackage.Literals.EDOUBLE == eType
					|| EcorePackage.Literals.EDOUBLE_OBJECT == eType) {
				return ((Number) value).doubleValue();
			} else if (EcorePackage.Literals.EFLOAT == eType
					|| EcorePackage.Literals.EFLOAT_OBJECT == eType) {
				return ((Number) value).floatValue();
			} else if (EcorePackage.Literals.EINT == eType
					|| EcorePackage.Literals.EINTEGER_OBJECT == eType) {
				return ((Number) value).intValue();
			}
		}
		return value;
	}

	public static <T extends EObject> T copy(T object) {
		return EcoreUtil.copy(object);
	}

	public static Resource copy(final Resource resource) {
		final ResourceSet set = resource.getResourceSet();
		final Registry registry;
		if (set != null)
			registry = set.getResourceFactoryRegistry();
		else
			registry = Registry.INSTANCE;
		final Factory factory = registry.getFactory(resource.getURI());
		if (factory == null)
			throw new RuntimeException("No resource factory found for URI '" + resource.getURI() + "'");
		return copy(factory, resource);
	}
	
	public static Resource copy(final Factory factory, final Resource resource) {
		final Resource newResource = factory.createResource(resource.getURI());
		newResource.setTimeStamp(resource.getTimeStamp());
		newResource.setModified(resource.isModified());
		newResource.getContents().addAll(EcoreUtil.copyAll(resource.getContents()));
		return newResource;
		
	}

	/**
	 * Applies a filter to a list of EObjects, using an instance of a generated
	 * switch.
	 * 
	 * @param list
	 *            the list to filter
	 * @param switchObject
	 *            should be of type FooSwitch<Boolean>, where Foo is the package
	 *            to which T belongs. This argument represents a test which
	 *            returns true iff a given object from the original list should
	 *            remain in the resulting list. Switch methods which return null
	 *            will be treated the same as those that return false, so it's not
	 *            necessary to specify a default case which returns false.
	 * @return a new list which contains only elements which passed the test
	 *         represented by switchObject
	 */
	public static <T extends EObject> List<T> filter(List<T> list, Object switchObject) {
		List<T> filtered = new ArrayList<T>();
		for (T obj : list) {
			Object filterResult = ReflectionUtils.invoke(switchObject, "doSwitch", obj);
			if (filterResult != null && (Boolean)filterResult)
				filtered.add(obj);
		}
		return filtered;
	}

	/**
	 * Maps one list of objects to a new list of objects, using an instance of a
	 * generated switch.
	 * 
	 * @param list
	 *            the list to listenerMap
	 * @param switchObject
	 *            should be of type FooSwitch<T>, where Foo is a generated
	 *            package and T is the type of the resulting mapped list.
	 * @return a new list of the same size as the old one, containing the
	 *         results of mapping the switchObject to each object in the input
	 *         list in succession
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> map(List<? extends EObject> list, Object switchObject) {
		List<T> mapped = new ArrayList<T>();
		for (EObject obj : list)
			mapped.add((T)ReflectionUtils.invoke(switchObject, "doSwitch", obj));
		return mapped;
	}
	
	/**
	 * Combines "filter" and "listenerMap"; for each object in the original list, if the
	 * switchObject returns null, it will not be added to the resulting list.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> filterMap(List<? extends EObject> list, Object switchObject) {
		List<T> mapped = new ArrayList<T>();
		for (EObject obj : list) {
			T obj2 = (T)ReflectionUtils.invoke(switchObject, "doSwitch", obj);
			if (obj2 != null)
				mapped.add(obj2);
		}
		return mapped;
	}
	
	@NoTestRequired
	protected static void printDiagnostic(Diagnostic diagnostic, String indent) {
		System.out.print(indent);
		System.out.println(diagnostic.getMessage());
		for (Diagnostic child : diagnostic.getChildren()) {
			printDiagnostic(child, indent + "  ");
		}
	}
	
	public static EList<EObject> getContainingList(EObject element) {
		EReference containmentFeature = element.eContainmentFeature();
		if (containmentFeature == null || !containmentFeature.isMany())
			return null;
		EObject container = element.eContainer();
		if (container == null)
			return null;
		EList<EObject> containingList = (EList<EObject>)container.eGet(containmentFeature);
		return containingList;
	}
	
	/**
	 * @return the index of this object within its parent's containment feature,
	 *         if it has a parent and the containment feature is many
	 */
	public static int getIndexWithinParent(EObject element) {
		EList<EObject> containingList = getContainingList(element);
		if (containingList != null)
			return ECollections.indexOf(containingList, element, 0);
		return -1;
	}

	/**
	 * Either sets the given object on the given feature of the given destination
	 * object or adds it, depending on whether the feature is many or not.
	 * Optionally provide an index at which to insert (-1 means add to the end)
	 * @return the old value of this feature, if the feature is not many
	 */
	@SuppressWarnings("unchecked")
	public static Object setOrAdd(EObject destination, EStructuralFeature feature, Object object, int index) {
		if (feature.isMany()) {
			if (object != null) {
				List list = (List)destination.eGet(feature);
				if (index == -1 || index > list.size())
					list.add(object);
				else
					list.add(index, object);
			}
			return null;
		} else {
			Object oldValue = destination.eGet(feature);
			destination.eSet(feature, object);
			return oldValue;
		}
	}

	public static Object setOrAdd(EObject destination, EStructuralFeature feature, Object object) {
		return setOrAdd(destination, feature, object, -1);
	}
	
	public static IStringifier<?> getStringifier(EStructuralFeature feature) {
		if (feature instanceof EAttribute) {
			return getStringifier((EAttribute)feature);
		} else {
			return getStringifier((EReference)feature);
		}
	}
	
	/**
	 * Returns the stringifier for formatting values of this attribute for display and conversely parsing strings into values.
	 * Usually this is a function of the attribute's type (e.g. Date), what plugins are loaded (e.g. MSLICE supports Mars time),
	 * and how ensemble.properties is configured (e.g. date.format.*), but it's also possible for the Activity Dictionary to
	 * override this for a particular parameter (e.g. a date parameter that has no hours and minutes).
	 * @param attribute -- AD parameter
	 * @return stringifier for formatting <T> values as strings and parsing strings as <T> values
	 */
	public static IStringifier<?> getStringifier(EAttribute attribute) {
		IStringifier<?> customStringifier = getParameterStringifier(attribute);
		if (customStringifier != null) {
			return customStringifier;
		} else {
			return getStringifier(attribute.getEAttributeType());
		}
	}
	
	public static IStringifier<?> getStringifier(EReference reference) {
		if (StringifierRegistry.hasRegisteredStringifier(reference.getName())) {
			return StringifierRegistry.getStringifier(reference.getName());
		}
		return new EObjectStringifier();
	}
	
	/**
	 * getStringifier(eAttribute.getEAttributeType() is deprecated.
	 * Use getStringifier(eAttribute) instead, when applicable.
	 */
	public static IStringifier<?> getStringifier(final EDataType eDataType) {
		
		if(eDataType instanceof EEnumImpl) {
			return new EEnumStringifier(eDataType);
		}
		
		
		IStringifier stringifier = null;
		// First check the type name, using hasRegisteredStringifier to avoid unnecessary warnings
		// from getStringifier
		boolean hasStringifier = StringifierRegistry.hasRegisteredStringifier(eDataType.getName());
		if (hasStringifier) {
			stringifier = StringifierRegistry.getStringifier(eDataType.getName());
		} else {
			// Next check the instance name
			Class<?> instanceClass = eDataType.getInstanceClass();
			if (instanceClass != null) {
				stringifier = StringifierRegistry.getStringifier(instanceClass);
			}
			// Do not use the DefaultStringifier since we would prefer to delegate to the EFactory instance
			if (stringifier instanceof DefaultStringifier) {
				return new EDataTypeStringifier(eDataType);
			}
		}
		return stringifier;
	}
	
	/**
	 * @deprecated use getFile(URI) instead
	 */
	@Deprecated
	public static IFile findFile(URI uri) {
		return getFile(uri);
	}
	
	public static IFile getFile(Resource resource) {
		return getFile(resource.getURI());
	}

	public static IFile getFile(URI uri) {
		if (uri != null && uri.isPlatformResource()) {
			String platformString = uri.toPlatformString(true);
			Path platformPath = new Path(platformString);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			return root.getFile(platformPath);
		}
		return null;
	}
	
	public static boolean canWrite(Resource resource) {
		File file = getJavaFile(resource.getURI());
		if (file != null) {
			return file.canWrite();
		}
		return true;
	}
	
	public static File getJavaFile(URI uri) {
		File file = null;
		if (uri.isPlatformResource()) {
			IFile iFile = getFile(uri);
			if (iFile != null) {
				IPath path = iFile.getLocation();
				if (path != null) {
					file = path.toFile();
				}                                  
			}          
		}
		else {
			file = new File(uri.toFileString());
		}
		return file;
	}
	
	public static IProject getProject(EObject object) {
		IProject project = null;
		Resource resource = object.eResource();
		if(resource != null) {
			URI uri = resource.getURI();
			IFile file = getFile(uri);
			if (file != null) {
				project = file.getProject();
			}
		}
		return project;
	}

	/**
	 * Create a platform resource URI for the given IResource
	 * 
	 * @param resource
	 * @return
	 */
	public static URI getURI(IResource resource) {
		IPath path = resource.getFullPath();
		return getURI(path);
	}

	public static URI getURI(IPath path) {
		return URI.createPlatformResourceURI(path.toString(), true);
	}

	public static URI createURI(URL url) throws URISyntaxException {
		return URI.createURI(url.toURI().toString());
	}
	
	public static <T extends EObject> Option<T> findAncestor(EObject object, Class<T> clazz) {
		return (Option<T>)getAncestors(object).find(Predicates.<EObject>isA(clazz));
	}
	
	public static Stream<EObject> getAncestors(final EObject object) {
		return Stream.unfold(new F<EObject, Option<P2<EObject, EObject>>>() {
			@Override
			public Option<P2<EObject, EObject>> f(final EObject object) {
				if (null == object)
					return none();
				return some(p(object, object.eContainer()));
			}
		}, object);
	}
	
	public static void populateReference(EObject object, EReference reference) {
		if (!object.eIsSet(reference)) {
			final EFactory factory = object.eClass().getEPackage().getEFactoryInstance();
			object.eSet(reference, factory.create(reference.getEReferenceType()));
		}
	}
	
	public static List<EObject> getAllContents(EObject object) {
		final TreeIterator<EObject> iterator = object.eAllContents();
		final List<EObject> list = new ArrayList<EObject>();
		while (iterator.hasNext())
			list.add(iterator.next());
		return list;
	}
	
	public static fj.data.List<P2<EStructuralFeature, EObject>> getAllFeatureValuePairs(final EObject object) {
		return iterableList(object.eClass().getEAllAttributes()).map(
			new F<EAttribute, P2<EStructuralFeature, EObject>>() {
			@Override
			public P2<EStructuralFeature, EObject> f(final EAttribute attr) {
				return p((EStructuralFeature)attr, object);
			}
		}).append(iterableList(object.eClass().getEAllReferences()).bind(
			new F<EReference, fj.data.List<P2<EStructuralFeature, EObject>>>() {
				@Override
				public fj.data.List<P2<EStructuralFeature, EObject>> f(final EReference ref) {
					final P2<EStructuralFeature, EObject> pair = p((EStructuralFeature)ref, object);
					final Object referent = object.eGet(ref);
					if (referent == null)
						return single(pair);
					final fj.data.List<EObject> referents;
					if (referent instanceof EList)
						referents = iterableList((EList<EObject>)referent);
					else
						referents = single((EObject)referent);
					return referents.bind(new F<EObject, fj.data.List<P2<EStructuralFeature, EObject>>>() {
						@Override
						public fj.data.List<P2<EStructuralFeature, EObject>> f(final EObject obj) {
							return iterableList(getAllFeatureValuePairs(obj).cons(pair));
						}
					});
				}
			}
		));
	}

	/**
	 * Return the reference values as a list regardless of the isMany() state
	 */
	public static List<Object> eGetAsList(EObject object, EStructuralFeature structuralFeature) {
		List<Object> result = new ArrayList<Object>();
		if(structuralFeature instanceof EReference) {
			result.addAll(eGetAsList(object, (EReference) structuralFeature));
		} else {
			result.addAll(eGetAsList(object, (EAttribute) structuralFeature));
		}
		return result;
	}
	
	public static List<Object> eGetAsList(EObject object, EAttribute attribute) {
		List<Object> result = new ArrayList<Object>();
		Object attributeValue = object.eGet(attribute);
		if(attributeValue != null) {
			if(attribute.isMany()) {			
				if(attributeValue instanceof List) {
					result.addAll((Collection<? extends EObject>) attributeValue);	
				}			
			} else {
				result.add(attributeValue);
			}
		}
		return result;
	}
	
	public static List<EObject> eGetAsList(EObject object, EReference structuralFeature) {
		List<EObject> result = new ArrayList<EObject>();
		if (structuralFeature.isMany()) {
			result.addAll((Collection<? extends EObject>) object.eGet(structuralFeature));
		} else {
			EObject value = (EObject) object.eGet(structuralFeature);
			if (value != null) {
				result.add(value);
			}
		}
		return result;
	}
	
	public static void copyOnto(EObject from, EObject to) {
		copyOnto(from, to, false, false);
	}
	
	public static void copyOnto(EObject from, EObject to, boolean onlyIfSet) {
		copyOnto(from, to, onlyIfSet, false);
	}
	
	public static void copyOnto(EObject from, EObject to, boolean onlyIfSet, boolean includeSuperFeatures) {
		for (EStructuralFeature feature : from.eClass().getEAllStructuralFeatures()) {
			final EClass eClass = to.eClass();
			final EList<EStructuralFeature> features = includeSuperFeatures ? eClass.getEAllStructuralFeatures() : eClass.getEStructuralFeatures();
			if (features.contains(feature)) {
				if (feature.isChangeable() && (!onlyIfSet || from.eIsSet(feature))) {
					if (feature instanceof EReference) {
						if (feature.isMany()) {
							if (((EReference)feature).isContainment()) {
								to.eSet(feature, EcoreUtil.copy((EObject)from.eGet(feature)));
							} else {
								final List dstList = (List)to.eGet(feature);
								dstList.clear();
								for (EObject object : (List<EObject>)from.eGet(feature))
									dstList.add(EcoreUtil.copy(object));
							}
						} else {
							to.eSet(feature, EcoreUtil.copy((EObject)from.eGet(feature)));
						}
					} else {
						to.eSet(feature, from.eGet(feature));
					}
				}
			}
		}
	}
	
	public static String convertToString(EDataType eDataType) {
		if (COMMON_TYPES_BY_TYPE.containsKey(eDataType))
			return COMMON_TYPES_BY_TYPE.get(eDataType);
		EPackage ePackage = eDataType.getEPackage();
		String nsURI = ePackage.getNsURI();
		URI uri = URI.createURI(nsURI);
		URI uriWithIdFragment = uri.appendFragment("//" + eDataType.getName());
		if (eDataType.eResource() != null && eDataType.eResource().getResourceSet() != null && eDataType.eResource().getResourceSet().getEObject(uriWithIdFragment, false) == eDataType)
			return uriWithIdFragment.toString();
		
		return uri.appendFragment(eDataType.getName()).toString();
	}
	
	public static EDataType createEDataTypeFromString(String string) {
		if (COMMON_TYPES_BY_STRING.containsKey(string.toLowerCase()))
			return COMMON_TYPES_BY_STRING.get(string.toLowerCase());
		ResourceSet resourceSet = createResourceSet();
		try {
			URI uri = URI.createURI(string);
			return (EDataType) resourceSet.getEObject(uri, true);
		}
		catch(Exception ex) {
			return null;
		}
	}

	public static void contributeResources(ResourceSet resourceSet, IContainer container) {
		contributeResources(resourceSet, container, null);
	}
	
	public static void contributeResources(ResourceSet resourceSet, IContainer container, Predicate<IResource>filter) {
		if (container == null || !container.exists()) {
			return;
		}
		IResource[] members = new IResource[0];
		try {
			members = container.members();
		} catch (CoreException e) {
			LogUtil.error(e);
		}
		for (IResource member : members) {
			if (member instanceof IContainer) {
				contributeResources(resourceSet, (IContainer) member);
				continue;
			}
			if (filter != null && !filter.apply(member)) {
				continue;
			}
			URI memberUri = getURI(member);
			try {
				resourceSet.getResource(memberUri, true);
			} catch (Exception e) {
				try {
					resourceSet.getResource(memberUri, true);
				} catch (Exception x) {
					// we tried
				}
			}
		}
	}
	
	public static void readFromByteArray(final Resource resource, final byte[] bytes, final Map options) {
		try {
			resource.load(new ByteArrayInputStream(bytes), options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] writeToByteArray(final Resource resource,
			final Map options) {
		// MAE-4898 - run this inside a transaction since it can potentially
		// modify the resource while saving
		return TransactionUtils.writing(resource, new RunnableWithResult.Impl<byte[]>() {
			@Override
			public void run() {
				final ByteArrayOutputStream stream = new ByteArrayOutputStream();
				try {
					resource.save(stream, options);
					setResult(stream.toByteArray());
				} catch (IOException e) {
					throw new RuntimeException(e);
				} finally {
					try {
						stream.close();
					} catch (IOException e) {
						LogUtil.error(e);
					}
				}
			}
		});
	}
	
	public static IUndoContext getUndoContext(EditingDomain domain) {
		if (domain != null) {
			return new ObjectUndoContext(domain);
		}
		return IOperationHistory.GLOBAL_UNDO_CONTEXT;
	}

	public static IUndoContext getUndoContext(EObject object, EditingDomain domain) {
		IUndoContext undoContext = adapt(object, IUndoContext.class);
		if (undoContext != null)
			return undoContext;
		return getUndoContext(domain);
	}
	
	public static IUndoContext getUndoContext(EObject object) {
		return getUndoContext(object, AdapterFactoryEditingDomain.getEditingDomainFor(object));
	}
	
	public static boolean removeNoSettableDescriptor(Object object, List descriptors, EAttribute attribute) {
		Iterator<IItemPropertyDescriptor> iterator = descriptors.iterator();
		while (iterator.hasNext()) {
			IItemPropertyDescriptor descriptor = iterator.next();
			if (descriptor.getFeature(object) == attribute && !descriptor.canSetProperty(object)) {
				iterator.remove();
				return true;
			}
        }
		return false;
	}

	 public static <T extends EObject> T copy(EditingDomain domain, T eObject) {
		CopyCommand command = (CopyCommand) CopyCommand.create(domain, eObject);
		command.execute();
		Collection result = command.getResult();
		return (T) result.toArray(new EObject[0])[0];
	}
	
	 public static void clearReachableObjectsOfType(ResourceSet resourceSet) {
		 Map<EClassifier, Collection<EObject>> cache = TYPES_BY_RESOURCE_SET_CACHE.remove(resourceSet);
		 if (cache != null) {
			 cache.clear();
		 }
	 }
	 
	public static void setReachableObjectsOfType(ResourceSet resourceSet, EClassifier type, Collection<EObject> collection) {
		Map<EClassifier, Collection<EObject>> cache = TYPES_BY_RESOURCE_SET_CACHE.get(resourceSet);
		if (cache == null) {
			cache = new HashMap<EClassifier, Collection<EObject>>();
			TYPES_BY_RESOURCE_SET_CACHE.put(resourceSet, cache);
		}
		cache.put(type, new ArrayList<EObject>(collection));
	}
	
	public static Collection<?> getPossibleValues(EObject target, IItemPropertyDescriptor pd, EClassifier type) {
		if (type instanceof EEnum) {
			return ((EEnum)type).getELiterals();
		} else if (type instanceof EDataType) {
			return pd.getChoiceOfValues(target);
		} else {
			return EMFUtils.getReachableObjectsOfType(target, type);
		}
	}
	 
	public static Collection<EObject> getReachableObjectsOfType(final EObject object, final EClassifier type) {
		if (object == null) {
			return Collections.emptyList();
		}
		Resource resource = object.eResource();
		if (resource != null) {
			ResourceSet resourceSet = resource.getResourceSet();
			if (resourceSet != null) {
				Map<EClassifier, Collection<EObject>> cache = TYPES_BY_RESOURCE_SET_CACHE.get(resourceSet);
				if (cache == null) {
					cache = new HashMap<EClassifier, Collection<EObject>>();
					TYPES_BY_RESOURCE_SET_CACHE.put(resourceSet, cache);
				}
				Collection<EObject> collection = cache.get(type);
				if (collection == null) {
					TransactionalEditingDomain domain = TransactionUtils.getDomain(resourceSet);
					if (domain != null) {
						collection = TransactionUtils.reading(domain, new RunnableWithResult.Impl<Collection<EObject>>() {
							@Override
							public void run() {
								try {
									setResult(ItemPropertyDescriptor.getReachableObjectsOfType(object, type));
									setStatus(Status.OK_STATUS);
								} catch (Exception e) {
									setStatus(new ExceptionStatus(Activator.PLUGIN_ID, "failed in reading", e));
								}
							}
						});
					} else {
						collection = ItemPropertyDescriptor.getReachableObjectsOfType(object, type);
					}
					cache.put(type, collection);
				}
				return collection;
			}
		}
		return ItemPropertyDescriptor.getReachableObjectsOfType(object, type);
	}
	
	public static void reload(final Resource resource) throws IOException {
		TransactionUtils.writing(resource, IOException.class, new RunnableWithThrowable() {
			@Override
			public void run() throws IOException {
				resource.unload();
				resource.load(null);
			}
		});
	}
	
	public static Object getPropertyValue(IItemPropertyDescriptor pd, Object model) {
		Object value = pd.getPropertyValue(model);
		if (value instanceof PropertyValueWrapper) {
			value = ((PropertyValueWrapper)value).getEditableValue(model);
		}
		return value;
	}

	public static EditingDomain getAnyDomain(Object object) {
		if (object instanceof EStructuralFeature.Setting) {
			EStructuralFeature.Setting map = (EStructuralFeature.Setting) object;
			object = map.getEObject();
		}
		if (object instanceof Collection) {
			Collection<?> collection = (Collection<?>) object;
			if (collection.isEmpty()) {
				return null;
			}
			object = collection.iterator().next();
		}
		if (object instanceof EditingDomain) {
			return (EditingDomain)object;
		}
		if (object instanceof Resource) {
			Resource resource = (Resource) object;
			IEditingDomainProvider editingDomainProvider =
		        (IEditingDomainProvider)EcoreUtil.getExistingAdapter(resource, IEditingDomainProvider.class);
		    if (editingDomainProvider != null) {
		        return editingDomainProvider.getEditingDomain();
		    }
			object = resource.getResourceSet();
		}
		if (object instanceof ResourceSet) {
			ResourceSet resourceSet = (ResourceSet) object;
			if (resourceSet instanceof IEditingDomainProvider) {
				IEditingDomainProvider editingDomainProvider = (IEditingDomainProvider) resourceSet;
				return editingDomainProvider.getEditingDomain();
	        }
			IEditingDomainProvider editingDomainProvider = (IEditingDomainProvider)EcoreUtil.getExistingAdapter(resourceSet, IEditingDomainProvider.class);
	        if (editingDomainProvider != null) {
	            return editingDomainProvider.getEditingDomain();
	        }
		}
		return AdapterFactoryEditingDomain.getEditingDomainFor(object);
	}

	public static boolean isEditable(Object object) {
		EditingDomain editingDomain = getAnyDomain(object);
		if (editingDomain == null) {
			return true;
		}
		Resource resource = object instanceof EObject 
							? ((EObject)object).eResource() : 
								object instanceof Resource 
								? (Resource)object : null;
		return resource == null || !editingDomain.isReadOnly(resource);
	}

	public static String getDisplayName(EObject eObject) {
		IItemLabelProvider lp = adapt(eObject, IItemLabelProvider.class);
		if (lp != null) return lp.getText(eObject);
		EStructuralFeature nameFeature = eObject.eClass().getEStructuralFeature("name");
		if (nameFeature != null) {
			return (String) eObject.eGet(nameFeature);
		}
		else {
			return "Unnamed " + eObject.eClass().getName();
		}
	}
	
	public static String getDisplayName(EObject target, EStructuralFeature feature) {
		String displayName = getAnnotation(feature, ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_DISPLAY_NAME);
		if (displayName == null) {
			displayName = getAnnotation(feature, ANNOTATION_SOURCE_DESCRIPTOR, ANNOTATION_DESCRIPTOR_DISPLAY_NAME);
		}
		if (displayName == null) {
			IItemPropertySource source = adapt(target, IItemPropertySource.class);
			if (source != null) {
				IItemPropertyDescriptor itemPropertyDescriptor = source.getPropertyDescriptor(target, feature);
				if (itemPropertyDescriptor != null) {
					displayName = itemPropertyDescriptor.getDisplayName(feature);
				}
			}
		}
		if (displayName == null) {
			displayName = feature.getName();
		}
		return displayName;
	}

	public static boolean exists(URI uri) {
		return new ExtensibleURIConverterImpl().exists(uri, null);
	}

	/**
	 * If the URI is not in the resourceSet yet, create the resource.
	 * If the URI can be loaded, do so.
	 * 
	 * @param resourceSet
	 * @param uri
	 * @param contentType specify as null for default behavior
	 * @return a Resource
	 */
	public static Resource loadOrCreateResource(ResourceSet resourceSet, URI uri, String contentType) {
		Resource resource = resourceSet.getResource(uri, false);
		if (resource == null) {
			resource = resourceSet.createResource(uri, contentType);
		}
		boolean exists = resourceSet.getURIConverter().exists(uri, null);
		if (exists) {
			try {
				resource.load(resourceSet.getLoadOptions());
			} catch (IOException e) {
				LogUtil.error("can't load the resource from: " + uri, e);
				try {
					resource.load(resourceSet.getLoadOptions());
				} catch (IOException e1) {
					// okay whatever
				}
			}
		}
		return resource;
	}

	public static void save(Resource primaryResource, String description, IProgressMonitor monitor) {
		save(primaryResource, description, monitor, IGNORABLE_RESOURCE_PREDICATE);
	}
	
	/**
	 * Save the resource set contents.
	 * 
	 * Starts by saving the primary resource.  If an exception
	 * occurs, the exception is wrapped and thrown.
	 * 
	 * If the primary resource saving completes successfully,
	 * then this saves the other resources in the order in which 
	 * they occur in the primary resource's resource set.  Resources 
	 * that are IgnorableResource are not saved.  Any exceptions that
	 * occur while saving these resources are logged and saving 
	 * continues.
	 * 
	 * Exceptions that occur 
	 * 
	 * @param resourceSet
	 * @param primaryResource
	 * @param description
	 * @param monitor
	 */
	public static void save(Resource primaryResource, String description, IProgressMonitor monitor, Predicate<Resource> ignorable) {
		ResourceSet resourceSet = primaryResource.getResourceSet();
		EList<Resource> resources = resourceSet.getResources();
		monitor.beginTask(description, resources.size());
		try {
			Map options = new HashMap();
			options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
			// The following option may be useful in some circumstances, e.g. plan AD updates.
//			options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
			try {
				updateMonitorWithResource(monitor, primaryResource);
				primaryResource.save(options);
			} catch (Exception e) {
				throw new IllegalStateException("saving resource to "+primaryResource.getURI(), e);
			}
			monitor.worked(1);
			Notifier notifier = null;
			AdapterImpl adapter = null;
			if (resources instanceof NotifyingList) {
				notifier = (Notifier)((NotifyingList) resources).getNotifier();
				adapter = new ResourceSetModificationLogger();
			}
			// SPF-10495 -- Synchronize with any concurrent threads that try to add to or remove from the resources of the resource set
			synchronized (resources) {
				try {
					if (notifier != null) {
						notifier.eAdapters().add(adapter);
					}
					for (Resource resource : resources) {
						if (primaryResource != resource) {
							if (!ignorable.apply(resource)) {
								try {
									updateMonitorWithResource(monitor, resource);
									resource.save(options);
								} catch (Exception e) {
									LogUtil.error("saving resource "+resource.getURI()+" not saved: " + e.getMessage());
								}
							}
							monitor.worked(1);
						}
					}
				} finally {
					if (notifier != null) {
						notifier.eAdapters().remove(adapter);
					}
				}
			}
		} finally {
			monitor.done();
		}
	}

	/**
	 * Update the monitor to display the current resource name (if any) as the subtask.
	 *  
	 * @param monitor
	 * @param resource
	 */
	public static void updateMonitorWithResource(IProgressMonitor monitor, Resource resource) {
		String lastSegment = resource.getURI().lastSegment();
		if (lastSegment != null) {
			String resourceName = CommonUtils.decodeUTF8(lastSegment);
			monitor.subTask(resourceName);
		}
	}
	
	public static void unresolveProxies(EObject object) {
		for (EReference r : object.eClass().getEReferences()) {
			if (object.eContainmentFeature() == r) {
				continue;
			}
			for (EObject referencedObject : eGetAsList(object, r)) {
				if (r.isContainment() && referencedObject instanceof BasicEObjectImpl) {
					unresolveProxies(referencedObject);
				} else {
					unresolveProxy(referencedObject);
				}
			}
		}
	}

	public static void unresolveProxy(EObject referencedObject) {
		((BasicEObjectImpl)referencedObject).eSetProxyURI(EcoreUtil.getURI(referencedObject));
	}

	private static final class ResourceSetModificationLogger extends AdapterImpl {
		@Override
		public void notifyChanged(Notification msg) {
			switch (msg.getEventType()) {
			case Notification.ADD:
			case Notification.ADD_MANY:
			case Notification.REMOVE:
			case Notification.REMOVE_MANY:
				LogUtil.error("resource set modified during save: "+msg, new UnsupportedOperationException("modify during save"));
			}
		}
	}

	public static EStructuralFeature eGetFeature(EObject eObject, String featureName) {
		EStructuralFeature f = eObject.eClass().getEStructuralFeature(featureName);
		if (f != null)
			return f;
		ArrayList<String> features = new ArrayList<String>();
		for (EStructuralFeature sf : eObject.eClass().getEAllStructuralFeatures())
			features.add(sf.getName());
		throw new RuntimeException("Feature '" + featureName + "' not found. Available features: " + features + " for class " + eObject.eClass().getName());
	}

	/**
	 * This is a much faster method for simply testing to see if something has a choice of values.
	 * It is implemented based on getChoiceOfValues from ItemPropertyDescriptor.
	 * 
	 * @param pd
	 * @param object
	 * @return
	 */
	public static boolean hasChoiceOfValues(IItemPropertyDescriptor pd, Object object) {
		if (object instanceof EObject) {
			Object feature = pd.getFeature(object);
			if (feature instanceof EReference[]) {
				return true;
			} 
			if (feature instanceof EReference) {
				return true;
			} 
			if (feature instanceof EAttribute) {
				EAttribute attribute = (EAttribute) feature;
				if (attribute.getEType() instanceof EEnum) {
					return true;
				}
				EDataType eDataType = attribute.getEAttributeType();
				List<String> enumeration = ExtendedMetaData.INSTANCE.getEnumerationFacet(eDataType);
				if (!enumeration.isEmpty()) {
					return true;
				}
			}
		}
		// must fall back on property descriptor
		Collection<?> values = getChoiceOfValues(pd, object);
		return (values != null && !values.isEmpty());
	}
	
	/**
	 * This method of getChoiceOfValues uses our fast EMFUtils cache in the case where
	 * the property descriptor is typically slow, and falls back on the property descriptor 
	 * for cases where the property descriptor is typically fast enough.
	 * 
	 * NOTE: because this caches the result value it is not appropriate to call this in cases
	 *       where the reference may have new possible values created or old possible values 
	 *       deleted from the resource set the object is in.
	 * 
	 * @param pd
	 * @param object
	 * @return
	 */
	public static Collection<?> getChoiceOfValues(IItemPropertyDescriptor pd, Object object) {
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			Object feature = pd.getFeature(object);
			if (feature instanceof EReference) {
				EReference reference = (EReference) feature;
				return getReachableObjectsOfType(eObject, reference.getEType());
			}
		}
		return pd.getChoiceOfValues(object);
	}

	public static void setResourceReadOnlyFor(EObject eObject, boolean isReadOnly) {
		try {
			EditingDomain eDomain = AdapterFactoryEditingDomain.getEditingDomainFor(eObject);
			if (eDomain != null && eDomain instanceof AdapterFactoryEditingDomain) {
				if (!eDomain.isReadOnly(eObject.eResource()))
					((AdapterFactoryEditingDomain) eDomain).getResourceToReadOnlyMap().put(eObject.eResource(), isReadOnly);
			}
		} catch (Exception e) {
			LogUtil.error("Could not set resource as read-only. Either the resource doesn't have an EditingDomain or it doesn't have a map used to maintain read only state.", e);
		}
	}

	public static boolean equals(final Resource r1, final Resource r2) {
		final EList<EObject> contents1 = r1.getContents();
		final EList<EObject> contents2 = r2.getContents();
		if (contents1.size() != contents2.size())
			return false;
		
		for (int i = 0; i < contents1.size(); i++) {
			final EObject o1 = contents1.get(i);
			final EObject o2 = contents2.get(i);
			if (!EcoreUtil.equals(o1, o2))
				return false;
		}
		
		return true;
	}
	
	public static Collection<EObject> getConstraintStatusLocus(EObject target, EStructuralFeature feature) {
		Collection<EObject> locus = new ArrayList<EObject>(1);
		ObjectFeature objectFeature = CommonFactory.eINSTANCE.createObjectFeature();
		objectFeature.setObject(target);
		objectFeature.setFeature(feature);
		locus.add(objectFeature);
		return locus;
	}
	
	public static Collection<EObject> getConstraintStatusLocus(EObject target, EClass eClass, Collection<String> featureNames) {
		Collection<EObject> locus = new ArrayList<EObject>();
		for (String name : featureNames) {
			EStructuralFeature feature = eClass.getEStructuralFeature(name);
			if (feature != null) {
				ObjectFeature objectFeature = CommonFactory.eINSTANCE.createObjectFeature();
				objectFeature.setObject(target);
				objectFeature.setFeature(feature);
				locus.add(objectFeature);
			}
		}
		return locus;
	}
	
	public static Collection<EObject> getConstraintStatusLocus(EObject target, EStructuralFeature feature, Collection<ObjectFeature> dependencies) {
		Collection<EObject> locus = new ArrayList<EObject>();
		ObjectFeature objectFeature = CommonFactory.eINSTANCE.createObjectFeature();
		objectFeature.setObject(target);
		objectFeature.setFeature(feature);
		locus.add(objectFeature);
		if (dependencies != null) {
			locus.addAll(dependencies);
		}
		return locus;
	}

	/**
	 * Dump error diagnostics to a logger, and then dump the
	 * warning diagnostics to the logger.
	 * 
	 * @param logger
	 * @param resource
	 */
	public static void logDiagnostics(Logger logger, Resource resource) {
		for (org.eclipse.emf.ecore.resource.Resource.Diagnostic diagnostic : resource.getErrors()) {
			String message = diagnostic.getMessage();
			logger.error(message);
		}
		for (org.eclipse.emf.ecore.resource.Resource.Diagnostic diagnostic : resource.getWarnings()) {
			String message = diagnostic.getMessage();
			logger.warn(message);
		}
	}
	
	/**
	 * For a recently loaded EMF resource that is supposed to contain one
	 * EObject, return that EObject.  Returns null if there is no resource
	 * or no EObject in the contents.  Warns if there is more than one
	 * object.  Logs diagnostics if there are no contents.
	 * 
	 * @param <T>
	 * @param resource
	 * @return
	 */
	public static <T extends EObject> T getLoadedContent(Resource resource) {
		if (resource == null) {
			return null;
		}
		EList<EObject> contents = resource.getContents();
		if (contents.isEmpty()) {
			logDiagnostics(LogUtil.logger(), resource);
			return null;
		}
		int size = contents.size();
		if (size > 1) {
			LogUtil.warn("expected only one content object, found " + size);
		}
		return (T)contents.get(0);
	}

	/**
	 * Given a path that is relative to the source base,
	 * return a path that is relative to the target base.
	 * Returns null if provided null as the path.
	 * 
	 * @param targetBase
	 * @param sourceBase
	 * @param path
	 * @return
	 */
	public static IPath resolveDeresolve(URI targetBase, URI sourceBase, IPath path) {
		if (path == null || path.isEmpty()) {
			return null;
		}
		URI pathURI = URI.createURI(path.toString());
		URI resolvedURI = pathURI.resolve(sourceBase);
		URI deresolvedURI = resolvedURI.deresolve(targetBase);
		String deresolvedString = deresolvedURI.toString();
		return new Path(deresolvedString);
	}
	
	public static void executeCommand(EditingDomain domain, Command command) {
		CommandUndoableOperation operation = new CommandUndoableOperation(domain, command);
		IUndoContext undoContext = getUndoContext(domain);
		CommonUtils.execute(operation, undoContext);
	}
	
	/** @deprecated First argument is unnecessary. */
	@Deprecated
	public static void useProjectURIConverter(ResourceSet resourceSet, Resource resource) {
		useProjectURIConverter(resource);
	}

	/** Avoids "Unresolved reference to project:/Resources/CrewMembers.resource#FE_2" */
	public static void useProjectURIConverter(Resource resource) {
		if (resource != null) {
			IFile file = EMFUtils.getFile(resource);
			if (file != null) {
				useProjectURIConverter(resource, file.getProject());
			}
		}
	}

	/** Avoids "Unresolved reference to project:/Resources/CrewMembers.resource#FE_2" */
	public static void useProjectURIConverter(Resource resource, IProject project) {
		if (resource != null && project != null) {
			resource.getResourceSet().setURIConverter(new ProjectURIConverter(project));
		}
	}

	public static final Collection<UpdateValueOperationContributor> CONTRIBUTORS = ClassRegistry.createInstances(UpdateValueOperationContributor.class);
	
	public static IUndoableOperation addContributorOperations(IUndoableOperation operation, EObject target, 
			EStructuralFeature feature, Object oldValue, Object newValue) {
		if (!CONTRIBUTORS.isEmpty()) {
			CompositeOperation toDoList = new CompositeOperation(operation.getLabel());
			toDoList.add(operation);
			for (UpdateValueOperationContributor contributor : CONTRIBUTORS) {
				contributor.contributeOperations(toDoList, target, feature, oldValue, newValue);
			}
			return toDoList;
		}
		return operation;
	}
	
}
