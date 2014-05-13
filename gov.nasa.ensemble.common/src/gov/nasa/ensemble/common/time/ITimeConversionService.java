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
package gov.nasa.ensemble.common.time;

import fj.data.List;

/**
 * An interface representing a service for performing time conversions. It can
 * be queried for the formats it supports.
 */
public interface ITimeConversionService {
	
	/**
	 * @return a list of time formats supported by this service
	 */
	List<String> getSupportedFormats();
	
	/**
	 * @param inputTime A time in the source format
	 * @param sourceFormat The format to convert from
	 * @param targetFormat The format to convert to
	 * @return The resulting time, after converting to the target format
	 */
	String convert(String inputTime, String sourceFormat, String targetFormat) throws TimeConversionException;
}
