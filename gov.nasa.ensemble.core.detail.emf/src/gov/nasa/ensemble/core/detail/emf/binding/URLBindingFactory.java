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
package gov.nasa.ensemble.core.detail.emf.binding;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.type.editor.StringTypeEditor;
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * 
 * @author Eugene Turkov
 * 
 */
public class URLBindingFactory extends BindingFactory {

	public static Image BROWSE_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/browse.png").createImage();

	public static Image OPEN_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/open.png").createImage();

	private final static IStringifier<URL> urlStringifier = StringifierRegistry.getStringifier(URL.class);

	private final static String BUTTON_KEY = "BUTTON_KEY"; // to access UI "open url"
	private final static String BROWSE_BUTTON_KEY = "BROWSE_BUTTON_KEY";

	private static URL DEFAULT_URL;

	/**
	 * This root composite is the composite to which all controls will be added
	 */
	private Composite createRootComposite(FormToolkit formToolkit, Composite parentComposite) {
		boolean makeColumnsEqualWidth = false;
		int numberOfColumns = 2;
		GridLayout rootCompositeGridLayout = new GridLayout(numberOfColumns, makeColumnsEqualWidth);
		GridData rootCompositeGridData = Activator.createStandardGridData();

		rootCompositeGridLayout.marginWidth = 0;
		rootCompositeGridLayout.marginHeight = 0;

		Composite rootComposite = formToolkit.createComposite(parentComposite, SWT.NONE);
		rootComposite.setLayout(rootCompositeGridLayout);
		rootComposite.setLayoutData(rootCompositeGridData);

		return rootComposite;
	}

	/**
	 * The hyperlinkAndBrowse composite is the composite which shall contain the hyperlink text box and the browse button.
	 */
	private Composite hyperlinkAndBrowseComposite(Composite rootComposite) {
		boolean grabExcessHorizontalSpace = true;
		boolean grabExcessVerticalSpace = false;
		GridData hyperlinkAndBrowseCompositeGridData = new GridData(SWT.FILL, SWT.CENTER, grabExcessHorizontalSpace, grabExcessVerticalSpace);

		boolean makeColumnsEqualWidth = false;

		GridLayout hyperlinkAndBrowseCompositeGridLayoutMulti = new GridLayout(2, makeColumnsEqualWidth);

		Composite hyperlinkAndBrowseComposite = new Composite(rootComposite, SWT.NONE);
		hyperlinkAndBrowseComposite.setLayout(hyperlinkAndBrowseCompositeGridLayoutMulti);
		hyperlinkAndBrowseComposite.setLayoutData(hyperlinkAndBrowseCompositeGridData);

		hyperlinkAndBrowseCompositeGridLayoutMulti.marginHeight = 0;
		hyperlinkAndBrowseCompositeGridLayoutMulti.marginWidth = 0;
		hyperlinkAndBrowseCompositeGridLayoutMulti.horizontalSpacing = 0;

		return hyperlinkAndBrowseComposite;
	}

