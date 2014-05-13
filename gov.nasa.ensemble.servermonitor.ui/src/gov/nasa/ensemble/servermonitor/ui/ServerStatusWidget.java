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
package gov.nasa.ensemble.servermonitor.ui;

import static gov.nasa.ensemble.common.functional.Lists.*;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.osgi.framework.Bundle;

import fj.F;
import fj.P1;
import fj.data.List;
import fj.data.Option;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.servermonitor.IServerMonitor;
import gov.nasa.ensemble.common.servermonitor.IServerMonitorListener;
import gov.nasa.ensemble.common.servermonitor.ServerMonitorRegistry;
import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.common.ui.WidgetUtils;

/**
 * This widget lives in the "trim" area.  It displays 
 * a set of "LEDs" that convey the current state of
 * existing connections to services on remote machines.
 * 
 * Meanings are:
 *   grey = no attempt to connect yet
 *   yellow = attempting to connect
 *   green = currently connected, no errors, received response to last request
 *   green with spinning yellow = request sent, still awaiting response
 *   red = problem with the connection
 * 
 * @see EnsembleServerRegistry
 * @author Andrew
 */
public class ServerStatusWidget extends WorkbenchWindowControlContribution {

	private static P1<List<IServerStatusWidgetListenerProvider>> widgetListenerProviders = 
		new P1<List<IServerStatusWidgetListenerProvider>>() {
		@Override
		public List<IServerStatusWidgetListenerProvider> _1() {
			return fj(ClassRegistry.createInstances(IServerStatusWidgetListenerProvider.class));
		}
	}.memo();
	
	private static final F<IServerStatusWidgetListenerProvider, Boolean> matches(final String host, final int port) {
		return new F<IServerStatusWidgetListenerProvider, Boolean>() {
			public Boolean f(final IServerStatusWidgetListenerProvider provider) {
				return provider != null 
						&& CommonUtils.equals(host, provider.getHost()) && port == provider.getPort();
			}
		};
	}
	
	private static enum ServerStatusEnum {
		NOT_CONTACTED_YET,
		ERROR_STATE,
		AWAITING_CONTACT,
		AWAITING_RESPONSE,
		ONLINE
	}
	
	private static final Image grey = IconLoader.getIcon(Activator.getDefault().getBundle(), "icons/ServerStatusLEDs_grey.png");
	private static final Image green = IconLoader.getIcon(Activator.getDefault().getBundle(), "icons/ServerStatusLEDs_green.png");
	private static final Image yellow = IconLoader.getIcon(Activator.getDefault().getBundle(), "icons/ServerStatusLEDs_yellow.png");
	private static final Image red = IconLoader.getIcon(Activator.getDefault().getBundle(), "icons/ServerStatusLEDs_red.png");
	private static final Vector<Image> busy_icons = create_busy_icons();
		
	private static final int N_BUSY_PHASES = 9; // 0..8
	private static final int MS_PER_BUSY_PHASE = 250; // Change 4 times per second.
	
	private static final Vector<Image> create_busy_icons () {
		Vector<Image> result = new Vector<Image> (N_BUSY_PHASES);
		Bundle bundle = Activator.getDefault().getBundle();
		for (int state = 0; state < N_BUSY_PHASES; state++)
			result.add(state, IconLoader.getIcon(bundle, "icons/ServerStatusLEDs_busy_" + state + ".png"));
		return result;
	}
	
	/**
	 * Cache the current trim so we can 'dispose' it on demand
	 */
	private Composite trimComposite = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.menus.AbstractTrimWidget#dispose()
	 * 
	 * Dispose the current trim widget (if any)
	 */
	@Override
	public void dispose() {
		if (trimComposite != null && !trimComposite.isDisposed())
			trimComposite.dispose();
		trimComposite = null;
	}

