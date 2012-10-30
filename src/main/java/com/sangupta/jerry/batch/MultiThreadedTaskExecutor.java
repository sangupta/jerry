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
	
	public MultiThreadedTaskExecutor(MultiThreadableOperation operation) {
		this(operation, DEFAULT_BATCH_SIZE);
	}
	
	public MultiThreadedTaskExecutor(MultiThreadableOperation operation, int batchSize) {
		if(operation == null) {
			throw new IllegalArgumentException("Operation cannot be null.");
		}
		
		this.multiThreadableOperation = operation;
		
		pool = Executors.newFixedThreadPool(batchSize);
	}
	
	public void addInvocation(final Object argument) {
		pool.submit(new Callable<Void>() {

			@SuppressWarnings("unchecked")
			@Override
			public Void call() throws Exception {
				try {
					multiThreadableOperation.runWithArguments(argument);
				} catch(Throwable t) {
					LOGGER.error("Error thrown running thread for argument " + argument.toString(), t);
				}
				
				return null;
			}
		});
	}
	
	public void waitForCompletion() throws InterruptedException {
		if(pool != null) {
			pool.shutdown();
			
			pool.awaitTermination(1, TimeUnit.DAYS);
		}
	}
	
}
