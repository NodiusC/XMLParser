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
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents an element in an XML document
 * 
 * @author NodinChan
 *
 */
public final class XMLElement extends XMLHierarchical {
	
	private final String name;
	
	private String value;
	
	private XMLHierarchical parent;
	
	private final Map<String, XMLAttribute> attributes;
	
	/**
	 * Constructs an {@link XMLElement} with the given name
	 * 
	 * @param name The name of the element
	 */
	public XMLElement(String name) {
		this.name = name;
		this.value = "";
		this.attributes = new TreeMap<String, XMLAttribute>();
	}
	
	@Override
	public XMLElement appendElement(XMLElement element) {
		return insertElement(element, getElementCount());
	}
	
	@Override
	public XMLElement appendElementAfter(XMLElement element, XMLElement relative) {
		return insertElement(element, getElementIndex(relative) + 1);
	}
	
	/**
	 * Returns the {@link XMLAttribute} with the given name
	 * 
	 * @param name The attribute name
	 * 
	 * @return The attribute if found, otherwise null
	 */
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
	
	/**
	 * Returns a list of {@link XMLAttribute}s
	 * 
	 * @return The copy of the list of attributes
	 */
	public List<XMLAttribute> getAttributes() {
		return new LinkedList<XMLAttribute>(this.attributes.values());
	}
	
	/**
	 * Returns the value of the {@link XMLAttribute} with the given name
	 * 
	 * @param name The attribute name
	 * 
	 * @return The value if present, otherwise null
	 */
	public String getAttributeValue(String name) {
		return (hasAttribute(name)) ? getAttribute(name).getValue() : null;
	}
	
	/**
	 * Returns the name of the {@link XMLElement}
	 * 
	 * @return The element name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the parent of the {@link XMLElement}
	 * 
	 * @return The parent if one exists, otherwise null
	 */
	public XMLHierarchical getParent() {
		return this.parent;
	}
	
	/**
	 * Returns the value of the {@link XMLElement}
	 * 
	 * @return The value if no elements are present, otherwise null
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Determines whether an {@link XMLAttribute} with the given name is present
	 * 
	 * @param name The attribute name
	 * 
	 * @return True if present, otherwise false
	 */
	public boolean hasAttribute(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		return this.attributes.containsKey(name);
	}
	
	/**
	 * Indicates whether any {@link XMLAttribute}s are present
	 * 
	 * @return True if any is present, otherwise false
	 */
	public boolean hasAttributes() {
		return !this.attributes.isEmpty();
	}
	
	@Override
	public XMLElement insertElement(XMLElement element, int position) {
		super.insertElement(element, position);
		
		this.value = null;
		return this;
	}
	
	@Override
	public XMLElement prependElement(XMLElement element) {
		return insertElement(element, 0);
	}
	
	@Override
	public XMLElement prependElementBefore(XMLElement element, XMLElement relative) {
		return insertElement(element, getElementIndex(relative));
	}
	
	/**
	 * Removes the {@link XMLElement} from its parent
	 * 
	 * @return The element that is removed
	 */
	public XMLElement remove() {
		if (this.parent != null)
			this.parent.removeElement(this);
		
		return this;
	}
	
	/**
	 * Removes the {@link XMLAttribute} with the given name
	 * 
	 * @param name The attribute name
	 * 
	 * @return The {@link XMLElement} that the attribute is removed from
	 */
	public XMLElement removeAttribute(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		this.attributes.remove(name);
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
	
	/**
	 * Sets the {@link XMLAttribute} value, replacing any existing attributes of the same given name
	 * 
	 * @param attribute The attribute to set
	 * 
	 * @return The {@link XMLElement} that the attribute is set on
	 */
	public XMLElement setAttribute(XMLAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		
		this.attributes.put(attribute.getName(), attribute.copy());
		return this;
	}
	
	/**
	 * Sets the {@link XMLAttribute} value, replacing any existing attributes of the same given name
	 * 
	 * @param name The attribute name
	 * 
	 * @param value The value to set
	 * 
	 * @return The {@link XMLElement} that the attribute is set on
	 */
	public XMLElement setAttribute(String name, String value) {
		return setAttribute(new XMLAttribute(name, value));
	}
	
	/**
	 * Sets the parent of the {@link XMLElement}
	 * 
	 * @param parent The parent of the element
	 */
	public void setParent(XMLHierarchical parent) {
		if (this.parent == parent)
			return;
		
		if (!parent.hasElement(remove()))
			parent.appendElement(this);
		
		this.parent = parent;
	}
	
	/**
	 * Sets the value of the {@link XMLElement}, removing all existing elements
	 * 
	 * @param value The element value
	 * 
	 * @return The element that the value is set on
	 */
	public XMLElement setValue(String value) {
		this.value = (value != null) ? value : "";
		return removeElements();
	}
}