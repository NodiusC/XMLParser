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

public final class XMLElement extends XMLHierarchical {
	
	private final String name;
	
	private String value;
	
	private XMLHierarchical parent;
	
	private final List<XMLAttribute> attributes;
	
	public XMLElement(String name) {
		this.name = name;
		this.value = "";
		this.attributes = new LinkedList<XMLAttribute>();
	}
	
	public XMLElement appendAttribute(XMLAttribute attribute) {
		return insertAttribute(attribute, this.attributes.size());
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
	
	@Override
	public XMLElement appendElement(XMLElement element) {
		return insertElement(element, getElementCount());
	}
	
	@Override
	public XMLElement appendElementAfter(XMLElement element, XMLElement relative) {
		return insertElement(element, getElementIndex(relative) + 1);
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
	
	public XMLAttribute getAttribute(int position) {
		if (position < 0 || position > this.attributes.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.attributes.size() - 1));
		
		return this.attributes.get(position);
	}
	
	public int getAttributeCount() {
		return this.attributes.size();
	}
	
	public int getAttributeIndex(XMLAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		return this.attributes.indexOf(attribute);
	}
	
	public List<XMLAttribute> getAttributes() {
		return new LinkedList<XMLAttribute>(this.attributes);
	}
	
	public String getName() {
		return this.name;
	}
	
	public XMLHierarchical getParent() {
		return this.parent;
	}
	
	public String getValue() {
		return (getElementCount() < 1) ? value : null;
	}
	
	public boolean hasAttribute(XMLAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		return hasAttribute(attribute.getName());
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
	
	public XMLElement insertAttribute(XMLAttribute attribute, int position) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		if (position < 0 || position > this.attributes.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.attributes.size() - 1));
		
		if (hasAttribute(attribute))
			removeAttribute(attribute.getName());
		
		this.attributes.add(position, attribute);
		return this;
	}
	
	public XMLElement insertAttribute(String name, String value, int position) {
		return insertAttribute(new XMLAttribute(name, value), position);
	}
	
	@Override
	public XMLElement insertElement(XMLElement element, int position) {
		super.insertElement(element, position);
		return removeValue();
	}
	
	public XMLElement prependAttribute(XMLAttribute attribute) {
		return insertAttribute(attribute, 0);
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
	
	@Override
	public XMLElement prependElement(XMLElement element) {
		return insertElement(element, 0);
	}
	
	@Override
	public XMLElement prependElementBefore(XMLElement element, XMLElement relative) {
		return insertElement(element, getElementIndex(relative));
	}
	
	public XMLElement remove() {
		if (this.parent != null)
			this.parent.removeElement(this);
		
		return this;
	}
	
	public XMLElement removeAttribute(XMLAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		if (!hasAttribute(attribute))
			throw new IllegalArgumentException("No such attribute");
		
		this.attributes.remove(attribute);
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
	
	@Override
	public XMLElement removeElement(XMLElement element) {
		super.removeElement(element);
		return this;
	}
	
	@Override
	public XMLElement removeElement(int position) {
		super.removeElement(position);
		return this;
	}
	
	@Override
	public XMLElement removeElements() {
		super.removeElements();
		return this;
	}
	
	public XMLElement removeValue() {
		this.value = "";
		return this;
	}
	
	public void setParent(XMLHierarchical parent) {
		if (this.parent == parent)
			return;
		
		remove();
		
		if (!parent.hasElement(this))
			parent.appendElement(this);
		
		this.parent = parent;
	}
	
	public XMLElement setValue(String value) {
		this.value = (value != null) ? value : "";
		return removeElements();
	}
}