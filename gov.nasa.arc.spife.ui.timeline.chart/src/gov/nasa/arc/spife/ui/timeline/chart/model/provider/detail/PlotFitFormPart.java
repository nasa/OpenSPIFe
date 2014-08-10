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
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.type.editor.TextEditor;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.AmountStringifier;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.measure.unit.Unit;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.jscience.physics.amount.Amount;

final class PlotFitFormPart extends SectionPart {

	private Plot plot = null;

	private Text minText = null;
	private Text maxText = null;

	private Button autoButton = null;
	private Button customButton = null;

	private DataBindingContext dataBindingContext = new DataBindingContext();

	private Listener listener = new Listener();

	private Profile<?> profile;

	private Section section;

	public PlotFitFormPart(Composite parent, FormToolkit toolkit, int style) {
		super(parent, toolkit, style);
	}

	@Override
	public void dispose() {
		super.dispose();
		teardown();
	}

	@Override
	public void initialize(IManagedForm form) {
		super.initialize(form);

		section = getSection();
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setText("Fit and Range Settings");

		FormToolkit toolkit = form.getToolkit();
		Composite composite = toolkit.createComposite(section);
		composite.setLayout(new GridLayout(1, true));
		autoButton = toolkit.createButton(composite, "Auto Fit (scale to fit current range)", SWT.RADIO);
		customButton = toolkit.createButton(composite, "Custom", SWT.RADIO);

		Composite minMaxComposite = toolkit.createComposite(composite);
		minMaxComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = 15;
		minMaxComposite.setLayout(layout);

		AmountStringifier stringifier = new AmountStringifier() {

			/**
			 * @throws ParseException
			 */
			@Override
			public Amount getJavaObjectFromTrimmed(String string, Amount defaultObject) throws ParseException {
				return parseLimitText(string);
			}

			@Override
			public String getDisplayString(Amount amount) {
				return formatLimitText(amount);
			}

		};

		toolkit.createLabel(minMaxComposite, "Max: ");
		maxText = toolkit.createText(minMaxComposite, null, SWT.BORDER);
		maxText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		new TextEditor(maxText, stringifier).addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void propertyChange(final PropertyChangeEvent evt) {
				Object newValue = evt.getNewValue();
				Amount max = (Amount) (newValue instanceof Amount ? newValue : Amount.valueOf((Double) newValue, plot.getProfile().getUnits()));
				Amount min = parseLimitTextSafely(minText.getText());
				executeSetOperation(ChartPackage.Literals.PLOT__EXTENT, new AmountExtent(min, max));
			}
		});

		toolkit.createLabel(minMaxComposite, "Min: ");
		minText = toolkit.createText(minMaxComposite, null, SWT.BORDER);
		minText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		new TextEditor(minText, stringifier).addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void propertyChange(final PropertyChangeEvent evt) {
				Object newValue = evt.getNewValue();
				Amount min = (Amount) (newValue instanceof Amount ? newValue : Amount.valueOf((Double) evt.getNewValue(), plot.getProfile().getUnits()));
				Amount max = parseLimitTextSafely(maxText.getText());
				executeSetOperation(ChartPackage.Literals.PLOT__EXTENT, new AmountExtent(min, max));
			}
		});

		SelectionListener listener = new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* do nothing */
			}

			@Override
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				Button source = (Button) e.getSource();
				boolean customEnabled = customButton == source && source.getSelection();

				minText.setEnabled(customEnabled);
				maxText.setEnabled(customEnabled);

				if (customEnabled) {
					Amount min = parseLimitTextSafely(minText.getText());
					Amount max = parseLimitTextSafely(maxText.getText());

					final Plot plot = (Plot) getManagedForm().getInput();
					final TransactionalEditingDomain domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(plot);
					CompoundCommand command = new CompoundCommand("Plot extents change");
					command.append(SetCommand.create(domain, plot, ChartPackage.Literals.PLOT__EXTENT, new AmountExtent(min, max)));
					command.append(SetCommand.create(domain, plot, ChartPackage.Literals.PLOT__FIT, FitPolicy.CUSTOM));
					EMFUtils.executeCommand(domain, command);
				} else if (autoButton == source && source.getSelection()) {
					executeSetOperation(ChartPackage.Literals.PLOT__FIT, FitPolicy.AUTO);
				}
			}
		};

		autoButton.addSelectionListener(listener);
		customButton.addSelectionListener(listener);
		section.setClient(composite);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean setFormInput(Object input) {
		super.setFormInput(input);
		if (plot != null) {
			teardown();
		}
		plot = (Plot) input;
		if (plot != null) {
			setup(plot);
		}
		refresh();
		return true;
	}

	private void setup(Plot newPlot) {
		plot = newPlot;
		plot.eAdapters().add(listener);
		profile = plot.getProfile();
		if (profile != null) {
			profile.eAdapters().add(listener);
		}
		dataBindingContext = new DataBindingContext();
		EMFDetailUtils.bindControlViability(dataBindingContext, minText, plot, ChartPackage.Literals.PLOT__FIT, FitPolicy.CUSTOM);
		EMFDetailUtils.bindControlViability(dataBindingContext, maxText, plot, ChartPackage.Literals.PLOT__FIT, FitPolicy.CUSTOM);
	}

	private void teardown() {
		if (plot != null) {
			plot.eAdapters().remove(listener);
			plot = null;
		}
		if (profile != null) {
			profile.eAdapters().remove(listener);
			profile = null;
		}
		if (dataBindingContext != null) {
			dataBindingContext.dispose();
			dataBindingContext = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void refresh() {
		if (plot != null) {
			if (ChartStyle.HEAT_MAP == plot.getChart().getStyle()) {
				section.setVisible(false);
				return;
			}
			FitPolicy policy = plot.getFit();
			if (policy.equals(FitPolicy.AUTO) && !autoButton.isDisposed()) {
				autoButton.setSelection(true);
			} else if (policy.equals(FitPolicy.CUSTOM) && !customButton.isDisposed()) {
				customButton.setSelection(true);
			}
			AmountExtent<?> extent = plot.getExtent();
			if (extent != null) {
				Amount min = extent.getMin();
				if (!minText.isDisposed() && min != null) {
					minText.setText(formatLimitText(min));
				}
				Amount max = extent.getMax();
				if (!maxText.isDisposed() && max != null) {
					maxText.setText(formatLimitText(max));
				}
			}
		}
	}

	private String formatLimitText(Amount amount) {
		if (amount == null) {
			return "";
		}
		return EnsembleAmountFormat.INSTANCE.formatAmount(amount);
	}

	private Amount parseLimitTextSafely(String text) {
		Plot plot = (Plot) getManagedForm().getInput();
		try {
			try {
				return EnsembleAmountFormat.INSTANCE.parseAmount(text, plot.getProfile().getUnits());
			} catch (Exception e) {
				try {
					double d = Double.parseDouble(text);
					Unit<?> u = plot.getProfile().getUnits();
					return Amount.valueOf(d, u);
				} catch (Exception x) {
					return (Amount) JScienceFactory.eINSTANCE.createFromString(JSciencePackage.Literals.EDURATION, text);
				}
			}
		} catch (NumberFormatException e) {
			Logger.getLogger(PlotFitFormPart.class).warn("couldn't parse limit: " + text, e);
		}
		return null;
	}

	private Amount parseLimitText(String text) throws ParseException {
		Plot plot = (Plot) getManagedForm().getInput();
		try {
			return EnsembleAmountFormat.INSTANCE.parseAmount(text, plot.getProfile().getUnits());
		} catch (Exception e) {
			try {
				double d = Double.parseDouble(text);
				Unit<?> u = plot.getProfile().getUnits();
				return Amount.valueOf(d, u);
			} catch (Exception x) {
				Amount durationValue = (Amount) JScienceFactory.eINSTANCE.createFromString(JSciencePackage.Literals.EDURATION, text);
				if (durationValue != null) {
					return durationValue;
				}
			}
		}
		Unit<?> units = plot.getProfile().getUnits();
		if (units == null) {
			units = Unit.ONE;
		}
		throw new ParseException("Cannot parse '" + text + "', try 1.0 " + EnsembleUnitFormat.INSTANCE.format(units), 0);
	}

	private void executeSetOperation(EStructuralFeature feature, Object value) {
		final Plot plot = (Plot) getManagedForm().getInput();
		final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(plot);
		final SetCommand command = (SetCommand) SetCommand.create(domain, plot, feature, value);
		EMFUtils.executeCommand(domain, command);
	}

	public class Listener extends AdapterImpl {

		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			if (JSciencePackage.Literals.PROFILE__EXTENT == feature || ChartPackage.Literals.PLOT__EXTENT == feature || ChartPackage.Literals.CHART__STYLE == feature) {
				WidgetUtils.runLaterInDisplayThread(getManagedForm().getForm(), new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				});
			}
		}

	}

}
