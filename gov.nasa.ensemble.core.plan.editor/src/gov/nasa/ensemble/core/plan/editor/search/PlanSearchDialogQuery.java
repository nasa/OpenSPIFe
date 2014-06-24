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

import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Objects of this class represent each single line query that is displayed by
 * the PlanSearchPage. The design is as follows:
 * [Combo of attributes] [PlanSearchInput based on attribute] [delete query button]
 * 
 * @author Alonzo Benavides
 * @bug No known bugs
 */
public class PlanSearchDialogQuery {
	public static final Image IMAGE_DELETE = AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "/icons/trash_search.gif").createImage();
	
	/* SWT objects */
	private PlanSearchPage searchPage = null;
	private Composite queryComposite = null;	
	private PlanSearchInput input = null;
	private Combo attributesCombo = null;	
	private Button deleteQuery = null;
	// map to store displayName used in combo box to attriute name mappings
	private Map<String, String> displayNameToNameMap = null;
	
	/**
	 * Constructor for PlanSearchDialogQuery
	 * 
	 * @param queryComposite composite for query space
	 * @param searchPage the search dialog page which contains this query
	 * @param attributes list of attributes that may be searched
	 */
	public PlanSearchDialogQuery(Composite queryComposite, PlanSearchPage searchPage, List attributes, Map<String, String> nameToDisplayNameMap){
		this.queryComposite = queryComposite;
		this.searchPage = searchPage;
		this.displayNameToNameMap = getInverseMap(nameToDisplayNameMap);

		createAttributesCombo(attributes);
		
		Composite inputComposite = new Composite(queryComposite, SWT.NONE);
		inputComposite.setFont(queryComposite.getFont());
		GridLayout layout = new GridLayout(2, false);
		inputComposite.setLayout(layout);
		inputComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		
		String item = attributesCombo.getItem(attributesCombo.getSelectionIndex());
		String attributeName = displayNameToNameMap.get(item);
		if(attributeName == null) {
			attributeName = item;
		}
		
		input = new PlanSearchInput(inputComposite, PlanSearchInput.STRING, attributeName, item);
		input.refresh();
		
		createDeleteButton();
	}
	
	private Map<String, String> getInverseMap(Map<String, String> nameToDisplayNameMap) {
		Map<String, String> displayNameToNameMap = new WeakHashMap<String, String>();
		Set<String> keySet = nameToDisplayNameMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			String value = nameToDisplayNameMap.get(key);
			String newKey = value;
			String newValue = key;
			displayNameToNameMap.put(newKey, newValue);
		}
		return displayNameToNameMap;
	}

	/**
	 * Creates the drop down of attributes which the user can search on.
	 */
	public void createAttributesCombo(List attributes){		
		attributesCombo = new Combo(queryComposite, SWT.READ_ONLY);
	
		for(Object attr : attributes){			
			attributesCombo.add(attr.toString());
		}
		
		attributesCombo.setFont(queryComposite.getFont());
		attributesCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1));			
		attributesCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateSearchSelection();
			}
		});	
		
		attributesCombo.select(1);
	}
	
	/**
	 * Creates the delete button for each line query.
	 */
	public void createDeleteButton(){
		GridData data = new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1);
		
		deleteQuery = new Button(queryComposite, SWT.PUSH);
		deleteQuery.setImage(IMAGE_DELETE);
		deleteQuery.setLayoutData(data);
		deleteQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchPage.deleteSearchPlanQuery(queryComposite);
				searchPage.refresh();
			}
		});
	}
	
	/**
	 * This is the method called when the attributes combo listener detects
	 * a change in the list. This should update the query to reflect
	 * the type of search the user would like to execute. This will include
	 * changing the the PlanSearchInput.
	 */
	public void updateSearchSelection(){
		if(input == null){
			return;
		}
		
		int attributeID = attributesCombo.getSelectionIndex();

		// conver the displayName to the attribute, using the map
		String displayName = attributesCombo.getItems()[attributeID];
		String attribute = displayNameToNameMap.get(displayName);
		if(attribute == null) {
			attribute = displayName;
		}
		
		input.destroy();
		
		if(attribute.equals(PlanSearchPage.FILLER)){
			attributesCombo.select(0);
		}
		
		if(attribute.equals(PlanSearchPage.CATEGORY_STRING) || attribute.equals(PlanSearchPage.TYPE_STRING)){
			input = new PlanSearchInput(input.inputComposite, PlanSearchInput.MATCH, attribute, displayName);
		}
		else if(searchPage.getIndexer().getBooleanAttributes().contains(attribute)){
			input = new PlanSearchInput(input.inputComposite, PlanSearchInput.BOOLEAN, attribute, displayName);
		}
		else{
			input = new PlanSearchInput(input.inputComposite, PlanSearchInput.STRING, attribute, displayName);	
		}
		input.refresh();
	}
	
	/**
	 * Dispose of everything.
	 */
	public void disposeAll(){
		input.destroy();
		attributesCombo.dispose();
		queryComposite.dispose();
		deleteQuery.dispose();
		searchPage.dispose();
	}
	
	/**
	 * Returns the input area for this query.
	 * 
	 * @return input
	 */
	public PlanSearchInput getPlanSearchInput(){
		return input;
	}	
	
	/**
	 * Returns the composite representing this entire line in the dialog.
	 * 
	 * @return queryComposite
	 */
	public Composite getComposite(){
		return queryComposite;
	}
	
	/**
	 * Returns index of attributes combo.
	 * 
	 * @return attributeID the attribute index from attributes combo
	 */
	public int getAttributeID() {
		int selectionIndex = attributesCombo.getSelectionIndex();
		return selectionIndex;
	}

	/**
	 * Changes the attributes combo selection to a new index, and the
	 * search input is changed accordingly.
	 * 
	 * @param attributeID
	 */
	public void setAttributeID(int attributeID) {
		attributesCombo.select(attributeID);
		updateSearchSelection();
	}
}
