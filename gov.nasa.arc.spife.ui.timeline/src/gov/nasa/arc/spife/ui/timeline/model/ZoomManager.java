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
package gov.nasa.arc.spife.ui.timeline.model;

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.ZoomOption;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

public class ZoomManager extends org.eclipse.gef.editparts.ZoomManager {

	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;

	private static final Logger trace = Logger.getLogger(ZoomManager.class);

	private enum ZoomResolution {
		milliseconds, seconds, minutes, hours, days, weeks,
	}

	private final Page page;
	private int zoomLevel = 4; // default value
	private Point zoomLocation = new Point();
	private Date zoomDate = null;
	private boolean zooming;

	public Date getZoomDate() {
		return zoomDate;
	}

	public void setZoomDate(Date zoomDate) {
		this.zoomDate = zoomDate;
	}

	public ZoomManager(final Page page) {
		super((ScalableFigure) null, null);
		this.page = page;
		if (page.getZoomOptions().isEmpty()) {
			gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(page, new Runnable() {
				@Override
				public void run() {
					page.getZoomOptions().addAll(getDefaultZoomOptions());
					if (page.getZoomOption() == null) {
						page.setZoomOption(page.getZoomOptions().get(zoomLevel));
					}
				}
			});
		} else {
			if (page.getZoomOption() == null) {
				zoomLevel = 0;
			} else {
				zoomLevel = page.getZoomOptions().indexOf(page.getZoomOption());
				if (zoomLevel == -1) {
					zoomLevel = 0;
				}
			}
			gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(page, new Runnable() {
				@Override
				public void run() {
					page.setZoomOption(page.getZoomOptions().get(zoomLevel));
				}
			});
		}

		setZoomLevels(new double[] {});
		List<String> list = new ArrayList<String>();
		for (ZoomOption o : getZoomOptions()) {
			list.add(o.getName());
		}

		setZoomLevelContributions(list);
	}

	@Override
	public String getZoomAsText() {
		return page.getZoomOption().getName();
	}

	@Override
	public void setZoomAsText(String zoomString) {
		int i = 0;
		for (ZoomOption o : getZoomOptions()) {
			if (o.getName().equals(zoomString)) {
				setZoomLocation(-1, -1);
				setZoomLevel(i);
				fireZoomChanged();
				break;
			}
			i++;
		}
	}

	public void setZoomOption(final ZoomOption zoomOption) {
		final int oldZoomLevel = this.zoomLevel;
		List<ZoomOption> zoomOptions = getZoomOptions();
		int count = zoomOptions.size();
		if (count > 0) {
			final long msInterval = zoomOption.getMsInterval();
			if (msInterval > zoomOptions.get(0).getMsInterval()) {
				this.zoomLevel = 0;
			} else if (msInterval < zoomOptions.get(count - 1).getMsInterval()) {
				this.zoomLevel = count - 1;
			} else {
				for (int i = 0; i < count - 1; i++) {
					ZoomOption o = zoomOptions.get(i);
					ZoomOption n = zoomOptions.get(i + 1);
					if (msInterval < o.getMsInterval() && msInterval > n.getMsInterval()) {
						this.zoomLevel = i;
					}
				}
			}
		}

		if (this.zoomLevel != oldZoomLevel) {
			executeZoomCommand(zoomLevel);
		}
	}

	protected void executeZoomCommand(int zoomLevel) {
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(page);
		Command command = SetCommand.create(domain, page, TimelinePackage.Literals.PAGE__ZOOM_OPTION, getZoomOptions().get(zoomLevel));
		EMFUtils.executeCommand(domain, command);
	}

	private void setZoomLevel(int level) {
		List<ZoomOption> zoomOptions = getZoomOptions();
		if (level > -1 && level < zoomOptions.size()) {
			this.zoomLevel = level;
			executeZoomCommand(zoomLevel);
		}
	}

	@Override
	public void zoomOut() {
		if (zoomLevel > 0) {
			setZoomLevel(zoomLevel - 1);
			fireZoomChanged();
		}
	}

	@Override
	public void zoomIn() {
		if (zoomLevel < getZoomOptions().size() - 1) {
			setZoomLevel(zoomLevel + 1);
			fireZoomChanged();
		}
	}

	@Override
	public boolean canZoomIn() {
		return zoomLevel < getZoomOptions().size() - 1;
	}

	@Override
	public boolean canZoomOut() {
		return zoomLevel > 0;
	}

	public void setZoomLocation(int x, int y) {
		this.zoomLocation.x = x;
		this.zoomLocation.y = y;
	}

	public Point getZoomLocation() {
		return zoomLocation;
	}

	private List<ZoomOption> getZoomOptions() {
		return page.getZoomOptions();
	}

