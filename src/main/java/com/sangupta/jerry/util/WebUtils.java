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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebResponse;

/**
 * @author sangupta
 *
 */
public class WebUtils {
	
	/**
	 * Download the URL at the given location URL and store it as a temporary file on disk.
	 * The temporary file is set to be deleted at the exit of the application.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static File downloadToTempFile(String url) throws IOException {
		String extension = UriUtils.extractExtension(url);
		File tempFile = File.createTempFile("download", extension);
		tempFile.deleteOnExit();
		
		WebResponse response = WebInvoker.getResponse(url);
		if(response != null) {
			if(response.getResponseCode() == 200 && response.getBytes() != null) {
				FileUtils.writeByteArrayToFile(tempFile, response.getBytes());
				return tempFile;
			}
		}
		
		return null;
	}

}
