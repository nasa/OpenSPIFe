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
package gov.nasa.arc.spife.ui.timeline.util;

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.GanttSection;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.TimelineContent;
import gov.nasa.arc.spife.timeline.model.ZoomOption;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.AccessibleTool;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.MultiPageEditorPart;

public class TimelineUtils implements TimelineConstants {

	private final static int POINTS_PER_INCH = 72;

	public static int getRowElementHeight() {
		return TIMELINE_PREFERENCES.getInt(TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT);
	}
	
	public static int getRowElementHeight(TimelineViewerEditPart ep) {
		return getRowElementHeight(ep.getViewer());
	}
	
	public static int getRowElementHeight(TimelineViewer viewer) {
		Object model = viewer.getTimelineSectionModel();
		Integer height = null;
		if (model instanceof TimelineContent) {
			height = ((TimelineContent) model).getRowHeight();
		} else if (model instanceof GanttSection) {
			height = ((GanttSection) model).getRowHeight();
		}
		if (height == null) {
			Timeline timeline = viewer.getTimeline();
			ETimeline tModel = timeline.getTimelineModel();
			height = tModel.getRowHeight();
		}
		return height != null ? height : TIMELINE_PREFERENCES.getInt(TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT);
	}
	
	/**
	 * @deprecated - use EMFUtils.executeCommand(domain, command);
	 */
	@Deprecated
	public static void executeCommand(EditingDomain domain, Command command) {
		EMFUtils.executeCommand(domain, command);
	}
	
	@SuppressWarnings("unchecked")
	public static void registerAsList(EditPart editPart) {
		EditPartViewer v = editPart.getViewer();
		if (v != null) {
			Map r = v.getEditPartRegistry();
			Object model = editPart.getModel();
			List<EditPart> list = (List<EditPart>) r.get(model);
			if (list == null) {
				list = new ArrayList<EditPart>();
				r.put(model, list);
			}
			list.add(editPart);
		}
	}

	@SuppressWarnings("unchecked")
	public static void unregisterAsList(EditPart editPart) {
		EditPartViewer v = editPart.getViewer();
		if (v != null) {
			Map r = v.getEditPartRegistry();
			Object model = editPart.getModel();
			List<EditPart> list = (List<EditPart>) r.get(model);
			if (list != null) {
				list.remove(editPart);
			}
		}
	}
	
//	private static Font rowElementFont;
//	private static int oldFontHieght;
//	public static Font getRowElementHieghtFont() {
//		int height = getRowElementHeight();
//		if (rowElementFont == null) {
//			rowElementFont = deriveRowElementHieghtFont(Display.getDefault().getSystemFont());
//		} else if (oldFontHieght != height) {
//			rowElementFont = deriveRowElementHieghtFont(rowElementFont);
//		}
//		return rowElementFont;
//	}
	
	public static Font deriveRowElementHeightFont(Font font) {
		return deriveFontSize(font, TimelinePreferencePage.P_CONTENT_FONT_SIZE);
	}
	
	public static Font deriveScaleHeightFont(Font font) {
		return deriveFontSize(font, TimelinePreferencePage.P_SCALE_FONT_SIZE);
	}

	private static Font deriveFontSize(Font font, String preferenceKey) {
		int pixelHeight = TIMELINE_PREFERENCES.getInt(preferenceKey);
		if (font == null) {
			font = Display.getDefault().getSystemFont();
		}
		Device device = font.getDevice();
		if (device == null) {
			device = WidgetUtils.getDisplay();
		}
		int pixelsPerInch = device.getDPI().y;
		int pointHeight;
		if (pixelsPerInch == 0) {
			pointHeight = pixelHeight;
		} else {
			pointHeight = POINTS_PER_INCH * pixelHeight / pixelsPerInch;
		}
		FontData[] fontData = font.getFontData();
		for (int i=0; i<fontData.length; i++) {
			fontData[i].setHeight(pointHeight);
		}
		
		String symbolicFontName = font.toString() + "_" + pointHeight;
		FontRegistry fontRegistry = FontUtils.FONT_REGISTRY_INSTANCE;
		boolean fontExists = fontRegistry.getKeySet().contains(symbolicFontName);
		Font desiredFont = null;
		if(!fontExists) {
			desiredFont = FontUtils.getStyledFont(font.getDevice(), fontData);
			fontRegistry.put(symbolicFontName, fontData);
		} else {
			desiredFont = fontRegistry.get(symbolicFontName);
		}
		return desiredFont;
	}
	
