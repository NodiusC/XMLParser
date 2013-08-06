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

public class XMLSection extends XMLObject {
	
	private final Map<String, XMLObject> elements;
	
	public XMLSection(String name) {
		super(name);
		this.elements = new LinkedHashMap<String, XMLObject>();
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
	
	@Override
	public XMLType getType() {
		return XMLType.SECTION;
	}
	
	public boolean hasElement(String name) {
		return (name != null) ? elements.containsKey(name) : false;
	}
	
	public void removeElement(String name) {
		if (name == null || !hasElement(name))
			return;
		
		this.elements.remove(name);
	}
}