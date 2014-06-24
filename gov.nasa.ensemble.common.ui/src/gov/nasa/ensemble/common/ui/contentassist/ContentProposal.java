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
package gov.nasa.ensemble.common.ui.contentassist;

import org.eclipse.jface.fieldassist.IContentProposal;

/**
 * Very basic implementation of IContentProposal.  This is similar to what is dumped out by
 * Eclipse when hitting ctrl-space.
 * 
 * The content field is what will be written out.
 * The label field is what the user sees. (think String - java.lang)
 * The description field is a long description. (think JavaDoc)
 * The position is where the cursor should be after the edit
 */
public class ContentProposal implements IContentProposal {
	private String content;
	private String description;
	private String label;
	private int position;

	public ContentProposal(String content, String description, String label, int position) {
		this.content = content;
		this.description = description;
		this.label = label;
		this.position = position;
	}
	
	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getCursorPosition() {
		return position;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
