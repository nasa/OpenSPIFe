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

import static fj.data.List.*;
import static fj.pre.Equal.*;
import fj.F;
import fj.P1;
import fj.data.List;
import gov.nasa.ensemble.common.extension.ClassRegistry;

public class TimeConversions implements ITimeConversionService {
	
	public static final String UTC_SCET = "UTC/SCET";
	public static final String SCLK_SCLKD = "SCLK/SCLKD";
	public static final String LST_LMST = "LST/LMST";
	public static final String ET_SECONDS = "ET/SECONDS";

	private static final P1<ITimeConversionService> instance = 
		ClassRegistry.lazyInstance(ITimeConversionService.class, new P1<ITimeConversionService>() {
			@Override
			public ITimeConversionService _1() {
				return new MissingTimeConversionService();
			}
		});
	
	private final ITimeConversionService service;

	private TimeConversions(final ITimeConversionService service) {
		this.service = service;
	}
	
	@Override
	public List<String> getSupportedFormats() {
		return service.getSupportedFormats();
	}

	@Override
	public String convert(String inputTime, String sourceFormat, String targetFormat) throws TimeConversionException {
		checkFormats(sourceFormat, targetFormat);
		try {
			return service.convert(inputTime, sourceFormat, targetFormat);
		} catch (TimeConversionException e) {
			throw e;
		} catch (Exception e) {
			throw new TimeConversionException(e);
		}
	}
	
	public boolean supports(final String... formats) {
		return list(formats).forall(new F<String, Boolean>() {
			@Override
			public Boolean f(final String format) {
				return service.getSupportedFormats().exists(stringEqual.eq(format));
			}
		});
	}

	private void checkFormats(String ... formats) {
		for (String format : formats) {
			if (!supports(format))
				throw new TimeConversionException("Format not supported: " + format);
		}
	}
	
	public static TimeConversions from(final ITimeConversionService service) {
		return new TimeConversions(service);
	}
	
	public static TimeConversions instance() {
		return from(instance._1());
	}
	
	private static class MissingTimeConversionService implements ITimeConversionService {
		@Override
		public List<String> getSupportedFormats() {
			return nil();
		}

		@Override
		public String convert(String inputTime, String sourceFormat, String targetFormat) {
			throw new IllegalArgumentException("No time conversion service is available");
		}
	}

}
