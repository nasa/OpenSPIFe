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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;

import org.eclipse.core.databinding.Binding;
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

public class CDateTimeBindingFactory extends BindingFactory {

	private static final String DATE_FORMAT_PATTERN = EnsembleProperties.getProperty("date.format.display", "yyyy-MM-dd HH:mm:ss");
	private static final String TIME_FORMAT_PATTERN = EnsembleProperties.getProperty("time.format.display", "h:mm a");
	private boolean useTimeFormat = false;
	
	public CDateTimeBindingFactory(boolean useTimeFormat) {
		this.useTimeFormat = useTimeFormat;
	}
	
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
		CDateTime cdt = new CDateTime(parent, CDT.BORDER | 
				CDT.TAB_FIELDS |
				CDT.SPINNER);
		if (useTimeFormat) {
			cdt.setPattern(TIME_FORMAT_PATTERN);
		} else {
			cdt.setPattern(DATE_FORMAT_PATTERN);
		}
		cdt.setTimeZone("GMT");
		cdt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		cdt.setEnabled(isEditable);
		toolkit.adapt(cdt, true, true);
		EMFDetailUtils.bindControlViability(p, new Control[] {cdt});
		return EMFDetailUtils.bindEMFUndoable(p, new CDateTimeObservableValue(cdt));
	}

}
