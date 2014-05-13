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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.AutoListMap;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.resource.ProgressMonitorXMLLoadImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.impl.NotifyingListImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.ecore.xmi.IllegalValueException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;
import org.eclipse.emf.ecore.xmi.UnresolvedReferenceException;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <!-- begin-user-doc --> The <b>Resource </b> associated with the package. <!-- end-user-doc -->
 * 
 * @see gov.nasa.ensemble.core.model.plan.util.PlanResourceFactoryImpl
 * @generated NOT
 */
public class PlanResourceImpl extends XMIResourceImpl {

	private static final List<PlanBackwardsCompatibilityHandler> HANDLERS = ClassRegistry.createInstances(PlanBackwardsCompatibilityHandler.class);

	private static final String PLAN_VERSION_PROPERTY = "plan.persistence.version";
	private static final String PLAN_VERSION = EnsembleProperties.getProperty(PLAN_VERSION_PROPERTY);
	
	
	/**
	 * Creates an instance of the resource. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param uri
	 *            the URI of the new resource.
	 * @generated NOT
	 */
	public PlanResourceImpl(URI uri) {
		super(uri);
		setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
		getEObjectToIDMap();
		getIDToEObjectMap();
		XMLResource.URIHandler uriHandler = ClassRegistry.createInstance(PlanURIHandler.class);
		if (uriHandler != null) {
			initializeDefaultLoadOptions(uriHandler);
		}
		initializeDefaultSaveOptions(uriHandler); // calling this populates the member in the superclass which is accessed directly in save <ug!>
	}
	
	public void initializeDefaultLoadOptions(XMLResource.URIHandler uriHandler) {
		Map<Object, Object> options = getDefaultLoadOptions();
		options.put(XMLResource.OPTION_URI_HANDLER, uriHandler);
	}

