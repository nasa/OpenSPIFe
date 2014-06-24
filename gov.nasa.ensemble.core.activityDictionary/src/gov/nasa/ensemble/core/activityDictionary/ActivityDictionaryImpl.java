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
package gov.nasa.ensemble.core.activityDictionary;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryEvent.TYPE;
import gov.nasa.ensemble.core.activityDictionary.ui.preference.ActivityDictionaryPreferences;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.INamedDefinition;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;
import gov.nasa.ensemble.emf.constraint.ModelCommonClientSelector;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;

public class ActivityDictionaryImpl extends ActivityDictionary {

	private EActivityDictionary defaultActivityDictionary;

	public ActivityDictionaryImpl() {
		super();
	}

	@Override
	public void load(InputStream inputStream) throws Exception {
		throw new UnsupportedOperationException("Load is not done through an input stream");
	}

	@Override
	public void load() throws Exception {
		load(ActivityDictionaryPreferences.getActivityDictionaryLocation());
	}
	
	@Override
	public void load(URI uri) {
		EActivityDictionary eActivityDictionary = DictionaryUtil.load(uri);
		if (eActivityDictionary != null) {
			EMFUtils.logDiagnostics(LogUtil.logger(), eActivityDictionary.eResource());
			load(eActivityDictionary);
		} else {
			throw new IllegalStateException("failed to load as EActivityDictionary: " + uri);
		}
	}
	
	protected final void finishLoading(Resource eResource) {
		EMFUtils.setResourceReadOnlyFor(this, true);
		EPackage.Registry.INSTANCE.put(this.getNsURI(), this);
		try {
			URL url = this.getURL();
			if (url != null) {
				String string = url.toString();
				EPackage.Registry.INSTANCE.put(string, this);
			}
		} catch (IOException e) {
			LogUtil.error(e);
		}
		setEFactoryInstance(new DynamicDictionaryFactory());
		ModelCommonClientSelector.registerPackage(this.getNsURI(), ActivityDictionaryPlugin.getDefault().getBundle());
		fireActivityDictionaryEvent(TYPE.LOADED);
	}
	
	@Override
	public void ensureDefaultDictionary() {
		if (defaultActivityDictionary != null) {
			String currentURI = getNsURI();
			String defaultURI = defaultActivityDictionary.getNsURI();
			if (!CommonUtils.equals(currentURI, defaultURI)) {
				LogUtil.warn("somebody made a mess and didn't clean it up: " + currentURI);
				restoreDefaultDictionary();
			}
		}
	}

	@Override
	public void restoreDefaultDictionary() {
		if (defaultActivityDictionary != null) {
			load(defaultActivityDictionary);
		}
	}

	private Collection<? extends EParameterDef> defaultAttributeDefs;
	private Collection<? extends EClassifier> defaultClassifiers;
	private Collection<? extends INamedDefinition> defaultExtendedDefinitions;

	protected final synchronized void load(EActivityDictionary ad) {
		clearCache();
		getAttributeDefs().clear();
		getExtendedDefinitions().clear();
		EPackage.Registry.INSTANCE.remove(this.getNsURI());
		
		if (ad == defaultActivityDictionary) {
			getAttributeDefs().addAll(defaultAttributeDefs);
			getEClassifiers().addAll(defaultClassifiers);
			getExtendedDefinitions().addAll(defaultExtendedDefinitions);
		} else {
			EClass baseClass = EcoreFactory.eINSTANCE.createEClass();
			baseClass.setName("EAD__BaseClass");
			for (Object object : ad.getAttributeDefs()) {
				baseClass.getEStructuralFeatures().add(EMFUtils.copy((EParameterDef) object));
			}
			ad.getEClassifiers().add(baseClass);
			for (EClassifier klass : ad.getEClassifiers()) {
				if (klass instanceof EActivityDef) {
					((EActivityDef)klass).getESuperTypes().add(baseClass);
				}
			}
			if (isLoadingDefaultDictionary()) {
				defaultActivityDictionary = ad;
				defaultAttributeDefs = new ArrayList<EParameterDef>(ad.getAttributeDefs());
				defaultClassifiers = new ArrayList<EClassifier>(ad.getEClassifiers());
				defaultExtendedDefinitions = new ArrayList<INamedDefinition>(ad.getExtendedDefinitions());
			}
			getAttributeDefs().addAll(ad.getAttributeDefs());
			getEClassifiers().addAll(ad.getEClassifiers());
			getExtendedDefinitions().addAll(ad.getExtendedDefinitions());
		}
				
		setNsPrefix(ad.getNsPrefix());
		setNsURI(ad.getNsURI());
		setName(ad.getName());
		setAuthor(ad.getAuthor());
		setDate(ad.getDate());
		setDescription(ad.getDescription());
		
		String version = ad.getVersion();
		// remove quotes from the version number
		if (version != null) {
			version = version.replaceAll("\"", "");
		}
		setVersion(version);
		
		Resource eResource = ad.eResource();
		finishLoading(eResource);
	}

}
