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
package gov.nasa.ensemble.core.rcp.update;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

// The implementation in this class has been commented out
// so that the dependency of this plugin on the update plugins
// can be removed.  The class has been retained so that if
// someone wants to add similar functionality in the future,
// they can find this file.  If you are reading this for this
// reason you may want to consider rewritting the functionality
// due to the changes in the API since this class was used.

/**
 * Action class that contacts an update site, searches for updates, and installs the updates.
 */
public class FindUpdatesAction extends Action implements IWorkbenchWindowActionDelegate {

	@Override
	public void dispose() {
		// no impl
    }

	@Override
	public void init(IWorkbenchWindow window) {
		// no impl
    }

	@Override
	public void run(IAction action) {
		// no impl
    }

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// no impl
    }
	
//	public static final String UPDATES_URL_PROPERTY = "apache.server.url.updates";
//
//	private static final Logger trace = Logger.getLogger(FindUpdatesAction.class);
//	private final boolean[] updatesInstalled = {false};
//	private boolean doForceNotUpdate = false;
//	private String updateSiteURL     = null;
//	
//	private StringBuilder featuresToInstall = new StringBuilder();
//	private List<IInstallFeatureOperation> installOps = new ArrayList<IInstallFeatureOperation>();
//
//	public FindUpdatesAction() {
//		super();
//
//		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
//		updateSiteURL = properties.getProperty(UPDATES_URL_PROPERTY);
//		if (updateSiteURL == null || updateSiteURL.trim().length() == 0) {
//			trace.debug("The property '" + UPDATES_URL_PROPERTY + "' must be defined in order to check for software updates.");
//		}
//	}
//
//	public void init(IWorkbenchWindow window) {
//		// nothing to do
//	}
//	
//	public void dispose() {
//		// nothing to do
//	}
//
//	public void selectionChanged(IAction action, ISelection selection) {
//		// we don't care
//	}
//
//	/**
//	 * Call run(IAction)
//	 */
//	@Override
//	public void run() {
//		run(null);
//	}
//
//	public void run(IAction action) {
//		if (updateSiteURL != null) {
//			// reset some important flags
//			doForceNotUpdate = false;
//			
//			try {
//				IRunnableWithProgress op = new IRunnableWithProgress () {
//					public void run(IProgressMonitor monitor) {
//						try {
//							final Job j = doUpdate(monitor);
//							new Thread() {
//								@Override
//								public void run() {
//									try {
//										j.join();
//									} catch (InterruptedException e) {
//										//do nothing
//									}
//									Display.getDefault().syncExec(new Runnable() {
//										public void run() {
//											if (updatesInstalled[0])
//												PlatformUI.getWorkbench().restart();
//											// force a restart 
//											//if updates were installed
//										}
//									});
//								}
//							}.start();
//						} catch (MalformedURLException e) {
//							trace.error("Failed to update due to poorly formed URL: "+e.getMessage(), e);
//						} catch (CoreException e) {
//							trace.error("Failed to update due to CoreException: "+e.getMessage(), e);
//						}
//					}
//				};
//				
//				ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell ());
//				pmd.run(true, true, op);
//				
//				
////				if (updatesInstalled)
////					PlatformUI.getWorkbench().restart();  // force a restart if updates were installed
//				
//			} catch (InvocationTargetException e) {
//				trace.error("pmd.run", e);
//			} catch (InterruptedException e) {
//				trace.error("pmd.run", e);
//			}
//		}
//	}
//
//	/**
//	 * @param monitor
//	 * @throws CoreException
//	 * @throws MalformedURLException
//	 * @throws InvocationTargetException
//	 */
//	private Job doUpdate(IProgressMonitor monitor) throws CoreException, MalformedURLException {
//		// access both the remote update site and the local site
//		final ISite rs = SiteManager.getSite(new URL(updateSiteURL), monitor);
//		final IFeatureReference[] frs = rs.getFeatureReferences();
//		final ILocalSite ls = SiteManager.getLocalSite();
//		final IConfiguredSite ics = ls.getCurrentConfiguration().getConfiguredSites()[0];
//		final IFeatureReference[] lfrs = ics.getConfiguredFeatures();
//
//
//
//		// create a job to load the updates if there are some available 
//		// thus guaranteeing that this load will not take place in the UI threadO
//		Job obtainUpdates = new Job("Loading Updates") {
//			@SuppressWarnings("deprecation")
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//
//				StringBuilder fti = new StringBuilder();
//				List<IInstallFeatureOperation> iops = new ArrayList<IInstallFeatureOperation>();
//
//				// compare the features on the remote site with those on the local site
//				// and determine if an upgrade needs to take place
//				for(int i = 0; i < frs.length; i++) {
//					for (int j = 0; j < lfrs.length; j++) {
//
//						try {
//
//							if (frs[i].getVersionedIdentifier().getIdentifier().equals(lfrs[j].getVersionedIdentifier().getIdentifier())) {
//								if (frs[i].getVersionedIdentifier().getVersion().isGreaterThan(lfrs[j].getVersionedIdentifier().getVersion())) {
//
//									// found a feature that needs to be updated
//									fti.append('\t')
//									.append(frs[i].getName())
//									.append(" ")
//									.append(frs[i].getVersionedIdentifier().getVersion())
//									.append('\n');
//
//									// add an operation that will update the feature
//									// but do NOT yet perform the update or download
//									iops.add(
//											OperationsManager.getOperationFactory().createInstallOperation(
//													ics,
//													frs[i].getFeature(monitor), null, null, null)
//									);
//								}
//							}
//						}
//						catch (Throwable t) {
//							if (t instanceof ThreadDeath) {
//								throw (ThreadDeath)t;
//							}
//							trace.error("Failed to update: "+t.getMessage(), t);
//							openErrorDialog(t, fti);
//							System.exit(0);
//						}
//					}
//				}
//
//				setFeaturesToInstall(fti);
//				setInstallOps(iops);
//
//
//
//
//
//				// if there are features to update, then attempt to download and update
//				if (installOps.size() > 0) {
//					StringBuilder message =
//						new StringBuilder("The following updates have been found:\n\n")
//					.append(featuresToInstall)
//					.append("\nDo you wish to proceed with the upgrade?")
//					.append("\nYou will not be able to use the software otherwise.");
//					final String msg = message.toString();
//
//					// be sure to open the message dialog in the UI thread
//					Display.getDefault().syncExec(new Runnable() {
//						public void run() {
//							MessageDialog dialog = new MessageDialog(
//									Display.getDefault().getActiveShell(),
//									"Updates Available For Download",
//									null,
//									msg,
//									MessageDialog.INFORMATION,
//									new String[] { "Yes", "Exit" },
//									0);
//							int ret = dialog.open();
//							if(ret == 1) {
//								doForceNotUpdate = true;
//								PlatformUI.getWorkbench().close(); // they don't want to update, so close :(
//							}
//						}
//					});
//
//					try {
//
//						// if the user did NOT select "Exit", then perform the
//						// update
//						if (!doForceNotUpdate) {
//
//							// create a job to load the updates if there are
//							// some
//							// available
//							// thus guaranteeing that this load will not take
//							// place in
//							// the UI threadO
//							Job installUpdates = new Job("Installing Updates") {
//								@Override
//								protected IStatus run(IProgressMonitor monitor) {
//
//									try {
//										for (Iterator<IInstallFeatureOperation> iter = installOps
//												.iterator(); iter.hasNext();) {
//											IInstallFeatureOperation op = iter
//													.next();
//											op.execute(monitor, null);
//										}
//
//										ls.save();
//
//										EnsembleUsageLogger.logUsage(
//												"UPDATE: FindUpdatesAction",
//												featuresToInstall.toString());
//										updatesInstalled[0] = true;
//									} catch (Throwable e) {
//										if (e instanceof ThreadDeath) {
//											throw (ThreadDeath) e;
//										}
//										openErrorDialog(e, featuresToInstall);
//										System.exit(0);
//									}
//									
//									
//									
//									return Status.OK_STATUS;
//								}
//							};
//							installUpdates.schedule();
//							installUpdates.join();
//						}
//
//					} catch (Throwable e) {
//						if (e instanceof ThreadDeath) {
//							throw (ThreadDeath)e;
//						}
//						openErrorDialog(e, featuresToInstall);
//						System.exit(0);
//					}
//					
//
//
//				} else {
//					monitor.setTaskName("No updates pending.");
//				}
//				return Status.OK_STATUS;
//			}
//		};
//
//		// schedule the job
//		obtainUpdates.schedule();
//		return obtainUpdates;
//	}
//	
//	
//	private void openErrorDialog(Throwable error, StringBuilder featuresToInstall) {
//
//		// If there was any error during the update, don't allow the program to continue to run.
//		
//		EnsembleUsageLogger.logUsage("UPDATE: Error during Maestro Update: " + error);
//		
//		StringBuilder stackTraceMessage = new StringBuilder();
//		
//		for (StackTraceElement st : error.getStackTrace()) {
//			stackTraceMessage.append(st.toString());
//		}
//		
//		StringBuilder errorMsgBuilder =
//			new StringBuilder("There was an error when attempting to update to:\n\n")
//		.append(featuresToInstall)
//		.append("\nThe error was: \n")
//		.append(error.toString())
//		.append("\n\nStack Trace Message: \n")
//		.append(stackTraceMessage)
//		.append("\n\nPlease contact the Maestro Team in order to use Maestro.")
//		.append("\n\n maestro@list.jpl.nasa.gov");
//
//		final String errMsg = errorMsgBuilder.toString();
//		
//		// log the issue
//		trace.error(errMsg, error);
//
//
//		// be sure to open the message dialog in the UI thread
//		Display.getDefault().syncExec(new Runnable() {
//			public void run() {
//				MessageDialog errorDialog = new MessageDialog(
//						Display.getDefault().getActiveShell(),
//						"Error During Update",
//						null,
//						errMsg,
//						MessageDialog.ERROR,
//						new String[] { "Exit" },
//						0);
//
//				errorDialog.open();
//			}
//		});
//	}
//	
//	
//	public void setFeaturesToInstall(StringBuilder sb) {
//		featuresToInstall = sb;
//	}
//	
//	public void setInstallOps(List<IInstallFeatureOperation> iops) {
//		installOps = iops;
//	}
//	
//	public StringBuilder getFeaturesToInstall() {
//		return featuresToInstall;
//	}
//	
//	public List<IInstallFeatureOperation> getInstallOps() {
//		return installOps;
//	}
//
}
