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
import gov.nasa.ensemble.core.plan.PlanSearchConstants;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * This class is where we create a unique search input area for the user to
 * create a specific query depending on which attribute was selected in the
 * PlanSearchDialogQuery.
 * 
 * @author Alonzo Benavides
 * @bug need to add date search input, this means allowing a search on duration. 
 * 		In PlanIndexer, the indexAttributes method must be changed to allow
 * 		duration.
 */
public class PlanSearchInput {
	/* SWT objects for input area */
	public Composite inputComposite = null;
	private Combo options = null;
	private MBox searchText = null;

	/* Attribute which we are searching, and the query label depending on input */
	private String fieldName = "";
	private String queryLabel = "";
	private String displayName;

	/* The inputID defines what kind of input area is required */
	private int inputID;
	public static final int STRING = 0;
	public static final int BOOLEAN = 1;
	public static final int DATE = 2;
	public static final int MATCH = 3;

	/*
	 * The booleanID defines how this query will be appended to the overall
	 * BooleanQuery used in PlanSearcher when zearch is called.
	 */
	private int booleanID;
	private int defaultBooleanID = AND;
	public static final int AND = 0;
	public static final int OR = 1;
	public static final int NOT = 2;

	/* indexes for STRING_OPTIONS */
	public static final int CONTAIN_WORD = 0;
	public static final int CONTAIN_STRING = 1;
	public static final int NOT_CONTAIN_WORD = 2;
	public static final int NOT_CONTAIN_STRING = 3;

	/* indexes for BOOLEAN_OPTIONS */
	public static final int IS_TRUE = 0;
	public static final int NOT_IS_TRUE = 1;

	/* indexes for DATE_OPTIONS */
	public static final int IS_BETWEEN = 0;
	public static final int IS_BEFORE = 1;
	public static final int IS_AFTER = 2;

	/* indexes for MATCH_OPTIONS */
	public static final int IS = 0;
	public static final int NOT_IS = 1;

	/*
	 * The search options for each kind of inputID. These arrays populate the
	 * options combo depending on inputID.
	 */
	public static final String[] STRING_OPTIONS = { "contains word(s)",
			"contains the string of letters", "does not contain word(s)",
			"does not contain the string of letters" };
	public static final String[] BOOLEAN_OPTIONS = { "is true", "is false" };
	public static final String[] DATE_OPTIONS = { "is between", "is before",
			"is after" };
	public static final String[] MATCH_OPTIONS = { "is", "is not" };

	/**
	 * The entire search area has its own composite because we will want to
	 * update only the search area when the user decides to change which type of
	 * search they want for the specific line in question.
	 * 
	 * @param inputComposite
	 */
	public PlanSearchInput(Composite inputComposite, int inputID, String fieldName, String displayName) {
		this.fieldName = fieldName;
		this.displayName = displayName;
		this.inputComposite = inputComposite;
		this.inputID = inputID;
		booleanID = defaultBooleanID;
		createInputPattern();
	}

