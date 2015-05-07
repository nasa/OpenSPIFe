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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.plan.PlanSearchConstants;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.RAMDirectory;

/**
 * Searching abstraction which use lucene IndexSearcher and IndexReader
 * to conduct index based search, which we will refer to as zearch.
 * 
 * @author Alonzo Benavides
 * @bug no known bugs
 */
public class PlanSearcher {
	/* vector for results found by zearch */
	private Vector<Integer> results = null;
	
	/* reader for index file */
	private IndexReader reader = null;
	
	/* searcher to parse output from reader */
	private IndexSearcher searcher = null;

	/* vector for multiple queries */
	private Vector<Query> queries = null;
	
	/* array for what kind logical operation to append with a query 
	 * the possible values for this array are as follows:
	 * PlanSearchInput.AND = 0 
	 * PlanSearchInput.OR  = 1
	 * PlanSearchInput.NOT = 2 */
	private int[] booleanOps = null;
	
	/* query that will return all items in the index */
	public static final Query DEFAULT_QUERY = new TermQuery(new Term("exist", "true"));
	
	/**
	 * Constructor for PlanSearcher.
	 * 
	 * @param dir the directory for the index location
	 */
	public PlanSearcher(RAMDirectory dir){
		results = new Vector<Integer>();
		queries = new Vector<Query>();
		booleanOps = new int[32];
		
		/* there should not be more than 32 queries... */
		for(int i = 0; i < 32; i++){
			booleanOps[i] = 0;
		}
		
		try{
			reader = IndexReader.open(dir);
			searcher = new IndexSearcher(reader);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		/* just in case the wildcard queries get too large */
		BooleanQuery.setMaxClauseCount(8192);
	}
	
	/**
	 * Search function to populate results vector based on vector of queries.
	 * 
	 * @return results from search
	 */
	public Vector<Integer> zearch(){
		clearResults();
		long t0 = System.currentTimeMillis();
		if(queries == null || queries.size() == 0){
			int max = 0;
			
//			try {
				max = searcher.maxDoc();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			for(int i = 0; i < max; i++){
				results.add(new Integer(i));
			}
			
			return results;
		}
		
		BooleanQuery bQuery = new BooleanQuery();
		int size = queries.size();
		
		for(int i = 0; i < size; i++){
			switch(booleanOps[i]){
				case PlanSearchInput.AND: 	bQuery.add(queries.get(i), BooleanClause.Occur.MUST);
											break;
				case PlanSearchInput.OR: 	bQuery.add(queries.get(i), BooleanClause.Occur.SHOULD);
											break;
				case PlanSearchInput.NOT: 	bQuery.add(queries.get(i), BooleanClause.Occur.MUST_NOT);
											break;
			}
		}
		
		try{
			searcher.search(bQuery, new Collector() {
				@Override
				public void setScorer(Scorer scorer) throws IOException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void collect(int doc) throws IOException {
					results.add(new Integer(doc));
				}

				@Override
				public void setNextReader(IndexReader reader, int docBase)
						throws IOException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean acceptsDocsOutOfOrder() {
					// TODO Auto-generated method stub
					return false;
				}
			});
			
		} catch(Exception e){
			System.out.println("bad search: " + e.getMessage());
		}
		
		long t1 = System.currentTimeMillis();
		
		LogUtil.debug("search for " + bQuery + " took: "
				+ String.valueOf((double)(t1-t0) / 1000) + " seconds");
		
		return results;
	}
	
	/**
	 * Search function to populate results vector based on a single query.
	 * This method will clear any previously stored results and queries.
	 * 
	 * @return results from single query search
	 */
	public Vector<Integer> zearch(Query query){
		clearResults();
		queries.clear();
		
		if(query == null){
			return null;
		}
			
		try{
			searcher.search(query, new Collector() {

				@Override
				public void setScorer(Scorer scorer) throws IOException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void collect(int doc) throws IOException {
					results.add(new Integer(doc));
				}

				@Override
				public void setNextReader(IndexReader reader, int docBase)
						throws IOException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean acceptsDocsOutOfOrder() {
					// TODO Auto-generated method stub
					return false;
				}
				
			});
			
		} catch(Exception e){
			System.out.println("bad search: " + e.getMessage());
		}
		
		return results;
	}
	
	/**
	 * Refresh the IndexReader so that the IndexSearcher will be aware of any new changes.
	 */
	public void refresh(){
		IndexReader tempReader = null;
		
		try {
			tempReader = searcher.getIndexReader().reopen();
			
			if(tempReader.equals(reader)){
				return;
			}
			
			searcher.close();
			reader.close();
			reader = tempReader;
			searcher = new IndexSearcher(reader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method opens a new IndexReader and creates a new IndexSearcher for
	 * the passed directory. This should be used when we want to use a new Index.
	 * 
	 * @param dir new RAMDirectory for new index location
	 */
	public void openDir(RAMDirectory dir){
		results.clear();
		
		try{
			reader = IndexReader.open(dir);
			searcher = new IndexSearcher(reader);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method adds a query that has already been created
	 * 
	 * @param query query to be added to list
	 */
	public void addQuery(Query query){
		if(query == null){
			return;
		}
		
		queries.add(query);
	}
	
	/**
	 * Adds a specific query along with the type of boolean operation
	 * to be used on that query.
	 * 
	 * @param query query to be added
	 * @param booleanID type of boolean operation associated with query
	 */
	public void addQuery(Query query, int booleanID) {
		if(query == null){
			return;
		}
		
		booleanOps[queries.size()] = booleanID;
		
		queries.add(query);
	}
	
	/**
	 * Given a search input and a field name to search against, we can
	 * create a query and add it to our vector of queries.
	 * 
	 * @param searchInput
	 * @param fieldName
	 */
	public void addQuery(String fieldName, String searchInput){
		if(searchInput.equals("")){
			return;
		}
		
		searchInput = searchInput.toLowerCase();
		StringTokenizer tokenizer = new StringTokenizer(searchInput, PlanSearchConstants.DELIMITERS);	
		
		boolean first = true;
		while(tokenizer.hasMoreTokens()){
			String term = tokenizer.nextToken();
			if (first) {
				term = "*"+term;
				first = false;
			}
			if (!tokenizer.hasMoreTokens()) {
				term += "*";
			}
			Query query = new WildcardQuery(new Term(fieldName, specialQuoting(term)));	
			queries.add(query);
		}
	}
	
	/**
	 * Given a search input and a field name to search against, we can
	 * create a query and add it to our vector of queries.
	 * Finds plan elements with words containing all these substrings.
	 * @param literalValue Literal value, not Lucene syntax.  Parsed into words.
	 * @param fieldName
	 * @see PlanSearchConstants.DELIMITERS
	 */
	public void addQueryForWordsContaining(String fieldName, String literalValue) {
		if(literalValue.equals("")){
			return;
		}
		
		literalValue = literalValue.toLowerCase();
		StringTokenizer tokenizer = new StringTokenizer(literalValue, PlanSearchConstants.DELIMITERS);	
		
		while(tokenizer.hasMoreTokens()){
			String term = tokenizer.nextToken();
			term = "*" + PlanSearcher.specialQuoting(term) + "*";

			Query query = new WildcardQuery(new Term(fieldName, term));	
			queries.add(query);
		}
	}
	
	public void addQuery(String fieldName, String searchInput, String prefix
			, String suffix){
		if(searchInput.equals("")){
			return;
		}
		
		searchInput = searchInput.toLowerCase();
		StringTokenizer tokenizer = new StringTokenizer(searchInput, PlanSearchConstants.DELIMITERS);	
		
		boolean first = true;
		while(tokenizer.hasMoreTokens()){
			String term = tokenizer.nextToken();
			if (first) {
				term = prefix+term;
				first = false;
			}
			if (!tokenizer.hasMoreTokens()) {
				term += suffix;
			}
			Query query = new WildcardQuery(new Term(fieldName, term));	
			queries.add(query);
		}
	}	
	
	/**
	 * Getter method for ids of results
	 * 
	 * @return latest results found by zearch
	 */
	public Vector<String> getResultIDs() {
		Vector<String> ids = new Vector<String>();
		
		try{
			for(Integer hit : results){
				ids.add(searcher.doc(hit.intValue()).get("id"));
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return ids;
	}
	
	/**
	 * Getter method for results vector
	 * 
	 * @return latest results found by zearch
	 */
	public Vector<Integer> getResults() {
		return results;
	}
	
	/**
	 * Get IndexSearcher
	 * 
	 * @return IndexSearcher for this index
	 */
	public IndexSearcher getSearcher() {
		return searcher;
	}
	
	/**
	 * Clear the queries vector.
	 */
	public void clearQueries(){
		queries.clear();
	}
	
	/**
	 * Clear the queries vector.
	 */
	public void clearResults(){
		results.clear();
	}
	
	/** FIXME:  This is the only way I can get special characters like slash to work as
	 * a non-delimiter: to change it to something else both when indexing and searching. */
	public static String specialQuoting(String searchString) {
		String modifiedValue = searchString;
		char placeholder = '\u0900';
		for (String special : PlanSearchConstants.LUCENE_SPECIAL_CHARACTERS) {
			placeholder++;
			modifiedValue = modifiedValue.replace(special.charAt(0), placeholder);
		}
		return modifiedValue;
	}
	
}
