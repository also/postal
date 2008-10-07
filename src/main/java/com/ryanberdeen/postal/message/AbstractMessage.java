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

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/** Superclass for message classes.
 * @author rberdeen
 *
 */
public abstract class AbstractMessage implements Message {
	private HashMap<String, String> headers;

	private byte[] content;
	private CharSequence contentCharSequence;

	protected AbstractMessage() {
		headers = new HashMap<String, String>();
	}

	@SuppressWarnings("unchecked")
	protected AbstractMessage(AbstractMessage that) {
		this.headers = (HashMap<String, String>) that.headers.clone();
		this.headers.remove(CONTENT_LENGTH);
		if (that.content != null) {
			this.content = (byte[]) that.content.clone();
		}
		else if (that.contentCharSequence != null) {
			this.contentCharSequence = that.contentCharSequence.toString();
		}
	}

	public final boolean hasContent() {
		return content != null || contentCharSequence != null;
	}

	public final byte[] getContent() {
		if (contentCharSequence != null) {
			try {
				return contentCharSequence.toString().getBytes("UTF-8");
			}
			catch (UnsupportedEncodingException ex) {
				// utf-8 should always be supported
				throw new Error(ex);
			}
		}
		else {
			return content;
		}
	}

	public final String getContentAsString() {
		if (contentCharSequence != null) {
			return contentCharSequence.toString();
		}
		else if (content != null) {
			try {
				return new String(content, "UTF-8");
			}
			catch (UnsupportedEncodingException ex) {
				// utf-8 should always be supported
				throw new Error(ex);
			}
		}
		else {
			return null;
		}
	}

	public final int getContentLength() {
		byte[] content = getContent();
		if (content != null) {
			return content.length;
		}
		else {
			return -1;
		}
	}

	public final String getContentType() {
		return getHeader(CONTENT_TYPE);
	}

	/** Sets the Content-Type header of the message.
	 * @param contentType
	 */
	protected void setContentType(String contentType) {
		setHeader(CONTENT_TYPE, contentType);
	}

	public final String getHeader(String name) {
		return headers.get(name);
	}

	public final Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	protected void setHeader(String name, String value) {
		if(containsLineEnd(name)) {
			throw new IllegalArgumentException("End of line in header name: '" + name + "'");
		}
		if(value != null && containsLineEnd(value)) {
			throw new IllegalArgumentException("End of line in header value: '" + value + "'");
		}
		setHeaderUnchecked(name, value);
	}

	/** Set a header without checking for line breaks.
	 */
	protected void setHeaderUnchecked(String name, String value) {
		headers.put(name, value);
	}

	/** Sets the character content of the message.
	 * If the content type of the message has not been set, the content type is set to <code>text/plain</code>.
	 * @param contentCharSequence
	 */
	protected void setContent(CharSequence contentCharSequence) {
		content = null;
		if (contentCharSequence == null || contentCharSequence.length() == 0) {
			this.contentCharSequence = null;
		}
		else {
			this.contentCharSequence = contentCharSequence;
		}
		if(getHeader(CONTENT_TYPE) == null) {
			setHeader(CONTENT_TYPE, "text/plain");
		}
	}

	/** Sets the character content of the message, along with the content type.
	 * @param content
	 * @param contentType
	 */
	protected void setContent(CharSequence content, String contentType) {
		setContent(content);
		setContentType(contentType);
	}

	/** Sets the byte content of the message.
	 * @param content
	 */
	public void setContent(byte[] content) {
		contentCharSequence = null;
		if (content == null || content.length == 0) {
			this.content = null;
		}
		else {
			this.content = content;
		}
	}

	protected void setContent(byte[] content, String contentType) {
		setContent(content);
		setContentType(contentType);
	}

	/** Return true if the String contains \r or \n. */
	protected final boolean containsLineEnd(String string) {
		for(char character : string.toCharArray()) {
			if (character == '\r' || character == '\n') {
				return true;
			}
		}
		return false;
	}
}
