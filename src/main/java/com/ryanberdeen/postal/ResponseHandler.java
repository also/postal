/* $Id$ */

package com.ryanberdeen.postal;

import com.ryanberdeen.postal.message.IncomingResponseMessage;

/** Handles a response.
 * @author rberdeen
 *
 * @param <V> the result type of the <code>handleResponse</code> method
 */
public interface ResponseHandler<V> {
	public V handleResponse(IncomingResponseMessage response) throws Exception;
}
