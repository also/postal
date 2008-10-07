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

package com.ryanberdeen.postal.server;

import org.apache.mina.core.session.IoSession;

import com.ryanberdeen.postal.ConnectionManager;
import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.client.PostalClientHandler;

public class PostalServerHandler extends PostalClientHandler {
	private ConnectionManager connectionManager;

	public PostalServerHandler(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	@Override
	protected LocalConnection createLocalConnection(IoSession ioSession) {
		return connectionManager.createConnection(ioSession);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}
}