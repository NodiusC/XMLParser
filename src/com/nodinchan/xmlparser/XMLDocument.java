/*
 *     Copyright (C) 2013  Nodin Chan
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.nodinchan.xmlparser;

import java.util.LinkedList;
import java.util.List;

public final class XMLDocument extends XMLSection {
	
	private final String encoding;
	private final String version;
	private final String systemId;
	
	private final boolean standalone;
	
	public XMLDocument(String encoding, String version, String systemId, boolean standalone) {
		super("xml");
		this.encoding = encoding;
		this.version = version;
		this.systemId = systemId;
		this.standalone = standalone;
	}
	
	public XMLDocument(String encoding, String version, String systemId) {
		this(encoding, version, systemId, false);
	}
	
	public XMLDocument(String encoding, String version) {
		this(encoding, version, "", false);
	}
	
	public XMLDocument(String encoding) {
		this(encoding, "1.0", "", false);
	}
	
	public XMLDocument() {
		this("UTF-8", "1.0", "", false);
	}
	
	@Override
	public void addAttribute(XMLAttribute attribute) {}
	
	@Override
	public XMLAttribute getAttribute(String name) {
		return null;
	}
	
	@Override
	public List<XMLAttribute> getAttributes() {
		return new LinkedList<XMLAttribute>();
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public String getSystemId() {
		return systemId;
	}
	
	@Override
	public XMLType getType() {
		return XMLType.DOCUMENT;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public boolean hasAttribute(String name) {
		return false;
	}
	
	public boolean isStandalone() {
		return standalone;
	}
	
	@Override
	public void removeAttribute(String name) {}
}