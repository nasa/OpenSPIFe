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
package gov.nasa.ensemble.common.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

public class WizardResourceVisitor implements IResourceVisitor {

	private ArrayList items = new ArrayList();
	private String[] typeArray = new String[]{"chart"};
	
	public List getItems() {
		return items;
	}
	
	public WizardResourceVisitor( String type ){
		if( type != null){ 
			if(!type.trim().equalsIgnoreCase("")){
				this.typeArray = new String[]{type};
			}
		}
	}
	
	public WizardResourceVisitor( String[] typeArray ){
		if( typeArray != null ){
			if( typeArray.length > 0){
				this.typeArray = typeArray;
			}
		}
	}
	
	public boolean extensionExistsInTypeArray( String fileExtension ){
		boolean result = false;
		for( int i = 0; i < this.typeArray.length; i++ ){
			String type = this.typeArray[i];
			if( type != null ){
				if( ! type.trim().equalsIgnoreCase("") ){
					if( fileExtension.equalsIgnoreCase(type) ) {
						return true;
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean visit(IResource resource)  {
		if(resource instanceof IFile) {
			String fileExtension = resource.getFileExtension();
			if(fileExtension != null && extensionExistsInTypeArray(fileExtension)) {
				items.add(resource);
				return false;
			}
		}
		return true;
	}

}
