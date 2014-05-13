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
/**
 * 
 */
package gov.nasa.ensemble.common.ui.transfers;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.dnd.EnsembleDragAndDropOracle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;

/**
 * The transfer registry is a centralized location for serializing/externalizing
 * during clipboard operations or drag and drop operations.  The registry uses
 * TransferProvider extensions that know how to convert particular types into
 * different representations for placing on the clipboard.
 * 
 * This class has been optimized to use a local cache for clipboard operations
 * when the source and destination are the same running instance.  The cache
 * operates by putting a "MarkerTransferable" on the clipboard along with the
 * clipboard contents.  When the clipboard contents are requested from the
 * application, this MarkerTransferable is retrieved first.  If it exists and
 * is the same as the local MarkerTransferable, then the cache is valid and
 * will be used.  Otherwise, the clipboard contents will be deserialized
 * using the TransferProvider.  This will occur when the source and destination
 * are two separately running instances.
 * 
 * @author Andrew
 *
 */
public class TransferRegistry {

	private static final Logger trace = Logger.getLogger(TransferRegistry.class);
	private static final MarkerTransferProvider markerTransferProvider = new MarkerTransferProvider();
	private static TransferRegistry registry;

	private List<ITransferProvider> transferProviders = new ArrayList<ITransferProvider>();

	// support caching transferables to reduce deserialization
	private ITransferable cachedClipboardTransferable;
	private MarkerTransferable cachedClipboardMarker;
	private List<Transfer> cachedClipboardTransfers = Collections.emptyList();
	private ITransferable cachedDraggedTransferable;
	private Object cachedDraggedObject;
	private Transfer cachedDraggedTransfer;
	
