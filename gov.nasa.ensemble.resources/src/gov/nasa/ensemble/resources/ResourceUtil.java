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

import static fj.data.Array.*;
import static fj.data.List.*;
import static fj.data.Option.*;
import static fj.pre.Equal.*;
import static org.eclipse.core.resources.IResourceDelta.*;
import static org.eclipse.core.resources.ResourcesPlugin.*;

import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.internal.core.refactoring.resource.RenameResourceProcessor;

import fj.F;
import fj.P1;
import fj.data.List;
import fj.data.Option;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.functional.Sets;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.metadata.MetaExtensionPoint;
import gov.nasa.ensemble.common.metadata.MetaUtil;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;

@SuppressWarnings("restriction")
public class ResourceUtil {
	
	private static final List<IProjectPublishFilter> publishFilters = Lists.fj(ClassRegistry.createInstances(IProjectPublishFilter.class));
	
	/**
	 * Adds a given nature to a project
	 * 
	 * @param project
	 *            to have sample nature added or removed
	 * @throws CoreException
	 */
	public static void addNature(final IProject project, final String natureId, final IProgressMonitor monitor) throws CoreException {
		final IProjectDescription description = project.getDescription();
		description.setNatureIds(list(description.getNatureIds()).removeAll(stringEqual.eq(natureId)).snoc(natureId).toArray().array(String[].class));
		project.setDescription(description, monitor);
	}

	public static void removeNature(final IProject project, final String natureId, final IProgressMonitor monitor) throws CoreException {
		final IProjectDescription description = project.getDescription();
		description.setNatureIds(list(description.getNatureIds()).removeAll(stringEqual.eq(natureId)).toArray().array(String[].class));
		project.setDescription(description, monitor);
	}

	public static final F<ICommand, String> cmdToName = new F<ICommand, String>() {
		@Override
		public String f(final ICommand cmd) {
			return cmd.getBuilderName();
		}
	};

	public static final F<String, ICommand> idToCmd(final IProjectDescription desc) {
		return new F<String, ICommand>() {
			@Override
			public ICommand f(final String id) {
				final ICommand cmd = desc.newCommand();
				cmd.setBuilderName(id);
				return cmd;
			}
		};
	}

	public static void addBuilders(final IProject project, final String... builderIds) throws CoreException {
		final IProjectDescription desc = project.getDescription();
		final List<ICommand> existing = list(desc.getBuildSpec());
		final List<String> toAdd = list(builderIds);
		final List<ICommand> result = existing.append(toAdd.removeAll(new F<String, Boolean>() {
			@Override
			public Boolean f(final String id) {
				return existing.map(cmdToName).exists(stringEqual.eq(id));
			}
		}).map(idToCmd(desc)));

		if (result.length() > existing.length()) {
			desc.setBuildSpec(result.toArray().array(ICommand[].class));
			project.setDescription(desc, null);
		}
	}

	public static void removeBuilders(final IProject project, final String... builderIds) throws CoreException {
		final IProjectDescription description = project.getDescription();
		final Set<String> builderIdSet = new HashSet<String>(Arrays.asList(builderIds));
		final List<ICommand> newCommands = list(description.getBuildSpec()).filter(new Predicate<ICommand>() {
			@Override
			public boolean apply(ICommand command) {
				return builderIdSet.contains(command.getBuilderName());
			}
		});
		description.setBuildSpec(newCommands.toArray().array(ICommand[].class));
		// setting the description causes MAE-5218
		// project.setDescription(description, null);
	}

	public static void setPersistentProperty(final IResource resource, final QualifiedName key, final String value) {
		try {
			resource.setPersistentProperty(key, value);
			resource.touch(null);
			MetaUtil.data(resource).put(stringify(key), value);
		} catch (CoreException e) {
			LogUtil.error(e);
		}
	}

	public static Option<String> getPersistentProperty(final IResource resource, final QualifiedName key) {
		try {
			return fromNull(resource.getPersistentProperty(key)).orElse(MetaExtensionPoint.<String> getDefault(resource, stringify(key)));
		} catch (CoreException e) {
			return none();
		}
	}

	public static void setPersistentBoolean(final IResource resource, final QualifiedName key, final boolean b) {
		setPersistentProperty(resource, key, Boolean.toString(b));
	}

	public static Option<Boolean> getPersistentBoolean(final IResource resource, final QualifiedName key) {
		return getPersistentProperty(resource, key).map(new F<String, Boolean>() {
			@Override
			public Boolean f(final String stringProp) {
				return Boolean.parseBoolean(stringProp);
			}
		});
	}

	public static boolean transferPersistentProperty(final IResource source, final IResource target, final QualifiedName key) {
		final Option<String> maybeValue = getPersistentProperty(source, key);
		if (maybeValue.isNone())
			return false;
		setPersistentProperty(target, key, maybeValue.some());
		return true;
	}

