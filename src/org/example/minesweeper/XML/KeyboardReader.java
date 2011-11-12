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
	public XMLKeyboard loadEditedKeyboard(String fichero){
		//Archivo a procesar, debe tener una referencia a la DTD
		File XMLfile = new File(fichero);
		SAXHandler saxHandler = new SAXHandler();
		
		//Creamos la factoría y la configuramos con validación
		SAXParserFactory factory = SAXParserFactory.newInstance(); 
		factory.setValidating(true);

		//Creamos el parser y leemos
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(XMLfile, saxHandler);
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ParserConfigurationException e) {
			return null;
		}
		return saxHandler.getXMLKeyboard();
	}
}
