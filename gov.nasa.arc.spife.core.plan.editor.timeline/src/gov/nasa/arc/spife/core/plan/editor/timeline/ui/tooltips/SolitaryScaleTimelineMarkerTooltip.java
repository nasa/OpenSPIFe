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
package gov.nasa.arc.spife.core.plan.editor.timeline.ui.tooltips;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.part.ScaleTimelineMarkerEditPart;
import gov.nasa.arc.spife.ui.timeline.service.FileResourceMarkerService;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.editor.TemporalNodeHyperlinkListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.IMarker;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * Class to represent a tooltip for a single marker
 * @author Eugene Turkov
 *
 */
public class SolitaryScaleTimelineMarkerTooltip extends
		ScaleTimelineMarkerTooltip {

	private MouseListener mouseListener;

	protected SolitaryScaleTimelineMarkerTooltip(Display display, int style,
			final ScaleTimelineMarkerEditPart scaleTimelineMarkerEditPart
			, ViolationTracker violationTracker) {
		super(display, style, scaleTimelineMarkerEditPart, violationTracker);

		/*
		 *  mouse listener to listen for clicks on the title area. select
		 *  the appropriate violation in the plan advisor.
		 */
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Timeline timeline = scaleTimelineMarkerEditPart.getTimeline();
				IWorkbenchPage page = timeline.getSite().getPage();
				IViewReference[] viewReferences = page.getViewReferences();
				IViewPart viewPart = null;
				for(IViewReference viewReference: viewReferences) {
					viewPart = viewReference.getView(false);
					if (viewPart != null) {
						Object adapter = viewPart.getAdapter(TreeViewer.class);
						if (adapter instanceof TreeViewer) {
							TreeViewer treeViewer = (TreeViewer)adapter;
							ViolationTracker violationTracker = getViolationTracker();
							treeViewer.setSelection(new StructuredSelection(violationTracker), true);
							try {
								page.activate(viewPart);
							} catch (Exception e1) {
								// ignore failures in activation
							}
						}
					}
				}

			}
		};

		// add the mouse listener to the title
		Control[] controls = titleComposite.getChildren();
		for(Control control : controls) {
			control.addMouseListener(mouseListener);
		}
	}

	@Override
	protected Composite getSuggestionsComposite()
	{
		Violation violation = this.getViolationTracker().getViolation();
		TimelineViewer viewer = scaleTimelineMarkerEditPart.getViewer();
		PlanTimeline planTimeline = (PlanTimeline) viewer.getTimeline();
		final EPlan plan = planTimeline.getPlan();
		Set<Suggestion> suggestions = violation.getSuggestions();
		if (!suggestions.isEmpty()) {
			Section section = new Section(mainComposite, SWT.TITLE);
			section.setText("Suggestions:");
			section.setTitleBarBackground(ColorConstants.blue);
			section.setBackground(shell.getBackground());
			section.setLayoutData(new TableWrapData());
			Composite suggestionClient = new Composite(section, SWT.NONE);
			TableWrapLayout suggestionLayout = new TableWrapLayout();
			suggestionLayout.numColumns = 2;
			suggestionClient.setLayout(suggestionLayout);
			suggestionClient.setBackground(shell.getBackground());
			section.setClient(suggestionClient);
			FormToolkit toolkit = new FormToolkit(mainComposite.getDisplay());
			for (Suggestion suggestion : suggestions) {
				ImageDescriptor icon = suggestion.getIcon();
				Image image = null;
				if (icon != null) {
					image = (Image) getResourceManager().get(icon);
					if (image != null) {
						Label imageLabel = toolkit.createLabel(suggestionClient, "");
						imageLabel.setBackground(shell.getBackground());
						imageLabel.setImage(image);
					}
				}
				final Hyperlink link = toolkit.createHyperlink(suggestionClient, suggestion.getDescription(), SWT.WRAP);
				link.setBackground(shell.getBackground());
				link.setForeground(ColorConstants.blue);
				TableWrapData layoutData = new TableWrapData(TableWrapData.FILL);
				if (image == null) {
					layoutData.colspan = 2;
				}
				layoutData.maxWidth = TOOLTIP_WIDTH;
				link.setLayoutData(layoutData);
				final Suggestion linkSuggestion = suggestion;
				link.addHyperlinkListener(new HyperlinkAdapter() {

					@Override
					public void linkEntered(HyperlinkEvent e) {
						link.setFocus();
					}

					@Override
					public void linkActivated(HyperlinkEvent e) {
						IUndoContext ctx = TransactionUtils.getUndoContext(plan);
						linkSuggestion.execute(ctx, null, null);
						if (!shell.isDisposed()) {
							shell.setVisible(false);
						}
						if (groupScaleTimelineMarkerTooltip != null) {
							Shell shell2 = groupScaleTimelineMarkerTooltip.shell;
							if (!shell2.isDisposed()) {
								shell2.setVisible(false);
							}
						}
					}
					
				});
			}

			return section;
		}
		return null;
	}

	private IMarker getMarker() {
		TimelineViewer timelineViewer = scaleTimelineMarkerEditPart.getViewer();
		PlanTimeline planTimeline = (PlanTimeline) timelineViewer.getTimeline();
		TimelineMarker timelineMarker = scaleTimelineMarkerEditPart.getModel();
		FileResourceMarkerService fileResourceMarkerService = planTimeline.getFileResourceMarkerService();
		return fileResourceMarkerService.getMarker(timelineMarker);
	}

	private ViolationTracker getViolationTracker()
	{
		if(violationTracker == null)
		{
			TimelineViewer timelineViewer = scaleTimelineMarkerEditPart.getViewer();
			PlanTimeline planTimeline = (PlanTimeline) timelineViewer.getTimeline();

			EPlan plan = planTimeline.getPlanEditorModel().getEPlan();
			PlanAdvisorMember planAdvisorMember = PlanAdvisorMember.get(plan);

			IMarker marker = getMarker();
			List<ViolationTracker> violationTrackers = planAdvisorMember.getViolationTrackers();

			Violation markerViolation = planAdvisorMember.getMarkerViolation(marker);
			for (ViolationTracker violationTracker : violationTrackers) {
				Violation violation = violationTracker.getViolation();
				if (violation.equals(markerViolation)) {
					return violationTracker;
				}
			}
		}

		return violationTracker;
	}

	@Override
	protected Composite getTitleComposite()
	{
		Composite titleComposite = new Composite(mainComposite, SWT.NONE);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		titleComposite.setLayout(layout);
		titleComposite.setBackground(shell.getBackground());

		ViolationTracker violationTracker = this.getViolationTracker();
		Violation violation = violationTracker.getViolation();
		Image image = this.getImage(violationTracker);

		Label titleCompositeImageLabel = new Label(titleComposite, SWT.NONE);
		titleCompositeImageLabel.setBackground(shell.getBackground());
		titleCompositeImageLabel.setImage(image);

		Label titleCompositeTextLabel = new Label(titleComposite, SWT.WRAP);
		TableWrapData layoutData = new TableWrapData(TableWrapData.FILL);
		layoutData.maxWidth = TOOLTIP_WIDTH;
		titleCompositeTextLabel.setLayoutData(layoutData);
		titleCompositeTextLabel.setForeground(ColorConstants.blue);
		titleCompositeTextLabel.setBackground(shell.getBackground());
		titleCompositeTextLabel.setText(violation.getName() + ": " + violation.getDescription());
		titleCompositeTextLabel.setFont(FontUtils.getSystemBoldFont());

		return titleComposite;
	}

	@Override
	protected Composite getBodyComposite() {
		TimelineViewer viewer = scaleTimelineMarkerEditPart.getViewer();
		final EPlan plan = (EPlan) viewer.getTimeline().getModel();
		ISelectionProvider selectionProvider = viewer.getSite().getSelectionProvider();

		FormText formText = new FormText(mainComposite, SWT.NONE);
		formText.setBackground(shell.getBackground());
		formText.addHyperlinkListener(new TemporalNodeHyperlinkListener(selectionProvider, plan, identifiableRegistry) {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				super.linkActivated(e);
				Object object = e.getSource();
				if(object instanceof Composite) {
					Composite composite = (Composite)object;
					composite.getShell().dispose();
				}
			}
		});
		TableWrapData layoutData = new TableWrapData(TableWrapData.FILL);
		layoutData.maxWidth = TOOLTIP_WIDTH;
		formText.setLayoutData(layoutData);

		Violation violation = this.getViolationTracker().getViolation();
		String violationText = violation.getFormText(formText, identifiableRegistry);
		formText.setText("<form><P>" + violationText + "</P></form>", true, false);
		return formText;
	}
}
