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
package gov.nasa.ensemble.core.model.plan.advisor.util;

import gov.nasa.ensemble.core.model.plan.advisor.AdvisorFactory;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.List;
import java.util.ListIterator;

public class WaiverUtils {

	public static WaiverPropertiesEntry getWaiverEntry(RuleAdvisorMember member, String key, boolean useTransaction) {
		final List<WaiverPropertiesEntry> waivers = member.getWaivers();
		for (WaiverPropertiesEntry entry : waivers) {
			if (entry.getKey().equals(key)) {
				return entry;
			}
		}
		final WaiverPropertiesEntry entry = (WaiverPropertiesEntry) AdvisorFactory.eINSTANCE.create(AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY);
		entry.setKey(key);
		if (useTransaction) {
			TransactionUtils.writeIfNecessary(member, new Runnable() {
				@Override
				public void run() {
					waivers.add(entry);
				}
			});
		} else {
			// when called from an EMF detail binding factory (RuleSetBindingFactory), using TransactionUtils.writeIfNecessary
			// produces an IllegalStateException because of a containing read transaction from the use of 
			// TransactionUtils.runInDisplayThread in EMFDetailFactory.buildDetailSheet.
			boolean eDeliver = member.eDeliver();
			try {
				member.eSetDeliver(false);
				waivers.add(entry);
			} finally {
				member.eSetDeliver(eDeliver);
			}
		}
		return entry;
	}
	
	public static List<String> getWaivedViolations(RuleAdvisorMember member, String key) {
		return WaiverUtils.getWaiverEntry(member, key, true).getValue();
	}
	
	public static List<String> getExistingWaivedViolations(RuleAdvisorMember member, String key) {
		final List<WaiverPropertiesEntry> waivers = member.getWaivers();
		for (WaiverPropertiesEntry entry : waivers) {
			if (key.equals(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	public static void setRationale(final IWaivable waivable, final String rationale) {
		TransactionUtils.writing(waivable, new Runnable() {
			@Override
			public void run() {
				waivable.setWaiverRationale(rationale);
			}
		});
	}
	
	public static void setRationale(List<String> waivers, final String prefix, final String rationale) {
		final ListIterator<String> iterator = waivers.listIterator();
		boolean found = false;
		while (iterator.hasNext()) {
			if (iterator.next().startsWith(prefix)) {
				if (rationale != null) {
					TransactionUtils.writing(waivers, new Runnable() {
						@Override
						public void run() {
							iterator.set(prefix + rationale);
						}
					});
				} else {
					TransactionUtils.writing(waivers, new Runnable() {
						@Override
						public void run() {
							iterator.remove();
						}
					});
				}
				found = true;
				break;
			}
		}
		if (!found && (rationale != null)) {
			TransactionUtils.writing(waivers, new Runnable() {
				@Override
				public void run() {
					iterator.add(prefix + rationale);
				}
			});
		}
	}

	public static String getRationale(String prefix, List<String> waivers) {
		for (String waiver : waivers) {
			if (waiver.startsWith(prefix)) {
				String rationale = waiver.substring(prefix.length());
				return rationale;
			}
		}
		return null;
	}
	
}
