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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.GenericRunnable;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.metadata.MetaData;
import gov.nasa.ensemble.common.metadata.MetaUtil;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class WidgetUtils {
	
	/**
	 * If this is called from within the UI thread, it will be run immediately otherwise it will be
	 * {@link Display#asyncExec(int, Runnable)}ed on the {@link Display} of the passed {@link Widget}.
	 * 
	 * @param widget
	 *            the widget associated with this task. It will be checked for disposal prior to the runnable being executed.
	 * @param runnable
	 *            the task to carry out.
	 */
	public static void runInDisplayThread(final Widget widget, final Runnable runnable) {
		runInDisplayThread(widget, runnable, false);
	}

	/**
	 * If this is called from within the UI thread, it will be run immediately otherwise it will be
	 * {@link Display#asyncExec(int, Runnable)}ed on the {@link Display} of the passed {@link Widget}.
	 * 
	 * @param widget
	 *            the widget associated with this task. It will be checked for disposal prior to the runnable being executed.
	 * @param runnable
	 *            the task to carry out.
	 * @param blockUntilFinished
	 *            whether the invoking thread should wait until the runnable completes in the UI thread
	 * 
	 * @return whether the runnable was run
	 */
	public static boolean runInDisplayThread(final Widget widget, final Runnable runnable, boolean blockUntilFinished) {
		if (widget == null || widget.isDisposed())
			return false;
		final Display controlDisplay = widget.getDisplay();

		if (controlDisplay.isDisposed())
			return false;

		if (Display.findDisplay(Thread.currentThread()) == controlDisplay) {
			try {
				runnable.run();
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				Logger.getLogger(WidgetUtils.class).error("display thread throwable", t);
			}
		} else {
			final boolean[] result = new boolean[] { true };

			Runnable safeRunnable = new Runnable() {
				@Override
				public void run() {
					// check the widget for validity before running the posted runnable
					if (!widget.isDisposed()) {
						runnable.run();
					} else
						result[0] = false;
				}
			};
			if (blockUntilFinished)
				controlDisplay.syncExec(safeRunnable);
			else
				controlDisplay.asyncExec(safeRunnable);

			return result[0];
		}

		return true;
	}

	/**
	 * Queues a runnable for later {@link Display#asyncExec(Runnable)} on the {@link Display} of the passed {@link Widget}.
	 * Essentially this ensures that the runnable will be asyncExec'ed and put on the runnable queue no matter what. It will NOT run
	 * immediately if the invoking thread is already the UI thread--for that behavior use
	 * {@link #runInDisplayThread(Widget, Runnable)}.
	 * 
	 * @param widget
	 *            the widget associated with this task. It will be checked for disposal prior to the runnable being executed.
	 * @param runnable
	 *            the task to carry out.
	 */
	public static void runLaterInDisplayThread(final Widget widget, final Runnable runnable) {
		if (widget == null || widget.isDisposed())
			return;

		Display controlDisplay = null;
		try {
			controlDisplay = widget.getDisplay();
		} catch (SWTException e) {
			return;
		}

		if (controlDisplay.isDisposed())
			return;

		controlDisplay.asyncExec(new Runnable() {
			@Override
			public void run() {
				// check the widget for validity before running the posted runnable
				if (!widget.isDisposed()) {
					runnable.run();
				}
			}
		});
	}	
	
	
	public static void runInDisplayThreadAfter(final int thisManyIterations, final Widget widget, final Runnable runnable) {
		if (thisManyIterations <= 0) {
			runInDisplayThread(widget, runnable);
		} else {
			runLaterInDisplayThread(widget, new Runnable() {
				@Override
				public void run() {
					runInDisplayThreadAfter(thisManyIterations - 1, widget, runnable);
				}
			});
		}
	}
	
	public static void runInDisplayThreadAfterTime(final int millisecondDelay, final Widget widget, final Runnable runnable) {
		if ((widget != null) && !widget.isDisposed()) {
			Display display = widget.getDisplay();
			display.timerExec(millisecondDelay, new Runnable() {
				@Override
				public void run() {
					if ((widget != null) && !widget.isDisposed()) {
						runnable.run();
					}
				}
			});
		}
	}
	
	public static boolean isThereAWindow() {
		Shell activeShell = Display.getDefault().getActiveShell();
		return activeShell != null;
	}

	/** 
	 * @return true iff this method is called from a UI thread 
	 */
	public static boolean inDisplayThread() {
		try {
			return Display.findDisplay(Thread.currentThread()) != null;
		} catch (NoClassDefFoundError e) {
			return false;
		}
	}

	/**
	 * Return either the Display corresponding to this thread, or the default Display
	 * if this thread is not a display thread.
	 */
	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display != null)
			return display;
		return Display.getDefault();
	}
	
	/**
	 * Return a shell to parent on, if a shell is present.
	 * 
	 * @return a shell to parent on, if a shell is present
	 */
	public static Shell getShell() {
		IWorkbench workbench;
		try {
			workbench = PlatformUI.getWorkbench();
		} catch (IllegalStateException e) {
			return null;
		}
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		if (windows.length != 0) {
			return windows[0].getShell();
		}
		Display display = workbench.getDisplay();
		if (display == null) {
			return null;
		}
		Shell shell = display.getActiveShell();
		if (shell != null) {
			return shell;
		}
		Shell[] shells = display.getShells();
		if (shells.length != 0) {
			return shells[0];
		}
		return null;
	}
	
	public static String formatToolTipText(String tooltipText, int maxWidth) {
		if (tooltipText == null) {
			tooltipText = "";
		}
		StringBuffer buffer = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(tooltipText, " ");
		int length = 0;
		while (tokenizer.hasMoreTokens()) {
			if (length > maxWidth) {
				buffer.append("\n");
				length = 0;
			} else {
				buffer.append(' ');
				length++;
			}
			
			String token = tokenizer.nextToken();
			buffer.append(token);
			length += token.length();
		}
		tooltipText = buffer.toString();
		return tooltipText;
	}

	public static String formatToolTipText(String tooltipText) {
		return formatToolTipText(tooltipText, 50);
	}
	
	public static Button createButton(Composite parent, String text) {
		return createButton(parent, text, SWT.FLAT);
	}

	public static Button createButton(Composite parent, String text, int style) {
		Button button = new Button(parent, style);
		button.setText(text);
		return button;
	}
	
	/**
	 * From http://eclipsesource.com/blogs/2009/05/08/uitostring-in-a-snippet/
	 */
	public static void printChildren(Composite composite, int count) {
		StringBuilder spaces = new StringBuilder(count * 2);
		for (int i = 0; i < count * 2; i++) {
			spaces.append(' ');
		}
		for (Control c : composite.getChildren()) {
			System.out.println(String.format("%s%s (%s)", spaces.toString(), c.toString(), c.getLayoutData()));
			if (c instanceof Composite) {
				printChildren((Composite) c, count + 1);
			}
		}
	}
	
	/**
	 * If not in the display thread, just run the runnable.
	 * Otherwise, spawn a thread for the runnable and popup
	 * a progress dialog box.
	 * 
	 * @param runnable
	 * @return true if the runnable completed normally
	 */
	public static <T extends Exception, O extends Object> O avoidDisplayThread(GenericRunnable<T, O> runnable) throws T, OperationCanceledException {
		if (!WidgetUtils.inDisplayThread() || !WidgetUtils.isThereAWindow() || CommonPlugin.isJunitRunning()) {
			return runnable.run();
		}
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		Shell shell = window.getShell();
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		OtherThreadRunnable<T, O> otherThreadRunnable = new OtherThreadRunnable<T, O>(runnable);
		try {
	        dialog.run(true, true, otherThreadRunnable);
        } catch (InvocationTargetException e) {
        	@SuppressWarnings("unchecked")
	        T exception = (T)e.getCause();
			throw exception;
        } catch (InterruptedException e) {
        	throw new OperationCanceledException();
        }
		return otherThreadRunnable.result;
	}
	
	private static final class OtherThreadRunnable<T extends Exception, O extends Object> extends ThreadedCancellableRunnableWithProgress {

	    private final GenericRunnable<T, O> runnable;
		private O result = null;

		@Override
		protected void doRun(IProgressMonitor monitor) throws Exception {
		    result = runnable.run();
		}
		
	    private OtherThreadRunnable(GenericRunnable<T, O> runnable) {
		    this.runnable = runnable;
		    String string = null;
		    StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		    boolean out = false;
		    for (int i = 0 ; i < stackTrace.length ; i++) {
				String className = stackTrace[i].getClassName();
				if (out) {
					String methodName = stackTrace[i].getMethodName();
					string = className + "." + methodName + "(...)";
					break;
		    	}
		    	if (CommonUtils.equals(WidgetUtils.class.getCanonicalName(), className)) {
					out = true;
				}
			}
		    worker.setName(string);
		}

    }

	public static void setVisibleWithFade(final Shell shell, final boolean visible) {
		setVisibleWithFade(shell, visible, 200);
	}
	
	public static void setVisibleWithFade(final Shell shell, final boolean visibility, final long fadeTime) {
		final MetaData metadata = MetaUtil.data(shell);
		final String threadKey = "__meta_fade_thread";
		final String lastVisibilityKey = "__meta_fade_visibility";
		final Thread oldThread = metadata.remove(threadKey);
		if (oldThread != null) {
			Boolean lastVisibility = metadata.get(lastVisibilityKey);
			if (lastVisibility != null && lastVisibility == visibility)
				return;
			metadata.put(lastVisibilityKey, visibility);
			oldThread.interrupt();
		}
		
		final long sleepTime = 20;
		
		final Thread newThread = new Thread() {
			@Override
			public void run() {
				
				long t0 = System.currentTimeMillis();
				
				final int[] startAlpha = new int[1];
				runInDisplayThread(shell, new Runnable() {
					@Override
					public void run() {
						if (visibility && !shell.isVisible())
							shell.setAlpha(0);
						shell.setVisible(true);
						startAlpha[0] = shell.getAlpha();
					}
				}, true);
				
				final int currentAlpha[] = {visibility ? startAlpha[0] : 255 - startAlpha[0]};
				
				while (currentAlpha[0] < 255) {
					
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						break;
					}
					
					long lastIterationTime = System.currentTimeMillis() - t0;
					
					t0 = System.currentTimeMillis();
					
					int numSteps = (int)Math.max(1, fadeTime / lastIterationTime);
					final int stepSize = Math.max(1, 255 / numSteps);
					
					boolean wasRun = runInDisplayThread(shell, new Runnable() {
						@Override
						public void run() {
							currentAlpha[0] = Math.min(255, currentAlpha[0] + stepSize);
							int alpha = visibility ? currentAlpha[0] : 255 - currentAlpha[0];
							shell.setAlpha(alpha);
						}
					}, true);
					
					if (!wasRun)
						return;
					
					lastIterationTime = System.currentTimeMillis() - t0;
				}
				
				runInDisplayThread(shell, new Runnable() {
					@Override
					public void run() {
						if (!visibility)
							shell.setVisible(false);
//						shell.setAlpha(255);
					}
				});
			}
		};
		
		metadata.put(threadKey, newThread);
		
		newThread.start();
	}

	/**
	 * Convenience method for creating a context menu.  Will log errors from
	 * the menu listener for easier debugging.
	 * 
	 * @param control
	 * @param listener
	 */
	public static final MenuManager createContextMenu(Control control, final IMenuListener listener) {
		// Create menu manager.
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				try {
					listener.menuAboutToShow(mgr);
				} catch (RuntimeException e) {
					LogUtil.error("menuAboutToShow", e);
					throw e;
				}
			}
		});
		// Create menu.
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);
		return menuManager;
	}

	/**
	 * Execute the operation in the undo context, in a job.
	 * 
	 * If the operation is an IDisplayOperation, and both the widget and site are provided,
	 * then the job will be created as a DisplayOperationJob.
	 * 
	 * If the operation can not be executed, it will be disposed.
	 * 
	 * @param operation
	 * @param undoContext
	 * @param widget
	 * @param site
	 */
	public static void execute(final IUndoableOperation operation, IUndoContext undoContext, final Widget widget, final IWorkbenchSite site) {
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		IAdaptable adaptable = null;
		if ((operation instanceof IDisplayOperation) && (widget != null) && (site != null)) {
			IDisplayOperation displayOperation = (IDisplayOperation) operation;
			adaptable = new IDisplayOperation.Adaptable(displayOperation, widget, site);
		}
		if (undoContext != null) {
			operation.addContext(undoContext);
		}
		try {
			history.execute(operation, null, adaptable);
		} catch (ExecutionException e) {
			// should never occur
			LogUtil.error(e);
		}
	}
	
	/**
	 * @deprecated Use MessageDialog.openError(WidgetUtils.getShell(), ...).
	 */
	@Deprecated
	public static void showErrorToUser(final String title, Throwable exception, final String pluginID) {
		Throwable cause = exception;
		if (exception.getCause() != null) {
			cause = exception.getCause();
		}
		String message = exception.getMessage();
		if (message==null || message.isEmpty()) {
			message = exception.getClass().getSimpleName();
		}
		showErrorToUser(title, message, new ExceptionStatus(pluginID, "------", cause), pluginID);
	}

	/**
	 * @deprecated Use MessageDialog.openError(WidgetUtils.getShell(), ...).
	 */
	@Deprecated
	public static void showErrorToUser(final String title, final String message, final String pluginID) {
		showErrorToUser(title, message, new Status(IStatus.ERROR, pluginID, "------"), pluginID);
	}

	/**
	 * @deprecated Use MessageDialog.openWarning(WidgetUtils.getShell(), ...) in most cases.
	 */
	@Deprecated
	public static void showWarningToUser(final String title, final String message, final String pluginID) {
		showErrorToUser(title, message, new Status(IStatus.WARNING, pluginID, "------"), pluginID);
	}

	/**
	 * @deprecated Use MessageDialog.openInformation(WidgetUtils.getShell(), ...) in most cases.
	 */
	@Deprecated
	public static void showInfoToUser(final String title, final String message, final String pluginID) {
		showErrorToUser(title, message, new Status(IStatus.INFO, pluginID, "------"), pluginID);
	}

	/**
	 * @deprecated Use MessageDialog.openError(WidgetUtils.getShell(), ...) in most cases.
	 */
	@Deprecated
	public static void showErrorToUser(final String title, final String message, final IStatus status, final String pluginID) {
		WidgetUtils.runInDisplayThread(WidgetUtils.getShell(), new Runnable() {
			@Override
			public void run() {
				ErrorDialog.openError(WidgetUtils.getShell(), title, message, status);
			}
		}
		);
	}
	
	public static IEditorPart getActiveEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench==null) return null;
		IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
		if (activeWindow==null) return null;
		return getActiveEditor(activeWindow);
	}
		
	public static IEditorPart getActiveEditor(IWorkbenchWindow window) {
		IWorkbenchPage activePage = window.getActivePage();
		if (activePage==null) return null;
		return activePage.getActiveEditor();
	}
	
	/**
	 * If the selection is a structured selection, it can have elements. Return the su set of the
	 * selection's elements that are of the specific type
	 * @param selection a selection
	 * @return all elements of the selection that are of the specific type
	 */
	public static <T> Set<T> typeFromSelection(ISelection selection, Class<T> type) {
		if (selection instanceof IStructuredSelection) {
			Set<T> set = new LinkedHashSet<T>();
			for (Object o : (List<?>) ((IStructuredSelection) selection).toList()) {
				if (type.isInstance(o)) {
					set.add((T)o);
				}
			}
			return set;
		}
		return Collections.emptySet();
	}

	/**
	 * Pop up a message asynchronously.  Recommended for calling from wizard performFinish methods.
	 * @param kind -- e.g. MessageDialog.WARNING
	 * @param title
	 * @param message
	 */
	public static void delayedMessage(final int kind, final String title, final String message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// ignore
				}
				WidgetUtils.runLaterInDisplayThread(getShell(), new Runnable() {
					@Override
					public void run() {
						MessageDialog.open(kind, getShell(), title, message, SWT.NONE);
					}});
			}
		}).start();
	}
	
}
