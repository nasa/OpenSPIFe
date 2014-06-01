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
package gov.nasa.ensemble.common.ui.detail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;

public abstract class IconAndLinkDetail extends SimpleDetail {

	private Image icon;
	private String hyperlinkText;

	public IconAndLinkDetail(FormToolkit toolkit, Image icon, String hyperlinkText) {
		super(toolkit);
		this.icon = icon;
		this.hyperlinkText = hyperlinkText;
	}

	@Override
	public Control createControl(Composite parent) {
		Hyperlink link = toolkit.createHyperlink(parent, hyperlinkText, SWT.NONE);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				execute();
			}
		});
		return link;
	}

	@Override
	protected Label createLabel(Composite parent) {
		Label label = super.createLabel(parent);
		label.setImage(icon);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				execute();
			}
		});
		return label;
	}
	
	/**
	 * Subclasses should override to implement behavior when the icon or link is
	 * clicked.
	 */
	protected abstract void execute();

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getToolTipText() {
		return null; //no tooltip required for a hyperlink
	}
}
