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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.ProfileContributor;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.editor.lifecycle.EMFPlanSelectionExportWizard;
import gov.nasa.ensemble.core.plan.editor.lifecycle.FileSelectionPage;
import gov.nasa.ensemble.core.plan.editor.lifecycle.TimestampedEMFPlanSelectionExportWizardPage;
import gov.nasa.ensemble.core.plan.editor.lifecycle.TimestampedFileSelectionPage;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ColumnsPicklistWizardPage;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ResourceColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.StringPicklistWizardPage;
import gov.nasa.ensemble.core.plan.editor.merge.configuration.ColumnConfigurationResource;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.jscience.physics.amount.Amount;

public abstract class SpreadsheetExportWizard extends EMFPlanSelectionExportWizard {

	public enum ReportType { activity, resource }
	public enum Statistic {
		Resource_Name, Units, Min, Max, Mean, Standard_Deviation
	}
	protected class RowForOneActivity {
		EActivity activity;
		String[] allColumnsAsFormatted;
		public RowForOneActivity(EActivity activity, String[] allColumnsAsFormatted) {
			this.activity = activity;
			this.allColumnsAsFormatted = allColumnsAsFormatted;
		}
	}

	protected ColumnsPicklistWizardPage attributeColumnsPage;
	protected StringPicklistWizardPage resourceRowsPage;
	
	protected static final String[] OF_STRINGS = new String[0];

	private static final List<String> ACTIVITY_REPORT_ID_LIST =
			EnsembleProperties.getStringListPropertyValue(getReportMetaPropertyName(ReportType.activity, "list"), null);
	private static final List<String> RESOURCE_REPORT_ID_LIST =
			EnsembleProperties.getStringListPropertyValue(getReportMetaPropertyName(ReportType.resource, "list"), null);
	private static boolean ACTIVITY_REPORTS_PREDEFINED = ACTIVITY_REPORT_ID_LIST != null;
	private static boolean RESOURCE_REPORTS_PREDEFINED = RESOURCE_REPORT_ID_LIST != null;

	private static final String PREFERENCE_NAME_FOR_SELECTED_ATTRIBUTE_COLUMNS = "export.csv.attributes";
	private static final String PREFERENCE_NAME_FOR_SELECTED_RESOURCE_ROWS = "export.csv.attributes";
	
	@SuppressWarnings("unchecked")
	public static String getDisplayString(EAttribute attribute, Object value) {
		String string;
		try {
			IStringifier stringifier = EMFUtils.getStringifier(attribute);
			string = stringifier.getDisplayString(value);
		}
		catch (Exception e) {
			string = "[Error displaying value: " + e.getMessage() + "]";
		}
		if (string==null) return "";
		if ("".equals(string)) return "";
		return string;
	}

	@SuppressWarnings("deprecation")
	protected String[] getColumnsForActivity(EActivity activity, String[] attributeNames) {
		String[] result = new String[attributeNames.length];
		for (int i = 0; i < attributeNames.length; i++) {
			if (attributeNames[i].equalsIgnoreCase("Duration")){
				result[i] = new Integer(convertDurationToSeconds(getAttributeValue(activity, attributeNames[i]))).toString();
			} else if (attributeNames[i].equalsIgnoreCase("Start Time") || attributeNames[i].equalsIgnoreCase("End Time")){
				result[i] = convertGMTToStandardDate(getAttributeValue(activity, attributeNames[i])).toGMTString(); 
			} else {
				result[i] = getAttributeValue(activity, attributeNames[i]);
			}
		}
		return result;
	}
	
