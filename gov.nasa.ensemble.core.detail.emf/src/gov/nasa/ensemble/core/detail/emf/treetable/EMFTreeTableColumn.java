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
package gov.nasa.ensemble.core.detail.emf.treetable;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.image.CheckboxImages;
import gov.nasa.ensemble.common.ui.image.CheckboxImages.Mode;
import gov.nasa.ensemble.common.ui.treetable.AbstractTreeTableColumn;
import gov.nasa.ensemble.common.ui.type.editor.StringifierCellEditor;
import gov.nasa.ensemble.core.detail.emf.binding.PropertyDescriptorUpdateOperation;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.detail.emf.util.LabelProviderWrapper;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.operations.IWorkbenchOperationSupport;

public class EMFTreeTableColumn<T extends EObject> extends AbstractTreeTableColumn<T> {
	
	protected final IItemPropertyDescriptor itemPropertyDescriptor;
	private static CheckboxImages checkboxImages = null;
	
	public EMFTreeTableColumn(IItemPropertyDescriptor itemPropertyDescriptor, String headerName, int defaultWidth) {
		this(headerName, defaultWidth, itemPropertyDescriptor);
	}
	
	public EMFTreeTableColumn(String headerName, int defaultWidth, IItemPropertyDescriptor itemPropertyDescriptor) {
		super(headerName, defaultWidth);
		this.itemPropertyDescriptor = itemPropertyDescriptor;
	}
	
	@Override
	public int getAlignment() {
		return SWT.LEFT;
	}
	
	protected static CheckboxImages getCheckboxImages() {
		if (checkboxImages == null) {
			Shell shell = WidgetUtils.getShell();
			checkboxImages = new CheckboxImages(new ImageRegistry(), shell); //WHA?
		}
		return checkboxImages;
	}	
	
	public IItemPropertyDescriptor getItemPropertyDescriptor() {
		return itemPropertyDescriptor;
	}
	
	@Override
	public Image getImage(T facet) {
		Object value = getValue(facet);
		if(value instanceof Boolean) {
			boolean enabled = canModify(facet);
			Boolean b = (Boolean) value;
			Mode mode = Mode.INDETERMINATE;
			if (Boolean.TRUE.equals(b)) {
				mode = Mode.CHECKED;
			} else if (Boolean.FALSE.equals(b)) {
				mode = Mode.UNCHECKED;
			}
			return getCheckboxImages().getCheckboxImage(mode, enabled);
		}
		return super.getImage(facet);
	}
	
	@Override
	public CellEditor getCellEditor(Composite parent, T facet) {
		Object value = getValue(facet);
		if(value instanceof Boolean) {
			return new CheckboxNullCellEditor(parent);
		}

		EObject eObject = getEObject(facet);
		return createPropertyEditor(parent, (T)eObject);
	}	
	
	protected static final class CheckboxNullCellEditor extends
	CheckboxCellEditor {
		protected CheckboxNullCellEditor(Composite parent) {
			super(parent);
		}

		@Override
		protected void doSetValue(Object value) {
			if (value == null) {
				value = Boolean.FALSE; // Checkbox editor doesn't like null
			}
			super.doSetValue(value);
		}
	}
	
	protected ILabelProvider getEditLabelProvider(Object object) {
		return new LabelProviderWrapper(itemPropertyDescriptor.getLabelProvider(object));
	}
	
