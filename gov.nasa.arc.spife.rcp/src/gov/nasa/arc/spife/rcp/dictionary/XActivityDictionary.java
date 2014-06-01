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
package gov.nasa.arc.spife.rcp.dictionary;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryImpl;

/*
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotRgbRegistry;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryImpl;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.ENumericRequirement;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.EReferenceParameter;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ResourceDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.unit.Unit;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.xml.sax.SAXException;
*/
public class XActivityDictionary extends ActivityDictionaryImpl {
	
//	private static final Pattern PATTERN_RGB = Pattern.compile("rgb\\((\\d+,\\d+,\\d+)\\)", Pattern.CASE_INSENSITIVE);
//	
//	public XActivityDictionary() {
//		super();
//	}
//	
//	@Override
//	public void load(URI uri) {
//		// If cannot be read as EActivityDictionary, try as NGPSAD
//		Exception exception = null;
//		try {
//			Resource resource = loadResource(uri);
//			EObject root = EMFUtils.getLoadedContent(resource);
//			if (root instanceof Dictionary) {
//				Dictionary dictionary = ((Dictionary)root);
//				if (dictionary != null) {
//					load(dictionary);
//				}
//				return;
//			}
//		} catch (Exception e) {
//			exception = e;
//		}
//		// Next try to read it as an EActivityDictionary
//		try {
//			super.load(uri);
//		} catch (Exception e) {
//			LogUtil.error("Loading as eAD", e);
//			if (exception != null) {
//				LogUtil.error("Loading as NGPSAD", exception);
//			}
//		}
//	}
//	
//	private void load(Dictionary dictionary) {
//		clearCache();
//		getAttributeDefs().clear();
//		getExtendedDefinitions().clear();
//		
//		append(dictionary);
//		
//		finishLoading(dictionary.eResource());
//	}
//	
//	private void append(Dictionary dictionary) {
//		buildMetadata(dictionary);
//		buildDefinitions(dictionary);
//
////		annotateFromProperties(this, dictionary.getProperties());
////		buildConstants(dictionary);
////		buildAttributes(dictionary);
////		buildResourceDefs(dictionary);
////		buildActivityDefs(dictionary);
////		buildActivityGroupDef(dictionary);
////		
//		EClass baseClass = EcoreFactory.eINSTANCE.createEClass();
//		baseClass.setName("XDef__BaseClass");
//		for (Object object : getAttributeDefs()) {
//			EParameterDef copy = EMFUtils.copy((EParameterDef) object);
//			baseClass.getEStructuralFeatures().add(copy);
//		}
//		getEClassifiers().add(0, baseClass);
//		for (EClassifier klass : getEClassifiers()) {
//			if (klass instanceof EActivityDef) {
//				((EActivityDef)klass).getESuperTypes().add(baseClass);
//			}
//		}
//	}
//	
//	private void buildMetadata(Dictionary dictionary) {
//		setAuthor(dictionary.getAuthor());
//		setDate(dictionary.getDate());
//		setDescription(dictionary.getDescription());
//		setVersion(dictionary.getVersion());
//		
//		String name = dictionary.getName();
//		setName(name);
//		setNsPrefix(name);
//		setNsURI("http://" + dictionary.getDomain() + "/");
//	}
//	
//	private void buildDefinitions(Dictionary dictionary) {
//		for (Definition definition : dictionary.getDefinitions()) {
//			if (definition instanceof EnumDef) {
//				buildEnumDef((EnumDef)definition);
//			} else if (definition instanceof AttributeDef) {
//				buildAttributeDef((AttributeDef)definition);
//			} else if (definition instanceof ReferenceDef) {
//				buildReferenceDef((ReferenceDef)definition);
//			} else if (definition instanceof ActivityDef) {
//				buildActivityDef((ActivityDef)definition);
//			} else if (definition instanceof ActivityGroupDef) {
//				buildActivityGroupDef((ActivityGroupDef)definition);
//			} else if (definition instanceof ObjectDef) {
//				buildObjectDef((ObjectDef)definition);
//			} else if (definition instanceof ResourceDef) {
//				buildResourceDef((ResourceDef)definition);
//			}
//		}
//		
//	}
//	
//	private void buildEnumDef(EnumDef definition) {
//		EEnum eEnum = EcoreFactory.eINSTANCE.createEEnum();
//		eEnum.setName(definition.getName());
//		int index = 0;
//		for (EnumValue value : definition.getValues()) {
//			EEnumLiteral eLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
//			eLiteral.setName(value.getName());
//			String literal = value.getLiteral();
//			if (CommonUtils.isNullOrEmpty(literal)) {
//				literal = value.getName();
//			}
//			eLiteral.setLiteral(literal);
//			eLiteral.setValue(index++);
//			
//			//annotate color
//			String color = matchColor(value.getColor());
//			if (color != null) {
//				EMFUtils.addAnnotation(eLiteral, PlotRgbRegistry.DESCRIPTOR, new String[] { PlotRgbRegistry.RGB, color } );
//			}
//
//			eEnum.getELiterals().add(eLiteral);
//		}
//		getEClassifiers().add(eEnum);
//		
//	}
//	
//	private String matchColor(String value) {
//		if (value == null) {
//			return null;
//		}
//		Matcher matcher = PATTERN_RGB.matcher(value.trim());
//		boolean matchFound = matcher.find();
//		if (matchFound) {
//			return matcher.group(1);
//		}
//		return value;
//	}
//	
//	private void buildAttributeDef(AttributeDef def) {
//		EAttributeParameter attribute = createAttributeParameter(def);
//		getAttributeDefs().add(attribute);
//	}
//
//	private EAttributeParameter createAttributeParameter(AttributeDef def) {
//		EAttributeParameter attribute = DictionaryFactory.eINSTANCE.createEAttributeParameter();
//		attribute.setName(def.getName());
//		attribute.setEType(getEDataType(def.getType(), true));
//		attribute.setDefaultValueLiteral(def.getDefaultValueLiteral());
//		attribute.setUnits(parseUnits(def.getUnits()));
//		attribute.setDescription(def.getDescription());
//		if (!CommonUtils.isNullOrEmpty(def.getDisplayName())) {
//			EMFUtils.addAnnotation(attribute, ParameterDescriptor.ANNOTATION_SOURCE, new String[] { ParameterDescriptor.ANNOTATION_DETAIL_DISPLAY_NAME, def.getDisplayName() });
//		}
//		if (!CommonUtils.isNullOrEmpty(def.getCategory())) {
//			EMFUtils.addAnnotation(attribute, ParameterDescriptor.ANNOTATION_SOURCE, new String[] { ParameterDescriptor.ANNOTATION_DETAIL_CATEGORY, def.getCategory() });
//		}
//		if (!CommonUtils.isNullOrEmpty(def.getShortDescription())) {
//			EMFUtils.addAnnotation(attribute, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, new String[] { EMFDetailUtils.ANNOTATION_DETAIL_SHORT_DESCRIPTION, def.getShortDescription() });
//		}
//		addAnnotations(attribute, def.getAnnotations());
//		return attribute;
//	}
//	
//	private EDataType getEDataType(String type, boolean nullAllowed) {
//		EClassifier classifier = getEClassifier(type);
//		if (classifier == null) {
//			if ("duration".equalsIgnoreCase(type)) {
//				classifier = JSciencePackage.Literals.EDURATION;
//			} else {
//				classifier = EMFUtils.createEDataTypeFromString(type);
//			}
//		}
//		if (classifier instanceof EDataType) {
//			if (nullAllowed) {
//				if (EcorePackage.Literals.EBOOLEAN == classifier) {
//					classifier = EcorePackage.Literals.EBOOLEAN_OBJECT;
//				} else if (EcorePackage.Literals.EDOUBLE == classifier) {
//					classifier = EcorePackage.Literals.EDOUBLE_OBJECT;
//				} else if (EcorePackage.Literals.EFLOAT == classifier) {
//					classifier = EcorePackage.Literals.EFLOAT_OBJECT;
//				} else if (EcorePackage.Literals.EINT == classifier) {
//					classifier = EcorePackage.Literals.EINTEGER_OBJECT;
//				} else if (EcorePackage.Literals.ELONG == classifier) {
//					classifier = EcorePackage.Literals.ELONG_OBJECT;
//				}
//			} else {
//				if (EcorePackage.Literals.EBOOLEAN_OBJECT == classifier) {
//					classifier = EcorePackage.Literals.EBOOLEAN;
//				} else if (EcorePackage.Literals.EDOUBLE_OBJECT == classifier) {
//					classifier = EcorePackage.Literals.EDOUBLE;
//				} else if (EcorePackage.Literals.EFLOAT_OBJECT == classifier) {
//					classifier = EcorePackage.Literals.EFLOAT;
//				} else if (EcorePackage.Literals.EINTEGER_OBJECT == classifier) {
//					classifier = EcorePackage.Literals.EINT;
//				} else if (EcorePackage.Literals.ELONG_OBJECT == classifier) {
//					classifier = EcorePackage.Literals.ELONG;
//				}
//			}
//			return (EDataType) classifier;
//		}
//		LogUtil.error("no type found for '"+type+"'");
//		return null;
//	}
//	
//	private void addAnnotations(EModelElement element, EList<Annotation> annotations) {
//		for (Annotation annotation : annotations) {
//			EMFUtils.addAnnotation(element, annotation.getSource(), new String[] {annotation.getKey(), annotation.getValue()});
//		}
//	}
//
//	private Unit<?> parseUnits(String units) {
//		try {
//			return EnsembleUnitFormat.INSTANCE.parse(units);
//		} catch (ParseException e) {
//			LogUtil.error("cannot parse '"+units+"'");
//		}
//		return Unit.ONE;
//	}
//	
//	private void buildReferenceDef(ReferenceDef def) {
//		EReferenceParameter reference = createReferenceParameter(def);
//		getAttributeDefs().add(reference);
//	}
//	
//	private EReferenceParameter createReferenceParameter(ReferenceDef def) {
//		EReferenceParameter reference = DictionaryFactory.eINSTANCE.createEReferenceParameter();
//		reference.setName(def.getName());
//		reference.setEType(getEClass(def.getType()));
//		reference.setDescription(def.getDescription());
//		String containment = def.getContainment();
//		if ("true".equals(containment)) {
//			reference.setContainment(true);
//		}
//		EMFUtils.addAnnotation(reference, ParameterDescriptor.ANNOTATION_SOURCE, new String[] { ParameterDescriptor.ANNOTATION_DETAIL_DISPLAY_NAME, def.getDisplayName() });
//		addAnnotations(reference, def.getAnnotations());
//		buildRequirements(reference, def.getRequirements());
//		buildEffects(reference, def.getEffects());
//		return reference;
//	}
//	
//	private void buildRequirements(EReferenceParameter reference, EList<Requirement> requirements) {
//		for (Requirement requirement : requirements) {
//			if (requirement instanceof StateRequirement) {
//				EStateRequirement stateReq = createStateRequirement((StateRequirement)requirement);
//				if (stateReq != null) {
//					reference.getRequirements().add(stateReq);
//				}
//			} else if (requirement instanceof NumericRequirement) {
//				ENumericRequirement numReq = createNumericRequirement((NumericRequirement)requirement);
//				if (numReq != null) {
//					reference.getRequirements().add(numReq);
//				}
//			}
//		}
//	}
//
//	private void buildEffects(EReferenceParameter reference, List<String> effects) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	private EClass getEClass(String type) {
//		if ("StringToStringMapEntry".equals(type)) {
//			return EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY;
//		}
//		EClassifier eClassifier = getEClassifier(type);
//		if (eClassifier instanceof EClass) {
//			return (EClass) eClassifier;
//		}
//		LogUtil.error("EClass '"+type+"' not found");
//		return null;
//	}
//	
//	private void buildActivityDef(ActivityDef def) {
//		EActivityDef eActivityDef = DictionaryFactory.eINSTANCE.createEActivityDef();
//		eActivityDef.setName(def.getName());
//		eActivityDef.setCategory(def.getCategory());
//		eActivityDef.setDuration(def.getDuration());
//		eActivityDef.setDescription(def.getDescription());
//		if (!CommonUtils.isNullOrEmpty(def.getDisplayName())) {
//			DictionaryUtil.setDisplayName(eActivityDef, def.getDisplayName());
//		}
//		if (!CommonUtils.isNullOrEmpty(def.getHiddenParams())) {
//			DictionaryUtil.setHidden(eActivityDef,  def.getHiddenParams());
//		}
//		addAnnotations(eActivityDef, def.getAnnotations());
//		buildParameterDefs(eActivityDef, def.getParameters());
//		buildRequirements(eActivityDef, def.getRequirements());
//		buildEffects(eActivityDef, def.getEffects());
//		getEClassifiers().add(eActivityDef);
//	}
//	
//	private void buildParameterDefs(EActivityDef eActivityDef, EList<ParameterDef> parameters) {
//		for (ParameterDef parameter : parameters) {
//			EStructuralFeature feature = null;
//			if (parameter instanceof AttributeDef) {
//				feature = createAttributeParameter((AttributeDef)parameter);
//			} else if (parameter instanceof ReferenceDef) {
//				feature = createReferenceParameter((ReferenceDef)parameter);
//			}
//			if (feature != null) {
//				eActivityDef.getEStructuralFeatures().add(feature);
//			}
//		}
//	}
//	
//	private void buildRequirements(EActivityDef eActivityDef, EList<Requirement> requirements) {
//		for (Requirement requirement : requirements) {
//			if (requirement instanceof StateRequirement) {
//				EStateRequirement stateReq = createStateRequirement((StateRequirement)requirement);
//				if (stateReq != null) {
//					eActivityDef.getStateRequirements().add(stateReq);
//				}
//			} else if (requirement instanceof NumericRequirement) {
//				ENumericRequirement numReq = createNumericRequirement((NumericRequirement)requirement);
//				if (numReq != null) {
//					eActivityDef.getNumericRequirements().add(numReq);
//				}
//			}
//		}
//	}
//	
//	private EStateRequirement createStateRequirement(StateRequirement requirement) {
//		try {
//			EStateRequirement req = DictionaryFactory.eINSTANCE.createEStateRequirement();
//			req.setRequiredState(requirement.getRequiredState());
//			req.setDefinition(getDefinition(EStateResourceDef.class, requirement.getDefinition()));
////			req.setPeriod(getPeriod(requirement.getPeriod()));
////			buildOffsets(requirement, req);
//			return req;
//		} catch (Exception e) {
//			LogUtil.error(e);
//		}
//		return null;
//	}
//	
//	private ENumericRequirement createNumericRequirement(NumericRequirement requirement) {
//		try {
//			ENumericRequirement req = DictionaryFactory.eINSTANCE.createENumericRequirement();
//			req.setExpression(requirement.getExpression());
////			req.setPeriod(getPeriod(requirement.getPeriod()));
////			buildOffsets(requirement, req);
//			return req;
//		} catch (Exception e) {
//			LogUtil.error(e);
//		}
//		return null;
//	}
//
//
//	private void buildEffects(EActivityDef eActivityDef, EList<String> effects) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	private void buildActivityGroupDef(ActivityGroupDef definition) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	private void buildObjectDef(ObjectDef definition) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	private void buildResourceDef(ResourceDef definition) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	private Resource loadResource(URI uri) throws IOException {
//		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
//		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
//		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
//		EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, null, new HashMap<Resource, Boolean>());
//		ResourceSet resourceSet = editingDomain.getResourceSet();
//		/*
//		 * This option can be enabled to defer resolving references within a resource until the whole document has been parsed.
//		 * The default strategy is to try to resolve each reference as it is encountered and then, at the end, resolve any ones
//		 * that failed. This wastes time looking up forward references that do not exist yet, which, if you're using intrinsic
//		 * IDs, can involve iterating over every object in the resource.
//		 */
//		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
//		/*
//		 * This option is used to provide a parser pool, from which SAXParser instances are created and reused. XMLParserPool
//		 * defines a simple interface for obtaining and releasing parsers based on their features and properties. Specifying a
//		 * parser pool, such as an instance of the default implementation, XMLParserPoolImpl, can improve performance dramatically
//		 * when a resource performs repeated loads. A single parser pool can also be shared among multiple resources. Default
//		 * implementation is also thread-safe.
//		 */
//		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl() {
//
//			@Override
//			protected SAXParser makeParser(Map<String, Boolean> features,
//					Map<String, ?> properties) throws ParserConfigurationException, SAXException {
//				// TODO Auto-generated method stub
//				SAXParser parser = super.makeParser(features, properties);
//				return parser;
//			}
//			
//		});
//		/*
//		 * This option can be used to share the cache of mappings between qualified XML names (namespace + local name) and
//		 * corresponding Ecore features across invocations of load(), or even among resources. It can take some time to determine
//		 * these associations, since they can be affected by ExtendedMetaData or an XMLMap, so they are cached during a load. If
//		 * you use this option to specify the same map for several loads, that instance will be used as the cache, improving the
//		 * performance for all but the first. You can share a single map among multiple resources, unless they load different
//		 * models with conflicting qualified names.
//		 */
//		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
//
//		// Register the package to ensure it is available during loading.
//		//
//		resourceSet.getPackageRegistry().put(XDictionaryPackage.eNS_URI, XDictionaryPackage.eINSTANCE);
//		Resource resource = editingDomain.getResourceSet().createResource(uri);
//		resource.load(null);
//		return resource;
//	}
	
}
