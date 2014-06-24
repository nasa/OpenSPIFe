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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.report.html.table.PlanDiffOutputAsTableHTML;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffEngine;
import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.AlphabeticalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.ChronologicalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeBottomUp;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeByName;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeFlat;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeTopDown;
import gov.nasa.ensemble.core.model.plan.diff.trees.SortCombinedPlan;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/** This used by Eclipse's built-in Compare With Each Other command when two plans are selected.
 * It's no longer used by Score, which hides the built-in command and implements its own by the same name.
 * That opens the browser with ScorePlanDiffUtil.generateAndOpenPlanDiffReport.  */
public class PlanDiffBrowserViewer extends Viewer {

	EPlan earlierPlan;
	EPlan laterPlan;

	Browser browser = null;
	private Composite parent;
	private static boolean tryMozilla = false;

	public PlanDiffBrowserViewer(Composite parent) {
		this.parent = parent;
	}

	@Override
	public Control getControl() {
		return getBrowser();
	}

	private Browser getBrowser() {
		if (browser == null) {
			if (tryMozilla) {
				try {
					browser = new Browser(parent, SWT.MOZILLA);
				} catch (Exception e) {
					LogUtil.error("Can't use Mozilla", e);
				}
			}
			if (browser == null) {
				browser = new Browser(parent, SWT.NONE);
			}
		}
		return browser;
	}

	@Override
	public Object getInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISelection getSelection() {
		return null;
	}

	@Override
	public void refresh() {
		getBrowser().refresh();
	}

	@Override
	public void setInput(Object newInput) {
		earlierPlan = null;
		laterPlan = null;
		if (newInput instanceof ICompareInput) {
			ICompareInput input = (ICompareInput) newInput;
			ITypedElement left = input.getLeft();
			ITypedElement right = input.getRight();
			try {
				boolean leftOlderThanRight = isOlder(left, right);
				earlierPlan = loadOnePlan(leftOlderThanRight? left : right);
				laterPlan   = loadOnePlan(leftOlderThanRight? right : left);
				PlanDiffList differenceInfo = computeDiffInfo ();
				final File outputFile = File.createTempFile("Plan-Comparison", ".html");
				PrintStream out = new PrintStream(new FileOutputStream(outputFile));
//				new PlanDiffOutputAsTreeHTML().writeToStream(computeDiffTrees(differenceInfo),
//						differenceInfo,
//						earlierPlan, laterPlan, out);
				new PlanDiffOutputAsTableHTML().writeToStream(
						differenceInfo, earlierPlan, laterPlan, null, out);
				out.close();
				browser.setUrl(outputFile.toURI().toURL().toExternalForm());
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(parent.getShell(),
						"Error while comparing plans",
						"Sorry, an error occurred while comparing plans:\n" + e);
				OutputStream stream = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(stream));
				browser.setText("<h1>Plan Diff Error</h1><pre>"
						+ stream.toString() + "</pre>");
			}
		}
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		// No-op
	}

	public void setURL(String url) {
		getBrowser().setUrl(url);
	}

	private PlanDiffList computeDiffInfo() {
		PlanDiffList differences = PlanDiffEngine.findChanges(earlierPlan, laterPlan);
		if (differences == null) {
			throw new IllegalStateException("Sorry, unable to compare plans.");
		}
		return differences;
	}

	@SuppressWarnings("unused")
	private List<AbstractDiffTree> computeDiffTrees(
			PlanDiffList differenceInfo) throws Exception {
		List<AbstractDiffTree> trees = new ArrayList<AbstractDiffTree>(4);
		Comparator<PlanDiffNode> originalOrder = new SortCombinedPlan(earlierPlan,
				laterPlan, differenceInfo);
		Comparator<PlanDiffNode> alphabeticalOrder = new AlphabeticalOrder();
		Comparator<PlanDiffNode> chronologicalOrder = new ChronologicalOrder();
		trees.add(new PlanDiffTreeFlat(differenceInfo, chronologicalOrder));
		trees.add(new PlanDiffTreeTopDown(differenceInfo, originalOrder));
		trees.add(new PlanDiffTreeBottomUp(differenceInfo, originalOrder));
		trees.add(new PlanDiffTreeByName(differenceInfo, alphabeticalOrder));
		return trees;
	}
	
	private EPlan loadOnePlan(ITypedElement resourceOrFileElement) throws IOException, CoreException {
		if (resourceOrFileElement instanceof IResourceProvider) {
			return EPlanUtils.loadOnePlan(((IResourceProvider) resourceOrFileElement).getResource());
		}
		else if (resourceOrFileElement instanceof IStreamContentAccessor) { // e.g. a FileRevisionTypedElement
			return EPlanUtils.loadOnePlan(((IStreamContentAccessor) resourceOrFileElement).getContents());
		}
		else {
			throw new IOException("Don't know how to load a plan from a " + resourceOrFileElement.getClass().getName() + ".");
		}
	}
	
	/**
	 * Finds the earlier/older of two resources.
	 * Non-resources like FileRevisionTypedElement (Local History) are considered
	 * older than resources -- history is older than the present.
	 * @return false iff right is same age or older than left
	 */
	private boolean isOlder(ITypedElement left, ITypedElement right) {
	    long leftTimestamp = IResource.NULL_STAMP;
	    long rightTimestamp = IResource.NULL_STAMP;
	    
		try {
			if (left instanceof IResourceProvider) {
				leftTimestamp = (((IResourceProvider) left).getResource().getLocalTimeStamp());
			}
			if (right instanceof IResourceProvider) {
				rightTimestamp = (((IResourceProvider) right).getResource().getLocalTimeStamp());
			}
		} catch (Exception e) {
			LogUtil.error("Resource timestamp comparison error: " + e);
			return true;
		}
		if (rightTimestamp==IResource.NULL_STAMP) return false;
		if (leftTimestamp==IResource.NULL_STAMP) return true;
		return leftTimestamp < rightTimestamp;
	}

}
