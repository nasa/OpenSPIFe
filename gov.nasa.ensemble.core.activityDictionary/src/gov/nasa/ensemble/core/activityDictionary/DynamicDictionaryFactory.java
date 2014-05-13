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
package gov.nasa.ensemble.core.activityDictionary;

import gov.nasa.ensemble.dictionary.util.DictionaryUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DynamicDictionaryFactory extends EFactoryImpl {
	
	public static final DynamicDictionaryFactory eINSTANCE = new DynamicDictionaryFactory();
	
	@Override
	public EObject create(EClass eClass) {
		EObject object = super.create(eClass);
		if (object != null) {
			initializeParameters(object);
		}
		return object;
	}

	private void initializeParameters(EObject object) {
		EClass eClass = object.eClass();
		for (EAttribute feature : eClass.getEAllAttributes()) {
			if (feature.isID() && object.eGet(feature) == null) {
				object.eSet(feature, EcoreUtil.generateUUID());
			}
			String valueLiteral = EMFUtils.getAnnotation(eClass, DictionaryUtil.ANNOTATION_SOURCE_OVERRIDE, feature.getName());
			if (valueLiteral != null) {
				EDataType eDataType = feature.getEAttributeType();
				Object value = EcoreUtil.createFromString(eDataType, valueLiteral);
				object.eSet(feature, value);
			}
		}
		EList<EStructuralFeature> structuralFeatures = eClass.getEAllStructuralFeatures();
		for (EStructuralFeature feature : structuralFeatures) {
			EClassifier eClassifier = feature.getEType();
			if (feature instanceof EReference
					&& feature.isMany()
					&& ((Collection)object.eGet(feature)).isEmpty()
					&& eClassifier.getEPackage() == ActivityDictionary.getInstance()
					&& feature.getLowerBound() > 0) {
				EClass eReferenceType = (EClass) feature.getEType();
				EFactory factory = eReferenceType.getEPackage().getEFactoryInstance();
				List<EObject> values = new ArrayList<EObject>();
				Object featureDefaultValue = feature.getDefaultValue();
				if (featureDefaultValue instanceof Object[]) {
					featureDefaultValue = Arrays.asList((Object[]) featureDefaultValue);
				}
				Collection<? extends EObject> defaultValues = (Collection<? extends EObject>)featureDefaultValue;
				if (defaultValues != null) {
					for (EObject defaultValue : defaultValues) {
						EObject element = EcoreUtil.copy(defaultValue);
						values.add(element);
					}
				} else {
					for (int i=0; i<feature.getLowerBound(); i++) {
						EObject element = factory.create(eReferenceType);
						values.add(element);
					}
				}
				object.eSet(feature, values);
			} else if (feature instanceof EReference 
					&& object.eGet(feature) == null) {
				EFactory customFactory = eClassifier.getEPackage().getEFactoryInstance();
				EObject defaultValueObject = customFactory.create((EClass) eClassifier);
				object.eSet(feature, defaultValueObject);
			} else if ((eClassifier instanceof EDataType) 
						&& feature.isMany()
						&& (feature.getDefaultValueLiteral() != null)
						&& (feature.getDefaultValueLiteral().length() > 0)) {
				try {
					EDataType eType = (EDataType) eClassifier;
					EPackage eTypePackage = eType.getEPackage();
					if (eTypePackage != null) {
						EFactory factory = eTypePackage.getEFactoryInstance();
						StringTokenizer tokenizer = new StringTokenizer(feature.getDefaultValueLiteral(), ",");
						List<Object> defaultValueList = new ArrayList<Object>();
						while (tokenizer.hasMoreTokens()) {
							String token = tokenizer.nextToken();
							defaultValueList.add(factory.createFromString(eType, token));
						}
						object.eSet(feature, defaultValueList);
					}
				} catch (Exception e) {
					Logger.getLogger(DynamicDictionaryFactory.class).error("parsing default "+feature.getDefaultValueLiteral()+" for "+feature);
				}
			}
		}
	}

}