	/**
	 * This returns the cell editor that will be used to edit the value of this
	 * property. This default implementation determines the type of cell editor
	 * from the nature of the structural feature.
	 */
	public CellEditor createPropertyEditor(Composite parent, final T object) {
		if (!itemPropertyDescriptor.canSetProperty(object)) {
			return null;
		}

		CellEditor result = null;
		Object genericFeature = itemPropertyDescriptor.getFeature(object);
		final ILabelProvider labelProvider = getEditLabelProvider(object);
		final String displayName = itemPropertyDescriptor.getDisplayName(object);
		if (genericFeature instanceof EReference[]) {
			result = new ExtendedComboBoxCellEditor(
					parent,
					new ArrayList<Object>(itemPropertyDescriptor.getChoiceOfValues(object)),
					labelProvider,
					itemPropertyDescriptor.isSortChoices(object));
		} else if (genericFeature instanceof EStructuralFeature) {
			final EStructuralFeature feature = (EStructuralFeature) genericFeature;
			final EClassifier eType = feature.getEType();
			final Collection<?> choiceOfValues = itemPropertyDescriptor.getChoiceOfValues(object);
			if (choiceOfValues != null) {
				if (itemPropertyDescriptor.isMany(object)) {
					boolean valid = true;
					for (Object choice : choiceOfValues) {
						if (!eType.isInstance(choice)) {
							valid = false;
							break;
						}
					}

					if (valid) {
						result = new ExtendedDialogCellEditor(parent, labelProvider) {
							@SuppressWarnings("deprecation")
							@Override
							protected Object openDialogBox(Control cellEditorWindow) {
								FeatureEditorDialog dialog = new FeatureEditorDialog(
										cellEditorWindow.getShell(),
										labelProvider,
										object,
										feature.getEType(),
										(List<?>) doGetValue(),
										displayName,
										new ArrayList<Object>(choiceOfValues),
										itemPropertyDescriptor.isMultiLine(object),
										itemPropertyDescriptor.isSortChoices(object));
								dialog.open();
								return dialog.getResult();
							}
						};
					}
				}
				
				if (result == null) {
					result = new ExtendedComboBoxCellEditor(
							parent, 
							new ArrayList<Object>(choiceOfValues),
							labelProvider,
							itemPropertyDescriptor.isSortChoices(object)) {

						@Override
						public void doSetFocus() {
							final CCombo combo = (CCombo)getControl();
							if (!combo.isDisposed()) {
								String text = combo.getText();
								if (text.length() == 0 && CommonUtils.isWSCocoa()) {
									combo.getDisplay().timerExec(1000, new Runnable() {
										@Override
										public void run() {
											focusIt(combo);
										}
									});
								} else {
									focusIt(combo);
								}
							}	
						}
						
						private void focusIt(final CCombo combo) {
							if (!combo.isDisposed()) {
								combo.setFocus();
								combo.setListVisible(true);
							}
						}
					};
				}
			} else if (eType instanceof EDataType) {
				result = getEDataTypeCellEditor(parent, object, result, eType);
			}
		}
		return result;
	}	
	
	protected CellEditor getEDataTypeCellEditor(Composite parent,
			final T object, CellEditor result, final EClassifier eType) {
		Object genericFeature = itemPropertyDescriptor.getFeature(object);
		final ILabelProvider labelProvider = getEditLabelProvider(object);
		final EStructuralFeature feature = (EStructuralFeature) genericFeature;
		final String displayName = itemPropertyDescriptor.getDisplayName(object);
		EDataType eDataType = (EDataType) eType;
		if (eDataType.isSerializable()) {
			if (itemPropertyDescriptor.isMany(object)) {
				result = new ExtendedDialogCellEditor(parent, labelProvider) {
					@SuppressWarnings("deprecation")
					@Override
					protected Object openDialogBox(Control cellEditorWindow) {
						FeatureEditorDialog dialog = new FeatureEditorDialog(
								cellEditorWindow.getShell(),
								labelProvider,
								object,
								feature.getEType(),
								(List<?>) doGetValue(),
								displayName,
								null,
								itemPropertyDescriptor.isMultiLine(object),
								itemPropertyDescriptor.isSortChoices(object));
						dialog.open();
						return dialog.getResult();
					}
				};
			} else if (eDataType.getInstanceClass() == Boolean.class
					|| eDataType.getInstanceClass() == Boolean.TYPE) {
				result = new CheckboxCellEditor(parent) {
					@Override
					protected void doSetValue(Object value) {
						if (value == null) {
							value = Boolean.FALSE; // Checkbox editor doesn't like null
						}
						super.doSetValue(value);
					}
				};
			} else {
				IStringifier stringifier = getStringifier(object);
				if (stringifier != null) {
					return new StringifierCellEditor(parent, stringifier);
				}
			}
		}

		return result;
	}
	
