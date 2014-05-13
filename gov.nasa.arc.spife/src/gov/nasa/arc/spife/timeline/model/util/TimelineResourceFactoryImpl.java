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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.arc.spife.timeline.model.util;

import java.util.Arrays;

import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource Factory</b> associated with the package.
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.timeline.model.util.TimelineResourceImpl
 * @generated
 */
public class TimelineResourceFactoryImpl extends ResourceFactoryImpl {
	/**
	 * Creates an instance of the resource factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimelineResourceFactoryImpl() {
		super();
	}

	/**
	 * Creates an instance of the resource.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Resource createResource(URI uri) {
		XMIResource result = new TimelineResourceImpl(uri);
		result.getDefaultSaveOptions().put(XMLResource.OPTION_URI_HANDLER, new URIHandlerImpl() {
			/**
			 * This URI deresolve method will produce the usual XMI URIs except when
			 * dealing with URIs for resources containing Profiles.  These URIs will
			 * be converted from project specific platform URIs to project relative
			 * URIs.
			 */
			@Override
			public URI deresolve(URI uri) {
				// Ensure we're providing this special behavior for only known registered
				// users of this class .chart and .timeline
				String fileExtension = baseURI.fileExtension();
				if (!(fileExtension.equalsIgnoreCase("timeline") || fileExtension.equalsIgnoreCase("chart")))
					return super.deresolve(uri);
				
				// Only URIs which begin with platform:/resource/<project name> will be
				// converted into project:/ URIs.
				if (!(uri.isPlatformResource() && uri.segmentCount() > 2))
					return super.deresolve(uri);
				
				// Only files registered as having Profile content types will be converted.
				if (!ProfileUtil.isProfileContentType(EMFUtils.getFile(uri)))
					return super.deresolve(uri);
				
				// Strip the first two segments off of a platform URI to create a project URI.
				String[] segments = Arrays.copyOfRange(uri.segments(), 2, uri.segmentCount());
				return URI.createHierarchicalURI(ProjectURIConverter.SCHEME_PROJECT, null, null, segments, null, uri.fragment());
			}
		});
		return result;
	}

} //TimelineResourceFactoryImpl
