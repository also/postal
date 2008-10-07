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

import java.util.Iterator;

import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;


public class FilterChain implements Filter, RequestHandler {
	private Iterator<Filter> filterIterator;
	private RequestHandler requestHandler;
	private OutgoingResponseMessage response;
	
	public FilterChain(Iterator<Filter> filterIterator, RequestHandler requestHandler) {
		this.filterIterator = filterIterator;
		this.requestHandler = requestHandler;
	}

	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception {
		if (filterIterator.hasNext()) {
			filterIterator.next().filterRequest(request, chain);
		}
		else {
			response = requestHandler.handleRequest(request);
		}
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		filterRequest(request, this);
		return response;
	}
	
}
