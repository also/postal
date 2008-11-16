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

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.OutgoingConnection;
import com.ryanberdeen.postal.protocol.PostalProtocolCodecFactory;

public class PostalClient {
	private IoConnector ioConnector;

	public PostalClient(IoConnector ioConnector) {
		this.ioConnector = ioConnector;
		ioConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PostalProtocolCodecFactory()));
		ioConnector.setHandler(new PostalClientHandler());
	}

	/** Creates a new PostalClient using a {@link NioSocketConnector}.
	 */
	public PostalClient() {
		this(new NioSocketConnector());
	}

	public LocalConnection connect(String hostname, int port) {
		return connect(new InetSocketAddress(hostname, port));
	}

	public LocalConnection connect(SocketAddress socketAddress) {
		OutgoingConnection connection = new OutgoingConnection(ioConnector, socketAddress);
		connection.connect();
		return connection;
	}

	public void stop() {
		ioConnector.dispose();
	}
}
