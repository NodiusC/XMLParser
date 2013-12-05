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

package com.nodinchan.parser.xml;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;

import com.nodinchan.parser.xml.XMLElement.XMLAttribute;

public class XMLParser {
	
	public static String compose(XMLDocument document) {
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
		
		compose(xml, document, 0);
		return xml.toString().trim();
	}
	
	private static void compose(StringBuilder xml, XMLElement parent, int layer) {
		int type = -1;
		
		for (XMLElement element : parent.getElements()) {
			boolean node = element.getElements().size() < 1;
			
			if ((type == 0 && node) || (type == 1 && !node))
				xml.append("\n");
			
			type = (node) ? 1 : 0;
			
			for (int indent = 0; indent < layer; indent++)
				xml.append("    ");
			
			xml.append("<" + element.getName());
			
			for (XMLAttribute attribute : element.getAttributes())
				xml.append(' ' + attribute.getName() + "=\"" + attribute.getValue() + "\"");
			
			xml.append(">" + ((!node) ? "\n" : ""));
			
			if (node)
				xml.append((element.getValue() != null) ? element.getValue() : "");
			else
				compose(xml, element, layer + 1);
			
			if (!node) {
				for (int indent = 0; indent < layer; indent++)
					xml.append("    ");
			}
			
			xml.append("</" + element.getName() + ">\n");
		}
	}
	
	public static XMLDocument parse(InputStream stream) {
		if (stream == null)
			throw new IllegalArgumentException();
		
		XMLDocument document = new XMLDocument();
		
		try {
			XMLEventReader reader = XMLInputFactory.newFactory().createXMLEventReader(stream);
			
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
	
	private static void parse(XMLElement parent, XMLEventReader reader) throws XMLStreamException {
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
				StringBuilder characters = new StringBuilder();
				
				if (next.isCharacters()) {
					while (reader.hasNext()) {
						if (!next.isCharacters())
							break;
						
						characters.append(reader.nextEvent().asCharacters().getData());
						next = reader.peek();
					}
				}
				
				StartElement startElement = event.asStartElement();
				
				XMLElement element = new XMLElement(startElement.getName().getLocalPart());
				
				switch (next.getEventType()) {
				
				case XMLEvent.END_ELEMENT:
					element.setValue(characters.toString());
					break;
					
				case XMLEvent.START_ELEMENT:
					parse(element, reader);
					break;
					
				default:
					element = null;
					break;
				}
				
				if (element == null)
					break;
				
				Iterator<?> attributes = startElement.getAttributes();
				
				while (attributes.hasNext()) {
					Attribute attribute = (Attribute) attributes.next();
					element.appendAttribute(attribute.getName().getLocalPart(), attribute.getValue());
				}
				
				parent.appendElement(element);
				break;
				
			default:
				continue;
			}
			
			previous = event;
		}
	}
}