	/**
	 * This method will create the SWT objects necessary for the search input
	 * area. This is called in the constructor for PlanSearchInput.
	 */
	public void createInputPattern() {
		options = new Combo(inputComposite, SWT.FILL | SWT.READ_ONLY);

		switch (inputID) {
		case STRING:
			for (String tag : STRING_OPTIONS) {
				options.add(tag);
			}
			break;

		case BOOLEAN:
			for (String tag : BOOLEAN_OPTIONS) {
				options.add(tag);
			}
			break;

		case DATE:
			for (String tag : DATE_OPTIONS) {
				options.add(tag);
			}
			break;

		case MATCH:
			for (String tag : MATCH_OPTIONS) {
				options.add(tag);
			}
			break;
		}

		options.setFont(inputComposite.getFont());
		options.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true,
				1, 1));
		options.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateBooleanID();
				refresh();
			}
		});

		if (inputID == MATCH) {
			searchText = new MBox(inputComposite, true);
		} else {
			searchText = new MBox(inputComposite, false);
		}

		searchText.setFont(inputComposite.getFont());
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true,
				1, 1));

		if (inputID == BOOLEAN) {
			searchText.setVisible(false);
		}

		if (inputID == MATCH) {
			List<EActivityDef> defs = ActivityDictionary.getInstance()
					.getActivityDefs();
			if (defs != null) {
				if (fieldName.equals(PlanSearchPage.TYPE_STRING)) {
					outer: for (EActivityDef def : defs) {
						String name = def.getName();
						if (name != null) {
							for (String item : searchText.getItems()) {
								if (name.equals(item)) {
									continue outer;
								}
							}
							searchText.add(name);
						} else {
							LogUtil.warnOnce(def.getName()
									+ " has 'null' Name in the AD.");
						}
					}
				}
				if (fieldName.equals(PlanSearchPage.CATEGORY_STRING)) {
					outer: for (EActivityDef def : defs) {
						String cat = def.getCategory();
						if (cat != null) {
							for (String item : searchText.getItems()) {
								if (cat.equals(item)) {
									continue outer;
								}
							}
							searchText.add(cat);
						} else {
							LogUtil.warnOnce(def.getName()
									+ " has 'null' Category in the AD.");
						}
					}
				}
				if (fieldName.equals(PlanSearchPage.SCHEDULED_STRING)) {
					searchText.setText("true");
					searchText.setEditable(false);
				}
			}
		}

		options.select(inputID == STRING ? 1 : 0);
	}

	/**
	 * Updates the booleanID by checking the type of input and the selected
	 * option.
	 */
	public void updateBooleanID() {
		int index = options.getSelectionIndex();

		switch (inputID) {
		case STRING:
			switch (index) {
			case CONTAIN_WORD:
				booleanID = defaultBooleanID;
				break;
			case CONTAIN_STRING:
				booleanID = defaultBooleanID;
				break;
			case NOT_CONTAIN_WORD:
				booleanID = NOT;
				break;
			case NOT_CONTAIN_STRING:
				booleanID = NOT;
				break;
			}
			break;

		case BOOLEAN:
			booleanID = AND;
			break;

		case DATE:
			break;

		case MATCH:
			switch (index) {
			case IS:
				booleanID = defaultBooleanID;
				break;
			case NOT_IS:
				booleanID = NOT;
				break;
			}
			break;
		}
	}

	/**
	 * Constructs queries represented by this PlanSearchInput. Those queries are
	 * stored in the given PlanSearcher for future searching.
	 * 
	 * @param searcher
	 *            the PlanSearcher which needs to accumulate all queries
	 */
	public void addQueries(PlanSearcher searcher) {
		int index = options.getSelectionIndex();
		String searchString = searchText.getText();
		Query query = null;
		switch (inputID) {
		case STRING:
			String lowerCase = searchString.toLowerCase();
			switch (index) {
			case CONTAIN_WORD:
			case NOT_CONTAIN_WORD:
				StringTokenizer tokenizer = new StringTokenizer(lowerCase,
						PlanSearchConstants.DELIMITERS);
				while (tokenizer.hasMoreTokens()) {
					String text = tokenizer.nextToken();
					query = new TermQuery(new Term(fieldName, text));
					searcher.addQuery(query, booleanID);
				}
				return;
			case CONTAIN_STRING:
			case NOT_CONTAIN_STRING:
				query = new WildcardQuery(new Term(fieldName
						+ PlanIndexer.FIELDNAME_NOT_ANALYZED_SUFFIX, "*"
						+ lowerCase + "*"));
			}
			break;
		case BOOLEAN:
			switch (index) {
			case IS_TRUE:
				query = new TermQuery(new Term(fieldName, "true"));
				break;
			case NOT_IS_TRUE:
				query = new TermQuery(new Term(fieldName, "false"));
				break;
			}
			break;
		case MATCH:
			query = new TermQuery(new Term(fieldName, searchString));
			break;
		case DATE:
			// not yet handled?
			break;
		}
		searcher.addQuery(query, booleanID);
	}

	/**
	 * This method disposes of all the elements created for this search area.
	 */
	public void destroy() {
		options.dispose();
		searchText.dispose();
	}

	/**
	 * This method will return the string queryLabel that the user could see
	 * after conducting the search. The queryLabel will also be used as an ID in
	 * the search history section of the search view.
	 * 
	 * @return queryLabel for the search term
	 */
	public String getSearchLabel() {
		int index = options.getSelectionIndex();
		String featureName = (!CommonUtils.isNullOrEmpty(displayName) ? displayName : fieldName);
		if (inputID == BOOLEAN) {
			queryLabel = featureName + " " + options.getItem(index);
		} else {
			queryLabel = featureName + " " + options.getItem(index) + ": "
					+ searchText.getText();
		}
		return queryLabel;
	}

	/**
	 * This method clears all the necessary fields and drop downs. This will be
	 * called when the user attempts to "reset" the search criteria that may
	 * have been populated by the user or by previous search criteria.
	 */
	public void clear() {
		searchText.setText("");
		options.select(0);
		updateBooleanID();
		refresh();
	}

	/**
	 * This method refreshes the search input area composite.
	 */
	public void refresh() {
		if (inputComposite != null) {
			inputComposite.layout();
		}
	}

	/**
	 * Returns the inputID of the input area.
	 * 
	 * @return inputID
	 */
	public int getInputID() {
		return inputID;
	}

	/**
	 * Getter for the text in the searchText MBox.
	 * 
	 * @return the search input text
	 */
	public String getSearchInput() {
		return searchText.getText();
	}

	/**
	 * Getter for the field name for which the search input was created.
	 * 
	 * @return field name which represents selected attribute
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Getter for options combo selection index
	 * 
	 * @return options selection index
	 */
	public int getOptionID() {
		return options.getSelectionIndex();
	}

	/**
	 * Returns the booleanID for the input.
	 * 
	 * @return booleanID
	 */
	public int getBooleanID() {
		return booleanID;
	}

	/**
	 * Sets a new inputID. All SWT objects of the input area are destroyed and a
	 * new input area is created based on the new inputID.
	 * 
	 * @param inputID
	 *            the type of search input
	 */
	public void setInputID(int inputID) {
		this.inputID = inputID;
		destroy();
		createInputPattern();
		refresh();
	}

	/**
	 * Sets the new index for the options combo.
	 * 
	 * @param optionID
	 *            new option index
	 */
	public void setOptionID(int optionID) {
		options.select(optionID);
		updateBooleanID();
		refresh();
	}

	/**
	 * Sets the text for the user input area, the MBox. This is used when the
	 * previous search criteria is loaded in PlanSearchPage.
	 * 
	 * @param text
	 *            textual user input
	 */
	public void setInputText(String text) {
		searchText.setText(text);
	}

	/**
	 * Sets the default booleanID which describes whether or not the queries
	 * created by the input is appended with an && or an ||.
	 * 
	 * @param newBooleanID
	 *            the new default booleanID for queries
	 */
	public void setDefaultBooleanID(int newBooleanID) {
		defaultBooleanID = newBooleanID;
		updateBooleanID();
	}
}
