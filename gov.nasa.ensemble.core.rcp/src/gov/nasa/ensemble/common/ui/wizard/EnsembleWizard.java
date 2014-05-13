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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.Bundle;

/**
 * Class to standardize all ensemble wizards
 * @author Eugene Turkov
 *
 */
public abstract class EnsembleWizard extends Wizard {
	
	private String translatableName;
	private String description;
	protected static final WizardPage DEFAULT_ERROR_PAGE = new EnsembleWizardErrorPage();
	protected WizardPage errorPage = DEFAULT_ERROR_PAGE;
	/*
	 * created and disposed within this class
	 */
	private Image smallImage = null;
	private ImageDescriptor smallImageDescriptor = null;
	
	protected static final String EXT_POINT_ID_IMPORT_WIZARDS = "org.eclipse.ui.importWizards";
	protected static final String EXT_POINT_ID_EXPORT_WIZARDS = "org.eclipse.ui.exportWizards";
	protected static final String EXT_POINT_ID_NEW_WIZARDS = "org.eclipse.ui.newWizards";
	
	public final ImageDescriptor getSmallImageDescriptor() {
		if(smallImageDescriptor == null) {
			configure();
		}
		return smallImageDescriptor;
	}
	
	@Override
	public String getWindowTitle() {
		String prefix = getWindowTitlePrefix();
		if (prefix != null) {
			return prefix + super.getWindowTitle();
		}
		return super.getWindowTitle();
	}
	
	protected String getWindowTitlePrefix() {
		return null;
	}

	/**
	 * Subclasses should implement this method to provide a large icon image to
	 * be displayed in each page of the wizard. Return null for no image
	 * support. To apply a different image based on unique page, override the
	 * createPageControls method.
	 * @return the large image descriptor
	 */
	public abstract ImageDescriptor getLargeImageDescriptor();

	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		//setShellImage(this.getShell(), getSmallImageDescriptor());
		this.setWindowTitle(getTranslatableName());
		applyLargeImageToPages();		
	}		
		
	/**
	 * Sets the image which is displayed in the main wizard shell.
	 * @param shell the shell containing the wizard & windowPages
	 * @param imageDescriptor describes the image to be shown as the small
	 * icon for the shell.
	 */
	/*
	private void setShellImage(final Shell shell, ImageDescriptor imageDescriptor) {
		IWizardContainer container = this.getContainer();
		if(container instanceof WizardDialog) {
			WizardDialog wizardDialog = (WizardDialog)container;
			wizardDialog.addPageChangedListener(new IPageChangedListener() {				
				public void pageChanged(PageChangedEvent event) {
					Object page = event.getSelectedPage();
					if(page != null && page instanceof IWizardPage) {
						IWizardPage wizardPage = (IWizardPage)page;
						IWizardPage matchedPage = EnsembleWizard.this.getPage(wizardPage.getName());
						if(shell != null && matchedPage == null && oldImage != null && !oldImage.equals(shell.getImage())) {
							shell.setImage(oldImage);
						}
					}					
				}
			});
		}
		
		if(shell != null) {
			oldImage = container.getShell().getImage();

			if(smallImage == null) {
				if(imageDescriptor != null) {
					smallImage = imageDescriptor.createImage();
				}
				
				else {
					LogUtil.warn("can't set small icon for wizard: "
							+ this.getClass().getName());
				}
			}			
			shell.setImage(smallImage);
		}		
	}
	*/
	
	/**
	 * Iterates through all of the windowPages and applies the large image to each page.
	 */
	protected void applyLargeImageToPages() {
		IWizardPage[] wizardPages = this.getPages();
		ImageDescriptor imageDescriptor = getLargeImageDescriptor();
		if(imageDescriptor != null) {
			for(IWizardPage wizardPage : wizardPages) {
				Image currentImage = wizardPage.getImage();
				if(imageDescriptor != null) {
					if(currentImage != null && !currentImage.equals(getDefaultPageImage())) {
						// we may be in someone else's business disposing of the default image...
						// that'd be bad.
						// currentImage.dispose();
					}
					wizardPage.setImageDescriptor(imageDescriptor);
				}		
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if(smallImage != null && !smallImage.isDisposed()) {
			smallImage.dispose();
		}
	}	
	
    protected final String getTranslatableName() {
    	if(translatableName == null) {
    		configure();
    	}
    	return translatableName;
    }       
    
    protected final String getDescription() {
    	if(description == null) {
    		configure();
    	}
    	return description;
    }	
    
	private void configure() {
		try {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			String extPointID = getWizardExtensionPointID();
			if (!CommonUtils.isNullOrEmpty(extPointID)) {
				IConfigurationElement[] config = registry.getConfigurationElementsFor(extPointID);
				String wizardId = getWizardID();
				for (IConfigurationElement e : config) {
					// skip non-wizard contributors (e.g. primaryWizard or category)
					if (!"wizard".equals(e.getName())) {
						continue;
					}
					String id = e.getAttribute("id");
					if(id.equalsIgnoreCase(wizardId)) {
						translatableName = e.getAttribute("name");
						description = getDescription(e);
						String icon = e.getAttribute("icon");
						Bundle bundle = Platform.getBundle(e.getNamespaceIdentifier());
						if (icon != null) {
							URL url = bundle.getEntry(icon);
							smallImageDescriptor = ImageDescriptor.createFromURL(url);
						}
						break;
					}
				}
			}
		} catch (Exception ex) {
			LogUtil.error(ex);
		}
	}

	private static String getDescription(IConfigurationElement e) {
		String result = null;
		IConfigurationElement[] children = e.getChildren();
		for(IConfigurationElement child : children) {
			String name = child.getName();
			if(name.equalsIgnoreCase("description")) {
				result = child.getValue().toString();
				break;
			}
		}

		return result;
	}
	
	/**
	 * This allows the wizard to configure itself consistently with the
	 * plugin.xml entry. If used (not null), the wizard will get the same
	 * image, description and name as the entry in the plugin.xml
	 * 
	 * Return null if the wizard is not called via a plugin.xml entry (i.e. an action)
	 * 
	 * @return
	 */
	protected abstract String getWizardExtensionPointID();
	
	public String getWizardID() {
		return ClassIdRegistry.getUniqueID(getClass());
	}
	
	public IProject getSelectedProject(IWorkbench workbench, IStructuredSelection selection) {
		return EditorPartUtils.getSelectedProject(workbench, selection);
	}
	
	/**
	 * Check that wizard is in a state to proceed displaying it's first content page.
	 * 
	 * @return true if the wizard is in an okay state, false if the error page should be displayed.
	 */
	protected boolean checkWizardPreconditions(){
		return true;
	}
	
	/**
	 * Subclasses of EnsembleWizard should override this method in place of addPages.
	 * 
	 * @see #addPages()
	 */
	protected abstract void addContentPages();
	
	/**
	 * Overriding addPages in subclasses of EnsembleWizard is discouraged.
	 * 
	 * @see #checkWizardPreconditions()
	 * @see #addContentPages()
	 * @deprecated Use addContentPages instead.
	 */
	@Override
	@Deprecated
	public void addPages() {		
		if( ! this.checkWizardPreconditions()){
			this.addPage(this.errorPage);
		} else {
			addContentPages();
		}
	}

}
