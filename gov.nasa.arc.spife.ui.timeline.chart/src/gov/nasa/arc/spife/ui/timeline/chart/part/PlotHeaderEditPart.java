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
package gov.nasa.arc.spife.ui.timeline.chart.part;

import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartDropEditPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.policy.PlotHeaderCurrentTimeFeedbackPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotUtil;
import gov.nasa.arc.spife.ui.timeline.policy.CursorTimeFeedbackEditPolicy;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineHeaderRowSelectionEditPolicy;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.jscience.physics.amount.Amount;

public class PlotHeaderEditPart extends TimelineViewerEditPart<Plot> {

	protected static final Image IMAGE_CLOSE = WidgetPlugin.getDefault().getImageRegistry().get(WidgetPlugin.KEY_IMAGE_CLOSE_NICE);
	protected RectangleFigure colorBox = null;
	protected Label currentValueLabel = null;
	private Listener listener = new Listener();
	private boolean showCloseLabel;
	private Font font = null;
	private Label nameLabel = null;
	
	public PlotHeaderEditPart() {
		this(true);
	}
	
	public PlotHeaderEditPart(boolean showCloseLabel) {
		this.showCloseLabel = showCloseLabel;
	}
	
	@Override
	public void activate() {
		super.activate();
		getModel().eAdapters().add(listener);
		Profile<?> profile = getModel().getProfile();
		if (profile != null) {
			profile.eAdapters().add(listener);
		}
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		getModel().eAdapters().remove(listener);
		Profile<?> profile = getModel().getProfile();
		if (profile != null) {
			profile.eAdapters().remove(listener);
		}
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
	}

	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected IFigure createFigure() {
		final Plot plot = getModel();
		
		IFigure f = new Figure();
		font = TimelineUtils.deriveRowElementHeightFont(f.getFont());
		f.setLayoutManager(new BorderLayout() {
			@Override
			/* This is here so that the header height matches the row data height */
			protected Dimension calculateMinimumSize(IFigure container, int hint, int hint2) {
				Dimension d = super.calculateMinimumSize(container, hint, hint2);
				d.height = TimelineUtils.getRowElementHeight(PlotHeaderEditPart.this);
				d.width = 0;
				return d;
			}

			@Override
			protected Dimension calculatePreferredSize(IFigure container, int hint, int hint2) {
				Dimension d = super.calculatePreferredSize(container, hint, hint2);
				d.height = TimelineUtils.getRowElementHeight(PlotHeaderEditPart.this);
				d.width = 0;
				return d;
			}
		});
		f.setOpaque(true);
		
		// close
		Label closeLabel = null;
		if (showCloseLabel) {
			closeLabel = new Label();
			closeLabel.setIcon(IMAGE_CLOSE);
			closeLabel.addMouseListener(new MouseListener.Stub() {
				@Override
				public void mousePressed(MouseEvent me) {
					final Chart chart = getModel().getChart();
					EditingDomain domain = EMFUtils.getAnyDomain(chart);
					Command command = RemoveCommand.create(domain, chart, ChartPackage.Literals.CHART__PLOTS, plot);
					EMFUtils.executeCommand(domain, command);
					getViewer().setSelection(new StructuredSelection());
				}
			});
		}
		
		// name
		nameLabel = new Label();
		nameLabel.setLabelAlignment(PositionConstants.LEFT);

		// color
		boolean isHeatMap = ChartStyle.HEAT_MAP == plot.getChart().getStyle();
		if (!isHeatMap) {
			colorBox = new RectangleFigure();
			colorBox.setOpaque(true);
			colorBox.setPreferredSize(16,16);
		}
		
		refreshPlotLabel(plot);

		currentValueLabel = new Label();
		currentValueLabel.setFont(font);
		currentValueLabel.setTextAlignment(PositionConstants.RIGHT);
		
		Figure majorLabel = new Figure();
		ToolbarLayout layout = new ToolbarLayout(true);
		layout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
		layout.setSpacing(5);
		majorLabel.setLayoutManager(layout);
		if (showCloseLabel && closeLabel != null)
			majorLabel.add(closeLabel);
		if (colorBox != null) {
			majorLabel.add(colorBox);
		}
		majorLabel.add(nameLabel);
		
		f.add(majorLabel, BorderLayout.CENTER);
		f.add(currentValueLabel, BorderLayout.RIGHT);
		f.setToolTip(createTooltipFigure(plot));
		
		return f;
	}

