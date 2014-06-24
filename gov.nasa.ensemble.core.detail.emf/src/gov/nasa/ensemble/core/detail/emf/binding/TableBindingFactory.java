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
package gov.nasa.ensemble.core.detail.emf.binding;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailFormToolkit;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.IBindingFactory;
import gov.nasa.ensemble.core.detail.emf.ITableBindingFactoryToolbarContributor;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemIntersectionPropertySource;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertySource;
import gov.nasa.ensemble.core.detail.emf.treetable.EMFTreeTableContentProvider;
import gov.nasa.ensemble.core.detail.emf.treetable.EMFTreeTableUtils;
import gov.nasa.ensemble.core.detail.emf.treetable.EObjectIndexPair;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.CommandUndoableOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.BasicFeatureMapEntry;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TableBindingFactory extends BindingFactory {
	private static final String ADD_IMAGE_KEY = "/icons/add.png";
	private static final String DELETE_IMAGE_KEY = "/icons/delete.png";
	private static final List<ITableBindingFactoryToolbarContributor> TOOLBAR_CONTRIBUTORS =
		ClassRegistry.createInstances(ITableBindingFactoryToolbarContributor.class);
	private DataBindingContext dataBindingContext = new EMFDataBindingContext();
	
	@Override
	public Binding createBinding(DetailProviderParameter parameter) {
		EObject model = parameter.getTarget();
		EditingDomain editingDomain = EMFUtils.getAnyDomain(model);
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		Object feature = pd.getFeature(model);
		if(feature instanceof EReference) {
			createReferenceBinding(parameter, model, editingDomain, pd, feature);
		} 
		/*else if(feature instanceof EAttribute) {
			createAttributeBinding(parameter, model, editingDomain, pd, feature);
		}
		*/
		return null;
	}

	private void createReferenceBinding(DetailProviderParameter parameter, EObject model, final EditingDomain editingDomain, IItemPropertyDescriptor pd, Object feature) {
		String displayName = pd.getDisplayName(model);
		if (model instanceof MultiEObject) {
			LogUtil.warn("Tables not supported for multiple selection - " + displayName);
			return;
		}
		EReference reference = (EReference) feature;
		Composite parent = parameter.getParent();
		FormToolkit toolkit = parameter.getDetailFormToolkit();
		Section section = DetailFormToolkit.createSection(toolkit, parent, displayName, null);
		section.setLayout(new RowLayout(SWT.VERTICAL));
		Composite composite = toolkit.createComposite(section);
		section.setClient(composite);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).applyTo(composite);
		
		Composite topButtonComposite = toolkit.createComposite(section, SWT.NO_BACKGROUND);
		topButtonComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		section.setTextClient(topButtonComposite);
		
		boolean editable = pd.canSetProperty(model);
		final TreeTableViewer<EObject, EAttribute> viewer = EMFTreeTableUtils.createEMFTreeTableViewer(composite, reference, reference.getEReferenceType(), editingDomain, false, true, true, editable);
		TreeTableComposite treeTableComposite = viewer.getTreeTableComposite();
		GridDataFactory.fillDefaults().grab(true, false).applyTo(treeTableComposite);

		if (editable) {
			addEditButton(composite, displayName, editingDomain, toolkit, viewer);
			populateTopButtonComposite(topButtonComposite, displayName, viewer, reference, editingDomain, model, editable, toolkit);
		}
		if (topButtonComposite.getChildren().length == 0) {
			section.setTextClient(null);
		}

		viewer.setInput(model);
		TreeColumn[] columns = viewer.getTree().getColumns();
		for (TreeColumn column : columns) {
			column.pack();
		}
		
		ISelectionChangedListener selectionChangedListener = parameter.getSelectionChangedListener();
		if (selectionChangedListener != null) {
			viewer.addSelectionChangedListener(selectionChangedListener);
		}
	}

	private Button addEditButton(final Composite composite, final String title, final EditingDomain domain, final FormToolkit toolkit, final TreeTableViewer viewer) {
		toolkit.setBorderStyle(SWT.BORDER);
		final Shell shell = composite.getShell();
		final Button edit = new Button(composite, SWT.NO_BACKGROUND);
		edit.setText("Edit");
		edit.setEnabled(false); //by default nothing is selected
		edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection s = viewer.getSelection();
				if (s instanceof StructuredSelection) {
					StructuredSelection structuredSelection = (StructuredSelection) s;
					if (!structuredSelection.isEmpty()) {
						EditTableDialog dialog = new EditTableDialog(shell, title, structuredSelection, domain, toolkit);
						dialog.open();
					}
				}
			}
		});
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection s = event.getSelection();
				if (s instanceof StructuredSelection) {
					edit.setEnabled(!s.isEmpty());
				} else {
					edit.setEnabled(false);
				}
			}
			
		});
		return edit;
	}

	private void populateTopButtonComposite(Composite composite, String displayName, TreeTableViewer<EObject, EAttribute> viewer, EReference reference, EditingDomain editingDomain, EObject model, boolean editable, FormToolkit toolkit) {
		for (ITableBindingFactoryToolbarContributor contributor : TOOLBAR_CONTRIBUTORS) {
			contributor.contribute(composite, displayName, viewer, reference);
		}		
		
		if (reference.getLowerBound() == reference.getUpperBound()) {
			return;
		}
		
		if (EMFTreeTableUtils.canAdd(reference)) {
			Button addButton = new Button(composite, SWT.PUSH);
			addButton.setImage(getAddImage());
			addButton.setToolTipText("Add " + displayName);
			SelectionListener addButtonSelectionListener = new AddButtonSelectionListener(editingDomain, model, addButton, reference);
			addButton.addSelectionListener(addButtonSelectionListener);
		}
		
		if (EMFTreeTableUtils.canRemove(reference)) {
			Button removeButton = new Button(composite, SWT.PUSH);
			removeButton.setToolTipText("Remove " + displayName);
			removeButton.setImage(getDeleteImage());
			RemoveButtonSelectionListener removeButtonSelectionListener = new RemoveButtonSelectionListener(editingDomain, viewer,removeButton);
			removeButton.addSelectionListener(removeButtonSelectionListener);
			viewer.getTree().addKeyListener(removeButtonSelectionListener);
		}
	}

	private Image getDeleteImage() {
		return getImage(DELETE_IMAGE_KEY);
	}
	
	private Image getImage(String key) {
		Activator activator = Activator.getDefault();
		ImageRegistry imageRegistry = activator.getImageRegistry();
		Image image = imageRegistry.get(key);
		if(image == null) {
			ImageDescriptor imageDescriptorFromPlugin = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, key);
			image = imageDescriptorFromPlugin.createImage();
			imageRegistry.put(key, image);
		}
		return image;
	}

	private Image getAddImage() {
		return getImage(ADD_IMAGE_KEY);
	}
	
	private class AddButtonSelectionListener extends SelectionAdapter {
		
		private EditingDomain editingDomain;
		private EObject model;
		private Button button;
		private IUndoableOperation operation;
		private EStructuralFeature structuralFeature;

		public AddButtonSelectionListener (final EditingDomain editingDomain, final EObject model, final Button button, EStructuralFeature structuralFeature) {
			this.model = model;
			this.structuralFeature = structuralFeature;
			this.editingDomain = editingDomain;
			this.button = button;
			this.operation = createOperation();
			if(operation == null) {
				LogUtil.warnOnce("null add operation for " +  structuralFeature);
			}
			boolean enable = false;
			if(operation != null && operation.canExecute()) {
				enable = true;
			}
			this.button.setEnabled(enable);
			
		}
		
		@Override
		public synchronized void widgetSelected(SelectionEvent e) {
			IUndoContext undoContext = EMFUtils.getUndoContext(editingDomain);
			CommonUtils.execute(operation, undoContext);
			
			this.operation = createOperation();
			this.button.setEnabled(operation.canExecute());
		}
		
		private IUndoableOperation createOperation() {
			Collection<?> newChildDescriptors = editingDomain==null? Collections.EMPTY_SET :
				editingDomain.getNewChildDescriptors(model, null);
			CommandParameter commandParameter = null;
			if(newChildDescriptors.size() > 1) {
				Iterator<?> iterator = newChildDescriptors.iterator();
				while(iterator.hasNext()) {
					CommandParameter cp = (CommandParameter) iterator.next();
					Object feature = cp.getFeature();
					if(structuralFeature.equals(feature)) {
						commandParameter = cp;
						break;
					} else if(cp.getValue() instanceof BasicFeatureMapEntry) { //TODO: How about this else if?
						BasicFeatureMapEntry entry = (BasicFeatureMapEntry) cp.getValue();
						feature = entry.getEStructuralFeature();
						if(structuralFeature.equals(feature)) {
							commandParameter = cp;
							break;
						}
						
					}
				}
			} else if(newChildDescriptors.size() > 0){
				commandParameter = (CommandParameter)newChildDescriptors.iterator().next();
			}
			
			if(commandParameter != null) {
				Object value = commandParameter.getValue();
				if(value instanceof EObject) {
					EObject child = (EObject) value;
					EList<EAttribute> attributes = child.eClass().getEAllAttributes();
					for(EAttribute attribute : attributes) {
						Class<?> instanceClass = attribute.getEType().getInstanceClass();
						if(instanceClass == String.class) {
							if(!attribute.isMany()) {
								child.eSet(attribute, "<" + instanceClass.getSimpleName() + ">");
							}
						}
					}
				}
				Command cmd = CreateChildCommand.create(editingDomain, model, commandParameter, Collections.singleton(value));
				return new CommandUndoableOperation(editingDomain, cmd);
			}

			return null;
		}
		
	}
	
	private class RemoveButtonSelectionListener extends SelectionAdapter implements KeyListener {
		
		private TreeTableViewer<EObject, EAttribute> treeTableViewer;
		private EditingDomain editingDomain;
		
		public RemoveButtonSelectionListener(final EditingDomain editingDomain, final TreeTableViewer<EObject, EAttribute> treeTableViewer, final Button button) {
			
			this.treeTableViewer = treeTableViewer;
			this.editingDomain = editingDomain;
			button.setEnabled(false);
			treeTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public synchronized void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) treeTableViewer.getSelection();
					List list = selection.toList();
					Command command = getRemoveCommand(editingDomain,
							treeTableViewer, list);
					button.setEnabled(command.canExecute());
					int size = selection.size();
					String toolTipText = button.getToolTipText();
					if(size > 1 && !toolTipText.endsWith("s")) {
						button.setToolTipText(toolTipText + "s");
					} else {
						int lastIndex = toolTipText.lastIndexOf("s");
						if(lastIndex == toolTipText.length() -1){
							button.setToolTipText(toolTipText.substring(0, lastIndex));
						}
					}
				}
			});
		}
		
		/**
		 * The remove command to return may be a simple straight-forward remove command
		 * or it may be a complicated remove command where we are trying to remove an item from a
		 * table open a multi-valued attribute.
		 * 
		 * @return an appropriate remove command.
		 */
		private Command getRemoveCommand(
				final EditingDomain editingDomain,
				final TreeTableViewer<EObject, EAttribute> treeTableViewer,
				List list) {
			Command command = RemoveCommand.create(editingDomain, list);
			if(command instanceof UnexecutableCommand) {
				IContentProvider contentProvider = treeTableViewer.getContentProvider();
				if(contentProvider instanceof EMFTreeTableContentProvider) {
					EMFTreeTableContentProvider emfContentProvider = (EMFTreeTableContentProvider)contentProvider;
					EStructuralFeature structuralFeature = emfContentProvider.getStructuralFeature();
					EObject owner = null;
					if(structuralFeature instanceof EAttribute && structuralFeature.isMany()) {
						List itemsToRemove = new ArrayList();
						for(Object object : list) {
							if(object instanceof EObjectIndexPair) {
								EObjectIndexPair pair = (EObjectIndexPair) object;
								if(owner == null) {
									owner = pair.getObject();
								} else if(owner.equals(pair.getObject())) {
									LogUtil.warn("object index pairs in the same list have different owners.");
									continue;
								}
								int index = pair.getIndex();
								List values = (List) owner.eGet(structuralFeature);
								Object selectedValue = values.get(index);
								itemsToRemove.add(selectedValue);										
							}
						}
						command = RemoveCommand.create(editingDomain, owner, structuralFeature, itemsToRemove);
					}
				}
			}
			return command;
		}		
		
		@Override
		public synchronized void widgetSelected(SelectionEvent e) {
			doRemoval();
		}

		private void doRemoval() {
			EMFUtils.executeCommand(editingDomain, createCommand());
		}
		
		private Command createCommand() {
			IStructuredSelection selection = (IStructuredSelection) treeTableViewer.getSelection();
			List list = selection.toList();
			return getRemoveCommand(editingDomain, treeTableViewer, list);		
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// do nothing
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.keyCode == SWT.DEL) {
				doRemoval();
			}
		}
	}
	
	private class EditTableDialog extends Dialog {

		private EObject target;
		private IItemPropertySource source;
		private FormToolkit toolkit;
		private String title;
		
		protected EditTableDialog(Shell parentShell, String title, IStructuredSelection selection, EditingDomain domain, FormToolkit toolkit) {
			super(parentShell);
			this.title = title;
			this.toolkit = toolkit;
			AdapterFactory domainAdapterFactory = EMFUtils.getAdapterFactory(domain);
			List elements = selection.toList();
			if (elements.size() == 1) {
				this.target = (EObject) elements.get(0); 
				this.source = (IItemPropertySource) domainAdapterFactory.adapt(target, IItemPropertySource.class);
			} else if (elements.size() > 1) {
				this.source = new MultiItemIntersectionPropertySource(elements);
				this.target = new MultiEObject(elements, (MultiItemPropertySource) source);
			} else {
				// do nothing if there's nothing selected in the table
			}
		}
		
		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(title);
		}
		
		@Override
		protected Control createDialogArea(Composite parent) {
			Composite dialogArea = (Composite) super.createDialogArea(parent);
			GridLayout layout = new GridLayout(2, false);
			dialogArea.setLayout(layout);
			List<IItemPropertyDescriptor> pds = source.getPropertyDescriptors(target);
			for (IItemPropertyDescriptor pd : pds) {
				if (pd.canSetProperty(target)) {
					DetailProviderParameter dpp = createDetailProviderParameter(dialogArea, target, pd);
					IBindingFactory bf = EMFDetailUtils.getBindingFactory(dpp);
					bf.createBinding(dpp);
				}
			}
			return dialogArea;
		}
		
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, false);
		}
					
		private DetailProviderParameter createDetailProviderParameter(Composite parent, EObject target, IItemPropertyDescriptor pd) {
			DetailProviderParameter p = new DetailProviderParameter();
			p.setParent(parent);
			p.setTarget(target);
			p.setDetailFormToolkit(toolkit);
			p.setPropertyDescriptor(pd);
			p.setDataBindingContext(dataBindingContext);
			return p;
		}
	}


}
