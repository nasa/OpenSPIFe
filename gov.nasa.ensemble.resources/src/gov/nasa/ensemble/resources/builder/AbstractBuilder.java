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
package gov.nasa.ensemble.resources.builder;

import static fj.data.Option.*;

import java.util.Collections;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

public abstract class AbstractBuilder extends IncrementalProjectBuilder {
	
	/** Build flag used to indicate that the next build request should be ignored */
	public static final String IGNORE_BUILD = "gov.nasa.ensemble.sequence.project.build.option.ignore";
	
	private int kind;
	private Map<String, String> args;
	
	protected class Visitor implements IResourceVisitor, IResourceDeltaVisitor {

		private IProgressMonitor monitor;

		public Visitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public boolean visit(IResource resource) throws CoreException {
			handleFullBuild(resource, monitor);
			if (resource instanceof IContainer)
				return shouldVisitChildren((IContainer)resource);
			return false;
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			final IResource resource = delta.getResource();
			final int kind = delta.getKind();
			switch (kind) {
			case IResourceDelta.REMOVED:
				// Commented out because it causes MAE-4508 and MAE-4509
//				if (0 == (delta.getFlags() & IResourceDelta.MOVED_TO))
					handleRemoved(resource, monitor);
				// else it was a move, and will be handled in the next case
				break;
			case IResourceDelta.ADDED:
				// Commented out because it causes MAE-4508 and MAE-4509
//				if (0 != (delta.getFlags() & IResourceDelta.MOVED_FROM))
//					handleMoved(resource, delta.getMovedFromPath(), monitor);
//				else
					handleAdded(resource, monitor);
				break;
			case IResourceDelta.CHANGED:
				handleChanged(resource, monitor);
				break;
			}
			
			if (resource instanceof IContainer)
				return shouldVisitChildren((IContainer)resource);
			
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected final IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (!BuilderControl.getInstance().isBuildingOk()) // MAE-5716
			throw new OperationCanceledException();
		
		args = fromNull(args).orSome(Collections.<String, String>emptyMap());
		if (Boolean.parseBoolean(fromNull((String)args.get(IGNORE_BUILD)).orSome("false")))
			return null;
		
		this.kind = kind;
		this.args = args;
		if (preBuild(monitor)) {
			try {
				if (kind == FULL_BUILD) {
					fullBuild(monitor);
				} else {
					IResourceDelta delta = getDelta(getProject());
					if (delta == null) {
						fullBuild(monitor);
					} else {
						incrementalBuild(delta, monitor);
					}
				}
			} finally {
				postBuild(monitor);
			}
		}
		return null;
	}

	/**
	 * Called before each build starts. Subclasses may override.
	 * @return whether or not to proceed with the build
	 * @throws CoreException 
	 */
	protected boolean preBuild(IProgressMonitor monitor) throws CoreException {
		return true;
	}
	
	/**
	 * Called after each build finishes. Subclasses may override.
	 * @throws CoreException 
	 */
	protected void postBuild(IProgressMonitor monitor) throws CoreException {
		// do nothing by default
	}

	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		getProject().accept(new Visitor(monitor));
	}
	
	protected void incrementalBuild(final IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		delta.accept(new Visitor(monitor));
	}

	public void handleFullBuild(IResource resource, IProgressMonitor monitor) throws CoreException {
		handleChanged(resource, monitor);
	}

	protected void handleMoved(IResource resource, IPath fromPath, IProgressMonitor monitor) throws CoreException {
		handleRemoved(resource, monitor);
		handleAdded(resource, monitor);
	}
	
	protected boolean shouldVisitChildren(IContainer container) {
		return true;
	}

	protected int getBuildKind() {
		return kind;
	}
	
	protected Map getArgs() {
		return args;
	}
	
	protected abstract void handleAdded(IResource resource, IProgressMonitor monitor) throws CoreException;
	
	protected abstract void handleChanged(IResource resource, IProgressMonitor monitor) throws CoreException;

	protected abstract void handleRemoved(IResource resource, IProgressMonitor monitor) throws CoreException;
}
