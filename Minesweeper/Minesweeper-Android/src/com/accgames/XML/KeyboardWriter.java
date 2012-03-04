package com.accgames.XML;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

import android.util.Log;

/**
 * Clase que lleva todo lo referido al guardado del teclado editado en XML
 * 
 * @author Gloria Pozuelo, Gonzalo Benito y Javier Álvarez
 * 
 */

public class KeyboardWriter {
	private Document doc;

	public KeyboardWriter() {
	}

	// ---------------------------------------------------------------------------
	// //
	/* CREACIÓN DEL ÁRBOL DOM */
	// ---------------------------------------------------------------------------
	// //

	public void saveEditedKeyboard(int num, HashMap<Integer, String> keyList,
			FileOutputStream fos) throws ParserConfigurationException {
		// Creo el Documento DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.newDocument();

		// Creo el documento raíz y lo pongo como hijo único del documento
		Element keyboardNode = doc.createElement("keyboard");
		keyboardNode.setAttribute("num", "" + num);
		doc.appendChild(keyboardNode);

		Iterator it = keyList.entrySet().iterator();
		// Para cada fila del teclado
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			Element rowNode = createRowNode(e.getKey(), e.getValue());
			// Lo añado al nodo raíz
			keyboardNode.appendChild(rowNode);
		}
		try {
			saveXML(doc, fos);
		} catch (TransformerException e) {
			Log.d("XMLHELPER", "Exception: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("XMLHELPER", "Exception: " + e);
			e.printStackTrace();
		}
	}

	// MÉTODOS AUXILIARES

	private Element createRowNode(Object key, Object value) {
		// Creo un nodo con sus atributos
		Element keyNode = doc.createElement("rowmap");
		keyNode.setAttribute("action", "" + value);
		keyNode.setAttribute("key", "" + key);

		return keyNode;
	}

	/**
	 * Guarda el teclado editado
	 * 
	 * @param doc
	 * @param fos
	 * @throws TransformerException
	 * @throws TransformerException
	 * @throws IOException
	 */
	private void saveXML(Document doc, FileOutputStream fos) throws TransformerException, IOException {
		// Creación del transformador a partir de una factoría
		TransformerFactory factory = TransformerFactory.newInstance();
		// factory.setAttribute("indent-number", 4);
		Transformer transformer = factory.newTransformer();

		// Creación de un Source a partir del arbol DOM
		DOMSource origen = new DOMSource(doc);

		// Creación de un Result a partir del fichero de destino
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		// Configuración del transformador y ejecución
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"keyboard.dtd");
		transformer.transform(origen, new StreamResult(osw));

		osw.flush();
		osw.close();

	}
	
}
