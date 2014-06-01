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
package gov.nasa.arc.spife.europa;

import gov.nasa.arc.spife.europa.clientside.EuropaCommand;
import gov.nasa.arc.spife.europa.clientside.EuropaServerMonitor;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.ui.SWTUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.ERule;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * This thread allows for the asyncronous initialization of europa
 * from the model.  Please note that there is an opportunity for a
 * bug to enter here because the model could be changed while
 * initialization is still proceeding.  Because initialize is 
 * synchronized on the the europa instance, and europa updaters
 * also synchronize on the europa instance, this means that updates
 * will block until initialization is complete.  Some of the updates
 * may already have been sent to europa as part of initialization.
 * These redundant updates are _probably_ harmless.
 * 
 * @author Andrew
 * 
 */
class EuropaMemberInitializationThread extends Thread {

	private Logger logger = Logger.getLogger(EuropaMemberInitializationThread.class);
    private static final String ACTIVITY_DICTIONARY_GLOBAL = "AD_VERSION";
    private static final int MAXIMUM_RETRIES = 5;
    
	private final EuropaMember europaMember;
	private final EPlan plan;
	private final EuropaSessionClient client;
	private boolean quit = false;
	private int retries = 0;
	
	public EuropaMemberInitializationThread(EuropaMember europaMember, EPlan plan, EuropaSessionClient client) {
		super(EuropaMemberInitializationThread.class.toString());
		this.europaMember = europaMember;
		this.plan = plan;
		this.client = client;
    }
	
	public void setQuit(boolean quit) {
		this.quit = quit;
	}

	@Override
	public void run() {
		logger.info("Initializing Europa for plan "+plan.getName());
		TemporalMember member = plan.getMember(TemporalMember.class);
		Date start = member.getStartTime();
		while ((start == null) || Europa.getDoingModelAutoExport()) {
			try {
	            Thread.sleep(500); // wait 0.5 second for start
            } catch (InterruptedException e) {
            	if (quit) {
            		return;
            	}
            }
			start = member.getStartTime();
		}
		EuropaServerMonitor monitor = EuropaServerMonitor.getInstance();
		while (!client.isConnected()) {
			try {
				String europaHost = EuropaPreferences.getEuropaHost();
				int europaPort = EuropaPreferences.getEuropaPort();
				monitor.updateLastRequestTimeMillis();
				client.connect(europaHost, europaPort);
				monitor.updateLastResponseTimeMillis();
			} 
			catch (EuropaFatalException e) {
				logger.error("Disabling Europa Server for this plan because of fatal exception : ",e);
				monitor.setException(e);
				return;				
			}
			catch (Exception e) {
				Throwable cause = e.getCause();
				
				if (cause==null) 
					return;
				
				if (!(cause instanceof IOException))
					cause = new IOException(cause.getMessage());

				String prefix = "Server type ";
				int ind1 = cause.getMessage().indexOf(prefix);
				int ind2 = cause.getMessage().indexOf(" doesn't exist");
				if (ind1 != -1 && ind2 != -1) {
					String stype = cause.getMessage().substring(ind1 + prefix.length(), ind2);
					logger.error("\nDisabling Europa Server for this plan because Activity Dictionary " + stype + " has never been translated.\nSuggestion: try Export->Europa->Model menu item and close/reopen plan.");
					return;
				}
				
				logger.error("Error on connect: " + e.getMessage() + " for " + cause.getMessage());
				monitor.setException(cause);
				if (CommonPlugin.isJunitRunning()) {
					throw new RuntimeException("Error on connect", e);
				}
				try {
					if (++retries > MAXIMUM_RETRIES) {
						return;
					}
					// try again after 1 minute
					Thread.sleep(60*1000);
				} catch (InterruptedException e1) {
					if (quit) {
						return;
					}
				}
			}
		}
		
		try {
			Europa europa = new Europa(start, client);
			checkActivityDictionary();
			checkRules(europa);
			europaMember.initializationStarted(europa);
			monitor.updateLastRequestTimeMillis();
			europa.initialize(plan);
			monitor.updateLastResponseTimeMillis();
			europaMember.initializationFinished(europa);
			logger.info("Europa has been initialized for plan "+plan.getName());
		}
		catch (RuntimeException e) {
			logger.info("Europa failed to be initialized for plan "+plan.getName());
			monitor.setException(e);
		}
	}

