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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;

import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;

public class WizardPageProvider implements Comparable<WizardPageProvider> {

	/** Undefined sort key */
	private static final int UNDEFINED_SORT_KEY_VALUE = -1;
	
	private int sortKey = UNDEFINED_SORT_KEY_VALUE;
	private String id = null;
	private Class<? extends TransferableExtensionWizardPage> pageClass = null;
	
	public WizardPageProvider() {
		//default constructor
	}
	
	public WizardPageProvider(int sortKey, String id, Class<? extends TransferableExtensionWizardPage> pageClass) {
		this.sortKey = sortKey;
		this.id = id;
		this.pageClass = pageClass;
	}
	
	/**
	 * Method that creates the wizard page.
	 * @return IWizardPage provided to the wizard
	 */
	public TransferableExtensionWizardPage createWizardPage() {
		if (pageClass != null) {
			try {
				return pageClass.newInstance();
			} catch (Exception e) {
				Logger.getLogger(WizardPageProvider.class).error(e.getMessage(), e);
			}
		}
		return null;
	}

	/** 
	 * Default constant for the sort key, providers that 
	 * return UNDEFINED_SORT_KEY_VALUE will have their windowPages 
	 * appended to the end of the wizard windowPages, in no
	 * appreciable order.
	 * @return sort key used ot order windowPages
	 */
	public int getSortKey() {
		return sortKey;
	}

	/**
	 * Returns the identifier for the resulting wizard page.
	 * The id can be used to combine wizard windowPages through
	 * overriding page providers that implement 
	 * <code>handles(int pageId)</code>
	 * @return identifier for the page
	 */ 
	public String getId() {
		return id;
	}
	
	/**
	 * Allows implementing classes provide windowPages that override the functionality of other page providers.
	 * 
	 * @param provider
	 * @return boolean
	 */
	public boolean handles(WizardPageProvider provider) {
		return false;
	}

	/**
	 * Return the set of warnings that this wizard page will allow the
	 * user to fix.
	 * @param transferable
	 * @param location
	 * @return should not be null
	 */
	public Collection<String> getWarnings(ITransferable transferable, IStructureLocation location) {
		return Collections.emptyList();
	}
	
	/**
	 * Compares the sort key of the page providers so that
	 * windowPages can be sorted properly by a wizard.
	 */
	@Override
	public int compareTo(WizardPageProvider o) {
		if (o == this) {
			return 0;
		} else if (CommonUtils.equals(o.getSortKey(), getSortKey())) {
			return super.hashCode() - o.hashCode();
		} else {
			return getSortKey() - o.getSortKey();
		}
	}
	
}
