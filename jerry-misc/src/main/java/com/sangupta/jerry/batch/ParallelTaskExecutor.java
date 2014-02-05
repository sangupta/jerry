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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author sangupta
 *
 */
public class ParallelTaskExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParallelTaskExecutor.class);
	
	private List<Runnable> tasks = null;
	
	private boolean executionStarted = false;
	
	public synchronized void addTask(Runnable task) {
		if(this.executionStarted) {
			throw new IllegalStateException("Tasks cannot be executed once the executor has started running.");
		}
		
		if(tasks == null) {
			tasks = new ArrayList<Runnable>();
		}
		
		tasks.add(task);
	}
	
	public synchronized void run() {
		if(this.executionStarted) {
			throw new IllegalStateException("Executor already running.");
		}
		
		this.executionStarted = true;
		
		ExecutorService pool = null;
		try {
			// create the pool and store all future's
			pool = Executors.newFixedThreadPool(tasks.size());
			List<Future<?>> futures = new ArrayList<Future<?>>();
			for(Runnable task : tasks) {
				futures.add(pool.submit(task));
			}
			
			// check on all future's
			for(Future<?> future : futures) {
				try {
					future.get();
				} catch(Throwable t) {
					LOGGER.error("Unable to run the thread.", t);
				}
			}
			
			pool.shutdown();
			
			LOGGER.debug("returning from parallel task executor.");
		} catch(Throwable t) {
			LOGGER.error("Error running parallel tasks.", t);
		}
	}

}
