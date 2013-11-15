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

package com.sangupta.jerry.oauth;

import java.util.HashMap;
import java.util.Map;

import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.oauth.domain.OAuthConstants;
import com.sangupta.jerry.oauth.domain.OAuthSignatureMethod;

/**
 * @author sangupta
 *
 */
public class OAuthClient {

	private final String consumerKey;
	
	private final String consumerSecret;
	
	private final OAuthSignatureMethod signatureMethod;
	
	private final String oAuthVersion;
	
	private final String authorizationHeader;
	
	private final boolean includeOAuthParamsInBody;
	
	public OAuthClient(String consumerKey, String consumerSecret) {
		this(consumerKey, consumerSecret, OAuthSignatureMethod.HMAC_SHA1, OAuthConstants.OAUTH_VERSION_1_0, OAuthConstants.OAUTH_AUTHORIZATION_HEADER_NAME, false);
	}
	
	public OAuthClient(String consumerKey, String consumerSecret, boolean includeOAuthParamsInBody) {
		this(consumerKey, consumerSecret, OAuthSignatureMethod.HMAC_SHA1, OAuthConstants.OAUTH_VERSION_1_0, OAuthConstants.OAUTH_AUTHORIZATION_HEADER_NAME, includeOAuthParamsInBody);
	}
	
	public OAuthClient(String consumerKey, String consumerSecret, OAuthSignatureMethod signatureMethod, String oauthVersion) {
		this(consumerKey, consumerSecret, signatureMethod, oauthVersion, OAuthConstants.OAUTH_AUTHORIZATION_HEADER_NAME, false);
	}
	
	public OAuthClient(String consumerKey, String consumerSecret, OAuthSignatureMethod signatureMethod, String oauthVersion, String authorizationHeader, boolean includeOAuthParamsInBody) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.signatureMethod = signatureMethod;
		this.oAuthVersion = oauthVersion;
		this.authorizationHeader = authorizationHeader;
		this.includeOAuthParamsInBody = includeOAuthParamsInBody;
	}
	
	/**
	 * @param endPoint
	 * @param post
	 * @param oAuthClient
	 * @param userName
	 * @param password
	 * @return
	 */
	public WebRequest createXAuthRequest(String endPoint, String userName, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(OAuthConstants.X_AUTH_USERNAME, userName);
		params.put(OAuthConstants.X_AUTH_PASSWORD, password);
		params.put(OAuthConstants.X_AUTH_MODE, OAuthConstants.DEFAULT_XAUTH_MODE);
		
//		return OAuthUtils.createOAuthRequest(endPoint, WebRequestMethod.POST, this.signatureMethod, this.oAuthVersion, this.authorizationHeader, this.consumerKey, this.consumerSecret, params, this.includeOAuthParamsInBody);
		return null;
	}

	/**
	 * @param endPoint
	 * @param post
	 * @param user
	 * @param asMap
	 */
	public WebRequest createUserSignedOAuthRequest(String endPoint, WebRequestMethod method, OAuthUser user, Map<String, String> params) {
		return null;
		// return OAuthUtils.createUserSignedOAuthRequest(endPoint, method, this.signatureMethod, this.oAuthVersion, this.authorizationHeader, this.consumerKey, this.consumerSecret, user.getTokenKey(), user.getTokenSecret(), params, this.includeOAuthParamsInBody);
	}

}
