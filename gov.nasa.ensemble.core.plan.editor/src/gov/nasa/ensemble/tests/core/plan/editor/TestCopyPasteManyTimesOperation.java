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
package gov.nasa.ensemble.tests.core.plan.editor;

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.SelectionProviderAdapter;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.PlanCopyPasteManyTimesOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.Assert;

/**
 * The first digit is the number of copies to make.
 * The second digit is the number of activities to copy.
 * The third digit is the number of activity groups to copy.
 * 
 */
public class TestCopyPasteManyTimesOperation extends UndoableOperationTestCase {
	
	private static final Shell SHELL = new Shell((Shell)null);

	// 0 activities only
	//=======================================================================
	// make 0 copies
	public void testPlanCopyPasteManyTimesOperation000() {
		IStructuredSelection targetSelection = StructuredSelection.EMPTY;
		int copies = 0;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		assertFalse(operation.canExecute());
	}
	
	// make 1 copy
	public void testPlanCopyPasteManyTimesOperation100() {
		IStructuredSelection targetSelection = StructuredSelection.EMPTY;
		int copies = 1;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		assertFalse(operation.canExecute());
	}
	
	// make 2 copies
	public void testPlanCopyPasteManyTimesOperation200() {
		IStructuredSelection targetSelection = StructuredSelection.EMPTY;
		int copies = 2;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		assertFalse(operation.canExecute());
	}
	
	//=======================================================================		
	
	// 1 activity only
	//=======================================================================
	// make 0 copies
	public void testPlanCopyPasteManyTimesOperation010() {
		IStructuredSelection targetSelection = getTargetSelection1Activity();
		int copies = 0;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	
	// make 1 copy
	public void testPlanCopyPasteManyTimesOperation110() {
		IStructuredSelection targetSelection = getTargetSelection1Activity();
		int copies = 1;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);	
	}
	
