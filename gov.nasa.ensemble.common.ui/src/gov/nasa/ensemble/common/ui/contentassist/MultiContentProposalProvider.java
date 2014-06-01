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
package gov.nasa.ensemble.common.ui.contentassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

/**
 * Combines the input from multips IContentProposalProviders into a single sensicle list.
 * This will sort the lists based upon the provided label name.
 */
public class MultiContentProposalProvider implements IContentProposalProvider {

	private List<IContentProposalProvider> proposalProviders = new ArrayList<IContentProposalProvider>();
	
	public void addProposalProvider(IContentProposalProvider provider) {
		proposalProviders.add(provider);
	}
	
	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		List<IContentProposal> proposals = new ArrayList<IContentProposal>();
		for (IContentProposalProvider provider : proposalProviders) {
			IContentProposal[] icp = provider.getProposals(contents, position);
			if (icp == null) continue;
			for (IContentProposal prop: icp) 
				proposals.add(prop);
		}
		Collections.sort(proposals, new Comparator<IContentProposal>() {
			@Override
			public int compare(IContentProposal arg0, IContentProposal arg1) {
				return arg0.getLabel().compareTo(arg1.getLabel());
			}			
		});
		return proposals.toArray(new IContentProposal[0]);
	}

}