	/** @return List of activity rows, each row being a list of cell content strings. */
	protected List<RowForOneActivity> getColumnsForActivitiesToExport(EPlan plan, List<String> attributeNames) {
		final TreeTableColumnConfiguration<AbstractMergeColumn> configuration = getTableColumnConfiguration();
		//get columns to export per activity
		List<EActivity> activitiesToExport = getActivitySelectionPage().getActivitiesToExport();
		List<RowForOneActivity> reportContents = new ArrayList();
		String[] namesArray = attributeNames.toArray(OF_STRINGS);
		for (int i = activitiesToExport.size()-1 ; i >= 0; i--) {
			EActivity activity = activitiesToExport.remove(i);
			String[] values = getColumnsForActivity(activity, namesArray);
			reportContents.add(new RowForOneActivity(activity, values));
		}
		if (configuration != null) {
			// Sort the same way as the editor is currently set to do
			final ITreeTableColumn<Object> sortColumn = configuration.getSortColumn();
			if (sortColumn != null) {
				final Comparator<Object> editorComparator = sortColumn.getComparator();
				Comparator<RowForOneActivity> reportComparator = new Comparator<RowForOneActivity>() {
					@Override
					public int compare(RowForOneActivity row1, RowForOneActivity row2) {
						return editorComparator.compare(
								sortColumn.getFacet(row1.activity),
								sortColumn.getFacet(row2.activity));
					}
				};
				// and in the same direction:  ascending or descending
				if (configuration.getSortDirection()==SWT.UP) {
					reportComparator = Collections.reverseOrder(reportComparator);
				}
				Collections.sort(reportContents, reportComparator);
			}
		}
		return reportContents;
	}
	
	protected TreeTableColumnConfiguration<AbstractMergeColumn> getTableColumnConfiguration() {
		ColumnConfigurationResource resource = ColumnsPicklistWizardPage.getTableColumnConfigurationResource(this.getPlan());
		return (resource != null ? resource.getConfiguration() : null);
	}
	
	protected String[] trimColumnHeaders(List<String> untrimmedNames) {
		String[] result = new String[untrimmedNames.size()];
		int i = 0;
		for (String untrimmed : untrimmedNames) {
			result[i++] = trimColumnHeader(untrimmed);
		}
		return result;
	}
	
	protected String trimColumnHeader(String untrimmedName) {
		if (untrimmedName.indexOf('-') > -1)
			return untrimmedName.replaceFirst("(\\w+ - )", "");
		else return untrimmedName;
	}

	private String getAttributeValue(EActivity activity, String fullColumnName) {
		//TODO:  What's the efficient way to look up one parameter by name?  Call getAllEAttributes instead of getting strings?
		if (fullColumnName.equalsIgnoreCase("Name")) return activity.getName();
		if (fullColumnName.equalsIgnoreCase("Activity Type")) return activity.getType();
		if (fullColumnName.equalsIgnoreCase("Subsystem")) {
			if (activity.getData()==null) return "(No definition)";
			else return (String) activity.getData().eClass().eGet(DictionaryPackage.Literals.EACTIVITY_DEF__CATEGORY);
		}
		
		Class columnClass = attributeColumnsPage.getPickType(fullColumnName);
		if (columnClass==null) throw new IllegalStateException("Column not found:  " + fullColumnName);
		String columnName = trimColumnHeader(fullColumnName).replaceAll(" ", "");
		
		if (ParameterColumn.class == columnClass) return getParameterValue(activity, columnName);
		if (ResourceColumn.class.isAssignableFrom(columnClass)) return getNumericResourceValueForActivity(activity, columnName);
		AbstractMergeColumn column = attributeColumnsPage.getPickObject(fullColumnName);
		
		if (column != null) return column.getText(column.getFacet(activity));		
		
		return "???";
	}
	
	private String getNumericResourceValueForActivity(EActivity activity, String columnName) {
		for (ENumericResourceDef resourceDef : getNumericResourceDefs()) {
			if (resourceDef.getName().equalsIgnoreCase(columnName)) {
				ComputableAmount value = ADEffectUtils.getEffectAmount(activity, resourceDef);
				if (value==null) return "";
				if (value.getAmount()==null) return "";
				return value.getAmount().toString();
			}
		}
		return "";
	}
	
	private HashMap<EAttribute, String> cachedNameMatchResults = new HashMap<EAttribute,String>();

	private List<ProfileContributor> profileContributors = ClassRegistry.createInstances(ProfileContributor.class);

	private String defaultDirectory = System.getProperty("user.home");
	
