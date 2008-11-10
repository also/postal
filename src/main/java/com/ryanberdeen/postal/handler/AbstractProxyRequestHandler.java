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

package com.ryanberdeen.postal.handler;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.ryanberdeen.postal.Connection;
import com.ryanberdeen.postal.ConnectionLifecycleListener;
import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.ProxiedConnection;
import com.ryanberdeen.postal.ResponseHandler;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.IncomingResponseMessage;
import com.ryanberdeen.postal.message.OutgoingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;


/** Handles a request by sending it to another connection.
 * @author rberdeen
 *
 */
public abstract class AbstractProxyRequestHandler implements RequestHandler {

	private static final String PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX = AbstractProxyRequestHandler.class.getName() + ".proxiedConnections."; 
	
	protected abstract LocalConnection getTargetConnection(IncomingRequestMessage request);
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage incomingRequest) throws IOException {
		LocalConnection targetConnection;
		
		String targetConnectionId = incomingRequest.getHeader(ProxiedConnection.TARGET_CONNECTION_ID_HEADER_NAME);
		String proxiedConnectionId = null;
		if (targetConnectionId != null) {
			targetConnection = (LocalConnection) incomingRequest.getLocalConnection().getAttribute(PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX + targetConnectionId);
		}
		else {
			targetConnection = getTargetConnection(incomingRequest);
			proxiedConnectionId = incomingRequest.getLocalConnection().getConnectionId();
			targetConnection.getAttribute(PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX + proxiedConnectionId, new NewProxiedConnectionCallable(targetConnection, incomingRequest.getLocalConnection()));
		}
		
		sendProxiedRequest(targetConnection, incomingRequest, proxiedConnectionId);
		
		// the response will be sent by the proxy response handler
		return null;
	}
	
	public static void sendProxiedRequest(LocalConnection target, IncomingRequestMessage incomingRequest, String proxiedConnectionId) throws IOException {
		OutgoingRequestMessage outgoingRequest = new OutgoingRequestMessage(target, incomingRequest);
		if (proxiedConnectionId != null) {
			outgoingRequest.setHeader(ProxiedConnection.PROXIED_CONNECTION_ID_HEADER_NAME, proxiedConnectionId);
		}
		target.sendRequest(outgoingRequest, new ProxyResponseHandler(incomingRequest));
	}
	
	private static class NewProxiedConnectionCallable implements Callable<LocalConnection> {
		private LocalConnection targetConnection;
		private LocalConnection proxiedConnection;
		
		NewProxiedConnectionCallable(LocalConnection targetConnection, LocalConnection proxiedConnection) {
			this.targetConnection = targetConnection;
			this.proxiedConnection = proxiedConnection;
		}
		
		public LocalConnection call() throws Exception {
			proxiedConnection.addConnectionLifecycleListener(new ProxiedConnectionClosedListener(targetConnection, proxiedConnection));
			return proxiedConnection;
		}
	}
	
	private static class ProxiedConnectionClosedListener implements ConnectionLifecycleListener {
		private LocalConnection targetConnection;
		private LocalConnection proxiedConnection;
		
		ProxiedConnectionClosedListener(LocalConnection targetConnection, LocalConnection proxiedConnection) {
			this.targetConnection = targetConnection;
			this.proxiedConnection = proxiedConnection;
		}

		public void connectionClosed(Connection connection) {
			targetConnection.setAttribute(PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX + proxiedConnection.getConnectionId(), null);

			targetConnection.sendRequest(new OutgoingRequestMessage(targetConnection, "/connection/proxied/" + proxiedConnection.getConnectionId() + "/closed"));
		}
	}
	
	/** Handles a response by sending it to the original requestor.
	 * @author rberdeen
	 *
	 */
	private static class ProxyResponseHandler implements ResponseHandler<Object> {
		private IncomingRequestMessage request;
		
		public ProxyResponseHandler(IncomingRequestMessage request) {
			this.request = request;
		}
		
		public Object handleResponse(IncomingResponseMessage targetResponse) throws Exception {
			OutgoingResponseMessage response = new OutgoingResponseMessage(request, targetResponse);
			request.getConnection().sendResponse(response);
			
			return null;
		}
	}
}