	// make 2 copies
	public void testPlanCopyPasteManyTimesOperation210() {
		IStructuredSelection targetSelection = getTargetSelection1Activity();
		int copies = 2;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	//=======================================================================	
	
	// 2 activities only
	//=======================================================================
	// make 0 copies
	public void testPlanCopyPasteManyTimesOperation020() {
		IStructuredSelection targetSelection = getTargetSelection2Activities();
		int copies = 0;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	
	// make 1 copy
	public void testPlanCopyPasteManyTimesOperation120() {
		IStructuredSelection targetSelection = getTargetSelection2Activities();
		int copies = 1;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);	
	}
	
	// make 2 copies
	public void testPlanCopyPasteManyTimesOperation220() {
		IStructuredSelection targetSelection = getTargetSelection2Activities();
		int copies = 2;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	//=======================================================================

	// 1 activity group only
	//=======================================================================
	// make 0 copies
	public void testPlanCopyPasteManyTimesOperation001() {
		IStructuredSelection targetSelection = getTargetSelection1ActivityGroup();
		int copies = 0;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	
	// make 1 copy
	public void testPlanCopyPasteManyTimesOperation101() {
		IStructuredSelection targetSelection = getTargetSelection1ActivityGroup();
		int copies = 1;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);	
	}
	
	// make 2 copies
	public void testPlanCopyPasteManyTimesOperation201() {
		IStructuredSelection targetSelection = getTargetSelection1ActivityGroup();
		int copies = 2;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	//=======================================================================	
	
	// 2 activity groups only
	//=======================================================================
	// make 0 copies
	public void testPlanCopyPasteManyTimesOperation002() {
		IStructuredSelection targetSelection = getTargetSelection2ActivityGroups();
		int copies = 0;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	
	// make 1 copy
	public void testPlanCopyPasteManyTimesOperation102() {
		IStructuredSelection targetSelection = getTargetSelection2ActivityGroups();
		int copies = 1;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);	
	}
	
	// make 2 copies
	public void testPlanCopyPasteManyTimesOperation202() {
		IStructuredSelection targetSelection = getTargetSelection2ActivityGroups();
		int copies = 2;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	//=======================================================================	
	
	// 2 activities 2 activity groups
	//=======================================================================
	// make 0 copies
	public void testPlanCopyPasteManyTimesOperation022() {
		IStructuredSelection targetSelection = getTargetSelection2ActivityGroups2Activities();
		int copies = 0;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	
	// make 1 copy
	public void testPlanCopyPasteManyTimesOperation122() {
		IStructuredSelection targetSelection = getTargetSelection2ActivityGroups2Activities();
		int copies = 1;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);	
	}
	
	// make 2 copies
	public void testPlanCopyPasteManyTimesOperation222() {
		IStructuredSelection targetSelection = getTargetSelection2ActivityGroups2Activities();
		int copies = 2;
		SelectionProviderAdapter selectionProvider = new SelectionProviderAdapter();
		
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(targetSelection, modifier, copies, selectionProvider);
		IUndoContext undoContext = TransactionUtils.getUndoContext(targetSelection.getFirstElement());
		WidgetUtils.execute(operation, undoContext, SHELL, new Site(selectionProvider));
		
		assertCorrectFinalSelection(targetSelection, copies, selectionProvider);		
	}
	//=======================================================================		
	
	// TODO: create a method to test constraints too.
	
	// create a target selection containing one activity
	private IStructuredSelection getTargetSelection1Activity() {
		OperationTestPlanRecord record = new OperationTestPlanRecord();
		EPlanElement[] selectedElements = new EActivity[] { 
				record.activity1_2
				};
		EPlanElement[] targetElements = selectedElements;
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		return targetSelection;
	}	
	
	// create a target selection containing two activities
	private IStructuredSelection getTargetSelection2Activities() {
		OperationTestPlanRecord record = new OperationTestPlanRecord();
		EPlanElement[] selectedElements = new EActivity[] { 
				record.activity1_2, record.activity2_1
				};
		EPlanElement[] targetElements = selectedElements;
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		return targetSelection;
	}		
	
	private IStructuredSelection getTargetSelection1ActivityGroup() {
		OperationTestPlanRecord record = new OperationTestPlanRecord();
		EPlanElement[] selectedElements = new EActivityGroup[] { 
				record.group1
				};
		EPlanElement[] targetElements = selectedElements;
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		return targetSelection;		
	}	
	
	private IStructuredSelection getTargetSelection2ActivityGroups() {
		OperationTestPlanRecord record = new OperationTestPlanRecord();
		EPlanElement[] selectedElements = new EActivityGroup[] { 
				record.group1
				};
		EPlanElement[] targetElements = selectedElements;
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		return targetSelection;		
	}	
	
	private IStructuredSelection getTargetSelection2ActivityGroups2Activities() {
		OperationTestPlanRecord record = new OperationTestPlanRecord();
		EPlanElement[] selectedElements = new EPlanElement[] { 
				record.group1, record.group2, record.activity3_1, record.activity3_2
				};
		EPlanElement[] targetElements = selectedElements;
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		return targetSelection;		
	}		
	
	// make sure that the final selection is of the correct size and that the original selection is not in it.
	private void assertCorrectFinalSelection(
			IStructuredSelection targetSelection, int copies,
			ISelectionProvider selectionProvider) {
		final List actualSelection = ((IStructuredSelection)selectionProvider.getSelection()).toList();
		assertEquals("Expected to have selected " + copies + " copies of "
					+ targetSelection + " but was " + actualSelection, 
			 targetSelection.size() * copies, actualSelection.size());
		for(Object object : targetSelection.toList())
			assertFalse(actualSelection.contains(object));
	}
	
	/**
	 * A site that just provides a selection provider
	 * 
	 * @author abachman
	 */
	public class Site implements IWorkbenchSite {

		private final SelectionProviderAdapter selectionProvider;

		public Site(SelectionProviderAdapter selectionProvider) {
			this.selectionProvider = selectionProvider;
		}

		@Override
		public Object getAdapter(Class adapter) {
			Assert.fail();
			return null;
		}

		@Override
		public Object getService(Class api) {
			Assert.fail();
			return null;
		}

		@Override
		public boolean hasService(Class api) {
			Assert.fail();
			return false;
		}

		@Override
		public IWorkbenchPage getPage() {
			Assert.fail();
			return null;
		}

		@Override
		public ISelectionProvider getSelectionProvider() {
			return selectionProvider;
		}

		@Override
		public Shell getShell() {
			Assert.fail();
			return null;
		}

		@Override
		public IWorkbenchWindow getWorkbenchWindow() {
			Assert.fail();
			return null;
		}

		@Override
		public void setSelectionProvider(ISelectionProvider provider) {
			Assert.fail();
		}

	}

}
