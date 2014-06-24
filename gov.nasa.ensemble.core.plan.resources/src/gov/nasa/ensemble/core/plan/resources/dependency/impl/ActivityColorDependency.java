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
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;

import org.eclipse.swt.graphics.RGB;

public class ActivityColorDependency extends ActivityDependency {

	public ActivityColorDependency(DependencyMaintenanceSystem dms, EActivity activity) {
		super(dms, activity);
	}

	@Override
	public String getName() {
		return super.getName() + ".color";
	}
	
	@Override
	public void applyValue() {
		RGB rgb = (RGB) getValue();
		if (rgb != null) {
			ERGB eRGB = ColorUtils.getAsERGB(rgb);
			getActivity().getMember(CommonMember.class).setColor(eRGB);
		}
		super.applyValue();
	}

	@Override
	public boolean update() {
		EActivity activity = getActivity();
		EActivityDef def = ADParameterUtils.getActivityDef(activity);
		String colorExpression = ResourceUtils.getColorExpression(def);
		Object value = null;
		if (colorExpression  != null) {
			try {
                value = ColorUtils.parseRGB(colorExpression);
	        } catch (Exception e) {
                // tried to shortcut it, use as expression now
	        }
	        if (value == null) {
                value = getValue(colorExpression, null);
                if (value != null) {
                    value = ColorUtils.parseRGB((String) value);
                }
	        }
		}
		if (!CommonUtils.equals(value, getValue())) {
			setValue(value);
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) 
				&& obj instanceof ActivityColorDependency;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + "color".hashCode();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
