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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.PlanService;
import gov.nasa.ensemble.core.model.plan.activityDictionary.provider.DynamicDictonaryAdapterFactory;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.IMember;
import gov.nasa.ensemble.core.plan.IMemberFactory;
import gov.nasa.ensemble.core.plan.MemberRegistry;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.preferences.PlanEditorPreferences;
import gov.nasa.ensemble.dictionary.provider.DefinedObjectItemProviderAdapterFacory;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.transaction.ExtensionPointResourceSetListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.UnresolvedProxyCrossReferencer;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelectionProvider;

/**
 * This is a wrapper around the plan object that is  provided to the editors that
 * will exist inside the MultiPagePlanEditor.
 * 
 * This class is the Controller in the MVC sense that is shared between the PlanTableWidget
 * and the SPIF-e timeline.  This Controller handles SelectionChangeEvents that are of interest to
 * both the Table and the Timeline.
 * 
 * There is also a controller that is table-specific (TableViewer), through which 
 * model changes are propagated. 
 */
public class PlanEditorModel extends AbstractEnsembleEditorModel {

	public static final String PRODUCT_CONFIGURATION_DIRECTORY = "configuration.planning";
	public static final IResourceModelingController RESOURCE_MODELING_CONTROLLER = ClassRegistry.createInstance(IResourceModelingController.class);
	
	private final ISelectionProvider selectionProvider = new EnsembleSelectionProvider(this.toString());
	private TransactionalEditingDomain editingDomain;
	private final EPlan plan;

	private Collection<PlanService> services;
	
	public PlanEditorModel(EPlan plan) {
		super(isReadonly(plan));
		this.plan = plan;
		configureEditingDomain(plan);
	}
	
	private static boolean isReadonly(EPlan plan) {
		if (plan == null) {
			throw new NullPointerException("null plan");
		}
		return plan.isReadOnly();
	}
	
	private static TransactionalEditingDomain getEditingDomain(EPlan plan) {
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		if (domain == null) {
			PlanFactory.getInstance().initPlan(plan);
			domain = TransactionUtils.getDomain(plan);
		}
		return domain;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public Object getAdapter(Class adapter) {
		if (EObject.class.isAssignableFrom(adapter))
			return plan;
		return null;
	}
	
	public EPlan getEPlan() {
		return plan;
	}
	
	public Plan getPlan() {
		return WrapperUtils.getRegistered(plan);
	}

	@Override
	public IUndoContext getUndoContext() {
		return TransactionUtils.getUndoContext(plan);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		deactivatePlanServices();
		services = null;
		// Dispose the adapter factory
		AdapterFactory adapterFactory = EMFUtils.getAdapterFactory(editingDomain);
		if (adapterFactory instanceof IDisposable) {
			((IDisposable)adapterFactory).dispose();
		}
		// Dispose the plan data once all the listeners are removed
		WrapperUtils.dispose(plan);
		editingDomain = null;
	}

	public ISelectionProvider getSelectionProvider() {
		return selectionProvider;
	}
	
	private void configureEditingDomain(final EPlan plan) {
		editingDomain = getEditingDomain(plan);
		Resource resource = plan.eResource();
		if (plan.isReadOnly() && editingDomain instanceof AdapterFactoryEditingDomain && resource != null) {
			((AdapterFactoryEditingDomain)editingDomain).getResourceToReadOnlyMap().put(resource, Boolean.TRUE);
		}
		final ResourceSet resourceSet = editingDomain.getResourceSet();
		IFile file = EMFUtils.getFile(resource);
		if (file != null) {
			final IProject project = file.getProject();
			if (project != null) {
				resourceSet.setURIConverter(new ProjectURIConverter(project));
			}
		}
		configureAdapterFactory(editingDomain);
		// Protect against interference from a concurrent thread (e.g. a PlanAdviser) that may also
		// be adding to the the editing domain's resource set
		TransactionUtils.reading(editingDomain, new Runnable() {
			@Override
			public void run() {
				EPlanUtils.contributeProductResources(plan);
			}
		});
		pruneDanglingReferences(plan);
	}
	
	public static void configureAdapterFactory(EditingDomain editingDomain) {
		if (editingDomain instanceof AdapterFactoryEditingDomain) {
			AdapterFactory f = ((AdapterFactoryEditingDomain)editingDomain).getAdapterFactory();
			if (f instanceof ComposedAdapterFactory) {
				ComposedAdapterFactory factory = (ComposedAdapterFactory)f;
				factory.addAdapterFactory(new DefinedObjectItemProviderAdapterFacory());
				factory.addAdapterFactory(new DynamicDictonaryAdapterFactory());
			}
		}
	}

	public void interfaceCreated() {
		for (IMemberFactory<IMember> f : MemberRegistry.getInstance().getFactories()) {
			WrapperUtils.getMember(getEPlan(), f.getMemberClass());
		}
		if (RESOURCE_MODELING_CONTROLLER != null && !PlanEditorPreferences.isAutomaticResourceModeling()) {
			RESOURCE_MODELING_CONTROLLER.enableAutomaticResourceModeling(plan, false);
		}
		// Instantiate the services and activate
		ExtensionPointResourceSetListener.addListener(editingDomain);
		activatePlanServices();
		
		//Force garbage collection after opening a plan
		System.gc();
	}
	
	public void activatePlanServices() {
		if (services == null) {
			services = PlanService.Registry.createServices(getEPlan());
			for (PlanService service : services) {
				try {
					service.activate();
				} catch (ThreadDeath t) {
					throw t;
				} catch (Throwable e) {
					LogUtil.error("activating service", e);
				}
			}
		}
	}
	
	public void deactivatePlanServices() {
		if (services != null) {
			for (PlanService service : services) {
				try {
					service.deactivate();
				} catch (ThreadDeath t) {
					throw t;
				} catch (Throwable e) {
					LogUtil.error("deactivating service", e);
				}
			}
		}
	}

	private void pruneDanglingReferences(final EPlan plan) {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			@SuppressWarnings("unchecked")
			public void run() {
				Map<EObject, Collection<Setting>> proxiesMap = UnresolvedProxyCrossReferencer.find(plan);
				for (EObject proxy : proxiesMap.keySet()) {
					for (Setting setting : proxiesMap.get(proxy)) {
						EStructuralFeature feature = setting.getEStructuralFeature();
						if (feature instanceof EReference) {
							Object newValue = null;
							if (feature.isMany()) {
								newValue = new ArrayList((Collection)setting.get(true));
								((Collection)newValue).remove(proxy);
							}
							setting.set(newValue);
						}
					}
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T extends PlanService> T getPlanService(Class<T> klass) {
		if (services == null) {
			return null;
		}
		for (PlanService service : services) {
			if (klass.equals(service.getClass())) {
				return (T) service;
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof PlanEditorModel)) {
			return false;
		}
		PlanEditorModel other = ((PlanEditorModel)object);
		try {	
			Object thisSourceURI = WrapperUtils.getRegistered(plan).getTransientProperty(EditorPlugin.ATTRIBUTE_SOURCE_URI);
			Object otherSourceURI = WrapperUtils.getRegistered(other.plan).getTransientProperty(EditorPlugin.ATTRIBUTE_SOURCE_URI);
			return (thisSourceURI != null) && thisSourceURI.equals(otherSourceURI);
		} catch (Exception e) {
			Logger.getLogger(PlanEditorModel.class).error("error in equals", e);
		}
		return false;
	}

}
