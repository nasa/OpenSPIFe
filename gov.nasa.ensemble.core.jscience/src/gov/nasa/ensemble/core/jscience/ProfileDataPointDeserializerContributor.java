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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.emf.patch.DeserializerContributor;

import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class ProfileDataPointDeserializerContributor extends DeserializerContributor {

	
	@Override
	public boolean isDeserializer(EObject target, EStructuralFeature feature) {
		return (target instanceof Profile && feature == JSciencePackage.Literals.PROFILE__DATA_POINTS);
	}
	
	@Override
	public Object deserialize(EObject target, EStructuralFeature feature, String value) {
	 	if (isDeserializer(target, feature)) {
	 		String v = value.substring(value.indexOf("[")+1, value.lastIndexOf("]")); // White-space varies if its numeric or enum
			int delim = v.lastIndexOf(',');
			String dateString = v.substring(0, delim).trim();
			String valueString = v.substring(delim + 1).trim();
	 		
			Profile profile = (Profile) target;
	 		EDataType dataType = profile.getDataType();
	 		Date date = (Date)EcoreFactory.eINSTANCE.createFromString(EcorePackage.Literals.EDATE, dateString);
	 		Object object = null;
	 		if (valueString != null && !valueString.isEmpty() && !valueString.equals("null")) {
	 			object = EcoreUtil.createFromString(dataType, valueString);
	 		}
			DataPoint<Object> dataPoint = JScienceFactory.eINSTANCE.createEDataPoint(date, object);
			return dataPoint;
	 	}
	 	return null;
	}
	
	@Override
	public void setValue(EObject target, EStructuralFeature feature, Object object) {
		if (isDeserializer(target, feature) && object instanceof DataPoint) {
			List<DataPoint> dataPoints = (List<DataPoint>) target.eGet(feature);
			dataPoints.add((DataPoint) object);
		}
	}
	
}
