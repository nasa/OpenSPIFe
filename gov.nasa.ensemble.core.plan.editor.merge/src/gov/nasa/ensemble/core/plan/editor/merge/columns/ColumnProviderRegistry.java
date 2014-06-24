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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.common.extension.ExtensionUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.editor.merge.AbstractPlanMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.IRuntimeMergeColumnProvider;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityGroupDef;
import gov.nasa.ensemble.dictionary.EParameterDef;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

public class ColumnProviderRegistry {

	private static final Logger trace = Logger.getLogger(ColumnProviderRegistry.class);
	private static ColumnProviderRegistry instance = null;
	
	private final List<IMergeColumnProvider> providers;
	private final List<AbstractMergeColumn> columns;
	private final Map<String, AbstractMergeColumn<?>> idToColumnMap = new LinkedHashMap<String, AbstractMergeColumn<?>>();
	private final Map<Class<? extends IMergeColumnProvider>, IMergeColumnProvider> classToProviderMap = new LinkedHashMap<Class<? extends IMergeColumnProvider>, IMergeColumnProvider>();
	
	public static ColumnProviderRegistry getInstance() {
		if (instance == null) {
			synchronized (ColumnProviderRegistry.class) {
				if (instance == null) {
					instance = new ColumnProviderRegistry();
				}
			}
		}
		return instance;
	}

	private ColumnProviderRegistry() {
		providers = Collections.unmodifiableList(getProviders());
		columns = Collections.unmodifiableList(getColumns());
		for (IMergeColumnProvider provider : providers) {
			classToProviderMap.put(provider.getClass(), provider);
		}
	}

	/**
	 * Returns the set of all merge column providers
	 * @return
	 */
	public List<IMergeColumnProvider> getMergeColumnProviders() {
		return providers;
	}
	
	/**
	 * Return the provider instance for the supplied class.
	 * @param klass
	 * @return
	 */
	public <T extends IMergeColumnProvider> T getMergeColumnProvider(Class<T> klass) {
		return klass.cast(classToProviderMap.get(klass));
	}
	
	/**
	 * Returns the set of all the defined merge columns 
	 * @return
	 */
	public List<AbstractMergeColumn> getMergeColumns() {
		return columns;
	}

	/**
	 * Returns the column with the given id
	 * @param id
	 * @return the column with the given id
	 */
	public AbstractMergeColumn getMergeColumnById(String id) {
		return idToColumnMap.get(id);
	}
	
	/*
	 * Utility functions
	 */
	
