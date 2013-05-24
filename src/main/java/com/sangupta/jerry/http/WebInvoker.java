/**
 *
 * jerry - Common Java Functionality
 * Copyright (c) 2012, Sandeep Gupta
 * 
 * http://www.sangupta/projects/jerry
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.jerry.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.XStreamUtils;

/**
 * Utility class containing methods pertaining to invocation of REST based webservices and
 * capturing the responses obtained from the same. For advanced usage, the {@link WebRequest} facade
 * can be used directly to gain much more control over the request/response handling.
 * 
 * @author sangupta
 * @since 0.1.0
 * 
 */
public class WebInvoker {
	
	/**
	 * An instance of {@link WebInvocationInterceptor} that needs to be used when handling interceptors.
	 * If an interceptor is added while a call is being made, it will not apply to that request. Interceptors
	 * will work only on a fresh request. 
	 */
	public static WebInvocationInterceptor interceptor = null;
	
	/**
	 * My private logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebInvoker.class);
	
	/**
	 * Return the HTTP response body for a GET request to the given URL. In case an {@link IOException}
	 * is thrown, it will be eaten up, logged at DEBUG level, and <code>null</code> returned.
	 * 
	 * @param url
	 * @return
	 */
	public static String fetchResponse(String url) {
		return fetchResponse(url, CookiePolicy.BEST_MATCH);
	}
	
	/**
	 * 
	 * @param url
	 * @param cookiePolicy
	 * @return
	 */
	public static String fetchResponse(String url, String cookiePolicy) {
		try {
			return WebRequest.get(url).cookiePolicy(cookiePolicy).execute().webResponse().getContent();
		} catch(IOException e) {
			logger.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static WebResponse getResponse(String url) {
		return getResponse(url, CookiePolicy.BEST_MATCH);
	}
	
	/**
	 * Return the entire response for a GET request to the given URL.
	 *  
	 * @param url
	 * @return
	 */
	public static WebResponse getResponse(String url, String cookiePolicy) {
		try {
			return WebRequest.get(url).cookiePolicy(cookiePolicy).execute().webResponse();
		} catch(Exception e) {
			logger.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}
	
	/**
	 * Returns the HTTP headers etc by making a HEAD request to the given URL as
	 * a {@link Map}.
	 * 
	 * @param url
	 */
	public static Map<String, String> getHeaders(String url, boolean followRedirects) {
		return getHeaders(url, followRedirects, CookiePolicy.BEST_MATCH);
	}
	
	/**
	 * 
	 * @param url
	 * @param followRedirects
	 * @param cookiePolicy
	 * @return
	 */
	public static Map<String, String> getHeaders(String url, boolean followRedirects, String cookiePolicy) {
		try {
			if(followRedirects) {
				return WebRequest.head(url).cookiePolicy(cookiePolicy).followRedirects().execute().webResponse().getHeaders();
			}
			
			return WebRequest.head(url).cookiePolicy(cookiePolicy).noRedirects().execute().webResponse().getHeaders();
		} catch(IOException e) {
			logger.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}
	
	/**
	 * Make a HEAD request to the URL and return the web response.
	 * 
	 * @param url
	 * @param followRedirects
	 * @return
	 */
	public static WebResponse headRequest(String url, boolean followRedirects) {
		return headRequest(url, followRedirects, CookiePolicy.BEST_MATCH);
	}
	
	/**
	 * 
	 * @param url
	 * @param followRedirects
	 * @param cookiePolicy
	 * @return
	 */
	public static WebResponse headRequest(String url, boolean followRedirects, String cookiePolicy) {
		try {
			if(followRedirects) {
				return WebRequest.head(url).cookiePolicy(cookiePolicy).followRedirects().execute().webResponse();
			}
			
			return WebRequest.head(url).cookiePolicy(cookiePolicy).noRedirects().execute().webResponse();
		} catch(IOException e) {
			logger.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}
	
	/**
	 * Invoke the given URL by the specified method and return the entire HTTP response.
	 * 
	 * @param uri
	 * @param method
	 * @return
	 */
	public static WebResponse invokeUrl(final String uri, final WebRequestMethod method) {
		WebRequest request = getWebRequest(uri, method);
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			logger.debug("Unable to fetch repsonse from url: {}", uri, e);
		}
		
		return null;
	}
	
	/**
	 * Invoke the given URL by the specified method, supplying the header and params as specified and return
	 * the entire HTTP response.
	 * 
	 * @param uri
	 * @param method
	 * @param headers
	 * @param params
	 * @return
	 */
	public static WebResponse invokeUrl(final String uri, final WebRequestMethod method, final Map<String, String> headers, final Map<String, String> params) {
		WebRequest request = getWebRequest(uri, method);
		
		if(AssertUtils.isNotEmpty(headers)) {
			for(Entry<String, String> header : headers.entrySet()) {
				request.addHeader(header.getKey(), header.getValue());
			}
		}
		
		if(AssertUtils.isNotEmpty(params)) {
			request.bodyForm(getFormParams(params));
		}
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			logger.debug("Unable to fetch repsonse from url: {}", uri, e);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param uri
	 * @param method
	 * @param requestContentType
	 * @param requestBody
	 * @return
	 */
	public static WebResponse invokeUrl(final String uri, final WebRequestMethod method, final String requestContentType, final String requestBody) {
		WebRequest request = getWebRequest(uri, method);
		
		request.bodyString(requestBody, ContentType.create(requestContentType));
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			logger.debug("Unable to fetch repsonse from url: {}", uri, e);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param uri
	 * @param object
	 * @return
	 */
	public static WebResponse postXML(final String uri, final Object object) {
		WebRequest request = getWebRequest(uri, WebRequestMethod.POST);
		
		String requestBody = XStreamUtils.getXStream(object.getClass()).toXML(object);
		request.bodyString(requestBody, ContentType.create("text/xml"));
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			logger.debug("Unable to fetch repsonse from url: {}", uri, e);
		}
		
		return null;
	}
	
	/**
	 * Get the {@link WebRequest} object for the given method.
	 * 
	 * @param uri
	 * @param method
	 * @return
	 */
	private static WebRequest getWebRequest(final String uri, final WebRequestMethod method) {
		if(method == null) {
			throw new IllegalArgumentException("WebRequestMethod cannot be null");
		}
		
		switch(method) {
			case DELETE:
				return WebRequest.delete(uri);
				
			case GET:
				return WebRequest.get(uri);
				
			case HEAD:
				return WebRequest.head(uri);
				
			case OPTIONS:
				return WebRequest.options(uri);
				
			case POST:
				return WebRequest.post(uri);
				
			case PUT:
				return WebRequest.put(uri);
				
			case TRACE:
				return WebRequest.trace(uri);
		}
		
		throw new IllegalStateException("All options of enumeration have a check above, reaching this is impossible. This is a coding horror.");
	}
	
	/**
	 * Create a {@link NameValuePair} list from the given {@link Map} of params to be passed.
	 * 
	 * @param params
	 * @return
	 */
	private static List<NameValuePair> getFormParams(Map<String, String> params) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		for(Entry<String, String> entry : params.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		
		return nvps;
	}

}
