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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

public abstract class IntrospectiveContextualCommandContributor {
	
	private final Object INAPPLICABLE_TOKEN = new String[] {"IntrospectiveMenuContributor found this object was not the applicable type and could not be adapted to it."};
		
	protected boolean isApplicableResult(Object resultOfEnsuringCorrectType) {
		return resultOfEnsuringCorrectType != INAPPLICABLE_TOKEN;
	}
	
	public final Object ensureCorrectType(Object objectInTree) {
		if (objectInTree==null) return INAPPLICABLE_TOKEN;
		try {
			Class<?> applicableClass = getApplicableClass();
			if (objectInTree instanceof IAdaptable) {
				Object adaptation = ((IAdaptable) objectInTree).getAdapter(applicableClass);
				if (adaptation != null) {
					objectInTree = adaptation;
				}
			}
			if (!applicableClass.isInstance(objectInTree)) return INAPPLICABLE_TOKEN;
		} catch (NoSuchMethodException e) {
			LogUtil.error(e);
			return INAPPLICABLE_TOKEN;
		}
		return objectInTree;
	}
	
	public Class<?> getApplicableClass() throws NoSuchMethodException {
		return findMethodRevealingApplicableClass().getParameterTypes()[getNumberOfParameterOfTypeT()];
	}

	private Method findMethodRevealingApplicableClass() throws NoSuchMethodException {
		String targetName = getNameOfMethodAcceptingParameterOfTypeT();
		List<Method> candidates = new ArrayList<Method>();
		for (Method method : getClass().getDeclaredMethods()) {
			if (method.getName().equals(targetName)) {
				candidates.add(method);
			}
		}
		if (!candidates.isEmpty()) {
			return getBestCandidateMethod(candidates);
		}
		for (Method method : getClass().getMethods()) {
			if (method.getName().equals(targetName)) {
				candidates.add(method);
			}
		}
		if (!candidates.isEmpty()) {
			return getBestCandidateMethod(candidates);
		}
		throw new NoSuchMethodException("No " + targetName + " method in " + this);
	}

	private Method getBestCandidateMethod(List<Method> candidates) {
		if (candidates.size() == 1) {
			return candidates.get(0);
		}
		int paramIndex = getNumberOfParameterOfTypeT();
		Method bestMethod = null;
		Class<?> bestClass = null;
		for (Method method : candidates) {
			Class<?> methodParameterClass = method.getParameterTypes()[paramIndex];
			if (bestMethod == null || bestClass == null
					|| bestClass.isAssignableFrom(methodParameterClass)) {
				bestMethod = method;
				bestClass = methodParameterClass;
			}
		}
		return bestMethod;
	}

	protected abstract String getNameOfMethodAcceptingParameterOfTypeT();

	protected abstract int getNumberOfParameterOfTypeT();


}
