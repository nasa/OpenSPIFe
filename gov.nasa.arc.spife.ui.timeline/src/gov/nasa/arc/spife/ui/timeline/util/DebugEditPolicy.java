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
package gov.nasa.arc.spife.ui.timeline.util;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

public class DebugEditPolicy implements EditPolicy {

	private String role = null;

	private EditPart host = null;
	
	public DebugEditPolicy(String role) {
		this.role = role;
	}

	public void activate() {
		System.out.println(role+".activate()\n\t"+getHost());
	}

	public void deactivate() {
		System.out.println(role+".deactivate()\n\t"+getHost());
	}

	public void eraseSourceFeedback(Request request) {
		System.out.println(role+".eraseSourceFeedback("+getRequestString(request)+")\n\t"+getHost());
	}

	public void eraseTargetFeedback(Request request) {
		System.out.println(role+".eraseTargetFeedback("+getRequestString(request)+")\n\t"+getHost());
	}

	public Command getCommand(Request request) {
		System.out.println(role+".getCommand("+getRequestString(request)+")\n\t"+getHost());
		return null;
	}

	public EditPart getHost() {
		return host;
	}

	public EditPart getTargetEditPart(Request request) {
		System.out.println(role+".getTargetEditPart("+getRequestString(request)+")\n\t"+getHost());
		return getHost();
	}

	public void setHost(EditPart editpart) {
		host = editpart;
	}

	public void showSourceFeedback(Request request) {
		System.out.println(role+".showSourceFeedback("+getRequestString(request)+")\n\t"+getHost());
	}

	public void showTargetFeedback(Request request) {
		System.out.println(role+".showTargetFeedback("+getRequestString(request)+")\n\t"+getHost());
	}

	public boolean understandsRequest(Request request) {
		System.out.println(role+".understandsRequest("+getRequestString(request)+")\n\t"+getHost());
		return false;
	}
	
	private String getRequestString(Request request) {
		return request.getClass().getSimpleName()+" '"+request.getType()+"'";
	}
	
}
