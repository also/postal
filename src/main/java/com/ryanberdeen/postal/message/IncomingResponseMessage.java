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

public class IncomingResponseMessage extends AbstractResponseMessage implements IncomingMessage {
	private LocalConnection localConnection;

	public IncomingResponseMessage(int status, String reason) {
		super(status, reason);
	}

	@Deprecated
	public IncomingResponseMessage(LocalConnection connection, int status, String reason) {
		super(status, reason);
		this.localConnection = connection;
	}

	public final LocalConnection getLocalConnection() {
		return localConnection;
	}

	public void setLocalConnection(LocalConnection localConnection) {
		this.localConnection = localConnection;
	}
}
