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
package gov.nasa.ensemble.core.plan.formula.js;

import gov.nasa.ensemble.common.extension.DynamicExtensionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.plan.formula.js.constraint.JSAnnotationClientSelector;
import org.junit.Assert;

import org.eclipse.core.runtime.IStatus;
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

public class TestJSAnnotationConstraint extends Assert {

	private static EcoreFactory FACTORY = EcoreFactory.eINSTANCE;
	private static final IBatchValidator batchValidator = (IBatchValidator)ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
	
	static {
		batchValidator.setIncludeLiveConstraints(true);	
	}
	
	@BeforeClass
	public static void registerTestPackage() throws Exception {
		JSAnnotationClientSelector.registerPackage("platform:/resource/test", Activator.getDefault().getBundle());
		// wait until jobs kicked off by adding the contribution are complete
		DynamicExtensionUtils.waitForJobs();
	}
	
	@AfterClass
	public static void unregisterTestPackage() throws Exception {
		JSAnnotationClientSelector.unregisterPackage("platform:/resource/test");
	}
	
	private EPackage testPackage;
	private EFactory testFactory;
	private EClass animal;
	private EClass dog;
	private EAttribute birthYear;
	private EAttribute deathYear;
	
	@Before
	public void setUp() throws Exception {
		testPackage = FACTORY.createEPackage();
		testPackage.setName("TestPackage");
		testPackage.setNsPrefix("test");
		testPackage.setNsURI("platform:/resource/test");
		
		animal = FACTORY.createEClass();
		animal.setName("Animal");
		testPackage.getEClassifiers().add(animal);
		
		birthYear = FACTORY.createEAttribute();
		birthYear.setName("birthYear");
		birthYear.setEType(EcorePackage.Literals.EINT);
		animal.getEStructuralFeatures().add(birthYear);
		EMFUtils.addAnnotation(birthYear, "constraint", "birthRange", "1900 <= $value && $value <= 2400");
		EMFUtils.addAnnotation(birthYear, "message", "birthRange", "must be between 1900 and 2400 inclusive");
		
		deathYear = FACTORY.createEAttribute();
		deathYear.setName("deathYear");
		deathYear.setEType(EcorePackage.Literals.EINT);
		animal.getEStructuralFeatures().add(deathYear);
		
		EMFUtils.addAnnotation(animal, "constraint", "birthBeforeDeath", "birthYear <= deathYear");
		EMFUtils.addAnnotation(animal, "message", "birthBeforeDeath", "The death year of {0} cannot be before its birth year");
		EMFUtils.addAnnotation(animal, "constraint", "maximumAge", "(deathYear - birthYear) <= 300");
		EMFUtils.addAnnotation(animal, "message", "maximumAge", "The maximum age of an animal is 300 years");
		

		dog = FACTORY.createEClass();
		dog.setName("Dog");
		testPackage.getEClassifiers().add(dog);
		dog.getESuperTypes().add(animal);
		
		EMFUtils.addAnnotation(dog, "constraint", "maximumAge", "(deathYear - birthYear) <= 20");
		EMFUtils.addAnnotation(dog, "message", "maximumAge", "The maximum age of a dog is 20 years");
		
		testFactory = testPackage.getEFactoryInstance();
	}
	
	@Test
	public void testAnimalBirthOutOfRange() {
		EObject generic = testFactory.create(animal);
		generic.eSet(birthYear, 2600);
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalDiedBeforeBirth() {
		EObject generic = testFactory.create(animal);
		generic.eSet(birthYear, 2010);
		generic.eSet(deathYear, 2009);
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalTooOld() {
		EObject generic = testFactory.create(animal);
		generic.eSet(birthYear, 2010);
		generic.eSet(deathYear, 2500);
		assertFalse(validateConstraints(generic));
	}
	
	@Test
	public void testAnimalOK() {
		EObject generic = testFactory.create(animal);
		generic.eSet(birthYear, 2010);
		generic.eSet(deathYear, 2100);
		assertTrue(validateConstraints(generic));
	}
	
	@Test
	public void testDogBirthOutOfRange() {
		EObject fido = testFactory.create(dog);
		fido.eSet(birthYear, 2600);
		assertFalse(validateConstraints(fido));
	}
	
	@Test
	public void testDogDiedBeforeBirth() {
		EObject fido = testFactory.create(dog);
		fido.eSet(birthYear, 2010);
		fido.eSet(deathYear, 2009);
		assertFalse(validateConstraints(fido));
	}
	
	@Test
	public void testDogTooOld() {
		EObject fido = testFactory.create(dog);
		fido.eSet(birthYear, 2010);
		fido.eSet(deathYear, 2035);
		assertFalse(validateConstraints(fido));
	}
	
	@Test
	public void testDogOK() {
		EObject fido = testFactory.create(dog);
		fido.eSet(birthYear, 2010);
		fido.eSet(deathYear, 2020);
		assertTrue(validateConstraints(fido));
	}
	
	private boolean validateConstraints(EObject target) {
		final IStatus status = batchValidator.validate(target);
		return status.isOK();
	}
}
