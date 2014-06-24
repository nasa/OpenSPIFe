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
package gov.nasa.ensemble.core.plan.editor.drudgery;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

public class SwapHandler extends DrudgerySavingHandler {

	private static final Logger TRACE = Logger.getLogger(SwapHandler.class);
	
	public SwapHandler() {
		super("Swap", "Swapping");
	}
	
	@Override
	protected int getLowerBoundSelectionCount() {
		return 2;
	}
	
	@Override
	protected int getUpperBoundSelectionCount() {
		return 2;
	}
	
	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements) {
		Map<EPlanElement, Date> map = new HashMap<EPlanElement, Date>();
		if (elements.size() == 2) {
			TemporalMember temporalMemberZero = elements.get(0).getMember(TemporalMember.class);
			TemporalExtent temporalExtentZero = temporalMemberZero.getExtent();
			
			TemporalMember temporalMemberOne = elements.get(1).getMember(TemporalMember.class);
			TemporalExtent temporalExtentOne = temporalMemberOne.getExtent();
			
			if (temporalExtentZero == null || temporalExtentOne == null) {
				TRACE.error("Swap requires extents on both attivities");
				return map;
			}
			
			if(!sameSpan(temporalExtentZero, temporalExtentOne)) {
				map.put(elements.get(1), temporalExtentZero.getStart());
				map.put(elements.get(0), new Date(temporalExtentOne.getEnd().getTime()
						- temporalExtentZero.getDurationMillis()));
			}
			
			else {
					swapShorterMember(temporalMemberZero, temporalMemberOne
							, elements, map);			
			}
		} else {
			TRACE.error("Swap requires two selected elements");
		}
		return map;
	}
	
	@Override
	public boolean isEnabledForSelection(ISelection selection) {
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		if (elements.size() != 2) {
			return false;
		} else {
			ECollections.sort(elements, TemporalChainUtils.CHAIN_ORDER);
			TemporalExtent one = elements.get(0).getMember(TemporalMember.class).getExtent();
			TemporalExtent two = elements.get(1).getMember(TemporalMember.class).getExtent();
			return(!(one == null || two == null));
		}
	}
	
	/**
	 * This is the case where one member is completely contained within the span
	 * of the other and only the shorter contained member needs to have its 
	 * start/end times adjusted. The amount of space to the left and right should
	 * alternate after each call to the "swap" action.
	 * 
	 * @param temporalMemberZero
	 * @param temporalMemberOne
	 * @param elements
	 * @param map
	 */
	private void swapShorterMember(TemporalMember temporalMemberZero,
			TemporalMember temporalMemberOne,
			List<? extends EPlanElement> elements, Map<EPlanElement, Date> map) {

		TemporalMember longerTemporalMember = getLongerTemporalMember(
				temporalMemberZero, temporalMemberOne);

		TemporalMember shorterTemporalMember = getShorterTemporalMember(
				temporalMemberZero, temporalMemberOne);

		long leftSpace = getLeftSpace(longerTemporalMember,
				shorterTemporalMember);
		long rightSpace = getRightSpace(longerTemporalMember,
				shorterTemporalMember);

		if (leftSpace < rightSpace) {
			long newEnd = longerTemporalMember.getEndTime().getTime()
					- leftSpace;
			long newStart = newEnd
					- shorterTemporalMember.getExtent().getDurationMillis();
			if (shorterTemporalMember.equals(temporalMemberZero)) {
				map.put(elements.get(0), new Date(newStart));
			}

			else {
				map.put(elements.get(1), new Date(newStart));
			}
		}

		else if (leftSpace > rightSpace) {
			long newStart = longerTemporalMember.getStartTime().getTime()
					+ rightSpace;

			if (shorterTemporalMember.equals(temporalMemberZero)) {
				map.put(elements.get(0), new Date(newStart));
			}

			else {
				map.put(elements.get(1), new Date(newStart));
			}
		}
	}
	
	/**
	 * Given two members, the shorter one is returned
	 * @param a one member
	 * @param b another member
	 * @return the shortest member
	 */	
	private static TemporalMember getShorterTemporalMember(TemporalMember a
			, TemporalMember b) {
		TemporalMember result = a;
		if(a.getExtent().getDurationMillis() > b.getExtent().getDurationMillis()) {
			result = b;
		}
		
		return result;		
	}
	
	/**
	 * Given two members, the longer one is returned
	 * @param a one member
	 * @param b another member
	 * @return the longest member
	 */
	private static TemporalMember getLongerTemporalMember(TemporalMember a
			, TemporalMember b) {
		TemporalMember result = a;
		if(a.getExtent().getDurationMillis() < b.getExtent().getDurationMillis()) {
			result = b;
		}
		
		return result;
	}
	
	/**
	 * Determines the amount of space between the start of the longer member and
	 * the start of the shorter member.
	 * @param longerTemporalMember
	 * @param shorterTemporalMember
	 * @return the amount of space to the "left"
	 */
	private static long getLeftSpace(TemporalMember longerTemporalMember
			, TemporalMember shorterTemporalMember) {
		long leftSpace = shorterTemporalMember.getStartTime().getTime()
			- longerTemporalMember.getStartTime().getTime();
		
		return leftSpace;
	}
	
	/**
	 * Determines the amount of space between the end of the longer member
	 * and the end of the shorter member.
	 * @param longerTemporalMember
	 * @param shorterTemporalMember
	 * @return the amount of space to the "right"
	 */
	private static long getRightSpace(TemporalMember longerTemporalMember
			, TemporalMember shorterTemporalMember) {
		long rightSpace = longerTemporalMember.getEndTime().getTime() 
			- shorterTemporalMember.getEndTime().getTime();
		
		return rightSpace;
	}
	
	/**
	 * Check if one of the two extents falls within the span of the other
	 * @param a the first extent
	 * @param b the second extent
	 * @return true if one of the extents falls within the span of the other
	 */
	private static boolean sameSpan(TemporalExtent a, TemporalExtent b) {
		Date aStart = a.getStart();
		Date bStart = b.getStart();
		Date aEnd = a.getEnd();
		Date bEnd = b.getEnd();
		
		if(aStart.before(bStart) && aEnd.after(bEnd)) {
			return true;
		}
		
		else if(bStart.before(aStart) && bEnd.after(aEnd)) {
			return true;	
		}
		
		return false;
	}

	@Override
	public String getCommandId() {
		return SWAP_COMMAND_ID;
	}

}
