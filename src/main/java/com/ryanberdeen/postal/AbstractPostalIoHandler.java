package com.ryanberdeen.postal;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.ryanberdeen.postal.message.IncomingMessage;

public abstract class AbstractPostalIoHandler extends IoHandlerAdapter {
	@Override
	public void sessionCreated(IoSession ioSession) throws Exception {
		ioSession.setAttribute(LocalConnection.LOCAL_CONNECTION_KEY, createLocalConnection(ioSession));
	}

	protected abstract LocalConnection createLocalConnection(IoSession ioSession);

	@Override
	public void messageReceived(IoSession ioSession, Object message) throws Exception {
		IncomingMessage postalMessage = (IncomingMessage) message;
		LocalConnection localConnection = LocalConnection.getLocalConnection(ioSession);
		postalMessage.setLocalConnection(localConnection);
		localConnection.handleIncomingMessage(postalMessage);
	}
}
