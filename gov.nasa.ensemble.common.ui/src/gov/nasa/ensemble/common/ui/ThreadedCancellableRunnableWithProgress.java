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
 * 
 */
package gov.nasa.ensemble.common.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public abstract class ThreadedCancellableRunnableWithProgress implements IRunnableWithProgress {

    private IProgressMonitor monitor = null;
    private Throwable exception = null;
    boolean successful = false;

    protected final Thread worker = new Thread("ThreadedCancellableRunnableWithProgress") {
		@Override
		public void run() {
			try {
			    doRun(monitor);
                successful = true;
            } catch (ThreadDeath t) {
            	throw t;
            } catch (Throwable t) {
            	exception = t;
            }
		}
	};

    @Override
	@SuppressWarnings("deprecation")
    public final void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
    	this.monitor = monitor;
    	worker.start();
    	while (!successful) {
    		if (monitor.isCanceled()) {
    			worker.stop();
    			throw new InterruptedException();
    		}
    		if (exception != null) {
    			throw new InvocationTargetException(exception);
    		}
    		Thread.sleep(200);
    	}
    }

	protected abstract void doRun(IProgressMonitor monitor) throws Exception;
	
}
