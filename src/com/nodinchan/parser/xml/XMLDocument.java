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

public final class XMLDocument extends XMLHierarchical {
	
	private final String encoding;
	private final String version;
	private final String systemId;
	
	private final boolean standalone;
	
	public XMLDocument(String encoding, String version, String systemId, boolean standalone) {
		this.encoding = encoding;
		this.version = version;
		this.systemId = systemId;
		this.standalone = standalone;
	}
	
	public XMLDocument(String encoding, String version, String systemId) {
		this(encoding, version, systemId, false);
	}
	
	public XMLDocument(String encoding, String version) {
		this(encoding, version, "", false);
	}
	
	public XMLDocument(String encoding) {
		this(encoding, "1.0", "", false);
	}
	
	public XMLDocument() {
		this("UTF-8", "1.0", "", false);
	}
	
	@Override
	public XMLDocument appendElement(XMLElement element) {
		return insertElement(element, getElementCount());
	}
	
	@Override
	public XMLDocument appendElementAfter(XMLElement element, XMLElement relative) {
		return insertElement(element, getElementIndex(relative) + 1);
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public XMLElement getRoot() {
		if (getElements().size() < 1)
			throw new IllegalStateException("Document must have a root");
		
		return getElements().get(0);
	}
	
	public String getSystemId() {
		return systemId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public XMLDocument insertElement(XMLElement element, int position) {
		return getClass().cast(super.insertElement(element, position));
	}
	
	public boolean isStandalone() {
		return standalone;
	}
	
	@Override
	public XMLDocument prependElement(XMLElement element) {
		return insertElement(element, 0);
	}
	
	@Override
	public XMLDocument prependElementBefore(XMLElement element, XMLElement relative) {
		return insertElement(element, getElementIndex(relative));
	}
	
	@Override
	public XMLDocument removeElement(XMLElement element) {
		return getClass().cast(super.removeElement(element));
	}
	
	@Override
	public XMLDocument removeElements() {
		return getClass().cast(super.removeElements());
	}
	
	public void save(File file) throws IOException {
		if (file == null)
			throw new IllegalArgumentException("File cannot be null");
		
		if (!file.getName().endsWith(".xml"))
			throw new IllegalArgumentException("File extension cannot be other than .xml");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		
		writer.write(XMLParser.compose(this));
		writer.close();
	}
}