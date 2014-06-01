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
import static org.eclipse.ui.IWorkbenchPage.MATCH_ID;
import static org.eclipse.ui.IWorkbenchPage.MATCH_INPUT;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import fj.Effect;
import fj.F;
import fj.data.List;
import fj.data.Option;

public class WorkbenchFunctions {
	public static final List<IWorkbenchWindow> allWindows() {
		if (!PlatformUI.isWorkbenchRunning())
			return nil();
		return list(PlatformUI.getWorkbench().getWorkbenchWindows());
	}
	
	public static final F<Shell, Display> shellDisplay = new F<Shell, Display>() {
		@Override
		public Display f(final Shell shell) {
			return shell.getDisplay();
		}
	};
	
	public static final F<IWorkbenchWindow, Shell> windowShell = new F<IWorkbenchWindow, Shell>() {
		@Override
		public Shell f(final IWorkbenchWindow window) {
			return window.getShell();
		}
	};
	
	public static final F<IWorkbenchWindow, List<IWorkbenchPage>> windowPages = new F<IWorkbenchWindow, List<IWorkbenchPage>>() {
		@Override
		public List<IWorkbenchPage> f(final IWorkbenchWindow window) {
			return list(window.getPages());
		}
	};
	
	public static final F<IWorkbenchWindow, Option<IWorkbenchPage>> windowActivePage = new F<IWorkbenchWindow, Option<IWorkbenchPage>>() {
		@Override
		public Option<IWorkbenchPage> f(final IWorkbenchWindow window) {
			return fromNull(window.getActivePage());
		}
	};
	
	public static final F<IWorkbenchPage, List<IViewReference>> pageViewRefs = new F<IWorkbenchPage, List<IViewReference>>() {
		@Override
		public List<IViewReference> f(final IWorkbenchPage page) {
			return list(page.getViewReferences());
		}
	};
	
	public static final F<IWorkbenchPage, List<IEditorReference>> pageEditorRefs = new F<IWorkbenchPage, List<IEditorReference>>() {
		@Override
		public List<IEditorReference> f(final IWorkbenchPage page) {
			return list(page.getEditorReferences());
		}
	};
	
	public static final F<IViewReference, IViewPart> refToView = new F<IViewReference, IViewPart>() {
		@Override
		public IViewPart f(final IViewReference ref) {
			return ref.getView(true);
		}
	};

	public static final F<IEditorReference, IEditorPart> refToEditor = new F<IEditorReference, IEditorPart>() {
		@Override
		public IEditorPart f(final IEditorReference ref) {
			return ref.getEditor(true);
		}
	};
	
	public static final F<IEditorPart, Boolean> isDirty = new F<IEditorPart, Boolean>() {
		@Override
		public Boolean f(final IEditorPart editor) {
			return editor.isDirty();
		}
	};
	
	public static final F<IWorkbenchPage, Option<IViewPart>> findView(final String id) {
		return new F<IWorkbenchPage, Option<IViewPart>>() {
			@Override
			public Option<IViewPart> f(final IWorkbenchPage page) {
				return fromNull(page.findView(id));
			}
		};
	}
	
	public static final F<IWorkbenchPage, List<IEditorReference>> 
	findEditors(final IEditorInput input, final String id, final int matchFlags) {
		return new F<IWorkbenchPage, List<IEditorReference>>() {
			@Override
			public List<IEditorReference> f(final IWorkbenchPage page) {
				return list(page.findEditors(input, id, matchFlags));
			}
		};
	}
	
	public static final F<IWorkbenchPage, List<IEditorReference>> findEditors(final String id) {
		return findEditors(null, id, MATCH_ID);
	}
	
	public static final F<IWorkbenchPage, List<IEditorReference>> findEditors(final IEditorInput input) {
		return findEditors(input, null, MATCH_INPUT);
	}
	
	public static F<IWorkbenchPage, Option<IEditorPart>> findEditor(final IEditorInput input) {
		return new F<IWorkbenchPage, Option<IEditorPart>>() {
			@Override
			public Option<IEditorPart> f(final IWorkbenchPage page) {
				return fromNull(page.findEditor(input));
			}
		};
	}
	
	public static Option<Display> firstDisplay() {
		return allWindows().toOption().map(windowShell).map(shellDisplay);
	}
	
	public static Effect<Display> asyncExec(final Runnable runnable) {
		return new Effect<Display>() {
			@Override
			public void e(Display display) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						runnable.run();
					}
				});
			}
		};
	}
}
