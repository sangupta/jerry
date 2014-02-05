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

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class for logging. Code should log messages using one of the methods
 * in this class rather than directly, so that more debugging information can be
 * appended to the message.
 * 
 * @author sangupta
 */
public class LogUtils {

	/**
	 * Builds a string that best suits a short log message.
	 * 
	 * @param request
	 *            an {@link HttpServletRequest} object containing the request
	 *            the client has made of the controller.
	 * 
	 * @param message
	 *            the log message intended to be written.
	 * 
	 * @return a log message enhanced with data from the request.
	 */
	public static String buildShortLogMessage(HttpServletRequest request, String message) {
		StringBuilder builder = new StringBuilder(message.length() + 1024);

		builder.append(request.getRequestURI());
		builder.append(" invoked from ");
		builder.append(request.getUserPrincipal());
		builder.append(" by ");
		builder.append(request.getRemoteUser() == null ? "unknown" : request.getRemoteUser());
		builder.append(": ");
		builder.append(message);

		return builder.toString();
	}

	/**
	 * Builds a string that best suits a log message. This method appends the
	 * provided message with as much relevant information as it can get from the
	 * provided request.
	 * 
	 * @param request
	 *            an {@link HttpServletRequest} object containing the request
	 *            the client has made of the controller.
	 * 
	 * @param message
	 *            the log message intended to be written.
	 * 
	 * @return a log message enhanced with data from the request.
	 */
	public static String buildLogMessage(HttpServletRequest request, String message) {
		StringBuilder builder = new StringBuilder(message.length() + 1024);

		builder.append(request.getRequestURI());
		builder.append(" invoked from ");
		builder.append(request.getRemoteHost());
		builder.append(" by ");
		builder.append(request.getRemoteUser() == null ? "unknown" : request.getRemoteUser());

		builder.append("\n\nRequest Parameters:\n");
		String parameters = buildRequestParametersForLog(request, 2, "\n");
		if (parameters == null) {
			builder.append("  No request parameters provided\n");
		} else {
			builder.append(parameters);
		}

		builder.append("\n\n");
		builder.append(message);

		builder.append("\n\nRequest Headers:\n");
		String headers = buildRequestHeadersForLog(request, 2, "\n");
		if (headers == null) {
			builder.append("  No request headers provided\n");
		} else {
			builder.append(headers);
		}

		return builder.toString();
	}

	/**
	 * Builds a string that best suits a log message with an error. This method
	 * appends the provided message with as much relevant information as it can
	 * get from the provided request.
	 * 
	 * @param request
	 *            an {@link HttpServletRequest} object containing the request
	 *            the client has made of the controller.
	 * 
	 * @param message
	 *            the log message intended to be written.
	 * 
	 * @param error
	 *            the error being logged.
	 * 
	 * @return a log message that includes the error, enhanced with data from
	 *         the request.
	 */
	public static String buildLogMessage(HttpServletRequest request, String message, Throwable error) {
		return buildLogMessage(request, buildLogMessage(message, error));
	}

	/**
	 * Builds a string that best suits a log message with an error.
	 * 
	 * @param message
	 *            the log message intended to be written.
	 * 
	 * @param error
	 *            the error being logged.
	 * 
	 * @return a log message that includes the error.
	 */
	public static String buildLogMessage(String message, Throwable error) {
		StringBuilder builder = new StringBuilder(2048);

		builder.append(message);

		if (error != null) {
			builder.append("; raised error:\n");
			StringWriter writer = new StringWriter();
			error.printStackTrace(new java.io.PrintWriter(writer));
			builder.append(writer.toString());

		}

		return builder.toString();
	}

	/**
	 * Builds a string from the request headers so they will fit in a log
	 * message. If the request contains no headers, this method returns
	 * <code>null</code>.
	 * 
	 * @param request
	 *            an {@link HttpServletRequest} object containing the request
	 *            the client has made of the controller.
	 * 
	 * @param indent
	 *            the number of spaces to inserted before each header.
	 * 
	 * @param split
	 *            a string inserted between each parameter.
	 * 
	 * @return a string built from the request headers, or <code>null</code> if
	 *         there aren't any.
	 */
	@SuppressWarnings("unchecked")
	public static String buildRequestHeadersForLog(HttpServletRequest request, int indent, String split) {
		StringBuilder builder = new StringBuilder(1024);
		
		String padding = "";
		for (int i = 0; i < indent; i++) {
			padding += " ";
		}

		int count = 0;
		
		// push "referrer" to the top line, so its easier to find
		String referrer = request.getHeader("referrer");
		if (referrer != null) {
			builder.append(padding).append("referrer=").append(referrer).append(split);
			count++;
		}

		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String key = headers.nextElement();
			if (!"referrer".equals(key)) {
				builder.append(padding).append(key).append("=").append(request.getHeader(key)).append(split);
				count++;
			}
		}
		
