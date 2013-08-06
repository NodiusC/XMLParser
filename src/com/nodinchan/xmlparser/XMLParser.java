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

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public final class XMLParser {
	
	public static XMLDocument parse(InputStream inputStream) {
		XMLDocument document = new XMLDocument();
		
		try {
			XMLEventReader reader = XMLInputFactory.newFactory().createXMLEventReader(inputStream);
			
			if (!reader.hasNext())
				return document;
			
			XMLEvent firstEvent = reader.peek();
			
			if (firstEvent.isStartDocument()) {
				StartDocument startDoc = (StartDocument) firstEvent;
				
				String encoding = startDoc.getCharacterEncodingScheme();
				String version = startDoc.getVersion();
				String systemId = startDoc.getSystemId();
				boolean standalone = startDoc.isStandalone();
				
				document = new XMLDocument(encoding, version, systemId, standalone);
				reader.next();
			}
			
			parse(document, reader);
			
		} catch (Exception e) {}
		
		return document;
	}
	
	private static void parse(XMLSection parent, XMLEventReader reader) throws XMLStreamException {
		while (reader.hasNext()) {
			XMLEvent peek = reader.peek();
			
			if (peek.isEndDocument())
				return;
			
			XMLEvent event = reader.nextEvent();
			XMLEvent next = reader.peek();
			
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				
				XMLObject object = null;
				
				if (next.isCharacters()) {
					StringBuilder characters = new StringBuilder();
					
					while (reader.hasNext()) {
						XMLEvent afterCharacters = reader.peek();
						
						if (!afterCharacters.isCharacters())
							break;
						
						characters.append(reader.nextEvent().asCharacters().getData());
					}
					
					next = reader.peek();
					
					if (next.isEndElement()) {
						XMLElement element = new XMLElement(startElement.getName().getLocalPart());
						object = element;
						
						element.setValue(characters.toString());
						parent.addElement(element);
					}
				}
				
				if (next.isStartElement()) {
					XMLSection section = new XMLSection(startElement.getName().getLocalPart());
					object = section;
					
					parse(section, reader);
					parent.addElement(section);
				}
				
				if (object == null)
					continue;
				
				Iterator<?> attributes = startElement.getAttributes();
				
				while (attributes.hasNext()) {
					Attribute attribute = (Attribute) attributes.next();
					
					String name = attribute.getName().getLocalPart();
					String value = attribute.getValue();
					
					object.addAttribute(new XMLAttribute(name, value));
				}
			}
		}
	}
}