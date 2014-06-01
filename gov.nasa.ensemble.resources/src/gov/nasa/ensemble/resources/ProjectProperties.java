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
package gov.nasa.ensemble.resources;

import static fj.data.Either.*;
import static fj.data.Option.*;
import static fj.pre.Equal.*;
import static gov.nasa.ensemble.resources.ResourceUtil.*;
import static java.util.Collections.*;
import static org.eclipse.core.resources.IResourceDelta.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;

import fj.F;
import fj.P1;
import fj.data.Either;
import fj.data.Option;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.logging.LogUtil;

public class ProjectProperties {
	
	public static enum WriteOptions {
		DONT_WRITE,
		WRITE_SYNC,
		WRITE_ASYNC
	}
	
	private static final String ROOT_PROPS_FOLDER_NAME = ".projectProperties";

	private static final String PROP_FILE_PREFIX = "___";
	
	private static final String QUALIFIER = "gov.nasa.ensemble.resources.projprops";
	
	private static final Option<String> noValue = none();
	
	private static final Map<IResource, ProjectProperties> globalMap = 
		synchronizedMap(new HashMap<IResource, ProjectProperties>());
	private static final P1<fj.data.List<IProjectPropertyListener>> globalListeners =
		ClassRegistry.lazyInstances(IProjectPropertyListener.class);
	private static final P1<fj.data.List<IProjectPropertyDefaults>> defaults =
		ClassRegistry.lazyInstances(IProjectPropertyDefaults.class);
	
	public static synchronized final ProjectProperties projProps(final IResource resource) {
		synchronized (globalMap) {
			ProjectProperties props = globalMap.get(resource);
			if (props == null) {
				props = new ProjectProperties(resource);
				globalMap.put(resource, props);
			}
			return props;
		}
	}

	private final IResource resource;
	
	private final List<IProjectPropertyListener> listeners = synchronizedList(new ArrayList<IProjectPropertyListener>());
	
	private final P1<Map<String, String>> props = resourceMemo(new P1<Map<String, String>>() {
		@Override
		public Map<String,String> _1() {
			final Map<String, String> map = new HashMap<String, String>();
			final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						readAllProps(resource, map);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			};
			try {
				if (ws().isTreeLocked())
					runnable.run(null);
				else
					ws().run(runnable, null);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
			return synchronizedMap(map);
		}
	});
	
	private static final P1<BlockingQueue<Either<ProjectPropertyEvent, IFolder>>> 
	writeQueue = new P1<BlockingQueue<Either<ProjectPropertyEvent, IFolder>>>() {
		@Override
		public BlockingQueue<Either<ProjectPropertyEvent, IFolder>> _1() {
			final LinkedBlockingQueue<Either<ProjectPropertyEvent, IFolder>> queue = 
				new LinkedBlockingQueue();
			new Thread("Project properties writer") {
				@Override
				public void run() {
					while (true) {
						try {
							final Either<ProjectPropertyEvent, IFolder> request = 
								queue.take();
							if (request.isLeft()) {
								writeProp(request.left().value());
							} else {
								request.right().value().delete(true, null);
							}
						} catch (Throwable t) {
							throw new RuntimeException(t);
						}
					}
				}
			}.start();
			return queue;
		}
	}.memo();
	
	private final IResourceChangeListener resourceListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(final IResourceChangeEvent event) {
			final IResourceDelta delta = event.getDelta();
			if (delta != null) {
				try {
					delta.accept(new IResourceDeltaVisitor() {
						@Override
						public boolean visit(IResourceDelta delta) throws CoreException {
							final IResource deltaRsrc = delta.getResource();
							final int kind = delta.getKind();
							final IFolder propsFolder = propsFolder(resource);
							if (REMOVED == kind && resource.equals(deltaRsrc)) {
								props._1().clear();
								final Either<ProjectPropertyEvent, IFolder> right = 
									right(propsFolder);
								writeQueue._1().add(right);
							} else if (propsFolder.equals(deltaRsrc.getParent())) {
								final String key = getPropName(deltaRsrc);
								switch (kind) {
								case CHANGED:
									if ((delta.getFlags() & CONTENT) == 0)
										return false;
									//$FALL-THROUGH$
								case ADDED:
									set(key, readProp(resource, key), WriteOptions.DONT_WRITE);
									break;
								case REMOVED:
									set(key, noValue, WriteOptions.DONT_WRITE);
									break;
								}
							} else if (ADDED == kind) {
								importFromPersistProps(deltaRsrc);
							}
							return true;
						}
					});
					
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}
		}
	};
	
	private ProjectProperties(final IResource resource) {
		this.resource = resource;
		ws().addResourceChangeListener(resourceListener);
	}
	
	public Option<String> get(final String key) {
		return fromNull(props._1().get(key)).orElse(getDefault(key));
	}

	public Option<Set<Entry<String, String>>> entrySet() {
		return fromNull(props._1().entrySet());
	}

	private P1<Option<String>> getDefault(final String key) {
		return new P1<Option<String>>() {
			@Override
			public Option<String> _1() {
				// TODO memoize this
				return Option.join(defaults._1().map(new F<IProjectPropertyDefaults, Option<String>>() {
					@Override
					public Option<String> f(final IProjectPropertyDefaults defaults) {
						return defaults.getDefault(resource, key);
					}
				}).find(Option.<String>isSome_()));
			}
		};
	}

	public void set(final String key, final String value) {
		set(key, some(value));
	}

	public void unset(final String key) {
		set(key, noValue);
	}
	
	public void set(final String key, final String value, final WriteOptions writeOpts) throws CoreException {
		set(key, some(value), writeOpts);
	}
	
