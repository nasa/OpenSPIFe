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
package gov.nasa.ensemble.common.functional;

import static fj.control.parallel.Strategy.*;
import static gov.nasa.ensemble.common.CommonUtils.*;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fj.F;
import fj.P1;
import fj.Unit;
import fj.control.parallel.Strategy;

public class Strategies {
	public static final Strategy<Unit> sequential = Strategy.seqStrategy();
	public static final Strategy<Unit> thread = Strategy.simpleThreadStrategy();
	
	public static final Strategy<Unit> executor(ExecutorService service) {
		return Strategy.executorStrategy(service);
	}
	
	public static final Strategy<Unit> completion(CompletionService service) {
		return Strategy.completionStrategy(service);
	}
	
	public static final <A> Strategy<A> job(final String jobName) {
		return job(jobName, 0L);
	}
	
	public static final <A> Strategy<A> job(final String jobName, final long delay) {
		return strategy(new F<P1<A>, P1<A>>() {
			@Override
			public P1<A> f(final P1<A> input) {
				final A[] result = (A[])new Object[1];
				final Job job = new Job(jobName) {
					@Override
					public IStatus run(IProgressMonitor monitor) {
						result[0] = input._1();
						return Status.OK_STATUS;
					}
				};
				job.schedule(delay);
				return new P1<A>() {
					@Override
					public A _1() {
						try {
							job.join();
						} catch (InterruptedException e) {
							throw runtime(e);
						}
						return result[0];
					}
				};
			}
		});
	}
}
