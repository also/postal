package com.ryanberdeen.postal;

import org.apache.mina.core.session.IoSession;

public class IncomingConnection extends LocalConnection {
	public IncomingConnection(IoSession ioSession, String connectionId) {
		super(connectionId);
		connected(ioSession);
	}
}
