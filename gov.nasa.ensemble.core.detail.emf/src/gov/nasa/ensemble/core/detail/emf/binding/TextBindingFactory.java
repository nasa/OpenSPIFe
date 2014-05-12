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
package gov.nasa.ensemble.core.detail.emf.binding;

import static fj.data.Option.*;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.thread.ThreadUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.date.defaulting.DefaultDateUtil;
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class TextBindingFactory extends BindingFactory {
	
	public static final String PROP_DETAIL_SHOW_VALIDATION_BUBBLE = "ensemble.detail.showbubble";
	private static final Color INVALID_INPUT_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private static final Color VALID_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

	public static final String CONTEXT_EMF_ATTRIBUTE = "emf_attribute";
	
	private static ToolTip bubble;

	private static Boolean showBubble = null;
	
	private static final ExecutorService pool = 
		ThreadUtils.newCoalescingThreadPool("TextBindingFactory bubble revealer");


	@Override
	public Binding createBinding(DetailProviderParameter p) {
		Composite parent = p.getParent();
		return createBinding(parent, true, p);
	}

	public Binding createBinding(Composite parent, boolean createLabel, final DetailProviderParameter p) {
		final EObject target = p.getTarget();
		final IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		if (useBubble()) {
			if (bubbleAvailable())
				bubble.dispose();
			bubble = new ToolTip(parent.getShell(), SWT.BALLOON | SWT.ICON_ERROR);
		}
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		if (feature == null) {
			return null;
		}

		final Text text = createTextControl(p, parent, createLabel);
		if (text == null) {
			return null;
		}
		
		Control control = EMFDetailUtils.addDescriptionTooltip(pd, target, text);
		// SWT doesn't allow tooltips on disabled controls so add to parent as well!
		EMFDetailUtils.addDescriptionTooltip(pd, target, parent); 
		
		IObservableValue targetObservableValue = observeText(control);
		StringifierUpdateValueStrategy targetToModel = new StringifierUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE, target);
		StringifierUpdateValueStrategy modelToTarget = new StringifierUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE, target);
		Binding binding = EMFDetailUtils.bindEMFUndoable(p, targetObservableValue, targetToModel, modelToTarget);
		EMFDetailUtils.bindTextModifyUndoable(text, target, EMFDetailUtils.getDisplayName(target, pd));
		// bindValidatorDecoration after bindEMFUndoable so the setting the text color to invalid does not get undone
		EMFDetailUtils.bindControlViability(p, new Control[] {text});
		EMFDetailUtils.bindValidatorDecoration(p, text);
		return binding;
	}
	
	/**
	 * This is its own method so that a 3.5-based subclass can override it to
	 * use the version of observeText which takes an array of event types.
	 */
	protected IObservableValue observeText(Control control) {
		return SWTObservables.observeText(control, new int[] {SWT.FocusOut, SWT.DefaultSelection});
	}

	protected Text createTextControl(final DetailProviderParameter p, Composite parent, boolean createLabel) {
		final IItemPropertyDescriptor pd;
		final EObject eObject;
		if (p.getPropertyDescriptor() instanceof MultiItemPropertyDescriptor) {
			final MultiItemPropertyDescriptor multi = (MultiItemPropertyDescriptor) p.getPropertyDescriptor();
			pd = multi.getPrimaryDescriptor();
			final EObject multiPrimary = fromNull(multi.getPrimaryTarget()).orSome(p.getTarget());
			EObject owner = EMFDetailUtils.getCommandOwner(pd, target);
			if (owner != null) {
				eObject =  fromNull(owner).orSome(multiPrimary);
			} else {
				eObject = multiPrimary;
			}
		} else {
			pd = p.getPropertyDescriptor();
			eObject = p.getTarget();
		}
		EAttribute feature = (EAttribute) pd.getFeature(eObject);
		FormToolkit toolkit = p.getDetailFormToolkit();
		boolean isEditable = pd.canSetProperty(eObject);
		GridData gridData;
		final IStringifier<Object> stringifier = getStringifier(eObject, feature);
		Object value = EMFUtils.getPropertyValue(p.getPropertyDescriptor(), p.getTarget());
		String string = stringifier.getDisplayString(value);
		if(string == null) {
			string = "";
		}
		boolean isMultiLine = pd.isMultiLine(eObject);
		if (!isMultiLine) {
			isMultiLine = ((string.length() > 100) && string.contains(" ")) ? true : false;
		}
		int editableFlags = (isEditable ? SWT.BORDER : SWT.READ_ONLY );
		int flags = isMultiLine ? (SWT.MULTI | editableFlags | SWT.WRAP | SWT.V_SCROLL) : editableFlags;
		if ((flags & SWT.WRAP) != 0 && string.indexOf(' ') == -1  && !isMultiLine) {
			flags &= ~SWT.WRAP;
		}
		//
		// Create the label
		if (createLabel) {
			Label label = EMFDetailUtils.createLabel(parent, toolkit, eObject, pd);
			if (isMultiLine) {
				gridData = new GridData();
				gridData.verticalAlignment = SWT.TOP;
				gridData.horizontalSpan = 2;
				label.setLayoutData(gridData);
			}
		}
		//
		// Create the editor composite that will store the text and the short description if available
		String shortDescription = getShortDescription(eObject, feature);
		Composite composite = null;
		if (shortDescription != null && shortDescription.length() > 0) {
			composite = toolkit.createComposite(parent, SWT.NONE);
			gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			composite.setLayoutData(gridData);
			GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			composite.setLayout(layout);
		}

		final Text text = new Text((composite != null) ? composite : parent, flags);
		
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		// SPF-7921 Allow text control to be narrower than its preferred size to reduce the need for horizontal scrolling
		gridData.minimumWidth = 1;
		gridData.widthHint = Activator.CONTROL_WIDTH_HINT;
		
		if (isMultiLine) {
			GC gc = new GC(WidgetUtils.getDisplay());
			FontMetrics fm = gc.getFontMetrics();
			gc.dispose();
			
			gridData.horizontalSpan = 2;
			gridData.horizontalIndent = 10;
			gridData.verticalAlignment = SWT.FILL;
			gridData.grabExcessVerticalSpace = true;
			if ("gtk".equalsIgnoreCase(SWT.getPlatform())) {
				gridData.heightHint = 3 * (fm.getHeight() + 2);
			} else {
				gridData.minimumHeight = 3 * (fm.getHeight() + 2);
			}
			
			/* update the container's width hint to match what the layout allocated for it */
			gridData.widthHint = parent.getBounds().width;
		}
		text.setLayoutData(gridData);
		text.setText(string);
		text.setEditable(isEditable);
		
		if (isMultiLine) {
			text.addTraverseListener(new TraverseListener() {
				@Override
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
						e.doit = true;
					}
				}
			});
		}
		final ModifyListener modifylistener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.getSource();
				updateValidityColor(text, eObject, pd, stringifier, true);
			}
		};
		text.addModifyListener(modifylistener);
		final FocusListener focusListener = new FocusAdapter() {	
			@Override
			public void focusLost(FocusEvent e) {							
					WidgetUtils.runLaterInDisplayThread(text, new Runnable() {
						@Override
						public void run() {
							p.getDataBindingContext().updateTargets();
						}
					});
				}
			};
		text.addFocusListener(focusListener);
		
		text.addDisposeListener(
			new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					text.removeModifyListener(modifylistener);
					text.removeFocusListener(focusListener);
					if (bubbleAvailable())
						bubble.dispose();
					text.removeDisposeListener(this);
				}
			}
		);
		
		if (composite != null) {
			toolkit.createLabel(composite, shortDescription);
		}
		toolkit.adapt(text, true, !isMultiLine);
		
		parent.pack();
		
		// SPF-10636: Restrict the height of the Text widget, some text fields
		// have large text which may result in an outrageous Text field height.
		if(isMultiLine && text.getLineCount() > 20) {
			gridData.heightHint = 200;
			// re-pack it to reset the text height
			parent.pack();
		}
		return text;
	}

	private String getShortDescription(EObject object, EAttribute feature) {
		String shortDescription = EMFUtils.getAnnotation(feature, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, EMFDetailUtils.ANNOTATION_DETAIL_SHORT_DESCRIPTION);
		if (shortDescription != null && !shortDescription.isEmpty() && shortDescription.charAt(0) == '.') {
			String descriptionFeatureName = shortDescription.substring(1);
			EStructuralFeature descriptionFeature = object.eClass().getEStructuralFeature(descriptionFeatureName);
			if (descriptionFeature == null) {
				return shortDescription;
			}
			Object value = object.eGet(descriptionFeature);
			if (value != null) {
				return value.toString();
			}
		}
		return shortDescription;
	}

	/**
	 * Sets the validity color according to the current text.
	 * 
	 * ShowTip, determines if a "tip" about the error should be displayed
	 */
	private void updateValidityColor(final Text text, final EObject eObject, IItemPropertyDescriptor pd, final IStringifier<Object> stringifier, final boolean showTip) {
		try {
			// Don't update the validity color if the Text has an associated error decoration from EMF validation
			if (!EMFDetailUtils.hasErrorDecoration(text)) {
				text.setForeground(VALID_COLOR);
				// Default control tooltip description
				EMFDetailUtils.addDescriptionTooltip(pd, eObject, text);
			}
			Object value = pd.getPropertyValue(eObject);
			if (value instanceof PropertyValueWrapper) {
				value = ((PropertyValueWrapper)value).getEditableValue(eObject);
			}
			Object parsedValue = null;
			if (stringifier == null) {
				text.setForeground(INVALID_INPUT_COLOR);
				Logger.getLogger(TextBindingFactory.class).error("null stringifier for: " + pd.getDisplayName(eObject));
			} else {
				Object defaultValue = value;
				if (defaultValue==null && (IStringifier)stringifier instanceof DateStringifier) {
					Date smartDefault = DefaultDateUtil.tryHarderToFindDefaultDateIfApplicable(eObject, stringifier);
					if (smartDefault != null) defaultValue = smartDefault;
				}
				parsedValue = stringifier.getJavaObject(text.getText(), defaultValue);
			}
			if (bubbleShowing()) {
				bubble.setVisible(false);
			}
			if (parsedValue instanceof Date) {
				String toolTipText = MissionCalendarUtils.formatToolTipDate((Date)parsedValue);
				if (!toolTipText.equals(text.getText())) {
					text.setToolTipText(toolTipText);
					// SWT doesn't allow tooltips on disabled controls so add to parent as well
					text.getParent().setToolTipText(toolTipText);
				}
			}
		} catch (final ParseException pe) {
			showError(text, pe.getMessage(), showTip);
		}
	}

	public static void showError(final Text text, final String msg, boolean showBubble) {
		text.setForeground(INVALID_INPUT_COLOR);
		text.setToolTipText(msg);
		if (showBubble) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					WidgetUtils.runInDisplayThread(text, new Runnable() {
						@Override
						public void run() {
							if (bubbleAvailable()) {
								Point textLocation = text.getLocation();
								Point textSize = text.getSize();
								textLocation = new Point(textLocation.x, textLocation.y + textSize.y);
								Point bubbleLocation = WidgetUtils.getDisplay().map(text.getParent(), null, textLocation);
								bubble.setText("Invalid Input");
								bubble.setMessage(msg == null ? "" : msg);
								bubble.setLocation(bubbleLocation);
								bubble.setVisible(true);
								bubble.setAutoHide(true);
							}
						}
					}, true);
				}
			});
		}
	}
	
	public static void clearError(Text text) {
		text.setForeground(VALID_COLOR);
		if (bubbleShowing()) {
			bubble.setVisible(false);
		}
	}
	
	private static boolean useBubble() {
		if (showBubble == null)
			showBubble  = "true".equalsIgnoreCase(System.getProperty(PROP_DETAIL_SHOW_VALIDATION_BUBBLE, "true"));
		return showBubble;
	}
	
	private static boolean bubbleAvailable() {
		return bubble != null && !bubble.isDisposed();
	}
	
	private static boolean bubbleShowing() {
		return bubbleAvailable() && bubble.isVisible();
	}
	
	@SuppressWarnings("unchecked")
	private <T> IStringifier<T> getStringifier(EObject eObject, EAttribute eAttribute) {
		// first try looking up a stringifier by attribute
		IEMFStringifierFactory factory = (IEMFStringifierFactory)Platform.getAdapterManager().getAdapter(eObject, IEMFStringifierFactory.class);
		if (factory != null) {
			IStringifier stringifier = factory.getStringifier(eAttribute);
			if (stringifier != null)
				return stringifier;
		}
		return (IStringifier<T>) EMFUtils.getStringifier(eAttribute);
	}
}
