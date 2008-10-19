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

package com.ryanberdeen.postal.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.message.IncomingMessage;

public class PostalClientHandler extends IoHandlerAdapter {
	@Override
	public void sessionCreated(IoSession ioSession) throws Exception {
		ioSession.setAttribute(LocalConnection.LOCAL_CONNECTION_KEY, createLocalConnection(ioSession));
	}

	protected LocalConnection createLocalConnection(IoSession ioSession) {
		return new LocalConnection(ioSession);
	}

	@Override
	public void messageReceived(IoSession ioSession, Object message) throws Exception {
		IncomingMessage postalMessage = (IncomingMessage) message;
		LocalConnection localConnection = LocalConnection.getLocalConnection(ioSession);
		postalMessage.setLocalConnection(localConnection);
		localConnection.handleIncomingMessage(postalMessage);
	}
}
