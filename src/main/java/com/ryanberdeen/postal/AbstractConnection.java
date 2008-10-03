/* $Id$ */

package com.ryanberdeen.postal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.ryanberdeen.postal.message.IncomingMessage;
import com.ryanberdeen.postal.message.IncomingResponseMessage;
import com.ryanberdeen.postal.message.OutgoingMessage;
import com.ryanberdeen.postal.message.OutgoingRequestMessage;


public abstract class AbstractConnection implements Connection {
	private String connectionId;
	
	private long lastMessageRecievedTime = -1;
	private long lastMessageSentTime = -1;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	private ArrayList<ConnectionLifecycleListener> connectionLifecycleListeners = new ArrayList<ConnectionLifecycleListener>();
	
	private IdGenerator idSource = new DefaultIdGenerator();
	
	private static final ResponseHandler<IncomingResponseMessage> DEFAULT_RESPONSE_HANDLER = new ResponseHandler<IncomingResponseMessage>() {
		public IncomingResponseMessage handleResponse(IncomingResponseMessage response) {
			return response;
		}
	};
	
	public AbstractConnection() {}
	
	public AbstractConnection(String connectionId) {
		this.connectionId = connectionId;
	}
	
	public String getConnectionId() {
		return connectionId;
	}
	
	protected void onSend(OutgoingMessage message) {
		lastMessageSentTime = System.currentTimeMillis();
	}
	
	protected void onRecieve(IncomingMessage message) {
		lastMessageRecievedTime = System.currentTimeMillis();
	}
	
	public long getLastMessageRecievedTime() {
		return lastMessageRecievedTime;
	}
	
	public long getLastMessageSentTime() {
		return lastMessageSentTime;
	}
	
	public void addConnectionLifecycleListener(ConnectionLifecycleListener connectionLifecycleListener) {
		this.connectionLifecycleListeners.add(connectionLifecycleListener);
	}
	
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	/** Returns the specified attribute, initializing it with the result of the {@link Callable} if it does not exist.
	 * This method is thread safe.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key, Callable<T> defaultValueCallable) {
		synchronized (attributes) {
			T result = (T) attributes.get(key);
			
			if (result == null) {
				try {
					result = defaultValueCallable.call();
				}
				catch (RuntimeException ex) {
					throw ex;
				}
				catch (Exception ex) {
					throw new RuntimeException(ex);
				}
				
				attributes.put(key, result);
			}
			
			return result;
		}
	}
	
	/** Return an id.
	 * The id is guaranteed to be unique for the duration of the connection.
	 */
	public String generateId() {
		return idSource.generateId();
	}
	
	/** Sends a request whose response will be handled by the caller.
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Future<IncomingResponseMessage> sendRequest(OutgoingRequestMessage request) throws IOException {
		return sendRequest(request, DEFAULT_RESPONSE_HANDLER);
	}
	
	public IncomingResponseMessage sendRequestAndAwaitResponse(OutgoingRequestMessage request) throws IOException, InterruptedException {
		try {
			return sendRequest(request).get();
		}
		catch (ExecutionException ex) {
			assert false : "Exception in defualt response handler";
			return null;
		}
	}
	
	public void onClose() {
		for (ConnectionLifecycleListener connectionLifecycleListener : connectionLifecycleListeners) {
			connectionLifecycleListener.connectionClosed(this);
		}
	}

}
