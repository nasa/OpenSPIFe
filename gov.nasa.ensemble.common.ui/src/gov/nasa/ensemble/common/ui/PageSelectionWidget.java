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

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * Widget to control the currently display page of a multi-page view of data.
 * This class is used with an instance of {@link IMultiPageController} which
 * controls the page selection.
 */
public class PageSelectionWidget extends Composite implements IPageCountChangeListener {

	private static final Image FIRST_IMAGE = WidgetPlugin.getImage("icons/firstarrow.png");
	private static final Image PREV_IMAGE = WidgetPlugin.getImage("icons/leftarrow.png");
	private static final Image NEXT_IMAGE = WidgetPlugin.getImage("icons/rightarrow.png");
	private static final Image LAST_IMAGE = WidgetPlugin.getImage("icons/lastarrow.png");
	
	private IMultiPageController pageProvider;
	private Spinner pageSpinner = null;
	private Button firstButton;
	private Button prevButton;
	private Button nextButton;
	private Button lastButton;
	private Label maxPagesLabel;

	/**
	 * Constructor for the PlanSelectionWidget.
	 * 
	 * @param parent
	 *            the parent widget
	 * @param style
	 *            style hints for the layout manager, if any
	 * @param controller
	 *            a IMultiPageController that controls page selection for a
	 *            widget.
	 */
	public PageSelectionWidget(Composite parent, int style,
			IMultiPageController controller) {
		super(parent, style);
		// Register so that we hear when another page is available so that we
		// can enable our buttons
		controller.addPageCountChangeListener(this);
		this.pageProvider = controller;
		
		/*
		 * Set the layout so that it is wrappable. That way all the components
		 * can still be seen even if the Composite has shrunk. It will take up
		 * vertical space when required.
		 */
		RowLayout layout = new RowLayout();
		layout.justify = true;
		setLayout(layout);

		Composite prevButtonPanel = new Composite(this, SWT.NONE);
		prevButtonPanel.setLayout(new FillLayout());
		firstButton = new Button(prevButtonPanel, SWT.FLAT);
		firstButton.setImage(FIRST_IMAGE);
		firstButton.addSelectionListener(new PageChangeSelectionListener() {
			@Override
			public int getPage() {
				return 0;
			}
		});
		firstButton.setToolTipText("First page of results");

		prevButton = new Button(prevButtonPanel, SWT.FLAT);
		prevButton.setImage(PREV_IMAGE);
		prevButton.addSelectionListener(new PageChangeSelectionListener() {
			@Override
			public int getPage() {
				return pageProvider.getCurrentPage() - 1;
			}
		});
		prevButton.setToolTipText("Previous page of results");
		Font font = getBoldFont();
		Composite pagePanel = new Composite(this, SWT.NONE);
		pagePanel.setLayout(new GridLayout(3, false));
		Label pageLabel = new Label(pagePanel, SWT.RIGHT);
		pageLabel.setFont(font);
		pageLabel.setText("Page ");
		pageSpinner = new Spinner(pagePanel, SWT.DROP_DOWN);
		pageSpinner.setMinimum(0);
		pageSpinner.setMaximum(0);
		pageSpinner.setSelection(0);
		pageSpinner.addSelectionListener(new PageChangeSelectionListener() {
			@Override
			public int getPage() {
				return pageSpinner.getSelection() - 1;
			}
		});
		maxPagesLabel = new Label(pagePanel, SWT.NONE);
		maxPagesLabel.setFont(font);
		maxPagesLabel.setText(" of 1");

		Composite nextButtonPanel = new Composite(this, SWT.NONE);
		nextButtonPanel.setLayout(new FillLayout());
		nextButton = new Button(nextButtonPanel, SWT.FLAT);
		nextButton.setImage(NEXT_IMAGE);
		nextButton.addSelectionListener(new PageChangeSelectionListener() {
			@Override
			public int getPage() {
				return pageProvider.getCurrentPage() + 1;
			}
		});
		nextButton.setToolTipText("Next page of results");

		lastButton = new Button(nextButtonPanel, SWT.FLAT);
		lastButton.setImage(LAST_IMAGE);
		lastButton.addSelectionListener(new PageChangeSelectionListener() {
			@Override
			public int getPage() {
				return pageProvider.getNumPages() - 1;
			}
		});
		lastButton.setToolTipText("Last page of results");

		checkButtonState(0);

	}

	private Font getBoldFont() {
		Font systemFont = FontUtils.getSystemFont();
		int desiredFontHeight = 10;
		String symbolicFontName = systemFont.toString() + "_" + desiredFontHeight;
		FontRegistry fontRegistry = FontUtils.FONT_REGISTRY_INSTANCE;
		boolean fontExists = fontRegistry.getKeySet().contains(symbolicFontName);
		Font font = null;
		if(!fontExists) {
			font = FontUtils.getStyledFont(desiredFontHeight, SWT.NONE);
			fontRegistry.put(symbolicFontName, font.getFontData());
		} else {
			font = fontRegistry.get(symbolicFontName);
		}
		return font;
	}

	/**
	 * Listener to a page change event from the IMultiPageController.
	 * 
	 * @param maxPages
	 *            the newly updated total number of windowPages of data.
	 */
	@Override
	public void pageCountChanged(final int maxPages) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				checkButtonState(maxPages);
			}
		});
	}

	private void checkButtonState(int maxPages) {
		if (maxPages == 0) {
			pageSpinner.setMaximum(0);
			pageSpinner.setSelection(0);
			maxPagesLabel.setText(" of 0   ");
		} else {
			if (pageSpinner.getMinimum() != 1)
				pageSpinner.setMinimum(1);
			pageSpinner.setMaximum(maxPages);
			String pageModifier = pageProvider.hasUnknownMaxTotalPages() ? "+"
					: "";
			maxPagesLabel.setText(" of " + maxPages + pageModifier);
		}
		boolean hasNext = pageProvider.getCurrentPage() < (pageProvider
				.getNumPages() - 1);
		boolean hasPrev = pageProvider.getCurrentPage() > 0;
		firstButton.setEnabled(hasPrev);
		prevButton.setEnabled(hasPrev);
		nextButton.setEnabled(hasNext);
		lastButton.setEnabled(hasNext);
	}

	/**
	 * Convenience class overriden above to implement page change button actions
	 */
	private abstract class PageChangeSelectionListener implements
			SelectionListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			changePage();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			changePage();
		}

		/**
		 * @return The new current page when the button is pressed or a spinner
		 *         modified
		 */
		public abstract int getPage();

		private void changePage() {
			pageProvider.setCurrentPage(getPage());
			pageSpinner.setSelection(pageProvider.getCurrentPage() + 1);
			checkButtonState(pageProvider.getNumPages());
		}

	}

}
