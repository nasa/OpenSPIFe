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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.util.DurationStringifier;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jscience.physics.amount.Amount;

public class MoveSelectedDialog extends Dialog {

	private static final String[] FORMAT_HELP = {
		"Examples:",
		"'-2d' (minus two days)",
		"'+1h' or '+01:00' or '+01:00:00' (plus one hour)",
		"'-30m' or '-00:30' or '-00:30:00' (minus thirty minutes)",
		"'129/ 2013 18:30' (day 129 at 18:30)",
		"'10:00' (same day at 10:00)",
		};
	private static final boolean DEFAULT_TO_CURRENT = EnsembleProperties.getBooleanPropertyValue("plan.editor.move.defaulttocurrent", true);
	private final Date referenceDate;
	private Amount<Duration> offset;
	
	private Text text;
	
	private final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	private final IStringifier<Amount<Duration>> DURATION_STRINGIFIER = new DurationStringifier();

	protected MoveSelectedDialog(Shell parentShell, Date referenceDate) {
		super(parentShell);
		this.referenceDate = referenceDate;
	}
	
	public Amount<Duration> getOffset() {
		return offset;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
        	offset = updateOffset();
        } else {
        	offset = null;
        }
        super.buttonPressed(buttonId);
    }
	
	@SuppressWarnings("unchecked")
	public Amount<Duration> updateOffset() {
		String string = text.getText();
		if (string != null && string.length() > 0) {
			try {
				if (string.startsWith("+") || string.startsWith("-")) {
					String durationString = string.substring(1);
					Amount<Duration> duration = DURATION_STRINGIFIER.getJavaObject(durationString, null);
					if ((duration != null) && string.startsWith("-")) {
						duration = duration.opposite();
					}
					return duration;
				} else {
					Date date = DATE_STRINGIFIER.getJavaObject(string, referenceDate);
					long delta = date.getTime() - referenceDate.getTime();
					return Amount.valueOf(delta, SI.MILLI(SI.SECOND));
				}
			} catch (Exception e) {
				// fall through to set false
			}
		}
		return null;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Move Selected Actions");
		super.configureShell(newShell);
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control result = super.createContents(parent);
        validateInput();
        return result;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout(1, true);
		container.setLayout(gridLayout);
		
		GridData data = null;
		
		Label purposeLabel = new Label(container, SWT.WRAP);
        purposeLabel.setText("Enter the desired start time of the earliest element or how far to move them");
        data = new GridData(GridData.GRAB_HORIZONTAL
                | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_CENTER);
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        purposeLabel.setLayoutData(data);
        purposeLabel.setFont(parent.getFont());
		
		text = new Text(container, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));
        text.setText(DEFAULT_TO_CURRENT? DATE_STRINGIFIER.getDisplayString(referenceDate) : "");
        text.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
      
         for (String formatHelpLine : FORMAT_HELP) {
        	Label formatLabel = new Label(container, SWT.WRAP);
        	formatLabel.setText(formatHelpLine.replace('\'', '"'));
        	data = new GridData(GridData.GRAB_HORIZONTAL
        			| GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
        			| GridData.VERTICAL_ALIGN_CENTER);
        	data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        	formatLabel.setLayoutData(data);
        	formatLabel.setFont(parent.getFont());
        }
        
		return container;
	}

	private void validateInput() {
		Amount<Duration> offset = updateOffset();
		Control button = getButton(IDialogConstants.OK_ID);
		button.setEnabled(offset != null);
	}

}
