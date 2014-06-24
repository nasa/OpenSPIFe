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
package gov.nasa.arc.spife.core.plan.editor.timeline.models;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EAttributeTimelineContentProvider extends GroupingTimelineContentProvider {

	private final EAttribute attributeDef;

	public EAttributeTimelineContentProvider(EPlan plan, EAttribute attributeDef) {
		super(plan);
		this.attributeDef = attributeDef;
	}
	
	@Override
	public String getName() {
		return ParameterDescriptor.getInstance().getDisplayName(attributeDef);
	}

	@Override
	protected List<Object> getActivityValues(EActivity activity) {
		EObject data = activity.getData();
		if (data == null) {
			return null;
		}
		EClass eClass = data.eClass();
		if (eClass == null) {
			return null;
		}
		EStructuralFeature feature = eClass.getEStructuralFeature(attributeDef.getName());
		if (feature == null) {
			return null;
		}
		Object value = data.eGet(feature);
		if (attributeDef.isMany()) {
			return value == null ? Collections.emptyList() : new ArrayList<Object>((Collection) value);
		}// else...
		return Collections.singletonList(value);
	}

	@Override
	protected Comparator getGroupingValuesComparator() {
		EClassifier eType = attributeDef.getEType();
		if (eType instanceof EEnum) {
			final EEnum eEnum = (EEnum) eType;
			return new Comparator<EEnumLiteral>() {
				@Override
				public int compare(EEnumLiteral o1, EEnumLiteral o2) {
					int v1 = eEnum.getELiterals().indexOf(o1);
					int v2 = eEnum.getELiterals().indexOf(o2);
					return v1 - v2;
				}
			};
		}
		return null;
	}

	@Override
	protected boolean isRelevant(EStructuralFeature f) {
		return attributeDef.getName().equals(f.getName());
	}

}
