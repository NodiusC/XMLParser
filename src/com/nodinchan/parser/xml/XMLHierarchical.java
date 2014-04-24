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

public abstract class XMLHierarchical {
	
	private final List<XMLElement> elements;
	
	public XMLHierarchical() {
		this.elements = new LinkedList<XMLElement>();
	}
	
	public XMLHierarchical appendElement(XMLElement element) {
		return insertElement(element, getElementCount());
	}
	
	public XMLHierarchical appendElementAfter(XMLElement element, XMLElement relative) {
		if (!hasElement(relative))
			throw new IllegalArgumentException("Relative element not found");
		
		return insertElement(element, getElementIndex(relative) + 1);
	}
	
	public XMLElement getElement(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		
		for (XMLElement e : getElements()) {
			if (!name.equals(e.getName()))
				continue;
			
			return e;
		}
		
		return null;
	}
	
	public XMLElement getElement(int position) {
		if (position < 0 || position > this.elements.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.elements.size() - 1));
		
		return this.elements.get(position);
	}
	
	public int getElementCount() {
		return this.elements.size();
	}
	
	public int getElementIndex(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		return this.elements.indexOf(element);
	}
	
	public List<XMLElement> getElements() {
		return new LinkedList<XMLElement>(this.elements);
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
	
	public boolean hasElement(XMLElement element) {
		return this.elements.contains(element);
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
	
	public XMLHierarchical insertElement(XMLElement element, int position) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		if (position < 0 || position > this.elements.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.elements.size() - 1));
		
		if (element.getParent() != this)
			element.setParent(this);
		
		this.elements.add(position, element);
		return this;
	}
	
	public XMLHierarchical prependElement(XMLElement element) {
		return insertElement(element, 0);
	}
	
	public XMLHierarchical prependElementBefore(XMLElement element, XMLElement relative) {
		if (!hasElement(relative))
			throw new IllegalArgumentException("Relative element not found");
		
		return insertElement(element, getElementIndex(relative));
	}
	
	public XMLHierarchical removeElement(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		if (!hasElement(element))
			throw new IllegalArgumentException("No such element");
		
		element.setParent(null);
		
		this.elements.remove(element);
		return this;
	}
	
	public XMLHierarchical removeElement(int position) {
		if (position < 0 || position > this.elements.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.elements.size() - 1));
		
		this.elements.remove(position).setParent(null);
		return this;
	}
	
	public XMLHierarchical removeElements() {
		for (XMLElement element : getElements())
			element.remove();
		
		return this;
	}
}