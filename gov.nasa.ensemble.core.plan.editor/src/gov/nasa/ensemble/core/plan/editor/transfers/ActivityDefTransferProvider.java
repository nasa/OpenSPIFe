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
package gov.nasa.ensemble.core.plan.editor.transfers;

import gov.nasa.ensemble.common.ui.transfers.ITransferProvider;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.ActivityDefSerializableTransfer;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.dnd.Transfer;

/**
 * ActivityDefTransferProvider
 */
public class ActivityDefTransferProvider implements ITransferProvider<Serializable[]> {

	public static final ActivityDefSerializableTransfer transfer = ActivityDefSerializableTransfer.getInstance();
	
	private final Logger trace = Logger.getLogger(getClass());
	
	public Transfer getTransfer() {
		return transfer;
	}
	
	public boolean canPack(ITransferable transferable) {
		return (transferable instanceof ActivityDefTransferable);
	}

	public Serializable[] packTransferObject(ITransferable transferable) {
		if (!(transferable instanceof ActivityDefTransferable)) {
			return null;
		}
		ActivityDefTransferable defTransferable = (ActivityDefTransferable) transferable;
		try {
			List<EActivityDef> definitions = defTransferable.definitions;
			Serializable[] result = new Serializable[definitions.size()];
			int i = 0;
			for (EActivityDef definition : definitions) {
				result[i] = definition.getName();
				i++;
			}
			return result;
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			trace.error("packTransferObject", t);
			return null;
		}
	}

	public ITransferable unpackTransferObject(Serializable[] transferObject) {
		List<EActivityDef> definitions = new ArrayList<EActivityDef>();
		for (Serializable serializable : transferObject) {
			if (serializable instanceof String) {
				definitions.add(ActivityDictionary.getInstance().getActivityDef((String) serializable));
			} else {
				return null;
			}
		}
		return new ActivityDefTransferable(definitions);
	}

}
