package org.example.minesweeper.XML;

import java.util.ArrayList;


/**
 * Clase que representa una Teclado XML.
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez
 */

public class XMLKeyboard {
	private ArrayList<KeyObject> keyList;
	private int num; // N�mero de acciones definidas para el teclado
	// Para llevar el n�mero de errores que se produjeron
	private int numErrors;


	public XMLKeyboard(){
		keyList = new ArrayList<KeyObject>();
		numErrors = 0;
	}
	
// GETTERS

	public ArrayList<KeyObject> getKeyList() {
		return keyList;
	}
	
	public int getNum(){return num;}
	public int getNumErrors() {return numErrors;}
	public String get(int i){
		return keyList.get(i).getKey();
	}
	
// OTHERS
	
	/**
	 * A�ade un objeto a la lista de objetos.
	 * @param o Objeto a a�adir.
	 */
	public void addObject(KeyObject o){
		keyList.add(o);
	}

	public void riseNumberOfErrors(int unit){
		numErrors += unit;
	}

	public String getAction(int i) {
		return keyList.get(i).getAction();
	}
}
