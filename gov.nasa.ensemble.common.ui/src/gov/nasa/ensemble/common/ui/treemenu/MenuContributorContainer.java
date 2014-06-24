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
package gov.nasa.ensemble.common.ui.treemenu;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * For convenience, you can package up a bunch of potential menu items in one class,
 * by listing them as enclosed public classes that extend MenuContributor.
 * @see gov.nasa.ensemble.common.ui.contextmenu.test.ExampleOfMenuContributorContainer
 * @author kanef
 *
 */
public class MenuContributorContainer implements IContextualCommandContributor {

	@Override
	public Collection<ContextualCommandContributor<?>> getContributors(Object regardlessOfObjectSelected) {
		List<ContextualCommandContributor<?>> result = new LinkedList<ContextualCommandContributor<?>>();
		for (Class clazz : getClass().getClasses()) {
			if (ContextualCommandContributor.class.isAssignableFrom(clazz)) {
				ContextualCommandContributor instance;
				try {
					// Enclosed classes have constructors that always implicitly take
					// an instance of the enclosing class as an extra argument.
					Constructor constructor = clazz.getConstructor(getClass());
					instance = (ContextualCommandContributor<?>) constructor.newInstance(this);
					result.add(instance);
				} catch (InstantiationException e) {
					LogUtil.error(e);
				} catch (IllegalAccessException e) {
					LogUtil.error(e);
				} catch (IllegalArgumentException e) {
					LogUtil.error(e);
				} catch (SecurityException e) {
					LogUtil.error(e);
				} catch (InvocationTargetException e) {
					LogUtil.error(e);
				} catch (NoSuchMethodException e) {
					LogUtil.error(e);
				}
			}
		}
		return result;
	}	

}
