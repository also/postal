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

package com.ryanberdeen.postal.message;

import java.util.HashMap;

import com.ryanberdeen.postal.Connection;
import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.ProxiedConnection;


public class IncomingRequestMessage extends AbstractRequestMessage implements IncomingRequestHeaders, IncomingMessage {
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private LocalConnection localConnection;
	
	protected IncomingRequestMessage(LocalConnection localConnection, String requestType, String uri) {
		super(requestType, uri);
		this.localConnection = localConnection;
	}
	
	public final Connection getConnection() {
		String proxiedConnectionId = getHeader(ProxiedConnection.PROXIED_CONNECTION_ID_HEADER_NAME);
		if (proxiedConnectionId != null) {
			return ProxiedConnection.getProxiedConnection(localConnection, proxiedConnectionId);
		}
		else {
			return localConnection;
		}
	}

	public final LocalConnection getLocalConnection() {
		return localConnection;
	}
	
	public Object getAttribute(String name) {
		return attributes != null ? attributes.get(name) : null;
	}

	public void setAttribute(String name, Object value) {
		if (attributes == null) {
			attributes = new HashMap<String, Object>();
		}
		attributes.put(name, value);
	}
}
