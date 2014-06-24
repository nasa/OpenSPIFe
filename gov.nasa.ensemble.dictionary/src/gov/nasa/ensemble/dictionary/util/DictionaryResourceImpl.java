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
/**
 * 
 */
package gov.nasa.ensemble.dictionary.util;

import gov.nasa.ensemble.dictionary.EAttributeParameter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xml.type.SimpleAnyType;

public class DictionaryResourceImpl extends XMIResourceImpl {
	
	public DictionaryResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected XMLHelper createXMLHelper() {
	    return new DictionaryXMLHelper(this);
	}
	
	private static final class DictionaryXMLHelper extends XMIHelperImpl {
		
		private DictionaryXMLHelper(XMLResource resource) {
			super(resource);
		}

		@Override
		public void setValue(EObject object, EStructuralFeature feature, Object value, int position) {
			Object newValue = value;
			if ((object instanceof EAttributeParameter) && ("name".equals(feature.getName())) && (value instanceof String)) {
				String string = (String) value;
				if (string.contains(" ")) {
					newValue = string.replace(" ", "_");
				}
			}
			super.setValue(object, feature, newValue, position);
			if (value != newValue) {
				throw new IllegalArgumentException("Illegal EAttributeParameter name '" + value + "' replaced with '" + newValue + "'");
			}
		}
		
		@Override
		public EClassifier getType(EFactory eFactory, String typeName) {
		    if (eFactory != null) {
		      EPackage ePackage = eFactory.getEPackage();
		      if (extendedMetaData != null) {
		        return extendedMetaData.getType(ePackage, typeName);
		      } else {
		    	EClassifier eClass = ePackage.getEClassifier(typeName);
		        if (eClass == null && xmlMap != null) {
		          return xmlMap.getClassifier(ePackage.getNsURI(), typeName);
		        }
		        return eClass;
		      }
		    }
		    return null;
		}

		@Override
		public EObject createObject(EFactory eFactory, EClassifier type) {
			EObject newObject = null;
		    if (eFactory != null) {
		      if (extendedMetaData != null) {
		        if (type == null) {
		          return null;
		        } else if (type instanceof EClass) {
		          EClass eClass = (EClass)type;
		          if (!eClass.isAbstract()) {
		            newObject = eFactory.create((EClass)type);
		          }
		        } else {
		          SimpleAnyType result = (SimpleAnyType)EcoreUtil.create(anySimpleType);
		          result.setInstanceType((EDataType)type);
		          newObject = result;
		        }
		      } else {
		        if (type instanceof EClass) {
		          EClass eClass = (EClass)type;
		          if (!eClass.isAbstract()) {
		            newObject = eFactory.create((EClass)type);
		          }
		        } else {
		        	newObject = type;
		        }
		      }
		    }
		    return newObject;
		}
	}


}
