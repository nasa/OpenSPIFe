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
package gov.nasa.ensemble.core.activityDictionary;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.io.XMLUtilities;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryEvent.TYPE;
import gov.nasa.ensemble.core.activityDictionary.ui.preference.ActivityDictionaryPreferences;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl;
import gov.nasa.ensemble.dictionary.util.DictionaryResourceImpl;
import gov.nasa.ensemble.emf.resource.IgnorableResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * ActivityDictionary object that centralizes the access to all
 * related definitions through an optional discovery method:
 * 
 * List<ActivityDef> defs = ActivityDictionary.getInstance().getDefinitions(ActivityDef.class);
 * 
 * The ActivityDictionary object fires events through the ActivityDictionaryListener 
 * interface and ActivityDictionaryEvent object. Events are fired when the AD will clear,
 * when they have been cleared, when the AD will load, and when the AD has been loaded.
 * Listeners should clear their caches and/or update views.
 */
public abstract class ActivityDictionary extends EActivityDictionaryImpl implements MissionExtendable, IAdaptable {

	protected static final Logger trace = Logger.getLogger(ActivityDictionary.class);
	private static ActivityDictionary instance = null;
	private static boolean loadingDefaultDictionary = false;

	/**
	 * Notification mechanism
	 */
	private List<ActivityDictionaryListener> listeners = new ArrayList<ActivityDictionaryListener>();
	
	public static synchronized ActivityDictionary getInstance() {
		if (instance == null) {
			try {
				instance = MissionExtender.construct(ActivityDictionary.class);
			} catch (ConstructionException e) {
				trace.error("Could not construct mission specific ActivityDictionary.class object, using default implementation", e);
				instance = new ActivityDictionaryImpl();
			}
			try {
				loadingDefaultDictionary = true;
				instance.load();
			} catch (final Exception e) {
				trace.error("Could not load ActivityDictionary", e);
				// Commenting out Arash's SPF-12028 change for now since it broke the build
				// by adding dependencies to some UI packages that are not declared in
				// the checked-in manifest.
				final Shell shell = WidgetUtils.getShell();
				WidgetUtils.runInDisplayThread(shell, new Runnable() {
					@Override
					public void run() {
						ErrorDialog.openError(
								shell, 
								"Error loading Activity Dictionary", 
								"Fatal error loading activity dictionary, application will exit.", 
								new ExceptionStatus("gov.nasa.ensemble.core.activityDictionary", "fatal exception thrown", e));
						PlatformUI.getWorkbench().close();
					}
				});
			} finally {
				loadingDefaultDictionary = false;
			}
			if (instance != null && instance.eResource() == null) {
				/*
				 * Create a resource that indicates that there is a single AD. Also register it
				 * with the package registry to create a unique URI with the
				 * nsUri that is used to look up the resource via the EPackagRegistry.INSTANCE
				 */
				URI adUri = URI.createURI("http://ensemble.nasa.gov/session.dictionary");
				Resource resource = new IgnorableDictionaryResource(adUri);
				resource.getContents().add(instance);
				EPackage.Registry.INSTANCE.put(adUri.toString(), instance);
			}
		}
		return instance;
	}
	
	protected final boolean isLoadingDefaultDictionary() {
		return loadingDefaultDictionary;
	}
	