	private TransferRegistry() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("gov.nasa.ensemble.common.TransferProvider");
		for (IExtension extension : point.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				String elementName = configurationElement.getName();
				if ("provider".equals(elementName)) {
					try {
						ITransferProvider provider = (ITransferProvider)configurationElement.createExecutableExtension("class");
						transferProviders.add(provider);
					} catch (ThreadDeath td) {
						throw td;
					} catch (Throwable t) {
						trace.error("during ITransferProvider instantiation", t);
					}
				}
			}
		}
	}
	
	/**
	 * Returns the singleton TransferRegistry
	 * @return the singleton TransferRegistry
	 */
	public static TransferRegistry getInstance() {
		if (registry == null) {
			registry = new TransferRegistry();
		}
		return registry;
	}

	/**
	 * Returns all the transfers that a transfer provider has registered
	 * @return all the transfers that a transfer provider has registered
	 */
	public Transfer[] getAllTransfers() {
		Transfer[] allTransfers = new Transfer[transferProviders.size()];
		int i = 0;
		for (ITransferProvider provider : transferProviders) {
			try {
				allTransfers[i] = provider.getTransfer();
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("getAllTransfers", t);
			}
			i++;
		}
		return allTransfers;
	}
	
	/**
	 * Checks all the registered transfer providers to see if they can support the supplied objects. Returns an array of transfers
	 * that claim to support the supplied objects.
	 * 
	 * @param transferable
	 * @return array of transfers that claim to support the supplied objects
	 */
	public Transfer[] getTransfersFor(ITransferable transferable) {
		List<Transfer> transfers = new ArrayList<Transfer>();
		for (ITransferProvider provider : transferProviders) {
			try {
				boolean packable = provider.canPack(transferable);
				Transfer transfer = provider.getTransfer();
				if (packable && (transfer != null)) {
					transfers.add(transfer);
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("getTransfersFor", t);
			}
		}
		return transfers.toArray(new Transfer[transfers.size()]);
	}
	
	/**
	 * Puts the objects into the clipboard in all supported formats.
	 * 
	 * @param transferable
	 * @param clipboard
	 */
	public void putOnClipboard(ITransferable transferable) {
		final List<Object> datas = new ArrayList<Object>();
		final List<Transfer> transfers = new ArrayList<Transfer>();
		for (ITransferProvider provider : transferProviders) {
			try {
				if (provider.canPack(transferable)) {
					Object clipboardObject = provider.packTransferObject(transferable);
					Transfer transfer = provider.getTransfer();
					if ((clipboardObject != null) && (transfer != null)) {
						datas.add(clipboardObject);
						transfers.add(transfer);
					}
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("putOnClipboard", t);
			}
		}
		synchronized (this) {
			cachedClipboardTransferable = transferable;
			cachedClipboardMarker = new MarkerTransferable();
			cachedClipboardTransfers = transfers;
			datas.add(markerTransferProvider.packTransferObject(cachedClipboardMarker));
			transfers.add(markerTransferProvider.getTransfer());
			final Display display = WidgetUtils.getDisplay();
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					Clipboard clipboard = ClipboardServer.instance.getClipboard(display); 
					clipboard.setContents(datas.toArray(new Object[datas.size()]), 
										  transfers.toArray(new Transfer[transfers.size()]));
				}
			});
		}
	}

	/**
	 * Retrieves the contents of the clipboard. getFromClipboard iterates over the list of acceptableTypes. The first type in this
	 * list that can be read from the clipboard by a registered transfer determines the type of the objects that will be returned.
	 * The ClipboardContents that is returned indicates which type this is, in addition to the objects themselves.
	 * 
	 * @param acceptableTypes
	 * @param clipboard
	 * @return ClipboardContents
	 */
	public synchronized ClipboardContents getFromClipboard(final List<TransferData> acceptableTypes) {
		final ClipboardContents result[] = { null };
		final Display display = WidgetUtils.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				Clipboard clipboard = ClipboardServer.instance.getClipboard(display); 
				ClipboardContents contents = getCachedContents(acceptableTypes, clipboard);
				if (contents != null) {
					result[0] = contents;
					return;
				}
				for (TransferData type : acceptableTypes) {
					for (ITransferProvider provider : transferProviders) {
						Transfer transfer = provider.getTransfer();
						if (transfer.isSupportedType(type)) {
							Object transferObject = clipboard.getContents(transfer);
							if (transferObject != null) {
								@SuppressWarnings("unchecked")
								ITransferable transferable = provider.unpackTransferObject(transferObject);
								if (transferable != null) {
									result[0] = new ClipboardContents(type, transferable);
									return;
								}
							}
						}
					}
				}
			}
		});
		return result[0];
	}

	/**
	 * Given a transferable and the target data type, this method returns the
	 * object which can be passed to the transfer for dragging.  Should be
	 * called from EnsembleDragSourceListener.getDragData [dragSetData]
	 * 
	 * @param transferable
	 * @param dataType
	 * @return the object for input to the corresponding transfer
	 */
	public Object getDragData(ITransferable transferable, TransferData dataType) {
		for (ITransferProvider provider : transferProviders) {
			try {
				Transfer transfer = provider.getTransfer();
				if (transfer != null && transfer.isSupportedType(dataType)) {
					Object dragData;
					if (!EnsembleDragAndDropOracle.isSameProcessDragSourceAndDropTarget()) {
						dragData = provider.packTransferObject(transferable);
					} else {
						// this just generates something unique for this drag/drop
						long millis = System.currentTimeMillis();
						byte b1 = (byte) (millis & 0xFF); millis >>= 8;
						byte b2 = (byte) (millis & 0xFF); millis >>= 8;
						byte b3 = (byte) (millis & 0xFF); millis >>= 8;
						byte b4 = (byte) (millis & 0xFF); millis >>= 8;
						byte b5 = (byte) (millis & 0xFF); millis >>= 8;
						byte b6 = (byte) (millis & 0xFF); millis >>= 8;
						byte b7 = (byte) (millis & 0xFF); millis >>= 8;
						byte b8 = (byte) (millis & 0xFF);
						dragData = new byte[] { b1, b2, b3, b4, b5, b6, b7, b8 };
					}
					if (dragData != null) {
						synchronized (this) {
							cachedDraggedTransferable = transferable;
							cachedDraggedObject = dragData;
							cachedDraggedTransfer = transfer;
						}
						return dragData;
					}
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("getDragData", t);
			}
		}
		return null;
	}

	/**
	 * Given an object retrieved from a transfer and the corresponding data type,
	 * this method returns a transferable corresponding to it.  Should be
	 * called from EnsembleDropTargetListener.executeDrop [drop]
	 * 
	 * @param object
	 * @param dataType
	 * @return the transferable represented by the given transfer object
	 */
	public ITransferable getDroppedObjects(Object object, TransferData dataType) {
		if (isDraggedTransferableCached(object, dataType)) {
			ITransferable transferable = cachedDraggedTransferable;
			synchronized (this) {
				cachedDraggedTransfer = null;
				cachedDraggedObject = null;
				cachedDraggedTransferable = null;
			}
			return transferable;
		}
		for (ITransferProvider provider : transferProviders) {
			try {
				Transfer transfer = provider.getTransfer();
				if (transfer != null && transfer.isSupportedType(dataType)) {
					@SuppressWarnings("unchecked")
					ITransferable transferable = provider.unpackTransferObject(object);
					if (transferable != null) {
						return transferable;
					}
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("getDroppedObjects", t);
			}
		}
		return null;
	}

	/**
	 * Return whether or not the dragged transferable is cached
	 * 
	 * @param object
	 * @param dataType
	 * @return whether or not the dragged transferable is cached
	 */
	private boolean isDraggedTransferableCached(Object object, TransferData dataType) {
		if (cachedDraggedTransfer == null) {
			return false;
		}
		if (!cachedDraggedTransfer.isSupportedType(dataType)) {
			return false;
		}
		if (object.equals(cachedDraggedObject)) {
			return true;
		}
		// Andrew: this is less than ideal, but we can't use the nice marker method
		// that we did on the clipboard. at least this works and is efficient.
		if ((object instanceof byte[]) && (cachedDraggedObject instanceof byte[])) {
			return Arrays.equals((byte[]) object, (byte[]) cachedDraggedObject);
		}
		return false;
	}

	/**
	 * Return the clipboard contents from our local cache, if the contents are present and support the acceptableType
	 * 
	 * @param acceptableTypes
	 * @param clipboard
	 * @return ClipboardContents
	 */
	private ClipboardContents getCachedContents(List<TransferData> acceptableTypes, Clipboard clipboard) {
		TransferData type = getMatchingType(acceptableTypes);
		if (type != null) {
			Transfer markerTransfer = markerTransferProvider.getTransfer();
			Object markerTransferObject = clipboard.getContents(markerTransfer);
			if (markerTransferObject instanceof byte[]) {
				ITransferable transferable = markerTransferProvider.unpackTransferObject((byte[]) markerTransferObject);
				if (transferable instanceof MarkerTransferable) {
					MarkerTransferable markerTransferable = (MarkerTransferable) transferable;
					if (markerTransferable.getMarker() == cachedClipboardMarker.getMarker()) {
						return new ClipboardContents(type, cachedClipboardTransferable);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Return the first acceptableType that is matched by a cached transfer
	 * 
	 * @param acceptableTypes
	 * @return the first acceptableType that is matched by a cached transfer
	 */
	private TransferData getMatchingType(List<TransferData> acceptableTypes) {
		for (TransferData acceptableType : acceptableTypes) {
			for (Transfer cachedTransfer : cachedClipboardTransfers) {
				if (cachedTransfer.isSupportedType(acceptableType)) {
					return acceptableType;
				}
			}
		}
		return null;
	}

	/**
	 * Clear out the clipboard contents
	 */
	public static void clearClipboardContents() {
		final Display display = WidgetUtils.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				Clipboard clipboard = ClipboardServer.instance.getClipboard(display);
				try {
					clipboard.clearContents();
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t2) {
					LogUtil.warn("failed to clear the clipboard");
				}
			}
		});
	}
	
}