	public static void addPersistentPropertyListener(final IResource resource, final PropertyChangeListener listener, final QualifiedName... keys) {
		final String[] strKeys = Sets.j(array(keys).map(new F<QualifiedName, String>() {
			@Override
			public String f(final QualifiedName input) {
				return stringify(input);
			}
		})).toArray(new String[0]);
		MetaUtil.data(resource).addListener(listener, strKeys);
	}

	public static String stringify(final QualifiedName name) {
		return fromNull(name.getQualifier()).orSome("") + "." + name.getLocalName();
	}

	public static byte[] getContents(final IFile file) throws CoreException, IOException {
		final InputStream is = file.getContents(true);
		try {
			return IOUtils.toByteArray(is);
		} finally {
			is.close();
		}
	}

	public static String getStringContents(final IFile file) throws CoreException, IOException {
		return new String(getContents(file));
	}

	public static Option<IPath> getRelativePath(final IContainer from, final IResource to) {
		if (to.equals(from))
			return some((IPath) Path.EMPTY);
		if (to.getParent() == null)
			return none();
		return getRelativePath(from, to.getParent()).map(new F<IPath, IPath>() {
			@Override
			public IPath f(final IPath input) {
				return input.append(to.getName());
			}
		});
	}

	public static IPath getRelativePathOrError(final IContainer from, final IResource to) {
		final Option<IPath> relativePath = getRelativePath(from, to);
		if (relativePath.isNone())
			throw new Error(from + " does not contain " + to);
		return relativePath.some();
	}

	public static void mkdirs(IResource resource) throws CoreException {
		mkdirs(resource, true, null);
	}

	public static void mkdirs(IResource resource, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
		if (resource.exists())
			return;
		mkdirs(resource.getParent(), keepHistory, monitor);
		if (resource instanceof IFolder && !resource.exists())
			((IFolder) resource).create(true, true, monitor);
	}

