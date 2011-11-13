package org.example.minesweeper.XML;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Element;


/**
 * Clase que representa una Teclado XML.
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 */

public class XMLKeyboard {

	private HashMap<Integer, String> keyList; /* Mapa tecla - acción */
	private HashMap<Integer, String> keyButton; /* Mapa tecla - botón */
	private int num; // Número de acciones definidas para el teclado
	// Para llevar el número de errores que se produjeron
	private int numErrors;


	public XMLKeyboard(){
		keyList = new HashMap<Integer, String>();
		keyButton = new HashMap<Integer, String>();
		/* Botones configurables */
		keyButton.put(24, "Volume Up");
		keyButton.put(25, "Volume Down");
		keyButton.put(82, "Search");
		keyButton.put(84, "Menu");
		numErrors = 0;
		num = 0;
	}
	
	/**
	 * Dafault keyboard config
	 */
	public void fillXMLKeyboard(){
		keyList.put(24, "zoom");
		keyList.put(25, "exploration");
		keyList.put(82, "instructions");
		keyList.put(84, "coordinates");
		num = 4;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
// GETTERS
	public HashMap<Integer, String> getKeyList() {
		return keyList;
	}
	
	public int getNum(){return num;}
	public int getNumErrors() {return numErrors;}

	public String getAction(int i) {
		return keyList.get(i);
	}
	
	public String searchButtonByAction(String s){
		Iterator it = keyButton.entrySet().iterator();
		Map.Entry e = null;
		boolean found = false;
		// Para cada fila del teclado
		while (!found && it.hasNext()) {
			e = (Map.Entry) it.next();
			found = s.equals(e.getValue());
		}
		if (e != null) return keyList.get(e.getValue());
		else return null;
	}
	
// OTHERS

	public void riseNumberOfErrors(int unit){
		numErrors += unit;
	}

	public void addObject(Integer k, String v) {
		keyList.put(k, v);
	}

}
