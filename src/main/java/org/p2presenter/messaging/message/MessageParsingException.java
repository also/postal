/* $Id$ */

package org.p2presenter.messaging.message;

import java.io.IOException;

public class MessageParsingException extends IOException {
	private static final long serialVersionUID = 1L;

	public MessageParsingException() {
	}

	public MessageParsingException(String s) {
		super(s);
	}

}
