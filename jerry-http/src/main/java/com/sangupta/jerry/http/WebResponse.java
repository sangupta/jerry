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

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpHeaders;


/**
 * Class that encapsulates the response information obtained from invoking a HTTP URL. 
 * 
 * @author sangupta
 */
public class WebResponse implements Serializable, Closeable {
	
	/**
	 * Generated using Eclipse
	 */
	private static final long serialVersionUID = 5896497905018410580L;

	/**
     * The response code returned by the webservice invocation.
     */
    int responseCode;
    
    /**
     * The response message returned by the webservice invocation.
     */
    String message;
    
    /**
     * The response body returned by the webservice invocation.
     */
    private byte[] bytes;
    
    /**
     * The charset of the content received
     */
    Charset charSet;
    
    /**
     * The content-type as specified by the server
     */
    String contentType;
    
    /**
     * The response headers received
     */
    final Map<String, String> headers = new HashMap<String, String>();
    
    /**
     * The size of the response
     */
    long size;
    
    public WebResponse(byte[] bytes) {
    	this.bytes = bytes;
    }
    
    @Override
    public void close() {
    	try {
    		InputStream stream = this.asStream();
    		if(stream != null) {
    			stream.close();
    		}
    	} catch(Exception e) {
    		// eat up
    	}
    }
    
    public String getContent() {
    	return asString(Consts.UTF_8);
    }
    
    public String asContent(Charset charset) {
    	return asString(charset);
    }
    
    /**
     * Returns the fetched response as a {@link String} parsed using the
     * response {@link Charset} or the provided {@link Charset} if none is specified.
     * 
     * @return
     */
    public String asString(Charset charset) {
    	if(this.bytes != null) {
            try {
                if (this.charSet != null) {
	                return new String(this.bytes, this.charSet.name());
                }
                
                return new String(this.bytes, charset);
            } catch (UnsupportedEncodingException ex) {
                // eat up
            }
            
            return new String(this.bytes);
    	}
    	
    	return null;
    }
    
    /**
     * Returns the fetched response as an {@link InputStream}.
     * 
     * @return
     */
    public InputStream asStream() {
    	if(this.bytes != null) {
    		return new ByteArrayInputStream(this.bytes);
    	}
    	
    	return null;
    }
    
    /**
     * Return the fetched response as a byte-array. The returned byte-array is
     * a clone of the response array.
     * 
     * @return
     */
    public byte[] asBytes() {
    	return this.bytes.clone();
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
    
    /**
     * Check if the request returned a successful response. All response codes between HTTP 200
     * and HTTP 299 are considered to be successful.
     * 
     * @return
     */
    public boolean isSuccess() {
    	if(this.responseCode >= 200 && this.responseCode <= 299) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Check if the response indicates a redirected request. All response codes between HTTP 300
     * and HTTP 399 are considered to be a redirect.
     * 
     * @return
     */
    public boolean isRedirect() {
    	if(this.responseCode >= 300 && this.responseCode <= 399) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Check if the response indicates an erroneous client request. All response codes between HTTP 400
     * and HTTP 499 are considered to be a client error.
     * 
     * @return
     */
    public boolean isClientError() {
    	if(this.responseCode >= 400 && this.responseCode <= 499) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Check if the response indicates an error on the server. All response codes between HTTP 500
     * and HTTP 599 are considered to be a server error.
     * 
     * @return
     */
    public boolean isServerError() {
    	if(this.responseCode >= 500 && this.responseCode <= 599) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Return the value of the header if present, or <code>null</code>
     * 
     * @param headerName
     * @return
     */
    private String getHeader(String headerName) {
    	if(headers == null) {
    		return null;
    	}
    	
    	return headers.get(headerName);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return this.responseCode + " " + this.message;
    }
    
    /**
     * Utility function that returns a string representation of this response
     * which can be used for debugging purposes. Should not be used in production
     * code as is slow.
     * 
     * @return
     */
    public String trace() {
    	StringBuilder builder = new StringBuilder();
    	
    	builder.append("[Response: code=");
    	builder.append(this.responseCode);
    	
    	builder.append(", message=");
    	builder.append(this.message);
    	
    	builder.append(", contentType=");
    	builder.append(this.contentType);
    	
    	builder.append(", size=");
    	builder.append(this.size);
    	
    	builder.append("]");
    	return builder.toString();
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
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the charSet
	 */
	public Charset getCharSet() {
		return charSet;
	}

}
