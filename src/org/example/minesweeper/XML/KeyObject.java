package org.example.minesweeper.XML;

import java.io.Serializable;

/**
 * 
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 *
 */
public class KeyObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private String action, key;

	public KeyObject(String acc, String key){
		action = acc;
		this.key = key;
	}
	
// GETTERS

	public String getAction(){return action;}
	public String getKey(){return key;}

	
	public boolean equals(Object obj){
		if (obj instanceof KeyObject){
			return ((KeyObject) obj).key.equals(key);
		}
		else
			return false;
	}
}
