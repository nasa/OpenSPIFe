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
package gov.nasa.ensemble.emf;

import gov.nasa.ensemble.common.io.XMLUtilities;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestEMFJQueryFormGenerator extends Assert {

	@Test
	public void jqueryGeneration() throws ParserConfigurationException {
		//
		// Set up the package
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("junit");
		ePackage.setNsPrefix("junit");
		ePackage.setNsURI("http://junit.ensemble.gov/package.ecore");
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName("JUnitClass");
		eClass.getEStructuralFeatures().add(createEAttribute("stringExample", EcorePackage.Literals.ESTRING));
		eClass.getEStructuralFeatures().add(createEAttribute("booleanExample", EcorePackage.Literals.EBOOLEAN));
		eClass.getEStructuralFeatures().add(createEAttribute("doubleExample", EcorePackage.Literals.EDOUBLE));
		ePackage.getEClassifiers().add(eClass);
		//
		// Set up the in memory instances
		EditingDomain domain = EMFUtils.createEditingDomain();
		ResourceSet resourceSet = domain.getResourceSet();
		resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		Resource resource = resourceSet.createResource(URI.createURI("http://junit.ensemble.gov/resource.xml"));
		resource.getContents().add(eClass);
		EObject object = ePackage.getEFactoryInstance().create(eClass);
		resource.getContents().add(object);
		//
		// Debug output
		// resource.save(System.out, null);
		//
		// Render to jquery-mobile
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		ReflectiveItemProviderAdapterFactory adapterFactory = new ReflectiveItemProviderAdapterFactory();
		IItemPropertySource source = (IItemPropertySource) adapterFactory.adaptNew(object, IItemPropertySource.class);
		IItemLabelProvider labelProvider = (IItemLabelProvider) adapterFactory.adapt(object, IItemLabelProvider.class);
		assertNotNull(source);
		List<IItemPropertyDescriptor> pds = source.getPropertyDescriptors(object);
		Element htmlElement = document.createElement("html");
		document.appendChild(htmlElement);
		Element headElement = createHeadElement(document, htmlElement);
		htmlElement.appendChild(headElement);
		
		Element bodyElement = (Element) htmlElement.appendChild(document.createElement("body"));
		htmlElement.appendChild(bodyElement);
		
		Element pageElement = (Element) bodyElement.appendChild(document.createElement("div"));
		pageElement.setAttribute("data-role", "page");
		pageElement.setAttribute("data-theme", "b");
		pageElement.setAttribute("id", "page1");
		
		Element headerElement = (Element) pageElement.appendChild(document.createElement("div"));
		headerElement.setAttribute("data-role", "header");
		headerElement.setAttribute("id", "hdrMain");
		headerElement.setAttribute("name", "hdrMain");
		headerElement.setAttribute("data-nobackbtn", "true");
		((Element)headerElement.appendChild(document.createElement("h1"))).setTextContent(labelProvider.getText(object));
		
		Element contentElement = (Element) pageElement.appendChild(document.createElement("div"));
		contentElement.setAttribute("data-role", "content");
		contentElement.setAttribute("id", "contentMain");
		contentElement.setAttribute("name", "contentMain");
		
		Element formElement = (Element) contentElement.appendChild(document.createElement("form"));
		formElement.setAttribute("id", "form1");
		
		for (IItemPropertyDescriptor pd : pds) {
			EStructuralFeature feature = (EStructuralFeature)pd.getFeature(object);
			Element divElement = document.createElement("div");
			divElement.setAttribute("id", feature.getName()+"Div");
			divElement.setAttribute("data-role", "fieldcontain");
			formElement.appendChild(divElement);
			
			Element uiElement = document.createElement("label");
			uiElement.setAttribute("for", feature.getName());
			
			String displayName = pd.getDisplayName(object);
			if (feature.isRequired()) {
				displayName += " *";
			}
			uiElement.setTextContent(displayName);
			divElement.appendChild(uiElement);

			Element inputElement = document.createElement("input");
			inputElement.setAttribute("id", feature.getName());
			inputElement.setAttribute("name", feature.getName()+"_r");
			inputElement.setAttribute("type", "text");
			divElement.appendChild(inputElement);
		}
		XMLUtilities.print(document, System.out);
	}

	private Element createHeadElement(Document document, Element htmlElement) {
		Element headElement = (Element) htmlElement.appendChild(document.createElement("head"));
		createLink(document, headElement, "stylesheet", "http://code.jquery.com/mobile/1.0a2/jquery.mobile-1.0a2.min.css");
		createLink(document, headElement, "stylesheet", "css/colors.css");
		createScript(document, headElement, "http://code.jquery.com/jquery-1.4.4.min.js");
		createScript(document, headElement, "http://code.jquery.com/mobile/1.0a2/jquery.mobile-1.0a2.min.js");
		return headElement;
	}

	private void createScript(Document document, Element headElement, String src) {
		Element linkElement = (Element) headElement.appendChild(document.createElement("script"));
		linkElement.setAttribute("src", src);
	}

	private void createLink(Document document, Element headElement, String rel, String href) {
		Element linkElement = (Element) headElement.appendChild(document.createElement("link"));
		linkElement.setAttribute("rel", rel);
		linkElement.setAttribute("href", href);
	}

	// <link rel="stylesheet" href="http://code.jquery.com/mobile/1.0a2/jquery.mobile-1.0a2.min.css" />
	private EAttribute createEAttribute(String name, EDataType dataType) {
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(dataType);
		return attribute;
	}
	
}
