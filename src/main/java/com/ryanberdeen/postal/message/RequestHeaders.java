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

package com.ryanberdeen.postal.message;

/** Headers that are available for request messages.
 * @author rberdeen
 *
 */
public interface RequestHeaders extends Headers {
	@Deprecated
	public interface RequestType {
		public static String GET = "GET";
		public static String EVALUATE = "EVALUATE";
	}
	
	public static final String MESSAGE_ID = "Message-Id";
	
	/** Return the type of request.
	 */
	public String getRequestType();
	
	/** Return the requested URI.
	 */
	public String getUri();
	
	/** Return the message id, if any.
	 */
	public String getMessageId();
}
