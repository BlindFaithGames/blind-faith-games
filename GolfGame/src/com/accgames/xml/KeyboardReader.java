package com.accgames.xml;

import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Clase encargada de validar y leer el archivo XML
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 *
 */
public class KeyboardReader {

	public KeyboardReader(){}
	
	/**
	 * Carga un teclado desde un ficheor XML.
	 * @param fis Fichero XML.
	 * @return El teclado creado.
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
			System.out.println("XML Pasing Excpetion = " + e);
			return null;
		}		
	}
}
