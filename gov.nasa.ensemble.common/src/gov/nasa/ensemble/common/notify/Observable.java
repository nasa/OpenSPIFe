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
package gov.nasa.ensemble.common.notify;

import static fj.P.*;
import static fj.control.parallel.QueueActor.*;
import static fj.data.List.*;
import static gov.nasa.ensemble.common.functional.Effects.*;
import fj.Effect;
import fj.F;
import fj.P2;
import fj.Unit;
import fj.control.parallel.QueueActor;
import fj.control.parallel.Strategy;
import fj.data.List;

public abstract class Observable<A> {
	
	private A val;
	private List<Effect<A>> observers = nil();
	
	private final QueueActor<P2<A, List<Effect<A>>>> actor = 
		queueActor(Strategy.<Unit>idStrategy(), new Effect<P2<A, List<Effect<A>>>>() {
		@Override
		public void e(final P2<A, List<Effect<A>>> p) {
			p._2().reverse().foreach(apply(p._1()));
		}
	});
	
	public Observable(final A initialVal) {
		this.val = initialVal;
	}
	
	public synchronized void observe(final Effect<A> observer) {
		observers = observers.cons(observer);
	}
	
	public synchronized boolean ignore(final Effect<A> observer) {
		final List<Effect<A>> old = observers;
		observers = observers.removeAll(new F<Effect<A>, Boolean>() {
			@Override
			public Boolean f(final Effect<A> effect) {
				return effect == observer;
			}
		});
		return observers.length() != old.length();
	}
	
	public A get() {
		return val;
	}
	
	public synchronized void set(final A val) {
		this.val = val;
		actor.act(p(val, observers));
	}
}
