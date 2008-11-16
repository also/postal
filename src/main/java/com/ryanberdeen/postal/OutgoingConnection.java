package com.ryanberdeen.postal;

import java.net.SocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;

public class OutgoingConnection extends LocalConnection {
	private static final int CONNECT_TIMEOUT = 3000;
	private IoConnector ioConnector;
	private SocketAddress socketAddress;

	public OutgoingConnection(IoConnector ioConnector, SocketAddress socketAddress) {
		this.ioConnector = ioConnector;
		this.socketAddress = socketAddress;
	}

	public void connect() {
		ConnectFuture connectFuture = ioConnector.connect(socketAddress);
		connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
		IoSession ioSession = connectFuture.getSession();
		connected(ioSession);
	}
}
