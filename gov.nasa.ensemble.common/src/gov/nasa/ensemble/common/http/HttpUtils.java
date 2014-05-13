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
package gov.nasa.ensemble.common.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	
	public static final int DFAULT_HTTP_PORT = 80;
	public static final int DFAULT_HTTPS_PORT = 443;
	
	public static void execute(HttpClient client, HttpRequestBase request, HttpResponseHandler handler) throws Exception {
		HttpResponse response = null;
		try {
			response = client.execute(request);
			if (handler != null) {
				handler.handleResponse(response);
			}
		} finally {
			HttpUtils.consume(request, response);
		}
	}
	
	public static <T> T execute(HttpClient client, HttpRequestBase request, HttpResponseHandlerWithResult<T> handler) throws Exception {
		T result = null;
		HttpResponse response = null;
		try {
			response = client.execute(request);
			if (handler != null) {
				result = handler.handleResponse(response);
			}
		} finally {
			HttpUtils.consume(request, response);
		}
		return result;
	}

	public static void consume(HttpRequestBase request, HttpResponse response) throws IOException {
		if (response != null) {
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				if (request != null) {
					throw new IOException("releasing entity for "+request.getMethod(), e);
				} else {
					throw new IOException("releasing entity", e);
				}
			}
		}
	}
	
	public static abstract class HttpResponseHandler {
		
		public abstract void handleResponse(HttpResponse response) throws Exception;
		
	}
	
	public static abstract class HttpResponseHandlerWithResult<T extends Object> {
		
		public abstract T handleResponse(HttpResponse response) throws Exception;
		
	}
	
}
