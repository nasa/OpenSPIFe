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
package gov.nasa.ensemble.common.ui.type.editor;

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class URLEditor extends AbstractTypeEditor<String> {

	private static final Logger trace = Logger.getLogger(URLEditor.class);
	private final Composite parent;
	private final Composite urlComposite;
	private Hyperlink textURL;
	private Text editableURL;
	private Button editButton;
	private String url = "";
	FormToolkit toolkit;
	
	public URLEditor(Composite parent) {
		super(String.class);
		this.parent = parent;
		toolkit = new FormToolkit(parent.getDisplay());
		this.urlComposite = createURLComposite();
	}
	
	public Control getEditorControl() {
		return urlComposite;
	}
	
	@Override
	public void setObject(final Object object) {
		super.setObject(object);
		// Set the local cache, then force a redisplay of the fields.
		if (object != null) {
			url = (String) object;
		}
		WidgetUtils.runInDisplayThread(parent, new Runnable() {
			public void run() {
				if (textURL != null) {
					textURL.setText(url);
					if (url == null || url.equals("") ) {
						textURL.setVisible(false);
					}
					else {
						textURL.setVisible(true);
					}
				}
				if (editableURL != null) {
					editableURL.setText(url);
				}
			}
		});
	}
	
	private Hyperlink createTextURL(Composite urlComp) {
		
		Hyperlink h = toolkit.createHyperlink(urlComp, url, SWT.NO_FOCUS);
		h.setText(url);
		
		if (url == null || url.equals("") ) {
			h.setVisible(false);
		}
			
		h.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
//		          // ADD THESE 2 TO DEPS
//				  org.eclipse.ui.ide,
//				  org.eclipse.core.resources
//				  IWorkbench wb = PlatformUI.getWorkbench();
//				   IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
//				   IWorkbenchPage page = win.getActivePage();
//				   IPath p = new Path(url);
//				   IFile f = new File(p, null);
//				   page.openEditor(
//				      new FileEditorInput(f),
//				      IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
				if ("".equals(url)) {
					MessageDialog.openInformation(textURL.getShell(),"No URL", "Click on the Edit URL button, to set the URL.");
				}
				else {
					IWorkbenchBrowserSupport browserSupport =
						PlatformUI.getWorkbench().getBrowserSupport();
						try {
						   IWebBrowser  browser = browserSupport.createBrowser("");
						   // Remove any '\ ' and replace any ' ' with %20;
						   // TODO: do a full HTML encode?
						   String reformedURL = url.replace("\\ ", " ");
						   reformedURL = reformedURL.replaceAll(" ", "%20");
						   browser.openURL(new URL(reformedURL));
						} catch (PartInitException e1) {
							trace.error("Failed to INIT part for opening WebBrowser in URLEditor", e1);
						} catch (MalformedURLException e1) {
							trace.info("Malformed URL " + url, e1);
							// Show warning to User.
							MessageDialog.openError(textURL.getShell(),"Malformed URL", "The URL (" + url +") is malformed and can not be opened in a webbrowser.");
						}
				}
			}
		});
		GridData gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
		h.setLayoutData(gd);
		return h;
	}
	private Text createEditableURL(Composite urlComp) {
		final Text t = toolkit.createText(urlComp, url);
		GridData gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
		t.setLayoutData(gd);
		t.addFocusListener(new FocusAdapter(){
			@Override
			public void focusLost(FocusEvent e) {
				WidgetUtils.runInDisplayThread(parent, new Runnable() {
					public void run() {
						if (! editButton.isDisposed()) {
							toggleURLFields(false);
						}
					}
				});
			}
		});
		t.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				if (event.keyCode == SWT.ESC) {
					WidgetUtils.runInDisplayThread(parent, new Runnable() {
						public void run() {
							if (! editButton.isDisposed()) {
								t.setText(url);
							}
						}
					});
				} else if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
					WidgetUtils.runInDisplayThread(parent, new Runnable() {
						public void run() {
							if (! editButton.isDisposed()) {
								toggleURLFields(false);
							}
						}
					});
				}
			}
		});
		return t;
	}
	
	private Composite createURLComposite() {
		final Composite urlComp = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		gl.marginBottom=0;
		gl.marginLeft =0;
		gl.marginRight =0;
		gl.marginTop =0;
		urlComp.setLayout(gl);
		
		editButton = new Button(urlComp,SWT.TOGGLE);
		editButton.setImage(MissionUIConstants.getInstance().getIcon("link_edit"));
		editButton.setSelection(false);
		editButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				boolean edit = editButton.getSelection();
				toggleURLFields(edit);
			}
		});
		GridDataFactory.createFrom(new GridData(GridData.CENTER)).hint(25, SWT.DEFAULT).grab(false,false).applyTo(editButton);
		
		textURL = createTextURL(urlComp);
		GridData gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
		gd.verticalAlignment = GridData.CENTER;
		gd.minimumHeight=30;
		textURL.setLayoutData(gd);
		
		return urlComp;
	}

	protected void toggleURLFields(final boolean edit) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				
				if (edit) {
					// Ensure that an old editable url is not floating around
					if (editableURL != null) {
						// Store any changes
						String newURL = editableURL.getText();
						editableURL.dispose();
						editableURL = null;
						
						setObject(newURL);
					}
					if (textURL != null) {
						textURL.dispose();
						textURL = null;
					}
					
					editableURL = createEditableURL(urlComposite);
					GridData gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
					gd.minimumHeight = 30;
					editableURL.setLayoutData(gd);
				}
				else {
					// Ensure an old textURL is not floating around
					if (textURL != null) {
						textURL.dispose();
						textURL = null;
					}
					
					if (editableURL != null) {
						// Store any changes
						String newURL = editableURL.getText();
						editableURL.dispose();
						editableURL = null;
						
						setObject(newURL);
					}
					
					textURL = createTextURL(urlComposite);
					GridData gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
					
					textURL.setLayoutData(gd);
				}
				editButton.setSelection(edit);
				urlComposite.getParent().layout(true, true);
			}
		});
	}
	
}
