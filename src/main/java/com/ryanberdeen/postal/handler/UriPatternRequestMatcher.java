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

package com.ryanberdeen.postal.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ryanberdeen.postal.message.IncomingRequestHeaders;

public class UriPatternRequestMatcher implements RequestMatcher {
	private Pattern pattern;
	private String[] parameterNames;
	
	public UriPatternRequestMatcher() {}
	
	public UriPatternRequestMatcher(String pattern, String... parameterNames) {
		setPattern(pattern);
		this.parameterNames = parameterNames;
	}
	
	public boolean match(IncomingRequestHeaders incomingRequestHeaders) {
		Matcher matcher = pattern.matcher(incomingRequestHeaders.getUri());
		if (matcher.matches()) {
			if (parameterNames != null) {
			int max = Math.min(matcher.groupCount(), parameterNames.length);
				for (int i = 0; i < max; i++) {
					incomingRequestHeaders.setAttribute(parameterNames[i], matcher.group(i + 1));
				}
			}
			
			return true;
		}
		return false;
	}
	
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}
	
	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}
}
