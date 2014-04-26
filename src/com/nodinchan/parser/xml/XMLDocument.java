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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents an XML document
 * 
 * @author NodinChan
 *
 */
public final class XMLDocument extends XMLHierarchical {
	
	private final String encoding;
	private final String version;
	private final String systemId;
	
	private final boolean standalone;
	
	/**
	 * Constructs an {@link XMLDocument} with the given encoding, version, system ID, and standalone value
	 * 
	 * @param encoding The encoding used for the XML document
	 * 
	 * @param version The version of the XML standard that the XML document conforms to
	 *                
	 * @param systemId The system ID of the XML document
	 * 
	 * @param standalone If the XML document has an internal DTD or is linked to an external DTD,
	 *                   or any external entity references
	 */
	public XMLDocument(String encoding, String version, String systemId, boolean standalone) {
		this.encoding = encoding;
		this.version = version;
		this.systemId = systemId;
		this.standalone = standalone;
	}
	
	/**
	 * Constructs an {@link XMLDocument} with the given encoding, version, and system ID
	 * 
	 * @param encoding The encoding used for the XML document
	 * 
	 * @param version The version of the XML standard that the XML document conforms to
	 *                
	 * @param systemId The system ID of the XML document
	 */
	public XMLDocument(String encoding, String version, String systemId) {
		this(encoding, version, systemId, false);
	}
	
	/**
	 * Constructs an {@link XMLDocument} with the given encoding and version
	 * 
	 * @param encoding The encoding used for the XML document
	 * 
	 * @param version The version of the XML standard that the XML document conforms to
	 */
	public XMLDocument(String encoding, String version) {
		this(encoding, version, "", false);
	}
	
	/**
	 * Constructs an {@link XMLDocument} with the given encoding
	 * 
	 * @param encoding The encoding used for the XML document
	 */
	public XMLDocument(String encoding) {
		this(encoding, "1.0", "", false);
	}
	
	/**
	 * Constructs an {@link XMLDocument}
	 */
	public XMLDocument() {
		this("UTF-8", "1.0", "", false);
	}
	
	/**
	 * Sets the {@link XMLElement} as the root of the {@link XMLDocument}
	 * 
	 * @param element The root element
	 * 
	 * @return The document
	 */
	@Override
	public XMLDocument appendElement(XMLElement element) {
		return setRoot(element);
	}
	
	/**
	 * Sets the {@link XMLElement} as the root of the {@link XMLDocument}
	 * 
	 * @param element The root element
	 * 
	 * @param relative Unused
	 * 
	 * @return The document
	 */
	@Override
	public XMLDocument appendElementAfter(XMLElement element, XMLElement relative) {
		return setRoot(element);
	}
	
	/**
	 * Returns the encoding used for the XML document
	 * 
	 * @return The encoding
	 */
	public String getEncoding() {
		return encoding;
	}
	
	/**
	 * Returns the root {@link XMLElement} of of the XML document
	 * 
	 * @return The root element
	 */
	public XMLElement getRoot() {
		if (getElementCount() < 1)
			throw new IllegalStateException("Document must have a root");
		
		return getElements().get(0);
	}
	
	/**
	 * Returns the system ID of the XML document
	 * 
	 * @return The system ID
	 */
	public String getSystemId() {
		return systemId;
	}
	
	/**
	 * Returns the version of the XML standard that the XML document conforms to
	 * 
	 * @return The XML standard version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the {@link XMLElement} as the root of the {@link XMLDocument}
	 * 
	 * @param element The root element
	 * 
	 * @param position Unused
	 * 
	 * @return The document
	 */
	@Override
	public XMLDocument insertElement(XMLElement element, int position) {
		return setRoot(element);
	}
	
	/**
	 * Returns whether the XML document has an internal DTD or is linked to an external DTD,
	 * or any external entity references
	 * 
	 * @return True if the XML document has an internal DTD, otherwise false
	 */
	public boolean isStandalone() {
		return standalone;
	}
	
	/**
	 * Sets the {@link XMLElement} as the root of the {@link XMLDocument}
	 * 
	 * @param element The root element
	 * 
	 * @return The document
	 */
	@Override
	public XMLDocument prependElement(XMLElement element) {
		return setRoot(element);
	}
	
	/**
	 * Sets the {@link XMLElement} as the root of the {@link XMLDocument}
	 * 
	 * @param element The root element
	 * 
	 * @param relative Unused
	 * 
	 * @return The document
	 */
	@Override
	public XMLDocument prependElementBefore(XMLElement element, XMLElement relative) {
		return setRoot(element);
	}
	
	@Override
	public XMLDocument removeElement(XMLElement element) {
		super.removeElement(element);
		return this;
	}
	
	@Override
	public XMLDocument removeElement(int position) {
		super.removeElement(position);
		return this;
	}
	
	@Override
	public XMLDocument removeElements() {
		super.removeElements();
		return this;
	}
	
	/**
	 * Saves the {@link XMLDocument} to the given XML file
	 * 
	 * @param file The XML file to save to
	 * 
	 * @throws IOException
	 */
	public void save(File file) throws IOException {
		if (file == null)
			throw new IllegalArgumentException("File cannot be null");
		
		if (!file.getName().endsWith(".xml"))
			throw new IllegalArgumentException("File extension cannot be other than .xml");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		
		writer.write(XMLParser.compose(this));
		writer.close();
	}
	
	/**
	 * Sets the {@link XMLElement} as the root of the {@link XMLDocument}
	 * 
	 * @param element The root element
	 * 
	 * @return The document
	 */
	public XMLDocument setRoot(XMLElement element) {
		if (element == null)
			throw new IllegalArgumentException("Root cannot be null");
		
		removeElements();
		
		super.insertElement(element, 0);
		return this;
	}
}