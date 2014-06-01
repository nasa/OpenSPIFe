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
package gov.nasa.ensemble.core.model.plan.diff.report.action;

import gov.nasa.ensemble.common.debug.EnsembleUsageLogger;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.report.html.table.PlanDiffOutputAsTableHTML;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffEngine;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class CompareToPlanWizard extends Wizard {
	
	private CompareToPlanPage page;

	private static final Logger LOGGER = Logger.getLogger(CompareToPlanWizard.class);
	private static String pluginId = "gov.nasa.ensemble.core.rml3.diff";
	
	@Override
	public void addPages() {
		page = new CompareToPlanPage();
		addPage(page);
	}
	
	@Override
	public boolean canFinish() {
		return (page != null && page.getSelectedFile() != null && page.getSelectedFile().exists());
	}


	@Override
	public boolean performFinish() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final File file = page.getSelectedFile();
		final boolean reverse = page.getReverse();
		final boolean external = page.getExternal();
		final EPlan openPlan = EditorPartUtils.getAdapter(EPlan.class);
		Job job = new Job("Comparing to plan: " + file.getName()) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				EnsembleUsageLogger.logUsage("CompareToPlanPage.readPlan", file.getPath());
				if (openPlan == null) {
					return new Status(IStatus.ERROR, pluginId, "You don't seem to have a plan open.");	
				}
				monitor.beginTask("Comparing plans", 100);
				try {
					final EPlan planReadFromFile = PlanDiffUtils.loadPlanFromFile(file, openPlan.eResource().getResourceSet());
					if (planReadFromFile == null) {
						return new Status(IStatus.ERROR, pluginId, "Unable to read the other plan from that file.");	
					}
					monitor.worked(10);
					EPlan plan1 = reverse? openPlan : planReadFromFile;
					EPlan plan2 = reverse? planReadFromFile : openPlan;

					PlanDiffList differences = PlanDiffEngine.findChanges(plan1, plan2);
					if (differences == null) {
						return new Status(IStatus.ERROR, pluginId, "Sorry, unable to compare plans.");
					}
					if (differences != null) {
						final String title = plan1.getName() + " vs. " + plan2.getName();
						final File outputFile = File.createTempFile("Plan-Comparison", ".html");
						PrintStream out = new PrintStream(new FileOutputStream(outputFile));
//						List<AbstractDiffTree> trees = new ArrayList<AbstractDiffTree>(2);
//						Comparator<PlanDiffNode> originalOrder = new SortCombinedPlan(plan1, plan2, differences);
//						Comparator<PlanDiffNode> alphabeticalOrder = new AlphabeticalOrder();
//						Comparator<PlanDiffNode> chronologicalOrder = new ChronologicalOrder();
//						monitor.worked(5);
//						trees.add(new PlanDiffTreeFlat(differences, chronologicalOrder));
//						monitor.worked(5);
//						trees.add(new PlanDiffTreeTopDown(differences, originalOrder));
//						monitor.worked(5);
//						trees.add(new PlanDiffTreeBottomUp(differences, originalOrder));
//						monitor.worked(5);
//						trees.add(new PlanDiffTreeByName(differences, alphabeticalOrder));
//						monitor.worked(5);
//						new PlanDiffOutputAsTreeHTML().writeToStream(trees,
//								differences,
//								plan1, plan2,
//								out);
						new PlanDiffOutputAsTableHTML().writeToStream(
								differences,
								plan1, plan2,
								getUserProvidedSummary(), out);
						out.close();
						monitor.worked(5);
						WidgetUtils.runInDisplayThread(window.getShell(), new Runnable() {
							public void run() {
								try {
									IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
									IWebBrowser browser = external? support.getExternalBrowser()
											: support.createBrowser(IWorkbenchBrowserSupport.AS_EDITOR, "PlanDiff", "Compare Plans", title);
									java.net.URI uri = outputFile.toURI();
									URL url = uri.toURL();
									browser.openURL(url);
								} catch (IOException e) {
									e.printStackTrace();
								} catch (PartInitException e) {
									e.printStackTrace();
								}
							}});
					}
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LOGGER.error("Failed importing from "+file.getPath(), t);
					String message = "An error occurred:\n" + t.toString();
					return new ExceptionStatus(pluginId, message, t);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		return true;
	}

	protected String getUserProvidedSummary() {
		return page.getSummaryOfChanges();
	}
}
