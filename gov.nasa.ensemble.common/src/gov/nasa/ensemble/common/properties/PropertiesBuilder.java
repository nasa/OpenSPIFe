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
package gov.nasa.ensemble.common.properties;

import static fj.P.*;

import java.util.Map.Entry;
import java.util.Properties;

import fj.F;
import fj.P2;
import fj.data.List;
import fj.data.Option;

public class PropertiesBuilder {

	private final List<P2<String, Object>> pairs;

	public PropertiesBuilder() {
		this(List.<P2<String, Object>>nil());
	}
	
	protected PropertiesBuilder(List<P2<String, Object>> pairs) {
		this.pairs = pairs;
	}
	
	public PropertiesBuilder put(final String key, final Object value) {
		return new PropertiesBuilder(pairs.removeAll(new F<P2<String, Object>, Boolean>() {
			@Override
			public Boolean f(final P2<String, Object> p) {
				return key.equals(p._1());
			}
		}).cons(p(key, value)));
	}
	
	public PropertiesBuilder put(final String key, final Option<? extends Object> value) {
		if (value.isNone())
			return this;
		return put(key, value.some());
	}
	
	public Properties build() {
		final Properties response = new Properties();
		for (final P2<String, Object> pair : pairs)
			response.put(pair._1(), pair._2());
		return response;
	}

	public static PropertiesBuilder from(final Properties properties) {
		PropertiesBuilder builder = new PropertiesBuilder();
		for (final Entry<Object,Object> entry : properties.entrySet())
			builder = builder.put((String)entry.getKey(), entry.getValue());
		return builder;
	}
	
	public static final F<PropertiesBuilder, Properties> build = new F<PropertiesBuilder, Properties>() {
		@Override
		public Properties f(final PropertiesBuilder builder) {
			return builder.build();
		}
	};
}
