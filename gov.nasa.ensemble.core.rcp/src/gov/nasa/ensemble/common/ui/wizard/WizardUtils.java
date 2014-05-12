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
package gov.nasa.ensemble.common.ui.wizard;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.type.editor.StringTypeEditor;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

public abstract class WizardUtils {
	public static final String SUBMENU_EXPORT_WIZARD_CATEGORY = "export.wizard.category";
	public static final String SUBMENU_IMPORT_WIZARD_CATEGORY = "import.wizard.category";
	
	public static StringTypeEditor createDateTimeEditor(Composite composite, String string, final Date timepointDate) {
		IStringifier<Date> dateStringifier = StringifierRegistry.getStringifier(Date.class);
		GridData textLayoutDate = new GridData();
		textLayoutDate.horizontalAlignment = SWT.FILL;
		textLayoutDate.grabExcessHorizontalSpace = true;
		
		Label label = new Label(composite, SWT.NONE);
		label.setText(string);
		
		StringTypeEditor editor = new StringTypeEditor(dateStringifier, composite, timepointDate, true);
		editor.getEditorControl().setLayoutData(textLayoutDate);
		return editor;
	}
	
	@SuppressWarnings("restriction")
	public static void addImportWizardSubmenu(IMenuManager menu, IWorkbenchAction action,  IWorkbenchWindow workbenchWindow, ISelection selection){
		addWizardSubmenu(menu, action, WizardUtils.SUBMENU_IMPORT_WIZARD_CATEGORY, 
				org.eclipse.ui.internal.wizards.ImportWizardRegistry.getInstance(), "Import", workbenchWindow, selection);
	}
	
	public static WizardResourceVisitor getResourceVisitor( String type ){
		return new WizardResourceVisitor(type);
	}

	public static WizardResourceVisitor getResourceVisitor( String[] typeArray ){
		return new WizardResourceVisitor(typeArray);
	}
	
	@SuppressWarnings("restriction")
	public static void addExportWizardSubmenu(IMenuManager menu, IWorkbenchAction action,  IWorkbenchWindow workbenchWindow, ISelection selection ){
		addWizardSubmenu(menu,  action, WizardUtils.SUBMENU_EXPORT_WIZARD_CATEGORY, 
				org.eclipse.ui.internal.wizards.ExportWizardRegistry.getInstance(), "Export", workbenchWindow, selection);
	
	}
	
	@SuppressWarnings("restriction")
	public static void addWizardSubmenu(IMenuManager menu, IWorkbenchAction globalAction,
			String propertyKey, org.eclipse.ui.internal.wizards.AbstractWizardRegistry wizardRegistry, String submenuTitle, 
			 IWorkbenchWindow workbenchWindow, ISelection selection) {
		String propertyValue = EnsembleProperties.getProperty(propertyKey);
		
		if (propertyValue == null) {
			menu.add(globalAction);
		} else {
			@SuppressWarnings("restriction")
			IWizardCategory category = wizardRegistry.findCategory(propertyValue);
			if (category == null) {
				LogUtil.error("couldn't find the category");
				menu.add(globalAction);
				return;
			}
			MenuManager submenu = new MenuManager(submenuTitle);
			IWizardDescriptor[] wizards = category.getWizards();
			if (wizards.length != 0) {
				for (final IWizardDescriptor descriptor : wizards) {
					Action action = new OpenWizardDialogAction(descriptor, workbenchWindow, selection);
					submenu.add(action);
				}
				submenu.add(new Separator());
			}
			globalAction.setText("Other...");
			submenu.add(globalAction);
			menu.add(submenu);
		}
	}
	
		
	
	
	
	
}
