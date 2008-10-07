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

import java.io.IOException;
import java.io.OutputStream;

/** A set of headers that can be written to an output stream.
 * @author rberdeen
 *
 */
public interface OutgoingHeaders extends Headers {
	public void setContentType(String contentType);
	
	public void setHeader(String header, String value);
	
	/** Write the headers, including the request/response line, to the output stream.
	 * The headers include the blank line that separates headers from content.
	 */
	public void writeHeaders(OutputStream out) throws IOException;
}
