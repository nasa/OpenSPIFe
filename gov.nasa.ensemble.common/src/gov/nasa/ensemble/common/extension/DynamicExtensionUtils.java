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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.internal.registry.osgi.ExtensionEventDispatcherJob;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public final class DynamicExtensionUtils {
	
	/**
	 * Method to remove extensions contributed from inherited plugins
	 * 
	 * @param extensionPointId
	 * @param ids
	 */
	private static void removeExtensions(String extensionPointId, String attributeName, List<String> ids) {	
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(extensionPointId);
		removeExtensions(extensionRegistry, extensionPoint, attributeName, ids);
	}
	
	public static void removeIgnoredExtensions() {
		removeExtensionsByClass();
		removeExtensionsById();
	}

	private static void removeExtensionsByClass() {
		Map<String, List<String>> map = EnsembleProperties.getMapPropertyValue("extension.ignore.class");
		Set<String> keySet = map.keySet();
		for (String extensionPointId : keySet) {
			List<String> extensions = map.get(extensionPointId);
			removeExtensions(extensionPointId, "class", extensions);
		}
	}

	private static void removeExtensionsById() {
		Map<String, List<String>> map = EnsembleProperties.getMapPropertyValue("extension.ignore.id");
		Set<String> keySet = map.keySet();
		for (String extensionPointId : keySet) {
			List<String> extensions = map.get(extensionPointId);
			removeExtensions(extensionPointId, "id", extensions);
		}
	}	
	
	/**
	 * Method for dynamically registering an EMF constraint provider.  The packageUri will be used to resolve the %namespaceUri key in the plugin extension
	 * XML file found at contributionPath in the contributionBundle
	 * 
	 * @param packageUri the URI of an EMF package whose classes are to be validated using the constraints of the provider
	 * @param contributorBundle the OSGi bundle for the plugin containing the EMF package
	 * @param contributionPath the path of an XML file containing the necessary extensions for the contribution
	 * @param contributionBundle the OSGI bundle for the plugin relative to which the contributionPath is resolved
	 */
	public static void addDynamicConstraintContribution(String packageUri, Bundle contributorBundle, String contributionPath, Bundle contributionBundle) {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		Object key = ((ExtensionRegistry) registry).getTemporaryUserToken( );
		IContributor contributor = ContributorFactoryOSGi.createContributor(contributorBundle);
		InputStream inputStream = null;
		final Hashtable<String, String> keyMap = new Hashtable<String, String>();
		keyMap.put("namespaceUri", packageUri);
		ResourceBundle resourceBundle = new ResourceBundle() {

			@Override
			public Enumeration<String> getKeys() {
				return keyMap.keys();
			}

			@Override
			protected Object handleGetObject(String key) {
				return keyMap.get(key);
			}
			
		};
		try {
			inputStream = FileLocator.openStream(contributionBundle, new Path(contributionPath), false);
			registry.addContribution(inputStream, contributor, false, null, resourceBundle, key);
		} catch (IOException e) {
			LogUtil.error(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LogUtil.error(e);
				}
			}
		}
	}
	
	/**
	 * Wait for jobs relating to dynamic extension to be finished
	 */
	public static void waitForJobs() {
		IJobManager manager = Job.getJobManager();
		Job[] jobs = manager.find(null);
		while (true) {
			boolean joined = false;
			for (Job job : jobs) {
				if (job instanceof ExtensionEventDispatcherJob) {
					if (job.getResult() == null) {
						try {
							joined = true;
							job.join();
						} catch (InterruptedException e) {
							LogUtil.error(e);
						}
					}
				}
			}
			if (!joined) {
				break;
			}
		}
	}
	
	/**
	 * Synchronously removes an extension from the registry.
	 * 
	 * @param extensionRegistry
	 * @param extensionPoint
	 * @param attributeName
	 * @param ids
	 */
	private static void removeExtensions(IExtensionRegistry extensionRegistry, IExtensionPoint extensionPoint, String attributeName, List<String> ids) {
		BlockingExtensionEventDispatcherJob blockingJob = new BlockingExtensionEventDispatcherJob();
		blockingJob.schedule(); // schedule the blocking job to block ExtensionEventDispatcherJobs.
		boolean done = false;
		// the configuration elements change after every removal of an extension, so we have to "re-get" the configuration elements.
		while (!done) {
			done = true;
			for (final IConfigurationElement elem : extensionPoint.getConfigurationElements()) {
				final String id = elem.getAttribute(attributeName);
				if (ids.contains(id)) {
					IExtension declaringExtension = elem.getDeclaringExtension();
					try {
						Object masterToken = ReflectionUtils.get(extensionRegistry, "masterToken");
						extensionRegistry.removeExtension(declaringExtension, masterToken);
						done = false;
						break;
					} catch (ThreadDeath t) {
						throw t;
					} catch (Throwable t) {
						LogUtil.warn(t.getMessage());
					}
				}
			}		
		}
		IJobManager jobManager = Job.getJobManager();
		Job[] jobs = jobManager.find(null);
		for (Job job : jobs) {
			if (job instanceof ExtensionEventDispatcherJob) {
				ExtensionEventDispatcherJob extensionEventDispatcherJob = (ExtensionEventDispatcherJob) job;
				// The ExtensionEventDispatcherJob has listener(s) that
				// try to display.syncExec().  Since we remove extensions
				// at startup, syncExec will not be processed.  So, the job
				// has to wait until much later when it may be scheduled with
				// other things.
				// Instead, we run the job ourselves, since we are in the 
				// display thread and the job will run to completion here.
				extensionEventDispatcherJob.run(null);
				// Then we cancel the job so the JobManager will discard it.
				extensionEventDispatcherJob.cancel();
			}
		}
		// Finally, we allow the blocking job to complete, to free up its worker.
		blockingJob.go();
	}

	/**
	 * A Job that simply occupies the ExtensionEventDispatcherJob's Rule,
	 * to block it from executing.
	 * 
	 * @author abachman
	 */
	private static class BlockingExtensionEventDispatcherJob extends Job {

		private volatile boolean go = false;
		
		public BlockingExtensionEventDispatcherJob() {
			super("Blocking extension event dispatcher job");
			ISchedulingRule rule = (ISchedulingRule) ReflectionUtils.get(ExtensionEventDispatcherJob.class, "EXTENSION_EVENT_RULE");
			setRule(rule);
		}
		
		public synchronized void go() {
			go = true;
			notify();
		}
		
		@Override
		protected synchronized IStatus run(IProgressMonitor monitor) {
			while (!go) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// fall out
				}
			}
			return Status.OK_STATUS;
		}

	}


}
