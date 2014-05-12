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
package gov.nasa.ensemble.core.model.plan.diff.report.viewer;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.report.Activator;
import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffDifftypeNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffModificationNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNameNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffObjectNode;
import gov.nasa.ensemble.core.model.plan.provider.EPlanElementLabelProvider;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PlanDiffLabelProvider implements ILabelProvider, IColorProvider, IFontProvider {
	
	private static ImageDescriptor addedImageDescriptor = badge("plus");
	private static ImageDescriptor deletedImageDescriptor = badge("minus");
	private static ImageDescriptor modifiedImageDescriptor = badge("delta");
	private static ImageDescriptor movedinImageDescriptor = badge("arrow-in");
	private static ImageDescriptor movedoutImageDescriptor = badge("arrow-out");
	
	private static final FontData systemFontData = Display.getDefault().getSystemFont().getFontData()[0];
	private static final Font unscheduledFont = new Font(null, systemFontData.getName(), systemFontData.getHeight(), SWT.ITALIC);
	private static final Color deletedColor = ColorConstants.gray;

	
	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	
	private EPlanElementLabelProvider activityIconProvider = new EPlanElementLabelProvider();

	public Image getImage(Object element) {
		if (element instanceof PlanDiffObjectNode) return getImage((PlanDiffObjectNode)element);
		return null;
	}
	
	private Image getImage(PlanDiffObjectNode element) {	
		EObject object = element.getObject();
		Image undecoratedImage = null;
		if (object instanceof EPlanElement) {
			undecoratedImage = activityIconProvider.getImage(object);
		}
		if (undecoratedImage==null) return null;
		ImageDescriptor badge = diffTypeToBadge(element.getDiffType());
		return new DecorationOverlayIcon(undecoratedImage, badge, IDecoration.BOTTOM_RIGHT).createImage();
	}
	
	public String getText(Object element) {
		if (element instanceof AbstractDiffTree) return getText((AbstractDiffTree)element);
		if (element instanceof PlanDiffObjectNode) return getText((PlanDiffObjectNode)element);
		if (element instanceof PlanDiffDifftypeNode) return getText((PlanDiffDifftypeNode)element);
		if (element instanceof PlanDiffModificationNode) return getText((PlanDiffModificationNode)element);
		if (element instanceof PlanDiffNameNode) return getText((PlanDiffNameNode)element);
		return "???";
	}

	private String getText(AbstractDiffTree element) {
		return "Sorted by " + element.getDescriptionForUser() + ":";
	}

	private String getText(PlanDiffObjectNode node) {
		EPlanElement object = (EPlanElement)node.getObject();
		TemporalMember temporalMember = object.getMember(TemporalMember.class);
		// if (!temporalMember.getScheduled()) ... make gray ...
		String startTimeString = DATE_STRINGIFIER.getDisplayString(temporalMember.getStartTime());
		String parentInfo = "";
		if (object.eContainer() instanceof EActivityGroup
				&& !node.parentObjectMentionedAboveInTree()) {
			parentInfo = " << " + node.getParentName();
		}
		return startTimeString + "  |  " + node.getName() + parentInfo ;
	}

	private ImageDescriptor diffTypeToBadge(DiffType diffType) {
		if (diffType==DiffType.ADD) return addedImageDescriptor;
		else if (diffType==DiffType.REMOVE) return deletedImageDescriptor;
		else if (diffType==DiffType.MODIFY) return modifiedImageDescriptor;
		else if (diffType==DiffType.MOVE_IN) return movedinImageDescriptor;
		else if (diffType==DiffType.MOVE_OUT) return movedoutImageDescriptor;
		else return null;
	}
	
	@SuppressWarnings("unused") // Unused at the moment
	private String diffTypeToString(DiffType diffType) {
		if (diffType==DiffType.ADD) return "added";
		else if (diffType==DiffType.REMOVE) return "deleted";
		else if (diffType==DiffType.MODIFY) return "modified";
		else if (diffType==DiffType.MOVE_IN) return "moved here";
		else if (diffType==DiffType.MOVE_OUT) return "moved elsewhere";
		else return "???";
	}

	private String getText(PlanDiffNameNode element) {
		return element.getName();
	}

	private String getText(PlanDiffDifftypeNode element) {
		return element.getDescription();
	}

	private String getText(PlanDiffModificationNode element) {
		return element.getDiff().getParameter().getName();
	}

	public void addListener(ILabelProviderListener listener) {
		// Not supported.		
	}

	public void dispose() {
		// Nothing needed?
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// Not supported.		
	}

	public Color getBackground(Object element) {
		return null;
	}

	public Color getForeground(Object element) {
		if (element instanceof PlanDiffNode) return getForeground((PlanDiffNode)element);
		return null;
	}

	public Color getForeground(PlanDiffNode node) {
		if (node.isDeleted()) {
			return deletedColor;
		} else {
			return null;
		}
	}


	public Font getFont(Object element) {
		if (element instanceof PlanDiffNode) return getFont((PlanDiffNode)element);
		return null;
	}

	private Font getFont(PlanDiffNode node) {
		if (node.isUnscheduled()) {
			return unscheduledFont;
		} else {
			return null;
		}
	}
	
	
	private static ImageDescriptor badge(String name) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"icons/badges/" + name + ".png");
	}


}
