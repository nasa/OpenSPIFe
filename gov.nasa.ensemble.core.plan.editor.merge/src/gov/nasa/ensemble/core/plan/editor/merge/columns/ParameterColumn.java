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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.image.CheckboxImages;
import gov.nasa.ensemble.common.ui.image.CheckboxImages.Mode;
import gov.nasa.ensemble.common.ui.type.editor.CocoaCompatibleTextCellEditor;
import gov.nasa.ensemble.common.ui.type.editor.StringifierCellEditor;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.ParameterStringifierUtils;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.MergePlugin;
import gov.nasa.ensemble.core.plan.editor.merge.editors.ChoicesCellEditor;
import gov.nasa.ensemble.core.plan.editor.merge.editors.EEnumComboBoxCellEditor;
import gov.nasa.ensemble.core.plan.editor.merge.editors.MultiselectCellEditor;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EChoice;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;
import gov.nasa.ensemble.emf.model.common.CommonPackage;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class ParameterColumn<T> extends AbstractMergeColumn<ParameterFacet<T>> {

	private static final ParameterDescriptor INSTANCE = ParameterDescriptor.getInstance();
	
	private static CheckboxImages checkboxImages = null;
	private static CheckboxImages getCheckboxImages() {
		if (checkboxImages == null) {
			Shell shell = WidgetUtils.getShell();
			checkboxImages = new CheckboxImages(MergePlugin.getDefault().getImageRegistry(), shell);
		}
		return checkboxImages;
	}
	
	private final Comparator<ParameterFacet<T>> parameterComparator = new ParameterComparator();
	private final String displayName;

	public ParameterColumn(IMergeColumnProvider provider, String displayName) {
		super(provider, displayName, 30);
		this.displayName = displayName;
	}

	public ParameterColumn(IMergeColumnProvider provider, String displayName, int defaultWidth) {
		super(provider, displayName, defaultWidth);
		this.displayName = displayName;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		if (feature instanceof EStructuralFeature) {
			String displayName = INSTANCE.getDisplayName((EStructuralFeature)feature);
			return (this.displayName.equals(displayName));
		}
		return false;
	}
	
	@Override
	public ParameterFacet<T> getFacet(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement planElement = (EPlanElement)element;
			EObject object = planElement.getData();
			if (object != null) {
				EStructuralFeature attribute = getFeature(object);
				if (attribute != null) {
					@SuppressWarnings("unchecked")
					T value = (T)object.eGet(attribute);
					return new ParameterFacet<T>(object, attribute, value);
				}
			}
		}
		return null;
	}

	private Map<EClass, EStructuralFeature> classAttribute = new LinkedHashMap<EClass, EStructuralFeature>();
	protected EStructuralFeature getFeature(EObject object) {
		EClass klass = object.eClass();
		if (classAttribute.containsKey(klass)) {
			return classAttribute.get(klass);
		}
		EStructuralFeature result = null;
		LinkedList<EClass> classes = new LinkedList<EClass>();
		classes.add(klass);
		while (!classes.isEmpty()) {
			EClass superclass = classes.removeFirst();
			EList<EStructuralFeature> features = superclass.getEStructuralFeatures();
			for (EStructuralFeature feature : features) {
				String displayName = INSTANCE.getDisplayName(feature);
				if (displayName.equals(this.displayName)) {
					if (result != null) {
						LogUtil.warn("two EAttributes had the same display name: " + result.getName() + " and " + feature.getName());
					}
					result = feature;
				}
			}
			classes.addAll(superclass.getESuperTypes());
		}
		classAttribute.put(klass, result);
		return result;
	}

	protected boolean isCheckboxType(EClassifier type) {
		if (type == EcorePackage.Literals.EBOOLEAN || type == EcorePackage.Literals.EBOOLEAN_OBJECT) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// facet presentation
	
	@Override
	public String getText(ParameterFacet<T> facet) {
		if (facet == null) {
			return "-";
		}
		EStructuralFeature feature = facet.getFeature();
		EClassifier type = feature.getEType();
		if (isCheckboxType(type)) {
				return null;
		}
		
		if (feature instanceof EAttribute) {
			IStringifier<Object> stringifier = ParameterStringifierUtils.getStringifier((EAttribute)feature);
			return stringifier.getDisplayString(facet.getValue());
		}
		EObject object = facet.getObject();
		IItemLabelProvider labelProvider = EMFUtils.getItemLabelProvider(object, feature);
		return labelProvider == null ? "" : labelProvider.getText(facet.getValue());
	}

	
	@Override
	public Image getImage(ParameterFacet<T> facet) {
		if (facet != null) {
			EStructuralFeature feature = facet.getFeature();
			EClassifier type = feature.getEType();
			if (isCheckboxType(type)) { 
				T value = facet.getValue();
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
			// The following is slow.  If you need this functionality
			// please consider implementing a custom merge column, or
			// use an EMFTreeTableColumn instead.  SPF-6367
//			EObject object = facet.getObject();
//			IItemLabelProvider labelProvider = EMFUtils.getItemLabelProvider(object, feature);
//			Object image = labelProvider.getImage(facet.getValue());
//			if(image instanceof Image) {
//				return (Image) image;
//			}
		}
		return super.getImage(facet);
	}

	
	@Override
	public Color getForeground(ParameterFacet<T> facet) {
		return ColorConstants.darkGreen;
	}

	/*
	 * in place editing
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public CellEditor getCellEditor(Composite parent, ParameterFacet<T> facet) {
		EStructuralFeature feature = facet.getFeature();
		EClassifier type = feature.getEType();
		if (isCheckboxType(type)) {
			return new CheckboxNullCellEditor(parent);
		}
		EPlanElement element = facet.getElement();
		if (feature.isMany()) {
			return new MultiselectCellEditor(parent, element, feature);
		}
		if (type instanceof EEnumImpl) {
			return new EEnumComboBoxCellEditor(parent, (EEnumImpl) type);
		}
		if (feature instanceof EAttributeParameter) {
			EAttributeParameter def = (EAttributeParameter) feature;
			List<EChoice> choices = def.getChoices();
			if (DictionaryUtil.containsOnlyValues(choices)) {
				return new ChoicesCellEditor(parent, def, choices);
			}
		}
		if (feature instanceof EReference) {
			final EReference reference = (EReference) feature;
			ComboBoxViewerCellEditor comboBoxViewerCellEditor = new EReferenceCellEditor(parent, element, reference);
			return comboBoxViewerCellEditor;
		}
		IStringifier<Object> stringifier = null;
		if (feature instanceof EAttribute) {
			stringifier = ParameterStringifierUtils.getStringifier((EAttribute) feature);
		}
		if (stringifier == null) {
			return new CocoaCompatibleTextCellEditor(parent);
		}
		return new StringifierCellEditor(parent, stringifier);
	}
	
	@Override
	public boolean editOnActivate(ParameterFacet<T> facet, IUndoContext undoContext, TreeItem item, int index) {
		EStructuralFeature feature = facet.getFeature();
		EClassifier type = feature.getEType();
		if (isCheckboxType(type)) {
			Boolean value = (Boolean) facet.getValue();
			boolean newValue = (value == null ? true : !value);
			modify(facet, newValue, undoContext);
			return true;
		}
		if (type == CommonPackage.Literals.EURL) {
			URL url = (URL)facet.getValue();
			if ((url == null) || "".equals(url.toString())) {
				MessageDialog.openInformation(new Shell(), "No URL", "No URL is set");
			} else {
				IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
				try {
					IWebBrowser browser = browserSupport.createBrowser("");
					browser.openURL(url);
				} catch (PartInitException e1) {
					LogUtil.error("Failed to INIT part for opening WebBrowser in URLEditor", e1);
				}
			}
			return false;
		}
		return false;
	}
	
	/**
	 * check to see if you actually have permission to modify the priority
	 * depending on your role
	 * Also checks that the field is editable, and that it is simple.
	 */
	@Override
	public boolean canModify(ParameterFacet<T> facet) {
		if (facet == null || facet.getObject() == null) {
			return false;
		}
		EPlanElement element = facet.getElement();
		if (element instanceof EActivity) {
			EActivity activity = (EActivity) element;
			if (activity.isIsSubActivity()) {
				return false;
			}
		}
		if (!PlanEditApproverRegistry.getInstance().canModify(element)) {
			return false;
		}
		EStructuralFeature attribute = facet.getFeature();
		if (!INSTANCE.isEditable(attribute) && !attribute.isMany()) {
			return false;
		}
		IItemPropertyDescriptor descriptor = EMFUtils.getFeatureDescriptor(element, attribute);
		if (descriptor != null) {
			return descriptor.canSetProperty(element);
		}
		return false;
	}
	
	@Override
	public T getValue(ParameterFacet<T> parameter) {
		return parameter.getValue();
	}
	
	@Override
	public void modify(ParameterFacet<T> parameter, Object newValue, IUndoContext undoContext) {
		try {
			@SuppressWarnings("unchecked")
			T value = (T)newValue;
			FeatureTransactionChangeOperation.execute(parameter.getObject(), parameter.getFeature(), value);
		} catch (ThreadDeath td) {
			throw td;
	    } catch (Throwable t) {
	    	LogUtil.error("ParameterColumn.modify", t);
	    	// TODO: feedback on ParseException?
	    }
	}
	
	/*
	 * default sort based on text of facet
	 */
	
	@Override
	public Comparator<ParameterFacet<T>> getComparator() {
		return parameterComparator;
	}

	private static final class CheckboxNullCellEditor extends
			CheckboxCellEditor {
		private CheckboxNullCellEditor(Composite parent) {
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

	/**
	 * Compare first by text, ties broken by checking actual value (if they are strings),
	 * ties broken by using plan element ordering 
	 * 
	 * @author Andrew
	 */
	private class ParameterComparator implements Comparator<ParameterFacet<T>> {
		@Override
		@SuppressWarnings("unchecked")
		public int compare(ParameterFacet<T> facet1, ParameterFacet<T> facet2) {
			if (facet1 == facet2) {
				return 0;
			}
			if (facet1 == null) {
				return -1;
			}
			if (facet2 == null) {
				return 1;
			}
			T value1 = facet1.getValue();
			T value2 = facet2.getValue();
			if (value1 == value2) {
				return 0;
			}
			if (value1 == null) {
				return -1;
			}
			if (value2 == null) {
				return 1;
			}
			if ((value1 instanceof Comparable) && (value2 instanceof Comparable)) {
				return ((Comparable<T>)value1).compareTo(value2);
			}
			if ((value1 instanceof Number) && (value2 instanceof Number)) {
				Number number1 = (Number) value1;
				Number number2 = (Number) value2;
				int result = Double.compare(number1.doubleValue(), number2.doubleValue());
				if (result != 0) {
					return result;
				}
			}
			if (value1 instanceof EEnumLiteral && value2 instanceof EEnumLiteral) {
				int enumNumber1 = ((EEnumLiteral) value1).getValue();
				int enumNumber2 = ((EEnumLiteral) value2).getValue();
				int result = enumNumber1 - enumNumber2;
				if (result != 0) {
					return result;
				}
			}
			String text1 = getText(facet1); 
			String text2 = getText(facet2);
			int result = Policy.getComparator().compare(text1, text2);
			if (result == 0) {
				if ((value1 instanceof String) && (value2 instanceof String)) {
					result = Policy.getComparator().compare(value1, value2);
				}
				if (result == 0) {
					EPlanElement pe1 = facet1.getElement();
					EPlanElement pe2 = facet2.getElement();
					result = PlanUtils.INHERENT_ORDER.compare(pe1, pe2);
				}
			}
			return result;
		}
	}

	public static int getDefaultWidth(EStructuralFeature feature, ParameterDescriptor descriptor) {
		EClassifier type = feature.getEType();
		if(type!=null){
			 Class<?> clazz = type.getInstanceClass();
			if( clazz==String.class ){
				if(descriptor!=null){
					boolean isMultiline=descriptor.isMultiline(feature);
					if (isMultiline) {
						return 150;
					}
				}
				return 60;
			}
		}
		return 50;
	}
	
}
