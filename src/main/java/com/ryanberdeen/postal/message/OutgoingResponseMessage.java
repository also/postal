/*
 * Copyright 2008 Ryan Berdeen.
 *
 * This file is part of Postal.
 *
 * Postal is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Postal is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Postal.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ryanberdeen.postal.message;

public class OutgoingResponseMessage extends AbstractResponseMessage implements OutgoingMessage {
	private static final int DEFAULT_STATUS = 200;
	
	/** Constructs a response message with the default status code 200.
	 * @param inResponseToMessage
	 */
	public OutgoingResponseMessage(RequestMessage inResponseToMessage) {
		this(inResponseToMessage, DEFAULT_STATUS);
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, int status) {
		super(status);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, CharSequence content) {
		this(inResponseToMessage, DEFAULT_STATUS);
		setContent(content);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, int status, CharSequence content) {
		this(inResponseToMessage, status);
		setContent(content);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, AbstractResponseMessage that) {
		super(that);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
	}
	
	@Override
	public final void setStatus(int status) {
		super.setStatus(status);
	}

	@Override
	public  void setContent(byte[] content) {
		super.setContent(content);
	}

	@Override
	public final void setContent(CharSequence content) {
		super.setContent(content);
	}

	@Override
	public final void setHeader(String name, String value) {
		super.setHeader(name, value);
	}
	
	@Override
	public final void setContentType(String contentType) {
		super.setContentType(contentType);
	}
}
