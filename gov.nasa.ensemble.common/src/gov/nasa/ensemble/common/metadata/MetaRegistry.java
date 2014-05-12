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
package gov.nasa.ensemble.common.metadata;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

public class MetaRegistry {
	private final Map<Key, List<MetaData>> map = 
		Collections.synchronizedMap(new HashMap<Key, List<MetaData>>());
	private final ReferenceQueue queue = new ReferenceQueue();
	
	{
		Thread thread = new Thread("MetaRegistry Reference Cleaner") {
			@Override
			public void run() {
				while (true) {
					try {
						clearKey((Key)queue.remove());
					} catch (ThreadDeath td) {
						throw td;
					} catch (Throwable t) {
						LogUtil.error(t);
					}
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	public MetaData data(Object object) {
		Assert.isNotNull(object);
		
		Key key = new Key(object, queue);
		
		// find the list of potential metadatas
		List<MetaData> list;
		synchronized (map) {
			list = map.get(key);
			if (list == null) {
				list = Collections.synchronizedList(new ArrayList<MetaData>());
				map.put(key, list);
			}
		}
		
		// find the metadata
		MetaData data;
		synchronized (list) {
			data = findMetaData(object, list);
			if (data == null)
				data = new MetaData();
			list.add(data);
			data.registerOwner(object);
		}
		
		return data;
	}
	
	private MetaData findMetaData(Object object, List<MetaData> list) {
		synchronized (list) {
			for (MetaData data : list)
				if (CommonUtils.equals(object, data.getOwner()))
					return data;
		}
		return null;
	}

	private void clearKey(final Key key) {
		synchronized (map) {
			List<MetaData> list = map.get(key);
			if (list == null)
				return;
			synchronized(list) {
				list.removeAll(Lists.filter(list, new Predicate<MetaData>() {
					@Override
					public boolean apply(MetaData data) {
						return data.getOwner() == null;
					}
				}));
			}
			if (list.isEmpty())
				map.remove(key);
		}
	}
	
	private static class Key extends WeakReference {
		private final int hashCode;

		public Key(Object object, ReferenceQueue queue) {
			super(object, queue);
			this.hashCode = object.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			return hashCode == ((Key)obj).hashCode;
		}
		
		@Override
		public int hashCode() {
			return hashCode;
		}
	}
}