	/**
	 * Get a timeline from the given object. works for ExecutionEvents, MultiPageEditorParts, 
	 * IEditorPart, EditPolicy, EditPart, TimelineViewer and Timeline
	 * 
	 * @param object
	 * @return
	 */
	public static Timeline getTimeline(Object object) {
		if (object instanceof ExecutionEvent) {
			ExecutionEvent event = (ExecutionEvent) object;
			IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
			if (activeEditor != null) {
				object = activeEditor;
			}
		}
		if (object instanceof MultiPageEditorPart) {
			MultiPageEditorPart editor = (MultiPageEditorPart) object;
			IEditorPart[] editorParts = editor.findEditors(editor.getEditorInput());
			for (IEditorPart editorPart : editorParts) {
				object = editorPart.getAdapter(Timeline.class);
				if (object != null) {
					break;
				}
			}
		}
		if (object instanceof IWorkbench) {
			IEditorPart editorPart = EditorPartUtils.getCurrent((IWorkbench)object);
			return getTimeline(editorPart);
		}
		if (object instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) object;
			object = CommonUtils.getAdapter(editorPart, Timeline.class);
		}
		if (object instanceof EditPolicy) {
			EditPolicy policy = (EditPolicy) object;
			object = policy.getHost();
		}
		if (object instanceof EditPart) {
			EditPart editPart = (EditPart) object;
			object = editPart.getViewer();
		}
		if (object instanceof TimelineViewer) {
			TimelineViewer timelineViewer = (TimelineViewer) object;
			object = timelineViewer.getTimeline();
		}
		if (object instanceof Timeline) {
			return (Timeline)object;
		}		
		return null;
	}
	
	/**
	 * 
	 * @deprecated call getTimeline(Object)
	 * @return Timeline
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static Timeline getTimelineInActivePart() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage page = null;
		if (workbench != null) {
			IWorkbenchWindow activeWindow = null;
			activeWindow = workbench.getActiveWorkbenchWindow();
			if (activeWindow != null) {
				page = activeWindow.getActivePage();
			}
		}
		
		if (page != null) {
			List<IWorkbenchPartReference> parts = new ArrayList<IWorkbenchPartReference>();
			parts.add(page.getActivePartReference());
			parts.addAll(Arrays.asList(page.getViewReferences()));
			for (IWorkbenchPartReference p : parts) {
				if (p == null)
					return null;
				IWorkbenchPart part = p.getPart(false);
				if (part != null) {
					Timeline timeline = CommonUtils.getAdapter(part, Timeline.class);
					if (timeline != null) {
						return timeline;
					}
				}
			}
		}
		
		IEditorPart editorPart = EditorPartUtils.getCurrent();
		if(editorPart != null) {
			Timeline timeline = CommonUtils.getAdapter(editorPart, Timeline.class);
			if (timeline != null) {
				return timeline;
			}
		}

		return null;
	}
	
	/**
	 * Update the cursor time based on current viewer and tool.
	 */
	public static synchronized void updateCursorTime(TimelineViewer timelineViewer, AccessibleTool tool) {
		Timeline timeline = timelineViewer.getTimeline();
		if (timeline != null) {
			Point pt = tool.getLocation();
			Page page = timelineViewer.getPage();
			GraphicalEditPart ep = timelineViewer.getTimelineEditPart().getDataEditPart();
			int figureX = ep.getFigure().getBounds().x;
			long time = page.getTime(pt.x - figureX).getTime();
			timeline.setCursorTime(time);
		}
	}
	
	public static Viewport getViewport(IFigure figure) {
		IFigure fig = figure;
		while (fig != null && !(fig instanceof Viewport))
			fig = fig.getParent();
		return (Viewport)fig;
	}

	public static Collection<? extends Object> getModelChildren(TimelineViewer viewer, EditPart topEditPart, Object model) {
		TreeTimelineContentProvider cp = viewer.getTreeTimelineContentProvider();
		List<Object> children = new ArrayList<Object>();
		ExpansionModel expansion = (ExpansionModel) viewer.getProperty(ExpansionModel.ID);
		Object root = viewer.getTimeline().getModel();
		if (topEditPart.getModel() == model || expansion.isExpanded(model)) {
			for (Object child : cp.getElements(model)) {
				if (child != null) {
					children.add(child);
					if (root == cp.getParent(child)) {
						if (getBoolean(viewer, TIMELINE_GROUP_ELEMENTS)) {
							children.addAll(getModelChildren(viewer, topEditPart, child));
						}
					} else {
						children.addAll(getModelChildren(viewer, topEditPart, child));
					}
				}
			}
		}
		return children;
	}

	public static boolean getBoolean(TimelineViewer viewer, String key) {
		Object value = viewer == null ? false : viewer.getProperty(key);
		return value == null || (Boolean) value;
	}
	
	public static boolean isMoveable(EditPart editPart) {
		if(editPart.understandsRequest(new Request(RequestConstants.REQ_MOVE))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determine whether the edit part is scrollable -- required to be moveable and, if a TimelineViewerEditPart,
	 * it must not have scrolling inhibited
	 * 
	 * @param editPart the EditPart to be tested for scrollability
	 * @return true if scrollable
	 */
	public static boolean isScrollable(EditPart editPart) {
		return isMoveable(editPart)
			&& !(editPart instanceof TimelineViewerEditPart && ((TimelineViewerEditPart)editPart).isScrollInhibited());
	}
	
	/**
	 * Utility method to determine if the edit part is visible vertically
	 * in the screen. This is necessary since the vertical scrolling is
	 * managed by a native widget which GEF does not seem to play nice with.
	 * 
	 * @param editPart to determine if associated figure is visible vertically for
	 * @return true is visible
	 */
	public static boolean isVerticallyVisible(TimelineViewerEditPart editPart) {
		ScrolledComposite scroller = editPart.getTimeline().getVerticalScroller();
		int position = scroller.getVerticalBar().getSelection();
		Rectangle visible = scroller.getBounds();
		visible = new Rectangle(visible.x , visible.y = position, visible.width, visible.height);
		IFigure figure = editPart.getFigure();
		org.eclipse.draw2d.geometry.Rectangle f = figure.getBounds();
		TimelineViewer viewer = editPart.getViewer();
		Rectangle current = new Rectangle(f.x, f.y + viewer.getControl().getBounds().y, f.width, f.height);
		return current.intersects(visible);
	}

	public static ZoomOption createZoomInterval(String title, long msInterval, long majorTickInterval, long minorTickInterval) {
		ZoomOption option = TimelineFactory.eINSTANCE.createZoomOption();
		option.setName(title);
		option.setMsInterval(msInterval);
		option.setMajorTickInterval(majorTickInterval);
		option.setMajorTickInterval(minorTickInterval);
		return option;
	}
}
