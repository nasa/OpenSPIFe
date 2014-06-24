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
package gov.nasa.arc.spife.core.plan.pear.view.ui;

import gov.nasa.ensemble.common.ui.UndoRedoUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.core.detail.emf.treetable.EMFTreeTableUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.CopyAction;
import org.eclipse.emf.edit.ui.action.CutAction;
import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.emf.edit.ui.action.PasteAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

public class ProfileEffectsAndRequirementsPage extends Page {

	private final IViewSite viewSite;
	private Composite composite = null;
	private TabFolder tabFolder;
	private TreeTableViewer<EObject, EAttribute> effectTableViewer = null;
	private TreeTableViewer<EObject, EAttribute> envelopeTableViewer = null;
	private TreeTableViewer<EObject, EAttribute> equalityTableViewer = null;
	private ISelectionListener listener = new Listener();

	private PlanEditorModel planEditorModel;
	private ProfileMember currentInput;
	private final ISelectionProvider selectionProvider;

	private CutAction cutAction = null;
	private CopyAction copyAction = null;
	private PasteAction pasteAction = null;

	private DeleteAction deleteAction = null;

	public ProfileEffectsAndRequirementsPage(IViewSite viewSite, IEditorPart editor, PlanEditorModel planEditorModel) {
		this.planEditorModel = planEditorModel;
		this.selectionProvider = editor.getSite().getSelectionProvider();
		this.viewSite = viewSite;
	}

	public void addRow() {
		if (currentInput == null) {
			return;
		}
		int index = tabFolder.getSelectionIndex();
		EClass eClass = getEClassForSelectionIndex(index);
		if (eClass != null) {
			EFactory factory = eClass.getEPackage().getEFactoryInstance();
			EObject eObject = factory.create(eClass);
			Object feature = getEReferenceForSelectionIndex(index);
			EditingDomain domain = planEditorModel.getEditingDomain();
			Command command = AddCommand.create(domain, currentInput, feature, Collections.singleton(eObject));
			EMFUtils.executeCommand(domain, command);
		}
	}

	public void removeRow() {
		if (currentInput == null) {
			return;
		}
		TreeTableViewer viewer = null;
		int index = tabFolder.getSelectionIndex();
		switch (index) {
		case 0:
			viewer = equalityTableViewer;
			break;
		case 1:
			viewer = envelopeTableViewer;
			break;
		case 2:
			viewer = effectTableViewer;
			break;
		}
		if (viewer != null) {
			ISelection selection = viewer.getSelection();
			if (selection instanceof StructuredSelection) {
				List list = ((StructuredSelection) selection).toList();
				EditingDomain domain = planEditorModel.getEditingDomain();
				Command command = RemoveCommand.create(domain, list);
				EMFUtils.executeCommand(domain, command);
			}
		}
	}

