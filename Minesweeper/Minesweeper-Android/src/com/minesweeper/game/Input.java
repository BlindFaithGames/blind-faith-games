package com.minesweeper.game;

import com.accgames.XML.XMLKeyboard;

public class Input {
	private static XMLKeyboard keyboard;
	
	public static XMLKeyboard getInstance(){
		if (keyboard == null){
			keyboard = new XMLKeyboard();
			// By default
		}
		return keyboard;
	}
	
}