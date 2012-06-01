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

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;


/**
 * Class that encapsulates the response information obtained from invoking a HTTP URL. 
 * 
 * @author sangupta
 */
public class WebResponse {
	
	/**
     * The response code returned by the webservice invocation.
     */
    private int responseCode;
    
    /**
     * The response message returned by the webservice invocation.
     */
    private String message;
    
    /**
     * The response body returned by the webservice invocation.
     */
    private byte[] bytes;
    
    /**
     * The charset of the content received
     */
    private String charSet;
    
    private String contentType;
    
    /**
     * The response headers received
     */
    private Map<String, String> headers;
    
    /**
     * The size of the response
     */
    private long size;
    
    /**
     * Utility method to add a header to this response
     * 
     * @param header
     * @param value
     */
    public void addResponseHeader(String header, String value) {
    	if(headers == null) {
    		headers = new HashMap<String, String>();
    	}
    	
    	headers.put(header, value);
    }
    
    public String getContent() {
    	if(this.bytes != null) {
        	if(this.charSet != null) {
        		try {
					return new String(this.bytes, charSet);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
        	}

        	return new String(this.bytes);
    	}
    	
    	return null;
    }
    
    /**
     * Return the last modified date as a long value
     * 
     * @return
     */
    public long getLastModified() {
    	String headerValue = getHeader(HttpHeaders.LAST_MODIFIED);
    	if(headerValue != null) {
    		try {
    			return Date.parse(headerValue);
    		} catch(Exception e) {
    			// eat this up
    		}
    	}
    	
    	return -1;
    }
    
    private String getHeader(String headerName) {
    	if(headers == null) {
    		return null;
    	}
    	
    	return headers.get(headerName);
    }
    
    // Usual accessor's follow

    /** 
     * Returns the responseCode.
     *
     * @return the responseCode.
     */
    public int getResponseCode() {
        return this.responseCode;
    }

    /** 
     * Sets the responseCode to the specified value.
     *
     * @param responseCode responseCode to set.
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}


	/**
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}


	/**
	 * @param bytes the bytes to set
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the charSet
	 */
	public String getCharSet() {
		return charSet;
	}

	/**
	 * @param charSet the charSet to set
	 */
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

}
