/*
 *     Copyright (C) 2014  Nodin Chan
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

import java.util.LinkedList;
import java.util.List;

public class XMLElement {
	
	private final String name;
	
	private String value;
	
	private XMLElement parent;
	
	private final LinkedList<XMLAttribute> attributes;
	private final LinkedList<XMLElement> elements;
	
	public XMLElement(String name) {
		this.name = name;
		this.value = "";
		this.attributes = new LinkedList<XMLAttribute>();
		this.elements = new LinkedList<XMLElement>();
	}
	
	public XMLElement appendAttribute(XMLAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		this.attributes.addLast(attribute);
		return this;
	}
	
	public XMLElement appendAttribute(String name, String value) {
		return appendAttribute(new XMLAttribute(name, value));
	}
	
	public XMLElement appendAttributeAfter(XMLAttribute attribute, String relative) {
		if (!hasAttribute(relative))
			throw new IllegalArgumentException("Relative attribute not found");
		
		return insertAttribute(attribute, this.attributes.indexOf(getAttribute(relative)) + 1);
	}
	
	public XMLElement appendAttributeAfter(String name, String value, String relative) {
		return appendAttributeAfter(new XMLAttribute(name, value), relative);
	}
	
	public XMLElement appendElement(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		return insertElement(element, this.elements.size());
	}
	
	public XMLElement appendElementAfter(XMLElement element, XMLElement relative) {
		if (!this.elements.contains(relative))
			throw new IllegalArgumentException("Relative element not found");
		
		return insertElement(element, this.elements.indexOf(relative) + 1);
	}
	
	public XMLElement clear() {
		for (XMLElement element : getElements())
			removeElement(element);
		
		setValue(null);
		return this;
	}
	
	public XMLAttribute getAttribute(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		for (XMLAttribute attribute : getAttributes()) {
			if (!attribute.getName().equals(name))
				continue;
			
			return attribute;
		}
		
		return null;
	}
	
	public List<XMLAttribute> getAttributes() {
		return new LinkedList<XMLAttribute>(attributes);
	}
	
	public List<XMLElement> getElements() {
		return new LinkedList<XMLElement>(elements);
	}
	
	public List<XMLElement> getElements(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		List<XMLElement> elements = new LinkedList<XMLElement>();
		
		for (XMLElement e : getElements()) {
			if (!name.equals(e.getName()))
				continue;
			
			elements.add(e);
		}
		
		return elements;
	}
	
	public final String getName() {
		return name;
	}
	
	public XMLElement getParent() {
		return parent;
	}
	
	public String getValue() {
		return (this.elements.size() < 1) ? value : null;
	}
	
	public boolean hasAttribute(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		for (XMLAttribute attribute : getAttributes()) {
			if (!attribute.getName().equals(name))
				continue;
			
			return true;
		}
		
		return false;
	}
	
	public boolean hasElement(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		for (XMLElement element : getElements()) {
			if (!name.equals(element.getName()))
				continue;
			
			return true;
		}
		
		return false;
	}
	
	public boolean hasValue() {
		return this.elements.size() < 1;
	}
	
	public XMLElement insertAttribute(XMLAttribute attribute, int position) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		if (position < 0 || position > this.attributes.size())
			throw new IllegalArgumentException("Position cannot be beyond 0 to " + (this.attributes.size() - 1));
		
		this.attributes.add(position, attribute);
		return this;
	}
	
	public XMLElement insertAttribute(String name, String value, int position) {
		return insertAttribute(new XMLAttribute(name, value), position);
	}
	
	public XMLElement insertElement(XMLElement element, int position) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		if (position < 0 || position > this.elements.size())
			throw new IllegalArgumentException("Position cannot be beyond 0 to " + (this.elements.size() - 1));
		
		if (element.parent != null)
			element.parent.removeElement(element);
		
		if (element.parent != this)
			element.parent = this;
		
		this.elements.add(position, element);
		return this;
	}
	
	public XMLElement prependAttribute(XMLAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		this.attributes.addFirst(attribute);
		return this;
	}
	
	public XMLElement prependAttribute(String name, String value) {
		return prependAttribute(new XMLAttribute(name, value));
	}
	
	public XMLElement prependAttributeBefore(XMLAttribute attribute, String relative) {
		if (!hasAttribute(relative))
			throw new IllegalArgumentException("Relative attribute not found");
		
		return insertAttribute(attribute, this.attributes.indexOf(getAttribute(relative)));
	}
	
	public XMLElement prependAttributeBefore(String name, String value, String relative) {
		return prependAttributeBefore(new XMLAttribute(name, value), relative);
	}
	
	public XMLElement prependElement(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		return insertElement(element, 0);
	}
	
	public XMLElement prependElementBefore(XMLElement element, XMLElement relative) {
		if (!this.elements.contains(relative))
			throw new IllegalArgumentException("Relative element not found");
		
		return insertElement(element, this.elements.indexOf(relative));
	}
	
	public XMLElement remove() {
		if (this.parent == null)
			return this;
		
		this.parent.removeElement(this);
		return this;
	}
	
	public XMLElement removeAttribute(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		if (!hasAttribute(name))
			throw new IllegalArgumentException("No such attribute");
		
		this.attributes.remove(getAttribute(name));
		return this;
	}
	
	public XMLElement removeElement(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		if (element.parent != this)
			throw new IllegalArgumentException("No such element");
		
		element.parent = null;
		this.elements.remove(element);
		return this;
	}
	
	public XMLElement setValue(String value) {
		this.value = (value != null) ? value : "";
		return this;
	}
	
	public static final class XMLAttribute {
		
		private final String name;
		private final String value;
		
		public XMLAttribute(String name, String value) {
			if (name == null || name.isEmpty())
				throw new IllegalArgumentException("Name cannot be empty");
			
			if (value == null)
				throw new IllegalArgumentException("Value cannot be null");
			
			this.name = name;
			this.value = value;
		}
		
		@Override
		public boolean equals(Object object) {
			return getClass().isInstance(object) && toString().equals(object.toString()); 
		}
		
		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}
		
		@Override
		public int hashCode() {
			return toString().hashCode();
		}
		
		@Override
		public String toString() {
			return name + "=" + value;
		}
	}
}