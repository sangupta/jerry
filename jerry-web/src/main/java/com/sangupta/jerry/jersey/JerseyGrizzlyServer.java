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
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ConsoleUtils;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

/**
 * A simple utility class to start a Grizzly-based Jersey webservices server
 * from a command-line application to server various web requests. Comes in
 * handy when you want to expose simple webservices, may be for a command line
 * tool.
 * 
 * @author sangupta
 *
 */
public class JerseyGrizzlyServer {

	/**
	 * The server URL where we will hook up.
	 */
	private final String serverURL;
	
	/**
	 * Initialization parameters for grizzly container
	 */
	private final Map<String, String> initParams;
	
	/**
	 * Keeps track of whether the server is running or not.
	 * 
	 */
	private volatile boolean started = false;
	
	/**
	 * The thread selector obtained from Grizzly container
	 */
    private SelectorThread threadSelector = null;
    
    /**
     * Construct an instance of the {@link JerseyGrizzlyServer}.
     *  
     * @param serverURL 
     * 
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
		initParams = new HashMap<String, String>();
		
		if(AssertUtils.isEmpty(customJerseyWebservices)) {
			throw new IllegalArgumentException("Atleast one custom webservice package must be specified.");
		}
		
		final StringBuilder packages = new StringBuilder();
		for(String customPackage : customJerseyWebservices) {
			packages.append(' ');
			packages.append(customPackage);
		}
		
		initParams.put("com.sun.jersey.config.property.packages", packages.toString());
	}
	
	/**
	 * Add a new initialization parameter.
	 * 
	 * @param paramName
	 * @param paramValue
	 */
	public void addInitParam(String paramName, String paramValue) {
		this.initParams.put(paramName, paramValue);
	}
	
	/**
	 * Start the server.
	 *  
	 * @throws IOException
	 *  
	 * @throws IllegalArgumentException 
	 * 
	 * @throws IllegalStateException if the server is already running.
	 * 
	 */
	public void startServer() throws IllegalArgumentException, IOException {
		this.startServer(null);
	}
	
	/**
	 * 
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void startServer(ApplicationContext context) throws IllegalArgumentException, IOException {
		if(this.started) {
			throw new IllegalStateException("Server is already running.");
		}
		
		if(context != null) {
			SpringServletWithCustomApplicationContext.setConfigurableApplicationContext((ConfigurableApplicationContext) context);
			this.threadSelector = GrizzlyWebContainerFactory.create(serverURL, SpringServletWithCustomApplicationContext.class, initParams);
		} else {
			this.threadSelector = GrizzlyWebContainerFactory.create(serverURL, initParams);
		}
		
		this.threadSelector.setReuseAddress(false);
		this.threadSelector.setSocketKeepAlive(false);
		
		this.started = true;
	}
	
	/**
	 * 
	 */
	public void startServerBlocking() {
		this.startServerBlocking(null, null);
	}
	
	public void startServerBlocking(String message) {
		this.startServerBlocking(null, message);
	}
	
	public void startServerBlocking(ApplicationContext context) {
		this.startServerBlocking(context, null);
	}
	
	/**
	 * 
	 * @param context the Spring {@link ApplicationContext} that needs to be passed to
	 * IOC container.
	 * 
	 * @param message the message to be used to display when holding user input to unblock
	 * server.
	 * 
	 * @throws RuntimeException in case we are unable to start the server
	 * 
	 */
	public void startServerBlocking(final ApplicationContext context, String message) {
		this.registerShutdownHook();
		
		try {
			this.startServer(context);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Unable to start the server", e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to start the server", e);
		}

		if(message == null) {
			message = "Type exit to stop the server: ";
		}
		
		do {
			String input = ConsoleUtils.readLine(message, false);
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
		
		if(this.threadSelector != null) {
			this.threadSelector.stopEndpoint();
		}
		
		this.started = false;
		this.threadSelector = null;
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
