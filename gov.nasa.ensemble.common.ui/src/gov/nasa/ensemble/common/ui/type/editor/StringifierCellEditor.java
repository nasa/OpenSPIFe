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
package gov.nasa.ensemble.common.ui.type.editor;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.date.defaulting.DefaultDateUtil;
import gov.nasa.ensemble.common.ui.date.defaulting.ISetContext;

import java.text.ParseException;

import org.eclipse.swt.widgets.Composite;

public class StringifierCellEditor<T> extends CocoaCompatibleTextCellEditor implements ISetContext {

	private final IStringifier<T> stringifier;
	private final StringTypeEditor<?> editor;
	private T object = null;
	private Object context;

	public StringifierCellEditor(Composite parent, IStringifier<T> stringifier) {
		super(parent);
		this.stringifier = stringifier;
		this.editor = new StringTypeEditor(stringifier, text);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doSetValue(Object object) {
		this.object = (T)object;
		if (editor != null) {
			editor.setObject(object);
		}
        String string = stringifier.getDisplayString((T)object);
        if (string == null) {
        	string = ""; // TextCellEditor doesn't like null
        }
		super.doSetValue(string);
	}
	
	@Override
	public void setContext(Object target) {
		context = target;
	}
	
	private T getDefaultValue() {
		if (object != null) return object;
		else if (stringifier instanceof DateStringifier) {
			return (T) DefaultDateUtil.tryHarderToFindDefaultDateIfApplicable(context, stringifier);
		} else {
			return null;
		}
	}

	@Override
	protected Object doGetValue() {
		String string = (String)super.doGetValue();
		try {
			T javaObject = stringifier.getJavaObject(string, getDefaultValue());
			if(javaObject == null) {
				javaObject = object;
			}
			setValueValid(true);
			return javaObject;
		} catch (ParseException e) {
			setValueValid(false);
			return e;
		} catch (NumberFormatException e) {
			setValueValid(false);
			return e;
		}
	}
	
}
