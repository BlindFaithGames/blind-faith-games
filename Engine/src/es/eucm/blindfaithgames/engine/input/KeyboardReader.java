package es.eucm.blindfaithgames.engine.input;

import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * 
 * Class that validates and reads XML files.
 * 
 * @author Gloria Pozuelo & Javier Álvarez
 *
 */
public class KeyboardReader {

	public KeyboardReader(){}
	
	/**
	 * Loads a keyboard from XML file.
	 * @param fis XML file
	 * @return The new keyboard
	 */
	public XMLKeyboard loadEditedKeyboard(FileInputStream fis) {
		try {
			
			/** Handling XML */
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sp = factory.newSAXParser();

			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			SAXHandler saxHandler = new SAXHandler();
			sp.parse(fis, saxHandler);
			
			return saxHandler.getXMLKeyboard();
			
		} catch (Exception e) {
			System.out.println("XML Parsing Exception = " + e);
			return null;
		}		
	}
}
