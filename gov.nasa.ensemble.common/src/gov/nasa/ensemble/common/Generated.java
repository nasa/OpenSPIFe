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
package gov.nasa.ensemble.common;

/**
 * The Generated annotation is used to mark source code that has been generated. It can also be used to differentiate user written
 * code from generated code in a single file.
 * 
 * When used, the value element must have the name of the code generator. The recommended convention is to use the fully qualified
 * name of the code generator in the value field. For example: com.company.package.classname.
 * 
 * The date element is used to indicate the date the source was generated. The date element must follow the ISO 8601 standard. For
 * example the date element would have the following value 2001-07-04T12:08:56.235-0700 which represents 2001-07-04 12:08:56 local
 * time in the U.S. Pacific Time time zone.
 * 
 * The comment element is a place holder for any comments that the code generator may want to include in the generated code.
 * 
 * @see <a href="http://java.sun.com/javase/6/docs/api/javax/annotation/Generated.html">Generated (Java Platform SE 6)</a>
 */
public @interface Generated {
	String[] value();

	String date() default "";

	String comments() default "";
}
