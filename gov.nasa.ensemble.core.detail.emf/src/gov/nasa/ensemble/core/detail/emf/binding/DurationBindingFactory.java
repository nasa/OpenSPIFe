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
package gov.nasa.ensemble.core.detail.emf.binding;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;
import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DurationBindingFactory extends BindingFactory {

	private static String formatPattern = "HH:mm:ss";
	
	@Override
	public Binding createBinding(DetailProviderParameter p) {
		Composite parent = p.getParent();
		return createBinding(parent, true, p);
	}

	public Binding createBinding(Composite parent, boolean createLabel, DetailProviderParameter p) {
		FormToolkit toolkit = p.getDetailFormToolkit();
		EObject target = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		if (feature == null) {
			return null;
		}
		boolean isEditable = pd.canSetProperty(target);
		if (createLabel) {
			EMFDetailUtils.createLabel(parent, toolkit, target, pd);
		}
		CDateTime cdt = new CDateTime(parent, CDT.BORDER | CDT.TAB_FIELDS | CDT.SPINNER);
		cdt.setPattern(formatPattern);
		cdt.setTimeZone("GMT");
		cdt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		cdt.setEnabled(isEditable);
		EMFDetailUtils.bindControlViability(p, new Control[] {cdt});
		DurationUpdateValueStrategy targetToModel = new DurationUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		DurationUpdateValueStrategy modelToTarget = new DurationUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		return EMFDetailUtils.bindEMFUndoable(p, new CDateTimeObservableValue(cdt), targetToModel, modelToTarget);
	}
	
	private class DurationUpdateValueStrategy extends EMFUpdateValueStrategy {
		public DurationUpdateValueStrategy(int updatePolicy) {
			super(updatePolicy);
		}

		@Override
		protected IConverter createConverter(Object fromType, Object toType) {
			if (fromType == Date.class && toType instanceof EAttribute) {
				final IStringifier stringifier = EMFUtils.getStringifier((EAttribute) toType);
				return new Converter(fromType, toType)
				{
					@Override
					public Object convert(Object fromObject)
					{
						Date from = (Date)fromObject;
						String durationString = DurationFormat.getFormattedDuration(from.getTime() / 1000);
						try {
							return stringifier.getJavaObject(durationString, null);
						} catch (ParseException e) {
							LogUtil.error(e);
							return null;
						}
					}

				};
			} else if (toType == Date.class && fromType instanceof EAttribute) {
				final IStringifier stringifier = EMFUtils.getStringifier((EAttribute) fromType);
				return new Converter(fromType, toType)
				{
					@Override
					public Object convert(Object fromObject)
					{
						String durationString = stringifier.getDisplayString(fromObject);
						long durationSeconds = DurationFormat.parseFormattedDuration(durationString);
						return new Date(durationSeconds * 1000);
					}

				};
			}
			return super.createConverter(fromType, toType);
		}
		
	}

}
