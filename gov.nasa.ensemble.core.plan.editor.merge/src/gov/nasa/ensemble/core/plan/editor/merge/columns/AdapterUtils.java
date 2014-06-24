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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * Useful methods for EMF adapter factories handling.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class AdapterUtils {
	/**
	 * Adapter factory instance. This contains all factories registered in the global registry.
	 */
	private static final ComposedAdapterFactory FACTORY = createAdapterFactory();

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private AdapterUtils() {
		// prevents instantiation.
	}

	/**
	 * This will try and return an adapter of the given type associated to the given EObject.
	 * 
	 * @param <T>
	 *            Type of the adapter to return.
	 * @param eObj
	 *            The EObject we seek an adapter for.
	 * @param type
	 *            The sought adapter's type.
	 * @return An associated adapter.
	 * @see AdapterFactory#adapt(Object, Object)
	 * @since 0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> T adapt(EObject eObj, Class<? extends T> type) {
		return (T)getAdapterFactory().adapt(eObj, type);
	}

	/**
	 * Returns a factory built with all the {@link AdapterFactory} instances available in the global registry.
	 * 
	 * @return A factory built with all the {@link AdapterFactory} instances available in the global registry.
	 */
	public static AdapterFactory getAdapterFactory() {
		return FACTORY;
	}

	/**
	 * This will try and get the IItemLabelProvider associated to the given EObject if its ItemProviderFactory
	 * is registered, then return the image it provides.
	 * 
	 * @param eObj
	 *            EObject we need an image for.
	 * @return The Image provided by the IItemLabelProvider associated with <tt>eObj</tt>, <code>null</code>
	 *         if it cannot be found.
	 * @see IItemLabelProvider#getImage(Object)
	 * @since 0.8
	 */
	public static Object getItemProviderImage(EObject eObj) {
		final IItemLabelProvider labelProvider = adapt(eObj, IItemLabelProvider.class);
		if (labelProvider != null)
			return labelProvider.getImage(eObj);
		return null;
	}

	/**
	 * This will try and get the IItemLabelProvider associated to the given EObject if its ItemProviderFactory
	 * is registered, then return the text it provides.
	 * 
	 * @param eObj
	 *            EObject we need the text of.
	 * @return The text provided by the IItemLabelProvider associated with <tt>eObj</tt>, <code>null</code> if
	 *         it cannot be found.
	 * @see IItemLabelProvider#getText(Object)
	 * @since 0.8
	 */
	public static String getItemProviderText(EObject eObj) {
		final String text;
		if (eObj == null) {
			text = "null"; //$NON-NLS-1$
		} else {
			final IItemLabelProvider labelProvider = adapt(eObj, IItemLabelProvider.class);
			if (labelProvider != null) {
				text = labelProvider.getText(eObj);
			} else {
				text = ""; //$NON-NLS-1$
			}
		}
		return text;
	}

	/**
	 * Returns an adapter factory containing all the global EMF registry's factories.
	 * 
	 * @return An adapter factory made of all the adapter factories declared in the registry.
	 */
	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}
}

