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

package com.ryanberdeen.postal.protocol;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.message.AbstractMessage;
import com.ryanberdeen.postal.message.RequestMessage;
import com.ryanberdeen.postal.message.ResponseMessage;

public class PostalProtocolEncoder implements ProtocolEncoder {
	private CharsetEncoder charsetEncoder = Charset.forName("UTF-8").newEncoder();
	private static String CRLF = "\r\n";

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		AbstractMessage postalMessage = (AbstractMessage) message;
		IoBuffer buffer = IoBuffer.allocate(postalMessage.getContentLength() + 1024, false);
		buffer.setAutoExpand(true);
		write(buffer, postalMessage);
		buffer.flip();
		out.write(buffer);
	}

	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub

	}

	private void writeHeaders(AbstractMessage message, IoBuffer out, boolean flushHeaders) throws CharacterCodingException {
		/* send the request or response line */
		String subject;
		if (message instanceof RequestMessage) {
			RequestMessage request = (RequestMessage) message;
			subject = request.getRequestType() + ' ' + request.getUri() + ' ' + LocalConnection.PROTOCOL_VERSION;
		}
		else {
			ResponseMessage response = (ResponseMessage) message;
			subject = LocalConnection.PROTOCOL + ' ' + response.getStatus() + ' ' + response.getReason();
		}
		out.putString(subject + CRLF, charsetEncoder);

		/* send the headers */
		for(Map.Entry<String, String> header : message.getHeaders().entrySet()) {
			if (header.getValue() != null) {
				writeHeader(out, header.getKey(), header.getValue());
			}
		}

		if (flushHeaders) {
			out.putString(CRLF, charsetEncoder);
		}
	}

	/** Writes the message to the output stream.
	 * @param out
	 * @throws IOException
	 */
	public final void write(IoBuffer out, AbstractMessage message) throws IOException {
		writeHeaders(message, out, false);

		/* send the content, if any */
		byte[] content = message.getContent();
		if(content != null) {
			writeHeader(out, "Content-Length",  content.length);
			writeNewLine(out);
			out.put(content);
		}
		else {
			writeNewLine(out);
		}

		/* messages are separated by a blank line */
		writeNewLine(out);
	}

	private void writeHeader(IoBuffer out, String name, Object value) throws CharacterCodingException {
		out.putString(name + ": " + value + CRLF, charsetEncoder);
	}

	private void writeNewLine(IoBuffer out) throws CharacterCodingException {
		out.putString(CRLF, charsetEncoder);
	}
}
