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
package gov.nasa.ensemble.emf.constraint;

import gov.nasa.ensemble.common.extension.DynamicExtensionUtils;
import gov.nasa.ensemble.emf.Activator;
import gov.nasa.ensemble.emf.util.EMFUtils;
import org.junit.Assert;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("restriction")
public class TestAttributeValueConstraints extends Assert {

	private static EcoreFactory FACTORY = EcoreFactory.eINSTANCE;
	private static final IBatchValidator batchValidator =
		(IBatchValidator)ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
	
	static {
		batchValidator.setIncludeLiveConstraints(true);	
	}
	
	@BeforeClass
	public static void registerTestPackage() throws Exception {
		ModelCommonClientSelector.registerPackage("platform:/resource/test", Platform.getBundle(Activator.PLUGIN_ID));
		// wait until jobs kicked off by adding the contribution are complete
		DynamicExtensionUtils.waitForJobs();
	}
	
	@AfterClass
	public static void unregisterTestPackage() throws Exception {
		ModelCommonClientSelector.unregisterPackage("platform:/resource/test");
	}
	
	private EPackage testPackage;
	private EFactory testFactory;
	private EClass animal;
	private EAttribute name;
	private EAttribute birthYear;
	private EAttribute deathYear;
	private EAttribute age;
	
	@Before
	public void setUp() throws Exception {
		testPackage = FACTORY.createEPackage();
		testPackage.setName("TestPackage");
		testPackage.setNsPrefix("test");
		testPackage.setNsURI("platform:/resource/test");
		
		animal = FACTORY.createEClass();
		animal.setName("Animal");
		testPackage.getEClassifiers().add(animal);
		
		name = FACTORY.createEAttribute();
		name.setName("birthYear");
		name.setEType(EcorePackage.Literals.ESTRING);
		animal.getEStructuralFeatures().add(name);
		EMFUtils.addAnnotation(name, "detail", "charlen", "10");	
		
		birthYear = FACTORY.createEAttribute();
		birthYear.setName("birthYear");
		birthYear.setEType(EcorePackage.Literals.EINT);
		animal.getEStructuralFeatures().add(birthYear);
		EMFUtils.addAnnotation(birthYear, "detail", "limit", "1900,2400");		
		
		deathYear = FACTORY.createEAttribute();
		deathYear.setName("deathYear");
		deathYear.setEType(EcorePackage.Literals.EINT);
		animal.getEStructuralFeatures().add(deathYear);
		EMFUtils.addAnnotation(deathYear, "detail", "limit", ",2500");
		
		age = FACTORY.createEAttribute();
		age.setName("age");
		age.setEType(EcorePackage.Literals.EINT);
		animal.getEStructuralFeatures().add(age);
		EMFUtils.addAnnotation(age, "detail", "limit", "0,");
		
		testFactory = testPackage.getEFactoryInstance();
	}
	
	@Test
	public void testAnimalNameTooLing() {
		EObject generic = testFactory.create(animal);
		generic.eSet(name, "Indomitable");
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalBirthTooEarly() {
		EObject generic = testFactory.create(animal);
		generic.eSet(birthYear, 2600);
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalBirthTooLate() {
		EObject generic = testFactory.create(animal);
		generic.eSet(birthYear, 2600);
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalDeathTooLate() {
		EObject generic = testFactory.create(animal);
		generic.eSet(deathYear, 2600);
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalNegativeAge() {
		EObject generic = testFactory.create(animal);
		generic.eSet(age, -50);
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalOK() {
		EObject generic = testFactory.create(animal);
		generic.eSet(name, "Fido");
		generic.eSet(birthYear, 2010);
		generic.eSet(deathYear, 2019);
		generic.eSet(age, 9);
		assertTrue(validateConstraints(generic));
	}
	
	private boolean validateConstraints(EObject target) {
		final IStatus status = batchValidator.validate(target);
		return status.isOK();
	}
}
