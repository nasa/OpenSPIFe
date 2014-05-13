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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.arc.spife.ui.table.days.DayViewer.DayHeaderMouseListener;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnLayout;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.core.plan.editor.merge.MergePlugin;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTotalComposite;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class DayComposite extends EnsembleComposite {
	
	private static final Image NOTE_ICON = MergePlugin.getImageDescriptor("icons/sticky-note-text.png").createImage();
	
	private Composite header;
	private Label label;
	private Label noteIndicator;
	private TreeTableComposite treeTableComposite;
	private MergeTotalComposite totalComposite;
	
	public DayComposite(Composite parent, TreeTableColumnConfiguration configuration) {
		super(parent, SWT.BORDER);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).margins(4, 4).spacing(4, 4).applyTo(this);
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		header = new Composite(this, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).margins(1, 1).applyTo(header);
		header.setBackground(ColorConstants.lightGray);
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		label = new Label(header, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		noteIndicator = new Label(header, SWT.NONE);
		noteIndicator.setImage(NOTE_ICON);
		noteIndicator.setLayoutData(new GridData(SWT.END, SWT.FILL, true, false, 1, 1));
		
		treeTableComposite = new TreeTableComposite(this, configuration, true);
		treeTableComposite.setLayout(new TreeTableColumnLayout(false));
		treeTableComposite.setData("name", "DayComposite,treeTableComposite");
		treeTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeTableComposite.getTree().getVerticalBar().setVisible(true);
		
		totalComposite = new MergeTotalComposite(this, configuration);
		totalComposite.setData("name", "DayComposite.totalComposite");
		totalComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		setTabList(new Control[] { treeTableComposite });
	}
	
	public void addMouseAdapter(DayHeaderMouseListener listener) {
		header.addMouseListener(listener);
		header.addMouseTrackListener(listener);
		label.addMouseListener(listener);
		label.addMouseTrackListener(listener);
		noteIndicator.addMouseListener(listener);
		noteIndicator.addMouseTrackListener(listener);
	}
	
	public void setHeaderToolTipText(String text) {
		header.setToolTipText(text);
		label.setToolTipText(text);
		noteIndicator.setToolTipText(text);
	}
	
	public void setNoteVisible(final boolean isVisible) {
		WidgetUtils.runInDisplayThread(noteIndicator, new Runnable() {
			@Override
			public void run() {
				noteIndicator.setVisible(isVisible);
			}
		});
	}
	
	public void setTitle(String title) {
		this.label.setText(title);
	}
	
	public Composite getHeaderControl() {
		return header;
	}
	
	public TreeTableComposite getTreeTableComposite() {
		return treeTableComposite;
	}
	
	public MergeTotalComposite getTotalComposite() {
		return totalComposite;
	}
	
	@Override
	public boolean setFocus() {
		return treeTableComposite.setFocus();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		header.dispose();
		label.dispose();
		noteIndicator.dispose();
		treeTableComposite.dispose();
		totalComposite.dispose();
	}
	
}
