/* $Id$ */

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
