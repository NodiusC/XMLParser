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

import java.util.LinkedList;
import java.util.List;

public class XMLSection extends XMLObject {
	
	private final List<XMLObject> elements;
	
	public XMLSection(String name) {
		super(name);
		this.elements = new LinkedList<XMLObject>();
	}
	
	public void addElement(XMLObject element) {
		if (element == null)
			return;
		
		this.elements.add(element);
	}
	
	public List<XMLObject> getElements(String name) {
		return getElements(this, name);
	}
	
	private List<XMLObject> getElements(XMLSection section, String name) {
		List<XMLObject> objects = new LinkedList<XMLObject>();
		
		for (XMLObject object : section.getElements()) {
			if (object.getName().equals(name))
				objects.add(object);
			
			if (object.getType().equals(XMLType.SECTION))
				objects.addAll(getElements((XMLSection) object, name));
		}
		
		return objects;
	}
	
	public List<XMLObject> getElements() {
		return new LinkedList<XMLObject>(elements);
	}
	
	@Override
	public XMLType getType() {
		return XMLType.SECTION;
	}
	
	public void removeElement(XMLObject element) {
		if (element == null)
			return;
		
		this.elements.remove(element);
	}
}