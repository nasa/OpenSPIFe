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
package gov.nasa.ensemble.core.plan.editor.merge.action;

import java.util.Date;

import javax.measure.unit.SI;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.IMergeRowHighlightDecorable;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

public class OneOfEachRowDecoratorParameter extends AbstractPlanEditorHandler {

	public static final String COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.merge.action.OneOfEachRowDecoratorParameter";

	public boolean isDecorable(IWorkbenchPart part) {
		if (part instanceof MultiPagePlanEditor) {
			part = ((MultiPagePlanEditor) part).getCurrentEditor();
		}
		if (part instanceof IMergeRowHighlightDecorable) {
			IMergeRowHighlightDecorable decorable = (IMergeRowHighlightDecorable) part;
			final String parameterName = EMFUtils.getDescriptorAnnotation(ActivityDictionary.getInstance(), decorable.getRowHighlightDecoratorKey());
			return (parameterName != null);
		}
		return false;
	}
	
	@Override
	protected void partActivated(IWorkbenchPart part) {
		setBaseEnabled(isDecorable(part));
	}
	
	@Override
	protected void pageChanged(PageChangedEvent event) {
		Object selectedPage = event.getSelectedPage();
		if (selectedPage instanceof IWorkbenchPart) {
			setBaseEnabled(isDecorable((IWorkbenchPart) selectedPage));
		}
	}
	
	
	@Override
	public Object execute(ExecutionEvent event) {
		MultiPagePlanEditor editor = getActiveEditor();
		if (editor != null) {
			IEditorPart currentEditor = editor.getCurrentEditor();
			if (currentEditor instanceof IMergeRowHighlightDecorable) {
				String key = ((IMergeRowHighlightDecorable) currentEditor).getRowHighlightDecoratorKey();
				ActivityDictionary activityDictionary = ActivityDictionary.getInstance();
				final String parameterName = EMFUtils.getDescriptorAnnotation(activityDictionary, key);
				if (parameterName != null) {
					final EActivityDef genericActivityDef = activityDictionary.getActivityDef("Activity");
					final EEnum eEnum = getEEnum(parameterName);
					if (eEnum != null) {
						final EPlan plan = editor.getPlan();
						final Date planStartTime = plan.getMember(TemporalMember.class).getStartTime();
						Job job = new Job("One of Each Row Highlight Decorator Value") {
							@Override
							protected IStatus run(IProgressMonitor monitor) {
								
								TransactionUtils.writing(plan, new Runnable() {
									@Override
									public void run() {
										for (EEnumLiteral literal : eEnum.getELiterals()) {
											EActivity activity = PlanFactory.getInstance().createActivity(genericActivityDef);
											activity.setName(literal.getLiteral());
											TemporalMember member = activity.getMember(TemporalMember.class);
											member.setStartTime(planStartTime);
											member.setDuration(AmountUtils.toAmount(3600, SI.SECOND));
											try {
												ADParameterUtils.setParameterObject(activity, parameterName, literal);
												plan.getChildren().add(activity);
											} catch (UndefinedParameterException e) {
												LogUtil.error(e);
											}
										}
									}
								});
								return Status.OK_STATUS;
							}
						};
						job.schedule();
						
					
					}
				}
			}
			
		}
		return null;
	}

	private EEnum getEEnum(String parameterName) {
		EParameterDef def = ActivityDictionary.getInstance().getAttributeDef(parameterName);
		if (def != null) {
			EGenericType eGenericType = def.getEGenericType();
			EClassifier classifier = eGenericType.getEClassifier();
			if (classifier instanceof EEnumImpl) {
				return (EEnum) classifier;
			} 
		}
		return null;
	}

	@Override
	public String getCommandId() {
		return COMMAND_ID;
	}

}
