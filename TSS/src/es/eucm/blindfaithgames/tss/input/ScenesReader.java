package es.eucm.blindfaithgames.tss.input;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import es.eucm.blindfaithgames.tss.game.SceneManager;


/**
 * 
 * Class that validates and reads XML files.
 * 
 * @author Gloria Pozuelo & Javier �lvarez
 *
 */
public class ScenesReader {

	public ScenesReader(){}
	
	/**
	 * Loads a keyboard from XML file.
	 * @param is XML file
	 * @return The new keyboard
	 */
	public SceneManager loadAdventure(InputStream is) {
		try {
			
			/** Handling XML */
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sp = factory.newSAXParser();

			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			SAXHandler saxHandler = new SAXHandler();
			sp.parse(is, saxHandler);
			return saxHandler.getSceneManager();
			
		} catch (Exception e) {
			System.out.println("XML Parsing Exception = " + e);
			return null;
		}		
	}
}
