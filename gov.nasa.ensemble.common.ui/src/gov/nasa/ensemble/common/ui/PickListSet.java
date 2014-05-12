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
package gov.nasa.ensemble.common.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;

public class PickListSet {

	private String name = null;
	private List<String> keys = null;
	private IDialogSettings settings = null;
	
	private static final String KEYS = "keys";
	
	public PickListSet(String name, List<String> keys) {
		super();
		this.name = name;
		this.keys = keys;
	}
	
	public PickListSet(String name, String string) {
		this.name = name;
		
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		List<String> items = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()) {
			items.add(tokenizer.nextToken().trim());
		}
		this.keys = items;
	}

	public PickListSet(IDialogSettings settings) {
		this.name = settings.getName();
		this.keys = Arrays.asList(settings.getArray(KEYS));
	}

	public List<String> getKeys() {
		return keys;
	}
	
	public String getName() {
		return name;
	}
	
	public void setKeys(List<String> keys) {
		this.keys = keys;
		settings = null;
	}

	public void setName(String name) {
		this.name = name;
		settings = null;
	}
	
	public String getKeysString() {
		StringBuffer buffer = new StringBuffer();
		Iterator<String> i = keys.iterator();
		while (i.hasNext()) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(",");
			}
		}
		return buffer.toString();
	}
	
	public IDialogSettings getSettings() {
		if (settings == null) {
			settings = new DialogSettings(this.name);
			settings.put(KEYS, this.keys.toArray(new String[0]));
		}
		return settings;
	}
	
}
