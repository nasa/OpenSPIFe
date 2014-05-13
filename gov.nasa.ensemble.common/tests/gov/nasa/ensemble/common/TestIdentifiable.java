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
package gov.nasa.ensemble.common;


import junit.framework.TestCase;

public class TestIdentifiable extends TestCase {

	/**
	 * This method tests the generation of unique ids for objects with transient identity
	 * and the retrieval of these objects by unique id.  Also checks apgen compatibility
	 * for the generated unique ids along the way.
	 */
	public void testGenerateUniqueIdAndGetIdentifiable() {
		IdentifiableRegistry<AnIdentifiable> identifiableRegistry = new IdentifiableRegistry<AnIdentifiable>();
		for (int i = 0 ; i < 10000 ; i++) {
			AnIdentifiable a = new AnIdentifiable();
			AnIdentifiable b = new AnIdentifiable();
			String uniqueIdA = identifiableRegistry.generateUniqueId(a);
			String uniqueIdB = identifiableRegistry.generateUniqueId(b);
			checkApgenCompatibility(uniqueIdA);
			checkApgenCompatibility(uniqueIdB);
			assertFalse(uniqueIdA.equals(uniqueIdB));
			AnIdentifiable a2 = identifiableRegistry.getIdentifiable(AnIdentifiable.class, uniqueIdA);
			assertSame(a, a2);
			AnIdentifiable b2 = identifiableRegistry.getIdentifiable(AnIdentifiable.class, uniqueIdB);
			assertSame(b, b2);
		}
	}

	/**
	 * This method tests the generation of unique ids for objects with persistent identity
	 * and the retrieval of these objects by unique id.
	 */
	public void testRegisterIdentifiableAndGetIdentifiable() {
		IdentifiableRegistry<IdentityPersisted> identifiableRegistry = new IdentifiableRegistry<IdentityPersisted>();
		IdentityPersisted[] identifiables = getPersistedIdentifiables(identifiableRegistry);
		for (int i = 0 ; i < 100 ; i++) {
			String uniqueId = String.valueOf(i);
			assertEquals(uniqueId, identifiables[i].getUniqueId());
			IdentityPersisted persisted = identifiableRegistry.getIdentifiable(IdentityPersisted.class, uniqueId);
			assertSame(identifiables[i], persisted);
			identifiableRegistry.releaseIdentifiable(identifiables[i]);
			assertNull(identifiableRegistry.getIdentifiable(IdentityPersisted.class, uniqueId));
		}
	}

	/*
	 * Utility functions
	 */
	
	/**
	 * Check the unique id for compatibility with apgen.
	 * 
	 * @param uniqueId
	 */
	private void checkApgenCompatibility(String uniqueId) {
		// must include at least one _
		int underscore = uniqueId.lastIndexOf('_');
		assertFalse(underscore == -1);
		// must start with any combination of letters, numbers, and _ (regex "word" characters)
		String alphanumerics_ = uniqueId.substring(0, underscore);
		assertTrue(alphanumerics_.matches("\\w+"));
		// must finish with numbers (regex "digit" characters)
		String digits = uniqueId.substring(underscore + 1, uniqueId.length());
		assertTrue(digits.matches("\\d+"));
	}
	
	/**
	 * This method simulates a database read operation, or a file read operation
	 * @param identifiableRegistry 
	 * @return a list of identifiables that were 'persisted'
	 */
	private IdentityPersisted[] getPersistedIdentifiables(IdentifiableRegistry<IdentityPersisted> identifiableRegistry) {
		int count = 100;
		IdentityPersisted[] results = new IdentityPersisted[count];
		for (int i = 0 ; i < count ; i++) {
			results[i] = new IdentityPersisted(identifiableRegistry, String.valueOf(i));
		}
		return results;
	}
	
	/**
	 * This class is a simulated object with persistent identity.  The unique id
	 * is stored externally when the object is persisted. (file, database, etc.) 
	 */
	private final class IdentityPersisted {
		
		private final String uniqueId;

		IdentityPersisted(IdentifiableRegistry<IdentityPersisted> identifiableRegistry, String uniqueId) {
			this.uniqueId = uniqueId;
			identifiableRegistry.registerIdentifiable(this, uniqueId);
		}
		
		public String getUniqueId() {
			return uniqueId;
		}
		
	}
	
	/**
	 * This class is a simulated object with transient identity.  The unique id
	 * is not persisted and is created each time the object is loaded, created, etc.
	 */
	private final class AnIdentifiable {

		// ids now completely external
		
	}

}
