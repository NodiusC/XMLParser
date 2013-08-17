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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class XMLStream extends InputStream {
	
	private final ByteArrayInputStream is;
	
	public XMLStream(XMLDocument document) {
		if (document == null)
			throw new IllegalArgumentException();
		
		StringBuilder xml = new StringBuilder();
		
		String encoding = document.getEncoding();
		String version = document.getVersion();
		String standalone = (document.isStandalone()) ? "yes" : "no";
		
		xml.append("<?xml");
		xml.append(" version=\"" + version + "\"");
		xml.append(" encoding=\"" + encoding + "\"");
		xml.append(" standalone=\"" + standalone + "\"");
		xml.append("?>\n");
		
		read(xml, document, 0);
		
		this.is = new ByteArrayInputStream(xml.toString().getBytes());
	}
	
	@Override
	public int read() throws IOException {
		return is.read();
	}
	
	private void read(StringBuilder xml, XMLSection parent, int layer) {
		if (xml == null || parent == null || layer < 0)
			throw new IllegalArgumentException();
		
		for (XMLObject object : parent.getElements()) {
			switch (object.getType()) {
			
			case ELEMENT:
				for (int time = 1; time <= layer; time++)
					xml.append("    ");
				
				xml.append("<" + object.getName());
				
				for (XMLAttribute attribute : object.getAttributes())
					xml.append(" " + attribute.getName() + "=\"" + attribute.getValue() + "\"");
				
				xml.append(">");
				xml.append(((XMLElement) object).getValue());
				xml.append("</" + object.getName() + ">");
				break;
				
			case SECTION:
				for (int time = 1; time <= layer; time++)
					xml.append("    ");
				
				xml.append("<" + object.getName());
				
				for (XMLAttribute attribute : object.getAttributes())
					xml.append(" " + attribute.getName() + "=\"" + attribute.getValue() + "\"");
				
				xml.append(">");
				
				read(xml, (XMLSection) object, layer + 1);
				
				xml.append("</" + object.getName() + ">");
				break;
				
			default:
				continue;
			}
			
			xml.append('\n');
		}
	}
}