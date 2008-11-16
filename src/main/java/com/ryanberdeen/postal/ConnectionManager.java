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

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import com.ryanberdeen.postal.handler.DefaultRequestHandlerMapping;


public class ConnectionManager implements ConnectionLifecycleListener, IdGenerator {
	private HashMap<String, LocalConnection> connections = new HashMap<String, LocalConnection>();
	private IdGenerator idSource = DefaultIdGenerator.newUniqueIdGenerator("GLOBAL");
	
	private DefaultRequestHandlerMapping requestHandlerMapping = new DefaultRequestHandlerMapping();
	
	/** Returns the connection with the specified ID.
	 */
	public LocalConnection getConnection(String connectionId) {
		return connections.get(connectionId);
	}
	
	public DefaultRequestHandlerMapping getRequestHandlerMapping() {
		return requestHandlerMapping;
	}
	
	public LocalConnection createConnection(IoSession ioSession) {
		LocalConnection connection;
		synchronized (connections) {
			connection = new IncomingConnection(ioSession, idSource.generateId());
		}
		connection.addConnectionLifecycleListener(this);
		connection.getRequestHandlerMapping().setParent(requestHandlerMapping);
		connections.put(connection.getConnectionId(), connection);
		connectionCreatedInternal(connection);
		return connection;
	}
	
	protected void connectionCreatedInternal(LocalConnection connection) {}
	
	public void connectionClosed(Connection connection) {
		connections.remove(connection.getConnectionId());
		connectionClosedInternal(connection);
	}
	
	protected void connectionClosedInternal(Connection connection) {}

	public String generateId() {
		return idSource.generateId();
	}
}