	/**
	 * Create control is called when the trim is initially formed
	 * and also when the trim is moved to a new side of the application.
	 */
	@Override
	protected Control createControl(Composite parent) {
		// Create a composite to place the label in 
		trimComposite = new Composite(parent, SWT.NONE);
		trimComposite.setLayout(createLayout());
		try {
			Collection<IServerMonitor> allMonitors = ServerMonitorRegistry.getInstance().getAllMonitors();
			LogUtil.info("Creating " + allMonitors.size() + " server-status LED monitors.");
			for (IServerMonitor server : allMonitors) {
				try {
					addServer(trimComposite, server);
				} catch (Exception e) {
					LogUtil.error("Could not create server-status LED monitor for host "
							+ server.getHost() + " on port " + server.getPort() , e);
				}
			}
		} catch (Exception e) {
			LogUtil.error("Could not get list of configured server-status LED monitors", e);
		}
		return trimComposite;
	}

	// Give some room around the control
	private RowLayout createLayout() {
		int type = getOrientation();
		RowLayout layout = new RowLayout(type);
		layout.marginTop = 2;
		layout.marginBottom = 2;
		layout.marginLeft = 2;
		layout.marginRight = 2;
		return layout;
	}
	
	private void addServer(Composite parent, final IServerMonitor server) {
		final Composite composite = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.marginLeft = 2;
		layout.marginRight = 2;
		composite.setLayout(layout);
		final Label icon = new Label(composite, SWT.CENTER);
		final Label text = new Label(composite, SWT.CENTER);
		text.setText(server.getShortName());
		updateStatusIconAndTooltip(server, composite, icon, text);
		composite.addMouseTrackListener(new CompositeMouseTrackListener(text, icon, server, composite));
		composite.addMouseListener(new CompositeMouseListener(text, icon, server, composite));
		server.addListener(new CompositeServerMonitorListener(text, icon, server, composite));
		
		final String host = server.getHost();
		final int port = server.getPort();
		final Option<IServerStatusWidgetListenerProvider> provider = widgetListenerProviders._1().find(matches(host, port));
		if (provider.isSome()) {
			final MouseListener mouseListener = provider.some().getMouseListener();
			composite.addMouseListener(mouseListener);
			icon.addMouseListener(mouseListener);
			text.addMouseListener(mouseListener);
		}
	}

	private int busy_phase (IServerMonitor server) {
		return (int) (System.currentTimeMillis()
				/ MS_PER_BUSY_PHASE)
				% N_BUSY_PHASES;
	}

	private void updateStatusIcon(final IServerMonitor server, final Composite composite, final Label icon) {
		final ServerStatusEnum status = computeStatus(server);
		final Image busy_icon = busy_icons.elementAt(busy_phase(server));
		WidgetUtils.runInDisplayThread(composite,
			new Runnable() {
				public void run() {
					updateIcon(icon, status, busy_icon);
				}
		});
	}

	private void updateStatusIconAndTooltip(final IServerMonitor server, final Composite composite, final Label icon, final Label text) {
		final ServerStatusEnum status = computeStatus(server);
		final String statusText = createStatusText(server, status);
		final Image busy_icon = busy_icons.elementAt(busy_phase(server));
		WidgetUtils.runInDisplayThread(composite,
			new Runnable() {
				public void run() {
					updateTooltip(icon, text, statusText);
					updateIcon(icon, status, busy_icon);
				}
		});
	}

	private ServerStatusEnum computeStatus(IServerMonitor server) {
		ServerStatusEnum status = null;
		long lastRequestTime = server.lastRequestTimeMillis();
		long lastResponseTime = server.lastResponseTimeMillis();
		if (server.getException() != null) {
			status = ServerStatusEnum.ERROR_STATE;
		}
		else if (lastRequestTime == 0 && lastResponseTime == 0) {
			status = ServerStatusEnum.NOT_CONTACTED_YET;
		}
		else if (lastResponseTime == 0) {
			status = ServerStatusEnum.AWAITING_CONTACT;
		}
		else if (lastRequestTime > lastResponseTime) {
			status = ServerStatusEnum.AWAITING_RESPONSE;
		}
		else {
			status = ServerStatusEnum.ONLINE;
		}
		return status;
	}

