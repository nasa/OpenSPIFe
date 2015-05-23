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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.ObjectDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.operations.OperationHistoryActionHandler;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * This class will construct the dialog page that will be opened
 * upon the user selecting the Open Plan Search feature. 
 * 
 * @author Alonzo Benavides
 * @bug Page does not resize correctly upon first creation.
 */
public class PlanSearchPage extends DialogPage implements ISearchPage {
	public static final Image IMAGE_ADD = AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "/icons/add_search.gif").createImage();
	
	/* for saving previous search information */
	private static final String DIALOG_SETTING_SECTION_NAME = "OpenPlanSearchDialog"; 
	private static final String DIALOG_INPUT = "OpenPlanSearchDialog.Input"; 
	
	/* labels */
	public static final String CATEGORY_STRING = "Category";
	public static final String TYPE_STRING = "Type";
	public static final String NAME_STRING = "Name";
	public static final String SCHEDULED_STRING = "Scheduled";
	public static final String FILLER = "----------";
	public static final String MATCH_ALL = "Match All Queries";
	public static final String MATCH_ANY = "Match Any Query";
	
	/* SWT objects for search page */
	private Composite pageComposite = null;
	private Combo defaultBooleanOption = null;
	private Button previousQuery = null;
	private Button clear = null;
	private Composite addComposite = null;
	private Button addQuery = null;
	
	/* Indexing back end objects */
	private PlanIndexer indexer = null;
	private PlanSearcher searcher = null;
	private Vector<String> indexOn = null;
	// store nice display name versions of the attributes
	private WeakHashMap<String, String> nameToDisplayNameMap = null;
	private ArrayList<PlanSearchDialogQuery> searchQueries = new ArrayList<PlanSearchDialogQuery>(); 
	
	/* Indexing synchronization */
	private CountDownLatch indexingCompletedLatch = new CountDownLatch(1);
	private long maximumIndexingWaitTime = 3;

	/**
	 * Constructors for PlanSearchPage dialog
	 */
	public PlanSearchPage(){
		super();
		createIndex();
	}
	
	public PlanSearchPage(String title) {
		super(title);
		createIndex();
	}

	public PlanSearchPage(String title, ImageDescriptor image) {
		super(title, image);
		createIndex();
	}
	
	/**
	 * Creates PlanIndexer and PlanSearcher. In addition, the PlanIndexer is
	 * used to create the index file containing all data that may be searched.
	 */
	public void createIndex(){ 
		indexer = new PlanIndexer();
		indexOn = new Vector<String>();
		ArrayList<String> tempList = new ArrayList<String>(); // this list for intermediate sorting
		nameToDisplayNameMap = new WeakHashMap<String, String>();
		
		// these are items we don't want showing up in the drop down list
		List<String> itemsToIgnore = new ArrayList<String>();
		
		/* add most common search types */
		tempList.add(NAME_STRING);
		tempList.add(TYPE_STRING);
		tempList.add(CATEGORY_STRING);
		tempList.add(SCHEDULED_STRING);
		Collections.sort(tempList);
		indexOn.addAll(tempList);
		tempList.clear();
		indexer.addBooleanAttribute(SCHEDULED_STRING);
		indexOn.add(FILLER);
		
		for (IPlanSearchProvider provider : PlanSearchProviderRegistry.getInstance().getProviders()) {
			String featureName = provider.getFeatureName();
			String displayName = provider.getDisplayName();
			tempList.add(featureName);
			nameToDisplayNameMap.put(featureName, displayName);
		}
		
		/* add activity attributes */
		List<EParameterDef> params = ActivityDictionary.getInstance().getAttributeDefs();
		ParameterDescriptor parameterDescriptor = ParameterDescriptor.getInstance();
		if(params != null){
			for(EParameterDef def : params){
				String name = def.getName();
				
				EAnnotation eAnnotation = def.getEAnnotation("descriptor");
				if(eAnnotation != null) {
					EMap<String, String> details = eAnnotation.getDetails();
					String visibility = details.get("visible");
					if(visibility != null) {
						if(!Boolean.valueOf(visibility)) {
							itemsToIgnore.add(name);
							continue;
						}
					}
				}
				
				/* this is just bc this type of search is not implemented yet */
				if(name.equals("duration")) {
					continue;
				}
				
				tempList.add(name);
				
				String displayName = parameterDescriptor.getDisplayName(def);
				nameToDisplayNameMap.put(name, displayName);
				
				EClassifier type = def.getEType();
	 	 	 	if (!def.isMany() && type instanceof ObjectDef) {
		 	 	 	for (EStructuralFeature feature : ((ObjectDef) type).getEStructuralFeatures()) {
			 	 	 	if (ParameterDescriptor.getInstance().isVisible(feature)) {
			 	 	 		String featureDisplayName = parameterDescriptor.getDisplayName(feature);
			 	 	 		featureDisplayName = displayName + ": " + featureDisplayName;
			 	 	 		String secondaryFeature = name+":"+feature.getName();
			 	 	 		nameToDisplayNameMap.put(secondaryFeature, featureDisplayName);
							tempList.add(secondaryFeature);
			 	 	 	}
		 	 	 	}
	 	 	 	}
	 	 	 	
			}
		}
		Collections.sort(tempList);
		indexOn.addAll(tempList);
		tempList.clear();
		if(tempList.size() > 0) {
			indexOn.add(FILLER);
		}
		
		/* add activity parameters */
		List<EActivityDef> defs = ActivityDictionary.getInstance().getActivityDefs();
		
		for(EActivityDef def : defs){
			List<EStructuralFeature> features = def.getEAllStructuralFeatures();
			
			for(EStructuralFeature feature : features){
				String name = feature.getName();
				String defaultValue = feature.getDefaultValueLiteral();
						
				if(name.equals("duration")){
					continue;
				}
				
				if(defaultValue != null && (defaultValue.equals("false") || defaultValue.equals("true"))){
					if(!indexer.getBooleanAttributes().contains(name) && !itemsToIgnore.contains(name)){
						indexer.addBooleanAttribute(name);
						tempList.add(name);
					}
				}
				else{
					if(!indexOn.contains(name) && !tempList.contains(name) && !itemsToIgnore.contains(name)){
						tempList.add(name);
					}					
				}
				String displayName = parameterDescriptor.getDisplayName(feature);
				nameToDisplayNameMap.put(name, displayName);
			}
		}
		
		Collections.sort(tempList);
		indexOn.addAll(tempList);
		
		/* start indexing thread */
		Job indexIt = new Job("Indexing Plan") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for(String item : indexOn){
					indexer.addAttribute(item);
				}
		
				try {		
					for (IWorkbenchWindow w : PlatformUI.getWorkbench().getWorkbenchWindows()) {
						for (IWorkbenchPage p : w.getPages()) {
							boolean indexedAtLeastOnePlan = false;
							for (IEditorPart part : p.getEditors()) {
								EPlan plan = CommonUtils.getAdapter(part, EPlan.class);								
								if (plan != null) {
									indexer.indexChildrenAttributes(plan);
									indexedAtLeastOnePlan = true;
								}
							}							
							if(!indexedAtLeastOnePlan) {
								return Status.CANCEL_STATUS;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				indexer.refresh();
		
				indexingCompletedLatch.countDown();
				return Status.OK_STATUS;
			}
		};
		indexIt.schedule();

		searcher = new PlanSearcher(indexer.getFileDirectory());
	}
    
    /**
     * This is run when the "search" button is pressed.
     */
	@Override
	public boolean performAction() {	
		saveWidgetValues();
	
		if(searchQueries.size() == 0){
			return false;
		}
		
		searcher.clearQueries();
		
		if(hasAllMustNotQueries()){
			searcher.addQuery(PlanSearcher.DEFAULT_QUERY); 
		}
		
		for(PlanSearchDialogQuery query : searchQueries){
			query.getPlanSearchInput().setDefaultBooleanID(defaultBooleanOption.getSelectionIndex());
			query.getPlanSearchInput().addQueries(searcher);
		}
		
		try{
			ISearchResultViewPart view = NewSearchUI.activateSearchResultView();
			IActionBars actionBars = view.getViewSite().getActionBars();
			OperationHistoryActionHandler undoHandler = (OperationHistoryActionHandler)actionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
			PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
			undoHandler.setContext(IOperationHistory.GLOBAL_UNDO_CONTEXT);
			OperationHistoryActionHandler redoHandler = (OperationHistoryActionHandler)actionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
			redoHandler.setContext(IOperationHistory.GLOBAL_UNDO_CONTEXT);
		
			String searchLabel = "";
			for(int i = 0; i < searchQueries.size(); i++){
				searchLabel += searchQueries.get(i).getPlanSearchInput().getSearchLabel();
			
				if(i < searchQueries.size() - 1){
					searchLabel += ",\n";
				}
			}
			// don't run query until the indexer is finished. (stop waiting after 3 seconds).
			indexingCompletedLatch.await(maximumIndexingWaitTime, TimeUnit.SECONDS);
			NewSearchUI.runQueryInBackground(new PlanSearchQuery(searchLabel, searcher, indexer));
		}
		catch (Exception e){
			LogUtil.error(e.getMessage());
			System.out.println("Exception in performing search action: " + e.getMessage());
			return false;
		}
	
		return true;
	}
	
	/**
	 * Creates the initial set-up of the dialog page and stores the
	 * composite for this page. 
	 */
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		parent.setLayout(new GridLayout(1, false));
		pageComposite = parent;
		
		Composite result = new Composite(parent, SWT.NONE);
		result.setFont(parent.getFont());
		GridLayout layout= new GridLayout(4, false);
		result.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		result.setLayout(layout);
		
		addTextPatternControls(result);
	
		setControl(pageComposite);
		Dialog.applyDialogFont(pageComposite);
		
		addSearchPlanQuery();
	}
	
	/**
	 * Adds the SWT objects that will be displayed in the page: 
	 * defaultBooleanOption: 	Creates the option for Match All Queries and Match Any Query.
	 * 							This is the difference between a boolean query of && or ||.
	 * previousQuery: 		 	Re-creates the last query that was done to completion.
	 * clear:					Resets all default options and leaves a single query.
	 * 
	 * @param group the composite for the entire PlanSearchDialogQuery
	 */
	private void addTextPatternControls(Composite group) {	
		defaultBooleanOption = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		defaultBooleanOption.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));
		defaultBooleanOption.setFont(group.getFont());
		defaultBooleanOption.add(MATCH_ALL);
		defaultBooleanOption.add(MATCH_ANY);
		defaultBooleanOption.select(0);	
		
		previousQuery = new Button(group, SWT.PUSH);
		previousQuery.setText("Previous Query");
		previousQuery.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		previousQuery.setFont(group.getFont());
		previousQuery.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}

			@Override
			public void widgetSelected(SelectionEvent e) {		
				for(int i = searchQueries.size(); i >= 0; i--){
					deleteSearchPlanQuery(searchQueries.get(0).getComposite());
				}
				restoreWidgetValues();
				refresh();
			}
		});
		
		clear = new Button(group, SWT.PUSH);
		clear.setText("Clear All");
		clear.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		clear.setFont(group.getFont());
		clear.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}

			@Override
			public void widgetSelected(SelectionEvent e) {	
				defaultBooleanOption.select(PlanSearchInput.AND);
				
				for(int i = searchQueries.size(); i >= 0; i--){
					deleteSearchPlanQuery(searchQueries.get(0).getComposite());
				}

				pageComposite.getShell().setSize(600, 360);
				pageComposite.layout();
			}
		});
	}
	
	/**
	 * Instantiates the addComposite and create the button to add more single
	 * line queries.
	 */
	public void createAddButton(){
		addComposite = new Composite(pageComposite, SWT.NONE);
		addComposite.setFont(pageComposite.getFont());
		GridLayout layout = new GridLayout(1, false);
		addComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
		addComposite.setLayout(layout);
		
		addQuery = new Button(addComposite, SWT.PUSH);
		GridData data = new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1);
		addQuery.setLayoutData(data);
		addQuery.setFont(pageComposite.getFont());
		addQuery.setImage(IMAGE_ADD);
		addQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addSearchPlanQuery();
				refresh();
			}
		});
	}
	
	/**
	 * Disposes of the add button and its composite so that there is
	 * no empty space in the search page.
	 */
	public void destroyAddButton(){
		if(addQuery != null){
			addComposite.dispose();
			addQuery.dispose();		
		}
	}
	
	/**
	 * Creates a new PlanSearchDialogQuery, which adds a new line query to 
	 * the search page.
	 */
	public void addSearchPlanQuery(){
		destroyAddButton();
		Composite child = new Composite(pageComposite, SWT.NONE);
		child.setFont(pageComposite.getFont());
		GridLayout layout= new GridLayout(4, false);
		child.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		child.setLayout(layout);
		
		// generate a list of items to be display in the drop down
		ArrayList<String> list = new ArrayList<String>();
		for(String string : indexOn) {
			String displayName = nameToDisplayNameMap.get(string);
			if(displayName == null) {
				displayName = string;
			}
			
			list.add(displayName);
		}
		searchQueries.add(new PlanSearchDialogQuery(child, this, list, nameToDisplayNameMap));
		createAddButton();
	}
	
	/**
	 * Deletes the entire PlanSearchDialogQuery that matches the
	 * composite that has been passed to this method. 
	 * 
	 * @param searchInputComposite composite of the query that must be deleted
	 */
	public void deleteSearchPlanQuery(Composite searchInputComposite){
		if(searchQueries.size() == 1){
			searchQueries.get(0).getPlanSearchInput().clear();
			searchQueries.get(0).setAttributeID(0);
			return;
		}
		
		destroyAddButton();
		for(PlanSearchDialogQuery query : searchQueries){
			if((query.getComposite()).equals(searchInputComposite)){
				query.disposeAll();
				searchQueries.remove(query);
				break;
			}
		}
		createAddButton();
	}
	
	/**
	 * Since Lucene is unable to conduct a single must_not query (negation), we
	 * must check to see if the user has chosen all negation type queries and if so
	 * we must append a must (and) or a should (or) query which will return all
	 * available activities.
	 * 
	 * @return whether all the search queries are negations
	 */
	public boolean hasAllMustNotQueries(){
		for(PlanSearchDialogQuery query : searchQueries){
			if(query.getPlanSearchInput().getBooleanID() == PlanSearchInput.AND
					|| query.getPlanSearchInput().getBooleanID() == PlanSearchInput.OR){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Resizes the dialog box so that all the queries can be clearly 
	 * viewed and modified.
	 */
	public void refresh(){
		if(searchQueries.size() > 1){
			pageComposite.getShell().setSize(650, 230 + 65*searchQueries.size());
		}

		pageComposite.layout();
	}

	/**
	 * Getter method for the PlanIndexer used to create the index file for
	 * the plans.
	 * 
	 * @return the PlanIndexer used to index the plan
	 */
	public PlanIndexer getIndexer(){
		return indexer;
	}
	
	/**
	 * ISearchPage mandatory method.
	 */
	@Override
	public void setContainer(ISearchPageContainer container) {
		// do nothing
	}
	
	/*
	 * --------------------------------------------------------------------------------------------
	 * Methods for saving and retrieving stored search information.
	 * --------------------------------------------------------------------------------------------
	 */
	
	/**
     * Returns the IDialogSettings used to store the values of the previous search
     */
	@SuppressWarnings("restriction")
	protected IDialogSettings getDialogSettings() {
        IDialogSettings workbenchSettings = org.eclipse.ui.internal.WorkbenchPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection(DIALOG_SETTING_SECTION_NAME);
        if (section == null) {
			section = workbenchSettings.addNewSection(DIALOG_SETTING_SECTION_NAME);
		}
        return section;
    }
    
    /**
     * Use the dialog store to restore widget values to the values that they
     * held last time this dialog was used to completion.
     */
    protected void restoreWidgetValues() {
        IDialogSettings settings = getDialogSettings();
        if(settings == null){
        	return;
        }
   
        String[] vals = settings.getArray(DIALOG_INPUT);
        
        if(vals != null){
        	for(int i = 0; i < vals.length; i++){
        		
        		// create an array of strings in the following format: "searchID;inputID;optionID;input"
        		String[] elements = vals[i].split(";");
        		if(i > 0){
        			addSearchPlanQuery();
        		}

        		defaultBooleanOption.select(Integer.parseInt(elements[0]));
        		searchQueries.get(i).setAttributeID(Integer.parseInt(elements[1]));
        		searchQueries.get(i).getPlanSearchInput().setInputID(Integer.parseInt(elements[2]));
        		searchQueries.get(i).getPlanSearchInput().setOptionID(Integer.parseInt(elements[3]));
        		
        		if(elements.length == 5){
        			searchQueries.get(i).getPlanSearchInput().setInputText(elements[4]);
        		}
        	}
        }
        else{
        	addSearchPlanQuery();
        }
    }
    
    /**
     * Once a search is done to completion, write widget values to the dialog store so that
     * they will persist into the next invocation of this dialog page.
     */
    protected void saveWidgetValues() {
        IDialogSettings settings = getDialogSettings();
        if(settings == null){
        	return;
        }
        
        // create an array of strings in the following format: "defaultBooleanID;searchID;inputID;optionID;input"
        String[] queryArray = new String[searchQueries.size()];
        for(int i = 0; i < queryArray.length; i++){
        	queryArray[i] = "" + defaultBooleanOption.getSelectionIndex() + ";" + searchQueries.get(i).getAttributeID() +";" 
        					+ searchQueries.get(i).getPlanSearchInput().getInputID() + ";"
        					+ searchQueries.get(i).getPlanSearchInput().getOptionID() + ";"
        					+ searchQueries.get(i).getPlanSearchInput().getSearchInput();
        }
        
        settings.put(DIALOG_INPUT, queryArray);
    }
}
