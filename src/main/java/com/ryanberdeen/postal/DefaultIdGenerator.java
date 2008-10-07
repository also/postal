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

package com.ryanberdeen.postal;


public class DefaultIdGenerator implements IdGenerator {
	private static int generatorNumber = 1;
	
	private String prefix = "";
	
	private int messageId = 1;
	
	public DefaultIdGenerator() { }
	
	public DefaultIdGenerator(String prefix) {
		if (prefix != null) {
			this.prefix = prefix + '-';
		}
	}
	
	public synchronized String generateId() {
		return LocalConnection.PROTOCOL + prefix + '-' + messageId++;
	}
	
	public static synchronized IdGenerator newUniqueMessageIdSource() {
		return newUniqueIdGenerator(null);
	}
	
	public static synchronized IdGenerator newUniqueIdGenerator(String prefix) {
		DefaultIdGenerator result = new DefaultIdGenerator(prefix);
		result.prefix += '-' + result.hashCode() + '-' + generatorNumber++;
		return result;
	}
}
