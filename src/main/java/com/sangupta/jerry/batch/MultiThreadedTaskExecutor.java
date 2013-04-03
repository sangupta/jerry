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

package com.sangupta.jerry.batch;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author sangupta
 *
 */
@SuppressWarnings("rawtypes")
public class MultiThreadedTaskExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadedTaskExecutor.class);

	private static final int DEFAULT_BATCH_SIZE = 10;
	
	private final MultiThreadableOperation multiThreadableOperation;
	
	private ExecutorService pool = null;
	
	/**
	 * Provides a user defined-name to this object so that errors can be traced easily.
	 * 
	 */
	private String name;
	
	public MultiThreadedTaskExecutor(String name, MultiThreadableOperation operation, boolean fixedSizePool) {
		this(name, operation, DEFAULT_BATCH_SIZE, fixedSizePool);
	}
	
	public MultiThreadedTaskExecutor(String name, MultiThreadableOperation operation, int batchSize) {
		this(name, operation, batchSize, false);
	}
	
	private MultiThreadedTaskExecutor(String name, MultiThreadableOperation operation, int batchSize, boolean fixedSizePool) {
		if(operation == null) {
			throw new IllegalArgumentException("Operation cannot be null.");
		}
		
		this.name = name;
		this.multiThreadableOperation = operation;
		
		if(fixedSizePool) {
			pool = Executors.newFixedThreadPool(batchSize);
		} else {
			pool = Executors.newCachedThreadPool();
		}
		
		LOGGER.debug("Starting executor pool: {}", this.name);
	}
	
	public Future<Void> addInvocation(final Object argument) {
		return pool.submit(new Callable<Void>() {

			@SuppressWarnings("unchecked")
			@Override
			public Void call() throws Exception {
				try {
					multiThreadableOperation.runWithArguments(argument);
				} catch(Throwable t) {
					LOGGER.error("[{}] Error thrown running thread for argument {}" + argument.toString(), name, t);
				}
				
				return null;
			}
		});
	}
	
	public void waitForCompletion() throws InterruptedException {
		if(pool != null) {
			try {
				pool.shutdown();
				
				pool.awaitTermination(1, TimeUnit.DAYS);
			} catch(Throwable t) {
				LOGGER.error("Unable to complete jobs and shutdown pool", t);
			}
		}
	}
	
	public void cleanUp() {
		if(pool != null && !pool.isShutdown()) {
			try {
				pool.shutdownNow();
			} catch(Throwable t) {
				LOGGER.error("Unable to shutdown pool", t);
			}
		}
	}
	
}
