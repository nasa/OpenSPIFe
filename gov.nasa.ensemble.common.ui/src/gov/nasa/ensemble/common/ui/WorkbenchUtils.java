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
package gov.nasa.ensemble.common.ui;

import static fj.data.List.list;
import static fj.data.List.nil;
import static fj.data.Option.fromNull;
import static fj.data.Option.none;
import static gov.nasa.ensemble.common.functional.Lists.fj;
import static gov.nasa.ensemble.common.functional.Sets.fj;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.ui.part.MultiPageEditorSite;

import fj.F;
import fj.data.List;
import fj.data.Option;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;

@SuppressWarnings("restriction")
public class WorkbenchUtils {
	
	
	/**
	 * This function takes a workbench part site, which might be from a top-level view,
	 * or from a top-level editor, or from a page-level editor in a multipage editor 
	 * site.  The function finds the corresponding part for this site and activates it.
	 * @param site
	 */
	public static void activate(IWorkbenchPartSite site) {
		try {
			IWorkbenchPart part;
			if (site instanceof MultiPageEditorSite) {
				part = ((MultiPageEditorSite)site).getMultiPageEditor();
			} else {
				part = site.getPart();
			}
			if (part == null) {
				return;
			}
			IWorkbenchPage page = site.getPage();
			if (page == null) {
				return;
			}
			page.activate(part);
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			Logger.getLogger(WorkbenchUtils.class).error("throwable in activate", t);
		}
	}
	
	public static List<String> missingInitialViews(final IWorkbenchPage page) {
		final IPerspectiveFactory factory;
		factory = ((PerspectiveDescriptor)page.getPerspective()).createFactory();
		ViewCapturingLayout layout = new ViewCapturingLayout();
		factory.createInitialLayout(layout);
		final F<String, Boolean> missing = new F<String, Boolean>() {
			@Override
			public Boolean f(final String viewId) {
				return page.findView(viewId) == null;
			}
		};
		return fj(layout.viewIds).filter(missing).toList();
	}
	
	public static fj.data.List<IWorkbenchPage> pagesFor(final String perspectiveId) {
		return currentWindows().bind(new F<IWorkbenchWindow, fj.data.List<IWorkbenchPage>>() {
				@Override
				public List<IWorkbenchPage> f(final IWorkbenchWindow window) {
					return fj(window.getPages()).filter(new F<IWorkbenchPage, Boolean>() {
						@Override
						public Boolean f(final IWorkbenchPage page) {
							return perspectiveId.equals(page.getPerspective().getId());
						}
					});
				}
			});
	}

	public static List<IWorkbenchWindow> currentWindows() {
		if (PlatformUI.isWorkbenchRunning())
			return fj(PlatformUI.getWorkbench().getWorkbenchWindows());
		return nil();
	}
	
	/**
	 * This is intended as a workaround for MAE-4630, to be used instead of {@link IWorkbenchPage#bringToTop(IWorkbenchPart)}. When
	 * invoked upon an editor part, it ensures that the editor is subsequently considered the "active editor" according to
	 * {@link IWorkbenchPage#getActiveEditor()}. {@link ILinkHelper}s in particular should call this in their activateEditor method,
	 * to avoid flip-flopping selections as described in the bug.
	 */
	public static void bringToTop(final IWorkbenchPage page, final IWorkbenchPart part) {
		page.bringToTop(part);
		if (part instanceof IEditorPart)
			ReflectionUtils.invoke(page, "makeActiveEditor", page.getReference(part));
	}
	
	public static final Option<Shell> getShell() {
		final F<IWorkbenchWindow, Option<Shell>> windowToOptShell = new F<IWorkbenchWindow, Option<Shell>>() {
			@Override
			public Option<Shell> f(final IWorkbenchWindow window) {
				return fromNull(window.getShell());
			}
		};
		final IWorkbench workbench;
		try {
			workbench = PlatformUI.getWorkbench();
		} catch (IllegalStateException e) {
			return none();
		}
		return fromNull(workbench.getActiveWorkbenchWindow()).bind(windowToOptShell)
			   .orElse(list(workbench.getWorkbenchWindows()).toOption().bind(windowToOptShell));
	}
}
