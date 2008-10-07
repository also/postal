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

/** Headers that are available for all messages.
 * Headers may be available before the message body has been read.
 * @author rberdeen
 *
 */
public interface Headers {
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_TYPE = "Content-Type";
	
	/** Return the value of the named header.
	 */
	public String getHeader(String name);
	
	/** Return true if the message has content.
	 * A message has content if and only if the Content-Length header is set.
	 * Equivalent to getContentType() != -1.
	 */
	public boolean hasContent();
	
	/** Return the length of the content in bytes.
	 * If the message has no content, returns -1.
	 */
	public int getContentLength();
	
	public String getContentType();
	
	/** Return true if the message is a request.
	 */
	public boolean isRequest();
	
	
}
