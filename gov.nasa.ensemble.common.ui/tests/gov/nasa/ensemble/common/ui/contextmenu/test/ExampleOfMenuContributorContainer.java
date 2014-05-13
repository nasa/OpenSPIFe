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

import gov.nasa.ensemble.common.ui.treemenu.ContextualCommandContributor;
import gov.nasa.ensemble.common.ui.treemenu.MenuContributorContainer;

public class ExampleOfMenuContributorContainer extends MenuContributorContainer {
	
	public void dummyAction() {
		// No-op.
	}

	public class CommandToDouble extends ContextualCommandContributor<Integer> {
		@Override
		public String getMenuItemName(Integer ignore) {
			return "Double this integer";
		}

		@Override
		public void performAction(Integer objectInTree) {
			dummyAction();
		}
	}

	public class CommandToHalve extends ContextualCommandContributor<Integer> {
		@Override
		public String getMenuItemName(Integer ignore) {
			return "Halve this even number";
		}

		@Override
		public boolean isApplicable(Integer treeItem) {
			return treeItem%2==0;
		}

		@Override
		public void performAction(Integer objectInTree) {
			dummyAction();
		}
	}

	public class IrrelevantEmbeddedClass {
		// Just to test that this doesn't get in the way.
	}
	
	public class CommandToRound extends ContextualCommandContributor<Double> {
		@Override
		public String getMenuItemName(Double ignore) {
			return "Round to a whole number";
		}

		@Override
		public void performAction(Double objectInTree) {
			dummyAction();
		}
	}

	public class CommandToDivide extends ContextualCommandContributor<Number> {
		@Override
		public String getMenuItemName(Number ignore) {
			return "Divide by this number";
		}

		@Override
		public boolean isApplicable(Number treeItem) {
			return !treeItem.equals(0) && !treeItem.equals(0.0);
		}
		@Override
		public void performAction(Number objectInTree) {
			dummyAction();
		}
	}
	
	public class CommandToUppercase extends ContextualCommandContributor<String> {
		@Override
		public String getMenuItemName(String ignore) {
			return "Convert to uppercase";
		}

		@Override
		public void performAction(String objectInTree) {
			dummyAction();
		}

	}

}
