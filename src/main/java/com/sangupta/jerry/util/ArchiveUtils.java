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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sangupta
 *
 */
public class ArchiveUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveUtils.class);
	
	public static List<File> unpackTAR(final File inputFile, final File outputDir) throws ArchiveException, IOException {
		LOGGER.info(String.format("Untaring %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

	    final List<File> untaredFiles = new LinkedList<File>();
	    final InputStream is = new FileInputStream(inputFile); 
	    final TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
	    TarArchiveEntry entry = null; 
	    while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
	        final File outputFile = new File(outputDir, entry.getName());
	        if (entry.isDirectory()) {
	        	LOGGER.debug(String.format("Attempting to write output directory %s.", outputFile.getAbsolutePath()));
	        	
	            if (!outputFile.exists()) {
	            	LOGGER.debug(String.format("Attempting to create output directory %s.", outputFile.getAbsolutePath()));
	                
	            	if (!outputFile.mkdirs()) {
	                    throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
	                }
	            }
	        } else {
	        	LOGGER.debug(String.format("Creating output file %s.", outputFile.getAbsolutePath()));
	        	
	            final OutputStream outputFileStream = new FileOutputStream(outputFile); 
	            IOUtils.copy(debInputStream, outputFileStream);
	            outputFileStream.close();
	        }
	        untaredFiles.add(outputFile);
	    }
	    debInputStream.close(); 

	    return untaredFiles;
	}

	/**
	 * @param file
	 * @param tempDir
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static List<File> unpackGZIP(File file, File tempDir) throws IOException {
		String outputFileName = GzipUtils.getUncompressedFilename(file.getName());
		File outputFile = new File(tempDir, outputFileName);
		
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream in = new BufferedInputStream(fin);
		FileOutputStream out = new FileOutputStream(outputFile);
		GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
		
		org.apache.commons.io.IOUtils.copy(gzIn, out);
		
		out.close();
		gzIn.close();
		in.close();
		fin.close();
		
		List<File> files = new ArrayList<File>();
		files.add(outputFile);
		
		return files;
	}

}
