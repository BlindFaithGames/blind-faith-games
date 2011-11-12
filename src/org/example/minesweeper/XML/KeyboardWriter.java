package org.example.minesweeper.XML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * Clase que lleva todo lo referido al guardado del teclado editado en XML
 * @author Gloria Pozuelo, Gonzalo Benito y Javier Álvarez
 *
 */

public class KeyboardWriter {
	private Document doc;

	public KeyboardWriter(){}

// --------------------------------------------------------------------------- //
/* CREACIÓN DEL ÁRBOL DOM */
// --------------------------------------------------------------------------- //


	public void saveEditedKeyboard(int num, ArrayList<KeyObject> keyList, String fichero)
										throws ParserConfigurationException{
		// Creo el Documento DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.newDocument();
		
		// Creo el documento raíz y lo pongo como hijo único del documento
		Element keyboardNode = doc.createElement("keyboard");
		keyboardNode.setAttribute("num", ""+num);
		doc.appendChild(keyboardNode);
		
		//Para cada fila del teclado
		for (KeyObject o: keyList){
			Element rowNode=createRowNode(o);
			//Lo añado al nodo raíz
			keyboardNode.appendChild(rowNode);			
		}
		try{
			saveXML(doc, fichero); 
		}
		catch(Exception e){}
	}

// MÉTODOS AUXILIARES
	
	private Element createRowNode(KeyObject k){
		//Creo un nodo con sus atributos
		Element keyNode = doc.createElement("rowmap");
		keyNode.setAttribute("action", k.getAction());
		keyNode.setAttribute("key", k.getKey());
		
		return keyNode;
	}
	
	/**
	 * Guarda el teclado editado
	 * @param doc
	 * @param fichero
	 * @throws TransformerException 
	 * @throws TransformerException
	 * @throws IOException 
	 */
	private void saveXML(Document doc, String fichero) throws TransformerException, IOException {
		//Creación del transformador a partir de una factoría
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setAttribute("indent-number", 4);  
		Transformer transformer = factory.newTransformer();
		
		//Creación de un Source a partir del arbol DOM
		DOMSource origen = new DOMSource(doc);
		if (!fichero.endsWith(".xml"))
			fichero = fichero + ".xml";
		
		File destinyFile = new File(fichero);
		//Creación de un Result a partir del fichero de destino
		OutputStreamWriter osw = new OutputStreamWriter( new FileOutputStream(destinyFile), "UTF-8" );
		//Configuración del transformador y ejecución
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "keyboard.dtd");
		transformer.transform(origen, new StreamResult(osw));
		
		osw.flush();
		osw.close();
	}
}
