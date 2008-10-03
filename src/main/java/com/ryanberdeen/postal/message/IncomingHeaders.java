/* $Id$ */

package com.ryanberdeen.postal.message;

import com.ryanberdeen.postal.LocalConnection;

/** Headers that have been received by a connection.
 * 
 * @author Ryan Berdeen
 *
 */
public interface IncomingHeaders extends Headers {
	/** Return the local (not proxied) connection that received the headers.
	 */
	public LocalConnection getLocalConnection();
}
