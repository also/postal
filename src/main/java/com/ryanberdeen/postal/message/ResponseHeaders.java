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

/** Headers that are available for response messages.
 * @author rberdeen
 *
 */
public interface ResponseHeaders extends Headers {
	public static final String IN_RESPONSE_TO = "In-Response-To";
	
	/** Return the status code.
	 */
	public int getStatus();
	
	/** Return the reason for the status code.
	 */
	public String getReason();
	
	/** Return the id of the message this is a response to.
	 */
	public String getInResponseTo();
}
