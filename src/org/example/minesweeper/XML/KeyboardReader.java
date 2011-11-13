package org.example.minesweeper.XML;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * Clase encargada de validar y leer el archivo XML
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 *
 */
public class KeyboardReader {

	public KeyboardReader(){}
	
	/**
	 * Carga un teclado desde un ficheor XML.
	 * @param fichero Fichero XML.
	 * @return El teclado creado.
	 */
	public XMLKeyboard loadEditedKeyboard(String fichero) {
		try {
			
			/** Handling XML */
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sp = factory.newSAXParser();

			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			SAXHandler saxHandler = new SAXHandler();
			sp.parse(new File(fichero), saxHandler);
			
			return saxHandler.getXMLKeyboard();
			
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
			return null;
		}		
	}
}
