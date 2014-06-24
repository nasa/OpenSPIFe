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
package gov.nasa.ensemble.emf.transaction;

import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Test;

public class TestTransactionUtils extends TestCase {

	@Test
	public void testNonTransactionWritting() throws IOException {
		EditingDomain editingDomain = EMFUtils.createEditingDomain();
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.createResource(URI.createFileURI(FileUtilities.createTempFile("junit", "txt").toString()));
		
		EObject object = new EObjectImpl() {
			// make non-abstract
		};
		resource.getContents().add(object);
		assertEquals(resource, object.eResource());
		
		TransactionUtils.writing(object, new Runnable() {

			@Override
			public void run() {
				// trivial operation
			}
			
		});
		
		assertEquals(resource, object.eResource());
	}
	
}
