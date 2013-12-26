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

package com.sangupta.jerry.oauth.domain;

/**
 * Constants with regards to OAuth workflows.
 * 
 * @author sangupta
 *
 */
public interface OAuthConstants {
	
	public static final String OAUTH_SIGNATURE = "oauth_signature";
	
	public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	
	public static final String OAUTH_TOKEN = "oauth_token";
	
	public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	
	public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	
	public static final String OAUTH_NONCE = "oauth_nonce";
	
	public static final String OAUTH_VERSION = "oauth_version";
	
	public static final String X_AUTH_MODE = "x_auth_mode";
	
	public static final String X_AUTH_USERNAME = "x_auth_username";
	
	public static final String X_AUTH_PASSWORD = "x_auth_password";
	
	public static final String DEFAULT_XAUTH_MODE = "client_auth";
	
	public static final String OAUTH_VERSION_1_0 = "1.0";
	
	public static final String OAUTH_AUTHORIZATION_HEADER_NAME = "OAuth";
	
}