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

public abstract class XMLObject {
	
	private final String name;
	
	private final Map<String, XMLAttribute> attributes;
	
	public XMLObject(String name) {
		this.name = name;
		this.attributes = new LinkedHashMap<String, XMLAttribute>();
	}
	
	public void addAttribute(XMLAttribute attribute) {
		if (attribute == null || hasAttribute(attribute.getName()))
			return;
		
		this.attributes.put(attribute.getName(), attribute);
	}
	
	public XMLAttribute getAttribute(String name) {
		return (name != null) ? attributes.get(name) : null;
	}
	
	public List<XMLAttribute> getAttributes() {
		return new LinkedList<XMLAttribute>(attributes.values());
	}
	
	public final String getName() {
		return name;
	}
	
	public XMLType getType() {
		return XMLType.UNKNOWN;
	}
	
	public boolean hasAttribute(String name) {
		return (name != null) ? attributes.containsKey(name) : false;
	}
	
	public void removeAttribute(String name) {
		if (name == null || !hasAttribute(name))
			return;
		
		this.attributes.remove(name);
	}
	
	public enum XMLType { ELEMENT, ITEM, UNKNOWN }
}