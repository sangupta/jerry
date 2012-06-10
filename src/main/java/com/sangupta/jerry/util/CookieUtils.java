package com.sangupta.jerry.util;

import javax.servlet.http.Cookie;

public class CookieUtils {
	
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
	 * Set the max age of the cookie in number of days.
	 * 
	 * @param cookie
	 * @param i
	 */
	public static void setMaxAgeInDays(Cookie cookie, int i) {
		if(cookie == null) {
			return;
		}
		
		if(i < 0) {
			return;
		}
		
		cookie.setMaxAge(i * (24 * 60 * 60));
	}
	
}