	public void set(final String key, final Option<String> optValue) {
		try {
			set(key, optValue, WriteOptions.WRITE_ASYNC);
		} catch (CoreException e) {
			throw new Error(e); // shouldn't be possible since we're using async
		}
	}
			
	private void set(final String key, Option<String> newValue, final WriteOptions writeOpts) throws CoreException {
		final Option<String> oldValue = get(key);
		newValue = newValue.orElse(getDefault(key));
		if (optionEqual(stringEqual).eq(newValue, oldValue))
			return;
		
		if (newValue.isSome())
			props._1().put(key, newValue.some());
		else
			props._1().remove(key);
		
		final ProjectPropertyEvent event = new ProjectPropertyEvent(resource, key, newValue);
		
		switch (writeOpts) {
		case DONT_WRITE:
			// do nothing
			break;
		case WRITE_SYNC:
			writeProp(event);
			break;
		case WRITE_ASYNC:
			final Either<ProjectPropertyEvent, IFolder> left = left(event);
			writeQueue._1().add(left);
			break;
		}
	}

	public ProjectPropertyListeners listeners(String... keys) {
		return new ProjectPropertyListeners(this, keys);
	}

	public void addListener(final IProjectPropertyListener listener) {
		listeners.add(listener);
	}

	public void removeListener(final IProjectPropertyListener listener) {
		listeners.remove(listener);
	}
	
	private void readAllProps(final IResource resource, final Map<String, String> map) throws CoreException, IOException {
		final IFolder propsFolder = propsFolder(resource);
		if (propsFolder.exists()) {
			for (IResource rsrc : propsFolder.members()) {
				if (!(rsrc instanceof IFile) || !rsrc.getName().startsWith(PROP_FILE_PREFIX)) 
					continue;
				final IFile file = (IFile)rsrc;
				final String key = getPropName(rsrc);
				map.put(key, getStringContents(file));
			}
		}
	}
	
	private static Option<String> readProp(final IResource resource, final String key) throws CoreException {
		final IFile propFile = propFile(resource, key);
		if (propFile.isAccessible()) {
			try {
				return some(getStringContents(propFile));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else
			return noValue;
	}
	
	private static void writeProp(final IResource resource, final String key, final Option<String> optValue) throws CoreException {
		final IFile propFile = propFile(resource, key);
		final QualifiedName qnKey = new QualifiedName(QUALIFIER, key);
		if (optValue.isSome()) {
			final String value = optValue.some();
			final InputStream in = IOUtils.toInputStream(value);
			if (!propFile.exists()) {
				mkdirs(propFile);
				propFile.create(in, true, null);
			} else
				propFile.setContents(in, true, true, null);
			resource.setPersistentProperty(qnKey, value);
		} else if (propFile.exists()) {
			propFile.delete(true, null);
			resource.setPersistentProperty(qnKey, null);
		}
		resource.touch(null);
	}
	
	static IFile propFile(final IResource resource, final String key) {
		return propsFolder(resource).getFile(PROP_FILE_PREFIX + key);
	}
	
	static IFolder propsFolder(final IResource resource) {
		final IProject project = resource.getProject();
		final IFolder rootPropsFolder = project.getFolder(ROOT_PROPS_FOLDER_NAME);
		final IPath path = getRelativePath(project, resource).some();
		return rootPropsFolder.getFolder(path);
	}
	
	private void notifyListeners(final ProjectPropertyEvent event) {
		final fj.data.List<IProjectPropertyListener> copiedListeners;
		synchronized (listeners) {
			copiedListeners = Lists.fj(listeners).append(globalListeners._1());
		}
		
		for (final IProjectPropertyListener listener : copiedListeners) {
			try {
				listener.propertyChanged(event);
			} catch (Throwable t) {
				LogUtil.error(t);
			}
		}
	}
	
	private static IWorkspace ws() {
		return ResourcesPlugin.getWorkspace();
	}

	public static Option<String> getPropNameFromPropResource(final IResource rsrc) {
		if (rsrc.getName().startsWith(PROP_FILE_PREFIX))
			return some(getPropName(rsrc));
		return none();
	}
	
	private static String getPropName(final IResource rsrc) {
		return rsrc.getName().replaceFirst(PROP_FILE_PREFIX, "");
	}

	private static void importFromPersistProps(IResource resource) throws CoreException {
		if (resource.isAccessible()) {
			final Map<QualifiedName, String> persistProps = resource.getPersistentProperties();
			for (Entry<QualifiedName, String> entry : persistProps.entrySet()) {
				final QualifiedName qnKey = entry.getKey();
				if (QUALIFIER.equals(qnKey.getQualifier()))
					projProps(resource).set(qnKey.getLocalName(), entry.getValue());
			}
		}
	}
	
	private static void writeProp(final ProjectPropertyEvent event)
			throws CoreException {
		if (event.resource.exists()) {
			ws().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						if (event.resource.exists())
							writeProp(event.resource, event.key, event.value);
					} catch (Throwable t) {
						LogUtil.error(t);
					}
				}
			}, event.resource.getProject(), IWorkspace.AVOID_UPDATE, null);
		}
		
		projProps(event.resource).notifyListeners(event);
	}
	
	public static Option<IResource> getResourceForPropertyFile(final IFile propFile) {
		return fromNull(propFile.getProject()).bind(new F<IProject, Option<IResource>>() {
			@Override
			public Option<IResource> f(final IProject project) {
				return ResourceUtil.getRelativePath(project.getFolder(ROOT_PROPS_FOLDER_NAME), propFile).bind(new F<IPath, Option<IResource>>() {
					@Override
					public Option<IResource> f(final IPath path) {
						return fromNull(project.findMember(path.removeLastSegments(1)));
					}
				});
			}
		});
	}
}
