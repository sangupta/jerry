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

package com.sangupta.jerry.util;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Utility functions to work with cookies.
 * 
 * @author sangupta
 *
 */
public class CookieUtils {
	
	/**
	 * Create a new cookie.
	 * 
	 * @param name
	 * @param value
	 * @param domain
	 * @param path
	 * @return
	 */
	public static Cookie createCookie(String name, String value, String domain, String path) {
		Cookie cookie = new Cookie(name, value);
		
		if(domain != null) {
			cookie.setDomain(domain);
		}
		
		if(path != null) {
			cookie.setPath(path);
		}
		
		return cookie;
	}
	
	/**
	 * Create a cookie with the given amount of time.
	 * 
	 * @param name
	 * @param value
	 * @param domain
	 * @param path
	 * @param days
	 * @return
	 */
	public static Cookie createCookie(String name, String value, String domain, String path, int days) {
		Cookie cookie = createCookie(name, value, domain, path);
		setMaxAgeInDays(cookie, days);
		return cookie;
	}
	
	public static Cookie createSessionCookie(String name, String value, String domain, String path) {
		Cookie cookie = createCookie(name, value, domain, path);
		return cookie;
	}
	
	public static void deleteCookie(Cookie cookie) {
		if(cookie != null) {
			cookie.setMaxAge(0);
		}
	}
	
	/**
	 * Fetch the {@link Cookie} from the available cookies as passed from the
	 * {@link HttpServletRequest}.
	 * 
	 * @param cookies
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(Cookie[] cookies, String cookieName) {
		if(cookieName == null || cookieName.length() == 0) {
			return null;
		}
		
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Return multiple cookies with the given name.
	 * 
	 * @param cookies
	 * @param cookieName
	 * @return
	 */
	public static Cookie[] getCookiesWithName(Cookie[] cookies, String cookieName) {
		if(cookieName == null || cookieName.length() == 0) {
			return null;
		}
		
		Cookie[] found = new Cookie[cookies.length];
		int count = 0;
		
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(cookieName)) {
					found[count++] = cookie;
				}
			}
		}
		
		if(count < found.length) {
			return Arrays.copyOfRange(found, 0, count);
		}
		
		return found;
	}
	
	/**
	 * Fetch the value of the cookie.
	 * 
	 * @param cookies
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(Cookie[] cookies, String cookieName) {
		Cookie cookie = getCookie(cookies, cookieName);
		if(cookie == null) {
			return null;
		}
		
		return cookie.getValue();
	}

	/**
	 * Set the max age of the cookie in number of days.
	 * 
	 * @param cookie
	 * @param i
	 */
	public static void setMaxAgeInDays(Cookie cookie, int days) {
		setMaxAgeInHours(cookie, 24 * days);
	}
	
	/**
	 * Set the max age of the cookie in number of days.
	 * 
	 * @param cookie
	 * @param i
	 */
	public static void setMaxAgeInHours(Cookie cookie, int hours) {
		setMaxAgeInMinutes(cookie, 60 * hours);
	}
	
	/**
	 * Set the max age of the cookie in number of days.
	 * 
	 * @param cookie
	 * @param i
	 */
	public static void setMaxAgeInMinutes(Cookie cookie, int minutes) {
		if(cookie == null) {
			return;
		}
		
		if(minutes < 0) {
			return;
		}
		
		cookie.setMaxAge(minutes * 60);
	}
	
}
