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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.type.editor.StringTypeEditor;
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.stringifier.URIStrigifier;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class URIBindingFactory extends BindingFactory {

	public static Image BROWSE_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/browse.png").createImage();

	public static Image OPEN_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/open.png").createImage();

	private final static String BUTTON_KEY = "BUTTON_KEY"; // to access UI "open url"
	private final static String BROWSE_BUTTON_KEY = "BROWSE_BUTTON_KEY";

	private static URIStrigifier projectUriStringfier;
	private static URL DEFAULT_URL;
	private static URIConverter uriConverter;

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
				String destination = URIBindingFactory.convertHyperlinkTextFieldToText(hyperlinkTextField.getText());
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
					URI uri = URI.createURI(destination);
					URI normalize = uriConverter.normalize(uri);
					String platformString = normalize.toPlatformString(true);
					IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					if (isProjectRelativePath(destination) || !uri.isFile()) {
						// i.e. destinations starting with "platform:/"
						IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
						try {
							IDE.openEditor(workbenchPage, file);
						} catch (PartInitException e1) {
							// let's then try to open it with eclipse default text editor
							try {
								FileEditorInput input = new FileEditorInput(file);
								workbenchPage.openEditor(input, "org.eclipse.ui.DefaultTextEditor");
							} catch (PartInitException partInitException2) {
								logger.error(partInitException2);
							}
						}
					} else {
						// i.e. system destinations starting with "file:/"
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
			}

			private Logger logger = Logger.getLogger(URIBindingFactory.class);
		});

		// add a key listener to set the focus on this go button when
		// the ENTER key is pressed while editing the hyperlinkTextField
		hyperlinkTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					URL url = null;
					String originalText = hyperlinkTextField.getText();
					try {
						String text = "";
						// Check for project relative paths
						if (isProjectRelativePath(originalText)) {
							// Project relative converted into Workspace relative path.
							text = convertToPlatformPath(originalText);
						}
						url = new URL(text);
					} catch (MalformedURLException e1) {
						// do nothing
					}
					String displayString = "";
					if (url != null) {
						displayString = projectUriStringfier.getDisplayString(toURI(url));
					}
					hyperlinkTextField.setText(displayString);
					if (openButton.isEnabled()) {
						openButton.setFocus();
					} else {
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
				String text = hyperlinkTextField.getText().trim();
				button.setEnabled(!text.isEmpty());
			}

			@Override
			public void focusGained(FocusEvent e) {
				Button button = (Button) hyperlinkAndBrowseComposite.getData(BUTTON_KEY);
				String originalText = hyperlinkTextField.getText().trim();
				URL url = null;
				try {
					String text = originalText;
					// Check for project relative paths
					if (isProjectRelativePath(originalText)) {
						// Project relative converted into Workspace relative path.
						text = convertToPlatformPath(originalText);
					}
					url = new URL(text);
				} catch (Throwable t) {
					// do nothing
				}

				if (url != null) {
					URI uri = toURI(url);
					stringTypeEditor.setObject(uri);
				}

				button.setEnabled(!originalText.isEmpty());
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
		final StringTypeEditor stringTypeEditor = new StringTypeEditor(projectUriStringfier, hyperlinkTextField);

		if (DEFAULT_URL == null) {
			try {
				DEFAULT_URL = new URL("file://c:/procedure.txt");
				stringTypeEditor.setObject(toURI(DEFAULT_URL));
			} catch (MalformedURLException e1) {
				// do nothing
			}
		}

		hyperlinkTextField.setText(propertyValue);
		GridData hyperlinkTextFieldGridData = new GridData(SWT.FILL, SWT.CENTER, grabExcessHorizontalSpace, grabExcessVerticalSpace);
		hyperlinkTextField.setLayoutData(hyperlinkTextFieldGridData);

		// update the focus of the open url button

		hyperlinkTextField.addFocusListener(createHyperlinkTextFieldFocusListener(hyperlinkAndBrowseComposite, hyperlinkTextField, stringTypeEditor));

		hyperlinkTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String originalText = hyperlinkTextField.getText().trim();
				Button button = (Button) hyperlinkAndBrowseComposite.getData(BUTTON_KEY);
				URL url = null;
				try {
					String text = originalText;
					// Check for project relative paths
					if (isProjectRelativePath(originalText)) {
						// Project relative converted into Workspace relative path.
						text = convertToPlatformPath(originalText);
					}
					url = new URL(text);
				} catch (Throwable t) {
					// do nothing
				}
				if (url != null) {
					stringTypeEditor.setObject(toURI(url));
				}
				button.setEnabled(!originalText.isEmpty());
			}

		});
		return hyperlinkTextField;
	}

	/**
	 * The button that will allow the user to browse the file system to choose a new url.
	 */
	private Button createBrowseButton(final Composite hyperlinkAndBrowseComposite, final Text hyperlinkTextField) {
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
				String text = null;
				FileDialog fileDialog = new FileDialog(browseButton.getShell());
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				String workspaceLocation = root.getLocation().toOSString();
				fileDialog.setFilterPath(workspaceLocation);
				String selectedFile = fileDialog.open();
				if (selectedFile != null) {
					File file = new File(selectedFile);
					IPath location = Path.fromOSString(file.getAbsolutePath());
					IFile iFile = root.getFileForLocation(location);
					if (selectedFile.startsWith(workspaceLocation) && iFile != null && iFile.getProject() != null && iFile.getProject().exists()) {
						// Project Relative
						String trimmed = selectedFile.substring(workspaceLocation.length());
						IPath path = new Path(trimmed).removeFirstSegments(1);
						String projectRelativePath = path.toOSString();
						text = ProjectURIConverter.createProjectURI(projectRelativePath).toString();
					} else {
						text = convertTextToHyperlinkTextField(selectedFile);
					}
					hyperlinkTextField.setText(text);
					hyperlinkTextField.setFocus();
					Button button = (Button) hyperlinkAndBrowseComposite.getData(BUTTON_KEY);
					button.setEnabled(!text.isEmpty());
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

		uriConverter = target.eResource().getResourceSet().getURIConverter();
		projectUriStringfier = new URIStrigifier(uriConverter);
		EMFDetailUtils.createLabel(parent, toolkit, target, pd);
		Composite rootComposite = createRootComposite(toolkit, parent);
		Composite hyperlinkAndBrowseComposite = hyperlinkAndBrowseComposite(rootComposite);
		Text hyperlinkTextField = createHyperlinkTextField(hyperlinkAndBrowseComposite, propertyValue);
		Button browseButton = createBrowseButton(hyperlinkAndBrowseComposite, hyperlinkTextField);
		Button openButton = createOpenButton(rootComposite, propertyName, hyperlinkTextField);
		hyperlinkAndBrowseComposite.setData(BUTTON_KEY, openButton);
		openButton.setEnabled(!hyperlinkTextField.getText().trim().equals(""));

		StringifierUpdateValueStrategy targetToModel = new ProjectUrlUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		StringifierUpdateValueStrategy modelToTarget = new ProjectUrlUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
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
			result = "file:" + result;
		}
		return result;
	}

	private static String convertHyperlinkTextFieldToText(String inputString) {
		String result = inputString;
		if (inputString.startsWith("project:/") || inputString.startsWith("platform:/")) {
			return inputString;
		}
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

	private String convertToPlatformPath(String projectPath) {
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editorPart != null) {
			IFileEditorInput editorInput = (IFileEditorInput) editorPart.getEditorInput();
			IFile editorFile = editorInput.getFile();
			IProject activeProject = editorFile.getProject();
			if (activeProject != null && activeProject.exists()) {
				URI uri = URI.createURI(projectPath);
				URI normalize = uriConverter.normalize(uri);
				return normalize.toString();
			} else {
				LogUtil.warn("Could not get active project. Failed to convert project relative path into workspace path: " + projectPath);
			}
		}
		return projectPath;
	}

	private boolean isProjectRelativePath(String path) {
		return path.trim().startsWith("project:/");
	}

	private URI toURI(URL url) {
		String string = url.toString();
		if (string.endsWith(":"))
			string += "//"; // URI validation code won't tolerate scheme-only but URL will
		return URI.createURI(string);
	}

	private final class ProjectUrlUpdateValueStrategy extends StringifierUpdateValueStrategy {
		private ProjectUrlUpdateValueStrategy(int updatePolicy) {
			super(updatePolicy);
		}

		@Override
		protected IConverter createConverter(Object fromType, Object toType) {
			if (fromType == String.class && toType instanceof EAttribute) {
				return new Converter(fromType, toType) {
					@Override
					public Object convert(Object fromObject) {
						try {
							return projectUriStringfier.getJavaObject((String) fromObject, null);
						} catch (ParseException e) {
							return null;
						}
					}
				};
			} else if (toType == String.class && fromType instanceof EAttribute) {
				return new Converter(fromType, toType) {
					@Override
					public String convert(Object fromObject) {
						return projectUriStringfier.getDisplayString((URI) fromObject);
					}
				};
			} else {
				return super.createConverter(fromType, toType);
			}
		}

		@Override
		protected IValidator createValidator(Object fromType, Object toType) {
			if (fromType == String.class && toType instanceof EAttribute) {
				return new IValidator() {
					@Override
					public IStatus validate(Object value) {
						try {
							projectUriStringfier.getJavaObject((String) value, null);
							return Status.OK_STATUS;
						} catch (ParseException e) {
							return Status.CANCEL_STATUS;
						}
					}
				};
			} else if (toType == String.class && fromType instanceof EAttribute) {
				return new IValidator() {
					@Override
					public IStatus validate(Object value) {
						return Status.OK_STATUS;
					}
				};
			} else {
				return super.createValidator(fromType, toType);
			}
		}

		@Override
		protected IStringifier<?> getStringifier(EDataType eDataType) {
			return projectUriStringfier;
		}
	}

}