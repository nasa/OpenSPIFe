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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.preferences.TooltipAttributesPreferencePage;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.StringStringifier;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.dictionary.EReferenceParameter;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.util.EEnumStringifier;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TooltipShellBuilder {

	/*
	 * Timeline constants
	 */
	
	private static int valueLabelWidthInPixels = TimelineConstants.TIMELINE_PREFERENCES.getInt(TimelineConstants.TOOLTIP_WIDTH);
	private static StringStringifier STRING_STRINGIFIER = new StringStringifier();
	private static String SPACE = " ";
	
	/* a comma separated list of attributes that is to be parsed and will directly
	 * determine which attributes, order respected, will be shown in the tooltip.
	 */
	private static String tooltipAttributes = null;

	/* tooltipAttributes variable after it has been parsed and formatted */
	private static ArrayList<String> supportedTooltipAttributes = null;

	private static final class PropertyChangeListener implements IPropertyChangeListener {
		
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String newTooltipAttributes = TimelineConstants.TIMELINE_PREFERENCES.getString(TooltipAttributesPreferencePage.P_TOOLTIP_ATTRIBUTES);
			// only react to the change if something happened.
			if (!tooltipAttributes.equals(newTooltipAttributes)) {
				tooltipAttributes = newTooltipAttributes;
				supportedTooltipAttributes = new ArrayList<String>(Arrays.asList(CommonUtils.COMMA_PATTERN.split(tooltipAttributes)));
			}
			valueLabelWidthInPixels = TimelineConstants.TIMELINE_PREFERENCES.getInt(TimelineConstants.TOOLTIP_WIDTH);
		}
	}
	
	static {
		try {
			TimelineConstants.TIMELINE_PREFERENCES.addPropertyChangeListener(new PropertyChangeListener());
			tooltipAttributes = TimelineConstants.TIMELINE_PREFERENCES.getString(TooltipAttributesPreferencePage.P_TOOLTIP_ATTRIBUTES);
			supportedTooltipAttributes = new ArrayList<String>(Arrays.asList(CommonUtils.COMMA_PATTERN.split(tooltipAttributes)));
			// remove any whitespace characters
			for (int i = 0; i < supportedTooltipAttributes.size(); i++) {
				supportedTooltipAttributes.set(i, supportedTooltipAttributes.get(i).trim());
			}
		} catch (Exception e) {
			LogUtil.error("error in static block for TooltipShellBuilder", e);
		}
	}

	/*
	 * Image caching
	 */
	
	/* cache images through the resource manager */
	private static ResourceManager resourceManager;
	private static int managerCreation[] = new int[0];

	/* package */ static ResourceManager getResourceManager() {
		synchronized (managerCreation) {
			if (resourceManager == null) {
				resourceManager = new LocalResourceManager(JFaceResources.getResources());
			}
		}
		return resourceManager;
	}	

	/**
	 * Main entry point to create a complete tooltip shell for the element, on the parent.
	 * 	
	 * @param parent
	 * @param element
	 * @return
	 */
	public static Shell createTooltipShell(Control parent, Object object) {
		EPlanElement element = (EPlanElement)object;
		Shell shell = new Shell(parent.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
		Display display = parent.getDisplay();
		shell.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		shell.setLayout(new FillLayout());
		Composite mainComposite = new Composite(shell, SWT.NONE);
		mainComposite.setBackground(shell.getBackground());
		mainComposite.setLayout(new TableWrapLayout());
		createTitleComposite(element, mainComposite);
		createTitleBodySeparatorLabel(mainComposite);
		findMarkersAndCreateMarkerComposite(element, mainComposite);
		createBodyComposite(element, mainComposite);
		return shell;
	}

	private static void createTitleComposite(EPlanElement element, Composite mainComposite) {
		
		Composite titleComposite = new Composite(mainComposite, SWT.NONE);
		int numberOfColumns = 2; // an image and some text
		GridLayout layout = new GridLayout(numberOfColumns, false);

		layout.marginHeight = 0;
		titleComposite.setLayout(layout);
		titleComposite.setBackground(mainComposite.getBackground());

		String title = getTitle(element);
		Image image = PlanUtils.getIcon(element);

		Label titleCompositeImageLabel = new Label(titleComposite, SWT.NONE);
		titleCompositeImageLabel.setBackground(titleComposite.getBackground());
		titleCompositeImageLabel.setImage(image);

		Label titleCompositeTextLabel = new Label(titleComposite, SWT.NONE);
		titleCompositeTextLabel.setBackground(titleComposite.getBackground());
		titleCompositeTextLabel.setText(title.replaceAll("&", "&&"));
		titleCompositeTextLabel.setFont(FontUtils.getSystemBoldFont());
	}

	private static String getTitle(EPlanElement ePlanElement) {
		String title = ePlanElement.getName();
		if (ePlanElement instanceof EActivity) {
			String type = ((EActivity)ePlanElement).getType();
			if (type != null) {
				title += " (" + type +")";
			}
		}
		return title;
	}

	private static void createTitleBodySeparatorLabel(Composite mainComposite) {
		Label titleBodySeparatorLabel = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		titleBodySeparatorLabel.setLayoutData(new TableWrapData(TableWrapData.FILL));
	}

	/**
	 * Get the composite which will contain all of the marker feedback widgets.
	 * @param element 
	 * 
	 * @return a composite that is the parent to all the marker feedback widgets.
	 */
	private static void findMarkersAndCreateMarkerComposite(EPlanElement element, Composite mainComposite) {
		if(element != null) {
			EPlan plan = EPlanUtils.getPlan(element);
			if (plan==null) {
				LogUtil.warn("No plan found for element " + element);
				return;
			}
			Resource emfResource = plan.eResource();
			Object adapter = plan.getAdapter(IResource.class);
			IMarker[] markers = null;
			if (adapter != null && adapter instanceof IResource) {
				IResource resource = (IResource)adapter;
				if (resource.exists()) {
					boolean includeSubtypes = true;
					int depth = IResource.DEPTH_INFINITE;
					try {
						markers = resource.findMarkers(IMarker.MARKER, includeSubtypes, depth);
					} catch (CoreException e1) {
						LogUtil.error(e1);
					}
				} else if (!plan.isTemplate()) {
					LogUtil.warnOnce(resource.toString() + " does not exist");
				}
			}
			if (markers != null) {
				createMarkerComposite(element, emfResource, markers, mainComposite);
			}
		}
	}

	/**
	 * Build the actual marker feedback widgets.
	 * 
	 * @param element
	 * @param emfResource
	 * @param markers
	 * @param mainComposite
	 */
	private static void createMarkerComposite(EPlanElement element, Resource emfResource,
			IMarker[] markers, Composite mainComposite) {
		Composite markerComposite = null;
		for (IMarker marker : markers) {
			Violation violation = getViolationForMarker(element, marker);
			if (violation != null) {
				if (markerComposite == null) {
					markerComposite = new Composite(mainComposite, SWT.NONE);
					GridLayout layout = new GridLayout(2, false);
					layout.marginHeight = 0;
					markerComposite.setLayout(layout);
					Color backgroundColor = mainComposite.getBackground();
					markerComposite.setBackground(backgroundColor);
				}
				buildOneMarkerRow(markerComposite, marker, violation);
			}
		}
		if (markerComposite != null) {
			createTitleBodySeparatorLabel(mainComposite);
		}
	}

	/**
	 * Returns the violation for a marker if it can be found
	 * @param element
	 * @param marker
	 * @return
	 */
	private static Violation getViolationForMarker(EPlanElement element, IMarker marker) {
		EPlan plan = EPlanUtils.getPlan(element);
		if (plan != null) {
			PlanAdvisorMember planAdvisorMember = PlanAdvisorMember.get(plan);
			if (planAdvisorMember != null) {
				Object source = planAdvisorMember.getMarkerViolation(marker);
				if (source instanceof Violation) {
					Violation violation = (Violation)source;
					if (violation.getElements().contains(element)) {
						return violation;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Build a single marker feedback widget.
	 * @param markerComposite 
	 * 
	 * @param backgroundColor
	 * @param marker
	 * @return
	 */
	private static void buildOneMarkerRow(Composite markerComposite, IMarker marker, Violation violation) {
		ImageDescriptor imageDescriptor = getMarkerImageDescriptor(marker);
		Image image = (Image) getResourceManager().get(imageDescriptor);								
		Label titleCompositeImageLabel = new Label(markerComposite, SWT.NONE);
		titleCompositeImageLabel.setBackground(markerComposite.getBackground());
		titleCompositeImageLabel.setImage(image);
		Label titleCompositeTextLabel = new Label(markerComposite, SWT.NONE);
		titleCompositeTextLabel.setBackground(markerComposite.getBackground());
		titleCompositeTextLabel.setText(violation.getName() + ": " + violation.getDescription());
		titleCompositeTextLabel.setFont(FontUtils.getSystemBoldFont());	
	}

	private static ImageDescriptor getMarkerImageDescriptor(IMarker marker) {
		String markerPluginId = null;
		try {
			markerPluginId = String.valueOf(marker.getAttribute(MarkerConstants.PLUGIN_ID));
		} catch (CoreException e) {
			LogUtil.error(e);
		}
		String imageDescriptorPath = null;
		try {
			imageDescriptorPath = String.valueOf(marker.getAttribute(MarkerConstants.IMAGE_DESCRIPTOR_PATH));
		} catch (CoreException e) {
			LogUtil.error(e);
		}
		ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(markerPluginId, imageDescriptorPath);
		return imageDescriptor;
	}
	
	private static void createBodyComposite(EPlanElement ePlanElement, Composite mainComposite) {
		Composite bodyComposite = new Composite(mainComposite, SWT.NONE);
		bodyComposite.setBackground(mainComposite.getBackground());
		int numberOfColumns = 2; // attribute, value pairs
		GridLayout gridLayout = new GridLayout(numberOfColumns, false);
		bodyComposite.setLayout(gridLayout);

		WeakHashMap<String, EStructuralFeature> nameToStructuralFeatureMap = getNameToEStructuralFeatureMap(ePlanElement);
		/*
		 * Build the UI to match the order of the tooltip attributes
		 */
		for (String supportedAttribute : supportedTooltipAttributes) {
			EStructuralFeature eStructuralFeature = nameToStructuralFeatureMap.get(supportedAttribute);
			String nestedAttribute = null;
			if (eStructuralFeature == null) {
				int barIndex = supportedAttribute.indexOf(':');
				if (barIndex > 0) {
					String containingFeatureName = supportedAttribute.substring(0, barIndex).trim();
					eStructuralFeature = nameToStructuralFeatureMap.get(containingFeatureName);
					if (eStructuralFeature != null) {
						nestedAttribute = supportedAttribute.substring(barIndex + 1).trim();
					}
				}
			}
			if (eStructuralFeature != null) {
				Label titleLabel = new Label(bodyComposite, SWT.NONE);
				titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
				titleLabel.setBackground(bodyComposite.getBackground());
				titleLabel.setText(supportedAttribute);

				Font boldFont = FontUtils.FONT_REGISTRY_INSTANCE.getBold(titleLabel.getFont().toString());
				titleLabel.setFont(boldFont);

				final Label textLabel = new Label(bodyComposite, SWT.NONE);
				textLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
				textLabel.setBackground(bodyComposite.getBackground());
				String attributeValue = new String();
				if(eStructuralFeature instanceof EAttribute) {
					attributeValue = getAttributeValue(ePlanElement, (EAttribute) eStructuralFeature);
				} else if(eStructuralFeature instanceof EReferenceParameter) {
					if (nestedAttribute != null) {
						Object container = getReferenceParameterValue(ePlanElement, (EReferenceParameter) eStructuralFeature);
						if (container instanceof EObject) {
							EStructuralFeature nestedFeature = nameToStructuralFeatureMap.get(nestedAttribute);
							if (nestedFeature instanceof EAttribute) {
								attributeValue = getAttributeValue(container, (EAttribute) nestedFeature);
							}
						}
					}
					else {
						attributeValue = getReferenceParameter(ePlanElement, (EReferenceParameter) eStructuralFeature);
					}
				}
				if (attributeValue != null) {
					String wrappedString = getWrappedString(textLabel, valueLabelWidthInPixels, attributeValue);
					textLabel.setText(STRING_STRINGIFIER.getDisplayString(wrappedString).replace("&", "&&")); // Escape mnemonic character '&' for widget 
				}
			}
		}
	}
	
	private static String getWrappedString(Drawable drawable, int width, String string) {
		GC gc = new GC(drawable);
		StringBuilder sb = new StringBuilder();
		String[] tokens = string.split(SPACE);
		StringBuilder lineBuilder = new StringBuilder();
		for (String token : tokens) {
			if (gc.textExtent(lineBuilder.toString() + token).x > width) {
				sb.append(lineBuilder.append(STRING_STRINGIFIER.getSystemLineSeparator()).toString());
				lineBuilder = new StringBuilder();
			}
			lineBuilder.append(token).append(SPACE);
		}
		sb.append(lineBuilder.toString());
		gc.dispose();
		return sb.toString();
	}

	private static WeakHashMap<String, EStructuralFeature> getNameToEStructuralFeatureMap(EPlanElement ePlanElement) {
		IItemPropertySource itemPropertySource = EMFUtils.adapt(ePlanElement, IItemPropertySource.class);
		WeakHashMap<String, EStructuralFeature> nameToStructuralFeatureMap = new WeakHashMap<String, EStructuralFeature>();
		EList<EMember> eMembers = ePlanElement.getMembers();
		// gather all of the attributes
		List<EStructuralFeature> allEStructuralFeatures = new ArrayList<EStructuralFeature>();
		for (EMember eMember : eMembers) {
			allEStructuralFeatures.addAll(eMember.eClass().getEAllStructuralFeatures());
		}
		EObject data = ePlanElement.getData();
		if (data != null) {
			allEStructuralFeatures.addAll(data.eClass().getEAllStructuralFeatures());
		}
		
		// build the map
		for (EStructuralFeature eStructuralFeature : allEStructuralFeatures) {
			IItemPropertyDescriptor descriptor = itemPropertySource.getPropertyDescriptor(ePlanElement, eStructuralFeature);
			if (descriptor == null) {
				continue;
			}
			String nameKey = descriptor.getDisplayName(ePlanElement);
			boolean foundTooltipAttribute = false;
			for (String tooltipAttribute : supportedTooltipAttributes) {
				if (tooltipAttribute.startsWith(nameKey)) {
					foundTooltipAttribute = true;
					int barIndex = tooltipAttribute.indexOf(':');
					if (barIndex > -1) {
						String nestedAttributeName = tooltipAttribute.substring(barIndex + 1).trim();
						EClassifier type = eStructuralFeature.getEType();
						if (type instanceof ObjectDef) {
							for (EAttribute nestedAttribute : ((ObjectDef)type).getEAllAttributes()) {
								String nestedDisplayName = ParameterDescriptor.getInstance().getDisplayName(nestedAttribute);
								if (nestedDisplayName.equals(nestedAttributeName)) {
									nameToStructuralFeatureMap.put(nestedDisplayName, nestedAttribute);
								}
							}
						}
					}
				}
			}
			if (!foundTooltipAttribute) {
				nameKey = eStructuralFeature.getName();
			}
			if (nameKey != null) {
				nameToStructuralFeatureMap.put(nameKey, eStructuralFeature);
			}
		}
		return nameToStructuralFeatureMap;
	}

	private static String getAttributeValue(Object object, EAttribute eAttribute) {
		IItemPropertySource source = EMFUtils.adapt(object, IItemPropertySource.class);
		IItemPropertyDescriptor startPD = source.getPropertyDescriptor(object, eAttribute);
		// First check the instance name
		IStringifier stringifier = EMFUtils.getStringifier(eAttribute);
		if (startPD != null) {
			Object value = EMFUtils.getPropertyValue(startPD, object);
			if (value instanceof Collection) {
				StringBuffer buffer = new StringBuffer();
				Collection collection = (Collection)value;
				for (Iterator i=collection.iterator(); i.hasNext(); ) {
					Object o = i.next();
					buffer.append(stringifier.getDisplayString(o));
					if (i.hasNext()) {
						buffer.append(", ");
					}
				}
				return buffer.toString();
			}
			return stringifier.getDisplayString(value);
		}
		return "";
	}
	
	private static Object getReferenceParameterValue(EPlanElement ePlanElement, EReferenceParameter eStructuralFeature) {
		IItemPropertySource source = EMFUtils.adapt(ePlanElement, IItemPropertySource.class);
		IItemPropertyDescriptor startPD = source.getPropertyDescriptor(ePlanElement, eStructuralFeature);
		if (startPD != null) {
			return EMFUtils.getPropertyValue(startPD, ePlanElement);
		}
		return null;
	}
	
	private static String getReferenceParameter(EPlanElement ePlanElement, EReferenceParameter eStructuralFeature) {
		IItemPropertySource source = EMFUtils.adapt(ePlanElement, IItemPropertySource.class);
		IItemPropertyDescriptor startPD = source.getPropertyDescriptor(ePlanElement, eStructuralFeature);
		// First check the instance name
		if (startPD != null) {
			Object value = EMFUtils.getPropertyValue(startPD, ePlanElement);
			if (value != null && StringifierRegistry.hasRegisteredStringifier(eStructuralFeature.getName())) {
				IStringifier stringifier = StringifierRegistry.getStringifier(eStructuralFeature.getName());
				return stringifier.getDisplayString(value);
			}
			if(value instanceof EcoreEList) {
				List<String> valueList = new ArrayList<String>();
				for (Object o : ((EcoreEList) value).toArray()) {
					valueList.add(getChoiceText(eStructuralFeature, o));
				}
				return EEnumStringifier.formatString(valueList.toString());
			}
		}
		return "";
	}


	private static String getChoiceText(EStructuralFeature feature, Object o) {
		String text = null;
		if (feature instanceof EReference) {
			IItemLabelProvider labeler = EMFUtils.adapt(o, IItemLabelProvider.class);
			text = labeler.getText(o);
		}
		return text;
	}



}
