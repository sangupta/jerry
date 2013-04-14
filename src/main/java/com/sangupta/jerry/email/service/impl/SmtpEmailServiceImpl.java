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

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.sangupta.jerry.email.domain.Email;
import com.sangupta.jerry.email.domain.EmailAddress;
import com.sangupta.jerry.email.service.EmailService;
import com.sangupta.jerry.util.AssertUtils;

/**
 * @author sangupta
 *
 */
public class SmtpEmailServiceImpl extends AbstractEmailServiceImpl implements EmailService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SmtpEmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * @see com.sangupta.jerry.email.service.EmailService#sendEmail(com.sangupta.jerry.email.domain.Email)
	 */
	@Override
	public boolean sendEmail(final Email email) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			
			 public void prepare(MimeMessage mimeMessage) throws Exception {
				 if(AssertUtils.isNotEmpty(email.getTo())) {
					 for(EmailAddress address : email.getTo()) {
						 mimeMessage.setRecipient(RecipientType.TO, address.getInternetAddress());
					 }
				 }
				 
				 if(AssertUtils.isNotEmpty(email.getCc())) {
					 for(EmailAddress address : email.getCc()) {
						 mimeMessage.setRecipient(RecipientType.CC, address.getInternetAddress());
					 }
				 }
				 
				 if(AssertUtils.isNotEmpty(email.getBcc())) {
					 for(EmailAddress address : email.getBcc()) {
						 mimeMessage.setRecipient(RecipientType.BCC, address.getInternetAddress());
					 }
				 }
				 
				 if(AssertUtils.isNotEmpty(email.getReplyTo())) {
					 Address[] replyTo = new Address[email.getReplyTo().size()];
					 int counter = 0;
					 for(EmailAddress address : email.getReplyTo()) {
						 replyTo[counter++] = address.getInternetAddress(); 
					 }
					 mimeMessage.setReplyTo(replyTo);
				 }
				 
				 mimeMessage.setFrom(email.getFrom().getInternetAddress());
				 mimeMessage.setSubject(email.getSubject());
				 mimeMessage.setContent(email.getText(), "text/html; charset=utf-8");
			 }
			
		};
		
		try {
			this.mailSender.send(preparator);
			return true;
		} catch(MailException e) {
			LOGGER.error("Unable to send email", e);
		}
		
		return false;
	}
	
	// Usual accessors follow

	/**
	 * @return the mailSender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	/**
	 * @param mailSender the mailSender to set
	 */
	@Required
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
}
