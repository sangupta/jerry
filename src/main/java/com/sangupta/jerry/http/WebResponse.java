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
    private String responseMessage;
    
    /**
     * The response body returned by the webservice invocation.
     */
    private byte[] bytes;
    
    /**
     * The content that was received as part of the response body
     */
    private String content;
    
    /**
     * The reponse content type as returned by the webservice invocation.
     */
    private String contentType;
    
    /**
     * The last modified header in case it was specified
     */
    private long lastModified;
    
    /**
     * The size of the response
     */
    private long size;
    
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
     * Returns the responseMessage.
     *
     * @return the responseMessage.
     */
    public String getResponseMessage() {
        return this.responseMessage;
    }

    /** 
     * Sets the responseMessage to the specified value.
     *
     * @param responseMessage responseMessage to set.
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
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
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
