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
	
	private static void parse(XMLDocument document, XMLEventReader reader) throws XMLStreamException {
		while (reader.hasNext()) {
			XMLEvent peek = reader.peek();
			
			if (peek.isEndDocument())
				return;
			
			XMLEvent event = reader.nextEvent();
			XMLEvent next = reader.peek();
			
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				
				Iterator<?> attributes = startElement.getAttributes();
				
				if (next.isCharacters()) {
					String characters = parseCharacters(reader);
					next = reader.peek();
					
					if (next.isEndElement()) {
						XMLElement element = new XMLElement(startElement.getName().getLocalPart());
						
						while (attributes.hasNext()) {
							Attribute attribute = (Attribute) attributes.next();
							
							String name = attribute.getName().getLocalPart();
							String value = attribute.getValue();
							
							element.addAttribute(new XMLAttribute(name, value));
						}
						
						element.setValue(characters);
						document.addElement(element);
					}
				}
				
				if (next.isStartElement()) {
					XMLItem item = new XMLItem(startElement.getName().getLocalPart());
					
					while (attributes.hasNext()) {
						Attribute attribute = (Attribute) attributes.next();
						
						String name = attribute.getName().getLocalPart();
						String value = attribute.getValue();
						
						item.addAttribute(new XMLAttribute(name, value));
					}
					
					parseItem(item, reader);
					document.addElement(item);
				}
			}
		}
	}
	
	private static String parseCharacters(XMLEventReader reader) throws XMLStreamException {
		StringBuilder characters = new StringBuilder();
		
		while (reader.hasNext()) {
			XMLEvent peek = reader.peek();
			
			if (!peek.isCharacters())
				break;
			
			characters.append(reader.nextEvent().asCharacters().getData());
		}
		
		return characters.toString();
	}
	
	private static void parseItem(XMLItem parent, XMLEventReader reader) throws XMLStreamException {
		while (reader.hasNext()) {
			XMLEvent peek = reader.peek();
			
			if (peek.isEndDocument())
				return;
			
			XMLEvent event = reader.nextEvent();
			XMLEvent next = reader.peek();
			
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				
				Iterator<?> attributes = startElement.getAttributes();
				
				if (next.isCharacters()) {
					String characters = parseCharacters(reader);
					next = reader.peek();
					
					if (next.isEndElement()) {
						XMLElement element = new XMLElement(startElement.getName().getLocalPart());
						
						while (attributes.hasNext()) {
							Attribute attribute = (Attribute) attributes.next();
							
							String name = attribute.getName().getLocalPart();
							String value = attribute.getValue();
							
							element.addAttribute(new XMLAttribute(name, value));
						}
						
						element.setValue(characters);
						parent.addElement(element);
					}
				}
				
				if (next.isStartElement()) {
					XMLItem item = new XMLItem(startElement.getName().getLocalPart());
					
					while (attributes.hasNext()) {
						Attribute attribute = (Attribute) attributes.next();
						
						String name = attribute.getName().getLocalPart();
						String value = attribute.getValue();
						
						item.addAttribute(new XMLAttribute(name, value));
					}
					
					parseItem(item, reader);
					parent.addElement(item);
				}
			}
		}
	}
}