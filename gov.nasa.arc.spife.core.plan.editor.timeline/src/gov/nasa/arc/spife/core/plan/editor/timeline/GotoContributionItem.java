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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.type.editor.StringTypeEditor;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import java.util.Date;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;

public class GotoContributionItem extends ContributionItem {

	private static final String ID = "gov.nasa.arc.spife.core.plan.editor.timeline.goto_widget";

	private IPartService service;
	private IPartListener partListener;
	private Timeline timeline;
	private Composite shell;
	private Text text;
	private StringTypeEditor textEditor;
	private Button goButton;
	private ToolItem toolitem;

	public GotoContributionItem(IPartService partService) {
		super(ID);
		service = partService;
		partService.addPartListener(partListener = new IPartListener() {
			@Override
			public void partActivated(IWorkbenchPart part) {
				if (part instanceof MultiPagePlanEditor) {
					MultiPagePlanEditor editor = (MultiPagePlanEditor)part;
					for (int i = 0; i < editor.getPageCount(); i++) {
						IEditorPart page = editor.getEditor(i);
						if (page instanceof TimelineEditorPart) {
							timeline = ((TimelineEditorPart)page).getTimeline();
							refresh();
						}
					}
				}
			}
			@Override
			public void partBroughtToTop(IWorkbenchPart p) 	{ /* no operation */ }
			@Override
			public void partClosed(IWorkbenchPart p) 		{ /* no operation */ }
			@Override
			public void partDeactivated(IWorkbenchPart p) {
				timeline = null;
			}
			@Override
			public void partOpened(IWorkbenchPart p) 		{ /* no operation */ }
		});
	}
	
	private void refresh() {
		WidgetUtils.runInDisplayThread(shell, new Runnable() {
			@Override
			public void run() {
				final boolean b = (timeline != null);
				text.setEnabled(b);
				goButton.setEnabled(b);
			}
		});
	}
	
	@Override
	public void fill(ToolBar parent, int index) {
		toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
		Control control = createControl(parent);
		toolitem.setControl(control);	
	}

	private Control createControl(Composite parent) {
		
		shell = new Composite(parent,SWT.NONE);
		
		GridLayout gridLayout = new GridLayout ();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		shell.setLayout (gridLayout);

		text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(100, -1));
		IStringifier<Date> dateStringifier = StringifierRegistry.getStringifier(Date.class);
		textEditor = new StringTypeEditor(dateStringifier, text);
		text.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// no operation
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					doit();
				}
			}
		});
		
		goButton = new Button (shell, SWT.PUSH);
		goButton.setText("Go!");
		goButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				doit();
			}
		});
		
		refresh();
		toolitem.setWidth(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		return shell;
	}

	private void doit() {
		if (textEditor.getObject() != null && timeline != null) {
			timeline.centerOnTime((Date)textEditor.getObject());
		}
	}
	
	@Override
	public void dispose() {
		if (partListener != null) {
			service.removePartListener(partListener);
			partListener = null;
		}
		text = null;
	}

}
