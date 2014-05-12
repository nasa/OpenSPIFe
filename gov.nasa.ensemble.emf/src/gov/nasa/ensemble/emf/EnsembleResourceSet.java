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

import fj.data.Option;
import gov.nasa.ensemble.common.extension.ClassRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Shared implementation of a {@link ResourceSet} in order to allow
 * for extensibility.
 * 
 * @author aaghevli
 */
public class EnsembleResourceSet extends ResourceSetImpl {

	private List<EnsembleResourceFactory> factories = ClassRegistry.createInstances(EnsembleResourceFactory.class);
	
	public EnsembleResourceSet() {
		super();
		
		for (EnsembleResourceFactory factory : factories) {
			factory.setResourceSet(this);
		}
		
		List<URIHandler> handlers = new ArrayList<URIHandler>(URIHandler.DEFAULT_HANDLERS);
		handlers.addAll(0, getURIHandlerContributions());
		setURIConverter(new ExtensibleURIConverterImpl(handlers, ContentHandler.Registry.INSTANCE.contentHandlers()));
	}

	/**
	 * Allows for the platform to contribute {@link URIHandler} elements to
	 * the resource set. Contributions can come from:
	 * 
	 * <ul>
	 * <li>ClassRegistry extension point</li>
	 * </ul>
	 * 
	 * @return a list of {@link URIHandler} elements.
	 */
	protected List<URIHandler> getURIHandlerContributions() {
		return ClassRegistry.createInstances(URIHandler.class);
	}
	
	@Override
	public Resource createResource(URI uri, String contentType) {
		for (EnsembleResourceFactory factory : factories) {
			Option<? extends Resource> option = factory.createResource(uri, contentType);
			if (option.isSome()) {
				Resource resource = option.some();
				getResources().add(resource); // consistent with super implementation
				return resource;
			}
		}
		return super.createResource(uri, contentType);
	}

}
