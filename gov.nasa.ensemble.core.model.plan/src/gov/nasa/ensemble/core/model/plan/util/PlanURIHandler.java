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
package gov.nasa.ensemble.core.model.plan.util;

import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;

/**
 * Allows an optional, URI handler to be associated with a plan resource to handle resolving
 * and deresolving proxy URI's in the plan serialization, for example to implement a specialized URI scheme
 * 
 * Use gov.nasa.ensemble.common.ClassRegistry extension point to register a subclass of PlanURIHandler to
 * be used with plan resources
 * 
 * @author rnado
 *
 */
public abstract class PlanURIHandler extends URIHandlerImpl {
	// Subclasses should override the resolve and deresolve methods inherited from XMLResource.URIHandler
}