	/**
	 * Check the activity dictionary for the model against the one in the tool
	 */
	private void checkActivityDictionary() {
		try {
	        Vector<Object> parameters = new Vector<Object>();
	        parameters.add(ACTIVITY_DICTIONARY_GLOBAL);
	        Object result = client.syncExecute(EuropaCommand.GET_GLOBAL_STRING, parameters, false);
	        ActivityDictionary AD = ActivityDictionary.getInstance();
//		String versionLabel = AD.getProperty(ActivityDictionary.PROPERTY_VERSION_LABEL);
	        String versionNumber = AD.getVersion();
	        if ((result instanceof String) && (versionNumber != null)) {
	        	String europaVersion = removeThirdLevelVersionNumber((String) result);
	        	String ensembleVersion = removeThirdLevelVersionNumber(versionNumber);
	        	if ((europaVersion.length() > 0) && !europaVersion.equals(ensembleVersion)) {
	        		showActivityDictionaryMismatchWarning(europaVersion, ensembleVersion);
	        	}
	        }
        } catch (Exception e) {
        	Logger logger = Logger.getLogger(EuropaMemberInitializationThread.class);
			logger.error("checkActivityDictionary", e);
        }
	}

	private void checkRules(Europa europa) {
		try {
	        ActivityDictionary AD = ActivityDictionary.getInstance();
	        List<String> europaFlightRuleIdentifiers = europa.getEuropaFlightRuleIdentifiers();
	        for (String identifier : europaFlightRuleIdentifiers) {
	        	ERule rule = AD.getDefinition(ERule.class, identifier);
	        	if (rule == null) {
	        		Logger logger = Logger.getLogger(EuropaMemberInitializationThread.class);
	    			logger.warn("Europa Rule ID wasn't found in the AD: " + identifier);
	        		rule = DictionaryFactory.eINSTANCE.createERule(Collections.singletonList("Europa"), identifier, null, "Retrieved from Europa model", null);
	                AD.getExtendedDefinitions().add(rule);
	        	}
	        }
        } catch (Exception e) {
        	Logger logger = Logger.getLogger(EuropaMemberInitializationThread.class);
			logger.error("checkRules", e);
        }
    }
	
	/** Numbering scheme:  20071231.17.42:
   Major version (20071231) is date of spreadsheet.
   Second (17) is incremented if translator changes to produce different AD and model.
   Third (42) is incremented if AD changes but model is unaffected.
   It's OK if Europa model version is 20071231.17.42 and AD version is 20071231.17.45. */
	private static String removeThirdLevelVersionNumber (String full) {
		String[] levels = full.split("\\.");
		if (levels.length < 3) return full;
		return levels[0] + "." + levels[1];
	}

	private void showActivityDictionaryMismatchWarning(String europaVersion, String ensembleVersion) {
		final String message = "The activity dictionary in use by Europa doesn't match the one in use by Ensemble."
		                     + "\n"
		                     + "\nEuropa version: " + europaVersion
		                     + "\nEnsemble version: " + ensembleVersion;
		Logger logger = Logger.getLogger(EuropaMemberInitializationThread.class);
		logger.warn(message);
		if (CommonPlugin.isJunitRunning()) {
			System.out.println(message);
		} else {
			SWTUtils.runInDisplayThread(new Runnable() {
				@Override
				public void run() {
					IWorkbench workbench = PlatformUI.getWorkbench();
					if (workbench != null) {
						IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
						if (window != null) {
							Shell shell = window.getShell();
							MessageDialog.openWarning(shell, "Activity Dictionary Mismatch", message);
							return;
						}
					}
				}
			});
		}
	}

}
