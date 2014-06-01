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
package gov.nasa.ensemble.emf.transaction;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;

public abstract class PostCommitListener implements ResourceSetListener {

	/**
	 * Our transactions don't notify on touch, so just use ANY. (fast and cheap!)
	 */
	@Override
	public NotificationFilter getFilter() {
		return NotificationFilter.ANY;
	}

	@Override
	public final boolean isAggregatePrecommitListener() {
		return false; // result doesn't matter for PostCommitListener
	}

	@Override
	public final boolean isPostcommitOnly() {
		return true;
	}

	@Override
	public final boolean isPrecommitOnly() {
		return false;
	}

	@Override
	public abstract void resourceSetChanged(ResourceSetChangeEvent event);

	@Override
	public final Command transactionAboutToCommit(ResourceSetChangeEvent event) {
		throw new UnsupportedOperationException("PostCommitListeners should not be notified here");
	}

}