	protected IStringifier getStringifier(EObject facet) {
		Object feature = itemPropertyDescriptor.getFeature(facet);
		if (feature instanceof EAttribute) {
			EAttribute eAttribute = (EAttribute)feature;
			IStringifier<?> stringifier = EMFUtils.getStringifier(eAttribute);
			return stringifier;
		}
		return null;
	}
	
	@Override
	public boolean needsUpdate(Object feature) {
		try {
			return feature == itemPropertyDescriptor.getFeature(null);
		} catch (Exception e) {
			// silence
		}
		return false;
	}
	
	@Override
	public String getToolTipText(T facet) {
		return itemPropertyDescriptor.getDescription(facet);
	}		
	

	
	@Override
	public T getFacet(Object element) {
		return (T) element;
	}

	@Override
	public boolean editOnActivate(T facet, IUndoContext undoContext, TreeItem item, int index) {
		Object feature = itemPropertyDescriptor.getFeature(facet);
		if (feature instanceof EAttribute) {
			EDataType eDataType = ((EAttribute)feature).getEAttributeType();
			if (eDataType != null
					&& (eDataType.getInstanceClass() == Boolean.class
						|| eDataType.getInstanceClass() == Boolean.TYPE)) {
				Boolean value = (Boolean) getValue(facet);
				boolean newValue = (value == null ? true : !value);
				modify(facet, newValue, undoContext);
				return true;
			}
		}
		return false;
	}	
	
	protected EObject getEObject(T facet) {
		return facet;
	}
	
	@Override
	public boolean canModify(T facet) {
		if(facet == null) {
			return false;
		}

		EObject object = getEObject(facet);
		return itemPropertyDescriptor.canSetProperty(object);
	}	

	@Override
	public String getText(T facet) {
		if(facet == null) {
			return null;
		}
		Object value = getValue(facet);
		IStringifier stringifier = null;
		if(value instanceof List) {
			//TODO: implement a list stringifier?
			return String.valueOf(((List)value).size() + " items");
		} else if(value instanceof Boolean) {
			return "";
		} else if (value == null) {
			return "—";
		} else {
			stringifier = getStringifier(getEObject(facet));
		}
		if (stringifier != null) {
			try {
				String displayString = stringifier.getDisplayString(value);
				return displayString == null ? "" : displayString;
			} catch (Exception e) {
				// fall through and use the item provider method
			}
		}
		EObject eObject = getEObject(facet);
		return itemPropertyDescriptor.getLabelProvider(eObject).getText(value);
	}
	
	@Override
	public void modify(T facet, Object value, IUndoContext undoContext) {
		IUndoableOperation operation = new PropertyDescriptorUpdateOperation("Set value", facet, itemPropertyDescriptor, value);
		operation = EMFDetailUtils.addContributorOperations(operation, facet, itemPropertyDescriptor, value);
		operation.addContext(undoContext);
		IWorkbenchOperationSupport operationSupport = PlatformUI.getWorkbench().getOperationSupport();
		IOperationHistory operationHistory = operationSupport.getOperationHistory();
		try {
			IStatus execute = operationHistory.execute(operation, null, null);
			if(execute.matches(IStatus.ERROR)) {
				throw new ExecutionException(execute.getMessage());
			}
		} catch (ExecutionException e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	public Object getValue(T facet) {
		if(facet == null) {
			return "";
		}
		return EMFUtils.getPropertyValue(itemPropertyDescriptor, facet);
	}

}
