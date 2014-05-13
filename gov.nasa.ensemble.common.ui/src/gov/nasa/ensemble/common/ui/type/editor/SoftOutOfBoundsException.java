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

/**
 * The SOFT out of bounds exception, is an indication to "clients"
 * that it is acceptable to use a notification instead of an error.
 * 
 * For example if an out of bounds exception is informative instead of just
 * being an error use SOFT instead of a normal OutOfBoundsException
 * 
 * @author alexeiser
 *
 */
public class SoftOutOfBoundsException extends OutOfBoundsException {
	private static final long serialVersionUID = -5356918315603559821L;

	public SoftOutOfBoundsException(String message, int i, Object javaObject) {
		super(message, i, javaObject);
	}

}
