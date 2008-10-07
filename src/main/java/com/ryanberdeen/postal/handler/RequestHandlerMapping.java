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

import com.ryanberdeen.postal.message.IncomingRequestHeaders;

/** Maps message headers to handlers than can handle the message.
 * 
 * @author Ryan Berdeen
 *
 */
public interface RequestHandlerMapping {
	
	/** Locates a handler that can handle the messages associated with the headers.
	 * @param headers the headers to match against
	 * @return the handler if one matches, <code>null</code> otherwise
	 */
	public RequestHandler getHandler(IncomingRequestHeaders headers);
}
