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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.AssertUtils;

/**
 * Utility class containing methods pertaining to invocation of REST based webservices and
 * capturing the responses obtained from the same.
 * 
 * @author sangupta
 */
public class WebInvoker {
	
	/**
	 * Internal handle to the http client instance
	 */
	private static HttpClient httpClient = null;
	
	/**
	 * My private logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebInvoker.class);
	
	/**
	 * Return the HTTP response body for a GET request to the given URL.
	 * 
	 * @param url
	 * @return
	 */
	public static String fetchResponse(String url) {
		WebResponse response = invokeUrl(null, WebRequestMethod.GET, (String) null, null);
		if(response != null) {
			return response.getContent();
		}
		
		return null;
	}
	
	/**
	 * Return the entire response for a GET request to the given URL.
	 *  
	 * @param url
	 * @return
	 */
	public static WebResponse getResponse(String url) {
		return invokeUrl(url, WebRequestMethod.GET, (String) null, null);
	}
	
	/**
	 * Returns the HTTP headers etc by making a HEAD request to the given URL
	 * 
	 * @param url
	 */
	public static WebResponse getHeaders(String url) {
		return invokeUrl(url, WebRequestMethod.HEAD, (String) null, null);
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
		return invokeUrl(uri, method, headers, params, null, null);
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
		return invokeUrl(uri, method, null, null, requestContentType, requestBody);
	}

	/**
	 * 
	 * @param uri
	 * @param method
	 * @param headers
	 * @param params
	 * @param requestContentType
	 * @param requestBody
	 * @return
	 */
	public static WebResponse invokeUrl(final String uri, final WebRequestMethod method, final Map<String, String> headers, final Map<String, String> params, final String requestContentType, final String requestBody) {
    	if(AssertUtils.isEmpty(uri)) {
    		return null;
    	}
    	
    	if(AssertUtils.isNotEmpty(requestBody) && method != WebRequestMethod.POST) {
    		throw new IllegalArgumentException("Request body can only be sent with POST request.");
    	}
    	
    	HttpRequestBase requestMethod = null;
        WebResponse webResponse = null;

    	switch(method) {
    		case POST:
    			requestMethod = new HttpPost(uri);
    			break;
    			
    		case DELETE:
    			requestMethod = new HttpDelete(uri);
    			break;
    			
    		case HEAD:
    			requestMethod = new HttpHead(uri);
    			break;
    			
    		case PUT:
    			requestMethod = new HttpPut(uri);
    			break;

    		case GET:
			default:
    			requestMethod = new HttpGet(uri);
    			break;
    			
    	}
    	
    	String headerValue = null;
    	
    	try {
    		logger.info("Invoking webservice at URI: " + uri);
    		
    		// set the headers if any is present
    		if(AssertUtils.isNotEmpty(headers)) {
    			for(Entry<String, String> entry : headers.entrySet()) {
    				requestMethod.addHeader(entry.getKey(), entry.getValue());
    			}
    		}
    		
    		// add the params
    		if(AssertUtils.isNotEmpty(params)) {
    			if(requestMethod instanceof HttpPost) {
        			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        			for(Entry<String, String> entry : params.entrySet()) {
        				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        			}
        			
    				HttpPost post = (HttpPost) requestMethod;
    				post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    			} else {
    				HttpParams hp = new BasicHttpParams();
    				
    				for(Entry<String, String> entry : params.entrySet()) {
    					hp.setParameter(entry.getKey(), entry.getValue());
    				}
    				
    				requestMethod.setParams(hp);
    			}
    		}
    		
    		// set the request content type
    		if(AssertUtils.isNotEmpty(requestContentType)) {
    			requestMethod.setHeader("Content-Type", requestContentType);
    		}
    		
    		// set the request body if applicable
    		if(AssertUtils.isNotEmpty(requestBody)) {
    			((HttpPost) requestMethod).setEntity(new StringEntity(requestBody));
    		}
    		
    		// check for availability of httpClient
    		if(httpClient == null) {
    			checkHttpClientAvailability();
    		}
    		
    		// Invoke the URL now
    		final HttpResponse httpResponse = httpClient.execute(requestMethod);
    		
    		final HttpEntity entity = httpResponse.getEntity();

    		// collect the data and send back to the function
    		final StatusLine statusLine = httpResponse.getStatusLine();
    		final int responseCode = statusLine.getStatusCode();
    		final String responseMessage = statusLine.getReasonPhrase();
            final String contentType;
            if(entity != null && entity.getContentType() != null) {
            	contentType = entity.getContentType().getValue();
            } else {
            	contentType = "";
            }
            
            // get response size
        	long size;
            if(entity != null) {
				size = entity.getContentLength();
            } else {
        		long value = 0;
            	Header header = httpResponse.getFirstHeader(HttpHeaders.CONTENT_LENGTH);
            	if(header != null) {
            		headerValue = header.getValue();
	            	if(AssertUtils.isNotEmpty(headerValue)) {
	            		try {
	            			value = Long.parseLong(headerValue);
	            		} catch(Exception e) {
	            			
	            		}
	            	}
            	}
            	size = value;
            }
            
            // get the response bytes
        	String charSet = null;
            byte[] responseBytes = null;
            if(entity != null) {
            	responseBytes = EntityUtils.toByteArray(entity);
				charSet = EntityUtils.getContentCharSet(entity);
            	
            }
            
            // process response cookies only if cookies were sent via the client side
            if (logger.isDebugEnabled()) {
                logger.debug("Response code: " + responseCode);
                logger.debug("Response message: " + responseMessage);
                logger.debug("Content Type: " + contentType);
                logger.debug("Content Length: " + size);
                logger.debug("CharSet: " + charSet);
            }
            
            logger.info("Webservice invocation returned with response code of " + responseCode);
            
            // encapsulate the response
			webResponse = new WebResponse();
            
            webResponse.setResponseCode(responseCode);
            webResponse.setMessage(responseMessage);
            webResponse.setCharSet(charSet);
            webResponse.setContentType(contentType);
            webResponse.setSize(size);
            webResponse.setBytes(responseBytes);
            
            // add response headers
            final Header[] responseHeaders = httpResponse.getAllHeaders();
            if(AssertUtils.isNotEmpty(responseHeaders)) {
            	for(Header header : responseHeaders) {
            		webResponse.addResponseHeader(header.getName(), header.getValue());
            	}
            }
            
    	} catch (ClientProtocolException e) {
    		logger.error("Exception invoking the webservice: " + uri, e);
		} catch (IOException e) {
			logger.error("Exception invoking the webservice: " + uri, e);
		}
		
		return webResponse;
    }
    
    /**
	 * 
	 */
	private static synchronized void checkHttpClientAvailability() {
		if(httpClient == null) {
			httpClient = HttpClientFactory.getInstance();
		}
		
		if(httpClient == null) {
			throw new RuntimeException("Unable to fetch a valid instance of HttpClient");
		}
	}

	/**
	 * @param httpClient the httpClient to set
	 */
	public static void setHttpClient(HttpClient httpClient) {
		if(httpClient == null) {
			throw new IllegalArgumentException("HttpClient cannot be null.");
		}
		
		WebInvoker.httpClient = httpClient;
	}
	
}