	private void refreshPlotLabelInDisplayThread(final Plot plot) {
		GEFUtils.runInDisplayThread(this, new Runnable() {			
			@Override
			public void run() {
				refreshPlotLabel(plot);
			}
		});
	}
	
	private void refreshPlotLabel(Plot plot) {
		IItemLabelProvider lp = EMFUtils.adapt(plot, IItemLabelProvider.class);
		final String newText = (lp != null)? lp.getText(plot) : plot.getName();
		final Color newColor = PlotUtil.getColor(getModel());
		if (nameLabel != null && !CommonUtils.equals(nameLabel.getText(), newText)) {
			nameLabel.setFont(font);
			nameLabel.setText(newText);
		}
		if (colorBox != null && !CommonUtils.equals(colorBox.getBackgroundColor(), newColor)) {
			colorBox.setBackgroundColor(newColor);
		}
	}

	private Label createTooltipFigure(final Plot plot) {
		Profile profile = plot.getProfile();
		Label label = new Label();
		EDataType type = profile==null? null : profile.getDataType();
		final StringBuffer buffer = new StringBuffer();
		if (type != EcorePackage.Literals.ESTRING
				&& type != EcorePackage.Literals.EENUM) {
			String maxString = "N/A";
			String minString = "N/A";
			if (profile != null 
					&& !profile.getDataPoints().isEmpty()) {
				Amount min = ProfileUtil.getMin(profile);
				Amount max = ProfileUtil.getMax(profile);
				if (min != null && max != null) {
					minString = EnsembleAmountFormat.INSTANCE.formatAmount(min);
					maxString = EnsembleAmountFormat.INSTANCE.formatAmount(max);
				}
			}
			buffer.append("Min: "+minString+"\n");
			buffer.append("Max: "+maxString+"\n");
		}
		if (profile != null && profile.eResource() != null) {
			buffer.append("URL: "+ profile.eResource().getURI().toString());
		}
		label.setText(buffer.toString());
		return label;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(DropEditPolicy.DROP_ROLE, new ChartDropEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new TimelineHeaderRowSelectionEditPolicy());
		installEditPolicy(CursorTimeFeedbackEditPolicy.ROLE, new PlotHeaderCurrentTimeFeedbackPolicy());
	}

	public Label getCurrentValueLabel() {
		return currentValueLabel;
	}
	
	private class Listener extends AdapterImpl implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			if (TimelinePreferencePage.P_CONTENT_FONT_SIZE.equals(property)) {
				if (font != null) {
					Font oldFont = font;
					font = TimelineUtils.deriveRowElementHeightFont(oldFont);
					//oldFont.dispose();
				} else {
					font = TimelineUtils.deriveRowElementHeightFont(FontUtils.getSystemFont());
				}
				nameLabel.setFont(font);
				currentValueLabel.setFont(font);
				getFigure().repaint();
			} else if (TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT.equals(property)) {
				getFigure().getParent().invalidateTree();
				refreshVisuals();
			}
		}
		
		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			Plot plot = getModel();
			if (ChartPackage.Literals.PLOT__RGB == feature
					|| JSciencePackage.Literals.PROFILE__VALID == feature) {
				refreshPlotLabelInDisplayThread(plot);
			} else if (ChartPackage.Literals.PLOT__PROFILE == feature) {
				Profile oldProfile = (Profile) notification.getOldValue();
				if (oldProfile != null) {
					oldProfile.eAdapters().remove(listener);
				}
				Profile newProfile = (Profile) notification.getNewValue();
				if (newProfile != null) {
					newProfile.eAdapters().add(listener);
				}
				refreshPlotLabelInDisplayThread(plot);
			} else {
				if (plot != null) { // can be changed to null while shutting down
					getFigure().setToolTip(createTooltipFigure(plot));
				}
			}
		}
		
	}

}
