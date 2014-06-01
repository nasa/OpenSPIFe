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
package gov.nasa.ensemble.emf;


import gov.nasa.ensemble.common.extension.ClassRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor.Registry;

/**
 * Default implementation of an editing domain so as
 * to allow for centralized addition and removal of services
 * and shared development of resource loaders
 * 
 * @author aaghevli
 */
public class EnsembleEditingDomain extends AdapterFactoryEditingDomain {

	private static final List<EnsembleEditingDomainContributor> CONTRIBUTORS = ClassRegistry.createInstances(EnsembleEditingDomainContributor.class);
	
	public EnsembleEditingDomain() {
		super(new ComposedAdapterFactory(Registry.INSTANCE), new BasicCommandStack(), new EnsembleEditingDomainResourceSet());
		resourceToReadOnlyMap = new HashMap<Resource, Boolean>();
		((EnsembleEditingDomainResourceSet)getResourceSet()).setEditingDomain(this);
		initializePackageRegistry();
	}
	
	/**
	 * Initialize the package registry to allow for overriding the default packages and factories
	 * to customize the construction of the classes
	 */
	private void initializePackageRegistry() {
		for (EnsembleEditingDomainContributor c : CONTRIBUTORS) {
			Entry<String, EPackage> entry = c.getPackageToRegister();
			if (entry != null) {
				resourceSet.getPackageRegistry().put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * Internal resource set implementation that extends {@link EnsembleResourceSet} in
	 * order to provide for the {@link IEditingDomainProvider} interface to be populated
	 * 
	 * @author aaghevli
	 */
	private static class EnsembleEditingDomainResourceSet extends EnsembleResourceSet implements IEditingDomainProvider {

		private EditingDomain editingDomain;
		
		@Override
		public EditingDomain getEditingDomain() {
			return editingDomain;
		}

		public void setEditingDomain(EditingDomain editingDomain) {
			this.editingDomain = editingDomain;
		}
		
	}
	
}
