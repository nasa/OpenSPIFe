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
package gov.nasa.ensemble.common.help;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;

public class ContextProvider implements IContextProvider {

	private final String id;
	private final int contextChangeMask;

	public ContextProvider(String id) {
		this.id = id;
		contextChangeMask = NONE;
	}

	public ContextProvider(String id, int contextChangeMask) {
		this.id = id;
		this.contextChangeMask  = contextChangeMask;
	}
	
	@Override
	public IContext getContext(Object target) {
		return HelpSystem.getContext(id);
	}

	@Override
	public int getContextChangeMask() {
		return this.contextChangeMask;
	}

	@Override
	public String getSearchExpression(Object target) {
		return null;
	}

}
