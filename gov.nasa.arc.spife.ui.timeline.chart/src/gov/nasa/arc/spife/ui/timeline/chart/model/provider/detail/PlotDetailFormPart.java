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
package gov.nasa.arc.spife.ui.timeline.chart.model.provider.detail;

import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.detail.IDetail;
import gov.nasa.ensemble.common.ui.detail.SimpleDetail;
import gov.nasa.ensemble.common.ui.detail.view.DetailFormPart;
import gov.nasa.ensemble.common.ui.type.editor.BooleanEditor;
import gov.nasa.ensemble.common.ui.type.editor.ColorEditor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;

final class PlotDetailFormPart extends DetailFormPart {
	
	private Plot plot = null;
	private ColorEditor colorEditor = null;
	private BooleanEditor showTextEditor = null;
	private BooleanEditor autoAssignRGBEditor = null;
	private Adapter plotAdapter = new PlotAdapter();
	
	public PlotDetailFormPart() {
		super();
	}

	@Override
	public void initialize(IManagedForm form) {
		super.initialize(form);
		addDetails(form.getForm().getBody(), buildDetails(form)); 
	}

	@Override
	public void dispose() {
		plot.eAdapters().remove(plotAdapter);
		super.dispose();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean setFormInput(Object input) {
		this.plot = (Plot) input;
		if (this.plot != null) {
			colorEditor.setObject(StringConverter.asString(plot.getRgb()));
			showTextEditor.setObject(plot.isShowText());
			autoAssignRGBEditor.setObject(plot.isAutoAssignRGB());
			plot.eAdapters().add(plotAdapter);
		}
		return true;
	}

	private List<IDetail> buildDetails(IManagedForm form) {
		List<IDetail> details = new ArrayList<IDetail>();
		details.add(new SimpleDetail(form.getToolkit()) {

			@Override
			public String getName() {
				return "Color";
			}

			@Override
			public String getToolTipText() {
				return null;
			}
			
			@Override
			public Control createControl(Composite parent) {
				if (colorEditor == null) {
					colorEditor = new ColorEditor(parent);
					colorEditor.addPropertyChangeListener(new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							RGB rgb = null;
							try {
								Integer hexcode = Integer.parseInt(evt.getNewValue().toString(), 16);
								rgb = new RGB((hexcode/256/256)%256, (hexcode/256)%256, hexcode%256);
							} catch(Exception e) {
								try {
									rgb = StringConverter.asRGB((String)evt.getNewValue());
								} catch(Exception x) {
									// silence
								}
							}
							if (rgb != null && !CommonUtils.equals(rgb, plot.getRgb())) {
								final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(plot);
								final SetCommand command = (SetCommand) SetCommand.create(domain, plot, ChartPackage.Literals.PLOT__RGB, rgb);
								EMFUtils.executeCommand(domain, command);
							}
						}
					});
				}
				return colorEditor.getEditorControl();
			}
			
		});
		
		details.add(new SimpleDetail(form.getToolkit()) {

			@Override
			public String getName() {
				return "Show Text";
			}

			@Override
			public String getToolTipText() {
				return "Display text in chart for selected plot";
			}

			@Override
			public Control createControl(Composite parent) {
				if (showTextEditor == null) {
					showTextEditor = new BooleanEditor(parent);
					showTextEditor.addPropertyChangeListener(new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							Boolean value = (Boolean) evt.getNewValue();
							if (value != null && !CommonUtils.equals(value, plot.isShowText())) {
								final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(plot);
								final SetCommand command = (SetCommand) SetCommand.create(domain, plot, ChartPackage.Literals.PLOT__SHOW_TEXT, value);
								EMFUtils.executeCommand(domain, command);
							}
						}
					});
				}
				return showTextEditor.getEditorControl();
			}
			
		});
		
		details.add(new SimpleDetail(form.getToolkit()) {

			@Override
			public String getName() {
				return "Auto Assign RGB";
			}

			@Override
			public String getToolTipText() {
				return "Auto assign RGB for selected plot";
			}

			@Override
			public Control createControl(Composite parent) {
				if (autoAssignRGBEditor == null) {
					autoAssignRGBEditor = new BooleanEditor(parent);
					autoAssignRGBEditor.addPropertyChangeListener(new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							Boolean value = (Boolean) evt.getNewValue();
							if (value != null && !CommonUtils.equals(value, plot.isAutoAssignRGB())) {
								final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(plot);
								final SetCommand command = (SetCommand) SetCommand.create(domain, plot, ChartPackage.Literals.PLOT__AUTO_ASSIGN_RGB, value);
								EMFUtils.executeCommand(domain, command);
							}
						}
					});
				}
				return autoAssignRGBEditor.getEditorControl();
			}
			
		});
		
		return details;
	}
	
	private class PlotAdapter extends AdapterImpl {

		@Override
		public void notifyChanged(Notification msg) {
			Object feature = msg.getFeature();
			if (ChartPackage.Literals.PLOT__RGB == feature && colorEditor != null) {
				colorEditor.setObject(StringConverter.asString(plot.getRgb()));
			} else if (ChartPackage.Literals.PLOT__SHOW_TEXT == feature && showTextEditor != null) {
				showTextEditor.setObject(plot.isShowText());
			} else if (ChartPackage.Literals.PLOT__AUTO_ASSIGN_RGB == feature && autoAssignRGBEditor != null) {
				autoAssignRGBEditor.setObject(plot.isAutoAssignRGB());
			}
			super.notifyChanged(msg);
		}
		
	}
	
}
