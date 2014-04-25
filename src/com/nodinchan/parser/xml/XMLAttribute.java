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

/**
 * Represents an attribute in an XML document
 * 
 * @author NodinChan
 *
 */
public final class XMLAttribute {
	
	private final String name;
	
	private String value;
	
	/**
	 * Constructs an {@link XMLAttribute} with the given name and value
	 * 
	 * @param name The name of the attribute
	 * 
	 * @param value The value of the attribute
	 */
	public XMLAttribute(String name, String value) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		if (value == null)
			throw new IllegalArgumentException("Value cannot be null");
		
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Constructs an {@link XMLAttribute} with the given name
	 * 
	 * @param name The name of the attribute
	 */
	public XMLAttribute(String name) {
		this(name, "");
	}
	
	public XMLAttribute copy() {
		return new XMLAttribute(name, value);
	}
	
	@Override
	public boolean equals(Object object) {
		return getClass().isInstance(object) && toString().equals(object.toString()); 
	}
	
	/**
	 * Returns the name of the {@link XMLAttribute}
	 * 
	 * @return The attribute name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the value of the {@link XMLAttribute}
	 * 
	 * @return The attribute value
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	/**
	 * Sets the value of the {@link XMLAttribute}
	 * 
	 * @param value The new attribute value
	 */
	public void setValue(String value) {
		this.value = (value != null) ? value : "";
	}
	
	@Override
	public String toString() {
		return name + "=\"" + value + "\"";
	}
}