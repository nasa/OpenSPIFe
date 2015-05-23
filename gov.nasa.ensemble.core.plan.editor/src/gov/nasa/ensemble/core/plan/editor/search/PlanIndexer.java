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
package gov.nasa.ensemble.core.plan.editor.search;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;

/**
 * This class is an abstraction for the lucene IndexWriter object. The purpose
 * of this object is to store the plan element attributes along with the IndexWriter
 * which creates the Index file. Each plan element will correspond to a certain
 * document in the Index.
 *
 * @author Alonzo Benavides
 * @bug No known bugs
 */
public class PlanIndexer {
	 
	/* Analyzer which MUST be used in PlanSearcher to ensure correct parsing */
	public static final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35, Collections.EMPTY_SET);
	
	/* Suffix to add to a field name to index by the entire unanalyzed value */
	public static final String FIELDNAME_NOT_ANALYZED_SUFFIX = "RAW";

	/* The writer which creates the index file */
	private IndexWriter writer = null;

	/* Directory stored in RAM for index file */
	private RAMDirectory dir = null;

	/* Collection of attributes to index upon */
	private Vector<String> attributes = null;

	/* Sub-collection of attributes that have boolean values */
	private Vector<String> booleanAttributes = null;

	/* Registry for creating an id for each plan element so we may reference them */
	public IdentifiableRegistry<EPlanElement> idRegistry = new IdentifiableRegistry<EPlanElement>();

	/**
	 * Constructor for PlanIndexer
	 */
	public PlanIndexer(){
		booleanAttributes = new Vector<String>();
		attributes = new Vector<String>();
		dir = new RAMDirectory();

		try {
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
			writer = new IndexWriter(dir, config);
			writer.commit();
		}
		catch (IOException e) {
			System.out.println("IOException in opening IndexWriter: " + e.getMessage());
		}
	}

	/**
	 * Creates a new Index file with no entries. Calling this method will destroy
	 * all the information stored previously.
	 */
	public void clear(){
		if (dir != null) {
			dir.close();
		}
		dir = new RAMDirectory();
		idRegistry = new IdentifiableRegistry<EPlanElement>();

		try {
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
			writer = new IndexWriter(dir, config);
			writer.commit();
		}
		catch (IOException e) {
			System.out.println("IOException in opening IndexWriter: " + e.getMessage());
		}
	}

	/**
	 * Commits changes to the index file so that the latest changes are reflected
	 * to any IndexReader or IndeaxSearcher objects.
	 */
	public void refresh(){
		try{
			writer.commit();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Closes stream to index file.
	 */
	public void close(){
		try{
			writer.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void indexElementByLabel(EPlanChild element) {
		if(element == null){
			return;
		}

		Document doc = new Document();

		String id = element.getPersistentID();
		idRegistry.registerIdentifiable(element, id);
		doc.add(new Field("id", id, Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field("exist", "true", Field.Store.YES, Field.Index.ANALYZED));

		String stringValue = null;
		IItemLabelProvider lp = EMFUtils.adapt(element, IItemLabelProvider.class);
		if (lp != null) {
			stringValue = lp.getText(element);
		} else {
			stringValue = element.getName();
		}
		if (stringValue != null) {
			doc.add(new Field("name", PlanSearcher.specialQuoting(stringValue), Field.Store.YES, Field.Index.ANALYZED));
			attributes.add("name");
		}
		try {
			writer.addDocument(doc);
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indexes the attributes of a given EPlanElement. The attributes
	 * indexed are determined by the field names in the attributes
	 * vector.
	 *
	 * @param element element that has attributes to be indexed
	 */
	public synchronized void indexAttributes(EPlanElement element){
		Document doc = new Document();
		String id = element.getPersistentID();
		idRegistry.registerIdentifiable(element, id);
		doc.add(new Field("id", id, Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field("exist", "true", Field.Store.YES, Field.Index.ANALYZED));
		
		for (IPlanSearchProvider provider : PlanSearchProviderRegistry.getInstance().getProviders()) {
			String featureName = provider.getFeatureName();
			String facet = provider.getFacet(element);
			addToDoc(doc, element, featureName, facet);
		}
		
		for(String fieldName : attributes) {
			addToDoc(doc, element, fieldName, null);
		}
		try {
			writer.addDocument(doc);
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addToDoc(Document doc, EPlanElement element, String fieldName, String value) {
		IStringifier<String> stringy = StringifierRegistry.getStringifier(String.class);
		EObject data = element.getData();
		Field.Index indexMode = Field.Index.ANALYZED;
		EStructuralFeature tempFeature = null;
		// For secondary features, e.g. opsNote:englishLongName
		int colonIndex = fieldName.indexOf(':');
		String secondaryFieldName = null;
		if(colonIndex > 0) {
			secondaryFieldName = fieldName.substring(colonIndex+1);
			fieldName = fieldName.substring(0, colonIndex);
		}
		
		if (data != null) {
			tempFeature = data.eClass().getEStructuralFeature(fieldName);			
		}
								
		if(fieldName.equals(PlanSearchPage.NAME_STRING) && element.eClass().getEStructuralFeature("name") != null){
			value = stringy.getDisplayString((String) element.eGet(element.eClass().getEStructuralFeature("name")));
		} else {
			EActivityDef activityDef = null;
			if(element instanceof EActivity) {
				activityDef = ADParameterUtils.getActivityDef((EActivity)element);
			}
			
			else if(element instanceof EActivityGroup && tempFeature != null && !tempFeature.isMany()) {
				value = ADParameterUtils.getParameterString(element, fieldName);
			}
					
			if(fieldName.equals(PlanSearchPage.TYPE_STRING) && element instanceof EActivity){
				if(activityDef != null){
					value = activityDef.getName();
					indexMode = Field.Index.NOT_ANALYZED;
				}
			}
			else if(fieldName.equals(PlanSearchPage.CATEGORY_STRING)  && element instanceof EActivity){
				if(activityDef != null){
					value = activityDef.getCategory();
					indexMode = Field.Index.NOT_ANALYZED;
				}
			}
			else if(fieldName.equals(PlanSearchPage.SCHEDULED_STRING)  && element instanceof EActivity){
				value = (element.getMember(TemporalMember.class).getScheduled() ? "true" : "false");
				indexMode = Field.Index.NOT_ANALYZED;
			}
			else if(fieldName.equals("duration")){
				value = ADParameterUtils.getParameterString(element, fieldName);
			}
						
			else if (tempFeature != null && tempFeature.isMany()) {
					EStructuralFeature eStructuralFeature = tempFeature;
					StringBuilder stringBuilder = new StringBuilder();
					IItemPropertySource source = EMFUtils.adapt(element, IItemPropertySource.class);
					IItemPropertyDescriptor pd = source.getPropertyDescriptor(element, eStructuralFeature);
					if (pd != null) {
						Object propertyValue = EMFUtils.getPropertyValue(pd, element);
						if (propertyValue != null && StringifierRegistry.hasRegisteredStringifier(fieldName)) {
							IStringifier stringifier = StringifierRegistry.getStringifier(fieldName);
							String displayString = stringifier.getDisplayString(propertyValue);
							if(displayString != null) {
								stringBuilder.append(displayString);
							}
						}
					}
				
					ArrayList<EObject> elements = new ArrayList<EObject>();
					Object eGet = element.getData().eGet(eStructuralFeature);
					Collection<? extends EObject> collection = (Collection<? extends EObject>)eGet;
					elements.addAll(collection);
					Iterator<EObject> iterator = elements.iterator();
					while(iterator.hasNext()) {
						EObject eObject = iterator.next();
						String text = EMFUtils.adapt(eObject, IItemLabelProvider.class).getText(eObject);
						stringBuilder.append(text);
						if(iterator.hasNext()) {
							stringBuilder.append(" ");
						}
						
						else {
							value = stringBuilder.toString();
						}
					}
			}

				/* Right here we need to find a value for the duration so the user input can be parsed
				 * so that it matches how the duration was saved.
				 */
			
			else if(element instanceof EActivity){
				if(activityDef != null){
					EStructuralFeature feature = null;

			        try {
				        feature = ADParameterUtils.getParameterFeature(element, fieldName);
			        } catch (UndefinedParameterException e) {
				        feature = null;
			        }

			        if(feature != null){
			    		EClassifier type = feature.getEType();

						if (type instanceof EDataType) {
							value = getDataTypeFeatureValue(element, fieldName, (EDataType) type);
						}
						else if (type instanceof ObjectDef) {
							if (secondaryFieldName != null) {
								try {
									EObject parameterObject = (EObject) ADParameterUtils.getParameterObject(element, fieldName);
									EStructuralFeature secondaryFeature = ADParameterUtils.getParameterFeature(parameterObject, secondaryFieldName);
									EClassifier secondaryType = secondaryFeature.getEType();
									if (secondaryType instanceof EDataType) {
										value = getDataTypeFeatureValue(parameterObject, secondaryFieldName, (EDataType) secondaryType);
									}
									else if (secondaryType instanceof ObjectDef) {
										value = getObjectDefFeatureValue(parameterObject, secondaryFieldName, secondaryFeature);
									}
									fieldName = fieldName + ":" + secondaryFieldName;
								} catch (UndefinedParameterException e) {
									value = null;
								}
							} else {
				 	 	 	 	value = getObjectDefFeatureValue(element, fieldName, feature);
							}
				 	 	}
			        }
				}					
			}
			else {
				if(value == null) {
					return;
				}
			}
		}

		if (value != null) {
			if (indexMode == Field.Index.ANALYZED) {
				// SPF-4457 Search "contains the words" does not find words separated by hyphens	
				// SPF-6003 Cannot find activity with forward slash in the name 
				// SPF-11124 Same as SPF-6003.
				String modifiedValue = value;
				modifiedValue = PlanSearcher.specialQuoting(modifiedValue);
				// don't index empty space...
				if (modifiedValue.trim().length() > 0) {
					doc.add(new Field(fieldName, modifiedValue, Field.Store.YES, Field.Index.ANALYZED));
				}
				// SPF-9231 -- Also index the full unanalyzed lower-cased value under an extended field name
				if (value.trim().length() > 0) {
					doc.add(new Field(fieldName + FIELDNAME_NOT_ANALYZED_SUFFIX, value.toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				}
			} else {
				// don't index empty space...
				if (value.trim().length() > 0) {
					doc.add(new Field(fieldName, value, Field.Store.YES, indexMode));
				}
			}
		}
	}
	
	private String getObjectDefFeatureValue(EObject object, String fieldName, EStructuralFeature feature) {
		IItemPropertySource source = EMFUtils.adapt(object, IItemPropertySource.class);
		IItemPropertyDescriptor pd = source.getPropertyDescriptor(object, feature);
		if (pd != null) {
			Object propertyValue = EMFUtils.getPropertyValue(pd, object);
			if (propertyValue != null && StringifierRegistry.hasRegisteredStringifier(fieldName)) {
				IStringifier<Object> stringifier = StringifierRegistry.getStringifier(fieldName);
				return stringifier.getDisplayString(propertyValue);
			}
		}
		return "";
	}

	private String getDataTypeFeatureValue(EPlanElement element, String fieldName, EDataType dataType) {
		//FIXME: check this code segment
		Class instanceClass = dataType.getInstanceClass();
		if(instanceClass != Map.class) {
			return ADParameterUtils.getParameterString(element, fieldName);
		} else {
			LogUtil.warn("can't get parameter string for " + fieldName);
		}
		return null;
	}
	
	private String getDataTypeFeatureValue(EObject object, String fieldName, EDataType dataType) {
		Class instanceClass = dataType.getInstanceClass();
		if(instanceClass != Map.class) {
			return ADParameterUtils.getObjectString(object, fieldName);
		} else {
			LogUtil.warn("can't get parameter string for " + fieldName);
		}
		return null;
	}
	
	/**
	 * Recursively indexes the attributes of a given EPlanElement and its
	 * children.
	 *
	 * @param elem element that has attributes and children that
	 * 			need to be indexed
	 */
	public void indexAttributesRec(EPlanElement elem){
		if(elem == null){
			return;
		}

		if(elem instanceof EPlan){
			indexAttributes(elem);
		}

		for(EPlanElement child : elem.getChildren()){
			indexAttributes(child);
			indexAttributesRec(child);
		}
	}

	/**
	 * Add an item to the vector of attributes that need to be indexed.
	 *
	 * @param fieldName name of attribute that needs to be indexed
	 */
	public void addAttribute(String fieldName){
		attributes.add(fieldName);
	}

	/**
	 * Remove an attribute to the vector of attributes that need to be indexed.
	 *
	 * @param fieldName name of attribute that does not need to be indexed
	 */
	public void removeAttribute(String fieldName){
		attributes.remove(fieldName);
	}

	/**
	 * Deletes all the entries in the attributes vector.
	 */
	public void clearAttributes(){
		attributes.clear();
	}

	/**
	 * Gets the directory that corresponds to the index.
	 *
	 * @return RAMDirectory for file that stores index
	 */
	public RAMDirectory getFileDirectory() {
		return dir;
	}

	/**
	 * Delete the corresponding document for elem and then re-insert a new
	 * document for elem, then refresh the index.
	 *
	 * @param elem EPlanElement that has been modified
	 */
	public void updateDoc(EPlanElement elem){
		String id = idRegistry.getUniqueId(elem);

		try {
			writer.deleteDocuments(new Term("id", id));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		indexAttributes(elem);
		refresh();
	}

	/**
	 * Get writer for this index
	 *
	 * @return IndexWriter for this index
	 */
//	public IndexWriter getWriter() {
//		return writer;
//	}

	/**
	 * Get list of attributes for this index
	 *
	 * @return IndexWriter for this index
	 */
	public Vector<String> getAttributes() {
		return attributes;
	}

	/**
	 * Indexes all the children of the given plan element recursively.
	 *
	 * @param elem plan element whose children need to be indexed
	 */
	public void indexChildrenAttributes(EPlanElement elem) {
		if(elem == null){
			return;
		}

		for(EPlanElement child : elem.getChildren()){
			indexAttributes(child);
			indexAttributesRec(child);
		}
	}

	/**
	 * Returns the vector of attributes that store boolean values.
	 *
	 * @return boolean attributes
	 */
	public Vector<String> getBooleanAttributes() {
		return booleanAttributes;
	}

	/**
	 * Adds an item to the boolean attributes vector.
	 *
	 * @param attribute fieldName that has a boolean value
	 */
	public void addBooleanAttribute(String attribute) {
		booleanAttributes.add(attribute);
	}
	
}