	private String createStatusText(IServerMonitor server, ServerStatusEnum status) {
		StringBuilder text = new StringBuilder();
		text.append(server.getLongName());
		if (status != null) {
			switch (status) {
			case NOT_CONTACTED_YET:
				text.append(" has not been contacted yet.");
				break;
			case ONLINE:
				text.append(" is online.");
				break;
			case AWAITING_CONTACT:
				text.append(" should be starting up.");
				break;
			case AWAITING_RESPONSE:
				text.append(" has not yet responded to the last request it was sent.");
				break;
			case ERROR_STATE:
				text.append(" has experienced an error.");
				break;
			}
		}
		text.append("\n");
		text.append("Host: " + server.getHost() + "\n");
		text.append("Port: " + server.getPort() + "\n");
		text.append(server.getInfo());
		Throwable exception = server.getException();
		if (exception != null) {
			text.append(exception.getClass().getName() + ": " + exception.getMessage() + "\n");
		}
		long lastContactTime = server.lastResponseTimeMillis();
		if (lastContactTime > 0) {
			long milliseconds = System.currentTimeMillis() - lastContactTime;
			double seconds = milliseconds / 1000.0;
			text.append("\n");
			text.append("Last communication was " + seconds + " seconds ago");
		}
		return text.toString();
	}

	/**
	 * Must be called in the display thread
	 * @param icon
	 * @param text
	 * @param status
	 * @param statusText
	 * @param busy_icon
	 */

	private void updateTooltip(Label icon, Label text, String statusText) {
		icon.setToolTipText(statusText);
		text.setToolTipText(statusText);
	}

	private void updateIcon(Label icon, ServerStatusEnum status, Image busy_icon) {
		if (status != null) {
			switch (status) {
			case NOT_CONTACTED_YET:
				icon.setImage(grey);
				break;
			case ONLINE:
				icon.setImage(green);
				break;
			case AWAITING_CONTACT:
				icon.setImage(yellow);
				break;
			case AWAITING_RESPONSE:
			 	icon.setImage(busy_icon);
				break;
			case ERROR_STATE:
				icon.setImage(red);
				break;
			}
		}
	}

	/*
	 * Listener classes follow
	 */
	
	private final class CompositeServerMonitorListener implements IServerMonitorListener {
		private final Label text;
		private final Label icon;
		private final IServerMonitor server;
		private final Composite composite;

		private CompositeServerMonitorListener(Label text, Label icon, IServerMonitor monitor, Composite composite) {
			this.text = text;
			this.icon = icon;
			this.server = monitor;
			this.composite = composite;
		}

		public void noteResponseReceived(long nowInMillis) {
			updateStatusIcon(server, composite, icon);
		}

		public void noteRequestSent(long nowInMillis) {
			updateStatusIcon(server, composite, icon);
		}

		public void handleException(Throwable exception) {
			updateStatusIconAndTooltip(server, composite, icon, text);
		}
	}

	private final class CompositeMouseListener implements MouseListener {
		private final Label text;
		private final Label icon;
		private final IServerMonitor server;
		private final Composite composite;

		private CompositeMouseListener(Label text, Label icon, IServerMonitor server, Composite composite) {
			this.composite = composite;
			this.text = text;
			this.server = server;
			this.icon = icon;
		}

		public void mouseDown(MouseEvent e) {
			updateStatusIconAndTooltip(server, composite, icon, text);
		}

		public void mouseDoubleClick(MouseEvent e) {
			// TODO: open preference page for this server?
		}

		public void mouseUp(MouseEvent e) {
			// no update here
		}
	}

	private final class CompositeMouseTrackListener implements MouseTrackListener {
		private final Label text;
		private final Label icon;
		private final IServerMonitor server;
		private final Composite composite;

		private CompositeMouseTrackListener(Label text, Label icon, IServerMonitor server, Composite composite) {
			this.text = text;
			this.icon = icon;
			this.server = server;
			this.composite = composite;
		}

		public void mouseEnter(MouseEvent e) {
			updateStatusIconAndTooltip(server, composite, icon, text);
		}

		public void mouseHover(MouseEvent e) {
			updateStatusIconAndTooltip(server, composite, icon, text);
		}

		public void mouseExit(MouseEvent e) {
			// no need to update here
		}
	}

}
