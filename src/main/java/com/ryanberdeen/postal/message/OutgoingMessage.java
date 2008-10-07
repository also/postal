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

public interface OutgoingMessage extends Message, OutgoingHeaders {
	/** Set the content as a byte array.
	 */
	public void setContent(byte[] content);
	
	/** Set the content as a CharSequence.
	 * If the <code>Content-Type</code> has not been set, it is set to <code>text/plain</code>.
	 */
	public void setContent(CharSequence content);
	
	public void write(OutputStream out) throws IOException;
}
