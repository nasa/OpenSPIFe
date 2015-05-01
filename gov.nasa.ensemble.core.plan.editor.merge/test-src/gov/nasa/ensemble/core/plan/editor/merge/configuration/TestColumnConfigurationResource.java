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
package gov.nasa.ensemble.core.plan.editor.merge.configuration;

import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferencePage;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.swt.SWT;

public class TestColumnConfigurationResource extends TestCase {

	private AbstractMergeColumn defaultZeroColumn = MergeEditorPreferences.nameMergeColumn;
	private AbstractMergeColumn defaultSortColumn = MergeEditorPreferences.getDefaultSortColumn(MergeEditorPreferencePage.P_MERGE_EDITOR_SORT_COLUMN);
	private List<AbstractMergeColumn> defaultColumns = MergeEditorPreferences.getSelectedColumns();
	
	public void testDefault() throws IOException {
		File tempFile = FileUtilities.createTempFile(getClass().getName(), ".table");
		URI uri = URI.createFileURI(tempFile.getAbsolutePath());
		
		// check defaults
		ColumnConfigurationResource resource = new ColumnConfigurationResource(uri);
		TreeTableColumnConfiguration<AbstractMergeColumn> configuration = resource.getConfiguration();
		AbstractMergeColumn zeroColumn = configuration.getZeroColumn();
		assertSame(defaultZeroColumn, zeroColumn);
		AbstractMergeColumn sortColumn = configuration.getSortColumn();
		assertSame(defaultSortColumn, sortColumn);
		List<? extends AbstractMergeColumn> columns = configuration.getColumns();
		assertSame(configuration.getZeroColumn(), columns.get(0));
		assertSameColumns(defaultColumns, columns);
		for (AbstractMergeColumn column : columns) {
			assertNotNull(column);
		}
		
		// save out the defaults
		resource.save(null);
		
		// load back in the defaults
		ColumnConfigurationResource resource2 = new ColumnConfigurationResource(uri);
		resource2.load(null);
		
		// compare the loaded with what was saved
		TreeTableColumnConfiguration<AbstractMergeColumn> configuration2 = resource2.getConfiguration();
		assertSame(configuration.getZeroColumn(), configuration2.getZeroColumn());
		assertSame(configuration.getSortColumn(), configuration2.getSortColumn());
		List<? extends AbstractMergeColumn> columns2 = configuration2.getColumns();
		assertSame(configuration2.getZeroColumn(), columns2.get(0));
		assertSameColumns(columns, columns2);
		for (AbstractMergeColumn column2 : columns2) {
			assertNotNull(column2);
		}
		
	}

	public void testDatafile() throws IOException {
		// load the test configuration
		URI uri = URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.editor.merge/datafiles/test.table", true);
		ColumnConfigurationResource resource = new ColumnConfigurationResource(uri);
		resource.load(null);
		TreeTableColumnConfiguration<AbstractMergeColumn> configuration = resource.getConfiguration();
		
		// check expected values
		AbstractMergeColumn sortColumn = configuration.getSortColumn();
		assertNotNull(sortColumn);
		assertEquals("Temporal - Duration Sum", sortColumn.getId());
		assertEquals(SWT.UP, configuration.getSortDirection());
		assertSame(MergeEditorPreferences.nameMergeColumn, configuration.getZeroColumn());
		List<? extends AbstractMergeColumn> columns = configuration.getColumns();
		assertEquals(5, columns.size());
		assertSame(configuration.getZeroColumn(), columns.get(0));
		for (AbstractMergeColumn column : columns) {
			assertNotNull(column);
		}
		List<AbstractMergeColumn> expectedColumns = Arrays.asList(
				MergeEditorPreferences.nameMergeColumn,
				MergeEditorPreferences.getColumn("Temporal - Start Time"),
				MergeEditorPreferences.getColumn("Temporal - Duration Sum"),
				MergeEditorPreferences.getColumn("Constraints - Constraints"),
				MergeEditorPreferences.getColumn("Temporal - Scheduled")
				);
		assertSameColumns(expectedColumns, columns);
		for (int i = 0 ; i < columns.size() ; i++) {
			// the test file is set up so that the column widths are the same as the indices
			AbstractMergeColumn column = columns.get(i);
			int columnWidth = configuration.getColumnWidth(column);
			assertEquals(i, columnWidth);
		}
		
		File temp = FileUtilities.createTempFile(getClass().getName() + "-", ".table");
		try {
			URI uri2 = URI.createFileURI(temp.getAbsolutePath());
			ColumnConfigurationResource resource2 = new ColumnConfigurationResource(uri2);
			TreeTableColumnConfiguration<AbstractMergeColumn> configuration2 = resource2.getConfiguration();
			configuration2.setSort(configuration.getSortColumn(), configuration.getSortDirection());
			configuration2.setColumns(new ArrayList<AbstractMergeColumn>(configuration.getColumns()));
			for (AbstractMergeColumn column : columns) {
				configuration2.resizeColumn(column, configuration.getColumnWidth(column));
			}
			resource2.save(null);
			
			assertEqualContents(uri, uri2);
		} finally {
			temp.delete();
		}
		
	}

	/**
	 * Does a comparison of the contents of the two URIs,
	 * ignoring comments in the contents.  Will compare line-by-line.
	 * 
	 * @param uri
	 * @param uri2
	 * @throws IOException
	 */
	private void assertEqualContents(URI uri, URI uri2) throws IOException {
		URIConverter converter = new ExtensibleURIConverterImpl();
		InputStream inputStream1 = converter.createInputStream(uri);
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));
		InputStream inputStream2 = converter.createInputStream(uri2);
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2));
		while (true) {
			String line1 = readUncommentedLine(reader1);
			String line2 = readUncommentedLine(reader2);
			assertEquals(line1, line2);
			if (line1 == null) {
				break;
			}
		}
	}
	
	/**
	 * Read the next line that doesn't start with a comment character ('#')
	 * Returns null at the end of the reader.
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private String readUncommentedLine(BufferedReader reader) throws IOException {
		String line;
		do {
			line = reader.readLine();
		} while ((line != null) && (line.startsWith("#")));
		return line;
	}

	/**
	 * Compare two column lists for size and to ensure they have the same
	 * columns in the same order. 
	 * 
	 * @param expectedColumns
	 * @param columns
	 */
	private void assertSameColumns(List<? extends AbstractMergeColumn> expectedColumns, List<? extends AbstractMergeColumn> columns) {
		assertEquals(expectedColumns.size(), columns.size());
		for (int i = 0 ; i < expectedColumns.size() ; i++) {
			AbstractMergeColumn column = expectedColumns.get(i);
			AbstractMergeColumn column2 = columns.get(i);
			assertSame(column, column2);
		}
	}

}
