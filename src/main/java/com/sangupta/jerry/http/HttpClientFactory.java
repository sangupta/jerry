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

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

/**
 * @author sangupta
 *
 */
public class HttpClientFactory {

	/**
	 * Defines the maximum number of total connections we will maintain
	 */
	private static int MAX_TOTAL_CONNECTIONS = 500;

	/**
	 * Defines the maximum number of connections per route that can be opened.
	 */
	private static int MAX_CONNECTIONS_PER_ROUTE = 50;

	/**
	 * Time in seconds in which connections held by {@link HttpUtil} and {@link AuthenticatedHttpUtil}
	 * will be timed out and the call rejected.
	 */
	public static int CONNECTION_TIME_OUT = 300;
	
	/**
	 * Time in seconds in which calls via {@link HttpUtil} and {@link AuthenticatedHttpUtil} will be failed
	 * if no packets are received on the socket.
	 */
	public static int SOCKET_TIME_OUT = 180;
	
	/**
	 * Number of entries to cache when fetching URLs via the {@link HttpUtil} and {@link AuthenticatedHttpUtil}
	 */
	public static int MAX_CACHE_ENTRIES = 10000;
	
	/**
	 * The maximum size of the entry in bytes which will be cached by the {@link HttpUtil} and {@link AuthenticatedHttpUtil}
	 */
	public static int MAX_CACHE_ENTRY_SIZE = 32768;

    /**
     *  Create an HttpClient with the ThreadSafeClientConnManager.
     *  This connection manager must be used if more than one thread will
     *  be using the HttpClient.
     */
	private static ThreadSafeClientConnManager HTTP_CONNECTION_MANAGER;
	
	/**
	 * The singleton instance of HttpClient
	 */
	private static HttpClient httpClient = null;

	/**
	 * Specifies if we need to use caching during connections or not
	 */
	private static boolean enableCaching = true;
	
	/**
	 * Specifies whether we need to allow all SSL hostnames or not
	 */
	private static boolean allowAllSSLHostnames = true;
	
	private static boolean initialized = false;

	private static synchronized void initialize() {
		if(initialized) {
			return;
		}
		
		HTTP_CONNECTION_MANAGER = new ThreadSafeClientConnManager();
		
		if(MAX_TOTAL_CONNECTIONS > 0) {
			HTTP_CONNECTION_MANAGER.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		}
		
		if(MAX_CONNECTIONS_PER_ROUTE > 0) {
			HTTP_CONNECTION_MANAGER.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);
		}
    	
    	if(enableCaching) {
	    	CacheConfig cacheConfig = new CacheConfig();  
	    	cacheConfig.setMaxCacheEntries(MAX_CACHE_ENTRIES);
	    	cacheConfig.setMaxObjectSizeBytes(MAX_CACHE_ENTRY_SIZE);
	    	
	    	httpClient = new CachingHttpClient(new DefaultHttpClient(HTTP_CONNECTION_MANAGER), cacheConfig);
    	}

    	if(httpClient == null) {
    		httpClient = new DefaultHttpClient(HTTP_CONNECTION_MANAGER);
    	}
    	
    	if(allowAllSSLHostnames) {
	    	SSLSocketFactory sf = (SSLSocketFactory)httpClient.getConnectionManager().getSchemeRegistry().getScheme("https").getSchemeSocketFactory();
	    	sf.setHostnameVerifier(new AllowAllHostnameVerifier());
    	}
    	
    	HttpParams httpParams = httpClient.getParams();
    	
    	if(SOCKET_TIME_OUT > 0) {
    		httpParams.setIntParameter("http.socket.timeout", SOCKET_TIME_OUT * 1000);
    	}
    	
    	if(CONNECTION_TIME_OUT > 0) {
    		httpParams.setIntParameter("http.connection.timeout", CONNECTION_TIME_OUT * 1000);
    	}
    	
    	initialized = true;
	}
	
	public static HttpClient getInstance() {
		if(!initialized) {
			initialize();
		}
		
		return httpClient;
	}
}
