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

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

/**
 * @author sangupta
 *
 */
public class WorkerGroupTest {
	
	private static final Random RANDOM = new Random();
	
	@Test
	public void testWorkerGroups() throws InterruptedException {
		WorkerGroup group = new WorkerGroup(10, "WorkerGroupTest", new Runnable() {
			
			private AtomicInteger ai = new AtomicInteger();
			
			@Override
			public void run() {
				do {
					ai.incrementAndGet();
					
					int time = RANDOM.nextInt(1000);
					try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						break;
					}
				} while(true);
				
				System.out.println("Closing thread: " + Thread.currentThread().getName());
			}
		});
		
		System.out.println("Starting at " + System.currentTimeMillis());
		group.startWorkers();
		
		Thread.sleep(5000);
		
		System.out.println("Stopping at " + System.currentTimeMillis());
		group.stopWorkers();
		System.out.println("Stopped at " + System.currentTimeMillis());
	}
	
}