	private boolean nameMatch(EAttribute attribute, String columnName) {
		if (cachedNameMatchResults.containsKey(attribute)) {
			String cachedName = cachedNameMatchResults.get(attribute);
			return cachedName==columnName || cachedName.equals(columnName);
		}
		String attributeName1 = attribute.getName().replaceAll(" ", "");
		if (attributeName1 != null && attributeName1.equalsIgnoreCase(columnName)) {
			cachedNameMatchResults.put(attribute, columnName);
			return true;
		}
		String attributeName2 = ParameterDescriptor.getInstance().getDisplayName(attribute).replaceAll(" ", "");
		if (attributeName2 != null && attributeName2.equalsIgnoreCase(columnName)) {
			cachedNameMatchResults.put(attribute, columnName);
			return true;
		}
		return false;
	}

	private String getParameterValue(EActivity activity, String columnName) {		
		//   a. Common attributes:
		for (EMember member : activity.getMembers()) {
			for (EAttribute attribute : member.eClass().getEAllAttributes()) {
				if (nameMatch(attribute, columnName)) {
					return getDisplayString(attribute, member.eGet(attribute));
				}
			}
		}
		//   b. Activity-type-specific arguments:
		EObject activityDef = activity.getData();
		if (activityDef != null) { // null in some JUnit tests
			for (EAttribute attribute : activityDef.eClass().getEAllAttributes()) {
				if (nameMatch(attribute, columnName)) {
					return getDisplayString(attribute, activityDef.eGet(attribute));
				}
			}
			
			String objectText = "";
			for (EReference reference : activityDef.eClass().getEReferences()) {
				if (reference.getName().equalsIgnoreCase(columnName)) {
					List<EObject> list = EMFUtils.eGetAsList(activityDef, reference);
					for (EObject object : list) {
						IItemLabelProvider lp = EMFUtils.adapt(object, IItemLabelProvider.class);
						if (objectText == "")
							objectText = lp.getText(object);
						else 
							objectText += ", " + lp.getText(object);
					}
					return objectText;
				}
			}
		}
		return "";
	}

	public SpreadsheetExportWizard() {
		super();
	}

	protected TimestampedEMFPlanSelectionExportWizardPage getActivitySelectionPage() {
		for (IWizardPage page : getPages()) {
			if (page instanceof TimestampedEMFPlanSelectionExportWizardPage) {
				return (TimestampedEMFPlanSelectionExportWizardPage) page;
			}
		}
		LogUtil.error("Can't find activity selection page that should have been created.");
		return null; // shouldn't happen
	}
	
	protected TimestampedFileSelectionPage getFileSelectionPage() {
		return ((TimestampedFileSelectionPage) fileSelectionPage);
	}

	@Override
	protected void addPages(EPlan plan) {
		boolean generatingPlanTable = isGeneratingActivityTable();
		boolean generatingResourceSummaryTable = isGeneratingResourceSummaryTable();
		if (ACTIVITY_REPORTS_PREDEFINED && generatingPlanTable) {
			addPage(new ReportSelectionPage(ReportType.activity, ACTIVITY_REPORT_ID_LIST));			
		}
		if (RESOURCE_REPORTS_PREDEFINED && generatingResourceSummaryTable) {
			addPage(new ReportSelectionPage(ReportType.resource, RESOURCE_REPORT_ID_LIST));			
		}
		fileSelectionPage = createFileSelectionPage();
		addPage(fileSelectionPage);
		if (generatingPlanTable) {
			ColumnConfigurationResource configurationResource = ColumnsPicklistWizardPage.getTableColumnConfigurationResource(this.getPlan());
			attributeColumnsPage = new ColumnsPicklistWizardPage("Columns", configurationResource);
			attributeColumnsPage.setPreferenceName(PREFERENCE_NAME_FOR_SELECTED_ATTRIBUTE_COLUMNS);
			addPage(attributeColumnsPage);
		}
		if (generatingResourceSummaryTable) {
			resourceRowsPage = new StringPicklistWizardPage(getAllResourceNames(plan), "Resources");
			resourceRowsPage.setPreferenceName(PREFERENCE_NAME_FOR_SELECTED_RESOURCE_ROWS);
			addPage(resourceRowsPage);
		}
		if (!ACTIVITY_REPORTS_PREDEFINED) {
			initializePageContentsIfNoReportTypes(plan);
		}
	}
	
