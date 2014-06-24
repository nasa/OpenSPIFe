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

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DurationStringifier;

import java.text.ParseException;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jscience.physics.amount.Amount;


public class DistributeByDialog extends Dialog
{

	private Label errorLabel;
	private Button earthDurationButton;
	private Button marsDurationButton;
	private Text textField;
	private Button endAndStartButton;
	private Button startAndStartButton;
	
	private IInputValidator inputValidator;
	private Color errorMessageColor;
	
	private static boolean earthDurationSelected = true;
	private static boolean startAndStartSelected = false;
	private static String textValue = EnsembleAmountFormat.INSTANCE.formatAmount(AmountUtils.toAmount(3600, SI.SECOND));

	public boolean isEarthDurationSelected()
	{
		return earthDurationSelected;
	}

	public boolean isStartAndStartSelected()
	{
		return startAndStartSelected;
	}

	public String getTextValue()
	{
		return textValue;
	}

	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public DistributeByDialog(Shell parentShell)
	{
		super(parentShell);
		
		inputValidator = new IInputValidator()
		{
			IStringifier<Amount<Duration>> stringifier = new DurationStringifier();

			@Override
			public String isValid(String newText)
			{
				try
				{
					if(newText.trim().equals(""))
						newText = "empty";
					
					stringifier.getJavaObject(newText, null);
				}
				
				catch (ParseException e)
				{
					return e.getMessage();
				} 
				
				catch (NumberFormatException e)
				{
					return e.getMessage();
				}
				
				return "";
			}
		};
		
		errorMessageColor = ColorMap.RGB_INSTANCE.getColor(new RGB(255, 0, 0));
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container = new Composite(container, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);

		final Label distributeLabel = new Label(container, SWT.NONE);
		distributeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		distributeLabel.setText("Between:");

		final Composite timesComposite = constructRadioOptions(container);
		timesComposite.setLayoutData(new GridData());

//		new Label(container, SWT.NONE);

		final Label byLabel = new Label(container, SWT.NONE);
		byLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		byLabel.setText("by");

		textField = new Text(container, SWT.LEFT | SWT.BORDER);
		textField.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				validateInput();
			}
		});
		textField.setText(textValue);
		final GridData gd_textField = new GridData(SWT.FILL, SWT.CENTER, true, false);
		textField.setLayoutData(gd_textField);
		
		errorLabel = new Label(container, SWT.NONE);
		errorLabel.setText("");
		final GridData gd_errorLabel = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		errorLabel.setLayoutData(gd_errorLabel);
		errorLabel.setForeground(errorMessageColor);

		final Label inLabel = new Label(container, SWT.NONE);
		inLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		inLabel.setText("in:");

		final Composite durationComposite = createPlanetRadio(container);
		durationComposite.setLayoutData(new GridData());
		//
		return container;
	}

	private Composite createPlanetRadio(Composite container) {
		final Composite durationComposite = new Composite(container, SWT.NONE);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		durationComposite.setLayout(gridLayout_2);

		marsDurationButton = new Button(durationComposite, SWT.RADIO);
		marsDurationButton.setSelection(!earthDurationSelected);
		marsDurationButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		marsDurationButton.setText("Mars Duration");

		earthDurationButton = new Button(durationComposite, SWT.RADIO);
		earthDurationButton.setSelection(earthDurationSelected);
		earthDurationButton.setText("Earth Duration");
		return durationComposite;
	}

	private Composite constructRadioOptions(Composite container) {
		final Composite timesComposite = new Composite(container, SWT.NONE);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 0;
		timesComposite.setLayout(gridLayout_1);

		startAndStartButton = new Button(timesComposite, SWT.RADIO);
		startAndStartButton.setSelection(startAndStartSelected);
		startAndStartButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		startAndStartButton.setText("Start and Start");

		endAndStartButton = new Button(timesComposite, SWT.RADIO);
		endAndStartButton.setSelection(!startAndStartSelected);
		endAndStartButton.setText("End and Start");
		return timesComposite;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	private void validateInput()
	{
		if(this.getShell() == null || !this.getShell().isVisible())
			return;
		
		this.errorLabel.setText(this.inputValidator.isValid(this.textField
				.getText()));
		
		if(errorLabel.getText().equals(""))
			this.getButton(IDialogConstants.OK_ID).setEnabled(true);
		else
			this.getButton(IDialogConstants.OK_ID).setEnabled(false);
		
		errorLabel.pack();
	}
	
	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(385, super.getInitialSize().y);
	}
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Distribute By");
	}
	
	@Override
	public boolean close()
	{
		earthDurationSelected = this.earthDurationButton.getSelection();
		startAndStartSelected = this.startAndStartButton.getSelection();
		if(this.errorLabel.getText().equals(""))
			textValue = this.textField.getText();
		
		//errorMessageColor.dispose();
		return super.close();
	}
	
}
