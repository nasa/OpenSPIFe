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
package gov.nasa.ensemble.core.activityDictionary.view.transfer;

import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public class NamedDefTransferable<T extends INamedDefinition> implements ITransferable, Externalizable {
	
	private static final Logger trace = Logger.getLogger(NamedDefTransferable.class);
	private Collection<T> namedDefs = null;
	private Class<T> klass = null;

	public NamedDefTransferable(Class<T> klass) {
		this.klass = klass;
	}
	
	public NamedDefTransferable(Class<T> klass, Collection<T> defs) {
		this(klass);
		setNamedDefinitions(defs);
	}
	
	@Override
	public void dispose() {
		// nothing to dispose
	}
	
	public Collection<T> getNamedDefinitions() {
		return namedDefs;
	}
	
	public void setNamedDefinitions(Collection<T> defs) {
		namedDefs = defs;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		if (namedDefs == null) {
			out.writeInt(0);
		} else {
			out.writeInt(namedDefs.size());
			for (INamedDefinition def : namedDefs) {
				out.writeObject(def.getName());
			}
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int numOfDefs = in.readInt();
		Collection<T> nameDefs = new ArrayList<T>();
		for (int i = 0; i < numOfDefs; i++) {
			String name = (String) in.readObject();
			T def = getNamedDefinition(name);
			if (def != null) {
				nameDefs.add(def);
			} else {
				trace.error("'"+klass.getSimpleName()+"'"+name+"' not found");
			}
		}
		this.namedDefs = nameDefs;
	}

	protected T getNamedDefinition(String name) {
		ActivityDictionary ad = ActivityDictionary.getInstance();
		T def = ad.getDefinition(this.klass, name);
		return def;
	}

}