	protected void initializePageContentsIfNoReportTypes(EPlan plan) {
		// Old behavior if not configured.  Name after plan and timestamp.
		getFileSelectionPage().setDefaultFilePath(plan, getDialogSettings());
		if (attributeColumnsPage != null) { // N/A when only generating resource summary
			// SPF-8577:  Commented out filtering because it excluded DateFormatTimepointParameterColumn,
			// among others.
			// attributesPage.filterChoicesByType(ParameterColumn.class, ResourceColumn.class);
			attributeColumnsPage.setSpecialChoices("Name", "Activity Type", "Subsystem");
			attributeColumnsPage.setDescription("Select activity attributes to export as columns.");
		}
		if (resourceRowsPage != null) { // N/A when only generating plan activity attributes
			resourceRowsPage.setDescription("Select resources to summarize as rows.");
		}
	}

	protected void updatePageContentsAfterReportTypeChanged(ReportType reportType, String newReportId) {
		// Predefined reports introduced by SPF-8876
		String filename = EnsembleProperties.getStringPropertyValue(getReportPropertyName(reportType, newReportId, "filename"), null);
		if (filename != null) {
			if (filename.indexOf(File.separatorChar)==-1) {
				// Unless a directory is configured, default to the user's home directory.
				filename = defaultDirectory + File.separator + filename;
				if (isGeneratingResourceSummaryTable() && !isGeneratingActivityTable()) {
					filename +=  "-resource-summary";
				}
			}
			filename +=  "." + getPreferredExtension();
			getFileSelectionPage().setCurrentFile(new File(filename));
		}
		switch (reportType) {
		case activity:
			if (attributeColumnsPage != null) { // N/A when only generating resource summary
				attributeColumnsPage.setPreferenceName(getReportPropertyName(reportType, newReportId, "attributes"));
				attributeColumnsPage.setDescription("On this page, you may customize which activity attributes to export as columns.");
			}
			break;
		case resource:
			if (resourceRowsPage != null) { // N/A when only generating resource summary
				resourceRowsPage.setPreferenceName(getReportPropertyName(reportType, newReportId, "resources"));
				resourceRowsPage.setDescription("On this page, you may customize which activity attributes to export as columns.");
			}
		}		
		}

	private static String getReportMetaPropertyName(ReportType reportType, String subproperty) {
		return "spreadsheet.predefined." + reportType.toString() + ".report." + subproperty;
	}

	private static String getReportPropertyName(ReportType reportType, String reportId, String subproperty) {
		return getReportTypePrefix(reportType)  + "." + reportId + "." + subproperty;
	}

	private static String getReportTypePrefix(ReportType reportType) {
		// Should not be null if called
		return EnsembleProperties.getStringPropertyValue(getReportMetaPropertyName(reportType, "prefix"), null);
	}

	public Date convertGMTToStandardDate(String gmtString) {
		// FIXME:  May be inefficient.  This may return EarthTimeFlexibleFormat, which is designed for human input
		// and tries a zillion combinations.  If we know this is stored in, say, ISO8601,
		// we should use that specific format if this is called a lot of times.
	    IStringifier<Date> stringifier = StringifierRegistry.getStringifier(Date.class);
	    Date retDate = new Date();
	    try {
	    	retDate = stringifier.getJavaObject(gmtString, new Date(System.currentTimeMillis()));
	    } catch (ParseException e1) {
	    LogUtil.error(e1);
	    }
	    
	    return retDate;
	}

	public int convertDurationToSeconds(String duration) {
		//TODO:  Consider using new DurationFormat().parseFormattedDuration(duration)
		String[] timePart = duration.replace('/', ':').split(":");
		int hour = 0;
		int minute = 0;
		int second = 0;
		
		if (timePart.length >= 1){
			hour = Integer.parseInt(timePart[0]);
		}
		if (timePart.length >= 2){
			minute = Integer.parseInt(timePart[1]);
		}
		if (timePart.length == 3){
			second = Integer.parseInt(timePart[2]);
		}
			
		return hour * 60 * 60 + minute * 60 + second;
	}

