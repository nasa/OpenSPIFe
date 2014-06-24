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
package gov.nasa.ensemble.common.ui.multiselect;

/*******************************************************************************
 * Copyright (c) 2009 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/
/**
 * Event sent with an {@link IInPlaceDialogListener} that contains information about the close event that occurred
 * 
 * @author Shawn Minto
 * @since 3.3
 */
public class InPlaceDialogEvent {

	private final int returnCode;

	private final boolean isClosing;

	public InPlaceDialogEvent(int returnCode, boolean isClosing) {
		this.returnCode = returnCode;
		this.isClosing = isClosing;
	}

	public boolean isClosing() {
		return isClosing;
	}

	public int getReturnCode() {
		return returnCode;
	}
}
