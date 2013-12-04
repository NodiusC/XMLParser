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

package com.nodinchan.parser.xml;

import java.util.*;
import java.util.Map.Entry;

public class XMLElement {
	
	private final String name;
	
	private String value;
	
	private XMLElement parent;
	
	private final List<XMLElement> elements;
	
	private final Map<String, String> attributes;
	
	public XMLElement(String name) {
		this.name = name;
		this.elements = new LinkedList<XMLElement>();
		this.attributes = new LinkedHashMap<String, String>();
	}
	
	public void addAttribute(String name, String value) {
		if (name == null || name.isEmpty() || hasAttribute(name) || value == null)
			return;
		
		this.attributes.put(name, value);
	}
	
	public void addElement(XMLElement element) {
		if (element == null)
			return;
		
		element.setParent(this);
	}
	
	public String getAttribute(String name) {
		return (hasAttribute(name)) ? attributes.get(name) : null;
	}
	
	public List<Entry<String, String>> getAttributes() {
		return new LinkedList<Entry<String, String>>(attributes.entrySet());
	}
	
	public List<XMLElement> getElements() {
		return new LinkedList<XMLElement>(elements);
	}
	
	public static List<XMLElement> getElementsByName(XMLElement element, String name) {
		if (element == null || name == null || name.isEmpty())
			return new LinkedList<XMLElement>();
		
		List<XMLElement> elements = new LinkedList<XMLElement>();
		
		for (XMLElement e : element.getElements()) {
			if (!name.equals(e.getName()))
				continue;
			
			elements.add(e);
		}
		
		return elements;
	}
	
	public List<XMLElement> getElementsByName(String name) {
		return getElementsByName(this, name);
	}
	
	public static List<XMLElement> getElementsByValue(XMLElement element, String value) {
		if (element == null || value == null)
			return new LinkedList<XMLElement>();
		
		List<XMLElement> elements = new LinkedList<XMLElement>();
		
		for (XMLElement e : element.getElements()) {
			if (!value.equals(e.getValue()))
				continue;
			
			elements.add(e);
		}
		
		return elements;
	}
	
	public List<XMLElement> getElementsByValue(String value) {
		return getElementsByValue(this, value);
	}
	
	public final String getName() {
		return name;
	}
	
	public XMLElement getParent() {
		return parent;
	}
	
	public String getValue() {
		if (!isNode())
			return null;
		
		return value;
	}
	
	public boolean hasAttribute(String name) {
		return (name != null && !name.isEmpty()) ? this.attributes.containsKey(name) : false;
	}
	
	public boolean isNode() {
		return elements.size() < 1;
	}
	
	public void removeAttribute(String name) {
		if (name == null || name.isEmpty() || !hasAttribute(name))
			return;
		
		this.attributes.remove(name);
	}
	
	public void removeElement(XMLElement element) {
		if (element == null)
			return;
		
		element.setParent(null);
	}
	
	public void setParent(XMLElement parent) {
		if (this.parent == parent)
			return;
		
		if (this.parent != null)
			this.parent.elements.remove(this);
		
		this.parent = parent;
		
		if (this.parent != null)
			this.parent.elements.add(this);
	}
	
	public void setValue(String value) {
		this.value = (value != null) ? value : "";
	}
}