	@Override
	protected FileSelectionPage createFileSelectionPage() {
		fileSelectionPage = new TimestampedEMFPlanSelectionExportWizardPage(SWT.SAVE, getPlan()) {
			@Override
			protected boolean requirePreferredExtension() {
			  return true;
			}
		};
		if (getPreferredExtension() != null) {
			fileSelectionPage.setPreferredExtensions(getPreferredExtension());
		}
	
		return fileSelectionPage;
	}

	@Override
	public boolean performFinish() {
		Date start = getActivitySelectionPage().getStartBound();
		Date end = getActivitySelectionPage().getEndBound();
		PlanEditorUtil.setPlanExportExtentPropertyValue(getPlan(), new TemporalExtent(start, end));
		return super.performFinish();
	}
	
	//****** RESOURCE STATISTICS SUMMARY ******/
	
	/** @return List of resource rows, each row being a list of cell content strings. */
	protected List<String[]> getColumnsForResourceStats(EPlan plan, Statistic[] columnHeaders) {
		Date start = getEffectiveSelectedStart(plan);
		Date end = getEffectiveSelectedEnd(plan);
		String[] resourceNames = getSelectedResourceNames(plan);
		if (resourceNames==null) return Collections.EMPTY_LIST;
		List<String[]> result = new ArrayList<String[]>(resourceNames.length);
		for (String resourceName : resourceNames) {
			String[] row = new String[columnHeaders.length];
			Profile wholeProfile = getResourceProfile(plan, resourceName);
			Profile profile = wholeProfile==null? null : ProfileUtil.trimmedSubset(wholeProfile, start, end);
			for (Statistic stat : columnHeaders) {
				int i = stat.ordinal();
				switch (stat) {
				case Resource_Name:
					row[i] = resourceName;
					break;
				case Units:
					row[i] = wholeProfile==null? "" :wholeProfile.getUnits().toString();
					break;
				default: 
					row[i] = profile==null?
							"--" 
							: 
								computeStatistic(profile, stat);
				}
			}
			result.add(row);
		}
		return result;
	}
	
	protected String getDescription(EPlan plan) {
		IStringifier<Date> stringifier = StringifierRegistry.getStringifier(Date.class);
		return "Resource summary from " + stringifier.getDisplayString(getEffectiveSelectedStart(plan))
				 + " to " + stringifier.getDisplayString(getEffectiveSelectedEnd(plan));
	}
	
	private Date getEffectiveSelectedStart(EPlan plan) {
		Date start;
		TimestampedEMFPlanSelectionExportWizardPage activitySelectionPage = getActivitySelectionPage();
		if (activitySelectionPage.isAllSelected()) {
			return plan.getMember(TemporalMember.class).getStartTime();
		} else if (activitySelectionPage.isRangeSelected()) {
			start = activitySelectionPage.getStartBound();
		} else {
			start = plan.getMember(TemporalMember.class).getEndTime(); // sic -- upper bound
			for (EActivity activity : activitySelectionPage.getActivitiesToExport()) {
				start = DateUtils.earliest(start, activity.getMember(TemporalMember.class).getStartTime());
			}
		}
		return start;
		
	}
	private Date getEffectiveSelectedEnd(EPlan plan) {
		Date end;
		TimestampedEMFPlanSelectionExportWizardPage activitySelectionPage = getActivitySelectionPage();
		if (activitySelectionPage.isAllSelected()) {
			return plan.getMember(TemporalMember.class).getEndTime();
		} else if (activitySelectionPage.isRangeSelected()) {
			end = activitySelectionPage.getEndBound();
		} else {
			end = plan.getMember(TemporalMember.class).getStartTime(); // sic -- lower bound
			for (EActivity activity : activitySelectionPage.getActivitiesToExport()) {
				end   = DateUtils.latest(end, activity.getMember(TemporalMember.class).getEndTime());
			}
		}
		return end;
	}

