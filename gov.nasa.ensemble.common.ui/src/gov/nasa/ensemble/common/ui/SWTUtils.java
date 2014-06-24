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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import fj.F;

/**
 * A collection of SWT utility methods.
 */
public class SWTUtils {

	/**
	 * Like WidgetUtils.runInDisplayThread but can be used when no control
	 * is available.  Try to use the WidgetUtils version whenever possible, 
	 * because it insulates you from problems with widget disposal, and also 
	 * handles the possibility of multiple displays.
	 */
	public static void runInDisplayThread(final Runnable runnable) {
		if (Display.findDisplay(Thread.currentThread()) == Display.getDefault()) {
			try {
				runnable.run();
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				Logger.getLogger(SWTUtils.class).error("error in display thread", t);
			}
		} else {
			Display.getDefault().asyncExec(runnable);			
		}
	}
	
	/**
	 * Return the total width and height of a text string. Handles multiple-line
	 * text (unlike GC.stringExtent(String)).
	 * 
	 * @param gc
	 *            the GraphicsContext that the text is intended to render on
	 * @param text
	 *            the text to compute the size of
	 * @return size (width, height) in pixels
	 */
	public static Point getStringExtent(GC gc, String text) {
		Point size = new Point(0, 0);

		/* Parse the string into separate lines */
		StringReader stringReader = new StringReader(text);
		BufferedReader br = new BufferedReader(stringReader);
		try {
			String line;
			int lines = 0;
			int lineLength = 0;
			int lineHeight = 0;
			while ((line = br.readLine()) != null) {
				lines++;
				Point p = gc.stringExtent(line);
				lineLength = Math.max(lineLength, p.x);
				lineHeight = Math.max(lineHeight, p.y);
			}
			size.x = lineLength;
			size.y = lines * lineHeight;
		} catch (IOException io) {
			// IOExceptions should be impossible to get
		}

		return size;
	}

	/**
	 * Relayout a composite that needs to be laid out but is not responding to
	 * standard requests. This will attempt to relayout everything from the
	 * given Composite up the stack until it either hits a ScrolledForm or the
	 * root of the application.
	 * 
	 * @param p
	 *            the composite to layout
	 */
	public static void relayout(Composite p) {
		Composite c = p;
		while (c != null) {
			c.setRedraw(false);
			c = c.getParent();
			if (c instanceof ScrolledForm) {
				break;
			}
		}
		c = p;
		while (c != null) {
			c.layout(true);
			c = c.getParent();
			if (c instanceof ScrolledForm) {
				((ScrolledForm) c).reflow(true);
				break;
			}
		}
		c = p;
		while (c != null) {
			c.setRedraw(true);
			c = c.getParent();
			if (c instanceof ScrolledForm) {
				break;
			}
		}
	}
	
    /**
	 * Return the image created from the 
	 * @param plugin the plugin that this icon is located in
	 * @param imageName relative to the plugin path
	 * @return the image, null if none found
	 */
	public static Image getImage(AbstractUIPlugin plugin, String imageName) {
		URL url = FileLocator.find(plugin.getBundle(), new Path(imageName), null);
		return ImageDescriptor.createFromURL(url).createImage();
	}
	
	public static List<IWorkbenchPart> getPartsWhichSatisfy(final F<IWorkbenchPart, Boolean> accepter) {
		final List<IWorkbenchPart> result = new ArrayList<IWorkbenchPart>();
		
		applyToAllParts(new PartOperator() {
			@Override
			public void execute(IWorkbenchPart part) {
				if (accepter.f(part))
					result.add(part);
			}
		});
		
		return result;
	}
	
	public static void applyToAllParts(PartOperator operator) {
		IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow workbenchWindow : workbenchWindows) {
			IWorkbenchPage[] pages = workbenchWindow.getPages();
			for (IWorkbenchPage page : pages) {
				IViewReference[] viewRefs = page.getViewReferences();
				IEditorReference[] editorRefs = page.getEditorReferences();
				for (IViewReference ref : viewRefs)
					operator.execute(ref.getPart(false));
				for (IEditorReference ref : editorRefs)
					operator.execute(ref.getPart(false));
			}
		}
	}
	
	/**
	 * If we are running in the display thread,
	 * take a moment to process any pending updates.
	 * This allows for repainting exposed areas and
	 * ensures that the progress bar will move properly
	 * when the application has lost focus.
	 */
	public static void updateDisplay() {
		Display display = Display.getCurrent(); // yes we want getCurrent()
		if ((display != null) && (PlatformUI.isWorkbenchRunning() == true)) {
			try {
				display.readAndDispatch();
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				Logger.getLogger(SWTUtils.class).error("updateDisplay failed", t);
			}
		}
	}
	
	public interface PartOperator {
		public void execute(IWorkbenchPart part);
	}
}
