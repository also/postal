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
import java.net.SocketAddress;

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
	private SocketAddress socketAddress;
	private static final int CONNECT_TIMEOUT = 3000;

	public PostalClient(IoConnector ioConnector, SocketAddress socketAddress) {
		this.ioConnector = ioConnector;
		this.socketAddress = socketAddress;
		ioConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PostalProtocolCodecFactory()));
		ioConnector.setHandler(new PostalClientHandler());

	}

	/** Creates a new PostalClient that will connect to the specified socket
	 * address using a {@link NioSocketConnector}.
	 * @param socketAddress socket address to connect to
	 */
	public PostalClient(SocketAddress socketAddress) {
		this(new NioSocketConnector(), socketAddress);
	}

	public PostalClient(String host, int port) {
		this(new InetSocketAddress(host, port));
	}

	public LocalConnection connect() {
		ConnectFuture connectFuture = ioConnector.connect(socketAddress);
		connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
		ioSession = connectFuture.getSession();
		return LocalConnection.getLocalConnection(ioSession);
	}

	public void stop() {
		ioConnector.dispose();
	}
}