	@SuppressWarnings("unchecked")
	private String computeStatistic(Profile profile, Statistic stat) {
		if (ProfileUtil.isNumeric(profile)) {
			switch (stat) {
			case Min: return formatAmount(ProfileUtil.getMin(profile));
			case Max: return formatAmount(ProfileUtil.getMax(profile));
			case Mean: return formatAmount(ProfileUtil.getMean(profile));
			case Standard_Deviation: return formatAmount(ProfileUtil.getStandardDeviation(profile));
			default: throw new IllegalArgumentException();
			}
		} else {
			if ( profile.getDataPoints().isEmpty()) {
				return "None";
			}
			SortedSet<String> allValues = new TreeSet();
			for (Object datapoint : profile.getDataPoints()) {
				if (datapoint instanceof DataPoint) {
					Object value = ((DataPoint) datapoint).getValue();
					if (value != null) {
						allValues.add(value.toString());
					}
				} else {
					allValues.add(datapoint.toString());
				}
			}
			if (allValues.isEmpty()) return "none";
			switch (stat) {
			case Min: return allValues.first();
			case Max: return allValues.last();
			case Mean: return "";
			case Standard_Deviation: return Integer.toString(allValues.size())
					
					;
			default: return "--";
			}
		}
	}

	private String formatAmount(Amount amount) {
		if (amount==null) return "";
		return Double.toString(amount.getEstimatedValue());
	}

	protected Statistic[] getResourceColumnHeaders() {
		return Statistic.values();
	}
	
	protected String[] mapToStrings(Enum[] enumeratedValues) {
		String[] result = new String[enumeratedValues.length];
		for (int i=0; i < result.length; i++) {
			result[i] = enumeratedValues[i].name().replace('_', ' ');
		}
		return result;
	}

	private List<ENumericResourceDef> getNumericResourceDefs() {
		return ActivityDictionary.getInstance().getDefinitions(ENumericResourceDef.class);
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFinish() {
		if (isGeneratingActivityTable()) {
			if (attributeColumnsPage==null) return false;
			String[] selected = attributeColumnsPage.getSelectedPickNames();
			if (selected == null || selected.length < 1) return false;
		}
		if (isGeneratingResourceSummaryTable()) {
			if (resourceRowsPage==null) return false;
			String[] selected = resourceRowsPage.getSelectedPickNames();
			if (selected == null || selected.length < 1) return false;
		}
		return super.canFinish();
	}

	private Collection<String> getAllResourceNames(EPlan plan) {
		SortedSet<String> result = new TreeSet<String>();
		if (profileContributors==null) {
			return null;
		} else {
			for (ProfileContributor contributor : profileContributors) {
				result.addAll(contributor.getAllProfileNames(plan));
			}
		}
		return result;
	}
	
	private String[] getSelectedResourceNames(EPlan plan) {
		return resourceRowsPage.getSelectedPickNames();
	}
	
	private Profile getResourceProfile(EPlan plan, String profileKey) {
		if (profileContributors==null) {
			return null;
		} else {
			for (ProfileContributor contributor : profileContributors) {
				Profile profile = contributor.getProfile(plan, profileKey);
				if (profile != null && !profile.getDataPoints().isEmpty()) {
					return profile;
				}
			}
		}
		return null;
	}

	protected List<String> getActivityColumnHeaders() {
		return Arrays.asList(attributeColumnsPage.getSelectedPickNames());
	}

	protected boolean isGeneratingActivityTable() {
		return true;
	}

	protected boolean isGeneratingResourceSummaryTable() {
		return true;
	}
	private static Map<String,String> getReportSelectionPageNameMap(ReportType reportType, List<String> idList) {
		Map<String, String> nameMap = new HashMap<String, String>(idList.size());
		for (String id : idList) {
			nameMap.put(id,  EnsembleProperties.getStringPropertyValue(getReportPropertyName(reportType, id, "name"), id));
		}
		return nameMap;
	}

	private class ReportSelectionPage extends MultipleChoiceSelectionPage<String> {
		ReportSelectionPage(final ReportType reportType, List<String> idList) {
			super("Select a predefined report or press Next to customize.",
					idList, getReportSelectionPageNameMap(reportType, idList));
			addPropertyChangeListener(new IPropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					updatePageContentsAfterReportTypeChanged(reportType, (String) event.getNewValue());
					getContainer().updateButtons();
				}
			});
		}
	}

}