	protected ActivityDictionary() {
		eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification n) {
				if (Notification.ADD == n.getEventType()
						|| Notification.ADD_MANY == n.getEventType()) {
					fireActivityDictionaryEvent(TYPE.DEF_ADDED);
				}
			}
		});
	}
	
	/**
	 * Base function that allows the activity dictionary to load using
	 * whatever method it would prefer. Base implementation delegates to
	 * load(URI), but overriding classes can implement to load other ways.
	 */
	protected void load() throws Exception {
		validate();
		URL url = getURL();
		if (url == null) {
			throw new IOException("ActivityDictionary URL is null");
		}
		load(url.openStream());
	}
	
	/**
	 * Load the activity dictionary from a file.
	 */
	protected abstract void load(InputStream inputStream) throws Exception;
	
	public void load(URI uri) {
		throw new IllegalStateException("no default load(URI)");
	}
	
	public void load(URL url) {
		if (url == null) {
			LogUtil.error("null url for AD");
			return;
		}
		try {
			load(URI.createURI(url.toURI().toString()));
		} catch (URISyntaxException e) {
			File file = new File(url.getPath().replace("%20", " "));
			load(URI.createURI(file.toURI().toString()));
		}
	}
	
	public URL getURL() throws IOException {
		return ActivityDictionaryPreferences.getActivityDictionaryLocation();
	}

	/**
	 * Ensure the default dictionary is loaded.  Used for unit tests. 
	 */
	public abstract void ensureDefaultDictionary();
	
	/**
	 * Restore the default dictionary.  Used for unit tests. 
	 */
	public abstract void restoreDefaultDictionary();
	
	/**
	 * Validates the AD against the schema defined in the preferences and logs any errors
	 * @return true if valid, false otherwise
	 */
	public boolean validate() {
		URL schemaUrl = null;
		URL adUrl = null;
		try {
			schemaUrl = ActivityDictionaryPreferences.getActivityDictionarySchemaLocation();
			adUrl = ActivityDictionaryPreferences.getActivityDictionaryLocation();
		} catch (MalformedURLException e) {
			trace.error("in validation", e);
		}

		if (adUrl == null) {
			// cannot do anything if the location url does not exist
			trace.warn("Activity dictionary url is not specified");
			return true;
		}
		
		if (schemaUrl == null) {
			// cannot do anything if the schema location url does not exist
			trace.warn("Activity dictionary schema url is not specified");
			return true;
		}
		
		StringWriter writer = new StringWriter();
		WriterAppender appender = new WriterAppender(new SimpleLayout(), writer);
		trace.addAppender(appender);
		
		try {
			return XMLUtilities.isValidXMLAgainstSchema(adUrl, schemaUrl, trace);
		} catch (Exception e) {
			trace.error(e.getMessage(), e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				trace.error(e.getMessage(), e);
			}
		}
		return false;
	}
	
	//
	// Convenience functions
	//

	public EParameterDef getAttributeDef(String name) {
		for (EParameterDef def : getAttributeDefs()) {
			if (def.getName().equals(name)) {
				return def;
			}
		}
		return null;
	}
	
	/** @deprecated - use getDefinitions(ERule.class) */
	@Deprecated
	public List<ERule> getRules() {
		return getDefinitions(ERule.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.activityDictionary.IDefinitionContext#addDefinition(java.lang.Object)
	 */
	public final void addDefinition(EClass definition) {
		if (definition == null) {
			throw new NullPointerException("Cannot add null definition");
		}
		getEClassifiers().add(definition);
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.activityDictionary.IDefinitionContext#addDefinitionContextListener(gov.nasa.ensemble.core.activityDictionary.DefinitionContextListener)
	 */
	public void addActivityDictionaryListener(ActivityDictionaryListener listener) {
		listeners.add(listener);
	}
	
	protected void fireActivityDictionaryEvent(TYPE type) {
		ActivityDictionaryEvent event = new ActivityDictionaryEvent(this, type);
		for(ActivityDictionaryListener listener : listeners) {
			listener.definitionContextChanged(event);
		}
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.activityDictionary.IDefinitionContext#removeDefinitionContextListener(gov.nasa.ensemble.core.activityDictionary.DefinitionContextListener)
	 */
	public void removeActivityDictionaryListener(ActivityDictionaryListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Soley to serve JUnit test cases
	 */
	public void fireActivityDictionaryLoadedEvent() {
		if (!CommonPlugin.isJunitRunning())
			throw new UnsupportedOperationException("Cannot invoke this method if not running a JUnit test");
		fireActivityDictionaryEvent(TYPE.LOADED);
	}
	
	private static class IgnorableDictionaryResource extends DictionaryResourceImpl implements IgnorableResource {

		public IgnorableDictionaryResource(URI uri) {
			super(uri);
		}

	}
	
	
}
