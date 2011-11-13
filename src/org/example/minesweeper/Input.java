package org.example.minesweeper;

import org.example.minesweeper.XML.XMLKeyboard;

public class Input {
	private static XMLKeyboard keyboard;
	
	public static XMLKeyboard getInstance(){
		if (keyboard == null){
			keyboard = new XMLKeyboard();
			// By default
			keyboard.fillXMLKeyboard();
		}
		return keyboard;
	}
	
}