	public void initializeDefaultSaveOptions(XMLResource.URIHandler uriHandler) {
		Map<Object, Object> options = getDefaultSaveOptions();
		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);
		options.put(XMLResource.OPTION_FLUSH_THRESHOLD, 1024 * 1024); // 1 MB flushing
		if (uriHandler != null) {
			options.put(XMLResource.OPTION_URI_HANDLER, uriHandler);
		}
	}
	
	/**
	 * Overriden to suppress notification
	 */
	@Override
	public EList<Diagnostic> getErrors() {
		if (errors == null) {
			errors = new NotifyingListImpl<Diagnostic>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean isNotificationRequired() {
					return false;
				}

				@Override
				public Object getNotifier() {
					return PlanResourceImpl.this;
				}

				@Override
				public int getFeatureID() {
					return RESOURCE__ERRORS;
				}
			};
		}
		return errors;
	}

	/**
	 * Overriden to suppress notification
	 */
	@Override
	public EList<Diagnostic> getWarnings() {
		if (warnings == null) {
			warnings = new NotifyingListImpl<Diagnostic>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean isNotificationRequired() {
					return false;
				}

				@Override
				public Object getNotifier() {
					return PlanResourceImpl.this;
				}

				@Override
				public int getFeatureID() {
					return RESOURCE__WARNINGS;
				}
			};
		}
		return warnings;
	}

	@Override
	protected XMLSave createXMLSave() {
		return createXMLSave(this.getContents(), true);
	}

	protected XMLSave createXMLSave(Map options) {
		if (options != null) {
			Object object = options.get(XMLResource.OPTION_ROOT_OBJECTS);
			if (object instanceof List) {
				@SuppressWarnings("unchecked")
				List<EObject> list = (List<EObject>) object;
				return createXMLSave(list, false);
			}
		}
		return createXMLSave();
	}

	private XMLSave createXMLSave(List<EObject> roots, boolean saveDefaults) {
		if (CommonPlugin.isJunitRunning()) {
			getEObjectToIDMap().clear();
			getIDToEObjectMap().clear();
		} else {
			System.gc(); // SPF-6899: frees about 14 MB for a template plan
		}
		return new PlanResourceSaveImpl(createXMLHelper(), saveDefaults);
	}
	
	@Override
	public void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		super.doLoad(inputStream, options);
		
		for (PlanBackwardsCompatibilityHandler handler : HANDLERS) {
			handler.postLoad(this);
		}
	}

	@Override
	public void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		XMLSave xmlSave = createXMLSave(options);
		if (options == null) {
			options = Collections.EMPTY_MAP;
		}
		ResourceHandler handler = (ResourceHandler) options.get(OPTION_RESOURCE_HANDLER);
		if (handler != null) {
			handler.preSave(this, outputStream, options);
		}
		xmlSave.save(this, outputStream, options);
		if (!CommonPlugin.isJunitRunning()) {
			System.gc(); // SPF-6899: frees about 70MB on template plan
		}
		if (handler != null) {
			handler.postSave(this, outputStream, options);
		}
	}
	
	@Override
	protected XMLLoad createXMLLoad() {
		final XMLResource planResource = this;
		return new PlanXMLLoadImpl(createXMLHelper(), planResource);
	}

	@Override
	protected PlanResourceXMLHelper createXMLHelper() {
		return new PlanResourceXMLHelper(this);
	}


	private static final class PlanResourceSaveImpl extends XMISaveImpl {
		
		private final boolean saveDefaults;

		private PlanResourceSaveImpl(XMLHelper helper, boolean saveDefaults) {
			super(helper);
			this.saveDefaults = saveDefaults;
		}

		@Override
		protected boolean shouldSaveFeature(EObject o, EStructuralFeature f) {
			if (saveDefaults) {
				return true;
			}
			return super.shouldSaveFeature(o, f);
		}
		
		@Override
		public void traverse(List<? extends EObject> contents) {
			super.traverse(contents);
			if (!CommonPlugin.isJunitRunning()) {
				System.gc(); // SPF-6899: frees about 43MB on template plan, 12MB on a regular plan
			}
		}

		@Override
		protected String getDatatypeValue(Object value, EStructuralFeature f, boolean isAttribute) {
			if (PlanPackage.Literals.EPLAN__MODEL_VERSION == f) {
				return PLAN_VERSION;
			}
			return super.getDatatypeValue(value, f, isAttribute);
		}
		
	}

	private static final class PlanXMLLoadImpl extends ProgressMonitorXMLLoadImpl {
		
		private final XMLResource planResource;

		private PlanXMLLoadImpl(XMLHelper helper, XMLResource planResource) {
			super(helper);
			this.planResource = planResource;
		}

		@Override
		protected DefaultHandler makeDefaultHandler() {
			return new SAXXMIHandler(resource, helper, options) {
				@Override
				protected void setAttribValue(EObject object, String name, String value) {
					for (SpecialAttributeHandler handler : SpecialAttributeHandlerRegistry.getHandlersFor(name)) {
						if (handler.maybeHandle(planResource, object, name, value)) {
							return;
						}
					}
					// Fall through to default behavior if no handler chose to handle it,
					// or if the one(s) that did also want the default behavior.
					super.setAttribValue(object, name, value);
				}

			};
		}

		@Override
		protected void handleErrors() throws IOException {
			Logger logger = Logger.getLogger(PlanResourceImpl.class);
			Map<String, List<FeatureNotFoundException>> featureNameToExceptions = new AutoListMap<String, FeatureNotFoundException>(String.class);
			Map<Object, List<IllegalValueException>> valueToExceptions = new AutoListMap<Object, IllegalValueException>(Object.class);
			Map<String, List<PackageNotFoundException>> uriToExceptions = new AutoListMap<String, PackageNotFoundException>(String.class);
			Map<String, List<org.eclipse.emf.ecore.xmi.ClassNotFoundException>> classNameToExceptions = new AutoListMap<String, org.eclipse.emf.ecore.xmi.ClassNotFoundException>(String.class);
			Map<String, List<UnresolvedReferenceException>> locationToUnresolvedReferenceExceptions = new AutoListMap<String, UnresolvedReferenceException>(String.class);
			for (Resource.Diagnostic error : resource.getErrors()) {
				if (error instanceof FeatureNotFoundException) {
					FeatureNotFoundException exception = (FeatureNotFoundException) error;
					String featureName = exception.getName();
					featureNameToExceptions.get(featureName).add(exception);
				} else if (error instanceof IllegalValueException) {
					IllegalValueException exception = (IllegalValueException) error;
					Object value = exception.getValue();
					if (value instanceof EMember) {
						value = value.getClass();
					}
					valueToExceptions.get(value).add(exception);
				} else if (error instanceof PackageNotFoundException) {
					PackageNotFoundException exception = (PackageNotFoundException) error;
					String uri = exception.uri();
					uriToExceptions.get(uri).add(exception);
				} else if (error instanceof UnresolvedReferenceException) {
					UnresolvedReferenceException exception = (UnresolvedReferenceException) error;
					locationToUnresolvedReferenceExceptions.get(exception.getLocation()).add(exception);
				} else if (error instanceof org.eclipse.emf.ecore.xmi.ClassNotFoundException) {
					org.eclipse.emf.ecore.xmi.ClassNotFoundException exception = (org.eclipse.emf.ecore.xmi.ClassNotFoundException) error;
					String className = exception.getName();
					classNameToExceptions.get(className).add(exception);
				} else {
					String message = error.getMessage();
					logger.warn(message);
				}
			}
			for (List<FeatureNotFoundException> exceptions : featureNameToExceptions.values()) {
				printGroupedExceptions(logger, exceptions);
			}
			for (List<IllegalValueException> exceptions : valueToExceptions.values()) {
				printGroupedExceptions(logger, exceptions);
			}
			for (List<PackageNotFoundException> exceptions : uriToExceptions.values()) {
				printGroupedExceptions(logger, exceptions);
			}
			{
				List<org.eclipse.emf.ecore.xmi.ClassNotFoundException> combinedExceptions
				= new LinkedList<org.eclipse.emf.ecore.xmi.ClassNotFoundException>();
				for (List<org.eclipse.emf.ecore.xmi.ClassNotFoundException> exceptions : classNameToExceptions.values()) {
					combinedExceptions.addAll(exceptions);
				}
				if (!combinedExceptions.isEmpty()) {	
					printUndefinedActivityTypes(logger, combinedExceptions);
				}
			}
			for  (List<UnresolvedReferenceException> exceptions : locationToUnresolvedReferenceExceptions.values()) {
				printUnresolvedReferences(logger, exceptions);
			}
		}

		/** Prints a list of undefined types (e.g. activity types not in AD). */
		private void printUndefinedActivityTypes(Logger logger, List<org.eclipse.emf.ecore.xmi.ClassNotFoundException> exceptions) {
			org.eclipse.emf.ecore.xmi.ClassNotFoundException firstException = exceptions.get(0);
			SortedSet<String> typeNames = new TreeSet<String>();
			for (org.eclipse.emf.ecore.xmi.ClassNotFoundException exception : exceptions) {
				typeNames.add(exception.getName()); 
			}
			StringBuilder builder = new StringBuilder("Unresolved type names in ");
			builder.append(firstException.getLocation());
			for (String typeName : typeNames) {
				builder.append("\n  * ");
				builder.append(typeName);
			}
			logger.warn(builder.toString());
		}

		/** Prints an id to line&column table, with line breaks. */
		private void printUnresolvedReferences(Logger logger, List<UnresolvedReferenceException> exceptions) {
			UnresolvedReferenceException firstException = exceptions.get(0);
			if (exceptions.size() == 1) {
				logger.warn(firstException);
				return;
			}
			StringBuilder builder = new StringBuilder("Unresolved references in ");
			builder.append(firstException.getLocation());
			builder.append("\n");
			int lineSize = builder.length();
			int count = 0;
			for (UnresolvedReferenceException exception : exceptions) {
				int line = exception.getLine();
				int column = exception.getColumn();
				String reference = exception.getReference();
				String message = "  " + line + ":" + column + " " + reference;
				if ((count != 0) && (count + message.length() > lineSize)) {
					builder.append("\n");
					count = 0;
				}
				builder.append(message);
				count += message.length();
			}
			if (count != 0) {
				builder.append("\n");
			}
			logger.warn(builder.toString());
		}

		private void printGroupedExceptions(Logger logger, List<? extends XMIException> exceptions) {
			XMIException exception = exceptions.get(0);
			String message = exception.getMessage();
			message = addExtras(exceptions, message);
			logger.warn(message);
		}

		private String addExtras(List<? extends Resource.Diagnostic> exceptions, String message) {
			int extra = exceptions.size() - 1;
			if (extra > 0) {
				message += " (plus " + extra + " more instances later in the file)";
			}
			return message;
		}
	}
	
} // PlanResourceImpl
