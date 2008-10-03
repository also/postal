/* $Id$ */

package com.ryanberdeen.postal.handler;

import com.ryanberdeen.postal.message.IncomingRequestHeaders;

public interface RequestMatcher {
	public boolean match(IncomingRequestHeaders incomingRequestHeaders);
}
