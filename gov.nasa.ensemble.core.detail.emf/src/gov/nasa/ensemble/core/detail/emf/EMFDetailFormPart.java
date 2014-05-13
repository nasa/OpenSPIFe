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
package gov.nasa.ensemble.core.detail.emf;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.collections.AutoListMap;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.detail.DetailUtils;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.PropertyDescriptorContributor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor.OverrideableCommandOwner;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class EMFDetailFormPart extends AbstractFormPart implements IEMFDetailFormPart {

	private static List<PropertyDescriptorContributor> PROPERTY_DESCRIPTOR_CONTRIBUTOR = ClassRegistry.createInstances(PropertyDescriptorContributor.class);

	/**
	 * Hidden flag, if true, the feature is not displayed. As an alternative, EMF Model can set 'Property Type' to 'None'
	 */
	public static final String ANNOTATION_DETAIL_HIDDEN = "hidden";
	
	/**
	 * Inspects references within the same scope as the containing object. Setting this value to 'true' would display the contents
	 * of the reference(s).
	 */
	public static final String ANNOTATION_DETAIL_INSPECT_REFERENCE = "inspectReference";

	/**
	 * Implement reference as a table widget
	 */
	private static final String ANNOTATION_DETAIL_TABLE = "table";
	
	/**
	 * Reflectively inspects the EReference by providing a ReflectiveItemProvider. This annotation has no effect unless
	 * 'inspectReference' is true.
	 */
	public static final String ANNOTATION_DETAIL_INSPECT_REFLECTIVELY = "inspectReflectively";
	
	private DataBindingContext dataBindingContext = new EMFDataBindingContext();
	private Composite composite;
	private EObject formInput;
	
	private final List<IItemPropertyDescriptor> propertyDescriptors;
	private final FormToolkit toolkit;
	private final boolean showTitle;
	private final ISelectionProvider selectionProvider;

	private ScrolledForm scrolledForm;
	private ILabelProvider labelProvider;
	private ILabelProviderListener labelProviderListener;
		
	private static PropertyDescriptorSorter sorter;
	
	public EMFDetailFormPart(FormToolkit toolkit, List<IItemPropertyDescriptor> propertyDescriptors, boolean showTitle, ISelectionProvider selectionProvider) {
		this.toolkit = toolkit;
		this.propertyDescriptors = propertyDescriptors;
		this.showTitle = showTitle;
		this.selectionProvider = selectionProvider;
    }

	@Override
	public void initialize(IManagedForm form) {
		super.initialize(form);

		scrolledForm = form.getForm();
		createControl(scrolledForm.getBody());
		if (showTitle) {
			initializeTitleSeparator(scrolledForm);
		}
	}

	public void createControl(Composite parent) {
		composite = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
	}

	public Composite getComposite() {
		return composite;
	}

	private void initializeTitleSeparator(ScrolledForm scrolledForm) {
		Composite separator = toolkit.createCompositeSeparator(composite);
		GridData separatorData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		separatorData.heightHint = 2;
		separator.setLayoutData(separatorData);
    }

	@Override
	public void dispose() {
		Composite composite = this.getComposite();
		if(composite != null && !composite.isDisposed() && composite.isVisible()) {
			composite.setVisible(false);
		}
		super.dispose();
		dataBindingContext.dispose();
		if (labelProvider != null) {
			if (labelProviderListener != null) {
				labelProvider.removeListener(labelProviderListener);
			}
			labelProvider.dispose();
		}
		formInput = null;
		scrolledForm = null;
	}

	@Override
	public boolean setFormInput(Object input) {
		if (input instanceof EObject) {
			formInput = (EObject) input;
			if (showTitle) {
				updateTitle();
			}
			buildPropertySection(composite, formInput, propertyDescriptors, null, null);
			return true;
		}
		return super.setFormInput(input);
	}

	private void updateTitle() {
		Image image = null;
		String text = "";
	    EObject input = formInput;
	    if (input != null) {
	    	ILabelProvider provider = getLabelProvider(input);
	    	if (provider != null) {
				image = provider.getImage(input);
				text = provider.getText(input);
				if (text == null) {
					LogUtil.error("null text returned from provider for object: " + provider.getClass() + " " + input);
					text = "";
				}
	    	}
	    }
	    final Image finalImage = image;
	    final String finalText = text;
	    WidgetUtils.runInDisplayThread(scrolledForm, new Runnable() {
	    	@Override
			public void run() {
	    		scrolledForm.setImage(finalImage);
	    		scrolledForm.setText(finalText.replaceAll("&", "&&")); //SWT interprets & as a mnemonic char so must double the ampersands to display on Title
	    	}
	    });
    }

	private ILabelProvider getLabelProvider(final Object input) {
		if (labelProvider == null) {
			labelProvider = CommonUtils.getAdapter(input, ILabelProvider.class);
			if (labelProvider == null) {
				AdapterFactory adapterFactory = EMFUtils.getAdapterFactory(input);
				if (adapterFactory != null) {
					AdapterFactoryLabelProvider adlp = new AdapterFactoryLabelProvider(adapterFactory);
					adlp.setFireLabelUpdateNotifications(true); // why do we need to do this?
					labelProvider = adlp;
				}
			}
			if (labelProvider != null) {
				this.labelProviderListener = new ILabelProviderListener() {
					@Override
					public void labelProviderChanged(LabelProviderChangedEvent event) {
						WidgetUtils.runInDisplayThread(getComposite(), new Runnable() {
							@Override
							public void run() {
								updateTitle();
							}
						});
					}
				};
				this.labelProvider.addListener(labelProviderListener);
			}
		}
		return labelProvider;
	}

	private void buildPropertySection(Composite parent, EObject target, List<IItemPropertyDescriptor> pds, IItemLabelProvider labeler) {
		if (!hasVisibleDescriptors(target, pds))
			return;

		String text = null;
		Image image = null;
		if (labeler != null) {
			text = labeler.getText(target);
			Object imageURL = labeler.getImage(text);
			if (imageURL != null) {
				try {
					image = ExtendedImageRegistry.getInstance().getImage(imageURL);
				} catch (Exception e) {
					LogUtil.error("failed to get image", e);
				}
			}
		}
		buildPropertySection(parent, target, pds, text, image);
	}

	private void buildPropertySection(Composite parent, EObject target, List<IItemPropertyDescriptor> pds, String defaultCategory, Image icon) {
		if (pds.isEmpty()) {
			IDetailProvider provider = EMFUtils.adapt(target, IDetailProvider.class);
			DetailProviderParameter parameter = createDetailProviderParameter(parent, target, null);
			if (provider != null && provider.canCreateBindings(parameter)) {
				provider.createBinding(parameter);
			}
			return;
		}
		List<IItemPropertyDescriptor> uncategorizedPropertyDescriptors = new ArrayList<IItemPropertyDescriptor>();
		Map<String, List<IItemPropertyDescriptor>> categoryToPropertyDescriptors = new AutoListMap<String, IItemPropertyDescriptor>(String.class);
		for (IItemPropertyDescriptor pd : pds) {
			if (isFiltered(target, pd)){
				continue;
			}
			String category = null;
			PropertyDescriptorSorter sorter = getPropertyDescriptorSorter(toolkit, target);
			if(sorter != null) {
				category = sorter.getCategory(target, pd);
			} else {
				category = pd.getCategory(target);
			}
			
			if (category == null) {
				uncategorizedPropertyDescriptors.add(pd);
			} else {
				categoryToPropertyDescriptors.get(category).add(pd);
			}
		}
		if (!uncategorizedPropertyDescriptors.isEmpty()) {
			createCategorySection(parent, target, defaultCategory, icon, uncategorizedPropertyDescriptors);
		}
		for (Map.Entry<String, List<IItemPropertyDescriptor>> entry : categoryToPropertyDescriptors.entrySet()) {
			String category = entry.getKey();
			List<IItemPropertyDescriptor> propertyDescriptors = entry.getValue();
			if (category.startsWith(EMFDetailUtils.UNCATEGORIZED)) {
				category = null;
			}
			createCategorySection(parent, target, category, icon, propertyDescriptors);
		}
	}

	private void createCategorySection(Composite parent, EObject target, String category, Image icon, List<IItemPropertyDescriptor> propertyDescriptors) {
	    Section section = createCategorySection(toolkit, parent, category, icon);
		Composite categorySubSection = (Composite)section.getClient();
	    for (IItemPropertyDescriptor pd : propertyDescriptors) {
	    	try {
	    		createBinding(categorySubSection, target, pd);
	    	} catch (Exception e) {
	    		LogUtil.error(e.getMessage()+": creating binding on "+pd.getFeature(target)+": "+target, e);
	    	}
	    }
    }

	private void createBinding(Composite parent, EObject target, IItemPropertyDescriptor pd) {
		if (pd instanceof OverrideableCommandOwner) {
			EObject owner = EMFDetailUtils.getCommandOwner(pd, target);
			if (owner != null) {
				target = owner;
			}
		}
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		if (feature != null && EMFUtils.testBooleanAnnotation(feature, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_HIDDEN)) {
			return;
		}
		
		IDetailProvider provider = CommonUtils.getAdapter(pd, IDetailProvider.class);
		if (provider == null) {
			provider = EMFUtils.adapt(target, IDetailProvider.class);
		}
		DetailProviderParameter parameter = createDetailProviderParameter(parent, target, pd);
		if (provider != null && provider.canCreateBindings(parameter)) {
			provider.createBinding(parameter);
			return;
		}

		Object propertyValue = pd.getPropertyValue(target);
		if (propertyValue instanceof PropertyValueWrapper) {
			PropertyValueWrapper wrapper = (PropertyValueWrapper) propertyValue;
			List<IItemPropertyDescriptor> propertyDescriptors = wrapper.getPropertyDescriptors(propertyValue);
			if (!propertyDescriptors.isEmpty()) {
				String text = wrapper.getText(propertyValue);
				Object editableValue = wrapper.getEditableValue(propertyValue);
				buildPropertySection(parent, (EObject)editableValue, propertyDescriptors, text, null);
				return;
			}
		}
		
		if (feature instanceof EReference) {
			createReferenceBinding(parent, target, pd, (EReference)feature);
		} else if (feature instanceof EAttribute){
			createAttributeBinding(parent, target, pd, (EAttribute)feature);
		} else if (pd instanceof MultiItemPropertyDescriptor) {
			pd = ((MultiItemPropertyDescriptor)pd).getPrimaryDescriptor();
			if (pd != null)
				createBinding(parent, target, pd);
		}
	}

	private void createReferenceBinding(Composite parent, EObject target, IItemPropertyDescriptor pd, EReference reference) {
	    // Check to see if the EClassifier would like to be displayed
	    // in detail. May want to reserve this for EReference types in
	    // the future, and should probably check if the object has its
	    // own IItemPropertySource
	    if (EMFUtils.testBooleanAnnotation(reference, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_INSPECT_REFERENCE)) {
	    	if (EMFUtils.testBooleanAnnotation(reference, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_INSPECT_REFLECTIVELY)) {
    			ReflectiveItemProviderAdapterFactory ripaf = new ReflectiveItemProviderAdapterFactory();
    			EObject eObject = (EObject)target.eGet(reference);
    			IItemPropertySource source = (IItemPropertySource) ripaf.adapt(eObject, IItemPropertySource.class);
    			if (source != null) {
    				List<IItemPropertyDescriptor> pds = source.getPropertyDescriptors(eObject);
    				String displayName = pd.getDisplayName(eObject);
					buildPropertySection(parent, eObject, pds, displayName, null);
    			}
	    		return;
	    	}
			inspectReferenceFeature(parent, target, reference);
	    } else if (EMFUtils.testBooleanAnnotation(reference, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_TABLE)) {
	    	DetailProviderParameter parameter = createDetailProviderParameter(parent, target, pd);
	    	EMFDetailUtils.TABLE_BINDING_FACTORY.createBinding(parameter);
	    } else {
	    	createEditor(parent, target, pd);
	    }
    }

	private void createAttributeBinding(Composite parent, EObject target, IItemPropertyDescriptor pd, EAttribute attribute) {
	    Binding binding = createEditor(parent, target, pd);
	    if (binding != null) {
	    	dataBindingContext.addBinding(binding);
	    }
    }

	private DetailProviderParameter createDetailProviderParameter(Composite parent, EObject target, IItemPropertyDescriptor pd) {
		DetailProviderParameter p = new DetailProviderParameter();
		p.setDataBindingContext(dataBindingContext);
		p.setDetailFormToolkit(toolkit);
		p.setParent(parent);
		p.setTarget(target);
		p.setPropertyDescriptor(pd);
		p.setFormPart(this);
		p.setSelectionProvider(selectionProvider);
		return p;
	}

	/**
	 * Displays the contents of the EReference into the current details pane.
	 * 
	 * @param parent
	 *            component to add UI contributions to
	 * @param target
	 *            EObject to inspect for references reference
	 * @param feature
	 *            EReference to inspect
	 */
	@Override
	public void inspectReferenceFeature(Composite parent, EObject target, EStructuralFeature feature) {
		if (target instanceof MultiEObject) {
			LogUtil.warnOnce("multiselection is not supported for references within the same scope as the containing object");
			return;
		}
		if (feature.isMany()) {
			Collection<?> references = (Collection<?>) target.eGet(feature);
			for (Object child : references) {
				if (child instanceof EObject) {
					EObject eObject = (EObject) child;
					IItemPropertySource source = EMFUtils.adapt(eObject, IItemPropertySource.class);
					if (source != null) {
						IItemLabelProvider provider = EMFUtils.adapt(eObject, IItemLabelProvider.class);
						buildPropertySection(parent, eObject, source.getPropertyDescriptors(eObject), provider);
					}
				}
			}
		} else {
	    	Object object = target.eGet(feature);
	    	if (object instanceof EObject) {
	    		EObject eObject = (EObject) object;
	    		IItemPropertySource source = EMFUtils.adapt(eObject, IItemPropertySource.class);
	    		if (source != null) {
	    			String displayName = feature.getName();//EMFUtils.getDisplayName(eObject);
	    			IItemPropertySource targetSource = EMFUtils.adapt(target, IItemPropertySource.class);
	    			if (targetSource != null) {
		    			IItemPropertyDescriptor pd = targetSource.getPropertyDescriptor(target, feature);
		    			if (pd != null) {
		    				displayName = pd.getDisplayName(target);
		    			}
	    			}
					buildPropertySection(parent, eObject, source.getPropertyDescriptors(eObject), displayName, null);
	    			return;
	    		}
	    	}
		}
	}

	/**
	 * Creates the editor for the given target's property descriptor.
	 * 
	 * @param parent
	 *            component to contribute UI information to
	 * @param target
	 *            EObject that gets edited
	 * @param pd
	 *            descriptor to create editor for
	 * @return the Binding of the object, this requires disposal
	 */
	private Binding createEditor(final Composite parent, final EObject target, final IItemPropertyDescriptor pd) {
		Object genericFeature = pd.getFeature(target);
		if (genericFeature == null) {
			return null;
		}
		if (genericFeature instanceof EReference[]) {
			Logger.getLogger(EMFDetailFormPart.class).error("cannot create editor for an array of references");
			return null;
		}
		if (genericFeature instanceof EStructuralFeature) {
			// Extensibility point to allow custom binding to be used
			//
			DetailProviderParameter parameter = createDetailProviderParameter(parent, target, pd);
			IBindingFactory bindingFactory = EMFDetailUtils.getBindingFactory(parameter);
			if (bindingFactory != null) {
				Binding binding = bindingFactory.createBinding(parameter);
				if (binding != null) {
					return binding;
				}
			}
		}
		return null;
	}
	
	private synchronized boolean hasVisibleDescriptors(EObject target, List<IItemPropertyDescriptor> pds) {
		for (IItemPropertyDescriptor pd : pds) {
			if (!isFiltered(target, pd)) {
				return true;
			}
		}
		return false;
	}

	private boolean isFiltered(EObject target, IItemPropertyDescriptor pd) {
		EStructuralFeature f = (EStructuralFeature) pd.getFeature(target);
		//
		// Marked as invisible
		if (EMFUtils.testBooleanAnnotation(f, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_HIDDEN)) {
			return true;
		}
		String[] filterFlags = pd.getFilterFlags(target);
		//
		// No filters exist
		if (filterFlags == null || filterFlags.length == 0) {
			return false;
		}
		//
		// Filter is disabled
		Collection<String> disabledFilterFlags = DetailUtils.getDisabledFilterFlags();
		for (String filterFlag : filterFlags) {
			if (disabledFilterFlags.contains(filterFlag)) {
				return false;
			}
		}
		//
		// Filter exists, and is not disabled
		return true;
	}

	/*
	 * Public static methods
	 */
	public static Section createCategorySection(FormToolkit toolkit, Composite parent, String category, Image icon) {
		return createCategorySection(toolkit, parent, category, icon, false);
	}
	
	public static Section createCategorySection(FormToolkit toolkit, Composite parent, String category, Image icon, boolean hasTwistie) {
		if (EMFDetailUtils.isCategoryHeaderHidden(category)) {
			category = null;
		}
		Section categorySection = DetailFormToolkit.createSection(toolkit, parent, category, icon, hasTwistie);
	    categorySection.clientVerticalSpacing = 0;
	    if ((parent.getLayout() instanceof RowLayout) || (parent.getLayout() instanceof ColumnLayout)) {
	    	categorySection.setLayoutData(null);
	    }
	    Composite categorySubSection = toolkit.createComposite(categorySection);
	    GridLayout layout = new GridLayout(2, false);
	    layout.marginHeight = 3;
	    layout.verticalSpacing = 2;
	    layout.horizontalSpacing = 10;
	    categorySubSection.setLayout(layout);
	    categorySection.setClient(categorySubSection);
		return categorySection;
	}

	public static List<IFormPart> getFormParts(FormToolkit toolkit, EObject object, boolean showTitle, ISelectionProvider selectionProvider) {
		IItemPropertySource source = null;
		if (object instanceof MultiEObject) {
			source = ((MultiEObject)object).getSource();
		} else {
			source = EMFUtils.adapt(object, IItemPropertySource.class);
		}
		List<IFormPart> formParts = new ArrayList<IFormPart>();
		if (source != null) {
			List<IItemPropertyDescriptor> pds = new ArrayList<IItemPropertyDescriptor>();
			for (PropertyDescriptorContributor c : PROPERTY_DESCRIPTOR_CONTRIBUTOR) {
				pds.addAll(c.getPropertyDescriptors(object));
			}
			pds.addAll(source.getPropertyDescriptors(object));
			Map<String, List<IItemPropertyDescriptor>> map = EMFDetailUtils.groupByCategory(object, pds);
			
			PropertyDescriptorSorter sorter = getPropertyDescriptorSorter(toolkit, object);
			if (sorter != null) {
				sorter.sort(object, (LinkedHashMap<String, List<IItemPropertyDescriptor>>) map);
			}
			for (List<IItemPropertyDescriptor> descriptors : map.values()) {
				if (!EMFDetailUtils.isCategoryEmpty(object, descriptors))
					formParts.add(new EMFDetailFormPart(toolkit, descriptors, showTitle, selectionProvider));
				showTitle = false;
			}
			if (showTitle) {
				formParts.add(new EMFTitleFormPart());
			}
		}
		return formParts;
	}

	/**
	 * Retrieves the property descriptor sorter by first adapting the object to an IDetailProvider
	 * element and then accessing the PropertyDescriptorSorter from that, otherwise, the mission extendable
	 * sorter is used
	 * 
	 * @param toolkit that is used
	 * @param object to adapt
	 * @return the property descriptor sorter if configured, null otherwise
	 */
	private static PropertyDescriptorSorter getPropertyDescriptorSorter(FormToolkit toolkit, EObject object) {
		PropertyDescriptorSorter pdSorter = null;
		IDetailProvider detailProvider = EMFUtils.adapt(object, IDetailProvider.class);
		if (detailProvider != null) {
			DetailProviderParameter parameter = new DetailProviderParameter();
			parameter.setTarget(object);
			parameter.setDetailFormToolkit(toolkit);
			pdSorter = detailProvider.getPropertyDescriptorSorter(parameter);
		}
		
		if (pdSorter == null) {
			if (sorter == null) {
				try {
					sorter = MissionExtender.construct(PropertyDescriptorSorter.class);
				} catch (ConstructionException e) {
					LogUtil.error("couldn't create a property descriptor sorter", e);
				}
			}
			pdSorter = sorter;
		}
		if (pdSorter == null) {
			pdSorter = new PropertyDescriptorSorter(); //default sorter
		}
		return pdSorter;
	}

}
