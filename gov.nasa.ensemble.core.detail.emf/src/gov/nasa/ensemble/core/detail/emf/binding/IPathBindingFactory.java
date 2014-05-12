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
import gov.nasa.ensemble.common.ui.preferences.DirectoryFieldEditor;
import gov.nasa.ensemble.common.ui.preferences.ExtendedStringButtonFieldEditor;
import gov.nasa.ensemble.common.ui.preferences.FileFieldEditor;
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.ButtonFieldEditor;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.detail.emf.util.IDirectoryFieldEditor;
import gov.nasa.ensemble.core.detail.emf.util.IFileFieldEditor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class IPathBindingFactory extends BindingFactory {

	private static final String ANNOTATION_DETAIL_TYPE = "type";
	private static final String ANNOTATION_DETAIL_EXTENSIONFILTER = "extensionFilter";
	private static final String ANNOTATION_DETAIL_ENABLE_OPEN = "enableOpen";
	private static final String ANNOTATION_DETAIL_ENABLE_NEW_FILE = "enableNewFile";

	private final static String OPEN_BUTTON = "OPEN_BUTTON";
	
	private URI baseURI;

	public static Image OPEN_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/open.png").createImage();

	@Override
	public Binding createBinding(DetailProviderParameter p) {
		FormToolkit toolkit = p.getDetailFormToolkit();
		Composite parent = p.getParent();
        parent.setBackgroundMode(SWT.INHERIT_FORCE);
		final Composite rootComposite = new Composite(parent, SWT.NONE);
		{
			GridLayoutFactory.fillDefaults().numColumns(2).margins(0, 0).applyTo(rootComposite);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(2, 1).applyTo(rootComposite);
		}
		Composite composite = new Composite(rootComposite, SWT.NONE);
		{
			GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(composite);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(composite);
		}
		EObject target = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		boolean isEditable = pd.canSetProperty(target);
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		if (feature == null) {
			return null;
		}
		String labelText = EMFDetailUtils.getDisplayName(target, pd);
		baseURI = target.eResource().getURI();
		String preferenceName = (feature.getName() == null) ? "" : feature.getName();
		String type = EMFUtils.getAnnotation(feature, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_TYPE);
		String extensionFilter = EMFUtils.getAnnotation(feature, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_EXTENSIONFILTER);
		boolean enableNewFile = Boolean.parseBoolean(EMFUtils.getAnnotation(feature, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_ENABLE_NEW_FILE));
		List<String> fileExtensionFilter;
		if (extensionFilter != null) {
			fileExtensionFilter = prefixExtensions(Collections.unmodifiableList(Arrays.asList(extensionFilter.split("\\s*,\\s*"))), "*.");
		} else {
			fileExtensionFilter = Collections.emptyList();
		}
		ExtendedStringButtonFieldEditor fileFieldEditor;
		if ("IFile".equalsIgnoreCase(type)) {
			IFileFieldEditor editor = new IFileFieldEditor(preferenceName, labelText, composite);
			editor.setBaseURI(baseURI);
			if ((fileExtensionFilter != null) && (fileExtensionFilter.size() > 0)) {
				editor.setFileExtensions(fileExtensionFilter.toArray(new String[fileExtensionFilter.size()]));
			}
			if (enableNewFile) {
				editor.setFileChooserStyle(SWT.SAVE);
			}
			fileFieldEditor = editor;
		} else if ("IPath".equalsIgnoreCase(type) || "IContainer".equalsIgnoreCase(type) || "IFolder".equalsIgnoreCase(type)) {
			IDirectoryFieldEditor editor = new IDirectoryFieldEditor(preferenceName, labelText, composite);
			editor.setBaseURI(baseURI);
			fileFieldEditor = editor;
		} else if ("Directory".equalsIgnoreCase(type)) {
			fileFieldEditor = new DirectoryFieldEditor(preferenceName, labelText, composite);
		} else {
			if (!isEditable) {
				ButtonFieldEditor editor = new ButtonFieldEditor(preferenceName, labelText, composite);
				fileFieldEditor = editor;
			}
			else {
				FileFieldEditor editor = new FileFieldEditor(preferenceName, labelText, composite);
				if (enableNewFile) {
					editor.setFileChooserStyle(SWT.SAVE);
				}
				if ((fileExtensionFilter != null) && (fileExtensionFilter.size() > 0)) {
					editor.setFileExtensions(fileExtensionFilter.toArray(new String[fileExtensionFilter.size()]));
				}
				fileFieldEditor = editor;
			}
		}
		fileFieldEditor.setChangeButtonText("...");
		boolean editable = pd.canSetProperty(target);
		fileFieldEditor.setEnabled(editable, composite); // disables the entire row
		fileFieldEditor.getLabelControl(composite).setEnabled(true); // re-enables the label
		
		Label label = fileFieldEditor.getLabel();
		EMFDetailUtils.addDescriptionTooltip(pd, target, label);
		Control control = fileFieldEditor.getTextControl(composite);
		toolkit.adapt(control, true, true);
		EMFDetailUtils.addDescriptionTooltip(pd, target, control);
		Button changeButton = fileFieldEditor.getChangeButton();
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).hint(Activator.CONTROL_WIDTH_HINT, SWT.DEFAULT).applyTo(control);
		EMFDetailUtils.bindValidatorDecoration(p, control);
		EMFDetailUtils.bindControlViability(p, new Control[] { control, changeButton });
		IObservableValue targetObservableValue = observeText(control);
		PathUpdateValueStrategy targetToModel = new PathUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		PathUpdateValueStrategy modelToTarget = new PathUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		final Binding binding = EMFDetailUtils.bindEMFUndoable(p, targetObservableValue, targetToModel, modelToTarget);
		EMFDetailUtils.bindTextModifyUndoable((Text)control, target, labelText);
		fileFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.VALUE)) {
					binding.updateTargetToModel();
					Button openButton = (Button) rootComposite.getData(OPEN_BUTTON);
					if (openButton != null)
						openButton.setEnabled(!event.getNewValue().toString().trim().equals(""));
				}
			}
		});

		if ("File".equalsIgnoreCase(type) || "IFile".equalsIgnoreCase(type)) {
			String enableOpen = EMFUtils.getAnnotation(feature, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_ENABLE_OPEN);
			if (Boolean.parseBoolean(enableOpen)) {
				Button openButton = createOpenButton(rootComposite, labelText, (Text)control);
				openButton.setEnabled(!fileFieldEditor.getStringValue().trim().equals(""));
				rootComposite.setData(OPEN_BUTTON, openButton);
			}
		}

		return binding;
	}

	/**
	 * This is its own method so that a 3.5-based subclass can override it to use the version of observeText which takes an array of event types.
	 */
	protected IObservableValue observeText(Control control) {
		return SWTObservables.observeText(control, new int[] { SWT.FocusOut, SWT.DefaultSelection });
	}

	/**
	 * Returns a new unmodifiable list containing prefixed versions of the extensions in the given list.
	 */
	private static List<String> prefixExtensions(List<String> extensions, String prefix) {
		List<String> result = new ArrayList<String>();
		for (String extension : extensions) {
			result.add(prefix + extension);
		}
		return Collections.unmodifiableList(result);
	}

	private Button createOpenButton(Composite rootComposite, String propertyName, final Text textField) {
		final Button openButton = new Button(rootComposite, SWT.PUSH);
		openButton.setToolTipText("Open " + propertyName);
		openButton.setImage(OPEN_ICON);

		openButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		openButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public synchronized void widgetSelected(SelectionEvent event) {
				IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				java.net.URI uri = null;
				File file = new File(textField.getText());
				if (!file.exists()) {
					uri = getURI(textField.getText());
					if (uri == null)
						return;
				} else {
					uri = file.toURI();
				}

				final IFileStore fileStore = EFS.getLocalFileSystem().getStore(uri);
				FileStoreEditorInput input = new FileStoreEditorInput(fileStore);
				try {
					// let's then try to open it with eclipse default text editor
					Object editor = workbenchPage.openEditor(input, "org.eclipse.ui.DefaultTextEditor");
					if (editor == null)
						throw new PartInitException("let's then try to open it with eclipse default text editor");
				} catch (PartInitException e) {
					// let the system decide on which editor to open
					try {
						workbenchPage.openEditor(input, IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
					} catch (PartInitException partInitException) {
						LogUtil.error(partInitException);
					}
				}
			}
			
			protected java.net.URI getURI(String text) {
				URI uri = URI.createURI(text).resolve(baseURI);
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				String platformString = uri.toPlatformString(true);
				return root.getFile(new Path(platformString)).getLocationURI();
			}
		});

		return openButton;
	}
}
