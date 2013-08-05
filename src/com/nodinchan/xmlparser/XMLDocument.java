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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class XMLDocument {
	
	private final String encoding;
	private final String version;
	private final String systemId;
	
	private final boolean standalone;
	
	private final Map<String, XMLObject> elements;
	
	public XMLDocument(String encoding, String version, String systemId, boolean standalone) {
		this.encoding = encoding;
		this.version = version;
		this.systemId = systemId;
		this.standalone = standalone;
		this.elements = new LinkedHashMap<String, XMLObject>();
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
	
	public void addElement(XMLObject element) {
		if (element == null || hasElement(element.getName()))
			return;
		
		this.elements.put(element.getName(), element);
	}
	
	public XMLObject getElement(String name) {
		return (name != null) ? elements.get(name) : null;
	}
	
	public List<XMLObject> getElements() {
		return new LinkedList<XMLObject>(elements.values());
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public String getSystemId() {
		return systemId;
	}
	
	public String getVersion() {
		return version;
	}
	
	public boolean hasElement(String name) {
		return (name != null) ? elements.containsKey(name) : false;
	}
	
	public boolean isStandalone() {
		return standalone;
	}
	
	public void removeElement(String name) {
		if (name == null || !hasElement(name))
			return;
		
		this.elements.remove(name);
	}
}