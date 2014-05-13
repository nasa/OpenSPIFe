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
package gov.nasa.ensemble.common;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.Assert;

import org.eclipse.osgi.framework.internal.core.AbstractBundle;
import org.eclipse.osgi.framework.internal.core.PackageAdminImpl;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

@SuppressWarnings("restriction")
public class TestBundleResolution {
	
	@Test
	public void checkBundleResolution() {
		Set<Bundle> bundles = findUnresolvedBundles();
		if (!bundles.isEmpty()) {
			StringBuilder builder = new StringBuilder("couldn't resolve bundles:\n");
			for (Bundle bundle : bundles) {
				String symbolicName = bundle.getSymbolicName();
				builder.append('\t').append(symbolicName).append('\n');
			}
			Assert.fail(builder.toString());
		}
	}

	private Set<Bundle> findUnresolvedBundles() {
		Bundle commonBundle = CommonPlugin.getDefault().getBundle();
		BundleContext context = commonBundle.getBundleContext();
		ServiceReference packageAdminRef = context.getServiceReference(PackageAdmin.class.getName());
		PackageAdminImpl packageAdmin = (PackageAdminImpl) context.getService(packageAdminRef);
		packageAdmin.refreshPackages(null);
		Bundle[] bundles = context.getBundles();
//		bundleContext.getServiceReference(clazz);
		Set<Bundle> unresolvedBundles = new LinkedHashSet<Bundle>();
		for (Bundle bundle : bundles) {
			AbstractBundle abstractBundle = (AbstractBundle)bundle;
			if (abstractBundle.isResolved()) {
				continue;
			}
			unresolvedBundles.add(bundle);
			BundleException exception = abstractBundle.getResolutionFailureException();
			LogUtil.warn("failed to resolve: " + bundle.getSymbolicName(), exception);
//			if (isFragment(bundle)) {
//				continue;
//			}
//			try {
//				bundle.start();
//			} catch (BundleException e) {
//				Throwable cause = e.getCause();
//				if ((cause instanceof IllegalStateException) &&
//					(cause.getMessage().equals("Workbench has not been created yet."))) {
//					continue;
//				}
//				unresolvedBundles.add(bundle);
//			} catch (IllegalStateException e) {
//				unresolvedBundles.add(bundle);
//			}
		}
		return unresolvedBundles;
	}

//	private boolean isFragment(Bundle bundle) {
//		Dictionary headers = bundle.getHeaders();
//		Enumeration keys = headers.keys();
//		while (keys.hasMoreElements()) {
//			Object key = keys.nextElement();
//			if ((key instanceof String) && "Fragment-Host".equalsIgnoreCase((String)key)) {
//				return true;
//			}
//		}
//		return false;
//	}

}
