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
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class PlanSearchQuery implements ISearchQuery{
	private PlanSearchResult searchResult = null;
	private PlanSearcher searcher = null;
	private PlanIndexer indexer = null;
	private boolean hasRun = false;
	
	public PlanSearchQuery(String searchLabel, PlanSearcher searcher, PlanIndexer indexer){
		this.searcher = searcher;
		this.indexer = indexer;
		searchResult = new PlanSearchResult(this, searchLabel);
	}
	
	@Override
	public boolean canRerun() {
		return true;
	}

	@Override
	public boolean canRunInBackground() {
		return true;
	}

	@Override
	public String getLabel() {
		return searchResult.getSearchLabel();
	}

	@Override
	public ISearchResult getSearchResult() {
		return searchResult;
	}

	@Override
	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
		String label = getLabel();
		
		monitor.beginTask("Searching for " + "\"" + label + "\"...", IProgressMonitor.UNKNOWN);
		
		if(hasRun){
			searchResult.clear();
			searchResult.removeAll();	
			update();
		}
		else{
			searcher.refresh();
		}

		searcher.zearch();
							
		List<String> resultIDs = searcher.getResultIDs();
							
		for (String resultID : resultIDs) {
			searchResult.addAll(indexer.idRegistry.getIdentifiable(EPlanElement.class, resultID));
		}			
		
		monitor.done();
		hasRun = true;
		return Status.OK_STATUS;
	}
	
	public void update(){ 
		indexer.close();
		indexer.clear();

		/* index attributes of all elements in plan */
		try {		
			for (IWorkbenchWindow w : PlatformUI.getWorkbench().getWorkbenchWindows()) {
				for (IWorkbenchPage p : w.getPages()) {
					for (IEditorPart part : p.getEditors()) {
						EPlan plan = CommonUtils.getAdapter(part, EPlan.class);
								
						if (plan != null) {
							indexer.indexChildrenAttributes(plan);			
						}
								
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		indexer.refresh();	
		searcher.openDir(indexer.getFileDirectory());
	}
}