	@Override
	public void createControl(Composite parent) {
		EditingDomain editingDomain = planEditorModel.getEditingDomain();

		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		tabFolder = new TabFolder(composite, SWT.BORDER);
		equalityTableViewer = createEMFTreeTableViewer(editingDomain, tabFolder, 0, "Equality Requirements");
		envelopeTableViewer = createEMFTreeTableViewer(editingDomain, tabFolder, 1, "Min/Max Requirements");
		effectTableViewer = createEMFTreeTableViewer(editingDomain, tabFolder, 2, "Effects");
		effectTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (!selection.equals(selectionProvider.getSelection())) {
					selectionProvider.setSelection(selection);
				}
			}
		});

		IPageSite site = getSite();
		site.setSelectionProvider(selectionProvider);
		IWorkbenchPage page = site.getPage();
		page.addPostSelectionListener(listener);
		listener.selectionChanged(null, selectionProvider.getSelection());

		UndoRedoUtils.setupUndoRedo(site.getActionBars(), viewSite, planEditorModel.getUndoContext());
	}

	private TreeTableViewer<EObject, EAttribute> createEMFTreeTableViewer(EditingDomain editingDomain, TabFolder tabFolder, int indexLookup, String tabName) {
		EReference reference = getEReferenceForSelectionIndex(indexLookup);
		EClass eClass = getEClassForSelectionIndex(indexLookup);
		TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(tabName);

		TreeTableViewer<EObject, EAttribute> viewer = EMFTreeTableUtils.createEMFTreeTableViewer(tabFolder, reference, eClass, editingDomain, true, true, true, true);
		tabItem.setControl(viewer.getControl());
		return viewer;
	}

	@Override
	public void setActionBars(IActionBars actionBars) {
		effectTableViewer.setActionBars(actionBars);
		envelopeTableViewer.setActionBars(actionBars);
		equalityTableViewer.setActionBars(actionBars);
		super.setActionBars(actionBars);

		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

		EditingDomain editingDomain = planEditorModel.getEditingDomain();

		cutAction = new CutActionImpl(editingDomain);
		cutAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);

		copyAction = new CopyAction(editingDomain);
		copyAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

		pasteAction = new PasteActionImpl(editingDomain);
		pasteAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);

		deleteAction = new DeleteActionImpl(editingDomain);
		deleteAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);

		addPostSelectionChangedListeners(cutAction);
		addPostSelectionChangedListeners(copyAction);
		addPostSelectionChangedListeners(pasteAction);
		addPostSelectionChangedListeners(deleteAction);

		IMenuListener menuListener = new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager menuManager) {
				menuManager.add(new Separator("edit"));
				menuManager.add(new ActionContributionItem(cutAction));
				menuManager.add(new ActionContributionItem(copyAction));
				menuManager.add(new ActionContributionItem(pasteAction));
				menuManager.add(new Separator());
				menuManager.add(new ActionContributionItem(deleteAction));
			}
		};
		WidgetUtils.createContextMenu(effectTableViewer.getTree(), menuListener);
		WidgetUtils.createContextMenu(envelopeTableViewer.getTree(), menuListener);
		WidgetUtils.createContextMenu(equalityTableViewer.getTree(), menuListener);
	}

	@Override
	public Control getControl() {
		return composite;
	}

	@Override
	public void dispose() {
		removePostSelectionChangedListeners(cutAction);
		removePostSelectionChangedListeners(copyAction);
		removePostSelectionChangedListeners(pasteAction);
		removePostSelectionChangedListeners(deleteAction);

		composite = null;
		planEditorModel = null;
		IPageSite site = getSite();
		site.setSelectionProvider(null);
		IWorkbenchPage page = site.getPage();
		page.removePostSelectionListener(listener);
	}

	@Override
	public void setFocus() {
		composite.setFocus();
	}

	private void setSelection(ISelection selection) {
		ProfileMember treeTableInput = null;
		if (selection instanceof IStructuredSelection) {
			List list = ((IStructuredSelection) selection).toList();
			for (Object object : list) {
				ProfileMember member = null;
				if (object instanceof EPlanElement) {
					EPlanElement pe = (EPlanElement) object;
					ProfileMember profileMember = pe.getMember(ProfileMember.class);
					member = profileMember;
				} else if (object instanceof ProfileReference) {
					ProfileMember profileMember = (ProfileMember) ((ProfileReference) object).eContainer();
					member = profileMember;
				}
				if (treeTableInput == null) {
					treeTableInput = member;
				} else if (treeTableInput != member) {
					// do not support multiple members
					treeTableInput = null;
					break;
				}
			}
		}
		if (currentInput != treeTableInput) {
			setTreeTableInput(treeTableInput);
		}
	}

	private void setTreeTableInput(ProfileMember profileMember) {
		currentInput = profileMember;
		effectTableViewer.setInput(profileMember);
		envelopeTableViewer.setInput(profileMember);
		equalityTableViewer.setInput(profileMember);
	}

	private EReference getEReferenceForSelectionIndex(int index) {
		EReference feature = null;
		switch (index) {
		case 0:
			feature = ProfilePackage.Literals.PROFILE_MEMBER__CONSTRAINTS;
			break;
		case 1:
			feature = ProfilePackage.Literals.PROFILE_MEMBER__CONSTRAINTS;
			break;
		case 2:
			feature = ProfilePackage.Literals.PROFILE_MEMBER__EFFECTS;
			break;
		}
		return feature;
	}

	private EClass getEClassForSelectionIndex(int index) {
		EClass eClass = null;
		switch (index) {
		case 0:
			eClass = ProfilePackage.Literals.PROFILE_EQUALITY_CONSTRAINT;
			break;
		case 1:
			eClass = ProfilePackage.Literals.PROFILE_ENVELOPE_CONSTRAINT;
			break;
		case 2:
			eClass = ProfilePackage.Literals.PROFILE_EFFECT;
			break;
		}
		return eClass;
	}

	private void addPostSelectionChangedListeners(ISelectionChangedListener list) {
		effectTableViewer.addPostSelectionChangedListener(list);
		envelopeTableViewer.addPostSelectionChangedListener(list);
		equalityTableViewer.addPostSelectionChangedListener(list);
	}

	private void removePostSelectionChangedListeners(ISelectionChangedListener list) {
		effectTableViewer.removePostSelectionChangedListener(list);
		envelopeTableViewer.removePostSelectionChangedListener(list);
		equalityTableViewer.removePostSelectionChangedListener(list);
	}

	private final class DeleteActionImpl extends DeleteAction {
		private DeleteActionImpl(EditingDomain domain) {
			super(domain);
		}

		@Override
		public void run() {
			EMFUtils.executeCommand(domain, command);
		}
	}

	private final class CutActionImpl extends CutAction {
		private CutActionImpl(EditingDomain domain) {
			super(domain);
		}

		@Override
		public void run() {
			EMFUtils.executeCommand(domain, command);
		}
	}

	private final class PasteActionImpl extends PasteAction {
		private PasteActionImpl(EditingDomain domain) {
			super(domain);
		}

		@Override
		public Command createCommand(Collection<?> selection) {
			int index = tabFolder.getSelectionIndex();
			EReference feature = getEReferenceForSelectionIndex(index);
			ProfileMember owner = currentInput;
			return PasteFromClipboardCommand.create(domain, owner, feature);
		}

		@Override
		public void run() {
			EMFUtils.executeCommand(domain, command);
		}
	}

	private class Listener implements ISelectionListener {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (part instanceof ProfileEffectsAndRequirementsView) {
				return;
			}
			setSelection(selection);
		}

	}

}
