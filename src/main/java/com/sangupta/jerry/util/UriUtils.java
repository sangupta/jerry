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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility functions around the URI.
 * 
 * @author sangupta
 *
 */
public class UriUtils {

	/**
	 * Characters that are allowed in a URI.
	 */
	private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UriUtils.class);

	/**
	 * Function to convert a given string into URI encoded format.
	 * 
	 * @param input
	 *            the source string
	 * 
	 * @return the encoded string
	 */
	public static String encodeURIComponent(String input) {
		if (AssertUtils.isEmpty(input)) {
			return input;
		}

		int l = input.length();
		StringBuilder output = new StringBuilder(l * 3);
		try {
			for (int i = 0; i < l; i++) {
				String e = input.substring(i, i + 1);
				if (ALLOWED_CHARS.indexOf(e) == -1) {
					byte[] b = e.getBytes("utf-8");
					output.append(StringUtils.getHex(b));
					continue;
				}
				output.append(e);
			}
			return output.toString();
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Unable to encode bytes to UTF-8", e);
		}
		
		return input;
	}
	
	/**
	 * Function to decode a given string from URI encoded format.
	 * 
	 * @param encodedURI the encoded string component
	 * @return the decoded string
	 */
	public static String decodeURIComponent(String encodedURI) {
		if(AssertUtils.isEmpty(encodedURI)) {
			return encodedURI;
		}
		
		char actualChar;
		StringBuffer buffer = new StringBuffer();

		int bytePattern, sumb = 0;

		for (int index = 0, more = -1; index < encodedURI.length(); index++) {
			actualChar = encodedURI.charAt(index);

			switch (actualChar) {
				case '%': {
					actualChar = encodedURI.charAt(++index);
					int hb = (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
					actualChar = encodedURI.charAt(++index);
					int lb = (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
					bytePattern = (hb << 4) | lb;
					break;
				}

				case '+': {
					bytePattern = ' ';
					break;
				}

				default: {
					bytePattern = actualChar;
				}
			}

			if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
				sumb = (sumb << 6) | (bytePattern & 0x3f);
				if (--more == 0)
					buffer.append((char) sumb);
			} else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
				buffer.append((char) bytePattern);
			} else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
				sumb = bytePattern & 0x1f;
				more = 1;
			} else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
				sumb = bytePattern & 0x0f;
				more = 2;
			} else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
				sumb = bytePattern & 0x07;
				more = 3;
			} else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
				sumb = bytePattern & 0x03;
				more = 4;
			} else { // 1111110x
				sumb = bytePattern & 0x01;
				more = 5;
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * Extract the file name from the URL removing the scheme, domain,
	 * query params and named anchor, if present.
	 * 
	 * @param url the URL to be used
	 * 
	 * @return extracted filename from the URL
	 */
	public static String extractName(String url) {
		int index1 = url.indexOf('?');
		int index2 = url.indexOf('#');
		
		if(index1 == -1) {
			index1 = url.length() + 1;
		}
		
		if(index2 == -1) {
			index2 = url.length() + 1;
		}
		
		int index = Math.min(index1, index2);
		if(index < url.length()) {
			url = url.substring(0, index);
		}
		
		index1 = url.lastIndexOf('/');
		index2 = url.lastIndexOf('\\');
		
		index = Math.max(index1, index2);
		
		url = url.substring(index + 1);
		
		return url;
	}

	/**
	 * Extract the filename from the URL.
	 * 
	 * @param url
	 * @return
	 */
	public static String extractFileName(String url) {
		int index = url.lastIndexOf('/');
		
		if(index == -1) {
			return null;
		}
		
		url = url.substring(index + 1);
		
		// query param
		int end = url.indexOf('?');
		if(end != -1) {
			url = url.substring(0, end);
		}
		
		// anchor name
		end = url.indexOf('#');
		if(end != -1) {
			url = url.substring(0, end);
		}
		
		return url; 
	}

	/**
	 * Extract the extension from the URL
	 * 
	 * @param url
	 * @return
	 */
	public static String extractExtension(String url) {
		// check for any slash characters that remain
		int index = url.lastIndexOf('/');
		if(index != -1) {
			url = url.substring(index + 1);
		}

		// now for the dot part
		index = url.lastIndexOf('.');
		
		// check if extension present
		if(index == -1) {
			return null;
		}
		
		url = url.substring(index + 1);
		
		// query param
		int end = url.indexOf('?');
		if(end != -1) {
			url = url.substring(0, end);
		}
		
		// anchor name
		end = url.indexOf('#');
		if(end != -1) {
			url = url.substring(0, end);
		}
		
		return url;
	}

	/**
	 * Encode the given set of parameters into a URL format, considering that the parameter
	 * values are already encoded.
	 * 
	 * @param params
	 * @return
	 */
	public static String urlEncode(Map<String, String> params) {
		return urlEncode(params, false);
	}

	/**
	 * 
	 * @param testParams
	 * @return
	 */
	public static String urlEncode(Map<String, String> params, boolean encodeValues) {
		if(AssertUtils.isEmpty(params)) {
			return StringUtils.EMPTY_STRING;
		}
		
		Set<Entry<String, String>> entrySet = params.entrySet();
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(Entry<String, String> entry : entrySet) {
			if(!first) {
				builder.append("&");
			}
			
			first = false;
			
			builder.append(entry.getKey());
			builder.append("=");
			
			if(encodeValues) {
				builder.append(encodeURIComponent(entry.getValue()));
			} else {
				builder.append(entry.getValue());
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * Normalizes a given URL. Add the protocol if necessary, convert domain to lower-case,
	 * remove port number if it is 80, align the query parameters, then sort them, remove
	 * the anchor link, change https to http
	 * 
	 * @param url
	 * @return
	 */
	public static String normalizeUrl(String taintedURL) {
		if(AssertUtils.isEmpty(taintedURL)) {
			return taintedURL;
		}
		
		int hasProtocol = taintedURL.indexOf("://");
		if(hasProtocol == -1) {
			// no protocol found
			// append HTTP to the URL
			taintedURL = "http://" + taintedURL;
		}
		
		final URL url;
        try {
            url = new URL(taintedURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL: " + taintedURL);
        }

        final String path = url.getPath().replace("/$", "");
        final SortedMap<String, String> params = createParameterMap(url.getQuery());
        final int port = url.getPort();
        final String queryString;

        if (params != null) {
            queryString = "?" + canonicalize(params);
        } else {
            queryString = "";
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(url.getProtocol());
        sb.append("://");
        sb.append(url.getHost());
        if(port != -1 && port != 80) {
        	sb.append(":");
        	sb.append(port);
        }
        sb.append(path);
        sb.append(queryString);
        
        return sb.toString();
	}
	
	/**
     * Takes a query string, separates the constituent name-value pairs, and
     * stores them in a SortedMap ordered by lexicographical order.
     * @return Null if there is no query string.
     */
    private static SortedMap<String, String> createParameterMap(final String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }

        final String[] pairs = queryString.split("&");
        final Map<String, String> params = new HashMap<String, String>(pairs.length);

        for (final String pair : pairs) {
            if (pair.length() < 1) {
                continue;
            }

            String[] tokens = pair.split("=", 2);
            for (int j = 0; j < tokens.length; j++) {
                try {
                    tokens[j] = URLDecoder.decode(tokens[j], "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            
            switch (tokens.length) {
                case 1:
                    if (pair.charAt(0) == '=') {
                        params.put("", tokens[0]);
                    } else {
                        params.put(tokens[0], "");
                    }
                    
                    break;

                case 2:
                    params.put(tokens[0], tokens[1]);
                    break;
            }
        }

        return new TreeMap<String, String>(params);
    }
    
    /**
     * Canonicalize the query string.
     *
     * @param sortedParamMap Parameter name-value pairs in lexicographical order.
     * @return Canonical form of query string.
     */
    private static String canonicalize(final SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap == null || sortedParamMap.isEmpty()) {
            return "";
        }

        final StringBuffer sb = new StringBuffer(350);
        final Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();

        while (iter.hasNext()) {
            final Map.Entry<String, String> pair = iter.next();
            sb.append(percentEncodeRfc3986(pair.getKey()));
            sb.append('=');
            sb.append(percentEncodeRfc3986(pair.getValue()));
            if (iter.hasNext()) {
                sb.append('&');
            }
        }

        return sb.toString();
    }

    /**
     * Percent-encode values according the RFC 3986. The built-in Java URLEncoder does not encode
     * according to the RFC, so we make the extra replacements.
     *
     * @param string Decoded string.
     * @return Encoded string per RFC 3986.
     */
    private static String percentEncodeRfc3986(final String string) {
        try {
            return URLEncoder.encode(string, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

	/**
	 * Extract the host value from the URL.
	 * 
	 * @param url
	 * @return
	 */
	public static String extractHost(String url) {
		try {
			URI uri = new URI(url);
			return uri.getHost();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Extract the protocol or the scheme from the given URL. For example,
	 * in the URL http://www.sangupta.com, the protocol is http.
	 * 
	 * @param href
	 * @return
	 */
	public static String extractProtocol(String url) {
		if(url == null) {
			return null;
		}
		
		try {
			URI uri = new URI(url);
			return uri.getScheme();
		} catch (URISyntaxException e) {
			// eat up
		}
		
		// we will try and fall back to approach of sub-strings
		int index = url.indexOf("://");
		if(index == -1) {
			return null;
		}
		
		return url.substring(0, index);	
	}

	/**
	 * Extract the base url (scheme + domain) from the given URL.
	 * 
	 * @param url
	 *            the url from which the information needs to be extracted
	 * 
	 * @return the base URL as extracted, or <code>null</code> in case the URL
	 * 		   is empty or cannot be parsed properly.
	 */
	public static String getBaseUrl(String url) {
		if(AssertUtils.isEmpty(url)) {
			return null;
		}

		int index = url.indexOf("://");
		index = url.indexOf('/', index + 3);
		
		if(index == -1) {
			return null;
		}
		
		return url.substring(0, index);
	}
	
	/**
	 * Remove the scheme and domain name from the url and return the entire
	 * path, query string and name anchor, if present.
	 * 
	 * @param url
	 *            the URL from which the information that needs to be stripped
	 *            off
	 * 
	 * @return the url without scheme and domain, or <code>null</code> in case
	 *         the URL is empty or cannot be parsed properly.
	 * 
	 */
	public static String removeSchemeAndDomain(String url) {
		if(AssertUtils.isEmpty(url)) {
			return null;
		}
		
		int index = url.indexOf("://");
		index = url.indexOf('/', index + 3);
		
		if(index == -1) {
			return null;
		}
		
		return url.substring(index + 1);
	}

}
