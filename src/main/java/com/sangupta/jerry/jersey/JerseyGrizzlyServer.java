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

package com.sangupta.jerry.jersey;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ConsoleUtils;

/**
 * @author sangupta
 *
 */
public class JerseyGrizzlyServer {

	/**
	 * The server URL where we will hook up.
	 */
	private final String serverURL;
	
	/**
	 * Keeps track of whether the server is running or not.
	 * 
	 */
	private volatile boolean started = false;
	
	/**
	 * The thread selector obtained from Grizzly container
	 */
    private final HttpServer httpServer;
    
    /**
     * 
     * @param serverURL
     * @param customJerseyWebservice
     */
    public JerseyGrizzlyServer(final String serverURL, final String customJerseyWebservice) {
    	this(serverURL, new String[] { customJerseyWebservice });
    }
    
	/**
	 * Create a new {@link LineUpServer} instance also loading
	 * custom webservices from the package provided. The webservices
	 * must be Jersey-enabled to work properly.
	 * 
	 * @param serverURL
	 * @param customJerseyWebservices
	 */
	public JerseyGrizzlyServer(final String serverURL, final String... customJerseyWebservices) {
		if(AssertUtils.isEmpty(serverURL)) {
			throw new IllegalArgumentException("Server URL must be provided, cannot be null/empty");
		}
		
		this.serverURL = serverURL;
		
		if(AssertUtils.isEmpty(customJerseyWebservices)) {
			throw new IllegalArgumentException("Atleast one custom webservice package must be specified.");
		}
		
		ResourceConfig config = new ResourceConfig().packages(customJerseyWebservices);
		this.httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(this.serverURL), config, false);
	}
	
	/**
	 * 
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void startServer() throws IOException {
		if(this.started) {
			throw new IllegalStateException("Server is already running.");
		}

		this.httpServer.start();
		this.started = true;
	}
	
	/**
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void startServerBlocking() {
		this.registerShutdownHook();
		
		try {
			this.startServer();
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Unable to start the server", e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to start the server", e);
		}
		
		do {
			String input = ConsoleUtils.readLine("Type exit to stop server: ", false);
			if("exit".equalsIgnoreCase(input)) {
				break;
			}
		} while(true);
		
		this.stopServer();
	}
	
	/**
	 * Stop the currently running server.
	 * 
	 * @throws IllegalStateException if the server is already stopped.
	 * 
	 */
	public void stopServer() {
		if(!this.started) {
			throw new IllegalStateException("Server has not yet started.");
		}
		
		this.httpServer.stop();
		this.started = false;
	}
	
	/**
	 * Returns whether the server is running or not.
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return this.started;
	}
	
	/**
	 * Method that will register a shutdown hook so that the server
	 * can be closed, when the application exits.
	 * 
	 */
	public void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			/**
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				super.run();
				
				if(started) {
					stopServer();
				}
			}
			
		});
	}
}
