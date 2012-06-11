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

package com.sangupta.jerry.appupdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.UriUtils;

import com.thoughtworks.xstream.XStream;

/**
 * Wraps the functionality of checking for application updates, and allowing the user to
 * download them. This is particularly useful for command line JAVA applications.
 * 
 * @author sangupta
 *
 */
public class ApplicationUpdater {

	/**
	 * My private logger
	 */
	private final static Log logger = LogFactory.getLog(ApplicationUpdater.class);
	
	/**
	 * Method that checks for application updates and returns 
	 * 
	 * @param applicationUpdateUrl
	 */
	public static XmlUpdateDefinition checkForApplicationUpdates(String applicationUpdateUrl, String applicationVersion) {
		return updatesAvailable(applicationUpdateUrl, applicationVersion);
	}

	/**
	 * Method that checks if there is an application update available on the web or not. If yes, the 
	 * method will download the application update (JAR/WAR etc) to the local disk.
	 * 
	 * @param applicationUpdateUrl the update URL where update.xml definition can be found
	 * 
	 * @param applicationVersion the current application version number
	 * 
	 * @param downloadDirectory the directory to download the file in. If the supplied directory is empty, the method
	 * will download the file in the current directory as returned by the system.
	 */
	public static void checkAndDownloadForApplicationUpdates(String applicationUpdateUrl, String applicationVersion, File downloadDirectory) {
		// check if we have an update available
		XmlUpdateDefinition definition = updatesAvailable(applicationUpdateUrl, applicationVersion);
		
		// if not, exit
		if(definition == null) {
			return;
		}
		
		// if yes, download the file
		logger.info("Downloading application version: " + definition.getVersion() + " from url: " + definition.getUrl() + "...");

		// go ahead and download the updated file
		WebResponse response = WebInvoker.getResponse(definition.getUrl());
		if(response == null) {
			return;
		}
		
		byte[] updatedFile = response.getBytes();
		if(updatedFile != null) {
			// write this file to disk
			// form the name of the file
			String fileName = UriUtils.extractFileName(applicationUpdateUrl);
			
			try {
				File jarFile;
				if(downloadDirectory == null) {
					jarFile = new File(fileName);
				} else {
					jarFile = new File(downloadDirectory, fileName);
				}
				
				// write the file to disk
				OutputStream out = null;
		        try {
		            out = new FileOutputStream(jarFile, false);
		            out.write(updatedFile);
		            out.close(); // don't swallow close Exception if copy completes normally
		        } finally {
		            IOUtils.closeQuietly(out);
		        }
		        
				logger.info("Updated application written to disk as: " + jarFile.getAbsolutePath());
			} catch (IOException e) {
				logger.error("Unable to write downloaded JAR file.", e);
			}
		}
	}
	
	/**
	 * Method that checks if there are updates available at the given application
	 * update URL.
	 * 
	 * @param applicationUpdateUrl the URL of the update.xml file on the web
	 * 
	 * @return the update definition if there is an update available, <code>null</code> if not.
	 */
	private static XmlUpdateDefinition updatesAvailable(String applicationUpdateUrl, String applicationVersion) {
		if(AssertUtils.isEmpty(applicationUpdateUrl)) {
			throw new IllegalArgumentException("Application update URL cannot be null/empty.");
		}
		
		if(AssertUtils.isEmpty(applicationVersion)) {
			throw new IllegalArgumentException("Application version cannot be null/empty.");
		}
		
		WebResponse response = WebInvoker.getResponse(applicationUpdateUrl);
		if(response.getResponseCode() != 200) {
			// unable to fetch app update definition
			return null;
		}
		
		String responseXml = response.getContent();
		XStream xStream = new XStream();
		xStream.processAnnotations(XmlUpdateDefinition.class);
		XmlUpdateDefinition definition = (XmlUpdateDefinition) xStream.fromXML(responseXml);
		
		if(isUpdateAvailable(applicationVersion, definition.getVersion())) {
			return definition;
		}
		
		// no update available
		// simply return
		return null;
	}

	/**
	 * Method that decides if the current application version is older than the version available
	 * on the web.
	 * 
	 * @param currentApplicationVersion the current application version
	 * @param onWebVersion the application version on the web
	 * 
	 * @return <code>true</code> if the current version is older, <code>false</code> otherwise.
	 */
	private static boolean isUpdateAvailable(String currentApplicationVersion, String onWebVersion) {
		if(AssertUtils.isEmpty(currentApplicationVersion)) {
			return false;
		}
		
		if(AssertUtils.isEmpty(onWebVersion)) {
			return false;
		}
		
		// split the version on dot symbol
		String[] current = currentApplicationVersion.split(".");
		String[] available = onWebVersion.split(".");
		
		// get the min number of tokens
		int count = Math.min(current.length, available.length);
		
		boolean updateAvailable = false;
		boolean equals = true;
		for(int index = 0; index < count; index++) {
			String curr = current[index];
			String avail = available[index];
			
			int currentNum = Integer.parseInt(curr);
			int availableNum = Integer.parseInt(avail);
			
			if(availableNum > currentNum) {
				updateAvailable = true;
				break;
			}
			
			if(availableNum != currentNum) {
				equals = false;
				break;
			}
		}
		
		if(updateAvailable) {
			return true;
		}
		
		if(equals && (available.length > current.length)) {
			return true;
		}
		
		return false;
	}
	
}
