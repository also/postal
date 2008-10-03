/*$Id$ */

package com.ryanberdeen.postal.message;

import com.ryanberdeen.postal.Connection;

public interface IncomingRequestHeaders extends RequestHeaders, IncomingHeaders {
	public Connection getConnection();
	
	public void setAttribute(String name, Object value);
	
	public Object getAttribute(String name);
}
