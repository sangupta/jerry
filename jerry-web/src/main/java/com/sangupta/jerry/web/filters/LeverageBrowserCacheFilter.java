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

package com.sangupta.jerry.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.DateUtils;

/**
 * This filter will add cache-control response headers to static resources
 * so that they are cached on the browser side, and the server receives lesser
 * number of calls.
 *
 * @author sangupta
 */
public class LeverageBrowserCacheFilter implements Filter {

    private static final String[] STATIC_RESOURCE_EXTENSIONS = { ".css", ".png" , ".js", ".gif", ".jpg" };

    private static final Logger LOGGER = LoggerFactory.getLogger(LeverageBrowserCacheFilter.class);
    
    private static String ONE_YEAR_AS_SECONDS = String.valueOf(((long) DateUtils.ONE_YEAR / 1000l));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    /**
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String uri = request.getRequestURI();

        // check if this is a servletRequest to a static resource, like images/css/js
        // if yes, add the servletResponse header
        boolean staticResource = isStaticResource(uri);

        filterChain.doFilter(servletRequest, servletResponse);

        // if static resources
        if(staticResource) {
            LOGGER.debug("Marking URI: {} as a static resource", uri);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.addDateHeader("Expires", System.currentTimeMillis() + DateUtils.ONE_YEAR);
            response.addHeader("Cache-Control", "public, max-age=" + ONE_YEAR_AS_SECONDS);

            // turn the line below to check if a resource was cached due to this filter
            // response.addHeader("X-Filter", "LeverageBrowserCache");
        }
    }

    /**
     * Method that given a URL checks if the resources is static or not - depending on
     * the request extension like .css, .js, .png etc.
     *
     * @param uri
     * @return
     */
    private boolean isStaticResource(String uri) {
        // find extension
        int index = uri.lastIndexOf('.');
        if(index == -1) {
            return false; // no extension found
        }

        String currentExtension = uri.substring(index);
        for(String extension : STATIC_RESOURCE_EXTENSIONS) {
            if(extension.equals(currentExtension)) {
                return true;
            }
        }

        // nothing found
        return false;
    }
}
