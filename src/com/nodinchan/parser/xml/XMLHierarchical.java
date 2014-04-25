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

/**
 * 
 * @author NodinChan
 *
 */
public abstract class XMLHierarchical {
	
	private final List<XMLElement> elements;
	
	public XMLHierarchical() {
		this.elements = new LinkedList<XMLElement>();
	}
	
	/**
	 * Appends the {@link XMLElement} to the end
	 * 
	 * @param element The element to append
	 * 
	 * @return The parent of the element appended
	 */
	public XMLHierarchical appendElement(XMLElement element) {
		return insertElement(element, getElementCount());
	}
	
	/**
	 * Appends the {@link XMLElement} after the given XMLElement
	 * 
	 * @param element The element to append
	 * 
	 * @param relative The relative element
	 * 
	 * @return The parent of the element appended
	 */
	public XMLHierarchical appendElementAfter(XMLElement element, XMLElement relative) {
		if (!hasElement(relative))
			throw new IllegalArgumentException("Relative element not found");
		
		return insertElement(element, getElementIndex(relative) + 1);
	}
	
	/**
	 * Returns the first {@link XMLElement} with the given name
	 * 
	 * @param name The element name
	 * 
	 * @return The element if found, otherwise null
	 */
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
	
	/**
	 * Returns the {@link XMLElement} at the given position
	 * 
	 * @param position The position
	 * 
	 * @return The element found
	 */
	public XMLElement getElement(int position) {
		if (position < 0 || position > this.elements.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.elements.size() - 1));
		
		return this.elements.get(position);
	}
	
	/**
	 * Returns the number of {@link XMLElement}s
	 * 
	 * @return The element count
	 */
	public int getElementCount() {
		return this.elements.size();
	}
	
	/**
	 * Returns the index of the given {@link XMLElement}
	 * 
	 * @param element The element to find
	 * 
	 * @return The index if found, otherwise -1
	 */
	public int getElementIndex(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		return this.elements.indexOf(element);
	}
	
	/**
	 * Returns a list of {@link XMLElement}s
	 * 
	 * @return The copy of the list of elements
	 */
	public List<XMLElement> getElements() {
		return new LinkedList<XMLElement>(this.elements);
	}
	
	/**
	 * Returns a list of {@link XMLElement}s with the given name
	 * 
	 * @param name The element name
	 * 
	 * @return The list of elements found
	 */
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
	
	/**
	 * Determines whether the {@link XMLElement} is one of the children
	 * 
	 * @param element The element to determine
	 * 
	 * @return True if found, otherwise false
	 */
	public boolean hasElement(XMLElement element) {
		return this.elements.contains(element);
	}
	
	/**
	 * Determines whether an {@link XMLElement} with the given name is one of the children
	 * 
	 * @param name The element name
	 * 
	 * @return True if found, otherwise false
	 */
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
	
	/**
	 * Indicates whether any {@link XMLElement}s are present
	 * 
	 * @return True if any is present, otherwise false
	 */
	public boolean hasElements() {
		return !this.elements.isEmpty();
	}
	
	/**
	 * Inserts the {@link XMLElement} at the given position
	 * 
	 * @param element The element to insert
	 * 
	 * @param position The position
	 * 
	 * @return The parent of the element inserted
	 */
	public XMLHierarchical insertElement(XMLElement element, int position) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		if (position < 0 || position > this.elements.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.elements.size() - 1));
		
		this.elements.add(position, element);
		
		element.setParent(this);
		return this;
	}
	
	/**
	 * Prepends the {@link XMLElement} to the start
	 * 
	 * @param element The element to prepend
	 * 
	 * @return The parent of the element prepended
	 */
	public XMLHierarchical prependElement(XMLElement element) {
		return insertElement(element, 0);
	}
	
	/**
	 * Prepends the {@link XMLElement} before the given XMLElement
	 * 
	 * @param element The element to prepend
	 * 
	 * @param relative The relative element
	 * 
	 * @return The parent of the element prepended
	 */
	public XMLHierarchical prependElementBefore(XMLElement element, XMLElement relative) {
		if (!hasElement(relative))
			throw new IllegalArgumentException("Relative element not found");
		
		return insertElement(element, getElementIndex(relative));
	}
	
	/**
	 * Removes the {@link XMLElement}
	 * 
	 * @param element The element to remove
	 * 
	 * @return The parent that the element is removed from
	 */
	public XMLHierarchical removeElement(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Element cannot be null");
		
		if (!hasElement(element))
			throw new IllegalArgumentException("No such element");
		
		this.elements.remove(element);
		
		element.setParent(null);
		return this;
	}
	
	/**
	 * Removes the {@link XMLElement} at the given position
	 * 
	 * @param position The position
	 * 
	 * @return The parent that the element is removed from
	 */
	public XMLHierarchical removeElement(int position) {
		if (position < 0 || position > this.elements.size())
			throw new IndexOutOfBoundsException("Position cannot be beyond 0 to " + (this.elements.size() - 1));
		
		this.elements.remove(position).setParent(null);
		return this;
	}
	
	/**
	 * Removes all existing {@link XMLElement}
	 * 
	 * @return The parent that the elements are removed from
	 */
	public XMLHierarchical removeElements() {
		for (XMLElement element : getElements())
			removeElement(element);
		
		return this;
	}
}