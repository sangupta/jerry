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

package com.sangupta.jerry.email.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Information on one email that needs to be sent.
 * 
 * @author sangupta
 *
 */
public class Email {
	
	private EmailAddress from;
	
	private Set<EmailAddress> to;
	
	private Set<EmailAddress> cc;
	
	private Set<EmailAddress> bcc;
	
	private Set<EmailAddress> replyTo;

	private String subject;
	
	private String text;
	
	/**
	 * Default constructor
	 * 
	 */
	public Email() {
		
	}
	
	// Convenience methods
	
	public void setFrom(String fromAddress) {
		this.from = new EmailAddress(fromAddress);
	}
	
	public void addTo(String toAddress) {
		addTo(new EmailAddress(toAddress));
	}
	
	public void addTo(EmailAddress address) {
		if(this.to == null) {
			this.to = new HashSet<EmailAddress>();
		}
		
		this.to.add(address);
	}
	
	public void addCc(String toAddress) {
		addCc(new EmailAddress(toAddress));
	}
	
	public void addCc(EmailAddress address) {
		if(this.cc == null) {
			this.cc = new HashSet<EmailAddress>();
		}
		
		this.cc.add(address);
	}
	
	public void addBcc(String toAddress) {
		addBcc(new EmailAddress(toAddress));
	}
	
	public void addBcc(EmailAddress address) {
		if(this.bcc == null) {
			this.bcc = new HashSet<EmailAddress>();
		}
		
		this.bcc.add(address);
	}
	
	public void addReplyTo(String replyTo) {
		addReplyTo(new EmailAddress(replyTo));
	}
	
	/**
	 * @param emailAddress
	 */
	public void addReplyTo(EmailAddress emailAddress) {
		if(this.replyTo == null) {
			this.replyTo = new HashSet<EmailAddress>();
		}
		
		this.replyTo.add(emailAddress);
	}

	// Usual accessors follow

	/**
	 * @return the from
	 */
	public EmailAddress getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(EmailAddress from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Set<EmailAddress> getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Set<EmailAddress> to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public Set<EmailAddress> getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(Set<EmailAddress> cc) {
		this.cc = cc;
	}

	/**
	 * @return the bcc
	 */
	public Set<EmailAddress> getBcc() {
		return bcc;
	}

	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(Set<EmailAddress> bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return the replyTo
	 */
	public Set<EmailAddress> getReplyTo() {
		return replyTo;
	}

	/**
	 * @param replyTo the replyTo to set
	 */
	public void setReplyTo(Set<EmailAddress> replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
}
