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

public final class XMLElement extends XMLObject {
	
	private String value;
	
	public XMLElement(String name) {
		super(name);
		this.value = "";
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public XMLType getType() {
		return XMLType.ELEMENT;
	}
	
	public void setValue(String value) {
		this.value = (value != null) ? value : "";
	}
}