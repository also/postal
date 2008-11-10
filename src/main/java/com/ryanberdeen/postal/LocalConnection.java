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

package com.ryanberdeen.postal;

import java.io.Closeable;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.mina.core.session.IoSession;

import com.ryanberdeen.postal.handler.DefaultRequestHandlerMapping;
import com.ryanberdeen.postal.handler.RequestHandler;
import com.ryanberdeen.postal.message.IncomingMessage;
import com.ryanberdeen.postal.message.IncomingRequestHeaders;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.IncomingResponseMessage;
import com.ryanberdeen.postal.message.OutgoingMessage;
import com.ryanberdeen.postal.message.OutgoingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;
import com.ryanberdeen.postal.message.RequestMessage;

/** A local (not proxied) connection.
 *
 */
public class LocalConnection extends AbstractConnection implements Closeable {
	public static final String PROTOCOL = "P2PR";
	public static final String VERSION = "0.1";
	public static final String PROTOCOL_VERSION = PROTOCOL + '/' + VERSION;
	public static final String LOCAL_CONNECTION_KEY = "localConnection";
	
	private IoSession ioSession;

	private DefaultRequestHandlerMapping requestHandlerMapping = new DefaultRequestHandlerMapping();
	
	/** Used to execute request and response handlers. */
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	private HashMap<String, ResponseHandlerFutureTask<?>> responseHandlers = new HashMap<String, ResponseHandlerFutureTask<?>>();
	
	boolean running = false;
	
	public LocalConnection(IoSession ioSession) {
		this.ioSession = ioSession;
	}
	
	public LocalConnection(IoSession ioSession, String connectionId) {
		super(connectionId);
		this.ioSession = ioSession;
	}
	
	public DefaultRequestHandlerMapping getRequestHandlerMapping() {
		return requestHandlerMapping;
	}
	
	public void handleIncomingMessage(IncomingMessage message) {
		try {
			if (message.isRequest()) {
				RequestHandler requestHandler = requestHandlerMapping.getHandler((IncomingRequestHeaders) message);
				
				if (requestHandler != null) {
					RequestHandlerRunnable runnable = new RequestHandlerRunnable(requestHandler, (IncomingRequestMessage) message);
					executorService.execute(runnable);
				}
				else {
					RequestMessage request = (RequestMessage) message;
					sendResponse(new OutgoingResponseMessage(request, 404, "No handler configured for request (URI: " + request.getUri() + ")"));
				}
			}
			else {
				IncomingResponseMessage response = (IncomingResponseMessage) message;
				ResponseHandlerFutureTask<?> handler = responseHandlers.remove(response.getInResponseTo());
				if (handler != null) {
					handler.setResponse(response);
					executorService.execute(handler);
				}
				else {
					// the response was ignored
				}
			}
		}
		catch (Throwable t) {
			// TODO shouldn't close connection on all exceptions
			handle(t);
		}
	}
	
	/** Sends a request whose response will be handled by the specified handler.
	 * @param <V> the result type of the<code>handleResponse</code> method
	 * @param request the request message to send
	 * @param responseHandler the response message handler, <code>null</code> to ignore response
	 * @return a {@link Future} allowing access to the result of the response handler, <code>null</code> if response will be ignored
	 */
	public <V> Future<V> sendRequest(OutgoingRequestMessage request, ResponseHandler<V> responseHandler) {
		ResponseHandlerFutureTask<V> future = null;
		if (responseHandler != null) {
			future = new ResponseHandlerFutureTask<V>(responseHandler);
			responseHandlers.put(request.getMessageId(), future);
		}
		doSend(request);
		return future;
	}
	
	/** Sends the message.
	 */
	private void doSend(OutgoingMessage message) {
		ioSession.write(message);
		onSend(message);
	}
	
	public void sendResponse(OutgoingResponseMessage response) {
		doSend(response);
	}
	
	private <T extends Throwable> T handle(T t) {
		// TODO
		t.printStackTrace();
		close();

		return t;
	}
	
	public void close() {
		running = false;
		
		// TODO
		try {
			for (ResponseHandlerFutureTask<?> futureResponseHandler : responseHandlers.values()) {
				futureResponseHandler.cancel(false);
			}
			ioSession.close();
		}
		finally {
			onClose();
		}
	}
	
	/** Handles the specified request with the specified handler when run.
	 * @author Ryan Berdeen
	 *
	 */
	private class RequestHandlerRunnable implements Runnable {
		private RequestHandler requestHandler;
		private IncomingRequestMessage request;
		
		public RequestHandlerRunnable(RequestHandler requestHandler, IncomingRequestMessage request) {
			this.requestHandler = requestHandler;
			this.request = request;
		}
		
		public void run() {
			OutgoingResponseMessage response = null;
			try {
				response = requestHandler.handleRequest(request);
			}
			catch (Throwable t) {
				// TODO log
				t.printStackTrace();
				/* send a response indicating server error */
				// TODO send more information about failure
				response = new OutgoingResponseMessage(request, 500);
				response.setContent(t.getMessage());
			}
			if (response != null) {
				doSend(response);
			}
		}
	}
	
	private static class ResponseHandlerFutureTask<V> extends FutureTask<V> {
		private ResponseHandlerCallable<V> responseHandlerCallable;
		
		public ResponseHandlerFutureTask(ResponseHandler<V> responseHandler) {
			this(new ResponseHandlerCallable<V>(responseHandler));
		}
		
		private ResponseHandlerFutureTask(ResponseHandlerCallable<V> responseHandlerCallable) {
			super(responseHandlerCallable);
			this.responseHandlerCallable = responseHandlerCallable;
		}
		
		public void setResponse(IncomingResponseMessage response) {
			responseHandlerCallable.response = response;
		}
	}
	
	private static class ResponseHandlerCallable<V> implements Callable<V> {
		private ResponseHandler<V> responseHandler;
		private IncomingResponseMessage response;
		
		public ResponseHandlerCallable(ResponseHandler<V> responseHandler) {
			this.responseHandler = responseHandler;
		}
		
		public V call() throws Exception {
			// TODO handle exceptions
			return responseHandler.handleResponse(response);
		}
	}

	public static LocalConnection getLocalConnection(IoSession ioSession) {
		return (LocalConnection) ioSession.getAttribute(LOCAL_CONNECTION_KEY);
	}
}
