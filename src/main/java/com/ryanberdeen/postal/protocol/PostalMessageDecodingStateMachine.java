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

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.ConsumeToCrLfDecodingState;
import org.apache.mina.filter.codec.statemachine.ConsumeToLinearWhitespaceDecodingState;
import org.apache.mina.filter.codec.statemachine.ConsumeToTerminatorDecodingState;
import org.apache.mina.filter.codec.statemachine.CrLfDecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateMachine;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;
import org.apache.mina.filter.codec.statemachine.LinearWhitespaceSkippingState;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.message.AbstractMessage;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.IncomingResponseMessage;
import com.ryanberdeen.postal.message.Message;

public class PostalMessageDecodingStateMachine extends DecodingStateMachine {
	private CharsetDecoder charsetDecoder = Charset.forName("UTF-8").newDecoder();
	private String requestType;
	private int status;
	private String reason;
	private String uri;
	private HashMap<String, String> headers;
	private String currentHeaderName;

	@Override
	protected DecodingState init() throws Exception {
		headers = new HashMap<String, String>();
		return READ_REQUEST_TYPE;
	}

	@Override
	protected void destroy() throws Exception {
		requestType = null;
		status = -1;
		reason = null;
		headers = null;
		currentHeaderName = null;
	}

	@Override
	protected DecodingState finishDecode(List<Object> childProducts, ProtocolDecoderOutput out) throws Exception {
		for (Object message: childProducts) {
			out.write(message);
		}
		return null;
	}

	@Override
	public DecodingState finishDecode(ProtocolDecoderOutput out) {
		return null;
	}

    private final DecodingState READ_REQUEST_TYPE = new ConsumeToLinearWhitespaceDecodingState() {
        @Override
        protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
            requestType = product.getString(charsetDecoder);
            if (requestType.equals(LocalConnection.PROTOCOL)) {
            	requestType = null;
            	return BEFORE_READ_STATUS;
            }
            else {
            	return BEFORE_READ_URIL;
            }
        }
    };

	private final DecodingState BEFORE_READ_URIL = new LinearWhitespaceSkippingState() {
		@Override
		protected DecodingState finishDecode(int skippedBytes) throws Exception {
			return READ_URI;
		}
	};

    private final DecodingState READ_URI = new ConsumeToLinearWhitespaceDecodingState() {
        @Override
        protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
            uri = product.getString(charsetDecoder);
            return AFTER_READ_URI;
        }
    };

	private final DecodingState AFTER_READ_URI = new LinearWhitespaceSkippingState() {
		@Override
		protected DecodingState finishDecode(int skippedBytes) throws Exception {
			return READ_PROTOCOL;
		}
	};

    private final DecodingState READ_PROTOCOL = new ConsumeToCrLfDecodingState() {
        @Override
        protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
            return FIND_EMPTY_LINE;
        }
    };

	private final DecodingState BEFORE_READ_STATUS = new LinearWhitespaceSkippingState() {
		@Override
		protected DecodingState finishDecode(int skippedBytes) throws Exception {
			return READ_STATUS;
		}
	};

    private final DecodingState READ_STATUS = new ConsumeToLinearWhitespaceDecodingState() {
        @Override
        protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
            status = Integer.valueOf(product.getString(charsetDecoder));
            return AFTER_READ_STATUS;
        }
    };

	private final DecodingState AFTER_READ_STATUS = new LinearWhitespaceSkippingState() {
		@Override
		protected DecodingState finishDecode(int skippedBytes) throws Exception {
			return READ_REASON;
		}
	};

    private final DecodingState READ_REASON = new ConsumeToCrLfDecodingState() {
        @Override
        protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
            reason = product.getString(charsetDecoder);
            return FIND_EMPTY_LINE;
        }
    };

	private final DecodingState FIND_EMPTY_LINE = new CrLfDecodingState() {
		@Override
		protected DecodingState finishDecode(boolean foundCRLF, ProtocolDecoderOutput out) throws Exception {
			if (foundCRLF) {
				return createReadContentDecodingState(out);
			} else {
				return READ_HEADER_NAME;
			}
		}
	};

    private final DecodingState READ_HEADER_NAME = new ConsumeToTerminatorDecodingState((byte) ':') {
		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
			currentHeaderName = product.getString(charsetDecoder);
			return AFTER_READ_HEADER_NAME;
		}
	};

	private final DecodingState AFTER_READ_HEADER_NAME = new LinearWhitespaceSkippingState() {
		@Override
		protected DecodingState finishDecode(int skippedBytes) throws Exception {
			return READ_HEADER_VALUE;
		}
	};

	private final DecodingState READ_HEADER_VALUE = new ConsumeToCrLfDecodingState() {
		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
			headers.put(currentHeaderName, product.getString(charsetDecoder));
			return FIND_EMPTY_LINE;
		}
	};

	private DecodingState createReadContentDecodingState(ProtocolDecoderOutput out) {
		final AbstractMessage message;
		if (requestType != null) {
			message = new IncomingRequestMessage(requestType, uri);
		}
		else {
			message = new IncomingResponseMessage(status, reason);
		}

		message.setHeaders(headers);
		final String lengthValue = headers.get(Message.CONTENT_LENGTH);
		if (lengthValue == null || lengthValue.equals("0")) {
			out.write(message);
			return READ_MESSAGE_SEPARATOR;
		}
		else {
			return new FixedLengthDecodingState(Integer.valueOf(lengthValue)) {
				@Override
				protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
					byte[] content = new byte[Integer.valueOf(lengthValue)];
					product.get(content);
					message.setContent(content);
					out.write(message);
					return READ_MESSAGE_SEPARATOR;
				}
			};
		}
	}

	private final DecodingState READ_MESSAGE_SEPARATOR = new CrLfDecodingState() {
		@Override
		protected DecodingState finishDecode(boolean foundCRLF, ProtocolDecoderOutput out) throws Exception {
			if (!foundCRLF) {
				throw new PostalMessageProtocolDecoderException("Expected CRLF after message");
			}
			else {
				return null;
			}
		}
	};

}
