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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class UniqueMultiSelectSetOperation extends AbstractTransactionUndoableOperation {

	private final DetailProviderParameter parameter;
	private final Map<Object, TriState> map;
	private final Map<Object, TriState> originalMap;

	private CompoundCommand compoundCommand = null;
	private EditingDomain domain;

	public UniqueMultiSelectSetOperation(DetailProviderParameter parameter, Map<Object, TriState> originalMap, Map<Object, TriState> map) {
		super("Edit " + getFeatureDisplayName(parameter));
		this.parameter = parameter;
		this.originalMap = originalMap;
		this.map = map;

	}

	private static String getFeatureDisplayName(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		Object feature = parameter.getPropertyDescriptor().getFeature(target);
		if (target != null && feature instanceof EStructuralFeature) {
			return EMFUtils.getDisplayName(target, (EStructuralFeature) feature);
		}
		return "";
	}

	@Override
	protected void dispose(UndoableState state) {
		// no disposal
	}

	@Override
	protected void execute() throws Throwable {
		List<EObject> targets = new ArrayList<EObject>();
		List<IItemPropertyDescriptor> pds = new ArrayList<IItemPropertyDescriptor>();
		EObject target = parameter.getTarget();
		if (target instanceof MultiEObject) {
			targets.addAll(((MultiEObject) target).getEObjects());
			pds.addAll(((MultiItemPropertyDescriptor) parameter.getPropertyDescriptor()).getPropertyDescriptors());
		} else {
			targets.add(target);
			pds.add(parameter.getPropertyDescriptor());
		}

		if (targets.isEmpty()) {
			return;
		}

		domain = AdapterFactoryEditingDomain.getEditingDomainFor(targets.get(0));
		ObjectUndoContext domainUndoContext = new ObjectUndoContext(domain);
		if (!hasContext(domainUndoContext)) {
			addContext(domainUndoContext);
		}

		compoundCommand = new CompoundCommand();
		for (int i = 0; i < pds.size(); i++) {
			IItemPropertyDescriptor pd = pds.get(i);
			EObject model = target = targets.get(i);
			EStructuralFeature feature = (EStructuralFeature) pd.getFeature(model);
			EObject commandOwner = EMFDetailUtils.getCommandOwner(pd, model);
			if (commandOwner != null) {
				model = commandOwner;
			}
			for (Object referencedObject : map.keySet()) {
				TriState triState = map.get(referencedObject);
				if (TriState.QUASI == triState) {
					continue;
				}
				boolean contained = ((Collection) model.eGet(feature)).contains(referencedObject);
				TriState originalTriState = originalMap.get(referencedObject);
				if (!CommonUtils.equals(triState, originalTriState)) {
					if (TriState.TRUE == triState && !contained) {
						compoundCommand.append(AddCommand.create(domain, model, feature, referencedObject));
					} else if (TriState.FALSE == triState && contained) {
						compoundCommand.append(RemoveCommand.create(domain, model, feature, referencedObject));
					}
				}
			}
		}
		if (!compoundCommand.isEmpty()) {
			gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(domain, new Runnable() {
				@Override
				public void run() {
					compoundCommand.execute();
				}
			});
		}
	}

	@Override
	public String toString() {
		return getLabel();
	}

	@Override
	protected void undo() throws Throwable {
		if (compoundCommand != null && !compoundCommand.isEmpty()) {
			gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(domain, new Runnable() {
				@Override
				public void run() {
					compoundCommand.undo();
				}
			});
		}
	}

}
