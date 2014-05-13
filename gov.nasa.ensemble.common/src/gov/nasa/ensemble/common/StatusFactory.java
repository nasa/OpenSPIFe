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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class StatusFactory {
	private final String bundleId;

	public StatusFactory(final String bundleId) {
		this.bundleId = bundleId;
	}

	public IStatus error(final String message) {
		return new Status(IStatus.ERROR, bundleId, message);
	}

	public IStatus error(final String message, final Throwable e) {
		return new Status(IStatus.ERROR, bundleId, message, e);
	}

	public IStatus warn(final String message) {
		return status(IStatus.WARNING, message);
	}

	public IStatus info(final String message) {
		return status(IStatus.INFO, message);
	}

	public IStatus cancel(final String message) {
		return status(IStatus.CANCEL, message);
	}

	public IStatus cancel(final String message, final Throwable t) {
		return status(IStatus.CANCEL, message);
	}

	public IStatus ok() {
		return Status.OK_STATUS;
	}

	public IStatus cancel() {
		return Status.CANCEL_STATUS;
	}

	public IStatus status(final int severity, final String message) {
		return new Status(severity, bundleId, message);
	}
}
