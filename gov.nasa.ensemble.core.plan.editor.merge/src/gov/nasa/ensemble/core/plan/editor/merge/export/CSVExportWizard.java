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
package gov.nasa.ensemble.core.plan.editor.merge.export;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.lifecycle.FileSelectionPage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public class CSVExportWizard extends SpreadsheetExportWizard {

	private static final String CRLF = new String(new char[] {(char) 0x0D, (char) 0x0A});
	private static final char DOUBLE_QUOTE = '"';
	private static final String UNESCAPED_DOUBLE_QUOTE = Character.toString(DOUBLE_QUOTE);
	private static final String ESCAPED_DOUBLE_QUOTE
			= UNESCAPED_DOUBLE_QUOTE + UNESCAPED_DOUBLE_QUOTE;

	// CSV doesn't support two sheets.  If this is split into two different subclasses,
	// we only need one at a time, but originally we needed two.
	private boolean needTwoFiles = isGeneratingActivityTable() && isGeneratingResourceSummaryTable();
	private FileSelectionPage summaryFilePage;
	
	/**
	 * Write one row of a CSV file.
	 * @param out -- stream
	 * @param fields -- printed representation of cell contents
	 * @see RFC-4180
	 */
	private void writeRow(PrintStream out, String[] fields) {
		boolean first = true;

		for (String field : fields) {
			if (!first) out.append(',');
			first = false;
			out.append(DOUBLE_QUOTE);
			if (field==null) {
				out.append("---");
			} else {
				out.append(field.replaceAll(UNESCAPED_DOUBLE_QUOTE, ESCAPED_DOUBLE_QUOTE));
			}
			out.append(DOUBLE_QUOTE);
		}
		out.append(CRLF);
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "/icons/full/wizban/csv.png");
	}

	@Override
	protected String getPreferredExtension() {
		return "csv";
	}

	@Override
	protected void savePlan(EPlan plan, File file) throws Exception {
		if (needTwoFiles) {
			savePlan(plan, makeStream(file));
			saveSummary(plan, makeStream(summaryFilePage.getSelectedFile()));
		} else if (isGeneratingActivityTable()) {
			savePlan(plan, makeStream(file));
		} else if (isGeneratingResourceSummaryTable()) {
			saveSummary(plan, makeStream(file));
		} else {
			LogUtil.error("Logical error in this code.");
		}
	}

	private PrintStream makeStream(File file)
			throws UnsupportedEncodingException, FileNotFoundException {
		return new PrintStream(new BufferedOutputStream(new FileOutputStream(file)), true, "UTF-8");
	}
	
	protected void savePlan(EPlan plan, PrintStream out) {	
		List<String> attributeNames = getActivityColumnHeaders();
		writeRow(out, trimColumnHeaders(attributeNames));
		List<RowForOneActivity> allActivitiesColumns = getColumnsForActivitiesToExport(plan, attributeNames);
		for (RowForOneActivity activityColumns : allActivitiesColumns) {
			writeRow(out, activityColumns.allColumnsAsFormatted);
		}
	}

	private void saveSummary(EPlan plan, PrintStream out) {
		Statistic[] columnHeaders = getResourceColumnHeaders();
		writeRow(out, mapToStrings(columnHeaders));
		List<String[]> allResourceColumns = getColumnsForResourceStats(plan, columnHeaders);
		for (String[] resourceColumns : allResourceColumns) {
			writeRow(out, resourceColumns);
		}
	}

	@Override
	protected void addPages(EPlan plan) {
		super.addPages(plan);
		if (needTwoFiles) {
			summaryFilePage = createSummaryFileSelectionPage();
			summaryFilePage.setTitle("Resource summary file");
			summaryFilePage.setMessage("CSV only supports one sheet per file.  Please specify where the resource summary sheet should go.");
			addPage(summaryFilePage);
		}
	}
	
	
	@Override
	protected void updatePageContentsAfterReportTypeChanged(ReportType reportType, String newReportId) {
		super.updatePageContentsAfterReportTypeChanged(reportType, newReportId);
		if (needTwoFiles) {
			summaryFilePage.setCurrentFile(baseSheet2FilenameOnSheet1Filename());
		}
	}

	protected FileSelectionPage createSummaryFileSelectionPage() {
		FileSelectionPage newPage = new FileSelectionPage(SWT.SAVE) {
			@Override
			protected boolean requirePreferredExtension() {
			  return true;
			}

			@Override
			public void setVisible(boolean visible) {
				// Each time we flip to this page, we update the default.
				if (visible) {
					 setCurrentFile(baseSheet2FilenameOnSheet1Filename());
				}
				super.setVisible(visible);
			}
		};
		if (getPreferredExtension() != null) {
			newPage.setPreferredExtensions(getPreferredExtension());
		}
		return newPage;
	}

	protected File baseSheet2FilenameOnSheet1Filename() {
		File sheet1File = fileSelectionPage.getSelectedFile();
		String sheet1Name = sheet1File.getName().replace(".csv", "");
		return new File(sheet1File.getParentFile(), sheet1Name + "_resource.csv");
	}

}
