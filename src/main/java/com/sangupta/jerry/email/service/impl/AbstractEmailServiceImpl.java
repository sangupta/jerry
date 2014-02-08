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

package com.sangupta.jerry.email.service.impl;

import com.sangupta.jerry.email.domain.Email;
import com.sangupta.jerry.email.domain.EmailAddress;
import com.sangupta.jerry.email.service.EmailService;

/**
 * An abstract implementation that completes the many utility methods
 * of the {@link EmailService} so that concrete implementations can focus
 * purely on sending the email, by a single method, {@link EmailService#sendEmail(com.boogle.email.domain.Email)}.
 * 
 * @author sangupta
 *
 */
public abstract class AbstractEmailServiceImpl implements EmailService {

	/**
	 * @see com.boogle.email.service.EmailService#sendEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendEmail(String fromAddress, String toAddress, String subject, String text) {
		Email email = new Email();
		email.setFrom(fromAddress);
		email.addTo(toAddress);
		email.setSubject(subject);
		email.setText(text);
		
		return sendEmail(email);
	}

	/**
	 * @see com.boogle.email.service.EmailService#sendEmail(com.boogle.email.domain.EmailAddress, com.boogle.email.domain.EmailAddress, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendEmail(EmailAddress fromAddress, EmailAddress toAddress, String subject, String text) {
		Email email = new Email();
		email.setFrom(fromAddress);
		email.addTo(toAddress);
		email.setSubject(subject);
		email.setText(text);
		
		return sendEmail(email);
	}
	
	/**
	 * Method to validate the provided email object for mandatory parameters.
	 * 
	 * @param email
	 * @return
	 */
	protected boolean validateEmail(Email email) {
		return true;
	}

}
