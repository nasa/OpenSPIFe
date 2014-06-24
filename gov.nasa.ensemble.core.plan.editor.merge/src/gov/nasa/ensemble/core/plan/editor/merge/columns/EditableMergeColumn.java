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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.MergePlugin;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class EditableMergeColumn extends AbstractMergeColumn<EPlanElement> {

	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(EditableMergeColumn.class);
	
	public EditableMergeColumn(IMergeColumnProvider provider) {
		super(provider, "Locked?", 20);
	}

	@Override
	public int getAlignment() {
		return SWT.CENTER;
	}

	@Override
	public boolean needsUpdate(Object feature) {
//		if (event.getField() == Parameter.FIELD.OBJECT) {
//			Parameter parameter = ((Parameter)event.getSource());
//			if (internalName.equalsIgnoreCase(parameter.getName())) {
//				return true;
//			}
//		}
		return true;
	}
	
	@Override
	public EPlanElement getFacet(Object element) {
		if (element instanceof EPlanElement) {
			return (EPlanElement)element;
		}
		return null;
	}
	
	// facet presentation
	
	@Override
	public Image getImage(EPlanElement element) {
		if (!PlanEditApproverRegistry.getInstance().canModify(element)) {
			return MergePlugin.getDefault().getLockedImage();
		} else {
			return null;
		}
	}

	/*
	 * Support sorting by this column
	 */
	
	private static final Comparator<EPlanElement> comparator =
		new Comparator<EPlanElement>() {
			@Override
			public int compare(EPlanElement o1, EPlanElement o2) {
				boolean locked1 = PlanEditApproverRegistry.getInstance().canModify(o1);
				boolean locked2 = PlanEditApproverRegistry.getInstance().canModify(o2);
				if (locked1 && !locked2) {
					return -1;
				} else if (locked2 && !locked1) {
					return 1;
				} else {
					return 0;
				}
			}
		};
	
	@Override
	public Comparator<EPlanElement> getComparator() {
		return comparator;
	}
	
}
