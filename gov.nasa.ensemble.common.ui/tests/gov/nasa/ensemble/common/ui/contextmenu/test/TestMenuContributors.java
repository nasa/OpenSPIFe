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
package gov.nasa.ensemble.common.ui.contextmenu.test;

import gov.nasa.ensemble.common.ui.contextmenu.test.ExampleOfMenuContributorContainer.*;
import gov.nasa.ensemble.common.ui.treemenu.IContextualCommandContributor;
import gov.nasa.ensemble.common.ui.treemenu.ContextualCommandContributor;
import gov.nasa.ensemble.common.ui.treemenu.TreeContextMenuFactory;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestMenuContributors extends TestCase {

	private class TestableTreeContextMenuFactory extends TreeContextMenuFactory {
		public TestableTreeContextMenuFactory(IContextualCommandContributor... contributorProviders) {
			// Just to avoid confusing compiler with no-argument constructor.
			super(contributorProviders);
		}

		@Override
		protected List<ContextualCommandContributor> getMenuContributors(Object objectInTree) {
			// Just to make this protected method visible to the test.
			return super.getMenuContributors(objectInTree);
		}
	}
	
	private TestableTreeContextMenuFactory FACTORY
	= new TestableTreeContextMenuFactory(
			new ExampleOfMenuContributorContainer(),
			new ExampleOfMenuContributorStandalone());

	public void testCorrectMenuItemsAppear() {
		assertMenuCommands(0.0, CommandToRound.class);
		assertMenuCommands(0, CommandToDouble.class, CommandToHalve.class);
		assertMenuCommands(42, CommandToDivide.class, CommandToDouble.class, CommandToHalve.class);
		assertMenuCommands(41, CommandToDivide.class, CommandToDouble.class);
		assertMenuCommands("foo", CommandToUppercase.class, ExampleOfMenuContributorStandalone.class);
	}

	private void assertMenuCommands(Object treeObject, Class<? extends ContextualCommandContributor>... expectedClasses) {
		List<ContextualCommandContributor> actualInstances = FACTORY.getMenuContributors(treeObject);
		List<Class> actualClasses = new ArrayList<Class>(actualInstances.size());
		for (ContextualCommandContributor instance : actualInstances) {
			actualClasses.add(instance.getClass());
		}
		if(!(expectedClasses.equals(actualClasses))) {
			assertEquals("Incorrect menu item classes contributed for treeObject="
					+ treeObject + " -- ",
					classNames(expectedClasses), classNames(actualClasses.toArray(new Class[0])));
		}
	}

	private String classNames(Class[] objects) {
		if (objects.length==0) return "<none>";
		StringBuilder s = new StringBuilder();
		s.append('[');
		for (Class c : objects) {
			s.append(c==null? "(null)" : c.getSimpleName());
			s.append(", ");
		}
		s.delete(s.length()-2, s.length());  // delete last comma and space
		s.append(']');
		return s.toString();
	}
	
}
