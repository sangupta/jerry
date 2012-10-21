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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.sangupta.jerry.util.AssertUtils;

/**
 * {@link ResponseHandler} class that handles the extract the {@link WebResponse} object
 * from the provided {@link HttpResponse} object.
 * 
 * @author sangupta
 * @since 0.3
 * @added 20 October 2012
 */
public class WebResponseHandler implements ResponseHandler<WebResponse> {

	/**
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	@Override
	public WebResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        
		final WebResponse webResponse = new WebResponse(EntityUtils.toByteArray(entity));
		
		// decipher from status line
		webResponse.responseCode = statusLine.getStatusCode();
		webResponse.message = statusLine.getReasonPhrase();
		
		// set size
		if(entity != null) {
			webResponse.size = entity.getContentLength();
        } else {
    		long value = 0;
        	Header header = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH);
        	if(header != null) {
        		String headerValue = header.getValue();
            	if(AssertUtils.isNotEmpty(headerValue)) {
            		try {
            			value = Long.parseLong(headerValue);
            		} catch(Exception e) {
            			// eat the exception
            		}
            	}
        	}
        	
        	webResponse.size = value;
        }
		
		// content type
		if(entity != null && entity.getContentType() != null) {
        	webResponse.contentType = entity.getContentType().getValue();
        }
		
		// response headers
		final Header[] responseHeaders = response.getAllHeaders();
        if(AssertUtils.isNotEmpty(responseHeaders)) {
        	for(Header header : responseHeaders) {
        		webResponse.headers.put(header.getName(), header.getValue());
        	}
        }
		
		// charset
		ContentType type = ContentType.get(entity);
		if(type != null) {
			webResponse.charSet = type.getCharset();
		}
		// return the object finally
        return webResponse;
	}

}