	private List<IMergeColumnProvider> getProviders() {
		List<IMergeColumnProvider> providers = new ArrayList<IMergeColumnProvider>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider");
		for (IExtension extension : point.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				try {
					Object object = configurationElement.createExecutableExtension("class");
					if (object instanceof IMergeColumnProvider) {
						providers.add((IMergeColumnProvider) object);
					}
				} catch (CoreException e) {
					trace.error(e.getMessage());
				}
			}
		}
		return providers;
	}
	
	/**
	 * Get all static columns registered for the ColumnProviderRegistry.
	 * @return
	 */
	private List<AbstractMergeColumn> getColumns() {
		List<AbstractMergeColumn> allColumns = new ArrayList<AbstractMergeColumn>();
		for (IMergeColumnProvider provider : providers) {
			List<? extends AbstractMergeColumn> columns;
			try {
				columns = provider.getColumns();
			} catch (RuntimeException e) {
				trace.error("failed to get columns from a provider: " + provider.getClass());
				continue;
			}
			for (AbstractMergeColumn<?> column : columns) {
				String id = column.getId();
				if (id.contains(",")) {
					trace.error("ids for columns must not contain commas: " + id);
				} else {
					if (idToColumnMap.containsKey(id)) {
						ITreeTableColumn<?> otherColumn = idToColumnMap.get(id);
						trace.warn("two columns have the same id ("+id+"): " + column + " and " + otherColumn);
					}
					idToColumnMap.put(id, column);
				}
			}
			allColumns.addAll(columns);
		}
		for (AbstractMergeColumn column : getAttributeColumns()) {
			addColumn(allColumns, column);
		}
		for (AbstractMergeColumn column : new ParameterColumnProvider().getColumns()) {
			addColumn(allColumns, column);
		}
		for (AbstractMergeColumn column : new ActivityClassificationColumnProvider().getColumns()) {
			addColumn(allColumns, column);
		}
		return allColumns;
	}
	
	/**
	 * Get all columns for the ColumnProviderRegistry including plan specific columns.
	 * @param plan
	 * @return
	 */
	public List<AbstractMergeColumn> getExtraColumns(EPlan plan) {
		List<AbstractMergeColumn> allColumns = new ArrayList();
		List<AbstractPlanMergeColumnProvider> planColumnProviders = getRuntimePlanMergeColumnProviders(plan);
		for (AbstractPlanMergeColumnProvider provider : planColumnProviders) {
			allColumns.addAll(provider.getColumns());
		}
		return allColumns;
	}
	
	private static List<AbstractPlanMergeColumnProvider> getRuntimePlanMergeColumnProviders(EPlan plan) {
		List<AbstractPlanMergeColumnProvider> providers = new ArrayList();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint("gov.nasa.ensemble.common.ClassRegistry");
		for (IConfigurationElement lookupElement : extensionPoint.getConfigurationElements()) {
			String className = lookupElement.getAttribute("class");
			if (IRuntimeMergeColumnProvider.class.getName().equals(className)) {
				for (IConfigurationElement implementationElement : lookupElement.getChildren("implementation")) {
					Class implClass = ExtensionUtils.getClass(implementationElement, implementationElement.getAttribute("class"));
					if (AbstractPlanMergeColumnProvider.class.isAssignableFrom(implClass)) {
						try {
							Constructor constructor = implClass.getConstructor(new Class[] { EPlan.class });
							AbstractPlanMergeColumnProvider provider = (AbstractPlanMergeColumnProvider) constructor.newInstance(new Object[] { plan } );
							providers.add(provider);
						} catch (Exception e) {
							LogUtil.error(e);
						}
					}
				}
			}
		}
		return providers;
	}

	private void addColumn(List<AbstractMergeColumn> allColumns, AbstractMergeColumn column) {
		String id = column.getId();
		if (id.contains(",")) {
			trace.error("ids for columns must not contain commas: " + id);
		}  else {
			if (idToColumnMap.containsKey(id)) {
				trace.debug("Attribute handler for id '"+id+"' already defined, ignoring default");
			} else {
				idToColumnMap.put(id, column);
				allColumns.add(column);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<AbstractMergeColumn<?>> getAttributeColumns() {
		ParameterDescriptor descriptor = ParameterDescriptor.getInstance();
		IMergeColumnProvider provider = new NamedMergeColumnProvider("Attributes");
		List<AbstractMergeColumn<?>> columns = new ArrayList<AbstractMergeColumn<?>>();
		for (EParameterDef parameterDef : ActivityDictionary.getInstance().getAttributeDefs()) {
			if (parameterDef instanceof EAttribute) {
				EAttribute attribute = (EAttribute) parameterDef;
				if (descriptor.isVisible(attribute)) {
					String displayName = ParameterDescriptor.getInstance().getDisplayName(attribute);
					ParameterColumn column = new ParameterColumn(provider, displayName, ParameterColumn.getDefaultWidth(parameterDef, descriptor));
					columns.add(column);
				}
			}
		}
		return columns;
	}

	private final static class ParameterColumnProvider extends NamedMergeColumnProvider {
		
		private final List<AbstractMergeColumn<?>> columns = new ArrayList<AbstractMergeColumn<?>>();
		
		public ParameterColumnProvider() {
			super("Parameter");
			final ParameterDescriptor descriptor = ParameterDescriptor.getInstance();
			for (EActivityDef activityDef : ActivityDictionary.getInstance().getActivityDefs()) {
				EList<EStructuralFeature> features = activityDef.getEAllStructuralFeatures();
				for (EStructuralFeature feature : features) {
					if (descriptor.isVisible(feature)) {
						String displayName = descriptor.getDisplayName(feature);
						ParameterColumn column = new ParameterColumn(this, displayName, ParameterColumn.getDefaultWidth(feature, descriptor));
						columns.add(column);
					}
				}
			}
			final EActivityGroupDef groupDefinition = ActivityDictionary.getInstance().getActivityGroupDef();
			if (groupDefinition == null) {
				LogUtil.warn("No Activity Group Defined in AD");
			} else {
				for (EStructuralFeature f : groupDefinition.getEStructuralFeatures()) {
					if (descriptor.isVisible(f)) {
						String displayName = descriptor.getDisplayName(f);
						ParameterColumn column = new ParameterColumn(this, displayName, ParameterColumn.getDefaultWidth(f, descriptor));
						columns.add(column);
					}
				}
			}			
		}
		
		@Override
		public List<? extends AbstractMergeColumn<?>> getColumns() {
			return columns;
		}
	}
	
	private final static class ActivityClassificationColumnProvider extends NamedMergeColumnProvider {
		
		private List<AbstractMergeColumn<ParameterFacet<String>>> columns = new ArrayList<AbstractMergeColumn<ParameterFacet<String>>>();
		
		public ActivityClassificationColumnProvider() {
			super("Classification");
			columns.add(initColumn("Subsystem", DictionaryPackage.Literals.EACTIVITY_DEF__CATEGORY));
			columns.add(initColumn("Activity Type", EcorePackage.Literals.ENAMED_ELEMENT__NAME));
		}
		
		@Override
		public List<? extends AbstractMergeColumn<?>> getColumns() {
			return columns;
		}
		
		private AbstractMergeColumn<ParameterFacet<String>> initColumn(String columnName, final EStructuralFeature f) {
			AbstractMergeColumn column = new AbstractMergeColumn<ParameterFacet<String>>(this, columnName, 50) {
				
				@Override
				public boolean needsUpdate(Object feature) {
					return false;
				}

				@Override
				public ParameterFacet<String> getFacet(Object element) {
					if (element instanceof EActivity) {
						EObject data = ((EActivity) element).getData();
						if (data != null) {
							Object value = data.eClass().eGet(f);
							return new ParameterFacet(data.eClass(), f, value);
						}
					}
					return null;
				}
				
				@Override
				public String getText(ParameterFacet<String> facet) {
					if (facet == null || (facet != null && facet.getValue() == null)) return "-";
					return facet.getValue();
				}
				
			};
			return column;
		}
	}
}
