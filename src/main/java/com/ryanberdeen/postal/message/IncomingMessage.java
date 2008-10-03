/*$Id$ */

package com.ryanberdeen.postal.message;

/** A message with incoming headers.
 * This interface simply unifies Message and IncomingHeaders; it does not specify any new operations.
 * @author rberdeen
 *
 */
public interface IncomingMessage extends Message, IncomingHeaders {}
