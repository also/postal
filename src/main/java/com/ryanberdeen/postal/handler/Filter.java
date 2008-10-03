/* $Id$ */

package com.ryanberdeen.postal.handler;

import com.ryanberdeen.postal.message.IncomingRequestMessage;

public interface Filter {
	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception;
}
