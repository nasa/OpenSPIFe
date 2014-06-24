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
package gov.nasa.ensemble.core.plan.parameters;


import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class EMFParameterSerializer implements IParameterSerializer<Object> {

	private final Logger trace = Logger.getLogger(EMFParameterSerializer.class);
	
	private final EDataType eDataType;
	
	public EMFParameterSerializer(EDataType eDataType) {
		this.eDataType = eDataType;
	}
	
	@Override
	public String getHibernateString(Object javaObject) {
		return eDataType.getEPackage().getEFactoryInstance().convertToString(eDataType, javaObject);
	}

	@Override
	public Object getJavaObject(String hibernateString) {
		try {
			return EcoreUtil.createFromString(eDataType, hibernateString);
		} catch (NumberFormatException e) {
			trace.error("bad string for type "+eDataType+": '"+hibernateString+"'", e);
		}
		return null;
	}

}
