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
package gov.nasa.arc.spife.europa.clientside;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EuropaClientSidePlugin extends AbstractUIPlugin {
	@SuppressWarnings("unused")
	private static final String DE_LIBRARY_DEBUG_NAME = "DynamicEuropa_g";
	@SuppressWarnings("unused")
	private static final String DE_LIBRARY_OPTIMIZED_NAME = "DynamicEuropa_o";
	
	// The plug-in ID
	public static final String PLUGIN_ID = "gov.nasa.arc.spife.europa.clientside";

	// The shared instance
	private static EuropaClientSidePlugin plugin;
	protected static boolean loadedNativeLib;
	protected static Throwable loadException=null;
	
	static {
		if (CommonUtils.isOSWindows()) {
			loadLibrary("pthreadVC2");
		}
		
		boolean useDebugLib =
			("true".equals(System.getenv("DEUROPA_USE_DEBUG_LIB"))) ||
			("true".equals(System.getProperty("deuropa.useDebugLib")));				
			
		String europaLib = (useDebugLib ? DE_LIBRARY_DEBUG_NAME 
										: DE_LIBRARY_OPTIMIZED_NAME);
		LogUtil.info("Loading "+europaLib);
		loadedNativeLib = loadLibrary(europaLib);
		if (loadedNativeLib)
			LogUtil.info("Loaded "+europaLib);
	}

	private static boolean loadLibrary(String libName) {
		try {
			System.loadLibrary(libName);
			return true;
		} 
		catch (Throwable e) {
			loadException = e;
			LogUtil.error("Exception loading " + System.mapLibraryName(libName),e);
		}
		return false;
	}
	
	/**
	 * The constructor.  Empty.
	 */
	public EuropaClientSidePlugin() {
		//empty! 
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
  public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
  public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EuropaClientSidePlugin getDefault() {
		return plugin;
	}
	
	public static boolean getInitialized() { return loadedNativeLib; }
	public static Throwable getException() { return loadException; }

}
