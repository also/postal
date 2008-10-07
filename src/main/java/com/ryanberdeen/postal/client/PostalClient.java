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

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.protocol.PostalProtocolCodecFactory;

public class PostalClient {
	private IoConnector ioConnector;
	private IoSession ioSession;
	private String host;
	private int port;
	private static final int CONNECT_TIMEOUT = 3000;

	public PostalClient(String host, int port) {
		this.host = host;
		this.port = port;
		ioConnector = new NioSocketConnector();
		ioConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PostalProtocolCodecFactory()));
		ioConnector.setHandler(new PostalClientHandler());
	}

	public LocalConnection connect() {
		ConnectFuture connectFuture = ioConnector.connect(new InetSocketAddress(host, port));
		connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
		try {
			ioSession = connectFuture.getSession();
			return PostalClientHandler.getLocalConnection(ioSession);
		}
		catch (RuntimeIoException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
