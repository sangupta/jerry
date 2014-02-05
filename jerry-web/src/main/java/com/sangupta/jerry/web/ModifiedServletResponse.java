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

package com.sangupta.jerry.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * A wrapper object that holds a modified response to be
 * sent back to the client.
 * 
 * @author sangupta
 *
 */
public class ModifiedServletResponse {

    final byte[] bytes;

    final Map<String, Object> headers = new HashMap<String, Object>();

    Set<Cookie> cookies;

    int statusCode;

    String charset;

    String contentType;

    String characterEncoding;

    Locale locale;

    public ModifiedServletResponse(HttpServletResponseWrapperImpl wrapper, byte[] byteResponse) {
        if(wrapper.headers != null) {
            this.headers.putAll(wrapper.headers);
        }
        this.cookies = wrapper.cookies;
        this.statusCode = wrapper.statusCode;
        this.charset = wrapper.charset;
        this.contentType = wrapper.contentType;
        this.characterEncoding = wrapper.characterEncoding;
        this.locale = wrapper.locale;

        this.bytes = byteResponse;
    }

    /**
     * Copy this modified response to the given {@link ServletResponse} object. The flush method is
     * invoked in the end.
     * 
     * @param response
     * @throws java.io.IOException
     */
    public void copyToResponse(ServletResponse response) throws IOException {
        this.copyToResponse((HttpServletResponse) response);
    }

    /**
     * Copy this modified response to the given {@link HttpServletResponse} object. The flush method is
     * invoked in the end.
     * 
     * @param response
     * @throws IOException
     */
    public void copyToResponse(HttpServletResponse response) throws IOException {
        // character encoding
        if(this.characterEncoding != null) {
            response.setCharacterEncoding(this.characterEncoding);
        }

        // charset
        if(this.charset != null) {
            response.setCharacterEncoding(charset);
        }

        // content type
        if(this.contentType != null) {
            response.setContentType(this.contentType);
        }

        // set the status
        if(this.statusCode > 0) {
            response.setStatus(this.statusCode);
        }

        // set the locale
        if(this.locale != null) {
            response.setLocale(this.locale);
        }

        // copy and add all cookies
        if(this.cookies != null) {
            for(Cookie cookie : this.cookies) {
                response.addCookie(cookie);
            }
        }

        // copy the byte array stream and also set its length
        final OutputStream finalOut = response.getOutputStream();
        int length = this.bytes.length;
        response.setContentLength(length);

        // write the entire byte chunk
        finalOut.write(this.bytes);

        finalOut.flush();
    }
}
