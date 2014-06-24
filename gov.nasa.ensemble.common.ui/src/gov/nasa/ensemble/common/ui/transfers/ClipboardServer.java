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
package gov.nasa.ensemble.common.ui.transfers;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.lang.Thread.State;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;

public class ClipboardServer {

	private static final class MemoryClipboard extends Clipboard {
		private final Map<Transfer, Object> map = new LinkedHashMap<Transfer, Object>();
		private final Set<TransferData> supportedTypes = new LinkedHashSet<TransferData>();

		private MemoryClipboard(Display display) {
			super(display);
		}

		@Override
		protected void checkSubclass() {
			// ignore
		}

		@Override
		public TransferData[] getAvailableTypes() {
			return supportedTypes.toArray(new TransferData[supportedTypes.size()]);
		}

		@Override
		public Object getContents(Transfer transfer) {
			return map.get(transfer);
		}

		@Override
		public void setContents(Object[] data, Transfer[] dataTypes) {
			map.clear();
			supportedTypes.clear();
			for (int i = 0 ; i < data.length && i < dataTypes.length ; i++) {
				Transfer transfer = dataTypes[i];
				map.put(transfer, data[i]);
				TransferData[] types = transfer.getSupportedTypes();
				supportedTypes.addAll(Arrays.asList(types));
			}
		}
		
		@Override
		public void clearContents(int clipboards) {
			map.clear();
			supportedTypes.clear();
		}
	}

	public static final ClipboardServer instance = new ClipboardServer();
	
	private final Logger trace = Logger.getLogger(getClass());
	private final Map<Display, Clipboard> displayToClipboard = new HashMap<Display, Clipboard>();
	
	private ClipboardServer() {
		// default
	}

	public Clipboard getClipboard(Display display) {
		if (display == null) {
			display = WidgetUtils.getDisplay();
		}
		if (display == null) {
			throw new NullPointerException("couldn't find display for clipboard");
		}
		if (display.isDisposed()) {
			throw new RuntimeException("the display has been disposed");
		}
		Clipboard clipboard = displayToClipboard.get(display);
		if (clipboard == null) {
			synchronized (this) {
				clipboard = displayToClipboard.get(display);
				if (clipboard == null) {
					if (CommonPlugin.isJunitRunning()) {
						try {
							clipboard = new MemoryClipboard(display);
						} catch (SWTException e) {
							Thread[] tarray = new Thread[512];
							int count = Thread.enumerate(tarray);
							for (int i = 0 ; i < count ; i++) {
								Thread thread = tarray[i];
								String name = thread.getName();
								LogUtil.info(name);
								Display threadDisplay = Display.findDisplay(thread);
								if (threadDisplay != null) {
									throw new RuntimeException("thread display on thread: " + name);
								}
							}
							Thread thread = display.getThread();
							State state = thread.getState();
							throw new RuntimeException("thread display on thread: " + thread.getName() + " in state: " + state);
						}
					} else {
						clipboard = new Clipboard(display);
					}
					displayToClipboard.put(display, clipboard);
				}
			}
		}
		return clipboard;
	}

	public synchronized void disposeClipboards() {
		trace.debug("disposing " + displayToClipboard.size() + " clipboards");
		for (Map.Entry<Display, Clipboard> entry : displayToClipboard.entrySet()) {
			Display display = entry.getKey();
			if (!display.isDisposed()) {
				final Clipboard clipboard = entry.getValue();
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						clipboard.dispose();
					}
				});
			}
		}
		displayToClipboard.clear();
	}

}
