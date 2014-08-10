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
package gov.nasa.arc.spife.ui.timeline.util;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;

public class EMFCommandWrapper extends org.eclipse.gef.commands.Command {

	private final EditingDomain domain;
	private final Command emfCommand;

	public EMFCommandWrapper(EditingDomain domain, Command emfCommand) {
		super(emfCommand.getLabel());
		this.domain = domain;
		this.emfCommand = emfCommand;
	}

	@Override
	public boolean canExecute() {
		return emfCommand.canExecute();
	}

	@Override
	public boolean canUndo() {
		return emfCommand.canUndo();
	}

	@Override
	public void dispose() {
		emfCommand.dispose();
	}

	@Override
	public void execute() {
		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(domain, new Runnable() {
			@Override
			public void run() {
				emfCommand.execute();
			}
		});
	}

	@Override
	public void redo() {
		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(domain, new Runnable() {
			@Override
			public void run() {
				emfCommand.redo();
			}
		});
	}

	@Override
	public void undo() {
		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(domain, new Runnable() {
			@Override
			public void run() {
				emfCommand.undo();
			}
		});
	}

}
