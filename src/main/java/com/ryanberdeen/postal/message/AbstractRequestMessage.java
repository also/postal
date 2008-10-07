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

import com.ryanberdeen.postal.LocalConnection;

/** Superclass for request messages.
 * @author rberdeen
 *
 */
public abstract class AbstractRequestMessage extends AbstractMessage implements RequestMessage {
	private String requestType;
	private String uri;
	
	public AbstractRequestMessage(String requestType, String uri) {
		this.requestType = requestType;
		this.uri = uri;
	}
	
	protected AbstractRequestMessage(AbstractRequestMessage that) {
		super(that);
		this.requestType = that.requestType;
		this.uri = that.uri;
	}
	
	public final boolean isRequest() {
		return true;
	}
	
	public final String getRequestType() {
		return requestType;
	}
	
	protected void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public final String getUri() {
		return uri;
	}
	
	protected void setUri(String uri) {
		this.uri = uri;
	}
	
	public final String getMessageId() {
		return getHeader(MESSAGE_ID);
	}
	
	@Override
	protected final String getStartLine() {
		return requestType + ' ' + uri + ' ' + LocalConnection.PROTOCOL_VERSION;
	}
}
