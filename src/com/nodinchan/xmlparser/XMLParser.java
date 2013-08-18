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

import com.nodinchan.xmlparser.XMLObject.XMLType;

public final class XMLParser {
	
	public static String convert(XMLDocument document) {
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
		
		convert(xml, document, 0);
		
		return xml.toString();
	}
	
	private static void convert(StringBuilder xml, XMLSection parent, int layer) {
		if (xml == null || parent == null || layer < 0)
			throw new IllegalArgumentException();
		
		XMLType previous = XMLType.UNKNOWN;
		
		for (XMLObject object : parent.getElements()) {
			switch (object.getType()) {
			
			case ELEMENT:
				if (previous.equals(XMLType.SECTION))
					xml.append('\n');
				
				for (int time = 1; time <= layer; time++)
					xml.append("    ");
				
				xml.append("<" + object.getName());
				
				for (XMLAttribute attribute : object.getAttributes())
					xml.append(" " + attribute.getName() + "=\"" + attribute.getValue() + "\"");
				
				xml.append(">");
				xml.append(((XMLElement) object).getValue());
				xml.append("</" + object.getName() + ">\n");
				break;
				
			case SECTION:
				if (previous.equals(XMLType.ELEMENT))
					xml.append('\n');
				
				for (int time = 1; time <= layer; time++)
					xml.append("    ");
				
				xml.append("<" + object.getName());
				
				for (XMLAttribute attribute : object.getAttributes())
					xml.append(" " + attribute.getName() + "=\"" + attribute.getValue() + "\"");
				
				xml.append(">");
				
				convert(xml, (XMLSection) object, layer + 1);
				
				xml.append("</" + object.getName() + ">\n");
				break;
				
			default:
				continue;
			}
			
			previous = object.getType();
		}
	}
	
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
			reader.close();
			
		} catch (Exception e) {}
		
		return document;
	}
	
	private static void parse(XMLSection parent, XMLEventReader reader) throws XMLStreamException {
		XMLEvent previous = null;
		XMLEvent event = null;
		XMLEvent next = null;
		
		while (reader.hasNext()) {
			event = reader.nextEvent();
			next = reader.peek();
			
			switch (event.getEventType()) {
			
			case XMLEvent.END_ELEMENT:
				if (previous != null && previous.isEndElement())
					return;
				
				break;
				
			case XMLEvent.START_ELEMENT:
				XMLObject object = null;
				
				StartElement startElement = event.asStartElement();
				StringBuilder characters = new StringBuilder();
				
				if (next.isCharacters()) {
					while (reader.hasNext()) {
						if (!next.isCharacters())
							break;
						
						characters.append(reader.nextEvent().asCharacters().getData());
						next = reader.peek();
					}
				}
				
				switch (next.getEventType()) {
				
				case XMLEvent.END_ELEMENT:
					XMLElement element = new XMLElement(startElement.getName().getLocalPart());
					element.setValue(characters.toString());
					
					object = element;
					break;
					
				case XMLEvent.START_ELEMENT:
					XMLSection section = new XMLSection(startElement.getName().getLocalPart());
					parse(section, reader);
					
					object = section;
					break;
				}
				
				if (object == null)
					break;
				
				Iterator<?> attributes = startElement.getAttributes();
				
				while (attributes.hasNext()) {
					Attribute attribute = (Attribute) attributes.next();
					
					String name = attribute.getName().getLocalPart();
					String value = attribute.getValue();
					
					object.addAttribute(new XMLAttribute(name, value));
				}
				
				parent.addElement(object);
				break;
				
			default:
				continue;
			}
			
			previous = event;
		}
	}
}