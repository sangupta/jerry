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

package com.sangupta.jerry.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * An abstract Quartz {@link Job} that is Spring-ified to include convenience methods to work in a Spring environment.
 * 
 * @author sangupta
 */
public abstract class BaseQuartzJob implements Job {

    private ApplicationContext applicationContext = null;

    /**
     * Delegate the actual job execution to {@link BaseQuartzJob#executeInternal(JobExecutionContext)} method which must
     * be overridden by subclasses. The method actually extracts the Spring {@link ApplicationContext} and allows a 
     * convenience method {@link BaseQuartzJob#getBean(String)} to return a Spring Initialized Bean.
     * 
     * @param jobExecutionContext
     * @throws JobExecutionException
     *
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public final void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	try {
	        initializeApplicationContext(jobExecutionContext);
	        executeInternal(jobExecutionContext);
    	} catch(Throwable t) {
    		getLogger().error("Unable to execute job successfully", t);
    	}
    }

    /**
     * Logger to use to when writing errors.
     * 
     * @return
     */
    protected abstract Logger getLogger();
    
    /**
     * Actual job execution method.
     * 
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    protected abstract void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException;
    
    /**
     * Initialize Spring Application Context from Quartz SchedulerContext.
     * 
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    private void initializeApplicationContext(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            this.applicationContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
            if (this.applicationContext == null) {
                throw new JobExecutionException("No application context available in scheduler context for key \"applicationContext\"");
            }
        } catch (SchedulerException e) {
            getLogger().error("Error extracting Spring application context from SchedulerContext", e);
            throw new JobExecutionException("Error fetching application context available in scheduler context for key \"applicationContext\"");
        }
    }
    
    /**
     * Utility method to extract a Spring Bean from the Spring {@link ApplicationContext} that is supplied in the Quartz
     * {@link JobExecutionContext}. The method is parameterized to use dynamic typing.
     *  
     * @param <T>
     * @param beanName
     * @param jobExecutionContext
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <T> T getBean(String beanName) {
        if(this.applicationContext != null) {
            return (T) this.applicationContext.getBean(beanName);
        }
        
        return null;
    }
    
    /**
     * Utility method to extract a Spring Bean from the Spring {@link ApplicationContext} that is supplied in the Quartz
     * {@link JobExecutionContext}. The method is parameterized to use dynamic typing.
     * @param clazz
     * @return
     */
    public final <T> T getBean(Class<T> clazz) {
    	if(this.applicationContext != null) {
            return (T) this.applicationContext.getBean(clazz);
        }
        return null;
    }
    
}