		if (count == 0) {
			return null;
		}
		
		return builder.toString();
	}

	/**
	 * Builds a string from the request parameters so they will fit in a log
	 * message. If the request contains no parameters, this method returns
	 * <code>null</code>.
	 * 
	 * @param request
	 *            an {@link HttpServletRequest} object containing the request
	 *            the client has made of the controller.
	 * 
	 * @param indent
	 *            the number of spaces to indent each parameter.
	 * 
	 * @param split
	 *            a string inserted between each parameter.
	 * 
	 * @return a string built from the request parameters, or <code>null</code>
	 *         if there aren't any.
	 */
	@SuppressWarnings("unchecked")
	public static String buildRequestParametersForLog(HttpServletRequest request, int indent, String split) {
		StringBuilder builder = new StringBuilder(1024);
		
		String padding = "";
		for (int i = 0; i < indent; i++) {
			padding += " ";
		}
		
		Enumeration<String> parameters = request.getParameterNames();
		int count = 0;
		
		while (parameters.hasMoreElements()) {
			String key = parameters.nextElement();
			String[] values = request.getParameterValues(key);
			builder.append(padding).append(key).append("=");
			
			if (values.length == 1) {
				builder.append(values[0]);
			} else if (values.length > 1) {
				builder.append("[");
				for (int i = 0; i < values.length; i++) {
					if (i > 0)
						builder.append(",");
					builder.append(values[i]);
				}
				builder.append("]");
			}
			builder.append(split);
			count++;
		}
		
		if (count == 0) {
			return null;
		}
		
		return builder.toString();
	}

	/**
	 * Returns the string representation of the given object by invoking the
	 * <code>toString</code> method, if available, else returns a string
	 * representation as generated by {@link LogUtils#forceDumpAsString(Object)}
	 * method.<br/>
	 * <br/>
	 * <b>It is not recommended to use this method in PRODUCTION environment, as
	 * reflection on object might slow execution down.</b>
	 * 
	 * @param object the object to be dumped as string
	 * 
	 * @return {@link String} representation of the object
	 */
	public static final String dumpAsString(Object object) {
		StringBuilder result = new StringBuilder();

		if (object == null) {
			result.append(StringUtils.BLANK_STRING);
		} else {
			result.append("(Param Class: ");
			result.append(object.getClass().getName());
			result.append("; Param Value: ");

			// check if object implements toString method, then use that
			// otherwise
			// dump using the string
			boolean foundMethod = false;
			Method[] methods = object.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals("toString")) {
					// the class declares a toString method, return invocation
					// of this.
					result.append(object.toString());
					foundMethod = true;
					break;
				}
			}
			if (!foundMethod) {
				// no declaring method was found - use reflection to dump the
				// object's content
				result.append(forceDumpAsString(object));
			}
		}

		result.append(" )");
		
		return result.toString();
	}

	/**
	 * Build an object dump as string by calling each and every no-argument
	 * getter on the object, and creating a list of all such attributes, which
	 * is then suffixed to the object's class name. The method can be used to
	 * generate <code>toString</code> methods for value-objects while
	 * development.<br/>
	 * <br/>
	 * <b>It is NOT recommended to use this method excessively in PRODUCTION
	 * environment, as it might slow down the execution.</b>
	 * 
	 * @param object
	 * 
	 * @return
	 */
	public static final String forceDumpAsString(Object object) {
		if (object == null) {
			return StringUtils.BLANK_STRING;
		}

		StringBuilder result = new StringBuilder();

		Method[] methods = object.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
				String propertyNameWithoutFirstCharacter = method.getName().substring(4);
				String propertyNamefirstCharacter = method.getName().substring(3, 4).toLowerCase();
				String propertyName = propertyNamefirstCharacter + propertyNameWithoutFirstCharacter;

				Object propertyValue;

				try {
					propertyValue = method.invoke(object);
				} catch (Exception e) {
					propertyValue = "(EXCEPTION invoking getter: " + e.getMessage() + ")";
				}

				result.append("{");
				result.append(propertyName);
				result.append(": ");
				result.append(dumpAsString(propertyValue));
				result.append("}");
			}
		}

		return result.toString();
	}

}