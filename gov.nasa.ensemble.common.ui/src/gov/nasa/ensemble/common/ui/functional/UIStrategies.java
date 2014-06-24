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
package gov.nasa.ensemble.common.ui.functional;

import static fj.control.parallel.Strategy.*;

import org.eclipse.swt.widgets.Display;

import fj.F;
import fj.P1;
import fj.control.parallel.Strategy;

public class UIStrategies {
	public static <A> Strategy<A> asyncExecStrategy(final Display display) {
		return strategy(new F<P1<A>, P1<A>>() {
			@Override
			public P1<A> f(final P1<A> input) {
				final A[] result = (A[])new Object[1];
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						input._1();
					}
				});
				return new P1<A>() {
					@Override
					public A _1() {
						if (result[0] == null)
							display.syncExec(new Runnable() {
								@Override
								public void run() {
									// do nothing
								}
							});
						return result[0];
					}
				};
			}
		});
	}
}
