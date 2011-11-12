package org.example.minesweeper.XML;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Clase para manejar los eventos enviados por el parser SAX.
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 */
public class SAXHandler extends DefaultHandler {
	private XMLKeyboard k;
	private int counter, num;
	private String action, key;

	
	public XMLKeyboard getXMLKeyboard() {
		return k;
	}
	
// --------------------------------------------------------------------- //
/* DEFINE METHODS OF DefaultHandler */
// --------------------------------------------------------------------- //
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}
	
	public void startDocument(){	
		counter = 0;
	}	
	
	public void startElement(String uri, String localName, String qName, Attributes att){
		if (qName.equals("keyboard")){
			num = Integer.parseInt(att.getValue("num"));
			k = new XMLKeyboard();
		}
		else if (qName.equals("rowmap")){
			action = att.getValue("action");
		}
		else if (qName.equals("key")){
			key = att.getValue("key");
		}		
	}

	public void endElement(String uri, String localName, String qName){
		if (qName.equals("rowmap")){
			k.addObject(new KeyObject(action, key));
			counter++;
		}		
		else if (qName.equals("keyboard")){
			if (counter != num)
				k.riseNumberOfErrors(1);
		}
	}
}

