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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.treetable.TreeTableLabelProvider;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.MergeRowHighlightDecorator;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferences;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class MergeTreeLabelProvider extends TreeTableLabelProvider implements
		ILabelProvider, IColorProvider, IFontProvider {

	private Font boldFont = null;
	private Font normalFont = null;
	private Font italicFont = null;
	private Font italicBoldFont = null;
	private Integer labelFontSize = null;

	private static final Color SEPARATOR_COLOR = ColorMap.RGB_INSTANCE.getColor(new RGB(0xa0, 0x20, 0xf0));
	private static final Color UNSCHEDULED_COLOR = ColorConstants.gray;
	private MergeRowHighlightDecorator rowHighlightDecorator = null;
	
	public MergeTreeLabelProvider() {
		updateFonts();
	}

	@Override
	public void dispose() {
		super.dispose();
		for (Font font : getAllFontsUsed()) {
			if (font != null && !font.isDisposed()) {
				font.dispose();
			}
		}
	}
	
	public void setRowHighlightDecorator(MergeRowHighlightDecorator decorator) {
		this.rowHighlightDecorator = decorator;
	}
	
	public MergeRowHighlightDecorator getRowHighlightDecorator() {
		return rowHighlightDecorator;
	}
	
	@Override
	public Color getBackground(Object element) {
		if (rowHighlightDecorator != null && rowHighlightDecorator.isRowColorHighlightingVisible()) {
			return rowHighlightDecorator.getBackgroundColor(element);
		}
		if (isSeparator(element)) {
			return SEPARATOR_COLOR;
		}
		return null;
	}
	
	@Override
	public Color getForeground(Object element) {
		if (element instanceof EPlanElement) {
			TemporalMember member = ((EPlanElement) element).getMember(TemporalMember.class);
			if (member != null && member.getScheduled() != null &&  !member.getScheduled()) {
				return UNSCHEDULED_COLOR;
			}
		}
		if (rowHighlightDecorator != null && rowHighlightDecorator.isRowColorHighlightingVisible()) {
			return rowHighlightDecorator.getForegroundColor(element);
		}
		return null;
	}

	@Override
	public Font getFont(Object element) {
		if (element instanceof EPlanElement) {
			TemporalMember member = ((EPlanElement) element).getMember(TemporalMember.class);
			if (member != null && member.getScheduled() != null && !member.getScheduled()) {
				if (element instanceof EActivity) {
					return italicFont;
				} else {
					return italicBoldFont;
				}
			}
		}
		if (element instanceof EActivity) {
			return normalFont;
		}
		return boldFont;
	}

	/**
	 * Called each time the font size is changed. In order to see the text in
	 * the new font size, the old font must be disposed and a new one created.
	 * 
	 * @param fontSize
	 */
	public void updateFonts() {
		Font[] oldFonts = getAllFontsUsed();
		FontData systemFontData = Display.getDefault().getSystemFont().getFontData()[0];
		int height = getLabelFontSize();
		boldFont = new Font(null, systemFontData.getName(), height, SWT.BOLD);
		normalFont = new Font(null, systemFontData.getName(), height, SWT.NORMAL);
		italicFont = new Font(null, systemFontData.getName(), height, SWT.ITALIC);
		italicBoldFont = new Font(null, systemFontData.getName(), height, SWT.ITALIC | SWT.BOLD);
		for (Font oldFont : oldFonts) {
			if (oldFont != null && !oldFont.isDisposed()) {
				oldFont.dispose();
			}
		}
	}

	private Font[] getAllFontsUsed() {
		return new Font[] { boldFont, normalFont, italicFont, italicBoldFont };
	}

	public int getLabelFontSize() {
		if (labelFontSize != null) {
			return labelFontSize;
		}
		return getFontSizePreference();
	}
	
	public void setLabelFontSize(int fontSize) {
		labelFontSize = fontSize;
	}
	
	protected int getFontSizePreference() {
		return MergeEditorPreferences.getFontSize();
	}

	// implementation of SAP side effect
	private boolean isSeparator(final Object element) {
		if (element instanceof EActivityGroup) {
			String name = ((EActivityGroup) element).getName();
			return (name == null || name.trim().length() == 0);
		}
		return false;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		if ((feature == PlanPackage.Literals.EPLAN_ELEMENT__NAME)
			|| (feature == PlanPackage.Literals.COMMON_MEMBER__EXPANDED)) {
			return true;
		} else if(rowHighlightDecorator != null && rowHighlightDecorator.isRowColorHighlightingVisible()) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean isExpanded(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement ePlanElement = (EPlanElement) element;
			return ePlanElement.getMember(CommonMember.class).isExpanded();
		}
		return false;
	}
	
	@Override
	public void expand(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement ePlanElement = (EPlanElement) element;
			CommonMember member = ePlanElement.getMember(CommonMember.class);
			/*
			 * do this on the sly!
			 */
			boolean eDeliver = member.eDeliver();
			try {
				member.eSetDeliver(false);
				member.setExpanded(true);
			} finally {
				member.eSetDeliver(eDeliver);
			}
		}
	}
	
}