	private List<ZoomOption> getDefaultZoomOptions() {
		List<ZoomOption> list = getZoomOptionsFromExtensionPoint();
		if (list.isEmpty()) {
			list.addAll(Arrays.asList(new ZoomOption[] { TimelineFactory.eINSTANCE.createZoomOption("24 hours", 24 * HOUR, 5 * MINUTE, 1 * HOUR, 24 * HOUR, 6 * HOUR), TimelineFactory.eINSTANCE.createZoomOption("12 hours", 12 * HOUR, 5 * MINUTE, 1 * HOUR, 24 * HOUR, 6 * HOUR), TimelineFactory.eINSTANCE.createZoomOption("6 hours", 6 * HOUR, 5 * MINUTE, 1 * HOUR), TimelineFactory.eINSTANCE.createZoomOption("4 hours", 4 * HOUR, 5 * MINUTE, 30 * MINUTE),
					TimelineFactory.eINSTANCE.createZoomOption("2 hours", 2 * HOUR, 5 * MINUTE, 5 * MINUTE), TimelineFactory.eINSTANCE.createZoomOption("1 hours", 1 * HOUR, 5 * MINUTE, 5 * MINUTE), TimelineFactory.eINSTANCE.createZoomOption("30 mins", 30 * MINUTE, 5 * MINUTE, 1 * MINUTE), TimelineFactory.eINSTANCE.createZoomOption("15 mins", 15 * MINUTE, 5 * MINUTE, 1 * MINUTE), }));
		}
		return list;
	}

	private List<ZoomOption> getZoomOptionsFromExtensionPoint() {
		List<ZoomOption> options = new ArrayList<ZoomOption>();

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint("gov.nasa.arc.spife.core.plan.editor.timeline.TimelineZoomOptions");
		if (extensionPoint == null)
			return options;
		for (IConfigurationElement element : extensionPoint.getConfigurationElements()) {
			ZoomOption zoomOption = parseZoomOption(element);
			if (zoomOption == null) {
				continue;
			}
			options.add(zoomOption);

			String defaultString = element.getAttribute("default");
			if (defaultString != null && defaultString.equals("true")) {
				zoomLevel = options.size() - 1;
			}
		}
		return options;
	}

	private ZoomOption parseZoomOption(IConfigurationElement element) {
		return parseZoomOption(element, true);
	}

	@SuppressWarnings("fallthrough")
	private ZoomOption parseZoomOption(IConfigurationElement element, boolean parseMoveInterval) {
		String valueString = element.getAttribute("value");
		String resolutionString = element.getAttribute("resolution");

		long value = 0;
		ZoomResolution resolution = null;
		try {
			value = Long.parseLong(valueString);
			resolution = ZoomResolution.valueOf(resolutionString);
		} catch (Exception e) {
			trace.error("Could not parse '" + valueString + "' '" + resolutionString + "'");
			return null;
		}

		long multiplier = 1;
		switch (resolution) {
		case weeks:
			multiplier *= 7;
		case days:
			multiplier *= 24;
		case hours:
			multiplier *= 60;
		case minutes:
			multiplier *= 60;
		case seconds:
			multiplier *= 1000;
		case milliseconds:
			// no operation
		}

		String name = null;
		switch (resolution) {
		case weeks:
			name = "week";
			break;
		case days:
			name = "day";
			break;
		case hours:
			name = "hour";
			break;
		case minutes:
			name = "minute";
			break;
		case seconds:
			name = "s";
			break;
		case milliseconds:
			name = "ms";
			break;
		}

		if (value > 1 && resolution != ZoomResolution.milliseconds) {
			name += "s";
		}

		long msInterval = value * multiplier;
		ZoomOption zoomOption = TimelineFactory.eINSTANCE.createZoomOption(value + " " + name, msInterval);

		if (parseMoveInterval) {
			ZoomOption moveResolutionOption = null;
			IConfigurationElement[] children = element.getChildren("moveResolution");
			if (children == null || children.length == 0) {
				trace.error("ZoomOption '" + zoomOption + "' has no zoom threshold, defaulting");
				moveResolutionOption = TimelineFactory.eINSTANCE.createZoomOption("", computeMoveThreshold(zoomOption.getMsInterval()));
			} else if (children.length == 1) {
				moveResolutionOption = parseZoomOption(children[0], false);
			} else {
				trace.error("ZoomOption '" + zoomOption + "' has multiple zoom thresholds, using the first one");
				moveResolutionOption = parseZoomOption(children[0], false);
			}
			zoomOption.setMsMoveThreshold(moveResolutionOption.getMsInterval());
		} else {
			zoomOption.setMsMoveThreshold(msInterval);
			zoomOption.setMsNudgeThreshold(msInterval);
		}

		zoomOption.setMajorTickInterval(multiplier);
		zoomOption.setMinorTickInterval(msInterval);

		return zoomOption;
	}

	public int computeMoveThreshold(double millisecondThreshold) {
		int evenTimeIntervals[] = { 5, 10, 15, 30, 60, // sec
				5 * 60, 10 * 60, 15 * 60, 30 * 60, // min
				1 * 60 * 60, 2 * 60 * 60, 3 * 60 * 60, 6 * 60 * 60, 12 * 60 * 60, // hr
				1 * 24 * 60 * 60, 2 * 24 * 60 * 60, 3 * 24 * 60 * 60, 5 * 24 * 60 * 60 // days
		};

		// use a proportional tolerance the label width
		int index = 0;
		while (index < evenTimeIntervals.length && millisecondThreshold > evenTimeIntervals[index] * 1000) {
			index++;
		}

		index = Math.min(index, evenTimeIntervals.length - 1);
		return evenTimeIntervals[index] * 1000;
	}

	public boolean isZooming() {
		return this.zooming;
	}

	public void setZooming(boolean zooming) {
		this.zooming = zooming;

	}

}
