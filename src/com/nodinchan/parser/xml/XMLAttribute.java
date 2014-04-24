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

public final class XMLAttribute {
	
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
		return name + "=\"" + value + "\"";
	}
}