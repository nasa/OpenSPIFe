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

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ExcelExportWizard extends SpreadsheetExportWizard {
	
	/**
	 * Write one row of an Excel file.
	 * @param out -- stream
	 * @param fields -- printed representation of cell contents
	 */
	private void writeRow(Row row, String[] fields, CellStyle style) {
		int i=0;
		for (String field : fields) {
			Cell cell = row.createCell(i++);
			cell.setCellValue(field);
			if (style != null) {
				cell.setCellStyle(style);
			}
		}
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "/icons/full/wizban/excel.png");
	
	}

	@Override
	protected String getPreferredExtension() {
		return "xls";
	}
	
	@Override
	protected void savePlan(EPlan plan, File destinationFilename) throws Exception {
		boolean generatingResourceSummaryTable = isGeneratingResourceSummaryTable();
		boolean generatingPlanTable = isGeneratingActivityTable();

		Workbook wb = new HSSFWorkbook();
		Sheet planSheet = generatingPlanTable? wb.createSheet("Plan") : null;
		Sheet resourceSheet = generatingResourceSummaryTable? wb.createSheet("Resource Statistics") : null;
		CellStyle headerStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);

		if (generatingPlanTable && planSheet != null) {
			List<String> attributeNames = getActivityColumnHeaders();
			writeRow(planSheet.createRow(0), trimColumnHeaders(attributeNames), headerStyle);

			int rowNum = 1;
			List<RowForOneActivity> allActivitiesColumns = getColumnsForActivitiesToExport(plan, attributeNames);
			for (RowForOneActivity activityColumns : allActivitiesColumns) {
				writeRow(planSheet.createRow(rowNum++), activityColumns.allColumnsAsFormatted, null);
			}
		}
		
		if (generatingResourceSummaryTable && resourceSheet != null) {
			writeRow(resourceSheet.createRow(0), new String[] {getDescription(plan)} , headerStyle);
			Statistic[] statistics = getResourceColumnHeaders();
			writeRow(resourceSheet.createRow(1), mapToStrings(statistics) , headerStyle);

			int rowNum = 2;
			List<String[]> resourcesColumns = getColumnsForResourceStats(plan, statistics);
			for (String[] resourceColumns : resourcesColumns) {
				writeRow(resourceSheet.createRow(rowNum++), resourceColumns, null);
			}
		}
		
		try {
			FileOutputStream out = new FileOutputStream(destinationFilename);
			wb.write(out);
			out.close();
		} catch (IllegalArgumentException iae){
			iae.printStackTrace();
		}
		catch (FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}

}
