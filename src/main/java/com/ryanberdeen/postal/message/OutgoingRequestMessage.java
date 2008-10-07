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

import com.ryanberdeen.postal.IdGenerator;

public class OutgoingRequestMessage extends AbstractRequestMessage implements OutgoingMessage {
	private static final String DEFAULT_REQUEST_TYPE = "GET";
	
	public OutgoingRequestMessage(IdGenerator messageIdSource) {
		this(messageIdSource, "*");
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, String url) {
		this(idSource, DEFAULT_REQUEST_TYPE, url);
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, String requestType, String url) {
		super(requestType, url);
		setHeader(MESSAGE_ID, idSource.generateId());
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, AbstractRequestMessage that) {
		super(that);
		setHeader(MESSAGE_ID, idSource.generateId());
	}
	
	@Override
	public final void setRequestType(String requestType) {
		super.setRequestType(requestType);
	}
	
	@Override
	public final void setUri(String uri) {
		super.setUri(uri);
	}
	
	@Override
	public final void setHeader(String name, String value) {
		super.setHeader(name, value);
	}
	
	@Override
	public void setContentType(String contentType) {
		super.setContentType(contentType);
	}

	@Override
	public final void setContent(byte[] content) {
		super.setContent(content);
	}
	
	@Override
	public void setContent(byte[] content, String contentType) {
		super.setContent(content, contentType);
	}

	@Override
	public final void setContent(CharSequence content) {
		super.setContent(content);
	}
}
