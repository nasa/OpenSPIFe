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
package gov.nasa.ensemble.core.model.common.util;

import static gov.nasa.ensemble.common.functional.Lists.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RunnableWithResult;

import fj.F;
import fj.data.List;
import fj.pre.Equal;
import gov.nasa.ensemble.common.functional.Functions;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

public class ModelFunctions {
	
	public static <A, B> F<A, B> transactional(final F<A, B> f) {
		return new F<A, B>() {
			public B f(final A a) {
				return TransactionUtils.reading(a, new RunnableWithResult.Impl<B>() {
					public void run() {
						setResult(f.f(a));
					}
				});
			}
		};
	}
	
	public static final F<EEnum, List<Enumerator>> enumToLiterals = 
		new F<EEnum, List<Enumerator>>() {
		public List<Enumerator> f(final EEnum eenum) {
			return fj(eenum.getELiterals()).map(Functions.<EEnumLiteral, Enumerator>cast());
		}
	};
	
	public static final F<Enumerator, String> literalToString = new F<Enumerator, String>() {
		public String f(final Enumerator literal) {
			return literal.getLiteral();
		}
	};
	
	public static final F<Enumerator, String> literalToName = new F<Enumerator, String>() {
		public String f(final Enumerator literal) {
			return literal.getName();
		}
	};
	
	public static final F<Enumerator, Integer> literalToValue = new F<Enumerator, Integer>() {
		public Integer f(final Enumerator literal) {
			return literal.getValue();
		}
	};
	
	public static final F<EEnum, List<String>> enumToNames = new F<EEnum, List<String>>() {
		public List<String> f(final EEnum eenum) {
			return enumToLiterals.f(eenum).map(literalToName);
		}
	};
	
	/**
	 * Returns a function which determines if a notification was generated by
	 * the given EObject or any of its children.
	 */
	public static final F<Notification, Boolean> cameFrom(final EObject object) {
		return new F<Notification, Boolean>() {
			public Boolean f(final Notification notification) {
				final Object notifier = notification.getNotifier();
				if (notifier instanceof EObject)
					return EMFUtils.getAncestors((EObject)notifier).find(Equal.<EObject>anyEqual().eq(object)).isSome();
				return false;
			}
		};
	}
}
