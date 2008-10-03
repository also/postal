/* $Id$ */

package com.ryanberdeen.postal.handler;

import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;

/** Handles a request.
 * @author rberdeen
 *
 */
public interface RequestHandler {
	/**
	 * Handle the request. Returns the response, with null indicating no
	 * response is necessary or that a response has already been sent.
	 */
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception;
}