	public static byte[] zipContainer(final IContainer container, final IProgressMonitor monitor) throws CoreException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		zipContainer(container, baos, monitor);
		return baos.toByteArray();
	}

	public static void zipContainer(final IContainer container, final OutputStream os, final IProgressMonitor monitor) throws CoreException {
		final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(os));
		container.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		// monitor.beginTask("Zipping " + container.getName(), container.get);
		container.accept(new IResourceVisitor() {
			@Override
			public boolean visit(IResource resource) throws CoreException {
				// MSLICE-1258
				for (IProjectPublishFilter filter : publishFilters)
					if (!filter.shouldInclude(resource))
						return true;
						
				if (resource instanceof IFile) {
					final IFile file = (IFile) resource;
					final IPath relativePath = ResourceUtil.getRelativePath(container, file).some();
					final ZipEntry entry = new ZipEntry(relativePath.toString());
					try {
						out.putNextEntry(entry);
						out.write(ResourceUtil.getContents(file));
						out.closeEntry();
					} catch (IOException e) {
						throw new CoreException(new Status(IStatus.ERROR, EnsembleResourcesPlugin.PLUGIN_ID, "Failed to write contents of " + file.getName(), e));
					}
				} else if (resource instanceof IFolder) {
					final IFolder folder = (IFolder) resource;
					if (folder.members().length > 0)
						return true;
					final IPath relativePath = ResourceUtil.getRelativePath(container, folder).some();
					final ZipEntry entry = new ZipEntry(relativePath.toString() + "/");
					try {
						out.putNextEntry(entry);
						out.closeEntry();
					} catch (IOException e) {
						LogUtil.error(e);
						throw new CoreException(new Status(IStatus.ERROR, EnsembleResourcesPlugin.PLUGIN_ID, "Failed to compress directory " + folder.getName(), e));
					}
				}
				return true;
			}
		});
		try {
			out.close();
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, EnsembleResourcesPlugin.PLUGIN_ID, "Failed to close output stream", e));
		}
	}

	public static IProject unzipProject(String projectName, ZipInputStream zis, IProgressMonitor monitor) throws CoreException, IOException {
		final String dirPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();
		if (monitor == null)
			monitor = new NullProgressMonitor();
		try {
			monitor.beginTask("Loading " + projectName, 300);
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {	
				File to = (!new File(dirPath, entry.getName()).getAbsolutePath().contains(projectName)) ? new File(dirPath + File.separator + projectName, entry.getName()) : new File(dirPath, entry.getName()); 
				if (entry.isDirectory()) {
					if (!to.exists() && !to.mkdirs()) {
						throw new IOException("Error creating directory: " + to);
					}
				} else {
					File parent = to.getParentFile();
					if (parent != null && !parent.exists() && !parent.mkdirs()) {
						throw new IOException("Error creating directory: " + parent);
					}
					FileOutputStream fos = new FileOutputStream(to);
					try {
						monitor.subTask("Expanding: " + to.getName());
						IOUtils.copy(zis, fos);
					} finally {
						IOUtils.closeQuietly(fos);
					}
				}
				monitor.worked(10);
			}
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			project.create(null);
			project.open(null);
			return project;
		} catch (IOException e) {
			throw new CoreException(new ExceptionStatus(EnsembleResourcesPlugin.PLUGIN_ID, "adding project to workspace", e));
		} finally {
			zis.close();
			monitor.done();
		}
	}

	public static boolean hasBuilder(final IProject project, final String builderId) throws CoreException {
		return list(project.getDescription().getBuildSpec()).map(cmdToName).exists(stringEqual.eq(builderId));
	}

	public static void printDeltas(final IResourceDelta delta) {
		try {
			delta.accept(new IResourceDeltaVisitor() {
				@Override
				public boolean visit(IResourceDelta delta) {
					final IResource rsrc = delta.getResource();
					switch (delta.getKind()) {
					case ADDED:
						System.err.println("added " + rsrc);
						return true;
					case REMOVED:
						System.err.println("removed " + rsrc);
						return false;
					case CHANGED:
						if (rsrc instanceof IFile)
							System.err.println("changed " + rsrc);
						return true;
					}
					return true;
				}
			});
		} catch (CoreException e) {
			LogUtil.error(e);
		}
	}

	public static void recursiveDeleteEmpty(IContainer container, final F<IContainer, Boolean> stop, IProgressMonitor monitor) {
		try {
			while (!stop.f(container) && container.exists() && container.members().length <= 0) {
				if (container.exists()) {
					try {
						container.delete(true, monitor);
					} catch (CoreException e) {
						LogUtil.error(e);
					}
				}
				container = container.getParent();
			}
		} catch (CoreException e) {
			LogUtil.error(e);
		}
	}

	/**
	 * Run a workspace runnable and collect any deltas that occur during it using the passed visitor
	 * 
	 * @throws CoreException
	 */
	public static void runAndListen(final IWorkspaceRunnable runnable, final IResourceDeltaVisitor visitor, final IProgressMonitor monitor) throws CoreException {
		final IResourceChangeListener listener = new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				if (event.getDelta() != null) {
					try {
						event.getDelta().accept(visitor);
					} catch (CoreException e) {
						throw new Error(e);
					}
				}
			}
		};

		final IWorkspace workspace = getWorkspace();
		try {
			workspace.run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					workspace.addResourceChangeListener(listener);
					runnable.run(monitor);
				}
			}, monitor);
		} finally {
			workspace.removeResourceChangeListener(listener);
		}
	}

	/**
	 * This function sounds dirty but means well.
	 */
	public static void touchDescendents(final IContainer folder) throws CoreException {
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				for (final IResource resource : folder.members()) {
					if (resource instanceof IFolder)
						touchDescendents((IFolder) resource);
					else
						resource.touch(null);
				}
			}
		}, null);
	}

	public static boolean contains(final IContainer container, final IResource resource) {
		return getRelativePath(container, resource).isSome();
	}

	public static final ProjectProperties projProps(final IResource resource) {
		return ProjectProperties.projProps(resource);
	}

	/**
	 * Renames a given resource
	 */
	public static void renameResource(IResource resource, String newName, IProgressMonitor monitor) {
		IProgressMonitor progressMonitor = (monitor == null) ? new NullProgressMonitor() : monitor;
		try {
			// Renaming Project and plan
			RenameResourceProcessor processor = 
					(RenameResourceProcessor) new RenameResourceProcessor(resource).getAdapter(RenameResourceProcessor.class);
			processor.setNewResourceName(newName);
			Change createChange = processor.createChange(progressMonitor);
			createChange.perform(new NullProgressMonitor());
		} catch (CoreException e) {
			LogUtil.error("Failed to rename resource: "+resource.getRawLocation(), e);
			return;
		}
	}
	
	/**
	 * Gets the corresponding workspace IFile if the java.io.File is located in the workspace.
	 * 
	 * @param javaFile The java.io.File representation of the system file.
	 * @return The corresponding workspace IFile object. Otherwise <code>null</code>, if java.io.File exists outside of workspace.
	 */
	public static IFile getFile(File javaFile) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(javaFile.getAbsolutePath());
		IFile file = workspace.getRoot().getFileForLocation(location);
		return file;
	}
	
	public static File getFile(IResource iResource) {
		if (iResource != null) {
			IPath path = iResource.getLocation();
			if (path != null) {
				return path.toFile();
			}                                  
		}
		return null;
	}
	
	/**
	 * Like {@link P1#memo()} but using the workspace lock as a locking mechanism... this should avoid deadlocks due to lock inversion when the tuple can be access either inside or outside of a
	 * workspace runnable. (e.g. MAE-5506)
	 */
	public static final <A> P1<A> resourceMemo(final P1<A> p) {
		return new P1<A>() {
			private volatile Option<A> v = none();

			@Override
			public A _1() {
				if (v.isNone()) {
					try {
						final IWorkspace ws = ResourcesPlugin.getWorkspace();
						final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
							@Override
							public void run(IProgressMonitor monitor) {
								if (v.isNone())
									v = some(p._1());
							}
						};
						if (ws.isTreeLocked()) { // MAE-5685
							runnable.run(null);
						} else {
							ws.run(runnable, null);
						}
					} catch (CoreException e) {
						throw new Error(e);
					}
				}
				return v.some();
			}
		};
	}
}
