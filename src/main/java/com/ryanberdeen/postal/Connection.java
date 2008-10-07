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

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.ryanberdeen.postal.message.IncomingResponseMessage;
import com.ryanberdeen.postal.message.OutgoingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;


public interface Connection extends IdGenerator {
	public String getConnectionId();

	/** Adds a listener to be notified of connection lifecycle events.
	 */
	public void addConnectionLifecycleListener(ConnectionLifecycleListener connectionLifecycleListener);
	// TODO needs removeConnectionLifecycleListener
	
	public void setAttribute(String key, Object value);

	public Object getAttribute(String key);

	/** Returns the specified attribute, initializing it with the result of the {@link Callable} if it does not exist.
	 * This method is thread safe.
	 */
	public <T> T getAttribute(String key, Callable<T> defaultValueCallable);

	/** Returns the time the last message was received, in milliseconds.
	 */
	public long getLastMessageRecievedTime();

	/** Returns the time the last message was sent, in milliseconds.
	 */
	public long getLastMessageSentTime();

	/** Sends a request whose response will be handled by the caller.
	 * @param request
	 * @return a {@link Future} providing access to the response message
	 * @throws IOException
	 */
	public Future<IncomingResponseMessage> sendRequest(
			OutgoingRequestMessage request) throws IOException;

	/** Sends a request whose response will be handled by the specified handler.
	 * @param <V> the result type of the<code>handleResponse</code> method
	 * @param request the request message to send
	 * @param responseHandler the response message handler
	 * @return a {@link Future} allowing access to the result of the response handler
	 * @throws IOException if an exception occurs while sending the message
	 */
	public <V> Future<V> sendRequest(OutgoingRequestMessage request,
			ResponseHandler<V> responseHandler) throws IOException;

	public IncomingResponseMessage sendRequestAndAwaitResponse(
			OutgoingRequestMessage request) throws IOException,
			InterruptedException;

	/** Sends a response message. The connection will be closed if any exceptions occur.
	 * @throws IOException if an exception occurrs while sending the message
	 */
	public void sendResponse(OutgoingResponseMessage response)
			throws IOException;

}