package org.example.minesweeper.XML;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


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

// SETTERS
	
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
	
	
// OTHERS

	public void riseNumberOfErrors(int unit){
		numErrors += unit;
	}

	public void addObject(Integer k, String v) {
		keyList.put(k, v);
	}
	
	public String searchButtonByAction(String action){
		Iterator it = keyList.entrySet().iterator();
		Map.Entry e = null;
		String s1 = "";
		boolean found = false;
		// Para cada fila del teclado
		while (!found && it.hasNext()) {
			e = (Map.Entry) it.next();
			found = action.equals(e.getValue());
		}
		if (e != null)
			s1 = keyButton.get(e.getKey());
		return s1;
	}
	
	public Integer getKeyByAction(String action){
		Iterator it = keyList.entrySet().iterator();
		Map.Entry e = null;
		String s1 = "";
		boolean found = false;
		// Para cada fila del teclado
		while (!found && it.hasNext()) {
			e = (Map.Entry) it.next();
			found = action.equals(e.getValue());
		}
		if (e != null) return (Integer) e.getKey();
		else return null;
	}
	
	/**
	 * Check if a button given by parameter is available
	 * @param key
	 * @return
	 */
	public void addButtonAction(int key, String action, String button){
		// Si no tenemos info de la tecla
		if (keyList.get(key) == null){
			// añadimos el nombre de la tecla
			keyButton.put(key, button);
		}
		// Eliminar tecla asignada a esa acción
		Integer k = getKeyByAction(action);
		if (k != null){
			keyList.remove(k);
		}
		keyList.put(key, action);
	}

}
