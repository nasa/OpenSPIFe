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
package gov.nasa.arc.spife.timeline.provider;

import gov.nasa.arc.spife.Activator;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

public abstract class TreeTimelineContentProvider implements ITreeItemContentProvider, IDisposable {

	public static interface Listener {
		public void contentRefresh(Set<? extends Object> element);
		public void labelUpdate(Set<? extends Object> element);
	}
	
	private final VisitList<Listener> listeners = new VisitList<Listener>();

	private Realm realm = null;
	
	public void setRealm(Realm realm) {
		this.realm = realm;
	}
	
	public void activate() {
		// no default activation
	}
	
	public void dispose() {
		// no default activation
	}
	
	public boolean hasChildren(Object object) {
		return !getChildren(object).isEmpty();
	}

	public Collection<?> getElements(Object object) {
		return getChildren(object);
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	public void refreshContents(final Set<? extends Object> elements) {
		runInRealm("refresh", new Runnable() {
			public void run() {
				listeners.visitAll(new VisitList.Visitor<Listener>() {
					public void visit(Listener listener) {
						listener.contentRefresh(elements);
					}
				});
			}
		});
	}

	public void updateLabels(final Set<? extends Object> elements) {
		runInRealm("update", new Runnable() {
			public void run() {
				listeners.visitAll(new VisitList.Visitor<Listener>() {
					public void visit(Listener listener) {
						listener.labelUpdate(elements);
					}
				});
			}
		});
	}
	
	private void runInRealm(String message, Runnable runnable) {
		if (Realm.getDefault() == realm) {
			try {
				runnable.run();
			} catch (Exception e) {
				Activator plugin = Activator.getDefault();
				ILog log = plugin.getLog();
				if (log != null) {
					log.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "failed to perform " + message, e));
				} else {
					LogUtil.error(new Exception("failed to perform " + message));
				}
			}
		} else {
			realm.asyncExec(runnable);
		}
	}
	
	public class TreeViewerNotifyChangedListener implements INotifyChangedListener {
		public void notifyChanged(Notification notification) {
			if (notification.isTouch()) {
				return;
			}
			if (notification instanceof ViewerNotification) {
				ViewerNotification viewerNotification = (ViewerNotification) notification;
				Object element = viewerNotification.getElement();
				if (viewerNotification.isContentRefresh()) {
					refreshContents(Collections.singleton(element));
				}
				if (viewerNotification.isLabelUpdate()) {
					updateLabels(Collections.singleton(element));
				}
			}
		}
	}
	
}
