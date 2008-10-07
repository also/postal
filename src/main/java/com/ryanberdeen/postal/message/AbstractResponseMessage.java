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

/** Superclass for response messages.
 * @author rberdeen
 *
 */
public abstract class AbstractResponseMessage extends AbstractMessage implements ResponseMessage {
	private int status;
	private String reason = "None";
	
	protected AbstractResponseMessage(int status) {
		this.status = status;
	}
	
	protected AbstractResponseMessage(int status, String reason) {
		this(status);
		this.reason = reason;
	}
	
	protected AbstractResponseMessage(AbstractResponseMessage that) {
		super(that);
		this.status = that.status;
		this.reason = that.reason;
	}
	
	public final int getStatus() {
		return status;
	}
	
	/** Sets the status of the message.
	 */
	protected void setStatus(int status) {
		this.status = status;
	}
	
	public final String getReason() {
		return reason;
	}
	
	/** Sets the reason for the message.
	 */
	protected void setReason(String reason) {
		this.reason = reason;
	}
	
	public final boolean isRequest() {
		return false;
	}
	
	public final String getInResponseTo() {
		return getHeader(IN_RESPONSE_TO);
	}

	@Override
	@Deprecated
	public final String getStartLine() {
		return LocalConnection.PROTOCOL + ' ' + getStatus() + ' ' + getReason();
	}
}
