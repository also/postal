/* $Id$ */

package com.ryanberdeen.postal.handler;

import com.ryanberdeen.postal.message.Headers;

public abstract class AbstractNestedRequestHandlerMapping implements RequestHandlerMapping {

	public RequestHandler getHandler(Headers headers) {
		return null;
	}
	
	protected abstract RequestHandler getHandlerInternal(Headers headers);
}