	/**
	 * When the go button is clicked, the hyperlink is opened
	 */
	private Button createOpenButton(Composite rootComposite, String propertyName, final Text hyperlinkTextField) {
		boolean grabExcessHorizontalSpace = false;
		boolean grabExcessVerticalSpace = false;

		final Button openButton = new Button(rootComposite, SWT.PUSH);
		openButton.setToolTipText("Open " + propertyName);

		final Button browseButton = (Button) hyperlinkTextField.getData(BROWSE_BUTTON_KEY);

		openButton.setImage(OPEN_ICON);

		openButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, grabExcessHorizontalSpace, grabExcessVerticalSpace));

		openButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// check to see if this is a web page
				String destination = URLBindingFactory.convertHyperlinkTextFieldToText(hyperlinkTextField.getText());
				// maybe replace with a regex instead of a string constant?
				if (destination.indexOf("://") != -1) {
					IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
					try {
						IWebBrowser browser = browserSupport.getExternalBrowser();
						browser.openURL(new URL(destination));
					} catch (PartInitException partInitException) {
						logger.error(partInitException);
					} catch (MalformedURLException malformedURLException) {
						logger.error(malformedURLException);
					}
				}

				// assume that it is a resource to open locally
				else {

					IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IFileStore fileStore = new LocalFileStore(new File(destination));
					FileStoreEditorInput input = new FileStoreEditorInput(fileStore);
					try {
						// let the system decide on which editor to open
						workbenchPage.openEditor(input, IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
					} catch (PartInitException partInitException) {
						// let's then try to open it with eclipse default text editor
						try {
							workbenchPage.openEditor(input, "org.eclipse.ui.DefaultTextEditor");
						} catch (PartInitException partInitException2) {
							logger.error(partInitException2);
						}
					}
				}
			}

			private Logger logger = Logger.getLogger(URLBindingFactory.class);
		});

		// add a key listener to set the focus on this go button when
		// the ENTER key is pressed while editing the hyperlinkTextField
		hyperlinkTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					URL javaObject = null;
					try {
						javaObject = new URL(hyperlinkTextField.getText());
					} catch (MalformedURLException e1) {
						// do nothing
					}

					String displayString = "";
					if (javaObject != null) {
						displayString = urlStringifier.getDisplayString(javaObject);
					}
					hyperlinkTextField.setText(displayString);
					if (openButton.isEnabled()) {
						openButton.setFocus();
					}

					else {
						browseButton.setFocus();
					}
				}
			}

		});
		return openButton;
	}

	private FocusListener createHyperlinkTextFieldFocusListener(final Composite hyperlinkAndBrowseComposite, final Text hyperlinkTextField, final StringTypeEditor stringTypeEditor) {
		FocusListener focusListener = new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				Button button = (Button) hyperlinkAndBrowseComposite.getData(BUTTON_KEY);
				String trim = hyperlinkTextField.getText().trim();

				if (trim.equals(DEFAULT_URL.toString())) {
					hyperlinkTextField.setText("");
				}

				boolean enabled = !hyperlinkTextField.getText().trim().equals("");
				button.setEnabled(enabled);
			}

			@Override
			public void focusGained(FocusEvent e) {
				Button button = (Button) hyperlinkAndBrowseComposite.getData(BUTTON_KEY);

				URL url = null;
				try {
					url = urlStringifier.getJavaObject(hyperlinkTextField.getText().trim(), null);
				}

				catch (Throwable t) {
					// do nothing
				}

				if (url != null) {
					stringTypeEditor.setObject(url);
				}

				else if (stringTypeEditor.getObject() == null) {
					stringTypeEditor.setObject(DEFAULT_URL);
				}

				if (hyperlinkTextField.getText().equals(DEFAULT_URL.toString())) {
					hyperlinkTextField.setText("");
				}
				button.setEnabled(!hyperlinkTextField.getText().trim().equals(""));
			}
		};

		return focusListener;
	}

	/**
	 * Text field that will contain the url
	 */
	private Text createHyperlinkTextField(final Composite hyperlinkAndBrowseComposite, String propertyValue) {
		boolean grabExcessHorizontalSpace = true;
		boolean grabExcessVerticalSpace = false;
		final Text hyperlinkTextField = new Text(hyperlinkAndBrowseComposite, SWT.BORDER);
		final StringTypeEditor stringTypeEditor = new StringTypeEditor(urlStringifier, hyperlinkTextField);

		if (DEFAULT_URL == null) {
			try {
				DEFAULT_URL = new URL("file://c:/procedure.txt");
			} catch (MalformedURLException e1) {
				// do nothing
			}
		}
		stringTypeEditor.setObject(DEFAULT_URL);

		hyperlinkTextField.setText(propertyValue);
		GridData hyperlinkTextFieldGridData = new GridData(SWT.FILL, SWT.CENTER, grabExcessHorizontalSpace, grabExcessVerticalSpace);
		hyperlinkTextField.setLayoutData(hyperlinkTextFieldGridData);

		// update the focus of the open url button

		hyperlinkTextField.addFocusListener(createHyperlinkTextFieldFocusListener(hyperlinkAndBrowseComposite, hyperlinkTextField, stringTypeEditor));

		hyperlinkTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Button button = (Button) hyperlinkAndBrowseComposite.getData(BUTTON_KEY);
				try {
					urlStringifier.getJavaObject(hyperlinkTextField.getText().trim(), null);
				}

				catch (Throwable t) {

					button.setEnabled(false);
					return;
				}

				button.setEnabled(!hyperlinkTextField.getText().trim().equals(""));
			}

		});
		return hyperlinkTextField;
	}

	/**
	 * The button that will allow the user to browse the file system to choose a new url.
	 */
	private Button createBrowseButton(Composite hyperlinkAndBrowseComposite, final Text hyperlinkTextField) {
		boolean grabExcessHorizontalSpace = false;
		boolean grabExcessVerticalSpace = false;
		final Button browseButton = new Button(hyperlinkAndBrowseComposite, SWT.PUSH);

		browseButton.setImage(BROWSE_ICON);
		browseButton.setToolTipText("Browse");
		GridData browseButtonLayoutData = new GridData(SWT.LEFT, SWT.CENTER, grabExcessHorizontalSpace, grabExcessVerticalSpace);
		browseButton.setLayoutData(browseButtonLayoutData);

		browseButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(browseButton.getShell());
				String selectedFile = fileDialog.open();
				if (selectedFile != null) {
					String convertedText = URLBindingFactory.convertTextToHyperlinkTextField(selectedFile);
					hyperlinkTextField.setText(convertedText);
					hyperlinkTextField.setFocus();
				}
			}
		});

		hyperlinkTextField.setData(BROWSE_BUTTON_KEY, browseButton);
		return browseButton;
	}

	@Override
	public Binding createBinding(DetailProviderParameter p) {
		FormToolkit toolkit = p.getDetailFormToolkit();
		Composite parent = p.getParent();
		EObject target = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();

		Object propertyValueObject = pd.getPropertyValue(target);
		String propertyName = pd.getDisplayName(target);
		String propertyValue = null;
		if (propertyValueObject != null && propertyValueObject instanceof PropertyValueWrapper) {
			PropertyValueWrapper propertyValueWrapper = (PropertyValueWrapper) propertyValueObject;
			Object editableValue = propertyValueWrapper.getEditableValue(target);
			propertyValue = editableValue.toString();
		}

		if (propertyValue == null) {
			propertyValue = "";
		}

		EMFDetailUtils.createLabel(parent, toolkit, target, pd);
		Composite rootComposite = createRootComposite(toolkit, parent);
		Composite hyperlinkAndBrowseComposite = hyperlinkAndBrowseComposite(rootComposite);
		Text hyperlinkTextField = createHyperlinkTextField(hyperlinkAndBrowseComposite, propertyValue);
		@SuppressWarnings("unused")
		Button browseButton = createBrowseButton(hyperlinkAndBrowseComposite, hyperlinkTextField);
		Button openButton = createOpenButton(rootComposite, propertyName, hyperlinkTextField);
		hyperlinkAndBrowseComposite.setData(BUTTON_KEY, openButton);
		openButton.setEnabled(!hyperlinkTextField.getText().trim().equals(""));

		StringifierUpdateValueStrategy targetToModel = new StringifierUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		StringifierUpdateValueStrategy modelToTarget = new StringifierUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		ISWTObservableValue observeText = SWTObservables.observeText(hyperlinkTextField, SWT.FocusOut);

		EMFDetailUtils.bindValidatorDecoration(p, hyperlinkTextField);
		EMFDetailUtils.bindControlViability(p, new Control[] { browseButton, openButton, hyperlinkTextField });
		Binding binding = EMFDetailUtils.bindEMFUndoable(p, observeText, targetToModel, modelToTarget);
		EMFDetailUtils.bindTextModifyUndoable(hyperlinkTextField, target, propertyName);
		targetToModel.setBinding(binding);
		return binding;
	}

	private static String convertTextToHyperlinkTextField(String inputString) {
		String result = inputString.replaceAll("\\\\", "/");
		result = result.replaceAll(" ", "%20");
		if (!result.startsWith("ftp") && !result.startsWith("http")) {
			// it must be a file...
			result = "file://" + result;
		}

		return result;
	}

	private static String convertHyperlinkTextFieldToText(String inputString) {
		String result = inputString;
		if (inputString.startsWith("file://")) {
			result = inputString.replaceFirst("file://", "");
			result = result.replaceAll("%20", " ");
		}

		if (!result.startsWith("file://") && result.startsWith("file:/")) {
			result = result.replaceFirst("file:/", "file:///");
		}

		if (result.startsWith("/") && !result.startsWith("///")) {
			result.replaceFirst("/", "///");
		}
		return result;
	}
}
